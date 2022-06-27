package com.tianxin.activity.Memberverify;

import androidx.annotation.Nullable;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.tianxin.activity.LatestNews.activity_trend;
import com.tianxin.activity.videoalbum.activity_list_Images;
import com.tianxin.activity.videoalbum.activity_list_Video;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.pullableview.PullToRefreshLayout;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_settlein_item;
import com.tencent.opensource.model.Renzheng;
import com.tencent.opensource.model.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实名认证功能
 */
public class activity_livebroadcast extends BasActivity2 {
    @BindView(R.id.swiprefresh)
    PullToRefreshLayout swiprefresh;
    @BindView(R.id.itemback)
    itembackTopbr itemBack;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.stv1)
    TextView stv1;
    @BindView(R.id.stv2)
    TextView stv2;
    @BindView(R.id.stv3)
    TextView stv3;
    @BindView(R.id.stv4)
    TextView stv4;
    @BindView(R.id.stv5)
    TextView stv5;
    @BindView(R.id.stv6)
    TextView stv6;
    @BindView(R.id.stv7)
    TextView stv7;
    @BindView(R.id.stv8)
    TextView stv8;
    @BindView(R.id.tvleft)
    TextView tvleft;
    Renzheng renzheng;
    private final String TAG = activity_livebroadcast.class.getSimpleName();
    private String[] guild;

    public static void setAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_livebroadcast.class);
        //intent.setData(Uri.parse("http://www.baidu.com"));
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(activity, getResources().getColor(R.color.white));
        return R.layout.activity_livebroadcast;
    }

    @Override
    public void iniview() {
        itemBack.settitle(getString(R.string.tv_msg11));
        swiprefresh.setOnRefreshListener(pullToRefreshLayout);
        TYPE = getIntent().getIntExtra(Constants.TYPE, 0);
        guild = getResources().getStringArray(R.array.guild);
        String name = String.format("%s%s", TextUtils.isEmpty(userInfo.getGivenname()) ? userInfo.getName() : userInfo.getGivenname(), getString(R.string.tv_msg12));
        tv1.setText(name);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Glide.with(activity).load(userInfo.getAvatar()).into(icon);
        } else {
            try {
                icon.setImageResource(UserInfo.getInstance().getSex().equals("1") ? R.mipmap.boy_on : R.mipmap.girl_on);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initData() {
        datamodule.getusername(paymnets);
        datamodule.getrmard(false, paymnets);
    }

    private void settextitle(Renzheng renzheng) {

        switch (userInfo.getState()) {
            case 0:
                stv1.setText(getString(R.string.tv_msg7));
                break;
            case 1:
                stv1.setText(getString(R.string.tv_msg8));
                tostv1(stv1);
                break;
            case 2:
                String name = String.format("%s%s", TextUtils.isEmpty(userInfo.getGivenname()) ? userInfo.getName() : userInfo.getGivenname(), getString(R.string.tv_msg13));
                tv1.setText(name);
                stv1.setText(getString(R.string.tv_msg9));
                tostv1(stv1);
                break;
            case 3:
                stv1.setText(getString(R.string.tv_msg10));
                tostv1(stv1);
                break;
            default:
                stv1.setText(getString(R.string.tv_msg14));
                tostv1(stv1);
                break;
        }
        stv3.setText(renzheng.getCover() >= 1 ? getString(R.string.livebr_tv3) : renzheng.getCover() + " " + getString(R.string.livebr_tv4));    //封面
        stv4.setText(renzheng.getPhoto() >= 6 ? getString(R.string.livebr_tv3) : renzheng.getPhoto() + " " + getString(R.string.livebr_tv4));    //相册
        stv5.setText(renzheng.getVideo() >= 2 ? getString(R.string.livebr_tv3) : renzheng.getVideo() + " " + getString(R.string.livebr_tv4));    //视频
        stv6.setText(renzheng.getTrend() >= 5 ? getString(R.string.livebr_tv3) : renzheng.getTrend() + " " + getString(R.string.livebr_tv4));    //动态
        stv7.setText(renzheng.getFollow() >= 10 ? getString(R.string.livebr_tv3) : renzheng.getFollow() + " " + getString(R.string.livebr_tv4)); //关注

        if (renzheng.getCover() >= 1) {
            stv3.setTextColor(getResources().getColor(R.color.rtc_green_bg));
        }
        if (renzheng.getPhoto() >= 6) {
            stv4.setTextColor(getResources().getColor(R.color.rtc_green_bg));
        }
        if (renzheng.getVideo() >= 2) {
            stv5.setTextColor(getResources().getColor(R.color.rtc_green_bg));
        }
        if (renzheng.getTrend() >= 5) {
            stv6.setTextColor(getResources().getColor(R.color.rtc_green_bg));
        }
        if (renzheng.getFollow() >= 10) {
            stv7.setTextColor(getResources().getColor(R.color.rtc_green_bg));

        }

        //申请主播功能要求已完成目标
        if (renzheng.getCover() >= 1 && renzheng.getPhoto() >= 6 && renzheng.getVideo() >= 2 && renzheng.getTrend() >= 5 && renzheng.getFollow() >= 10) {
            stv8.setBackground(getResources().getDrawable(R.drawable.bg_radius_bottom_pink4));
            stv8.setText(getString(R.string.livebr_tv3));
        }

    }

    @OnClick({R.id.lin11, R.id.lin22, R.id.icon, R.id.lin1, R.id.lin2, R.id.lin3, R.id.lin4, R.id.lin5, R.id.lin6, R.id.contentPanel})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.lin11:
                activity_Namecenter.starsetAction(context);
                break;
            case R.id.lin22:
                if (userInfo.getState() == Constants.TENCENT2) {
                    if (renzheng.getPhoto() >= 6 && renzheng.getVideo() >= 2 && renzheng.getTrend() >= 5 && renzheng.getFollow() >= 10) {
                        dialog_settlein_item.dialogettleinitem(context, paymnets);
                    } else {
                        Toashow.show(getString(R.string.tm125));
                    }

                } else {
                    Toashow.show(getString(R.string.nametoashow));
                }
                break;
            case R.id.lin1:
            case R.id.lin2:
                //上传照片
                activity_list_Images.starsetAction(context);
                break;
            case R.id.lin3:
                //上传视频
                activity_list_Video.starsetAction(context);
                break;
            case R.id.lin4:
                //最新动态
                activity_trend.starsetAction(context);
                break;
            case R.id.lin5:
                if (renzheng.getFollow() >= 10) {
                    Toashow.show(getString(R.string.tm126));
                } else {
                    Toashow.show(getString(R.string.tm124));
                }
                break;
            case R.id.contentPanel:
            case R.id.icon:
                //编辑更多资料
                startActivityForResult(new Intent(context, activity_updateedit.class), Config.sussess);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Config.sussess == requestCode) {
            datamodule.getusername(paymnets);
        }
    }

    private void tostv1(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.c_main));
    }

    /**
     * 下拉或上拉获取数据
     */
    PullToRefreshLayout.OnRefreshListener pullToRefreshLayout = new PullToRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            initData();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            initData();
        }


    };

    /**
     * 返回回调
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onSuccess(Object object) {
            renzheng = (Renzheng) object;
            userInfo.settRole(renzheng.gettRole());
            userInfo.setVip(renzheng.getVip());
            userInfo.setLevel(renzheng.getLevel());
            userInfo.setSex(String.valueOf(renzheng.getSex()));
            userInfo.setName(renzheng.getAlias());
            userInfo.setAvatar(renzheng.getPicture());
            settextitle(renzheng);
        }

        @Override
        public void onSuccess(String msg) {
            if (TextUtils.isEmpty(msg)) {
                return;
            }
            switch (Integer.parseInt(msg)) {
                case 1:
                    stv2.setText(guild[0]);
                    break;
                case 2:
                    stv2.setText(guild[1]);
                    break;
            }
        }

        @Override
        public void onSuccess() {
            datamodule.getrmard(false, this);
        }
    };
}