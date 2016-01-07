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

//Base class for autonomous. It is used for testing movement autonomously,
//and includes methods for moving a certain distance or spinning a certain angle.
public class AutonomousOpMode extends BaseOpMode {
    final int initialLoops = 10;
    int loop = 1;
    long startTime;


    @Override
    public void init() {
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");

//        frontOds = hardwareMap.opticalDistanceSensor.get("frontOds");
        frontLeftUltra = hardwareMap.ultrasonicSensor.get("frontLeftUltra");
//        rightUltra = hardwareMap.ultrasonicSensor.get("rightUltra");
        frontRightUltra = hardwareMap.ultrasonicSensor.get("frontRightUltra");
//        leftUltra = hardwareMap.ultrasonicSensor.get("leftUltra");
        rightServo = hardwareMap.servo.get("rightServo");
        leftServo = hardwareMap.servo.get("leftServo");
        armServo = hardwareMap.servo.get("armServo");
        rightSweepServo = hardwareMap.servo.get("rightSweep");
        leftSweepServo = hardwareMap.servo.get("leftSweep");
        frontLightSensor = hardwareMap.lightSensor.get("frontLight");
        backLightSensor = hardwareMap.lightSensor.get("backLight");
        rightColorSensor = hardwareMap.lightSensor.get("rightColor");
        leftColorSensor = hardwareMap.lightSensor.get("leftColor");
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startTime = System.currentTimeMillis();
        } else {
            goLeft(DEFAULT_POWER);
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

    protected double goDistanceLeftWheelsForward(double power, double inches, long startTime) {
        leftWheelsForward(power);
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
