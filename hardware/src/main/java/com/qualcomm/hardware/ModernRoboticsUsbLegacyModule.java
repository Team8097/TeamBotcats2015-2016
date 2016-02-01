// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import java.util.Arrays;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;

public class ModernRoboticsUsbLegacyModule extends ModernRoboticsUsbDevice implements LegacyModule
{
    public static final boolean DEBUG_LOGGING = false;
    public static final int MONITOR_LENGTH = 13;
    public static final byte START_ADDRESS = 3;
    public static final byte MIN_PORT_NUMBER = 0;
    public static final byte MAX_PORT_NUMBER = 5;
    public static final byte NUMBER_OF_PORTS = 6;
    public static final byte I2C_ACTION_FLAG = -1;
    public static final byte I2C_NO_ACTION_FLAG = 0;
    public static final byte SIZE_ANALOG_BUFFER = 2;
    public static final byte SIZE_I2C_BUFFER = 27;
    public static final byte SIZE_OF_PORT_BUFFER = 32;
    public static final byte NXT_MODE_ANALOG = 0;
    public static final byte NXT_MODE_I2C = 1;
    public static final byte NXT_MODE_9V_ENABLED = 2;
    public static final byte NXT_MODE_DIGITAL_0 = 4;
    public static final byte NXT_MODE_DIGITAL_1 = 8;
    public static final byte NXT_MODE_READ = Byte.MIN_VALUE;
    public static final byte NXT_MODE_WRITE = 0;
    public static final byte BUFFER_FLAG_S0 = 1;
    public static final byte BUFFER_FLAG_S1 = 2;
    public static final byte BUFFER_FLAG_S2 = 4;
    public static final byte BUFFER_FLAG_S3 = 8;
    public static final byte BUFFER_FLAG_S4 = 16;
    public static final byte BUFFER_FLAG_S5 = 32;
    public static final int ADDRESS_BUFFER_STATUS = 3;
    public static final int ADDRESS_ANALOG_PORT_S0 = 4;
    public static final int ADDRESS_ANALOG_PORT_S1 = 6;
    public static final int ADDRESS_ANALOG_PORT_S2 = 8;
    public static final int ADDRESS_ANALOG_PORT_S3 = 10;
    public static final int ADDRESS_ANALOG_PORT_S4 = 12;
    public static final int ADDRESS_ANALOG_PORT_S5 = 14;
    public static final int ADDRESS_I2C_PORT_SO = 16;
    public static final int ADDRESS_I2C_PORT_S1 = 48;
    public static final int ADDRESS_I2C_PORT_S2 = 80;
    public static final int ADDRESS_I2C_PORT_S3 = 112;
    public static final int ADDRESS_I2C_PORT_S4 = 144;
    public static final int ADDRESS_I2C_PORT_S5 = 176;
    public static final byte OFFSET_I2C_PORT_MODE = 0;
    public static final byte OFFSET_I2C_PORT_I2C_ADDRESS = 1;
    public static final byte OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
    public static final byte OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
    public static final byte OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
    public static final byte OFFSET_I2C_PORT_FLAG = 31;
    public static final int[] ADDRESS_ANALOG_PORT_MAP;
    public static final int[] ADDRESS_I2C_PORT_MAP;
    public static final int[] BUFFER_FLAG_MAP;
    public static final int[] DIGITAL_LINE;
    public static final int[] PORT_9V_CAPABLE;
    private final ReadWriteRunnableSegment[] a;
    private final I2cController.I2cPortReadyCallback[] b;
    
    protected ModernRoboticsUsbLegacyModule(final SerialNumber serialNumber, final RobotUsbDevice device, final EventLoopManager manager) throws RobotCoreException, InterruptedException {
        super(serialNumber, manager, new ReadWriteRunnableStandard(serialNumber, device, 13, 3, false));
        this.a = new ReadWriteRunnableSegment[12];
        this.b = new I2cController.I2cPortReadyCallback[6];
        this.readWriteRunnable.setCallback(this);
        for (int i = 0; i < 6; ++i) {
            this.a[i] = this.readWriteRunnable.createSegment(i, ModernRoboticsUsbLegacyModule.ADDRESS_I2C_PORT_MAP[i], 32);
            this.a[i + 6] = this.readWriteRunnable.createSegment(i + 6, ModernRoboticsUsbLegacyModule.ADDRESS_I2C_PORT_MAP[i] + 31, 1);
            this.enableAnalogReadMode(i);
            this.readWriteRunnable.queueSegmentWrite(i);
        }
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB Legacy Module";
    }
    
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public void close() {
        super.close();
    }
    
    public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int port) {
        this.b[port] = callback;
    }
    
    public void deregisterForPortReadyCallback(final int port) {
        this.b[port] = null;
    }
    
    public void enableI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length) {
        this.a(physicalPort);
        this.b(length);
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.a[physicalPort].getWriteBuffer();
            writeBuffer[0] = -127;
            writeBuffer[1] = (byte)i2cAddress;
            writeBuffer[2] = (byte)memAddress;
            writeBuffer[3] = (byte)length;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length) {
        this.a(physicalPort);
        this.b(length);
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.a[physicalPort].getWriteBuffer();
            writeBuffer[0] = 1;
            writeBuffer[1] = (byte)i2cAddress;
            writeBuffer[2] = (byte)memAddress;
            writeBuffer[3] = (byte)length;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void enableAnalogReadMode(final int physicalPort) {
        this.a(physicalPort);
        try {
            this.a[physicalPort].getWriteLock().lock();
            this.a[physicalPort].getWriteBuffer()[0] = 0;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
        this.writeI2cCacheToController(physicalPort);
    }
    
    public void enable9v(final int physicalPort, final boolean enable) {
        if (Arrays.binarySearch(ModernRoboticsUsbLegacyModule.PORT_9V_CAPABLE, physicalPort) < 0) {
            throw new IllegalArgumentException("9v is only available on the following ports: " + Arrays.toString(ModernRoboticsUsbLegacyModule.PORT_9V_CAPABLE));
        }
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte b = this.a[physicalPort].getWriteBuffer()[0];
            byte b2;
            if (enable) {
                b2 = (byte)(b | 0x2);
            }
            else {
                b2 = (byte)(b & 0xFFFFFFFD);
            }
            this.a[physicalPort].getWriteBuffer()[0] = b2;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
        this.writeI2cCacheToController(physicalPort);
    }
    
    public void setReadMode(final int physicalPort, final int i2cAddr, final int memAddr, final int memLen) {
        this.a(physicalPort);
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.a[physicalPort].getWriteBuffer();
            writeBuffer[0] = -127;
            writeBuffer[1] = (byte)i2cAddr;
            writeBuffer[2] = (byte)memAddr;
            writeBuffer[3] = (byte)memLen;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void setWriteMode(final int physicalPort, final int i2cAddress, final int memAddress) {
        this.a(physicalPort);
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.a[physicalPort].getWriteBuffer();
            writeBuffer[0] = 1;
            writeBuffer[1] = (byte)i2cAddress;
            writeBuffer[2] = (byte)memAddress;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void setData(final int physicalPort, final byte[] data, final int length) {
        this.a(physicalPort);
        this.b(length);
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.a[physicalPort].getWriteBuffer();
            System.arraycopy(data, 0, writeBuffer, 4, length);
            writeBuffer[3] = (byte)length;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void setDigitalLine(final int physicalPort, final int line, final boolean set) {
        this.a(physicalPort);
        this.c(line);
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte b = this.a[physicalPort].getWriteBuffer()[0];
            byte b2;
            if (set) {
                b2 = (byte)(b | ModernRoboticsUsbLegacyModule.DIGITAL_LINE[line]);
            }
            else {
                b2 = (byte)(b & ~ModernRoboticsUsbLegacyModule.DIGITAL_LINE[line]);
            }
            this.a[physicalPort].getWriteBuffer()[0] = b2;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
        this.writeI2cCacheToController(physicalPort);
    }
    
    public byte[] readAnalog(final int physicalPort) {
        this.a(physicalPort);
        return this.read(ModernRoboticsUsbLegacyModule.ADDRESS_ANALOG_PORT_MAP[physicalPort], 2);
    }
    
    public byte[] getCopyOfReadBuffer(final int physicalPort) {
        this.a(physicalPort);
        Object o = null;
        try {
            this.a[physicalPort].getReadLock().lock();
            final byte[] readBuffer = this.a[physicalPort].getReadBuffer();
            o = new byte[readBuffer[3]];
            System.arraycopy(readBuffer, 4, o, 0, ((byte[])o).length);
        }
        finally {
            this.a[physicalPort].getReadLock().unlock();
        }
        return (byte[])o;
    }
    
    public byte[] getCopyOfWriteBuffer(final int physicalPort) {
        this.a(physicalPort);
        Object o = null;
        try {
            this.a[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.a[physicalPort].getWriteBuffer();
            o = new byte[writeBuffer[3]];
            System.arraycopy(writeBuffer, 4, o, 0, ((byte[])o).length);
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
        return (byte[])o;
    }
    
    public void copyBufferIntoWriteBuffer(final int physicalPort, final byte[] buffer) {
        this.a(physicalPort);
        this.b(buffer.length);
        try {
            this.a[physicalPort].getWriteLock().lock();
            System.arraycopy(buffer, 0, this.a[physicalPort].getWriteBuffer(), 4, buffer.length);
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void setI2cPortActionFlag(final int physicalPort) {
        this.a(physicalPort);
        try {
            this.a[physicalPort].getWriteLock().lock();
            this.a[physicalPort].getWriteBuffer()[31] = -1;
        }
        finally {
            this.a[physicalPort].getWriteLock().unlock();
        }
    }
    
    public boolean isI2cPortActionFlagSet(final int physicalPort) {
        this.a(physicalPort);
        boolean b = false;
        try {
            this.a[physicalPort].getReadLock().lock();
            b = (this.a[physicalPort].getReadBuffer()[31] == -1);
        }
        finally {
            this.a[physicalPort].getReadLock().unlock();
        }
        return b;
    }
    
    public void readI2cCacheFromController(final int physicalPort) {
        this.a(physicalPort);
        this.readWriteRunnable.queueSegmentRead(physicalPort);
    }
    
    public void writeI2cCacheToController(final int physicalPort) {
        this.a(physicalPort);
        this.readWriteRunnable.queueSegmentWrite(physicalPort);
    }
    
    public void writeI2cPortFlagOnlyToController(final int physicalPort) {
        this.a(physicalPort);
        final ReadWriteRunnableSegment readWriteRunnableSegment = this.a[physicalPort];
        final ReadWriteRunnableSegment readWriteRunnableSegment2 = this.a[physicalPort + 6];
        try {
            readWriteRunnableSegment.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteBuffer()[0] = readWriteRunnableSegment.getWriteBuffer()[31];
        }
        finally {
            readWriteRunnableSegment.getWriteLock().unlock();
            readWriteRunnableSegment2.getWriteLock().unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(physicalPort + 6);
    }
    
    public boolean isI2cPortInReadMode(final int physicalPort) {
        this.a(physicalPort);
        boolean b = false;
        try {
            this.a[physicalPort].getReadLock().lock();
            b = (this.a[physicalPort].getReadBuffer()[0] == -127);
        }
        finally {
            this.a[physicalPort].getReadLock().unlock();
        }
        return b;
    }
    
    public boolean isI2cPortInWriteMode(final int physicalPort) {
        this.a(physicalPort);
        boolean b = false;
        try {
            this.a[physicalPort].getReadLock().lock();
            b = (this.a[physicalPort].getReadBuffer()[0] == 1);
        }
        finally {
            this.a[physicalPort].getReadLock().unlock();
        }
        return b;
    }
    
    public boolean isI2cPortReady(final int physicalPort) {
        return this.a(physicalPort, this.read(3));
    }
    
    private void a(final int n) {
        if (n < 0 || n > 5) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", n, (byte)0, (byte)5));
        }
    }
    
    private void b(final int n) {
        if (n < 0 || n > 27) {
            throw new IllegalArgumentException(String.format("buffer length of %d is invalid; max value is %d", n, (byte)27));
        }
    }
    
    private void c(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
        }
    }
    
    @Override
    public void readComplete() throws InterruptedException {
        if (this.b == null) {
            return;
        }
        final byte read = this.read(3);
        for (int i = 0; i < 6; ++i) {
            if (this.b[i] != null && this.a(i, read)) {
                this.b[i].portIsReady(i);
            }
        }
    }
    
    private boolean a(final int n, final byte b) {
        return (b & ModernRoboticsUsbLegacyModule.BUFFER_FLAG_MAP[n]) == 0x0;
    }
    
    public Lock getI2cReadCacheLock(final int physicalPort) {
        this.a(physicalPort);
        return this.a[physicalPort].getReadLock();
    }
    
    public Lock getI2cWriteCacheLock(final int physicalPort) {
        this.a(physicalPort);
        return this.a[physicalPort].getWriteLock();
    }
    
    public byte[] getI2cReadCache(final int physicalPort) {
        this.a(physicalPort);
        return this.a[physicalPort].getReadBuffer();
    }
    
    public byte[] getI2cWriteCache(final int physicalPort) {
        this.a(physicalPort);
        return this.a[physicalPort].getWriteBuffer();
    }
    
    @Deprecated
    public void readI2cCacheFromModule(final int port) {
        this.readI2cCacheFromController(port);
    }
    
    @Deprecated
    public void writeI2cCacheToModule(final int port) {
        this.writeI2cCacheToController(port);
    }
    
    @Deprecated
    public void writeI2cPortFlagOnlyToModule(final int port) {
        this.writeI2cPortFlagOnlyToController(port);
    }
    
    static {
        ADDRESS_ANALOG_PORT_MAP = new int[] { 4, 6, 8, 10, 12, 14 };
        ADDRESS_I2C_PORT_MAP = new int[] { 16, 48, 80, 112, 144, 176 };
        BUFFER_FLAG_MAP = new int[] { 1, 2, 4, 8, 16, 32 };
        DIGITAL_LINE = new int[] { 4, 8 };
        PORT_9V_CAPABLE = new int[] { 4, 5 };
    }
}
