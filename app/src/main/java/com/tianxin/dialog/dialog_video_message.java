package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 女性提示升级为主播
 */
public class dialog_video_message extends BaseDialog {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.send1)
    TextView send1;
    @BindView(R.id.send2)
    TextView send2;
    @BindView(R.id.send3)
    TextView send3;

    public static void show(Context context, Paymnets paymnets) {
        dialog_video_message dialog_video_message = new dialog_video_message(context, paymnets);
        dialog_video_message.setCanceledOnTouchOutside(false);
        dialog_video_message.show();

    }

    public dialog_video_message(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        name.setText(R.string.dialog_call1);
        money.setText(R.string.dialog_call2);
        send1.setText(R.string.dialog_call3);
        send2.setText(R.string.dialog_call4);
        send3.setVisibility(View.VISIBLE);
    }

    @Override
    public int getview() {
        return R.layout.dialogjinbi;
    }

    @Override
    @OnClick({R.id.send1, R.id.send2, R.id.send3,})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
                break;
            case R.id.send2:
                if (paymnets != null) {
                    paymnets.onFail();

                }
                break;
            case R.id.send3:
                Config_Msg.getInstance().setDialog_call(true);
                break;
        }
        dismiss();
    }
}
