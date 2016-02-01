// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.Gamepad;
import android.content.Context;
import android.widget.Toast;
import com.qualcomm.robotcore.util.Dimmer;
import android.app.Activity;
import android.widget.TextView;

public class UpdateUI
{
    protected TextView textDeviceName;
    protected TextView textWifiDirectStatus;
    protected TextView textRobotStatus;
    protected TextView[] textGamepad;
    protected TextView textOpMode;
    protected TextView textErrorMessage;
    Restarter a;
    FtcRobotControllerService b;
    Activity c;
    Dimmer d;
    
    public UpdateUI(final Activity activity, final Dimmer dimmer) {
        this.textGamepad = new TextView[2];
        this.c = activity;
        this.d = dimmer;
    }
    
    public void setTextViews(final TextView textWifiDirectStatus, final TextView textRobotStatus, final TextView[] textGamepad, final TextView textOpMode, final TextView textErrorMessage, final TextView textDeviceName) {
        this.textWifiDirectStatus = textWifiDirectStatus;
        this.textRobotStatus = textRobotStatus;
        this.textGamepad = textGamepad;
        this.textOpMode = textOpMode;
        this.textErrorMessage = textErrorMessage;
        this.textDeviceName = textDeviceName;
    }
    
    public void setControllerService(final FtcRobotControllerService controllerService) {
        this.b = controllerService;
    }
    
    public void setRestarter(final Restarter restarter) {
        this.a = restarter;
    }
    
    private void a(final String message) {
        DbgLog.msg(message);
        this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                UpdateUI.this.textWifiDirectStatus.setText((CharSequence)message);
            }
        });
    }
    
    private void b(final String s) {
        this.c.runOnUiThread((Runnable) new Runnable() {
            @Override
            public void run() {
                UpdateUI.this.textDeviceName.setText((CharSequence) s);
            }
        });
    }
    
    private void a() {
        this.a.requestRestart();
    }
    
    public class Callback
    {
        public void restartRobot() {
            UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    Toast.makeText((Context)UpdateUI.this.c, (CharSequence)"Restarting Robot", 0).show();
                }
            });
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500L);
                    }
                    catch (InterruptedException ex) {}
                    UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            UpdateUI.this.a();
                        }
                    });
                }
            }.start();
        }
        
        public void updateUi(final String opModeName, final Gamepad[] gamepads) {
            UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    for (int n = 0; n < UpdateUI.this.textGamepad.length && n < gamepads.length; ++n) {
                        if (gamepads[n].id == -1) {
                            UpdateUI.this.textGamepad[n].setText((CharSequence)" ");
                        }
                        else {
                            UpdateUI.this.textGamepad[n].setText((CharSequence)gamepads[n].toString());
                        }
                    }
                    UpdateUI.this.textOpMode.setText((CharSequence)("Op Mode: " + opModeName));
                    UpdateUI.this.textErrorMessage.setText((CharSequence)RobotLog.getGlobalErrorMsg());
                }
            });
        }
        
        public void wifiDirectUpdate(final WifiDirectAssistant.Event event) {
            switch (event) {
                case DISCONNECTED: {
                    UpdateUI.this.a("Wifi Direct - disconnected");
                    break;
                }
                case CONNECTED_AS_GROUP_OWNER: {
                    UpdateUI.this.a("Wifi Direct - enabled");
                    break;
                }
                case ERROR: {
                    UpdateUI.this.a("Wifi Direct - ERROR");
                    break;
                }
                case CONNECTION_INFO_AVAILABLE: {
                    WifiDirectAssistant wifiDirectAssistant = UpdateUI.this.b.getWifiDirectAssistant();
                    UpdateUI.this.b(wifiDirectAssistant.getDeviceName());
                }
            }
        }
        
        public void robotUpdate(final String status) {
            DbgLog.msg(status);
            UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    UpdateUI.this.textRobotStatus.setText((CharSequence)status);
                    UpdateUI.this.textErrorMessage.setText((CharSequence)RobotLog.getGlobalErrorMsg());
                    if (RobotLog.hasGlobalErrorMsg()) {
                        UpdateUI.this.d.longBright();
                    }
                }
            });
        }
    }
}
