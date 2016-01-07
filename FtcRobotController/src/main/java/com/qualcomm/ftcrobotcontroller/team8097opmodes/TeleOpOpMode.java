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

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

//Opmode for TeleOp. Allows for remote control of movement in any direction as well as spinning in place.
//Also allows for movement of the arm to drop climbers in case autonomous fails.
public class TeleOpOpMode extends BaseOpMode {
    Gamepad activeGamepad = gamepad1;
    int sweepStage = 0;
    static final int POS_IN = 0;
    static final int POS_TRIANGLE = 1;
    static final int POS_OUT = 2;
    int currentSweeperPos = POS_IN;
    int goalSweeperPos = POS_IN;
    long startSweepTime;

    @Override
    public void init() {
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        armServo = hardwareMap.servo.get("armServo");
        armServo.setPosition(armServoInitPos);
        rightSweepServo = hardwareMap.servo.get("rightSweep");
        leftSweepServo = hardwareMap.servo.get("leftSweep");
        leftSweepServo.setPosition(leftSweepIn);
        rightSweepServo.setPosition(rightSweepIn);
    }

    @Override
    public void loop() {
        control();
//        telemetry.addData("leftStickY", activeGamepad.left_stick_y);
//        telemetry.addData("leftStickX", activeGamepad.left_stick_x);
//        telemetry.addData("frontRightMotor", motorFrontRight.getPower());
//        telemetry.addData("frontLeftMotor", motorFrontLeft.getPower());
//        telemetry.addData("backRightMotor", motorBackRight.getPower());
//        telemetry.addData("backLeftMotor", motorBackLeft.getPower());
    }

    private void control() {
        if (gamepad1.a || gamepad2.a) {
            armServo.setPosition(armServoFinalPos);
        } else if (gamepad1.b || gamepad2.b) {
            armServo.setPosition(armServoInitPos);
        }
        if ((gamepad1.x || gamepad2.x) && currentSweeperPos == goalSweeperPos && currentSweeperPos != POS_OUT) {
            goalSweeperPos++;
            startSweepTime = System.currentTimeMillis();
        } else if ((gamepad1.y || gamepad2.y) && currentSweeperPos == goalSweeperPos && currentSweeperPos != POS_IN) {
            goalSweeperPos--;
            startSweepTime = System.currentTimeMillis();
        } else if (currentSweeperPos != goalSweeperPos) {
            moveSweeper(currentSweeperPos, goalSweeperPos);
        }
        if (Math.abs(gamepad1.left_stick_y) > Math.abs(gamepad2.left_stick_y)) {
            activeGamepad = gamepad1;
        } else if (Math.abs(gamepad2.left_stick_y) > Math.abs(gamepad1.left_stick_y)) {
            activeGamepad = gamepad2;
        }
        if (activeGamepad.dpad_left) {
            spinLeft(0.25);
        } else if (activeGamepad.dpad_right) {
            spinRight(0.25);
        } else {
            double joystickInputY = -activeGamepad.left_stick_y;
            double joystickInputX = activeGamepad.left_stick_x;
            goDirection(joystickInputX, joystickInputY);
        }

    }

    protected void goDirection(double x, double y) {
        motorFrontRight.setPower((y - x) / 2.0);
        motorBackRight.setPower((y + x) / 2.0);
        motorFrontLeft.setPower((-y - x) / 2.0);
        motorBackLeft.setPower((-y + x) / 2.0);
    }

    protected void moveSweeper(int startPos, int endPos) {
        if (startPos == POS_IN || startPos == POS_OUT) {
            sweepTriangle();
        } else if (endPos == POS_IN) {
            sweepIn();
        } else if (endPos == POS_OUT) {
            sweepOut();
        }
    }

    protected void sweepTriangle() {
        final int rightOut = 0;
        final int leftTriangle = 1;
        final int rightTriangle = 2;
        if (sweepStage == rightOut) {
            if (System.currentTimeMillis() - startSweepTime < 200) {
                rightSweepServo.setPosition(rightSweepOut);
            } else {
                sweepStage++;
                startSweepTime = System.currentTimeMillis();
            }
        } else if (sweepStage == leftTriangle) {
            if (System.currentTimeMillis() - startSweepTime < 200) {
                leftSweepServo.setPosition(leftSweepTriangle);
            } else {
                sweepStage++;
                startSweepTime = System.currentTimeMillis();
            }
        } else if (sweepStage == rightTriangle) {
            if (System.currentTimeMillis() - startSweepTime < 200) {
                rightSweepServo.setPosition(rightSweepTriangle);
            } else {
                sweepStage++;
                startSweepTime = System.currentTimeMillis();
            }
        } else {
            sweepStage = 0;
            currentSweeperPos = POS_TRIANGLE;
        }
    }

    protected void sweepOut() {
        if (System.currentTimeMillis() - startSweepTime < 600) {
            rightSweepServo.setPosition(rightSweepOut);
            leftSweepServo.setPosition(leftSweepOut);
        } else {
            currentSweeperPos = POS_OUT;
        }
    }

    protected void sweepIn() {
        final int rightOut = 0;
        final int leftIn = 1;
        final int rightIn = 2;
        if (sweepStage == rightOut) {
            if (System.currentTimeMillis() - startSweepTime < 200) {
                rightSweepServo.setPosition(rightSweepOut);
            } else {
                sweepStage++;
                startSweepTime = System.currentTimeMillis();
            }
        } else if (sweepStage == leftIn) {
            if (System.currentTimeMillis() - startSweepTime < 200) {
                leftSweepServo.setPosition(leftSweepIn);
            } else {
                sweepStage++;
                startSweepTime = System.currentTimeMillis();
            }
        } else if (sweepStage == rightIn) {
            if (System.currentTimeMillis() - startSweepTime < 200) {
                rightSweepServo.setPosition(rightSweepIn);
            } else {
                sweepStage++;
                startSweepTime = System.currentTimeMillis();
            }
        } else {
            sweepStage = 0;
            currentSweeperPos = POS_IN;
        }
    }
}