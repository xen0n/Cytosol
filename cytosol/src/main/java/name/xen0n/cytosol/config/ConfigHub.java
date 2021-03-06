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
package name.xen0n.cytosol.config;

import java.util.HashMap;

import android.content.Context;


public class ConfigHub {
    private static ConfigHub hub;
    private HashMap<String, SharedPreferencesHelper> helpers;

    // TODO: refactor this to make the "utils" mechanism more generic
    private UIConfigUtil uiUtil;
    private UpdaterConfigUtil updaterUtil;

    private ConfigHub() {
        helpers = new HashMap<String, SharedPreferencesHelper>();
    }

    private static void ensureHub() {
        if (hub == null) {
            hub = new ConfigHub();
        }
    }

    public static SharedPreferencesHelper getPrefHelper(Context context) {
        ensureHub();
        return hub.ensurePrefHelper("", context);
    }

    public static SharedPreferencesHelper getPrefHelper(
            String name,
            Context context) {
        ensureHub();
        return hub.ensurePrefHelper(name, context);
    }

    public static UIConfigUtil getUIUtil(Context context) {
        ensureHub();
        return hub.ensureUIUtil(context);
    }

    public static UpdaterConfigUtil getUpdaterUtil(Context context) {
        ensureHub();
        return hub.ensureUpdaterUtil(context);
    }

    private SharedPreferencesHelper ensurePrefHelper(
            String name,
            Context context) {
        if (helpers.containsKey(name)) {
            return helpers.get(name);
        }

        // Create a SharedPreferencesHelper.
        // TODO: Currently only MODE_PRIVATE prefs are supported
        SharedPreferencesHelper helper;
        if (name.length() == 0) {
            // Default shared preferences
            helper = new SharedPreferencesHelper(context);
        } else {
            helper = new SharedPreferencesHelper(name, context);
        }

        helpers.put(name, helper);
        return helper;
    }

    private UIConfigUtil ensureUIUtil(Context context) {
        if (uiUtil == null) {
            uiUtil = new UIConfigUtil(context);
        }

        return uiUtil;
    }

    private UpdaterConfigUtil ensureUpdaterUtil(Context context) {
        if (updaterUtil == null) {
            updaterUtil = new UpdaterConfigUtil(context);
        }

        return updaterUtil;
    }
}
