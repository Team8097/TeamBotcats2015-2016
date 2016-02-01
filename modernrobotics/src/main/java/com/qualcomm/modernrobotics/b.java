// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.modernrobotics;

import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Arrays;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.concurrent.BlockingQueue;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

class b implements RobotUsbDevice
{
    public boolean a;
    public SerialNumber b;
    public String c;
    private byte[] f;
    private byte[] g;
    private BlockingQueue<byte[]> h;
    protected byte[] d;
    protected byte[] e;
    
    public void setBaudRate(final int rate) throws RobotCoreException {
    }
    
    public void setDataCharacteristics(final byte dataBits, final byte stopBits, final byte parity) throws RobotCoreException {
    }
    
    public void setLatencyTimer(final int latencyTimer) throws RobotCoreException {
    }
    
    public void purge(final RobotUsbDevice.Channel channel) throws RobotCoreException {
        this.h.clear();
    }
    
    public void write(final byte[] data) throws RobotCoreException {
        this.a(data);
    }
    
    public int read(final byte[] data) throws RobotCoreException {
        return this.read(data, data.length, Integer.MAX_VALUE);
    }
    
    public int read(final byte[] data, final int length, final int timeout) throws RobotCoreException {
        return this.a(data, length, timeout);
    }
    
    public void close() {
    }
    
    private void a(final byte[] array) {
        if (this.a) {
            RobotLog.d(this.b + " USB recd: " + Arrays.toString(array));
        }
        new Thread() {
            @Override
            public void run() {
                final int unsignedByteToInt = TypeConversion.unsignedByteToInt(array[3]);
                final int unsignedByteToInt2 = TypeConversion.unsignedByteToInt(array[4]);
                try {
                    Thread.sleep(10L);
                    byte[] copy = null;
                    switch (array[2]) {
                        case Byte.MIN_VALUE: {
                            copy = new byte[com.qualcomm.modernrobotics.b.this.e.length + unsignedByteToInt2];
                            System.arraycopy(com.qualcomm.modernrobotics.b.this.e, 0, copy, 0, com.qualcomm.modernrobotics.b.this.e.length);
                            copy[3] = array[3];
                            copy[4] = array[4];
                            System.arraycopy(com.qualcomm.modernrobotics.b.this.f, unsignedByteToInt, copy, com.qualcomm.modernrobotics.b.this.e.length, unsignedByteToInt2);
                            break;
                        }
                        case 0: {
                            copy = new byte[com.qualcomm.modernrobotics.b.this.d.length];
                            System.arraycopy(com.qualcomm.modernrobotics.b.this.d, 0, copy, 0, com.qualcomm.modernrobotics.b.this.d.length);
                            copy[3] = array[3];
                            copy[4] = 0;
                            System.arraycopy(array, 5, com.qualcomm.modernrobotics.b.this.f, unsignedByteToInt, unsignedByteToInt2);
                            break;
                        }
                        default: {
                            copy = Arrays.copyOf(array, array.length);
                            copy[2] = -1;
                            copy[3] = array[3];
                            copy[4] = 0;
                            break;
                        }
                    }
                    com.qualcomm.modernrobotics.b.this.h.put(copy);
                }
                catch (InterruptedException ex) {
                    RobotLog.w("USB mock bus interrupted during write");
                }
            }
        }.start();
    }
    
    private int a(final byte[] array, final int n, final int n2) {
        byte[] copy = null;
        if (this.g != null) {
            copy = Arrays.copyOf(this.g, this.g.length);
            this.g = null;
        }
        else {
            try {
                copy = this.h.poll(n2, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ex) {
                RobotLog.w("USB mock bus interrupted during read");
            }
        }
        if (copy == null) {
            RobotLog.w("USB mock bus read timeout");
            System.arraycopy(this.e, 0, array, 0, this.e.length);
            array[2] = -1;
            array[4] = 0;
        }
        else {
            System.arraycopy(copy, 0, array, 0, n);
        }
        if (copy != null && n < copy.length) {
            this.g = new byte[copy.length - n];
            System.arraycopy(copy, array.length, this.g, 0, this.g.length);
        }
        if (this.a) {
            RobotLog.d(this.b + " USB send: " + Arrays.toString(array));
        }
        return array.length;
    }
}
