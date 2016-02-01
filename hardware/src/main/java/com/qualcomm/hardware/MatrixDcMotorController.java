// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import java.util.Arrays;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Iterator;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.Set;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class MatrixDcMotorController implements DcMotorController
{
    private a[] a;
    public static final byte POWER_MAX = 100;
    public static final byte POWER_MIN = -100;
    protected MatrixMasterController master;
    protected DcMotorController.DeviceMode deviceMode;
    private int b;
    
    public MatrixDcMotorController(final MatrixMasterController master) {
        this.a = new a[] { new a(), new a(), new a(), new a(), new a() };
        this.master = master;
        this.b = 0;
        master.registerMotorController(this);
        for (byte motor = 0; motor < 4; ++motor) {
            master.queueTransaction(new MatrixI2cTransaction(motor, (byte)0, 0, (byte)0));
            this.a[motor].f = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
            this.a[motor].d = true;
        }
        this.deviceMode = DcMotorController.DeviceMode.READ_ONLY;
    }
    
    protected byte runModeToFlagMatrix(final DcMotorController.RunMode mode) {
        switch (mode) {
            case RUN_USING_ENCODERS: {
                return 2;
            }
            case RUN_WITHOUT_ENCODERS: {
                return 1;
            }
            case RUN_TO_POSITION: {
                return 3;
            }
            case RESET_ENCODERS: {
                return 4;
            }
            default: {
                return 4;
            }
        }
    }
    
    protected DcMotorController.RunMode flagMatrixToRunMode(final byte flag) {
        switch (flag) {
            case 2: {
                return DcMotorController.RunMode.RUN_USING_ENCODERS;
            }
            case 1: {
                return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
            }
            case 3: {
                return DcMotorController.RunMode.RUN_TO_POSITION;
            }
            case 4: {
                return DcMotorController.RunMode.RESET_ENCODERS;
            }
            default: {
                RobotLog.e("Invalid run mode flag " + flag);
                return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
            }
        }
    }
    
    public boolean isBusy(final int motor) {
        final MatrixI2cTransaction transaction = new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.a.a);
        this.master.queueTransaction(transaction);
        this.master.waitOnRead();
        return (this.a[transaction.motor].c & 0x80) != 0x0;
    }
    
    public void setMotorControllerDeviceMode(final DcMotorController.DeviceMode mode) {
        this.deviceMode = mode;
    }
    
    public DcMotorController.DeviceMode getMotorControllerDeviceMode() {
        return this.deviceMode;
    }
    
    public void setMotorChannelMode(final int motor, final DcMotorController.RunMode mode) {
        this.a(motor);
        if (!this.a[motor].d && this.a[motor].f == mode) {
            return;
        }
        this.master.queueTransaction(new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.a.a, this.runModeToFlagMatrix(mode)));
        if ((this.a[motor].f = mode) == DcMotorController.RunMode.RESET_ENCODERS) {
            this.a[motor].d = true;
        }
        else {
            this.a[motor].d = false;
        }
    }
    
    public DcMotorController.RunMode getMotorChannelMode(final int motor) {
        this.a(motor);
        return this.a[motor].f;
    }
    
    public void setMotorPowerFloat(final int motor) {
        this.a(motor);
        if (!this.a[motor].d) {
            this.master.queueTransaction(new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.a.a, 4));
        }
        this.a[motor].d = true;
    }
    
    public boolean getMotorPowerFloat(final int motor) {
        this.a(motor);
        return this.a[motor].d;
    }
    
    public void setMotorPower(final Set<DcMotor> motors, final double power) {
        Range.throwIfRangeIsInvalid(power, -1.0, 1.0);
        for (final DcMotor dcMotor : motors) {
            byte speed = (byte)(power * 100.0);
            if (dcMotor.getDirection() == DcMotor.Direction.REVERSE) {
                speed *= -1;
            }
            final int portNumber = dcMotor.getPortNumber();
            this.master.queueTransaction(new MatrixI2cTransaction((byte)portNumber, speed, this.a[portNumber].a, (byte)(this.runModeToFlagMatrix(this.a[portNumber].f) | 0x8)));
        }
        this.master.queueTransaction(new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.i, 1));
    }
    
    public void setMotorPower(final int motor, final double power) {
        this.a(motor);
        Range.throwIfRangeIsInvalid(power, -1.0, 1.0);
        this.master.queueTransaction(new MatrixI2cTransaction((byte)motor, (byte)(power * 100.0), this.a[motor].a, this.runModeToFlagMatrix(this.a[motor].f)));
        this.a[motor].e = power;
    }
    
    public double getMotorPower(final int motor) {
        this.a(motor);
        return this.a[motor].e;
    }
    
    public void setMotorTargetPosition(final int motor, final int position) {
        this.a(motor);
        this.master.queueTransaction(new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.a.b, position));
        this.a[motor].a = position;
    }
    
    public int getMotorTargetPosition(final int motor) {
        this.a(motor);
        if (this.master.queueTransaction(new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.a.b))) {
            this.master.waitOnRead();
        }
        return this.a[motor].a;
    }
    
    public int getMotorCurrentPosition(final int motor) {
        this.a(motor);
        if (this.master.queueTransaction(new MatrixI2cTransaction((byte)motor, MatrixI2cTransaction.a.e))) {
            this.master.waitOnRead();
        }
        return this.a[motor].b;
    }
    
    public int getBattery() {
        if (this.master.queueTransaction(new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.d))) {
            this.master.waitOnRead();
        }
        return this.b;
    }
    
    public String getDeviceName() {
        return "Matrix Motor Controller";
    }
    
    public String getConnectionInfo() {
        return this.master.getConnectionInfo();
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
        this.setMotorPowerFloat(3);
        this.setMotorPowerFloat(4);
    }
    
    public void handleReadBattery(final byte[] buffer) {
        this.b = 40 * TypeConversion.unsignedByteToInt(buffer[4]);
        RobotLog.v("Battery voltage: " + this.b + "mV");
    }
    
    public void handleReadPosition(final MatrixI2cTransaction transaction, final byte[] buffer) {
        this.a[transaction.motor].b = TypeConversion.byteArrayToInt(Arrays.copyOfRange(buffer, 4, 8));
        RobotLog.v("Position motor: " + transaction.motor + " " + this.a[transaction.motor].b);
    }
    
    public void handleReadTargetPosition(final MatrixI2cTransaction transaction, final byte[] buffer) {
        this.a[transaction.motor].a = TypeConversion.byteArrayToInt(Arrays.copyOfRange(buffer, 4, 8));
        RobotLog.v("Target motor: " + transaction.motor + " " + this.a[transaction.motor].a);
    }
    
    public void handleReadMode(final MatrixI2cTransaction transaction, final byte[] buffer) {
        this.a[transaction.motor].c = buffer[4];
        RobotLog.v("Mode: " + this.a[transaction.motor].c);
    }
    
    private void a(final int n) {
        if (n < 1 || n > 4) {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", n, 4));
        }
    }
    
    private class a
    {
        public int a;
        public int b;
        public byte c;
        public boolean d;
        public double e;
        public DcMotorController.RunMode f;
        
        public a() {
            this.a = 0;
            this.b = 0;
            this.c = 0;
            this.e = 0.0;
            this.d = true;
            this.f = DcMotorController.RunMode.RESET_ENCODERS;
        }
    }
}
