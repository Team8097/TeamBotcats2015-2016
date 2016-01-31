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

import android.view.MotionEvent;

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
    boolean hookDown = false;
    boolean climberServoOut = false;

    int loop = 1;
    final int initialLoops = 10;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("1motor2");
        motorFrontRight = hardwareMap.dcMotor.get("1motor1");
        motorBackRight = hardwareMap.dcMotor.get("0motor2");
        motorBackLeft = hardwareMap.dcMotor.get("0motor1");
//        motorSpinny = hardwareMap.dcMotor.get("2hitech5motor2");
//        motorMoveArm = hardwareMap.dcMotor.get("2hitech5motor1");
//        motorExtend = hardwareMap.dcMotor.get("2hitech0motor1");
//        motorCollection = hardwareMap.dcMotor.get("2hitech0motor2");
        climberServo = hardwareMap.servo.get("4servo2");
        rightFlapServo = hardwareMap.servo.get("4servo1");
        leftFlapServo = hardwareMap.servo.get("4servo3");
        rightSweepServo = hardwareMap.servo.get("4servo4");
        leftSweepServo = hardwareMap.servo.get("4servo5");
//        rightHookServo = hardwareMap.servo.get("4servo5");
//        leftHookServo = hardwareMap.servo.get("4servo4");
        gamepad1.right_stick_y = -1.0f;
        gamepad1.right_stick_x = -1.0f;
        gamepad2.right_stick_y = -1.0f;
        gamepad2.right_stick_x = -1.0f;
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            climberServo.setPosition(climberServoInitPos);
            rightSweepServo.setPosition(rightSweepIn);
            leftSweepServo.setPosition(leftSweepIn);
//            rightHookServo.setPosition(rightHookFinalPos);
//            leftHookServo.setPosition(leftHookFinalPos);
            rightFlapServo.setPosition(rightFlapServoInitPos);
            leftFlapServo.setPosition(leftFlapServoInitPos);
            loop++;
        } else {
            control();
            logData("left trigger", String.valueOf(gamepad1.left_trigger));
            logData("right trigger", String.valueOf(gamepad1.right_trigger));
            logData("left stick button", String.valueOf(gamepad1.left_stick_button));
            logData("right stick button", String.valueOf(gamepad1.right_stick_button));
//        telemetry.addData("leftStickY", activeGamepad.left_stick_y);
//        telemetry.addData("leftStickX", activeGamepad.left_stick_x);
//        telemetry.addData("frontRightMotor", motorFrontRight.getPower());
//        telemetry.addData("frontLeftMotor", motorFrontLeft.getPower());
//        telemetry.addData("backRightMotor", motorBackRight.getPower());
//        telemetry.addData("backLeftMotor", motorBackLeft.getPower());
//        telemetry.addData("left", gamepad1.left_bumper);
//        telemetry.addData("right", gamepad1.right_bumper);
        }
    }

    private void control() {
        if (gamepad1.a || gamepad2.a) {
            if (climberServoOut) {
                climberServo.setPosition(climberServoInitPos);
                climberServoOut = false;
            } else {
                climberServo.setPosition(climberServoFinalPos);
                climberServoOut = true;
            }
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
        if (gamepad1.b || gamepad2.b) {
            if (hookDown) {
                rightHookServo.setPosition(rightHookInitPos);
                leftHookServo.setPosition(leftHookInitPos);
                hookDown = false;
            } else {
                rightHookServo.setPosition(rightHookFinalPos);
                leftHookServo.setPosition(leftHookFinalPos);
                hookDown = true;
            }
        }
        if (gamepad1.left_bumper || gamepad2.left_bumper) {
            motorSpinny.setPower(-0.5);
        } else if (gamepad1.right_bumper || gamepad2.right_bumper) {
            motorSpinny.setPower(0.5);
        } else {
            motorSpinny.setPower(0);
        }
        if (Math.abs(gamepad1.right_stick_y) > 0) {
            motorMoveArm.setPower(Math.pow(gamepad1.right_stick_y, 1.0 / 3.0));
        } else if (Math.abs(gamepad2.right_stick_y) > 0) {
            motorMoveArm.setPower(Math.pow(gamepad2.right_stick_y, 1.0 / 3.0));
        }
        if (gamepad1.dpad_up || gamepad2.dpad_up) {
            motorExtend.setPower(0.25);
        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
            motorExtend.setPower(-0.25);
        } else {
            motorExtend.setPower(0);
        }
        if (gamepad1.dpad_right || gamepad2.dpad_right) {
            motorCollection.setPower(1);
        } else if (gamepad1.dpad_left || gamepad2.dpad_left) {
            motorCollection.setPower(-1);
        } else {
            motorCollection.setPower(0);
        }
        if (Math.abs(gamepad1.left_stick_y) > Math.abs(gamepad2.left_stick_y)) {
            activeGamepad = gamepad1;
        } else if (Math.abs(gamepad2.left_stick_y) > Math.abs(gamepad1.left_stick_y)) {
            activeGamepad = gamepad2;
        }
        if (activeGamepad.right_stick_y > -1) {
            spinRight((activeGamepad.right_stick_y + 1) / 6.0);
        } else if (gamepad1.right_stick_x > -1) {
            spinLeft(-(activeGamepad.right_stick_x + 1) / 6.0);
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

    @Override
    public void stop() {
        motorCollection.setPower(0);
        motorSpinny.setPower(0);
        motorExtend.setPower(0);
        motorMoveArm.setPower(0);
        super.stop();
    }
}