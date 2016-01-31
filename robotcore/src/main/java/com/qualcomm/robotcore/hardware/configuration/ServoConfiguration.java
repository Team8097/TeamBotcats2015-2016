// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

public class ServoConfiguration extends DeviceConfiguration
{
    public ServoConfiguration(final int port, final String name, final boolean enabled) {
        super(port, ConfigurationType.SERVO, name, enabled);
    }
    
    public ServoConfiguration(final int port) {
        super(port, ConfigurationType.SERVO, "NO DEVICE ATTACHED", false);
    }
    
    public ServoConfiguration(final String name) {
        super(ConfigurationType.SERVO);
        super.setName(name);
        super.setType(ConfigurationType.SERVO);
    }
}
