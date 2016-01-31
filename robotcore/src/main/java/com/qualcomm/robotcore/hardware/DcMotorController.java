// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public interface DcMotorController extends HardwareDevice
{
    void setMotorControllerDeviceMode(final DeviceMode p0);
    
    DeviceMode getMotorControllerDeviceMode();
    
    void setMotorChannelMode(final int p0, final RunMode p1);
    
    RunMode getMotorChannelMode(final int p0);
    
    void setMotorPower(final int p0, final double p1);
    
    double getMotorPower(final int p0);
    
    boolean isBusy(final int p0);
    
    void setMotorPowerFloat(final int p0);
    
    boolean getMotorPowerFloat(final int p0);
    
    void setMotorTargetPosition(final int p0, final int p1);
    
    int getMotorTargetPosition(final int p0);
    
    int getMotorCurrentPosition(final int p0);
    
    public enum RunMode
    {
        RUN_USING_ENCODERS, 
        RUN_WITHOUT_ENCODERS, 
        RUN_TO_POSITION, 
        RESET_ENCODERS;
    }
    
    public enum DeviceMode
    {
        SWITCHING_TO_READ_MODE, 
        SWITCHING_TO_WRITE_MODE, 
        READ_ONLY, 
        WRITE_ONLY, 
        READ_WRITE;
    }
}
