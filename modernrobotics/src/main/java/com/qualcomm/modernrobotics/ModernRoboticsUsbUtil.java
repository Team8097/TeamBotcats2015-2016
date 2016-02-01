// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import android.content.Context;
import com.qualcomm.analytics.Analytics;

public class ModernRoboticsUsbUtil
{
    public static final int MFG_CODE_MODERN_ROBOTICS = 77;
    public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
    public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
    public static final int DEVICE_ID_LEGACY_MODULE = 73;
    public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
    private static Analytics a;
    
    public static void init(final Context context, final HardwareMap map) {
        if (ModernRoboticsUsbUtil.a == null) {
            ModernRoboticsUsbUtil.a = new Analytics(context, "update_rc", map);
        }
    }
    
    public static RobotUsbDevice openUsbDevice(final RobotUsbManager usbManager, final SerialNumber serialNumber) throws RobotCoreException {
        return a(usbManager, serialNumber);
    }
    
    private static RobotUsbDevice a(final RobotUsbManager robotUsbManager, final SerialNumber serialNumber) throws RobotCoreException {
        String string = "";
        boolean b = false;
        for (int scanForDevices = robotUsbManager.scanForDevices(), i = 0; i < scanForDevices; ++i) {
            if (serialNumber.equals((Object)robotUsbManager.getDeviceSerialNumberByIndex(i))) {
                b = true;
                string = robotUsbManager.getDeviceDescriptionByIndex(i) + " [" + serialNumber.getSerialNumber() + "]";
                break;
            }
        }
        if (!b) {
            a("unable to find USB device with serial number " + serialNumber.toString());
        }
        RobotUsbDevice openBySerialNumber = null;
        try {
            openBySerialNumber = robotUsbManager.openBySerialNumber(serialNumber);
        }
        catch (RobotCoreException ex) {
            a("Unable to open USB device " + serialNumber + " - " + string + ": " + ex.getMessage());
        }
        try {
            openBySerialNumber.setBaudRate(250000);
            openBySerialNumber.setDataCharacteristics((byte)8, (byte)0, (byte)0);
            openBySerialNumber.setLatencyTimer(2);
        }
        catch (RobotCoreException ex2) {
            openBySerialNumber.close();
            a("Unable to open USB device " + serialNumber + " - " + string + ": " + ex2.getMessage());
        }
        try {
            Thread.sleep(400L);
        }
        catch (InterruptedException ex3) {}
        return openBySerialNumber;
    }
    
    public static byte[] getUsbDeviceHeader(final RobotUsbDevice dev) throws RobotCoreException {
        return a(dev);
    }
    
    private static byte[] a(final RobotUsbDevice robotUsbDevice) throws RobotCoreException {
        final byte[] array = new byte[5];
        final byte[] array2 = new byte[3];
        final byte[] array3 = { 85, -86, -128, 0, 3 };
        try {
            robotUsbDevice.purge(RobotUsbDevice.Channel.RX);
            robotUsbDevice.write(array3);
            robotUsbDevice.read(array);
        }
        catch (RobotCoreException ex) {
            a("error reading Modern Robotics USB device headers");
        }
        if (!com.qualcomm.modernrobotics.a.a(array, 3)) {
            return array2;
        }
        robotUsbDevice.read(array2);
        return array2;
    }
    
    public static DeviceManager.DeviceType getDeviceType(final byte[] deviceHeader) {
        return a(deviceHeader);
    }
    
    private static DeviceManager.DeviceType a(final byte[] array) {
        if (array[1] != 77) {
            return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
        }
        switch (array[2]) {
            case 77: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
            }
            case 83: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
            }
            case 73: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
            }
            case 65: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
            }
            default: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;
            }
        }
    }
    
    private static void a(final String s) throws RobotCoreException {
        System.err.println(s);
        throw new RobotCoreException(s);
    }
}
