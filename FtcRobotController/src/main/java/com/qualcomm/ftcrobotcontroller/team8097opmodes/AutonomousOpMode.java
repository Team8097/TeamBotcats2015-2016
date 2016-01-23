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
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;

import java.util.ArrayList;
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
        motorFrontLeft = hardwareMap.dcMotor.get("0motor1");
        motorFrontRight = hardwareMap.dcMotor.get("0motor2");
        motorBackRight = hardwareMap.dcMotor.get("1motor1");
        motorBackLeft = hardwareMap.dcMotor.get("1motor2");
        frontRightUltra = hardwareMap.ultrasonicSensor.get("2ultra4");
        frontLeftUltra = hardwareMap.ultrasonicSensor.get("2ultra5");
        frontLightSensor = hardwareMap.lightSensor.get("2light3");
        backLightSensor = hardwareMap.lightSensor.get("2light2");
        rightColorSensor = hardwareMap.lightSensor.get("3light3");
        leftColorSensor = hardwareMap.lightSensor.get("3light1");
//        motorSpinny = hardwareMap.dcMotor.get("3hitech5motor2");
        climberServo = hardwareMap.servo.get("4servo1");
        rightFlapServo = hardwareMap.servo.get("4servo2");
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

    protected void goPerfectlyStraight2Wheels() {
        if (Math.abs(motorFrontRight.getCurrentPosition()) > Math.abs(motorFrontLeft.getCurrentPosition()) + 8) {
            motorFrontRight.setPower(DEFAULT_POWER * 0.75);
            motorFrontLeft.setPower(-DEFAULT_POWER);
            logData("fixing", "right too fast");
        } else if (Math.abs(motorFrontLeft.getCurrentPosition()) > Math.abs(motorFrontRight.getCurrentPosition()) + 8) {
            motorFrontRight.setPower(DEFAULT_POWER);
            motorFrontLeft.setPower(-DEFAULT_POWER * 0.75);
            logData("fixing", "left too fast");
        } else {
            motorFrontRight.setPower(DEFAULT_POWER);
            motorFrontLeft.setPower(-DEFAULT_POWER);
            logData("fixing", "perfect");
        }
    }

    protected double goDistanceForward(double power, double inches, long startTime) {
        goForward(power);
        return getDistanceToGo(power, inches, startTime);
    }

    private double getDistanceToGo(double power, double inches, long startTime) {
        double millisPerInch = MILLIS_PER_INCH_DEFAULT * (DEFAULT_POWER / power);
        double goTime = inches * millisPerInch;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        if (timeElapsed < goTime) {
            double distanceToGo = (goTime - timeElapsed) / millisPerInch;
            return distanceToGo;
        } else {
            return 0;
        }
    }

    protected double goDistanceRightWheelsForward(double power, double inches, long startTime) {
        rightWheelsForward(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceRightWheelsBackward(double power, double inches, long startTime) {
        rightWheelsBackward(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceLeftWheelsForward(double power, double inches, long startTime) {
        leftWheelsForward(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceLeftWheelsBackward(double power, double inches, long startTime) {
        leftWheelsBackward(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceFrontWheelsRight(double power, double inches, long startTime) {
        frontWheelsRight(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceFrontWheelsLeft(double power, double inches, long startTime) {
        frontWheelsLeft(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceBackWheelsRight(double power, double inches, long startTime) {
        backWheelsRight(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceBackWheelsLeft(double power, double inches, long startTime) {
        backWheelsLeft(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceBackward(double power, double inches, long startTime) {
        goBackward(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceLeft(double power, double inches, long startTime) {
        goLeft(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceDiagLeft(double power, double inches, long startTime) {
        double millisPerInch = MILLIS_PER_INCH_DEFAULT * (DEFAULT_POWER / power);
        double goTime = inches * millisPerInch;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        goDiagLeft(power);
        if (timeElapsed < goTime) {
            double distanceToGo = (goTime - timeElapsed) / millisPerInch;
            return distanceToGo;
        } else {
            return 0;
        }
    }

    protected double goDistanceRight(double power, double inches, long startTime) {
        goRight(power);
        return getDistanceToGo(power, inches, startTime);
    }

    protected double goDistanceDiagRight(double power, double inches, long startTime) {
        double millisPerInch = MILLIS_PER_INCH_DEFAULT * (DEFAULT_POWER / power);
        double goTime = inches * millisPerInch;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        goDiagRight(power);
        if (timeElapsed < goTime) {
            double distanceToGo = (goTime - timeElapsed) / millisPerInch;
            return distanceToGo;
        } else {
            return 0;
        }
    }

    protected double spinRightDegrees(double power, double degrees, long startTime) {
        double millisPerDegree = MILLIS_PER_DEGREE_DEFAULT * (DEFAULT_POWER / power);
        double goTime = degrees * millisPerDegree;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        if (timeElapsed < goTime) {
            spinRight(power);
            double degreesToGo = (goTime - timeElapsed) / millisPerDegree;
            return degreesToGo;
        } else {
            stopRobot();
            return 0;
        }
    }

    protected double spinLeftDegrees(double power, double degrees, long startTime) {
        double millisPerDegree = MILLIS_PER_DEGREE_DEFAULT * (DEFAULT_POWER / power);
        double goTime = degrees * millisPerDegree;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        if (timeElapsed < goTime) {
            spinLeft(power);
            double degreesToGo = (goTime - timeElapsed) / millisPerDegree;
            return degreesToGo;
        } else {
            stopRobot();
            return 0;
        }
    }
}
