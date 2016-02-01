// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import android.text.style.ForegroundColorSpan;
import android.text.SpannableString;
import android.text.Spannable;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import android.os.Environment;
import java.io.Serializable;
import java.io.IOException;
import com.qualcomm.robotcore.util.RobotLog;
import android.widget.ScrollView;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class ViewLogsActivity extends Activity
{
    TextView a;
    int b;
    public static final String FILENAME = "Filename";
    String c;
    
    public ViewLogsActivity() {
        this.b = 300;
        this.c = " ";
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_view_logs);
        this.a = (TextView)this.findViewById(R.id.textAdbLogs);
        final ScrollView scrollView = (ScrollView)this.findViewById(R.id.scrollView);
        scrollView.post((Runnable)new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(130);
            }
        });
    }
    
    protected void onStart() {
        super.onStart();
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("Filename");
        if (serializableExtra != null) {
            this.c = (String)serializableExtra;
        }
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    ViewLogsActivity.this.a.setText((CharSequence)ViewLogsActivity.this.a(ViewLogsActivity.this.readNLines(ViewLogsActivity.this.b)));
                }
                catch (IOException ex) {
                    RobotLog.e(ex.toString());
                    ViewLogsActivity.this.a.setText((CharSequence)("File not found: " + ViewLogsActivity.this.c));
                }
            }
        });
    }
    
    public String readNLines(final int n) throws IOException {
        Environment.getExternalStorageDirectory();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(this.c)));
        final String[] array = new String[n];
        int n2 = 0;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            array[n2 % array.length] = line;
            ++n2;
        }
        int n3 = n2 - n;
        if (n3 < 0) {
            n3 = 0;
        }
        String string = "";
        for (int i = n3; i < n2; ++i) {
            string = string + array[i % array.length] + "\n";
        }
        final int lastIndex = string.lastIndexOf("--------- beginning");
        if (lastIndex < 0) {
            return string;
        }
        return string.substring(lastIndex);
    }
    
    private Spannable a(final String s) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        final String[] split = s.split("\\n");
        int n = 0;
        for (final String s2 : split) {
            if (s2.contains("E/RobotCore") || s2.contains("### ERROR: ")) {
                ((Spannable)spannableString).setSpan((Object)new ForegroundColorSpan(-65536), n, n + s2.length(), 33);
            }
            n += s2.length();
            ++n;
        }
        return (Spannable)spannableString;
    }
}
