// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.logitech;

import android.os.Build;
import android.view.MotionEvent;

import com.qualcomm.robotcore.hardware.Gamepad;

public class LogitechGamepadF310 extends Gamepad {
    public LogitechGamepadF310() {
        this(null);
    }

    public LogitechGamepadF310(final GamepadCallback callback) {
        super(callback);
        this.joystickDeadzone = 0.06f;
    }

    @Override
    public void update(final MotionEvent event) {
        this.id = event.getDeviceId();
        this.timestamp = event.getEventTime();
        if (Build.VERSION.RELEASE.startsWith("5")) {
            this.a(event);
        } else {
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
            this.callCallback();
        }
    }

    private void a(final MotionEvent motionEvent) {
        this.left_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(0));
        this.left_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(1));
        this.right_stick_x = this.cleanMotionValues(motionEvent.getAxisValue(11));
        this.right_stick_y = this.cleanMotionValues(motionEvent.getAxisValue(14));
        this.left_trigger = motionEvent.getAxisValue(23);
        this.right_trigger = motionEvent.getAxisValue(22);
        this.dpad_down = (motionEvent.getAxisValue(16) > this.dpadThreshold);
        this.dpad_up = (motionEvent.getAxisValue(16) < -this.dpadThreshold);
        this.dpad_right = (motionEvent.getAxisValue(15) > this.dpadThreshold);
        this.dpad_left = (motionEvent.getAxisValue(15) < -this.dpadThreshold);
        this.callCallback();
    }

    @Override
    public String type() {
        return "F310";
    }
}
