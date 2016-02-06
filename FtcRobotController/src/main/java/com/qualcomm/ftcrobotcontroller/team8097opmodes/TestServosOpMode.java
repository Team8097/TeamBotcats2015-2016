package com.qualcomm.ftcrobotcontroller.team8097opmodes;

public class TestServosOpMode extends BaseOpMode {
    double pos1 = 0.5;
    double pos2 = 0.5;

    @Override
    public void init() {
        climberServo = hardwareMap.servo.get("climbers");
        rightFlapServo = hardwareMap.servo.get("rightButton");
        leftFlapServo = hardwareMap.servo.get("leftButton");
        rightHookServo = hardwareMap.servo.get("rightHook");
        leftHookServo = hardwareMap.servo.get("leftHook");
        rightSweepServo = hardwareMap.servo.get("rightSweep");
        leftSweepServo = hardwareMap.servo.get("leftSweep");
        armLatchServo = hardwareMap.servo.get("armLatch");
        climberServo.setPosition(climberServoInitPos);
        rightSweepServo.setPosition(rightSweepIn);
        leftSweepServo.setPosition(leftSweepIn);
        rightHookServo.setPosition(rightHookUpPos);
        leftHookServo.setPosition(leftHookUpPos);
        rightFlapServo.setPosition(rightFlapServoFinalPos);
        leftFlapServo.setPosition(leftFlapServoFinalPos);
        armLatchServo.setPosition(armLatchInitPos);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            if (pos1 + 0.002 <= 1)
                pos1 += 0.002;
        } else if (gamepad1.b) {
            if (pos2 + 0.002 <= 1)
                pos2 += 0.002;
        } else if (gamepad1.x) {
            if (pos1 - 0.002 >= 0)
                pos1 -= 0.002;
        } else if (gamepad1.y) {
            if (pos2 - 0.002 >= 0)
                pos2 -= 0.002;
        }
        rightHookServo.setPosition(pos1);
        logData("rightPos", String.valueOf(pos1));
        leftHookServo.setPosition(pos2);
        logData("leftPos", String.valueOf(pos2));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
