// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.robot.RobotState;

public class Heartbeat implements RobocolParsable
{
    public static final short PAYLOAD_SIZE = 11;
    public static final short BUFFER_SIZE = 14;
    public static final short MAX_SEQUENCE_NUMBER = 10000;
    private static short a;
    private long b;
    private short c;
    private RobotState d;

    public Heartbeat() {
        this.c = a();
        this.b = System.nanoTime();
        this.d = RobotState.NOT_STARTED;
    }

    public Heartbeat(final Token token) {
        switch (token) {
            case EMPTY: {
                this.c = 0;
                this.b = 0L;
                this.d = RobotState.NOT_STARTED;
                break;
            }
        }

    }

    public long getTimestamp() {
        return this.b;
    }

    public double getElapsedTime() {
        return (System.nanoTime() - this.b) / 1.0E9;
    }

    public short getSequenceNumber() {
        return this.c;
    }

    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.HEARTBEAT;
    }

    public byte getRobotState() {
        return this.d.asByte();
    }

    public void setRobotState(final RobotState state) {
        this.d = state;
    }

    @Override
    public byte[] toByteArray() throws RobotCoreException {
        final ByteBuffer allocate = ByteBuffer.allocate(14);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort((short)11);
            allocate.putShort(this.c);
            allocate.putLong(this.b);
            allocate.put(this.d.asByte());
        }
        catch (BufferOverflowException e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }

    @Override
    public void fromByteArray(final byte[] byteArray) throws RobotCoreException {
        if (byteArray.length < 14) {
            throw new RobotCoreException("Expected buffer of at least 14 bytes, received " + byteArray.length);
        }
        final ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, 11);
        this.c = wrap.getShort();
        this.b = wrap.getLong();
        this.d = RobotState.fromByte(wrap.get());
    }

    @Override
    public String toString() {
        return String.format("Heartbeat - seq: %4d, time: %d", this.c, this.b);
    }

    private static synchronized short a() {
        final short a = Heartbeat.a;
        ++Heartbeat.a;
        if (Heartbeat.a > 10000) {
            Heartbeat.a = 0;
        }
        return a;
    }

    static {
        Heartbeat.a = 0;
    }

    public enum Token
    {
        EMPTY;
    }
}