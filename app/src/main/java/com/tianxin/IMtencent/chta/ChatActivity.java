package com.tianxin.IMtencent.chta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tianxin.R;
import com.tianxin.activity.WelcomeActivity;
import com.tianxin.Util.Constants;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import static com.tencent.imsdk.v2.V2TIMManager.V2TIM_STATUS_LOGINED;

/**
 * 聊天ACTIVITY 界面
 */
public class ChatActivity extends AppCompatActivity {
    private String TAG = ChatActivity.class.getName();
    private ChatFragment chatFragment;

    public static void setAction(Context context, member member) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(String.valueOf(member.getId()));
        chatInfo.setChatName(member.getTruename());
        chatInfo.setKefu(member.getKefu() == 1 ? true : false);
        chatInfo.setIconUrlList(TextUtils.isEmpty(member.getPicture()) ? "" : member.getPicture());
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void setAction(Context context, String kefu, String name) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(kefu);
        chatInfo.setChatName(name);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chat(getIntent());
    }

    private void chat(Intent intent) {

        //外部传入的数据
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            startSplashActivity(null);
            return;
        }

        //接解析接收的数据对方的数据
        ChatInfo mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        if (mChatInfo == null) {
            startSplashActivity(null);
            return;
        }

        //IM通信在线状态##已登录
        if (V2TIMManager.getInstance().getLoginStatus() == V2TIM_STATUS_LOGINED) {
            chatFragment = new ChatFragment();
            chatFragment.setArguments(bundle);

            //载入聊天界面
            getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, chatFragment).commitAllowingStateLoss();
        } else {
            startSplashActivity(bundle);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        chat(intent);
    }

    /**
     * 重新回到启动页登录
     *
     * @param bundle
     */
    private void startSplashActivity(Bundle bundle) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

}