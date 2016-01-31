// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.I2cSlave;

public class FT_4222_I2c_Slave implements I2cSlave
{
    FT_4222_Device a;
    FT_Device b;
    
    public FT_4222_I2c_Slave(final FT_4222_Device ft4222Device) {
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
    
    public int init() {
        final int init = this.a.init();
        if (init != 0) {
            return init;
        }
        if (!this.a()) {
            return 1012;
        }
        final int cmdSet = this.cmdSet(5, 2);
        if (cmdSet < 0) {
            return cmdSet;
        }
        this.a.mChipStatus.g = 2;
        return 0;
    }
    
    public int reset() {
        final int wValue2 = 1;
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        return this.cmdSet(91, wValue2);
    }
    
    public int getAddress(final int[] addr) {
        final byte[] buf = { 0 };
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        if (this.b.VendorCmdGet(33, 92, buf, 1) < 0) {
            return 18;
        }
        addr[0] = buf[0];
        return 0;
    }
    
    public int setAddress(final int addr) {
        final byte[] array = { (byte)(addr & 0xFF) };
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        if (this.cmdSet(92, array[0]) < 0) {
            return 18;
        }
        return 0;
    }
    
    public int read(final byte[] buffer, final int sizeToTransfer, final int[] sizeTransferred) {
        final int[] array = { 0 };
        final long currentTimeMillis = System.currentTimeMillis();
        final int readTimeout = this.b.getReadTimeout();
        if (sizeToTransfer < 1) {
            return 6;
        }
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        final int a2 = this.a(array);
        if (a2 != 0) {
            return a2;
        }
        if (sizeToTransfer > array[0]) {
            return 1010;
        }
        sizeTransferred[0] = 0;
        int length;
        for (length = this.b.getQueueStatus(); length < sizeToTransfer && System.currentTimeMillis() - currentTimeMillis < readTimeout; length = this.b.getQueueStatus()) {}
        if (length > sizeToTransfer) {
            length = sizeToTransfer;
        }
        final int read = this.b.read(buffer, length);
        if (read < 0) {
            return 1011;
        }
        sizeTransferred[0] = read;
        return 0;
    }
    
    public int write(final byte[] buffer, final int sizeToTransfer, final int[] sizeTransferred) {
        final int[] array = { 0 };
        if (sizeToTransfer < 1) {
            return 6;
        }
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        final int a2 = this.a(array);
        if (a2 != 0) {
            return a2;
        }
        if (sizeToTransfer > array[0]) {
            return 1010;
        }
        sizeTransferred[0] = 0;
        if (sizeToTransfer == (sizeTransferred[0] = this.b.write(buffer, sizeToTransfer))) {
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
    
    int a(final int[] array) {
        array[0] = 0;
        final int maxBuckSize = this.a.getMaxBuckSize();
        switch (this.a.mChipStatus.g) {
            case 2: {
                array[0] = maxBuckSize - 4;
                return 0;
            }
            default: {
                return 17;
            }
        }
    }
}
