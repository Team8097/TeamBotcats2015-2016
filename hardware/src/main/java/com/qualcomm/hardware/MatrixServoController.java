// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Range;
import java.util.Arrays;
import com.qualcomm.robotcore.hardware.ServoController;

public class MatrixServoController implements ServoController
{
    public static final int SERVO_POSITION_MAX = 240;
    private MatrixMasterController a;
    protected ServoController.PwmStatus pwmStatus;
    protected double[] servoCache;
    
    public MatrixServoController(final MatrixMasterController master) {
        this.servoCache = new double[4];
        this.a = master;
        this.pwmStatus = ServoController.PwmStatus.DISABLED;
        Arrays.fill(this.servoCache, 0.0);
        master.registerServoController(this);
    }
    
    public void pwmEnable() {
        this.a.queueTransaction(new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.h, 15));
        this.pwmStatus = ServoController.PwmStatus.ENABLED;
    }
    
    public void pwmDisable() {
        this.a.queueTransaction(new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.h, 0));
        this.pwmStatus = ServoController.PwmStatus.DISABLED;
    }
    
    public ServoController.PwmStatus getPwmStatus() {
        return this.pwmStatus;
    }
    
    public void setServoPosition(final int channel, final double position) {
        this.a(channel);
        Range.throwIfRangeIsInvalid(position, 0.0, 1.0);
        this.a.queueTransaction(new MatrixI2cTransaction((byte)channel, (byte)(position * 240.0), (byte)0));
    }
    
    public void setServoPosition(final int channel, final double position, final byte speed) {
        this.a(channel);
        Range.throwIfRangeIsInvalid(position, 0.0, 1.0);
        this.a.queueTransaction(new MatrixI2cTransaction((byte)channel, (byte)(position * 240.0), speed));
    }
    
    public double getServoPosition(final int channel) {
        if (this.a.queueTransaction(new MatrixI2cTransaction((byte)channel, MatrixI2cTransaction.a.g))) {
            this.a.waitOnRead();
        }
        return this.servoCache[channel] / 240.0;
    }
    
    public String getDeviceName() {
        return "Matrix Servo Controller";
    }
    
    public String getConnectionInfo() {
        return this.a.getConnectionInfo();
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
        this.pwmDisable();
    }
    
    private void a(final int n) {
        if (n < 1 || n > 4) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, (byte)4));
        }
    }
    
    public void handleReadServo(final MatrixI2cTransaction transaction, final byte[] buffer) {
        this.servoCache[transaction.servo] = TypeConversion.unsignedByteToInt(buffer[4]);
    }
}
