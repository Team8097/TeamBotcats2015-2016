// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;

public abstract class DeviceManager
{
    public abstract Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException;
    
    public abstract DcMotorController createUsbDcMotorController(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public DcMotor createDcMotor(final DcMotorController controller, final int portNumber) {
        return new DcMotor(controller, portNumber, DcMotor.Direction.FORWARD);
    }
    
    public abstract ServoController createUsbServoController(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public Servo createServo(final ServoController controller, final int portNumber) {
        return new Servo(controller, portNumber, Servo.Direction.FORWARD);
    }
    
    public abstract LegacyModule createUsbLegacyModule(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public abstract DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public abstract TouchSensor createNxtTouchSensor(final LegacyModule p0, final int p1);
    
    public abstract TouchSensorMultiplexer createNxtTouchSensorMultiplexer(final LegacyModule p0, final int p1);
    
    public abstract AnalogInput createAnalogInputDevice(final AnalogInputController p0, final int p1);
    
    public abstract AnalogOutput createAnalogOutputDevice(final AnalogOutputController p0, final int p1);
    
    public abstract DigitalChannel createDigitalChannelDevice(final DigitalChannelController p0, final int p1);
    
    public abstract PWMOutput createPwmOutputDevice(final DeviceInterfaceModule p0, final int p1);
    
    public abstract I2cDevice createI2cDevice(final I2cController p0, final int p1);
    
    public abstract DcMotorController createNxtDcMotorController(final LegacyModule p0, final int p1);
    
    public abstract ServoController createNxtServoController(final LegacyModule p0, final int p1);
    
    public abstract CompassSensor createNxtCompassSensor(final LegacyModule p0, final int p1);
    
    public abstract TouchSensor createDigitalTouchSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract AccelerationSensor createNxtAccelerationSensor(final LegacyModule p0, final int p1);
    
    public abstract LightSensor createNxtLightSensor(final LegacyModule p0, final int p1);
    
    public abstract IrSeekerSensor createNxtIrSeekerSensor(final LegacyModule p0, final int p1);
    
    public abstract IrSeekerSensor createI2cIrSeekerSensorV3(final DeviceInterfaceModule p0, final int p1);
    
    public abstract UltrasonicSensor createNxtUltrasonicSensor(final LegacyModule p0, final int p1);
    
    public abstract GyroSensor createNxtGyroSensor(final LegacyModule p0, final int p1);
    
    public abstract GyroSensor createModernRoboticsI2cGyroSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract OpticalDistanceSensor createAnalogOpticalDistanceSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract ColorSensor createAdafruitI2cColorSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract ColorSensor createNxtColorSensor(final LegacyModule p0, final int p1);
    
    public abstract ColorSensor createModernRoboticsI2cColorSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract LED createLED(final DigitalChannelController p0, final int p1);
    
    public enum DeviceType
    {
        FTDI_USB_UNKNOWN_DEVICE, 
        MODERN_ROBOTICS_USB_UNKNOWN_DEVICE, 
        MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, 
        MODERN_ROBOTICS_USB_SERVO_CONTROLLER, 
        MODERN_ROBOTICS_USB_LEGACY_MODULE, 
        MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, 
        MODERN_ROBOTICS_USB_SENSOR_MUX;
    }
}
