/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/18 0018
 */


package com.tianxin.BasActivity;

import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_STATE;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_TOKEN;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_TYPE;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_USERID;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIP;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.Module.ControlPanel;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.ViewPager.YPagerAdapter2;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.activity.LatestNews.upfile.Fileupdate;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Test.MyOpenhelper;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.Login.UserLoginActivity;
import com.tianxin.activity.ZYservices.activity_photo_album;
import com.tianxin.activity.register.activity_register;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.adapter.Radapter;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.adapter.setAdapter;
import com.tianxin.amap.lbsamap;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Loading;
import com.tianxin.dialog.dialog_activity_Megsse;
import com.tianxin.dialog.dialog_load;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.Permissionsto;
import com.tencent.opensource.dialog.AppManager;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.costransferpractice.object.ObjectActivity;
import com.tencent.qcloud.costransferpractice.transfer.UploadActivity;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BasActivity2 extends AppCompatActivity {
    private static final String TAG = BasActivity2.class.getSimpleName();
    public List<Object> list = new ArrayList<>();
    public List<Object> clslist = new ArrayList<>();
    public List<Fragment> fragments = new ArrayList<>();
    public List<String> onRequestPermissions = new ArrayList<>();
    public Unbinder unbinder;
    public BasActivity2 context;
    public BasActivity2 activity;
    public UserInfo userInfo;
    public MyOpenhelper openhelper;
    public AMapLocation mapLocation;
    public int totalPage;
    public int TYPE;
    public int couneroo;
    public int certificates;
    public int endtotalPage;
    public int mCurrentItem;
    public Gson gson = new Gson();
    public final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public Fileupdate fileupdate;
    public Permissionsto permissionsto;
    public dialog_load myshow;
    public lbsamap lbsamap;
    public int timers = 60;
    public boolean truecode = true;
    public Radapter radapter, radapter2;
    public setViewPager adapter;
    public pageadapter vpageadapter;
    public YPagerAdapter2 ypagerAdapter;
    public TiktokAdapter mAdapter;
    public String userid;
    public member member;
    public String getuserid;
    public setAdapter adappter;
    public int mPlayingPosition;
    public int sex = 1;
    public Handler handler = new Handler(Looper.getMainLooper());
    public Datamodule datamodule;
    public Mesresult mesresult;
    public final int OPEN_FILE_CODE = 10001;
    public final int OPEN_CAMERA_CODE = 10002;
    public final int IMAGE_CROP_CODE = 10003;
    public File file;
    public boolean netonk = false;
    public BasestartActivity basestartActivity;
    public ProgressDialog progressDialog;
    public Dialog_Loading dialogLoading;
    public String JSON;
    public String videoUrl;
    public ControlPanel controlPanel;
    dialog_activity_Megsse ationstar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将Activity实例添加到AppManager的堆栈
        AppManager.getAppManager().addActivity(activity);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        //第一种方法 首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        // 第二种方法 避免从桌面启动程序后，会重新实例化入口类的activity
        /*if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }*/
        context = this;
        activity = this;
        BackgroundTasks.initInstance();
        StatusBarUtil.transparencyBar(activity);
        StatusBarUtil.setStatusBar(activity, Color.WHITE);
        setContentView(getview());
        openhelper = MyOpenhelper.getOpenhelper();
        userInfo = UserInfo.getInstance();
        permissionsto = Permissionsto.getInstance();
        unbinder = ButterKnife.bind(this);
        fileupdate = new Fileupdate(context);           //上传文件接口
        myshow = dialog_load.myshow(context);           //弹窗消息
        datamodule = new Datamodule(context, paymnets); //请求网络接口
        progressDialog = new ProgressDialog(context);
        basestartActivity = new BasestartActivity(context);
        iniview();
        initData();

    }

    /**
     * 设置沉浸式状态栏
     */
    public void setTranslucentStatus(boolean b) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            //版本小于4.4
            return;
        }
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bit = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (b) {
            winParams.flags |= bit;
        } else {
            winParams.flags &= ~bit;
        }
    }

    protected abstract int getview();

    public abstract void iniview();

    public abstract void initData();

    public abstract void OnClick(View v);

    public abstract void OnEorr();

    public void loadMoreData() {
        list.clear();
        totalPage = 0;
        radapter.notifyDataSetChanged();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        clslist.clear();
        fragments.clear();
        onRequestPermissions.clear();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (unbinder != null) {
            unbinder.unbind();
        }
        //将Activity实例从AppManager的堆栈中移除
        AppManager.getAppManager().finishActivity(activity);
    }

    /**
     * 转到登录页login
     */
    public static void startLogin() {
        UserInfo.getInstance().setToken("");
        UserInfo.getInstance().setAutoLogin(false);
        Intent intent = new Intent(DemoApplication.instance(), activity_register.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);
    }

    /**
     * 转到登录页login
     */
    public static void startLogin2() {
        UserInfo.getInstance().setToken("");
        UserInfo.getInstance().setAutoLogin(false);
        Intent intent = new Intent(DemoApplication.instance(), UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);
    }
    /**
     * 进入主页
     */
    public void starMainActivity() {
        Intent intent = new Intent();
        intent.setClass(context, userInfo.isAutoLogin() ? com.tianxin.activity.Main.MainActivity.class : UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 回调
     */
    public Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
        }

        @Override
        public void activity() {

        }

        @Override
        public void onLoadMore() {
            ToastUtil.toastLongMessage(DemoApplication.instance().getString(R.string.tv_msg1933));
            BaseActivity.logout(DemoApplication.instance());

        }

        @Override
        public void onRefresh() {
            dialog_Blocked.myshow(context, this);
        }

        @Override
        public void onSuccess() {
            BaseActivity.logout(DemoApplication.instance());
        }

        @Override
        public void fall(int code) {

        }

        @Override
        public void success(String date) {

        }

        @Override
        public void ToKen(String msg) {
            Toashow.show(msg);
            BaseActivity.logout(DemoApplication.instance());
        }
    };

    /**
     * 回调
     */
    public com.tianxin.listener.Callback callback = new Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFall() {

        }

        @Override
        public void onSuccess(AMapLocation amapLocation) {
            mapLocation = amapLocation;
            Datamodule.getInstance().toAMapLocation(mapLocation, null);
        }
    };

    /**
     * 加载中弹窗...
     */
    public void showDialog() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(getString(R.string.tv_ss_dialog_msg));
        progressDialog.show();
    }

    /**
     * 关加加载中...
     */
    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showdialogLoadings() {
        if (dialogLoading != null) {
            if (dialogLoading.isShowing()) {
                dialogLoading.dismiss();
            }
        }
        dialogLoading = Dialog_Loading.dialogLoading(context, getString(R.string.reg_dialog_tv));
    }

    public void dialogLoadings() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }

    public void dialogshow() {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
        dialogLoading = Dialog_Loading.dialogLoading(context, getString(R.string.tv_msg50));
    }

    public void dialogshow(String msg) {
        if (dialogLoading != null && dialogLoading.isShowing()) {
            dialogLoading.dismiss();
        }
        dialogLoading = Dialog_Loading.dialogLoading(context, msg);
    }

    public void toastLongMessage(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toastLongMessage(msg);
                finish();
            }
        });
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
        Intent intent = new Intent(context, videoijkplayer0.class);
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
     * @param play
     * @param imgs
     * @param position
     */
    public void tostartActivity(String title, String play, String imgs, int position) {
        Intent intent = new Intent(context, videoijkplayer0.class);
        intent.putExtra(com.tianxin.Util.Constants.TITLE, title);
        intent.putExtra(com.tianxin.Util.Constants.PATHVIDEO, play);
        intent.putExtra(com.tianxin.Util.Constants.PATHIMG, imgs);
        intent.putExtra(com.tianxin.Util.Constants.Edit, false);
        intent.putExtra(com.tianxin.utils.Constants.POSITION, position);
        startActivity(intent);
    }

    public void tostartActivity(Class<?> cls, int TYPE) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.TYPE, TYPE);
        startActivity(intent);
    }

    public void tostartActivity(Class<?> cls, String userid) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.USERID, userid);
        startActivity(intent);
    }

    public void tostartActivity(Class<?> cls, String userid, String JSON) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.JSON, JSON);
        intent.putExtra(Constants.USERID, userid);
        startActivity(intent);
    }

    public void tostartActivity(Class<?> cls, int TYPE, int requestCode) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.TYPE, TYPE);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 浏览图片
     *
     * @param path
     */
    public void activity_photo_album(String path) {
        Intent intent = new Intent(DemoApplication.instance(), activity_photo_album.class);
        intent.putExtra(Constants.PATHVIDEO, path);
        startActivity(intent);
    }

    /**
     * 打开上传功能
     */
    public void sUploadActivity(int TYPE) {
        // Toashow.show(activity_list_video1.this, "无权限上传请与客服联系");
        //Intent intent = new Intent();
        //提前在清单 文件定义好动作标准 com.bucket.BucketActivity
        //intent.addCategory("android.intent.category.DEFAULT");
        //展示主界面相关内容
        //intent.setAction("com.bucket.BucketActivity");
        //startActivity(intent);

        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.isNetworkAvailable));
            return;
        }
        Intent intent = new Intent(context, UploadActivity.class);
        intent.putExtra(ACTIVITY_USERID, userInfo.getUserId());
        intent.putExtra(ACTIVITY_TYPE, TYPE);
        intent.putExtra(ACTIVITY_VIP, userInfo.getVip());
        intent.putExtra(ACTIVITY_STATE, userInfo.getState());
        intent.putExtra(ACTIVITY_TOKEN, userInfo.getToken());
        try {
            //GPS定位
            if (!TextUtils.isEmpty(mapLocation.getProvince())) {
                intent.putExtra(ObjectActivity.province, mapLocation.getProvince());
                intent.putExtra(ObjectActivity.city, mapLocation.getCity());
                intent.putExtra(ObjectActivity.district, mapLocation.getDistrict());
                intent.putExtra(ObjectActivity.address, mapLocation.getAddress());
                intent.putExtra(ObjectActivity.jwd, mapLocation.getLatitude() + "," + mapLocation.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        startActivityForResult(intent, Config.sussess);
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
     * 弹出随机匹配会员
     *
     * @param context
     * @param object
     * @param paymnets
     */
    public void Dialogshowationstar(Context context, Object object, Paymnets paymnets) {
        if (ationstar != null && ationstar.isShowing()) {
            ationstar.dismiss();
        }
        ationstar = dialog_activity_Megsse.ationstar(context, object, paymnets);
    }

}
