package com.tianxin.activity.picenter.module;

import android.content.Context;
import android.text.TextUtils;

import com.tianxin.Util.Toashow;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.mm.opensdk.utils.Log;

import java.util.List;

/**
 * 信令接口处理
 */
public class Manager {

    private static final String TAG = "Manager";
    private static Manager smanager;
    private static Context mContext;

    public static Manager getInstance(Context mContext) {
        if (smanager == null) {
            smanager = new Manager(mContext);
        }
        return smanager;
    }

    public Manager(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 邀请某个人
     *
     * @param userid
     * @param json
     * @param title
     * @param desc
     */
    public void invite(String userid, String json, String title, String desc) {
        V2TIMOfflinePushInfo pushInfo = new V2TIMOfflinePushInfo();
        pushInfo.setTitle(title);
        pushInfo.setDesc(desc);
        V2TIMManager.getSignalingManager().invite(userid, json, true, null, 0,null );
    }

    /**
     * 邀请方取消邀请
     */
    public void cancel() {
        V2TIMManager.getSignalingManager().cancel("2201", "data", new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                android.util.Log.d(TAG, "onError: ");
            }

            @Override
            public void onSuccess() {
                android.util.Log.d(TAG, "onSuccess: ");
            }
        });
    }

    /**
     * 接收方接收邀请
     */
    public void accept() {
        V2TIMManager.getSignalingManager().accept("2201", "data", new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                android.util.Log.d(TAG, "onError: ");
            }

            @Override
            public void onSuccess() {
                android.util.Log.d(TAG, "onSuccess: ");
            }
        });
    }

    /**
     * 接收方拒绝邀请
     */
    public void reject() {
        V2TIMManager.getSignalingManager().reject("2201", "data", new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                android.util.Log.d(TAG, "onError: ");
            }

            @Override
            public void onSuccess() {
                android.util.Log.d(TAG, "onSuccess: ");
            }
        });
    }

    /**
     * 添加信令监听
     */
    public void addSignalingListener() {
        //V2TIMManager.getSignalingManager().addSignalingListener(v2TIMSignalingListener);
    }

    /**
     * 移除信令监听
     */
    public void removeSignalingListener() {
       // V2TIMManager.getSignalingManager().removeSignalingListener(v2TIMSignalingListener);
    }

    /**
     * 信令监听
     */
    private V2TIMSignalingListener v2TIMSignalingListener = new V2TIMSignalingListener() {
        @Override
        public void onReceiveNewInvitation(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
            super.onReceiveNewInvitation(inviteID, inviter, groupID, inviteeList, data);
            Log.d(TAG, "在收到新邀请时: " + inviteID + " " + inviter + " " + data);

        }

        @Override
        public void onInviteeAccepted(String inviteID, String invitee, String data) {
            super.onInviteeAccepted(inviteID, invitee, data);
            Log.d(TAG, "接受: " + inviteID + " " + invitee + " " + data);
            Toashow.show("接受" + inviteID);
        }

        @Override
        public void onInviteeRejected(String inviteID, String invitee, String data) {
            super.onInviteeRejected(inviteID, invitee, data);
            Log.d(TAG, "拒绝接受: " + inviteID + " " + invitee + "" + data);
            Toashow.show("拒绝接受" + inviteID);
        }

        @Override
        public void onInvitationCancelled(String inviteID, String inviter, String data) {
            super.onInvitationCancelled(inviteID, inviter, data);
            Log.d(TAG, "关于取消的邀请: " + inviteID + "" + inviter + " " + data);
            Toashow.show("关于取消的邀请" + inviteID);
        }

        @Override
        public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
            super.onInvitationTimeout(inviteID, inviteeList);
            Log.d(TAG, "邀请超时: " + inviteID);
            Toashow.show("邀请超时" + inviteID);
        }
    };

    /**
     * 搜索查查询对方帐号是否存在
     *
     * @param phoneNumber
     * @param type
     */
    private void searchContactsByPhone(String phoneNumber, int type) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        /**
         * 搜索ID号码
         */
        ProfileManager.getInstance().getUserInfoByUserId(phoneNumber, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

}
