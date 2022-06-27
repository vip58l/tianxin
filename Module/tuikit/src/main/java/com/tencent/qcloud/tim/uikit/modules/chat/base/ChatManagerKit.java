package com.tencent.qcloud.tim.uikit.modules.chat.base;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.modules.message.MessageRevokedManager;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 消息撤回处理 复制 删除 撤消
 */
public abstract class ChatManagerKit extends V2TIMAdvancedMsgListener implements MessageRevokedManager.MessageRevokeHandler {

    protected static final int MSG_PAGE_COUNT = 20;
    private static final String TAG = ChatManagerKit.class.getSimpleName();
    protected ChatProvider mCurrentProvider;

    protected boolean mIsMore;
    private boolean mIsLoading;

    private MessageInfo mLastMessageInfo;

    protected void init() {
        destroyChat();
        V2TIMManager.getMessageManager().addAdvancedMsgListener(this);
        MessageRevokedManager.getInstance().addHandler(this);
    }

    public void destroyChat() {
        mCurrentProvider = null;
    }

    public abstract ChatInfo getCurrentChatInfo();

    public void setCurrentChatInfo(ChatInfo info) {
        if (info == null) {
            return;
        }
        mCurrentProvider = new ChatProvider();
        mIsMore = true;
        mIsLoading = false;
    }

    public void onReadReport(List<V2TIMMessageReceipt> receiptList) {
        TUIKitLog.i(TAG, "onReadReport:" + receiptList.size());
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "onReadReport unSafetyCall");
            return;
        }
        if (receiptList.size() == 0) {
            return;
        }
        V2TIMMessageReceipt max = receiptList.get(0);
        for (V2TIMMessageReceipt msg : receiptList) {
            if (!TextUtils.equals(msg.getUserID(), getCurrentChatInfo().getId())) {
                continue;
            }
            if (max.getTimestamp() < msg.getTimestamp()) {
                max = msg;
            }
        }
        mCurrentProvider.updateReadMessage(max);
    }

    @Override
    public void onRecvNewMessage(V2TIMMessage msg) {
        TUIKitLog.i(TAG, "onRecvNewMessage msgID:" + msg.getMsgID());
        int elemType = msg.getElemType();
        if (elemType == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
            if (MessageInfoUtil.isTyping(msg.getCustomElem().getData())) {
                notifyTyping();
                return;
            } else if (MessageInfoUtil.isOnlineIgnoredDialing(msg.getCustomElem().getData())) {
                // 这类消息都是音视频通话邀请的在线消息，忽略
                TUIKitLog.i(TAG, "ignore online invitee message");
                return;
            }
        }

        onReceiveMessage(msg);
    }

    private void notifyTyping() {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "notifyTyping unSafetyCall");
            return;
        }
        mCurrentProvider.notifyTyping();
    }

    /**
     * 通知新朋友 成为好友
     *
     * @param timFriendInfoList
     */
    public void notifyNewFriend(List<V2TIMFriendInfo> timFriendInfoList) {
        if (timFriendInfoList == null || timFriendInfoList.size() == 0) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TUIKit.getAppContext().getString(R.string.and_and));
        for (V2TIMFriendInfo v2TIMFriendInfo : timFriendInfoList) {
            V2TIMUserFullInfo userProfile = v2TIMFriendInfo.getUserProfile();
            stringBuilder.append(TextUtils.isEmpty(userProfile.getNickName()) ? v2TIMFriendInfo.getUserID() : userProfile.getNickName()).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(TUIKit.getAppContext().getString(R.string.be_friend));
        ToastUtil.toastLongMessage(stringBuilder.toString());
    }

    /**
     * 接收消息
     *
     * @param msg
     */
    protected void onReceiveMessage(final V2TIMMessage msg) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "onReceiveMessage unSafetyCall");
            return;
        }
        addMessage(msg);
    }

    protected abstract boolean isGroup();

    /**
     * 添加消息
     *
     * @param msg
     */
    protected void addMessage(V2TIMMessage msg) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "addMessage unSafetyCall");
            return;
        }
        final List<MessageInfo> list = MessageInfoUtil.TIMMessage2MessageInfo(msg);
        if (list != null && list.size() != 0) {
            ChatInfo chatInfo = getCurrentChatInfo();
            boolean isGroupMessage = false;
            String groupID = null;
            String userID = null;
            if (!TextUtils.isEmpty(msg.getGroupID())) {
                // 群组消息
                if (chatInfo.getType() == V2TIMConversation.V2TIM_C2C || !chatInfo.getId().equals(msg.getGroupID())) {
                    return;
                }
                isGroupMessage = true;
                groupID = msg.getGroupID();
            } else if (!TextUtils.isEmpty(msg.getUserID())) {
                // C2C 消息
                if (chatInfo.getType() == V2TIMConversation.V2TIM_GROUP || !chatInfo.getId().equals(msg.getUserID())) {
                    return;
                }
                userID = msg.getUserID();
            } else {
                return;
            }
            mCurrentProvider.addMessageInfoList(list);

            for (MessageInfo msgInfo : list) {
                msgInfo.setRead(true);
                addGroupMessage(msgInfo);
            }
            if (isGroupMessage) {
                //设置群组消息已读
                V2TIMManager.getMessageManager().markGroupMessageAsRead(groupID, null);
            } else {
                //设置单聊消息已读
                V2TIMManager.getMessageManager().markC2CMessageAsRead(userID, null);
            }
        }
    }

    /**
     * 添加组消息
     *
     * @param msgInfo
     */
    protected void addGroupMessage(MessageInfo msgInfo) {
        // GroupChatManagerKit会重写该方法
    }

    /**
     * 删除消息
     *
     * @param position
     * @param messageInfo
     */
    public void deleteMessage(final int position, MessageInfo messageInfo) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "deleteMessage unSafetyCall");
            return;
        }
        List<V2TIMMessage> msgs = new ArrayList<>();
        msgs.add(mCurrentProvider.getDataSource().get(position).getTimMessage());
        V2TIMManager.getMessageManager().deleteMessages(msgs, new V2TIMCallback() {

            @Override
            public void onError(int code, String desc) {
                TUIKitLog.w(TAG, "deleteMessages code:" + code + "|desc:" + desc);
            }

            @Override
            public void onSuccess() {
                TUIKitLog.i(TAG, "deleteMessages success");
                mCurrentProvider.remove(position);
                ConversationManagerKit.getInstance().loadConversation(null);
            }
        });
    }

    /**
     * 撤销消息
     *
     * @param position
     * @param messageInfo
     */
    public void revokeMessage(final int position, final MessageInfo messageInfo) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "revokeMessage unSafetyCall");
            return;
        }
        V2TIMManager.getMessageManager().revokeMessage(messageInfo.getTimMessage(), new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                if (code > 10056) {
                    eorr2(code, desc);
                } else {
                    eorr1(code, desc);
                }

            }

            @Override
            public void onSuccess() {
                if (!safetyCall()) {
                    TUIKitLog.w(TAG, "revokeMessage unSafetyCall");
                    return;
                }
                mCurrentProvider.updateMessageRevoked(messageInfo.getId());
                ConversationManagerKit.getInstance().loadConversation(null);
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message
     * @param retry
     * @param callBack
     */
    public void sendMessage(final MessageInfo message, boolean retry, final IUIKitCallBack callBack) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "sendMessage unSafetyCall");
            return;
        }

        if (message == null || message.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
            return;
        }
        message.setSelf(true);
        message.setRead(true);
        assembleGroupMessage(message);

        OfflineMessageContainerBean containerBean = new OfflineMessageContainerBean();
        OfflineMessageBean entity = new OfflineMessageBean();
        entity.content = message.getExtra().toString();
        entity.sender = message.getFromUser();
        entity.nickname = TUIKitConfigs.getConfigs().getGeneralConfig().getUserNickname();
        entity.faceUrl = TUIKitConfigs.getConfigs().getGeneralConfig().getUserFaceUrl();
        containerBean.entity = entity;

        String userID = "";
        String groupID = "";
        boolean isGroup = false;
        if (getCurrentChatInfo().getType() == V2TIMConversation.V2TIM_GROUP) {
            groupID = getCurrentChatInfo().getId();
            isGroup = true;
            entity.chatType = V2TIMConversation.V2TIM_GROUP;
            entity.sender = groupID;
        } else {
            userID = getCurrentChatInfo().getId();
        }
        V2TIMOfflinePushInfo v2TIMOfflinePushInfo = new V2TIMOfflinePushInfo();
        v2TIMOfflinePushInfo.setExt(new Gson().toJson(containerBean).getBytes());
        // OPPO必须设置ChannelID才可以收到推送消息，这个channelID需要和控制台一致
        v2TIMOfflinePushInfo.setAndroidOPPOChannelID("tuikit");

        V2TIMMessage v2TIMMessage = message.getTimMessage();
        String msgID = V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, isGroup ? null : userID, isGroup ? groupID : null, V2TIMMessage.V2TIM_PRIORITY_DEFAULT, false, v2TIMOfflinePushInfo, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onError(int code, String desc) {
                TUIKitLog.v(TAG, "sendMessage fail:" + code + "=" + desc);
                if (!safetyCall()) {
                    TUIKitLog.w(TAG, "sendMessage unSafetyCall");
                    return;
                }
                if (callBack != null) {
                    callBack.onError(TAG, code, desc);
                }
                message.setStatus(MessageInfo.MSG_STATUS_SEND_FAIL);
                mCurrentProvider.updateMessageInfo(message);
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                TUIKitLog.v(TAG, "sendMessage onSuccess:" + v2TIMMessage.getMsgID());
                if (!safetyCall()) {
                    TUIKitLog.w(TAG, "sendMessage unSafetyCall");
                    return;
                }
                if (callBack != null) {
                    callBack.onSuccess(mCurrentProvider);
                }
                message.setStatus(MessageInfo.MSG_STATUS_SEND_SUCCESS);
                message.setMsgTime(v2TIMMessage.getTimestamp());
                mCurrentProvider.updateMessageInfo(message);
            }
        });

        //消息先展示，通过状态来确认发送是否成功
        TUIKitLog.i(TAG, "sendMessage msgID:" + msgID);
        message.setId(msgID);
        if (message.getMsgType() < MessageInfo.MSG_TYPE_TIPS) {
            message.setStatus(MessageInfo.MSG_STATUS_SENDING);
            if (retry) {
                mCurrentProvider.resendMessageInfo(message);
            } else {
                mCurrentProvider.addMessageInfo(message);
            }
        }
    }

    /**
     * 群集合消息
     */
    protected void assembleGroupMessage(MessageInfo message) {
        // GroupChatManager会重写该方法
    }

    /**
     * 获取信息获取群消息
     *
     * @param atInfoMsgSeq
     * @param lastMessage
     * @param callBack
     */
    public void getAtInfoChatMessages(long atInfoMsgSeq, V2TIMMessage lastMessage, final IUIKitCallBack callBack) {
        final ChatInfo chatInfo = getCurrentChatInfo();
        if (atInfoMsgSeq == -1 || lastMessage == null || lastMessage.getSeq() <= atInfoMsgSeq) {
            return;
        }
        if (chatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            V2TIMManager.getMessageManager().getGroupHistoryMessageList(chatInfo.getId(), (int) (lastMessage.getSeq() - atInfoMsgSeq), lastMessage, new V2TIMValueCallback<List<V2TIMMessage>>() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(TAG, "loadChatMessages getGroupHistoryMessageList failed, code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                    processHistoryMsgs(v2TIMMessages, chatInfo, callBack);
                }
            });
        }
    }

    /**
     * 加载聊天信息
     *
     * @param lastMessage
     * @param callBack
     */
    public void loadChatMessages(MessageInfo lastMessage, final IUIKitCallBack callBack) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "loadLocalChatMessages unSafetyCall");
            return;
        }
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (!mIsMore) {
            mCurrentProvider.addMessageInfo(null);
            callBack.onSuccess(null);
            mIsLoading = false;
            return;
        }

        V2TIMMessage lastTIMMsg = null;
        if (lastMessage == null) {
            mCurrentProvider.clear();
        } else {
            lastTIMMsg = lastMessage.getTimMessage();
        }
//        final int unread = (int) mCurrentConversation.getUnreadMessageNum();
        final ChatInfo chatInfo = getCurrentChatInfo();
        if (chatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            V2TIMManager.getMessageManager().getC2CHistoryMessageList(chatInfo.getId(), MSG_PAGE_COUNT, lastTIMMsg, new V2TIMValueCallback<List<V2TIMMessage>>() {
                @Override
                public void onError(int code, String desc) {
                    mIsLoading = false;
                    callBack.onError(TAG, code, desc);
                    TUIKitLog.e(TAG, "loadChatMessages getC2CHistoryMessageList failed, code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                    processHistoryMsgs(v2TIMMessages, chatInfo, callBack);
                }
            });
        } else {
            V2TIMManager.getMessageManager().getGroupHistoryMessageList(chatInfo.getId(), MSG_PAGE_COUNT, lastTIMMsg, new V2TIMValueCallback<List<V2TIMMessage>>() {
                @Override
                public void onError(int code, String desc) {
                    mIsLoading = false;
                    callBack.onError(TAG, code, desc);
                    TUIKitLog.e(TAG, "loadChatMessages getGroupHistoryMessageList failed, code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                    processHistoryMsgs(v2TIMMessages, chatInfo, callBack);
                }
            });
        }
    }

    /**
     * 设置消息已读
     *
     * @param v2TIMMessages
     * @param chatInfo
     * @param callBack
     */
    private void processHistoryMsgs(List<V2TIMMessage> v2TIMMessages, ChatInfo chatInfo, IUIKitCallBack callBack) {
        mIsLoading = false;
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "getLocalMessage unSafetyCall");
            return;
        }
        if (chatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            //设置群组消息已读
            V2TIMManager.getMessageManager().markC2CMessageAsRead(chatInfo.getId(), new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(TAG, "processHistoryMsgs setReadMessage failed, code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess() {
                    TUIKitLog.d(TAG, "processHistoryMsgs setReadMessage success");
                }
            });
        } else {
            //设置群组消息已读
            V2TIMManager.getMessageManager().markGroupMessageAsRead(chatInfo.getId(), new V2TIMCallback() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.e(TAG, "processHistoryMsgs markC2CMessageAsRead failed, code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess() {
                    TUIKitLog.d(TAG, "processHistoryMsgs markC2CMessageAsRead success");
                }
            });
        }

        if (v2TIMMessages.size() < MSG_PAGE_COUNT) {
            mIsMore = false;
        }
        ArrayList<V2TIMMessage> messages = new ArrayList<>(v2TIMMessages);
        Collections.reverse(messages);

        List<MessageInfo> msgInfos = MessageInfoUtil.TIMMessages2MessageInfos(messages, isGroup());
        mCurrentProvider.addMessageList(msgInfos, true);
        for (int i = 0; i < msgInfos.size(); i++) {
            MessageInfo info = msgInfos.get(i);
            if (info.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
                sendMessage(info, true, null);
            }
        }
        callBack.onSuccess(mCurrentProvider);
    }

    @Override
    public void handleInvoke(String msgID) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "handleInvoke unSafetyCall");
            return;
        }
        TUIKitLog.i(TAG, "handleInvoke msgID = " + msgID);
        mCurrentProvider.updateMessageRevoked(msgID);
    }

    protected boolean safetyCall() {
        return mCurrentProvider != null
                && getCurrentChatInfo() != null;
    }

    public void setLastMessageInfo(MessageInfo mLastMessageInfo) {
        this.mLastMessageInfo = mLastMessageInfo;
    }

    public MessageInfo getLastMessageInfo() {
        return mLastMessageInfo;
    }

    /**
     * 消息错误码
     *
     * @param code
     * @param desc
     */
    private void eorr2(int code, String desc) {
        switch (code) {
            case 20001:
                ToastUtil.toastLongMessage("请求包非法");
                break;
            case 20002:
                ToastUtil.toastLongMessage("UserSig 或 A2 失效");
                break;
            case 20003:
                ToastUtil.toastLongMessage("消息发送方或接收方 UserID 无效或不存在，请检查 UserID 是否已导入即时通信 IM");
                break;
            case 20004:
                ToastUtil.toastLongMessage("网络异常，请重试");
                break;
            case 20005:
                ToastUtil.toastLongMessage("服务端内部错误，请重试");
                break;
            case 20006:
                ToastUtil.toastLongMessage("触发发送单聊消息之前回调，App 后台返回禁止下发该消息");
                break;
            case 20007:
                ToastUtil.toastLongMessage("发送单聊消息，被对方拉黑，禁止发送消息发送状态默认展示为失败，您可以登录控制台修改该场景下的消息发送状态展示结果，具体操作请参见 黑名单检查");
                break;
            case 20008:
                ToastUtil.toastLongMessage("消息发送方和接收方属于不同的 SDKAppID 原因是客户端切换了 SDKAppID，但数据库未进行清理。解决办法是在切换 SDKAppID 时删除原来的数据库");
                break;
            case 20009:
                ToastUtil.toastLongMessage("消息发送双方互相不是好友，禁止发送（配置单聊消息校验好友关系才会出现）");
                break;
            case 20010:
                ToastUtil.toastLongMessage("发送单聊消息，自己不是对方的好友（单向关系），禁止发送");
                break;
            case 20011:
                ToastUtil.toastLongMessage("发送单聊消息，对方不是自己的好友（单向关系），禁止发送");
                break;
            case 20012:
                ToastUtil.toastLongMessage("发送方被禁言，该条消息被禁止发送");
                break;
            case 20016:
                ToastUtil.toastLongMessage("消息撤回超过了时间限制（默认2分钟）");
                break;
            case 20018:
                ToastUtil.toastLongMessage("删除漫游内部错误");
                break;
            case 20022:
                ToastUtil.toastLongMessage("该待撤回的消息不存在，请检查");
                break;
            case 20023:
                ToastUtil.toastLongMessage("该消息已被撤回");
                break;
            case 21005:
                ToastUtil.toastLongMessage("设置 token 请求比登录请求先到后台，请确保先登录，后设置 token");
                break;
            case 22001:
                ToastUtil.toastLongMessage("没有上传过任何离线推送证书");
                break;
            case 22002:
                ToastUtil.toastLongMessage("网络异常，请重试");
                break;
            case 22003:
                ToastUtil.toastLongMessage("上传的 token 为空");
                break;
            case 22004:
                ToastUtil.toastLongMessage("上传的 token 长度超过256字节");
                break;
            case 22005:
                ToastUtil.toastLongMessage("登录请求数据超过1024字节");
                break;
            case 22006:
                ToastUtil.toastLongMessage("请求超频");
                break;
            case 90001:
                ToastUtil.toastLongMessage("JSON 格式解析失败，请检查请求包是否符合 JSON 规范");
                break;
            case 90002:
                ToastUtil.toastLongMessage("JSON 格式请求包中 MsgBody 不符合消息格式描述，或者 MsgBody 不是 Array 类型，请参考 TIMMsgElement 对象 的定义");
                break;
            case 90003:
                ToastUtil.toastLongMessage("JSON 格式请求包体中缺少 To_Account 字段或者 To_Account 帐号不存在");
                break;
            case 90005:
                ToastUtil.toastLongMessage("JSON 格式请求包体中缺少 MsgRandom 字段或者 MsgRandom 字段不是 Integer 类型");
                break;
            case 90006:
                ToastUtil.toastLongMessage("JSON 格式请求包体中缺少 MsgTimeStamp 字段或者 MsgTimeStamp 字段不是 Integer 类型");
                break;
            case 90007:
                ToastUtil.toastLongMessage("JSON 格式请求包体中 MsgBody 类型不是 Array 类型，请将其修改为 Array 类型");
                break;
            case 90008:
                ToastUtil.toastLongMessage("JSON 格式请求包体中缺少 From_Account 字段或者 From_Account 帐号不存在");
                break;
            case 90009:
                ToastUtil.toastLongMessage("请求需要 App 管理员权限");
                break;
            case 90010:
                ToastUtil.toastLongMessage("JSON 格式请求包不符合消息格式描述，请参考 TIMMsgElement 对象 的定义");
                break;
            case 90011:
                ToastUtil.toastLongMessage("批量发消息目标帐号超过500，请减少 To_Account 中目标帐号数量");
                break;
            case 90012:
                ToastUtil.toastLongMessage("To_Account 没有注册或不存在，请确认 To_Account 是否导入即时通信 IM 或者是否拼写错误");
                break;
            case 90018:
                ToastUtil.toastLongMessage("请求的帐号数量超过限制");
                break;
            case 90022:
                ToastUtil.toastLongMessage("推送条件中的 TagsOr 和 TagsAnd 有重复标签");
                break;
            case 90024:
                ToastUtil.toastLongMessage("推送过于频繁，每两次推送间隔必须大于1秒");
                break;
            case 90026:
                ToastUtil.toastLongMessage("消息离线存储时间错误（最多不能超过7天）");
                break;
            case 90030:
                ToastUtil.toastLongMessage("属性长度为0或大于50");
                break;
            case 90031:
                ToastUtil.toastLongMessage("JSON 格式请求包体中 SyncOtherMachine 字段不是 Integer 类型");
                break;
            case 90032:
                ToastUtil.toastLongMessage("推送条件中的 tag 数量大于10，或添加标签请求中的标签数量大于10");
                break;
            case 90033:
                ToastUtil.toastLongMessage("属性无效");
                break;
            case 90034:
                ToastUtil.toastLongMessage("标签长度大于50");
                break;
            case 90040:
                ToastUtil.toastLongMessage("推送条件中其中1个 tag 为空");
                break;
            case 90043:
                ToastUtil.toastLongMessage("JSON 格式请求包中 OfflinePushInfo 不符合消息格式描述，请参考 OfflinePushInfo 对象 的定义");
                break;
            case 90044:
                ToastUtil.toastLongMessage("JSON 格式请求包体中 MsgLifeTime 字段不是 Integer 类型");
                break;
            case 90045:
                ToastUtil.toastLongMessage("未开通全员推送功能");
                break;
            case 90047:
                ToastUtil.toastLongMessage("推送次数超过当天限额（默认为100次）");
                break;
            case 90048:
                ToastUtil.toastLongMessage("请求的用户帐号不存在");
                break;
            case 90054:
                ToastUtil.toastLongMessage("撤回请求中的 MsgKey 不合法");
                break;
            case 90055:
                ToastUtil.toastLongMessage("批量发消息的包体过长，目前支持最大8k消息包体长度");
                break;
            case 90994:
                ToastUtil.toastLongMessage("服务内部错误，请重试");
                break;
            case 90995:
                ToastUtil.toastLongMessage("服务内部错误，请重试");
                break;
            case 91000:
                ToastUtil.toastLongMessage("服务内部错误，请重试");
                break;
            case 90992:
                ToastUtil.toastLongMessage("服务内部错误，请重试；如果所有请求都返回该错误码，且 App 配置了第三方回调，请检查 App 服务端是否正常向即时通信 IM 后台服务端返回回调结果");
                break;
            case 93000:
                ToastUtil.toastLongMessage("JSON 数据包超长，消息包体请不要超过8k");
                break;
            case 91101:
                ToastUtil.toastLongMessage("Web 端长轮询被踢（Web 端同时在线实例个数超出限制）");
                break;
            default:
                break;
        }
    }

    /**
     * 群组错误码
     *
     * @param code
     * @param desc
     */
    private void eorr1(int code, String desc) {
        switch (code) {
            case 10002:
                ToastUtil.toastLongMessage("服务端内部错误，请重试");
                break;
            case 10003:
                ToastUtil.toastLongMessage("请求中的接口名称错误，请核对接口名称并重试");
                break;
            case 10004:
                ToastUtil.toastLongMessage("参数非法，请根据错误描述检查请求是否正确");
                break;
            case 10005:
                ToastUtil.toastLongMessage("请求包体中携带的帐号数量过多");
                break;
            case 10006:
                ToastUtil.toastLongMessage("操作频率限制，请尝试降低调用的频率");
                break;
            case 10007:
                ToastUtil.toastLongMessage("操作权限不足（例如 Public 群组中普通成员尝试执行踢人操作，但只有 App 管理员才有权限；或者非群成员操作）");
                break;
            case 10009:
                ToastUtil.toastLongMessage("该群不允许群主主动退出");
                break;
            case 10010:
                ToastUtil.toastLongMessage("群组不存在，或者曾经存在过，但是目前已经被解散");
                break;
            case 10011:
                ToastUtil.toastLongMessage("解析 JSON 包体失败，请检查包体的格式是否符合 JSON 格式");
                break;
            case 10012:
                ToastUtil.toastLongMessage("发起操作的 UserID 非法，请检查发起操作的用户 UserID 是否填写正确");
                break;
            case 10013:
                ToastUtil.toastLongMessage("用户已经是群成员");
                break;
            case 10014:
                ToastUtil.toastLongMessage("群已满员，无法将请求中的用户加入群组，如果是批量加人，可以尝试减少加入用户的数量");
                break;
            case 10015:
                ToastUtil.toastLongMessage("群组 ID 非法，请检查群组 ID 是否填写正确");
                break;
            case 10016:
                ToastUtil.toastLongMessage("App 后台通过第三方回调拒绝本次操作");
                break;
            case 10017:
                ToastUtil.toastLongMessage(" 因被禁言而不能发送消息，请检查发送者是否被设置禁言");
                break;
            case 10018:
                ToastUtil.toastLongMessage("应答包长度超过最大包长（1 MB），请求的内容过多，请尝试减少单次请求的数据量");
                break;
            case 10019:
                ToastUtil.toastLongMessage("请求的用户帐号不存在");
                break;
            case 10021:
                ToastUtil.toastLongMessage("群组 ID 已被使用，请选择其他的群组 ID");
                break;
            case 10023:
                ToastUtil.toastLongMessage("发消息的频率超限，请延长两次发消息时间的间隔");
                break;
            case 10024:
                ToastUtil.toastLongMessage("此邀请或者申请请求已经被处理");
                break;
            case 10025:
                ToastUtil.toastLongMessage("群组 ID 已被使用，并且操作者为群主，可以直接使用");
                break;
            case 10026:
                ToastUtil.toastLongMessage("该 SDKAppID 请求的命令字已被禁用");
                break;
            case 10030:
                ToastUtil.toastLongMessage("请求撤回的消息不存在");
                break;
            case 10031:
                ToastUtil.toastLongMessage("消息撤回超过了时间限制（默认2分钟）");
                break;
            case 10032:
                ToastUtil.toastLongMessage("请求撤回的消息不支持撤回操作");
                break;
            case 10033:
                ToastUtil.toastLongMessage("群组类型不支持消息撤回操作");
                break;
            case 10034:
                ToastUtil.toastLongMessage("该消息类型不支持删除操作");
                break;
            case 10035:
                ToastUtil.toastLongMessage("音视频聊天室和在线成员广播大群不支持删除消息");
                break;
            case 10036:
                ToastUtil.toastLongMessage("音视频聊天室创建数量超过了限制，请参考 价格说明 购买预付费套餐“IM音视频聊天室”");
                break;
            case 10037:
                ToastUtil.toastLongMessage("单个用户可创建和加入的群组数量超过了限制，请参考 价格说明 购买或升级预付费套餐“单人可创建与加入群组数”");
                break;
            case 10038:
                ToastUtil.toastLongMessage("群成员数量超过限制，请参考 价格说明 购买或升级预付费套餐“扩展群人数上限（升级后需要调用 修改群资料接口 修改群最大人数）");
                break;
            case 10041:
                ToastUtil.toastLongMessage("该应用（SDKAppID）已配置不支持群消息撤回");
                break;
            case 10045:
                ToastUtil.toastLongMessage("自定义属性 key 超过大小限制32字节");
                break;
            case 10046:
                ToastUtil.toastLongMessage("自定义属性单个 val 超过了大小限制4000字节");
                break;
            case 10047:
                ToastUtil.toastLongMessage("自定义属性 key 数量超过了限制16");
                break;
            case 10048:
                ToastUtil.toastLongMessage("自定义属性所有 key 对应的 val 大小之和超过上限16000字节");
                break;
            case 10049:
                ToastUtil.toastLongMessage("自定义属性写操作触发频控");
                break;
            case 10050:
                ToastUtil.toastLongMessage("删除不存在的自定义属性");
                break;
            case 10051:
                ToastUtil.toastLongMessage("消息删除超过最大范围限制");
                break;
            case 10052:
                ToastUtil.toastLongMessage("消息删除时候群里不存在消息");
                break;
            case 10053:
                ToastUtil.toastLongMessage("群 @数量超过上限30");
                break;
            case 10054:
                ToastUtil.toastLongMessage("群成员过多，请分页拉取");
                break;
            case 10056:
                ToastUtil.toastLongMessage("自定义属性写操作竞争冲突，请获取最新的自定义属性后再进行写操作");
                break;
            default:
                break;
        }
    }
}
