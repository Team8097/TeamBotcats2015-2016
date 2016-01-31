package com.qualcomm.ftcrobotcontroller.team8097opmodes;

public class TestServosOpMode extends BaseOpMode {
    double sweepPos1 = 0.5;
    double sweepPos2 = 0.5;

    @Override
    public void init() {
//        rightHookServo = hardwareMap.servo.get("4servo5");
//        leftHookServo = hardwareMap.servo.get("4servo4");
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            sweepPos1 += 0.002;
        } else if (gamepad1.b) {
            sweepPos2 += 0.002;
        } else if (gamepad1.x) {
            sweepPos1 -= 0.002;
        } else if (gamepad1.y) {
            sweepPos2 -= 0.002;
        }
//        rightHookServo.setPosition(sweepPos1);
        telemetry.addData("rightPos", sweepPos1);
//        leftHookServo.setPosition(sweepPos2);
        telemetry.addData("leftPos", sweepPos2);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
