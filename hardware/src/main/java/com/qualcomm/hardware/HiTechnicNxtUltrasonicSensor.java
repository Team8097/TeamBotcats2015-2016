// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class HiTechnicNxtUltrasonicSensor extends UltrasonicSensor implements I2cController.I2cPortReadyCallback
{
    public static final int I2C_ADDRESS = 2;
    public static final int ADDRESS_DISTANCE = 66;
    public static final int MAX_PORT = 5;
    public static final int MIN_PORT = 4;
    Lock a;
    byte[] b;
    private final ModernRoboticsUsbLegacyModule c;
    private final int d;
    
    HiTechnicNxtUltrasonicSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        this.a(physicalPort);
        this.c = legacyModule;
        this.d = physicalPort;
        this.a = legacyModule.getI2cReadCacheLock(physicalPort);
        this.b = legacyModule.getI2cReadCache(physicalPort);
        legacyModule.enableI2cReadMode(physicalPort, 2, 66, 1);
        legacyModule.enable9v(physicalPort, true);
        legacyModule.setI2cPortActionFlag(physicalPort);
        legacyModule.readI2cCacheFromController(physicalPort);
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
    }
    
    public double getUltrasonicLevel() {
        byte b;
        try {
            this.a.lock();
            b = this.b[4];
        }
        finally {
            this.a.unlock();
        }
        return TypeConversion.unsignedByteToDouble(b);
    }
    
    public void portIsReady(final int port) {
        this.c.setI2cPortActionFlag(this.d);
        this.c.writeI2cCacheToController(this.d);
        this.c.readI2cCacheFromController(this.d);
    }
    
    public String status() {
        return String.format("NXT Ultrasonic Sensor, connected via device %s, port %d", this.c.getSerialNumber().toString(), this.d);
    }
    
    public String getDeviceName() {
        return "NXT Ultrasonic Sensor";
    }
    
    public String getConnectionInfo() {
        return this.c.getConnectionInfo() + "; port " + this.d;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
    
    private void a(final int n) {
        if (n < 4 || n > 5) {
            throw new IllegalArgumentException(String.format("Port %d is invalid for " + this.getDeviceName() + "; valid ports are %d or %d", n, 4, 5));
        }
    }
}
