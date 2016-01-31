// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;
import android.content.Context;

public class BatteryChecker
{
    private Context b;
    private long c;
    private long d;
    private BatteryWatcher e;
    protected Handler batteryHandler;
    Runnable a;
    
    public BatteryChecker(final Context context, final BatteryWatcher watcher, final long delay) {
        this.d = 5000L;
        this.a = new Runnable() {
            @Override
            public void run() {
                final float batteryLevel = BatteryChecker.this.getBatteryLevel();
                BatteryChecker.this.e.updateBatteryLevel(batteryLevel);
                RobotLog.i("Battery Checker, Level Remaining: " + batteryLevel);
                BatteryChecker.this.batteryHandler.postDelayed(BatteryChecker.this.a, BatteryChecker.this.c);
            }
        };
        this.b = context;
        this.e = watcher;
        this.c = delay;
        this.batteryHandler = new Handler();
    }
    
    public float getBatteryLevel() {
        final Intent registerReceiver = this.b.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        final int intExtra = registerReceiver.getIntExtra("level", -1);
        final int intExtra2 = registerReceiver.getIntExtra("scale", -1);
        int n = -1;
        if (intExtra >= 0 && intExtra2 > 0) {
            n = intExtra * 100 / intExtra2;
        }
        return n;
    }
    
    public void startBatteryMonitoring() {
        this.batteryHandler.postDelayed(this.a, this.d);
    }
    
    public void endBatteryMonitoring() {
        this.batteryHandler.removeCallbacks(this.a);
    }
    
    public interface BatteryWatcher
    {
        void updateBatteryLevel(final float p0);
    }
}
