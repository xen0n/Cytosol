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
package name.xen0n.cytosol.network.util;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;


public class ConnectivityState {
    protected Context ctx;

    public ConnectivityState(Context ctx) {
        this.ctx = ctx;
    }

    public static ConnectivityState getStateFor(Context ctx) {
        return new ConnectivityState(ctx);
    }

    protected final ConnectivityManager getManager() {
        return (ConnectivityManager) (ctx
            .getSystemService(Service.CONNECTIVITY_SERVICE));
    }

    public boolean isNoNetwork() {
        return getManager().getActiveNetworkInfo() == null;
    }

    public boolean isConnected(int networkType) {
        return getManager().getNetworkInfo(networkType).isConnected();
    }

    public boolean isMobileConnected() {
        return isConnected(ConnectivityManager.TYPE_MOBILE);
    }

    public boolean isWiFiConnected() {
        return isConnected(ConnectivityManager.TYPE_WIFI);
    }
}
