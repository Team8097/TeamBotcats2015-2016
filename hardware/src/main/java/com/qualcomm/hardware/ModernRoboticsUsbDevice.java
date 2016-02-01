// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.Executors;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import java.util.concurrent.ExecutorService;
import com.qualcomm.robotcore.util.SerialNumber;

public abstract class ModernRoboticsUsbDevice implements ReadWriteRunnable.Callback
{
    public static final int MFG_CODE_MODERN_ROBOTICS = 77;
    public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
    public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
    public static final int DEVICE_ID_LEGACY_MODULE = 73;
    public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
    protected SerialNumber serialNumber;
    protected ExecutorService readWriteService;
    protected ReadWriteRunnable readWriteRunnable;
    
    public ModernRoboticsUsbDevice(final SerialNumber serialNumber, final EventLoopManager manager, final ReadWriteRunnable readWriteRunnable) throws RobotCoreException, InterruptedException {
        this.readWriteService = Executors.newSingleThreadExecutor();
        this.serialNumber = serialNumber;
        this.readWriteRunnable = readWriteRunnable;
        RobotLog.v("Starting up device " + serialNumber.toString());
        this.readWriteService.execute(readWriteRunnable);
        readWriteRunnable.blockUntilReady();
        readWriteRunnable.setCallback(this);
        manager.registerSyncdDevice((SyncdDevice)readWriteRunnable);
    }
    
    public abstract String getDeviceName();
    
    public SerialNumber getSerialNumber() {
        return this.serialNumber;
    }
    
    public int getVersion() {
        return this.read(0);
    }
    
    public void close() {
        RobotLog.v("Shutting down device " + this.serialNumber.toString());
        this.readWriteService.shutdown();
        this.readWriteRunnable.close();
    }
    
    public void write(final int address, final byte data) {
        this.write(address, new byte[] { data });
    }
    
    public void write(final int address, final int data) {
        this.write(address, new byte[] { (byte)data });
    }
    
    public void write(final int address, final double data) {
        this.write(address, new byte[] { (byte)data });
    }
    
    public void write(final int address, final byte[] data) {
        this.readWriteRunnable.write(address, data);
    }
    
    public byte readFromWriteCache(final int address) {
        return this.readFromWriteCache(address, 1)[0];
    }
    
    public byte[] readFromWriteCache(final int address, final int size) {
        return this.readWriteRunnable.readFromWriteCache(address, size);
    }
    
    public byte read(final int address) {
        return this.read(address, 1)[0];
    }
    
    public byte[] read(final int address, final int size) {
        return this.readWriteRunnable.read(address, size);
    }
    
    @Override
    public void readComplete() throws InterruptedException {
    }
    
    @Override
    public void writeComplete() throws InterruptedException {
    }
}
