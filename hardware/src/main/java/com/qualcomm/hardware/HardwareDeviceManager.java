// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import java.util.HashMap;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.modernrobotics.RobotUsbManagerEmulator;
import android.content.Context;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.DeviceManager;

public class HardwareDeviceManager extends DeviceManager
{
    private static a a;
    private RobotUsbManager b;
    private final EventLoopManager c;
    
    public HardwareDeviceManager(final Context context, final EventLoopManager manager) throws RobotCoreException {
        this.c = manager;
        switch (a) {
            case b: {
                this.b = new RobotUsbManagerEmulator();
                break;
            }
            default: {
                this.b = new RobotUsbManagerFtdi(context);
            }
        }
    }
    
    public Map<SerialNumber, DeviceManager.DeviceType> scanForUsbDevices() throws RobotCoreException {
        final HashMap<SerialNumber, DeviceManager.DeviceType> hashMap = new HashMap<SerialNumber, DeviceManager.DeviceType>();
        try {
            for (int scanForDevices = this.b.scanForDevices(), i = 0; i < scanForDevices; ++i) {
                final SerialNumber deviceSerialNumberByIndex = this.b.getDeviceSerialNumberByIndex(i);
                final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, deviceSerialNumberByIndex);
                hashMap.put(deviceSerialNumberByIndex, ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)));
                openUsbDevice.close();
            }
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Error while scanning for USB devices", ex);
        }
        return hashMap;
    }
    
    public DcMotorController createUsbDcMotorController(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB DC Motor Controller - " + serialNumber.toString());
        Object o = null;
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
                this.a(openUsbDevice, "Modern Robotics USB DC Motor Controller", serialNumber);
            }
            o = new ModernRoboticsUsbDcMotorController(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB DC Motor Controller", ex);
        }
        return (DcMotorController)o;
    }
    
    public ServoController createUsbServoController(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Servo Controller - " + serialNumber.toString());
        Object o = null;
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
                this.a(openUsbDevice, "Modern Robotics USB Servo Controller", serialNumber);
            }
            o = new ModernRoboticsUsbServoController(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Servo Controller", ex);
        }
        return (ServoController)o;
    }
    
    public DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Core Device Interface Module - " + serialNumber.toString());
        Object o = null;
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
                this.a(openUsbDevice, "Modern Robotics USB Core Device Interface Module", serialNumber);
            }
            o = new ModernRoboticsUsbDeviceInterfaceModule(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Core Device Interface Module", ex);
        }
        return (DeviceInterfaceModule)o;
    }
    
    public LegacyModule createUsbLegacyModule(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Legacy Module - " + serialNumber.toString());
        Object o = null;
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
                this.a(openUsbDevice, "Modern Robotics USB Legacy Module", serialNumber);
            }
            o = new ModernRoboticsUsbLegacyModule(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Legacy Module", ex);
        }
        return (LegacyModule)o;
    }
    
    public DcMotorController createNxtDcMotorController(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT DC Motor Controller - Port: " + physicalPort);
        return (DcMotorController)new HiTechnicNxtDcMotorController(this.a(legacyModule), physicalPort);
    }
    
    public ServoController createNxtServoController(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Servo Controller - Port: " + physicalPort);
        return (ServoController)new HiTechnicNxtServoController(this.a(legacyModule), physicalPort);
    }
    
    public CompassSensor createNxtCompassSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Compass Sensor - Port: " + physicalPort);
        return new HiTechnicNxtCompassSensor(this.a(legacyModule), physicalPort);
    }
    
    public TouchSensor createDigitalTouchSensor(final DeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        RobotLog.v("Creating Modern Robotics Digital Touch Sensor - Port: " + physicalPort);
        return new ModernRoboticsDigitalTouchSensor((DeviceInterfaceModule)this.a(deviceInterfaceModule), physicalPort);
    }
    
    public AccelerationSensor createNxtAccelerationSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Acceleration Sensor - Port: " + physicalPort);
        return new HiTechnicNxtAccelerationSensor(this.a(legacyModule), physicalPort);
    }
    
    public LightSensor createNxtLightSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Light Sensor - Port: " + physicalPort);
        return new HiTechnicNxtLightSensor(this.a(legacyModule), physicalPort);
    }
    
    public GyroSensor createNxtGyroSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Gyro Sensor - Port: " + physicalPort);
        return new HiTechnicNxtGyroSensor(this.a(legacyModule), physicalPort);
    }
    
    public IrSeekerSensor createNxtIrSeekerSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT IR Seeker Sensor - Port: " + physicalPort);
        return new HiTechnicNxtIrSeekerSensor(this.a(legacyModule), physicalPort);
    }
    
    public IrSeekerSensor createI2cIrSeekerSensorV3(final DeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - Port: " + physicalPort);
        return new ModernRoboticsI2cIrSeekerSensorV3((DeviceInterfaceModule)this.a(deviceInterfaceModule), physicalPort);
    }
    
    public UltrasonicSensor createNxtUltrasonicSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Ultrasonic Sensor - Port: " + physicalPort);
        return new HiTechnicNxtUltrasonicSensor(this.a(legacyModule), physicalPort);
    }
    
    public OpticalDistanceSensor createAnalogOpticalDistanceSensor(final DeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        RobotLog.v("Creating Modern Robotics Analog Optical Distance Sensor - Port: " + physicalPort);
        return new ModernRoboticsAnalogOpticalDistanceSensor(this.a(deviceInterfaceModule), physicalPort);
    }
    
    public TouchSensor createNxtTouchSensor(final LegacyModule legacyModule, final int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Touch Sensor - Port: " + physicalPort);
        return new HiTechnicNxtTouchSensor(this.a(legacyModule), physicalPort);
    }
    
    public TouchSensorMultiplexer createNxtTouchSensorMultiplexer(final LegacyModule legacyModule, final int port) {
        RobotLog.v("Creating HiTechnic NXT Touch Sensor Multiplexer - Port: " + port);
        return new HiTechnicNxtTouchSensorMultiplexer(this.a(legacyModule), port);
    }
    
    public AnalogInput createAnalogInputDevice(final AnalogInputController controller, final int channel) {
        RobotLog.v("Creating Analog Input Device - Port: " + channel);
        return new AnalogInput(controller, channel);
    }
    
    public AnalogOutput createAnalogOutputDevice(final AnalogOutputController controller, final int channel) {
        RobotLog.v("Creating Analog Output Device - Port: " + channel);
        return new AnalogOutput(controller, channel);
    }
    
    public DigitalChannel createDigitalChannelDevice(final DigitalChannelController controller, final int channel) {
        RobotLog.v("Creating Digital Channel Device - Port: " + channel);
        return new DigitalChannel(controller, channel);
    }
    
    public PWMOutput createPwmOutputDevice(final DeviceInterfaceModule controller, final int channel) {
        RobotLog.v("Creating PWM Output Device - Port: " + channel);
        return new PWMOutput((PWMOutputController)controller, channel);
    }
    
    public I2cDevice createI2cDevice(final I2cController controller, final int channel) {
        RobotLog.v("Creating I2C Device - Port: " + channel);
        return new I2cDevice(controller, channel);
    }
    
    public ColorSensor createAdafruitI2cColorSensor(final DeviceInterfaceModule controller, final int channel) {
        RobotLog.v("Creating Adafruit I2C Color Sensor - Port: " + channel);
        return new AdafruitI2cColorSensor(controller, channel);
    }
    
    public ColorSensor createNxtColorSensor(final LegacyModule controller, final int channel) {
        RobotLog.v("Creating HiTechnic NXT Color Sensor - Port: " + channel);
        return new HiTechnicNxtColorSensor(controller, channel);
    }
    
    public ColorSensor createModernRoboticsI2cColorSensor(final DeviceInterfaceModule controller, final int channel) {
        RobotLog.v("Creating Modern Robotics I2C Color Sensor - Port: " + channel);
        return new ModernRoboticsI2cColorSensor(controller, channel);
    }
    
    public GyroSensor createModernRoboticsI2cGyroSensor(final DeviceInterfaceModule controller, final int channel) {
        RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - Port: " + channel);
        return new ModernRoboticsI2cGyro(controller, channel);
    }
    
    public LED createLED(final DigitalChannelController controller, final int channel) {
        RobotLog.v("Creating LED - Port: " + channel);
        return new LED(controller, channel);
    }
    
    public static void enableDeviceEmulation() {
        HardwareDeviceManager.a = HardwareDeviceManager.a.b;
    }
    
    public static void disableDeviceEmulation() {
        HardwareDeviceManager.a = HardwareDeviceManager.a.a;
    }
    
    private ModernRoboticsUsbLegacyModule a(final LegacyModule legacyModule) {
        if (!(legacyModule instanceof ModernRoboticsUsbLegacyModule)) {
            throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
        }
        return (ModernRoboticsUsbLegacyModule)legacyModule;
    }
    
    private ModernRoboticsUsbDeviceInterfaceModule a(final DeviceInterfaceModule deviceInterfaceModule) {
        if (!(deviceInterfaceModule instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
            throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
        }
        return (ModernRoboticsUsbDeviceInterfaceModule)deviceInterfaceModule;
    }
    
    private void a(final RobotUsbDevice robotUsbDevice, final String s, final SerialNumber serialNumber) throws RobotCoreException {
        final String string = s + " [" + serialNumber + "] is returning garbage data via the USB bus";
        robotUsbDevice.close();
        this.a(string);
    }
    
    private void a(final String s) throws RobotCoreException {
        System.err.println(s);
        throw new RobotCoreException(s);
    }
    
    static {
        HardwareDeviceManager.a = HardwareDeviceManager.a.a;
    }
    
    private enum a
    {
        a, 
        b;
    }
}
