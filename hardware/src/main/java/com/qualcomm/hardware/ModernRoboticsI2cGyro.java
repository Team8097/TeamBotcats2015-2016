// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.util.Arrays;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class ModernRoboticsI2cGyro extends GyroSensor implements HardwareDevice, I2cController.I2cPortReadyCallback
{
    public static final int ADDRESS_I2C = 32;
    protected static final int OFFSET_FIRMWARE_REV = 0;
    protected static final int OFFSET_MANUFACTURE_CODE = 1;
    protected static final int OFFSET_SENSOR_ID = 2;
    protected static final int OFFSET_COMMAND = 3;
    protected static final int OFFSET_HEADING_DATA = 4;
    protected static final int OFFSET_INTEGRATED_Z_VAL = 6;
    protected static final int OFFSET_RAW_X_VAL = 8;
    protected static final int OFFSET_RAW_Y_VAL = 10;
    protected static final int OFFSET_RAW_Z_VAL = 12;
    protected static final int OFFSET_Z_AXIS_OFFSET = 14;
    protected static final int OFFSET_Z_AXIS_SCALE_COEF = 16;
    protected static final int OFFSET_NEW_I2C_ADDRESS = 112;
    protected static final int OFFSET_TRIGGER_1 = 113;
    protected static final int OFFSET_TRIGGER_2 = 114;
    protected static final int TRIGGER_1_VAL = 85;
    protected static final int TRIGGER_2_VAL = 170;
    protected static final byte COMMAND_NORMAL = 0;
    protected static final byte COMMAND_NULL = 78;
    protected static final byte COMMAND_RESET_Z_AXIS = 82;
    protected static final byte COMMAND_WRITE_EEPROM = 87;
    protected static final int BUFFER_LENGTH = 18;
    protected ConcurrentLinkedQueue<GyroI2cTransaction> transactionQueue;
    private int a;
    private final DeviceInterfaceModule b;
    private final byte[] c;
    private final Lock d;
    private final byte[] e;
    private final Lock f;
    private final int g;
    private HeadingMode h;
    private MeasurementMode i;
    private a j;
    
    public ModernRoboticsI2cGyro(final DeviceInterfaceModule deviceInterfaceModule, final int physicalPort) {
        this.a = 32;
        this.b = deviceInterfaceModule;
        this.g = physicalPort;
        this.c = deviceInterfaceModule.getI2cReadCache(physicalPort);
        this.d = deviceInterfaceModule.getI2cReadCacheLock(physicalPort);
        this.e = deviceInterfaceModule.getI2cWriteCache(physicalPort);
        this.f = deviceInterfaceModule.getI2cWriteCacheLock(physicalPort);
        this.h = HeadingMode.HEADING_CARDINAL;
        deviceInterfaceModule.enableI2cReadMode(physicalPort, 32, 0, 18);
        deviceInterfaceModule.setI2cPortActionFlag(physicalPort);
        deviceInterfaceModule.writeI2cCacheToController(physicalPort);
        deviceInterfaceModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
        this.transactionQueue = new ConcurrentLinkedQueue<GyroI2cTransaction>();
        this.j = new a();
        this.i = MeasurementMode.GYRO_NORMAL;
    }
    
    public boolean queueTransaction(final GyroI2cTransaction transaction, final boolean force) {
        if (!force) {
            final Iterator<GyroI2cTransaction> iterator = this.transactionQueue.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isEqual(transaction)) {
                    this.buginf("NO Queue transaction " + transaction.toString());
                    return false;
                }
            }
        }
        this.buginf("YES Queue transaction " + transaction.toString());
        this.transactionQueue.add(transaction);
        return true;
    }
    
    public boolean queueTransaction(final GyroI2cTransaction transaction) {
        return this.queueTransaction(transaction, false);
    }
    
    public void calibrate() {
        this.queueTransaction(new GyroI2cTransaction((byte)78));
    }
    
    public boolean isCalibrating() {
        return this.i != MeasurementMode.GYRO_NORMAL;
    }
    
    public HeadingMode getHeadingMode() {
        return this.h;
    }
    
    public void setHeadingMode(final HeadingMode headingMode) {
        this.h = headingMode;
    }
    
    public MeasurementMode getMeasurementMode() {
        return this.i;
    }
    
    public int getHeading() {
        if (this.h != HeadingMode.HEADING_CARDINAL) {
            return this.j.e;
        }
        if (this.j.e == 0) {
            return this.j.e;
        }
        return Math.abs(this.j.e - 360);
    }
    
    public double getRotation() {
        this.notSupported();
        return 0.0;
    }
    
    public int getIntegratedZValue() {
        return this.j.f;
    }
    
    public int rawX() {
        return this.j.g;
    }
    
    public int rawY() {
        return this.j.h;
    }
    
    public int rawZ() {
        return this.j.i;
    }
    
    public void resetZAxisIntegrator() {
        this.queueTransaction(new GyroI2cTransaction((byte)82));
    }
    
    public String getDeviceName() {
        return "Modern Robotics Gyro";
    }
    
    public String getConnectionInfo() {
        return this.b.getConnectionInfo() + "; I2C port: " + this.g;
    }
    
    public String status() {
        return String.format("Modern Robotics Gyro, connected via device %s, port %d", this.b.getSerialNumber().toString(), this.g);
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
    
    private void a() {
        try {
            this.d.lock();
            final ByteBuffer wrap = ByteBuffer.wrap(this.c);
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            this.j.a = this.c[4];
            this.j.b = this.c[5];
            this.j.c = this.c[6];
            this.j.d = this.c[7];
            this.j.e = wrap.getShort(8);
            this.j.f = wrap.getShort(10);
            this.j.g = wrap.getShort(12);
            this.j.h = wrap.getShort(14);
            this.j.i = wrap.getShort(16);
            this.j.j = wrap.getShort(18);
            this.j.k = wrap.getShort(20);
        }
        finally {
            this.d.unlock();
        }
    }
    
    private void b() {
        this.queueTransaction(new GyroI2cTransaction());
    }
    
    public void portIsReady(final int port) {
        if (this.transactionQueue.isEmpty()) {
            this.b();
            return;
        }
        GyroI2cTransaction gyroI2cTransaction = this.transactionQueue.peek();
        if (gyroI2cTransaction.a == I2cTransactionState.PENDING_I2C_READ) {
            this.b.readI2cCacheFromModule(this.g);
            gyroI2cTransaction.a = I2cTransactionState.PENDING_READ_DONE;
            return;
        }
        if (gyroI2cTransaction.a == I2cTransactionState.PENDING_I2C_WRITE) {
            final GyroI2cTransaction gyroI2cTransaction2 = this.transactionQueue.poll();
            if (this.transactionQueue.isEmpty()) {
                return;
            }
            gyroI2cTransaction = this.transactionQueue.peek();
        }
        else if (gyroI2cTransaction.a == I2cTransactionState.PENDING_READ_DONE) {
            this.a();
            final GyroI2cTransaction gyroI2cTransaction3 = this.transactionQueue.poll();
            if (this.transactionQueue.isEmpty()) {
                return;
            }
            gyroI2cTransaction = this.transactionQueue.peek();
        }
        try {
            if (gyroI2cTransaction.e) {
                if (gyroI2cTransaction.c == 3) {
                    this.j.d = gyroI2cTransaction.b[0];
                    this.i = MeasurementMode.GYRO_CALIBRATING;
                }
                this.b.enableI2cWriteMode(port, this.a, (int)gyroI2cTransaction.c, (int)gyroI2cTransaction.d);
                this.b.copyBufferIntoWriteBuffer(port, gyroI2cTransaction.b);
                gyroI2cTransaction.a = I2cTransactionState.PENDING_I2C_WRITE;
            }
            else {
                this.b.enableI2cReadMode(port, this.a, (int)gyroI2cTransaction.c, (int)gyroI2cTransaction.d);
                gyroI2cTransaction.a = I2cTransactionState.PENDING_I2C_READ;
            }
            this.b.writeI2cCacheToController(port);
        }
        catch (IllegalArgumentException ex) {
            RobotLog.e(ex.getMessage());
        }
        if (this.j.d == 0) {
            this.i = MeasurementMode.GYRO_NORMAL;
        }
    }
    
    protected void buginf(final String s) {
    }
    
    protected enum I2cTransactionState
    {
        QUEUED, 
        PENDING_I2C_READ, 
        PENDING_I2C_WRITE, 
        PENDING_READ_DONE, 
        DONE;
    }
    
    public class GyroI2cTransaction
    {
        I2cTransactionState a;
        byte[] b;
        byte c;
        byte d;
        boolean e;
        
        public GyroI2cTransaction() {
            this.c = 0;
            this.d = 18;
            this.e = false;
        }
        
        public GyroI2cTransaction(final byte data) {
            this.c = 3;
            (this.b = new byte[1])[0] = data;
            this.d = (byte)this.b.length;
            this.e = true;
        }
        
        public boolean isEqual(final GyroI2cTransaction transaction) {
            if (this.c != transaction.c) {
                return false;
            }
            switch (this.c) {
                case 3:
                case 16: {
                    return Arrays.equals(this.b, transaction.b);
                }
                default: {
                    return false;
                }
            }
        }
    }
    
    public enum HeadingMode
    {
        HEADING_CARTESIAN, 
        HEADING_CARDINAL;
    }
    
    public enum MeasurementMode
    {
        GYRO_CALIBRATING, 
        GYRO_NORMAL;
    }
    
    private class a
    {
        byte a;
        byte b;
        byte c;
        byte d;
        short e;
        short f;
        short g;
        short h;
        short i;
        short j;
        short k;
    }
}
