// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobocolParsable
{
    public static final int HEADER_LENGTH = 3;
    public static final byte[] EMPTY_HEADER_BUFFER = new byte[3];
    
    MsgType getRobocolMsgType();
    
    byte[] toByteArray() throws RobotCoreException;
    
    void fromByteArray(final byte[] p0) throws RobotCoreException;
    
    public enum MsgType
    {
        EMPTY(0), 
        HEARTBEAT(1), 
        GAMEPAD(2), 
        PEER_DISCOVERY(3), 
        COMMAND(4), 
        TELEMETRY(5);
        
        private static final MsgType[] a;
        private final int b;
        
        public static MsgType fromByte(final byte b) {
            MsgType empty = MsgType.EMPTY;
            try {
                empty = MsgType.a[b];
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                RobotLog.w(String.format("Cannot convert %d to MsgType: %s", b, ex.toString()));
            }
            return empty;
        }
        
        private MsgType(final int type) {
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
