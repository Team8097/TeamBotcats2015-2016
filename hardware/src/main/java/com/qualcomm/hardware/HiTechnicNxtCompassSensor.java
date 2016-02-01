// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.CompassSensor;

public class HiTechnicNxtCompassSensor extends CompassSensor implements I2cController.I2cPortReadyCallback
{
    public static final byte I2C_ADDRESS = 2;
    public static final byte MODE_CONTROL_ADDRESS = 65;
    public static final byte CALIBRATION = 67;
    public static final byte MEASUREMENT = 0;
    public static final byte HEADING_IN_TWO_DEGREE_INCREMENTS = 66;
    public static final byte ONE_DEGREE_HEADING_ADDER = 67;
    public static final byte CALIBRATION_FAILURE = 70;
    public static final byte DIRECTION_START = 7;
    public static final byte DIRECTION_END = 9;
    public static final double INVALID_DIRECTION = -1.0;
    public static final int HEADING_WORD_LENGTH = 2;
    public static final int COMPASS_BUFFER = 65;
    public static final int COMPASS_BUFFER_SIZE = 5;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private CompassSensor.CompassMode g;
    private boolean h;
    private boolean i;
    
    public HiTechnicNxtCompassSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        this.g = CompassSensor.CompassMode.MEASUREMENT_MODE;
        this.h = false;
        this.i = false;
        legacyModule.enableI2cReadMode(physicalPort, 2, 65, 5);
        this.a = legacyModule;
        this.b = legacyModule.getI2cReadCache(physicalPort);
        this.c = legacyModule.getI2cReadCacheLock(physicalPort);
        this.d = legacyModule.getI2cWriteCache(physicalPort);
        this.e = legacyModule.getI2cWriteCacheLock(physicalPort);
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, this.f = physicalPort);
    }
    
    public double getDirection() {
        if (this.h) {
            return -1.0;
        }
        if (this.g == CompassSensor.CompassMode.CALIBRATION_MODE) {
            return -1.0;
        }
        byte[] copyOfRange = null;
        try {
            this.c.lock();
            copyOfRange = Arrays.copyOfRange(this.b, 7, 9);
        }
        finally {
            this.c.unlock();
        }
        return TypeConversion.byteArrayToShort(copyOfRange, ByteOrder.LITTLE_ENDIAN);
    }
    
    public String status() {
        return String.format("NXT Compass Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.f);
    }
    
    public void setMode(final CompassSensor.CompassMode mode) {
        if (this.g == mode) {
            return;
        }
        this.g = mode;
        this.a();
    }
    
    private void a() {
        this.h = true;
        final byte b = (byte)((this.g == CompassSensor.CompassMode.CALIBRATION_MODE) ? 67 : 0);
        this.a.enableI2cWriteMode(this.f, 2, 65, 1);
        try {
            this.e.lock();
            this.d[3] = b;
        }
        finally {
            this.e.unlock();
        }
    }
    
    private void b() {
        if (this.g == CompassSensor.CompassMode.MEASUREMENT_MODE) {
            this.a.enableI2cReadMode(this.f, 2, 65, 5);
        }
        this.h = false;
    }
    
    public boolean calibrationFailed() {
        if (this.g == CompassSensor.CompassMode.CALIBRATION_MODE || this.h) {
            return false;
        }
        boolean b = false;
        try {
            this.c.lock();
            b = (this.b[3] == 70);
        }
        finally {
            this.c.unlock();
        }
        return b;
    }
    
    public void portIsReady(final int port) {
        this.a.setI2cPortActionFlag(this.f);
        this.a.readI2cCacheFromController(this.f);
        if (this.h) {
            this.b();
            this.a.writeI2cCacheToController(this.f);
        }
        else {
            this.a.writeI2cPortFlagOnlyToController(this.f);
        }
    }
    
    public String getDeviceName() {
        return "NXT Compass Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.f;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
}
