// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface AnalogInputController extends HardwareDevice
{
    int getAnalogInputValue(final int p0);
    
    SerialNumber getSerialNumber();
}
