// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class ModernRoboticsI2cIrSeekerSensorV3 extends IrSeekerSensor implements I2cController.I2cPortReadyCallback
{
    public volatile int I2C_ADDRESS;
    public static final int ADDRESS_MEM_START = 4;
    public static final int MEM_LENGTH = 12;
    public static final int OFFSET_1200HZ_HEADING_DATA = 4;
    public static final int OFFSET_1200HZ_SIGNAL_STRENGTH = 5;
    public static final int OFFSET_600HZ_HEADING_DATA = 6;
    public static final int OFFSET_600HZ_SIGNAL_STRENGTH = 7;
    public static final int OFFSET_1200HZ_LEFT_SIDE_RAW_DATA = 8;
    public static final int OFFSET_1200HZ_RIGHT_SIDE_RAW_DATA = 10;
    public static final int OFFSET_600HZ_LEFT_SIDE_RAW_DATA = 12;
    public static final int OFFSET_600HZ_RIGHT_SIDE_RAW_DATA = 14;
    public static final byte SENSOR_COUNT = 2;
    public static final double MAX_SENSOR_STRENGTH = 256.0;
    public static final byte INVALID_ANGLE = 0;
    public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625;
    private final DeviceInterfaceModule a;
    private final int b;
    private IrSeekerSensor.Mode c;
    private final byte[] d;
    private final Lock e;
    private double f;
    
    public ModernRoboticsI2cIrSeekerSensorV3(final DeviceInterfaceModule module, final int physicalPort) {
        this.I2C_ADDRESS = 56;
        this.f = 0.00390625;
        this.a = module;
        this.b = physicalPort;
        this.c = IrSeekerSensor.Mode.MODE_1200HZ;
        this.d = this.a.getI2cReadCache(physicalPort);
        this.e = this.a.getI2cReadCacheLock(physicalPort);
        this.a.enableI2cReadMode(physicalPort, this.I2C_ADDRESS, 4, 12);
        this.a.setI2cPortActionFlag(physicalPort);
        this.a.writeI2cCacheToController(physicalPort);
        this.a.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
    }
    
    public void setSignalDetectedThreshold(final double threshold) {
        this.f = threshold;
    }
    
    public double getSignalDetectedThreshold() {
        return this.f;
    }
    
    public void setMode(final IrSeekerSensor.Mode mode) {
        this.c = mode;
    }
    
    public IrSeekerSensor.Mode getMode() {
        return this.c;
    }
    
    public boolean signalDetected() {
        return this.getStrength() > this.f;
    }
    
    public double getAngle() {
        double n = 0.0;
        final int n2 = (this.c == IrSeekerSensor.Mode.MODE_1200HZ) ? 4 : 6;
        try {
            this.e.lock();
            n = this.d[n2];
        }
        finally {
            this.e.unlock();
        }
        return n;
    }
    
    public double getStrength() {
        double n = 0.0;
        final int n2 = (this.c == IrSeekerSensor.Mode.MODE_1200HZ) ? 5 : 7;
        try {
            this.e.lock();
            n = TypeConversion.unsignedByteToDouble(this.d[n2]) / 256.0;
        }
        finally {
            this.e.unlock();
        }
        return n;
    }
    
    public IrSeekerSensor.IrSeekerIndividualSensor[] getIndividualSensors() {
        final IrSeekerSensor.IrSeekerIndividualSensor[] array = new IrSeekerSensor.IrSeekerIndividualSensor[2];
        try {
            this.e.lock();
            final int n = (this.c == IrSeekerSensor.Mode.MODE_1200HZ) ? 8 : 12;
            final byte[] array2 = new byte[2];
            System.arraycopy(this.d, n, array2, 0, array2.length);
            array[0] = new IrSeekerSensor.IrSeekerIndividualSensor(-1.0, TypeConversion.byteArrayToShort(array2, ByteOrder.LITTLE_ENDIAN) / 256.0);
            final int n2 = (this.c == IrSeekerSensor.Mode.MODE_1200HZ) ? 10 : 14;
            final byte[] array3 = new byte[2];
            System.arraycopy(this.d, n2, array3, 0, array3.length);
            array[1] = new IrSeekerSensor.IrSeekerIndividualSensor(1.0, TypeConversion.byteArrayToShort(array3, ByteOrder.LITTLE_ENDIAN) / 256.0);
        }
        finally {
            this.e.unlock();
        }
        return array;
    }
    
    public void portIsReady(final int port) {
        this.a.setI2cPortActionFlag(port);
        this.a.readI2cCacheFromController(port);
        this.a.writeI2cPortFlagOnlyToController(port);
    }
    
    public String getDeviceName() {
        return "Modern Robotics I2C IR Seeker Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port " + this.b;
    }
    
    public int getVersion() {
        return 3;
    }
    
    public void close() {
    }
    
    public void setI2cAddress(final int newAddress) {
        IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
        RobotLog.i(this.getDeviceName() + ", just changed the I2C address. Original address: " + this.I2C_ADDRESS + ", new address: " + newAddress);
        this.I2C_ADDRESS = newAddress;
        this.a.enableI2cReadMode(this.b, this.I2C_ADDRESS, 4, 12);
        this.a.setI2cPortActionFlag(this.b);
        this.a.writeI2cCacheToController(this.b);
        this.a.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, this.b);
    }
    
    public int getI2cAddress() {
        return this.I2C_ADDRESS;
    }
}
