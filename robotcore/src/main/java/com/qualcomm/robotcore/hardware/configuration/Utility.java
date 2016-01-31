// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import android.os.Environment;
import android.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.graphics.Color;

import java.util.List;
import java.util.Iterator;

import com.qualcomm.robotcore.hardware.DeviceManager;

import java.util.Set;

import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import com.qualcomm.robotcore.exception.RobotCoreException;

import java.util.Collection;

import com.qualcomm.robotcore.util.SerialNumber;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.ArrayList;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;

import android.content.Context;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.app.Activity;

public class Utility {
    public static final String AUTOCONFIGURE_K9LEGACYBOT = "K9LegacyBot";
    public static final String AUTOCONFIGURE_K9USBBOT = "K9USBBot";
    public static final String CONFIG_FILES_DIR;
    public static final String DEFAULT_ROBOT_CONFIG = "robot_config";
    public static final String FILE_EXT = ".xml";
    public static final String DEFAULT_ROBOT_CONFIG_FILENAME = "robot_config.xml";
    public static final String NO_FILE = "No current file!";
    public static final String UNSAVED = "Unsaved";
    private Activity a;
    private SharedPreferences b;
    private static int c;
    private WriteXMLFileHandler d;
    private String e;

    public Utility(final Activity activity) {
        this.a = activity;
        this.b = PreferenceManager.getDefaultSharedPreferences((Context) activity);
        this.d = new WriteXMLFileHandler((Context) activity);
    }

    public void createConfigFolder() {
        final File file = new File(Utility.CONFIG_FILES_DIR);
        boolean mkdir = true;
        if (!file.exists()) {
            mkdir = file.mkdir();
        }
        if (!mkdir) {
            RobotLog.e("Can't create the Robot Config Files directory!");
            this.complainToast("Can't create the Robot Config Files directory!", (Context) this.a);
        }
    }

    public ArrayList<String> getXMLFiles() {
        final File[] listFiles = new File(Utility.CONFIG_FILES_DIR).listFiles();
        if (listFiles == null) {
            RobotLog.i("robotConfigFiles directory is empty");
            return new ArrayList<String>();
        }
        final ArrayList<String> list = new ArrayList<String>();
        for (final File file : listFiles) {
            if (file.isFile()) {
                final String name = file.getName();
                if (Pattern.compile("(?i).xml").matcher(name).find()) {
                    list.add(name.replaceFirst("[.][^.]+$", ""));
                }
            }
        }
        return list;
    }

    public boolean writeXML(final Map<SerialNumber, ControllerConfiguration> deviceControllers) {
        final ArrayList<ControllerConfiguration> deviceControllerConfigurations = new ArrayList<ControllerConfiguration>();
        deviceControllerConfigurations.addAll(deviceControllers.values());
        try {
            this.e = this.d.writeXml(deviceControllerConfigurations);
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("Duplicate name")) {
                this.complainToast("Found " + ex.getMessage(), (Context) this.a);
                RobotLog.e("Found " + ex.getMessage());
                return true;
            }
        }
        return false;
    }

    public void writeToFile(final String filename) throws RobotCoreException, IOException {
        this.d.writeToFile(this.e, Utility.CONFIG_FILES_DIR, filename);
    }

    public String getOutput() {
        return this.e;
    }

    public void complainToast(final String msg, final Context context) {
        final Toast text = Toast.makeText(context, (CharSequence) msg, 0);
        text.setGravity(17, 0, 0);
        final TextView textView = (TextView) text.getView().findViewById(16908299);
        textView.setTextColor(-1);
        textView.setTextSize(18.0f);
        text.show();
    }

    public void createLists(final Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> entries, final Map<SerialNumber, ControllerConfiguration> deviceControllers) {
        for (final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : entries) {
            switch (entry.getValue()) {
                case MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER: {
                    deviceControllers.put(entry.getKey(), this.buildMotorController(entry.getKey()));
                    break;
                }
                case MODERN_ROBOTICS_USB_SERVO_CONTROLLER: {
                    deviceControllers.put(entry.getKey(), this.buildServoController(entry.getKey()));
                    break;
                }
                case MODERN_ROBOTICS_USB_LEGACY_MODULE: {
                    deviceControllers.put(entry.getKey(), this.buildLegacyModule(entry.getKey()));
                    break;
                }
                case MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE: {
                    deviceControllers.put(entry.getKey(), this.buildDeviceInterfaceModule(entry.getKey()));
                    break;
                }
            }
        }
    }

    public DeviceInterfaceModuleConfiguration buildDeviceInterfaceModule(final SerialNumber serialNumber) {
        final DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration("Device Interface Module " + Utility.c, serialNumber);
        deviceInterfaceModuleConfiguration.setPwmDevices(this.createPWMList());
        deviceInterfaceModuleConfiguration.setI2cDevices(this.createI2CList());
        deviceInterfaceModuleConfiguration.setAnalogInputDevices(this.createAnalogInputList());
        deviceInterfaceModuleConfiguration.setDigitalDevices(this.createDigitalList());
        deviceInterfaceModuleConfiguration.setAnalogOutputDevices(this.createAnalogOutputList());
        ++Utility.c;
        return deviceInterfaceModuleConfiguration;
    }

    public LegacyModuleControllerConfiguration buildLegacyModule(final SerialNumber serialNumber) {
        final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = new LegacyModuleControllerConfiguration("Legacy Module " + Utility.c, this.createLegacyModuleList(), serialNumber);
        ++Utility.c;
        return legacyModuleControllerConfiguration;
    }

    public ServoControllerConfiguration buildServoController(final SerialNumber serialNumber) {
        final ServoControllerConfiguration servoControllerConfiguration = new ServoControllerConfiguration("Servo Controller " + Utility.c, this.createServoList(), serialNumber);
        ++Utility.c;
        return servoControllerConfiguration;
    }

    public MotorControllerConfiguration buildMotorController(final SerialNumber serialNumber) {
        final MotorControllerConfiguration motorControllerConfiguration = new MotorControllerConfiguration("Motor Controller " + Utility.c, this.createMotorList(), serialNumber);
        ++Utility.c;
        return motorControllerConfiguration;
    }

    public ArrayList<DeviceConfiguration> createMotorList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        list.add(new MotorConfiguration(1));
        list.add(new MotorConfiguration(2));
        return (ArrayList<DeviceConfiguration>) list;
    }

    public ArrayList<DeviceConfiguration> createServoList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 1; i <= 6; ++i) {
            list.add(new ServoConfiguration(i));
        }
        return (ArrayList<DeviceConfiguration>) list;
    }

    public ArrayList<DeviceConfiguration> createLegacyModuleList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 6; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.NOTHING));
        }
        return list;
    }

    public ArrayList<DeviceConfiguration> createPWMList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 2; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE));
        }
        return list;
    }

    public ArrayList<DeviceConfiguration> createI2CList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 6; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.I2C_DEVICE));
        }
        return list;
    }

    public ArrayList<DeviceConfiguration> createAnalogInputList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 8; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.ANALOG_INPUT));
        }
        return list;
    }

    public ArrayList<DeviceConfiguration> createDigitalList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 8; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE));
        }
        return list;
    }

    public ArrayList<DeviceConfiguration> createAnalogOutputList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 2; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT));
        }
        return list;
    }

    public void updateHeader(final String default_name, final int pref_hardware_config_filename_id, final int fileTextView, final int header_id) {
        final String replaceFirst = this.b.getString(this.a.getString(pref_hardware_config_filename_id), default_name).replaceFirst("[.][^.]+$", "");
        ((TextView) this.a.findViewById(fileTextView)).setText((CharSequence) replaceFirst);
        if (replaceFirst.equalsIgnoreCase("No current file!")) {
            this.changeBackground(Color.parseColor("#bf0510"), header_id);
        } else if (replaceFirst.toLowerCase().contains("Unsaved".toLowerCase())) {
            this.changeBackground(-12303292, header_id);
        } else {
            this.changeBackground(Color.parseColor("#790E15"), header_id);
        }
    }

    public void saveToPreferences(String filename, final int pref_hardware_config_filename_id) {
        filename = filename.replaceFirst("[.][^.]+$", "");
        final SharedPreferences.Editor edit = this.b.edit();
        edit.putString(this.a.getString(pref_hardware_config_filename_id), filename);
        edit.apply();
    }

    public void changeBackground(final int color, final int header_id) {
        ((LinearLayout) this.a.findViewById(header_id)).setBackgroundColor(color);
    }

    public String getFilenameFromPrefs(final int pref_hardware_config_filename_id, final String default_name) {
        return this.b.getString(this.a.getString(pref_hardware_config_filename_id), default_name);
    }

    public void resetCount() {
        Utility.c = 1;
    }

    public void setOrangeText(final String msg0, final String msg1, final int info_id, final int layout_id, final int orange0, final int orange1) {
        final LinearLayout linearLayout = (LinearLayout) this.a.findViewById(info_id);
        linearLayout.setVisibility(0);
        linearLayout.removeAllViews();
        this.a.getLayoutInflater().inflate(layout_id, (ViewGroup) linearLayout, true);
        final TextView textView = (TextView) linearLayout.findViewById(orange0);
        final TextView textView2 = (TextView) linearLayout.findViewById(orange1);
        textView2.setGravity(3);
        textView.setText((CharSequence) msg0);
        textView2.setText((CharSequence) msg1);
    }

    public void confirmSave() {
        final Toast text = Toast.makeText((Context) this.a, (CharSequence) "Saved", 0);
        text.setGravity(80, 0, 50);
        text.show();
    }

    public AlertDialog.Builder buildBuilder(final String title, final String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context) this.a);
        builder.setTitle((CharSequence) title).setMessage((CharSequence) message);
        return builder;
    }

    public String prepareFilename(String currentFile) {
        if (currentFile.toLowerCase().contains("Unsaved".toLowerCase())) {
            currentFile = currentFile.substring(7).trim();
        }
        if (currentFile.equalsIgnoreCase("No current file!")) {
            currentFile = "";
        }
        return currentFile;
    }

    static {
        CONFIG_FILES_DIR = Environment.getExternalStorageDirectory() + "/FIRST/";
        Utility.c = 1;
    }
}
