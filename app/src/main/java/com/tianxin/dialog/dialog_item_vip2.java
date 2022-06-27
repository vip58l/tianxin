/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/25 0025
 */


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
import com.tencent.opensource.model.videolist;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_item_vip2 extends BaseDialog {
    @BindView(R.id.mymoney)
    TextView mymoney;

    public dialog_item_vip2(@NonNull Context context, Paymnets paymnets, videolist video) {
        super(context, paymnets);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        mymoney.setText(String.format(mymoney.getText().toString(), video.getJinbi()));
    }

    public static void dialogitemvip(Context context, Paymnets paymnet, videolist video) {
        dialog_item_vip2 dialogItemVip = new dialog_item_vip2(context, paymnet, video);
        dialogItemVip.setCanceledOnTouchOutside(false);
        dialogItemVip.show();
    }

    @Override
    public int getview() {
        return R.layout.dialog_item_vip2;
    }

    @OnClick({R.id.goosvip, R.id.mymoney})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.goosvip:
                //点击升级VIP会员
                if (paymnets!=null){
                    paymnets.onClick();
                }

                break;
            case R.id.mymoney:
                //支付金币
                if (paymnets!=null){
                    paymnets.onSuccess();
                }

                break;
        }
        dismiss();
    }


}
