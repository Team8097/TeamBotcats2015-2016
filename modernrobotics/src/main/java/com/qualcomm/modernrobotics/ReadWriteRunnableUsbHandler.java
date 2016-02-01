// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;
import java.util.Arrays;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class ReadWriteRunnableUsbHandler
{
    protected final int MAX_SEQUENTIAL_USB_ERROR_COUNT = 10;
    protected final int USB_MSG_TIMEOUT = 100;
    protected int usbSequentialReadErrorCount;
    protected int usbSequentialWriteErrorCount;
    protected RobotUsbDevice device;
    protected final byte[] respHeader;
    protected byte[] writeCmd;
    protected byte[] readCmd;
    
    public ReadWriteRunnableUsbHandler(final RobotUsbDevice device) {
        this.usbSequentialReadErrorCount = 0;
        this.usbSequentialWriteErrorCount = 0;
        this.respHeader = new byte[5];
        this.writeCmd = new byte[] { 85, -86, 0, 0, 0 };
        this.readCmd = new byte[] { 85, -86, -128, 0, 0 };
        this.device = device;
    }
    
    public void throwIfUsbErrorCountIsTooHigh() throws RobotCoreException {
        if (this.usbSequentialReadErrorCount > 10 || this.usbSequentialWriteErrorCount > 10) {
            throw new RobotCoreException("Too many sequential USB errors on device");
        }
    }
    
    public void read(final int address, final byte[] buffer) throws RobotCoreException, InterruptedException {
        this.a(address, buffer);
    }
    
    private void a(final int n, final byte[] array) throws RobotCoreException, InterruptedException {
        this.readCmd[3] = (byte)n;
        this.readCmd[4] = (byte)array.length;
        this.device.write(this.readCmd);
        Arrays.fill(this.respHeader, (byte)0);
        final int read = this.device.read(this.respHeader, this.respHeader.length, 100);
        if (!a.a(this.respHeader, array.length)) {
            ++this.usbSequentialReadErrorCount;
            if (read == this.respHeader.length) {
                Thread.sleep(100L);
                this.a(this.readCmd, "comm error");
            }
            else {
                this.a(this.readCmd, "comm timeout");
            }
        }
        if (this.device.read(array, array.length, 100) != array.length) {
            this.a(this.readCmd, "comm timeout on payload");
        }
        this.usbSequentialReadErrorCount = 0;
    }
    
    public void write(final int address, final byte[] buffer) throws RobotCoreException, InterruptedException {
        this.b(address, buffer);
    }
    
    private void b(final int n, final byte[] array) throws RobotCoreException, InterruptedException {
        this.writeCmd[3] = (byte)n;
        this.writeCmd[4] = (byte)array.length;
        this.device.write(Util.concatenateByteArrays(this.writeCmd, array));
        Arrays.fill(this.respHeader, (byte)0);
        final int read = this.device.read(this.respHeader, this.respHeader.length, 100);
        if (!a.a(this.respHeader, 0)) {
            ++this.usbSequentialWriteErrorCount;
            if (read == this.respHeader.length) {
                Thread.sleep(100L);
                this.a(this.writeCmd, "comm error");
            }
            else {
                this.a(this.writeCmd, "comm timeout");
            }
        }
        this.usbSequentialWriteErrorCount = 0;
    }
    
    public void purge(final RobotUsbDevice.Channel channel) throws RobotCoreException {
        this.device.purge(channel);
    }
    
    public void close() {
        this.device.close();
    }
    
    private void a(final byte[] buffer, final String s) throws RobotCoreException {
        RobotLog.w(bufferToString(buffer) + " -> " + bufferToString(this.respHeader));
        this.device.purge(RobotUsbDevice.Channel.BOTH);
        throw new RobotCoreException(s);
    }
    
    protected static String bufferToString(final byte[] buffer) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (buffer.length > 0) {
            sb.append(String.format("%02x", buffer[0]));
        }
        for (int i = 1; i < buffer.length; ++i) {
            sb.append(String.format(" %02x", buffer[i]));
        }
        sb.append("]");
        return sb.toString();
    }
}
