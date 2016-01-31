// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class h extends k
{
    private static FT_Device d;
    
    h(final FT_Device d) {
        super(d);
        h.d = d;
    }
    
    boolean a(final short n, final short n2) {
        final int n3 = n2 & 0xFFFF;
        final int n4 = n & 0xFFFF;
        boolean b = false;
        if (n >= 1024) {
            return b;
        }
        final byte latencyTimer = h.d.getLatencyTimer();
        h.d.setLatencyTimer((byte)119);
        if (h.d.c().controlTransfer(64, 145, n3, n4, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        h.d.setLatencyTimer(latencyTimer);
        return b;
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        if (ft_EEPROM.getClass() != FT_EEPROM_232R.class) {
            return 1;
        }
        final int[] array = new int[80];
        final FT_EEPROM_232R ft_EEPROM_232R = (FT_EEPROM_232R)ft_EEPROM;
        try {
            for (short n = 0; n < 80; ++n) {
                array[n] = this.a(n);
            }
            int n2 = 0x0 | (array[0] & 0xFF00);
            if (ft_EEPROM_232R.HighIO) {
                n2 |= 0x4;
            }
            if (ft_EEPROM_232R.LoadVCP) {
                n2 |= 0x8;
            }
            int n3;
            if (ft_EEPROM_232R.ExternalOscillator) {
                n3 = (n2 | 0x2);
            }
            else {
                n3 = (n2 & 0xFFFD);
            }
            array[0] = n3;
            array[1] = ft_EEPROM_232R.VendorId;
            array[2] = ft_EEPROM_232R.ProductId;
            array[3] = 1536;
            array[4] = this.a((Object)ft_EEPROM);
            int b = this.b(ft_EEPROM);
            if (ft_EEPROM_232R.InvertTXD) {
                b |= 0x100;
            }
            if (ft_EEPROM_232R.InvertRXD) {
                b |= 0x200;
            }
            if (ft_EEPROM_232R.InvertRTS) {
                b |= 0x400;
            }
            if (ft_EEPROM_232R.InvertCTS) {
                b |= 0x800;
            }
            if (ft_EEPROM_232R.InvertDTR) {
                b |= 0x1000;
            }
            if (ft_EEPROM_232R.InvertDSR) {
                b |= 0x2000;
            }
            if (ft_EEPROM_232R.InvertDCD) {
                b |= 0x4000;
            }
            if (ft_EEPROM_232R.InvertRI) {
                b |= 0x8000;
            }
            array[5] = b;
            array[10] = (ft_EEPROM_232R.CBus0 | ft_EEPROM_232R.CBus1 << 4 | ft_EEPROM_232R.CBus2 << 8 | ft_EEPROM_232R.CBus3 << 12);
            array[11] = ft_EEPROM_232R.CBus4;
            final int a = this.a(ft_EEPROM_232R.Product, array, this.a(ft_EEPROM_232R.Manufacturer, array, 12, 7, true), 8, true);
            if (ft_EEPROM_232R.SerNumEnable) {
                this.a(ft_EEPROM_232R.SerialNumber, array, a, 9, true);
            }
            if (array[1] == 0 || array[2] == 0) {
                return 2;
            }
            final byte latencyTimer = h.d.getLatencyTimer();
            h.d.setLatencyTimer((byte)119);
            final boolean a2 = this.a(array, 63);
            h.d.setLatencyTimer(latencyTimer);
            if (a2) {
                return 0;
            }
            return 1;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    FT_EEPROM a() {
        final FT_EEPROM_232R ft_EEPROM_232R = new FT_EEPROM_232R();
        final int[] array = new int[80];
        try {
            for (int i = 0; i < 80; ++i) {
                array[i] = this.a((short)i);
            }
            if ((array[0] & 0x4) == 0x4) {
                ft_EEPROM_232R.HighIO = true;
            }
            else {
                ft_EEPROM_232R.HighIO = false;
            }
            if ((array[0] & 0x8) == 0x8) {
                ft_EEPROM_232R.LoadVCP = true;
            }
            else {
                ft_EEPROM_232R.LoadVCP = false;
            }
            if ((array[0] & 0x2) == 0x2) {
                ft_EEPROM_232R.ExternalOscillator = true;
            }
            else {
                ft_EEPROM_232R.ExternalOscillator = false;
            }
            ft_EEPROM_232R.VendorId = (short)array[1];
            ft_EEPROM_232R.ProductId = (short)array[2];
            this.a(ft_EEPROM_232R, array[4]);
            this.a((Object)ft_EEPROM_232R, array[5]);
            if ((array[5] & 0x100) == 0x100) {
                ft_EEPROM_232R.InvertTXD = true;
            }
            else {
                ft_EEPROM_232R.InvertTXD = false;
            }
            if ((array[5] & 0x200) == 0x200) {
                ft_EEPROM_232R.InvertRXD = true;
            }
            else {
                ft_EEPROM_232R.InvertRXD = false;
            }
            if ((array[5] & 0x400) == 0x400) {
                ft_EEPROM_232R.InvertRTS = true;
            }
            else {
                ft_EEPROM_232R.InvertRTS = false;
            }
            if ((array[5] & 0x800) == 0x800) {
                ft_EEPROM_232R.InvertCTS = true;
            }
            else {
                ft_EEPROM_232R.InvertCTS = false;
            }
            if ((array[5] & 0x1000) == 0x1000) {
                ft_EEPROM_232R.InvertDTR = true;
            }
            else {
                ft_EEPROM_232R.InvertDTR = false;
            }
            if ((array[5] & 0x2000) == 0x2000) {
                ft_EEPROM_232R.InvertDSR = true;
            }
            else {
                ft_EEPROM_232R.InvertDSR = false;
            }
            if ((array[5] & 0x4000) == 0x4000) {
                ft_EEPROM_232R.InvertDCD = true;
            }
            else {
                ft_EEPROM_232R.InvertDCD = false;
            }
            if ((array[5] & 0x8000) == 0x8000) {
                ft_EEPROM_232R.InvertRI = true;
            }
            else {
                ft_EEPROM_232R.InvertRI = false;
            }
            final int n = array[10];
            ft_EEPROM_232R.CBus0 = (byte)(n & 0xF);
            ft_EEPROM_232R.CBus1 = (byte)((n & 0xF0) >> 4);
            ft_EEPROM_232R.CBus2 = (byte)((n & 0xF00) >> 8);
            ft_EEPROM_232R.CBus3 = (byte)((n & 0xF000) >> 12);
            ft_EEPROM_232R.CBus4 = (byte)(array[11] & 0xFF);
            int n2 = array[7] & 0xFF;
            n2 -= 128;
            ft_EEPROM_232R.Manufacturer = this.a(n2 / 2, array);
            int n3 = array[8] & 0xFF;
            n3 -= 128;
            ft_EEPROM_232R.Product = this.a(n3 / 2, array);
            int n4 = array[9] & 0xFF;
            n4 -= 128;
            ft_EEPROM_232R.SerialNumber = this.a(n4 / 2, array);
            return ft_EEPROM_232R;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    int b() {
        return (63 - (12 + ((this.a((short)7) & 0xFF00) >> 8) / 2 + ((this.a((short)8) & 0xFF00) >> 8) / 2 + 1) - ((this.a((short)9) & 0xFF00) >> 8) / 2 - 1) * 2;
    }
    
    int a(final byte[] array) {
        if (array.length > this.b()) {
            return 0;
        }
        final int[] array2 = new int[80];
        for (short n = 0; n < 80; ++n) {
            array2[n] = this.a(n);
        }
        short n2 = (short)((short)(63 - this.b() / 2 - 1) & 0xFFFF);
        for (int i = 0; i < array.length; i += 2) {
            int n3;
            if (i + 1 < array.length) {
                n3 = (array[i + 1] & 0xFF);
            }
            else {
                n3 = 0;
            }
            final int n4 = n3 << 8 | (array[i] & 0xFF);
            final int[] array3 = array2;
            final short n5 = n2;
            n2 = (short)(n5 + 1);
            array3[n5] = n4;
        }
        if (array2[1] == 0 || array2[2] == 0) {
            return 0;
        }
        final byte latencyTimer = h.d.getLatencyTimer();
        h.d.setLatencyTimer((byte)119);
        final boolean a = this.a(array2, 63);
        h.d.setLatencyTimer(latencyTimer);
        if (!a) {
            return 0;
        }
        return array.length;
    }
    
    byte[] a(final int n) {
        final byte[] array = new byte[n];
        if (n == 0 || n > this.b()) {
            return null;
        }
        short n2 = (short)((short)(63 - this.b() / 2 - 1) & 0xFFFF);
        for (int i = 0; i < n; i += 2) {
            final short n3 = n2;
            n2 = (short)(n3 + 1);
            final int a = this.a(n3);
            if (i + 1 < array.length) {
                array[i + 1] = (byte)(a & 0xFF);
            }
            array[i] = (byte)((a & 0xFF00) >> 8);
        }
        return array;
    }
}
