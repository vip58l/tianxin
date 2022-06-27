/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/10 0010
 */


package com.tianxin.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 监听广播 BroadcastReceiver
 */
public class Receiver2 extends BroadcastReceiver {
    String TAG = Receiver2.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive: " + action);

        //Intent playIntent = new Intent(context, activity_GiftInfo.class);
        //playIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(playIntent);

    }


}


