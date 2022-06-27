package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_show_activity extends BaseDialog {
    @BindView(R.id.image)
    ImageView image;

    public static void show(Context context, Paymnets paymnets) {
        dialog_show_activity dialog_show_activity = new dialog_show_activity(context, paymnets);
        dialog_show_activity.show();
    }

    public static void show(Context context) {
        dialog_show_activity dialog_show_activity = new dialog_show_activity(context, null);
        dialog_show_activity.show();
    }

    public dialog_show_activity(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);

        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            ImageLoadHelper.glideShowImageWithUrl(context, userInfo.getSex().equals("1") ? R.mipmap.boy_off : R.mipmap.girl_off, image);
        } else {
            ImageLoadHelper.glideShowImageWithUrl(context, userInfo.getAvatar(), image);
        }


    }

    @Override
    public int getview() {
        return R.layout.dialog_show_avtivity;
    }

    @Override
    @OnClick({R.id.send22})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.send22:
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
                break;
        }

    }
}
