/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/23 0023
 */


package com.tianxin.BasActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tianxin.Fragment.page1.fragment.one2;
import com.tianxin.Fragment.page1.fragment.one4;
import com.tianxin.Fragment.page2.fragment.LiveTuivoiceRoom;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.scenes.LiveRoomAnchorActivity;
import com.tianxin.IMtencent.scenes.LiveRoomFragment;
import com.tianxin.Fragment.page1.fragment.one3;
import com.tianxin.Fragment.fragment.Fromnetfollowlist;
import com.tianxin.Fragment.page2.fragment.List_fuli_two;
import com.tianxin.IMtencent.scenes.adapter.RoomListAdapter;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.LatestNews.activity_news;
import com.tianxin.activity.Memberverify.activity_Namecenter;
import com.tianxin.activity.ZYservices.activity_myper;
import com.tianxin.activity.Searchactivity.activity_select;
import com.tianxin.activity.picenter.activity_picbage;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.adapter.setAdapter;
import com.tianxin.amap.baidumap;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Loading;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_item_view_zb;
import com.tianxin.dialog.dialog_success;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.Permissionsto;
import com.tianxin.widget.Banner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.liteav.TUIVoiceRoomLogin;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.perimg;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 公共模板
 */
public abstract class BasFragment extends Fragment {
    public String TAG = BasFragment.class.getSimpleName();
    public Unbinder bind;
    public UserInfo userInfo;
    public Context context;
    public Activity activity;
    public List<item> list = new ArrayList<>();
    public List<Object> list2 = new ArrayList<>();
    public List<String> mlist = new ArrayList<>();
    public List<Fragment> fragmentslist = new ArrayList<>();
    public Gson gson = new Gson();
    public AMapLocation mapLocation;
    public setAdapter setAdapter;
    public Radapter radapter;
    public Banner banner;
    public int totalPage;
    public int style = 1;
    public int type = 1;
    public int currentPage = 1;
    public int countPage = 20;
    public int mCurrentItem;
    public int TYPE;
    public int sex;
    public boolean Page_Date;
    public Bundle savedInstanceState;
    public baidumap baidumap;
    public setViewPager setViewPager;
    public setViewPager adapter;
    public TiktokAdapter mAdapter;
    public Permissionsto permissionsto;
    public Datamodule datamodule;
    public String getUserId;

    public View rootView;
    public final int OPEN_FILE_CODE = 10001;
    public com.tencent.opensource.model.info info;
    public Allcharge allcharge;
    public com.tencent.opensource.model.member member;
    public BasestartActivity basestartActivity;
    public MyDecoration myDecoration;
    public TUIVoiceRoomLogin tuiVoiceRoomLogin;
    public Dialog_Loading dialog_loading;
    private Fragment f1,f2,f3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        return getview(inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        datamodule = new Datamodule(context, paymnets);
        userInfo = UserInfo.getInstance();
        permissionsto = Permissionsto.getInstance();
        context = getContext();
        activity = (Activity) getContext();
        baidumap = new baidumap();
        basestartActivity = new BasestartActivity(context);
        myDecoration = new MyDecoration(context, LinearLayout.HORIZONTAL);
        iniview();
        initData();
    }

    public abstract View getview(LayoutInflater inflater);

    public abstract void iniview();

    public abstract void initData();

    public abstract void OnClick(View v);

    public abstract void OnEorr();

    public abstract void onRefresh();

    public void loadMoreData() {
        if (Config.isNetworkAvailable()) {
            totalPage = 0;
            list2.clear();
            radapter.notifyDataSetChanged();
            initData();
        } else {
            Toashow.show(getString(R.string.eorrfali2));
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    public Callback callback = new Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onSuccess(AMapLocation amapLocation) {
            mapLocation = amapLocation;
            Datamodule.getInstance().toAMapLocation(mapLocation, null);
            if (setAdapter != null) {
                setAdapter.setSamapLocation(mapLocation);
            }
            if (radapter != null) {
                radapter.setaMapLocation(mapLocation);
            }


        }
    };

    public Paymnets paymnets = new Paymnets() {

        @Override
        public void onSuccess() {
            tostartActivity(activity_Namecenter.class);
        }

        @Override
        public void ToKen(String msg) {
            Toashow.show(msg);
            BaseActivity.logout(context);
        }


    };

    public void tostartActivity(Class<?> cls) {
        startActivity(new Intent(DemoApplication.instance(), cls));
    }

    public void tostartActivity(Class<?> cls, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        startActivityForResult(intent, requestCode);
    }

    public void tostartActivity(Class<?> cls, int position, int requestCode, String msg) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.Msg, msg);
        startActivityForResult(intent, requestCode);
    }

    public void tostartActivity(Class<?> cls, String json) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.JSON, json);
        startActivity(intent);
    }

    public void tostartActivity(Class<?> cls, String json, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.JSON, json);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 设置字体大小
     *
     * @param tab
     */
    public void Tabtext(TabLayout.Tab tab) {
        TextView textView = new TextView(context);
        textView.setTextSize(22);
        textView.setTextColor(Color.DKGRAY);
        textView.setText(tab.getText());
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        tab.setCustomView(textView);
    }

    /**
     * 心理咨询
     */
    public List<Fragment> xlzyshow() {
        fragmentslist.add(one3.typeshow(1));
        fragmentslist.add(one3.typeshow(2));
        fragmentslist.add(one3.typeshow(3));
        fragmentslist.add(Fromnetfollowlist.args(0));
        return fragmentslist;
    }

    /**
     * 首页列表展示
     */
    public List<Fragment> jyshow() {
        fragmentslist.add(one2.show(1));//首页    列表
        //fragmentslist.add(one2.show(2));//同城       列表
        //fragmentslist.add(one2.show(3));//附近人     列表
        //fragmentslist.add(one4.show(1, 1));//首页    列表

        fragmentslist.add(one4.show(2, 2));//同城     方格模板
        fragmentslist.add(one4.show(3, 3));//附近人   方格模板
        fragmentslist.add(one4.show(4, 4));//视频聊天  方格模板

        //fragmentslist.add(one5.show(1));
        //fragmentslist.add(one1.show(1));

        //fragmentslist.add(one6.show(1));//聚会列表
        return fragmentslist;
    }

    /**
     * 直播列表 返回3个Fragment
     *
     * @return
     */
    public List<Fragment> ListFragment() {
        if (fragmentslist.size() == 0) {
            fragmentslist.add(f2=new LiveRoomFragment());     //采集直播源
            fragmentslist.add(f1=new List_fuli_two());  //视频直播源
            fragmentslist.add(f3=new LiveTuivoiceRoom());  //音频直播源
        }
        return fragmentslist;
    }

    /**
     * 搜索好友
     */
    public void tostartActivity() {
        startActivity(new Intent(context, activity_select.class));
    }

    /**
     * 开直播
     */
    public void tostartActivity2() {

        if (userInfo.getState() != 2) {
            //提示认证实名
            dialog_item_view_zb.b(context, paymnets);
        } else {
            LiveRoomAnchorActivity.start(context, "");
        }
    }

    /**
     * 检查申请权限
     *
     * @return
     */
    public boolean checkpermissions() {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        for (String permission : list) {
            int star = ContextCompat.checkSelfPermission(activity, permission);
            if (star != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     */
    public boolean checkpermissions(int OPEN_SET_REQUEST_CODE) {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        for (String permission : list) {
            int star = ContextCompat.checkSelfPermission(activity, permission);
            if (star != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(list.toArray(new String[list.size()]), OPEN_SET_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    /**
     * 打开播放器
     *
     * @param videolist
     * @param play
     * @param imgs
     * @param position
     */
    public void tostartActivity(videolist videolist, String play, String imgs, int position) {
        Intent intent = new Intent(getContext(), videoijkplayer0.class);
        intent.putExtra(com.tianxin.Util.Constants.TITLE, videolist.getTitle());
        intent.putExtra(com.tianxin.Util.Constants.PATHVIDEO, play);
        intent.putExtra(com.tianxin.Util.Constants.PATHIMG, imgs);
        intent.putExtra(com.tianxin.Util.Constants.Edit, false);
        intent.putExtra(com.tianxin.utils.Constants.POSITION, position);
        startActivity(intent);
    }

    /**
     * 打开播放器
     *
     * @param videolist
     * @param play
     * @param imgs
     * @param position
     */
    public void tostartActivity(videolist videolist, String play, String imgs, int position, boolean showview) {
        Intent intent = new Intent(getContext(), videoijkplayer0.class);
        intent.putExtra(com.tianxin.Util.Constants.TITLE, videolist.getTitle());
        intent.putExtra(com.tianxin.Util.Constants.PATHVIDEO, play);
        intent.putExtra(com.tianxin.Util.Constants.PATHIMG, imgs);
        intent.putExtra(com.tianxin.Util.Constants.Edit, false);
        intent.putExtra(com.tianxin.utils.Constants.POSITION, position);
        intent.putExtra(com.tianxin.Util.Constants.showview, showview);
        startActivity(intent);
    }

    /**
     * 打开图片浏览
     *
     * @param array
     */
    public void tostartActivity(List<perimg> array) {
        Intent intent = new Intent();
        intent.setClass(getContext(), activity_picbage.class);
        intent.putExtra(Constants.POSITION, 0);
        intent.putExtra(Constants.perimg, (Serializable) array);
        startActivity(intent);
    }

    /**
     * 打开个人主页
     *
     * @param member
     */
    public void tostartActivity(member member) {
        if (member == null) {
            return;
        }
        //new Intent(getContext(), BuildConfig.TYPE == 1 ? activity_home_page.class : activity_myper.class).putExtra(Constants.USERID, String.valueOf(member.getId())));
        Intent intent = new Intent(context, BuildConfig.TYPE == Constants.TENCENT ? activity_picenter.class : activity_myper.class);
        intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    /**
     * 发布动态
     */
    public void activitynews() {

        //判断封号或者是否需要实名认证
        switch (userInfo.getState()) {
            case 0:
            case 1:
                //要求你实名认证
                if (userInfo.getReale() == 1) {
                    dialog_success.show(context, getString(R.string.tm133), new Paymnets() {
                        @Override
                        public void onSuccess() {
                            activity_Namecenter.starsetAction(context);
                        }

                        @Override
                        public void onFail() {

                        }
                    });
                    return;
                }
                break;
            case 3:
                //封号提示
                dialog_Blocked.myshow(context);
                return;
        }

        startActivityForResult(new Intent(context, activity_news.class), Config.sussess);
    }

    /**
     * 跳转到播放器
     *
     * @param cls
     * @param position
     */
    protected void sstartActivityForResult(Class<?> cls, int position, int totalPage, int type, List<item> list) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.TOTALPAGE, totalPage);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.dadelist, (Serializable) list);
        startActivityForResult(intent, Config.sussess);
    }


    /**
     * 加载中弹窗...
     */
    public void showDialogFrag() {
        if (dialog_loading != null) {
            if (dialog_loading.isShowing()) {
                dialog_loading.cancel();
            }
        }
        dialog_loading = Dialog_Loading.dialogLoading(getActivity());
    }

    /**
     * 关加加载中...
     */
    public void dismissDialogFrag() {
        if (dialog_loading != null) {
            dialog_loading.dismiss();
        }

    }

    /**
     * 初始化加载数据
     *
     * @param smartRefreshLayout
     */
    public static void smartRefresh(SmartRefreshLayout smartRefreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                smartRefreshLayout.autoRefresh();
            }
        }, 1000);
    }


}
