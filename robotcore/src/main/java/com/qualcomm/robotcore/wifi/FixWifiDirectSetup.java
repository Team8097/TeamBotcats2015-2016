// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.wifi;

import android.net.wifi.WifiManager;

public class FixWifiDirectSetup
{
    public static final int WIFI_TOGGLE_DELAY = 2000;
    
    public static void fixWifiDirectSetup(final WifiManager wifiManager) throws InterruptedException {
        a(false, wifiManager);
        a(true, wifiManager);
    }
    
    private static void a(final boolean wifiEnabled, final WifiManager wifiManager) throws InterruptedException {
        wifiManager.setWifiEnabled(wifiEnabled);
        Thread.sleep(2000L);
    }
}
