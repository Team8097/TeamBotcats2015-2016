// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReadWriteRunnableBlocking extends ReadWriteRunnableStandard
{
    protected final Lock blockingLock;
    protected final Lock waitingLock;
    protected final Condition blockingCondition;
    protected final Condition waitingCondition;
    protected ReadWriteRunnable.BlockingState blockingState;
    private volatile boolean a;
    
    public ReadWriteRunnableBlocking(final SerialNumber serialNumber, final RobotUsbDevice device, final int monitorLength, final int startAddress, final boolean debug) {
        super(serialNumber, device, monitorLength, startAddress, debug);
        this.blockingLock = new ReentrantLock();
        this.waitingLock = new ReentrantLock();
        this.blockingCondition = this.blockingLock.newCondition();
        this.waitingCondition = this.waitingLock.newCondition();
        this.blockingState = ReadWriteRunnable.BlockingState.BLOCKING;
        this.a = false;
    }
    
    @Override
    public void blockUntilReady() throws RobotCoreException, InterruptedException {
        try {
            this.blockingLock.lock();
            while (this.blockingState == ReadWriteRunnable.BlockingState.BLOCKING) {
                this.blockingCondition.await(100L, TimeUnit.MILLISECONDS);
                if (this.shutdownComplete) {
                    RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
                    RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
                    throw new RobotCoreException("cannot block, device is shut down");
                }
            }
        }
        finally {
            this.blockingLock.unlock();
        }
    }
    
    @Override
    public void startBlockingWork() {
        try {
            this.waitingLock.lock();
            this.blockingState = ReadWriteRunnable.BlockingState.BLOCKING;
            this.waitingCondition.signalAll();
        }
        finally {
            this.waitingLock.unlock();
        }
    }
    
    @Override
    public void write(final int address, final byte[] data) {
        synchronized (this.localDeviceWriteCache) {
            System.arraycopy(data, 0, this.localDeviceWriteCache, address, data.length);
            this.a = true;
        }
    }
    
    @Override
    public boolean writeNeeded() {
        return this.a;
    }
    
    @Override
    public void setWriteNeeded(final boolean set) {
        this.a = set;
    }
    
    @Override
    protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
        try {
            this.blockingLock.lock();
            this.blockingState = ReadWriteRunnable.BlockingState.WAITING;
            this.blockingCondition.signalAll();
        }
        finally {
            this.blockingLock.unlock();
        }
        try {
            this.waitingLock.lock();
            while (this.blockingState == ReadWriteRunnable.BlockingState.WAITING) {
                this.waitingCondition.await();
                if (this.shutdownComplete) {
                    RobotLog.w("wait for sync'd events requested, but device is shut down - " + this.serialNumber);
                    throw new RobotCoreException("cannot block, device is shut down");
                }
            }
        }
        finally {
            this.waitingLock.unlock();
        }
    }
}
