// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import java.util.List;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import java.util.ArrayList;
import java.util.Iterator;
import android.widget.LinearLayout;
import java.io.IOException;
import android.widget.Toast;
import android.view.View;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.ftccommon.R;
import android.os.Bundle;
import java.util.HashSet;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import java.util.Set;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import com.qualcomm.robotcore.hardware.DeviceManager;
import android.widget.Button;
import android.content.Context;
import android.app.Activity;

public class AutoConfigureActivity extends Activity
{
    private Context a;
    private Button b;
    private Button c;
    private DeviceManager d;
    protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices;
    protected Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> entries;
    private Map<SerialNumber, ControllerConfiguration> e;
    private Thread f;
    private Utility g;
    
    public AutoConfigureActivity() {
        this.scannedDevices = new HashMap<SerialNumber, DeviceManager.DeviceType>();
        this.entries = new HashSet<Map.Entry<SerialNumber, DeviceManager.DeviceType>>();
        this.e = new HashMap<SerialNumber, ControllerConfiguration>();
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AutoConfigureActivity)(this.a = (Context)this)).setContentView(R.layout.activity_autoconfigure);
        this.g = new Utility((Activity)this);
        this.b = (Button)this.findViewById(R.id.configureLegacy);
        this.c = (Button)this.findViewById(R.id.configureUSB);
        try {
            this.d = (DeviceManager)new HardwareDeviceManager(this.a, (EventLoopManager)null);
        }
        catch (RobotCoreException e) {
            this.g.complainToast("Failed to open the Device Manager", this.a);
            DbgLog.error("Failed to open deviceManager: " + e.toString());
            DbgLog.logStacktrace((Exception)e);
        }
    }
    
    protected void onStart() {
        super.onStart();
        this.g.updateHeader("K9LegacyBot", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final String filenameFromPrefs = this.g.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        if (filenameFromPrefs.equals("K9LegacyBot") || filenameFromPrefs.equals("K9USBBot")) {
            this.d();
        }
        else {
            this.a();
        }
        this.b.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                AutoConfigureActivity.this.f = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DbgLog.msg("Scanning USB bus");
                            AutoConfigureActivity.this.scannedDevices = (Map<SerialNumber, DeviceManager.DeviceType>)AutoConfigureActivity.this.d.scanForUsbDevices();
                        }
                        catch (RobotCoreException ex) {
                            DbgLog.error("Device scan failed");
                        }
                        AutoConfigureActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                AutoConfigureActivity.this.g.resetCount();
                                if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
                                    AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                    AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                    AutoConfigureActivity.this.a();
                                }
                                AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                                AutoConfigureActivity.this.e = (Map<SerialNumber, ControllerConfiguration>)new HashMap();
                                AutoConfigureActivity.this.g.createLists((Set)AutoConfigureActivity.this.entries, AutoConfigureActivity.this.e);
                                if (AutoConfigureActivity.this.g()) {
                                    AutoConfigureActivity.this.a("K9LegacyBot");
                                }
                                else {
                                    AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                    AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                    AutoConfigureActivity.this.b();
                                }
                            }
                        });
                    }
                });
                AutoConfigureActivity.this.f.start();
            }
        });
        this.c.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                AutoConfigureActivity.this.f = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DbgLog.msg("Scanning USB bus");
                            AutoConfigureActivity.this.scannedDevices = (Map<SerialNumber, DeviceManager.DeviceType>)AutoConfigureActivity.this.d.scanForUsbDevices();
                        }
                        catch (RobotCoreException ex) {
                            DbgLog.error("Device scan failed");
                        }
                        AutoConfigureActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                AutoConfigureActivity.this.g.resetCount();
                                if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
                                    AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                    AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                    AutoConfigureActivity.this.a();
                                }
                                AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                                AutoConfigureActivity.this.e = (Map<SerialNumber, ControllerConfiguration>)new HashMap();
                                AutoConfigureActivity.this.g.createLists((Set)AutoConfigureActivity.this.entries, AutoConfigureActivity.this.e);
                                if (AutoConfigureActivity.this.e()) {
                                    AutoConfigureActivity.this.a("K9USBBot");
                                }
                                else {
                                    AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                    AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                    AutoConfigureActivity.this.c();
                                }
                            }
                        });
                    }
                });
                AutoConfigureActivity.this.f.start();
            }
        });
    }
    
    private void a(final String s) {
        this.g.writeXML((Map)this.e);
        try {
            this.g.writeToFile(s + ".xml");
            this.g.saveToPreferences(s, R.string.pref_hardware_config_filename);
            this.g.updateHeader(s, R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
            Toast.makeText(this.a, (CharSequence)("AutoConfigure " + s + " Successful"), 0).show();
        }
        catch (RobotCoreException ex) {
            this.g.complainToast(ex.getMessage(), this.a);
            DbgLog.error(ex.getMessage());
        }
        catch (IOException ex2) {
            this.g.complainToast("Found " + ex2.getMessage() + "\n Please fix and re-save", this.a);
            DbgLog.error(ex2.getMessage());
        }
    }
    
    private void a() {
        this.g.setOrangeText("No devices found!", "To configure K9LegacyBot, please: \n   1. Attach a LegacyModuleController, \n       with \n       a. MotorController in port 0, with a \n         motor in port 1 and port 2 \n       b. ServoController in port 1, with a \n         servo in port 1 and port 6 \n      c. IR seeker in port 2\n      d. Light sensor in port 3 \n   2. Press the K9LegacyBot button\n \nTo configure K9USBBot, please: \n   1. Attach a USBMotorController, with a \n       motor in port 1 and port 2 \n    2. USBServoController in port 1, with a \n      servo in port 1 and port 6 \n   3. LegacyModule, with \n      a. IR seeker in port 2\n      b. Light sensor in port 3 \n   4. Press the K9USBBot button", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void b() {
        this.g.setOrangeText("Wrong devices found!", "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. LegacyModuleController, with \n " + "      a. MotorController in port 0, with a \n" + "          motor in port 1 and port 2 \n " + "      b. ServoController in port 1, with a \n" + "          servo in port 1 and port 6 \n" + "       c. IR seeker in port 2\n" + "       d. Light sensor in port 3 ", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void c() {
        this.g.setOrangeText("Wrong devices found!", "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. USBMotorController with a \n" + "      motor in port 1 and port 2 \n " + "   2. USBServoController with a \n" + "      servo in port 1 and port 6 \n" + "   3. LegacyModuleController, with \n " + "       a. IR seeker in port 2\n" + "       b. Light sensor in port 3 ", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void d() {
        this.g.setOrangeText("Already configured!", "", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private boolean e() {
        int n = 1;
        int n2 = 1;
        int n3 = 1;
        for (final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : this.entries) {
            final DeviceManager.DeviceType deviceType = entry.getValue();
            if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && n != 0) {
                this.a(entry.getKey(), "sensors");
                n = 0;
            }
            if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER && n2 != 0) {
                this.a(entry.getKey(), "motor_1", "motor_2", "wheels");
                n2 = 0;
            }
            if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER && n3 != 0) {
                this.a(entry.getKey(), this.f(), "servos");
                n3 = 0;
            }
        }
        if (n != 0 || n2 != 0 || n3 != 0) {
            return false;
        }
        ((LinearLayout)this.findViewById(R.id.autoconfigure_info)).removeAllViews();
        return true;
    }
    
    private ArrayList<String> f() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("servo_1");
        list.add("NO DEVICE ATTACHED");
        list.add("NO DEVICE ATTACHED");
        list.add("NO DEVICE ATTACHED");
        list.add("NO DEVICE ATTACHED");
        list.add("servo_6");
        return list;
    }
    
    private void a(final SerialNumber serialNumber, final String name) {
        final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = (LegacyModuleControllerConfiguration)this.e.get(serialNumber);
        legacyModuleControllerConfiguration.setName(name);
        final DeviceConfiguration a = this.a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
        final DeviceConfiguration a2 = this.a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 6; ++i) {
            if (i == 2) {
                list.add(a);
            }
            if (i == 3) {
                list.add(a2);
            }
            else {
                list.add(new DeviceConfiguration(i));
            }
        }
        legacyModuleControllerConfiguration.addDevices((List)list);
    }
    
    private boolean g() {
        int n = 1;
        for (final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : this.entries) {
            if (entry.getValue() == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && n != 0) {
                this.b(entry.getKey(), "devices");
                n = 0;
            }
        }
        if (n != 0) {
            return false;
        }
        ((LinearLayout)this.findViewById(R.id.autoconfigure_info)).removeAllViews();
        return true;
    }
    
    private void b(final SerialNumber serialNumber, final String name) {
        final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = (LegacyModuleControllerConfiguration)this.e.get(serialNumber);
        legacyModuleControllerConfiguration.setName(name);
        final MotorControllerConfiguration a = this.a(ControllerConfiguration.NO_SERIAL_NUMBER, "motor_1", "motor_2", "wheels");
        a.setPort(0);
        final ServoControllerConfiguration a2 = this.a(ControllerConfiguration.NO_SERIAL_NUMBER, this.f(), "servos");
        a2.setPort(1);
        final DeviceConfiguration a3 = this.a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
        final DeviceConfiguration a4 = this.a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        list.add(a);
        list.add(a2);
        list.add((MotorControllerConfiguration)a3);
        list.add((MotorControllerConfiguration)a4);
        for (int i = 4; i < 6; ++i) {
            list.add((MotorControllerConfiguration)new DeviceConfiguration(i));
        }
        legacyModuleControllerConfiguration.addDevices((List)list);
    }
    
    private DeviceConfiguration a(final DeviceConfiguration.ConfigurationType configurationType, final String s, final int n) {
        return new DeviceConfiguration(n, configurationType, s, true);
    }
    
    private MotorControllerConfiguration a(final SerialNumber serialNumber, final String s, final String s2, final String name) {
        MotorControllerConfiguration motorControllerConfiguration;
        if (!serialNumber.equals((Object)ControllerConfiguration.NO_SERIAL_NUMBER)) {
            motorControllerConfiguration = (MotorControllerConfiguration)this.e.get(serialNumber);
        }
        else {
            motorControllerConfiguration = new MotorControllerConfiguration();
        }
        motorControllerConfiguration.setName(name);
        final ArrayList<MotorConfiguration> list = new ArrayList<MotorConfiguration>();
        final MotorConfiguration motorConfiguration = new MotorConfiguration(1, s, true);
        final MotorConfiguration motorConfiguration2 = new MotorConfiguration(2, s2, true);
        list.add(motorConfiguration);
        list.add(motorConfiguration2);
        motorControllerConfiguration.addMotors((List)list);
        return motorControllerConfiguration;
    }
    
    private ServoControllerConfiguration a(final SerialNumber serialNumber, final ArrayList<String> list, final String name) {
        ServoControllerConfiguration servoControllerConfiguration;
        if (!serialNumber.equals((Object)ControllerConfiguration.NO_SERIAL_NUMBER)) {
            servoControllerConfiguration = (ServoControllerConfiguration)this.e.get(serialNumber);
        }
        else {
            servoControllerConfiguration = new ServoControllerConfiguration();
        }
        servoControllerConfiguration.setName(name);
        final ArrayList<ServoConfiguration> list2 = new ArrayList<ServoConfiguration>();
        for (int i = 1; i <= 6; ++i) {
            final String s = list.get(i - 1);
            boolean b = true;
            if (s.equals("NO DEVICE ATTACHED")) {
                b = false;
            }
            list2.add(new ServoConfiguration(i, s, b));
        }
        servoControllerConfiguration.addServos((ArrayList)list2);
        return servoControllerConfiguration;
    }
}
