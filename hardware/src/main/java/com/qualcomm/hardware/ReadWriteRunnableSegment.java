// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class ReadWriteRunnableSegment
{
    private int c;
    final Lock a;
    private final byte[] d;
    final Lock b;
    private final byte[] e;
    
    public ReadWriteRunnableSegment(final int address, final int size) {
        this.c = address;
        this.a = new ReentrantLock();
        this.d = new byte[size];
        this.b = new ReentrantLock();
        this.e = new byte[size];
    }
    
    public int getAddress() {
        return this.c;
    }
    
    public void setAddress(final int address) {
        this.c = address;
    }
    
    public Lock getReadLock() {
        return this.a;
    }
    
    public byte[] getReadBuffer() {
        return this.d;
    }
    
    public Lock getWriteLock() {
        return this.b;
    }
    
    public byte[] getWriteBuffer() {
        return this.e;
    }
    
    @Override
    public String toString() {
        return String.format("Segment - address:%d read:%d write:%d", this.c, this.d.length, this.e.length);
    }
}
