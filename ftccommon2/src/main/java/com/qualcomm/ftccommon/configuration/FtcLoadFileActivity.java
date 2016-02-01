// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon.configuration;

import android.widget.LinearLayout;
import com.qualcomm.ftccommon.DbgLog;
import java.io.File;
import android.content.Intent;
import java.util.Iterator;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import com.qualcomm.ftccommon.R;
import android.os.Bundle;
import android.content.DialogInterface;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.content.Context;
import java.util.ArrayList;
import android.app.Activity;

public class FtcLoadFileActivity extends Activity
{
    private ArrayList<String> b;
    private Context c;
    private Utility d;
    DialogInterface.OnClickListener a;
    public static final String CONFIGURE_FILENAME = "CONFIGURE_FILENAME";
    
    public FtcLoadFileActivity() {
        this.b = new ArrayList<String>();
        this.a = (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int button) {
            }
        };
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_load);
        this.c = (Context)this;
        (this.d = new Utility((Activity)this)).createConfigFolder();
        this.a();
    }
    
    protected void onStart() {
        super.onStart();
        this.b = (ArrayList<String>)this.d.getXMLFiles();
        this.b();
        this.d.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        this.c();
    }
    
    private void a() {
        ((Button)this.findViewById(R.id.files_holder).findViewById(R.id.info_btn)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                final AlertDialog.Builder buildBuilder = FtcLoadFileActivity.this.d.buildBuilder("Available files", "These are the files the Hardware Wizard was able to find. You can edit each file by clicking the edit button. The 'Activate' button will set that file as the current configuration file, which will be used to start the robot.");
                buildBuilder.setPositiveButton((CharSequence)"Ok", FtcLoadFileActivity.this.a);
                final AlertDialog create = buildBuilder.create();
                create.show();
                ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
            }
        });
        ((Button)this.findViewById(R.id.autoconfigure_holder).findViewById(R.id.info_btn)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                final AlertDialog.Builder buildBuilder = FtcLoadFileActivity.this.d.buildBuilder("AutoConfigure", "This is the fastest way to get a new machine up and running. The AutoConfigure tool will automatically enter some default names for devices. AutoConfigure expects certain devices.  If there are other devices attached, the AutoConfigure tool will not name them.");
                buildBuilder.setPositiveButton((CharSequence)"Ok", FtcLoadFileActivity.this.a);
                final AlertDialog create = buildBuilder.create();
                create.show();
                ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
            }
        });
    }
    
    private void b() {
        if (this.b.size() == 0) {
            this.d.setOrangeText("No files found!", "In order to proceed, you must create a new file", R.id.empty_filelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
        }
        else {
            final ViewGroup viewGroup = (ViewGroup)this.findViewById(R.id.empty_filelist);
            viewGroup.removeAllViews();
            viewGroup.setVisibility(8);
        }
    }
    
    private void c() {
        final ViewGroup viewGroup = (ViewGroup)this.findViewById(R.id.inclusionlayout);
        viewGroup.removeAllViews();
        for (final String text : this.b) {
            final View inflate = LayoutInflater.from((Context)this).inflate(R.layout.file_info, (ViewGroup)null);
            viewGroup.addView(inflate);
            ((TextView)inflate.findViewById(R.id.filename_editText)).setText((CharSequence)text);
        }
    }
    
    public void new_button(final View v) {
        this.d.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
        this.startActivity(new Intent(this.c, (Class)FtcConfigurationActivity.class));
    }
    
    public void file_edit_button(final View v) {
        this.d.saveToPreferences(this.a(v, true), R.string.pref_hardware_config_filename);
        this.startActivity(new Intent(this.c, (Class)FtcConfigurationActivity.class));
    }
    
    public void file_activate_button(final View v) {
        this.d.saveToPreferences(this.a(v, false), R.string.pref_hardware_config_filename);
        this.d.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
    }
    
    public void file_delete_button(final View v) {
        final String a = this.a(v, true);
        final File file = new File(Utility.CONFIG_FILES_DIR + a);
        if (file.exists()) {
            file.delete();
        }
        else {
            this.d.complainToast("That file does not exist: " + a, this.c);
            DbgLog.error("Tried to delete a file that does not exist: " + a);
        }
        this.b = (ArrayList<String>)this.d.getXMLFiles();
        this.d.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
        this.d.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        this.c();
    }
    
    private String a(final View view, final boolean b) {
        String s = ((TextView)((LinearLayout)((LinearLayout)view.getParent()).getParent()).findViewById(R.id.filename_editText)).getText().toString();
        if (b) {
            s += ".xml";
        }
        return s;
    }
    
    public void launch_autoConfigure(final View v) {
        this.startActivity(new Intent(this.getBaseContext(), (Class)AutoConfigureActivity.class));
    }
    
    public void onBackPressed() {
        final String filenameFromPrefs = this.d.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        final Intent intent = new Intent();
        intent.putExtra("CONFIGURE_FILENAME", filenameFromPrefs);
        this.setResult(-1, intent);
        this.finish();
    }
}
