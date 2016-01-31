// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class I2cDeviceReader
{
    private final I2cDevice a;
    
    public I2cDeviceReader(final I2cDevice i2cDevice, final int i2cAddress, final int memAddress, final int length) {
        (this.a = i2cDevice).enableI2cReadMode(i2cAddress, memAddress, length);
        i2cDevice.setI2cPortActionFlag();
        i2cDevice.writeI2cCacheToModule();
        i2cDevice.registerForI2cPortReadyCallback(new I2cController.I2cPortReadyCallback() {
            @Override
            public void portIsReady(final int port) {
                I2cDeviceReader.this.a();
            }
        });
    }
    
    public byte[] getReadBuffer() {
        return this.a.getCopyOfReadBuffer();
    }
    
    private void a() {
        this.a.setI2cPortActionFlag();
        this.a.readI2cCacheFromModule();
        this.a.writeI2cPortFlagOnlyToModule();
    }
}
