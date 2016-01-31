// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

class f extends k
{
    private static FT_Device d;
    
    f(final FT_Device d) {
        super(d);
        f.d = d;
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[64];
        if (ft_EEPROM.getClass() != FT_EEPROM.class) {
            return 1;
        }
        try {
            for (short n = 0; n < 64; ++n) {
                array[n] = this.a(n);
            }
            array[1] = ft_EEPROM.VendorId;
            array[2] = ft_EEPROM.ProductId;
            array[3] = f.d.g.bcdDevice;
            array[4] = this.a((Object)ft_EEPROM);
            final int a = this.a(ft_EEPROM.Product, array, this.a(ft_EEPROM.Manufacturer, array, 10, 7, true), 8, true);
            if (ft_EEPROM.SerNumEnable) {
                this.a(ft_EEPROM.SerialNumber, array, a, 9, true);
            }
            if (array[1] == 0 || array[2] == 0) {
                return 2;
            }
            if (this.a(array, 63)) {
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
        final FT_EEPROM ft_EEPROM = new FT_EEPROM();
        final int[] array = new int[64];
        try {
            for (int i = 0; i < 64; ++i) {
                array[i] = this.a((short)i);
            }
            ft_EEPROM.VendorId = (short)array[1];
            ft_EEPROM.ProductId = (short)array[2];
            this.a(ft_EEPROM, array[4]);
            final int n = 10;
            ft_EEPROM.Manufacturer = this.a(n, array);
            final int n2 = n + (ft_EEPROM.Manufacturer.length() + 1);
            ft_EEPROM.Product = this.a(n2, array);
            ft_EEPROM.SerialNumber = this.a(n2 + (ft_EEPROM.Product.length() + 1), array);
            return ft_EEPROM;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    int b() {
        return (63 - (10 + ((this.a((short)7) & 0xFF00) >> 8) / 2 + ((this.a((short)8) & 0xFF00) >> 8) / 2 + 1) - 1 - ((this.a((short)9) & 0xFF00) >> 8) / 2) * 2;
    }
    
    int a(final byte[] array) {
        if (array.length > this.b()) {
            return 0;
        }
        final int[] array2 = new int[64];
        for (short n = 0; n < 64; ++n) {
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
        if (!this.a(array2, 63)) {
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
