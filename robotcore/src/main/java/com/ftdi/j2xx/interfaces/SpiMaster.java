// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.interfaces;

public interface SpiMaster
{
    int init(final int p0, final int p1, final int p2, final int p3, final byte p4);
    
    int reset();
    
    int setLines(final int p0);
    
    int singleWrite(final byte[] p0, final int p1, final int[] p2, final boolean p3);
    
    int singleRead(final byte[] p0, final int p1, final int[] p2, final boolean p3);
    
    int singleReadWrite(final byte[] p0, final byte[] p1, final int p2, final int[] p3, final boolean p4);
    
    int multiReadWrite(final byte[] p0, final byte[] p1, final int p2, final int p3, final int p4, final int[] p5);
}
