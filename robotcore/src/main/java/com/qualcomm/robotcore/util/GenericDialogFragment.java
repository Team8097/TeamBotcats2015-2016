// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.content.DialogInterface;
import android.content.Context;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;

public class GenericDialogFragment extends DialogFragment
{
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final String string = this.getArguments().getString("dialogMsg", "App error condition!");
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getActivity());
        builder.setMessage((CharSequence)string).setPositiveButton((CharSequence)"OK", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
            }
        });
        return (Dialog)builder.create();
    }
}
