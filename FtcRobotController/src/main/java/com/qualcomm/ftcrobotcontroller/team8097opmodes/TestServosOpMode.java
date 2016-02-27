package com.qualcomm.ftcrobotcontroller.team8097opmodes;

public class TestServosOpMode extends BaseOpMode {
    double pos1 = 0.5;
    double pos2 = 0.5;
    double pos3 = 0.5;
    double pos4 = 0.5;

    @Override
    public void init() {
//        climberServo = hardwareMap.servo.get("climbers");
//        rightFlapServo = hardwareMap.servo.get("rightButton");
//        leftFlapServo = hardwareMap.servo.get("leftButton");
//        rightHookServo = hardwareMap.servo.get("rightHook");
//        leftHookServo = hardwareMap.servo.get("leftHook");
//        rightSweepServo = hardwareMap.servo.get("rightSweep");
//        leftSweepServo = hardwareMap.servo.get("leftSweep");
//        armLatchServo = hardwareMap.servo.get("armLatch");
//        climberServo.setPosition(climberServoInitPos);
//        rightSweepServo.setPosition(rightSweepIn);
//        leftSweepServo.setPosition(leftSweepIn);
//        rightHookServo.setPosition(rightHookUpPos);
//        leftHookServo.setPosition(leftHookUpPos);
//        rightFlapServo.setPosition(rightFlapServoFinalPos);
//        leftFlapServo.setPosition(leftFlapServoFinalPos);
//        armLatchServo.setPosition(armLatchInitPos);
        boxSpin = hardwareMap.servo.get("armSpin1");
        boxLift = hardwareMap.servo.get("boxLift");
        boxTilt = hardwareMap.servo.get("boxTilt");
    }

    @Override
    public void loop() {
        if (gamepad1.y) {
            if (pos1 + 0.002 <= 1)
                pos1 += 0.002;
        } else if (gamepad1.a) {
            if (pos1 - 0.002 >= 0)
                pos1 -= 0.002;
        }
        if (gamepad1.b) {
            if (pos2 + 0.002 <= 1)
                pos2 += 0.002;
        } else if (gamepad1.x) {
            if (pos2 - 0.002 >= 0)
                pos2 -= 0.002;
        }
        if (gamepad1.dpad_up) {
            if (pos3 + 0.002 <= 1)
                pos3 += 0.002;
        } else if (gamepad1.dpad_down) {
            if (pos3 - 0.002 >= 0)
                pos3 -= 0.002;
        }
        if (gamepad1.dpad_right) {
            if (pos4 + 0.002 <= 1)
                pos4 += 0.002;
        } else if (gamepad1.dpad_left) {
            if (pos4 - 0.002 >= 0)
                pos4 -= 0.002;
        }
        boxSpin.setPosition(pos1);
        logData("boxSpin", String.valueOf(pos1));
        boxLift.setPosition(pos3);
        logData("boxLift", String.valueOf(pos3));
        boxTilt.setPosition(pos4);
        logData("boxTilt", String.valueOf(pos4));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
