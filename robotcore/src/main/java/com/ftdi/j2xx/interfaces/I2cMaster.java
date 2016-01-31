// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.interfaces;

public interface I2cMaster
{
    int init(final int p0);
    
    int reset();
    
    int read(final int p0, final byte[] p1, final int p2, final int[] p3);
    
    int write(final int p0, final byte[] p1, final int p2, final int[] p3);
}
