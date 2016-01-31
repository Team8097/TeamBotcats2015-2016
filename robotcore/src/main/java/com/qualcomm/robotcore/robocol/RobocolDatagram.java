// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import java.net.InetAddress;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.net.DatagramPacket;

public class RobocolDatagram
{
    private DatagramPacket a;
    
    public RobocolDatagram(final RobocolParsable message) throws RobotCoreException {
        this.setData(message.toByteArray());
    }
    
    public RobocolDatagram(final byte[] message) {
        this.setData(message);
    }
    
    protected RobocolDatagram(final DatagramPacket packet) {
        this.a = packet;
    }
    
    protected RobocolDatagram() {
        this.a = null;
    }
    
    public RobocolParsable.MsgType getMsgType() {
        return RobocolParsable.MsgType.fromByte(this.a.getData()[0]);
    }
    
    public int getLength() {
        return this.a.getLength();
    }
    
    public int getPayloadLength() {
        return this.a.getLength() - 3;
    }
    
    public byte[] getData() {
        return this.a.getData();
    }
    
    public void setData(final byte[] data) {
        this.a = new DatagramPacket(data, data.length);
    }
    
    public InetAddress getAddress() {
        return this.a.getAddress();
    }
    
    public void setAddress(final InetAddress address) {
        this.a.setAddress(address);
    }
    
    @Override
    public String toString() {
        int length = 0;
        String name = "NONE";
        Object hostAddress = null;
        if (this.a != null && this.a.getAddress() != null && this.a.getLength() > 0) {
            name = RobocolParsable.MsgType.fromByte(this.a.getData()[0]).name();
            length = this.a.getLength();
            hostAddress = this.a.getAddress().getHostAddress();
        }
        return String.format("RobocolDatagram - type:%s, addr:%s, size:%d", name, hostAddress, length);
    }
    
    protected DatagramPacket getPacket() {
        return this.a;
    }
    
    protected void setPacket(final DatagramPacket packet) {
        this.a = packet;
    }
}
