package com.qualcomm.ftcrobotcontroller.team8097opmodes;

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

import java.util.Arrays;
import java.util.HashMap;

// This is the base class for all our opmodes,
// and includes methods for basic movement and variables for all sensors, motors, etc.
public abstract class BaseOpMode extends OpMode {
    public final static double DEFAULT_POWER = 0.2;
    public final static double MILLIS_PER_INCH_DEFAULT = 37.736 * (0.25 / DEFAULT_POWER);
    //    public final static double MILLIS_PER_DEGREE_DEFAULT = 5 * (0.25 / DEFAULT_POWER);
    public final static double WHEEL_DIAMETER = 4;
    public final static double ROBOT_DIAMETER = 18;
    public final static double ENCODER_TICKS_PER_INCH = (1440 / (WHEEL_DIAMETER * Math.PI)) * (Math.sqrt(2) / 2);
    public final static double ENCODER_TICKS_PER_INCH_DIAG = 1440 / (WHEEL_DIAMETER * Math.PI);
    public final static double ENCODER_TICKS_PER_DEGREE = ((1440 / (WHEEL_DIAMETER * Math.PI)) * (ROBOT_DIAMETER / WHEEL_DIAMETER)) / 360;
    public final static double INCHES_PER_CENT = 0.393701;
    public final static int LEFT_ULTRA_PERFECT_DIST = 19;
    public final static int RIGHT_ULTRA_PERFECT_DIST = 19;
    public final static double BLUE_THRESHOLD = 0.6;

//    double rightSweepTriangle = 0.524;
//    double leftSweepTriangle = 0.534;
//    double rightSweepIn = 0.832;
//    double leftSweepIn = 0.206;
//    double rightSweepOut = 0.038;
//    double leftSweepOut = 0.992;

    //    DcMotor motorSpinny;
    DcMotor motorExtend;
    //    DcMotor motorLiftArm;
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
    //    Servo rightSweepServo;
//    Servo leftSweepServo;
    Servo climberServo;
    //    Servo armLatchServo;
    Servo boxSpin;
    Servo boxLift;
    Servo boxTilt;

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

    final double leftFlapServoInitPos = 0;
    final double leftFlapServoFinalPos = 0;
    final double rightFlapServoInitPos = 0;
    final double rightFlapServoFinalPos = 0;
    final double climberServoInitPos = 0.0;
    final double climberServoFinalPos = 1.0;
    final double rightHookInitPos = 0;
    final double rightHookDownPos = 0;
    final double leftHookInitPos = 0;
    final double leftHookDownPos = 0;

    final double tiltInitPos = 0;
    final double tiltLeftPos = 0;
    final double tiltRightPos = 0;
    final double spinInitPos = 0;
    final double spinRightPos = 0;
    final double spinLeftPos = 0;
    final double liftInitPos = 0;
    final double liftUpPos = 0;


//    final double armLatchInitPos = 0.894;
//    final double armLatchFinalPos = 0.624;

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
//        double[] newPowers = new double[2];
//        newPowers[0] = power;
//        newPowers[1] = power;
//        if (Math.abs(encoder0) > Math.abs(encoder1) + 8) {
//            newPowers[0] = power * 0.75;
//        } else if (Math.abs(encoder1) > Math.abs(encoder0) + 8) {
//            newPowers[1] = power * 0.75;
//        }
//        return newPowers;
        return new double[]{power, power};
    }

    public double[] syncEncoders4Motors(double power, int encoder0, int encoder1, int encoder2, int encoder3) {
//        HashMap<Integer, Integer> encoderIndexes = new HashMap<Integer, Integer>();
//        encoderIndexes.put(Math.abs(encoder0), 0);
//        encoderIndexes.put(Math.abs(encoder1), 1);
//        encoderIndexes.put(Math.abs(encoder2), 2);
//        encoderIndexes.put(Math.abs(encoder3), 3);
//        int[] encoderValuesSorted = new int[]{Math.abs(encoder0), Math.abs(encoder1), Math.abs(encoder2), Math.abs(encoder3)};
//        Arrays.sort(encoderValuesSorted);
//        double[] newPowers = new double[4];
//        newPowers[0] = power;
//        newPowers[1] = power;
//        newPowers[2] = power;
//        newPowers[3] = power;
//        if (encoderValuesSorted[3] > encoderValuesSorted[2] + 8) {
//            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = power * 0.85;
//        }
//        if (encoderValuesSorted[2] > encoderValuesSorted[1] + 8) {
//            newPowers[encoderIndexes.get(encoderValuesSorted[2])] = power * 0.85;
//            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = newPowers[encoderIndexes.get(encoderValuesSorted[3])] * 0.85;
//        }
//        if (encoderValuesSorted[1] > encoderValuesSorted[0] + 8) {
//            newPowers[encoderIndexes.get(encoderValuesSorted[1])] = power * 0.85;
//            newPowers[encoderIndexes.get(encoderValuesSorted[2])] = newPowers[encoderIndexes.get(encoderValuesSorted[2])] * 0.85;
//            newPowers[encoderIndexes.get(encoderValuesSorted[3])] = newPowers[encoderIndexes.get(encoderValuesSorted[3])] * 0.85;
//        }
//        return newPowers;
        return new double[]{power, power, power, power};
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
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        frontRightUltra = hardwareMap.ultrasonicSensor.get("rightUltra");
        frontLeftUltra = hardwareMap.ultrasonicSensor.get("leftUltra");
        frontLightSensor = hardwareMap.lightSensor.get("frontLight");
        backLightSensor = hardwareMap.lightSensor.get("backLight");
        rightColorSensor = hardwareMap.lightSensor.get("rightColor");
        leftColorSensor = hardwareMap.lightSensor.get("leftColor");
//        motorSpinny = hardwareMap.dcMotor.get("spinny");
//        motorLiftArm = hardwareMap.dcMotor.get("liftArm");
//        motorExtend = hardwareMap.dcMotor.get("extend");
//        motorCollection = hardwareMap.dcMotor.get("collect");
        climberServo = hardwareMap.servo.get("climbers");
        rightFlapServo = hardwareMap.servo.get("rightButton");
        leftFlapServo = hardwareMap.servo.get("leftButton");
        rightHookServo = hardwareMap.servo.get("rightHook");
        leftHookServo = hardwareMap.servo.get("leftHook");
//        armLatchServo = hardwareMap.servo.get("armLatch");
//        rightSweepServo = hardwareMap.servo.get("rightSweep");
//        leftSweepServo = hardwareMap.servo.get("leftSweep");
    }
}
