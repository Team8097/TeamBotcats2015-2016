// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController;
import java.util.Map;
import java.util.Iterator;
import java.math.RoundingMode;
import java.math.BigDecimal;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.util.ElapsedTime;
import android.content.Context;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.util.BatteryChecker;

public class FtcEventLoopHandler implements BatteryChecker.BatteryWatcher
{
    public static final String NO_VOLTAGE_SENSOR = "no voltage sensor found";
    EventLoopManager a;
    BatteryChecker b;
    Context c;
    ElapsedTime d;
    double e;
    UpdateUI.Callback f;
    HardwareFactory g;
    HardwareMap h;
    
    public FtcEventLoopHandler(final HardwareFactory hardwareFactory, final UpdateUI.Callback callback, final Context robotControllerContext) {
        this.d = new ElapsedTime();
        this.e = 0.25;
        this.h = new HardwareMap();
        this.g = hardwareFactory;
        this.f = callback;
        this.c = robotControllerContext;
        (this.b = new BatteryChecker(robotControllerContext, (BatteryChecker.BatteryWatcher)this, 180000L)).startBatteryMonitoring();
    }
    
    public void init(final EventLoopManager eventLoopManager) {
        this.a = eventLoopManager;
    }
    
    public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
        return this.h = this.g.createHardwareMap(this.a);
    }
    
    public void displayGamePadInfo(final String activeOpModeName) {
        this.f.updateUi(activeOpModeName, this.a.getGamepads());
    }
    
    public Gamepad[] getGamepads() {
        return this.a.getGamepads();
    }
    
    public void resetGamepads() {
        this.a.resetGamepads();
    }
    
    public void sendTelemetryData(final Telemetry telemetry) {
        if (this.d.time() > this.e) {
            this.d.reset();
            if (telemetry.hasData()) {
                this.a.sendTelemetryData(telemetry);
            }
            telemetry.clearData();
        }
    }
    
    public void sendBatteryInfo() {
        this.a();
        this.b();
    }
    
    private void a() {
        this.sendTelemetry("RobotController Battery Level", String.valueOf(this.b.getBatteryLevel()));
    }
    
    private void b() {
        double voltage = Double.MAX_VALUE;
        for (final VoltageSensor voltageSensor : this.h.voltageSensor) {
            if (voltageSensor.getVoltage() < voltage) {
                voltage = voltageSensor.getVoltage();
            }
        }
        String value;
        if (this.h.voltageSensor.size() == 0) {
            value = "no voltage sensor found";
        }
        else {
            value = String.valueOf(new BigDecimal(voltage).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        this.sendTelemetry("Robot Battery Level", value);
    }
    
    public void sendTelemetry(final String tag, final String msg) {
        final Telemetry telemetry = new Telemetry();
        telemetry.setTag(tag);
        telemetry.addData(tag, msg);
        if (this.a != null) {
            this.a.sendTelemetryData(telemetry);
            telemetry.clearData();
        }
    }
    
    public void shutdownMotorControllers() {
        for (final Map.Entry<String, DcMotorController> entry : this.h.dcMotorController.entrySet()) {
            final String s = entry.getKey();
            final DcMotorController dcMotorController = (DcMotorController)entry.getValue();
            DbgLog.msg("Stopping DC Motor Controller " + s);
            dcMotorController.close();
        }
    }
    
    public void shutdownServoControllers() {
        for (final Map.Entry<String, ServoController> entry : this.h.servoController.entrySet()) {
            final String s = entry.getKey();
            final ServoController servoController = (ServoController)entry.getValue();
            DbgLog.msg("Stopping Servo Controller " + s);
            servoController.close();
        }
    }
    
    public void shutdownLegacyModules() {
        for (final Map.Entry<String, LegacyModule> entry : this.h.legacyModule.entrySet()) {
            final String s = entry.getKey();
            final LegacyModule legacyModule = (LegacyModule)entry.getValue();
            DbgLog.msg("Stopping Legacy Module " + s);
            legacyModule.close();
        }
    }
    
    public void shutdownCoreInterfaceDeviceModules() {
        for (final Map.Entry<String, DeviceInterfaceModule> entry : this.h.deviceInterfaceModule.entrySet()) {
            final String s = entry.getKey();
            final DeviceInterfaceModule deviceInterfaceModule = (DeviceInterfaceModule)entry.getValue();
            DbgLog.msg("Stopping Core Interface Device Module " + s);
            deviceInterfaceModule.close();
        }
    }
    
    public void restartRobot() {
        this.b.endBatteryMonitoring();
        this.f.restartRobot();
    }
    
    public void sendCommand(final Command command) {
        this.a.sendCommand(command);
    }
    
    public String getOpMode(final String extra) {
        if (this.a.state != RobotState.RUNNING) {
            return "Stop Robot";
        }
        return extra;
    }
    
    public void updateBatteryLevel(final float percent) {
        this.sendTelemetry("RobotController Battery Level", String.valueOf(percent));
    }
}
