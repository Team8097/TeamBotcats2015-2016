package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// This is the base class for all our opmodes,
// and includes methods for basic movement and variables for all sensors, motors, etc.
public abstract class BaseOpMode extends OpMode {
    public final static double DEFAULT_POWER = 0.2;
    public final static double MILLIS_PER_INCH_DEFAULT = 37.736 * (0.25 / DEFAULT_POWER);
    public final static double MILLIS_PER_DEGREE_DEFAULT = 5 * (0.25 / DEFAULT_POWER);
    public final static double INCHES_PER_CENT = 0.393701;
    public final static int LEFT_ULTRA_PERFECT_DIST = 21;
    public final static int RIGHT_ULTRA_PERFECT_DIST = 21;
    public final static double BLUE_THRESHOLD = 0.6;

    double rightSweepTriangle = 0.524;
    double leftSweepTriangle = 0.534;
    double rightSweepIn = 0.832;
    double leftSweepIn = 0.206;
    double rightSweepOut = 0.038;
    double leftSweepOut = 0.992;

    DcMotor motorSpinny;
    DcMotor motorExtend;
    DcMotor motorMoveArm;
    DcMotor motorCollection;

    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;
    //    Servo swiper;
    Servo rightFlapServo;
    Servo leftFlapServo;
    Servo rightSweepServo;
    Servo leftSweepServo;
    Servo climberServo;
    Servo rightHookServo;
    Servo leftHookServo;

    LightSensor rightColorSensor;
    LightSensor leftColorSensor;

    LightSensor frontLightSensor;
    LightSensor backLightSensor;
    //    OpticalDistanceSensor frontOds;
    UltrasonicSensor frontLeftUltra;
    //    UltrasonicSensor rightUltra;
    UltrasonicSensor frontRightUltra;
//    UltrasonicSensor leftUltra;

//    TouchSensor rightBumpSensor;
//    TouchSensor leftBumpSensor;

    final double leftFlapServoInitPos = 0.414;
    final double leftFlapServoFinalPos = 0.282;
    final double rightFlapServoInitPos = 0.414;
    final double rightFlapServoFinalPos = 0.588;
    final double climberServoInitPos = 1;
    final double climberServoFinalPos = 0;
    final double rightHookInitPos = 0;
    final double rightHookFinalPos = 1;
    final double leftHookInitPos = 1;
    final double leftHookFinalPos = 0;

    private HashMap<String, String> telemetryData = new HashMap<String, String>();

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
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
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
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
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
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void frontWheelsLeft(double power) {
        motorFrontRight.setPower(power);
        motorFrontLeft.setPower(power);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void backWheelsRight(double power) {
        motorBackRight.setPower(power);
        motorBackLeft.setPower(power);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
    }

    protected void backWheelsLeft(double power) {
        motorBackRight.setPower(-power);
        motorBackLeft.setPower(-power);
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
    }

    protected void leftWheelsForward(double power) {
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(-power);
        motorBackLeft.setPower(-power);
    }

    protected void leftWheelsBackward(double power) {
        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(power);
        motorBackLeft.setPower(power);
    }

    protected void rightWheelsForward(double power) {
        motorFrontRight.setPower(power);
        motorBackRight.setPower(power);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void rightWheelsBackward(double power) {
        motorFrontRight.setPower(-power);
        motorBackRight.setPower(-power);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
    }

    protected void logData(String label, String value) {
        telemetry.addData(label, value);
        telemetryData.put(label, value);
        String data = "";
        for (String key : telemetryData.keySet()) {
            data += key + ": " + telemetryData.get(key) + "" + "\n";
        }
        FtcRobotControllerActivity.logData.obtainMessage(0, data).sendToTarget();
    }

    private void allInit() {
        motorFrontLeft = hardwareMap.dcMotor.get("0motor1");
        motorFrontRight = hardwareMap.dcMotor.get("0motor2");
        motorBackRight = hardwareMap.dcMotor.get("1motor1");
        motorBackLeft = hardwareMap.dcMotor.get("1motor2");
        frontRightUltra = hardwareMap.ultrasonicSensor.get("2ultra4");
        frontLeftUltra = hardwareMap.ultrasonicSensor.get("2ultra5");
        frontLightSensor = hardwareMap.lightSensor.get("2light3");
        backLightSensor = hardwareMap.lightSensor.get("2light2");
        rightColorSensor = hardwareMap.lightSensor.get("3light3");
        leftColorSensor = hardwareMap.lightSensor.get("3light1");
        motorSpinny = hardwareMap.dcMotor.get("3hitech5motor2");
        motorMoveArm = hardwareMap.dcMotor.get("3hitech5motor1");
        motorExtend = hardwareMap.dcMotor.get("3hitech0motor1");
        motorCollection = hardwareMap.dcMotor.get("3hitech0motor2");
        climberServo = hardwareMap.servo.get("4servo1");
        rightFlapServo = hardwareMap.servo.get("4servo2");
        leftFlapServo = hardwareMap.servo.get("4servo3");
        rightSweepServo = hardwareMap.servo.get("4servo4");
        leftSweepServo = hardwareMap.servo.get("4servo5");
    }
}
