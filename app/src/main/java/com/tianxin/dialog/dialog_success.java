package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_success extends BaseDialog {
    String TAG = dialog_success.class.getSimpleName();
    @BindView(R.id.msgname)
    TextView msgname;
    @BindView(R.id.chartmsg)
    TextView chartmsg;
    @BindView(R.id.kankan)
    TextView kankan;
    @BindView(R.id.exit)
    TextView exit;

    public static void show(Context context, Paymnets paymnets) {
        dialog_success dialogSuccess = new dialog_success(context, paymnets);
        dialogSuccess.setCanceledOnTouchOutside(false);
        dialogSuccess.show();
    }

    public static void show(Context context, String msg, Paymnets paymnets) {
        dialog_success dialogSuccess = new dialog_success(context, msg, paymnets);
        dialogSuccess.setCanceledOnTouchOutside(false);
        dialogSuccess.show();
    }

    public dialog_success(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
    }

    public dialog_success(@NonNull Context context, String msg, Paymnets paymnets) {
        super(context, paymnets);
        if (!TextUtils.isEmpty(msg)) {
            chartmsg.setText(msg);
        }
    }

    @Override
    public int getview() {
        return R.layout.dialog_realname;
    }

    @Override
    @OnClick({R.id.kankan, R.id.exit})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.kankan:
                if (paymnets != null) {
                    paymnets.onFail();
                }
                break;
            case R.id.exit:
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (paymnets != null) {
            paymnets.onFail();
        }
        if (isShowing()) {
            dismiss();
        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");
        dismiss();
    }
}
