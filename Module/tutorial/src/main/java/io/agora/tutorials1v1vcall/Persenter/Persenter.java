package io.agora.tutorials1v1vcall.Persenter;

import android.app.Activity;
import android.content.Intent;
import android.util.ArrayMap;

import com.tencent.opensource.model.UserInfo;

import java.util.Map;

/**
 * 中间层MVP
 */
public class Persenter {
    public final static int requestCode = 1000;
    public final static String GiftInfo = "giftInfo";

    /**
     * 发送到服务端用于生成toke用于鉴权
     *
     * @return
     */
    public Map<String, String> PostDate(String channelName,UserInfo userInfo) {
        Map<String, String> date = new ArrayMap<>();
        date.put("channelName", channelName);
        date.put("userid", userInfo.getUserId());
        date.put("token", userInfo.getToken());
        return date;
    }

    public void startActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("com.paixide.dialog.money.activity_GiftInfo");
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivityForResult(intent, requestCode);
    }


}
