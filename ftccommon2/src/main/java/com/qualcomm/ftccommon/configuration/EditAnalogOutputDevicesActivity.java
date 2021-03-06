// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import android.content.Intent;
import java.io.Serializable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Iterator;
import android.widget.TextView;
import android.view.ViewGroup;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.preference.PreferenceManager;
import com.qualcomm.ftccommon.R;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import android.view.View;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditAnalogOutputDevicesActivity extends Activity
{
    private Utility a;
    private View b;
    private View c;
    private ArrayList<DeviceConfiguration> d;
    private AdapterView.OnItemSelectedListener e;
    
    public EditAnalogOutputDevicesActivity() {
        this.d = new ArrayList<DeviceConfiguration>();
        this.e = (AdapterView.OnItemSelectedListener)new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long l) {
                final String string = parent.getItemAtPosition(pos).toString();
                final LinearLayout linearLayout = (LinearLayout)view.getParent().getParent().getParent();
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
                    EditAnalogOutputDevicesActivity.this.a(linearLayout);
                }
                else {
                    EditAnalogOutputDevicesActivity.this.a(linearLayout, string);
                }
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        };
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.analog_outputs);
        PreferenceManager.setDefaultValues((Context)this, R.xml.preferences, false);
        this.a = new Utility((Activity)this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.b = this.getLayoutInflater().inflate(R.layout.analog_output_device, (ViewGroup)this.findViewById(R.id.linearLayout_analogOutput0), true);
        ((TextView)this.b.findViewById(R.id.port_number_analogOutput)).setText((CharSequence)"0");
        this.c = this.getLayoutInflater().inflate(R.layout.analog_output_device, (ViewGroup)this.findViewById(R.id.linearLayout_analogOutput1), true);
        ((TextView)this.c.findViewById(R.id.port_number_analogOutput)).setText((CharSequence)"1");
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            for (final String s : extras.keySet()) {
                this.d.add(Integer.parseInt(s), (DeviceConfiguration)extras.getSerializable(s));
            }
            for (int i = 0; i < this.d.size(); ++i) {
                final View a = this.a(i);
                final DeviceConfiguration deviceConfiguration = this.d.get(i);
                this.a(a);
                this.b(a, deviceConfiguration);
                this.a(a, deviceConfiguration);
            }
        }
    }
    
    private void a(final View view, final DeviceConfiguration deviceConfiguration) {
        final Spinner spinner = (Spinner)view.findViewById(R.id.choiceSpinner_analogOutput);
        final ArrayAdapter arrayAdapter = (ArrayAdapter)spinner.getAdapter();
        if (deviceConfiguration.isEnabled()) {
            spinner.setSelection(arrayAdapter.getPosition((Object)deviceConfiguration.getType().toString()));
        }
        else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this.e);
    }
    
    private void b(final View view, final DeviceConfiguration deviceConfiguration) {
        final EditText editText = (EditText)view.findViewById(R.id.editTextResult_analogOutput);
        if (deviceConfiguration.isEnabled()) {
            editText.setText((CharSequence)deviceConfiguration.getName());
            editText.setEnabled(true);
        }
        else {
            editText.setText((CharSequence)"NO DEVICE ATTACHED");
            editText.setEnabled(false);
        }
    }
    
    private void a(final View view) {
        ((EditText)view.findViewById(R.id.editTextResult_analogOutput)).addTextChangedListener((TextWatcher)new a(view));
    }
    
    private View a(final int n) {
        switch (n) {
            case 0: {
                return this.b;
            }
            case 1: {
                return this.c;
            }
            default: {
                return null;
            }
        }
    }
    
    public void saveanalogOutputDevices(final View v) {
        this.a();
    }
    
    private void a() {
        final Bundle bundle = new Bundle();
        for (int i = 0; i < this.d.size(); ++i) {
            bundle.putSerializable(String.valueOf(i), (Serializable)this.d.get(i));
        }
        final Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        this.setResult(-1, intent);
        this.finish();
    }
    
    public void cancelanalogOutputDevices(final View v) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    private void a(final LinearLayout linearLayout) {
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.port_number_analogOutput)).getText().toString());
        final EditText editText = (EditText)linearLayout.findViewById(R.id.editTextResult_analogOutput);
        editText.setEnabled(false);
        editText.setText((CharSequence)"NO DEVICE ATTACHED");
        this.d.get(int1).setEnabled(false);
    }
    
    private void a(final LinearLayout linearLayout, final String s) {
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.port_number_analogOutput)).getText().toString());
        final EditText editText = (EditText)linearLayout.findViewById(R.id.editTextResult_analogOutput);
        editText.setEnabled(true);
        final DeviceConfiguration deviceConfiguration = this.d.get(int1);
        deviceConfiguration.setType(deviceConfiguration.typeFromString(s));
        deviceConfiguration.setEnabled(true);
        this.a(editText, deviceConfiguration);
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
    
    private class a implements TextWatcher
    {
        private int b;
        
        private a(final View view) {
            this.b = Integer.parseInt(((TextView)view.findViewById(R.id.port_number_analogOutput)).getText().toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        }
        
        public void afterTextChanged(final Editable editable) {
            EditAnalogOutputDevicesActivity.this.d.get(this.b).setName(editable.toString());
        }
    }
}
