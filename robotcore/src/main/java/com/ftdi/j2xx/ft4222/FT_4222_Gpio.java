// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.Gpio;

public class FT_4222_Gpio implements Gpio
{
    private FT_4222_Device b;
    private FT_Device c;
    boolean a;
    
    public FT_4222_Gpio(final FT_4222_Device ft4222Device) {
        this.a = true;
        this.b = ft4222Device;
        this.c = this.b.mFtDev;
    }
    
    public int cmdSet(final int wValue1, final int wValue2) {
        return this.c.VendorCmdSet(33, wValue1 | wValue2 << 8);
    }
    
    public int cmdSet(final int wValue1, final int wValue2, final byte[] buf, final int datalen) {
        return this.c.VendorCmdSet(33, wValue1 | wValue2 << 8, buf, datalen);
    }
    
    public int cmdGet(final int wValue1, final int wValue2, final byte[] buf, final int datalen) {
        return this.c.VendorCmdGet(32, wValue1 | wValue2 << 8, buf, datalen);
    }
    
    public int init(final int[] gpio) {
        final b mChipStatus = this.b.mChipStatus;
        final d d = new d();
        final byte[] array = { 0 };
        final e e = new e();
        this.cmdSet(7, 0);
        this.cmdSet(6, 0);
        final int init = this.b.init();
        if (init != 0) {
            Log.e("GPIO_M", "FT4222_GPIO init - 1 NG ftStatus:" + init);
            return init;
        }
        if (mChipStatus.a == 2 || mChipStatus.a == 3) {
            return 1013;
        }
        this.a(d);
        byte c = d.c;
        array[0] = d.d[0];
        for (int i = 0; i < 4; ++i) {
            if (gpio[i] == 1) {
                c = (byte)((c | 1 << i) & 0xF);
            }
            else {
                c = (byte)(c & ~(1 << i) & 0xF);
            }
        }
        e.c = array[0];
        this.cmdSet(33, c);
        return 0;
    }
    
    public int read(final int portNum, final boolean[] bValue) {
        final d d = new d();
        final int a = this.a(portNum);
        if (a != 0) {
            return a;
        }
        final int a2 = this.a(d);
        if (a2 != 0) {
            return a2;
        }
        this.a(portNum, d.d[0], bValue);
        return 0;
    }
    
    public int newRead(final int portNum, final boolean[] bValue) {
        final int a = this.a(portNum);
        if (a != 0) {
            return a;
        }
        final int queueStatus = this.c.getQueueStatus();
        if (queueStatus > 0) {
            final byte[] data = new byte[queueStatus];
            this.c.read(data, queueStatus);
            this.a(portNum, data[queueStatus - 1], bValue);
            return queueStatus;
        }
        return -1;
    }
    
    public int write(final int portNum, final boolean bValue) {
        final d d = new d();
        final int a = this.a(portNum);
        if (a != 0) {
            return a;
        }
        if (!this.c(portNum)) {
            return 1015;
        }
        this.a(d);
        if (bValue) {
            final byte[] d2 = d.d;
            final int n = 0;
            d2[n] |= (byte)(1 << portNum);
        }
        else {
            final byte[] d3 = d.d;
            final int n2 = 0;
            d3[n2] &= (byte)(~(1 << portNum) & 0xF);
        }
        return this.c.write(d.d, 1);
    }
    
    public int newWrite(final int portNum, final boolean bValue) {
        final d d = new d();
        final int a = this.a(portNum);
        if (a != 0) {
            return a;
        }
        if (!this.c(portNum)) {
            return 1015;
        }
        this.a(d);
        if (bValue) {
            final byte[] d2 = d.d;
            final int n = 0;
            d2[n] |= (byte)(1 << portNum);
        }
        else {
            final byte[] d3 = d.d;
            final int n2 = 0;
            d3[n2] &= (byte)(~(1 << portNum) & 0xF);
        }
        if (this.a) {
            final byte[] d4 = d.d;
            final int n3 = 0;
            d4[n3] |= 0x8;
        }
        else {
            final byte[] d5 = d.d;
            final int n4 = 0;
            d5[n4] &= 0x7;
        }
        final int write = this.c.write(d.d, 1);
        this.a = !this.a;
        return write;
    }
    
    int a(final int n) {
        final b mChipStatus = this.b.mChipStatus;
        if (mChipStatus.a == 2 || mChipStatus.a == 3) {
            return 1013;
        }
        if (n >= 4) {
            return 1014;
        }
        return 0;
    }
    
    int a(final d d) {
        final byte[] buf = new byte[8];
        final int cmdGet = this.cmdGet(32, 0, buf, 8);
        d.a.a = buf[0];
        d.a.b = buf[1];
        d.b = buf[5];
        d.c = buf[6];
        d.d[0] = buf[7];
        if (cmdGet == 8) {
            return 0;
        }
        return cmdGet;
    }
    
    void a(final int n, final byte b, final boolean[] array) {
        array[0] = this.d((b & 1 << n) >> n & 0x1);
    }
    
    boolean b(final int n) {
        final b mChipStatus = this.b.mChipStatus;
        boolean b = true;
        switch (mChipStatus.a) {
            case 0: {
                if ((n == 0 || n == 1) && (mChipStatus.g == 1 || mChipStatus.g == 2)) {
                    b = false;
                }
                if (this.d(mChipStatus.i) && n == 2) {
                    b = false;
                }
                if (this.d(mChipStatus.j) && n == 3) {
                    b = false;
                    break;
                }
                break;
            }
            case 1: {
                if (n == 0 || n == 1) {
                    b = false;
                }
                if (this.d(mChipStatus.i) && n == 2) {
                    b = false;
                }
                if (this.d(mChipStatus.j) && n == 3) {
                    b = false;
                    break;
                }
                break;
            }
            case 2:
            case 3: {
                b = false;
                break;
            }
        }
        return b;
    }
    
    boolean c(final int n) {
        final d d = new d();
        boolean b = this.b(n);
        this.a(d);
        if (b && (d.c >> n & 0x1) != 0x1) {
            b = false;
        }
        return b;
    }
    
    boolean d(final int n) {
        return n != 0;
    }
}
