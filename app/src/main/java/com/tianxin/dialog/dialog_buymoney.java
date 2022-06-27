package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_buymoney extends BaseDialog {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.send3)
    TextView send3;
    member member;
    Allcharge allcharge;
    com.tencent.opensource.model.info info;

    public static void show(Context context, member member, Paymnets paymnets) {
        dialog_buymoney dialog_first = new dialog_buymoney(context, paymnets, member);
        dialog_first.setCanceledOnTouchOutside(false);
        dialog_first.show();
    }

    public dialog_buymoney(Context context, Paymnets paymnets, member member) {
        super(context, paymnets);
        this.member = member;
        setImage(member.getInreview() == 1 ? member.getAvatar() : member.getPicture());
        this.datamodule.getallcharge(String.valueOf(member.getId()), new Paymnets() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object obj) {
                allcharge = (Allcharge) obj;
                send3.setText(String.format(context.getString(R.string.msg_vip3), allcharge.getContact()));
            }

        }); //获取打电话收费配置
        this.datamodule.getbalance(new Paymnets() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object object) {
                info = (com.tencent.opensource.model.info) object; //用户金币余额
                userInfo.setJinbi(info.getMoney());
            }
        });    //获取本人金币余额
    }

    private void setImage(String url) {
        if (TextUtils.isEmpty(url)) {
            ImageLoadHelper.glideShowImageWithUrl(context, member.getSex() == 1 ? R.mipmap.boy_on : R.mipmap.girl_on, image);
        } else {
            ImageLoadHelper.glideShowImageWithUrl(context, url, image);
        }
    }

    @Override
    public int getview() {
        return R.layout.dialog_buymoney;
    }

    @OnClick({R.id.send1, R.id.send2, R.id.send3, R.id.send4})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                paymnets.onItemClick(1);
                break;
            case R.id.send2:
                paymnets.onItemClick(2);
                break;
            case R.id.send3:
                paymnets.onSucces(allcharge,info);
                break;
            case R.id.send4:
                paymnets.onItemClick(4);
                break;
        }
        dismiss();
    }

}
