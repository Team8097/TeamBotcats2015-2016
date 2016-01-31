// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.factory;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robot.Robot;

public class RobotFactory
{
    public static Robot createRobot() throws RobotCoreException {
        final RobocolDatagramSocket robocolDatagramSocket = new RobocolDatagramSocket();
        final EventLoopManager eventLoopManager = new EventLoopManager(robocolDatagramSocket);
        final Robot robot = new Robot();
        robot.eventLoopManager = eventLoopManager;
        robot.socket = robocolDatagramSocket;
        return robot;
    }
}
