// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robot;

import java.net.SocketException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.eventloop.EventLoop;
import java.net.InetAddress;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import java.util.concurrent.ArrayBlockingQueue;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.eventloop.EventLoopManager;

public class Robot
{
    public EventLoopManager eventLoopManager;
    public RobocolDatagramSocket socket;
    public ArrayBlockingQueue<RobocolDatagram> sendQueue;
    public ArrayBlockingQueue<RobocolDatagram> eventQueue;
    
    public Robot() {
        this.eventLoopManager = null;
        this.socket = null;
        this.sendQueue = null;
        this.eventQueue = null;
    }
    
    public void start(final InetAddress driverStationAddr, final EventLoop eventLoop) throws RobotCoreException {
        try {
            this.socket.listen(driverStationAddr);
            this.eventLoopManager.start(eventLoop);
        }
        catch (SocketException e) {
            RobotLog.logStacktrace(e);
            throw new RobotCoreException("Robot start failed: " + e.toString());
        }
    }
    
    public void shutdown() {
        if (this.eventLoopManager != null) {
            this.eventLoopManager.shutdown();
        }
        if (this.socket != null) {
            this.socket.close();
        }
    }
}
