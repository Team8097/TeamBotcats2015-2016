// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.ftdi.j2xx.D2xxManager;
import android.content.Context;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;

public class RobotUsbManagerFtdi implements RobotUsbManager
{
    private Context a;
    private D2xxManager b;
    
    public RobotUsbManagerFtdi(final Context context) {
        this.a = context;
        try {
            this.b = D2xxManager.getInstance(context);
        }
        catch (D2xxManager.D2xxException ex) {
            RobotLog.e("Unable to create D2xxManager; cannot open USB devices");
        }
    }
    
    @Override
    public int scanForDevices() throws RobotCoreException {
        return this.b.createDeviceInfoList(this.a);
    }
    
    @Override
    public SerialNumber getDeviceSerialNumberByIndex(final int index) throws RobotCoreException {
        return new SerialNumber(this.b.getDeviceInfoListDetail(index).serialNumber);
    }
    
    @Override
    public String getDeviceDescriptionByIndex(final int index) throws RobotCoreException {
        return this.b.getDeviceInfoListDetail(index).description;
    }
    
    @Override
    public RobotUsbDevice openBySerialNumber(final SerialNumber serialNumber) throws RobotCoreException {
        final FT_Device openBySerialNumber = this.b.openBySerialNumber(this.a, serialNumber.toString());
        if (openBySerialNumber == null) {
            throw new RobotCoreException("FTDI driver failed to open USB device with serial number " + serialNumber + " (returned null device)");
        }
        return new RobotUsbDeviceFtdi(openBySerialNumber);
    }
}
