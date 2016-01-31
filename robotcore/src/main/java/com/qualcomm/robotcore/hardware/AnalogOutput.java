// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class AnalogOutput implements HardwareDevice
{
    private AnalogOutputController a;
    private int b;
    
    public AnalogOutput(final AnalogOutputController controller, final int channel) {
        this.a = null;
        this.b = -1;
        this.a = controller;
        this.b = channel;
    }
    
    public void setAnalogOutputVoltage(final int voltage) {
        this.a.setAnalogOutputVoltage(this.b, voltage);
    }
    
    public void setAnalogOutputFrequency(final int freq) {
        this.a.setAnalogOutputFrequency(this.b, freq);
    }
    
    public void setAnalogOutputMode(final byte mode) {
        this.a.setAnalogOutputMode(this.b, mode);
    }
    
    @Override
    public String getDeviceName() {
        return "Analog Output";
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; analog port " + this.b;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void close() {
    }
}
