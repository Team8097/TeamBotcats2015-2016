package com.qualcomm.ftcrobotcontroller.team8097opmodes;

public class TestServosOpMode extends BaseOpMode {
    double pos1 = 0.5;
    double pos2 = 0.5;
    double pos3 = 0.5;
    double pos4 = 0.5;

    @Override
    public void init() {
        boxSpin = hardwareMap.servo.get("boxSpin");
        boxLift = hardwareMap.servo.get("boxLift");
        boxTilt = hardwareMap.servo.get("boxTilt");
        climberServo = hardwareMap.servo.get("climbers");
        rightFlapServo = hardwareMap.servo.get("rightFlap");
        leftFlapServo = hardwareMap.servo.get("leftFlap");
        rightHookServo = hardwareMap.servo.get("rightHook");
        leftHookServo = hardwareMap.servo.get("leftHook");
        climberServo.setPosition(climberServoInitPos);
        rightHookServo.setPosition(rightHookInitPos);
        leftHookServo.setPosition(leftHookInitPos);
        rightFlapServo.setPosition(rightFlapServoInitPos);
        leftFlapServo.setPosition(leftFlapServoInitPos);
//        boxSpin.setPosition(spinInitPos);
//        boxLift.setPosition(liftInitPos);
//        boxTilt.setPosition(tiltInitPos);
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
        boxLift.setPosition(pos1);
        logData("boxLift", String.valueOf(pos1));
        boxSpin.setPosition(pos3);
        logData("boxSpin", String.valueOf(pos3));
        boxTilt.setPosition(pos4);
        logData("boxTilt", String.valueOf(pos4));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
