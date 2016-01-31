// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.os.Build;
import android.os.Message;
import android.os.Handler;
import android.view.View;

public class ImmersiveMode
{
    View a;
    Handler b;
    
    public ImmersiveMode(final View decorView) {
        this.b = new Handler() {
            public void handleMessage(final Message msg) {
                ImmersiveMode.this.hideSystemUI();
            }
        };
        this.a = decorView;
    }
    
    public void cancelSystemUIHide() {
        this.b.removeMessages(0);
    }
    
    public void hideSystemUI() {
        this.a.setSystemUiVisibility(4098);
    }
    
    public static boolean apiOver19() {
        return Build.VERSION.SDK_INT >= 19;
    }
}
