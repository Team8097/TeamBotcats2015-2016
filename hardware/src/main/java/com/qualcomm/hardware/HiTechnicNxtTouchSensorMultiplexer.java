// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;

public class HiTechnicNxtTouchSensorMultiplexer extends TouchSensorMultiplexer
{
    int a;
    public static final int MASK_TOUCH_SENSOR_1 = 1;
    public static final int MASK_TOUCH_SENSOR_2 = 2;
    public static final int MASK_TOUCH_SENSOR_3 = 4;
    public static final int MASK_TOUCH_SENSOR_4 = 8;
    public static final int INVALID = -1;
    public static final int[] MASK_MAP;
    private final ModernRoboticsUsbLegacyModule b;
    private final int c;
    
    public HiTechnicNxtTouchSensorMultiplexer(final ModernRoboticsUsbLegacyModule legacyModule, final int physicalPort) {
        this.a = 4;
        legacyModule.enableAnalogReadMode(physicalPort);
        this.b = legacyModule;
        this.c = physicalPort;
    }
    
    public String status() {
        return String.format("NXT Touch Sensor Multiplexer, connected via device %s, port %d", this.b.getSerialNumber().toString(), this.c);
    }
    
    public String getDeviceName() {
        return "NXT Touch Sensor Multiplexer";
    }
    
    public String getConnectionInfo() {
        return this.b.getConnectionInfo() + "; port " + this.c;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public void close() {
    }
    
    public boolean isTouchSensorPressed(final int channel) {
        this.a(channel);
        return (this.a() & HiTechnicNxtTouchSensorMultiplexer.MASK_MAP[channel]) > 0;
    }
    
    public int getSwitches() {
        return this.a();
    }
    
    private int a() {
        final short n = (short)(1023 - TypeConversion.byteArrayToShort(this.b.readAnalog(3), ByteOrder.LITTLE_ENDIAN));
        int n2 = 339 * n / (1023 - n);
        n2 += 5;
        return n2 / 10;
    }
    
    private void a(final int n) {
        if (n <= 0 || n > this.a) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, this.a));
        }
    }
    
    static {
        MASK_MAP = new int[] { -1, 1, 2, 4, 8 };
    }
}
