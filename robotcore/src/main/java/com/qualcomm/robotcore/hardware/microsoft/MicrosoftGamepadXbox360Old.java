// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.microsoft;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MicrosoftGamepadXbox360Old extends Gamepad {
    public MicrosoftGamepadXbox360Old() {
        this(null);
    }

    public MicrosoftGamepadXbox360Old(final GamepadCallback callback) {
        super(callback);
        this.joystickDeadzone = 0.15f;
    }

    @Override
    public String type() {
        return "Xbox 360";
    }
}