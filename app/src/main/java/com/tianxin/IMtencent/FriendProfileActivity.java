/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/21 0021
 */


package com.tianxin.IMtencent;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * 点击聊天窗口头像
 */
public class FriendProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_friend_profile_activity);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);
        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
        layout.setOnButtonClickListener(new FriendProfileLayout.OnButtonClickListener() {
            @Override
            public void onStartConversationClick(ContactItemBean info) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_C2C);
                chatInfo.setId(info.getId());
                String chatName = info.getId();
                if (!TextUtils.isEmpty(info.getRemark())) {
                    chatName = info.getRemark();
                } else if (!TextUtils.isEmpty(info.getNickname())) {
                    chatName = info.getNickname();
                }
                chatInfo.setChatName(chatName);
                Intent intent = new Intent(DemoApplication.instance(), ChatActivity.class);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                DemoApplication.instance().startActivity(intent);
            }

            @Override
            public void onDeleteFriendClick(String id) {

            }
        });
    }

}
