// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import android.util.Log;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.nio.channels.Pipe;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

class o
{
    private Semaphore[] a;
    private Semaphore[] b;
    private n[] c;
    private ByteBuffer d;
    private ByteBuffer[] e;
    private Pipe f;
    private Pipe.SinkChannel g;
    private Pipe.SourceChannel h;
    private int i;
    private int j;
    private Object k;
    private FT_Device l;
    private D2xxManager.DriverParameters m;
    private Lock n;
    private Condition o;
    private boolean p;
    private Lock q;
    private Condition r;
    private Object s;
    private int t;
    
    public o(final FT_Device l) {
        this.l = l;
        this.m = this.l.d();
        this.i = this.m.getBufferNumber();
        final int maxBufferSize = this.m.getMaxBufferSize();
        this.t = this.l.e();
        this.a = new Semaphore[this.i];
        this.b = new Semaphore[this.i];
        this.c = new n[this.i];
        this.e = new ByteBuffer[256];
        this.n = new ReentrantLock();
        this.o = this.n.newCondition();
        this.p = false;
        this.q = new ReentrantLock();
        this.r = this.q.newCondition();
        this.k = new Object();
        this.s = new Object();
        this.h();
        this.d = ByteBuffer.allocateDirect(maxBufferSize);
        try {
            this.f = Pipe.open();
            this.g = this.f.sink();
            this.h = this.f.source();
        }
        catch (IOException ex) {
            Log.d("ProcessInCtrl", "Create mMainPipe failed!");
            ex.printStackTrace();
        }
        for (int i = 0; i < this.i; ++i) {
            this.c[i] = new n(maxBufferSize);
            this.b[i] = new Semaphore(1);
            this.a[i] = new Semaphore(1);
            try {
                this.c(i);
            }
            catch (Exception ex2) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + i + " failed!");
                ex2.printStackTrace();
            }
        }
    }
    
    boolean a() {
        return this.p;
    }
    
    D2xxManager.DriverParameters b() {
        return this.m;
    }
    
    n a(final int n) {
        n n2 = null;
        synchronized (this.c) {
            if (n >= 0 && n < this.i) {
                n2 = this.c[n];
            }
        }
        // monitorexit(this.c)
        return n2;
    }
    
    n b(final int n) throws InterruptedException {
        this.a[n].acquire();
        n a = this.a(n);
        if (a.c(n) == null) {
            a = null;
        }
        return a;
    }
    
    n c(final int n) throws InterruptedException {
        this.b[n].acquire();
        return this.a(n);
    }
    
    public void d(final int n) throws InterruptedException {
        synchronized (this.c) {
            this.c[n].d(n);
        }
        // monitorexit(this.c)
        this.a[n].release();
    }
    
    public void e(final int n) throws InterruptedException {
        this.b[n].release();
    }
    
    public void a(final n n) throws D2xxManager.D2xxException {
        final short n2 = 0;
        final short n3 = 0;
        final boolean b = false;
        try {
            final int b2 = n.b();
            if (b2 < 2) {
                n.a().clear();
                return;
            }
            final int d;
            final int n4;
            synchronized (this.s) {
                d = this.d();
                n4 = b2 - 2;
                if (d < n4) {
                    Log.d("ProcessBulkIn::", " Buffer is full, waiting for read....");
                    this.a(b, n2, n3);
                    this.n.lock();
                    this.p = true;
                }
            }
            // monitorexit(this.s)
            if (d < n4) {
                this.o.await();
                this.n.unlock();
            }
            this.b(n);
        }
        catch (InterruptedException ex) {
            this.n.unlock();
            Log.e("ProcessInCtrl", "Exception in Full await!");
            ex.printStackTrace();
        }
        catch (Exception ex2) {
            Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
            ex2.printStackTrace();
            throw new D2xxManager.D2xxException("Fatal error in BulkIn.");
        }
    }
    
    private void b(final n n) throws InterruptedException {
        int n2 = 0;
        short n3 = 0;
        short n4 = 0;
        boolean b = false;
        final ByteBuffer a = n.a();
        final int b2 = n.b();
        if (b2 > 0) {
            final int n5 = b2 / this.t + ((b2 % this.t > 0) ? 1 : 0);
            for (int i = 0; i < n5; ++i) {
                int n6;
                int n7;
                if (i == n5 - 1) {
                    n6 = b2;
                    a.limit(n6);
                    n7 = i * this.t;
                    a.position(n7);
                    final byte value = a.get();
                    n3 = (short)(this.l.g.modemStatus ^ (short)(value & 0xF0));
                    this.l.g.modemStatus = (short)(value & 0xF0);
                    this.l.g.lineStatus = (short)(a.get() & 0xFF);
                    n7 += 2;
                    if (a.hasRemaining()) {
                        n4 = (short)(this.l.g.lineStatus & 0x1E);
                    }
                    else {
                        n4 = 0;
                    }
                }
                else {
                    n6 = (i + 1) * this.t;
                    a.limit(n6);
                    n7 = i * this.t + 2;
                    a.position(n7);
                }
                n2 += n6 - n7;
                this.e[i] = a.slice();
            }
            if (n2 != 0) {
                b = true;
                try {
                    final long write = this.g.write(this.e, 0, n5);
                    if (write != n2) {
                        Log.d("extractReadData::", "written != totalData, written= " + write + " totalData=" + n2);
                    }
                    this.f((int)write);
                    this.q.lock();
                    this.r.signalAll();
                    this.q.unlock();
                }
                catch (Exception ex) {
                    Log.d("extractReadData::", "Write data to sink failed!!");
                    ex.printStackTrace();
                }
            }
            a.clear();
            this.a(b, n3, n4);
        }
    }
    
    public int a(final byte[] array, final int n, long n2) {
        int n3 = 0;
        this.m.getMaxBufferSize();
        final long currentTimeMillis = System.currentTimeMillis();
        final ByteBuffer wrap = ByteBuffer.wrap(array, 0, n);
        if (n2 == 0L) {
            n2 = this.m.getReadTimeout();
        }
        while (this.l.isOpen()) {
            if (this.c() >= n) {
                synchronized (this.h) {
                    try {
                        this.h.read(wrap);
                        this.g(n);
                    }
                    catch (Exception ex) {
                        Log.d("readBulkInData::", "Cannot read data from Source!!");
                        ex.printStackTrace();
                    }
                }
                // monitorexit(this.h)
                synchronized (this.s) {
                    if (this.p) {
                        Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                        this.n.lock();
                        this.o.signalAll();
                        this.p = false;
                        this.n.unlock();
                    }
                }
                // monitorexit(this.s)
                n3 = n;
                break;
            }
            try {
                this.q.lock();
                this.r.await(System.currentTimeMillis() - currentTimeMillis, TimeUnit.MILLISECONDS);
                this.q.unlock();
            }
            catch (InterruptedException ex2) {
                Log.d("readBulkInData::", "Cannot wait to read data!!");
                ex2.printStackTrace();
                this.q.unlock();
            }
            if (System.currentTimeMillis() - currentTimeMillis >= n2) {
                break;
            }
        }
        return n3;
    }
    
    private int f(final int n) {
        final int j;
        synchronized (this.k) {
            this.j += n;
            j = this.j;
        }
        // monitorexit(this.k)
        return j;
    }
    
    private int g(final int n) {
        final int j;
        synchronized (this.k) {
            this.j -= n;
            j = this.j;
        }
        // monitorexit(this.k)
        return j;
    }
    
    private void h() {
        synchronized (this.k) {
            this.j = 0;
        }
        // monitorexit(this.k)
    }
    
    public int c() {
        final int j;
        synchronized (this.k) {
            j = this.j;
        }
        // monitorexit(this.k)
        return j;
    }
    
    public int d() {
        return this.m.getMaxBufferSize() - this.c() - 1;
    }
    
    public int e() {
        final int bufferNumber = this.m.getBufferNumber();
        synchronized (this.d) {
            try {
                int i;
                do {
                    this.h.configureBlocking(false);
                    i = this.h.read(this.d);
                    this.d.clear();
                } while (i != 0);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            this.h();
            for (int j = 0; j < bufferNumber; ++j) {
                final n a = this.a(j);
                if (a.d() && a.b() > 2) {
                    a.c();
                }
            }
        }
        // monitorexit(this.d)
        return 0;
    }
    
    public int a(final boolean b, final short n, final short n2) throws InterruptedException {
        final q q = new q();
        q.a = this.l.i.a;
        if (b && (q.a & 0x1L) != 0x0L && (this.l.a ^ 0x1L) == 0x1L) {
            final FT_Device l = this.l;
            l.a |= 0x1L;
            final Intent intent = new Intent("FT_EVENT_RXCHAR");
            intent.putExtra("message", "FT_EVENT_RXCHAR");
            LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(intent);
        }
        if (n != 0 && (q.a & 0x2L) != 0x0L && (this.l.a ^ 0x2L) == 0x2L) {
            final FT_Device i = this.l;
            i.a |= 0x2L;
            final Intent intent2 = new Intent("FT_EVENT_MODEM_STATUS");
            intent2.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(intent2);
        }
        if (n2 != 0 && (q.a & 0x4L) != 0x0L && (this.l.a ^ 0x4L) == 0x4L) {
            final FT_Device j = this.l;
            j.a |= 0x4L;
            final Intent intent3 = new Intent("FT_EVENT_LINE_STATUS");
            intent3.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(intent3);
        }
        return 0;
    }
    
    public void f() throws InterruptedException {
        for (int bufferNumber = this.m.getBufferNumber(), i = 0; i < bufferNumber; ++i) {
            if (this.a(i).d()) {
                this.d(i);
            }
        }
    }
    
    void g() {
        for (int i = 0; i < this.i; ++i) {
            try {
                this.e(i);
            }
            catch (Exception ex) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + i + " failed!");
                ex.printStackTrace();
            }
            this.c[i] = null;
            this.b[i] = null;
            this.a[i] = null;
        }
        for (int j = 0; j < 256; ++j) {
            this.e[j] = null;
        }
        this.a = null;
        this.b = null;
        this.c = null;
        this.e = null;
        this.d = null;
        if (this.p) {
            this.n.lock();
            this.o.signalAll();
            this.n.unlock();
        }
        this.q.lock();
        this.r.signalAll();
        this.q.unlock();
        this.n = null;
        this.o = null;
        this.k = null;
        this.q = null;
        this.r = null;
        try {
            this.g.close();
            this.g = null;
            this.h.close();
            this.h = null;
            this.f = null;
        }
        catch (IOException ex2) {
            Log.d("ProcessInCtrl", "Close mMainPipe failed!");
            ex2.printStackTrace();
        }
        this.l = null;
        this.m = null;
    }
}
