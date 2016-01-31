// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public interface HardwareDevice
{
    String getDeviceName();
    
    String getConnectionInfo();
    
    int getVersion();
    
    void close();
}
