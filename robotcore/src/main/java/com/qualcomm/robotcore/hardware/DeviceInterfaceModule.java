// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public interface DeviceInterfaceModule extends AnalogInputController, AnalogOutputController, DigitalChannelController, I2cController, PWMOutputController
{
    int getDigitalInputStateByte();
    
    void setDigitalIOControlByte(final byte p0);
    
    byte getDigitalIOControlByte();
    
    void setDigitalOutputByte(final byte p0);
    
    byte getDigitalOutputStateByte();
    
    boolean getLEDState(final int p0);
    
    void setLED(final int p0, final boolean p1);
}
