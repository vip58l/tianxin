/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/13 0013
 */


package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.HtmlTagHandler;
import com.tencent.opensource.model.getservice;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_item_cn1 extends Dialog {
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.titlesver)
    TextView titlesver;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.typemsg)
    TextView typemsg;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.senbnt)
    TextView senbnt;
    getservice object;
    int TYPE;

    private Paymnets paymnets;

    public dialog_item_cn1(@NonNull Context context, getservice object, int TYPE) {
        super(context, R.style.fei_style_dialog);
        this.object = object;
        this.TYPE = TYPE;
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_recy_item_cn1, null);
        setContentView(inflate);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inflate.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 1.5f);
        inflate.setLayoutParams(params);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        ButterKnife.bind(this);
        iniview();
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @OnClick({R.id.back, R.id.senbnt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                break;
            case R.id.senbnt:
                if (TYPE == 0) {
                    //立即预约
                } else {
                    if (paymnets != null) {
                        //立即发布
                        paymnets.onSuccess();
                    }
                }

                break;
        }
        dismiss();
    }

    private void iniview() {
        String text = "<b><font color='red'>￥</font><myfont color='red' size='60px'>" + object.getMoney() + "</myfont></b>/次  好评率<font color='000000'>100%</font>";
        Spanned spanned = Html.fromHtml(text, null, new HtmlTagHandler("myfont"));
        money.setText(spanned);
        title.setText(object.getTitle());
        if (object.getSecond() > 1) {
            typemsg.setText("多次");
        }
        if (!object.getMsg().isEmpty()) {
            titlesver.setText(Html.fromHtml(object.getMsg()));
        }
        if (object.getDuration() > 0) {
            duration.setText(object.getDuration() + "/分钟");
        }
        second.setText(object.getSecond() + "/次");
        senbnt.setText(this.TYPE == 0 ? "立即预约" : "立即发布");
    }

    public static dialog_item_cn1 dialogitemcn1(Context context, getservice object, int type) {
        dialog_item_cn1 dialogItemCn1 = new dialog_item_cn1(context, object, type);
        dialogItemCn1.show();
        return dialogItemCn1;
    }
}
