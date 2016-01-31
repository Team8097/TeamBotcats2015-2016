// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.interfaces.Gpio;
import com.ftdi.j2xx.interfaces.SpiSlave;
import com.ftdi.j2xx.interfaces.SpiMaster;
import com.ftdi.j2xx.interfaces.I2cSlave;
import com.ftdi.j2xx.interfaces.I2cMaster;
import com.ftdi.j2xx.FT_Device;

public class FT_4222_Device
{
    protected String TAG;
    protected FT_Device mFtDev;
    protected b mChipStatus;
    protected a mSpiMasterCfg;
    protected e mGpio;
    
    public FT_4222_Device(final FT_Device ftDev) {
        this.TAG = "FT4222";
        this.mFtDev = ftDev;
        this.mChipStatus = new b();
        this.mSpiMasterCfg = new a();
        this.mGpio = new e();
    }
    
    public int init() {
        final byte[] buf = new byte[13];
        if (this.mFtDev.VendorCmdGet(32, 1, buf, 13) != 13) {
            return 18;
        }
        this.mChipStatus.a(buf);
        return 0;
    }
    
    public int setClock(final byte clk) {
        if (clk == this.mChipStatus.f) {
            return 0;
        }
        final int vendorCmdSet = this.mFtDev.VendorCmdSet(33, 0x4 | clk << 8);
        if (vendorCmdSet == 0) {
            this.mChipStatus.f = clk;
        }
        return vendorCmdSet;
    }
    
    public int getClock(final byte[] clk) {
        if (this.mFtDev.VendorCmdGet(32, 4, clk, 1) >= 0) {
            this.mChipStatus.f = clk[0];
            return 0;
        }
        return 18;
    }
    
    public boolean cleanRxData() {
        final int queueStatus = this.mFtDev.getQueueStatus();
        if (queueStatus > 0) {
            final byte[] data = new byte[queueStatus];
            if (this.mFtDev.read(data, queueStatus) != data.length) {
                return false;
            }
        }
        return true;
    }
    
    protected int getMaxBuckSize() {
        if (this.mChipStatus.c != 0) {
            return 64;
        }
        switch (this.mChipStatus.a) {
            default: {
                return 512;
            }
            case 1:
            case 2: {
                return 256;
            }
        }
    }
    
    public boolean isFT4222Device() {
        if (this.mFtDev != null) {
            switch (this.mFtDev.getDeviceInfo().bcdDevice & 0xFF00) {
                case 6144: {
                    this.mFtDev.getDeviceInfo().type = 10;
                    return true;
                }
                case 6400: {
                    this.mFtDev.getDeviceInfo().type = 11;
                    return true;
                }
                case 5888: {
                    this.mFtDev.getDeviceInfo().type = 12;
                    return true;
                }
            }
        }
        return false;
    }
    
    public I2cMaster getI2cMasterDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_I2c_Master(this);
    }
    
    public I2cSlave getI2cSlaveDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_I2c_Slave(this);
    }
    
    public SpiMaster getSpiMasterDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_Spi_Master(this);
    }
    
    public SpiSlave getSpiSlaveDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_Spi_Slave(this);
    }
    
    public Gpio getGpioDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_Gpio(this);
    }
}
