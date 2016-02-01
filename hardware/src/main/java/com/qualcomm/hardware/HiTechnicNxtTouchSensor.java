// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class HiTechnicNxtTouchSensor extends TouchSensor
{
    private final ModernRoboticsUsbLegacyModule a;
    private final int b;
    
    public HiTechnicNxtTouchSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        legacyModule.enableAnalogReadMode(physicalPort);
        this.a = legacyModule;
        this.b = physicalPort;
    }
    
    public String status() {
        return String.format("NXT Touch Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
    
    public double getValue() {
        return (TypeConversion.byteArrayToShort(this.a.readAnalog(this.b), ByteOrder.LITTLE_ENDIAN) > 675.0) ? 0.0 : 1.0;
    }
    
    public boolean isPressed() {
        return this.getValue() > 0.0;
    }
    
    public String getDeviceName() {
        return "NXT Touch Sensor";
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
