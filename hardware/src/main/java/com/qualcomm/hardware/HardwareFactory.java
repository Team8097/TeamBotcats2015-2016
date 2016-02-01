// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

import java.util.List;

import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.EventLoopManager;

import java.io.InputStream;

import android.content.Context;

public class HardwareFactory {
    private Context a;
    private InputStream b;

    public HardwareFactory(final Context context) {
        this.b = null;
        this.a = context;
    }

    public HardwareMap createHardwareMap(final EventLoopManager manager) throws RobotCoreException, InterruptedException {
        if (this.b == null) {
            throw new RobotCoreException("XML input stream is null, HardwareFactory cannot create a hardware map");
        }
        final HardwareMap hardwareMap = new HardwareMap();
        RobotLog.v("Starting Modern Robotics device manager");
        final HardwareDeviceManager hardwareDeviceManager = new HardwareDeviceManager(this.a, manager);
        for (final ControllerConfiguration controllerConfiguration : new ReadXMLFileHandler(this.a).parse(this.b)) {
            final DeviceConfiguration.ConfigurationType type = controllerConfiguration.getType();
            switch (type) {
                case MOTOR_CONTROLLER: {
                    this.a(hardwareMap, hardwareDeviceManager, controllerConfiguration);
                    continue;
                }
                case SERVO_CONTROLLER: {
                    this.b(hardwareMap, hardwareDeviceManager, controllerConfiguration);
                    continue;
                }
                case LEGACY_MODULE_CONTROLLER: {
                    this.d(hardwareMap, hardwareDeviceManager, controllerConfiguration);
                    continue;
                }
                case DEVICE_INTERFACE_MODULE: {
                    this.c(hardwareMap, hardwareDeviceManager, controllerConfiguration);
                    continue;
                }
                default: {
                    RobotLog.w("Unexpected controller type while parsing XML: " + type.toString());
                    continue;
                }
            }
        }
        hardwareMap.appContext = this.a;
        return hardwareMap;
    }

    public void setXmlInputStream(final InputStream xmlInputStream) {
        this.b = xmlInputStream;
    }

    public InputStream getXmlInputStream() {
        return this.b;
    }

    public static void enableDeviceEmulation() {
        HardwareDeviceManager.enableDeviceEmulation();
    }

    public static void disableDeviceEmulation() {
        HardwareDeviceManager.disableDeviceEmulation();
    }

    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final ModernRoboticsUsbDcMotorController modernRoboticsUsbDcMotorController = (ModernRoboticsUsbDcMotorController) deviceManager.createUsbDcMotorController(controllerConfiguration.getSerialNumber());
        hardwareMap.dcMotorController.put(controllerConfiguration.getName(), modernRoboticsUsbDcMotorController);
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                hardwareMap.dcMotor.put(deviceConfiguration.getName(), deviceManager.createDcMotor((DcMotorController) modernRoboticsUsbDcMotorController, deviceConfiguration.getPort()));
            }
        }
        hardwareMap.voltageSensor.put(controllerConfiguration.getName(), modernRoboticsUsbDcMotorController);
    }

    private void b(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final ServoController usbServoController = deviceManager.createUsbServoController(controllerConfiguration.getSerialNumber());
        hardwareMap.servoController.put(controllerConfiguration.getName(), usbServoController);
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                hardwareMap.servo.put(deviceConfiguration.getName(), deviceManager.createServo(usbServoController, deviceConfiguration.getPort()));
            }
        }
    }

    private void c(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final DeviceInterfaceModule deviceInterfaceModule = deviceManager.createDeviceInterfaceModule(controllerConfiguration.getSerialNumber());
        hardwareMap.deviceInterfaceModule.put(controllerConfiguration.getName(), deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration) controllerConfiguration).getPwmDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration) controllerConfiguration).getI2cDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration) controllerConfiguration).getAnalogInputDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration) controllerConfiguration).getDigitalDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration) controllerConfiguration).getAnalogOutputDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
    }

    private void a(final List<DeviceConfiguration> list, final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule) {
        for (final DeviceConfiguration deviceConfiguration : list) {
            if (deviceConfiguration.isEnabled()) {
                final DeviceConfiguration.ConfigurationType type = deviceConfiguration.getType();
                switch (type) {
                    case OPTICAL_DISTANCE_SENSOR: {
                        this.h(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case ANALOG_INPUT: {
                        this.d(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case TOUCH_SENSOR: {
                        this.c(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case DIGITAL_DEVICE: {
                        this.b(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case PULSE_WIDTH_DEVICE: {
                        this.e(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case IR_SEEKER_V3: {
                        this.a(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case I2C_DEVICE: {
                        this.f(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case ANALOG_OUTPUT: {
                        this.g(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case ADAFRUIT_COLOR_SENSOR: {
                        this.i(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case LED: {
                        this.a(hardwareMap, deviceManager, (DigitalChannelController) deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case COLOR_SENSOR: {
                        this.j(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case GYRO: {
                        this.k(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case NOTHING: {
                        continue;
                    }
                    default: {
                        RobotLog.w("Unexpected device type connected to Device Interface Module while parsing XML: " + type.toString());
                        continue;
                    }
                }
            }
        }
    }

    private void d(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final LegacyModule usbLegacyModule = deviceManager.createUsbLegacyModule(controllerConfiguration.getSerialNumber());
        hardwareMap.legacyModule.put(controllerConfiguration.getName(), usbLegacyModule);
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                final DeviceConfiguration.ConfigurationType type = deviceConfiguration.getType();
                switch (type) {
                    case GYRO: {
                        this.e(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case COMPASS: {
                        this.f(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case IR_SEEKER: {
                        this.g(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case LIGHT_SENSOR: {
                        this.h(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case ACCELEROMETER: {
                        this.i(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case MOTOR_CONTROLLER: {
                        this.j(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case SERVO_CONTROLLER: {
                        this.k(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case TOUCH_SENSOR: {
                        this.a(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case TOUCH_SENSOR_MULTIPLEXER: {
                        this.b(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case ULTRASONIC_SENSOR: {
                        this.c(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case COLOR_SENSOR: {
                        this.d(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case MATRIX_CONTROLLER: {
                        this.l(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case NOTHING: {
                        continue;
                    }
                    default: {
                        RobotLog.w("Unexpected device type connected to Legacy Module while parsing XML: " + type.toString());
                        continue;
                    }
                }
            }
        }
    }

    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.irSeekerSensor.put(deviceConfiguration.getName(), deviceManager.createI2cIrSeekerSensorV3(deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void b(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.digitalChannel.put(deviceConfiguration.getName(), deviceManager.createDigitalChannelDevice((DigitalChannelController) deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void c(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.touchSensor.put(deviceConfiguration.getName(), deviceManager.createDigitalTouchSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void d(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.analogInput.put(deviceConfiguration.getName(), deviceManager.createAnalogInputDevice((AnalogInputController) deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void e(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.pwmOutput.put(deviceConfiguration.getName(), deviceManager.createPwmOutputDevice(deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void f(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.i2cDevice.put(deviceConfiguration.getName(), deviceManager.createI2cDevice((I2cController) deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void g(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.analogOutput.put(deviceConfiguration.getName(), deviceManager.createAnalogOutputDevice((AnalogOutputController) deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void h(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.opticalDistanceSensor.put(deviceConfiguration.getName(), deviceManager.createAnalogOpticalDistanceSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.touchSensor.put(deviceConfiguration.getName(), deviceManager.createNxtTouchSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void b(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.touchSensorMultiplexer.put(deviceConfiguration.getName(), deviceManager.createNxtTouchSensorMultiplexer(legacyModule, deviceConfiguration.getPort()));
    }

    private void c(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.ultrasonicSensor.put(deviceConfiguration.getName(), deviceManager.createNxtUltrasonicSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void d(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.colorSensor.put(deviceConfiguration.getName(), deviceManager.createNxtColorSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void e(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.gyroSensor.put(deviceConfiguration.getName(), deviceManager.createNxtGyroSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void f(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.compassSensor.put(deviceConfiguration.getName(), deviceManager.createNxtCompassSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void g(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.irSeekerSensor.put(deviceConfiguration.getName(), deviceManager.createNxtIrSeekerSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void h(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.lightSensor.put(deviceConfiguration.getName(), deviceManager.createNxtLightSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void i(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.accelerationSensor.put(deviceConfiguration.getName(), deviceManager.createNxtAccelerationSensor(legacyModule, deviceConfiguration.getPort()));
    }

    private void j(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        final DcMotorController nxtDcMotorController = deviceManager.createNxtDcMotorController(legacyModule, deviceConfiguration.getPort());
        hardwareMap.dcMotorController.put(deviceConfiguration.getName(), nxtDcMotorController);
        for (final DeviceConfiguration deviceConfiguration2 : ((MotorControllerConfiguration) deviceConfiguration).getMotors()) {
            hardwareMap.dcMotor.put(deviceConfiguration2.getName(), deviceManager.createDcMotor(nxtDcMotorController, deviceConfiguration2.getPort()));
        }
    }

    private void k(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        final ServoController nxtServoController = deviceManager.createNxtServoController(legacyModule, deviceConfiguration.getPort());
        hardwareMap.servoController.put(deviceConfiguration.getName(), nxtServoController);
        for (final DeviceConfiguration deviceConfiguration2 : ((ServoControllerConfiguration) deviceConfiguration).getServos()) {
            hardwareMap.servo.put(deviceConfiguration2.getName(), deviceManager.createServo(nxtServoController, deviceConfiguration2.getPort()));
        }
    }

    private void l(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        final MatrixMasterController matrixMasterController = new MatrixMasterController((ModernRoboticsUsbLegacyModule) legacyModule, deviceConfiguration.getPort());
        final MatrixDcMotorController matrixDcMotorController = new MatrixDcMotorController(matrixMasterController);
        hardwareMap.dcMotorController.put(deviceConfiguration.getName() + "Motor", matrixDcMotorController);
        hardwareMap.dcMotorController.put(deviceConfiguration.getName(), matrixDcMotorController);
        for (final DeviceConfiguration deviceConfiguration2 : ((MatrixControllerConfiguration) deviceConfiguration).getMotors()) {
            hardwareMap.dcMotor.put(deviceConfiguration2.getName(), deviceManager.createDcMotor((DcMotorController) matrixDcMotorController, deviceConfiguration2.getPort()));
        }
        final MatrixServoController matrixServoController = new MatrixServoController(matrixMasterController);
        hardwareMap.servoController.put(deviceConfiguration.getName() + "Servo", matrixServoController);
        hardwareMap.servoController.put(deviceConfiguration.getName(), matrixServoController);
        for (final DeviceConfiguration deviceConfiguration3 : ((MatrixControllerConfiguration) deviceConfiguration).getServos()) {
            hardwareMap.servo.put(deviceConfiguration3.getName(), deviceManager.createServo((ServoController) matrixServoController, deviceConfiguration3.getPort()));
        }
    }

    private void i(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.colorSensor.put(deviceConfiguration.getName(), deviceManager.createAdafruitI2cColorSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DigitalChannelController digitalChannelController, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.led.put(deviceConfiguration.getName(), deviceManager.createLED(digitalChannelController, deviceConfiguration.getPort()));
    }

    private void j(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.colorSensor.put(deviceConfiguration.getName(), deviceManager.createModernRoboticsI2cColorSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }

    private void k(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.gyroSensor.put(deviceConfiguration.getName(), deviceManager.createModernRoboticsI2cGyroSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
}
