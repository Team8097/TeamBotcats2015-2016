// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.ftccommon;

import android.widget.ListAdapter;

import com.qualcomm.robotcore.util.Version;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.widget.ListView;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import android.app.Activity;
import com.qualcomm.ftccommon.R;

public class AboutActivity extends Activity
{
    WifiDirectAssistant a;
    
    public AboutActivity() {
        this.a = null;
    }
    
    protected void onStart() {
        super.onStart();
        this.setContentView(R.layout.activity_about);
        final ListView listView = (ListView)this.findViewById(R.id.aboutList);
        try {
            (this.a = WifiDirectAssistant.getWifiDirectAssistant((Context)null)).enable();
        }
        catch (NullPointerException ex) {
            RobotLog.i("Cannot start Wifi Direct Assistant");
            this.a = null;
        }
        listView.setAdapter((ListAdapter)new ArrayAdapter<Item>(this, 17367044, 16908308) {
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                final TextView textView = (TextView)view.findViewById(16908308);
                final TextView textView2 = (TextView)view.findViewById(16908309);
                final Item a = this.a(position);
                textView.setText((CharSequence)a.title);
                textView2.setText((CharSequence)a.info);
                return view;
            }
            
            public int getCount() {
                return 3;
            }
            
            public Item a(final int n) {
                switch (n) {
                    case 0: {
                        return this.a();
                    }
                    case 1: {
                        return this.b();
                    }
                    case 2: {
                        return this.c();
                    }
                    default: {
                        return new Item();
                    }
                }
            }
            
            private Item a() {
                final Item item = new Item();
                item.title = "App Version";
                try {
                    item.info = AboutActivity.this.getPackageManager().getPackageInfo(AboutActivity.this.getPackageName(), 0).versionName;
                }
                catch (PackageManager.NameNotFoundException ex) {
                    item.info = ex.getMessage();
                }
                return item;
            }
            
            private Item b() {
                final Item item = new Item();
                item.title = "Library Version";
                item.info = Version.getLibraryVersion();
                return item;
            }
            
            private Item c() {
                final Item item = new Item();
                item.title = "Wifi Direct Information";
                item.info = "unavailable";
                final StringBuilder sb = new StringBuilder();
                if (AboutActivity.this.a != null && AboutActivity.this.a.isEnabled()) {
                    sb.append("Name: ").append(AboutActivity.this.a.getDeviceName());
                    if (AboutActivity.this.a.isGroupOwner()) {
                        sb.append("\nIP Address").append(AboutActivity.this.a.getGroupOwnerAddress().getHostAddress());
                        sb.append("\nPassphrase: ").append(AboutActivity.this.a.getPassphrase());
                        sb.append("\nGroup Owner");
                    }
                    else if (AboutActivity.this.a.isConnected()) {
                        sb.append("\nGroup Owner: ").append(AboutActivity.this.a.getGroupOwnerName());
                        sb.append("\nConnected");
                    }
                    else {
                        sb.append("\nNo connection information");
                    }
                    item.info = sb.toString();
                }
                return item;
            }
        });
    }
    
    protected void onStop() {
        super.onStop();
        if (this.a != null) {
            this.a.disable();
        }
    }
    
    public static class Item
    {
        public String title;
        public String info;
        
        public Item() {
            this.title = "";
            this.info = "";
        }
    }
}
