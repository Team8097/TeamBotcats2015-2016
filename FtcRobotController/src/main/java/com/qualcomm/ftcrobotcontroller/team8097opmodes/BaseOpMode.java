package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import android.view.MotionEvent;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
//import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
//import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// This is the base class for all our opmodes,
// and includes methods for basic movement and variables for all sensors, motors, etc.
public abstract class BaseOpMode extends OpMode {
    public final static double DEFAULT_POWER = 0.2;
    //    public final static double MILLIS_PER_INCH_DEFAULT = 37.736 * (0.25 / DEFAULT_POWER);
//    public final static double MILLIS_PER_DEGREE_DEFAULT = 5 * (0.25 / DEFAULT_POWER);
    public final static double WHEEL_DIAMETER = 4;
    public final static double ROBOT_DIAMETER = 18;
    public final static double ENCODER_TICKS_PER_INCH = (1440 / (WHEEL_DIAMETER * Math.PI)) * (Math.sqrt(2) / 2);
    public final static double ENCODER_TICKS_PER_INCH_DIAG = 1440 / (WHEEL_DIAMETER * Math.PI);
    public final static double ENCODER_TICKS_PER_DEGREE = ((1440 / (WHEEL_DIAMETER * Math.PI)) * (ROBOT_DIAMETER / WHEEL_DIAMETER)) / 360;
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
    Servo rightHookServo;
    Servo leftHookServo;
    Servo rightSweepServo;
    Servo leftSweepServo;
    Servo climberServo;

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

    protected void resetEncoders() {
        while (motorFrontRight.getCurrentPosition() != 0)
            motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        while (motorBackRight.getCurrentPosition() != 0)
            motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        while (motorFrontLeft.getCurrentPosition() != 0)
            motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        while (motorBackLeft.getCurrentPosition() != 0)
            motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    public double[] syncEncoders2Motors(double power, int encoder0, int encoder1) {
        double[] newPowers = new double[2];
        newPowers[0] = power;
        newPowers[1] = power;
        if (Math.abs(encoder0) > Math.abs(encoder1) + 8) {
            newPowers[0] = power * 0.75;
        } else if (Math.abs(encoder1) > Math.abs(encoder0) + 8) {
            newPowers[1] = power * 0.75;
        }
        return newPowers;
    }

    public double[] syncEncoders4Motors(double power, int encoder0, int encoder1, int encoder2, int encoder3) {
        HashMap<Integer, Integer> encoderIndexes = new HashMap<Integer, Integer>();
        encoderIndexes.put(Math.abs(encoder0), 0);
        encoderIndexes.put(Math.abs(encoder1), 1);
        encoderIndexes.put(Math.abs(encoder2), 2);
        encoderIndexes.put(Math.abs(encoder3), 3);
        int[] encoderValuesSorted = new int[]{Math.abs(encoder0), Math.abs(encoder1), Math.abs(encoder2), Math.abs(encoder3)};
        Arrays.sort(encoderValuesSorted);
        double[] newPowers = new double[4];
        newPowers[0] = power;
        newPowers[1] = power;
        newPowers[2] = power;
        newPowers[3] = power;
        if (encoderValuesSorted[3] > encoderValuesSorted[2] + 8) {
            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = power * 0.85;
        }
        if (encoderValuesSorted[2] > encoderValuesSorted[1] + 8) {
            newPowers[encoderIndexes.get(encoderValuesSorted[2])] = power * 0.85;
            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = newPowers[encoderIndexes.get(encoderValuesSorted[3])] * 0.85;
        }
        if (encoderValuesSorted[1] > encoderValuesSorted[0] + 8) {
            newPowers[encoderIndexes.get(encoderValuesSorted[1])] = power * 0.85;
            newPowers[encoderIndexes.get(encoderValuesSorted[2])] = newPowers[encoderIndexes.get(encoderValuesSorted[2])] * 0.85;
            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = newPowers[encoderIndexes.get(encoderValuesSorted[3])] * 0.85;
        }
        return newPowers;
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

    protected void logData(String label, String value) {
        telemetry.addData(label, value);
        telemetryData.put(label, value);
        String data = "";
        for (String key : telemetryData.keySet()) {
            data += key + ": " + telemetryData.get(key) + "" + "\n";
        }
        FtcRobotControllerActivity.logData.obtainMessage(0, data).sendToTarget();
    }

    protected void spinRight(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(-powers[0]);
        motorBackRight.setPower(-powers[1]);
        motorFrontLeft.setPower(-powers[2]);
        motorBackLeft.setPower(-powers[3]);
    }

    protected void spinLeft(double power) {
        double[] powers = syncEncoders4Motors(power, motorFrontRight.getCurrentPosition(), motorBackRight.getCurrentPosition(), motorFrontLeft.getCurrentPosition(), motorBackLeft.getCurrentPosition());
        motorFrontRight.setPower(powers[0]);
        motorBackRight.setPower(powers[1]);
        motorFrontLeft.setPower(powers[2]);
        motorBackLeft.setPower(powers[3]);
    }

    private void allInit() {
        motorFrontLeft = hardwareMap.dcMotor.get("1motor2");
        motorFrontRight = hardwareMap.dcMotor.get("1motor1");
        motorBackRight = hardwareMap.dcMotor.get("0motor2");
        motorBackLeft = hardwareMap.dcMotor.get("0motor1");
        frontRightUltra = hardwareMap.ultrasonicSensor.get("3ultra4");
        frontLeftUltra = hardwareMap.ultrasonicSensor.get("3ultra5");
        frontLightSensor = hardwareMap.lightSensor.get("3light1");
        backLightSensor = hardwareMap.lightSensor.get("3light0");
        rightColorSensor = hardwareMap.lightSensor.get("2light1");
        leftColorSensor = hardwareMap.lightSensor.get("2light2");
        motorSpinny = hardwareMap.dcMotor.get("2hitech5motor2");
        motorMoveArm = hardwareMap.dcMotor.get("2hitech5motor1");
        motorExtend = hardwareMap.dcMotor.get("2hitech0motor1");
        motorCollection = hardwareMap.dcMotor.get("2hitech0motor2");
        climberServo = hardwareMap.servo.get("4servo2");
        rightFlapServo = hardwareMap.servo.get("4servo1");
        leftFlapServo = hardwareMap.servo.get("4servo3");
//        rightHookServo = hardwareMap.servo.get("4servo5");
//        leftHookServo = hardwareMap.servo.get("4servo4");
    }
}
