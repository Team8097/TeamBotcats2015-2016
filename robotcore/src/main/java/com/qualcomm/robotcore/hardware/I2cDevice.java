// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;

public class I2cDevice implements HardwareDevice
{
    private I2cController a;
    private int b;
    
    public I2cDevice(final I2cController controller, final int port) {
        this.a = null;
        this.b = -1;
        this.a = controller;
        this.b = port;
    }
    
    public void enableI2cReadMode(final int i2cAddress, final int memAddress, final int length) {
        this.a.enableI2cReadMode(this.b, i2cAddress, memAddress, length);
    }
    
    public void enableI2cWriteMode(final int i2cAddress, final int memAddress, final int length) {
        this.a.enableI2cWriteMode(this.b, i2cAddress, memAddress, length);
    }
    
    public byte[] getCopyOfReadBuffer() {
        return this.a.getCopyOfReadBuffer(this.b);
    }
    
    public byte[] getCopyOfWriteBuffer() {
        return this.a.getCopyOfWriteBuffer(this.b);
    }
    
    public void copyBufferIntoWriteBuffer(final byte[] buffer) {
        this.a.copyBufferIntoWriteBuffer(this.b, buffer);
    }
    
    public void setI2cPortActionFlag() {
        this.a.setI2cPortActionFlag(this.b);
    }
    
    public boolean isI2cPortActionFlagSet() {
        return this.a.isI2cPortActionFlagSet(this.b);
    }
    
    public void readI2cCacheFromController() {
        this.a.readI2cCacheFromController(this.b);
    }
    
    public void writeI2cCacheToController() {
        this.a.writeI2cCacheToController(this.b);
    }
    
    public void writeI2cPortFlagOnlyToController() {
        this.a.writeI2cPortFlagOnlyToController(this.b);
    }
    
    public boolean isI2cPortInReadMode() {
        return this.a.isI2cPortInReadMode(this.b);
    }
    
    public boolean isI2cPortInWriteMode() {
        return this.a.isI2cPortInWriteMode(this.b);
    }
    
    public boolean isI2cPortReady() {
        return this.a.isI2cPortReady(this.b);
    }
    
    public Lock getI2cReadCacheLock() {
        return this.a.getI2cReadCacheLock(this.b);
    }
    
    public Lock getI2cWriteCacheLock() {
        return this.a.getI2cWriteCacheLock(this.b);
    }
    
    public byte[] getI2cReadCache() {
        return this.a.getI2cReadCache(this.b);
    }
    
    public byte[] getI2cWriteCache() {
        return this.a.getI2cWriteCache(this.b);
    }
    
    public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback) {
        this.a.registerForI2cPortReadyCallback(callback, this.b);
    }
    
    public void deregisterForPortReadyCallback() {
        this.a.deregisterForPortReadyCallback(this.b);
    }
    
    @Override
    public String getDeviceName() {
        return "I2cDevice";
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void close() {
    }
    
    @Deprecated
    public void readI2cCacheFromModule() {
        this.readI2cCacheFromController();
    }
    
    @Deprecated
    public void writeI2cCacheToModule() {
        this.writeI2cCacheToController();
    }
    
    @Deprecated
    public void writeI2cPortFlagOnlyToModule() {
        this.writeI2cPortFlagOnlyToController();
    }
}
