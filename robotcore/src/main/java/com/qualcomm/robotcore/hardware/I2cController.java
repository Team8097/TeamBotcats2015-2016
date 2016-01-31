// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.util.SerialNumber;

public interface I2cController extends HardwareDevice
{
    public static final byte I2C_BUFFER_START_ADDRESS = 4;
    
    SerialNumber getSerialNumber();
    
    void enableI2cReadMode(final int p0, final int p1, final int p2, final int p3);
    
    void enableI2cWriteMode(final int p0, final int p1, final int p2, final int p3);
    
    byte[] getCopyOfReadBuffer(final int p0);
    
    byte[] getCopyOfWriteBuffer(final int p0);
    
    void copyBufferIntoWriteBuffer(final int p0, final byte[] p1);
    
    void setI2cPortActionFlag(final int p0);
    
    boolean isI2cPortActionFlagSet(final int p0);
    
    void readI2cCacheFromController(final int p0);
    
    void writeI2cCacheToController(final int p0);
    
    void writeI2cPortFlagOnlyToController(final int p0);
    
    boolean isI2cPortInReadMode(final int p0);
    
    boolean isI2cPortInWriteMode(final int p0);
    
    boolean isI2cPortReady(final int p0);
    
    Lock getI2cReadCacheLock(final int p0);
    
    Lock getI2cWriteCacheLock(final int p0);
    
    byte[] getI2cReadCache(final int p0);
    
    byte[] getI2cWriteCache(final int p0);
    
    void registerForI2cPortReadyCallback(final I2cPortReadyCallback p0, final int p1);
    
    void deregisterForPortReadyCallback(final int p0);
    
    @Deprecated
    void readI2cCacheFromModule(final int p0);
    
    @Deprecated
    void writeI2cCacheToModule(final int p0);
    
    @Deprecated
    void writeI2cPortFlagOnlyToModule(final int p0);
    
    public interface I2cPortReadyCallback
    {
        void portIsReady(final int p0);
    }
}
