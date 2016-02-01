// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.eventloop.EventLoopManager;

import android.os.Binder;

import com.qualcomm.robotcore.util.RobotLog;

import android.content.Context;
import android.os.Build;
import android.content.Intent;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.robot.Robot;

import android.os.IBinder;

import com.qualcomm.robotcore.wifi.WifiDirectAssistant;

import android.app.Service;

public class FtcRobotControllerService extends Service implements WifiDirectAssistant.WifiDirectAssistantCallback {
    private final IBinder a;
    private WifiDirectAssistant b;
    private Robot c;
    private EventLoop d;
    private WifiDirectAssistant.Event e;
    private String f;
    private UpdateUI.Callback g;
    private final a h;
    private final ElapsedTime i;
    private Thread j;

    public FtcRobotControllerService() {
        this.a = (IBinder) new FtcRobotControllerBinder();
        this.e = WifiDirectAssistant.Event.DISCONNECTED;
        this.f = "Robot Status: null";
        this.g = null;
        this.h = new a();
        this.i = new ElapsedTime();
        this.j = null;
    }

    public WifiDirectAssistant getWifiDirectAssistant() {
        return this.b;
    }

    public WifiDirectAssistant.Event getWifiDirectStatus() {
        return this.e;
    }

    public String getRobotStatus() {
        return this.f;
    }

    public IBinder onBind(final Intent intent) {
        DbgLog.msg("Starting FTC Controller Service");
        DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
        (this.b = WifiDirectAssistant.getWifiDirectAssistant((Context) this)).setCallback((WifiDirectAssistant.WifiDirectAssistantCallback) this);
        this.b.enable();
        if (Build.MODEL.equals("FL7007")) {
            this.b.discoverPeers();
        } else {
            this.b.createGroup();
        }
        return this.a;
    }

    public boolean onUnbind(final Intent intent) {
        DbgLog.msg("Stopping FTC Controller Service");
        this.b.disable();
        this.shutdownRobot();
        return false;
    }

    public synchronized void setCallback(final UpdateUI.Callback callback) {
        this.g = callback;
    }

    public synchronized void setupRobot(final EventLoop eventLoop) {
        if (this.j != null && this.j.isAlive()) {
            DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
            this.j.interrupt();
            while (this.j.isAlive()) {
                Thread.yield();
            }
            DbgLog.msg("Old setup stopped; restarting setup");
        }
        RobotLog.clearGlobalErrorMsg();
        DbgLog.msg("Processing robot setup");
        this.d = eventLoop;
        (this.j = new Thread(new b(), "Robot Setup")).start();
        while (this.j.getState() == Thread.State.NEW) {
            Thread.yield();
        }
    }

    public synchronized void shutdownRobot() {
        if (this.j != null && this.j.isAlive()) {
            this.j.interrupt();
        }
        if (this.c != null) {
            this.c.shutdown();
        }
        this.c = null;
        this.a("Robot Status: null");
    }

    public void onWifiDirectEvent(final WifiDirectAssistant.Event event) {
        switch (event) {
            case CONNECTED_AS_GROUP_OWNER: {
                DbgLog.msg("Wifi Direct - Group Owner");
                this.b.cancelDiscoverPeers();
                break;
            }
            case CONNECTED_AS_PEER: {
                DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
                break;
            }
            case CONNECTION_INFO_AVAILABLE: {
                DbgLog.msg("Wifi Direct Passphrase: " + this.b.getPassphrase());
                break;
            }
            case ERROR: {
                DbgLog.error("Wifi Direct Error: " + this.b.getFailureReason());
                break;
            }
        }
        this.a(event);
    }

    private void a(final WifiDirectAssistant.Event e) {
        this.e = e;
        if (this.g != null) {
            this.g.wifiDirectUpdate(this.e);
        }
    }

    private void a(final String s) {
        this.f = s;
        if (this.g != null) {
            this.g.robotUpdate(s);
        }
    }

    public class FtcRobotControllerBinder extends Binder {
        public FtcRobotControllerService getService() {
            return FtcRobotControllerService.this;
        }
    }

    private class a implements EventLoopManager.EventLoopMonitor {
        public void onStateChange(final RobotState state) {
            if (FtcRobotControllerService.this.g == null) {
                return;
            }
            switch (state) {
                case INIT: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: init");
                    break;
                }
                case NOT_STARTED: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: not started");
                    break;
                }
                case RUNNING: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: running");
                    break;
                }
                case STOPPED: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: stopped");
                    break;
                }
                case EMERGENCY_STOP: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: EMERGENCY STOP");
                    break;
                }
                case DROPPED_CONNECTION: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: dropped connection");
                }
            }
        }
    }

    private class b implements Runnable {
        @Override
        public void run() {
            try {
                if (FtcRobotControllerService.this.c != null) {
                    FtcRobotControllerService.this.c.shutdown();
                    FtcRobotControllerService.this.c = null;
                }
                FtcRobotControllerService.this.a("Robot Status: scanning for USB devices");
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException ex3) {
                    FtcRobotControllerService.this.a("Robot Status: abort due to interrupt");
                    return;
                }
                FtcRobotControllerService.this.c = RobotFactory.createRobot();
                FtcRobotControllerService.this.a("Robot Status: waiting on network");
                FtcRobotControllerService.this.i.reset();
                while (!FtcRobotControllerService.this.b.isConnected()) {
                    try {
                        Thread.sleep(1000L);
                        if (FtcRobotControllerService.this.i.time() > 120.0) {
                            FtcRobotControllerService.this.a("Robot Status: network timed out");
                            return;
                        }
                        continue;
                    } catch (InterruptedException ex4) {
                        DbgLog.msg("interrupt waiting for network; aborting setup");
                        return;
                    }
//                    break;
                }
                FtcRobotControllerService.this.a("Robot Status: starting robot");
                try {
                    FtcRobotControllerService.this.c.eventLoopManager.setMonitor((EventLoopManager.EventLoopMonitor) FtcRobotControllerService.this.h);
                    FtcRobotControllerService.this.c.start(FtcRobotControllerService.this.b.getGroupOwnerAddress(), FtcRobotControllerService.this.d);
                } catch (RobotCoreException ex) {
                    FtcRobotControllerService.this.a("Robot Status: failed to start robot");
                    RobotLog.setGlobalErrorMsg(ex.getMessage());
                }
            } catch (RobotCoreException ex2) {
                FtcRobotControllerService.this.a("Robot Status: Unable to create robot!");
                RobotLog.setGlobalErrorMsg(ex2.getMessage());
            }
        }
    }
}
