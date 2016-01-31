// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.view.WindowManager;
import android.app.Activity;
import android.os.Handler;

public class Dimmer
{
    public static final int DEFAULT_DIM_TIME = 30000;
    public static final int LONG_BRIGHT_TIME = 60000;
    public static final float MAXIMUM_BRIGHTNESS = 1.0f;
    public static final float MINIMUM_BRIGHTNESS = 0.05f;
    Handler a;
    Activity b;
    final WindowManager.LayoutParams c;
    long d;
    float e;
    
    public Dimmer(final Activity activity) {
        this(30000L, activity);
    }
    
    public Dimmer(final long waitTime, final Activity activity) {
        this.a = new Handler();
        this.e = 1.0f;
        this.d = waitTime;
        this.b = activity;
        this.c = activity.getWindow().getAttributes();
        this.e = this.c.screenBrightness;
    }
    
    private float a() {
        final float n = 0.05f * this.e;
        if (n < 0.05f) {
            return 0.05f;
        }
        return n;
    }
    
    public void handleDimTimer() {
        this.a(this.e);
        this.a.removeCallbacks((Runnable)null);
        this.a.postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                Dimmer.this.a(Dimmer.this.a());
            }
        }, this.d);
    }
    
    private void a(final float screenBrightness) {
        this.c.screenBrightness = screenBrightness;
        this.b.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                Dimmer.this.b.getWindow().setAttributes(Dimmer.this.c);
            }
        });
    }
    
    public void longBright() {
        this.a(this.e);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Dimmer.this.a(Dimmer.this.a());
            }
        };
        this.a.removeCallbacksAndMessages((Object)null);
        this.a.postDelayed((Runnable)runnable, 60000L);
    }
}
