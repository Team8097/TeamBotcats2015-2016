// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class CompassSensor implements HardwareDevice
{
    public abstract double getDirection();
    
    public abstract String status();
    
    public abstract void setMode(final CompassMode p0);
    
    public abstract boolean calibrationFailed();
    
    @Override
    public String toString() {
        return String.format("Compass: %3.1f", this.getDirection());
    }
    
    public enum CompassMode
    {
        MEASUREMENT_MODE, 
        CALIBRATION_MODE;
    }
}
