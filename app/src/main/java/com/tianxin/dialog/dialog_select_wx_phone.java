package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasettfDialog;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.app.DemoApplication;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.member;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_select_wx_phone extends BasettfDialog {
    member member;
    @BindView(R.id.videocall)
    TextView videocall;
    @BindView(R.id.audiocall)
    TextView audiocall;

    public static void show(Context context, member member) {
        dialog_select_wx_phone dialogSelectWxPhone = new dialog_select_wx_phone(context, member);
        dialogSelectWxPhone.show();
    }

    public dialog_select_wx_phone(@NonNull Context context, member member) {
        super(context, null);
        this.member = member;
        videocall.setText(String.format(context.getString(R.string.wx44) + "", member.getWx()));
    }

    @Override
    public int getview() {
        return R.layout.dialog_select_wx_phone;
    }

    @Override
    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                Toashow.show(String.format(getContext().getString(R.string.wxcoyp), member.getWx()));
                Dialog_fenxing.paramcopy(getContext(), member.getWx());        // 复制微信号
                break;
            case R.id.layout2:
                coypgewx();
                break;
            case R.id.layout3:
                dismiss();
                break;
        }


    }

    /**
     * 复制微信号
     */
    private void coypgewx() {
        if (!TextUtils.isEmpty(member.getWx())) {
            if (!WXpayObject.isWeixinAvilible()) {
                Toashow.show(DemoApplication.instance().getString(R.string.tm80));
                return;
            }
            Dialog_fenxing.paramcopy(getContext(), member.getWx());        // 复制微信号
            Dialog_fenxing.getDialogFenxing(getContext()).getWechatApi(); //打开微信
        } else {
            Toashow.show(getContext().getString(R.string.tt11));
        }
    }
}
