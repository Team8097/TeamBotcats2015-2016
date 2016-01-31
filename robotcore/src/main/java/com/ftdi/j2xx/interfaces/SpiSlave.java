// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.interfaces;

public interface SpiSlave
{
    int init();
    
    int getRxStatus(final int[] p0);
    
    int read(final byte[] p0, final int p1, final int[] p2);
    
    int write(final byte[] p0, final int p1, final int[] p2);
    
    int reset();
}
