package com.tianxin.activity.sesemys;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.R;

/**
 * 消息提醒
 */
public class actrivity_sesemy_notice extends BasActivity2 {

    public static void starsetAction(Context context) {
        context.startActivity(new Intent(context, actrivity_sesemy_notice.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_sesemys_notice;
    }

    @Override
    public void iniview() {
        getChecked(findViewById(R.id.s_v1), Config_Msg.getInstance().isMessage_voice(), 1);
        getChecked(findViewById(R.id.s_v2), Config_Msg.getInstance().isMessage_shock(), 2);
    }

    @Override
    public void initData() {

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
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    aSwitch.setSwitchTextAppearance(context, R.style.s_true);

                    switch (type) {
                        case 1:
                            Config_Msg.getInstance().setMessage_voice(true);
                            break;
                        case 2:
                            Config_Msg.getInstance().setMessage_shock(true);
                            break;
                    }

                } else {
                    aSwitch.setSwitchTextAppearance(context, R.style.s_false);
                    switch (type) {
                        case 1:
                            Config_Msg.getInstance().setMessage_voice(false);
                            break;
                        case 2:
                            Config_Msg.getInstance().setMessage_shock(false);
                            break;
                    }

                }

            }

        });
    }
}
