// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.protocol;

public class SpiSlaveEvent
{
    private int a;
    private boolean b;
    private Object c;
    private Object d;
    private Object e;
    
    public SpiSlaveEvent(final int iEventType, final boolean bSync, final Object pArg0, final Object pArg1, final Object pArg2) {
        this.a = iEventType;
        this.b = bSync;
        this.c = pArg0;
        this.d = pArg1;
        this.e = pArg2;
    }
    
    public Object getArg0() {
        return this.c;
    }
    
    public void setArg0(final Object arg) {
        this.c = arg;
    }
    
    public Object getArg1() {
        return this.d;
    }
    
    public void setArg1(final Object arg) {
        this.d = arg;
    }
    
    public Object getArg2() {
        return this.e;
    }
    
    public void setArg2(final Object arg) {
        this.e = arg;
    }
    
    public int getEventType() {
        return this.a;
    }
    
    public void setEventType(final int type) {
        this.a = type;
    }
    
    public boolean getSync() {
        return this.b;
    }
    
    public void setSync(final boolean bSync) {
        this.b = bSync;
    }
}
