// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public interface ServoController extends HardwareDevice
{
    void pwmEnable();
    
    void pwmDisable();
    
    PwmStatus getPwmStatus();
    
    void setServoPosition(final int p0, final double p1);
    
    double getServoPosition(final int p0);
    
    public enum PwmStatus
    {
        ENABLED, 
        DISABLED;
    }
}
