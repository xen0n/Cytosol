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
package name.xen0n.cytosol.util;

import android.content.Context;
import android.telephony.TelephonyManager;


public class TelephonyHelper {
    public static TelephonyManager getTelephonyManager(Context ctx) {
        return (TelephonyManager) (ctx
            .getSystemService(Context.TELEPHONY_SERVICE));
    }

    public static String getPhoneNumber(Context ctx) {
        return getTelephonyManager(ctx).getLine1Number();
    }

    public static String getDeviceId(Context ctx) {
        return getTelephonyManager(ctx).getDeviceId();
    }

    public static int getPhoneType(Context ctx) {
        return getTelephonyManager(ctx).getPhoneType();
    }
}
