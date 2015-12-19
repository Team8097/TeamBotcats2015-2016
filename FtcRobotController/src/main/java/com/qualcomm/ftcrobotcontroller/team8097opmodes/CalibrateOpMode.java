package com.qualcomm.ftcrobotcontroller.team8097opmodes;

import android.content.Context;
import android.content.SharedPreferences;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;

import android.content.SharedPreferences.Editor;

public class CalibrateOpMode extends BaseOpMode {
    public static boolean calibrateTape;
    double tapeValue;
    public static boolean calibrateGround;
    double groundValue;
    int readings;

    @Override
    public void init() {
        readings = 0;
        calibrateTape = false;
        tapeValue = 0;
        calibrateGround = false;
        groundValue = 0;
        frontLightSensor = hardwareMap.lightSensor.get("frontLight");
        backLightSensor = hardwareMap.lightSensor.get("backLight");
    }

    @Override
    public void loop() {
        if (calibrateTape) {
            if (readings < 100) {
                readings++;
                tapeValue += (frontLightSensor.getLightDetected() + backLightSensor.getLightDetected()) / 2.0;
                setButtonsVisible(false);
            } else {
                readings = 0;
                tapeValue /= 100.0;
                calibrateTape = false;
                FtcRobotControllerActivity.setTapeText.obtainMessage(0, (float) tapeValue).sendToTarget();
                saveTapeValue();
            }
        } else if (calibrateGround) {
            if (readings < 100) {
                readings++;
                groundValue += (frontLightSensor.getLightDetected() + backLightSensor.getLightDetected()) / 2.0;
                setButtonsVisible(false);
            } else {
                readings = 0;
                groundValue /= 100.0;
                calibrateGround = false;
                FtcRobotControllerActivity.setGroundText.obtainMessage(0, (float) groundValue).sendToTarget();
                saveGroundValue();
            }
        } else {
            setButtonsVisible(true);
        }
    }

    private void setButtonsVisible(boolean visible) {
        if (visible) {
            FtcRobotControllerActivity.setButtonsVisible.obtainMessage().sendToTarget();
        } else {
            FtcRobotControllerActivity.setButtonsInvisible.obtainMessage().sendToTarget();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    private void saveTapeValue() {
        Editor editor = FtcRobotControllerActivity.calibrationSP.edit();
        editor.putFloat("tapeValue", (float) tapeValue);
        editor.apply();
    }

    private void saveGroundValue() {
        Editor editor = FtcRobotControllerActivity.calibrationSP.edit();
        editor.putFloat("groundValue", (float) tapeValue);
        editor.apply();
    }
}