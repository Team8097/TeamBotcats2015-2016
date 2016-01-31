// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface AnalogOutputController extends HardwareDevice
{
    SerialNumber getSerialNumber();
    
    void setAnalogOutputVoltage(final int p0, final int p1);
    
    void setAnalogOutputFrequency(final int p0, final int p1);
    
    void setAnalogOutputMode(final int p0, final byte p1);
}
