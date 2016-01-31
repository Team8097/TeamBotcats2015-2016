// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import java.io.InputStream;
import android.content.res.AssetManager;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Iterator;
import android.util.Log;
import java.io.IOException;
import android.os.Environment;
import java.util.ArrayList;
import android.content.Context;

public class ExtractAssets
{
    private static final String a;
    
    public static ArrayList<String> ExtractToStorage(final Context context, final ArrayList<String> files, final boolean useInternalStorage) throws IOException {
        if (!useInternalStorage && !"mounted".equals(Environment.getExternalStorageState())) {
            throw new IOException("External Storage not accessible");
        }
        final ArrayList list = new ArrayList<String>();
        final Iterator<String> iterator = files.iterator();
        while (iterator.hasNext()) {
            a(context, iterator.next(), useInternalStorage, list);
            if (list != null) {
                Log.d(ExtractAssets.a, "got " + list.size() + " elements");
            }
        }
        return (ArrayList<String>)list;
    }
    
    private static ArrayList<String> a(final Context context, String string, final boolean b, final ArrayList<String> list) {
        Log.d(ExtractAssets.a, "Extracting assests for " + string);
        String[] list2 = null;
        final AssetManager assets = context.getAssets();
        try {
            list2 = assets.list(string);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        InputStream open = null;
        FileOutputStream fileOutputStream = null;
        if (list2.length == 0) {
            try {
                open = assets.open(string);
                Log.d(ExtractAssets.a, "File: " + string + " opened for streaming");
                if (!string.startsWith(File.separator)) {
                    string = File.separator + string;
                }
                File file;
                if (b) {
                    file = context.getFilesDir();
                }
                else {
                    file = context.getExternalFilesDir((String)null);
                }
                final String concat = file.getPath().concat(string);
                if (list != null && list.contains(concat)) {
                    Log.e(ExtractAssets.a, "Ignoring Duplicate entry for " + concat);
                    return list;
                }
                final int lastIndex = concat.lastIndexOf(File.separatorChar);
                final String substring = concat.substring(0, lastIndex);
                final String substring2 = concat.substring(lastIndex, concat.length());
                final File file2 = new File(substring);
                if (file2.mkdirs()) {
                    Log.d(ExtractAssets.a, "Dir created " + substring);
                }
                fileOutputStream = new FileOutputStream(new File(file2, substring2));
                if (fileOutputStream != null) {
                    final byte[] array = new byte[1024];
                    int read;
                    while ((read = open.read(array)) != -1) {
                        fileOutputStream.write(array, 0, read);
                    }
                }
                fileOutputStream.close();
                if (list != null) {
                    list.add(concat);
                }
            }
            catch (IOException ex6) {
                Log.d(ExtractAssets.a, "File: " + string + " doesn't exist");
                if (open != null) {
                    try {
                        open.close();
                    }
                    catch (IOException ex2) {
                        Log.d(ExtractAssets.a, "Unable to close in stream");
                        ex2.printStackTrace();
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        }
                        catch (IOException ex3) {
                            Log.d(ExtractAssets.a, "Unable to close out stream");
                            ex3.printStackTrace();
                        }
                    }
                }
            }
            finally {
                if (open != null) {
                    try {
                        open.close();
                    }
                    catch (IOException ex4) {
                        Log.d(ExtractAssets.a, "Unable to close in stream");
                        ex4.printStackTrace();
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        }
                        catch (IOException ex5) {
                            Log.d(ExtractAssets.a, "Unable to close out stream");
                            ex5.printStackTrace();
                        }
                    }
                }
            }
            return list;
        }
        String concat2 = string;
        if (!string.equals("") && !string.endsWith(File.separator)) {
            concat2 = concat2.concat(File.separator);
        }
        final String[] array2 = list2;
        for (int length = array2.length, i = 0; i < length; ++i) {
            a(context, concat2.concat(array2[i]), b, list);
        }
        return list;
    }
    
    static {
        a = ExtractAssets.class.getSimpleName();
    }
}
