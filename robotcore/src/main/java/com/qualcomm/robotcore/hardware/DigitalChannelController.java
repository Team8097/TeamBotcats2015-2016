// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface DigitalChannelController extends HardwareDevice
{
    SerialNumber getSerialNumber();
    
    Mode getDigitalChannelMode(final int p0);
    
    void setDigitalChannelMode(final int p0, final Mode p1);
    
    boolean getDigitalChannelState(final int p0);
    
    void setDigitalChannelState(final int p0, final boolean p1);
    
    public enum Mode
    {
        INPUT, 
        OUTPUT;
    }
}
