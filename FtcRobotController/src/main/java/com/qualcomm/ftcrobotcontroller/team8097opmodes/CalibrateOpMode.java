package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;

import android.content.SharedPreferences.Editor;

public class CalibrateOpMode extends BaseOpMode {
    public static boolean calibrateTape;
    public static boolean calibrateRedTape;
    public static boolean calibrateGround;
    double frontValue;
    double backValue;
    int readings;

    @Override
    public void init() {
        readings = 0;
        calibrateTape = false;
        calibrateRedTape = false;
        calibrateGround = false;
        frontValue = 0;
        backValue = 0;
        frontLightSensor = hardwareMap.lightSensor.get("frontLight");
        backLightSensor = hardwareMap.lightSensor.get("backLight");
    }

    @Override
    public void loop() {
        frontLightSensor.enableLed(true);
        backLightSensor.enableLed(true);
        telemetry.addData("front light sensor", frontLightSensor.getLightDetected());
        telemetry.addData("back light sensor", backLightSensor.getLightDetected());
        if (calibrateTape) {
            if (readings < 100) {
                readings++;
                frontValue += frontLightSensor.getLightDetected();
                backValue += backLightSensor.getLightDetected();
                setButtonsClickable(false);
            } else {
                readings = 0;
                frontValue /= 100.0;
                backValue /= 100.0;
                calibrateTape = false;
                FtcRobotControllerActivity.setTapeText.obtainMessage(0, (int) (frontValue * 100), (int) (backValue * 100)).sendToTarget();
                saveTapeValue();
                frontValue = 0;
                backValue = 0;
            }
        } else if (calibrateGround) {
            if (readings < 100) {
                readings++;
                frontValue += frontLightSensor.getLightDetected();
                backValue += backLightSensor.getLightDetected();
                setButtonsClickable(false);
            } else {
                readings = 0;
                frontValue /= 100.0;
                backValue /= 100.0;
                calibrateGround = false;
                FtcRobotControllerActivity.setGroundText.obtainMessage(0, (int) (frontValue * 100), (int) (backValue * 100)).sendToTarget();
                saveGroundValue();
                frontValue = 0;
                backValue = 0;
            }
        } else if (calibrateRedTape) {
            if (readings < 100) {
                readings++;
                frontValue += frontLightSensor.getLightDetected();
                backValue += backLightSensor.getLightDetected();
                setButtonsClickable(false);
            } else {
                readings = 0;
                frontValue /= 100.0;
                backValue /= 100.0;
                calibrateRedTape = false;
                FtcRobotControllerActivity.setRedTapeText.obtainMessage(0, (int) (frontValue * 100), (int) (backValue * 100)).sendToTarget();
                saveRedTapeValue();
                frontValue = 0;
                backValue = 0;
            }
        } else {
            setButtonsClickable(true);
        }
    }

    private void setButtonsClickable(boolean visible) {
        if (visible) {
            FtcRobotControllerActivity.setButtonsClickable.obtainMessage().sendToTarget();
        } else {
            FtcRobotControllerActivity.setButtonsUnclickable.obtainMessage().sendToTarget();
        }
    }

    @Override
    public void stop() {
        super.stop();
        FtcRobotControllerActivity.setButtonsUnclickable.obtainMessage().sendToTarget();
    }

    private void saveTapeValue() {
        Editor editor = FtcRobotControllerActivity.calibrationSP.edit();
        editor.putFloat("frontTapeValue", (float) frontValue);
        editor.putFloat("backTapeValue", (float) backValue);
        editor.apply();
    }

    private void saveRedTapeValue() {
        Editor editor = FtcRobotControllerActivity.calibrationSP.edit();
        editor.putFloat("frontRedTapeValue", (float) frontValue);
        editor.putFloat("backRedTapeValue", (float) backValue);
        editor.apply();
    }

    private void saveGroundValue() {
        Editor editor = FtcRobotControllerActivity.calibrationSP.edit();
        editor.putFloat("frontGroundValue", (float) frontValue);
        editor.putFloat("backGroundValue", (float) backValue);
        editor.apply();
    }
}