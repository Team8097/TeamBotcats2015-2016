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

package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class GamepadTeleOp extends OpMode {


    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    Servo claw;
    Servo arm;

    boolean reverse = false;

    final static double ARM_MIN_RANGE = 0.20;
    final static double ARM_MAX_RANGE = 0.90;
    final static double CLAW_MIN_RANGE = 0.20;
    final static double CLAW_MAX_RANGE = 0.7;

    // position of the arm servo.
    double armPosition;

    // amount to change the arm servo position.
    double armDelta = 0.1;

    // position of the claw servo
    double clawPosition;

    // amount to change the claw servo position by
    double clawDelta = 0.1;

    /**
     * Constructor
     */
    public GamepadTeleOp() {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {
//        arm = hardwareMap.servo.get("servo_1");
//        claw = hardwareMap.servo.get("servo_6");

        // assign the starting position of the wrist and claw
        armPosition = 0.2;
        clawPosition = 0.2;

        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
//        touchSensor = hardwareMap.touchSensor.get("touch");
//        distanceSensor = hardwareMap.opticalDistanceSensor.get("distance");
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {
        if (gamepad1.x) {
            if (reverse) {
                motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
                motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
                motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
                motorBackRight.setDirection(DcMotor.Direction.REVERSE);
                reverse = false;

            } else {
                motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
                motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
                motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
                motorBackRight.setDirection(DcMotor.Direction.FORWARD);
                reverse = true;
            }

        }


        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

//        float right = gamepad1.left_stick_y;
//        float left = gamepad1.right_trigger;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
//        right = (float) scaleInput(right);
//        left = (float) scaleInput(left);

        // write the values to the motors
//        motorLeft.setDirection(DcMotor.Direction.REVERSE);
//        motorRight.setDirection(DcMotor.Direction.FORWARD);

        motorFrontRight.setPower(right);
        motorFrontLeft.setPower(left);
        motorBackRight.setPower(right);
        motorBackLeft.setPower(left);

//        SystemClock.sleep(500);
//        for (int i = 0; i < 20; i++) {
//            if (i % 2 == 0) {
//                motorLeft.setDirection(DcMotor.Direction.FORWARD);
//                motorRight.setDirection(DcMotor.Direction.FORWARD);
//                motorRight.setPower(0.5);
//                motorLeft.setPower(0.5);
//            } else {
//                motorLeft.setDirection(DcMotor.Direction.REVERSE);
//                motorRight.setDirection(DcMotor.Direction.REVERSE);
//                motorRight.setPower(0.5);
//                motorLeft.setPower(0.5);
//            }
//            SystemClock.sleep(100);
//        }
//        motorLeft.setDirection(DcMotor.Direction.FORWARD);
//        motorRight.setDirection(DcMotor.Direction.REVERSE);
//        motorRight.setPower(0.5);
//        motorLeft.setPower(0.5);
//        SystemClock.sleep(500);


        // update the position of the arm.



		/*
         * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
//        if (gamepad1.a) {
//            // if the A button is pushed on gamepad1, increment the position of
//            // the arm servo.
//            armPosition += armDelta;
//        }
//
//        if (gamepad1.y) {
//            // if the Y button is pushed on gamepad1, decrease the position of
//            // the arm servo.
//            armPosition -= armDelta;
//        }
//
//        // update the position of the claw
//        if (gamepad1.x) {
//            clawPosition += clawDelta;
//        }
//
//        if (gamepad1.b) {
//            clawPosition -= clawDelta;
//        }
//
//        // clip the position values so that they never exceed their allowed range.
//        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
//        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);
//
//        // write position values to the wrist and claw servo
//        arm.setPosition(armPosition);
//        claw.setPosition(clawPosition);
//
//        telemetry.addData("arm", String.format("%.2f", armPosition));
//        telemetry.addData("claw", String.format("%.2f", clawPosition));
//        telemetry.addData("Text", "*** Robot Data ***");
//        telemetry.addData("left pwr", motorLeft.getPower());
//        telemetry.addData("right pwr", motorRight.getPower());
        telemetry.addData("leftStickY", gamepad1.left_stick_y);
        telemetry.addData("frontRightMotor", motorFrontRight.getPower());
        telemetry.addData("frontLeftMotor", motorFrontLeft.getPower());
        telemetry.addData("backRightMotor", motorBackRight.getPower());
        telemetry.addData("backLeftMotor", motorBackLeft.getPower());
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}
