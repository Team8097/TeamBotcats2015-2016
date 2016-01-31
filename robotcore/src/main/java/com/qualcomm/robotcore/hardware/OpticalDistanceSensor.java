// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class OpticalDistanceSensor extends LightSensor
{
    @Override
    public String toString() {
        return String.format("OpticalDistanceSensor: %d", this.getLightDetected());
    }
}
