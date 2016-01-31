// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class j extends k
{
    j(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)12);
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_4232H.class) {
            return 1;
        }
        final FT_EEPROM_4232H ft_EEPROM_4232H = (FT_EEPROM_4232H)ft_EEPROM;
        try {
            array[0] = 0;
            if (ft_EEPROM_4232H.AL_LoadVCP) {
                final int[] array2 = array;
                final int n = 0;
                array2[n] |= 0x8;
            }
            if (ft_EEPROM_4232H.BL_LoadVCP) {
                final int[] array3 = array;
                final int n2 = 0;
                array3[n2] |= 0x80;
            }
            if (ft_EEPROM_4232H.AH_LoadVCP) {
                final int[] array4 = array;
                final int n3 = 0;
                array4[n3] |= 0x800;
            }
            if (ft_EEPROM_4232H.BH_LoadVCP) {
                final int[] array5 = array;
                final int n4 = 0;
                array5[n4] |= 0x8000;
            }
            array[1] = ft_EEPROM_4232H.VendorId;
            array[2] = ft_EEPROM_4232H.ProductId;
            array[3] = 2048;
            array[4] = this.a((Object)ft_EEPROM);
            array[5] = this.b(ft_EEPROM);
            if (ft_EEPROM_4232H.AL_LoadRI_RS485) {
                array[5] = (short)(array[5] | 0x1000);
            }
            if (ft_EEPROM_4232H.AH_LoadRI_RS485) {
                array[5] = (short)(array[5] | 0x2000);
            }
            if (ft_EEPROM_4232H.BL_LoadRI_RS485) {
                array[5] = (short)(array[5] | 0x4000);
            }
            if (ft_EEPROM_4232H.BH_LoadRI_RS485) {
                array[5] = (short)(array[5] | 0x8000);
            }
            array[6] = 0;
            int al_DriveCurrent = ft_EEPROM_4232H.AL_DriveCurrent;
            if (al_DriveCurrent == -1) {
                al_DriveCurrent = 0;
            }
            final int[] array6 = array;
            final int n5 = 6;
            array6[n5] |= al_DriveCurrent;
            if (ft_EEPROM_4232H.AL_SlowSlew) {
                final int[] array7 = array;
                final int n6 = 6;
                array7[n6] |= 0x4;
            }
            if (ft_EEPROM_4232H.AL_SchmittInput) {
                final int[] array8 = array;
                final int n7 = 6;
                array8[n7] |= 0x8;
            }
            int ah_DriveCurrent = ft_EEPROM_4232H.AH_DriveCurrent;
            if (ah_DriveCurrent == -1) {
                ah_DriveCurrent = 0;
            }
            final short n8 = (short)(ah_DriveCurrent << 4);
            final int[] array9 = array;
            final int n9 = 6;
            array9[n9] |= n8;
            if (ft_EEPROM_4232H.AH_SlowSlew) {
                final int[] array10 = array;
                final int n10 = 6;
                array10[n10] |= 0x40;
            }
            if (ft_EEPROM_4232H.AH_SchmittInput) {
                final int[] array11 = array;
                final int n11 = 6;
                array11[n11] |= 0x80;
            }
            int bl_DriveCurrent = ft_EEPROM_4232H.BL_DriveCurrent;
            if (bl_DriveCurrent == -1) {
                bl_DriveCurrent = 0;
            }
            final short n12 = (short)(bl_DriveCurrent << 8);
            final int[] array12 = array;
            final int n13 = 6;
            array12[n13] |= n12;
            if (ft_EEPROM_4232H.BL_SlowSlew) {
                final int[] array13 = array;
                final int n14 = 6;
                array13[n14] |= 0x400;
            }
            if (ft_EEPROM_4232H.BL_SchmittInput) {
                final int[] array14 = array;
                final int n15 = 6;
                array14[n15] |= 0x800;
            }
            final short n16 = (short)(ft_EEPROM_4232H.BH_DriveCurrent << 12);
            final int[] array15 = array;
            final int n17 = 6;
            array15[n17] |= n16;
            if (ft_EEPROM_4232H.BH_SlowSlew) {
                final int[] array16 = array;
                final int n18 = 6;
                array16[n18] |= 0x4000;
            }
            if (ft_EEPROM_4232H.BH_SchmittInput) {
                final int[] array17 = array;
                final int n19 = 6;
                array17[n19] |= 0x8000;
            }
            boolean b = false;
            int n20 = 77;
            if (this.a == 70) {
                n20 = 13;
                b = true;
            }
            final int a = this.a(ft_EEPROM_4232H.Product, array, this.a(ft_EEPROM_4232H.Manufacturer, array, n20, 7, b), 8, b);
            if (ft_EEPROM_4232H.SerNumEnable) {
                this.a(ft_EEPROM_4232H.SerialNumber, array, a, 9, b);
            }
            switch (ft_EEPROM_4232H.TPRDRV) {
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
        final FT_EEPROM_4232H ft_EEPROM_4232H = new FT_EEPROM_4232H();
        final int[] array = new int[this.b];
        if (this.c) {
            return ft_EEPROM_4232H;
        }
        try {
            for (short n = 0; n < this.b; ++n) {
                array[n] = this.a(n);
            }
            if ((short)((array[0] & 0x8) >> 3) == 1) {
                ft_EEPROM_4232H.AL_LoadVCP = true;
                ft_EEPROM_4232H.AL_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.AL_LoadVCP = false;
                ft_EEPROM_4232H.AL_LoadD2XX = true;
            }
            if ((short)((array[0] & 0x80) >> 7) == 1) {
                ft_EEPROM_4232H.BL_LoadVCP = true;
                ft_EEPROM_4232H.BL_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.BL_LoadVCP = false;
                ft_EEPROM_4232H.BL_LoadD2XX = true;
            }
            if ((short)((array[0] & 0x800) >> 11) == 1) {
                ft_EEPROM_4232H.AH_LoadVCP = true;
                ft_EEPROM_4232H.AH_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.AH_LoadVCP = false;
                ft_EEPROM_4232H.AH_LoadD2XX = true;
            }
            if ((short)((array[0] & 0x8000) >> 15) == 1) {
                ft_EEPROM_4232H.BH_LoadVCP = true;
                ft_EEPROM_4232H.BH_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.BH_LoadVCP = false;
                ft_EEPROM_4232H.BH_LoadD2XX = true;
            }
            ft_EEPROM_4232H.VendorId = (short)array[1];
            ft_EEPROM_4232H.ProductId = (short)array[2];
            this.a(ft_EEPROM_4232H, array[4]);
            this.a((Object)ft_EEPROM_4232H, array[5]);
            if ((array[5] & 0x1000) == 0x1000) {
                ft_EEPROM_4232H.AL_LoadRI_RS485 = true;
            }
            if ((array[5] & 0x2000) == 0x2000) {
                ft_EEPROM_4232H.AH_LoadRI_RS485 = true;
            }
            if ((array[5] & 0x4000) == 0x4000) {
                ft_EEPROM_4232H.AH_LoadRI_RS485 = true;
            }
            if ((array[5] & 0x8000) == 0x8000) {
                ft_EEPROM_4232H.AH_LoadRI_RS485 = true;
            }
            switch ((short)(array[6] & 0x3)) {
                case 0: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x4) == 4) {
                ft_EEPROM_4232H.AL_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.AL_SlowSlew = false;
            }
            if ((short)(array[6] & 0x8) == 8) {
                ft_EEPROM_4232H.AL_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.AL_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x30) >> 4)) {
                case 0: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x40) == 64) {
                ft_EEPROM_4232H.AH_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.AH_SlowSlew = false;
            }
            if ((short)(array[6] & 0x80) == 128) {
                ft_EEPROM_4232H.AH_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.AH_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x300) >> 8)) {
                case 0: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x400) == 1024) {
                ft_EEPROM_4232H.BL_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.BL_SlowSlew = false;
            }
            if ((short)(array[6] & 0x800) == 2048) {
                ft_EEPROM_4232H.BL_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.BL_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x3000) >> 12)) {
                case 0: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x4000) == 16384) {
                ft_EEPROM_4232H.BH_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.BH_SlowSlew = false;
            }
            if ((short)(array[6] & 0x8000) == 32768) {
                ft_EEPROM_4232H.BH_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.BH_SchmittInput = false;
            }
            final short tprdrv = (short)((array[11] & 0x18) >> 3);
            if (tprdrv < 4) {
                ft_EEPROM_4232H.TPRDRV = tprdrv;
            }
            else {
                ft_EEPROM_4232H.TPRDRV = 0;
            }
            int n2 = array[7] & 0xFF;
            if (this.a == 70) {
                n2 -= 128;
                ft_EEPROM_4232H.Manufacturer = this.a(n2 / 2, array);
                int n3 = array[8] & 0xFF;
                n3 -= 128;
                ft_EEPROM_4232H.Product = this.a(n3 / 2, array);
                int n4 = array[9] & 0xFF;
                n4 -= 128;
                ft_EEPROM_4232H.SerialNumber = this.a(n4 / 2, array);
            }
            else {
                ft_EEPROM_4232H.Manufacturer = this.a(n2 / 2, array);
                ft_EEPROM_4232H.Product = this.a((array[8] & 0xFF) / 2, array);
                ft_EEPROM_4232H.SerialNumber = this.a((array[9] & 0xFF) / 2, array);
            }
            return ft_EEPROM_4232H;
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
