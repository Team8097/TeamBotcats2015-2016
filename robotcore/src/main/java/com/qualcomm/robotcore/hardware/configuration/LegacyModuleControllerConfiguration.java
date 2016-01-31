// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class LegacyModuleControllerConfiguration extends ControllerConfiguration
{
    public LegacyModuleControllerConfiguration(final String name, final List<DeviceConfiguration> modules, final SerialNumber serialNumber) {
        super(name, modules, serialNumber, ConfigurationType.LEGACY_MODULE_CONTROLLER);
    }
}
