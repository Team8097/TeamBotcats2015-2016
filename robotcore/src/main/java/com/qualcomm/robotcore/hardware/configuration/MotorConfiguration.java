// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

public class MotorConfiguration extends DeviceConfiguration
{
    public MotorConfiguration(final int port, final String name, final boolean enabled) {
        super(port, ConfigurationType.MOTOR, name, enabled);
    }
    
    public MotorConfiguration(final int port) {
        super(ConfigurationType.MOTOR);
        super.setName("NO DEVICE ATTACHED");
        super.setPort(port);
    }
    
    public MotorConfiguration(final String name) {
        super(ConfigurationType.MOTOR);
        super.setName(name);
        super.setType(ConfigurationType.MOTOR);
    }
}
