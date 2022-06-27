package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.R;

public class dialog_load extends Dialog {
    private TextView titles;
    public static dialog_load dialogLoad;

    public dialog_load(@NonNull Context context) {
        super(context, R.style.MyDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load);
        titles = findViewById(R.id.title);
    }

    public void setTitle(String s) {
        titles.setText(s);
    }

    public static dialog_load myshow(Context context) {
        dialogLoad = new dialog_load(context);
        return dialogLoad;
    }

    public static void dialoghide() {
        if (dialogLoad != null) {
            dialogLoad.dismiss();
        }
    }

}


