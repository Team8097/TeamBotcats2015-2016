// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public interface LegacyModule extends HardwareDevice, I2cController
{
    void enableAnalogReadMode(final int p0);
    
    void enable9v(final int p0, final boolean p1);
    
    void setDigitalLine(final int p0, final int p1, final boolean p2);
    
    byte[] readAnalog(final int p0);
}
