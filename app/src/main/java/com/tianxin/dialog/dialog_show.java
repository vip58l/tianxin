package com.tianxin.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.Module.McallBack;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.member;
import com.tianxin.R;

import butterknife.OnClick;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import androidx.annotation.NonNull;

public class dialog_show extends BaseDialog {
    private String TAG = "dialog_show";
    private TextView chartmsg;
    private member member;

    public static void dialogshow(Context context, member member, Paymnets paymnets) {
        dialog_show dialogShow = new dialog_show(context, member, paymnets);
        dialogShow.show();
    }

    public dialog_show(@NonNull Context context, member member, Paymnets paymnets) {
        super(context, paymnets);
        this.member = member;
        chartmsg = findViewById(R.id.chartmsg);
        chartmsg.setText(getContext().getResources().getString(R.string.dialoged));
    }

    @Override
    public int getview() {
        return R.layout.dialog_ed;
    }

    @OnClick({R.id.kankan, R.id.exit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.kankan:
                McallBack.starsetAction(context);
                break;
            case R.id.exit:
                //dialog_Config.dialogBottom(context,paymnets);
                //转到金币充值页
                Detailedlist.starsetAction(context);
                break;
        }
        dismiss();

    }

}
