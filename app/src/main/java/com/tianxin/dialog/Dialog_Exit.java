package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 弹出提示消息
 */
public class Dialog_Exit extends Dialog {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.kankan)
    TextView kankan;
    @BindView(R.id.exit)
    TextView exit;
    public Paymnets paymnets;

    public static void show(Context context, Paymnets paymnets, String s1, String s2, String s3) {
        Dialog_Exit dialog_exit = new Dialog_Exit(context);
        dialog_exit.setdialogTitle(s1);
        dialog_exit.setkankan(s2);
        dialog_exit.setexit(s3);
        dialog_exit.setPaymnets(paymnets);
        dialog_exit.show();
    }

    public Dialog_Exit(Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_msg);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.exit, R.id.kankan})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.kankan:
                if (paymnets != null) {
                    paymnets.activity();
                }
                break;
            case R.id.exit:
                if (paymnets != null) {
                    paymnets.payens();
                }
                break;
        }
        dismiss();
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public void setdialogTitle(String msg) {
        this.title.setText(msg);
    }

    public void setkankan(String msg) {
        this.kankan.setText(msg);
    }

    public void setexit(String msg) {
        this.exit.setText(msg);
    }

    public static void dialogshow(Context context, Paymnets paymnets) {
        Dialog_Exit dialogExit = new Dialog_Exit(context);
        dialogExit.setdialogTitle(context.getString(R.string.dialog_msg_mess));
        dialogExit.setexit(context.getString(R.string.AlertDialog_negative));
        dialogExit.title.setTextSize(15);
        dialogExit.setkankan(context.getString(R.string.btn_ok));
        dialogExit.setPaymnets(paymnets);
        dialogExit.show();
    }

}
