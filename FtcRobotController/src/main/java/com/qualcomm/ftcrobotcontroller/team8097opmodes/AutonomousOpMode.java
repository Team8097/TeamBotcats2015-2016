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

import com.qualcomm.robotcore.hardware.DcMotorController;

import java.util.Arrays;
import java.util.HashMap;

//Base class for autonomous. It is used for testing movement autonomously,
//and includes methods for moving a certain distance or spinning a certain angle.
public class AutonomousOpMode extends BaseOpMode {
    final int initialLoops = 10;
    int loop = 1;
    long startTime;
    DcMotorController legacyControl1;
    DcMotorController legacyControl2;


    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("1motor1");
        motorFrontRight = hardwareMap.dcMotor.get("0motor2");
        motorBackRight = hardwareMap.dcMotor.get("0motor1");
        motorBackLeft = hardwareMap.dcMotor.get("1motor2");
        frontRightUltra = hardwareMap.ultrasonicSensor.get("3ultra4");
        frontLeftUltra = hardwareMap.ultrasonicSensor.get("3ultra5");
        frontLightSensor = hardwareMap.lightSensor.get("3light1");
        backLightSensor = hardwareMap.lightSensor.get("3light0");
        rightColorSensor = hardwareMap.lightSensor.get("2light1");
        leftColorSensor = hardwareMap.lightSensor.get("2light2");
        climberServo = hardwareMap.servo.get("4servo2");
        rightFlapServo = hardwareMap.servo.get("4servo1");
        leftFlapServo = hardwareMap.servo.get("4servo3");
        rightSweepServo = hardwareMap.servo.get("4servo4");
        leftSweepServo = hardwareMap.servo.get("4servo5");
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startTime = System.currentTimeMillis();
        } else {
//            if (gamepad1.a)
//                motorSpinny.setPower(0.1);
//            else if (gamepad1.b)
//                motorSpinny.setPower(-0.1);
//            else
            legacyControl1.setMotorPower(1, 0.25);
            legacyControl1.setMotorPower(2, 0.25);
//            legacyControl2.setMotorPower(1, 0.25);
            legacyControl2.setMotorPower(2, 0.25);
//            logData("spinny", String.valueOf(motorSpinny.getCurrentPosition()));
//            goPerfectlyStraight2Wheels();
//            logData("Front Right", String.valueOf(motorFrontRight.getCurrentPosition()));
//            logData("Front Left", String.valueOf(motorFrontLeft.getCurrentPosition()));
//            logData("Back Right", String.valueOf(motorBackRight.getCurrentPosition()));
//            logData("Back Left", String.valueOf(motorBackLeft.getCurrentPosition()));
        }
    }

    protected void stopRobot() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void goForward(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorBackRight.setPower(powers[1]);
        motorFrontLeft.setPower(-powers[2]);
        motorBackLeft.setPower(-powers[3]);
    }

    protected void goBackward(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(-powers[0]);
        motorBackRight.setPower(-powers[1]);
        motorFrontLeft.setPower(powers[2]);
        motorBackLeft.setPower(powers[3]);
    }

    protected void goLeft(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorBackRight.setPower(-powers[1]);
        motorFrontLeft.setPower(powers[2]);
        motorBackLeft.setPower(-powers[3]);
    }

    protected void goDiagLeft(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontRight.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorBackLeft.setPower(-powers[1]);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
    }

    protected void goRight(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(-powers[0]);
        motorBackRight.setPower(powers[1]);
        motorFrontLeft.setPower(-powers[2]);
        motorBackLeft.setPower(powers[3]);
    }

    protected void goDiagRight(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontLeft.getCurrentPosition(), motorBackRight.getCurrentPosition());
        motorFrontLeft.setPower(-powers[0]);
        motorBackRight.setPower(powers[1]);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void spinRight(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(-powers[0]);
        motorBackRight.setPower(-powers[1]);
        motorFrontLeft.setPower(-powers[2]);
        motorBackLeft.setPower(-powers[3]);
    }

    protected void spinLeft(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorBackRight.setPower(powers[1]);
        motorFrontLeft.setPower(powers[2]);
        motorBackLeft.setPower(powers[3]);
    }

    protected void frontWheelsRight(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition());
        motorFrontRight.setPower(-powers[0]);
        motorFrontLeft.setPower(-powers[1]);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void frontWheelsLeft(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorFrontLeft.setPower(powers[1]);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void backWheelsRight(double power) {
        double[] powers = syncEncoders2Motors(power, motorBackRight.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorBackRight.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
    }

    protected void backWheelsLeft(double power) {
        double[] powers = syncEncoders2Motors(power, motorBackRight.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorBackRight.setPower(-powers[0]);
        motorBackLeft.setPower(-powers[1]);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
    }

    protected void leftWheelsForward(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(-powers[0]);
        motorBackLeft.setPower(-powers[1]);
    }

    protected void leftWheelsBackward(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(powers[0]);
        motorBackLeft.setPower(powers[1]);
    }

    protected void rightWheelsForward(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorBackRight.setPower(powers[1]);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void rightWheelsBackward(double power) {
        double[] powers = syncEncoders2Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition());
        motorFrontRight.setPower(-powers[0]);
        motorBackRight.setPower(-powers[1]);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }
//    protected void goPerfectlyStraight2Wheels() {
//        if (Math.abs(motorFrontRight.getCurrentPosition()) > Math.abs(motorFrontLeft.getCurrentPosition()) + 8) {
//            motorFrontRight.setPower(DEFAULT_POWER * 0.75);
//            motorFrontLeft.setPower(-DEFAULT_POWER);
//            logData("fixing", "right too fast");
//        } else if (Math.abs(motorFrontLeft.getCurrentPosition()) > Math.abs(motorFrontRight.getCurrentPosition()) + 8) {
//            motorFrontRight.setPower(DEFAULT_POWER);
//            motorFrontLeft.setPower(-DEFAULT_POWER * 0.75);
//            logData("fixing", "left too fast");
//        } else {
//            motorFrontRight.setPower(DEFAULT_POWER);
//            motorFrontLeft.setPower(-DEFAULT_POWER);
//            logData("fixing", "perfect");
//        }
//    }

    public double[] syncEncoders2Motors(double power, int encoder0, int encoder1) {
        double[] newPowers = new double[2];
        newPowers[0] = power;
        newPowers[1] = power;
        if (Math.abs(encoder0) > Math.abs(encoder1) + 8) {
            newPowers[0] = power * 0.75;
        } else if (Math.abs(encoder1) > Math.abs(encoder0) + 8) {
            newPowers[1] = power * 0.75;
        }
        return newPowers;
    }

    public double[] syncEncoders4Motors(double power, int encoder0, int encoder1, int encoder2, int encoder3) {
        HashMap<Integer, Integer> encoderIndexes = new HashMap<Integer, Integer>();
        encoderIndexes.put(Math.abs(encoder0), 0);
        encoderIndexes.put(Math.abs(encoder1), 1);
        encoderIndexes.put(Math.abs(encoder2), 2);
        encoderIndexes.put(Math.abs(encoder3), 3);
        int[] encoderValuesSorted = new int[]{Math.abs(encoder0), Math.abs(encoder1), Math.abs(encoder2), Math.abs(encoder3)};
        Arrays.sort(encoderValuesSorted);
        double[] newPowers = new double[4];
        newPowers[0] = power;
        newPowers[1] = power;
        newPowers[2] = power;
        newPowers[3] = power;
        if (encoderValuesSorted[3] > encoderValuesSorted[2] + 8) {
            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = power * 0.85;
        }
        if (encoderValuesSorted[2] > encoderValuesSorted[1] + 8) {
            newPowers[encoderIndexes.get(encoderValuesSorted[2])] = power * 0.85;
            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = newPowers[encoderIndexes.get(encoderValuesSorted[3])] * 0.85;
        }
        if (encoderValuesSorted[1] > encoderValuesSorted[0] + 8) {
            newPowers[encoderIndexes.get(encoderValuesSorted[1])] = power * 0.85;
            newPowers[encoderIndexes.get(encoderValuesSorted[2])] = newPowers[encoderIndexes.get(encoderValuesSorted[2])] * 0.85;
            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = newPowers[encoderIndexes.get(encoderValuesSorted[3])] * 0.85;
        }
        return newPowers;
    }

    private double getDistanceToGo(double inches, int encoderTicksSoFar) {
        int totalEncoderTicks = (int) (inches * ENCODER_TICKS_PER_INCH);
        if (encoderTicksSoFar < totalEncoderTicks) {
            double distanceToGo = (totalEncoderTicks - encoderTicksSoFar) / ENCODER_TICKS_PER_INCH;
            return distanceToGo;
        } else {
            return 0;
        }
    }

    private double getDistanceToGoDiag(double inches, int encoderTicksSoFar) {
        int totalEncoderTicks = (int) (inches * ENCODER_TICKS_PER_INCH_DIAG);
        if (encoderTicksSoFar < totalEncoderTicks) {
            double distanceToGo = (totalEncoderTicks - encoderTicksSoFar) / ENCODER_TICKS_PER_INCH_DIAG;
            return distanceToGo;
        } else {
            return 0;
        }
    }

    private double getDegreesToGo(double degrees, int encoderTicksSoFar) {
        int totalEncoderTicks = (int) (degrees * ENCODER_TICKS_PER_DEGREE);
        if (encoderTicksSoFar < totalEncoderTicks) {
            double degreesToGo = (totalEncoderTicks - encoderTicksSoFar) / ENCODER_TICKS_PER_DEGREE;
            return degreesToGo;
        } else {
            return 0;
        }
    }

    protected double goDistanceForward(double power, double inches) {
        goForward(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceRightWheelsForward(double power, double inches) {
        rightWheelsForward(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceRightWheelsBackward(double power, double inches) {
        rightWheelsBackward(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceLeftWheelsForward(double power, double inches) {
        leftWheelsForward(power);
        return getDistanceToGo(inches, motorFrontLeft.getCurrentPosition());
    }

    protected double goDistanceLeftWheelsBackward(double power, double inches) {
        leftWheelsBackward(power);
        return getDistanceToGo(inches, motorFrontLeft.getCurrentPosition());
    }

    protected double goDistanceFrontWheelsRight(double power, double inches) {
        frontWheelsRight(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceFrontWheelsLeft(double power, double inches) {
        frontWheelsLeft(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceBackWheelsRight(double power, double inches) {
        backWheelsRight(power);
        return getDistanceToGo(inches, motorBackRight.getCurrentPosition());
    }

    protected double goDistanceBackWheelsLeft(double power, double inches) {
        backWheelsLeft(power);
        return getDistanceToGo(inches, motorBackRight.getCurrentPosition());
    }

    protected double goDistanceBackward(double power, double inches) {
        goBackward(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceLeft(double power, double inches) {
        goLeft(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceDiagLeft(double power, double inches) {
        goDiagLeft(power);
        return getDistanceToGoDiag(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceRight(double power, double inches) {
        goRight(power);
        return getDistanceToGo(inches, motorFrontRight.getCurrentPosition());
    }

    protected double goDistanceDiagRight(double power, double inches) {
        goDiagRight(power);
        return getDistanceToGoDiag(inches, motorBackRight.getCurrentPosition());
    }

    protected double spinRightDegrees(double power, double degrees) {
        spinRight(power);
        return getDegreesToGo(degrees, motorFrontRight.getCurrentPosition());
    }

    protected double spinLeftDegrees(double power, double degrees) {
        spinLeft(power);
        return getDegreesToGo(degrees, motorFrontRight.getCurrentPosition());

    }
}