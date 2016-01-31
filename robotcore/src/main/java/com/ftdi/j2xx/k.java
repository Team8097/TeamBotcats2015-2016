// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

import android.util.Log;

class k
{
    private FT_Device d;
    short a;
    int b;
    boolean c;
    
    k(final FT_Device d) {
        this.d = d;
    }
    
    int a(final short n) {
        final byte[] array = new byte[2];
        final int n2 = -1;
        if (n >= 1024) {
            return n2;
        }
        this.d.c().controlTransfer(-64, 144, 0, (int)n, array, 2, 0);
        return (array[1] & 0xFF) << 8 | (array[0] & 0xFF);
    }
    
    boolean a(final short n, final short n2) {
        final int n3 = n2 & 0xFFFF;
        final int n4 = n & 0xFFFF;
        boolean b = false;
        if (n >= 1024) {
            return b;
        }
        if (this.d.c().controlTransfer(64, 145, n3, n4, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    int c() {
        return this.d.c().controlTransfer(64, 146, 0, 0, (byte[])null, 0, 0);
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        return 1;
    }
    
    boolean a(final int[] array, final int n) {
        int n2 = 43690;
        int i = 0;
        while (i < n) {
            this.a((short)i, (short)array[i]);
            final int n3 = (array[i] ^ n2) & 0xFFFF;
            n2 = (((short)(n3 << 1 & 0xFFFF) | (short)(n3 >> 15 & 0xFFFF)) & 0xFFFF);
            ++i;
            Log.d("FT_EE_Ctrl", "Entered WriteWord Checksum : " + n2);
        }
        this.a((short)n, (short)n2);
        return true;
    }
    
    FT_EEPROM a() {
        return null;
    }
    
    int a(final Object o) {
        final FT_EEPROM ft_EEPROM = (FT_EEPROM)o;
        int n = 0x0 | 0x80;
        if (ft_EEPROM.RemoteWakeup) {
            n |= 0x20;
        }
        if (ft_EEPROM.SelfPowered) {
            n |= 0x40;
        }
        return ft_EEPROM.MaxPower / 2 << 8 | n;
    }
    
    void a(final FT_EEPROM ft_EEPROM, final int n) {
        ft_EEPROM.MaxPower = (short)(2 * (byte)(n >> 8));
        final byte b = (byte)n;
        if ((b & 0x40) == 0x40 && (b & 0x80) == 0x80) {
            ft_EEPROM.SelfPowered = true;
        }
        else {
            ft_EEPROM.SelfPowered = false;
        }
        if ((b & 0x20) == 0x20) {
            ft_EEPROM.RemoteWakeup = true;
        }
        else {
            ft_EEPROM.RemoteWakeup = false;
        }
    }
    
    int b(final Object o) {
        final FT_EEPROM ft_EEPROM = (FT_EEPROM)o;
        final boolean b = false;
        int n;
        if (ft_EEPROM.PullDownEnable) {
            n = ((b ? 1 : 0) | 0x4);
        }
        else {
            n = ((b ? 1 : 0) & 0xFB);
        }
        int n2;
        if (ft_EEPROM.SerNumEnable) {
            n2 = (n | 0x8);
        }
        else {
            n2 = (n & 0xF7);
        }
        return n2;
    }
    
    void a(final Object o, final int n) {
        final FT_EEPROM ft_EEPROM = (FT_EEPROM)o;
        if ((n & 0x4) > 0) {
            ft_EEPROM.PullDownEnable = true;
        }
        else {
            ft_EEPROM.PullDownEnable = false;
        }
        if ((n & 0x8) > 0) {
            ft_EEPROM.SerNumEnable = true;
        }
        else {
            ft_EEPROM.SerNumEnable = false;
        }
    }
    
    int a(final String s, final int[] array, int n, final int n2, final boolean b) {
        int n3 = 0;
        int n4 = s.length() * 2 + 2;
        array[n2] = (n4 << 8 | n * 2);
        if (b) {
            array[n2] += 128;
        }
        final char[] charArray = s.toCharArray();
        array[n++] = (n4 | 0x300);
        n4 -= 2;
        do {
            array[n++] = charArray[n3];
        } while (++n3 < n4 / 2);
        return n;
    }
    
    String a(int n, final int[] array) {
        String string = "";
        for (int n2 = ++n + ((array[n] & 0xFF) / 2 - 1), i = n; i < n2; ++i) {
            string = String.valueOf(string) + (char)array[i];
        }
        return string;
    }
    
    int a(final byte b) throws D2xxManager.D2xxException {
        final int n = 192;
        final short n2 = (short)(b & -1);
        final int[] array = new int[3];
        final short n3 = (short)this.a(n2);
        if (n3 != 65535) {
            switch (n3) {
                case 70: {
                    this.a = 70;
                    this.b = 64;
                    this.c = false;
                    return 64;
                }
                case 86: {
                    this.a = 86;
                    this.b = 128;
                    this.c = false;
                    return 128;
                }
                case 102: {
                    this.a = 102;
                    this.b = 128;
                    this.c = false;
                    return 256;
                }
                case 82: {
                    this.a = 82;
                    this.b = 1024;
                    this.c = false;
                    return 1024;
                }
                default: {
                    return 0;
                }
            }
        }
        else {
            final boolean a = this.a((short)192, (short)n);
            array[0] = this.a((short)192);
            array[1] = this.a((short)64);
            array[2] = this.a((short)0);
            if (!a) {
                this.a = 255;
                return this.b = 0;
            }
            this.c = true;
            if ((this.a((short)0) & 0xFF) == 0xC0) {
                this.c();
                this.a = 70;
                return this.b = 64;
            }
            if ((this.a((short)64) & 0xFF) == 0xC0) {
                this.c();
                this.a = 86;
                return this.b = 128;
            }
            if ((this.a((short)192) & 0xFF) == 0xC0) {
                this.c();
                this.a = 102;
                this.b = 128;
                return 256;
            }
            this.c();
            return 0;
        }
    }
    
    int a(final byte[] array) {
        return 0;
    }
    
    byte[] a(final int n) {
        return null;
    }
    
    int b() {
        return 0;
    }
}
