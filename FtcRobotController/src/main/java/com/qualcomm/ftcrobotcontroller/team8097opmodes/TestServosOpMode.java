package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import android.os.SystemClock;

public class TestServosOpMode extends BaseOpMode {
    double rightSweepPos = 0.5;
    double leftSweepPos = 0.5;

    @Override
    public void init() {
//        rightServo = hardwareMap.servo.get("rightServo");
//        leftServo = hardwareMap.servo.get("leftServo");
//        armServo = hardwareMap.servo.get("armServo");
        rightSweepServo = hardwareMap.servo.get("rightSweep");
        leftSweepServo = hardwareMap.servo.get("leftSweep");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            rightSweepPos += 0.002;
        } else if (gamepad1.b) {
            leftSweepPos += 0.002;
        } else if (gamepad1.x) {
            rightSweepPos -= 0.002;
        } else if (gamepad1.y) {
            leftSweepPos -= 0.002;
        }
        rightSweepServo.setPosition(rightSweepPos);
        leftSweepServo.setPosition(leftSweepPos);
        telemetry.addData("rightSweepPos", rightSweepPos);
        telemetry.addData("leftSweepPos", leftSweepPos);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
