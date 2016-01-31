// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class UltrasonicSensor implements HardwareDevice
{
    public abstract double getUltrasonicLevel();
    
    public abstract String status();
    
    @Override
    public String toString() {
        return String.format("Ultrasonic: %6.1f", this.getUltrasonicLevel());
    }
}
