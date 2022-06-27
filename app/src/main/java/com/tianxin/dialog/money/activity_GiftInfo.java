/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/10 0010
 */


package com.tianxin.dialog.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

import io.agora.tutorials1v1vcall.Persenter.Persenter;

import static com.tianxin.dialog.dialog_item_gift.dialogitemgift;

public class activity_GiftInfo extends Activity {
    private static final String TAG = "activity_GiftInfo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogitemgift(this, null, giftPanelDelegate, null);
    }


    private GiftPanelDelegate giftPanelDelegate = new GiftPanelDelegate() {
        @Override
        public void onGiftItemClick(GiftInfo giftInfo) {
            String toJson = new Gson().toJson(giftInfo);
            Intent intent = new Intent();
            intent.putExtra(Persenter.GiftInfo, toJson);
            setResult(Persenter.requestCode, intent);
        }

        @Override
        public void onChargeClick() {

        }
    };
}
