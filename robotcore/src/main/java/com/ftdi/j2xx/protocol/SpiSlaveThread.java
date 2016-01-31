// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.protocol;

import android.util.Log;
import java.util.concurrent.locks.ReentrantLock;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.Queue;

public abstract class SpiSlaveThread extends Thread
{
    public static final int THREAD_INIT = 0;
    public static final int THREAD_RUNNING = 1;
    public static final int THREAD_DESTORYED = 2;
    private Queue<SpiSlaveEvent> a;
    private Lock b;
    private Object c;
    private Object d;
    private boolean e;
    private boolean f;
    private int g;
    
    protected abstract boolean pollData();
    
    protected abstract void requestEvent(final SpiSlaveEvent p0);
    
    protected abstract boolean isTerminateEvent(final SpiSlaveEvent p0);
    
    public SpiSlaveThread() {
        this.a = new LinkedList<SpiSlaveEvent>();
        this.c = new Object();
        this.d = new Object();
        this.b = new ReentrantLock();
        this.g = 0;
        this.setName("SpiSlaveThread");
    }
    
    public boolean sendMessage(final SpiSlaveEvent event) {
        while (this.g != 1) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException ex) {}
        }
        this.b.lock();
        if (this.a.size() > 10) {
            this.b.unlock();
            Log.d("FTDI", "SpiSlaveThread sendMessage Buffer full!!");
            return false;
        }
        this.a.add(event);
        if (this.a.size() == 1) {
            synchronized (this.c) {
                this.e = true;
                this.c.notify();
            }
            // monitorexit(this.c)
        }
        this.b.unlock();
        if (event.getSync()) {
            synchronized (this.d) {
                this.f = false;
                while (!this.f) {
                    try {
                        this.d.wait();
                    }
                    catch (InterruptedException ex2) {
                        this.f = true;
                    }
                }
            }
            // monitorexit(this.d)
        }
        return true;
    }
    
    public void run() {
        boolean terminateEvent = false;
        this.g = 1;
        while (!Thread.interrupted() && !terminateEvent) {
            this.pollData();
            this.b.lock();
            if (this.a.size() <= 0) {
                this.b.unlock();
            }
            else {
                final SpiSlaveEvent spiSlaveEvent = this.a.peek();
                this.a.remove();
                this.b.unlock();
                this.requestEvent(spiSlaveEvent);
                if (spiSlaveEvent.getSync()) {
                    synchronized (this.d) {
                        while (this.f) {
                            try {
                                Thread.sleep(100L);
                            }
                            catch (InterruptedException ex) {}
                        }
                        this.f = true;
                        this.d.notify();
                    }
                    // monitorexit(this.d)
                }
                terminateEvent = this.isTerminateEvent(spiSlaveEvent);
            }
        }
        this.g = 2;
    }
}
