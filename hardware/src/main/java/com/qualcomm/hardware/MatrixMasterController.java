// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.nio.ByteBuffer;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.Iterator;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.qualcomm.robotcore.hardware.I2cController;

public class MatrixMasterController implements I2cController.I2cPortReadyCallback {
    private static final byte[] a;
    private static final byte[] b;
    private static final byte[] c;
    private static final byte[] d;
    private static final byte[] e;
    protected ConcurrentLinkedQueue<MatrixI2cTransaction> transactionQueue;
    protected ModernRoboticsUsbLegacyModule legacyModule;
    protected MatrixDcMotorController motorController;
    protected MatrixServoController servoController;
    protected int physicalPort;
    private volatile boolean f;
    private final ElapsedTime g;

    public MatrixMasterController(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        this.f = false;
        this.g = new ElapsedTime(0L);
        this.legacyModule = legacyModule;
        this.physicalPort = physicalPort;
        this.transactionQueue = new ConcurrentLinkedQueue<MatrixI2cTransaction>();
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback) this, physicalPort);
    }

    public void registerMotorController(final MatrixDcMotorController mc) {
        this.motorController = mc;
    }

    public void registerServoController(final MatrixServoController sc) {
        this.servoController = sc;
    }

    public int getPort() {
        return this.physicalPort;
    }

    public String getConnectionInfo() {
        return this.legacyModule.getConnectionInfo() + "; port " + this.physicalPort;
    }

    public boolean queueTransaction(final MatrixI2cTransaction transaction, final boolean force) {
        if (!force) {
            final Iterator<MatrixI2cTransaction> iterator = this.transactionQueue.iterator();
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

    public boolean queueTransaction(final MatrixI2cTransaction transaction) {
        return this.queueTransaction(transaction, false);
    }

    public void waitOnRead() {
        synchronized (this) {
            this.f = true;
            try {
                while (this.f) {
                    this.wait(0L);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void handleReadDone(final MatrixI2cTransaction transaction) {
        final byte[] i2cReadCache = this.legacyModule.getI2cReadCache(this.physicalPort);
        switch (transaction.property) {
            case d: {
                this.motorController.handleReadBattery(i2cReadCache);
                break;
            }
            case e: {
                this.motorController.handleReadPosition(transaction, i2cReadCache);
                break;
            }
            case b: {
                this.motorController.handleReadPosition(transaction, i2cReadCache);
                break;
            }
            case a: {
                this.motorController.handleReadMode(transaction, i2cReadCache);
                break;
            }
            case g: {
                this.servoController.handleReadServo(transaction, i2cReadCache);
                break;
            }
            default: {
                RobotLog.e((String) ("Transaction not a read " + (Object) ((Object) transaction.property)));
                break;
            }
        }
        synchronized (this) {
            if (this.f) {
                this.f = false;
                this.notify();
            }
        }
    }

    protected void sendHeartbeat() {
        this.queueTransaction(new MatrixI2cTransaction((byte) 0, MatrixI2cTransaction.a.j, 3));
    }

    public void portIsReady(final int port) {
        if (this.transactionQueue.isEmpty()) {
            if (this.g.time() > 2.0) {
                this.sendHeartbeat();
                this.g.reset();
            }
            return;
        }
        MatrixI2cTransaction transaction = this.transactionQueue.peek();
        if (transaction.state == MatrixI2cTransaction.b.b) {
            this.legacyModule.readI2cCacheFromModule(this.physicalPort);
            transaction.state = MatrixI2cTransaction.b.d;
            return;
        }
        if (transaction.state == MatrixI2cTransaction.b.c) {
            final MatrixI2cTransaction matrixI2cTransaction = this.transactionQueue.poll();
            if (this.transactionQueue.isEmpty()) {
                return;
            }
            transaction = this.transactionQueue.peek();
        } else if (transaction.state == MatrixI2cTransaction.b.d) {
            this.handleReadDone(transaction);
            final MatrixI2cTransaction matrixI2cTransaction2 = this.transactionQueue.poll();
            if (this.transactionQueue.isEmpty()) {
                return;
            }
            transaction = this.transactionQueue.peek();
        }
        int n = 0;
        int n2 = 0;
        byte[] data = null;
        switch (transaction.property) {
            case e: {
                n = b[transaction.motor];
                n2 = 4;
                data = new byte[]{0};
                break;
            }
            case d: {
                n = 67;
                data = new byte[]{0};
                n2 = 1;
                break;
            }
            case j: {
                n = 66;
                data = new byte[]{(byte) transaction.value};
                n2 = 1;
                break;
            }
            case i: {
                n = 68;
                data = new byte[]{(byte) transaction.value};
                n2 = 1;
                break;
            }
            case c: {
                n = d[transaction.motor];
                data = new byte[]{(byte) transaction.value};
                n2 = 1;
                break;
            }
            case b: {
                n = c[transaction.motor];
                data = TypeConversion.intToByteArray((int) transaction.value);
                n2 = 4;
                break;
            }
            case a: {
                n = e[transaction.motor];
                data = new byte[]{(byte) transaction.value};
                n2 = 1;
                break;
            }
            case f: {
                n = b[transaction.motor];
                ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                byteBuffer.put(TypeConversion.intToByteArray((int) 0));
                byteBuffer.put(TypeConversion.intToByteArray((int) transaction.target));
                byteBuffer.put(transaction.speed);
                byteBuffer.put(transaction.mode);
                data = byteBuffer.array();
                n2 = 10;
                break;
            }
            case g: {
                n = a[transaction.servo];
                data = new byte[]{transaction.speed, (byte) transaction.target};
                n2 = 2;
                break;
            }
            case h: {
                n = 69;
                data = new byte[]{(byte) transaction.value};
                n2 = 1;
                break;
            }
            default: {
                n = 0;
                data = new byte[]{(byte) transaction.value};
                n2 = 1;
                break;
            }
        }
        try {
            if (transaction.write) {
                this.legacyModule.setWriteMode(this.physicalPort, 16, n);
                this.legacyModule.setData(this.physicalPort, data, n2);
                transaction.state = MatrixI2cTransaction.b.c;
            } else {
                this.legacyModule.setReadMode(this.physicalPort, 16, n, n2);
                transaction.state = MatrixI2cTransaction.b.b;
            }
            this.legacyModule.setI2cPortActionFlag(this.physicalPort);
            this.legacyModule.writeI2cCacheToModule(this.physicalPort);
        } catch (IllegalArgumentException ex) {
            RobotLog.e(ex.getMessage());
        }
        this.buginf(transaction.toString());
    }

    protected void buginf(final String s) {
    }

    static {
        a = new byte[]{0, 70, 72, 74, 76};
        b = new byte[]{0, 78, 88, 98, 108};
        c = new byte[]{0, 82, 92, 102, 112};
        d = new byte[]{0, 86, 96, 106, 116};
        e = new byte[]{0, 87, 97, 107, 117};
    }
}
