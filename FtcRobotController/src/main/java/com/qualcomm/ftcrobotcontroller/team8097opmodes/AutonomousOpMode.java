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

public class AutonomousOpMode extends BaseOpMode {

    boolean onMountain = false;
    int fullTurnTime = 2400;//0.25 power
    int halfMeterTime = 0;//0.25 power
    final int initialLoops = 1;
    int loop = 1;
    long startTime;
    final static double BLUE = 0;
    final static double RED = 0;
    final static double defaultPower = 0.25;

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
//        touchSensor = hardwareMap.touchSensor.get("touch");
//        distanceSensor = hardwareMap.opticalDistanceSensor.get("distance");
//        colorSensor = hardwareMap.colorSensor.get("color");
//        lightSensor = hardwareMap.lightSensor.get("light");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        frontUltra = hardwareMap.ultrasonicSensor.get("frontUltra");
        armServo = hardwareMap.servo.get("arm");
//        rightUltra = hardwareMap.ultrasonicSensor.get("rightUltra");
//        backUltra = hardwareMap.ultrasonicSensor.get("backUltra");
//        leftUltra = hardwareMap.ultrasonicSensor.get("leftUltra");
    }

    @Override
    public void loop() {
        if (loop <= initialLoops) {
            loop++;
            startTime = System.currentTimeMillis();
        } else {
//        telemetry.addData("touch", touchSensor.isPressed());
//        telemetry.addData("frontUltra", frontUltra.getUltrasonicLevel());
//        telemetry.addData("rightUltra", rightUltra.getUltrasonicLevel());
//        telemetry.addData("backUltra", backUltra.getUltrasonicLevel());
//        telemetry.addData("leftUltra", leftUltra.getUltrasonicLevel());
//        telemetry.addData("light", lightSensor.getLightDetected());
//        telemetry.addData("blue", colorSensor.blue());
//        telemetry.addData("red", colorSensor.red());
//        if (System.currentTimeMillis() - startMoveTime < 2400) {
//            motorFrontRight.setPower(0.25);
//            motorBackRight.setPower(0.25);
//            motorFrontLeft.setPower(0.25);
//            motorBackLeft.setPower(0.25);
//        } else{
//            stopRobot();
//        }
            telemetry.addData("frontUltra", frontUltra.getUltrasonicLevel());
            if (System.currentTimeMillis() - startTime < 1500) {
                goForward(0.25);
            } else {
                stopRobot();
            }
        }
    }

    protected double goDistance(double power, double inches, long startTime) {
        double goTime = inches * MILLIS_PER_INCH;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        if (timeElapsed < goTime) {
            goForward(power);
            double distanceToGo = (goTime - timeElapsed) / MILLIS_PER_INCH;
            return distanceToGo;
        } else {
            stopRobot();
            return 0;
        }
    }

    protected double spinRightDegrees(double power, double degrees, long startTime) {
        double goTime = degrees * MILLIS_PER_DEGREE;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        if (timeElapsed < goTime) {
            spinRight(power);
            double degreesToGo = (goTime - timeElapsed) / MILLIS_PER_DEGREE;
            return degreesToGo;
        } else {
            stopRobot();
            return 0;
        }
    }

    protected double spinLeftDegrees(double power, double degrees, long startTime) {
        double goTime = degrees * MILLIS_PER_DEGREE;
        int timeElapsed = (int) (System.currentTimeMillis() - startTime);
        if (timeElapsed < goTime) {
            spinLeft(power);
            double degreesToGo = (goTime - timeElapsed) / MILLIS_PER_DEGREE;
            return degreesToGo;
        } else {
            stopRobot();
            return 0;
        }
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
