// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robot;

import com.qualcomm.robotcore.util.RobotLog;

public enum RobotState
{
    NOT_STARTED(0), 
    INIT(1), 
    RUNNING(2), 
    STOPPED(3), 
    EMERGENCY_STOP(4), 
    DROPPED_CONNECTION(5);
    
    private int a;
    private static final RobotState[] b;
    
    private RobotState(final int state) {
        this.a = state;
    }
    
    public byte asByte() {
        return (byte)this.a;
    }
    
    public static RobotState fromByte(final byte b) {
        RobotState not_STARTED = RobotState.NOT_STARTED;
        try {
            not_STARTED = RobotState.b[b];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            RobotLog.w(String.format("Cannot convert %d to RobotState: %s", b, ex.toString()));
        }
        return not_STARTED;
    }
    
    static {
        b = values();
    }
}
