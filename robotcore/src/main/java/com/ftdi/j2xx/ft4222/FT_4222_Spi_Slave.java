// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.ft4222;

import android.util.Log;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.SpiSlave;

public class FT_4222_Spi_Slave implements SpiSlave
{
    private FT_4222_Device a;
    private FT_Device b;
    private Lock c;
    
    public FT_4222_Spi_Slave(final FT_4222_Device pDevice) {
        this.a = pDevice;
        this.b = pDevice.mFtDev;
        this.c = new ReentrantLock();
    }
    
    public int init() {
        int n = 0;
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        final boolean a = true;
        final int b = 2;
        final boolean c = false;
        final boolean d = false;
        final int n2 = 0;
        final boolean e = true;
        final int n3 = 4;
        mSpiMasterCfg.a = (a ? 1 : 0);
        mSpiMasterCfg.b = b;
        mSpiMasterCfg.c = (c ? 1 : 0);
        mSpiMasterCfg.d = (d ? 1 : 0);
        mSpiMasterCfg.e = (byte)(e ? 1 : 0);
        this.c.lock();
        this.a.cleanRxData();
        if (this.b.VendorCmdSet(33, 0x42 | mSpiMasterCfg.a << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x44 | mSpiMasterCfg.b << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x45 | mSpiMasterCfg.c << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x46 | mSpiMasterCfg.d << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x43 | n2 << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x48 | mSpiMasterCfg.e << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x5 | n3 << 8) < 0) {
            n = 4;
        }
        this.c.unlock();
        mChipStatus.g = 4;
        return n;
    }
    
    public int getRxStatus(final int[] pRxSize) {
        if (pRxSize == null) {
            return 1009;
        }
        final int a = this.a();
        if (a != 0) {
            return a;
        }
        this.c.lock();
        final int queueStatus = this.b.getQueueStatus();
        this.c.unlock();
        int n;
        if (queueStatus >= 0) {
            pRxSize[0] = queueStatus;
            n = 0;
        }
        else {
            pRxSize[0] = -1;
            n = 4;
        }
        return n;
    }
    
    public int read(final byte[] buffer, final int bufferSize, final int[] sizeOfRead) {
        this.c.lock();
        if (this.b == null || !this.b.isOpen()) {
            this.c.unlock();
            return 3;
        }
        final int read = this.b.read(buffer, bufferSize);
        this.c.unlock();
        int n;
        if ((sizeOfRead[0] = read) >= 0) {
            n = 0;
        }
        else {
            n = 4;
        }
        return n;
    }
    
    public int write(final byte[] buffer, final int bufferSize, final int[] sizeTransferred) {
        if (sizeTransferred == null || buffer == null) {
            return 1009;
        }
        int a = this.a();
        if (a != 0) {
            return a;
        }
        if (bufferSize > 512) {
            return 1010;
        }
        this.c.lock();
        sizeTransferred[0] = this.b.write(buffer, bufferSize);
        this.c.unlock();
        if (sizeTransferred[0] != bufferSize) {
            Log.e("FTDI_Device::", "Error write =" + bufferSize + " tx=" + sizeTransferred[0]);
            a = 4;
        }
        return a;
    }
    
    private int a() {
        if (this.a.mChipStatus.g != 4) {
            return 1003;
        }
        return 0;
    }
    
    public int reset() {
        final int n = 0;
        int n2 = 0;
        this.c.lock();
        if (this.b.VendorCmdSet(33, 0x4A | n << 8) < 0) {
            n2 = 4;
        }
        this.c.unlock();
        return n2;
    }
    
    public int setDrivingStrength(final int clkStrength, final int ioStrength, final int ssoStregth) {
        int n = 0;
        final b mChipStatus = this.a.mChipStatus;
        if (mChipStatus.g != 3 && mChipStatus.g != 4) {
            return 1003;
        }
        final int n2 = clkStrength << 4 | ioStrength << 2 | ssoStregth;
        int n3;
        if (mChipStatus.g == 3) {
            n3 = 3;
        }
        else {
            n3 = 4;
        }
        this.c.lock();
        if (this.b.VendorCmdSet(33, 0xA0 | n2 << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x5 | n3 << 8) < 0) {
            n = 4;
        }
        this.c.unlock();
        return n;
    }
}
