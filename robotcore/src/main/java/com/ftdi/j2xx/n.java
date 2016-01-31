// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class n
{
    private int a;
    private ByteBuffer b;
    private int c;
    private boolean d;
    
    public n(final int n) {
        this.b = ByteBuffer.allocate(n);
        this.b(0);
    }
    
    void a(final int a) {
        this.a = a;
    }
    
    ByteBuffer a() {
        return this.b;
    }
    
    int b() {
        return this.c;
    }
    
    void b(final int c) {
        this.c = c;
    }
    
    synchronized void c() {
        this.b.clear();
        this.b(0);
    }
    
    synchronized boolean d() {
        return this.d;
    }
    
    synchronized ByteBuffer c(final int a) {
        ByteBuffer b = null;
        if (!this.d) {
            this.d = true;
            this.a = a;
            b = this.b;
        }
        return b;
    }
    
    synchronized boolean d(final int n) {
        boolean b = false;
        if (this.d && n == this.a) {
            this.d = false;
            b = true;
        }
        return b;
    }
}
