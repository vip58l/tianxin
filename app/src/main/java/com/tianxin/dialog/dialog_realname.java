/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/26 0026
 */


package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.dialogBase;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_realname extends dialogBase {
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.mtitle)
    TextView mtitle;
    @BindView(R.id.titlemsg)
    TextView titlemsg;
    @BindView(R.id.updatepic)
    TextView updatepic;
    @BindView(R.id.close)
    ImageView close;

    private Paymnets paymnets;

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public dialog_realname(@NonNull Context context) {
        super(context);
    }

    @Override
    public int itemview() {
        return R.layout.dialog_item_msgpic;
    }

    @Override
    public void inidate() {
        UserInfo userInfo = UserInfo.getInstance();
        mtitle.setText(R.string.tv_msg18);
        titlemsg.setText(R.string.tv_msg19);
        updatepic.setText(R.string.tv_msg20);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Glideload.loadImage(avatar, userInfo.getAvatar());
        } else {
            int bg = userInfo.getSex().equals("1") ? R.mipmap.icon_man : R.mipmap.icon_woman;
            Glideload.loadImage(avatar, bg);

        }


    }


    @Override
    @OnClick({R.id.close, R.id.updatepic})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                paymnets.onError();
                break;
            case R.id.updatepic:
                paymnets.onSuccess();
                break;
        }
        dismiss();

    }


    public static void Listener(Context context, Paymnets p) {
        dialog_realname dialog_realname = new dialog_realname(context);
        dialog_realname.setPaymnets(p);
        dialog_realname.setCanceledOnTouchOutside(false);
        dialog_realname.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        dismiss();
        paymnets.onRefresh();
        return super.onKeyDown(keyCode, event);
    }
}
