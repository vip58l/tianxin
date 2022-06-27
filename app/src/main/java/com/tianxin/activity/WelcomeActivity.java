package com.tianxin.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.dialog.dialog_first;
import com.tianxin.dialog.dialog_show_item_mpps;
import com.tianxin.listener.Paymnets;
import com.tianxin.R;
import com.tianxin.Util.StatusBarUtil;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;

/**
 * 欢迎页启动
 */
public class WelcomeActivity extends BasActivity2 {
    private static final String TAG = WelcomeActivity.class.getName();
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.NetworkInfo)
    TextView NetworkInfo;

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        return R.layout.activity_welcome;
    }

    @Override
    public void iniview() {
        if (!Config.isNetworkAvailable()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
                }
            });
            NetworkInfo.setVisibility(View.VISIBLE);
            handler.postDelayed(runnable, 1000);
            return;
        }
        if (checkinidate()) {
            startMain();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 检查写入权限和访问相册权限
     * Android 6.0以上需要申请权限
     */
    private boolean checkinidate() {
        if (permissionsto.getPemisson() == 1) {
            //已被申请过权限
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            onRequestPermissions.add(Manifest.permission.READ_PHONE_STATE);                  //获取权限
            onRequestPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);            //SD卡写入
            onRequestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            onRequestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            onRequestPermissions.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
            for (String permission : onRequestPermissions) {
                int checkSelfPermission = ContextCompat.checkSelfPermission(context, permission);
                if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                    //服务协议和隐私政策
                    dialog_first.key(context, paymnets);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取回调权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.requestCode) {
            for (String permission : permissions) {
                int checkSelfPermission = ContextCompat.checkSelfPermission(context, permission);
                //已授权标识 GRANTED---授权  DINIED---拒绝
                if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                    long name = System.currentTimeMillis();
                    permissionsto.setSussess(false);
                    permissionsto.setPemisson(1);
                    permissionsto.setMsg("授权失败");
                    permissionsto.setDate(String.valueOf(name));
                }
            }

            startMain();
        }
    }

    /**
     * 转到登录成功页Main
     */
    private void startMain() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userInfo != null) {
                    if (userInfo.isAutoLogin()) {
                        datamodule.welcomeLogin(new Paymnets() {
                            @Override
                            public void isNetworkAvailable() {

                            }

                            @Override
                            public void onFail() {
                                startLogin();
                            }

                            @Override
                            public void onFail(String msg) {
                                Toashow.show(msg);
                                startLogin();
                            }

                            @Override
                            public void onSuccess(Object object) {
                                mesresult = (Mesresult) object;

                                //本地获取加密签名串 登录腾讯IM
                                //login(userInfo.getUserId(),GenerateTestUserSig.genTestUserSig(userInfo.getUserId()));

                                //服务端获取加密签名串 登录腾讯IM
                                login(userInfo.getUserId(), mesresult.getUserSig());
                            }
                        });
                    } else {
                        startLogin();
                    }
                } else {
                    startLogin();
                }
            }
        }, 1500);
    }

    /**
     * 登录连接IM即时通信
     *
     * @param userid
     * @param getusersig
     */
    private void login(String userid, String getusersig) {
        TUIKit.login(userid, getusersig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startLogin();
                    }
                });
            }

            @Override
            public void onSuccess(Object data) {
                activity_sesemys.user_save_update_Profile(mesresult);
                starMainActivity();
            }
        });
    }

    private Paymnets paymnets = new Paymnets() {

        @Override
        public void onSuccess() {
            ActivityCompat.requestPermissions(activity, onRequestPermissions.toArray(new String[onRequestPermissions.size()]), Constants.requestCode);//去申请获取设备权限
        }

        @Override
        public void onFail() {
            dialog_show_item_mpps.myshow(context, this);
        }

        @Override
        public void onError() {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!Config.isNetworkAvailable()) {
                couneroo++;
                NetworkInfo.setText(String.format(getString(R.string.networkinfo_msg), String.valueOf(couneroo)));
                handler.postDelayed(this, 1000);
            } else {
                if (checkinidate()) {
                    startMain();
                }
            }
        }
    };


}

