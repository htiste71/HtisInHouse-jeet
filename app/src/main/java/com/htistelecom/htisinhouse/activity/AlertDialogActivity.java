package com.htistelecom.htisinhouse.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.htistelecom.htisinhouse.R;


public class AlertDialogActivity extends Activity {
    public static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // new UtilitiesWFMS(AlertDialogActivity.this).alertDialog();
        AlertDialog.Builder Builder = new AlertDialog.Builder(AlertDialogActivity.this)
                .setMessage(R.string.location_settings_desktop)
                .setTitle(R.string.location_warning)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setNegativeButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();

                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                });

        if (alertDialog == null || !alertDialog.isShowing()) {
            alertDialog = Builder.create();
            alertDialog.show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
