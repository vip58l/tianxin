package com.tianxin.Test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.common.BitmapUtils;
import com.tianxin.R;
import com.tianxin.Test.MDEMO.Account;
import com.tianxin.Test.MDEMO.Callback;
import com.tianxin.Test.MVC.model;
import com.tianxin.Test.MVP.Persenter;
import com.tianxin.Util.SystemUtil;
import com.tianxin.Util.Config;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSignalingInfo;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSignalingManager;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.agora.tutorials1v1vcall.activity.VideoChatViewActivity;

public class activity_Nornal extends AppCompatActivity {
    String TAG = "activity_Nornal";
    EditText edit;
    TextView restart;
    Persenter persenter;
    ImageView mImage;
    private static final int REQUEST_CODE = 100;
    V2TIMManager v2TIMManager;
    V2TIMSignalingManager signalingManager;
    TRTCAVCallImpl mITRTCAVCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__nornal);

        mITRTCAVCall = (TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAVCallListener);
        mITRTCAVCall.setWaitingLastActivityFinished(false);

        v2TIMManager = V2TIMManager.getInstance();
        signalingManager = V2TIMManager.getSignalingManager(); //信令接口
        signalingManager.addSignalingListener(v2TIMSignalingListener);

        persenter = new Persenter();
        edit = findViewById(R.id.edit);
        restart = findViewById(R.id.restart);
        mImage = findViewById(R.id.mImage);
        findViewById(R.id.button0).setOnClickListener(v -> OnClick());
        findViewById(R.id.button1).setOnClickListener(v -> OnClickMVC());
        findViewById(R.id.button2).setOnClickListener(v -> OnClickMVP());
        findViewById(R.id.button3).setOnClickListener(v -> OnClickMVVM());
        findViewById(R.id.button4).setOnClickListener(v -> checkinidate());
        findViewById(R.id.button5).setOnClickListener(v -> qrcode());

    }

    private void OnClick() {
        getAccountDate("普通模式 v -> OnClick()" + getUserinpul(), new Callback() {
            @Override
            public void onSuccess(Account account) {
                myonSuccess(account);
            }

            @Override
            public void onFailed() {
                myonFailed();
            }
        });
    }

    private void OnClickMVC() {
        model model = new model();
        model.getAccountDate("MVC模式 v -> OnClickMVC()" + getUserinpul(), new Callback() {
            @Override
            public void onSuccess(Account account) {
                myonSuccess(account);
            }

            @Override
            public void onFailed() {
                myonFailed();
            }
        });
    }

    private void OnClickMVP() {
        persenter.setMview(new Callback() {
            @Override
            public void onSuccess(Account account) {
                myonSuccess(account);
            }

            @Override
            public void onFailed() {
                myonFailed();
            }
        });
        persenter.getdate("MVP模式" + getUserinpul());
    }

    private void OnClickMVVM() {
        String userid = edit.getText().toString().trim();
        invite(!TextUtils.isEmpty(userid) ? userid : "2201");
    }

    //4.1打开CaptureActivity扫描
    private void Onclick() {
        startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);
    }

    //4.2传入内容生成二维码
    private void qrcode() {
        String content = "http://www.baid.om";

        try {
            Bitmap bitmap = BitmapUtils.create2DCode(content);
            mImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAccountDate(String name, Callback callback) {
        Random random = new Random();
        boolean isSuccess = random.nextBoolean();
        if (isSuccess) {
            Account account = new Account();
            account.setName(name);
            account.setAge(18);
            account.setLevel(20);
            callback.onSuccess(account);
        } else {
            callback.onFailed();
        }

    }

    private String getUserinpul() {
        return edit.getText().toString();
    }

    public void myonSuccess(Account account) {
        restart.setText(account.getName() + " " + account.getAge());
    }

    public void myonFailed() {
        restart.setText("获取失败");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && data != null) {
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            restart.setText(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //已授权标识 GRANTED---授权  DINIED---拒绝
        if (requestCode == Config.sussess) {
            for (String permission : permissions) {
                int granted = ContextCompat.checkSelfPermission(this, permission);
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, Config.sussess);
                    return;
                }
            }

            //所有权限已获取
            Onclick();
        }

    }


    /**
     * 检查写入权限和访问相册权限
     */
    private void checkinidate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);                            //相机权限
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);            //SD卡写入
            for (String permission : list) {
                int star = ContextCompat.checkSelfPermission(this, permission);
                if (star != PackageManager.PERMISSION_GRANTED) {
                    SystemUtil.getPermission(this, list);
                    return;
                }
            }
        }
        Onclick();
    }


    private void ssss() {


        //移除信令监听
        signalingManager.removeSignalingListener(v2TIMSignalingListener);

        //邀请方取消邀请
        signalingManager.cancel("2200", "5500", new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });

        //reject接收方拒绝邀请
        signalingManager.reject("11", "", new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });

        //获取信令信息
        signalingManager.getSignalingInfo(new V2TIMMessage());

        //添加邀请信令（可以用于群离线推送消息触发的邀请信令）。
        signalingManager.addInvitedSignaling(new V2TIMSignalingInfo(), new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        signalingManager.removeSignalingListener(v2TIMSignalingListener);
    }

    //添加信令监听
    private final V2TIMSignalingListener v2TIMSignalingListener = new V2TIMSignalingListener() {
        @Override
        public void onReceiveNewInvitation(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
            super.onReceiveNewInvitation(inviteID, inviter, groupID, inviteeList, data);
            Log.d(TAG, "收到邀请: " + inviter + " " + groupID + " 我的ID号" + inviteeList.get(0) + " " + data);

            if (!inviteeList.get(0).equals(UserInfo.getInstance().getUserId())) {
                Log.d(TAG, "不是邀请我的: ");
                return;
            }
            accept(inviteID, data);
        }

        @Override
        public void onInviteeAccepted(String inviteID, String invitee, String data) {
            super.onInviteeAccepted(inviteID, invitee, data);
            Log.d(TAG, "被邀请者接受邀请: " + inviteID + " " + data);
        }

        @Override
        public void onInviteeRejected(String inviteID, String invitee, String data) {
            super.onInviteeRejected(inviteID, invitee, data);
            Log.d(TAG, "被邀请者拒绝邀请: " + inviteID + " " + invitee + " " + data);
        }

        @Override
        public void onInvitationCancelled(String inviteID, String inviter, String data) {
            super.onInvitationCancelled(inviteID, inviter, data);
            Log.d(TAG, "邀请被取消: " + inviteID + " " + inviter + " " + data);
        }

        @Override
        public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
            super.onInvitationTimeout(inviteID, inviteeList);

            Log.d(TAG, "邀请超时: " + inviteID + " " + inviteeList.get(0));
        }
    };

    /**
     * 拨号的回调
     */
    private final TRTCAVCallListener mTRTCAVCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            Log.d(TAG, "onError: ");
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
            Log.d(TAG, "onInvited: ");
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {
            Log.d(TAG, "onGroupCallInviteeListUpdate: ");
        }

        @Override
        public void onUserEnter(String userId) {
            Log.d(TAG, "onUserEnter: ");
        }

        @Override
        public void onUserLeave(String userId) {
            Log.d(TAG, "onUserLeave: ");
        }

        @Override
        public void onReject(String userId) {
            Log.d(TAG, "onReject: ");
        }

        @Override
        public void onNoResp(String userId) {
            Log.d(TAG, "onNoResp: ");
        }

        @Override
        public void onLineBusy(String userId) {
            Log.d(TAG, "onLineBusy: ");
        }

        @Override
        public void onCallingCancel() {
            Log.d(TAG, " 作为被邀请方会收到，收到该回调说明本次通话被取消了: ");
        }

        @Override
        public void onCallingTimeout() {
            Log.d(TAG, "onCallingTimeout: ");
        }

        @Override
        public void onCallEnd() {
            Log.d(TAG, "onCallEnd: ");
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, "onUserVideoAvailable: ");
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, "onUserAudioAvailable: ");
        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            Log.d(TAG, "onUserVoiceVolume: ");
        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {

        }

        @Override
        public void longrangecall(String json) {

        }
    };

    /**
     * 邀请某个人
     */
    private void invite(String userid) {
        V2TIMOfflinePushInfo offlinePushInfo = new V2TIMOfflinePushInfo();
        offlinePushInfo.setDesc("好友邀请你聊天");
        offlinePushInfo.setTitle("好友邀请聊天");
        offlinePushInfo.setExt("好友邀请聊天".getBytes());
        offlinePushInfo.disablePush(false);
        offlinePushInfo.disablePush(false);
        //邀请对方进入房间 call_type 1语音 2 视频 3其他
        signalingManager.invite(userid, "{\"room_id\":163088722,\"businessID\":\"av_call\",\"version\":4,\"call_type\":3}", false, offlinePushInfo, 30, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "邀请发送失败: ");
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "邀请发送成功");
                VideoChatViewActivity();
            }
        });
    }

    /**
     * 接收方接收邀请
     *
     * @return
     */
    private void accept(String inviteID, String data) {
        signalingManager.accept(inviteID, data, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "同意邀请失败: ");
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "我同意邀请: ");
                VideoChatViewActivity();
            }
        });
    }

    /**
     * 进入直播放间
     */
    private void VideoChatViewActivity() {
        startActivity(new Intent(activity_Nornal.this, VideoChatViewActivity.class));
    }
}