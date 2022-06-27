package com.tianxin.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

public class dialgo_chat extends BaseDialog {

    public static void star(Context context) {
        dialgo_chat dialgo_chat = new dialgo_chat(context, null);
        dialgo_chat.show();
    }

    public dialgo_chat(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        getWindow().setGravity(Gravity.CENTER);
//        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @Override
    public int getview() {
        return R.layout.dialog_chat;
    }

    @Override
    @OnClick({R.id.caht_suessess, R.id.chat, R.id.fins,})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.caht_suessess:
                Config_Msg.getInstance().setChat(true);
                break;
            case R.id.chat:
            case R.id.fins:
                break;
        }
        dismiss();
    }
}
