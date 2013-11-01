/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package name.xen0n.cytosol.ui.widget;

import java.text.MessageFormat;

import name.xen0n.cytosol.R;
import name.xen0n.cytosol.util.ViewIdHelper;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;


public class GuidedEditText extends RelativeLayout {
    private static final String TAG = "GuidedEditText";

    // prevent NPE due to overriding of setBackground
    private boolean viewInitCompleted;

    private EditText editText;
    private TextView guideMessage;

    public static final int DEFAULT_LAYOUT_RES_ID = R.layout.cy__guidededittext;

    // private Context _ctx;

    public GuidedEditText(Context context) {
        super(context);
    }

    public GuidedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttrs(context, attrs);
    }

    public GuidedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initAttrs(context, attrs);
    }

    protected void initLayout(
            Context ctx,
            int resId,
            int editTextViewId,
            int guideTextViewId) {
        Log.v(
                TAG,
                "GuidedEditText layout ID = " + Integer.toHexString(resId));
        Log.v(
                TAG,
                "editText view ID = " + Integer.toHexString(editTextViewId));
        Log.v(
                TAG,
                "guideMessage view ID = "
                        + Integer.toHexString(guideTextViewId));

        View
            .inflate(ctx, (resId == 0 ? DEFAULT_LAYOUT_RES_ID : resId), this);
        editText = (EditText) findViewById(editTextViewId == 0
                ? R.id.cy__guidedEditText
                : editTextViewId);
        guideMessage = (TextView) findViewById(guideTextViewId == 0
                ? R.id.cy__guidedGuideMessage
                : guideTextViewId);

        // Important: set unique IDs for the inflated layouts, so that
        // state persistence works correctly!
        editText.setId(ViewIdHelper.generateViewId());
        guideMessage.setId(ViewIdHelper.generateViewId());

        // consider framework-level init "completed"
        viewInitCompleted = true;
    }

    protected void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.GuidedEditText,
                0,
                0);
        try {
            // load layout
            int layoutResId = a.getResourceId(
                    R.styleable.GuidedEditText_itemLayout,
                    0);
            if (layoutResId == 0) {
                // use library-provided layout
                Log.d(TAG, "Using library provided GuidedEditText layout.");

                initLayout(context, 0, 0, 0);
            } else {
                // library consumer-defined layout
                Log.d(TAG, "Using app provided GuidedEditText layout.");

                int editTextViewId = a.getResourceId(
                        R.styleable.GuidedEditText_editTextViewId,
                        0);
                int guideTextViewId = a.getResourceId(
                        R.styleable.GuidedEditText_guideTextViewId,
                        0);

                initLayout(
                        context,
                        layoutResId,
                        editTextViewId,
                        guideTextViewId);
            }

            // load properties
            int inputTypeProp = a.getInt(
                    R.styleable.GuidedEditText_android_inputType,
                    1);
            doSetInputType(inputTypeProp);

            String textProp = a
                .getString(R.styleable.GuidedEditText_android_text);
            if (textProp != null) {
                doSetText(textProp);
            }

            String hintProp = a
                .getString(R.styleable.GuidedEditText_android_hint);
            if (hintProp != null) {
                doSetHint(hintProp);
            }

            Drawable backgroundProp = a
                .getDrawable(R.styleable.GuidedEditText_android_background);
            if (backgroundProp != null) {
                doSetBackground(backgroundProp);
            }

            ColorStateList colorProp = a
                .getColorStateList(R.styleable.GuidedEditText_android_textColor);
            if (colorProp != null) {
                doSetTextColor(colorProp);
            }

            ColorStateList colorHintProp = a
                .getColorStateList(R.styleable.GuidedEditText_android_textColorHint);
            if (colorHintProp != null) {
                doSetHintTextColor(colorHintProp);
            }

            float textSizeProp = a.getDimension(
                    R.styleable.GuidedEditText_android_textSize,
                    -1.0f);
            if (textSizeProp > 0) {
                doSetTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeProp);
            }

            Boolean isGuideVisibleProp = a.getBoolean(
                    R.styleable.GuidedEditText_isGuideVisible,
                    true);
            doSetGuideVisible(isGuideVisibleProp);

            String guideTextProp = a
                .getString(R.styleable.GuidedEditText_guideText);
            if (guideTextProp != null) {
                doSetGuideText(guideTextProp);
            }

            ColorStateList guideTextColorProp = a
                .getColorStateList(R.styleable.GuidedEditText_guideTextColor);
            if (guideTextColorProp != null) {
                doSetGuideTextColor(guideTextColorProp);
            }

            // Padding calculation...
            // Here's code mimicking the AOSP behavior shown in
            // the Android framework code
            // (frameworks/base/core/java/android/view/View.java).
            //
            // TODO: RTL layout support for API level 17-

            // temp variables
            int pT = -1, pB = -1, pL = -1, pR = -1; // , pS = -1, pE = -1;
            // int pL_actual = -1, pR_actual = -1;

            // first let's reset the view default
            super.setPadding(0, 0, 0, 0);

            // padding prop
            int paddingProp = a.getDimensionPixelSize(
                    R.styleable.GuidedEditText_android_padding,
                    -1);
            if (paddingProp >= 0) {
                pT = pB = pL = pR = paddingProp;
            } else {
                // padding{Top,Bottom} props
                pT = a.getDimensionPixelSize(
                        R.styleable.GuidedEditText_android_paddingTop,
                        -1);
                pB = a.getDimensionPixelSize(
                        R.styleable.GuidedEditText_android_paddingBottom,
                        -1);

                /*
                 * pS = a.getDimensionPixelSize(
                 * R.styleable.GuidedEditText_android_paddingStart, -1);
                 * 
                 * pE = a.getDimensionPixelSize(
                 * R.styleable.GuidedEditText_android_paddingEnd, -1);
                 */
            }

            // padding{Left,Right} props
            pL = a.getDimensionPixelSize(
                    R.styleable.GuidedEditText_android_paddingLeft,
                    pL);
            pR = a.getDimensionPixelSize(
                    R.styleable.GuidedEditText_android_paddingRight,
                    pR);

            Log.v(TAG, MessageFormat.format(
                    "pT, pB, pL, pR = {0}, {1}, {2}, {3}",
                    pT,
                    pB,
                    pL,
                    pR));

            this.doSetPadding(
                    (pL >= 0 ? pL : 0),
                    (pT >= 0 ? pT : 0),
                    (pR >= 0 ? pR : 0),
                    (pB >= 0 ? pB : 0));
        } finally {
            a.recycle();
        }
    }

    // Properties.

    // inputType
    public int getInputType() {
        return editText.getInputType();
    }

    protected void doSetInputType(int type) {
        // if (editText != null)
        editText.setInputType(type);
    }

    public void setInputType(int type) {
        doSetInputType(type);
        invalidate();
        requestLayout();
    }

    // text
    public Editable getText() {
        return editText.getText();
    }

    protected void doSetText(CharSequence text) {
        // if (editText != null)
        editText.setText(text);
    }

    protected void doSetText(int resId) {
        // if (editText != null)
        editText.setText(resId);
    }

    public void setText(int resId) {
        doSetText(resId);
        invalidate();
        requestLayout();
    }

    public void setText(CharSequence text) {
        doSetText(text);
        invalidate();
        requestLayout();
    }

    // hint
    public CharSequence getHint() {
        return editText.getHint();
    }

    protected void doSetHint(CharSequence hint) {
        // if (editText != null)
        editText.setHint(hint);
    }

    public void setHint(CharSequence hint) {
        doSetHint(hint);
        invalidate();
        requestLayout();
    }

    // background
    public Drawable getBackground() {
        return viewInitCompleted ? editText.getBackground() : super
            .getBackground();
    }

    @SuppressWarnings("deprecation")
    protected void doSetBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(background);
        } else {
            // use old API
            // TODO: check compatibility issues like potential drawable
            // paddings
            editText.setBackgroundDrawable(background);
        }
    }

    public void setBackground(Drawable background) {
        if (viewInitCompleted) {
            doSetBackground(background);
            invalidate();
            requestLayout();
        } else {
            // ignore this, or we'll have duplicate backgrounds
        }
    }

    // textColor
    public ColorStateList getTextColors() {
        return editText.getTextColors();
    }

    protected void doSetTextColor(ColorStateList colors) {
        // if (editText != null)
        editText.setTextColor(colors);
    }

    protected void doSetTextColor(int color) {
        // if (editText != null)
        editText.setTextColor(color);
    }

    public void setTextColor(ColorStateList colors) {
        doSetTextColor(colors);
        invalidate();
        requestLayout();
    }

    public void setTextColor(int color) {
        doSetTextColor(color);
        invalidate();
        requestLayout();
    }

    // textColorHint
    public ColorStateList getHintTextColors() {
        return editText.getHintTextColors();
    }

    protected void doSetHintTextColor(ColorStateList colors) {
        // if (editText != null)
        editText.setHintTextColor(colors);
    }

    protected void doSetHintTextColor(int color) {
        // if (editText != null)
        editText.setHintTextColor(color);
    }

    public void setHintTextColor(ColorStateList colors) {
        doSetHintTextColor(colors);
        invalidate();
        requestLayout();
    }

    public void setHintTextColor(int color) {
        doSetHintTextColor(color);
        invalidate();
        requestLayout();
    }

    public float getTextSize() {
        return editText.getTextSize();
    }

    protected void doSetTextSize(float size) {
        // if (editText != null)
        editText.setTextSize(size);
    }

    protected void doSetTextSize(int unit, float size) {
        // if (editText != null)
        editText.setTextSize(unit, size);
    }

    public void setTextSize(float size) {
        doSetTextSize(size);
        invalidate();
        requestLayout();
    }

    public void setTextSize(int unit, float size) {
        doSetTextSize(unit, size);
        invalidate();
        requestLayout();
    }

    // padding
    public int getPaddingTop() {
        return editText.getPaddingTop();
    }

    public int getPaddingBottom() {
        return editText.getPaddingBottom();
    }

    public int getPaddingLeft() {
        return editText.getPaddingLeft();
    }

    public int getPaddingRight() {
        return editText.getPaddingRight();
    }

    /*
     * public int getPaddingStart() { return editText.getPaddingStart(); }
     * 
     * public int getPaddingEnd() { return editText.getPaddingEnd(); }
     */

    protected void doSetPadding(int left, int top, int right, int bottom) {
        Log.v(TAG, MessageFormat.format(
                "setPadding({0}, {1}, {2}, {3})",
                left,
                top,
                right,
                bottom));
        editText.setPadding(left, top, right, bottom);

        // sync guideMessage's marginRight
        LayoutParams params = (RelativeLayout.LayoutParams) guideMessage
            .getLayoutParams();
        params.rightMargin = right;
        guideMessage.setLayoutParams(params);
    }

    public void setPadding(int left, int top, int right, int bottom) {
        if (viewInitCompleted) {
            doSetPadding(left, top, right, bottom);
            invalidate();
            requestLayout();
        } else {
            // ignore this
        }
    }

    // TODO: relative padding
    /*
     * protected void doSetPaddingRelative( int start, int top, int end, int
     * bottom) { editText.setPaddingRelative(start, top, end, bottom);
     * guideMessage.setPaddingRelative(start, 0, end, 0); }
     * 
     * public void setPaddingRelative(int start, int top, int end, int
     * bottom) { if (viewInitCompleted) { doSetPadding(start, top, end,
     * bottom); invalidate(); requestLayout(); } else { // ignore this } }
     */

    // guideText
    public CharSequence getGuideText() {
        return guideMessage.getText();
    }

    protected void doSetGuideText(CharSequence text) {
        // if (guideMessage != null)
        guideMessage.setText(text);
    }

    protected void doSetGuideText(int resId) {
        // if (guideMessage != null)
        guideMessage.setText(resId);
    }

    public void setGuideText(CharSequence text) {
        doSetGuideText(text);
        invalidate();
        requestLayout();
    }

    public void setGuideText(int resId) {
        doSetGuideText(resId);
        invalidate();
        requestLayout();
    }

    // guideTextColor
    public ColorStateList getGuideTextColors() {
        return guideMessage.getTextColors();
    }

    protected void doSetGuideTextColor(ColorStateList colors) {
        // if (guideMessage != null)
        guideMessage.setTextColor(colors);
    }

    protected void doSetGuideTextColor(int color) {
        // if (guideMessage != null)
        guideMessage.setTextColor(color);
    }

    public void setGuideTextColor(ColorStateList colors) {
        doSetGuideTextColor(colors);
        invalidate();
        requestLayout();
    }

    public void setGuideTextColor(int color) {
        doSetGuideTextColor(color);
        invalidate();
        requestLayout();
    }

    // isGuideVisible
    public boolean isGuideVisible() {
        return guideMessage.getVisibility() == View.VISIBLE;
    }

    protected void doSetGuideVisible(boolean visible) {
        // if (guideMessage != null)
        guideMessage.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setGuideVisible(boolean visible) {
        doSetGuideVisible(visible);
        invalidate();
        requestLayout();
    }

    // Events.

    // TextChangedListener
    public void addTextChangedListener(TextWatcher watcher) {
        editText.addTextChangedListener(watcher);
    }

    // OnClickListener
    public void setOnClickListener(OnClickListener l) {
        editText.setOnClickListener(l);
    }

    // OnFocusChangeListener
    public OnFocusChangeListener getOnFocusChangeListener() {
        return editText.getOnFocusChangeListener();
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        editText.setOnFocusChangeListener(l);
    }

    // OnEditorActionListener
    public void setOnEditorActionListener(OnEditorActionListener l) {
        editText.setOnEditorActionListener(l);
    }

    /*
     * state persistence management
     */
    public static class SavedState extends BaseSavedState {
        private final CharSequence text;
        private final CharSequence guideText;
        private final int guideVisibility;

        private final int pT;
        private final int pB;
        private final int pL;
        private final int pR;

        public SavedState(
                Parcelable superState,
                CharSequence text,
                CharSequence guideText,
                int guideVisibility,
                int paddingTop,
                int paddingBottom,
                int paddingLeft,
                int paddingRight) {
            super(superState);

            this.text = text;
            this.guideText = guideText;
            this.guideVisibility = guideVisibility;

            pT = paddingTop;
            pB = paddingBottom;
            pL = paddingLeft;
            pR = paddingRight;
        }

        private SavedState(Parcel in) {
            super(in);

            text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            guideText = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            guideVisibility = in.readInt();

            pT = in.readInt();
            pB = in.readInt();
            pL = in.readInt();
            pR = in.readInt();
        }

        public CharSequence getText() {
            return text;
        }

        public CharSequence getGuideText() {
            return guideText;
        }

        public int getGuideVisibility() {
            return guideVisibility;
        }

        public void setPaddings(GuidedEditText text) {
            text.doSetPadding(pL, pT, pR, pB);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            TextUtils.writeToParcel(text, out, flags);
            TextUtils.writeToParcel(guideText, out, flags);
            out.writeInt(guideVisibility);

            out.writeInt(pT);
            out.writeInt(pB);
            out.writeInt(pL);
            out.writeInt(pR);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Log.v(TAG, "onSaveInstanceState: " + editText.getText().toString());

        Parcelable superState = super.onSaveInstanceState();

        // is persistence of fancy CharSequence things needed?
        SavedState ss = new SavedState(
                superState,
                editText.getText(),
                guideMessage.getText(),
                guideMessage.getVisibility(),
                editText.getPaddingTop(),
                editText.getPaddingBottom(),
                editText.getPaddingLeft(),
                editText.getPaddingRight());
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Log.v(TAG, "onRestoreInstanceState: " + ss.getText());
        doSetText(ss.getText());
        guideMessage.setVisibility(ss.getGuideVisibility());
        doSetGuideText(ss.getGuideText());
        ss.setPaddings(this);
    }
}
