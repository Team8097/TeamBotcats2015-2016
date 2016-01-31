// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.widget.TextView;
import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import java.util.Random;

public class Util
{
    public static String ASCII_RECORD_SEPARATOR;
    public static final String LOWERCASE_ALPHA_NUM_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    
    public static String getRandomString(final int stringLength, final String charSet) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringLength; ++i) {
            sb.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        return sb.toString();
    }
    
    public static void sortFilesByName(final File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            public int compare(final File file, final File file2) {
                return file.getName().compareTo(file2.getName());
            }
        });
    }
    
    public static void updateTextView(final TextView textView, final String msg) {
        if (textView != null) {
            textView.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    textView.setText((CharSequence)msg);
                }
            });
        }
    }
    
    public static byte[] concatenateByteArrays(final byte[] first, final byte[] second) {
        final byte[] array = new byte[first.length + second.length];
        System.arraycopy(first, 0, array, 0, first.length);
        System.arraycopy(second, 0, array, first.length, second.length);
        return array;
    }
    
    static {
        Util.ASCII_RECORD_SEPARATOR = "\u001e";
    }
}
