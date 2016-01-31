// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class PWMOutput implements HardwareDevice
{
    private PWMOutputController a;
    private int b;
    
    public PWMOutput(final PWMOutputController controller, final int port) {
        this.a = null;
        this.b = -1;
        this.a = controller;
        this.b = port;
    }
    
    public void setPulseWidthOutputTime(final int time) {
        this.a.setPulseWidthOutputTime(this.b, time);
    }
    
    public int getPulseWidthOutputTime() {
        return this.a.getPulseWidthOutputTime(this.b);
    }
    
    public void setPulseWidthPeriod(final int period) {
        this.a.setPulseWidthPeriod(this.b, period);
    }
    
    public int getPulseWidthPeriod() {
        return this.a.getPulseWidthPeriod(this.b);
    }
    
    @Override
    public String getDeviceName() {
        return "PWM Output";
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void close() {
    }
}
