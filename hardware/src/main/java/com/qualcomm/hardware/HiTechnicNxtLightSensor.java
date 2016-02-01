// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.LightSensor;

public class HiTechnicNxtLightSensor extends LightSensor
{
    public static final byte LED_DIGITAL_LINE_NUMBER = 0;
    private final ModernRoboticsUsbLegacyModule a;
    private final int b;
    
    HiTechnicNxtLightSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        legacyModule.enableAnalogReadMode(physicalPort);
        this.a = legacyModule;
        this.b = physicalPort;
    }
    
    public double getLightDetected() {
        return Range.scale((double)this.a.readAnalog(this.b)[0], -128.0, 127.0, 0.0, 1.0);
    }
    
    public int getLightDetectedRaw() {
        return TypeConversion.unsignedByteToInt(this.a.readAnalog(this.b)[0]);
    }
    
    public void enableLed(final boolean enable) {
        this.a.setDigitalLine(this.b, 0, enable);
    }
    
    public String status() {
        return String.format("NXT Light Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
    
    public String getDeviceName() {
        return "NXT Light Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
}
