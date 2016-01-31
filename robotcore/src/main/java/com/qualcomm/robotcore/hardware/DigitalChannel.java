// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class DigitalChannel implements HardwareDevice
{
    private DigitalChannelController a;
    private int b;
    
    public DigitalChannel(final DigitalChannelController controller, final int channel) {
        this.a = null;
        this.b = -1;
        this.a = controller;
        this.b = channel;
    }
    
    public DigitalChannelController.Mode getMode() {
        return this.a.getDigitalChannelMode(this.b);
    }
    
    public void setMode(final DigitalChannelController.Mode mode) {
        this.a.setDigitalChannelMode(this.b, mode);
    }
    
    public boolean getState() {
        return this.a.getDigitalChannelState(this.b);
    }
    
    public void setState(final boolean state) {
        this.a.setDigitalChannelState(this.b, state);
    }
    
    @Override
    public String getDeviceName() {
        return "Digital Channel";
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; digital port " + this.b;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void close() {
    }
}
