// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.Arrays;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.modernrobotics.ReadWriteRunnableUsbHandler;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;

public class ReadWriteRunnableStandard implements ReadWriteRunnable
{
    protected final byte[] localDeviceReadCache;
    protected final byte[] localDeviceWriteCache;
    protected Map<Integer, ReadWriteRunnableSegment> segments;
    protected ConcurrentLinkedQueue<Integer> segmentReadQueue;
    protected ConcurrentLinkedQueue<Integer> segmentWriteQueue;
    protected final SerialNumber serialNumber;
    protected final ReadWriteRunnableUsbHandler usbHandler;
    protected int startAddress;
    protected int monitorLength;
    protected volatile boolean running;
    protected volatile boolean shutdownComplete;
    private volatile boolean a;
    protected Callback callback;
    protected final boolean DEBUG_LOGGING;
    
    public ReadWriteRunnableStandard(final SerialNumber serialNumber, final RobotUsbDevice device, final int monitorLength, final int startAddress, final boolean debug) {
        this.localDeviceReadCache = new byte[256];
        this.localDeviceWriteCache = new byte[256];
        this.segments = new HashMap<Integer, ReadWriteRunnableSegment>();
        this.segmentReadQueue = new ConcurrentLinkedQueue<Integer>();
        this.segmentWriteQueue = new ConcurrentLinkedQueue<Integer>();
        this.running = false;
        this.shutdownComplete = false;
        this.a = false;
        this.serialNumber = serialNumber;
        this.startAddress = startAddress;
        this.monitorLength = monitorLength;
        this.DEBUG_LOGGING = debug;
        this.callback = new EmptyCallback();
        this.usbHandler = new ReadWriteRunnableUsbHandler(device);
    }
    
    @Override
    public void setCallback(final Callback callback) {
        this.callback = callback;
    }
    
    @Override
    public void blockUntilReady() throws RobotCoreException, InterruptedException {
        if (this.shutdownComplete) {
            RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
            RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
            throw new RobotCoreException("cannot block, device is shut down");
        }
    }
    
    public void startBlockingWork() {
    }
    
    public boolean writeNeeded() {
        return this.a;
    }
    
    public void setWriteNeeded(final boolean set) {
        this.a = set;
    }
    
    @Override
    public void write(final int address, final byte[] data) {
        synchronized (this.localDeviceWriteCache) {
            System.arraycopy(data, 0, this.localDeviceWriteCache, address, data.length);
            this.a = true;
        }
    }
    
    @Override
    public byte[] readFromWriteCache(final int address, final int size) {
        synchronized (this.localDeviceWriteCache) {
            return Arrays.copyOfRange(this.localDeviceWriteCache, address, address + size);
        }
    }
    
    @Override
    public byte[] read(final int address, final int size) {
        synchronized (this.localDeviceReadCache) {
            return Arrays.copyOfRange(this.localDeviceReadCache, address, address + size);
        }
    }
    
    @Override
    public void close() {
        try {
            this.blockUntilReady();
            this.startBlockingWork();
            this.running = false;
            while (!this.shutdownComplete) {
                Thread.yield();
            }
        }
        catch (InterruptedException ex) {
            RobotLog.w("Exception while closing USB device: " + ex.getMessage());
        }
        catch (RobotCoreException ex2) {
            RobotLog.w("Exception while closing USB device: " + ex2.getMessage());
        }
        finally {
            this.running = false;
            while (!this.shutdownComplete) {
                Thread.yield();
            }
        }
    }
    
    @Override
    public ReadWriteRunnableSegment createSegment(final int key, final int address, final int size) {
        final ReadWriteRunnableSegment readWriteRunnableSegment = new ReadWriteRunnableSegment(address, size);
        this.segments.put(key, readWriteRunnableSegment);
        return readWriteRunnableSegment;
    }
    
    public void destroySegment(final int key) {
        this.segments.remove(key);
    }
    
    public ReadWriteRunnableSegment getSegment(final int key) {
        return this.segments.get(key);
    }
    
    @Override
    public void queueSegmentRead(final int key) {
        this.queueIfNotAlreadyQueued(key, this.segmentReadQueue);
    }
    
    @Override
    public void queueSegmentWrite(final int key) {
        this.queueIfNotAlreadyQueued(key, this.segmentWriteQueue);
    }
    
    public void run() {
        int n = 1;
        int startAddress = 0;
        byte[] array = new byte[this.monitorLength + this.startAddress];
        final ElapsedTime elapsedTime = new ElapsedTime();
        final String string = "Device " + this.serialNumber.toString();
        this.running = true;
        RobotLog.v(String.format("starting read/write loop for device %s", this.serialNumber));
        try {
            this.usbHandler.purge(RobotUsbDevice.Channel.RX);
            while (this.running) {
                if (this.DEBUG_LOGGING) {
                    elapsedTime.log(string);
                    elapsedTime.reset();
                }
                try {
                    this.usbHandler.read(startAddress, array);
                    while (!this.segmentReadQueue.isEmpty()) {
                        final ReadWriteRunnableSegment readWriteRunnableSegment = this.segments.get(this.segmentReadQueue.remove());
                        final byte[] copy = new byte[readWriteRunnableSegment.getReadBuffer().length];
                        this.usbHandler.read(readWriteRunnableSegment.getAddress(), copy);
                        try {
                            readWriteRunnableSegment.getReadLock().lock();
                            System.arraycopy(copy, 0, readWriteRunnableSegment.getReadBuffer(), 0, readWriteRunnableSegment.getReadBuffer().length);
                        }
                        finally {
                            readWriteRunnableSegment.getReadLock().unlock();
                        }
                    }
                }
                catch (RobotCoreException ex) {
                    RobotLog.w(String.format("could not read from device %s: %s", this.serialNumber, ex.getMessage()));
                }
                synchronized (this.localDeviceReadCache) {
                    System.arraycopy(array, 0, this.localDeviceReadCache, startAddress, array.length);
                }
                if (this.DEBUG_LOGGING) {
                    this.dumpBuffers("read", this.localDeviceReadCache);
                }
                this.callback.readComplete();
                this.waitForSyncdEvents();
                if (n != 0) {
                    startAddress = this.startAddress;
                    array = new byte[this.monitorLength];
                    n = 0;
                }
                synchronized (this.localDeviceWriteCache) {
                    System.arraycopy(this.localDeviceWriteCache, startAddress, array, 0, array.length);
                }
                try {
                    if (this.writeNeeded()) {
                        this.usbHandler.write(startAddress, array);
                        this.setWriteNeeded(false);
                    }
                    while (!this.segmentWriteQueue.isEmpty()) {
                        final ReadWriteRunnableSegment readWriteRunnableSegment2 = this.segments.get(this.segmentWriteQueue.remove());
                        byte[] copy;
                        try {
                            readWriteRunnableSegment2.getWriteLock().lock();
                            copy = Arrays.copyOf(readWriteRunnableSegment2.getWriteBuffer(), readWriteRunnableSegment2.getWriteBuffer().length);
                        }
                        finally {
                            readWriteRunnableSegment2.getWriteLock().unlock();
                        }
                        this.usbHandler.write(readWriteRunnableSegment2.getAddress(), copy);
                    }
                }
                catch (RobotCoreException ex2) {
                    RobotLog.w(String.format("could not write to device %s: %s", this.serialNumber, ex2.getMessage()));
                }
                if (this.DEBUG_LOGGING) {
                    this.dumpBuffers("write", this.localDeviceWriteCache);
                }
                this.callback.writeComplete();
                this.usbHandler.throwIfUsbErrorCountIsTooHigh();
            }
        }
        catch (NullPointerException ex3) {
            RobotLog.w(String.format("could not write to device %s: FTDI Null Pointer Exception", this.serialNumber));
            RobotLog.logStacktrace((Exception)ex3);
            RobotLog.setGlobalErrorMsg("There was a problem communicating with a Modern Robotics USB device");
        }
        catch (InterruptedException ex5) {
            RobotLog.w(String.format("could not write to device %s: Interrupted Exception", this.serialNumber));
        }
        catch (RobotCoreException ex4) {
            RobotLog.w(ex4.getMessage());
            RobotLog.setGlobalErrorMsg(String.format("There was a problem communicating with a Modern Robotics USB device %s", this.serialNumber));
        }
        finally {
            this.usbHandler.close();
            this.running = false;
            this.shutdownComplete = true;
        }
        RobotLog.v(String.format("stopped read/write loop for device %s", this.serialNumber));
    }
    
    protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
    }
    
    protected void dumpBuffers(final String name, final byte[] byteArray) {
        RobotLog.v("Dumping " + name + " buffers for " + this.serialNumber);
        final StringBuilder sb = new StringBuilder(1024);
        for (int i = 0; i < this.startAddress + this.monitorLength; ++i) {
            sb.append(String.format(" %02x", TypeConversion.unsignedByteToInt(byteArray[i])));
            if ((i + 1) % 16 == 0) {
                sb.append("\n");
            }
        }
        RobotLog.v(sb.toString());
    }
    
    protected void queueIfNotAlreadyQueued(final int key, final ConcurrentLinkedQueue<Integer> queue) {
        if (!queue.contains(key)) {
            queue.add(key);
        }
    }
}
