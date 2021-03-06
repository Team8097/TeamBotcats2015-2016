// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class HiTechnicNxtGyroSensor extends GyroSensor
{
    private final ModernRoboticsUsbLegacyModule a;
    private final int b;
    
    HiTechnicNxtGyroSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        legacyModule.enableAnalogReadMode(physicalPort);
        this.a = legacyModule;
        this.b = physicalPort;
    }
    
    public void calibrate() {
        this.notSupported();
    }
    
    public boolean isCalibrating() {
        this.notSupported();
        return false;
    }
    
    public int getHeading() {
        this.notSupported();
        return 0;
    }
    
    public double getRotation() {
        return TypeConversion.byteArrayToShort(this.a.readAnalog(this.b), ByteOrder.LITTLE_ENDIAN);
    }
    
    public int rawX() {
        this.notSupported();
        return 0;
    }
    
    public int rawY() {
        this.notSupported();
        return 0;
    }
    
    public int rawZ() {
        this.notSupported();
        return 0;
    }
    
    public void resetZAxisIntegrator() {
        this.notSupported();
    }
    
    public String status() {
        return String.format("NXT Gyro Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
    
    public String getDeviceName() {
        return "NXT Gyro Sensor";
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
