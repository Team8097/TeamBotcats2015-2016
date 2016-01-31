// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.util.Log;
import android.hardware.usb.UsbManager;
import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbRequest;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbDevice;

public class FT_Device
{
    long a;
    Boolean b;
    UsbDevice c;
    UsbInterface d;
    UsbEndpoint e;
    UsbEndpoint f;
    private UsbRequest k;
    private UsbDeviceConnection l;
    private a m;
    private Thread n;
    private Thread o;
    D2xxManager.FtDeviceInfoListNode g;
    private o p;
    private k q;
    private byte r;
    r h;
    q i;
    private D2xxManager.DriverParameters s;
    private int t;
    Context j;
    private int u;
    
    public FT_Device(final Context parentContext, final UsbManager usbManager, final UsbDevice dev, final UsbInterface itf) {
        this.t = 0;
        final byte[] array = new byte[255];
        this.j = parentContext;
        this.s = new D2xxManager.DriverParameters();
        try {
            this.c = dev;
            this.d = itf;
            this.e = null;
            this.f = null;
            this.u = 0;
            this.h = new r();
            this.i = new q();
            this.g = new D2xxManager.FtDeviceInfoListNode();
            this.k = new UsbRequest();
            this.a(usbManager.openDevice(this.c));
            if (this.c() == null) {
                Log.e("FTDI_Device::", "Failed to open the device!");
                throw new D2xxManager.D2xxException("Failed to open the device!");
            }
            this.c().claimInterface(this.d, false);
            final byte[] rawDescriptors = this.c().getRawDescriptors();
            final int deviceId = this.c.getDeviceId();
            this.t = this.d.getId() + 1;
            this.g.location = (deviceId << 4 | (this.t & 0xF));
            final ByteBuffer allocate = ByteBuffer.allocate(2);
            allocate.order(ByteOrder.LITTLE_ENDIAN);
            allocate.put(rawDescriptors[12]);
            allocate.put(rawDescriptors[13]);
            this.g.bcdDevice = allocate.getShort(0);
            this.g.iSerialNumber = rawDescriptors[16];
            this.g.serialNumber = this.c().getSerial();
            this.g.id = (this.c.getVendorId() << 16 | this.c.getProductId());
            this.g.breakOnParam = 8;
            this.c().controlTransfer(-128, 6, 0x300 | rawDescriptors[15], 0, array, 255, 0);
            this.g.description = this.a(array);
            switch (this.g.bcdDevice & 0xFF00) {
                case 512: {
                    if (this.g.iSerialNumber == 0) {
                        this.q = new f(this);
                        this.g.type = 0;
                        break;
                    }
                    this.g.type = 1;
                    this.q = new e(this);
                    break;
                }
                case 1024: {
                    this.q = new f(this);
                    this.g.type = 0;
                    break;
                }
                case 1280: {
                    this.q = new d(this);
                    this.g.type = 4;
                    this.n();
                    break;
                }
                case 1536: {
                    this.q = new k(this);
                    final short n = (short)(this.q.a((short)0) & 0x1);
                    this.q = null;
                    if (n == 0) {
                        this.g.type = 5;
                        this.q = new h(this);
                        break;
                    }
                    this.g.type = 5;
                    this.q = new i(this);
                    break;
                }
                case 1792: {
                    this.g.type = 6;
                    this.n();
                    this.q = new c(this);
                    break;
                }
                case 2048: {
                    this.g.type = 7;
                    this.n();
                    this.q = new j(this);
                    break;
                }
                case 2304: {
                    this.g.type = 8;
                    this.q = new g(this);
                    break;
                }
                case 4096: {
                    this.g.type = 9;
                    this.q = new l(this);
                    break;
                }
                case 6144: {
                    this.g.type = 10;
                    if (this.t == 1) {
                        this.g.flags = 2;
                        break;
                    }
                    this.g.flags = 0;
                    break;
                }
                case 6400: {
                    this.g.type = 11;
                    if (this.t != 4) {
                        this.g.flags = 2;
                        break;
                    }
                    final int maxPacketSize = this.c.getInterface(this.t - 1).getEndpoint(0).getMaxPacketSize();
                    Log.e("dev", "mInterfaceID : " + this.t + "   iMaxPacketSize : " + maxPacketSize);
                    if (maxPacketSize == 8) {
                        this.g.flags = 0;
                        break;
                    }
                    this.g.flags = 2;
                    break;
                }
                case 5888: {
                    this.g.type = 12;
                    this.g.flags = 2;
                    break;
                }
                default: {
                    this.g.type = 3;
                    this.q = new k(this);
                    break;
                }
            }
            switch (this.g.bcdDevice & 0xFF00) {
                case 5888:
                case 6144:
                case 6400: {
                    if (this.g.serialNumber == null) {
                        final byte[] array2 = new byte[16];
                        this.c().controlTransfer(-64, 144, 0, 27, array2, 16, 0);
                        String string = "";
                        for (int i = 0; i < 8; ++i) {
                            string = String.valueOf(string) + (char)array2[i * 2];
                        }
                        this.g.serialNumber = new String(string);
                        break;
                    }
                    break;
                }
            }
            switch (this.g.bcdDevice & 0xFF00) {
                case 6144:
                case 6400: {
                    if (this.t == 1) {
                        final D2xxManager.FtDeviceInfoListNode g = this.g;
                        g.description = String.valueOf(g.description) + " A";
                        final D2xxManager.FtDeviceInfoListNode g2 = this.g;
                        g2.serialNumber = String.valueOf(g2.serialNumber) + "A";
                        break;
                    }
                    if (this.t == 2) {
                        final D2xxManager.FtDeviceInfoListNode g3 = this.g;
                        g3.description = String.valueOf(g3.description) + " B";
                        final D2xxManager.FtDeviceInfoListNode g4 = this.g;
                        g4.serialNumber = String.valueOf(g4.serialNumber) + "B";
                        break;
                    }
                    if (this.t == 3) {
                        final D2xxManager.FtDeviceInfoListNode g5 = this.g;
                        g5.description = String.valueOf(g5.description) + " C";
                        final D2xxManager.FtDeviceInfoListNode g6 = this.g;
                        g6.serialNumber = String.valueOf(g6.serialNumber) + "C";
                        break;
                    }
                    if (this.t == 4) {
                        final D2xxManager.FtDeviceInfoListNode g7 = this.g;
                        g7.description = String.valueOf(g7.description) + " D";
                        final D2xxManager.FtDeviceInfoListNode g8 = this.g;
                        g8.serialNumber = String.valueOf(g8.serialNumber) + "D";
                        break;
                    }
                    break;
                }
            }
            this.c().releaseInterface(this.d);
            this.c().close();
            this.a((UsbDeviceConnection)null);
            this.p();
        }
        catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e("FTDI_Device::", ex.getMessage());
            }
        }
    }
    
    private final boolean f() {
        return this.i() || this.j() || this.b();
    }
    
    private final boolean g() {
        return this.m() || this.l() || this.k() || this.j() || this.b() || this.i() || this.h();
    }
    
    final boolean a() {
        return this.l() || this.j() || this.b();
    }
    
    private final boolean h() {
        return (this.g.bcdDevice & 0xFF00) == 0x1000;
    }
    
    private final boolean i() {
        return (this.g.bcdDevice & 0xFF00) == 0x900;
    }
    
    final boolean b() {
        return (this.g.bcdDevice & 0xFF00) == 0x800;
    }
    
    private final boolean j() {
        return (this.g.bcdDevice & 0xFF00) == 0x700;
    }
    
    private final boolean k() {
        return (this.g.bcdDevice & 0xFF00) == 0x600;
    }
    
    private final boolean l() {
        return (this.g.bcdDevice & 0xFF00) == 0x500;
    }
    
    private final boolean m() {
        return (this.g.bcdDevice & 0xFF00) == 0x400 || ((this.g.bcdDevice & 0xFF00) == 0x200 && this.g.iSerialNumber == 0);
    }
    
    private final String a(final byte[] array) throws UnsupportedEncodingException {
        return new String(array, 2, array[0] - 2, "UTF-16LE");
    }
    
    UsbDeviceConnection c() {
        return this.l;
    }
    
    void a(final UsbDeviceConnection l) {
        this.l = l;
    }
    
    synchronized boolean a(final Context j) {
        boolean b = false;
        if (j != null) {
            this.j = j;
            b = true;
        }
        return b;
    }
    
    protected void setDriverParameters(final D2xxManager.DriverParameters params) {
        this.s.setMaxBufferSize(params.getMaxBufferSize());
        this.s.setMaxTransferSize(params.getMaxTransferSize());
        this.s.setBufferNumber(params.getBufferNumber());
        this.s.setReadTimeout(params.getReadTimeout());
    }
    
    D2xxManager.DriverParameters d() {
        return this.s;
    }
    
    public int getReadTimeout() {
        return this.s.getReadTimeout();
    }
    
    private void n() {
        if (this.t == 1) {
            final D2xxManager.FtDeviceInfoListNode g = this.g;
            g.serialNumber = String.valueOf(g.serialNumber) + "A";
            final D2xxManager.FtDeviceInfoListNode g2 = this.g;
            g2.description = String.valueOf(g2.description) + " A";
        }
        else if (this.t == 2) {
            final D2xxManager.FtDeviceInfoListNode g3 = this.g;
            g3.serialNumber = String.valueOf(g3.serialNumber) + "B";
            final D2xxManager.FtDeviceInfoListNode g4 = this.g;
            g4.description = String.valueOf(g4.description) + " B";
        }
        else if (this.t == 3) {
            final D2xxManager.FtDeviceInfoListNode g5 = this.g;
            g5.serialNumber = String.valueOf(g5.serialNumber) + "C";
            final D2xxManager.FtDeviceInfoListNode g6 = this.g;
            g6.description = String.valueOf(g6.description) + " C";
        }
        else if (this.t == 4) {
            final D2xxManager.FtDeviceInfoListNode g7 = this.g;
            g7.serialNumber = String.valueOf(g7.serialNumber) + "D";
            final D2xxManager.FtDeviceInfoListNode g8 = this.g;
            g8.description = String.valueOf(g8.description) + " D";
        }
    }
    
    synchronized boolean a(final UsbManager usbManager) {
        final boolean b = false;
        if (this.isOpen()) {
            return b;
        }
        if (usbManager == null) {
            Log.e("FTDI_Device::", "UsbManager cannot be null.");
            return b;
        }
        if (this.c() != null) {
            Log.e("FTDI_Device::", "There should not have an UsbConnection.");
            return b;
        }
        this.a(usbManager.openDevice(this.c));
        if (this.c() == null) {
            Log.e("FTDI_Device::", "UsbConnection cannot be null.");
            return b;
        }
        if (!this.c().claimInterface(this.d, true)) {
            Log.e("FTDI_Device::", "ClaimInteface returned false.");
            return b;
        }
        Log.d("FTDI_Device::", "open SUCCESS");
        if (!this.q()) {
            Log.e("FTDI_Device::", "Failed to find endpoints.");
            return b;
        }
        this.k.initialize(this.l, this.e);
        Log.d("D2XX::", "**********************Device Opened**********************");
        this.p = new o(this);
        this.m = new a(this, this.p, this.c(), this.f);
        (this.o = new Thread(this.m)).setName("bulkInThread");
        (this.n = new Thread(new p(this.p))).setName("processRequestThread");
        this.a(true, true);
        this.o.start();
        this.n.start();
        this.o();
        return true;
    }
    
    public synchronized boolean isOpen() {
        return this.b;
    }
    
    private synchronized void o() {
        this.b = true;
        final D2xxManager.FtDeviceInfoListNode g = this.g;
        g.flags |= 0x1;
    }
    
    private synchronized void p() {
        this.b = false;
        final D2xxManager.FtDeviceInfoListNode g = this.g;
        g.flags &= 0x2;
    }
    
    public synchronized void close() {
        if (this.n != null) {
            this.n.interrupt();
        }
        if (this.o != null) {
            this.o.interrupt();
        }
        if (this.l != null) {
            this.l.releaseInterface(this.d);
            this.l.close();
            this.l = null;
        }
        if (this.p != null) {
            this.p.g();
        }
        this.n = null;
        this.o = null;
        this.m = null;
        this.p = null;
        this.p();
    }
    
    protected UsbDevice getUsbDevice() {
        return this.c;
    }
    
    public D2xxManager.FtDeviceInfoListNode getDeviceInfo() {
        return this.g;
    }
    
    public int read(final byte[] data, final int length, final long wait_ms) {
        if (!this.isOpen()) {
            return -1;
        }
        if (length <= 0) {
            return -2;
        }
        if (this.p == null) {
            return -3;
        }
        return this.p.a(data, length, wait_ms);
    }
    
    public int read(final byte[] data, final int length) {
        return this.read(data, length, this.s.getReadTimeout());
    }
    
    public int read(final byte[] data) {
        return this.read(data, data.length, this.s.getReadTimeout());
    }
    
    public int write(final byte[] data, final int length) {
        return this.write(data, length, true);
    }
    
    public int write(final byte[] data, final int length, final boolean wait) {
        int n = -1;
        if (!this.isOpen()) {
            return n;
        }
        if (length < 0) {
            return n;
        }
        final UsbRequest k = this.k;
        if (wait) {
            k.setClientData((Object)this);
        }
        if (length == 0) {
            if (k.queue(ByteBuffer.wrap(new byte[1]), length)) {
                n = length;
            }
        }
        else if (k.queue(ByteBuffer.wrap(data), length)) {
            n = length;
        }
        if (wait) {
            UsbRequest requestWait;
            do {
                requestWait = this.l.requestWait();
                if (requestWait != null) {
                    continue;
                }
                Log.e("FTDI_Device::", "UsbConnection.requestWait() == null");
                return -99;
            } while (requestWait.getClientData() != this);
        }
        return n;
    }
    
    public int write(final byte[] data) {
        return this.write(data, data.length, true);
    }
    
    public short getModemStatus() {
        if (!this.isOpen()) {
            return -1;
        }
        if (this.p == null) {
            return -2;
        }
        this.a &= 0xFFFFFFFFFFFFFFFDL;
        return (short)(this.g.modemStatus & 0xFF);
    }
    
    public short getLineStatus() {
        if (!this.isOpen()) {
            return -1;
        }
        if (this.p == null) {
            return -2;
        }
        return this.g.lineStatus;
    }
    
    public int getQueueStatus() {
        if (!this.isOpen()) {
            return -1;
        }
        if (this.p == null) {
            return -2;
        }
        return this.p.c();
    }
    
    public boolean readBufferFull() {
        return this.p.a();
    }
    
    public long getEventStatus() {
        if (!this.isOpen()) {
            return -1L;
        }
        if (this.p == null) {
            return -2L;
        }
        final long a = this.a;
        this.a = 0L;
        return a;
    }
    
    public boolean setBaudRate(final int baudRate) {
        byte b = 1;
        final int[] array = new int[2];
        boolean b2 = false;
        if (!this.isOpen()) {
            return b2;
        }
        switch (baudRate) {
            case 300: {
                array[0] = 10000;
                break;
            }
            case 600: {
                array[0] = 5000;
                break;
            }
            case 1200: {
                array[0] = 2500;
                break;
            }
            case 2400: {
                array[0] = 1250;
                break;
            }
            case 4800: {
                array[0] = 625;
                break;
            }
            case 9600: {
                array[0] = 16696;
                break;
            }
            case 19200: {
                array[0] = 32924;
                break;
            }
            case 38400: {
                array[0] = 49230;
                break;
            }
            case 57600: {
                array[0] = 52;
                break;
            }
            case 115200: {
                array[0] = 26;
                break;
            }
            case 230400: {
                array[0] = 13;
                break;
            }
            case 460800: {
                array[0] = 16390;
                break;
            }
            case 921600: {
                array[0] = 32771;
                break;
            }
            default: {
                if (this.f() && baudRate >= 1200) {
                    b = com.ftdi.j2xx.b.a(baudRate, array);
                }
                else {
                    b = com.ftdi.j2xx.b.a(baudRate, array, this.g());
                }
                break;
            }
        }
        if (this.a() || this.i() || this.h()) {
            final int[] array2 = array;
            final int n = 1;
            array2[n] <<= 8;
            final int[] array3 = array;
            final int n2 = 1;
            array3[n2] &= 0xFF00;
            final int[] array4 = array;
            final int n3 = 1;
            array4[n3] |= this.t;
        }
        if (b == 1 && this.c().controlTransfer(64, 3, array[0], array[1], (byte[])null, 0, 0) == 0) {
            b2 = true;
        }
        return b2;
    }
    
    public boolean setDataCharacteristics(final byte dataBits, final byte stopBits, final byte parity) {
        boolean b = false;
        if (!this.isOpen()) {
            return b;
        }
        final short breakOnParam = (short)((short)(dataBits | parity << 8) | stopBits << 11);
        this.g.breakOnParam = breakOnParam;
        if (this.c().controlTransfer(64, 4, (int)breakOnParam, this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean setBreakOn() {
        return this.a(16384);
    }
    
    public boolean setBreakOff() {
        return this.a(0);
    }
    
    private boolean a(final int n) {
        boolean b = false;
        final int n2 = this.g.breakOnParam | n;
        if (!this.isOpen()) {
            return b;
        }
        if (this.c().controlTransfer(64, 4, n2, this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean setFlowControl(final short flowControl, final byte xon, final byte xoff) {
        boolean b = false;
        int n = 0;
        if (!this.isOpen()) {
            return b;
        }
        if (flowControl == 1024) {
            n = (short)((short)(xoff << 8) | (xon & 0xFF));
        }
        if (this.c().controlTransfer(64, 2, n, this.t | flowControl, (byte[])null, 0, 0) == 0) {
            b = true;
            if (flowControl == 256) {
                b = this.setRts();
            }
            else if (flowControl == 512) {
                b = this.setDtr();
            }
        }
        return b;
    }
    
    public boolean setRts() {
        boolean b = false;
        final int n = 514;
        if (!this.isOpen()) {
            return b;
        }
        if (this.c().controlTransfer(64, 1, n, this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean clrRts() {
        boolean b = false;
        final int n = 512;
        if (!this.isOpen()) {
            return b;
        }
        if (this.c().controlTransfer(64, 1, n, this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean setDtr() {
        boolean b = false;
        final int n = 257;
        if (!this.isOpen()) {
            return b;
        }
        if (this.c().controlTransfer(64, 1, n, this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean clrDtr() {
        boolean b = false;
        final int n = 256;
        if (!this.isOpen()) {
            return b;
        }
        if (this.c().controlTransfer(64, 1, n, this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public boolean setChars(final byte eventChar, final byte eventCharEnable, final byte errorChar, final byte errorCharEnable) {
        boolean b = false;
        final r h = new r();
        h.a = eventChar;
        h.b = eventCharEnable;
        h.c = errorChar;
        h.d = errorCharEnable;
        if (!this.isOpen()) {
            return b;
        }
        int n = eventChar & 0xFF;
        if (eventCharEnable != 0) {
            n |= 0x100;
        }
        if (this.c().controlTransfer(64, 6, n, this.t, (byte[])null, 0, 0) != 0) {
            return b;
        }
        int n2 = errorChar & 0xFF;
        if (errorCharEnable > 0) {
            n2 |= 0x100;
        }
        if (this.c().controlTransfer(64, 7, n2, this.t, (byte[])null, 0, 0) == 0) {
            this.h = h;
            b = true;
        }
        return b;
    }
    
    public boolean setBitMode(final byte mask, final byte bitMode) {
        final int type = this.g.type;
        boolean b = false;
        if (!this.isOpen()) {
            return b;
        }
        if (type == 1) {
            return b;
        }
        if (type == 0 && bitMode != 0) {
            if ((bitMode & 0x1) == 0x0) {
                return b;
            }
        }
        else if (type == 4 && bitMode != 0) {
            if ((bitMode & 0x1F) == 0x0) {
                return b;
            }
            if (bitMode == 2 & this.d.getId() != 0) {
                return b;
            }
        }
        else if (type == 5 && bitMode != 0) {
            if ((bitMode & 0x25) == 0x0) {
                return b;
            }
        }
        else if (type == 6 && bitMode != 0) {
            if ((bitMode & 0x5F) == 0x0) {
                return b;
            }
            if ((bitMode & 0x48) > 0 & this.d.getId() != 0) {
                return b;
            }
        }
        else if (type == 7 && bitMode != 0) {
            if ((bitMode & 0x7) == 0x0) {
                return b;
            }
            if (bitMode == 2 & this.d.getId() != 0 & this.d.getId() != 1) {
                return b;
            }
        }
        else if (type == 8 && bitMode != 0 && bitMode > 64) {
            return b;
        }
        if (this.c().controlTransfer(64, 11, bitMode << 8 | (mask & 0xFF), this.t, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public byte getBitMode() {
        final byte[] array = { 0 };
        if (!this.isOpen()) {
            return -1;
        }
        if (!this.g()) {
            return -2;
        }
        if (this.c().controlTransfer(-64, 12, 0, this.t, array, array.length, 0) == array.length) {
            return array[0];
        }
        return -3;
    }
    
    public boolean resetDevice() {
        boolean b = false;
        if (!this.isOpen()) {
            return b;
        }
        if (this.c().controlTransfer(64, 0, 0, 0, (byte[])null, 0, 0) == 0) {
            b = true;
        }
        return b;
    }
    
    public int VendorCmdSet(final int request, final int wValue) {
        if (!this.isOpen()) {
            return -1;
        }
        return this.c().controlTransfer(64, request, wValue, this.t, (byte[])null, 0, 0);
    }
    
    public int VendorCmdSet(final int request, final int wValue, final byte[] buf, final int datalen) {
        if (!this.isOpen()) {
            Log.e("FTDI_Device::", "VendorCmdSet: Device not open");
            return -1;
        }
        if (datalen < 0) {
            Log.e("FTDI_Device::", "VendorCmdSet: Invalid data length");
            return -1;
        }
        if (buf == null) {
            if (datalen > 0) {
                Log.e("FTDI_Device::", "VendorCmdSet: buf is null!");
                return -1;
            }
        }
        else if (buf.length < datalen) {
            Log.e("FTDI_Device::", "VendorCmdSet: length of buffer is smaller than data length to set");
            return -1;
        }
        return this.c().controlTransfer(64, request, wValue, this.t, buf, datalen, 0);
    }
    
    public int VendorCmdGet(final int request, final int wValue, final byte[] buf, final int datalen) {
        if (!this.isOpen()) {
            Log.e("FTDI_Device::", "VendorCmdGet: Device not open");
            return -1;
        }
        if (datalen < 0) {
            Log.e("FTDI_Device::", "VendorCmdGet: Invalid data length");
            return -1;
        }
        if (buf == null) {
            Log.e("FTDI_Device::", "VendorCmdGet: buf is null");
            return -1;
        }
        if (buf.length < datalen) {
            Log.e("FTDI_Device::", "VendorCmdGet: length of buffer is smaller than data length to get");
            return -1;
        }
        return this.c().controlTransfer(-64, request, wValue, this.t, buf, datalen, 0);
    }
    
    public void stopInTask() {
        try {
            if (!this.m.c()) {
                this.m.a();
            }
        }
        catch (InterruptedException ex) {
            Log.d("FTDI_Device::", "stopInTask called!");
            ex.printStackTrace();
        }
    }
    
    public void restartInTask() {
        this.m.b();
    }
    
    public boolean stoppedInTask() {
        return this.m.c();
    }
    
    public boolean purge(final byte flags) {
        boolean b = false;
        boolean b2 = false;
        if ((flags & 0x1) == 0x1) {
            b = true;
        }
        if ((flags & 0x2) == 0x2) {
            b2 = true;
        }
        return this.a(b, b2);
    }
    
    private boolean a(final boolean b, final boolean b2) {
        boolean b3 = false;
        int controlTransfer = 0;
        if (!this.isOpen()) {
            return b3;
        }
        if (b) {
            final int n = 1;
            for (int i = 0; i < 6; ++i) {
                controlTransfer = this.c().controlTransfer(64, 0, n, this.t, (byte[])null, 0, 0);
            }
            if (controlTransfer > 0) {
                return b3;
            }
            this.p.e();
        }
        if (b2 && this.c().controlTransfer(64, 0, 2, this.t, (byte[])null, 0, 0) == 0) {
            b3 = true;
        }
        return b3;
    }
    
    public boolean setLatencyTimer(final byte latency) {
        final boolean b = false;
        final int n = latency & 0xFF;
        if (!this.isOpen()) {
            return b;
        }
        boolean b2;
        if (this.c().controlTransfer(64, 9, n, this.t, (byte[])null, 0, 0) == 0) {
            this.r = latency;
            b2 = true;
        }
        else {
            b2 = false;
        }
        return b2;
    }
    
    public byte getLatencyTimer() {
        final byte[] array = { 0 };
        if (!this.isOpen()) {
            return -1;
        }
        if (this.c().controlTransfer(-64, 10, 0, this.t, array, array.length, 0) == array.length) {
            return array[0];
        }
        return 0;
    }
    
    public boolean setEventNotification(final long Mask) {
        boolean b = false;
        if (!this.isOpen()) {
            return b;
        }
        if (Mask != 0L) {
            this.a = 0L;
            this.i.a = Mask;
            b = true;
        }
        return b;
    }
    
    private boolean q() {
        for (int i = 0; i < this.d.getEndpointCount(); ++i) {
            Log.i("FTDI_Device::", "EP: " + String.format("0x%02X", this.d.getEndpoint(i).getAddress()));
            if (this.d.getEndpoint(i).getType() == 2) {
                if (this.d.getEndpoint(i).getDirection() == 128) {
                    this.f = this.d.getEndpoint(i);
                    this.u = this.f.getMaxPacketSize();
                }
                else {
                    this.e = this.d.getEndpoint(i);
                }
            }
            else {
                Log.i("FTDI_Device::", "Not Bulk Endpoint");
            }
        }
        return this.e != null && this.f != null;
    }
    
    public FT_EEPROM eepromRead() {
        if (!this.isOpen()) {
            return null;
        }
        return this.q.a();
    }
    
    public short eepromWrite(final FT_EEPROM eeData) {
        if (!this.isOpen()) {
            return -1;
        }
        return this.q.a(eeData);
    }
    
    public boolean eepromErase() {
        boolean b = false;
        if (!this.isOpen()) {
            return b;
        }
        if (this.q.c() == 0) {
            b = true;
        }
        return b;
    }
    
    public int eepromWriteUserArea(final byte[] data) {
        if (!this.isOpen()) {
            return 0;
        }
        return this.q.a(data);
    }
    
    public byte[] eepromReadUserArea(final int length) {
        if (!this.isOpen()) {
            return null;
        }
        return this.q.a(length);
    }
    
    public int eepromGetUserAreaSize() {
        if (!this.isOpen()) {
            return -1;
        }
        return this.q.b();
    }
    
    public int eepromReadWord(final short offset) {
        final int n = -1;
        if (!this.isOpen()) {
            return n;
        }
        return this.q.a(offset);
    }
    
    public boolean eepromWriteWord(final short address, final short data) {
        final boolean b = false;
        if (!this.isOpen()) {
            return b;
        }
        return this.q.a(address, data);
    }
    
    int e() {
        return this.u;
    }
}
