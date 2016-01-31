// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class TypeConversion
{
    private static final Charset a;
    
    public static byte[] shortToByteArray(final short shortInt) {
        return shortToByteArray(shortInt, ByteOrder.BIG_ENDIAN);
    }
    
    public static byte[] shortToByteArray(final short shortInt, final ByteOrder byteOrder) {
        return ByteBuffer.allocate(2).order(byteOrder).putShort(shortInt).array();
    }
    
    public static byte[] intToByteArray(final int integer) {
        return intToByteArray(integer, ByteOrder.BIG_ENDIAN);
    }
    
    public static byte[] intToByteArray(final int integer, final ByteOrder byteOrder) {
        return ByteBuffer.allocate(4).order(byteOrder).putInt(integer).array();
    }
    
    public static byte[] longToByteArray(final long longInt) {
        return longToByteArray(longInt, ByteOrder.BIG_ENDIAN);
    }
    
    public static byte[] longToByteArray(final long longInt, final ByteOrder byteOrder) {
        return ByteBuffer.allocate(8).order(byteOrder).putLong(longInt).array();
    }
    
    public static short byteArrayToShort(final byte[] byteArray) {
        return byteArrayToShort(byteArray, ByteOrder.BIG_ENDIAN);
    }
    
    public static short byteArrayToShort(final byte[] byteArray, final ByteOrder byteOrder) {
        return ByteBuffer.wrap(byteArray).order(byteOrder).getShort();
    }
    
    public static int byteArrayToInt(final byte[] byteArray) {
        return byteArrayToInt(byteArray, ByteOrder.BIG_ENDIAN);
    }
    
    public static int byteArrayToInt(final byte[] byteArray, final ByteOrder byteOrder) {
        return ByteBuffer.wrap(byteArray).order(byteOrder).getInt();
    }
    
    public static long byteArrayToLong(final byte[] byteArray) {
        return byteArrayToLong(byteArray, ByteOrder.BIG_ENDIAN);
    }
    
    public static long byteArrayToLong(final byte[] byteArray, final ByteOrder byteOrder) {
        return ByteBuffer.wrap(byteArray).order(byteOrder).getLong();
    }
    
    public static int unsignedByteToInt(final byte b) {
        return b & 0xFF;
    }
    
    public static double unsignedByteToDouble(final byte b) {
        return b & 0xFF;
    }
    
    public static long unsignedIntToLong(final int i) {
        return i & 0xFFFFFFFFL;
    }
    
    public static byte[] stringToUtf8(final String javaString) {
        final byte[] bytes = javaString.getBytes(TypeConversion.a);
        if (!javaString.equals(new String(bytes, TypeConversion.a))) {
            throw new IllegalArgumentException(String.format("string cannot be cleanly encoded into %s - '%s' -> '%s'", TypeConversion.a.name(), javaString, new String(bytes, TypeConversion.a)));
        }
        return bytes;
    }
    
    public static String utf8ToString(final byte[] utf8String) {
        return new String(utf8String, TypeConversion.a);
    }
    
    static {
        a = Charset.forName("UTF-8");
    }
}
