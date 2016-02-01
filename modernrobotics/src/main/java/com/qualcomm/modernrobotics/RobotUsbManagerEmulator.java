// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.modernrobotics;

import java.util.Iterator;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;

public class RobotUsbManagerEmulator implements RobotUsbManager
{
    private ArrayList<b> a;
    
    public RobotUsbManagerEmulator() {
        this.a = new ArrayList<b>();
    }
    
    public int scanForDevices() throws RobotCoreException {
        return this.a.size();
    }
    
    public SerialNumber getDeviceSerialNumberByIndex(final int index) throws RobotCoreException {
        return this.a.get(index).b;
    }
    
    public String getDeviceDescriptionByIndex(final int index) throws RobotCoreException {
        return this.a.get(index).c;
    }
    
    public RobotUsbDevice openBySerialNumber(final SerialNumber serialNumber) throws RobotCoreException {
        RobotLog.d("attempting to open emulated device " + serialNumber);
        for (final b b : this.a) {
            if (b.b.equals((Object)serialNumber)) {
                return (RobotUsbDevice)b;
            }
        }
        throw new RobotCoreException("cannot open device - could not find device with serial number " + serialNumber);
    }
}
