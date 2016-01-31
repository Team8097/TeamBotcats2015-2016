// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.charset.Charset;
import java.util.Comparator;

public class Command implements RobocolParsable, Comparable<Command>, Comparator<Command>
{
    public static final int MAX_COMMAND_LENGTH = 256;
    private static final Charset h;
    String a;
    String b;
    byte[] c;
    byte[] d;
    long e;
    boolean f;
    byte g;
    
    public Command(final String name) {
        this(name, "");
    }
    
    public Command(final String name, final String extra) {
        this.f = false;
        this.g = 0;
        this.a = name;
        this.b = extra;
        this.c = TypeConversion.stringToUtf8(this.a);
        this.d = TypeConversion.stringToUtf8(this.b);
        this.e = generateTimestamp();
        if (this.c.length > 256) {
            throw new IllegalArgumentException(String.format("command name length is too long (MAX: %d)", 256));
        }
        if (this.d.length > 256) {
            throw new IllegalArgumentException(String.format("command extra data length is too long (MAX: %d)", 256));
        }
    }
    
    public Command(final byte[] byteArray) throws RobotCoreException {
        this.f = false;
        this.g = 0;
        this.fromByteArray(byteArray);
    }
    
    public void acknowledge() {
        this.f = true;
    }
    
    public boolean isAcknowledged() {
        return this.f;
    }
    
    public String getName() {
        return this.a;
    }
    
    public String getExtra() {
        return this.b;
    }
    
    public byte getAttempts() {
        return this.g;
    }
    
    public long getTimestamp() {
        return this.e;
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.COMMAND;
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        if (this.g != 127) {
            ++this.g;
        }
        final short n = (short)(11 + this.c.length + this.d.length);
        final ByteBuffer allocate = ByteBuffer.allocate(3 + n);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort(n);
            allocate.putLong(this.e);
            allocate.put((byte)(this.f ? 1 : 0));
            allocate.put((byte)this.c.length);
            allocate.put(this.c);
            allocate.put((byte)this.d.length);
            allocate.put(this.d);
        }
        catch (BufferOverflowException e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }
    
    @Override
    public void fromByteArray(final byte[] byteArray) throws RobotCoreException {
        final ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, byteArray.length - 3);
        this.e = wrap.getLong();
        this.f = (wrap.get() == 1);
        wrap.get(this.c = new byte[TypeConversion.unsignedByteToInt(wrap.get())]);
        this.a = TypeConversion.utf8ToString(this.c);
        wrap.get(this.d = new byte[TypeConversion.unsignedByteToInt(wrap.get())]);
        this.b = TypeConversion.utf8ToString(this.d);
    }
    
    @Override
    public String toString() {
        return String.format("command: %20d %5s %s", this.e, this.f, this.a);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Command) {
            final Command command = (Command)o;
            if (this.a.equals(command.a) && this.e == command.e) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (int)(this.a.hashCode() & this.e);
    }
    
    @Override
    public int compareTo(final Command another) {
        final int compareTo = this.a.compareTo(another.a);
        if (compareTo != 0) {
            return compareTo;
        }
        if (this.e < another.e) {
            return -1;
        }
        if (this.e > another.e) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public int compare(final Command c1, final Command c2) {
        return c1.compareTo(c2);
    }
    
    public static long generateTimestamp() {
        return System.nanoTime();
    }
    
    static {
        h = Charset.forName("UTF-8");
    }
}
