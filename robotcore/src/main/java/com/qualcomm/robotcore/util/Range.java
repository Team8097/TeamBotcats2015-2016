// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

public class Range
{
    public static double scale(final double n, final double x1, final double x2, final double y1, final double y2) {
        return (y1 - y2) / (x1 - x2) * n + (y1 - x1 * (y1 - y2) / (x1 - x2));
    }
    
    public static double clip(final double number, final double min, final double max) {
        if (number < min) {
            return min;
        }
        if (number > max) {
            return max;
        }
        return number;
    }
    
    public static float clip(final float number, final float min, final float max) {
        if (number < min) {
            return min;
        }
        if (number > max) {
            return max;
        }
        return number;
    }
    
    public static void throwIfRangeIsInvalid(final double number, final double min, final double max) throws IllegalArgumentException {
        if (number < min || number > max) {
            throw new IllegalArgumentException(String.format("number %f is invalid; valid ranges are %f..%f", number, min, max));
        }
    }
}
