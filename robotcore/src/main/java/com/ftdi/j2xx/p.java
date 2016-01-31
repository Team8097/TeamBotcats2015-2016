// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

import android.util.Log;

class p implements Runnable
{
    private o b;
    int a;
    
    p(final o b) {
        this.b = b;
        this.a = this.b.b().getBufferNumber();
    }
    
    public void run() {
        int n = 0;
        try {
            do {
                final n c = this.b.c(n);
                if (c.b() > 0) {
                    this.b.a(c);
                    c.c();
                }
                this.b.d(n);
                n = ++n % this.a;
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }
        catch (InterruptedException ex) {
            Log.d("ProcessRequestThread::", "Device has been closed.");
            ex.printStackTrace();
        }
        catch (Exception ex2) {
            Log.e("ProcessRequestThread::", "Fatal error!");
            ex2.printStackTrace();
        }
    }
}
