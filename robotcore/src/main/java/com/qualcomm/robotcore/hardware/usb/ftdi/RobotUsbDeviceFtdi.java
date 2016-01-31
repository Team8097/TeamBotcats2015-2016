// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class RobotUsbDeviceFtdi implements RobotUsbDevice {
    private FT_Device a;

    public RobotUsbDeviceFtdi(final FT_Device device) {
        this.a = device;
    }

    @Override
    public void setBaudRate(final int rate) throws RobotCoreException {
        if (!this.a.setBaudRate(rate)) {
            throw new RobotCoreException("FTDI driver failed to set baud rate to " + rate);
        }
    }

    @Override
    public void setDataCharacteristics(final byte dataBits, final byte stopBits, final byte parity) throws RobotCoreException {
        if (!this.a.setDataCharacteristics(dataBits, stopBits, parity)) {
            throw new RobotCoreException("FTDI driver failed to set data characteristics");
        }
    }

    @Override
    public void setLatencyTimer(final int latencyTimer) throws RobotCoreException {
        if (!this.a.setLatencyTimer((byte) latencyTimer)) {
            throw new RobotCoreException("FTDI driver failed to set latency timer to " + latencyTimer);
        }
    }

    @Override
    public void purge(final Channel channel) throws RobotCoreException {
        byte flags = 0;
        switch (channel) {
            case RX: {
                flags = 1;
                break;
            }
            case TX: {
                flags = 2;
                break;
            }
            case BOTH: {
                flags = 3;
                break;
            }
        }
        this.a.purge(flags);
    }

    @Override
    public void write(final byte[] data) throws RobotCoreException {
        this.a.write(data);
    }

    @Override
    public int read(final byte[] data) throws RobotCoreException {
        return this.a.read(data);
    }

    @Override
    public int read(final byte[] data, final int length, final int timeout) throws RobotCoreException {
        return this.a.read(data, length, timeout);
    }

    @Override
    public void close() {
        this.a.close();
    }
}
