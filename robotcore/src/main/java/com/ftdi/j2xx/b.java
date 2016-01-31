// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

final class b
{
    static byte a(final int n, final int[] array, final boolean b) {
        final byte b2;
        if ((b2 = b(n, array, b)) == -1) {
            return -1;
        }
        if (b2 == 0) {
            array[0] = (array[0] & 0xFFFF3FFF) + 1;
        }
        final int a = a(array[0], array[1], b);
        int n2;
        int n3;
        if (n > a) {
            n2 = n * 100 / a - 100;
            n3 = n % a * 100 % a;
        }
        else {
            n2 = a * 100 / n - 100;
            n3 = a % n * 100 % n;
        }
        byte b3;
        if (n2 < 3) {
            b3 = 1;
        }
        else if (n2 == 3 && n3 == 0) {
            b3 = 1;
        }
        else {
            b3 = 0;
        }
        return b3;
    }
    
    private static byte b(final int n, final int[] array, final boolean b) {
        byte b2 = 1;
        if (n == 0) {
            return -1;
        }
        if ((3000000 / n & 0xFFFFC000) > 0) {
            return -1;
        }
        array[0] = 3000000 / n;
        array[1] = 0;
        if (array[0] == 1 && 3000000 % n * 100 / n <= 3) {
            array[0] = 0;
        }
        if (array[0] == 0) {
            return b2;
        }
        final int n2 = 3000000 % n * 100 / n;
        int n3;
        if (!b) {
            if (n2 <= 6) {
                n3 = 0;
            }
            else if (n2 <= 18) {
                n3 = 49152;
            }
            else if (n2 <= 37) {
                n3 = 32768;
            }
            else if (n2 <= 75) {
                n3 = 16384;
            }
            else {
                n3 = 0;
                b2 = 0;
            }
        }
        else if (n2 <= 6) {
            n3 = 0;
        }
        else if (n2 <= 18) {
            n3 = 49152;
        }
        else if (n2 <= 31) {
            n3 = 32768;
        }
        else if (n2 <= 43) {
            n3 = 0;
            array[1] = 1;
        }
        else if (n2 <= 56) {
            n3 = 16384;
        }
        else if (n2 <= 68) {
            n3 = 16384;
            array[1] = 1;
        }
        else if (n2 <= 81) {
            n3 = 32768;
            array[1] = 1;
        }
        else if (n2 <= 93) {
            n3 = 49152;
            array[1] = 1;
        }
        else {
            n3 = 0;
            b2 = 0;
        }
        final int n4 = 0;
        array[n4] |= n3;
        return b2;
    }
    
    private static final int a(final int n, final int n2, final boolean b) {
        if (n == 0) {
            return 3000000;
        }
        int n3 = (n & 0xFFFF3FFF) * 100;
        if (!b) {
            switch (n & 0xC000) {
                case 49152: {
                    n3 += 12;
                    break;
                }
                case 32768: {
                    n3 += 25;
                    break;
                }
                case 16384: {
                    n3 += 50;
                    break;
                }
            }
        }
        else if (n2 == 0) {
            switch (n & 0xC000) {
                case 49152: {
                    n3 += 12;
                    break;
                }
                case 32768: {
                    n3 += 25;
                    break;
                }
                case 16384: {
                    n3 += 50;
                    break;
                }
            }
        }
        else {
            switch (n & 0xC000) {
                case 0: {
                    n3 += 37;
                    break;
                }
                case 16384: {
                    n3 += 62;
                    break;
                }
                case 32768: {
                    n3 += 75;
                    break;
                }
                case 49152: {
                    n3 += 87;
                    break;
                }
            }
        }
        return 300000000 / n3;
    }
    
    static final byte a(final int n, final int[] array) {
        final byte b;
        if ((b = b(n, array)) == -1) {
            return -1;
        }
        if (b == 0) {
            array[0] = (array[0] & 0xFFFF3FFF) + 1;
        }
        final int a = a(array[0], array[1]);
        int n2;
        int n3;
        if (n > a) {
            n2 = n * 100 / a - 100;
            n3 = n % a * 100 % a;
        }
        else {
            n2 = a * 100 / n - 100;
            n3 = a % n * 100 % n;
        }
        byte b2;
        if (n2 < 3) {
            b2 = 1;
        }
        else if (n2 == 3 && n3 == 0) {
            b2 = 1;
        }
        else {
            b2 = 0;
        }
        return b2;
    }
    
    private static byte b(final int n, final int[] array) {
        byte b = 1;
        if (n == 0) {
            return -1;
        }
        if ((12000000 / n & 0xFFFFC000) > 0) {
            return -1;
        }
        array[1] = 2;
        if (n >= 11640000 && n <= 12360000) {
            array[0] = 0;
            return b;
        }
        if (n >= 7760000 && n <= 8240000) {
            array[0] = 1;
            return b;
        }
        array[0] = 12000000 / n;
        array[1] = 2;
        if (array[0] == 1 && 12000000 % n * 100 / n <= 3) {
            array[0] = 0;
        }
        if (array[0] == 0) {
            return b;
        }
        final int n2 = 12000000 % n * 100 / n;
        int n3;
        if (n2 <= 6) {
            n3 = 0;
        }
        else if (n2 <= 18) {
            n3 = 49152;
        }
        else if (n2 <= 31) {
            n3 = 32768;
        }
        else if (n2 <= 43) {
            n3 = 0;
            final int n4 = 1;
            array[n4] |= 0x1;
        }
        else if (n2 <= 56) {
            n3 = 16384;
        }
        else if (n2 <= 68) {
            n3 = 16384;
            final int n5 = 1;
            array[n5] |= 0x1;
        }
        else if (n2 <= 81) {
            n3 = 32768;
            final int n6 = 1;
            array[n6] |= 0x1;
        }
        else if (n2 <= 93) {
            n3 = 49152;
            final int n7 = 1;
            array[n7] |= 0x1;
        }
        else {
            n3 = 0;
            b = 0;
        }
        final int n8 = 0;
        array[n8] |= n3;
        return b;
    }
    
    private static int a(final int n, int n2) {
        if (n == 0) {
            return 12000000;
        }
        if (n == 1) {
            return 8000000;
        }
        int n3 = (n & 0xFFFF3FFF) * 100;
        n2 &= 0xFFFD;
        if (n2 == 0) {
            switch (n & 0xC000) {
                case 49152: {
                    n3 += 12;
                    break;
                }
                case 32768: {
                    n3 += 25;
                    break;
                }
                case 16384: {
                    n3 += 50;
                    break;
                }
            }
        }
        else {
            switch (n & 0xC000) {
                case 0: {
                    n3 += 37;
                    break;
                }
                case 16384: {
                    n3 += 62;
                    break;
                }
                case 32768: {
                    n3 += 75;
                    break;
                }
                case 49152: {
                    n3 += 87;
                    break;
                }
            }
        }
        return 1200000000 / n3;
    }
}
