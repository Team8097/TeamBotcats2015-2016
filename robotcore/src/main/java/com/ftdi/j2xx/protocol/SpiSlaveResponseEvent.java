// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.protocol;

public class SpiSlaveResponseEvent extends SpiSlaveEvent
{
    public static final int RES_SLAVE_READ = 3;
    public static final int OK = 0;
    public static final int DATA_CORRUPTED = 1;
    public static final int IO_ERROR = 2;
    public static final int RESET = 3;
    private int a;
    
    public SpiSlaveResponseEvent(final int iEventType, final int responseCode, final Object pArg0, final Object pArg1, final Object pArg2) {
        super(iEventType, false, pArg0, pArg1, pArg2);
        this.a = responseCode;
    }
    
    public int getResponseCode() {
        return this.a;
    }
}
