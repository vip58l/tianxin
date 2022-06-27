package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.Module.api.order_moeny;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.moneyLog;

import butterknife.BindView;

public class item_money_show extends BaseHolder {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.state)
    TextView state;

    public item_money_show(View view) {
        super(view);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        moneyLog moneyLog = (com.tencent.opensource.model.moneyLog) object;
        tv_title.setText(moneyLog.getMsg());
        datetime.setText(moneyLog.getDatetime());
        switch (moneyLog.getType()) {
            case 0:
            case 2:
                money.setText(String.format("+%s", moneyLog.getMoney()));
                money.setTextColor(Color.RED);
                break;
            case 1:
            case 3:
                money.setText(String.format("-%s", moneyLog.getMoney()));
                money.setTextColor(Color.GRAY);
                break;
            default:
                money.setTextColor(Color.TRANSPARENT);
                break;
        }
        tv_title.setText(String.valueOf(moneyLog.getMsg()));
        state.setText("可用余额：" + String.valueOf(moneyLog.getBalance()));
        datetime.setText(Config.stampToDate(moneyLog.getDatetime()));
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {
        order_moeny t = (order_moeny) object;
        if (!TextUtils.isEmpty(t.getSexplain())) {
            Spanned spanned = Html.fromHtml(String.format("%s <font color='red'>(%s)</font>", t.getMsg(), t.getSexplain()));
            tv_title.setText(spanned);
        } else {
            tv_title.setText(t.getMsg());
        }
        datetime.setText(Config.stampToDate(t.getDatetime()));
        money.setText("-" + t.getMoney());
        switch (t.getStatus()) {
            case Constants.TENCENT0:
                state.setText(R.string.tv_msg56);
                break;
            case Constants.TENCENT:
                state.setText(R.string.tv_msg57);
                state.setTextColor(context.getResources().getColor(R.color.rtc_green_bg));
                break;
            case Constants.TENCENT2:
                state.setText(R.string.tv_msg58);
                money.setTextColor(context.getResources().getColor(R.color.colorAccent));
                state.setTextColor(context.getResources().getColor(R.color.colorAccent));
                break;
            default:
                state.setTextColor(context.getResources().getColor(R.color.teal006));
                break;
        }

    }

    @Override
    public void OnClick(View v) {

    }
}
