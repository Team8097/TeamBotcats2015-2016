// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class HiTechnicNxtIrSeekerSensor extends IrSeekerSensor implements I2cController.I2cPortReadyCallback
{
    public static final int I2C_ADDRESS = 16;
    public static final int MEM_MODE_ADDRESS = 65;
    public static final int MEM_DC_START_ADDRESS = 66;
    public static final int MEM_AC_START_ADDRESS = 73;
    public static final int MEM_READ_LENGTH = 6;
    public static final byte MODE_AC = 0;
    public static final byte MODE_DC = 2;
    public static final byte DIRECTION = 4;
    public static final byte SENSOR_FIRST = 5;
    public static final byte SENSOR_COUNT = 9;
    public static final double MAX_SENSOR_STRENGTH = 256.0;
    public static final byte INVALID_ANGLE = 0;
    public static final byte MIN_ANGLE = 1;
    public static final byte MAX_ANGLE = 9;
    public static final double[] DIRECTION_TO_ANGLE;
    public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private IrSeekerSensor.Mode g;
    private double h;
    private volatile boolean i;
    
    public HiTechnicNxtIrSeekerSensor(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        this.h = 0.00390625;
        this.a = legacyModule;
        this.b = legacyModule.getI2cReadCache(physicalPort);
        this.c = legacyModule.getI2cReadCacheLock(physicalPort);
        this.d = legacyModule.getI2cWriteCache(physicalPort);
        this.e = legacyModule.getI2cWriteCacheLock(physicalPort);
        this.f = physicalPort;
        this.g = IrSeekerSensor.Mode.MODE_1200HZ;
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
        this.i = true;
    }
    
    public void setSignalDetectedThreshold(final double threshold) {
        this.h = threshold;
    }
    
    public double getSignalDetectedThreshold() {
        return this.h;
    }
    
    public void setMode(final IrSeekerSensor.Mode mode) {
        if (this.g == mode) {
            return;
        }
        this.g = mode;
        this.a();
    }
    
    public IrSeekerSensor.Mode getMode() {
        return this.g;
    }
    
    public boolean signalDetected() {
        if (this.i) {
            return false;
        }
        boolean b = false;
        try {
            this.c.lock();
            b = (this.b[4] != 0);
        }
        finally {
            this.c.unlock();
        }
        return b && this.getStrength() > this.h;
    }
    
    public double getAngle() {
        if (this.i) {
            return 0.0;
        }
        double n = 0.0;
        try {
            this.c.lock();
            if (this.b[4] < 1 || this.b[4] > 9) {
                n = 0.0;
            }
            else {
                n = HiTechnicNxtIrSeekerSensor.DIRECTION_TO_ANGLE[this.b[4]];
            }
        }
        finally {
            this.c.unlock();
        }
        return n;
    }
    
    public double getStrength() {
        if (this.i) {
            return 0.0;
        }
        double max = 0.0;
        try {
            this.c.lock();
            for (int i = 0; i < 9; ++i) {
                max = Math.max(max, this.a(this.b, i));
            }
        }
        finally {
            this.c.unlock();
        }
        return max;
    }
    
    public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors() {
        final IrSeekerSensor.IrSeekerIndividualSensor[] array = new IrSeekerSensor.IrSeekerIndividualSensor[9];
        if (this.i) {
            return array;
        }
        try {
            this.c.lock();
            for (int i = 0; i < 9; ++i) {
                array[i] = new IrSeekerSensor.IrSeekerIndividualSensor(HiTechnicNxtIrSeekerSensor.DIRECTION_TO_ANGLE[i * 2 + 1], this.a(this.b, i));
            }
        }
        finally {
            this.c.unlock();
        }
        return array;
    }
    
    public void setI2cAddress(final int newAddress) {
        throw new UnsupportedOperationException("This method is not supported.");
    }
    
    public int getI2cAddress() {
        return 16;
    }
    
    private void a() {
        this.i = true;
        final byte b = (byte)((this.g == IrSeekerSensor.Mode.MODE_600HZ) ? 2 : 0);
        this.a.enableI2cWriteMode(this.f, 16, 65, 1);
        try {
            this.e.lock();
            this.d[4] = b;
        }
        finally {
            this.e.unlock();
        }
    }
    
    private double a(final byte[] array, final int n) {
        return TypeConversion.unsignedByteToDouble(array[n + 5]) / 256.0;
    }
    
    public void portIsReady(final int port) {
        this.a.setI2cPortActionFlag(this.f);
        this.a.readI2cCacheFromController(this.f);
        if (this.i) {
            if (this.g == IrSeekerSensor.Mode.MODE_600HZ) {
                this.a.enableI2cReadMode(this.f, 16, 66, 6);
            }
            else {
                this.a.enableI2cReadMode(this.f, 16, 73, 6);
            }
            this.a.writeI2cCacheToController(this.f);
            this.i = false;
        }
        else {
            this.a.writeI2cPortFlagOnlyToController(this.f);
        }
    }
    
    public String getDeviceName() {
        return "NXT IR Seeker Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.f;
    }
    
    public int getVersion() {
        return 2;
    }
    
    public void close() {
    }
    
    static {
        DIRECTION_TO_ANGLE = new double[] { 0.0, -120.0, -90.0, -60.0, -30.0, 0.0, 30.0, 60.0, 90.0, 120.0 };
    }
}
