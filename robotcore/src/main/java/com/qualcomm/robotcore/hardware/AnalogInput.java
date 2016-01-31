// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class AnalogInput implements HardwareDevice
{
    private AnalogInputController a;
    private int b;
    
    public AnalogInput(final AnalogInputController controller, final int channel) {
        this.a = null;
        this.b = -1;
        this.a = controller;
        this.b = channel;
    }
    
    public int getValue() {
        return this.a.getAnalogInputValue(this.b);
    }
    
    @Override
    public String getDeviceName() {
        return "Analog Input";
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
