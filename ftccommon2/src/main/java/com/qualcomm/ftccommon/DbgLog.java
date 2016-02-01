// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import android.util.Log;

public class DbgLog
{
    public static final String TAG = "FIRST";
    public static final String ERROR_PREPEND = "### ERROR: ";
    
    public static void msg(final String message) {
        Log.i("FIRST", message);
    }
    
    public static void error(final String message) {
        Log.e("FIRST", "### ERROR: " + message);
    }
    
    public static void logStacktrace(final Exception e) {
        msg(e.toString());
        final StackTraceElement[] stackTrace = e.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            msg(stackTrace[i].toString());
        }
    }
}
