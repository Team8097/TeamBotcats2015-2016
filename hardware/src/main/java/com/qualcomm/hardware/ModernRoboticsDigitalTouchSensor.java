// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ModernRoboticsDigitalTouchSensor extends TouchSensor
{
    private DeviceInterfaceModule a;
    private int b;
    
    public ModernRoboticsDigitalTouchSensor(final DeviceInterfaceModule module, final int physicalPort) {
        this.a = null;
        this.b = -1;
        this.a = module;
        this.b = physicalPort;
    }
    
    public double getValue() {
        return this.isPressed() ? 1.0 : 0.0;
    }
    
    public boolean isPressed() {
        return this.a.getDigitalChannelState(this.b);
    }
    
    public String getDeviceName() {
        return "Modern Robotics Digital Touch Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; digital port " + this.b;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
}
