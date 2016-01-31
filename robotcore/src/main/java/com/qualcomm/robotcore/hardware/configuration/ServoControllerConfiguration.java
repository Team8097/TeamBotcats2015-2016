// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;

public class ServoControllerConfiguration extends ControllerConfiguration
{
    public ServoControllerConfiguration() {
        super("", new ArrayList<DeviceConfiguration>(), new SerialNumber(ControllerConfiguration.NO_SERIAL_NUMBER.getSerialNumber()), ConfigurationType.SERVO_CONTROLLER);
    }
    
    public ServoControllerConfiguration(final String name, final List<DeviceConfiguration> servos, final SerialNumber serialNumber) {
        super(name, servos, serialNumber, ConfigurationType.SERVO_CONTROLLER);
    }
    
    public List<DeviceConfiguration> getServos() {
        return super.getDevices();
    }
    
    public void addServos(final ArrayList<DeviceConfiguration> servos) {
        super.addDevices(servos);
    }
}
