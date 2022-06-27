package com.tianxin.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.chat_settings;
import com.tencent.opensource.model.chatmoney;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_chat_settings extends BasActivity2 {
    private static final String TAG = activity_chat_settings.class.getName();
    @BindView(R.id.s1)
    chat_settings s1;
    @BindView(R.id.s2)
    chat_settings s2;
    @BindView(R.id.s3)
    chat_settings s3;
    @BindView(R.id.s4)
    chat_settings s4;
    @BindView(R.id.molev)
    TextView molev;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    List<chatmoney> chatmonies;
    chatmoney schatmony;
    Allcharge allcharge;

    public static void starAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_chat_settings.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_chat_settings;
    }

    @Override
    public void iniview() {
        molev.setText(String.format(getString(R.string.chat_aa6), "" + userInfo.getLevel()));
        datamodule.chatmoney(paymnets2);
        datamodule.getallcharge(paymnets);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setAdapter(radapter = new Radapter(context, list, Radapter.chatitemholde));
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.s1, R.id.s2, R.id.s3, R.id.s4,})
    public void OnClick(View v) {
        if (schatmony == null) {
            Toashow.show(getString(R.string.tt7));
            return;
        }
        switch (v.getId()) {
            case R.id.s1:
                sAlertDialogjinbi(schatmony.getVideo(), 1);
                break;
            case R.id.s2:
                sAlertDialogjinbi(schatmony.getAudio(), 2);
                break;
            case R.id.s3:
                sAlertDialogjinbi(schatmony.getMsg(), 3);
                break;
            case R.id.s4:
                sAlertDialogjinbi(schatmony.getContact(), 4);
                break;

        }

    }

    @Override
    public void OnEorr() {

    }

    Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Paymnets.super.isNetworkAvailable();
        }

        @Override
        public void onSuccess(Object object) {
            allcharge = (Allcharge) object;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    s1.setMoney(String.format(getString(R.string.chat_aa7), "" + allcharge.getVideo()));
                    s2.setMoney(String.format(getString(R.string.chat_aa7), "" + allcharge.getAudio()));
                    s3.setMoney(String.format(getString(R.string.tchat_aa8), "" + allcharge.getMoney()));
                    s4.setMoney(String.format(getString(R.string.tt5), "" + allcharge.getContact()));
                }
            });
        }
    };

    Paymnets paymnets2 = new Paymnets() {
        @Override
        public void onFail() {

        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onSuccess(Object object) {
            chatmonies = (List<chatmoney>) object;
            for (chatmoney chatmony : chatmonies) {
                if (chatmony.getLevel() == userInfo.getLevel()) {
                    schatmony = chatmony;
                    break;
                }
            }
            list.addAll(chatmonies);
            radapter.notifyDataSetChanged();
        }
    };

    int checkedItem;

    /**
     * 弹出选择收费金币
     */
    public void sAlertDialogjinbi(String str, int type) {
        if (TextUtils.isEmpty(str)) {
            Toashow.show(getString(R.string.tt6));
            return;
        }
        String[] tabs2 = str.split(",");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.chat_aa9);
        builder.setSingleChoiceItems(tabs2, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String money = tabs2[which];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //更新聊天收费设置
                        datamodule.chatsetpost(type, money, new Paymnets() {
                            @Override
                            public void onSuccess() {
                                Toashow.show(getString(R.string.tt8));
                                datamodule.getallcharge(paymnets);
                            }

                            @Override
                            public void onFail() {
                                Toashow.show(getString(R.string.tt9));
                            }
                        });
                    }
                });
            }
        });
        builder.show();

    }


}