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

import com.qualcomm.robotcore.hardware.Gamepad;

//Opmode for TeleOp. Allows for remote control of movement in any direction as well as spinning in place.
//Also allows for movement of the arm to drop climbers in case autonomous fails.
public class NewTeleOpOpMode extends BaseOpMode {
    Gamepad activeGamepad;
    boolean hookDown = false;
    boolean climberServoOut = false;
    long climberServoStart = 0;
    long hookServoStart = 0;
    int servoStage = 0;
    long startServoTime = 0;
    boolean servosRight = false;
    boolean servosLeft = false;

    int loop = 1;
    final int initialLoops = 10;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorExtend = hardwareMap.dcMotor.get("extend");
        motorCollection = hardwareMap.dcMotor.get("collect");
//        climberServo = hardwareMap.servo.get("climbers");
//        rightFlapServo = hardwareMap.servo.get("rightFlap");
//        leftFlapServo = hardwareMap.servo.get("leftFlap");
//        rightHookServo = hardwareMap.servo.get("rightHook");
//        leftHookServo = hardwareMap.servo.get("leftHook");
//        boxSpin = hardwareMap.servo.get("boxSpin");
//        boxLift = hardwareMap.servo.get("boxLift");
//        boxTilt = hardwareMap.servo.get("boxTilt");
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            activeGamepad = gamepad1;
//            climberServo.setPosition(climberServoInitPos);
//            rightHookServo.setPosition(rightHookInitPos);
//            leftHookServo.setPosition(leftHookInitPos);
//            rightFlapServo.setPosition(rightFlapServoInitPos);
//            leftFlapServo.setPosition(leftFlapServoInitPos);
//            boxSpin.setPosition(spinInitPos);
//            boxLift.setPosition(liftInitPos);
//            boxTilt.setPosition(tiltInitPos);
            loop++;
        } else {
            control();
//            logData("left trigger", String.valueOf(gamepad1.left_trigger));
//            logData("right trigger", String.valueOf(gamepad1.right_trigger));
//            logData("left stick button", String.valueOf(gamepad1.left_stick_button));
//            logData("right stick button", String.valueOf(gamepad1.right_stick_button));
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
//        if (gamepad1.x || gamepad2.x) {
//            if (System.currentTimeMillis() - climberServoStart > 300) {
//                if (climberServoOut) {
//                    climberServo.setPosition(climberServoInitPos);
//                    climberServoOut = false;
//                } else {
//                    climberServo.setPosition(climberServoFinalPos);
//                    climberServoOut = true;
//                }
//                climberServoStart = System.currentTimeMillis();
//            }
//        }


//        if (gamepad1.b || gamepad2.b) {
//            if (System.currentTimeMillis() - hookServoStart > 300) {
//                if (hookDown) {
//                    rightHookServo.setPosition(rightHookInitPos);
//                    leftHookServo.setPosition(leftHookInitPos);
//                    hookDown = false;
//                } else {
//                    rightHookServo.setPosition(rightHookDownPos);
//                    leftHookServo.setPosition(leftHookDownPos);
//                    hookDown = true;
//                }
//                hookServoStart = System.currentTimeMillis();
//            }
//        }


//        if (gamepad1.dpad_up || gamepad2.dpad_up) {
//            motorExtend.setPower(0.25);
//        } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
//            motorExtend.setPower(-0.25);
//        } else {
//            motorExtend.setPower(0);
//        }


//        if ((gamepad1.dpad_right || gamepad2.dpad_right) && !servosRight) {
//            servosRight = true;
//            startServoTime = System.currentTimeMillis();
//        } else if (gamepad1.dpad_left || gamepad2.dpad_left && !servosLeft) {
//            servosLeft = true;
//            startServoTime = System.currentTimeMillis();
//        }
//        if (servosRight) {
//            servosRight();
//        } else if (servosLeft) {
//            servosLeft();
//        }
        if (gamepad1.a || gamepad2.a) {
            motorCollection.setPower(-1);
        } else if (gamepad1.y || gamepad2.y) {
            motorCollection.setPower(1);
        } else {
            motorCollection.setPower(0);
        }
        if (Math.abs(gamepad1.left_stick_y) > Math.abs(gamepad2.left_stick_y)) {
            activeGamepad = gamepad1;
        } else if (Math.abs(gamepad2.left_stick_y) > Math.abs(gamepad1.left_stick_y)) {
            activeGamepad = gamepad2;
        }
//        if ((activeGamepad.right_trigger > 0 || activeGamepad.left_trigger > 0) && (activeGamepad.left_stick_y > 0 || activeGamepad.left_stick_x > 0)) {
//            double joystickInputY = -activeGamepad.left_stick_y;
//            double joystickInputX = activeGamepad.left_stick_x;
//            double spin = activeGamepad.left_trigger - activeGamepad.right_trigger;
//            drift(joystickInputX, joystickInputY, spin);
//        } else
        if (activeGamepad.right_trigger > 0) {
            spinRight(activeGamepad.right_trigger / 3.0);
        } else if (activeGamepad.left_trigger > 0) {
            spinLeft(activeGamepad.left_trigger / 3.0);
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

    protected void drift(double x, double y, double spin) {
        motorFrontRight.setPower((y - x + spin) / 3.0);
        motorBackRight.setPower((y + x + spin) / 3.0);
        motorFrontLeft.setPower((-y - x + spin) / 3.0);
        motorBackLeft.setPower((-y + x + spin) / 3.0);
    }

    protected void servosRight() {
        final int stageSpin = 0;
        final int stageTilt = 1;
        final int stageGoBack = 2;
        if (servoStage == stageSpin) {
            if (System.currentTimeMillis() - startServoTime < 500) {
                boxSpin.setPosition(spinRightPos);
            } else {
                servoStage++;
                startServoTime = System.currentTimeMillis();
            }
        } else if (servoStage == stageTilt) {
            if (System.currentTimeMillis() - startServoTime < 750) {
                boxTilt.setPosition(tiltRightPos);
            } else {
                servoStage++;
                startServoTime = System.currentTimeMillis();
            }
        } else if (servoStage == stageGoBack) {
            if (System.currentTimeMillis() - startServoTime < 200) {
                boxSpin.setPosition(spinInitPos);
                boxTilt.setPosition(tiltInitPos);
            } else {
                servoStage++;
            }
        } else {
            servoStage = 0;
            servosRight = false;
        }
    }

    protected void servosLeft() {
        final int stageSpin = 0;
        final int stageTilt = 1;
        final int stageGoBack = 2;
        if (servoStage == stageSpin) {
            if (System.currentTimeMillis() - startServoTime < 500) {
                boxSpin.setPosition(spinLeftPos);
            } else {
                servoStage++;
                startServoTime = System.currentTimeMillis();
            }
        } else if (servoStage == stageTilt) {
            if (System.currentTimeMillis() - startServoTime < 750) {
                boxTilt.setPosition(tiltLeftPos);
            } else {
                servoStage++;
                startServoTime = System.currentTimeMillis();
            }
        } else if (servoStage == stageGoBack) {
            if (System.currentTimeMillis() - startServoTime < 200) {
                boxSpin.setPosition(spinInitPos);
                boxTilt.setPosition(tiltInitPos);
            } else {
                servoStage++;
            }
        } else {
            servoStage = 0;
            servosLeft = false;
        }
    }

    @Override
    public void stop() {
        motorCollection.setPower(0);
//        motorSpinny.setPower(0);
        motorExtend.setPower(0);
//        motorLiftArm.setPower(0);
        super.stop();
    }
}