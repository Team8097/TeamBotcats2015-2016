// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class ModernRoboticsAnalogOpticalDistanceSensor extends OpticalDistanceSensor
{
    private final ModernRoboticsUsbDeviceInterfaceModule a;
    private final int b;
    
    public ModernRoboticsAnalogOpticalDistanceSensor(final ModernRoboticsUsbDeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        this.a = deviceInterfaceModule;
        this.b = physicalPort;
    }
    
    public double getLightDetected() {
        return this.a.getAnalogInputValue(this.b) / 1023.0;
    }
    
    public int getLightDetectedRaw() {
        return this.a.getAnalogInputValue(this.b);
    }
    
    public void enableLed(final boolean enable) {
    }
    
    public String status() {
        return String.format("Optical Distance Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
    
    public String getDeviceName() {
        return "Modern Robotics Analog Optical Distance Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; analog port " + this.b;
    }
    
    public int getVersion() {
        return 0;
    }
    
    public void close() {
    }
}
