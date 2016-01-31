// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbManager
{
    int scanForDevices() throws RobotCoreException;
    
    SerialNumber getDeviceSerialNumberByIndex(final int p0) throws RobotCoreException;
    
    String getDeviceDescriptionByIndex(final int p0) throws RobotCoreException;
    
    RobotUsbDevice openBySerialNumber(final SerialNumber p0) throws RobotCoreException;
}
