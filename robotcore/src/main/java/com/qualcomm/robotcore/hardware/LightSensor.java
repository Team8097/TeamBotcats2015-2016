// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class LightSensor implements HardwareDevice
{
    public abstract double getLightDetected();
    
    public abstract int getLightDetectedRaw();
    
    public abstract void enableLed(final boolean p0);
    
    public abstract String status();
    
    @Override
    public String toString() {
        return String.format("Light Level: %1.2f", this.getLightDetected());
    }
}
