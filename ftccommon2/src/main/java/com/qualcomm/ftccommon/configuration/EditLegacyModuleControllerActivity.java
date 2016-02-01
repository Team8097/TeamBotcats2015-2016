// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import android.widget.Button;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import java.util.List;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import android.content.Intent;
import java.io.Serializable;
import android.text.TextWatcher;
import com.qualcomm.robotcore.util.RobotLog;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.view.ViewGroup;
import com.qualcomm.ftccommon.R;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.view.View;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import android.content.Context;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditLegacyModuleControllerActivity extends Activity
{
    private static boolean a;
    private Utility b;
    private String c;
    private Context d;
    public static final String EDIT_LEGACY_CONFIG = "EDIT_LEGACY_CONFIG";
    public static final int EDIT_MOTOR_CONTROLLER_REQUEST_CODE = 101;
    public static final int EDIT_SERVO_CONTROLLER_REQUEST_CODE = 102;
    public static final int EDIT_MATRIX_CONTROLLER_REQUEST_CODE = 103;
    private LegacyModuleControllerConfiguration e;
    private EditText f;
    private ArrayList<DeviceConfiguration> g;
    private View h;
    private View i;
    private View j;
    private View k;
    private View l;
    private View m;
    private AdapterView.OnItemSelectedListener n;
    
    public EditLegacyModuleControllerActivity() {
        this.g = new ArrayList<DeviceConfiguration>();
        this.n = (AdapterView.OnItemSelectedListener)new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long l) {
                final String string = parent.getItemAtPosition(pos).toString();
                final LinearLayout linearLayout = (LinearLayout)view.getParent().getParent().getParent();
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
                    EditLegacyModuleControllerActivity.this.a(linearLayout);
                }
                else {
                    EditLegacyModuleControllerActivity.this.a(linearLayout, string);
                }
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        };
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.legacy);
        this.h = this.getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)this.findViewById(R.id.linearLayout0), true);
        ((TextView)this.h.findViewById(R.id.portNumber)).setText((CharSequence)"0");
        this.i = this.getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)this.findViewById(R.id.linearLayout1), true);
        ((TextView)this.i.findViewById(R.id.portNumber)).setText((CharSequence)"1");
        this.j = this.getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)this.findViewById(R.id.linearLayout2), true);
        ((TextView)this.j.findViewById(R.id.portNumber)).setText((CharSequence)"2");
        this.k = this.getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)this.findViewById(R.id.linearLayout3), true);
        ((TextView)this.k.findViewById(R.id.portNumber)).setText((CharSequence)"3");
        this.l = this.getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)this.findViewById(R.id.linearLayout4), true);
        ((TextView)this.l.findViewById(R.id.portNumber)).setText((CharSequence)"4");
        this.m = this.getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)this.findViewById(R.id.linearLayout5), true);
        ((TextView)this.m.findViewById(R.id.portNumber)).setText((CharSequence)"5");
        PreferenceManager.setDefaultValues(this.d = (Context)this, R.xml.preferences, false);
        this.b = new Utility((Activity)this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.f = (EditText)this.findViewById(R.id.device_interface_module_name);
    }
    
    protected void onStart() {
        super.onStart();
        this.b.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        this.c = this.b.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("EDIT_LEGACY_CONFIG");
        if (serializableExtra != null) {
            this.e = (LegacyModuleControllerConfiguration)serializableExtra;
            this.g = (ArrayList<DeviceConfiguration>)this.e.getDevices();
            this.f.setText((CharSequence)this.e.getName());
            this.f.addTextChangedListener((TextWatcher)new a());
            ((TextView)this.findViewById(R.id.legacy_serialNumber)).setText((CharSequence)this.e.getSerialNumber().toString());
            for (int i = 0; i < this.g.size(); ++i) {
                final DeviceConfiguration deviceConfiguration = this.g.get(i);
                if (EditLegacyModuleControllerActivity.a) {
                    RobotLog.e("[onStart] module name: " + deviceConfiguration.getName() + ", port: " + deviceConfiguration.getPort() + ", type: " + deviceConfiguration.getType());
                }
                this.a(this.a(i), deviceConfiguration);
            }
        }
    }
    
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        Object o = null;
        if (resultCode == -1) {
            if (requestCode == 101) {
                o = data.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
            }
            else if (requestCode == 102) {
                o = data.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
            }
            else if (requestCode == 103) {
                o = data.getSerializableExtra("Edit Matrix ControllerConfiguration Activity");
            }
            if (o != null) {
                final ControllerConfiguration controllerConfiguration = (ControllerConfiguration)o;
                this.b((DeviceConfiguration)controllerConfiguration);
                this.a(this.a(controllerConfiguration.getPort()), this.g.get(controllerConfiguration.getPort()));
                if (!this.c.toLowerCase().contains("Unsaved".toLowerCase())) {
                    final String string = "Unsaved " + this.c;
                    this.b.saveToPreferences(string, R.string.pref_hardware_config_filename);
                    this.c = string;
                }
                this.b.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
            }
        }
    }
    
    public void saveLegacyController(final View v) {
        this.a();
    }
    
    private void a() {
        final Intent intent = new Intent();
        this.e.setName(this.f.getText().toString());
        intent.putExtra("EDIT_LEGACY_CONFIG", (Serializable)this.e);
        this.setResult(-1, intent);
        this.finish();
    }
    
    public void cancelLegacyController(final View v) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    private void a(final View view, final DeviceConfiguration deviceConfiguration) {
        final Spinner spinner = (Spinner)view.findViewById(R.id.choiceSpinner_legacyModule);
        spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition((Object)deviceConfiguration.getType().toString()));
        spinner.setOnItemSelectedListener(this.n);
        final String name = deviceConfiguration.getName();
        final EditText editText = (EditText)view.findViewById(R.id.editTextResult_name);
        final int int1 = Integer.parseInt(((TextView)view.findViewById(R.id.portNumber)).getText().toString());
        editText.addTextChangedListener((TextWatcher)new a(this.a(int1)));
        editText.setText((CharSequence)name);
        if (EditLegacyModuleControllerActivity.a) {
            RobotLog.e("[populatePort] name: " + name + ", port: " + int1 + ", type: " + deviceConfiguration.getType());
        }
    }
    
    private void a(final LinearLayout linearLayout) {
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.portNumber)).getText().toString());
        final EditText editText = (EditText)linearLayout.findViewById(R.id.editTextResult_name);
        editText.setEnabled(false);
        editText.setText((CharSequence)"NO DEVICE ATTACHED");
        final DeviceConfiguration deviceConfiguration = new DeviceConfiguration(DeviceConfiguration.ConfigurationType.NOTHING);
        deviceConfiguration.setPort(int1);
        this.b(deviceConfiguration);
        this.a(int1, 8);
    }
    
    private void a(final LinearLayout linearLayout, final String s) {
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.portNumber)).getText().toString());
        final EditText editText = (EditText)linearLayout.findViewById(R.id.editTextResult_name);
        final DeviceConfiguration deviceConfiguration = this.g.get(int1);
        editText.setEnabled(true);
        this.a(editText, deviceConfiguration);
        final DeviceConfiguration.ConfigurationType typeFromString = deviceConfiguration.typeFromString(s);
        if (typeFromString == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER || typeFromString == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER || typeFromString == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
            this.a(int1, s);
            this.a(int1, 0);
        }
        else {
            deviceConfiguration.setType(typeFromString);
            if (typeFromString == DeviceConfiguration.ConfigurationType.NOTHING) {
                deviceConfiguration.setEnabled(false);
            }
            else {
                deviceConfiguration.setEnabled(true);
            }
            this.a(int1, 8);
        }
        if (EditLegacyModuleControllerActivity.a) {
            final DeviceConfiguration deviceConfiguration2 = this.g.get(int1);
            RobotLog.e("[changeDevice] modules.get(port) name: " + deviceConfiguration2.getName() + ", port: " + deviceConfiguration2.getPort() + ", type: " + deviceConfiguration2.getType());
        }
    }
    
    private void a(final int port, final String s) {
        final DeviceConfiguration deviceConfiguration = this.g.get(port);
        final String name = deviceConfiguration.getName();
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        final SerialNumber no_SERIAL_NUMBER = ControllerConfiguration.NO_SERIAL_NUMBER;
        if (!deviceConfiguration.getType().toString().equalsIgnoreCase(s)) {
            Object o = new ControllerConfiguration("dummy module", (List)list, no_SERIAL_NUMBER, DeviceConfiguration.ConfigurationType.NOTHING);
            if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
                for (int i = 1; i <= 2; ++i) {
                    list.add(new MotorConfiguration(i));
                }
                o = new MotorControllerConfiguration(name, (List)list, no_SERIAL_NUMBER);
                ((ControllerConfiguration)o).setPort(port);
            }
            else if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                for (int j = 1; j <= 6; ++j) {
                    list.add(new ServoConfiguration(j));
                }
                o = new ServoControllerConfiguration(name, (List)list, no_SERIAL_NUMBER);
                ((ControllerConfiguration)o).setPort(port);
            }
            else if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                final ArrayList<MotorConfiguration> list2 = new ArrayList<MotorConfiguration>();
                for (int k = 1; k <= 4; ++k) {
                    list2.add(new MotorConfiguration(k));
                }
                final ArrayList<ServoConfiguration> list3 = new ArrayList<ServoConfiguration>();
                for (int l = 1; l <= 4; ++l) {
                    list3.add(new ServoConfiguration(l));
                }
                o = new MatrixControllerConfiguration(name, (List)list2, (List)list3, no_SERIAL_NUMBER);
                ((ControllerConfiguration)o).setPort(port);
            }
            ((ControllerConfiguration)o).setEnabled(true);
            this.b((DeviceConfiguration)o);
        }
    }
    
    public void editController_portALL(final View v) {
        final LinearLayout linearLayout = (LinearLayout)v.getParent().getParent();
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.portNumber)).getText().toString());
        final DeviceConfiguration deviceConfiguration = this.g.get(int1);
        if (!this.c(deviceConfiguration)) {
            this.a(int1, ((Spinner)linearLayout.findViewById(R.id.choiceSpinner_legacyModule)).getSelectedItem().toString());
        }
        this.a(deviceConfiguration);
    }
    
    private void a(final DeviceConfiguration deviceConfiguration) {
        deviceConfiguration.setName(((EditText)((LinearLayout)this.a(deviceConfiguration.getPort())).findViewById(R.id.editTextResult_name)).getText().toString());
        if (deviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER) {
            final int n = 101;
            final Intent intent = new Intent(this.d, (Class)EditMotorControllerActivity.class);
            intent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", (Serializable)deviceConfiguration);
            intent.putExtra("requestCode", n);
            this.setResult(-1, intent);
            this.startActivityForResult(intent, n);
        }
        else if (deviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER) {
            final int n2 = 102;
            final Intent intent2 = new Intent(this.d, (Class)EditServoControllerActivity.class);
            intent2.putExtra("Edit Servo ControllerConfiguration Activity", (Serializable)deviceConfiguration);
            this.setResult(-1, intent2);
            this.startActivityForResult(intent2, n2);
        }
        else if (deviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
            final int n3 = 103;
            final Intent intent3 = new Intent(this.d, (Class)EditMatrixControllerActivity.class);
            intent3.putExtra("Edit Matrix ControllerConfiguration Activity", (Serializable)deviceConfiguration);
            this.setResult(-1, intent3);
            this.startActivityForResult(intent3, n3);
        }
    }
    
    private void b(final DeviceConfiguration deviceConfiguration) {
        this.g.set(deviceConfiguration.getPort(), deviceConfiguration);
    }
    
    private View a(final int n) {
        switch (n) {
            case 0: {
                return this.h;
            }
            case 1: {
                return this.i;
            }
            case 2: {
                return this.j;
            }
            case 3: {
                return this.k;
            }
            case 4: {
                return this.l;
            }
            case 5: {
                return this.m;
            }
            default: {
                return null;
            }
        }
    }
    
    private void a(final int n, final int visibility) {
        ((Button)this.a(n).findViewById(R.id.edit_controller_btn)).setVisibility(visibility);
    }
    
    private boolean c(final DeviceConfiguration deviceConfiguration) {
        return deviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER || deviceConfiguration.getType() == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER;
    }
    
    private void a(final EditText editText, final DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
            editText.setText((CharSequence)"");
            deviceConfiguration.setName("");
        }
        else {
            editText.setText((CharSequence)deviceConfiguration.getName());
        }
    }
    
    static {
        EditLegacyModuleControllerActivity.a = false;
    }
    
    private class a implements TextWatcher
    {
        private int b;
        private boolean c;
        
        private a() {
            this.c = false;
            this.c = true;
        }
        
        private a(final View view) {
            this.c = false;
            this.b = Integer.parseInt(((TextView)view.findViewById(R.id.portNumber)).getText().toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }
        
        public void afterTextChanged(final Editable editable) {
            final String string = editable.toString();
            if (this.c) {
                EditLegacyModuleControllerActivity.this.e.setName(string);
            }
            else {
                EditLegacyModuleControllerActivity.this.g.get(this.b).setName(string);
            }
        }
    }
}
