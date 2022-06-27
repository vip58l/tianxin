package com.tianxin.activity.sesemys;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.videoPush;

/**
 * 隐私设置
 */
public class activity_privacy_settings extends BasActivity2 {

    private static final String TAG = activity_privacy_settings.class.getName();

    public static void starsetAction(Context context) {
        context.startActivity(new Intent(context, activity_privacy_settings.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_privacy_settings;
    }

    @Override
    public void iniview() {
        getChecked(findViewById(R.id.s_v1), Config_Msg.getInstance().isConceal_1(), 1);
        getChecked(findViewById(R.id.s_v2), Config_Msg.getInstance().isConceal_2(), 2);
        getChecked(findViewById(R.id.s_v3), Config_Msg.getInstance().isConceal_3(), 3);
        getChecked(findViewById(R.id.s_v4), Config_Msg.getInstance().isConceal_4(), 4);
        getChecked(findViewById(R.id.s_v5), Config_Msg.getInstance().isConceal_5(), 5);
    }

    @Override
    public void initData() {
        datamodule.videoPush(new Paymnets() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object object) {

                videoPush videoPush = (com.tencent.opensource.model.videoPush) object;
                Config_Msg.getInstance().setConceal_1(videoPush.getPush1() == 1 ? true : false);
                Config_Msg.getInstance().setConceal_2(videoPush.getPush2() == 1 ? true : false);
                Config_Msg.getInstance().setConceal_3(videoPush.getPush3() == 1 ? true : false);
                Config_Msg.getInstance().setConceal_4(videoPush.getPush4() == 1 ? true : false);
                Config_Msg.getInstance().setConceal_5(videoPush.getPush5() == 1 ? true : false);

                getChecked(findViewById(R.id.s_v1), Config_Msg.getInstance().isConceal_1(), 1);
                getChecked(findViewById(R.id.s_v2), Config_Msg.getInstance().isConceal_2(), 2);
                getChecked(findViewById(R.id.s_v3), Config_Msg.getInstance().isConceal_3(), 3);
                getChecked(findViewById(R.id.s_v4), Config_Msg.getInstance().isConceal_4(), 4);
                getChecked(findViewById(R.id.s_v5), Config_Msg.getInstance().isConceal_5(), 5);


            }
        });

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    private void getChecked(Switch aSwitch, boolean isbool, int type) {
        aSwitch.setChecked(isbool);
        aSwitch.setSwitchTextAppearance(context, R.style.s_false);
        /**
         * 监听事件
         */
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //控制开关字体颜色
                if (isChecked) {
                    aSwitch.setSwitchTextAppearance(context, R.style.s_true);
                    switch (type) {
                        case 1:
                            Config_Msg.getInstance().setConceal_1(true);
                            datamodule.editvideoPush(1, true);
                            break;
                        case 2:
                            Config_Msg.getInstance().setConceal_2(true);
                            datamodule.editvideoPush(2, true);
                            break;
                        case 3:
                            Config_Msg.getInstance().setConceal_3(true);
                            datamodule.editvideoPush(3, true);
                            break;
                        case 4:
                            Config_Msg.getInstance().setConceal_4(true);
                            datamodule.editvideoPush(4, true);
                            break;
                        case 5:
                            Config_Msg.getInstance().setConceal_5(true);
                            datamodule.editvideoPush(5, true);
                            break;
                    }

                } else {
                    aSwitch.setSwitchTextAppearance(context, R.style.s_false);
                    switch (type) {
                        case 1:
                            Config_Msg.getInstance().setConceal_1(false);
                            datamodule.editvideoPush(1, false);
                            break;
                        case 2:
                            Config_Msg.getInstance().setConceal_2(false);
                            datamodule.editvideoPush(2, false);
                            break;
                        case 3:
                            Config_Msg.getInstance().setConceal_3(false);
                            datamodule.editvideoPush(3, false);
                            break;
                        case 4:
                            Config_Msg.getInstance().setConceal_4(false);
                            datamodule.editvideoPush(4, false);
                            break;
                        case 5:
                            Config_Msg.getInstance().setConceal_5(false);
                            datamodule.editvideoPush(5, false);
                            break;
                    }

                }

                Log.d(TAG, "onCheckedChanged: " + isChecked);
                Log.d(TAG, "onCheckedChanged: " + aSwitch.isChecked());


            }

        });
    }
}
