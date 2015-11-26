/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LegacyModule;

public class AutonomousOpMode extends BaseOpMode {

    boolean onMountain = false;

    @Override
    public void init() {
//        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
//        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
//        motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
//        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
//        motorBackRight = hardwareMap.dcMotor.get("backRight");
//        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
//        motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
//        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        touchSensor = hardwareMap.touchSensor.get("touch");
//        distanceSensor = hardwareMap.opticalDistanceSensor.get("distance");
        colorSensor = hardwareMap.colorSensor.get("color");
        lightSensor = hardwareMap.lightSensor.get("light");
        ultrasonicSensor = hardwareMap.ultrasonicSensor.get("ultra");

    }

    @Override
    public void loop() {
        telemetry.addData("touch", touchSensor.isPressed());
        telemetry.addData("ultra", ultrasonicSensor.getUltrasonicLevel());
        telemetry.addData("light", lightSensor.getLightDetected());
        telemetry.addData("blue", colorSensor.blue());
        telemetry.addData("red", colorSensor.red());
    }


    protected boolean seesBaseOfMountain() {
        return false;
    }

    protected void buttonGo() {
        if (touchSensor.isPressed()) {
            motorFrontRight.setPower(0.5);
            motorFrontLeft.setPower(0.5);
        } else {
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);
        }
    }

    protected void distanceSensorGo() {
        if (distanceSensor.getLightDetected() < 0.5) {
            motorFrontRight.setPower(0.5);
            motorFrontLeft.setPower(0.5);
        } else {
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);
        }
    }
}
