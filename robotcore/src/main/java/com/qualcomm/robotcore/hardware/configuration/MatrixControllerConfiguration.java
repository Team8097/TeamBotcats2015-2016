// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import java.util.ArrayList;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class MatrixControllerConfiguration extends ControllerConfiguration
{
    private List<DeviceConfiguration> a;
    private List<DeviceConfiguration> b;
    
    public MatrixControllerConfiguration(final String name, final List<DeviceConfiguration> motors, final List<DeviceConfiguration> servos, final SerialNumber serialNumber) {
        super(name, serialNumber, ConfigurationType.MATRIX_CONTROLLER);
        this.a = servos;
        this.b = motors;
    }
    
    public List<DeviceConfiguration> getServos() {
        return this.a;
    }
    
    public void addServos(final ArrayList<DeviceConfiguration> servos) {
        this.a = servos;
    }
    
    public List<DeviceConfiguration> getMotors() {
        return this.b;
    }
    
    public void addMotors(final ArrayList<DeviceConfiguration> motors) {
        this.b = motors;
    }
}
