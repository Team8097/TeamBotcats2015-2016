// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import java.util.AbstractMap;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.InputDevice;

import java.util.ArrayList;
import java.util.HashSet;

import com.qualcomm.robotcore.util.Range;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.exception.RobotCoreException;

import java.util.Set;

import com.qualcomm.robotcore.robocol.RobocolParsable;

public class Gamepad implements RobocolParsable {
    public static final int ID_UNASSOCIATED = -1;
    public float left_stick_x;
    public float left_stick_y;
    public float right_stick_x;
    public float right_stick_y;
    public boolean dpad_up;
    public boolean dpad_down;
    public boolean dpad_left;
    public boolean dpad_right;
    public boolean a;
    public boolean b;
    public boolean x;
    public boolean y;
    public boolean guide;
    public boolean start;
    public boolean back;
    public boolean left_bumper;
    public boolean right_bumper;
    public boolean left_stick_button;
    public boolean right_stick_button;
    public float left_trigger;
    public float right_trigger;
    public byte user;
    public int id;
    public long timestamp;
    protected float dpadThreshold;
    protected float joystickDeadzone;
    private final GamepadCallback c;
    private static Set<Integer> d;
    private static Set<a> e;

    public float[] allFloatValues = new float[48];
    public boolean update = false;

    public Gamepad() {
        this(null);
    }

    public Gamepad(final GamepadCallback callback) {
        this.left_stick_x = 0.0f;
        this.left_stick_y = 0.0f;
        this.right_stick_x = 0.0f;
        this.right_stick_y = 0.0f;
        this.dpad_up = false;
        this.dpad_down = false;
        this.dpad_left = false;
        this.dpad_right = false;
        this.a = false;
        this.b = false;
        this.x = false;
        this.y = false;
        this.guide = false;
        this.start = false;
        this.back = false;
        this.left_bumper = false;
        this.right_bumper = false;
        this.left_stick_button = false;
        this.right_stick_button = false;
        this.left_trigger = 0.0f;
        this.right_trigger = 0.0f;
        this.user = -1;
        this.id = -1;
        this.timestamp = 0L;
        this.dpadThreshold = 0.2f;
        this.joystickDeadzone = 0.06f;//was 0.2
        this.c = callback;
    }

    public void copy(final Gamepad gamepad) throws RobotCoreException {
        this.fromByteArray(gamepad.toByteArray());
    }

    public void reset() {
        try {
            this.copy(new Gamepad());
        } catch (RobotCoreException ex) {
            RobotLog.e("Gamepad library in an invalid state");
            throw new IllegalStateException("Gamepad library in an invalid state");
        }
    }

    public void setJoystickDeadzone(final float deadzone) {
        if (deadzone < 0.0f || deadzone > 1.0f) {
            throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
        }
        this.joystickDeadzone = deadzone;
    }

    public void update(final MotionEvent event) {
        this.id = event.getDeviceId();
        this.timestamp = event.getEventTime();
        this.left_stick_x = this.cleanMotionValues(event.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(event.getAxisValue(1));
        this.right_stick_x = this.cleanMotionValues(event.getAxisValue(12));
        this.right_stick_y = this.cleanMotionValues(event.getAxisValue(13));
        this.left_trigger = (event.getAxisValue(11) + 1.0f) / 2.0f;
        this.right_trigger = (event.getAxisValue(14) + 1.0f) / 2.0f;
        this.dpad_down = (event.getAxisValue(16) > this.dpadThreshold);
        this.dpad_up = (event.getAxisValue(16) < -this.dpadThreshold);
        this.dpad_right = (event.getAxisValue(15) > this.dpadThreshold);
        this.dpad_left = (event.getAxisValue(15) < -this.dpadThreshold);
        for (int i = 0; i < allFloatValues.length; i++) {
            if (i <= 25 || i >= 32) {
                allFloatValues[i] = event.getAxisValue(i);
            }
        }
        update = true;
        this.callCallback();
    }

    public void update(final KeyEvent event) {
        this.id = event.getDeviceId();
        this.timestamp = event.getEventTime();
        final int keyCode = event.getKeyCode();
        if (keyCode == 19) {
            this.dpad_up = this.pressed(event);
        } else if (keyCode == 20) {
            this.dpad_down = this.pressed(event);
        } else if (keyCode == 22) {
            this.dpad_right = this.pressed(event);
        } else if (keyCode == 21) {
            this.dpad_left = this.pressed(event);
        } else if (keyCode == 96) {
            this.a = this.pressed(event);
        } else if (keyCode == 97) {
            this.b = this.pressed(event);
        } else if (keyCode == 99) {
            this.x = this.pressed(event);
        } else if (keyCode == 100) {
            this.y = this.pressed(event);
        } else if (keyCode == 110) {
            this.guide = this.pressed(event);
        } else if (keyCode == 108) {
            this.start = this.pressed(event);
        } else if (keyCode == 4) {
            this.back = this.pressed(event);
        } else if (keyCode == 103) {
            this.right_bumper = this.pressed(event);
        } else if (keyCode == 102) {
            this.left_bumper = this.pressed(event);
        } else if (keyCode == 106) {
            this.left_stick_button = this.pressed(event);
        } else if (keyCode == 107) {
            this.right_stick_button = this.pressed(event);
        }
        this.callCallback();
    }

    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.GAMEPAD;
    }

    @Override
    public byte[] toByteArray() throws RobotCoreException {
        final ByteBuffer allocate = ByteBuffer.allocate(45);
        try {
            final int n = 0;
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort((short) 42);
            allocate.put((byte) 2);
            allocate.putInt(this.id);
            allocate.putLong(this.timestamp).array();
            allocate.putFloat(this.left_stick_x).array();
            allocate.putFloat(this.left_stick_y).array();
            allocate.putFloat(this.right_stick_x).array();
            allocate.putFloat(this.right_stick_y).array();
            allocate.putFloat(this.left_trigger).array();
            allocate.putFloat(this.right_trigger).array();
            allocate.putInt((((((((((((((((n << 1) + (this.left_stick_button ? 1 : 0) << 1) + (this.right_stick_button ? 1 : 0) << 1) + (this.dpad_up ? 1 : 0) << 1) + (this.dpad_down ? 1 : 0) << 1) + (this.dpad_left ? 1 : 0) << 1) + (this.dpad_right ? 1 : 0) << 1) + (this.a ? 1 : 0) << 1) + (this.b ? 1 : 0) << 1) + (this.x ? 1 : 0) << 1) + (this.y ? 1 : 0) << 1) + (this.guide ? 1 : 0) << 1) + (this.start ? 1 : 0) << 1) + (this.back ? 1 : 0) << 1) + (this.left_bumper ? 1 : 0) << 1) + (this.right_bumper ? 1 : 0));
            allocate.put(this.user);
        } catch (BufferOverflowException e) {
            RobotLog.logStacktrace(e);
        }
        return allocate.array();
    }

    @Override
    public void fromByteArray(final byte[] byteArray) throws RobotCoreException {
        if (byteArray.length < 45) {
            throw new RobotCoreException("Expected buffer of at least 45 bytes, received " + byteArray.length);
        }
        final ByteBuffer wrap = ByteBuffer.wrap(byteArray, 3, 42);
        final byte value = wrap.get();
        if (value >= 1) {
            this.id = wrap.getInt();
            this.timestamp = wrap.getLong();
            this.left_stick_x = wrap.getFloat();
            this.left_stick_y = wrap.getFloat();
            this.right_stick_x = wrap.getFloat();
            this.right_stick_y = wrap.getFloat();
            this.left_trigger = wrap.getFloat();
            this.right_trigger = wrap.getFloat();
            final int int1 = wrap.getInt();
            this.left_stick_button = ((int1 & 0x4000) != 0x0);
            this.right_stick_button = ((int1 & 0x2000) != 0x0);
            this.dpad_up = ((int1 & 0x1000) != 0x0);
            this.dpad_down = ((int1 & 0x800) != 0x0);
            this.dpad_left = ((int1 & 0x400) != 0x0);
            this.dpad_right = ((int1 & 0x200) != 0x0);
            this.a = ((int1 & 0x100) != 0x0);
            this.b = ((int1 & 0x80) != 0x0);
            this.x = ((int1 & 0x40) != 0x0);
            this.y = ((int1 & 0x20) != 0x0);
            this.guide = ((int1 & 0x10) != 0x0);
            this.start = ((int1 & 0x8) != 0x0);
            this.back = ((int1 & 0x4) != 0x0);
            this.left_bumper = ((int1 & 0x2) != 0x0);
            this.right_bumper = ((int1 & 0x1) != 0x0);
        }
        if (value >= 2) {
            this.user = wrap.get();
        }
        update = true;
        this.callCallback();
    }

    public boolean atRest() {
        return this.left_stick_x == 0.0f && this.left_stick_y == 0.0f && this.right_stick_x == 0.0f && this.right_stick_y == 0.0f && this.left_trigger == 0.0f && this.right_trigger == 0.0f;
    }

    public String type() {
        return "Standard";
    }

    @Override
    public String toString() {
        String s = new String();
        if (this.dpad_up) {
            s += "dpad_up ";
        }
        if (this.dpad_down) {
            s += "dpad_down ";
        }
        if (this.dpad_left) {
            s += "dpad_left ";
        }
        if (this.dpad_right) {
            s += "dpad_right ";
        }
        if (this.a) {
            s += "a ";
        }
        if (this.b) {
            s += "b ";
        }
        if (this.x) {
            s += "x ";
        }
        if (this.y) {
            s += "y ";
        }
        if (this.guide) {
            s += "guide ";
        }
        if (this.start) {
            s += "start ";
        }
        if (this.back) {
            s += "back ";
        }
        if (this.left_bumper) {
            s += "left_bumper ";
        }
        if (this.right_bumper) {
            s += "right_bumper ";
        }
        if (this.left_stick_button) {
            s += "left stick button ";
        }
        if (this.right_stick_button) {
            s += "right stick button ";
        }
        return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", this.id, this.user, this.left_stick_x, this.left_stick_y, this.right_stick_x, this.right_stick_y, this.left_trigger, this.right_trigger, s);
    }

    protected float cleanMotionValues(final float number) {
        if (number < this.joystickDeadzone && number > -this.joystickDeadzone) {
            return 0.0f;
        }
        if (number > 1.0f) {
            return 1.0f;
        }
        if (number < -1.0f) {
            return -1.0f;
        }
        if (number < 0.0f) {
            Range.scale(number, this.joystickDeadzone, 1.0, 0.0, 1.0);
        }
        if (number > 0.0f) {
            Range.scale(number, -this.joystickDeadzone, -1.0, 0.0, -1.0);
        }
        return number;
    }

    protected boolean pressed(final KeyEvent event) {
        return event.getAction() == 0;
    }

    protected void callCallback() {
        if (this.c != null) {
            this.c.gamepadChanged(this);
        }
    }

    public static void enableWhitelistFilter(final int vendorId, final int productId) {
        if (Gamepad.e == null) {
            Gamepad.e = new HashSet<a>();
        }
        Gamepad.e.add(new a(vendorId, productId));
    }

    public static void clearWhitelistFilter() {
        Gamepad.e = null;
    }

    @TargetApi(19)
    public static synchronized boolean isGamepadDevice(final int deviceId) {
        if (Gamepad.d.contains(deviceId)) {
            return true;
        }
        Gamepad.d = new HashSet<Integer>();
        for (final int n : InputDevice.getDeviceIds()) {
            final InputDevice device = InputDevice.getDevice(n);
            final int sources = device.getSources();
            if ((sources & 0x401) == 0x401 || (sources & 0x1000010) == 0x1000010) {
                if (Build.VERSION.SDK_INT >= 19) {
                    if (Gamepad.e == null || Gamepad.e.contains(new a(device.getVendorId(), device.getProductId()))) {
                        Gamepad.d.add(n);
                    }
                } else {
                    Gamepad.d.add(n);
                }
            }
        }
        return Gamepad.d.contains(deviceId);
    }

    static {
        Gamepad.d = new HashSet<Integer>();
        Gamepad.e = null;
    }

    private static class a extends AbstractMap.SimpleEntry<Integer, Integer> {
        public a(final int n, final int n2) {
            super(n, n2);
        }
    }

    public interface GamepadCallback {
        void gamepadChanged(final Gamepad p0);
    }
}
