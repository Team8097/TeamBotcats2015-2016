package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import android.os.SystemClock;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public abstract class BaseOpMode extends OpMode {

    public final static double MILLIS_PER_INCH_DEFAULT = 50;
    public final static double MILLIS_PER_DEGREE_DEFAULT = 5;
    public final static double DEFAULT_POWER = 0.25;
    public final static double RED_THRESHOLD = 0.5;
    public final static double TAPE_THRESHOLD = 0.5;

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    Servo rightServo;
    Servo leftServo;
    Servo armServo;
    LightSensor colorLightSensor;
    LightSensor frontLightSensor;
    LightSensor backLightSensor;
    UltrasonicSensor frontUltra;
    UltrasonicSensor rightUltra;
    UltrasonicSensor backUltra;
    UltrasonicSensor leftUltra;


    protected void go(double leftPower, double rightPower) {
        motorFrontRight.setPower(rightPower);
        motorBackRight.setPower(rightPower);
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
    }

    protected void stopRobot() {
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
        SystemClock.sleep(100);
    }

    protected boolean turnMotorToPosition(DcMotor motor, int targetPosition, double power) {
        boolean turning = true;
        if (targetPosition - motor.getCurrentPosition() > 0) {
            motor.setDirection(DcMotor.Direction.FORWARD);
            if (motor.getCurrentPosition() >= targetPosition - 3) {
                motor.setPower(0);
                turning = false;
                telemetry.addData("Position Error", motor.getCurrentPosition() - targetPosition);
            } else {
                motor.setPower(power);
            }
        } else {
            motor.setDirection(DcMotor.Direction.REVERSE);
            if (motor.getCurrentPosition() <= targetPosition + 3) {
                motor.setPower(0);
                turning = false;
                telemetry.addData("Position Error", motor.getCurrentPosition() - targetPosition);
            } else {
                motor.setPower(power);
            }
        }
        return turning;
    }

    protected void goForward(double power) {
        motorFrontRight.setPower(power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(-power);
    }

    protected void goBackward(double power) {
        motorFrontRight.setPower(power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(-power);
    }

    protected void goLeft(double power) {
        motorFrontRight.setPower(power);
        motorBackRight.setPower(-power);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(-power);
    }

    protected void goRight(double power) {
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(power);
    }

    protected void spinRight(double power) {
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(-power);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(-power);
    }

    protected void spinLeft(double power) {
        motorFrontRight.setPower(power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);
    }
}
