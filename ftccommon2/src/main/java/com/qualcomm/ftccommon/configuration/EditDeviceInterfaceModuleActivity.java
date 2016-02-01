// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import java.util.Iterator;
import android.content.Intent;
import java.util.List;
import java.io.Serializable;
import android.widget.TextView;
import android.text.TextWatcher;
import com.qualcomm.robotcore.util.RobotLog;
import android.preference.PreferenceManager;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.qualcomm.ftccommon.R;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import android.content.Context;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditDeviceInterfaceModuleActivity extends Activity
{
    private Utility a;
    private String b;
    private Context c;
    public static final String EDIT_DEVICE_INTERFACE_MODULE_CONFIG = "EDIT_DEVICE_INTERFACE_MODULE_CONFIG";
    public static final int EDIT_PWM_PORT_REQUEST_CODE = 201;
    public static final int EDIT_I2C_PORT_REQUEST_CODE = 202;
    public static final int EDIT_ANALOG_INPUT_REQUEST_CODE = 203;
    public static final int EDIT_DIGITAL_REQUEST_CODE = 204;
    public static final int EDIT_ANALOG_OUTPUT_REQUEST_CODE = 205;
    private DeviceInterfaceModuleConfiguration d;
    private EditText e;
    private ArrayList<DeviceConfiguration> f;
    private AdapterView.OnItemClickListener g;
    
    public EditDeviceInterfaceModuleActivity() {
        this.f = new ArrayList<DeviceConfiguration>();
        this.g = (AdapterView.OnItemClickListener)new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                switch (position) {
                    case 0: {
                        EditDeviceInterfaceModuleActivity.this.a(201, EditDeviceInterfaceModuleActivity.this.d.getPwmDevices(), EditPWMDevicesActivity.class);
                        break;
                    }
                    case 1: {
                        EditDeviceInterfaceModuleActivity.this.a(202, EditDeviceInterfaceModuleActivity.this.d.getI2cDevices(), EditI2cDevicesActivity.class);
                        break;
                    }
                    case 2: {
                        EditDeviceInterfaceModuleActivity.this.a(203, EditDeviceInterfaceModuleActivity.this.d.getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
                        break;
                    }
                    case 3: {
                        EditDeviceInterfaceModuleActivity.this.a(204, EditDeviceInterfaceModuleActivity.this.d.getDigitalDevices(), EditDigitalDevicesActivity.class);
                        break;
                    }
                    case 4: {
                        EditDeviceInterfaceModuleActivity.this.a(205, EditDeviceInterfaceModuleActivity.this.d.getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
                        break;
                    }
                }
            }
        };
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.device_interface_module);
        final String[] stringArray = this.getResources().getStringArray(R.array.device_interface_module_options_array);
        final ListView listView = (ListView)this.findViewById(R.id.listView_devices);
        listView.setAdapter((ListAdapter)new ArrayAdapter((Context)this, 17367043, (Object[])stringArray));
        listView.setOnItemClickListener(this.g);
        PreferenceManager.setDefaultValues(this.c = (Context)this, R.xml.preferences, false);
        this.a = new Utility((Activity)this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        (this.e = (EditText)this.findViewById(R.id.device_interface_module_name)).addTextChangedListener((TextWatcher)new a());
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        this.b = this.a.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
        if (serializableExtra != null) {
            this.d = (DeviceInterfaceModuleConfiguration)serializableExtra;
            this.f = (ArrayList<DeviceConfiguration>)this.d.getDevices();
            this.e.setText((CharSequence)this.d.getName());
            ((TextView)this.findViewById(R.id.device_interface_module_serialNumber)).setText((CharSequence)this.d.getSerialNumber().toString());
        }
    }
    
    private void a(final int n, final List<DeviceConfiguration> list, final Class clazz) {
        final Bundle bundle = new Bundle();
        for (int i = 0; i < list.size(); ++i) {
            bundle.putSerializable(String.valueOf(i), (Serializable)list.get(i));
        }
        final Intent intent = new Intent(this.c, clazz);
        intent.putExtras(bundle);
        this.setResult(-1, intent);
        this.startActivityForResult(intent, n);
    }
    
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == -1) {
            if (requestCode == 201) {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    this.d.setPwmDevices((List)this.a(extras));
                }
            }
            else if (requestCode == 203) {
                final Bundle extras2 = data.getExtras();
                if (extras2 != null) {
                    this.d.setAnalogInputDevices((List)this.a(extras2));
                }
            }
            else if (requestCode == 204) {
                final Bundle extras3 = data.getExtras();
                if (extras3 != null) {
                    this.d.setDigitalDevices((List)this.a(extras3));
                }
            }
            else if (requestCode == 202) {
                final Bundle extras4 = data.getExtras();
                if (extras4 != null) {
                    this.d.setI2cDevices((List)this.a(extras4));
                }
            }
            else if (requestCode == 205) {
                final Bundle extras5 = data.getExtras();
                if (extras5 != null) {
                    this.d.setAnalogOutputDevices((List)this.a(extras5));
                }
            }
            this.a();
        }
    }
    
    private ArrayList<DeviceConfiguration> a(final Bundle bundle) {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (final String s : bundle.keySet()) {
            list.add(Integer.parseInt(s), (DeviceConfiguration)bundle.getSerializable(s));
        }
        return list;
    }
    
    private void a() {
        if (!this.b.toLowerCase().contains("Unsaved".toLowerCase())) {
            final String string = "Unsaved " + this.b;
            this.a.saveToPreferences(string, R.string.pref_hardware_config_filename);
            this.b = string;
        }
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
    }
    
    public void saveDeviceInterface(final View v) {
        this.b();
    }
    
    private void b() {
        final Intent intent = new Intent();
        this.d.setName(this.e.getText().toString());
        intent.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", (Serializable)this.d);
        this.setResult(-1, intent);
        this.finish();
    }
    
    public void cancelDeviceInterface(final View v) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    private class a implements TextWatcher
    {
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }
        
        public void afterTextChanged(final Editable editable) {
            EditDeviceInterfaceModuleActivity.this.d.setName(editable.toString());
        }
    }
}
