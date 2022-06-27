package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.activity.matching.activity_thesamecity_speed;
import com.tianxin.adapter.Radapter;
import com.tianxin.R;
import com.tianxin.Test.live_animation;
import com.tianxin.activity.DouYing.activity_jsonvideo;
import com.tianxin.activity.Withdrawal.Withdrawals;
import com.tianxin.activity.ZYservices.acitivity_NewsServices;
import com.tianxin.activity.ZYservices.activity_categories;
import com.tianxin.activity.ZYservices.activity_courses;
import com.tianxin.activity.ZYservices.activity_photo_album;
import com.tianxin.activity.ZYservices.activity_servicetitle;
import com.tianxin.activity.activity_home_page;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.activity_sevaluate;
import com.tianxin.activity.edit.activity_nickname2;
import com.tianxin.activity.matching.activity_audio_speed;
import com.tianxin.activity.meun.MEUN_MainActivity;
import com.tianxin.activity.activity_mylikeyou;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.utils.Constants;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 发布服务菜单
 */
public class dialog_item_show_home extends Dialog implements Paymnets {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<Object> list = new ArrayList<>();
    List<Class<?>> cls = new ArrayList<>();
    Radapter radapter;
    private UserInfo userInfo;

    public dialog_item_show_home(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_view_show_home);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        ButterKnife.bind(this);
        inidate();

    }

    private void inidate() {
        userInfo = UserInfo.getInstance();
        radapter = new Radapter(getContext(), list, Radapter.dialog_item_show_home);
        radapter.setPaymnets(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(radapter);
        list.addAll(Arrays.asList(getContext().getResources().getStringArray(R.array.arrayitem6)));
        inidatecls();
    }

    public static void showhome(Context context) {
        dialog_item_show_home dialog_item_show_home = new dialog_item_show_home(context);
        dialog_item_show_home.show();
    }

    @Override
    public void status(int position) {
        startActivitygo(position);
    }

    @OnClick({R.id.layout5})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layout5:
                dismiss();
                break;
        }
    }

    @Override
    public void success(String date) {
        try {
            List<navigation> navigations = JsonUitl.stringToList(date, navigation.class);
            list.addAll(navigations);
            radapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void fall(int code) {
    }


    /**
     * 初始化跳转类
     */
    public void inidatecls() {
        //跳转的Activity
        cls.add(activity_courses.class);      //发布课程
        cls.add(acitivity_NewsServices.class);//发布服务
        cls.add(activity_servicetitle.class); //更多简介
        cls.add(activity_categories.class);   //擅长领域
        cls.add(activity_sevaluate.class);    //用户评价
        cls.add(activity_mylikeyou.class);    //我的粉丝
        cls.add(com.tencent.qcloud.tim.tuikit.live.BuildConfig.TYPE == 1 ? activity_picenter.class : activity_home_page.class);    //预览主页

        cls.add(activity_nickname2.class);    //个性签名
        cls.add(activity_jsonvideo.class);    //抖音解析
        cls.add(activity_audio_speed.class);           //美女1v1匹配
        cls.add(activity_thesamecity_speed.class);     //电话拨打
        cls.add(MEUN_MainActivity.class);      //这里一个主页
        cls.add(live_animation.class);        //直播动画
        cls.add(Withdrawals.class);           //我的提现
        cls.add(activity_photo_album.class);  //图片浏览
    }

    /**
     * 跳转指定页
     *
     * @param position
     */
    private void startActivitygo(int position) {
        Intent intent = new Intent(getContext(), cls.get(position));
        intent.putExtra(Constants.USERID, userInfo.getUserId());
        startActivity(intent);
        dismiss();

    }

}
