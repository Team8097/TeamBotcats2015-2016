// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.exception.RobotCoreException;

public interface EventLoop
{
    void init(final EventLoopManager p0) throws RobotCoreException, InterruptedException;
    
    void loop() throws RobotCoreException, InterruptedException;
    
    void teardown() throws RobotCoreException, InterruptedException;
    
    void processCommand(final Command p0);
    
    OpModeManager getOpModeManager();
}
