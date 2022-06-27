/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/28 0028
 */


package com.tianxin.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tianxin.dialog.dialog_item_Avatar;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.dialog.dialog_follow_view;
import com.tencent.opensource.model.UserInfo;

/**
 * 监听头像广播监听
 */
public class Receiver1 extends BroadcastReceiver {
    private static final String TAG = Receiver1.class.getSimpleName();
    private UserInfo userInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        Avatar(context);
    }

    public void Avatar(Context context) {
        userInfo = UserInfo.getInstance();
        if (BuildConfig.TYPE == 2) {
            //检查是否已选择分类问题
            if (userInfo.getVideotype() == 0) {
                dialog_follow_view.mydialog(context);
                return;
            }
        }

        //检查头像是否已上传
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            dialog_item_Avatar.dialogitemmsgpic(context);
        }

    }

}
