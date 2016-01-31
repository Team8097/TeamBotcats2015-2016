// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;

public class DeviceInfoAdapter extends BaseAdapter implements ListAdapter
{
    private Map<SerialNumber, ControllerConfiguration> a;
    private SerialNumber[] b;
    private Context c;
    private int d;
    private int e;
    
    public DeviceInfoAdapter(final Activity context, final int list_id, final Map<SerialNumber, ControllerConfiguration> deviceControllers) {
        this.a = new HashMap<SerialNumber, ControllerConfiguration>();
        this.c = (Context)context;
        this.a = deviceControllers;
        this.b = deviceControllers.keySet().toArray(new SerialNumber[deviceControllers.size()]);
        this.d = list_id;
        this.e = this.e;
    }
    
    public int getCount() {
        return this.a.size();
    }
    
    public Object getItem(final int arg0) {
        return this.a.get(this.b[arg0]);
    }
    
    public long getItemId(final int arg0) {
        return 0L;
    }
    
    public View getView(final int pos, final View convertView, final ViewGroup parent) {
        View inflate = convertView;
        if (inflate == null) {
            inflate = ((Activity)this.c).getLayoutInflater().inflate(this.d, parent, false);
        }
        ((TextView)inflate.findViewById(16908309)).setText((CharSequence)this.b[pos].toString());
        ((TextView)inflate.findViewById(16908308)).setText((CharSequence)this.a.get(this.b[pos]).getName());
        return inflate;
    }
}
