// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.io.Serializable;

public class MotorControllerConfiguration extends ControllerConfiguration implements Serializable
{
    public MotorControllerConfiguration() {
        super("", new ArrayList<DeviceConfiguration>(), new SerialNumber(ControllerConfiguration.NO_SERIAL_NUMBER.getSerialNumber()), ConfigurationType.MOTOR_CONTROLLER);
    }
    
    public MotorControllerConfiguration(final String name, final List<DeviceConfiguration> motors, final SerialNumber serialNumber) {
        super(name, motors, serialNumber, ConfigurationType.MOTOR_CONTROLLER);
    }
    
    public List<DeviceConfiguration> getMotors() {
        return super.getDevices();
    }
    
    public void addMotors(final List<DeviceConfiguration> motors) {
        super.addDevices(motors);
    }
}
