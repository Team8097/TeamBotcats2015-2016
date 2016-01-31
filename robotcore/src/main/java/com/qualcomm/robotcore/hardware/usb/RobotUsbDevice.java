// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbDevice
{
    void setBaudRate(final int p0) throws RobotCoreException;
    
    void setDataCharacteristics(final byte p0, final byte p1, final byte p2) throws RobotCoreException;
    
    void setLatencyTimer(final int p0) throws RobotCoreException;
    
    void purge(final Channel p0) throws RobotCoreException;
    
    void write(final byte[] p0) throws RobotCoreException;
    
    int read(final byte[] p0) throws RobotCoreException;
    
    int read(final byte[] p0, final int p1, final int p2) throws RobotCoreException;
    
    void close();
    
    public enum Channel
    {
        RX, 
        TX, 
        NONE, 
        BOTH;
    }
}
