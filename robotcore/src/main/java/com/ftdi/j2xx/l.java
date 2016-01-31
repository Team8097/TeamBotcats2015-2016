// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class l extends k
{
    private static FT_Device d;
    
    l(final FT_Device d) {
        super(d);
        l.d = d;
        this.b = 128;
        this.a = 1;
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        short n = 0;
        if (ft_EEPROM.getClass() != FT_EEPROM_X_Series.class) {
            return 1;
        }
        final FT_EEPROM_X_Series ft_EEPROM_X_Series = (FT_EEPROM_X_Series)ft_EEPROM;
        do {
            array[n] = this.a(n);
        } while (++n < this.b);
        try {
            array[0] = 0;
            if (ft_EEPROM_X_Series.BCDEnable) {
                final int[] array2 = array;
                final int n2 = 0;
                array2[n2] |= 0x1;
            }
            if (ft_EEPROM_X_Series.BCDForceCBusPWREN) {
                final int[] array3 = array;
                final int n3 = 0;
                array3[n3] |= 0x2;
            }
            if (ft_EEPROM_X_Series.BCDDisableSleep) {
                final int[] array4 = array;
                final int n4 = 0;
                array4[n4] |= 0x4;
            }
            if (ft_EEPROM_X_Series.RS485EchoSuppress) {
                final int[] array5 = array;
                final int n5 = 0;
                array5[n5] |= 0x8;
            }
            if (ft_EEPROM_X_Series.A_LoadVCP) {
                final int[] array6 = array;
                final int n6 = 0;
                array6[n6] |= 0x80;
            }
            if (ft_EEPROM_X_Series.PowerSaveEnable) {
                boolean b = false;
                if (ft_EEPROM_X_Series.CBus0 == 17) {
                    b = true;
                }
                if (ft_EEPROM_X_Series.CBus1 == 17) {
                    b = true;
                }
                if (ft_EEPROM_X_Series.CBus2 == 17) {
                    b = true;
                }
                if (ft_EEPROM_X_Series.CBus3 == 17) {
                    b = true;
                }
                if (ft_EEPROM_X_Series.CBus4 == 17) {
                    b = true;
                }
                if (ft_EEPROM_X_Series.CBus5 == 17) {
                    b = true;
                }
                if (ft_EEPROM_X_Series.CBus6 == 17) {
                    b = true;
                }
                if (!b) {
                    return 1;
                }
                final int[] array7 = array;
                final int n7 = 0;
                array7[n7] |= 0x40;
            }
            array[1] = ft_EEPROM_X_Series.VendorId;
            array[2] = ft_EEPROM_X_Series.ProductId;
            array[3] = 4096;
            array[4] = this.a((Object)ft_EEPROM);
            array[5] = this.b(ft_EEPROM);
            if (ft_EEPROM_X_Series.FT1248ClockPolarity) {
                final int[] array8 = array;
                final int n8 = 5;
                array8[n8] |= 0x10;
            }
            if (ft_EEPROM_X_Series.FT1248LSB) {
                final int[] array9 = array;
                final int n9 = 5;
                array9[n9] |= 0x20;
            }
            if (ft_EEPROM_X_Series.FT1248FlowControl) {
                final int[] array10 = array;
                final int n10 = 5;
                array10[n10] |= 0x40;
            }
            if (ft_EEPROM_X_Series.I2CDisableSchmitt) {
                final int[] array11 = array;
                final int n11 = 5;
                array11[n11] |= 0x80;
            }
            if (ft_EEPROM_X_Series.InvertTXD) {
                final int[] array12 = array;
                final int n12 = 5;
                array12[n12] |= 0x100;
            }
            if (ft_EEPROM_X_Series.InvertRXD) {
                final int[] array13 = array;
                final int n13 = 5;
                array13[n13] |= 0x200;
            }
            if (ft_EEPROM_X_Series.InvertRTS) {
                final int[] array14 = array;
                final int n14 = 5;
                array14[n14] |= 0x400;
            }
            if (ft_EEPROM_X_Series.InvertCTS) {
                final int[] array15 = array;
                final int n15 = 5;
                array15[n15] |= 0x800;
            }
            if (ft_EEPROM_X_Series.InvertDTR) {
                final int[] array16 = array;
                final int n16 = 5;
                array16[n16] |= 0x1000;
            }
            if (ft_EEPROM_X_Series.InvertDSR) {
                final int[] array17 = array;
                final int n17 = 5;
                array17[n17] |= 0x2000;
            }
            if (ft_EEPROM_X_Series.InvertDCD) {
                final int[] array18 = array;
                final int n18 = 5;
                array18[n18] |= 0x4000;
            }
            if (ft_EEPROM_X_Series.InvertRI) {
                final int[] array19 = array;
                final int n19 = 5;
                array19[n19] |= 0x8000;
            }
            array[6] = 0;
            int ad_DriveCurrent = ft_EEPROM_X_Series.AD_DriveCurrent;
            if (ad_DriveCurrent == -1) {
                ad_DriveCurrent = 0;
            }
            final int[] array20 = array;
            final int n20 = 6;
            array20[n20] |= ad_DriveCurrent;
            if (ft_EEPROM_X_Series.AD_SlowSlew) {
                final int[] array21 = array;
                final int n21 = 6;
                array21[n21] |= 0x4;
            }
            if (ft_EEPROM_X_Series.AD_SchmittInput) {
                final int[] array22 = array;
                final int n22 = 6;
                array22[n22] |= 0x8;
            }
            int ac_DriveCurrent = ft_EEPROM_X_Series.AC_DriveCurrent;
            if (ac_DriveCurrent == -1) {
                ac_DriveCurrent = 0;
            }
            final short n23 = (short)(ac_DriveCurrent << 4);
            final int[] array23 = array;
            final int n24 = 6;
            array23[n24] |= n23;
            if (ft_EEPROM_X_Series.AC_SlowSlew) {
                final int[] array24 = array;
                final int n25 = 6;
                array24[n25] |= 0x40;
            }
            if (ft_EEPROM_X_Series.AC_SchmittInput) {
                final int[] array25 = array;
                final int n26 = 6;
                array25[n26] |= 0x80;
            }
            final int a = this.a(ft_EEPROM_X_Series.Product, array, this.a(ft_EEPROM_X_Series.Manufacturer, array, 80, 7, false), 8, false);
            if (ft_EEPROM_X_Series.SerNumEnable) {
                this.a(ft_EEPROM_X_Series.SerialNumber, array, a, 9, false);
            }
            array[10] = ft_EEPROM_X_Series.I2CSlaveAddress;
            array[11] = (ft_EEPROM_X_Series.I2CDeviceID & 0xFFFF);
            array[12] = ft_EEPROM_X_Series.I2CDeviceID >> 16;
            int cBus0 = ft_EEPROM_X_Series.CBus0;
            if (cBus0 == -1) {
                cBus0 = 0;
            }
            int cBus2 = ft_EEPROM_X_Series.CBus1;
            if (cBus2 == -1) {
                cBus2 = 0;
            }
            array[13] = (short)(cBus0 | cBus2 << 8);
            int cBus3 = ft_EEPROM_X_Series.CBus2;
            if (cBus3 == -1) {
                cBus3 = 0;
            }
            int cBus4 = ft_EEPROM_X_Series.CBus3;
            if (cBus4 == -1) {
                cBus4 = 0;
            }
            array[14] = (short)(cBus3 | cBus4 << 8);
            int cBus5 = ft_EEPROM_X_Series.CBus4;
            if (cBus5 == -1) {
                cBus5 = 0;
            }
            int cBus6 = ft_EEPROM_X_Series.CBus5;
            if (cBus6 == -1) {
                cBus6 = 0;
            }
            array[15] = (short)(cBus5 | cBus6 << 8);
            int cBus7 = ft_EEPROM_X_Series.CBus6;
            if (cBus7 == -1) {
                cBus7 = 0;
            }
            array[16] = (short)cBus7;
            if (array[1] == 0 || array[2] == 0) {
                return 2;
            }
            if (this.b(array, this.b - 1)) {
                return 0;
            }
            return 1;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    boolean b(final int[] array, final int n) {
        int n2 = 43690;
        int i = 0;
        do {
            final int n3 = array[i] & 0xFFFF;
            this.a((short)i, (short)n3);
            final int n4 = (n3 ^ n2) & 0xFFFF;
            n2 = (((n4 << 1 & 0xFFFF) | (((n4 & 0x8000) > 0) ? 1 : 0)) & 0xFFFF);
            if (++i == 18) {
                i = 64;
            }
        } while (i != n);
        this.a((short)n, (short)n2);
        return true;
    }
    
    FT_EEPROM a() {
        final FT_EEPROM_X_Series ft_EEPROM_X_Series = new FT_EEPROM_X_Series();
        final int[] array = new int[this.b];
        try {
            for (short n = 0; n < this.b; ++n) {
                array[n] = this.a(n);
            }
            if ((array[0] & 0x1) > 0) {
                ft_EEPROM_X_Series.BCDEnable = true;
            }
            else {
                ft_EEPROM_X_Series.BCDEnable = false;
            }
            if ((array[0] & 0x2) > 0) {
                ft_EEPROM_X_Series.BCDForceCBusPWREN = true;
            }
            else {
                ft_EEPROM_X_Series.BCDForceCBusPWREN = false;
            }
            if ((array[0] & 0x4) > 0) {
                ft_EEPROM_X_Series.BCDDisableSleep = true;
            }
            else {
                ft_EEPROM_X_Series.BCDDisableSleep = false;
            }
            if ((array[0] & 0x8) > 0) {
                ft_EEPROM_X_Series.RS485EchoSuppress = true;
            }
            else {
                ft_EEPROM_X_Series.RS485EchoSuppress = false;
            }
            if ((array[0] & 0x40) > 0) {
                ft_EEPROM_X_Series.PowerSaveEnable = true;
            }
            else {
                ft_EEPROM_X_Series.PowerSaveEnable = false;
            }
            if ((array[0] & 0x80) > 0) {
                ft_EEPROM_X_Series.A_LoadVCP = true;
                ft_EEPROM_X_Series.A_LoadD2XX = false;
            }
            else {
                ft_EEPROM_X_Series.A_LoadVCP = false;
                ft_EEPROM_X_Series.A_LoadD2XX = true;
            }
            ft_EEPROM_X_Series.VendorId = (short)array[1];
            ft_EEPROM_X_Series.ProductId = (short)array[2];
            this.a(ft_EEPROM_X_Series, array[4]);
            this.a((Object)ft_EEPROM_X_Series, array[5]);
            if ((array[5] & 0x10) > 0) {
                ft_EEPROM_X_Series.FT1248ClockPolarity = true;
            }
            else {
                ft_EEPROM_X_Series.FT1248ClockPolarity = false;
            }
            if ((array[5] & 0x20) > 0) {
                ft_EEPROM_X_Series.FT1248LSB = true;
            }
            else {
                ft_EEPROM_X_Series.FT1248LSB = false;
            }
            if ((array[5] & 0x40) > 0) {
                ft_EEPROM_X_Series.FT1248FlowControl = true;
            }
            else {
                ft_EEPROM_X_Series.FT1248FlowControl = false;
            }
            if ((array[5] & 0x80) > 0) {
                ft_EEPROM_X_Series.I2CDisableSchmitt = true;
            }
            else {
                ft_EEPROM_X_Series.I2CDisableSchmitt = false;
            }
            if ((array[5] & 0x100) == 0x100) {
                ft_EEPROM_X_Series.InvertTXD = true;
            }
            else {
                ft_EEPROM_X_Series.InvertTXD = false;
            }
            if ((array[5] & 0x200) == 0x200) {
                ft_EEPROM_X_Series.InvertRXD = true;
            }
            else {
                ft_EEPROM_X_Series.InvertRXD = false;
            }
            if ((array[5] & 0x400) == 0x400) {
                ft_EEPROM_X_Series.InvertRTS = true;
            }
            else {
                ft_EEPROM_X_Series.InvertRTS = false;
            }
            if ((array[5] & 0x800) == 0x800) {
                ft_EEPROM_X_Series.InvertCTS = true;
            }
            else {
                ft_EEPROM_X_Series.InvertCTS = false;
            }
            if ((array[5] & 0x1000) == 0x1000) {
                ft_EEPROM_X_Series.InvertDTR = true;
            }
            else {
                ft_EEPROM_X_Series.InvertDTR = false;
            }
            if ((array[5] & 0x2000) == 0x2000) {
                ft_EEPROM_X_Series.InvertDSR = true;
            }
            else {
                ft_EEPROM_X_Series.InvertDSR = false;
            }
            if ((array[5] & 0x4000) == 0x4000) {
                ft_EEPROM_X_Series.InvertDCD = true;
            }
            else {
                ft_EEPROM_X_Series.InvertDCD = false;
            }
            if ((array[5] & 0x8000) == 0x8000) {
                ft_EEPROM_X_Series.InvertRI = true;
            }
            else {
                ft_EEPROM_X_Series.InvertRI = false;
            }
            switch ((short)(array[6] & 0x3)) {
                case 0: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x4) == 4) {
                ft_EEPROM_X_Series.AD_SlowSlew = true;
            }
            else {
                ft_EEPROM_X_Series.AD_SlowSlew = false;
            }
            if ((short)(array[6] & 0x8) == 8) {
                ft_EEPROM_X_Series.AD_SchmittInput = true;
            }
            else {
                ft_EEPROM_X_Series.AD_SchmittInput = false;
            }
            switch ((short)((array[6] & 0x30) >> 4)) {
                case 0: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(array[6] & 0x40) == 64) {
                ft_EEPROM_X_Series.AC_SlowSlew = true;
            }
            else {
                ft_EEPROM_X_Series.AC_SlowSlew = false;
            }
            if ((short)(array[6] & 0x80) == 128) {
                ft_EEPROM_X_Series.AC_SchmittInput = true;
            }
            else {
                ft_EEPROM_X_Series.AC_SchmittInput = false;
            }
            ft_EEPROM_X_Series.I2CSlaveAddress = array[10];
            ft_EEPROM_X_Series.I2CDeviceID = array[11];
            final FT_EEPROM_X_Series ft_EEPROM_X_Series2 = ft_EEPROM_X_Series;
            ft_EEPROM_X_Series2.I2CDeviceID |= (array[12] & 0xFF) << 16;
            ft_EEPROM_X_Series.CBus0 = (byte)(array[13] & 0xFF);
            ft_EEPROM_X_Series.CBus1 = (byte)(array[13] >> 8 & 0xFF);
            ft_EEPROM_X_Series.CBus2 = (byte)(array[14] & 0xFF);
            ft_EEPROM_X_Series.CBus3 = (byte)(array[14] >> 8 & 0xFF);
            ft_EEPROM_X_Series.CBus4 = (byte)(array[15] & 0xFF);
            ft_EEPROM_X_Series.CBus5 = (byte)(array[15] >> 8 & 0xFF);
            ft_EEPROM_X_Series.CBus6 = (byte)(array[16] & 0xFF);
            this.a = (short)(array[73] >> 8);
            ft_EEPROM_X_Series.Manufacturer = this.a((array[7] & 0xFF) / 2, array);
            ft_EEPROM_X_Series.Product = this.a((array[8] & 0xFF) / 2, array);
            ft_EEPROM_X_Series.SerialNumber = this.a((array[9] & 0xFF) / 2, array);
            return ft_EEPROM_X_Series;
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
        if (!this.b(array2, this.b - 1)) {
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
