// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class d extends k
{
    d(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)10);
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_2232D.class) {
            return 1;
        }
        final FT_EEPROM_2232D ft_EEPROM_2232D = (FT_EEPROM_2232D)ft_EEPROM;
        try {
            array[0] = 0;
            if (ft_EEPROM_2232D.A_FIFO) {
                final int[] array2 = array;
                final int n = 0;
                array2[n] |= 0x1;
            }
            else if (ft_EEPROM_2232D.A_FIFOTarget) {
                final int[] array3 = array;
                final int n2 = 0;
                array3[n2] |= 0x2;
            }
            else {
                final int[] array4 = array;
                final int n3 = 0;
                array4[n3] |= 0x4;
            }
            if (ft_EEPROM_2232D.A_HighIO) {
                final int[] array5 = array;
                final int n4 = 0;
                array5[n4] |= 0x10;
            }
            if (ft_EEPROM_2232D.A_LoadVCP) {
                final int[] array6 = array;
                final int n5 = 0;
                array6[n5] |= 0x8;
            }
            else if (ft_EEPROM_2232D.B_FIFO) {
                final int[] array7 = array;
                final int n6 = 0;
                array7[n6] |= 0x100;
            }
            else if (ft_EEPROM_2232D.B_FIFOTarget) {
                final int[] array8 = array;
                final int n7 = 0;
                array8[n7] |= 0x200;
            }
            else {
                final int[] array9 = array;
                final int n8 = 0;
                array9[n8] |= 0x400;
            }
            if (ft_EEPROM_2232D.B_HighIO) {
                final int[] array10 = array;
                final int n9 = 0;
                array10[n9] |= 0x1000;
            }
            if (ft_EEPROM_2232D.B_LoadVCP) {
                final int[] array11 = array;
                final int n10 = 0;
                array11[n10] |= 0x800;
            }
            array[1] = ft_EEPROM_2232D.VendorId;
            array[2] = ft_EEPROM_2232D.ProductId;
            array[3] = 1280;
            array[4] = this.a((Object)ft_EEPROM);
            array[4] = this.b(ft_EEPROM);
            boolean b = false;
            int n11 = 75;
            if (this.a == 70) {
                n11 = 11;
                b = true;
            }
            final int a = this.a(ft_EEPROM_2232D.Product, array, this.a(ft_EEPROM_2232D.Manufacturer, array, n11, 7, b), 8, b);
            if (ft_EEPROM_2232D.SerNumEnable) {
                this.a(ft_EEPROM_2232D.SerialNumber, array, a, 9, b);
            }
            array[10] = this.a;
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
        final FT_EEPROM_2232D ft_EEPROM_2232D = new FT_EEPROM_2232D();
        final int[] array = new int[this.b];
        try {
            for (int i = 0; i < this.b; ++i) {
                array[i] = this.a((short)i);
            }
            switch ((short)(array[0] & 0x7)) {
                case 0: {
                    ft_EEPROM_2232D.A_UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232D.A_FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232D.A_FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_2232D.A_FastSerial = true;
                    break;
                }
            }
            if ((short)((array[0] & 0x8) >> 3) == 1) {
                ft_EEPROM_2232D.A_LoadVCP = true;
            }
            else {
                ft_EEPROM_2232D.A_HighIO = true;
            }
            if ((short)((array[0] & 0x10) >> 4) == 1) {
                ft_EEPROM_2232D.A_HighIO = true;
            }
            switch ((short)((array[0] & 0x700) >> 8)) {
                case 0: {
                    ft_EEPROM_2232D.B_UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232D.B_FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232D.B_FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_2232D.B_FastSerial = true;
                    break;
                }
            }
            if ((short)((array[0] & 0x800) >> 11) == 1) {
                ft_EEPROM_2232D.B_LoadVCP = true;
            }
            else {
                ft_EEPROM_2232D.B_LoadD2XX = true;
            }
            if ((short)((array[0] & 0x1000) >> 12) == 1) {
                ft_EEPROM_2232D.B_HighIO = true;
            }
            ft_EEPROM_2232D.VendorId = (short)array[1];
            ft_EEPROM_2232D.ProductId = (short)array[2];
            this.a(ft_EEPROM_2232D, array[4]);
            int n = array[7] & 0xFF;
            if (this.a == 70) {
                n -= 128;
                ft_EEPROM_2232D.Manufacturer = this.a(n / 2, array);
                int n2 = array[8] & 0xFF;
                n2 -= 128;
                ft_EEPROM_2232D.Product = this.a(n2 / 2, array);
                int n3 = array[9] & 0xFF;
                n3 -= 128;
                ft_EEPROM_2232D.SerialNumber = this.a(n3 / 2, array);
            }
            else {
                ft_EEPROM_2232D.Manufacturer = this.a(n / 2, array);
                ft_EEPROM_2232D.Product = this.a((array[8] & 0xFF) / 2, array);
                ft_EEPROM_2232D.SerialNumber = this.a((array[9] & 0xFF) / 2, array);
            }
            return ft_EEPROM_2232D;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    int b() {
        final int a = this.a((short)9);
        return (this.b - 1 - 1 - ((a & 0xFF) + ((a & 0xFF00) >> 8) / 2)) * 2;
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
