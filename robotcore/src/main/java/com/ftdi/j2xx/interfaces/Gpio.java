// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.interfaces;

public interface Gpio
{
    int init(final int[] p0);
    
    int read(final int p0, final boolean[] p1);
    
    int write(final int p0, final boolean p1);
}
