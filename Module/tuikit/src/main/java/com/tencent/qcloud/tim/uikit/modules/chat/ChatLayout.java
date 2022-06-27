package com.tencent.qcloud.tim.uikit.modules.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.opensource.listener.Callback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.AbsChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupApplyInfo;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupApplyManagerActivity;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoActivity;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoPresenter;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupUpdate;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;

import java.util.Arrays;
import java.util.List;

/**
 * 聊天面板
 */
public class ChatLayout extends AbsChatLayout implements GroupChatManagerKit.GroupNotifyHandler {
    private static final String TAG = "ChatLayout";
    private GroupInfo mGroupInfo;
    private GroupChatManagerKit mGroupChatManager;
    private C2CChatManagerKit mC2CChatManager;
    private boolean isGroup = false;

    public ChatLayout(Context context) {
        super(context);
    }

    public ChatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setChatInfo(ChatInfo chatInfo) {
        super.setChatInfo(chatInfo);
        if (chatInfo == null) {
            return;
        }
        isGroup = chatInfo.getType() != V2TIMConversation.V2TIM_C2C;
        if (isGroup) {
            //群聊
            tomGroupChatManager(chatInfo);
        } else {
            //c2c单聊
            tomC2CChatManager(chatInfo);
        }
    }

    @Override
    public ChatManagerKit getChatManager() {
        if (isGroup) {
            return mGroupChatManager;
        } else {
            return mC2CChatManager;
        }
    }

    /**
     * 加载消息
     */
    private void loadApplyList() {
        mGroupChatManager.getProvider().loadGroupApplies(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                List<GroupApplyInfo> applies = (List<GroupApplyInfo>) data;
                if (applies != null && applies.size() > 0) {
                    //%1$d 条入群申请
                    mGroupApplyLayout.getContent().setText(getContext().getString(R.string.group_apply_tips, applies.size()));
                    mGroupApplyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("loadApplyList onError: " + errMsg);
            }
        });
    }

    @Override
    public void onGroupForceExit() {
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).finish();
        }
    }

    @Override
    public void onGroupNameChanged(String newName) {
        getTitleBar().setTitle(newName, TitleBarLayout.POSITION.MIDDLE);
    }

    @Override
    public void onApplied(int size) {
        if (size == 0) {
            mGroupApplyLayout.setVisibility(View.GONE);
        } else {
            //%1$d 条入群申请
            mGroupApplyLayout.getContent().setText(getContext().getString(R.string.group_apply_tips, size));
            mGroupApplyLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 普通单聊
     *
     * @param chatInfo
     */
    private void tomC2CChatManager(ChatInfo chatInfo) {
        getTitleBar().getRightIcon().setImageResource(R.drawable.chat_c2c);
        mC2CChatManager = C2CChatManagerKit.getInstance();
        mC2CChatManager.setCurrentChatInfo(chatInfo);
        loadChatMessages(null);
    }

    /**
     * 群组聊聊天
     *
     * @param chatInfo
     */
    private void tomGroupChatManager(ChatInfo chatInfo) {
        mGroupChatManager = GroupChatManagerKit.getInstance();
        mGroupChatManager.setGroupHandler(this);
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(chatInfo.getId());
        groupInfo.setChatName(chatInfo.getChatName());
        mGroupChatManager.setCurrentChatInfo(groupInfo);
        mGroupInfo = groupInfo;
        loadChatMessages(null);
        loadApplyList();
        getTitleBar().getRightIcon().setImageResource(R.drawable.chat_group);
        getTitleBar().setOnRightClickListener(v -> {
            if (mGroupInfo != null) {
                Intent intent = new Intent(getContext(), GroupInfoActivity.class);
                intent.putExtra(TUIKitConstants.Group.GROUP_ID, mGroupInfo.getId());
                getContext().startActivity(intent);
            } else {
                /**
                 * 请稍后再试试
                 */
                ToastUtil.toastLongMessage(TUIKit.getAppContext().getString(R.string.wait_tip));
            }
        });
        //组应用管理器活动
        mGroupApplyLayout.setOnNoticeClickListener(v -> {
            Intent intent = new Intent(getContext(), GroupApplyManagerActivity.class);
            intent.putExtra(TUIKitConstants.Group.GROUP_INFO, mGroupInfo);
            getContext().startActivity(intent);
        });
        //拉取群资料
        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(mGroupInfo.getId()), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                V2TIMGroupInfo groupInfo = v2TIMGroupInfoResults.get(0).getGroupInfo();
                if (groupInfo.isAllMuted()){
                    checkisOwner(chatInfo);
                }

            }
        });



    }

    /**
     * 全员禁言中
     *
     * @param chatInfo
     */
    public void checkisOwner(ChatInfo chatInfo) {
        GroupInfoPresenter mPresenter = new GroupInfoPresenter(null);
        mPresenter.loadGroupInfo(chatInfo.getId(), new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                GroupInfo info = (GroupInfo) data;
                if (!info.isOwner()) {
                    getInputLayout().Inputenable(false); //全中员禁言中
                }


            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(" " + module + errCode + errMsg);

            }
        });
    }
}
