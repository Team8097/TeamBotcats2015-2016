// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class DeviceInterfaceModuleConfiguration extends ControllerConfiguration
{
    private List<DeviceConfiguration> a;
    private List<DeviceConfiguration> b;
    private List<DeviceConfiguration> c;
    private List<DeviceConfiguration> d;
    private List<DeviceConfiguration> e;
    
    public DeviceInterfaceModuleConfiguration(final String name, final SerialNumber serialNumber) {
        super(name, serialNumber, ConfigurationType.DEVICE_INTERFACE_MODULE);
    }
    
    public void setPwmDevices(final List<DeviceConfiguration> pwmDevices) {
        this.a = pwmDevices;
    }
    
    public List<DeviceConfiguration> getPwmDevices() {
        return this.a;
    }
    
    public List<DeviceConfiguration> getI2cDevices() {
        return this.b;
    }
    
    public void setI2cDevices(final List<DeviceConfiguration> i2cDevices) {
        this.b = i2cDevices;
    }
    
    public List<DeviceConfiguration> getAnalogInputDevices() {
        return this.c;
    }
    
    public void setAnalogInputDevices(final List<DeviceConfiguration> analogInputDevices) {
        this.c = analogInputDevices;
    }
    
    public List<DeviceConfiguration> getDigitalDevices() {
        return this.d;
    }
    
    public void setDigitalDevices(final List<DeviceConfiguration> digitalDevices) {
        this.d = digitalDevices;
    }
    
    public List<DeviceConfiguration> getAnalogOutputDevices() {
        return this.e;
    }
    
    public void setAnalogOutputDevices(final List<DeviceConfiguration> analogOutputDevices) {
        this.e = analogOutputDevices;
    }
}
