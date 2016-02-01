// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import android.graphics.Color;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class AdafruitI2cColorSensor extends ColorSensor implements I2cController.I2cPortReadyCallback
{
    public static final int I2C_ADDRESS_TCS34725 = 82;
    public static final int TCS34725_COMMAND_BIT = 128;
    public static final int TCS34725_ID = 18;
    public static final int ADDRESS_TCS34725_ENABLE = 0;
    public static final int TCS34725_ENABLE_AIEN = 16;
    public static final int TCS34725_ENABLE_AEN = 2;
    public static final int TCS34725_ENABLE_PON = 1;
    public static final int TCS34725_CDATAL = 20;
    public static final int TCS34725_RDATAL = 22;
    public static final int TCS34725_GDATAL = 24;
    public static final int TCS34725_BDATAL = 26;
    public static final int OFFSET_ALPHA_LOW_BYTE = 4;
    public static final int OFFSET_ALPHA_HIGH_BYTE = 5;
    public static final int OFFSET_RED_LOW_BYTE = 6;
    public static final int OFFSET_RED_HIGH_BYTE = 7;
    public static final int OFFSET_GREEN_LOW_BYTE = 8;
    public static final int OFFSET_GREEN_HIGH_BYTE = 9;
    public static final int OFFSET_BLUE_LOW_BYTE = 10;
    public static final int OFFSET_BLUE_HIGH_BYTE = 11;
    private final DeviceInterfaceModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private boolean g;
    private boolean h;
    
    public AdafruitI2cColorSensor(final DeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        this.g = false;
        this.h = false;
        this.f = physicalPort;
        this.a = deviceInterfaceModule;
        this.b = deviceInterfaceModule.getI2cReadCache(physicalPort);
        this.c = deviceInterfaceModule.getI2cReadCacheLock(physicalPort);
        this.d = deviceInterfaceModule.getI2cWriteCache(physicalPort);
        this.e = deviceInterfaceModule.getI2cWriteCacheLock(physicalPort);
        this.g = true;
        deviceInterfaceModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
    }
    
    public int red() {
        return this.a(7, 6);
    }
    
    public int green() {
        return this.a(9, 8);
    }
    
    public int blue() {
        return this.a(11, 10);
    }
    
    public int alpha() {
        return this.a(5, 4);
    }
    
    private int a(final int n, final int n2) {
        int n3;
        try {
            this.c.lock();
            n3 = (this.b[n] << 8 | (this.b[n2] & 0xFF));
        }
        finally {
            this.c.unlock();
        }
        return n3;
    }
    
    public int argb() {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
    }
    
    public void enableLed(final boolean enable) {
        throw new UnsupportedOperationException("enableLed is not implemented.");
    }
    
    public String getDeviceName() {
        return "Adafruit I2C Color Sensor";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port: " + this.f;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
    
    public void portIsReady(final int port) {
        if (this.g) {
            this.b();
            this.g = false;
            this.h = true;
        }
        else if (this.h) {
            this.a();
            this.h = false;
        }
        this.a.readI2cCacheFromController(this.f);
        this.a.setI2cPortActionFlag(this.f);
        this.a.writeI2cPortFlagOnlyToController(this.f);
    }
    
    private void a() {
        this.a.enableI2cReadMode(this.f, 82, 148, 8);
        this.a.writeI2cCacheToController(this.f);
    }
    
    private void b() {
        this.a.enableI2cWriteMode(this.f, 82, 128, 1);
        try {
            this.e.lock();
            this.d[4] = 3;
        }
        finally {
            this.e.unlock();
        }
        this.a.setI2cPortActionFlag(this.f);
        this.a.writeI2cCacheToController(this.f);
    }
    
    public void setI2cAddress(final int newAddress) {
        throw new UnsupportedOperationException("setI2cAddress is not supported.");
    }
    
    public int getI2cAddress() {
        throw new UnsupportedOperationException("getI2cAddress is not supported.");
    }
}
