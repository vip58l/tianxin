package com.tianxin.IMtencent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;

import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.activity.Login.UserLoginActivity;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tianxin.utils.ClickUtils;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

/**
 * 登录状态的Activity都要集成该类，来完成被踢下线等监听处理。
 */
public class BaseActivity extends AppCompatActivity {
    private static String TAG = BaseActivity.class.getSimpleName();
    public Activity activity;
    public Context context;
    public UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.navigation_bar_color));
            int vis = getWindow().getDecorView().getSystemUiVisibility();
            vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            getWindow().getDecorView().setSystemUiVisibility(vis);
        }

        //设置TUIKit的IM消息的全局监听
        TUIKit.addIMEventListener(mIMEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //用户未登录
        UserLOGOUT();

        setOfflinePushConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ClickUtils.clear();
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public static void logout(Context context) {
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setToken("");
        userInfo.setRefresh("");
        userInfo.setPhone("");
        userInfo.setName("");
        userInfo.setGivenname("");
        userInfo.setUserSig("");
        userInfo.setUserId("");
        userInfo.setAvatar("");
        userInfo.setLive(0);
        userInfo.setRemarks1("");
        userInfo.setAutoLogin(false);
        TUIKitLive.logout();
        Intent intent = new Intent(context, UserLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.LOGOUT, true);
        DemoApplication.instance().startActivity(intent);
    }

    /**
     * 监听消息通知提示
     */
    public static IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            super.onForceOffline();
            ToastUtil.toastLongMessage(DemoApplication.instance().getString(R.string.tv_msg193));
            logout(DemoApplication.instance());
        }

        @Override
        public void onUserSigExpired() {
            super.onUserSigExpired();
            ToastUtil.toastLongMessage(DemoApplication.instance().getString(R.string.tv_msg1933));
            logout(DemoApplication.instance());
        }

        @Override
        public void onNewMessage(V2TIMMessage v2TIMMessage) {
            super.onNewMessage(v2TIMMessage);
            //MessageNotification notification = MessageNotification.getInstance();
            //notification.notify(v2TIMMessage);
        }

    };

    /**
     * 打开聊天界面
     *
     * @param bean
     */
    public static void startChatChatInfo(OfflineMessageBean bean) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(bean.chatType);
        chatInfo.setId(bean.sender);
        chatInfo.setChatName(bean.nickname);
        Intent intent = new Intent(DemoApplication.instance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);
    }

    /**
     * 登录判断
     */
    public static void UserLOGOUT() {
        //        V2TIM_STATUS_LOGINED 已登录
        //        V2TIM_STATUS_LOGINING 登录中
        //        V2TIM_STATUS_LOGOUT 无登录
        if (V2TIMManager.V2TIM_STATUS_LOGOUT == V2TIMManager.getInstance().getLoginStatus() || !UserInfo.getInstance().isAutoLogin()) {
            //直接退出系统聊天
            BaseActivity.logout(DemoApplication.instance());
        }
    }

    public void setOfflinePushConfig() {
        //设置离线推送配置信息 note 将证书 ID 和 Push Token 上报到即时通信 IM 服务端
        V2TIMOfflinePushConfig v2TIMOfflinePushConfig = new V2TIMOfflinePushConfig(0, "");
        V2TIMManager.getOfflinePushManager().setOfflinePushConfig(v2TIMOfflinePushConfig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });

        //如果配置了离线推送，会收到厂商的离线推送通道下发的通知栏消息
        V2TIMManager.getOfflinePushManager().doBackground(0, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });

        //从5.0.1版本开始，对应 doBackground，会停止厂商的离线推送。但如果应用被 kill，仍然可以正常接收离线推送
        V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });


    }

}
