// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

public class ModernRoboticsUsbDeviceInterfaceModule extends ModernRoboticsUsbDevice implements DeviceInterfaceModule
{
    public static final boolean DEBUG_LOGGING = false;
    public static final int MIN_I2C_PORT_NUMBER = 0;
    public static final int MAX_I2C_PORT_NUMBER = 5;
    public static final int MAX_ANALOG_PORT_NUMBER = 7;
    public static final int MIN_ANALOG_PORT_NUMBER = 0;
    public static final int NUMBER_OF_PORTS = 6;
    public static final int START_ADDRESS = 3;
    public static final int MONITOR_LENGTH = 21;
    public static final int SIZE_I2C_BUFFER = 27;
    public static final int SIZE_ANALOG_BUFFER = 2;
    public static final int WORD_SIZE = 2;
    public static final int ADDRESS_BUFFER_STATUS = 3;
    public static final int ADDRESS_ANALOG_PORT_A0 = 4;
    public static final int ADDRESS_ANALOG_PORT_A1 = 6;
    public static final int ADDRESS_ANALOG_PORT_A2 = 8;
    public static final int ADDRESS_ANALOG_PORT_A3 = 10;
    public static final int ADDRESS_ANALOG_PORT_A4 = 12;
    public static final int ADDRESS_ANALOG_PORT_A5 = 14;
    public static final int ADDRESS_ANALOG_PORT_A6 = 16;
    public static final int ADDRESS_ANALOG_PORT_A7 = 18;
    public static final int ADDRESS_DIGITAL_INPUT_STATE = 20;
    public static final int ADDRESS_DIGITAL_IO_CONTROL = 21;
    public static final int ADDRESS_DIGITAL_OUTPUT_STATE = 22;
    public static final int ADDRESS_LED_SET = 23;
    public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_0 = 24;
    public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_1 = 30;
    public static final int ADDRESS_PULSE_OUTPUT_PORT_0 = 36;
    public static final int ADDRESS_PULSE_OUTPUT_PORT_1 = 40;
    public static final int ADDRESS_I2C0 = 48;
    public static final int ADDRESS_I2C1 = 80;
    public static final int ADDRESS_I2C2 = 112;
    public static final int ADDRESS_I2C3 = 144;
    public static final int ADDRESS_I2C4 = 176;
    public static final int ADDRESS_I2C5 = 208;
    public static final byte BUFFER_FLAG_I2C0 = 1;
    public static final byte BUFFER_FLAG_I2C1 = 2;
    public static final byte BUFFER_FLAG_I2C2 = 4;
    public static final byte BUFFER_FLAG_I2C3 = 8;
    public static final byte BUFFER_FLAG_I2C4 = 16;
    public static final byte BUFFER_FLAG_I2C5 = 32;
    public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_VOLTAGE = 0;
    public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_FREQ = 2;
    public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_MODE = 4;
    public static final int ANALOG_VOLTAGE_OUTPUT_BUFFER_SIZE = 5;
    public static final int OFFSET_PULSE_OUTPUT_TIME = 0;
    public static final int OFFSET_PULSE_OUTPUT_PERIOD = 2;
    public static final int PULSE_OUTPUT_BUFFER_SIZE = 4;
    public static final int OFFSET_I2C_PORT_MODE = 0;
    public static final int OFFSET_I2C_PORT_I2C_ADDRESS = 1;
    public static final int OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
    public static final int OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
    public static final int OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
    public static final int OFFSET_I2C_PORT_FLAG = 31;
    public static final int I2C_PORT_BUFFER_SIZE = 32;
    public static final byte I2C_MODE_READ = Byte.MIN_VALUE;
    public static final byte I2C_MODE_WRITE = 0;
    public static final byte I2C_ACTION_FLAG = -1;
    public static final byte I2C_NO_ACTION_FLAG = 0;
    public static final int LED_0_BIT_MASK = 1;
    public static final int LED_1_BIT_MASK = 2;
    public static final int[] LED_BIT_MASK_MAP;
    public static final int D0_MASK = 1;
    public static final int D1_MASK = 2;
    public static final int D2_MASK = 4;
    public static final int D3_MASK = 8;
    public static final int D4_MASK = 16;
    public static final int D5_MASK = 32;
    public static final int D6_MASK = 64;
    public static final int D7_MASK = 128;
    public static final int[] ADDRESS_DIGITAL_BIT_MASK;
    public static final int[] ADDRESS_ANALOG_PORT_MAP;
    public static final int[] ADDRESS_VOLTAGE_OUTPUT_PORT_MAP;
    public static final int[] ADDRESS_PULSE_OUTPUT_PORT_MAP;
    public static final int[] ADDRESS_I2C_PORT_MAP;
    public static final int[] BUFFER_FLAG_MAP;
    private static final int[] a;
    private static final int[] b;
    private static final int[] c;
    private static final int[] d;
    private final I2cController.I2cPortReadyCallback[] e;
    private final ElapsedTime[] f;
    private ReadWriteRunnableSegment[] g;
    private ReadWriteRunnableSegment[] h;
    private ReadWriteRunnableSegment[] i;
    private ReadWriteRunnableSegment[] j;
    
    protected ModernRoboticsUsbDeviceInterfaceModule(final SerialNumber serialNumber, final RobotUsbDevice device, final EventLoopManager manager) throws RobotCoreException, InterruptedException {
        super(serialNumber, manager, new ReadWriteRunnableStandard(serialNumber, device, 21, 3, false));
        this.e = new I2cController.I2cPortReadyCallback[6];
        this.f = new ElapsedTime[6];
        this.g = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.a.length];
        this.h = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.b.length];
        this.i = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.c.length];
        this.j = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.d.length];
        for (int i = 0; i < ModernRoboticsUsbDeviceInterfaceModule.a.length; ++i) {
            this.g[i] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.a[i], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_VOLTAGE_OUTPUT_PORT_MAP[i], 5);
        }
        for (int j = 0; j < ModernRoboticsUsbDeviceInterfaceModule.b.length; ++j) {
            this.h[j] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.b[j], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_PULSE_OUTPUT_PORT_MAP[j], 4);
        }
        for (int k = 0; k < ModernRoboticsUsbDeviceInterfaceModule.c.length; ++k) {
            this.i[k] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.c[k], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_I2C_PORT_MAP[k], 32);
            this.j[k] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.d[k], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_I2C_PORT_MAP[k] + 31, 1);
        }
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB Device Interface Module";
    }
    
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    public int getAnalogInputValue(final int channel) {
        this.d(channel);
        return TypeConversion.byteArrayToShort(this.read(ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_ANALOG_PORT_MAP[channel], 2), ByteOrder.LITTLE_ENDIAN);
    }
    
    public DigitalChannelController.Mode getDigitalChannelMode(final int channel) {
        return this.a(channel, (int)this.getDigitalIOControlByte());
    }
    
    public void setDigitalChannelMode(final int channel, final DigitalChannelController.Mode mode) {
        final int a = this.a(channel, mode);
        final byte fromWriteCache = this.readFromWriteCache(21);
        byte data;
        if (mode == DigitalChannelController.Mode.OUTPUT) {
            data = (byte)(fromWriteCache | a);
        }
        else {
            data = (byte)(fromWriteCache & a);
        }
        this.write(21, (int)data);
    }
    
    public boolean getDigitalChannelState(final int channel) {
        int n;
        if (DigitalChannelController.Mode.OUTPUT == this.getDigitalChannelMode(channel)) {
            n = this.getDigitalOutputStateByte();
        }
        else {
            n = this.getDigitalInputStateByte();
        }
        return (n & ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[channel]) > 0;
    }
    
    public void setDigitalChannelState(final int channel, final boolean state) {
        if (DigitalChannelController.Mode.OUTPUT == this.getDigitalChannelMode(channel)) {
            final byte fromWriteCache = this.readFromWriteCache(22);
            int n;
            if (state) {
                n = (fromWriteCache | ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[channel]);
            }
            else {
                n = (fromWriteCache & ~ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[channel]);
            }
            this.setDigitalOutputByte((byte)n);
        }
    }
    
    public int getDigitalInputStateByte() {
        return TypeConversion.unsignedByteToInt(this.read(20));
    }
    
    public byte getDigitalIOControlByte() {
        return this.read(21);
    }
    
    public void setDigitalIOControlByte(final byte input) {
        this.write(21, input);
    }
    
    public byte getDigitalOutputStateByte() {
        return this.read(22);
    }
    
    public void setDigitalOutputByte(final byte input) {
        this.write(22, input);
    }
    
    private int a(final int n, final DigitalChannelController.Mode mode) {
        if (mode == DigitalChannelController.Mode.OUTPUT) {
            return ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n];
        }
        return ~ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n];
    }
    
    private DigitalChannelController.Mode a(final int n, final int n2) {
        if ((ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n] & n2) > 0) {
            return DigitalChannelController.Mode.OUTPUT;
        }
        return DigitalChannelController.Mode.INPUT;
    }
    
    public boolean getLEDState(final int channel) {
        this.a(channel);
        return (this.read(23) & ModernRoboticsUsbDeviceInterfaceModule.LED_BIT_MASK_MAP[channel]) > 0;
    }
    
    public void setLED(final int channel, final boolean set) {
        this.a(channel);
        final byte fromWriteCache = this.readFromWriteCache(23);
        int data;
        if (set) {
            data = (fromWriteCache | ModernRoboticsUsbDeviceInterfaceModule.LED_BIT_MASK_MAP[channel]);
        }
        else {
            data = (fromWriteCache & ~ModernRoboticsUsbDeviceInterfaceModule.LED_BIT_MASK_MAP[channel]);
        }
        this.write(23, data);
    }
    
    public void setAnalogOutputVoltage(final int port, final int voltage) {
        this.b(port);
        final Lock writeLock = this.g[port].getWriteLock();
        final byte[] writeBuffer = this.g[port].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)voltage, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 0, shortToByteArray.length);
        }
        finally {
            writeLock.unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.a[port]);
    }
    
    public void setAnalogOutputFrequency(final int port, final int freq) {
        this.b(port);
        final Lock writeLock = this.g[port].getWriteLock();
        final byte[] writeBuffer = this.g[port].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)freq, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 2, shortToByteArray.length);
        }
        finally {
            writeLock.unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.a[port]);
    }
    
    public void setAnalogOutputMode(final int port, final byte mode) {
        this.b(port);
        final Lock writeLock = this.g[port].getWriteLock();
        final byte[] writeBuffer = this.g[port].getWriteBuffer();
        try {
            writeLock.lock();
            writeBuffer[4] = mode;
        }
        finally {
            writeLock.unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.a[port]);
    }
    
    public void setPulseWidthOutputTime(final int port, final int time) {
        this.c(port);
        final Lock writeLock = this.h[port].getWriteLock();
        final byte[] writeBuffer = this.h[port].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)time, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 0, shortToByteArray.length);
        }
        finally {
            writeLock.unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.b[port]);
    }
    
    public void setPulseWidthPeriod(final int port, final int period) {
        this.e(port);
        final Lock writeLock = this.h[port].getWriteLock();
        final byte[] writeBuffer = this.h[port].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)period, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 2, shortToByteArray.length);
        }
        finally {
            writeLock.unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.b[port]);
    }
    
    public int getPulseWidthOutputTime(final int port) {
        throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
    }
    
    public int getPulseWidthPeriod(final int port) {
        throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
    }
    
    public void enableI2cReadMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length) {
        this.e(physicalPort);
        this.f(length);
        try {
            this.i[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.i[physicalPort].getWriteBuffer();
            writeBuffer[0] = -128;
            writeBuffer[1] = (byte)i2cAddress;
            writeBuffer[2] = (byte)memAddress;
            writeBuffer[3] = (byte)length;
        }
        finally {
            this.i[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void enableI2cWriteMode(final int physicalPort, final int i2cAddress, final int memAddress, final int length) {
        this.e(physicalPort);
        this.f(length);
        try {
            this.i[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.i[physicalPort].getWriteBuffer();
            writeBuffer[0] = 0;
            writeBuffer[1] = (byte)i2cAddress;
            writeBuffer[2] = (byte)memAddress;
            writeBuffer[3] = (byte)length;
        }
        finally {
            this.i[physicalPort].getWriteLock().unlock();
        }
    }
    
    public byte[] getCopyOfReadBuffer(final int physicalPort) {
        this.e(physicalPort);
        Object o = null;
        try {
            this.i[physicalPort].getReadLock().lock();
            final byte[] readBuffer = this.i[physicalPort].getReadBuffer();
            o = new byte[readBuffer[3]];
            System.arraycopy(readBuffer, 4, o, 0, ((byte[])o).length);
        }
        finally {
            this.i[physicalPort].getReadLock().unlock();
        }
        return (byte[])o;
    }
    
    public byte[] getCopyOfWriteBuffer(final int physicalPort) {
        this.e(physicalPort);
        Object o = null;
        try {
            this.i[physicalPort].getWriteLock().lock();
            final byte[] writeBuffer = this.i[physicalPort].getWriteBuffer();
            o = new byte[writeBuffer[3]];
            System.arraycopy(writeBuffer, 4, o, 0, ((byte[])o).length);
        }
        finally {
            this.i[physicalPort].getWriteLock().unlock();
        }
        return (byte[])o;
    }
    
    public void copyBufferIntoWriteBuffer(final int physicalPort, final byte[] buffer) {
        this.e(physicalPort);
        this.f(buffer.length);
        try {
            this.i[physicalPort].getWriteLock().lock();
            System.arraycopy(buffer, 0, this.i[physicalPort].getWriteBuffer(), 4, buffer.length);
        }
        finally {
            this.i[physicalPort].getWriteLock().unlock();
        }
    }
    
    public void setI2cPortActionFlag(final int port) {
        this.e(port);
        try {
            this.i[port].getWriteLock().lock();
            this.i[port].getWriteBuffer()[31] = -1;
        }
        finally {
            this.i[port].getWriteLock().unlock();
        }
    }
    
    public boolean isI2cPortActionFlagSet(final int port) {
        this.e(port);
        boolean b = false;
        try {
            this.i[port].getReadLock().lock();
            b = (this.i[port].getReadBuffer()[31] == -1);
        }
        finally {
            this.i[port].getReadLock().unlock();
        }
        return b;
    }
    
    public void readI2cCacheFromController(final int port) {
        this.e(port);
        this.readWriteRunnable.queueSegmentRead(ModernRoboticsUsbDeviceInterfaceModule.c[port]);
    }
    
    public void writeI2cCacheToController(final int port) {
        this.e(port);
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.c[port]);
    }
    
    public void writeI2cPortFlagOnlyToController(final int port) {
        this.e(port);
        final ReadWriteRunnableSegment readWriteRunnableSegment = this.i[port];
        final ReadWriteRunnableSegment readWriteRunnableSegment2 = this.j[port];
        try {
            readWriteRunnableSegment.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteBuffer()[0] = readWriteRunnableSegment.getWriteBuffer()[31];
        }
        finally {
            readWriteRunnableSegment.getWriteLock().unlock();
            readWriteRunnableSegment2.getWriteLock().unlock();
        }
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.d[port]);
    }
    
    public boolean isI2cPortInReadMode(final int port) {
        this.e(port);
        boolean b = false;
        try {
            this.i[port].getReadLock().lock();
            b = (this.i[port].getReadBuffer()[0] == -128);
        }
        finally {
            this.i[port].getReadLock().unlock();
        }
        return b;
    }
    
    public boolean isI2cPortInWriteMode(final int port) {
        this.e(port);
        boolean b = false;
        try {
            this.i[port].getReadLock().lock();
            b = (this.i[port].getReadBuffer()[0] == 0);
        }
        finally {
            this.i[port].getReadLock().unlock();
        }
        return b;
    }
    
    public boolean isI2cPortReady(final int port) {
        return this.a(port, this.read(3));
    }
    
    public Lock getI2cReadCacheLock(final int port) {
        return this.i[port].getReadLock();
    }
    
    public Lock getI2cWriteCacheLock(final int port) {
        return this.i[port].getWriteLock();
    }
    
    public byte[] getI2cReadCache(final int port) {
        return this.i[port].getReadBuffer();
    }
    
    public byte[] getI2cWriteCache(final int port) {
        return this.i[port].getWriteBuffer();
    }
    
    public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback callback, final int port) {
        this.e[port] = callback;
    }
    
    public void deregisterForPortReadyCallback(final int port) {
        this.e[port] = null;
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
    
    private void a(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", n));
        }
    }
    
    private void b(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", n));
        }
    }
    
    private void c(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", n));
        }
    }
    
    private void d(final int n) {
        if (n < 0 || n > 7) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", n, 0, 7));
        }
    }
    
    private void e(final int n) {
        if (n < 0 || n > 5) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", n, 0, 5));
        }
    }
    
    private void f(final int n) {
        if (n > 27) {
            throw new IllegalArgumentException(String.format("buffer is too large (%d byte), max size is %d bytes", n, 27));
        }
    }
    
    private boolean a(final int n, final byte b) {
        return (b & ModernRoboticsUsbDeviceInterfaceModule.BUFFER_FLAG_MAP[n]) == 0x0;
    }
    
    @Override
    public void readComplete() throws InterruptedException {
        if (this.e == null) {
            return;
        }
        final byte read = this.read(3);
        for (int i = 0; i < 6; ++i) {
            if (this.e[i] != null && this.a(i, read)) {
                this.e[i].portIsReady(i);
            }
        }
    }
    
    static {
        LED_BIT_MASK_MAP = new int[] { 1, 2 };
        ADDRESS_DIGITAL_BIT_MASK = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
        ADDRESS_ANALOG_PORT_MAP = new int[] { 4, 6, 8, 10, 12, 14, 16, 18 };
        ADDRESS_VOLTAGE_OUTPUT_PORT_MAP = new int[] { 24, 30 };
        ADDRESS_PULSE_OUTPUT_PORT_MAP = new int[] { 36, 40 };
        ADDRESS_I2C_PORT_MAP = new int[] { 48, 80, 112, 144, 176, 208 };
        BUFFER_FLAG_MAP = new int[] { 1, 2, 4, 8, 16, 32 };
        a = new int[] { 0, 1 };
        b = new int[] { 2, 3 };
        c = new int[] { 4, 5, 6, 7, 8, 9 };
        d = new int[] { 10, 11, 12, 13, 14, 15 };
    }
}
