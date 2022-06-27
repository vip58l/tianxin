package com.tianxin.activity.loadmatching;

import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.moxun.tagcloudlib.view.TagCloudView;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.SystemUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.loadmatching.adapte.TagCloudViewAdapter;
import com.tianxin.activity.loadmatching.model.StarModel;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.Camera.MediaPlayHelper;
import com.tencent.opensource.model.Music;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * 3D星球加速匹配数据
 */
public class activity_Loadmatching extends BasActivity2 {
    private static String TAG = activity_Loadmatching.class.getSimpleName();
    private TagCloudView mTagCloudView;
    private TagCloudViewAdapter mTagCloudViewAdapter;
    private List<StarModel> mStarModelList = new ArrayList<>();
    private AMapLocation aMapLocation;
    private final int whats = 200;
    private final int delayMillis = 2000;
    private boolean starv1v = false;
    private MediaPlayHelper mediaPlayHelper;
    public static activity_Loadmatching activityLoadmatching;
    private ImageView misc;

    public static void starsetAction(Context context){
        context.startActivity(new Intent(context, activity_Loadmatching.class));
    }


    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.activity_loadmatching;
    }

    @Override
    public void iniview() {
        misc = findViewById(R.id.misc);
        mTagCloudView = findViewById(R.id.mTagCloudView);
        mTagCloudViewAdapter = new TagCloudViewAdapter(context, mStarModelList);
        mTagCloudView.setAdapter(mTagCloudViewAdapter);
        mTagCloudView.setOnTagClickListener((parent, view, position) -> {
            String userId = mStarModelList.get(position).getUserId();
            String nickName = mStarModelList.get(position).getNickName();
            //点击跳转到个人主页
            //tostartActivity(userId, nickName);
        });
        if (ActivityLocation.checkpermissions(activity)) {
            com.tianxin.amap.lbsamap.getmyLocation(callback);
        }
        handler = new myHandler(this);
        handler.sendEmptyMessageDelayed(whats, delayMillis);
        mediaPlayHelper = new MediaPlayHelper(context);
        activityLoadmatching = this;
    }

    @Override
    public void initData() {
        datamodule.loadmatching(paymnets);
        datamodule.playmusic(callabckMinsc);
        datamodule.getallcharge(llcharge); //获取打电话基本配置
        datamodule.getbalance(balance);    //获取用户金币余额

    }

    @Override
    @OnClick({R.id.view_diagnose_radar_back, R.id.misc})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.misc:
                if (mediaPlayHelper.getmMediaPlayer().isPlaying()) {
                    mediaPlayHelper.getmMediaPlayer().pause();
                    misc.setImageResource(R.mipmap.ms_sp_boy_sound_close);
                } else {
                    mediaPlayHelper.getmMediaPlayer().start();
                    misc.setImageResource(R.mipmap.ms_sp_boy_sound_open);
                }
                break;
            default:
                finish();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    private List<StarModel> getData(List<member> member) {
        if (mStarModelList != null) mStarModelList.clear();
        for (int i = 0; i < member.size(); i++) {
            StarModel data = new StarModel();
            com.tencent.opensource.model.member member1 = member.get(i);
            data.setUserId(String.valueOf(member1.getId()));
            data.setNickName(member1.getTruename());
            data.setPhotoUrl(member1.getPicture());
            mStarModelList.add(data);
        }
        return mStarModelList;
    }

    private void tostartActivity(String userId, String JSON) {
        Intent intent = new Intent();
        intent.setClass(context, activity_picenter.class);
        intent.putExtra(Constants.USERID, userId);
        intent.putExtra(Constants.JSON, JSON);
        startActivity(intent);
    }

    private void dilog_Delayed() {
        datamodule.Randmember_User(aMapLocation, new Paymnets() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object object) {
                member member = (com.tencent.opensource.model.member) object;
                Dialogshowationstar(context, member, paymnets);
            }
        });
    }

    private Callback callback = new Callback() {
        @Override
        public void onSuccess(AMapLocation amapLocation) {
            aMapLocation = amapLocation;
        }
    };


    /**
     * 申请权限检查
     */
    public static boolean checkSel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);       //动态申请相机权限
            list.add(Manifest.permission.RECORD_AUDIO); //动态申请相机权限录音
            int permissionGranted = PackageManager.PERMISSION_GRANTED;
            int READ_PHONE_STATE = ContextCompat.checkSelfPermission(DemoApplication.instance(), list.get(0));//电话权限
            int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(DemoApplication.instance(), list.get(1));//SD卡权限
            if (READ_PHONE_STATE != permissionGranted || WRITE_EXTERNAL_STORAGE != permissionGranted) {
                SystemUtil.getPermission(activityLoadmatching, list);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String permission : permissions) {
            int granted = ContextCompat.checkSelfPermission(context, permission);
            if (granted != PackageManager.PERMISSION_GRANTED) {
                Toashow.show(getString(R.string.ts_dialog_call));
                handler.sendEmptyMessageDelayed(whats, delayMillis);
                return;
            }
        }
        switch (requestCode) {
            case OPEN_SET_REQUEST_CODE:
                if (ActivityLocation.checkPermissions(activity)) {
                    com.tianxin.amap.lbsamap.getmyLocation(callback);
                } else {
                    Toashow.show(getString(R.string.tm87));
                }
                break;
            case Config.sussess:
                Toashow.show(getString(R.string.tm91));
                handler.sendEmptyMessageDelayed(whats, delayMillis);
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        starv1v = true;
        if (mediaPlayHelper != null) {
            mediaPlayHelper.getmMediaPlayer().pause();
            handler.removeMessages(whats);
        }
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
        starv1v = false;
        if (mediaPlayHelper != null) {
            mediaPlayHelper.getmMediaPlayer().start();
        }
        handler.sendEmptyMessageDelayed(whats, delayMillis);


    }

    private class myHandler extends Handler {
        private WeakReference<activity_Loadmatching> weakReference;

        public myHandler(activity_Loadmatching activity_Loadmatching) {
            weakReference = new WeakReference<activity_Loadmatching>(activity_Loadmatching);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case whats:
                    dilog_Delayed();
                    break;
            }
        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            List<member> member = (List<com.tencent.opensource.model.member>) object;
            getData(member);
            mTagCloudViewAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void ToKen(String msg) {
            activity_Loadmatching.super.paymnets.ToKen(msg);
        }

        @Override
        public void cancellation() {
            if (!starv1v) {
                handler.sendEmptyMessageDelayed(whats, delayMillis);
            }
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            starv1v = true;
            switch (TYPE) {
                case 1:
                    //发起支付宝请求
                    cs_alipay csAlipay = new cs_alipay(context, new Paymnets() {
                        @Override
                        public void onFail() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                                }
                            });
                        }

                        @Override
                        public void activity(String str) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cs_alipay.showAlert(context, str);
                                }
                            });
                        }

                    });
                    csAlipay.Paymoney(moneylist);
                    break;
                case 2:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }
        }

        @Override
        public void onSuccessCAll(Object object) {
            //可以进入个人主页
            member member = (com.tencent.opensource.model.member) object;
            if (member != null) {
                activity_picenter.setActionactivity(context, String.valueOf(member.getId()));
            }
        }
    };

    private Paymnets callabckMinsc = new Paymnets() {
        @Override
        public void onFail(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object object) {
            Music music = (Music) object;
            if (!TextUtils.isEmpty(music.getUrl())) {
                mediaPlayHelper.startplay(music.getUrl());
            }

        }
    };

    private Paymnets balance = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            info info = (com.tencent.opensource.model.info) object;
            userInfo.setJinbi(info.getMoney());
        }


    };

    private Paymnets llcharge = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onSuccess(Object object) {
            Allcharge allcharge = (Allcharge) object;
            Allcharge.getInstance().setAudio(allcharge.getAudio());
            Allcharge.getInstance().setVideo(allcharge.getVideo());
        }

        @Override
        public void onFail() {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityLoadmatching = null;
        handler.removeMessages(whats);
        if (mediaPlayHelper != null) {
            mediaPlayHelper.stop();
            mediaPlayHelper = null;
        }
    }
}