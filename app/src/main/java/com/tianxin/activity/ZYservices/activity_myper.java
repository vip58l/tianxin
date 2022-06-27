package com.tianxin.activity.ZYservices;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tianxin.getHandler.PostModule;
import com.tianxin.utils.AES.Resultinfo;
import com.tianxin.activity.picenter.fragment.per1;
import com.tianxin.activity.picenter.fragment.per2;
import com.tianxin.activity.picenter.fragment.per3;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.activity.edit.activity_nickname1;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.app.DemoApplication;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 咨询用户个人主页
 */
public class activity_myper extends BasActivity2 implements Paymnets {

    private static final String TAG = "activity_myper";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.circleImageView)
    ImageView circleImageView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.editquery)
    ImageView editquery;
    @BindView(R.id.tvedit)
    TextView tvedit;
    @BindView(R.id.tvedit1)
    TextView tvedit1;
    @BindView(R.id.count1)
    TextView count1;
    @BindView(R.id.count2)
    TextView count2;
    @BindView(R.id.bg_img)
    ImageView bg_img;
    private List<Fragment> fragmentlist;



    @Override
    protected int getview() {
        return R.layout.activity_myper;
    }

    @Override
    public void iniview() {
        userInfo = UserInfo.getInstance();
        getuserid = getIntent().getStringExtra(Constants.USERID);
        itemback.setHaidtopBackgroundColor(true);
        itemback.tvback.setTextColor(getResources().getColor(R.color.white));
        itemback.setIv_back_img(R.mipmap.authsdk_return_bg);
        viewpager.setAdapter(new setViewPager(getSupportFragmentManager(), getFragmentlist(), setViewPager.picenter));
        viewpager.setOffscreenPageLimit(4);
        tablayout.setupWithViewPager(viewpager);
        boolean equals = userInfo.getUserId().equals(getuserid);
        if (!equals) {
            tvedit.setVisibility(View.GONE);
            editquery.setVisibility(View.GONE);
            tvedit1.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void initData() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        PostModule.getModule(String.format(BuildConfig.HTTP_API + "/member?userid=%s&touserid=%s&token=%s", userInfo.getUserId(), getuserid, userInfo.getToken()), this);
    }

    @Override
    @OnClick({R.id.editquery, R.id.tvedit, R.id.tvedit1})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.editquery:
                //编辑昵称
                startActivityForResult(new Intent(this, activity_nickname1.class).putExtra(Constants.POSITION, 0), Config.sussess);
                break;
            case R.id.tvedit:
                //辑编资料
                startActivityForResult(new Intent(this, activity_updateedit.class), Config.sussess);
                break;
            case R.id.tvedit1:
                setFollow();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            initData();
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 返回相关分类内容
     *
     * @return
     */
    private List<Fragment> getFragmentlist() {
        fragmentlist = new ArrayList<>();
        fragmentlist.add(per1.perview(getuserid));
        fragmentlist.add(per2.perview());
        fragmentlist.add(per3.perview(getuserid));
        // fragmentlist.add(per4.perview());
        return fragmentlist;
    }

    @Override
    public void success(String str) {
        try {
            String decrypt = Resultinfo.decrypt(str);
            member = new Gson().fromJson(decrypt, member.class);
            setupdate(member);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupdate(member member) {
        if (!TextUtils.isEmpty(member.getPicture())) {
            Glideload.loadImage(circleImageView, member.getTencent() == 1 ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture());
            Glideload.loadImage(bg_img, member.getTencent() == 1 ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture(), 10, 25);
        } else {
            Glideload.loadImage(circleImageView, member.getSex() == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        }
        name.setText(TextUtils.isEmpty(member.getTvname()) ? member.getTruename() : member.getTvname());
        title.setVisibility(TextUtils.isEmpty(member.getTvname()) ? View.GONE : View.VISIBLE);
        title.setText(member.getTruename());
        count1.setText(String.valueOf(member.getTacount()));
        count2.setText(String.valueOf(member.getMyconun()));
        personal personal = member.getPersonal();
        followlist foll = member.getFollowlist();
        if (personal != null) {
            msg.setText(personal.getPesigntext());
        }
        if (foll != null) {
            tvedit1.setText(getString(R.string.tv_fall));
        }
    }

    @Override
    public void fall(int code) {
        ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
    }

    public void setFollow() {
        if (member == null) {
            Toashow.show(getString(R.string.eorrfali3));
            return;
        }
        if (userInfo.getUserId().equals(getuserid)) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg147));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getResources().getString(R.string.eorrfali2));
            return;
        }
    PostModule.getModule(BuildConfig.HTTP_API + "/gofollowlist?userid=" + userInfo.getUserId() + "&touserid=" + getuserid + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        ToastUtils.showShort(getString(R.string.tv_fall));
                        tvedit1.setText(getString(R.string.tv_fall));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }
}