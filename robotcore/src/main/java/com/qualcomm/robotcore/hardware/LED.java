// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class LED implements HardwareDevice
{
    private DigitalChannelController a;
    private int b;
    
    public LED(final DigitalChannelController controller, final int physicalPort) {
        this.a = null;
        this.b = -1;
        (this.a = controller).setDigitalChannelMode(this.b = physicalPort, DigitalChannelController.Mode.OUTPUT);
    }
    
    public void enable(final boolean set) {
        this.a.setDigitalChannelState(this.b, set);
    }
    
    @Override
    public String getDeviceName() {
        return null;
    }
    
    @Override
    public String getConnectionInfo() {
        return null;
    }
    
    @Override
    public int getVersion() {
        return 0;
    }
    
    @Override
    public void close() {
    }
}
