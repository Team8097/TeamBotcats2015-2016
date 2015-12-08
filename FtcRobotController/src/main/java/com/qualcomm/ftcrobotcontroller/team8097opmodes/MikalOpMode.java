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

public class MikalOpMode extends BaseOpMode {

    @Override
    public void init() {

        //Dc motors
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");

        //leagacy
        lightSensor = hardwareMap.lightSensor.get("lightSensor");
        colorSensor = hardwareMap.colorSensor.get("colorSensor");
        ultraUpSensor = hardwareMap.ultrasonicSensor.get("ultraUpSensor");
        ultraDownSensor = hardwareMap.ultrasonicSensor.get("ultraDownSensor");
        //ultraLeftSensor = hardwareMap.ultrasonicSensor.get("ultraLeftSensor");
        //ultraRightSensor = hardwareMap.ultrasonicSensor.get("ultraRightSensor");

        //Servos
        armServo = hardwareMap.servo.get("armServo");
        leftButtonPresserServo = hardwareMap.servo.get("leftButtonPresserServo");
        rightButtonPresserServo = hardwareMap.servo.get("rightButtonPresserServo");
    }

    @Override
    public void loop() {

        //Michaels Ultrasonic tests:

        if(true) {

            if(ultraUpSensor.getUltrasonicLevel() < 40) {
                goDirection(0, -1);
            }

            if(ultraDownSensor.getUltrasonicLevel() < 40) {
                goDirection(0, 1);
            }

            if(ultraDownSensor.getUltrasonicLevel() >= 40 & ultraUpSensor.getUltrasonicLevel() >= 40){


                goDirection(0,0);
            }

        }else {

            control();

        }



        telemetry.addData("frontRightMotor", motorFrontRight.getPower());
        telemetry.addData("frontLeftMotor", motorFrontLeft.getPower());
        telemetry.addData("backRightMotor", motorBackRight.getPower());
        telemetry.addData("backLeftMotor", motorBackLeft.getPower());

        lightSensor.enableLed(true);

        controllerdebug();




    }

    @Override
    public void stop() {
    }

    private void controllerdebug(){


        //Sensors

        //Legacy:
        telemetry.addData("Light getlightDected", lightSensor.getLightDetected());
        telemetry.addData("Light getlightDected RAW", lightSensor.getLightDetectedRaw());
        telemetry.addData("Light ", lightSensor);

        //Ultrasonic array:
        telemetry.addData("Ultrasonic Up Sensor", ultraUpSensor);
        telemetry.addData("Ultrasonic Down Sensor", ultraDownSensor);
        //telemetry.addData("Ultrasonic Left Sensor", ultraLeftSensor);
        //telemetry.addData("Ultrasonic Right Sensor", ultraRightSensor);

        telemetry.addData("Ultrasonic Level Up Sensor", ultraUpSensor.getUltrasonicLevel());
        telemetry.addData("Ultrasonic Level Down Sensor", ultraDownSensor.getUltrasonicLevel());
        //telemetry.addData("Ultrasonic Level Left Sensor", ultraLeftSensor.getUltrasonicLevel());
        //telemetry.addData("Ultrasonic Level Right Sensor", ultraRightSensor.getUltrasonicLevel());



        //Gamepad 1:
        telemetry.addData("1 Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("1 Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("1 Right Stick Y", gamepad1.right_stick_y);
        telemetry.addData("1 Right Stick X", gamepad1.right_stick_x);

        telemetry.addData("1 Dpad_Up",gamepad1.dpad_up);
        telemetry.addData("1 Dpad_Down",gamepad1.dpad_down);
        telemetry.addData("1 Dpad_Left",gamepad1.dpad_left);
        telemetry.addData("1 Dpad_Right",gamepad1.dpad_right);
        telemetry.addData("1 x",gamepad1.x);
        telemetry.addData("1 y",gamepad1.y);

        telemetry.addData("1 Left Bumper",gamepad1.left_bumper);
        telemetry.addData("1 Right Bumper",gamepad1.right_bumper);

        telemetry.addData("1 Left Trigger",gamepad1.left_trigger);
        telemetry.addData("1 Right Trigger",gamepad1.right_trigger);
        telemetry.addData("1 x",gamepad1.x);
        telemetry.addData("1 y",gamepad1.y);
        telemetry.addData("1 a",gamepad1.a);
        telemetry.addData("1 b",gamepad1.b);

        //Gamepad 2:
        telemetry.addData("2 Left Stick Y", gamepad2.left_stick_y);
        telemetry.addData("2 Left Stick X", gamepad2.left_stick_x);
        telemetry.addData("2 Right Stick Y", gamepad2.right_stick_y);
        telemetry.addData("2 Right Stick X", gamepad2.right_stick_x);

        telemetry.addData("2 Dpad_Up",gamepad2.dpad_up);
        telemetry.addData("2 Dpad_Down",gamepad2.dpad_down);
        telemetry.addData("2 Dpad_Left",gamepad2.dpad_left);
        telemetry.addData("2 Dpad_Right",gamepad2.dpad_right);
        telemetry.addData("2 x",gamepad2.x);
        telemetry.addData("2 y",gamepad2.y);

        telemetry.addData("2 Left Bumper",gamepad2.left_bumper);
        telemetry.addData("2 Right Bumper",gamepad2.right_bumper);

        telemetry.addData("2 Left Trigger",gamepad2.left_trigger);
        telemetry.addData("2 Right Trigger",gamepad2.right_trigger);
        telemetry.addData("2 x",gamepad2.x);
        telemetry.addData("2 y",gamepad2.y);
        telemetry.addData("2 a",gamepad2.a);
        telemetry.addData("2 b",gamepad2.b);

    }

    private void control() {
        if (gamepad1.dpad_left) {
            spinLeft(0.25);
        }
        if (gamepad1.dpad_right) {
            spinRight(0.25);
        } else {
            double joystickInputY = -gamepad1.left_stick_y;
            double joystickInputX = -gamepad1.left_stick_x;
            goDirection(joystickInputX, joystickInputY);
        }
    }

    protected void goDirection(double x, double y) {
        motorFrontRight.setPower((y - x) / 2.0);
        motorBackRight.setPower((y + x) / 2.0);
        motorFrontLeft.setPower((-y - x) / 2.0);
        motorBackLeft.setPower((-y + x) / 2.0);
    }

}
