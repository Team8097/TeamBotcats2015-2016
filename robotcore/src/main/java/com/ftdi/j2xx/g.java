// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class g extends k
{
    g(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)15);
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_232H.class) {
            return 1;
        }
        final FT_EEPROM_232H ft_EEPROM_232H = (FT_EEPROM_232H)ft_EEPROM;
        try {
            if (ft_EEPROM_232H.FIFO) {
                final int[] array2 = array;
                final int n = 0;
                array2[n] |= 0x1;
            }
            else if (ft_EEPROM_232H.FIFOTarget) {
                final int[] array3 = array;
                final int n2 = 0;
                array3[n2] |= 0x2;
            }
            else if (ft_EEPROM_232H.FastSerial) {
                final int[] array4 = array;
                final int n3 = 0;
                array4[n3] |= 0x4;
            }
            if (ft_EEPROM_232H.FT1248) {
                final int[] array5 = array;
                final int n4 = 0;
                array5[n4] |= 0x8;
            }
            if (ft_EEPROM_232H.LoadVCP) {
                final int[] array6 = array;
                final int n5 = 0;
                array6[n5] |= 0x10;
            }
            if (ft_EEPROM_232H.FT1248ClockPolarity) {
                final int[] array7 = array;
                final int n6 = 0;
                array7[n6] |= 0x100;
            }
            if (ft_EEPROM_232H.FT1248LSB) {
                final int[] array8 = array;
                final int n7 = 0;
                array8[n7] |= 0x200;
            }
            if (ft_EEPROM_232H.FT1248FlowControl) {
                final int[] array9 = array;
                final int n8 = 0;
                array9[n8] |= 0x400;
            }
            if (ft_EEPROM_232H.PowerSaveEnable) {
                final int[] array10 = array;
                final int n9 = 0;
                array10[n9] |= 0x8000;
            }
            array[1] = ft_EEPROM_232H.VendorId;
            array[2] = ft_EEPROM_232H.ProductId;
            array[3] = 2304;
            array[4] = this.a((Object)ft_EEPROM);
            array[5] = this.b(ft_EEPROM);
            int al_DriveCurrent = ft_EEPROM_232H.AL_DriveCurrent;
            if (al_DriveCurrent == -1) {
                al_DriveCurrent = 0;
            }
            final int[] array11 = array;
            final int n10 = 6;
            array11[n10] |= al_DriveCurrent;
            if (ft_EEPROM_232H.AL_SlowSlew) {
                final int[] array12 = array;
                final int n11 = 6;
                array12[n11] |= 0x4;
            }
            if (ft_EEPROM_232H.AL_SchmittInput) {
                final int[] array13 = array;
                final int n12 = 6;
                array13[n12] |= 0x8;
            }
            int bl_DriveCurrent = ft_EEPROM_232H.BL_DriveCurrent;
            if (bl_DriveCurrent == -1) {
                bl_DriveCurrent = 0;
            }
            final int[] array14 = array;
            final int n13 = 6;
            array14[n13] |= (short)(bl_DriveCurrent << 8);
            if (ft_EEPROM_232H.BL_SlowSlew) {
                final int[] array15 = array;
                final int n14 = 6;
                array15[n14] |= 0x400;
            }
            if (ft_EEPROM_232H.BL_SchmittInput) {
                final int[] array16 = array;
                final int n15 = 6;
                array16[n15] |= 0x800;
            }
            final int a = this.a(ft_EEPROM_232H.Product, array, this.a(ft_EEPROM_232H.Manufacturer, array, 80, 7, false), 8, false);
            if (ft_EEPROM_232H.SerNumEnable) {
                this.a(ft_EEPROM_232H.SerialNumber, array, a, 9, false);
            }
            array[10] = 0;
            array[12] = (array[11] = 0);
            array[12] = (ft_EEPROM_232H.CBus0 | ft_EEPROM_232H.CBus1 << 4 | ft_EEPROM_232H.CBus2 << 8 | ft_EEPROM_232H.CBus3 << 12);
            array[13] = 0;
            array[13] = (ft_EEPROM_232H.CBus4 | ft_EEPROM_232H.CBus5 << 4 | ft_EEPROM_232H.CBus6 << 8 | ft_EEPROM_232H.CBus7 << 12);
            array[14] = 0;
            array[14] = (ft_EEPROM_232H.CBus8 | ft_EEPROM_232H.CBus9 << 4);
            array[15] = this.a;
            array[69] = 72;
            if (this.a == 70) {
                return 1;
            }
            if (array[1] == 0 || array[2] == 0) {
                return 2;
            }
            if (this.a(array, this.b - 1)) {
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
        final FT_EEPROM_232H ft_EEPROM_232H = new FT_EEPROM_232H();
        final int[] array = new int[this.b];
        if (this.c) {
            return ft_EEPROM_232H;
        }
        try {
            for (short n = 0; n < this.b; ++n) {
                array[n] = this.a(n);
            }
            ft_EEPROM_232H.UART = false;
            switch (array[0] & 0xF) {
                case 0: {
                    ft_EEPROM_232H.UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_232H.FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_232H.FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_232H.FastSerial = true;
                    break;
                }
                case 8: {
                    ft_EEPROM_232H.FT1248 = true;
                    break;
                }
                default: {
                    ft_EEPROM_232H.UART = true;
                    break;
                }
            }
            if ((array[0] & 0x10) > 0) {
                ft_EEPROM_232H.LoadVCP = true;
                ft_EEPROM_232H.LoadD2XX = false;
            }
            else {
                ft_EEPROM_232H.LoadVCP = false;
                ft_EEPROM_232H.LoadD2XX = true;
            }
            if ((array[0] & 0x100) > 0) {
                ft_EEPROM_232H.FT1248ClockPolarity = true;
            }
            else {
                ft_EEPROM_232H.FT1248ClockPolarity = false;
            }
            if ((array[0] & 0x200) > 0) {
                ft_EEPROM_232H.FT1248LSB = true;
            }
            else {
                ft_EEPROM_232H.FT1248LSB = false;
            }
            if ((array[0] & 0x400) > 0) {
                ft_EEPROM_232H.FT1248FlowControl = true;
            }
            else {
                ft_EEPROM_232H.FT1248FlowControl = false;
            }
            if ((array[0] & 0x8000) > 0) {
                ft_EEPROM_232H.PowerSaveEnable = true;
            }
            ft_EEPROM_232H.VendorId = (short)array[1];
            ft_EEPROM_232H.ProductId = (short)array[2];
            this.a(ft_EEPROM_232H, array[4]);
            this.a((Object)ft_EEPROM_232H, array[5]);
            switch (array[6] & 0x3) {
                case 0: {
                    ft_EEPROM_232H.AL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_232H.AL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_232H.AL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_232H.AL_DriveCurrent = 3;
                    break;
                }
            }
            if ((array[6] & 0x4) > 0) {
                ft_EEPROM_232H.AL_SlowSlew = true;
            }
            else {
                ft_EEPROM_232H.AL_SlowSlew = false;
            }
            if ((array[6] & 0x8) > 0) {
                ft_EEPROM_232H.AL_SchmittInput = true;
            }
            else {
                ft_EEPROM_232H.AL_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x300) >> 8)) {
                case 0: {
                    ft_EEPROM_232H.BL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_232H.BL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_232H.BL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_232H.BL_DriveCurrent = 3;
                    break;
                }
            }
            if ((array[6] & 0x400) > 0) {
                ft_EEPROM_232H.BL_SlowSlew = true;
            }
            else {
                ft_EEPROM_232H.BL_SlowSlew = false;
            }
            if ((array[6] & 0x800) > 0) {
                ft_EEPROM_232H.BL_SchmittInput = true;
            }
            else {
                ft_EEPROM_232H.BL_SchmittInput = false;
            }
            ft_EEPROM_232H.CBus0 = (byte)(array[12] >> 0 & 0xF);
            ft_EEPROM_232H.CBus1 = (byte)(array[12] >> 4 & 0xF);
            ft_EEPROM_232H.CBus2 = (byte)(array[12] >> 8 & 0xF);
            ft_EEPROM_232H.CBus3 = (byte)(array[12] >> 12 & 0xF);
            ft_EEPROM_232H.CBus4 = (byte)(array[13] >> 0 & 0xF);
            ft_EEPROM_232H.CBus5 = (byte)(array[13] >> 4 & 0xF);
            ft_EEPROM_232H.CBus6 = (byte)(array[13] >> 8 & 0xF);
            ft_EEPROM_232H.CBus7 = (byte)(array[13] >> 12 & 0xF);
            ft_EEPROM_232H.CBus8 = (byte)(array[14] >> 0 & 0xF);
            ft_EEPROM_232H.CBus9 = (byte)(array[14] >> 4 & 0xF);
            ft_EEPROM_232H.Manufacturer = this.a((array[7] & 0xFF) / 2, array);
            ft_EEPROM_232H.Product = this.a((array[8] & 0xFF) / 2, array);
            ft_EEPROM_232H.SerialNumber = this.a((array[9] & 0xFF) / 2, array);
            return ft_EEPROM_232H;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    int b() {
        final int a = this.a((short)9);
        int n = (a & 0xFF) / 2;
        ++n;
        int n2 = ((a & 0xFF00) >> 8) / 2;
        ++n2;
        return (this.b - n - 1 - n2) * 2;
    }
    
    int a(final byte[] array) {
        if (array.length > this.b()) {
            return 0;
        }
        final int[] array2 = new int[this.b];
        for (short n = 0; n < this.b; ++n) {
            array2[n] = this.a(n);
        }
        short n2 = (short)(this.b - this.b() / 2 - 1 - 1);
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
        if (!this.a(array2, this.b - 1)) {
            return 0;
        }
        return array.length;
    }
    
    byte[] a(final int n) {
        final byte[] array = new byte[n];
        if (n == 0 || n > this.b()) {
            return null;
        }
        short n2 = (short)(this.b - this.b() / 2 - 1 - 1);
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
