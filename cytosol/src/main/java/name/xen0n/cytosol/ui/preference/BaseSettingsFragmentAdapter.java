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
package name.xen0n.cytosol.ui.preference;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import name.xen0n.cytosol.ui.widget.DynPageFragmentAdapter;
import android.content.Context;
import android.support.v4.app.FragmentManager;


public abstract class BaseSettingsFragmentAdapter
        extends DynPageFragmentAdapter<PreferenceListFragment> {
    Context _ctx;
    ArrayList<String> _titles;

    public BaseSettingsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        _ctx = context;
        _titles = new ArrayList<String>();

        // initialize fragments
        initFragments();
    }

    public abstract void initFragments();

    @Override
    public String getPageTitle(int position) {
        return _titles.get(position);
    }

    public void addItem(int xmlId, int titleResId) {
        addItem(xmlId, titleResId, PreferenceListFragment.class);
    }

    public void addItem(
            int xmlId,
            int titleResId,
            Class<? extends PreferenceListFragment> klass) {
        PreferenceListFragment frag;
        try {
            frag = (PreferenceListFragment) klass
                .getConstructor(int.class)
                .newInstance(xmlId);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }

        // retain state
        frag.setRetainInstance(true);

        _contents.add(frag);
        _titles.add(_ctx.getString(titleResId));

        notifyDataSetChanged();
    }
}
