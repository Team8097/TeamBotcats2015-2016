// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.ServoController;

public class ModernRoboticsUsbServoController extends ModernRoboticsUsbDevice implements ServoController
{
    public static final boolean DEBUG_LOGGING = false;
    public static final int MONITOR_LENGTH = 9;
    public static final int MAX_SERVOS = 6;
    public static final int SERVO_POSITION_MAX = 255;
    public static final byte PWM_ENABLE = 0;
    public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
    public static final byte PWM_DISABLE = -1;
    public static final byte START_ADDRESS = 64;
    public static final int ADDRESS_CHANNEL1 = 66;
    public static final int ADDRESS_CHANNEL2 = 67;
    public static final int ADDRESS_CHANNEL3 = 68;
    public static final int ADDRESS_CHANNEL4 = 69;
    public static final int ADDRESS_CHANNEL5 = 70;
    public static final int ADDRESS_CHANNEL6 = 71;
    public static final int ADDRESS_PWM = 72;
    public static final int ADDRESS_UNUSED = -1;
    public static final byte[] ADDRESS_CHANNEL_MAP;
    
    protected ModernRoboticsUsbServoController(final SerialNumber serialNumber, final RobotUsbDevice device, final EventLoopManager manager) throws RobotCoreException, InterruptedException {
        super(serialNumber, manager, new ReadWriteRunnableBlocking(serialNumber, device, 9, 64, false));
        this.pwmDisable();
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB Servo Controller";
    }
    
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public void close() {
        this.pwmDisable();
        super.close();
    }
    
    public void pwmEnable() {
        this.write(72, (byte)0);
    }
    
    public void pwmDisable() {
        this.write(72, (byte)(-1));
    }
    
    public ServoController.PwmStatus getPwmStatus() {
        if (this.read(72, 1)[0] == -1) {
            return ServoController.PwmStatus.DISABLED;
        }
        return ServoController.PwmStatus.ENABLED;
    }
    
    public void setServoPosition(final int channel, final double position) {
        this.a(channel);
        Range.throwIfRangeIsInvalid(position, 0.0, 1.0);
        this.write(ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP[channel], position * 255.0);
        this.pwmEnable();
    }
    
    public double getServoPosition(final int channel) {
        this.a(channel);
        return TypeConversion.unsignedByteToDouble(this.read(ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP[channel], 1)[0]) / 255.0;
    }
    
    private void a(final int n) {
        if (n < 1 || n > ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP.length) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, 6));
        }
    }
    
    static {
        ADDRESS_CHANNEL_MAP = new byte[] { -1, 66, 67, 68, 69, 70, 71 };
    }
}
