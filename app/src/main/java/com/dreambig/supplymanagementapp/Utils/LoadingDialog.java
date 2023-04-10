package com.dreambig.supplymanagementapp.Utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.dreambig.supplymanagementapp.R;

public class LoadingDialog {
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    public LoadingDialog(Context context){
        builder = new AlertDialog.Builder(context);
        builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.progress_loading);
        builder.setCancelable(false);
        alertDialog = builder.create();
    }

    public void show(){
        alertDialog.show();
    }

    public void dismiss(){
        alertDialog.dismiss();
    }
}
