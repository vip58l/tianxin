package com.tianxin.activity.ZYservices;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.getHandler.PostModule;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.gethelp;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import butterknife.BindView;

public class activity_zshouxianming extends BasActivity2 implements Paymnets {
    private static final String TAG = "activity_zshouxianming";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout;
    String touserid;
    String gettitle;

    @Override
    protected int getview() {
        return R.layout.activity_zshouxianming;
    }

    @Override
    public void iniview() {
        touserid = getIntent().getStringExtra(Constants.USERID);
        gettitle = getIntent().getStringExtra(Constants.TITLE);
        userInfo = UserInfo.getInstance();
        itemback.settitle(getString(R.string.tv_msg145));
        this.title.setText(gettitle);
        linearLayout.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        PostModule.getModule(BuildConfig.HTTP_API + "/gethomegethelp?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken() + "&touserid=" + touserid, this);
    }

    @Override
    public void success(String date) {
        try {
            gethelp gethelp = new Gson().fromJson(date, gethelp.class);
            if (!TextUtils.isEmpty(gethelp.getPic())) {
                Glideload.loadImage(iv_img, gethelp.getPic(), 6);
                linearLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fall(int code) {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }
}