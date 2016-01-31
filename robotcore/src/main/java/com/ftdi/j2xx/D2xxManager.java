// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

import java.io.IOException;
import java.util.Iterator;
import android.util.Log;
import android.hardware.usb.UsbDevice;
import android.content.Intent;
import java.util.Collection;
import java.util.Arrays;
import android.content.BroadcastReceiver;
import android.hardware.usb.UsbManager;
import java.util.ArrayList;
import java.util.List;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.content.Context;

public class D2xxManager
{
    private static D2xxManager a;
    protected static final String ACTION_USB_PERMISSION = "com.ftdi.j2xx";
    public static final byte FT_DATA_BITS_7 = 7;
    public static final byte FT_DATA_BITS_8 = 8;
    public static final byte FT_STOP_BITS_1 = 0;
    public static final byte FT_STOP_BITS_2 = 2;
    public static final byte FT_PARITY_NONE = 0;
    public static final byte FT_PARITY_ODD = 1;
    public static final byte FT_PARITY_EVEN = 2;
    public static final byte FT_PARITY_MARK = 3;
    public static final byte FT_PARITY_SPACE = 4;
    public static final short FT_FLOW_NONE = 0;
    public static final short FT_FLOW_RTS_CTS = 256;
    public static final short FT_FLOW_DTR_DSR = 512;
    public static final short FT_FLOW_XON_XOFF = 1024;
    public static final byte FT_PURGE_RX = 1;
    public static final byte FT_PURGE_TX = 2;
    public static final byte FT_CTS = 16;
    public static final byte FT_DSR = 32;
    public static final byte FT_RI = 64;
    public static final byte FT_DCD = Byte.MIN_VALUE;
    public static final byte FT_OE = 2;
    public static final byte FT_PE = 4;
    public static final byte FT_FE = 8;
    public static final byte FT_BI = 16;
    public static final byte FT_EVENT_RXCHAR = 1;
    public static final byte FT_EVENT_MODEM_STATUS = 2;
    public static final byte FT_EVENT_LINE_STATUS = 4;
    public static final byte FT_EVENT_REMOVED = 8;
    public static final byte FT_FLAGS_OPENED = 1;
    public static final byte FT_FLAGS_HI_SPEED = 2;
    public static final int FT_DEVICE_232B = 0;
    public static final int FT_DEVICE_8U232AM = 1;
    public static final int FT_DEVICE_UNKNOWN = 3;
    public static final int FT_DEVICE_2232 = 4;
    public static final int FT_DEVICE_232R = 5;
    public static final int FT_DEVICE_245R = 5;
    public static final int FT_DEVICE_2232H = 6;
    public static final int FT_DEVICE_4232H = 7;
    public static final int FT_DEVICE_232H = 8;
    public static final int FT_DEVICE_X_SERIES = 9;
    public static final int FT_DEVICE_4222_0 = 10;
    public static final int FT_DEVICE_4222_1_2 = 11;
    public static final int FT_DEVICE_4222_3 = 12;
    public static final byte FT_BITMODE_RESET = 0;
    public static final byte FT_BITMODE_ASYNC_BITBANG = 1;
    public static final byte FT_BITMODE_MPSSE = 2;
    public static final byte FT_BITMODE_SYNC_BITBANG = 4;
    public static final byte FT_BITMODE_MCU_HOST = 8;
    public static final byte FT_BITMODE_FAST_SERIAL = 16;
    public static final byte FT_BITMODE_CBUS_BITBANG = 32;
    public static final byte FT_BITMODE_SYNC_FIFO = 64;
    public static final int FTDI_BREAK_OFF = 0;
    public static final int FTDI_BREAK_ON = 16384;
    private static Context b;
    private static PendingIntent c;
    private static IntentFilter d;
    private static List<m> e;
    private ArrayList<FT_Device> f;
    private static UsbManager g;
    private BroadcastReceiver h;
    private static BroadcastReceiver i;
    
    static {
        D2xxManager.a = null;
        D2xxManager.b = null;
        D2xxManager.c = null;
        D2xxManager.d = null;
        D2xxManager.e = new ArrayList<m>(Arrays.asList(new m(1027, 24597), new m(1027, 24596), new m(1027, 24593), new m(1027, 24592), new m(1027, 24577), new m(1027, 24582), new m(1027, 24604), new m(1027, 64193), new m(1027, 64194), new m(1027, 64195), new m(1027, 64196), new m(1027, 64197), new m(1027, 64198), new m(1027, 24594), new m(2220, 4133), new m(5590, 1), new m(1027, 24599)));
        D2xxManager.i = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if ("com.ftdi.j2xx".equals(intent.getAction())) {
                    synchronized (this) {
                        final UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                        if (!intent.getBooleanExtra("permission", false)) {
                            Log.d("D2xx::", "permission denied for device " + usbDevice);
                        }
                    }
                }
            }
        };
    }
    
    private FT_Device a(final UsbDevice usbDevice) {
        FT_Device ft_Device = null;
        synchronized (this.f) {
            for (int size = this.f.size(), i = 0; i < size; ++i) {
                final FT_Device ft_Device2 = this.f.get(i);
                if (ft_Device2.getUsbDevice().equals((Object)usbDevice)) {
                    ft_Device = ft_Device2;
                    break;
                }
            }
        }
        // monitorexit(this.f)
        return ft_Device;
    }
    
    public boolean isFtDevice(final UsbDevice dev) {
        boolean b = false;
        if (D2xxManager.b == null) {
            return b;
        }
        final m m = new m(dev.getVendorId(), dev.getProductId());
        if (D2xxManager.e.contains(m)) {
            b = true;
        }
        Log.v("D2xx::", m.toString());
        return b;
    }
    
    private static synchronized boolean a(final Context b) {
        final boolean b2 = false;
        if (b == null) {
            return b2;
        }
        if (D2xxManager.b != b) {
            D2xxManager.b = b;
            D2xxManager.c = PendingIntent.getBroadcast(D2xxManager.b.getApplicationContext(), 0, new Intent("com.ftdi.j2xx"), 134217728);
            D2xxManager.d = new IntentFilter("com.ftdi.j2xx");
            D2xxManager.b.getApplicationContext().registerReceiver(D2xxManager.i, D2xxManager.d);
        }
        return true;
    }
    
    private boolean b(final UsbDevice usbDevice) {
        boolean b = false;
        if (!D2xxManager.g.hasPermission(usbDevice)) {
            D2xxManager.g.requestPermission(usbDevice, D2xxManager.c);
        }
        if (D2xxManager.g.hasPermission(usbDevice)) {
            b = true;
        }
        return b;
    }
    
    private D2xxManager(final Context parentContext) throws D2xxException {
        this.h = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                    final UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                    for (FT_Device ft_Device = D2xxManager.this.a(usbDevice); ft_Device != null; ft_Device = D2xxManager.this.a(usbDevice)) {
                        ft_Device.close();
                        synchronized (D2xxManager.this.f) {
                            D2xxManager.this.f.remove(ft_Device);
                        }
                        // monitorexit(D2xxManager.a(this.a))
                    }
                }
                else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                    D2xxManager.this.addUsbDevice((UsbDevice)intent.getParcelableExtra("device"));
                }
            }
        };
        Log.v("D2xx::", "Start constructor");
        if (parentContext == null) {
            throw new D2xxException("D2xx init failed: Can not find parentContext!");
        }
        a(parentContext);
        if (!a()) {
            throw new D2xxException("D2xx init failed: Can not find UsbManager!");
        }
        this.f = new ArrayList<FT_Device>();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        parentContext.getApplicationContext().registerReceiver(this.h, intentFilter);
        Log.v("D2xx::", "End constructor");
    }
    
    public static synchronized D2xxManager getInstance(final Context parentContext) throws D2xxException {
        if (D2xxManager.a == null) {
            D2xxManager.a = new D2xxManager(parentContext);
        }
        if (parentContext != null) {
            a(parentContext);
        }
        return D2xxManager.a;
    }
    
    private static boolean a() {
        if (D2xxManager.g == null && D2xxManager.b != null) {
            D2xxManager.g = (UsbManager)D2xxManager.b.getApplicationContext().getSystemService("usb");
        }
        return D2xxManager.g != null;
    }
    
    public boolean setVIDPID(final int vendorId, final int productId) {
        boolean b = false;
        if (vendorId != 0 && productId != 0) {
            final m m = new m(vendorId, productId);
            if (D2xxManager.e.contains(m)) {
                Log.i("D2xx::", "Existing vid:" + vendorId + "  pid:" + productId);
                return true;
            }
            if (!D2xxManager.e.add(m)) {
                Log.d("D2xx::", "Failed to add VID/PID combination to list.");
            }
            else {
                b = true;
            }
        }
        else {
            Log.d("D2xx::", "Invalid parameter to setVIDPID");
        }
        return b;
    }
    
    public int[][] getVIDPID() {
        final int size = D2xxManager.e.size();
        final int[][] array = new int[2][size];
        for (int i = 0; i < size; ++i) {
            final m m = D2xxManager.e.get(i);
            array[0][i] = m.a();
            array[1][i] = m.b();
        }
        return array;
    }
    
    private void b() {
        synchronized (this.f) {
            for (int size = this.f.size(), i = 0; i < size; ++i) {
                this.f.remove(i);
            }
        }
        // monitorexit(this.f)
    }
    
    public int createDeviceInfoList(final Context parentContext) {
        final Iterator<UsbDevice> iterator = D2xxManager.g.getDeviceList().values().iterator();
        final ArrayList<FT_Device> f = new ArrayList<FT_Device>();
        int size = 0;
        if (parentContext == null) {
            return size;
        }
        a(parentContext);
        while (iterator.hasNext()) {
            final UsbDevice usbDevice = iterator.next();
            if (this.isFtDevice(usbDevice)) {
                for (int interfaceCount = usbDevice.getInterfaceCount(), i = 0; i < interfaceCount; ++i) {
                    if (this.b(usbDevice)) {
                        synchronized (this.f) {
                            FT_Device a = this.a(usbDevice);
                            if (a == null) {
                                a = new FT_Device(parentContext, D2xxManager.g, usbDevice, usbDevice.getInterface(i));
                            }
                            else {
                                this.f.remove(a);
                                a.a(parentContext);
                            }
                            f.add(a);
                        }
                        // monitorexit(this.f)
                    }
                }
            }
        }
        synchronized (this.f) {
            this.b();
            this.f = f;
            size = this.f.size();
        }
        // monitorexit(this.f)
        return size;
    }
    
    public synchronized int getDeviceInfoList(final int numDevs, final FtDeviceInfoListNode[] deviceList) {
        for (int i = 0; i < numDevs; ++i) {
            deviceList[i] = this.f.get(i).g;
        }
        return this.f.size();
    }
    
    public synchronized FtDeviceInfoListNode getDeviceInfoListDetail(final int index) {
        if (index > this.f.size() || index < 0) {
            return null;
        }
        return this.f.get(index).g;
    }
    
    public static int getLibraryVersion() {
        return 537919488;
    }
    
    private boolean a(final Context context, final FT_Device ft_Device, final DriverParameters driverParameters) {
        boolean b = false;
        if (ft_Device == null) {
            return b;
        }
        if (context == null) {
            return b;
        }
        ft_Device.a(context);
        if (driverParameters != null) {
            ft_Device.setDriverParameters(driverParameters);
        }
        if (ft_Device.a(D2xxManager.g) && ft_Device.isOpen()) {
            b = true;
        }
        return b;
    }
    
    public synchronized FT_Device openByUsbDevice(final Context parentContext, final UsbDevice dev, final DriverParameters params) {
        FT_Device a = null;
        if (this.isFtDevice(dev)) {
            a = this.a(dev);
            if (!this.a(parentContext, a, params)) {
                a = null;
            }
        }
        return a;
    }
    
    public synchronized FT_Device openByUsbDevice(final Context parentContext, final UsbDevice dev) {
        return this.openByUsbDevice(parentContext, dev, null);
    }
    
    public synchronized FT_Device openByIndex(final Context parentContext, final int index, final DriverParameters params) {
        final FT_Device ft_Device = null;
        if (index < 0) {
            return ft_Device;
        }
        if (parentContext == null) {
            return ft_Device;
        }
        a(parentContext);
        FT_Device ft_Device2 = this.f.get(index);
        if (!this.a(parentContext, ft_Device2, params)) {
            ft_Device2 = null;
        }
        return ft_Device2;
    }
    
    public synchronized FT_Device openByIndex(final Context parentContext, final int index) {
        return this.openByIndex(parentContext, index, null);
    }
    
    public synchronized FT_Device openBySerialNumber(final Context parentContext, final String serialNumber, final DriverParameters params) {
        FT_Device ft_Device = null;
        if (parentContext == null) {
            return ft_Device;
        }
        a(parentContext);
        for (int i = 0; i < this.f.size(); ++i) {
            final FT_Device ft_Device2 = this.f.get(i);
            if (ft_Device2 != null) {
                final FtDeviceInfoListNode g = ft_Device2.g;
                if (g == null) {
                    Log.d("D2xx::", "***devInfo cannot be null***");
                }
                else if (g.serialNumber.equals(serialNumber)) {
                    ft_Device = ft_Device2;
                    break;
                }
            }
        }
        if (!this.a(parentContext, ft_Device, params)) {
            ft_Device = null;
        }
        return ft_Device;
    }
    
    public synchronized FT_Device openBySerialNumber(final Context parentContext, final String serialNumber) {
        return this.openBySerialNumber(parentContext, serialNumber, null);
    }
    
    public synchronized FT_Device openByDescription(final Context parentContext, final String description, final DriverParameters params) {
        FT_Device ft_Device = null;
        if (parentContext == null) {
            return ft_Device;
        }
        a(parentContext);
        for (int i = 0; i < this.f.size(); ++i) {
            final FT_Device ft_Device2 = this.f.get(i);
            if (ft_Device2 != null) {
                final FtDeviceInfoListNode g = ft_Device2.g;
                if (g == null) {
                    Log.d("D2xx::", "***devInfo cannot be null***");
                }
                else if (g.description.equals(description)) {
                    ft_Device = ft_Device2;
                    break;
                }
            }
        }
        if (!this.a(parentContext, ft_Device, params)) {
            ft_Device = null;
        }
        return ft_Device;
    }
    
    public synchronized FT_Device openByDescription(final Context parentContext, final String description) {
        return this.openByDescription(parentContext, description, null);
    }
    
    public synchronized FT_Device openByLocation(final Context parentContext, final int location, final DriverParameters params) {
        FT_Device ft_Device = null;
        if (parentContext == null) {
            return ft_Device;
        }
        a(parentContext);
        for (int i = 0; i < this.f.size(); ++i) {
            final FT_Device ft_Device2 = this.f.get(i);
            if (ft_Device2 != null) {
                final FtDeviceInfoListNode g = ft_Device2.g;
                if (g == null) {
                    Log.d("D2xx::", "***devInfo cannot be null***");
                }
                else if (g.location == location) {
                    ft_Device = ft_Device2;
                    break;
                }
            }
        }
        if (!this.a(parentContext, ft_Device, params)) {
            ft_Device = null;
        }
        return ft_Device;
    }
    
    public synchronized FT_Device openByLocation(final Context parentContext, final int location) {
        return this.openByLocation(parentContext, location, null);
    }
    
    public int addUsbDevice(final UsbDevice dev) {
        int n = 0;
        if (this.isFtDevice(dev)) {
            for (int interfaceCount = dev.getInterfaceCount(), i = 0; i < interfaceCount; ++i) {
                if (this.b(dev)) {
                    synchronized (this.f) {
                        FT_Device a = this.a(dev);
                        if (a == null) {
                            a = new FT_Device(D2xxManager.b, D2xxManager.g, dev, dev.getInterface(i));
                        }
                        else {
                            a.a(D2xxManager.b);
                        }
                        this.f.add(a);
                        ++n;
                    }
                    // monitorexit(this.f)
                }
            }
        }
        return n;
    }
    
    public static class FtDeviceInfoListNode
    {
        public int flags;
        public short bcdDevice;
        public int type;
        public byte iSerialNumber;
        public int id;
        public int location;
        public String serialNumber;
        public String description;
        public int handle;
        public int breakOnParam;
        public short modemStatus;
        public short lineStatus;
    }
    
    public static class DriverParameters
    {
        private int a;
        private int b;
        private int c;
        private int d;
        
        public DriverParameters() {
            this.a = 16384;
            this.b = 16384;
            this.c = 16;
            this.d = 5000;
        }
        
        public boolean setMaxBufferSize(final int size) {
            boolean b = false;
            if (size >= 64 && size <= 262144) {
                this.a = size;
                b = true;
            }
            else {
                Log.e("D2xx::", "***bufferSize Out of correct range***");
            }
            return b;
        }
        
        public int getMaxBufferSize() {
            return this.a;
        }
        
        public boolean setMaxTransferSize(final int size) {
            boolean b = false;
            if (size >= 64 && size <= 262144) {
                this.b = size;
                b = true;
            }
            else {
                Log.e("D2xx::", "***maxTransferSize Out of correct range***");
            }
            return b;
        }
        
        public int getMaxTransferSize() {
            return this.b;
        }
        
        public boolean setBufferNumber(final int number) {
            boolean b = false;
            if (number >= 2 && number <= 16) {
                this.c = number;
                b = true;
            }
            else {
                Log.e("D2xx::", "***nrBuffers Out of correct range***");
            }
            return b;
        }
        
        public int getBufferNumber() {
            return this.c;
        }
        
        public boolean setReadTimeout(final int timeout) {
            this.d = timeout;
            return true;
        }
        
        public int getReadTimeout() {
            return this.d;
        }
    }
    
    public static class D2xxException extends IOException
    {
        public D2xxException() {
        }
        
        public D2xxException(final String ftStatusMsg) {
            super(ftStatusMsg);
        }
    }
}
