// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import android.graphics.Color;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class HiTechnicNxtColorSensor extends ColorSensor implements I2cController.I2cPortReadyCallback
{
    public static final int ADDRESS_I2C = 2;
    public static final int ADDRESS_COMMAND = 65;
    public static final int ADDRESS_COLOR_NUMBER = 66;
    public static final int OFFSET_COMMAND = 4;
    public static final int OFFSET_COLOR_NUMBER = 5;
    public static final int OFFSET_RED_READING = 6;
    public static final int OFFSET_GREEN_READING = 7;
    public static final int OFFSET_BLUE_READING = 8;
    public static final int BUFFER_LENGTH = 5;
    public static final int COMMAND_PASSIVE_LED = 1;
    public static final int COMMAND_ACTIVE_LED = 0;
    private final LegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private aEnum f;
    private volatile int g;
    private final int h;
    
    HiTechnicNxtColorSensor(final LegacyModule legacyModule, final int physicalPort) {
        this.f = HiTechnicNxtColorSensor.aEnum.a;
        this.g = 0;
        this.a = legacyModule;
        this.h = physicalPort;
        this.b = legacyModule.getI2cReadCache(physicalPort);
        this.c = legacyModule.getI2cReadCacheLock(physicalPort);
        this.d = legacyModule.getI2cWriteCache(physicalPort);
        this.e = legacyModule.getI2cWriteCacheLock(physicalPort);
        legacyModule.enableI2cReadMode(physicalPort, 2, 65, 5);
        legacyModule.setI2cPortActionFlag(physicalPort);
        legacyModule.writeI2cCacheToController(physicalPort);
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
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
        return 0;
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
        this.f = HiTechnicNxtColorSensor.aEnum.b;
        try {
            this.e.lock();
            this.d[4] = g;
        }
        finally {
            this.e.unlock();
        }
    }
    
    public void setI2cAddress(final int newAddress) {
        throw new UnsupportedOperationException("setI2cAddress is not supported.");
    }
    
    public int getI2cAddress() {
        throw new UnsupportedOperationException("getI2cAddress is not supported.");
    }
    
    private int a(final int n) {
        byte b;
        try {
            this.c.lock();
            b = this.b[n];
        }
        finally {
            this.c.unlock();
        }
        return TypeConversion.unsignedByteToInt(b);
    }
    
    public String getDeviceName() {
        return "NXT Color Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port: " + this.h;
    }
    
    public int getVersion() {
        return 2;
    }
    
    public void close() {
    }
    
    public void portIsReady(final int port) {
        this.a.setI2cPortActionFlag(this.h);
        this.a.readI2cCacheFromController(this.h);
        if (this.f == HiTechnicNxtColorSensor.aEnum.b) {
            this.a.enableI2cWriteMode(this.h, 2, 65, 5);
            this.a.writeI2cCacheToController(this.h);
            this.f = HiTechnicNxtColorSensor.aEnum.c;
        }
        else if (this.f == HiTechnicNxtColorSensor.aEnum.c) {
            this.a.enableI2cReadMode(this.h, 2, 65, 5);
            this.a.writeI2cCacheToController(this.h);
            this.f = HiTechnicNxtColorSensor.aEnum.a;
        }
        else {
            this.a.writeI2cPortFlagOnlyToController(this.h);
        }
    }
    
    private enum aEnum
    {
        a, 
        b, 
        c;
    }
}
