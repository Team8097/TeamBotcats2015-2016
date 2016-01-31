// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface PWMOutputController extends HardwareDevice
{
    SerialNumber getSerialNumber();
    
    void setPulseWidthOutputTime(final int p0, final int p1);
    
    void setPulseWidthPeriod(final int p0, final int p1);
    
    int getPulseWidthOutputTime(final int p0);
    
    int getPulseWidthPeriod(final int p0);
}
