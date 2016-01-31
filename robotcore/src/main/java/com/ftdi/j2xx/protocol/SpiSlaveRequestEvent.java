// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.protocol;

public class SpiSlaveRequestEvent extends SpiSlaveEvent
{
    protected static final int REQ_DESTORY_THREAD = -1;
    protected static final int REQ_INIT_SLAVE = 1;
    protected static final int REQ_SLAVE_WRITE = 2;
    protected static final int REQ_SLAVE_READ = 3;
    
    public SpiSlaveRequestEvent(final int iEventType, final boolean bSync, final Object pArg0, final Object pArg1, final Object pArg2) {
        super(iEventType, bSync, pArg0, pArg1, pArg2);
    }
}
