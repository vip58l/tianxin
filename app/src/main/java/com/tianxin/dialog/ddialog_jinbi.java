package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Party;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;
import butterknife.OnClick;

public class ddialog_jinbi extends BaseDialog {
    private static final String TAG = ddialog_jinbi.class.getSimpleName();
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.send1)
    TextView send1;
    @BindView(R.id.send2)
    TextView send2;

    public static void myshow(Context context, Paymnets paymnets, videolist videolist) {
        ddialog_jinbi ddialogjinbi = new ddialog_jinbi(context, paymnets, videolist);
        ddialogjinbi.show();

    }

    public static void myshow(Context context, Object object, Paymnets paymnets) {
        ddialog_jinbi ddialogjinbi = new ddialog_jinbi(context, object, paymnets);
        ddialogjinbi.show();
    }

    public ddialog_jinbi(@NonNull Context context, Paymnets paymnets, videolist videolist) {
        super(context, paymnets);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        name.setText(videolist.getTitle());
        money.setText(String.format(context.getString(R.string.tv_ss43) + "", UserInfo.getInstance().getJinbi()));
        send1.setText(String.format(context.getString(R.string.tv_ss45) + "", videolist.getJinbi()));
        datamodule.getbalance(paymnets1);//查询金币
    }


    public ddialog_jinbi(@NonNull Context context, Object object, Paymnets paymnets) {
        super(context, paymnets);
        Party party = (Party) object;
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        name.setText(R.string.ok2);
        money.setText(String.format("" + getContext().getString(R.string.dialogjinbi_s1), party.getPartytime(), party.getAddress()));
        send1.setText(R.string.tv_msg113);
        send2.setText(R.string.tv_msg245);

    }


    @Override
    public int getview() {
        return R.layout.dialogjinbi;
    }

    @Override
    @OnClick({R.id.send1, R.id.send2})
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
        }
        dismiss();
    }

    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            info info = (com.tencent.opensource.model.info) object;
            money.setText(String.format(getContext().getString(R.string.tv_ss43) + "", info.getMoney()));
            userInfo.setJinbi(info.getMoney());
        }
    };
}
