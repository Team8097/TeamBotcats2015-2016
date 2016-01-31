// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.microsoft;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MicrosoftGamepadXbox360 extends Gamepad
{
    public MicrosoftGamepadXbox360() {
        this(null);
    }
    
    public MicrosoftGamepadXbox360(final GamepadCallback callback) {
        super(callback);
        this.joystickDeadzone = 0.06f;//was 0.15
    }
    
    @Override
    public String type() {
        return "Xbox 360";
    }
}
