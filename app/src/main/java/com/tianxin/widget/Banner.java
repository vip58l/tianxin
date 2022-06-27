/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseFrameLayout;
import com.tianxin.Module.Datamodule;
import com.tianxin.Module.McallBack;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Logi;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.activity.Aboutus.activity_joinin;
import com.tianxin.activity.Image.activity_img_cover;
import com.tianxin.activity.LatestNews.activity_trend;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.activity.Withdrawal.activity_balance;
import com.tianxin.activity.Withdrawal.moneylog.activity_moneylog;
import com.tianxin.activity.activity_follow;
import com.tianxin.activity.activity_item.fragment_load;
import com.tianxin.activity.activity_music_play;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.edit.activity_nickname3;
import com.tianxin.activity.game.Game_Activity_show;
import com.tianxin.activity.loadmatching.activity_Loadmatching;
import com.tianxin.activity.matching.activity_audio_speed;
import com.tianxin.activity.party.activity_party_list;
import com.tianxin.activity.shareqrcode.activity_Share;
import com.tianxin.adapter.Radapter;
import com.tianxin.Test.MainActivityshow;
import com.tianxin.activity.ZYservices.activity_mycurriculum;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_msg_svip;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.Module.api.banner;
import com.tianxin.R;
import com.tianxin.Test.live_animation;
import com.tianxin.activity.Withdrawal.Withdrawals;
import com.tianxin.activity.ZYservices.acitivity_NewsServices;
import com.tianxin.activity.ZYservices.activity_servicetitle;
import com.tianxin.activity.ZYservices.activity_categories;
import com.tianxin.activity.DouYing.activity_jsonvideo;
import com.tianxin.activity.activity_list_column;
import com.tianxin.activity.edit.activity_nickname2;
import com.tianxin.activity.matching.activity_thesamecity_speed;
import com.tianxin.activity.meun.MEUN_MainActivity;
import com.tianxin.activity.activity_mylikeyou;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.navigation;

import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 首页banner
 */
public class Banner extends BaseFrameLayout implements ViewPager.OnPageChangeListener {
    public String TAG = Banner.class.getSimpleName();
    public ViewPager viewpager;
    public LinearLayout dot;
    public View voice;
    public LinearLayout navigationicon2;
    public RelativeLayout bannner;
    public myHandlers handlers = new myHandlers();

    public Banner(@NonNull Context context) {
        this(context, null);

    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void init() {
        context = getContext();
        userInfo = UserInfo.getInstance();
        inflate(context, R.layout.indexlay_pageview, this);
        dot = findViewById(R.id.dot);
        bannner = findViewById(R.id.bannner);
        navigationicon2 = findViewById(R.id.navigationicon2);
        voice = findViewById(R.id.adarwaiting);
        viewpager = findViewById(R.id.viewpager);
        datamodule = new Datamodule(context);
        viewpager.setAdapter(banneradapter = new pageadapter(context, list));
        viewpager.addOnPageChangeListener(this);
        viewpager.setCurrentItem(0);

        //首页四个图标导航跳转
        findViewById(R.id.banner_title1).setOnClickListener(v -> tostartActivity(1));
        findViewById(R.id.banner_title2).setOnClickListener(v -> tostartActivity(2));
        findViewById(R.id.banner_title3).setOnClickListener(v -> tostartActivity(3));
        findViewById(R.id.banner_title4).setOnClickListener(v -> tostartActivity(4));

        viewlist();         //首页图标UI 其他导航图标
        getdate();          //首页滚动大图banner
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: ");

    }

    @Override
    public void onPageSelected(int position) {
        //全部设置默认未选中状态
        for (int i = 0; i < dot.getChildCount(); i++) {
            dot.getChildAt(i).setSelected(false);
        }
        //已选择状态
        dot.getChildAt(position).setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:  //请求暂停轮播
                handlers.sendEmptyMessage(MSG_KEEP_SILENT);
                break;
            case ViewPager.SCROLL_STATE_IDLE:  //请求恢复轮播
                handlers.sendEmptyMessageDelayed(MSG_BREAK_SILENT, MSG_DELAY);
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
        Logi.d(TAG, "从窗口分离");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onStart();
        Logi.d(TAG, "附着到窗口上");

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Logi.d(TAG, "窗口上的焦点已更改 ");
    }

    /**
     * 请求首页大图轮播图
     */
    public void getdate() {
        if (handlers != null) {
            handlers.removeCallbacksAndMessages(null);
        }
        list.clear();
        banneradapter.notifyDataSetChanged();
        dot.removeAllViews();

        datamodule.getdsbanner(Constants.TENCENT, datebanner);
    }

    /**
     * 刷新重新请求数据
     */
    public void OnRefreshL() {
        getdate();
        viewlist();
    }

    /**
     * 消息通知滚动图片
     */
    protected class myHandlers extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //比如连续发送N条数据处理
            if (handlers.hasMessages(MSG_UPDATE_IMAGE)) {
                //移除未发送的通知
                handlers.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    //到送最后页
                    if (viewpager.getCurrentItem() == list.size() - 1) {
                        //回到初始页
                        viewpager.setCurrentItem(preDotPosition);
                    } else {
                        //页下滚动翻下页
                        viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                    }
                    //请求更新显示轮播 3秒后滚动
                    handlers.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //请求暂停轮播
                    break;
                case MSG_BREAK_SILENT:
                    //请求恢复轮播 等待3秒+3秒后滚动 3+3=6秒后轮播
                    handlers.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
            }
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    public void onStart() {
        if (handlers != null) {
            handlers.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
        }
    }

    public void onPause() {
        if (handlers != null) {
            handlers.removeCallbacksAndMessages(null);
        }

    }

    public void onDestroy() {
        if (handlers != null) {
            handlers.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 初始化首页导航图标
     */
    private void viewlist() {

        if (BuildConfig.TYPE == Constants.TENCENT && mlist.size() == 0) {
            mlist.add(activity_mycurriculum.class);       //我的课程
            mlist.add(activity_music_play.class);         //音乐播放
            mlist.add(activity_list_column.class);        //课程列表
            mlist.add(live_animation.class);              //直播动画
            mlist.add(MainActivityshow.class);            //礼物特效
            mlist.add(activity_audio_speed.class);        //美女1v1匹配 随机匹配
            mlist.add(activity_music_play.class);         //图片查看
            mlist.add(acitivity_NewsServices.class);      //发布咨询
            mlist.add(activity_servicetitle.class);       //添加简介
            mlist.add(activity_categories.class);         //添加分类
            mlist.add(Withdrawals.class);                 //我的提现
            mlist.add(activity_thesamecity_speed.class);  //电话拨打
            mlist.add(activity_mylikeyou.class);          //喜欢我的人
            mlist.add(activity_jsonvideo.class);          //抖音解析
            mlist.add(MEUN_MainActivity.class);           //这里一个主页
            mlist.add(activity_nickname2.class);          //个性签名
            mlist.add(activity_Share.class);              //好友注册
            mlist.add(activity_balance.class);            //我的钱包
            mlist.add(References.class);                  //赚钱任务
            mlist.add(activity_moneylog.class);           //现金明细
            mlist.add(activity_trend.class);              //最新动态
            mlist.add(activity_nickname3.class);          //意见反馈
            mlist.add(Detailedlist.class);                //我的金币
            mlist.add(activity_follow.class);             //我的关注
            mlist.add(activity_joinin.class);             //合作加盟
            mlist.add(activity_party_list.class);         //聚会管理
            mlist.add(Game_Activity_show.class);          //我的技能
            mlist.add(activity_Loadmatching.class);       //视频速配
            mlist.add(activity_img_cover.class);          //美图浏览
            mlist.add(fragment_load.class);               //短视频
        }

        if (BuildConfig.TYPE == Constants.TENCENT) {
            navigationicon2.removeAllViews();
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -1);
            radapter = new Radapter(context, listobj, Radapter.Banner2, UIICON);
            GridLayoutManager manager = new GridLayoutManager(context, 4);
            manager.setOrientation(RecyclerView.VERTICAL);
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(radapter);
            recyclerView.setLayoutParams(param);
            recyclerView.setFocusable(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            navigationicon2.addView(recyclerView);
            datamodule.navigation(UIICON);
        }
    }

    /**
     * 跳转指定页
     */
    private void startActivitygo(navigation navigation) {
        String path = navigation.getPath();
        int activity = navigation.getActivity();

        //0=url 1=APP 服务端获取判断 是网页访问还是打开ACTIVITY
        if (activity == 1) {
            //找到所有类名称一样跳打开指定ACTIVITY
            for (Object o : mlist) {
                Class<?> cls = (Class<?>) o;
                if (cls.getSimpleName().equals(navigation.getCls().trim())) {

                    if (cls.getSimpleName().equals("fragment_load")) {
                        fragment_load.starsetAction(context);
                        break;
                    }

                    startActivity(new Intent(context, cls));
                    break;
                }
            }
        } else {
            if (!TextUtils.isEmpty(path.trim())) {
                boolean contains = path.contains("?");
                String videoUrl = String.format("%s&userid=%s&token=%s", (contains ? path : path + "?"), userInfo.getUserId(), userInfo.getToken());
                DyWebActivity.starAction(context, videoUrl);
            }
        }
    }

    /**
     * 首页导航跳转
     */
    private void tostartActivity(int type) {
        switch (type) {
            case A1:
                if (userInfo.getState() >= Constants.TENCENT3) {
                    dialog_Blocked.myshow(context);
                    return;
                }
                //视频速配
                activity_Loadmatching.starsetAction(context);
                break;
            case A2:
                //随机匹配
                activity_audio_speed.starsetAction(context);
                break;
            case A3:
                //美图浏览
                activity_img_cover.starsetAction(context);

                //匹配滚动图
                //activity_new_boon.starsetAction(context);

                //聊天群大厅
                //ChatActivityMessage.setAction(context);
                break;
            case A4:
                //短视频
                fragment_load.starsetAction(context);
                break;
        }

    }

    /**
     * ui图标监听
     */
    Paymnets UIICON = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            List<navigation> navigations = (List<navigation>) object;
            listobj.clear();
            listobj.addAll(navigations);
            radapter.notifyDataSetChanged();
        }

        @Override
        public void onSuccess(String msg) {
            listobj.clear();
            radapter.notifyDataSetChanged();
        }

        @Override
        public void status(int position) {
            navigation navigation = (navigation) listobj.get(position);
            if (navigation.getVip() == 1 && userInfo.getVip() == 0) {
                dialog_msg_svip.dialogmsgsvip(context, getContext().getString(R.string.dialog_msg_svip66), context.getString(R.string.tv_msg228), context.getString(R.string.tv_msg153), paymnets3);
                return;
            }
            startActivitygo(navigation);
        }

        @Override
        public void ToKen(String msg) {

        }
    };

    Paymnets datebanner = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {
           //广告获取失败-->隐藏广告轮播图
            bannner.setVisibility(GONE);
        }

        @Override
        public void onSuccess(Object object) {
            List<banner> AdList = (List<banner>) object;
            for (banner banner : AdList) {
                ImageView bannerimg = new ImageView(context);
                bannerimg.setScaleType(ImageView.ScaleType.CENTER);
                Glide.with(getContext()).load(banner.getPicture()).into(bannerimg);
                bannerimg.setOnClickListener(v -> {
                    if (!TextUtils.isEmpty(banner.getPath())) {
                        boolean contains = banner.getPath().contains("?");
                        String videoUrl = String.format("%s&userid=%s&token=%s", (contains ? banner.getPath() : banner.getPath() + "?"), userInfo.getUserId(), userInfo.getToken());
                        DyWebActivity.starAction(context, videoUrl);
                    }
                });
                list.add(bannerimg);

                //4个小点点
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(20, 20);
                param.setMargins(10, 0, 10, 0);
                ImageView raordimg = new ImageView(context);
                raordimg.setImageDrawable(context.getResources().getDrawable(R.drawable.live_gift_dots));
                dot.addView(raordimg, param);
                dot.getChildAt(0).setSelected(true);
            }
            bannner.setVisibility(VISIBLE);
            banneradapter.notifyDataSetChanged();
            handlers.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY); //3秒后通知消息开始轮播
        }
    };

    private Paymnets paymnets3 = new Paymnets() {
        @Override
        public void onSuccess() {
            startActivity(new Intent(getContext(), activity_svip.class));
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(getContext());
        }

    };

}
