package com.qualcomm.ftcrobotcontroller.team8097opmodes;

public class TestServosOpMode extends BaseOpMode {
    double sweepPos1 = 0.5;
    double sweepPos2 = 0.5;

    @Override
    public void init() {
//        rightServo = hardwareMap.servo.get("rightServo");
//        leftServo = hardwareMap.servo.get("leftServo");
//        armServo = hardwareMap.servo.get("armServo");
//        rightServo = hardwareMap.servo.get("rightServo");
//        leftServo = hardwareMap.servo.get("leftServo");
        swiper = hardwareMap.servo.get("swiper");
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
        swiper.setPosition(sweepPos1);
//        leftServo.setPosition(sweepPos2);
        telemetry.addData("swipePos", sweepPos1);
//        telemetry.addData("leftPos", sweepPos2);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
