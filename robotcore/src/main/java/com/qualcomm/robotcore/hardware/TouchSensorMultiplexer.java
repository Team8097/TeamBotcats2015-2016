// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class TouchSensorMultiplexer implements HardwareDevice
{
    public abstract boolean isTouchSensorPressed(final int p0);
    
    public abstract int getSwitches();
}
