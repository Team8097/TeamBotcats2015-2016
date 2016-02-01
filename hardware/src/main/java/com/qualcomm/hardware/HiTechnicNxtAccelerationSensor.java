// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.AccelerationSensor;

public class HiTechnicNxtAccelerationSensor extends AccelerationSensor implements I2cController.I2cPortReadyCallback
{
    public static final byte I2C_ADDRESS = 2;
    public static final int ADDRESS_ACCEL_START = 66;
    public static final int ACCEL_LENGTH = 6;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final int d;
    
    public HiTechnicNxtAccelerationSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        legacyModule.enableI2cReadMode(physicalPort, 2, 66, 6);
        this.a = legacyModule;
        this.b = legacyModule.getI2cReadCache(physicalPort);
        this.c = legacyModule.getI2cReadCacheLock(physicalPort);
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, this.d = physicalPort);
    }
    
    public AccelerationSensor.Acceleration getAcceleration() {
        final AccelerationSensor.Acceleration acceleration = new AccelerationSensor.Acceleration();
        try {
            this.c.lock();
            acceleration.x = this.a(this.b[4], this.b[7]);
            acceleration.y = this.a(this.b[5], this.b[8]);
            acceleration.z = this.a(this.b[6], this.b[9]);
        }
        finally {
            this.c.unlock();
        }
        return acceleration;
    }
    
    public String status() {
        return String.format("NXT Acceleration Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.d);
    }
    
    private double a(final double n, final double n2) {
        return (n * 4.0 + n2) / 200.0;
    }
    
    public void portIsReady(final int port) {
        this.a.setI2cPortActionFlag(this.d);
        this.a.writeI2cPortFlagOnlyToController(this.d);
        this.a.readI2cCacheFromController(this.d);
    }
    
    public String getDeviceName() {
        return "NXT Acceleration Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.d;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
}
