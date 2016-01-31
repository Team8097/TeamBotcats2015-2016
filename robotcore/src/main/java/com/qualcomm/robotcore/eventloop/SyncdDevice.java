// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface SyncdDevice
{
    void blockUntilReady() throws RobotCoreException, InterruptedException;
    
    void startBlockingWork();
}
