// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.IOException;

public class RunShellCommand
{
    boolean a;
    
    public RunShellCommand() {
        this.a = false;
    }
    
    public void enableLogging(final boolean enable) {
        this.a = enable;
    }
    
    public String run(final String cmd) {
        if (this.a) {
            RobotLog.v("running command: " + cmd);
        }
        final String a = this.a(cmd, false);
        if (this.a) {
            RobotLog.v("         output: " + a);
        }
        return a;
    }
    
    public String runAsRoot(final String cmd) {
        if (this.a) {
            RobotLog.v("running command: " + cmd);
        }
        final String a = this.a(cmd, true);
        if (this.a) {
            RobotLog.v("         output: " + a);
        }
        return a;
    }
    
    private String a(final String s, final boolean b) {
        final byte[] array = new byte[524288];
        String s2 = "";
        final ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        Process start = null;
        try {
            if (b) {
                processBuilder.command("su", "-c", s).redirectErrorStream(true);
            }
            else {
                processBuilder.command("sh", "-c", s).redirectErrorStream(true);
            }
            start = processBuilder.start();
            start.waitFor();
            final int read = start.getInputStream().read(array);
            if (read > 0) {
                s2 = new String(array, 0, read);
            }
        }
        catch (IOException e) {
            RobotLog.logStacktrace(e);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            if (start != null) {
                start.destroy();
            }
        }
        return s2;
    }
    
    public static void killSpawnedProcess(final String processName, final String packageName, final RunShellCommand shell) throws RobotCoreException {
        try {
            for (int i = getSpawnedProcessPid(processName, packageName, shell); i != -1; i = getSpawnedProcessPid(processName, packageName, shell)) {
                RobotLog.v("Killing PID " + i);
                shell.run(String.format("kill %d", i));
            }
        }
        catch (Exception ex) {
            throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", processName));
        }
    }
    
    public static int getSpawnedProcessPid(final String processName, final String packageName, final RunShellCommand shell) {
        final String run = shell.run("ps");
        String s = "invalid";
        for (final String s2 : run.split("\n")) {
            if (s2.contains(packageName)) {
                s = s2.split("\\s+")[0];
                break;
            }
        }
        for (final String s3 : run.split("\n")) {
            if (s3.contains(processName) && s3.contains(s)) {
                return Integer.parseInt(s3.split("\\s+")[1]);
            }
        }
        return -1;
    }
}
