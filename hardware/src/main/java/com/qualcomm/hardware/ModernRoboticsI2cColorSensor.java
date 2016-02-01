// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.TypeConversion;

import android.graphics.Color;

import java.util.concurrent.locks.Lock;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class ModernRoboticsI2cColorSensor extends ColorSensor implements I2cController.I2cPortReadyCallback {
    public volatile int I2C_ADDRESS;
    public static final int ADDRESS_COMMAND = 3;
    public static final int ADDRESS_COLOR_NUMBER = 4;
    public static final int OFFSET_COMMAND = 4;
    public static final int OFFSET_COLOR_NUMBER = 5;
    public static final int OFFSET_RED_READING = 6;
    public static final int OFFSET_GREEN_READING = 7;
    public static final int OFFSET_BLUE_READING = 8;
    public static final int OFFSET_ALPHA_VALUE = 9;
    public static final int BUFFER_LENGTH = 6;
    public static final int COMMAND_PASSIVE_LED = 1;
    public static final int COMMAND_ACTIVE_LED = 0;
    private final DeviceInterfaceModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private aEnum f;
    private volatile int g;
    private final int h;

    ModernRoboticsI2cColorSensor(final DeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        this.I2C_ADDRESS = 60;
        this.f = ModernRoboticsI2cColorSensor.aEnum.a;
        this.g = 0;
        this.a = deviceInterfaceModule;
        this.h = physicalPort;
        this.b = deviceInterfaceModule.getI2cReadCache(physicalPort);
        this.c = deviceInterfaceModule.getI2cReadCacheLock(physicalPort);
        this.d = deviceInterfaceModule.getI2cWriteCache(physicalPort);
        this.e = deviceInterfaceModule.getI2cWriteCacheLock(physicalPort);
        deviceInterfaceModule.enableI2cReadMode(physicalPort, this.I2C_ADDRESS, 3, 6);
        deviceInterfaceModule.setI2cPortActionFlag(physicalPort);
        deviceInterfaceModule.writeI2cCacheToController(physicalPort);
        deviceInterfaceModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback) this, physicalPort);
    }

    public int red() {
        return this.a(6);
    }

    public int green() {
        return this.a(7);
    }

    public int blue() {
        return this.a(8);
    }

    public int alpha() {
        return this.a(9);
    }

    public int argb() {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
    }

    public void enableLed(final boolean enable) {
        byte g = 1;
        if (enable) {
            g = 0;
        }
        if (this.g == g) {
            return;
        }
        this.g = g;
        this.f = ModernRoboticsI2cColorSensor.aEnum.b;
        try {
            this.e.lock();
            this.d[4] = g;
        } finally {
            this.e.unlock();
        }
    }

    private int a(final int n) {
        byte b;
        try {
            this.c.lock();
            b = this.b[n];
        } finally {
            this.c.unlock();
        }
        return TypeConversion.unsignedByteToInt(b);
    }

    public String getDeviceName() {
        return "Modern Robotics I2C Color Sensor";
    }

    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port: " + this.h;
    }

    public int getVersion() {
        return 1;
    }

    public void close() {
    }

    public void portIsReady(final int port) {
        this.a.setI2cPortActionFlag(this.h);
        this.a.readI2cCacheFromController(this.h);
        if (this.f == ModernRoboticsI2cColorSensor.aEnum.b) {
            this.a.enableI2cWriteMode(this.h, this.I2C_ADDRESS, 3, 6);
            this.a.writeI2cCacheToController(this.h);
            this.f = ModernRoboticsI2cColorSensor.aEnum.c;
        } else if (this.f == ModernRoboticsI2cColorSensor.aEnum.c) {
            this.a.enableI2cReadMode(this.h, this.I2C_ADDRESS, 3, 6);
            this.a.writeI2cCacheToController(this.h);
            this.f = ModernRoboticsI2cColorSensor.aEnum.a;
        } else {
            this.a.writeI2cPortFlagOnlyToController(this.h);
        }
    }

    public void setI2cAddress(final int newAddress) {
        IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
        RobotLog.i(this.getDeviceName() + ", just changed I2C address. Original address: " + this.I2C_ADDRESS + ", new address: " + newAddress);
        this.I2C_ADDRESS = newAddress;
        this.a.enableI2cReadMode(this.h, this.I2C_ADDRESS, 3, 6);
        this.a.setI2cPortActionFlag(this.h);
        this.a.writeI2cCacheToController(this.h);
        this.a.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback) this, this.h);
    }

    public int getI2cAddress() {
        return this.I2C_ADDRESS;
    }

    private enum aEnum {
        a,
        b,
        c;
    }
}
