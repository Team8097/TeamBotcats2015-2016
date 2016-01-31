// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import java.net.PortUnreachableException;
import java.io.IOException;
import java.net.SocketAddress;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.SocketException;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class RobocolDatagramSocket
{
    private final byte[] a;
    private DatagramSocket b;
    private final DatagramPacket c;
    private final RobocolDatagram d;
    private volatile State e;
    
    public RobocolDatagramSocket() {
        this.a = new byte[4098];
        this.c = new DatagramPacket(this.a, this.a.length);
        this.d = new RobocolDatagram();
        this.e = State.CLOSED;
    }
    
    public void listen(final InetAddress destAddress) throws SocketException {
        this.bind(new InetSocketAddress(RobocolConfig.determineBindAddress(destAddress), 20884));
    }
    
    public void bind(final InetSocketAddress bindAddress) throws SocketException {
        if (this.e != State.CLOSED) {
            this.close();
        }
        this.e = State.LISTENING;
        RobotLog.d("RobocolDatagramSocket binding to " + bindAddress.toString());
        this.b = new DatagramSocket(bindAddress);
    }
    
    public void connect(final InetAddress connectAddress) throws SocketException {
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(connectAddress, 20884);
        RobotLog.d("RobocolDatagramSocket connected to " + inetSocketAddress.toString());
        this.b.connect(inetSocketAddress);
    }
    
    public void close() {
        this.e = State.CLOSED;
        if (this.b != null) {
            this.b.close();
        }
        RobotLog.d("RobocolDatagramSocket is closed");
    }
    
    public void send(final RobocolDatagram message) {
        try {
            this.b.send(message.getPacket());
        }
        catch (IllegalArgumentException ex) {
            RobotLog.w("Unable to send RobocolDatagram: " + ex.toString());
            RobotLog.w("               " + message.toString());
        }
        catch (IOException ex2) {
            RobotLog.w("Unable to send RobocolDatagram: " + ex2.toString());
            RobotLog.w("               " + message.toString());
        }
        catch (NullPointerException ex3) {
            RobotLog.w("Unable to send RobocolDatagram: " + ex3.toString());
            RobotLog.w("               " + message.toString());
        }
    }
    
    public RobocolDatagram recv() {
        try {
            this.b.receive(this.c);
        }
        catch (PortUnreachableException ex3) {
            RobotLog.d("RobocolDatagramSocket receive error: remote port unreachable");
            return null;
        }
        catch (IOException ex) {
            RobotLog.d("RobocolDatagramSocket receive error: " + ex.toString());
            return null;
        }
        catch (NullPointerException ex2) {
            RobotLog.d("RobocolDatagramSocket receive error: " + ex2.toString());
        }
        this.d.setPacket(this.c);
        return this.d;
    }
    
    public State getState() {
        return this.e;
    }
    
    public InetAddress getInetAddress() {
        if (this.b == null) {
            return null;
        }
        return this.b.getInetAddress();
    }
    
    public InetAddress getLocalAddress() {
        if (this.b == null) {
            return null;
        }
        return this.b.getLocalAddress();
    }
    
    public boolean isRunning() {
        return this.e == State.LISTENING;
    }
    
    public boolean isClosed() {
        return this.e == State.CLOSED;
    }
    
    public enum State
    {
        LISTENING, 
        CLOSED, 
        ERROR;
    }
}
