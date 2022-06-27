package com.tianxin.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tianxin.R;

/**
 * 弹出新的页面
 */
public class AlertDialog_show extends AlertDialog implements DialogInterface.OnClickListener {

    protected AlertDialog_show(Context context) {
        super(context);
        Builder builder = new Builder(context);
        builder.setTitle("开启权限").
                setIcon(R.mipmap.ic_launcher).
                setMessage(R.string.dialog_aler).
                setPositiveButton("确定", this).setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}


