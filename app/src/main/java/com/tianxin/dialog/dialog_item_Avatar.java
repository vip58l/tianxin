/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/28 0028
 */


package com.tianxin.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.activity.edit.activity_uploadavatar;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class dialog_item_Avatar extends BaseDialog {
    String TAG = dialog_item_Avatar.class.getSimpleName();
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.titlemsg)
    TextView titlemsg;

    public dialog_item_Avatar(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);

        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            Glide.with(context).load(userInfo.getSex().equals("1") ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose).into(avatar);
        } else {
            Glide.with(context).load(userInfo.getAvatar()).into(avatar);
        }
        titlemsg.setText(String.format(titlemsg.getText().toString(), userInfo.getSex().equals("1") ? "女" : "男"));

        //getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //getWindow().setGravity(Gravity.BOTTOM);
        //getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public static dialog_item_Avatar dialogitemmsgpic(Context context) {
        dialog_item_Avatar dialog_item_avatar = new dialog_item_Avatar(context, null);
        dialog_item_avatar.show();
        return dialog_item_avatar;
    }

    @Override
    public int getview() {
        return R.layout.dialog_item_msgpic;
    }

    @Override
    @OnClick({R.id.updatepic, R.id.close})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.updatepic:
                Intent intent = new Intent(DemoApplication.instance(), activity_uploadavatar.class).putExtra(Constants.TYPE, 1);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                DemoApplication.instance().startActivity(intent);
                break;
            case R.id.close:
                break;
        }
    }
}
