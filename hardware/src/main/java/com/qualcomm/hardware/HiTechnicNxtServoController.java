// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.I2cController;

public class HiTechnicNxtServoController implements I2cController.I2cPortReadyCallback, ServoController
{
    public static final int I2C_ADDRESS = 2;
    public static final int MEM_START_ADDRESS = 66;
    public static final int MEM_READ_LENGTH = 7;
    public static final int MAX_SERVOS = 6;
    public static final int SERVO_POSITION_MAX = 255;
    public static final byte PWM_ENABLE = 0;
    public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
    public static final byte PWM_DISABLE = -1;
    public static final int OFFSET_SERVO1_POSITION = 4;
    public static final int OFFSET_SERVO2_POSITION = 5;
    public static final int OFFSET_SERVO3_POSITION = 6;
    public static final int OFFSET_SERVO4_POSITION = 7;
    public static final int OFFSET_SERVO5_POSITION = 8;
    public static final int OFFSET_SERVO6_POSITION = 9;
    public static final int OFFSET_PWM = 10;
    public static final int OFFSET_UNUSED = -1;
    public static final byte[] OFFSET_SERVO_MAP;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final int d;
    private ElapsedTime e;
    private volatile boolean f;
    
    public HiTechnicNxtServoController(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        this.e = new ElapsedTime(0L);
        this.f = true;
        this.a = legacyModule;
        this.d = physicalPort;
        this.b = legacyModule.getI2cWriteCache(physicalPort);
        this.c = legacyModule.getI2cWriteCacheLock(physicalPort);
        legacyModule.enableI2cWriteMode(physicalPort, 2, 66, 7);
        this.pwmDisable();
        legacyModule.setI2cPortActionFlag(physicalPort);
        legacyModule.writeI2cCacheToController(physicalPort);
        legacyModule.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, physicalPort);
    }
    
    public String getDeviceName() {
        return "NXT Servo Controller";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.d;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
        this.pwmDisable();
    }
    
    public void pwmEnable() {
        try {
            this.c.lock();
            if (0 != this.b[10]) {
                this.b[10] = 0;
                this.f = true;
            }
        }
        finally {
            this.c.unlock();
        }
    }
    
    public void pwmDisable() {
        try {
            this.c.lock();
            if (-1 != this.b[10]) {
                this.b[10] = -1;
                this.f = true;
            }
        }
        finally {
            this.c.unlock();
        }
    }
    
    public ServoController.PwmStatus getPwmStatus() {
        return ServoController.PwmStatus.DISABLED;
    }
    
    public void setServoPosition(final int channel, final double position) {
        this.a(channel);
        Range.throwIfRangeIsInvalid(position, 0.0, 1.0);
        final byte b = (byte)(position * 255.0);
        try {
            this.c.lock();
            if (b != this.b[HiTechnicNxtServoController.OFFSET_SERVO_MAP[channel]]) {
                this.f = true;
                this.b[HiTechnicNxtServoController.OFFSET_SERVO_MAP[channel]] = b;
                this.b[10] = 0;
            }
        }
        finally {
            this.c.unlock();
        }
    }
    
    public double getServoPosition(final int channel) {
        return 0.0;
    }
    
    private void a(final int n) {
        if (n < 1 || n > HiTechnicNxtServoController.OFFSET_SERVO_MAP.length) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, 6));
        }
    }
    
    public void portIsReady(final int port) {
        if (this.f || this.e.time() > 5.0) {
            this.a.setI2cPortActionFlag(this.d);
            this.a.writeI2cCacheToController(this.d);
            this.e.reset();
        }
        this.f = false;
    }
    
    static {
        OFFSET_SERVO_MAP = new byte[] { -1, 4, 5, 6, 7, 8, 9 };
    }
}
