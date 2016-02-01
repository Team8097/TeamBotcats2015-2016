// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class ModernRoboticsUsbDcMotorController extends ModernRoboticsUsbDevice implements DcMotorController, VoltageSensor
{
    public static final boolean DEBUG_LOGGING = false;
    public static final int MONITOR_LENGTH = 30;
    public static final int MIN_MOTOR = 1;
    public static final int MAX_MOTOR = 2;
    public static final byte POWER_MAX = 100;
    public static final byte POWER_BREAK = 0;
    public static final byte POWER_MIN = -100;
    public static final byte POWER_FLOAT = Byte.MIN_VALUE;
    public static final byte RATIO_MIN = Byte.MIN_VALUE;
    public static final byte RATIO_MAX = Byte.MAX_VALUE;
    public static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
    public static final int BATTERY_MAX_MEASURABLE_VOLTAGE_INT = 1023;
    public static final double BATTERY_MAX_MEASURABLE_VOLTAGE = 20.4;
    public static final byte DEFAULT_P_COEFFICIENT = Byte.MIN_VALUE;
    public static final byte DEFAULT_I_COEFFICIENT = 64;
    public static final byte DEFAULT_D_COEFFICIENT = -72;
    public static final byte START_ADDRESS = 64;
    public static final int CHANNEL_MODE_MASK_SELECTION = 3;
    public static final int CHANNEL_MODE_MASK_LOCK = 4;
    public static final int CHANNEL_MODE_MASK_REVERSE = 8;
    public static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
    public static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
    public static final int CHANNEL_MODE_MASK_ERROR = 64;
    public static final int CHANNEL_MODE_MASK_BUSY = 128;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY = 0;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED = 1;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
    public static final byte CHANNEL_MODE_FLAG_LOCK = 4;
    public static final byte CHANNEL_MODE_FLAG_REVERSE = 8;
    public static final byte CHANNEL_MODE_FLAG_NO_TIMEOUT = 16;
    public static final byte CHANNEL_MODE_FLAG_UNUSED = 32;
    public static final byte CHANNEL_MODE_FLAG_ERROR = 64;
    public static final byte CHANNEL_MODE_FLAG_BUSY = Byte.MIN_VALUE;
    public static final int ADDRESS_MOTOR1_TARGET_ENCODER_VALUE = 64;
    public static final int ADDRESS_MOTOR1_MODE = 68;
    public static final int ADDRESS_MOTOR1_POWER = 69;
    public static final int ADDRESS_MOTOR2_POWER = 70;
    public static final int ADDRESS_MOTOR2_MODE = 71;
    public static final int ADDRESS_MOTOR2_TARGET_ENCODER_VALUE = 72;
    public static final int ADDRESS_MOTOR1_CURRENT_ENCODER_VALUE = 76;
    public static final int ADDRESS_MOTOR2_CURRENT_ENCODER_VALUE = 80;
    public static final int ADDRESS_BATTERY_VOLTAGE = 84;
    public static final int ADDRESS_MOTOR1_GEAR_RATIO = 86;
    public static final int ADDRESS_MOTOR1_P_COEFFICIENT = 87;
    public static final int ADDRESS_MOTOR1_I_COEFFICIENT = 88;
    public static final int ADDRESS_MOTOR1_D_COEFFICIENT = 89;
    public static final int ADDRESS_MOTOR2_GEAR_RATIO = 90;
    public static final int ADDRESS_MOTOR2_P_COEFFICIENT = 91;
    public static final int ADDRESS_MOTOR2_I_COEFFICIENT = 92;
    public static final int ADDRESS_MOTOR2_D_COEFFICIENT = 93;
    public static final int ADDRESS_UNUSED = 255;
    public static final int[] ADDRESS_MOTOR_POWER_MAP;
    public static final int[] ADDRESS_MOTOR_MODE_MAP;
    public static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP;
    public static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP;
    public static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP;
    public static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP;
    private a[] a;
    
    protected ModernRoboticsUsbDcMotorController(final SerialNumber serialNumber, final RobotUsbDevice device, final EventLoopManager manager) throws RobotCoreException, InterruptedException {
        super(serialNumber, manager, new ReadWriteRunnableBlocking(serialNumber, device, 30, 64, false));
        this.a = new a[3];
        this.readWriteRunnable.setCallback(this);
        for (int i = 0; i < this.a.length; ++i) {
            this.a[i] = new a();
        }
        this.a();
        this.b();
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB DC Motor Controller";
    }
    
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public void close() {
        this.a();
        super.close();
    }
    
    public void setMotorControllerDeviceMode(final DcMotorController.DeviceMode mode) {
    }
    
    public DcMotorController.DeviceMode getMotorControllerDeviceMode() {
        return DcMotorController.DeviceMode.READ_WRITE;
    }
    
    public void setMotorChannelMode(final int motor, final DcMotorController.RunMode mode) {
        this.a(motor);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_MODE_MAP[motor], runModeToFlag(mode));
    }
    
    public DcMotorController.RunMode getMotorChannelMode(final int motor) {
        this.a(motor);
        return flagToRunMode(this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_MODE_MAP[motor]));
    }
    
    public void setMotorPower(final int motor, final double power) {
        this.a(motor);
        Range.throwIfRangeIsInvalid(power, -1.0, 1.0);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[motor], new byte[] { (byte)(power * 100.0) });
    }
    
    public double getMotorPower(final int motor) {
        this.a(motor);
        final byte read = this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[motor]);
        double n;
        if (read == -128) {
            n = 0.0;
        }
        else {
            n = read / 100.0;
        }
        return n;
    }
    
    public boolean isBusy(final int motor) {
        this.a(motor);
        return this.a[motor].a();
    }
    
    public void setMotorPowerFloat(final int motor) {
        this.a(motor);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[motor], new byte[] { -128 });
    }
    
    public boolean getMotorPowerFloat(final int motor) {
        this.a(motor);
        return this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[motor]) == -128;
    }
    
    public void setMotorTargetPosition(final int motor, final int position) {
        this.a(motor);
        Range.throwIfRangeIsInvalid((double)position, -2.147483648E9, 2.147483647E9);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], TypeConversion.intToByteArray(position));
    }
    
    public int getMotorTargetPosition(final int motor) {
        this.a(motor);
        return TypeConversion.byteArrayToInt(this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], 4));
    }
    
    public int getMotorCurrentPosition(final int motor) {
        this.a(motor);
        return TypeConversion.byteArrayToInt(this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4));
    }
    
    public double getVoltage() {
        return (TypeConversion.byteArrayToShort(this.read(84, 2)) >> 6 & 0x3FF) / 1023.0 * 20.4;
    }
    
    public void setGearRatio(final int motor, final double ratio) {
        this.a(motor);
        Range.throwIfRangeIsInvalid(ratio, -1.0, 1.0);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_GEAR_RATIO_MAP[motor], new byte[] { (byte)(ratio * 127.0) });
    }
    
    public double getGearRatio(final int motor) {
        this.a(motor);
        return this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_GEAR_RATIO_MAP[motor], 1)[0] / 127.0;
    }
    
    public void setDifferentialControlLoopCoefficients(final int motor, final DifferentialControlLoopCoefficients pid) {
        this.a(motor);
        if (pid.p > 255.0) {
            pid.p = 255.0;
        }
        if (pid.i > 255.0) {
            pid.i = 255.0;
        }
        if (pid.d > 255.0) {
            pid.d = 255.0;
        }
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[] { (byte)pid.p, (byte)pid.i, (byte)pid.d });
    }
    
    public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(final int motor) {
        this.a(motor);
        final DifferentialControlLoopCoefficients differentialControlLoopCoefficients = new DifferentialControlLoopCoefficients();
        final byte[] read = this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], 3);
        differentialControlLoopCoefficients.p = read[0];
        differentialControlLoopCoefficients.i = read[1];
        differentialControlLoopCoefficients.d = read[2];
        return differentialControlLoopCoefficients;
    }
    
    public static byte runModeToFlag(final DcMotorController.RunMode mode) {
        switch (mode) {
            case RUN_USING_ENCODERS: {
                return 1;
            }
            case RUN_WITHOUT_ENCODERS: {
                return 0;
            }
            case RUN_TO_POSITION: {
                return 2;
            }
            case RESET_ENCODERS: {
                return 3;
            }
            default: {
                return 1;
            }
        }
    }
    
    public static DcMotorController.RunMode flagToRunMode(final byte flag) {
        switch (flag & 0x3) {
            case 1: {
                return DcMotorController.RunMode.RUN_USING_ENCODERS;
            }
            case 0: {
                return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
            }
            case 2: {
                return DcMotorController.RunMode.RUN_TO_POSITION;
            }
            case 3: {
                return DcMotorController.RunMode.RESET_ENCODERS;
            }
            default: {
                return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
            }
        }
    }
    
    private void a() {
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
    }
    
    private void b() {
        for (int i = 1; i <= 2; ++i) {
            this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[i], new byte[] { -128, 64, -72 });
        }
    }
    
    private void a(final int n) {
        if (n < 1 || n > 2) {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", n, 2));
        }
    }
    
    @Override
    public void readComplete() throws InterruptedException {
        for (int i = 1; i <= 2; ++i) {
            this.a[i].a(this.getMotorCurrentPosition(i));
        }
    }
    
    static {
        ADDRESS_MOTOR_POWER_MAP = new int[] { 255, 69, 70 };
        ADDRESS_MOTOR_MODE_MAP = new int[] { 255, 68, 71 };
        ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = new int[] { 255, 64, 72 };
        ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = new int[] { 255, 76, 80 };
        ADDRESS_MOTOR_GEAR_RATIO_MAP = new int[] { 255, 86, 90 };
        ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = new int[] { 255, 87, 91 };
    }
    
    private static class a
    {
        private int[] a;
        private int[] b;
        private int c;
        
        private a() {
            this.a = new int[3];
            this.b = new int[3];
            this.c = 0;
        }
        
        public void a(final int n) {
            final int n2 = this.a[this.c];
            this.c = (this.c + 1) % this.a.length;
            this.b[this.c] = Math.abs(n2 - n);
            this.a[this.c] = n;
        }
        
        public boolean a() {
            int n = 0;
            final int[] b = this.b;
            for (int length = b.length, i = 0; i < length; ++i) {
                n += b[i];
            }
            return n > 6;
        }
    }
}
