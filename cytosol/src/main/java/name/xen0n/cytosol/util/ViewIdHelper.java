/*
 * Partly Copyright 2013 JNRain
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
package name.xen0n.cytosol.util;

import java.util.concurrent.atomic.AtomicInteger;


// code for pre-API 17 devices are taken from Android source code,
// with help from http://stackoverflow.com/questions/1714297
public class ViewIdHelper {
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(
            1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}. This value
     * will not collide with ID values generated at build time by aapt for
     * R.id.
     * 
     * @return a generated ID value
     */
    public static int generateViewId() {
        // Use platform implementation if available, or fallback to a
        // copy of that.
        //
        // Unfortunately Android4Maven's android.jar has no API level 17
        // versions... which means the following would not compile w/o
        // using reflection, something I'd rather NOT do in Java...
        // Commented out for now. Will fix once API level 17 hits Maven
        // Central.
        /*
         * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
         * return View.generateViewId(); }
         */

        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the
            // range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF)
                newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
