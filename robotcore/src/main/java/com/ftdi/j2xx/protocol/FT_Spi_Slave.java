// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.protocol;

import junit.framework.Assert;
import com.ftdi.j2xx.interfaces.SpiSlave;

public class FT_Spi_Slave extends SpiSlaveThread
{
    private aEnum a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private byte[] g;
    private int h;
    private int i;
    private SpiSlave j;
    private SpiSlaveListener k;
    private boolean l;
    private static /* synthetic */ int[] m;
    
    public FT_Spi_Slave(final SpiSlave pSlaveInterface) {
        this.j = pSlaveInterface;
        this.a = FT_Spi_Slave.aEnum.a;
    }
    
    public void registerSpiSlaveListener(final SpiSlaveListener pListener) {
        this.k = pListener;
    }
    
    public int open() {
        if (this.l) {
            return 1;
        }
        this.l = true;
        this.j.init();
        this.start();
        return 0;
    }
    
    public int close() {
        if (!this.l) {
            return 3;
        }
        this.sendMessage(new SpiSlaveRequestEvent(-1, true, null, null, null));
        this.l = false;
        return 0;
    }
    
    public int write(final byte[] wrBuf) {
        if (!this.l) {
            return 3;
        }
        if (wrBuf.length > 65536) {
            return 1010;
        }
        final int[] array = { 0 };
        int n = 0;
        final int length = wrBuf.length;
        final int a = this.a(wrBuf, 90, 129, this.i, length);
        final byte[] array2 = new byte[8 + wrBuf.length];
        array2[n++] = 0;
        array2[n++] = 90;
        array2[n++] = -127;
        array2[n++] = (byte)this.i;
        array2[n++] = (byte)((length & 0xFF00) >> 8);
        array2[n++] = (byte)(length & 0xFF);
        for (int i = 0; i < wrBuf.length; ++i) {
            array2[n++] = wrBuf[i];
        }
        array2[n++] = (byte)((a & 0xFF00) >> 8);
        array2[n++] = (byte)(a & 0xFF);
        this.j.write(array2, array2.length, array);
        if (array[0] != array2.length) {
            return 4;
        }
        ++this.i;
        if (this.i >= 256) {
            this.i = 0;
        }
        return 0;
    }
    
    private boolean a(final int n) {
        return n == 128 || n == 130 || n == 136;
    }
    
    private int a(final byte[] array, final int n, final int n2, final int n3, final int n4) {
        int n5 = 0;
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                n5 += (array[i] & 0xFF);
            }
        }
        return n5 + n + n2 + n3 + ((n4 & 0xFF00) >> 8) + (n4 & 0xFF);
    }
    
    private void b() {
        int n = 0;
        final byte[] array = new byte[8];
        array[n++] = 0;
        array[n++] = 90;
        array[n++] = -124;
        array[n++] = (byte)this.d;
        array[n++] = 0;
        array[n++] = 0;
        final int a = this.a(null, 90, 132, this.d, 0);
        array[n++] = (byte)((a & 0xFF00) >> 8);
        array[n++] = (byte)(a & 0xFF);
        this.j.write(array, array.length, new int[1]);
    }
    
    private void a(final byte[] array) {
        int n = 0;
        int n2 = 0;
        for (int i = 0; i < array.length; ++i) {
            final int d = array[i] & 0xFF;
            switch (a()[this.a.ordinal()]) {
                case 1: {
                    if (d != 90) {
                        n = 1;
                        break;
                    }
                    this.a = FT_Spi_Slave.aEnum.b;
                    this.b = d;
                    break;
                }
                case 2: {
                    if (!this.a(d)) {
                        n = 1;
                        n2 = 1;
                    }
                    else {
                        this.c = d;
                    }
                    this.a = FT_Spi_Slave.aEnum.c;
                    break;
                }
                case 3: {
                    this.d = d;
                    this.a = FT_Spi_Slave.aEnum.d;
                    break;
                }
                case 4: {
                    this.e = d * 256;
                    this.a = FT_Spi_Slave.aEnum.e;
                    break;
                }
                case 5: {
                    this.e += d;
                    this.f = 0;
                    this.g = new byte[this.e];
                    this.a = FT_Spi_Slave.aEnum.f;
                    break;
                }
                case 6: {
                    this.g[this.f] = array[i];
                    ++this.f;
                    if (this.f == this.e) {
                        this.a = FT_Spi_Slave.aEnum.g;
                        break;
                    }
                    break;
                }
                case 7: {
                    this.h = d * 256;
                    this.a = FT_Spi_Slave.aEnum.h;
                    break;
                }
                case 8: {
                    this.h += d;
                    if (this.h == this.a(this.g, this.b, this.c, this.d, this.e)) {
                        if (this.c == 128) {
                            this.b();
                            if (this.k != null) {
                                this.k.OnDataReceived(new SpiSlaveResponseEvent(3, 0, this.g, null, null));
                            }
                        }
                    }
                    else {
                        n2 = 1;
                    }
                    n = 1;
                    break;
                }
            }
            if (n2 != 0 && this.k != null) {
                this.k.OnDataReceived(new SpiSlaveResponseEvent(3, 1, null, null, null));
            }
            if (n != 0) {
                this.a = FT_Spi_Slave.aEnum.a;
                this.b = 0;
                this.c = 0;
                this.d = 0;
                this.e = 0;
                this.f = 0;
                this.h = 0;
                this.g = null;
                n = 0;
                n2 = 0;
            }
        }
    }
    
    protected boolean pollData() {
        final int[] array = { 0 };
        int n = this.j.getRxStatus(array);
        if (array[0] > 0 && n == 0) {
            final byte[] array2 = new byte[array[0]];
            n = this.j.read(array2, array2.length, array);
            if (n == 0) {
                this.a(array2);
            }
        }
        if (n == 4 && this.k != null) {
            this.k.OnDataReceived(new SpiSlaveResponseEvent(3, 2, this.g, null, null));
        }
        try {
            Thread.sleep(10L);
        }
        catch (InterruptedException ex) {}
        return true;
    }
    
    protected void requestEvent(final SpiSlaveEvent pEvent) {
        if (pEvent instanceof SpiSlaveRequestEvent) {
            switch (pEvent.getEventType()) {
            }
        }
        else {
            Assert.assertTrue("processEvent wrong type" + pEvent.getEventType(), false);
        }
    }
    
    protected boolean isTerminateEvent(final SpiSlaveEvent pEvent) {
        if (!Thread.interrupted()) {
            return true;
        }
        if (pEvent instanceof SpiSlaveRequestEvent) {
            switch (pEvent.getEventType()) {
                case -1: {
                    return true;
                }
            }
        }
        else {
            Assert.assertTrue("processEvent wrong type" + pEvent.getEventType(), false);
        }
        return false;
    }
    
    static /* synthetic */ int[] a() {
        final int[] m = FT_Spi_Slave.m;
        if (m != null) {
            return m;
        }
        final int[] i = new int[aEnum.values().length];
        try {
            i[aEnum.g.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            i[aEnum.h.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError2) {}
        try {
            i[aEnum.b.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError3) {}
        try {
            i[aEnum.f.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError4) {}
        try {
            i[aEnum.d.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError5) {}
        try {
            i[aEnum.e.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError6) {}
        try {
            i[aEnum.c.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError7) {}
        try {
            i[aEnum.a.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError8) {}
        return FT_Spi_Slave.m = i;
    }
    
    private enum aEnum
    {
        a("STATE_SYNC", 0), 
        b("STATE_CMD", 1), 
        c("STATE_SN", 2), 
        d("STATE_SIZE_HIGH", 3), 
        e("STATE_SIZE_LOW", 4), 
        f("STATE_COLLECT_DATA", 5), 
        g("STATE_CHECKSUM_HIGH", 6), 
        h("STATE_CHECKSUM_LOW", 7);
        
        static {
            aEnum[] i = new aEnum[] { aEnum.a, aEnum.b, aEnum.c, aEnum.d, aEnum.e, aEnum.f, aEnum.g, aEnum.h };
        }
        
        private aEnum(final String s, final int n) {
        }
    }
}
