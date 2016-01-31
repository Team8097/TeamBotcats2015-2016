// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.I2cMaster;

public class FT_4222_I2c_Master implements I2cMaster
{
    FT_4222_Device a;
    FT_Device b;
    int c;
    
    public FT_4222_I2c_Master(final FT_4222_Device ft4222Device) {
        this.a = ft4222Device;
        this.b = this.a.mFtDev;
    }
    
    public int cmdSet(final int wValue1, final int wValue2) {
        return this.b.VendorCmdSet(33, wValue1 | wValue2 << 8);
    }
    
    public int cmdSet(final int wValue1, final int wValue2, final byte[] buf, final int datalen) {
        return this.b.VendorCmdSet(33, wValue1 | wValue2 << 8, buf, datalen);
    }
    
    public int cmdGet(final int wValue1, final int wValue2, final byte[] buf, final int datalen) {
        return this.b.VendorCmdGet(32, wValue1 | wValue2 << 8, buf, datalen);
    }
    
    public int init(final int kbps) {
        final byte[] clk = { 0 };
        final int init = this.a.init();
        if (init != 0) {
            return init;
        }
        if (!this.a()) {
            return 1012;
        }
        this.cmdSet(81, 0);
        final int clock = this.a.getClock(clk);
        if (clock != 0) {
            return clock;
        }
        final int a = this.a(clk[0], kbps);
        final int cmdSet = this.cmdSet(5, 1);
        if (cmdSet < 0) {
            return cmdSet;
        }
        this.a.mChipStatus.g = 1;
        final int cmdSet2 = this.cmdSet(82, a);
        if (cmdSet2 < 0) {
            return cmdSet2;
        }
        this.c = kbps;
        return 0;
    }
    
    public int reset() {
        final int wValue2 = 1;
        final int a = this.a(true);
        if (a != 0) {
            return a;
        }
        return this.cmdSet(81, wValue2);
    }
    
    public int read(final int deviceAddress, final byte[] buffer, final int sizeToTransfer, final int[] sizeTransferred) {
        final short n = (short)(deviceAddress & 0xFFFF);
        final short n2 = (short)((deviceAddress & 0x380) >> 7);
        final short n3 = (short)sizeToTransfer;
        final int[] array = { 0 };
        final byte[] data = new byte[4];
        final long currentTimeMillis = System.currentTimeMillis();
        final int readTimeout = this.b.getReadTimeout();
        final int a = this.a(deviceAddress);
        if (a != 0) {
            return a;
        }
        if (sizeToTransfer < 1) {
            return 6;
        }
        final int a2 = this.a(true);
        if (a2 != 0) {
            return a2;
        }
        final int a3 = this.a(array);
        if (a3 != 0) {
            return a3;
        }
        if (sizeToTransfer > array[0]) {
            return 1010;
        }
        data[sizeTransferred[0] = 0] = (byte)((n << 1) + 1);
        data[1] = (byte)n2;
        data[2] = (byte)(n3 >> 8 & 0xFF);
        data[3] = (byte)(n3 & 0xFF);
        if (4 != this.b.write(data, 4)) {
            return 1011;
        }
        int length;
        for (length = this.b.getQueueStatus(); length < sizeToTransfer && System.currentTimeMillis() - currentTimeMillis < readTimeout; length = this.b.getQueueStatus()) {}
        if (length > sizeToTransfer) {
            length = sizeToTransfer;
        }
        if ((sizeTransferred[0] = this.b.read(buffer, length)) >= 0) {
            return 0;
        }
        return 1011;
    }
    
    public int write(final int deviceAddress, final byte[] buffer, final int sizeToTransfer, final int[] sizeTransferred) {
        final short n = (short)deviceAddress;
        final short n2 = (short)((deviceAddress & 0x380) >> 7);
        final short n3 = (short)sizeToTransfer;
        final byte[] data = new byte[sizeToTransfer + 4];
        final int[] array = { 0 };
        final int a = this.a(deviceAddress);
        if (a != 0) {
            return a;
        }
        if (sizeToTransfer < 1) {
            return 6;
        }
        final int a2 = this.a(true);
        if (a2 != 0) {
            return a2;
        }
        final int a3 = this.a(array);
        if (a3 != 0) {
            return a3;
        }
        if (sizeToTransfer > array[0]) {
            return 1010;
        }
        data[sizeTransferred[0] = 0] = (byte)(n << 1);
        data[1] = (byte)n2;
        data[2] = (byte)(n3 >> 8 & 0xFF);
        data[3] = (byte)(n3 & 0xFF);
        for (int i = 0; i < sizeToTransfer; ++i) {
            data[i + 4] = buffer[i];
        }
        sizeTransferred[0] = this.b.write(data, sizeToTransfer + 4) - 4;
        if (sizeToTransfer == sizeTransferred[0]) {
            return 0;
        }
        return 10;
    }
    
    boolean a() {
        return this.a.mChipStatus.a == 0 || this.a.mChipStatus.a == 3;
    }
    
    int a(final boolean b) {
        if (b) {
            if (this.a.mChipStatus.g != 1) {
                return 1004;
            }
        }
        else if (this.a.mChipStatus.g != 2) {
            return 1004;
        }
        return 0;
    }
    
    int a(final int n) {
        if ((n & 0xFC00) > 0) {
            return 1007;
        }
        return 0;
    }
    
    private int a(final int n, final int n2) {
        double n3 = 0.0;
        switch (n) {
            default: {
                n3 = 16.666666666666668;
                break;
            }
            case 1: {
                n3 = 41.666666666666664;
                break;
            }
            case 2: {
                n3 = 20.833333333333332;
                break;
            }
            case 3: {
                n3 = 12.5;
                break;
            }
        }
        int n5;
        if (60 <= n2 && n2 <= 100) {
            int n4 = (int)(1000000.0 / n2 / (8.0 * n3) - 1.0 + 0.5);
            if (n4 > 127) {
                n4 = 127;
            }
            n5 = n4;
        }
        else if (100 < n2 && n2 <= 400) {
            n5 = ((int)(1000000.0 / n2 / (6.0 * n3) - 1.0 + 0.5) | 0xC0);
        }
        else if (400 < n2 && n2 <= 1000) {
            n5 = ((int)(1000000.0 / n2 / (6.0 * n3) - 1.0 + 0.5) | 0xC0);
        }
        else if (1000 < n2 && n2 <= 3400) {
            n5 = (((int)(1000000.0 / n2 / (6.0 * n3) - 1.0 + 0.5) | 0x80) & 0xFFFFFFBF);
        }
        else {
            n5 = 74;
        }
        return n5;
    }
    
    int a(final int[] array) {
        array[0] = 0;
        final int maxBuckSize = this.a.getMaxBuckSize();
        switch (this.a.mChipStatus.g) {
            case 1: {
                array[0] = maxBuckSize - 4;
                return 0;
            }
            default: {
                return 17;
            }
        }
    }
}
