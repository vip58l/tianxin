package com.tianxin.IMtencent.contact;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.IMtencent.menu.AddMoreActivity;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tencent.imsdk.v2.V2TIMConversation;

import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;

/**
 * 群组列表
 */
public class GroupListActivity extends BaseActivity {

    private static final String TAG = GroupListActivity.class.getSimpleName();

    private TitleBarLayout mTitleBar;
    private ContactListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataSource();
    }

    private void init() {
        mTitleBar = findViewById(R.id.group_list_titlebar);
        mTitleBar.setTitle(getResources().getString(R.string.group), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.setTitle(getResources().getString(R.string.add_group), TitleBarLayout.POSITION.RIGHT);
        mTitleBar.getRightIcon().setVisibility(View.GONE);
        mTitleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DemoApplication.instance(), AddMoreActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("isGroup", true);
                startActivity(intent);
            }
        });

        mListView = findViewById(R.id.group_list);
        mListView.setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(V2TIMConversation.V2TIM_GROUP);
                String chatName = contact.getId();
                if (!TextUtils.isEmpty(contact.getRemark())) {
                    chatName = contact.getRemark();
                } else if (!TextUtils.isEmpty(contact.getNickname())) {
                    chatName = contact.getNickname();
                }
                chatInfo.setChatName(chatName);
                chatInfo.setId(contact.getId());
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void loadDataSource() {
        mListView.loadDataSource(ContactListView.DataSource.GROUP_LIST);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
