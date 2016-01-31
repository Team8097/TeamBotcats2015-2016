// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.Iterator;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.LinkedHashMap;

import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.Map;

public class OpModeManager {
    public static final String DEFAULT_OP_MODE_NAME = "Stop Robot";
    public static final OpMode DEFAULT_OP_MODE;
    private Map<String, Class<?>> a;
    private Map<String, OpMode> b;
    private String c;
    private OpMode d;
    private String e;
    private HardwareMap f;
    private HardwareMap g;
    private bEnum h;
    private boolean i;
    private boolean j;
    private boolean k;

    public OpModeManager(final HardwareMap hardwareMap) {
        this.a = new LinkedHashMap<String, Class<?>>();
        this.b = new LinkedHashMap<String, OpMode>();
        this.c = "Stop Robot";
        this.d = OpModeManager.DEFAULT_OP_MODE;
        this.e = "Stop Robot";
        this.f = new HardwareMap();
        this.g = new HardwareMap();
        this.h = OpModeManager.bEnum.a;
        this.i = false;
        this.j = false;
        this.k = false;
        this.f = hardwareMap;
        this.register("Stop Robot", a.class);
        this.initActiveOpMode("Stop Robot");
    }

    public void registerOpModes(final OpModeRegister register) {
        register.register(this);
    }

    public void setHardwareMap(final HardwareMap hardwareMap) {
        this.f = hardwareMap;
    }

    public HardwareMap getHardwareMap() {
        return this.f;
    }

    public Set<String> getOpModes() {
        final LinkedHashSet<String> set = new LinkedHashSet<String>();
        set.addAll(this.a.keySet());
        set.addAll(this.b.keySet());
        return (Set<String>) set;
    }

    public String getActiveOpModeName() {
        return this.c;
    }

    public OpMode getActiveOpMode() {
        return this.d;
    }

    public void initActiveOpMode(final String name) {
        this.e = name;
        this.i = true;
        this.j = true;
        this.h = OpModeManager.bEnum.a;
    }

    public void startActiveOpMode() {
        this.h = OpModeManager.bEnum.b;
        this.k = true;
    }

    public void stopActiveOpMode() {
        this.d.stop();
        this.initActiveOpMode("Stop Robot");
    }

    public void runActiveOpMode(final Gamepad[] gamepads) {
        this.d.time = this.d.getRuntime();
        this.d.gamepad1 = gamepads[0];
        this.d.gamepad2 = gamepads[1];
        if (this.i) {
            this.d.stop();
            this.a();
            this.h = OpModeManager.bEnum.a;
            this.j = true;
        }
        if (this.h == OpModeManager.bEnum.a){
            if (this.j) {
                this.d.hardwareMap = this.f;
                this.d.resetStartTime();
                this.d.init();
                this.j = false;
            }
            this.d.init_loop();
        }
        else{
            if (this.k) {
                this.d.start();
                this.k = false;
            }
            this.d.loop();
        }
    }

    public void logOpModes() {
        RobotLog.i("There are " + (this.a.size() + this.b.size()) + " Op Modes");
        final Iterator<Map.Entry<String, Class<?>>> iterator = this.a.entrySet().iterator();
        while (iterator.hasNext()) {
            RobotLog.i("   Op Mode: " + iterator.next().getKey());
        }
        final Iterator<Map.Entry<String, OpMode>> iterator2 = this.b.entrySet().iterator();
        while (iterator2.hasNext()) {
            RobotLog.i("   Op Mode: " + iterator2.next().getKey());
        }
    }

    public void register(final String name, final Class opMode) {
        if (this.a(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        }
        this.a.put(name, opMode);
    }

    public void register(final String name, final OpMode opMode) {
        if (this.a(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        }
        this.b.put(name, opMode);
    }

    private void a() {
        RobotLog.i("Attempting to switch to op mode " + this.e);
        try {
            if (this.b.containsKey(this.e)) {
                this.d = this.b.get(this.e);
            } else {
                this.d = (OpMode) this.a.get(this.e).newInstance();
            }
            this.c = this.e;
        } catch (InstantiationException ex) {
            this.a(ex);
        } catch (IllegalAccessException ex2) {
            this.a(ex2);
        }
        this.i = false;
    }

    private boolean a(final String s) {
        return this.getOpModes().contains(s);
    }

    private void a(final Exception e) {
        RobotLog.e("Unable to start op mode " + this.c);
        RobotLog.logStacktrace(e);
        this.c = "Stop Robot";
        this.d = OpModeManager.DEFAULT_OP_MODE;
    }

    static {
        DEFAULT_OP_MODE = new a();
    }

    private enum bEnum {
        a,
        b;
    }

    private static class a extends OpMode {
        @Override
        public void init() {
            this.a();
        }

        @Override
        public void init_loop() {
            this.a();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        @Override
        public void loop() {
            this.a();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        @Override
        public void stop() {
        }

        private void a() {
            final Iterator<ServoController> iterator = this.hardwareMap.servoController.iterator();
            while (iterator.hasNext()) {
                iterator.next().pwmDisable();
            }
            final Iterator<DcMotorController> iterator2 = this.hardwareMap.dcMotorController.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            }
            for (final DcMotor dcMotor : this.hardwareMap.dcMotor) {
                dcMotor.setPower(0.0);
                dcMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
            final Iterator<LightSensor> iterator4 = this.hardwareMap.lightSensor.iterator();
            while (iterator4.hasNext()) {
                iterator4.next().enableLed(false);
            }
        }
    }
}
