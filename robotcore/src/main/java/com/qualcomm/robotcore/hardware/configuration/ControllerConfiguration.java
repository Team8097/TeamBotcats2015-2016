// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;
import java.util.ArrayList;
import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;

public class ControllerConfiguration extends DeviceConfiguration implements Serializable
{
    public static final SerialNumber NO_SERIAL_NUMBER;
    private List<DeviceConfiguration> a;
    private SerialNumber b;
    
    public ControllerConfiguration(final String name, final SerialNumber serialNumber, final ConfigurationType type) {
        this(name, new ArrayList<DeviceConfiguration>(), serialNumber, type);
    }
    
    public ControllerConfiguration(final String name, final List<DeviceConfiguration> devices, final SerialNumber serialNumber, final ConfigurationType type) {
        super(type);
        super.setName(name);
        this.a = devices;
        this.b = serialNumber;
    }
    
    public List<DeviceConfiguration> getDevices() {
        return this.a;
    }
    
    @Override
    public ConfigurationType getType() {
        return super.getType();
    }
    
    public SerialNumber getSerialNumber() {
        return this.b;
    }
    
    public void addDevices(final List<DeviceConfiguration> devices) {
        this.a = devices;
    }
    
    public ConfigurationType deviceTypeToConfigType(final DeviceManager.DeviceType type) {
        if (type == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
            return ConfigurationType.MOTOR_CONTROLLER;
        }
        if (type == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
            return ConfigurationType.SERVO_CONTROLLER;
        }
        if (type == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
            return ConfigurationType.LEGACY_MODULE_CONTROLLER;
        }
        return ConfigurationType.NOTHING;
    }
    
    public DeviceManager.DeviceType configTypeToDeviceType(final ConfigurationType type) {
        if (type == ConfigurationType.MOTOR_CONTROLLER) {
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
        }
        if (type == ConfigurationType.SERVO_CONTROLLER) {
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
        }
        if (type == ConfigurationType.LEGACY_MODULE_CONTROLLER) {
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
        }
        return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
    }
    
    static {
        NO_SERIAL_NUMBER = new SerialNumber("-1");
    }
}
