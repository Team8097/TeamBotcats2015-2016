// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.analytics;

import java.net.MalformedURLException;

import android.os.AsyncTask;

import java.io.Serializable;

import javax.net.ssl.SSLSession;

import android.os.Environment;

import javax.net.ssl.KeyManager;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import java.net.URL;

import android.net.ConnectivityManager;

import java.io.Reader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Build;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.EOFException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.regex.Pattern;

import com.qualcomm.robotcore.util.Version;

import android.preference.PreferenceManager;

import com.qualcomm.robotcore.hardware.HardwareMap;

import android.content.IntentFilter;
import android.os.Bundle;

import com.qualcomm.robotcore.util.RobotLog;

import android.net.NetworkInfo;
import android.content.Intent;

import javax.net.ssl.HostnameVerifier;

import android.content.SharedPreferences;
import android.content.Context;

import java.util.UUID;
import java.nio.charset.Charset;

import android.content.BroadcastReceiver;

public class Analytics extends BroadcastReceiver {
    public static final String UUID_PATH = ".analytics_id";
    public static final String DATA_COLLECTION_PATH = ".ftcdc";
    static String a;
    public static final String RC_COMMAND_STRING = "update_rc";
    public static final String DS_COMMAND_STRING = "update_ds";
    public static final String EXTERNAL_STORAGE_DIRECTORY_PATH;
    public static final String LAST_UPLOAD_DATE = "last_upload_date";
    public static final String MAX_DEVICES = "max_usb_devices";
    public static int MAX_ENTRIES_SIZE;
    public static int TRIMMED_SIZE;
    private static final Charset m;
    static long b;
    static UUID c;
    static String d;
    String e;
    static String f;
    Context g;
    SharedPreferences h;
    boolean i;
    long j;
    int k;
    static final HostnameVerifier l;

    public void onReceive(final Context context, final Intent intent) {
        final Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("networkInfo") && ((NetworkInfo) extras.get("networkInfo")).getState().equals((Object) NetworkInfo.State.CONNECTED)) {
            RobotLog.i("Analytics detected NetworkInfo.State.CONNECTED");
            this.communicateWithServer();
        }
    }

    public void unregister() {
        this.g.unregisterReceiver((BroadcastReceiver) this);
    }

    public void register() {
        this.g.registerReceiver((BroadcastReceiver) this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
    }

    public Analytics(final Context context, final String commandString, final HardwareMap map) {
        this.i = false;
        this.j = 0L;
        this.k = 0;
        this.g = context;
        this.e = commandString;
        try {
            try {
                this.h = PreferenceManager.getDefaultSharedPreferences(context);
                Analytics.b = System.currentTimeMillis();
                Analytics.f = Version.getLibraryVersion();
                this.handleUUID(".analytics_id");
                final int calculateUsbDevices = this.calculateUsbDevices(map);
                if (this.h.getInt("max_usb_devices", this.k) < calculateUsbDevices) {
                    final SharedPreferences.Editor edit = this.h.edit();
                    edit.putInt("max_usb_devices", calculateUsbDevices);
                    edit.apply();
                }
                setApplicationName(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
                this.handleData();
                this.register();
                RobotLog.i("Analytics has completed initialization.");
            } catch (Exception ex2) {
                this.i = true;
            }
            if (this.i) {
                this.a();
                this.i = false;
            }
        } catch (Exception ex) {
            RobotLog.i("Analytics encountered a problem during initialization");
            RobotLog.logStacktrace(ex);
        }
    }

    protected int calculateUsbDevices(final HardwareMap map) {
        int n = 0 + map.legacyModule.size() + map.deviceInterfaceModule.size();
        final Iterator iterator = map.servoController.iterator();
        while (iterator.hasNext()) {
            if (Pattern.compile("(?i)usb").matcher(((ServoController) (iterator.next())).getDeviceName()).matches()) {
                ++n;
            }
        }
        final Iterator iterator2 = map.dcMotorController.iterator();
        while (iterator2.hasNext()) {
            if (Pattern.compile("(?i)usb").matcher(((DcMotorController) (iterator2.next())).getDeviceName()).matches()) {
                ++n;
            }
        }
        return n;
    }

    protected void handleData() throws IOException, ClassNotFoundException {
        final String string = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
        if (!new File(string).exists()) {
            this.createInitialFile(string);
        } else {
            final ArrayList<DataInfo> updateExistingFile = this.updateExistingFile(string, getDateFromTime(Analytics.b));
            if (updateExistingFile.size() >= Analytics.MAX_ENTRIES_SIZE) {
                this.trimEntries(updateExistingFile);
            }
            this.writeObjectsToFile(string, updateExistingFile);
        }
    }

    protected void trimEntries(final ArrayList<DataInfo> dataInfoArrayList) {
        dataInfoArrayList.subList(Analytics.TRIMMED_SIZE, dataInfoArrayList.size()).clear();
    }

    protected ArrayList<DataInfo> updateExistingFile(final String filepath, final String date) throws ClassNotFoundException, IOException {
        final ArrayList<DataInfo> objectsFromFile = this.readObjectsFromFile(filepath);
        final DataInfo dataInfo = objectsFromFile.get(objectsFromFile.size() - 1);
        if (dataInfo.a.equalsIgnoreCase(date)) {
            final DataInfo dataInfo2 = dataInfo;
            ++dataInfo2.numUsages;
        } else {
            objectsFromFile.add(new DataInfo(date, 1));
        }
        return objectsFromFile;
    }

    protected ArrayList<DataInfo> readObjectsFromFile(final String filepath) throws IOException, ClassNotFoundException {
        final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(new File(filepath)));
        final ArrayList<DataInfo> list = new ArrayList<DataInfo>();
        int i = 1;
        while (i != 0) {
            try {
                list.add((DataInfo) objectInputStream.readObject());
            } catch (EOFException ex) {
                i = 0;
            }
        }
        objectInputStream.close();
        return list;
    }

    protected void createInitialFile(final String filepath) throws IOException {
        final DataInfo dataInfo = new DataInfo(getDateFromTime(Analytics.b), 1);
        final ArrayList<DataInfo> info = new ArrayList<DataInfo>();
        info.add(dataInfo);
        this.writeObjectsToFile(filepath, info);
    }

    private void a() {
        RobotLog.i("Analytics is starting with a clean slate.");
        final SharedPreferences.Editor edit = this.h.edit();
        edit.putLong("last_upload_date", this.j);
        edit.apply();
        edit.putInt("max_usb_devices", 0);
        edit.apply();
        new File(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc").delete();
        new File(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".analytics_id").delete();
        this.i = false;
    }

    public void communicateWithServer() {
        new a().execute((Object[]) new String[]{Analytics.a});
    }

    public static void setApplicationName(final String name) {
        Analytics.d = name;
    }

    public void handleUUID(final String filename) {
        final File file = new File(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + filename);
        if (!file.exists()) {
            Analytics.c = UUID.randomUUID();
            this.handleCreateNewFile(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + filename, Analytics.c.toString());
        }
        final String fromFile = this.readFromFile(file);
        try {
            Analytics.c = UUID.fromString(fromFile);
        } catch (IllegalArgumentException ex) {
            RobotLog.i("Analytics encountered an IllegalArgumentException");
            Analytics.c = UUID.randomUUID();
            this.handleCreateNewFile(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + filename, Analytics.c.toString());
        }
    }

    protected String readFromFile(final File file) {
        try {
            final char[] array = new char[4096];
            final FileReader fileReader = new FileReader(file);
            final int read = fileReader.read(array);
            fileReader.close();
            return new String(array, 0, read).trim();
        } catch (FileNotFoundException ex) {
            RobotLog.i("Analytics encountered a FileNotFoundException while trying to read a file.");
        } catch (IOException ex2) {
            RobotLog.i("Analytics encountered an IOException while trying to read.");
        }
        return "";
    }

    protected void writeObjectsToFile(final String filepath, final ArrayList<DataInfo> info) throws IOException {
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filepath));
        final Iterator<DataInfo> iterator = info.iterator();
        while (iterator.hasNext()) {
            objectOutputStream.writeObject(iterator.next());
        }
        objectOutputStream.close();
    }

    protected void handleCreateNewFile(final String filepath, final String data) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filepath)), "utf-8"));
            writer.write(data);
        } catch (IOException ex) {
            RobotLog.i("Analytics encountered an IOException: " + ex.toString());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex2) {
                }
            }
        }
    }

    public static String getDateFromTime(final long time) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(time));
    }

    protected static UUID getUuid() {
        return Analytics.c;
    }

    public String updateStats(final String user, final ArrayList<DataInfo> dateInfo, final String commandString) {
        final String string = this.a("cmd", "=", commandString) + "&" + this.a("uuid", "=", user) + "&" + this.a("device_hw", "=", Build.MANUFACTURER) + "&" + this.a("device_ver", "=", Build.MODEL) + "&" + this.a("chip_type", "=", this.b()) + "&" + this.a("sw_ver", "=", Analytics.f) + "&" + this.a("max_dev", "=", String.valueOf(this.h.getInt("max_usb_devices", this.k))) + "&";
        String s = "";
        for (int i = 0; i < dateInfo.size(); ++i) {
            if (i > 0) {
                s += ",";
            }
            s += this.a(dateInfo.get(i).date(), ",", String.valueOf(dateInfo.get(i).numUsages()));
        }
        return string + this.a("dc", "=", "") + s;
    }

    private String a(final String s, final String s2, final String s3) {
        String string = "";
        try {
            string = URLEncoder.encode(s, Analytics.m.name()) + s2 + URLEncoder.encode(s3, Analytics.m.name());
        } catch (UnsupportedEncodingException ex) {
            RobotLog.i("Analytics caught an UnsupportedEncodingException");
        }
        return string;
    }

    private String b() {
        final String s = "UNKNOWN";
        final String[] array = {"CPU implementer", "Hardware"};
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            for (String s2 = bufferedReader.readLine(); s2 != null; s2 = bufferedReader.readLine()) {
                final String[] split = s2.toLowerCase().split(":");
                if (split.length >= 2) {
                    hashMap.put(split[0].trim(), split[1].trim());
                }
            }
            bufferedReader.close();
            String string = "";
            final String[] array2 = array;
            for (int length = array2.length, i = 0; i < length; ++i) {
                string = string + hashMap.get(array2[i].toLowerCase()) + " ";
            }
            final String trim = string.trim();
            if (trim.isEmpty()) {
                return s;
            }
            return trim;
        } catch (FileNotFoundException ex) {
            RobotLog.i("Analytics encountered a FileNotFoundException while looking for CPU info");
        } catch (IOException ex2) {
            RobotLog.i("Analytics encountered an IOException while looking for CPU info");
        }
        return s;
    }

    public boolean isConnected() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.g.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String ping(final URL baseUrl, final String data) {
        return call(baseUrl, data);
    }

    public static String call(final URL url, final String data) {
        String string = null;
        if (url != null && data != null) {
            try {
                final long currentTimeMillis = System.currentTimeMillis();
                HttpURLConnection httpURLConnection;
                if (url.getProtocol().toLowerCase().equals("https")) {
                    c();
                    final HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setHostnameVerifier(Analytics.l);
                    httpURLConnection = httpsURLConnection;
                } else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }
                httpURLConnection.setDoOutput(true);
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                outputStreamWriter.write(data);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                string = new String();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    string += line;
                }
                bufferedReader.close();
                RobotLog.i("Analytics took: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            } catch (IOException ex) {
                RobotLog.i("Analytics Failed to process command.");
            }
        }
        return string;
    }

    private static void c() {
        final TrustManager[] array = {new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
            }
        }};
        try {
            final SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, array, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static {
        Analytics.a = "https://ftcdc.qualcomm.com/DataApi";
        EXTERNAL_STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/";
        Analytics.MAX_ENTRIES_SIZE = 100;
        Analytics.TRIMMED_SIZE = 90;
        m = Charset.forName("UTF-8");
        Analytics.c = null;
        Analytics.f = "";
        l = new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        };
    }

    public static class DataInfo implements Serializable {
        private final String a;
        protected int numUsages;

        public DataInfo(final String adate, final int numUsages) {
            this.a = adate;
            this.numUsages = numUsages;
        }

        public String date() {
            return this.a;
        }

        public int numUsages() {
            return this.numUsages;
        }
    }

    private class a extends AsyncTask {
        protected Object doInBackground(final Object[] params) {
            if (Analytics.this.isConnected()) {
                try {
                    final URL url = new URL(Analytics.a);
                    if (!Analytics.getDateFromTime(Analytics.b).equals(Analytics.getDateFromTime(Analytics.this.h.getLong("last_upload_date", Analytics.this.j)))) {
                        final String ping = Analytics.ping(url, Analytics.this.a("cmd", "=", "ping"));
                        final String s = "\"rc\": \"OK\"";
                        if (ping == null || !ping.contains(s)) {
                            RobotLog.e("Analytics: Ping failed.");
                            return null;
                        }
                        RobotLog.i("Analytics ping succeeded.");
                        final String string = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
                        final ArrayList<DataInfo> objectsFromFile = Analytics.this.readObjectsFromFile(string);
                        if (objectsFromFile.size() >= Analytics.MAX_ENTRIES_SIZE) {
                            Analytics.this.trimEntries(objectsFromFile);
                        }
                        final String call = Analytics.call(url, Analytics.this.updateStats(Analytics.c.toString(), objectsFromFile, Analytics.this.e));
                        if (call == null || !call.contains(s)) {
                            RobotLog.e("Analytics: Upload failed.");
                            return null;
                        }
                        RobotLog.i("Analytics: Upload succeeded.");
                        final SharedPreferences.Editor edit = Analytics.this.h.edit();
                        edit.putLong("last_upload_date", Analytics.b);
                        edit.apply();
                        edit.putInt("max_usb_devices", 0);
                        edit.apply();
                        new File(string).delete();
                    }
                } catch (MalformedURLException ex) {
                    RobotLog.e("Analytics encountered a malformed URL exception");
                } catch (Exception ex2) {
                    Analytics.this.i = true;
                }
                if (Analytics.this.i) {
                    RobotLog.i("Analytics encountered a problem during communication");
                    Analytics.this.a();
                    Analytics.this.i = false;
                }
            }
            return null;
        }
    }
}
