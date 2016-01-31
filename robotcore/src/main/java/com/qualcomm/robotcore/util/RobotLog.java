// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.os.Environment;
import java.io.File;
import android.content.Context;
import com.qualcomm.robotcore.exception.RobotCoreException;
import android.util.Log;

public class RobotLog
{
    private static String a;
    public static final String TAG = "RobotCore";
    private static boolean b;
    
    public static void v(final String message) {
        Log.v("RobotCore", message);
    }
    
    public static void d(final String message) {
        Log.d("RobotCore", message);
    }
    
    public static void i(final String message) {
        Log.i("RobotCore", message);
    }
    
    public static void w(final String message) {
        Log.w("RobotCore", message);
    }
    
    public static void e(final String message) {
        Log.e("RobotCore", message);
    }
    
    public static void logStacktrace(final Exception e) {
        e(e.toString());
        final StackTraceElement[] stackTrace = e.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            e(stackTrace[i].toString());
        }
    }
    
    public static void logStacktrace(final RobotCoreException e) {
        e(e.toString());
        final StackTraceElement[] stackTrace = e.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            e(stackTrace[i].toString());
        }
        if (e.isChainedException()) {
            e("Exception chained from:");
            if (e.getChainedException() instanceof RobotCoreException) {
                logStacktrace((RobotCoreException)e.getChainedException());
            }
            else {
                logStacktrace(e.getChainedException());
            }
        }
    }
    
    public static void setGlobalErrorMsg(final String message) {
        if (RobotLog.a.isEmpty()) {
            RobotLog.a += message;
        }
    }
    
    public static void setGlobalErrorMsgAndThrow(final String message, final RobotCoreException e) throws RobotCoreException {
        setGlobalErrorMsg(message + "\n" + e.getMessage());
        throw e;
    }
    
    public static String getGlobalErrorMsg() {
        return RobotLog.a;
    }
    
    public static boolean hasGlobalErrorMsg() {
        return !RobotLog.a.isEmpty();
    }
    
    public static void clearGlobalErrorMsg() {
        RobotLog.a = "";
    }
    
    public static void logAndThrow(final String errMsg) throws RobotCoreException {
        w(errMsg);
        throw new RobotCoreException(errMsg);
    }
    
    public static void writeLogcatToDisk(final Context context, final int fileSizeKb) {
        if (RobotLog.b) {
            return;
        }
        RobotLog.b = true;
        new Thread("Logging Thread") {
            final /* synthetic */ String a = new File(getLogFilename(context)).getAbsolutePath();
            final /* synthetic */ String b = context.getPackageName();
            
            @Override
            public void run() {
                try {
                    RobotLog.v("saving logcat to " + this.a);
                    final RunShellCommand shell = new RunShellCommand();
                    RunShellCommand.killSpawnedProcess("logcat", this.b, shell);
                    shell.run(String.format("logcat -f %s -r%d -n%d -v time %s", this.a, fileSizeKb, 1, "UsbRequestJNI:S UsbRequest:S *:V"));
                }
                catch (RobotCoreException ex) {
                    RobotLog.v("Error while writing log file to disk: " + ex.toString());
                }
                finally {
                    RobotLog.b = false;
                }
            }
        }.start();
    }
    
    public static String getLogFilename(final Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + ".logcat";
    }
    
    public static void cancelWriteLogcatToDisk(final Context context) {
        final String packageName = context.getPackageName();
        final String absolutePath = new File(Environment.getExternalStorageDirectory(), packageName).getAbsolutePath();
        RobotLog.b = false;
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex2) {}
                try {
                    RobotLog.v("closing logcat file " + absolutePath);
                    RunShellCommand.killSpawnedProcess("logcat", packageName, new RunShellCommand());
                }
                catch (RobotCoreException ex) {
                    RobotLog.v("Unable to cancel writing log file to disk: " + ex.toString());
                }
            }
        }.start();
    }
    
    static {
        RobotLog.a = "";
        RobotLog.b = false;
    }
}
