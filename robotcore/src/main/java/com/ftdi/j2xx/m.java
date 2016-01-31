// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class m
{
    private int a;
    private int b;
    
    m(final int a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    m() {
        this.a = 0;
        this.b = 0;
    }
    
    public int a() {
        return this.a;
    }
    
    public int b() {
        return this.b;
    }
    
    public String toString() {
        return "Vendor: " + String.format("%04x", this.a) + ", Product: " + String.format("%04x", this.b);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof m)) {
            return false;
        }
        final m m = (m)o;
        return this.a == m.a && this.b == m.b;
    }
    
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
