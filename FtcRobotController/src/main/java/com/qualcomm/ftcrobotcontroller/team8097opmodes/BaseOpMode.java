package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

// This is the base class for all our opmodes,
// and includes methods for basic movement and variables for all sensors, motors, etc.
public abstract class BaseOpMode extends OpMode {
    public final static double DEFAULT_POWER = 0.2;
    public final static double MILLIS_PER_INCH_DEFAULT = 37.736 * (0.25 / DEFAULT_POWER);
    public final static double MILLIS_PER_DEGREE_DEFAULT = 5 * (0.25 / DEFAULT_POWER);
    public final static double BLUE_THRESHOLD = 0.6;
    public final static double TAPE_THRESHOLD = 0.43;

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    Servo rightServo;
    Servo leftServo;
    Servo rightSweepServo;
    Servo leftSweepServo;
    Servo armServo;
    LightSensor colorLightSensor;
    ColorSensor colorSensor;

    LightSensor frontLightSensor;
    LightSensor backLightSensor;
    OpticalDistanceSensor frontOds;
    UltrasonicSensor frontUltra;
    UltrasonicSensor rightUltra;
    UltrasonicSensor backUltra;
    UltrasonicSensor leftUltra;

    final double leftServoInitPos = 0.35;
    final double leftServoFinalPos = 0.5;
    final double rightServoInitPos = 0.5;
    final double rightServoFinalPos = 0.35;
    final double armServoInitPos = 1;
    final double armServoFinalPos = 0;

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
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(-power);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);
    }

    protected void goLeft(double power) {
        motorFrontRight.setPower(power);
        motorBackRight.setPower(-power);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(-power);
    }

    protected void goDiagLeft(double power) {
        motorFrontRight.setPower(power);
        motorBackLeft.setPower(-power);
    }

    protected void goRight(double power) {
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(power);
    }

    protected void goDiagRight(double power) {
        motorFrontLeft.setPower(-power);
        motorBackRight.setPower(power);
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

    protected void frontWheelsRight(double power) {
        motorFrontRight.setPower(-power);
        motorFrontLeft.setPower(-power);
    }

    protected void frontWheelsLeft(double power) {
        motorFrontRight.setPower(power);
        motorFrontLeft.setPower(power);
    }

    protected void backWheelsRight(double power) {
        motorBackRight.setPower(power);
        motorBackLeft.setPower(power);
    }

    protected void backWheelsLeft(double power) {
        motorBackRight.setPower(-power);
        motorBackLeft.setPower(-power);
    }
}
