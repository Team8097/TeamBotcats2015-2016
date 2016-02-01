// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import java.util.Iterator;
import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import android.content.Context;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.EventLoop;

public class FtcEventLoop implements EventLoop
{
    FtcEventLoopHandler a;
    OpModeManager b;
    OpModeRegister c;
    
    public FtcEventLoop(final HardwareFactory hardwareFactory, final OpModeRegister register, final UpdateUI.Callback callback, final Context robotControllerContext) {
        this.b = new OpModeManager(new HardwareMap());
        this.a = new FtcEventLoopHandler(hardwareFactory, callback, robotControllerContext);
        this.c = register;
    }
    
    public OpModeManager getOpModeManager() {
        return this.b;
    }
    
    public void init(final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        DbgLog.msg("======= INIT START =======");
        this.b.registerOpModes(this.c);
        this.a.init(eventLoopManager);
        final HardwareMap hardwareMap = this.a.getHardwareMap();
        ModernRoboticsUsbUtil.init(hardwareMap.appContext, hardwareMap);
        this.b.setHardwareMap(hardwareMap);
        hardwareMap.logDevices();
        DbgLog.msg("======= INIT FINISH =======");
    }
    
    public void loop() throws RobotCoreException {
        this.a.displayGamePadInfo(this.b.getActiveOpModeName());
        this.b.runActiveOpMode(this.a.getGamepads());
        this.a.sendTelemetryData(this.b.getActiveOpMode().telemetry);
    }
    
    public void teardown() throws RobotCoreException {
        DbgLog.msg("======= TEARDOWN =======");
        this.b.stopActiveOpMode();
        this.a.shutdownMotorControllers();
        this.a.shutdownServoControllers();
        this.a.shutdownLegacyModules();
        this.a.shutdownCoreInterfaceDeviceModules();
        DbgLog.msg("======= TEARDOWN COMPLETE =======");
    }
    
    public void processCommand(final Command command) {
        DbgLog.msg("Processing Command: " + command.getName() + " " + command.getExtra());
        this.a.sendBatteryInfo();
        final String name = command.getName();
        final String extra = command.getExtra();
        if (name.equals("CMD_RESTART_ROBOT")) {
            this.a();
        }
        else if (name.equals("CMD_REQUEST_OP_MODE_LIST")) {
            this.b();
        }
        else if (name.equals("CMD_INIT_OP_MODE")) {
            this.a(extra);
        }
        else if (name.equals("CMD_RUN_OP_MODE")) {
            this.c();
        }
        else {
            DbgLog.msg("Unknown command: " + name);
        }
    }
    
    private void a() {
        this.a.restartRobot();
    }
    
    private void b() {
        String s = "";
        for (final String s2 : this.b.getOpModes()) {
            if (!s2.equals("Stop Robot")) {
                if (!s.isEmpty()) {
                    s += Util.ASCII_RECORD_SEPARATOR;
                }
                s += s2;
            }
        }
        this.a.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", s));
    }
    
    private void a(final String extra) {
        final String opMode = this.a.getOpMode(extra);
        this.a.resetGamepads();
        this.b.initActiveOpMode(opMode);
        this.a.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", opMode));
    }
    
    private void c() {
        this.b.startActiveOpMode();
        this.a.sendCommand(new Command("CMD_RUN_OP_MODE_RESP", this.b.getActiveOpModeName()));
    }
}
