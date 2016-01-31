// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx.ft4222;

import android.util.Log;
import junit.framework.Assert;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.SpiMaster;

public class FT_4222_Spi_Master implements SpiMaster
{
    private FT_4222_Device a;
    private FT_Device b;
    
    public FT_4222_Spi_Master(final FT_4222_Device pDevice) {
        this.a = pDevice;
        this.b = pDevice.mFtDev;
    }
    
    public int init(final int ioLine, final int clock, final int cpol, final int cpha, final byte ssoMap) {
        final b mChipStatus = this.a.mChipStatus;
        byte b = 0;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        mSpiMasterCfg.a = ioLine;
        mSpiMasterCfg.b = clock;
        mSpiMasterCfg.c = cpol;
        mSpiMasterCfg.d = cpha;
        mSpiMasterCfg.e = ssoMap;
        if (mSpiMasterCfg.a != 1 && mSpiMasterCfg.a != 2 && mSpiMasterCfg.a != 4) {
            return 6;
        }
        this.a.cleanRxData();
        switch (mChipStatus.a) {
            case 0: {
                b = 1;
                break;
            }
            case 1: {
                b = 7;
                break;
            }
            case 2: {
                b = 15;
                break;
            }
            case 3: {
                b = 1;
                break;
            }
        }
        if ((b & mSpiMasterCfg.e) == 0x0) {
            return 6;
        }
        final a a = mSpiMasterCfg;
        a.e &= b;
        final int n = 0;
        final int n2 = 3;
        if (this.b.VendorCmdSet(33, 0x42 | mSpiMasterCfg.a << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x44 | mSpiMasterCfg.b << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x45 | mSpiMasterCfg.c << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x46 | mSpiMasterCfg.d << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x43 | n << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x48 | mSpiMasterCfg.e << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x5 | n2 << 8) < 0) {
            return 4;
        }
        mChipStatus.g = 3;
        return 0;
    }
    
    public int setLines(final int spiMode) {
        final int n = 1;
        if (this.a.mChipStatus.g != 3) {
            return 1003;
        }
        if (spiMode == 0) {
            return 17;
        }
        if (this.b.VendorCmdSet(33, 0x42 | spiMode << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x4A | n << 8) < 0) {
            return 4;
        }
        this.a.mSpiMasterCfg.a = spiMode;
        return 0;
    }
    
    public int singleWrite(final byte[] writeBuffer, final int sizeToTransfer, final int[] sizeTransferred, final boolean isEndTransaction) {
        return this.singleReadWrite(new byte[writeBuffer.length], writeBuffer, sizeToTransfer, sizeTransferred, isEndTransaction);
    }
    
    public int singleRead(final byte[] readBuffer, final int sizeToTransfer, final int[] sizeOfRead, final boolean isEndTransaction) {
        return this.singleReadWrite(readBuffer, new byte[readBuffer.length], sizeToTransfer, sizeOfRead, isEndTransaction);
    }
    
    public int singleReadWrite(final byte[] readBuffer, final byte[] writeBuffer, final int sizeToTransfer, final int[] sizeTransferred, final boolean isEndTransaction) {
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        if (writeBuffer == null || readBuffer == null || sizeTransferred == null) {
            return 1009;
        }
        sizeTransferred[0] = 0;
        if (mChipStatus.g != 3 || mSpiMasterCfg.a != 1) {
            return 1005;
        }
        if (sizeToTransfer == 0) {
            return 6;
        }
        if (sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length) {
            Assert.assertTrue("sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length", false);
        }
        if (writeBuffer.length != readBuffer.length || writeBuffer.length == 0) {
            Assert.assertTrue("writeBuffer.length != readBuffer.length || writeBuffer.length == 0", false);
        }
        sizeTransferred[0] = this.a(this.b, writeBuffer, readBuffer, sizeToTransfer);
        if (isEndTransaction) {
            this.b.write(null, 0);
        }
        return 0;
    }
    
    public int multiReadWrite(final byte[] readBuffer, final byte[] writeBuffer, final int singleWriteBytes, final int multiWriteBytes, final int multiReadBytes, final int[] sizeOfRead) {
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        if (multiReadBytes > 0 && readBuffer == null) {
            return 1009;
        }
        if (singleWriteBytes + multiWriteBytes > 0 && writeBuffer == null) {
            return 1009;
        }
        if (multiReadBytes > 0 && sizeOfRead == null) {
            return 1009;
        }
        if (mChipStatus.g != 3 || mSpiMasterCfg.a == 1) {
            return 1006;
        }
        if (singleWriteBytes > 15) {
            Log.e("FTDI_Device::", "The maxium single write bytes are 15 bytes");
            return 6;
        }
        final byte[] array = new byte[5 + singleWriteBytes + multiWriteBytes];
        array[0] = (byte)(0x80 | (singleWriteBytes & 0xF));
        array[1] = (byte)((multiWriteBytes & 0xFF00) >> 8);
        array[2] = (byte)(multiWriteBytes & 0xFF);
        array[3] = (byte)((multiReadBytes & 0xFF00) >> 8);
        array[4] = (byte)(multiReadBytes & 0xFF);
        for (int i = 0; i < singleWriteBytes + multiWriteBytes; ++i) {
            array[i + 5] = writeBuffer[i];
        }
        sizeOfRead[0] = this.a(this.b, array, readBuffer);
        return 0;
    }
    
    public int reset() {
        if (this.b.VendorCmdSet(33, 0x4A | 0 << 8) < 0) {
            return 4;
        }
        return 0;
    }
    
    public int setDrivingStrength(final int clkStrength, final int ioStrength, final int ssoStregth) {
        final b mChipStatus = this.a.mChipStatus;
        if (mChipStatus.g != 3 && mChipStatus.g != 4) {
            return 1003;
        }
        final int n = clkStrength << 4 | ioStrength << 2 | ssoStregth;
        int n2;
        if (mChipStatus.g == 3) {
            n2 = 3;
        }
        else {
            n2 = 4;
        }
        if (this.b.VendorCmdSet(33, 0xA0 | n << 8) < 0) {
            return 4;
        }
        if (this.b.VendorCmdSet(33, 0x5 | n2 << 8) < 0) {
            return 4;
        }
        return 0;
    }
    
    private int a(final FT_Device ft_Device, final byte[] data, final byte[] array) {
        int n = 0;
        final int n2 = 10;
        final int n3 = 30000;
        int n4 = 0;
        if (ft_Device == null || !ft_Device.isOpen()) {
            return -1;
        }
        ft_Device.write(data, data.length);
        while (n4 < array.length && n < n3) {
            final int queueStatus = ft_Device.getQueueStatus();
            if (queueStatus > 0) {
                n = 0;
                final byte[] data2 = new byte[queueStatus];
                final int read = ft_Device.read(data2, queueStatus);
                Assert.assertEquals(data2.length == read, true);
                for (int i = 0; i < data2.length; ++i) {
                    if (n4 + i < array.length) {
                        array[n4 + i] = data2[i];
                    }
                }
                n4 += read;
            }
            try {
                Thread.sleep(n2);
                n += n2;
            }
            catch (InterruptedException ex) {
                n = n3;
            }
        }
        if (array.length != n4 || n > n3) {
            Log.e("FTDI_Device::", "MultiReadWritePackage timeout!!!!");
            return -1;
        }
        return n4;
    }
    
    private int a(final FT_Device ft_Device, final byte[] array, final byte[] array2, final int n) {
        final byte[] array3 = new byte[16384];
        final byte[] array4 = new byte[array3.length];
        final int n2 = n / array3.length;
        final int n3 = n % array3.length;
        int n4 = 0;
        int n5 = 0;
        for (int i = 0; i < n2; ++i) {
            for (int j = 0; j < array3.length; ++j) {
                array3[j] = array[n5];
                ++n5;
            }
            if (this.b(ft_Device, array3, array4) <= 0) {
                return -1;
            }
            for (int k = 0; k < array4.length; ++k) {
                array2[n4] = array4[k];
                ++n4;
            }
        }
        if (n3 > 0) {
            final byte[] array5 = new byte[n3];
            final byte[] array6 = new byte[array5.length];
            for (int l = 0; l < array5.length; ++l) {
                array5[l] = array[n5];
                ++n5;
            }
            if (this.b(ft_Device, array5, array6) <= 0) {
                return -1;
            }
            for (int n6 = 0; n6 < array6.length; ++n6) {
                array2[n4] = array6[n6];
                ++n4;
            }
        }
        return n4;
    }
    
    private int b(final FT_Device ft_Device, final byte[] data, final byte[] array) {
        int n = 0;
        final int n2 = 10;
        final int n3 = 30000;
        int n4 = 0;
        if (ft_Device == null || !ft_Device.isOpen()) {
            return -1;
        }
        Assert.assertEquals(data.length == array.length, true);
        if (data.length != ft_Device.write(data, data.length)) {
            Log.e("FTDI_Device::", "setReadWritePackage Incomplete Write Error!!!");
            return -1;
        }
        while (n4 < array.length && n < n3) {
            final int queueStatus = ft_Device.getQueueStatus();
            if (queueStatus > 0) {
                n = 0;
                final byte[] data2 = new byte[queueStatus];
                final int read = ft_Device.read(data2, queueStatus);
                Assert.assertEquals(data2.length == read, true);
                for (int i = 0; i < data2.length; ++i) {
                    if (n4 + i < array.length) {
                        array[n4 + i] = data2[i];
                    }
                }
                n4 += read;
            }
            try {
                Thread.sleep(n2);
                n += n2;
            }
            catch (InterruptedException ex) {
                n = n3;
            }
        }
        if (array.length != n4 || n > n3) {
            Log.e("FTDI_Device::", "SingleReadWritePackage timeout!!!!");
            return -1;
        }
        return n4;
    }
}
