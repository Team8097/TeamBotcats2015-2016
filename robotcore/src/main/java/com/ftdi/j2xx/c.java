// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class c extends k
{
    c(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)12);
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_2232H.class) {
            return 1;
        }
        final FT_EEPROM_2232H ft_EEPROM_2232H = (FT_EEPROM_2232H)ft_EEPROM;
        try {
            if (!ft_EEPROM_2232H.A_UART) {
                if (ft_EEPROM_2232H.A_FIFO) {
                    final int[] array2 = array;
                    final int n = 0;
                    array2[n] |= 0x1;
                }
                else if (ft_EEPROM_2232H.A_FIFOTarget) {
                    final int[] array3 = array;
                    final int n2 = 0;
                    array3[n2] |= 0x2;
                }
                else {
                    final int[] array4 = array;
                    final int n3 = 0;
                    array4[n3] |= 0x4;
                }
            }
            if (ft_EEPROM_2232H.A_LoadVCP) {
                final int[] array5 = array;
                final int n4 = 0;
                array5[n4] |= 0x8;
            }
            if (!ft_EEPROM_2232H.B_UART) {
                if (ft_EEPROM_2232H.B_FIFO) {
                    final int[] array6 = array;
                    final int n5 = 0;
                    array6[n5] |= 0x100;
                }
                else if (ft_EEPROM_2232H.B_FIFOTarget) {
                    final int[] array7 = array;
                    final int n6 = 0;
                    array7[n6] |= 0x200;
                }
                else {
                    final int[] array8 = array;
                    final int n7 = 0;
                    array8[n7] |= 0x400;
                }
            }
            if (ft_EEPROM_2232H.B_LoadVCP) {
                final int[] array9 = array;
                final int n8 = 0;
                array9[n8] |= 0x800;
            }
            if (ft_EEPROM_2232H.PowerSaveEnable) {
                final int[] array10 = array;
                final int n9 = 0;
                array10[n9] |= 0x8000;
            }
            array[1] = ft_EEPROM_2232H.VendorId;
            array[2] = ft_EEPROM_2232H.ProductId;
            array[3] = 1792;
            array[4] = this.a((Object)ft_EEPROM);
            array[5] = this.b(ft_EEPROM);
            array[6] = 0;
            int al_DriveCurrent = ft_EEPROM_2232H.AL_DriveCurrent;
            if (al_DriveCurrent == -1) {
                al_DriveCurrent = 0;
            }
            final int[] array11 = array;
            final int n10 = 6;
            array11[n10] |= al_DriveCurrent;
            if (ft_EEPROM_2232H.AL_SlowSlew) {
                final int[] array12 = array;
                final int n11 = 6;
                array12[n11] |= 0x4;
            }
            if (ft_EEPROM_2232H.AL_SchmittInput) {
                final int[] array13 = array;
                final int n12 = 6;
                array13[n12] |= 0x8;
            }
            int ah_DriveCurrent = ft_EEPROM_2232H.AH_DriveCurrent;
            if (ah_DriveCurrent == -1) {
                ah_DriveCurrent = 0;
            }
            final short n13 = (short)(ah_DriveCurrent << 4);
            final int[] array14 = array;
            final int n14 = 6;
            array14[n14] |= n13;
            if (ft_EEPROM_2232H.AH_SlowSlew) {
                final int[] array15 = array;
                final int n15 = 6;
                array15[n15] |= 0x40;
            }
            if (ft_EEPROM_2232H.AH_SchmittInput) {
                final int[] array16 = array;
                final int n16 = 6;
                array16[n16] |= 0x80;
            }
            int bl_DriveCurrent = ft_EEPROM_2232H.BL_DriveCurrent;
            if (bl_DriveCurrent == -1) {
                bl_DriveCurrent = 0;
            }
            final short n17 = (short)(bl_DriveCurrent << 8);
            final int[] array17 = array;
            final int n18 = 6;
            array17[n18] |= n17;
            if (ft_EEPROM_2232H.BL_SlowSlew) {
                final int[] array18 = array;
                final int n19 = 6;
                array18[n19] |= 0x400;
            }
            if (ft_EEPROM_2232H.BL_SchmittInput) {
                final int[] array19 = array;
                final int n20 = 6;
                array19[n20] |= 0x800;
            }
            final short n21 = (short)(ft_EEPROM_2232H.BH_DriveCurrent << 12);
            final int[] array20 = array;
            final int n22 = 6;
            array20[n22] |= n21;
            if (ft_EEPROM_2232H.BH_SlowSlew) {
                final int[] array21 = array;
                final int n23 = 6;
                array21[n23] |= 0x4000;
            }
            if (ft_EEPROM_2232H.BH_SchmittInput) {
                final int[] array22 = array;
                final int n24 = 6;
                array22[n24] |= 0x8000;
            }
            boolean b = false;
            int n25 = 77;
            if (this.a == 70) {
                n25 = 13;
                b = true;
            }
            final int a = this.a(ft_EEPROM_2232H.Product, array, this.a(ft_EEPROM_2232H.Manufacturer, array, n25, 7, b), 8, b);
            if (ft_EEPROM_2232H.SerNumEnable) {
                this.a(ft_EEPROM_2232H.SerialNumber, array, a, 9, b);
            }
            switch (ft_EEPROM_2232H.TPRDRV) {
                case 0: {
                    array[11] = 0;
                    break;
                }
                case 1: {
                    array[11] = 8;
                    break;
                }
                case 2: {
                    array[11] = 16;
                    break;
                }
                case 3: {
                    array[11] = 24;
                    break;
                }
                default: {
                    array[11] = 0;
                    break;
                }
            }
            array[12] = this.a;
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
        final FT_EEPROM_2232H ft_EEPROM_2232H = new FT_EEPROM_2232H();
        final int[] array = new int[this.b];
        if (this.c) {
            return ft_EEPROM_2232H;
        }
        try {
            for (short n = 0; n < this.b; ++n) {
                array[n] = this.a(n);
            }
            final int n2 = array[0];
            switch ((short)(n2 & 0x7)) {
                case 0: {
                    ft_EEPROM_2232H.A_UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232H.A_FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232H.A_FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_2232H.A_FastSerial = true;
                    break;
                }
                default: {
                    ft_EEPROM_2232H.A_UART = true;
                    break;
                }
            }
            if ((short)((n2 & 0x8) >> 3) == 1) {
                ft_EEPROM_2232H.A_LoadVCP = true;
                ft_EEPROM_2232H.A_LoadD2XX = false;
            }
            else {
                ft_EEPROM_2232H.A_LoadVCP = false;
                ft_EEPROM_2232H.A_LoadD2XX = true;
            }
            switch ((short)((n2 & 0x700) >> 8)) {
                case 0: {
                    ft_EEPROM_2232H.B_UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232H.B_FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232H.B_FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_2232H.B_FastSerial = true;
                    break;
                }
                default: {
                    ft_EEPROM_2232H.B_UART = true;
                    break;
                }
            }
            if ((short)((n2 & 0x800) >> 11) == 1) {
                ft_EEPROM_2232H.B_LoadVCP = true;
                ft_EEPROM_2232H.B_LoadD2XX = false;
            }
            else {
                ft_EEPROM_2232H.B_LoadVCP = false;
                ft_EEPROM_2232H.B_LoadD2XX = true;
            }
            if ((short)((n2 & 0x8000) >> 15) == 1) {
                ft_EEPROM_2232H.PowerSaveEnable = true;
            }
            else {
                ft_EEPROM_2232H.PowerSaveEnable = false;
            }
            ft_EEPROM_2232H.VendorId = (short)array[1];
            ft_EEPROM_2232H.ProductId = (short)array[2];
            this.a(ft_EEPROM_2232H, array[4]);
            this.a((Object)ft_EEPROM_2232H, array[5]);
            switch ((short)(array[6] & 0x3)) {
                case 0: {
                    ft_EEPROM_2232H.AL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232H.AL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232H.AL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_2232H.AL_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x4) == 4) {
                ft_EEPROM_2232H.AL_SlowSlew = true;
            }
            else {
                ft_EEPROM_2232H.AL_SlowSlew = false;
            }
            if ((short)(array[6] & 0x8) == 8) {
                ft_EEPROM_2232H.AL_SchmittInput = true;
            }
            else {
                ft_EEPROM_2232H.AL_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x30) >> 4)) {
                case 0: {
                    ft_EEPROM_2232H.AH_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232H.AH_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232H.AH_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_2232H.AH_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x40) == 64) {
                ft_EEPROM_2232H.AH_SlowSlew = true;
            }
            else {
                ft_EEPROM_2232H.AH_SlowSlew = false;
            }
            final short n3 = (short)(array[6] & 0x80);
            if (n3 == 128) {
                ft_EEPROM_2232H.AH_SchmittInput = true;
            }
            else {
                ft_EEPROM_2232H.AH_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x300) >> 8)) {
                case 0: {
                    ft_EEPROM_2232H.BL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232H.BL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232H.BL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_2232H.BL_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x400) == 1024) {
                ft_EEPROM_2232H.BL_SlowSlew = true;
            }
            else {
                ft_EEPROM_2232H.BL_SlowSlew = false;
            }
            final short n4 = (short)(array[6] & 0x800);
            if (n3 == 2048) {
                ft_EEPROM_2232H.BL_SchmittInput = true;
            }
            else {
                ft_EEPROM_2232H.BL_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x3000) >> 12)) {
                case 0: {
                    ft_EEPROM_2232H.BH_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232H.BH_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232H.BH_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_2232H.BH_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x4000) == 16384) {
                ft_EEPROM_2232H.BH_SlowSlew = true;
            }
            else {
                ft_EEPROM_2232H.BH_SlowSlew = false;
            }
            if ((short)(array[6] & 0x8000) == 32768) {
                ft_EEPROM_2232H.BH_SchmittInput = true;
            }
            else {
                ft_EEPROM_2232H.BH_SchmittInput = false;
            }
            final short tprdrv = (short)((array[11] & 0x18) >> 3);
            if (tprdrv < 4) {
                ft_EEPROM_2232H.TPRDRV = tprdrv;
            }
            else {
                ft_EEPROM_2232H.TPRDRV = 0;
            }
            int n5 = array[7] & 0xFF;
            if (this.a == 70) {
                n5 -= 128;
                ft_EEPROM_2232H.Manufacturer = this.a(n5 / 2, array);
                int n6 = array[8] & 0xFF;
                n6 -= 128;
                ft_EEPROM_2232H.Product = this.a(n6 / 2, array);
                int n7 = array[9] & 0xFF;
                n7 -= 128;
                ft_EEPROM_2232H.SerialNumber = this.a(n7 / 2, array);
            }
            else {
                ft_EEPROM_2232H.Manufacturer = this.a(n5 / 2, array);
                ft_EEPROM_2232H.Product = this.a((array[8] & 0xFF) / 2, array);
                ft_EEPROM_2232H.SerialNumber = this.a((array[9] & 0xFF) / 2, array);
            }
            return ft_EEPROM_2232H;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    int b() {
        final int a = this.a((short)9);
        int n = (a & 0xFF) / 2 + ((a & 0xFF00) >> 8) / 2;
        ++n;
        return (this.b - 1 - 1 - n) * 2;
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
