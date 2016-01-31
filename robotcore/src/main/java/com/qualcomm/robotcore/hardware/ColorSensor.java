// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class ColorSensor implements HardwareDevice
{
    public abstract int red();
    
    public abstract int green();
    
    public abstract int blue();
    
    public abstract int alpha();
    
    public abstract int argb();
    
    public abstract void enableLed(final boolean p0);
    
    public abstract void setI2cAddress(final int p0);
    
    public abstract int getI2cAddress();
    
    @Override
    public String toString() {
        return String.format("argb: %d", this.argb());
    }
}
