// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;

public class PeerDiscovery implements RobocolParsable
{
    public static final short PAYLOAD_SIZE = 10;
    public static final short BUFFER_SIZE = 13;
    public static final byte ROBOCOL_VERSION = 1;
    private PeerType a;
    
    public PeerDiscovery(final PeerType peerType) {
        this.a = peerType;
    }
    
    public PeerType getPeerType() {
        return this.a;
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.PEER_DISCOVERY;
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        final ByteBuffer allocate = ByteBuffer.allocate(13);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort((short)10);
            allocate.put((byte)1);
            allocate.put(this.a.asByte());
        }
        catch (BufferOverflowException e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }
    
    @Override
    public void fromByteArray(final byte[] byteArray) throws RobotCoreException {
        if (byteArray.length < 13) {
            throw new RobotCoreException("Expected buffer of at least 13 bytes, received " + byteArray.length);
        }
        final ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, 10);
        switch (wrap.get()) {
            case 1: {
                this.a = PeerType.fromByte(wrap.get());
                break;
            }
        }
    }
    
    @Override
    public String toString() {
        return String.format("Peer Discovery - peer type: %s", this.a.name());
    }
    
    public enum PeerType
    {
        NOT_SET(0), 
        PEER(1), 
        GROUP_OWNER(2);
        
        private static final PeerType[] a;
        private int b;
        
        public static PeerType fromByte(final byte b) {
            PeerType not_SET = PeerType.NOT_SET;
            try {
                not_SET = PeerType.a[b];
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                RobotLog.w(String.format("Cannot convert %d to Peer: %s", b, ex.toString()));
            }
            return not_SET;
        }
        
        private PeerType(final int type) {
            this.b = type;
        }
        
        public byte asByte() {
            return (byte)this.b;
        }
        
        static {
            a = values();
        }
    }
}
