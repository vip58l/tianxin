package com.tianxin.activity.picenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.widget.view_item_conmp;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.Constants;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;

/**
 * 个人详情
 */
public class per1 extends BasFragment {
    private static final String TAG = per1.class.getName();
    @BindView(R.id.cforsds)
    TextView cforsds;
    @BindView(R.id.cforsds11)
    TextView cforsds11;
    @BindView(R.id.conmp1)
    view_item_conmp conmp1;
    @BindView(R.id.conmp2)
    view_item_conmp conmp2;
    @BindView(R.id.conmp3)
    view_item_conmp conmp3;
    @BindView(R.id.conmp4)
    view_item_conmp conmp4;
    @BindView(R.id.conmp5)
    view_item_conmp conmp5;
    @BindView(R.id.conmp6)
    view_item_conmp conmp6;
    private String getuserid;
    private member member;

    public static per1 perview(String getuserid) {
        Bundle args = new Bundle();
        args.putString(Constants.USERID, getuserid);
        per1 per1 = new per1();
        per1.setArguments(args);
        return per1;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        getuserid = arguments.getString(Constants.USERID);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.item_view_per, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        datamodule.getHashmap(getuserid, paymnets);
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    private void setview() {
        personal personal = member.getPersonal();
        if (personal != null) {
            cforsds.setText(personal.getCforsds());
            cforsds11.setText(personal.getPesigntext());
            conmp1.setPertitle("性别", member.getSex() == 1 ? "男" : "女");
            conmp2.setPertitle("身高", personal.getHeight() + " cm");
            conmp3.setPertitle("年龄", String.valueOf(personal.getAge()));
            conmp4.setPertitle("体重", String.valueOf(personal.getWeight()));
            conmp5.setPertitle("职业", String.valueOf(personal.getOccupation()));

            if (!TextUtils.isEmpty(personal.getCity())) {
                conmp6.setPertitle("城市", personal.getCity().replace("市", ""));
            }
        } else {
            conmp1.setPertitle("性别", member.getSex() == 1 ? "男" : "女");
            conmp2.setPertitle("身高", "0 cm");
            conmp3.setPertitle("年龄", "0");
            conmp4.setPertitle("体重", "0 kg");
            conmp5.setPertitle("职业", "保密");
            if (!TextUtils.isEmpty(personal.getCity())) {
                conmp6.setPertitle("城市", member.getCity().replace("市", ""));
            }
        }
    }

    public Paymnets paymnets = new Paymnets() {

        @Override
        public void onSuccess(Object object) {
            member = (member) object;
            setview();
        }

        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                }
            });
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
            }
        }

        @Override
        public void ToKen(String msg) {
            per1.super.paymnets.ToKen(msg);
        }
    };


}
