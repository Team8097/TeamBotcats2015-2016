// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Iterator;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.Charset;

public class Telemetry implements RobocolParsable
{
    public static final String DEFAULT_TAG = "TELEMETRY_DATA";
    private static final Charset a;
    private final Map<String, String> b;
    private final Map<String, Float> c;
    private String d;
    private long e;
    
    public Telemetry() {
        this.b = new HashMap<String, String>();
        this.c = new HashMap<String, Float>();
        this.d = "";
        this.e = 0L;
    }
    
    public Telemetry(final byte[] byteArray) throws RobotCoreException {
        this.b = new HashMap<String, String>();
        this.c = new HashMap<String, Float>();
        this.d = "";
        this.e = 0L;
        this.fromByteArray(byteArray);
    }
    
    public synchronized long getTimestamp() {
        return this.e;
    }
    
    public synchronized void setTag(final String tag) {
        this.d = tag;
    }
    
    public synchronized String getTag() {
        if (this.d.length() == 0) {
            return "TELEMETRY_DATA";
        }
        return this.d;
    }
    
    public synchronized void addData(final String key, final String msg) {
        this.b.put(key, msg);
    }
    
    public synchronized void addData(final String key, final Object msg) {
        this.b.put(key, msg.toString());
    }
    
    public synchronized void addData(final String key, final float msg) {
        this.c.put(key, msg);
    }
    
    public synchronized void addData(final String key, final double msg) {
        this.c.put(key, (float)msg);
    }
    
    public synchronized Map<String, String> getDataStrings() {
        return this.b;
    }
    
    public synchronized Map<String, Float> getDataNumbers() {
        return this.c;
    }
    
    public synchronized boolean hasData() {
        return !this.b.isEmpty() || !this.c.isEmpty();
    }
    
    public synchronized void clearData() {
        this.e = 0L;
        this.b.clear();
        this.c.clear();
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.TELEMETRY;
    }
    
    @Override
    public synchronized byte[] toByteArray() throws RobotCoreException {
        this.e = System.currentTimeMillis();
        if (this.b.size() > 256) {
            throw new RobotCoreException("Cannot have more than 256 string data points");
        }
        if (this.c.size() > 256) {
            throw new RobotCoreException("Cannot have more than 256 number data points");
        }
        final int n = this.a() + 8;
        final int n2 = 3 + n;
        if (n2 > 4098) {
            throw new RobotCoreException(String.format("Cannot send telemetry data of %d bytes; max is %d", n2, 4098));
        }
        final ByteBuffer allocate = ByteBuffer.allocate(n2);
        allocate.put(this.getRobocolMsgType().asByte());
        allocate.putShort((short)n);
        allocate.putLong(this.e);
        if (this.d.length() == 0) {
            allocate.put((byte)0);
        }
        else {
            final byte[] bytes = this.d.getBytes(Telemetry.a);
            if (bytes.length > 256) {
                throw new RobotCoreException(String.format("Telemetry tag cannot exceed 256 bytes [%s]", this.d));
            }
            allocate.put((byte)bytes.length);
            allocate.put(bytes);
        }
        allocate.put((byte)this.b.size());
        for (final Map.Entry<String, String> entry : this.b.entrySet()) {
            final byte[] bytes2 = entry.getKey().getBytes(Telemetry.a);
            final byte[] bytes3 = entry.getValue().getBytes(Telemetry.a);
            if (bytes2.length > 256 || bytes3.length > 256) {
                throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%s]", entry.getKey(), entry.getValue()));
            }
            allocate.put((byte)bytes2.length);
            allocate.put(bytes2);
            allocate.put((byte)bytes3.length);
            allocate.put(bytes3);
        }
        allocate.put((byte)this.c.size());
        for (final Map.Entry<String, Float> entry2 : this.c.entrySet()) {
            final byte[] bytes4 = entry2.getKey().getBytes(Telemetry.a);
            final float floatValue = entry2.getValue();
            if (bytes4.length > 256) {
                throw new RobotCoreException(String.format("Telemetry elements cannot exceed 256 bytes [%s:%f]", entry2.getKey(), floatValue));
            }
            allocate.put((byte)bytes4.length);
            allocate.put(bytes4);
            allocate.putFloat(floatValue);
        }
        return allocate.array();
    }
    
    @Override
    public synchronized void fromByteArray(final byte[] byteArray) throws RobotCoreException {
        this.clearData();
        final ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, byteArray.length - 3);
        this.e = wrap.getLong();
        final int unsignedByteToInt = TypeConversion.unsignedByteToInt(wrap.get());
        if (unsignedByteToInt == 0) {
            this.d = "";
        }
        else {
            final byte[] array = new byte[unsignedByteToInt];
            wrap.get(array);
            this.d = new String(array, Telemetry.a);
        }
        for (byte value = wrap.get(), b = 0; b < value; ++b) {
            final byte[] array2 = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
            wrap.get(array2);
            final byte[] array3 = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
            wrap.get(array3);
            this.b.put(new String(array2, Telemetry.a), new String(array3, Telemetry.a));
        }
        for (byte value2 = wrap.get(), b2 = 0; b2 < value2; ++b2) {
            final byte[] array4 = new byte[TypeConversion.unsignedByteToInt(wrap.get())];
            wrap.get(array4);
            this.c.put(new String(array4, Telemetry.a), wrap.getFloat());
        }
    }
    
    private int a() {
        int n = 0 + (1 + this.d.getBytes(Telemetry.a).length);
        ++n;
        for (final Map.Entry<String, String> entry : this.b.entrySet()) {
            n = n + (1 + entry.getKey().getBytes(Telemetry.a).length) + (1 + entry.getValue().getBytes(Telemetry.a).length);
        }
        ++n;
        final Iterator<Map.Entry<String, Float>> iterator2 = this.c.entrySet().iterator();
        while (iterator2.hasNext()) {
            n += 1 + iterator2.next().getKey().getBytes(Telemetry.a).length;
            n += 4;
        }
        return n;
    }
    
    static {
        a = Charset.forName("UTF-8");
    }
}
