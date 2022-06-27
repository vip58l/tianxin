package com.tianxin.activity.Withdrawal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.reward;
import com.tianxin.R;
import com.tianxin.activity.Withdrawal.moneylog.activity_moneylog;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的钱包可用余额
 */
public class activity_balance extends BasActivity2 {
    private static final String TAG = activity_balance.class.getName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.money)
    TextView money;

    public static void setAction(Context context) {
        Intent intent = new Intent(context, activity_balance.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getview() {
        return R.layout.activity_balance;
    }

    @Override
    public void iniview() {
        backtitle.setBackright(getString(R.string.tv_ms_msg));
        datamodule.reward(0, new Paymnets() {
            @Override
            public void payonItemClick(String balance, int TYPE) {
                Paymnets.super.payonItemClick(balance, TYPE);
                reward reward = gson.fromJson(balance, reward.class);
                if (!TextUtils.isEmpty(reward.getMoney())) {
                    money.setText(String.format("￥%s", reward.getMoney()));
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.actiongo, R.id.actionto, R.id.money, R.id.backright})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.actionto:
                Detailedlist.starsetAction(context);
                break;
            case R.id.actiongo:
                Withdrawals.setAction(activity);
                break;
            case R.id.money:
                break;
            case R.id.backright:
                activity_moneylog.setAction(context);
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

}