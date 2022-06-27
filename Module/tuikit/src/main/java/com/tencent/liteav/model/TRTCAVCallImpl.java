package com.tencent.liteav.model;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.imsdk.BaseConstants;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.liteav.demo.beauty.utils.BeautyUtils;
import com.tencent.opensource.dialog.AppManager;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageBean;
import com.tencent.qcloud.tim.uikit.modules.chat.base.OfflineMessageContainerBean;
import com.tencent.qcloud.tim.uikit.modules.message.MessageCustom;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 音视频通话的具体实现
 * 本功能使用腾讯云实时音视频 / 腾讯云即时通信IM 组合实现
 * 1. 为了方便您接入，在login中调用了initIM进行IM系统的初始化，如果您的项目中已经使用了IM，可以删除这里的初始化
 */
public class TRTCAVCallImpl implements ITRTCAVCall {
    private static final String TAG = TRTCAVCallImpl.class.getSimpleName();
    /**
     * 超时时间，单位秒
     */
    public static final int TIME_OUT_COUNT = 30;

    /**
     * room id 的取值范围
     */
    private static final int ROOM_ID_MIN = 1;
    private static final int ROOM_ID_MAX = Integer.MAX_VALUE;

    private static ITRTCAVCall sITRTCAVCall;
    private final Context mContext;
    /**
     * 底层SDK调用实例
     */
    private final TRTCCloud mTRTCCloud;
    private final V2TIMManager mTIMManager;

    /**
     * 当前IM登录用户名
     */
    private String mCurUserId = "";
    private int mSdkAppId;
    private String mCurUserSig;
    /**
     * 是否首次邀请
     */
    private boolean isOnCalling = false;
    private String mCurCallID = "";
    private int mCurRoomID = 0;
    /**
     * 当前是否在TRTC房间中
     */
    private boolean mIsInRoom = false;
    private long mEnterRoomTime = 0;
    /**
     * 当前邀请列表
     * C2C通话时会记录自己邀请的用户
     * IM群组通话时会同步群组内邀请的用户
     * 当用户接听、拒绝、忙线、超时会从列表中移除该用户
     */
    private List<String> mCurInvitedList = new ArrayList<>();

    /**
     * 当前语音通话中的远端用户
     */
    private final Set<String> mCurRoomRemoteUserSet = new HashSet<>();

    /**
     * C2C通话的邀请人
     * 例如A邀请B，B存储的mCurSponsorForMe为A
     */
    private String mCurSponsorForMe = "";
    /**
     * C2C通话是否回复过邀请人
     * 例如A邀请B，B回复接受/拒绝/忙线都置为true
     */
    private boolean mIsRespSponsor = false;
    /**
     * 当前通话的类型
     */
    private int mCurCallType = TYPE_UNKNOWN;
    /**
     * 当前群组通话的群组ID
     */
    private String mCurGroupId = "";

    /**
     * 最近使用的通话信令，用于快速处理 初始化对像
     */
    private CallModel mLastCallModel = new CallModel();

    private GiftInfo giftInfo;

    public GiftInfo getGiftInfo() {
        return giftInfo;
    }

    /**
     * 上层传入回调监听
     */
    private final TRTCInteralListenerManager mTRTCInteralListenerManager;

    private boolean mIsUseFrontCamera;

    private boolean mWaitingLastActivityFinished;

    public boolean isWaitingLastActivityFinished() {
        return mWaitingLastActivityFinished;
    }

    public void setWaitingLastActivityFinished(boolean waiting) {
        mWaitingLastActivityFinished = waiting;
    }

    /**
     * 1v1视频 、语音、信令监听
     */
    private final V2TIMSignalingListener mTIMSignallingListener = new V2TIMSignalingListener() {
        @Override
        public void onReceiveNewInvitation(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
            TUIKitLog.i(TAG, "收到邀请" + inviteID + " " + inviter + " " + groupID + " " + inviteeList.toString() + " " + data);

            //会员点击打招呼
            if (!ismemberEmpty(data)) {
                TUIKitLog.d(TAG, "member解析其他数据失败");
                return;
            }

            //礼物获取解析
            if (!isGiftInfo(data)) {
                TUIKitLog.e(TAG, "礼物获取解析");
                return;
            }

            //视频/语音/解析
            if (!isCallingData(data)) {
                TUIKitLog.e(TAG, "解析Json内容错误");
                return;
            }

            //收到邀请
            processInvite(inviteID, inviter, groupID, inviteeList, data);
        }

        @Override
        public void onInviteeAccepted(String inviteID, String invitee, String data) {
            TUIKitLog.d(TAG, "被邀请者接受邀请 inviteID:" + inviteID + ", invitee:" + invitee + "data" + data);
            if (!isCallingData(data)) {
                return;
            }
            mCurInvitedList.remove(invitee);
        }

        @Override
        public void onInviteeRejected(String inviteID, String invitee, String data) {
            Log.d(TAG, "被邀请者拒绝邀请: ");
            if (!isCallingData(data)) {
                return;
            }

            if (mCurCallID.equals(inviteID)) {
                try {
                    Map rejectData = new Gson().fromJson(data, Map.class);
                    mCurInvitedList.remove(invitee);
                    if (rejectData != null && rejectData.containsKey(CallModel.SIGNALING_EXTRA_KEY_LINE_BUSY)) {
                        if (mTRTCInteralListenerManager != null) {
                            mTRTCInteralListenerManager.onLineBusy(invitee);
                        }
                    } else {
                        if (mTRTCInteralListenerManager != null) {
                            mTRTCInteralListenerManager.onReject(invitee);
                        }
                    }
                    preExitRoom(null);
                } catch (JsonSyntaxException e) {
                    TUIKitLog.e(TAG, "被邀请者拒绝邀请:" + e);
                }
            }
        }

        @Override
        public void onInvitationCancelled(String inviteID, String inviter, String data) {
            if (!isCallingData(data)) {
                return;
            }
            TUIKitLog.d(TAG, "邀请被取消");
            if (mCurCallID.equals(inviteID)) {
                stopCall();
                if (mTRTCInteralListenerManager != null) {
                    mTRTCInteralListenerManager.onCallingCancel();
                }
            }
        }

        @Override
        public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
            if (inviteID != null && !inviteID.equals(mCurCallID)) {
                return;
            }
            TUIKitLog.d(TAG, "邀请超时");

            if (TextUtils.isEmpty(mCurSponsorForMe)) {
                // 邀请者
                for (String userID : inviteeList) {
                    if (mTRTCInteralListenerManager != null) {
                        mTRTCInteralListenerManager.onNoResp(userID);
                    }
                    mCurInvitedList.remove(userID);
                }
            } else {
                // 被邀请者
                if (inviteeList.contains(mCurUserId)) {
                    stopCall();
                    if (mTRTCInteralListenerManager != null) {
                        mTRTCInteralListenerManager.onCallingTimeout();
                    }
                }
                mCurInvitedList.removeAll(inviteeList);
            }
            // 每次超时都需要判断当前是否需要结束通话
            preExitRoom(null);
        }


    };

    /**
     * 解析邀请内容
     *
     * @param data
     * @return
     */
    private boolean isCallingData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            //判断是否包含该字符串 businessID
            if (jsonObject.has(CallModel.SIGNALING_EXTRA_KEY_BUSINESS_ID) && jsonObject.getString(CallModel.SIGNALING_EXTRA_KEY_BUSINESS_ID).equals(CallModel.SIGNALING_EXTRA_VALUE_BUSINESS_ID)) {
                return true;
            }
            //判断是否包含该字符串 call_type
            if (jsonObject.has(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE)) {
                return true;
            }
        } catch (Exception e) {
            TUIKitLog.e(TAG, "isCallingData json parse error 调用解析数据json分析错误");
        }

        return false;
    }

    /**
     * 解析邀请礼物内容
     *
     * @param data
     * @return
     */
    private boolean isGiftInfo(String data) {
        try {
            GiftInfo giftInfo = new Gson().fromJson(data, GiftInfo.class);
            if (!TextUtils.isEmpty(giftInfo.giftId)) {
                //回调礼物数据
                mTRTCInteralListenerManager.getgiftInfo(giftInfo);
                TUIKitLog.e(TAG, "礼物解析成功");
                return false;
            }
        } catch (Exception e) {
        }

        return true;
    }

    /**
     * 解析邀请人信息
     *
     * @param data
     * @return
     */
    private boolean ismemberEmpty(String data) {
        try {
            member member = new Gson().fromJson(data, member.class);
            if (member.getId() > 0) {
                mTRTCInteralListenerManager.longrangecall(data);
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 收到好友邀请消息
     *
     * @param inviteID
     * @param inviter
     * @param groupID
     * @param inviteeList
     * @param data
     */
    public void processInvite(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
        CallModel callModel = new CallModel();
        callModel.callId = inviteID;
        callModel.groupId = groupID;
        callModel.action = CallModel.VIDEO_CALL_ACTION_DIALING; //当前动作 正在呼叫
        callModel.invitedList = inviteeList;

        Map<String, Object> extraMap = null;
        try {
            extraMap = new Gson().fromJson(data, Map.class);
            if (extraMap == null) {
                TUIKitLog.e(TAG, "onReceiveNewInvitation extraMap is null, ignore");
                return;
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_VERSION)) {
                ///
                callModel.version = ((Double) extraMap.get(CallModel.SIGNALING_EXTRA_KEY_VERSION)).intValue();
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE)) {
                ///
                callModel.callType = ((Double) extraMap.get(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE)).intValue();
                mCurCallType = callModel.callType;
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_CALL_END)) {
                ///
                preExitRoom(null);
                return;
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_ROOM_ID)) {
                callModel.roomId = ((Double) extraMap.get(CallModel.SIGNALING_EXTRA_KEY_ROOM_ID)).intValue();
            }
        } catch (JsonSyntaxException e) {
            TUIKitLog.e(TAG, "onReceiveNewInvitation JsonSyntaxException:" + e);
        }

        //调起进入通话界面
        handleDialing(callModel, inviter);

        if (mCurCallID.equals(callModel.callId)) {
            mLastCallModel = (CallModel) callModel.clone();
        }
    }

    /**
     * TRTC的监听器
     */
    private final TRTCCloudListener mTRTCCloudListener = new TRTCCloudListener() {
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            TUIKitLog.e(TAG, "onError: " + errCode + " " + errMsg);
            stopCall();
            if (mTRTCInteralListenerManager != null) {
                mTRTCInteralListenerManager.onError(errCode, errMsg);
            }
        }

        @Override
        public void onEnterRoom(long result) {
            TUIKitLog.d(TAG, "onEnterRoom result:" + result);
            if (result < 0) {
                stopCall();
            } else {
                mIsInRoom = true;
            }
        }

        @Override
        public void onExitRoom(int reason) {
            TUIKitLog.d(TAG, "onExitRoom reason:" + reason);
        }

        @Override
        public void onRemoteUserEnterRoom(String userId) {
            TUIKitLog.d(TAG, "onRemoteUserEnterRoom userId:" + userId);
            mCurRoomRemoteUserSet.add(userId);
            // 只有单聊这个时间才是正确的，因为单聊只会有一个用户进群，群聊这个时间会被后面的人重置
            mEnterRoomTime = V2TIMManager.getInstance().getServerTime();
            if (mTRTCInteralListenerManager != null) {
                mTRTCInteralListenerManager.onUserEnter(userId);
            }
        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason) {
            TUIKitLog.d(TAG, "onRemoteUserLeaveRoom userId:" + userId + ", reason:" + reason);
            mCurRoomRemoteUserSet.remove(userId);
            mCurInvitedList.remove(userId);
            // 远端用户退出房间，需要判断本次通话是否结束
            if (mTRTCInteralListenerManager != null) {
                mTRTCInteralListenerManager.onUserLeave(userId);
            }
            preExitRoom(userId);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            TUIKitLog.d(TAG, "onUserVideoAvailable userId:" + userId + ", available:" + available);
            if (mTRTCInteralListenerManager != null) {
                mTRTCInteralListenerManager.onUserVideoAvailable(userId, available);
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            TUIKitLog.d(TAG, "onUserAudioAvailable userId:" + userId + ", available:" + available);
            if (mTRTCInteralListenerManager != null) {
                mTRTCInteralListenerManager.onUserAudioAvailable(userId, available);
            }
        }

        @Override
        public void onUserVoiceVolume(ArrayList<TRTCCloudDef.TRTCVolumeInfo> userVolumes, int totalVolume) {
            Map<String, Integer> volumeMaps = new HashMap<>();
            for (TRTCCloudDef.TRTCVolumeInfo info : userVolumes) {
                String userId = "";
                if (info.userId == null) {
                    userId = mCurUserId;
                } else {
                    userId = info.userId;
                }
                volumeMaps.put(userId, info.volume);
            }
            mTRTCInteralListenerManager.onUserVoiceVolume(volumeMaps);
        }
    };

    /**
     * 用于获取单例
     *
     * @param context
     * @return 单例
     */
    public static ITRTCAVCall sharedInstance(Context context) {
        synchronized (TRTCAVCallImpl.class) {
            if (sITRTCAVCall == null) {
                sITRTCAVCall = new TRTCAVCallImpl(context);
            }
            return sITRTCAVCall;
        }
    }

    /**
     * 销毁单例
     */
    public static void destroySharedInstance() {
        synchronized (TRTCAVCallImpl.class) {
            if (sITRTCAVCall != null) {
                sITRTCAVCall.destroy();
                sITRTCAVCall = null;
            }
        }
    }

    /**
     * 初始化数据
     *
     * @param context
     */
    public TRTCAVCallImpl(Context context) {
        mContext = context;
        mTIMManager = V2TIMManager.getInstance();
        mTRTCCloud = TRTCCloud.sharedInstance(context);
        mTRTCInteralListenerManager = new TRTCInteralListenerManager();
        mLastCallModel.version = TUIKitConstants.version;
    }

    //设置进入状态
    private void startCall() {
        isOnCalling = true;
    }

    /**
     * 停止此次通话，把所有的变量都会重置
     */
    public void stopCall() {
        isOnCalling = false;
        mIsInRoom = false;
        mEnterRoomTime = 0;
        mCurCallID = "";
        mCurRoomID = 0;
        mCurInvitedList.clear();
        mCurRoomRemoteUserSet.clear();
        mCurSponsorForMe = "";
        mLastCallModel = new CallModel();
        mLastCallModel.version = TUIKitConstants.version;
        mIsRespSponsor = false;
        mCurGroupId = "";
        mCurCallType = TYPE_UNKNOWN;
    }

    @Override
    public void init() {
    }

    public void handleDialing(CallModel callModel, String user) {
        if (!TextUtils.isEmpty(mCurCallID)) {
            // 正在通话时，收到了一个邀请我的通话请求,需要告诉对方忙线
            if (isOnCalling && callModel.invitedList.contains(mCurUserId)) {
                sendModel(user, CallModel.VIDEO_CALL_ACTION_LINE_BUSY, callModel); //电话占线
                return;
            }

            // 与对方处在同一个群中，此时收到了邀请群中其他人通话的请求，界面上展示连接动画
            if (!TextUtils.isEmpty(mCurGroupId) && !TextUtils.isEmpty(callModel.groupId)) {
                if (mCurGroupId.equals(callModel.groupId)) {
                    mCurInvitedList.addAll(callModel.invitedList);
                    //群组邀请ID会有重复情况，需要去重下
                    Set<String> set = new HashSet<String>();
                    set.addAll(mCurInvitedList);
                    mCurInvitedList = new ArrayList<String>(set);

                    if (mTRTCInteralListenerManager != null) {
                        mTRTCInteralListenerManager.onGroupCallInviteeListUpdate(mCurInvitedList);
                    }
                    return;
                }
            }
        }

        // 虽然是群组聊天，但是对方并没有邀请我，我不做处理
        if (!TextUtils.isEmpty(callModel.groupId) && !callModel.invitedList.contains(mCurUserId)) {
            return;
        }

        startCall();
        mCurCallID = callModel.callId;
        mCurRoomID = callModel.roomId;
        mCurCallType = callModel.callType;
        mCurSponsorForMe = user;
        mCurGroupId = callModel.groupId;
        final String cid = mCurCallID;

        TUIKitLog.i(TAG, "handleDialing: " + callModel.toString());
        // 邀请列表中需要移除掉自己
        callModel.invitedList.remove(mCurUserId);
        List<String> onInvitedUserListParam = callModel.invitedList;
        if (!TextUtils.isEmpty(mCurGroupId)) {
            mCurInvitedList.addAll(callModel.invitedList);
        }

        //回调给接上接口文件
        if (mTRTCInteralListenerManager != null) {
            mTRTCInteralListenerManager.onInvited(user, onInvitedUserListParam, !TextUtils.isEmpty(mCurGroupId), mCurCallType);
        }
    }

    @Override
    public void destroy() {
        //必要的清除逻辑
        V2TIMManager.getSignalingManager().removeSignalingListener(mTIMSignallingListener);
        mTRTCCloud.stopLocalPreview();
        mTRTCCloud.stopLocalAudio();
        mTRTCCloud.exitRoom();
    }

    @Override
    public void addListener(TRTCAVCallListener listener) {
        mTRTCInteralListenerManager.addListenter(listener);
    }

    @Override
    public void removeListener(TRTCAVCallListener listener) {
        mTRTCInteralListenerManager.removeListenter(listener);
    }

    @Override
    public void login(int sdkAppId, final String userId, final String userSign, final ITRTCAVCall.ActionCallBack callback) {
        //初始化登录
        TUIKitLog.i(TAG, "startTUIKitLogin, sdkAppId:" + sdkAppId + " userId:" + userId + " sign is empty:" + TextUtils.isEmpty(userSign));
        if (sdkAppId == 0 || TextUtils.isEmpty(userId) || TextUtils.isEmpty(userSign)) {
            TUIKitLog.e(TAG, "startTUIKitLogin fail. params invalid.");
            if (callback != null) {
                callback.onError(-1, "login fail, params is invalid.");
            }
            return;
        }
        mSdkAppId = sdkAppId;
        // 初始化信令 需要将信令监听器添加到IM上
        V2TIMManager.getSignalingManager().addSignalingListener(mTIMSignallingListener);
        String loginUser = mTIMManager.getLoginUser();
        int loginStatus = mTIMManager.getLoginStatus();

        if (loginUser != null && loginUser.equals(userId)) {
            TUIKitLog.d(TAG, TUIKit.getAppContext().getString(R.string.im_logined) + loginUser);
            mCurUserId = loginUser;
            mCurUserSig = userSign;
            if (callback != null) {
                callback.onSuccess();
            }
        } else {
            if (callback != null) {
                callback.onError(BaseConstants.ERR_SDK_NOT_LOGGED_IN, "not login im");
            }
        }
    }

    @Override
    public void logout(final ITRTCAVCall.ActionCallBack callBack) {
        stopCall();
        exitRoom();
    }

    @Override
    public void call(final String userId, int type) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(userId);
        internalCall(list, type, "");
    }

    @Override
    public void groupCall(final List<String> userIdList, int type, String groupId) {
        if (isCollectionEmpty(userIdList)) {
            return;
        }
        //邀请人进入视频或语音
        internalCall(userIdList, type, groupId);
    }

    /**
     * 统一的拨打逻辑
     *
     * @param userIdList 需要邀请的用户列表
     * @param type       邀请类型
     * @param groupId    群组通话的group id，如果是C2C需要传 ""
     */
    private void internalCall(final List<String> userIdList, int type, String groupId) {
        final boolean isGroupCall = !TextUtils.isEmpty(groupId);
        if (!isOnCalling) {
            mCurRoomID = generateRoomID();  // 首次拨打电话，生成id，并进入trtc房间
            mCurGroupId = groupId;
            mCurCallType = type; //语音-视频
            enterTRTCRoom();
            startCall();
        }

        // 非首次拨打，不能发起新的groupId通话
        if (!TextUtils.equals(mCurGroupId, groupId)) {
            return;
        }

        // 过滤已经邀请的用户id
        List<String> filterInvitedList = new ArrayList<>();
        for (String id : userIdList) {
            if (!mCurInvitedList.contains(id)) {
                filterInvitedList.add(id);
            }
        }
        // 如果当前没有需要邀请的id则返回
        if (isCollectionEmpty(filterInvitedList)) {
            return;
        }

        mCurInvitedList.addAll(filterInvitedList);
        TUIKitLog.i(TAG, "groupCall: filter:" + filterInvitedList + " all:" + mCurInvitedList);

        // 填充通话信令的model
        mLastCallModel.action = CallModel.VIDEO_CALL_ACTION_DIALING;
        mLastCallModel.invitedList = mCurInvitedList;
        mLastCallModel.roomId = mCurRoomID;
        mLastCallModel.groupId = mCurGroupId;
        mLastCallModel.callType = mCurCallType;

        // 首次拨打电话，生成id
        if (!TextUtils.isEmpty(mCurGroupId)) {
            // 群聊发送群消息
            mCurCallID = sendModel("", CallModel.VIDEO_CALL_ACTION_DIALING); //群聊正在呼叫
        } else {
            // 单聊发送C2C消息
            for (final String userId : filterInvitedList) {
                mCurCallID = sendModel(userId, CallModel.VIDEO_CALL_ACTION_DIALING);//单聊正在呼叫
            }
        }
        mLastCallModel.callId = mCurCallID;
    }

    /**
     * 重要：用于判断是否需要结束本次通话
     * 在用户超时、拒绝、忙线、有人退出房间时需要进行判断
     */
    private void preExitRoom(String leaveUser) {
        TUIKitLog.i(TAG, "preExitRoom: " + mCurRoomRemoteUserSet + " " + mCurInvitedList);
        if (mCurRoomRemoteUserSet.isEmpty() && mCurInvitedList.isEmpty() && mIsInRoom) {
            // 当没有其他用户在房间里了，则结束通话。
            if (!TextUtils.isEmpty(leaveUser)) {
                if (TextUtils.isEmpty(mCurGroupId)) {
                    sendModel(leaveUser, CallModel.VIDEO_CALL_ACTION_HANGUP);//挂断
                } else {
                    sendModel("", CallModel.VIDEO_CALL_ACTION_HANGUP);//挂断
                }
            }
            exitRoom();
            stopCall();
            if (mTRTCInteralListenerManager != null) {
                mTRTCInteralListenerManager.onCallEnd();
            }
        }
    }

    /**
     * trtc 退房
     */
    private void exitRoom() {
        mTRTCCloud.stopLocalPreview();
        mTRTCCloud.stopLocalAudio();
        mTRTCCloud.exitRoom();
    }

    @Override
    public void accept() {
        mIsRespSponsor = true;
        sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_ACTION_ACCEPT); //接听电话
        enterTRTCRoom();
    }

    /**
     * trtc 进房
     */
    private void enterTRTCRoom() {
        if (mCurCallType == ITRTCAVCall.TYPE_VIDEO_CALL) {
            // 开启基础美颜
            TXBeautyManager txBeautyManager = mTRTCCloud.getBeautyManager();
            // 自然美颜
            txBeautyManager.setBeautyStyle(1);
            txBeautyManager.setBeautyLevel(6);
            // 进房前需要设置一下关键参数
            TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
            encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540;
            encParam.videoFps = 15;
            encParam.videoBitrate = 1000;
            encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
            encParam.enableAdjustRes = true;
            mTRTCCloud.setVideoEncoderParam(encParam);
        }
        TUIKitLog.i(TAG, "enterTRTCRoom: " + mCurUserId + " room:" + mCurRoomID);
        TRTCCloudDef.TRTCParams TRTCParams = new TRTCCloudDef.TRTCParams(mSdkAppId, mCurUserId, mCurUserSig, mCurRoomID, "", "");
        TRTCParams.role = TRTCCloudDef.TRTCRoleAnchor;
        mTRTCCloud.enableAudioVolumeEvaluation(300);
        mTRTCCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_SPEAKER);
        mTRTCCloud.startLocalAudio();
        // 进房前，开始监听trtc的消息
        mTRTCCloud.setListener(mTRTCCloudListener);
        mTRTCCloud.enterRoom(TRTCParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
    }

    @Override
    public void reject() {
        mIsRespSponsor = true;
        sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_ACTION_REJECT); //拒接电话
        stopCall();
    }

    @Override
    public void hangup() {
        // 1. 如果还没有在通话中，说明还没有接通，所以直接拒绝了
        if (!isOnCalling) {
            reject();
            return;
        }

        boolean fromGroup = (!TextUtils.isEmpty(mCurGroupId));
        if (fromGroup) {
            groupHangup();  //群组
        } else {
            singleHangup(); //个人
        }
    }

    /**
     * 群组
     */
    private void groupHangup() {
        if (isCollectionEmpty(mCurRoomRemoteUserSet)) {
            // 当前以及没有人在通话了，直接向群里发送一个取消消息 发起人取消
            sendModel("", CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL);
        }
        stopCall();
        exitRoom();
    }

    /**
     * 个人
     */
    private void singleHangup() {
        for (String id : mCurInvitedList) {
            sendModel(id, CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL);//发起人取消
        }
        stopCall();
        exitRoom();
    }

    @Override
    public void openCamera(boolean isFrontCamera, TXCloudVideoView txCloudVideoView) {
        if (txCloudVideoView == null) {
            return;
        }
        mIsUseFrontCamera = isFrontCamera;
        mTRTCCloud.startLocalPreview(isFrontCamera, txCloudVideoView);
    }

    @Override
    public void closeCamera() {
        mTRTCCloud.stopLocalPreview();
    }

    @Override
    public void startRemoteView(String userId, TXCloudVideoView txCloudVideoView) {
        if (txCloudVideoView == null) {
            return;
        }
        mTRTCCloud.startRemoteView(userId, txCloudVideoView);
    }

    @Override
    public void stopRemoteView(String userId) {
        mTRTCCloud.stopRemoteView(userId);
    }

    @Override
    public void switchCamera(boolean isFrontCamera) {
        if (mIsUseFrontCamera == isFrontCamera) {
            return;
        }
        mIsUseFrontCamera = isFrontCamera;
        mTRTCCloud.switchCamera();
    }

    @Override
    public void setMicMute(boolean isMute) {
        mTRTCCloud.muteLocalAudio(isMute);
    }

    @Override
    public void setHandsFree(boolean isHandsFree) {
        if (isHandsFree) {
            mTRTCCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_SPEAKER);
        } else {
            mTRTCCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_EARPIECE);
        }
    }

    /**
     * // 单聊发送C2C消息
     *
     * @param user
     * @param action
     * @return
     */
    private String sendModel(final String user, int action) {
        return sendModel(user, action, null);
    }

    private void sendOnlineMessageWithOfflinePushInfoForGroupCall(final CallModel model) {
        V2TIMOfflinePushInfo v2TIMOfflinePushInfo = getOfflinePushInfo(model);

        MessageCustom custom = new MessageCustom();
        custom.businessID = MessageCustom.BUSINESS_ID_AV_CALL;
        custom.version = TUIKitConstants.version;
        V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(new Gson().toJson(custom).getBytes());

        for (String receiver : model.invitedList) {
            TUIKitLog.i(TAG, "sendOnlineMessage to " + receiver);
            V2TIMManager.getMessageManager().sendMessage(message, receiver, null, V2TIMMessage.V2TIM_PRIORITY_DEFAULT,
                    true, v2TIMOfflinePushInfo, new V2TIMSendCallback<V2TIMMessage>() {

                        @Override
                        public void onError(int code, String desc) {
                            TUIKitLog.e(TAG, "sendOnlineMessage failed, code:" + code + ", desc:" + desc);
                        }

                        @Override
                        public void onSuccess(V2TIMMessage v2TIMMessage) {
                            TUIKitLog.i(TAG, "sendOnlineMessage msgId:" + v2TIMMessage.getMsgID());
                        }

                        @Override
                        public void onProgress(int progress) {

                        }
                    });
        }
    }

    private V2TIMOfflinePushInfo getOfflinePushInfo(CallModel model) {
        OfflineMessageContainerBean containerBean = new OfflineMessageContainerBean();
        OfflineMessageBean entity = new OfflineMessageBean();
        entity.content = new Gson().toJson(model);
        entity.sender = V2TIMManager.getInstance().getLoginUser(); // 发送者肯定是登录账号
        entity.action = OfflineMessageBean.REDIRECT_ACTION_CALL;
        entity.sendTime = V2TIMManager.getInstance().getServerTime();
        entity.nickname = TUIKitConfigs.getConfigs().getGeneralConfig().getUserNickname();
        entity.faceUrl = TUIKitConfigs.getConfigs().getGeneralConfig().getUserFaceUrl();
        containerBean.entity = entity;
        entity.chatType = V2TIMConversation.V2TIM_GROUP;

        V2TIMOfflinePushInfo v2TIMOfflinePushInfo = new V2TIMOfflinePushInfo();
        v2TIMOfflinePushInfo.setExt(new Gson().toJson(containerBean).getBytes());
        // OPPO必须设置ChannelID才可以收到推送消息，这个channelID需要和控制台一致
        v2TIMOfflinePushInfo.setAndroidOPPOChannelID("tuikit");
        v2TIMOfflinePushInfo.setDesc(TUIKit.getAppContext().getString(R.string.offline_call_tip));
        return v2TIMOfflinePushInfo;
    }

    private String getCallId() {
        return mCurCallID;
    }

    /**
     * 信令发送函数，当CallModel 存在groupId时会向群组发送信令
     *
     * @param user
     * @param action
     * @param model
     */
    private String sendModel(final String user, int action, CallModel model) {
        String callID = null;
        final CallModel realCallModel;
        if (model != null) {
            realCallModel = (CallModel) model.clone();
            realCallModel.action = action;
        } else {
            realCallModel = generateModel(action);
        }

        final boolean isGroup = (!TextUtils.isEmpty(realCallModel.groupId));
        if (action == CallModel.VIDEO_CALL_ACTION_HANGUP && mEnterRoomTime != 0 && !isGroup) {
            realCallModel.duration = (int) (V2TIMManager.getInstance().getServerTime() - mEnterRoomTime);
            mEnterRoomTime = 0;
        }
        String receiver = "";
        String groupId = "";
        if (isGroup) {
            groupId = realCallModel.groupId;
        } else {
            receiver = user;
        }
        Map<String, Object> customMap = new HashMap();
        customMap.put(CallModel.SIGNALING_EXTRA_KEY_VERSION, TUIKitConstants.version);
        customMap.put(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE, realCallModel.callType);
        customMap.put(CallModel.SIGNALING_EXTRA_KEY_BUSINESS_ID, CallModel.SIGNALING_EXTRA_VALUE_BUSINESS_ID);

        // 信号
        switch (realCallModel.action) {
            case CallModel.VIDEO_CALL_ACTION_DIALING://正在呼叫
                customMap.put(CallModel.SIGNALING_EXTRA_KEY_ROOM_ID, realCallModel.roomId);
                String dialingDataStr = new Gson().toJson(customMap); //对接转为JSON
                if (isGroup) {
                    //然后群成员可以在群内发起群呼叫邀请 inviteInGroup，被邀请的群成员会收到邀请通知
                    callID = V2TIMManager.getSignalingManager().inviteInGroup(groupId, realCallModel.invitedList, dialingDataStr, false, TIME_OUT_COUNT, new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            TUIKitLog.e(TAG, "inviteInGroup callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            TUIKitLog.d(TAG, "inviteInGroup success:" + realCallModel);
                            realCallModel.callId = getCallId();
                            realCallModel.timeout = TIME_OUT_COUNT;
                            realCallModel.version = TUIKitConstants.version;
                            sendOnlineMessageWithOfflinePushInfoForGroupCall(realCallModel);
                        }
                    });
                } else {

                    realCallModel.callId = getCallId();
                    realCallModel.timeout = TIME_OUT_COUNT;
                    realCallModel.version = TUIKitConstants.version;
                    V2TIMOfflinePushInfo offlinePushInfo = getOfflinePushInfo(realCallModel);

                    //接口进行点对点呼叫，对方收到邀请通知 onReceiveNewInvitation 后可以选择接受、拒绝或等待超时
                    callID = V2TIMManager.getSignalingManager().invite(receiver, dialingDataStr, false, offlinePushInfo, TIME_OUT_COUNT, new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            TUIKitLog.e(TAG, "invite  callID:" + realCallModel.callId + ",error:" + code + " desc:" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            TUIKitLog.d(TAG, "invite success:" + realCallModel);
                        }
                    });
                }
                break;
            case CallModel.VIDEO_CALL_ACTION_ACCEPT: //接听电话
                String acceptDataStr = new Gson().toJson(customMap);
                V2TIMManager.getSignalingManager().accept(realCallModel.callId, acceptDataStr, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e(TAG, "accept callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        TUIKitLog.d(TAG, "accept success callID:" + realCallModel.callId);
                    }
                });
                break;

            case CallModel.VIDEO_CALL_ACTION_REJECT://拒接电话
                String rejectDataStr = new Gson().toJson(customMap);
                V2TIMManager.getSignalingManager().reject(realCallModel.callId, rejectDataStr, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e(TAG, "reject callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        TUIKitLog.d(TAG, "reject success callID:" + realCallModel.callId);
                    }
                });
                break;
            case CallModel.VIDEO_CALL_ACTION_LINE_BUSY://电话占线
                customMap.put(CallModel.SIGNALING_EXTRA_KEY_LINE_BUSY, "");
                String lineBusyMapStr = new Gson().toJson(customMap);
                V2TIMManager.getSignalingManager().reject(realCallModel.callId, lineBusyMapStr, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e(TAG, "reject  callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        TUIKitLog.d(TAG, "reject success callID:" + realCallModel.callId);
                    }
                });
                break;

            case CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL: //发起人取消
                String cancelMapStr = new Gson().toJson(customMap);
                Log.d(TAG, "sendModel发起人取消: " + realCallModel.callId);
                V2TIMManager.getSignalingManager().cancel(realCallModel.callId, cancelMapStr, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        TUIKitLog.e(TAG, "cancel callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        TUIKitLog.d(TAG, "cancel success callID:" + realCallModel.callId);
                    }
                });
                break;

            case CallModel.VIDEO_CALL_ACTION_HANGUP://挂断
                customMap.put(CallModel.SIGNALING_EXTRA_KEY_CALL_END, realCallModel.duration);
                String hangupMapStr = new Gson().toJson(customMap);
                if (isGroup) {
                    V2TIMManager.getSignalingManager().inviteInGroup(groupId, realCallModel.invitedList, hangupMapStr, false, 0, new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            TUIKitLog.e(TAG, "inviteInGroup-->hangup callID: " + realCallModel.callId + ", error:" + code + " desc:" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            TUIKitLog.d(TAG, "inviteInGroup-->hangup success callID:" + realCallModel.callId);
                        }
                    });
                } else {
                    V2TIMManager.getSignalingManager().invite(receiver, hangupMapStr, false, null, 0, new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            TUIKitLog.e(TAG, "invite-->hangup callID: " + realCallModel.callId + ", error:" + code + " desc:" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            TUIKitLog.d(TAG, "invite-->hangup success callID:" + realCallModel.callId);
                        }
                    });
                }
                break;

            default:
                break;
        }

        // 最后需要重新赋值
        if (realCallModel.action != CallModel.VIDEO_CALL_ACTION_REJECT &&
                realCallModel.action != CallModel.VIDEO_CALL_ACTION_HANGUP &&
                realCallModel.action != CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL &&
                model == null) {
            mLastCallModel = (CallModel) realCallModel.clone();
        }
        return callID;
    }

    private CallModel generateModel(int action) {
        CallModel callModel = (CallModel) mLastCallModel.clone();
        callModel.action = action;
        return callModel;
    }

    private static boolean isCollectionEmpty(Collection coll) {
        return coll == null || coll.size() == 0;
    }

    private static String CallModel2Json(CallModel callModel) {
        if (callModel == null) {
            return null;
        }
        return new Gson().toJson(callModel);
    }

    /**
     * 生成房间号
     *
     * @return
     */
    private static int generateRoomID() {
        Random random = new Random();
        return random.nextInt(ROOM_ID_MAX - ROOM_ID_MIN + 1) + ROOM_ID_MIN;
    }

    /**
     * 继承相关方法实现接口方法
     */
    private class TRTCInteralListenerManager implements TRTCAVCallListener {

        private final List<WeakReference<TRTCAVCallListener>> mWeakReferenceList;

        public TRTCInteralListenerManager() {
            mWeakReferenceList = new ArrayList<>();
        }

        public void addListenter(TRTCAVCallListener listener) {
            WeakReference<TRTCAVCallListener> listenerWeakReference = new WeakReference<>(listener);
            mWeakReferenceList.add(listenerWeakReference);
        }

        public void removeListenter(TRTCAVCallListener listener) {
            Iterator iterator = mWeakReferenceList.iterator();
            while (iterator.hasNext()) {
                WeakReference<TRTCAVCallListener> reference = (WeakReference<TRTCAVCallListener>) iterator.next();
                if (reference.get() == null) {
                    iterator.remove();
                    continue;
                }
                if (reference.get() == listener) {
                    iterator.remove();
                }
            }
        }

        @Override
        public void onError(int code, String msg) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onError(code, msg);
                }
            }
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onInvited(sponsor, userIdList, isFromGroup, callType);
                }
            }
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onGroupCallInviteeListUpdate(userIdList);
                }
            }
        }

        @Override
        public void onUserEnter(String userId) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onUserEnter(userId);
                }
            }
        }

        @Override
        public void onUserLeave(String userId) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onUserLeave(userId);
                }
            }
        }

        @Override
        public void onReject(String userId) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onReject(userId);
                }
            }
        }

        @Override
        public void onNoResp(String userId) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onNoResp(userId);
                }
            }
        }

        @Override
        public void onLineBusy(String userId) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onLineBusy(userId);
                }
            }
        }

        @Override
        public void onCallingCancel() {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onCallingCancel();
                }
            }
        }

        @Override
        public void onCallingTimeout() {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onCallingTimeout();
                }
            }
        }

        @Override
        public void onCallEnd() {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onCallEnd();
                }
            }
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onUserVideoAvailable(userId, isVideoAvailable);
                }
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onUserAudioAvailable(userId, isVideoAvailable);
                }
            }
        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.onUserVoiceVolume(volumeMap);
                }
            }
        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {
            //收到对方发来的礼物
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.getgiftInfo(giftInfo);
                }
            }

        }

        @Override
        public void longrangecall(String json) {
            //收到对方发来的消息内容
            for (WeakReference<TRTCAVCallListener> reference : mWeakReferenceList) {
                TRTCAVCallListener listener = reference.get();
                if (listener != null) {
                    listener.longrangecall(json);
                }
            }

        }
    }

    /**
     * 通过邀请方式发送礼物
     *
     * @param receiver
     */
    @Override
    public void invitereceiver(String receiver, String data) {
        V2TIMManager.getSignalingManager().invite(receiver, data, true, null, 0, null);
    }


}
