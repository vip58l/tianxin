package com.tianxin.IMtencent.chta;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendInfoResult;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMFriendshipManager;
import com.tencent.imsdk.v2.V2TIMGroupManager;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户资料管理自定义的文件
 * https://cloud.tencent.com/document/product/269/44482
 */
public class ChatV2TIMFriendInfoResult {
    String TAG = ChatV2TIMFriendInfoResult.class.getSimpleName();
    private V2TIMFriendshipManager friendshipManager;
    private V2TIMMessageManager messageManager;
    private V2TIMManager instance;
    private static V2TIMGroupManager groupManager;

    public ChatV2TIMFriendInfoResult() {
        instance = V2TIMManager.getInstance();
        friendshipManager = V2TIMManager.getFriendshipManager();
        messageManager = V2TIMManager.getMessageManager();
        groupManager = V2TIMManager.getGroupManager();


    }

    /**
     * 删除本地及云端的消息
     */
    public void deleteMessages() {
        V2TIMMessage v2TIMMessage = new V2TIMMessage();
        List<V2TIMMessage> list = new ArrayList<>();
        list.add(v2TIMMessage);
        messageManager.deleteMessages(list, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: ");
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }
        });


    }

    /**
     * 修改指定的好友信息接口为 setFriendInfo ，可修改好友备注等资料。
     */
    public void setFriendInfo(String userid, String friendRemark) {
        V2TIMFriendInfo v2TIMFriendInfo = new V2TIMFriendInfo();
        v2TIMFriendInfo.setUserID(userid);
        v2TIMFriendInfo.setFriendRemark(friendRemark);
        friendshipManager.setFriendInfo(v2TIMFriendInfo, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: 修改失败");
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: 修改成功");
            }
        });
    }

    /**
     * 查询指定的好友资料接口为 getFriendsInfo，从回调信息中通过 V2TIMFriendInfoResult 的 getRelation() 可以得到该用户与自己的关系：
     *
     * @param userid
     */
    public void getFriendsInfo(String userid) {
        List<String> list = new ArrayList<>();
        list.add(userid);
        friendshipManager.getFriendsInfo(list, new V2TIMValueCallback<List<V2TIMFriendInfoResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMFriendInfoResult> v2TIMFriendInfoResults) {

            }
        });
    }

    /**
     * 查询自己的资料接口为 getUsersInfo，其中参数 userIDList 需填入自己的 UserID。  //查询非好友用户资料
     *
     * @param userid
     */
    public void getUsersInfo(String userid) {
        List<String> list = new ArrayList<>();
        list.add(userid);
        instance.getUsersInfo(list, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {

            }
        });

    }

    /**
     * 修改自己的资料接口为 setSelfInfo。修改自己的资料成功后，会收到 onSelfInfoUpdated 回调。
     */
    public void setSelfInfo() {
        HashMap<String, byte[]> customHashMap = new HashMap<>();
        customHashMap.put("myabc", "myabc".getBytes());
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();
        v2TIMUserFullInfo.setAllowType(1);
        v2TIMUserFullInfo.setRole(1);
        v2TIMUserFullInfo.setSelfSignature("aaa");
        v2TIMUserFullInfo.setFaceUrl("aaa");
        v2TIMUserFullInfo.setNickname("GetRoomHotNewSome");
        v2TIMUserFullInfo.setGender(1);
        v2TIMUserFullInfo.setLevel(1);
        v2TIMUserFullInfo.setCustomInfo(customHashMap);

        instance.setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }

    /**
     * 屏蔽某人消息拉黑某人
     *
     * @param userid
     */
    public void addToBlackList(String userid) {
        List<String> list = new ArrayList<>();
        list.add(userid);
        friendshipManager.addToBlackList(list, new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {

            }
        });

    }

    /**
     * 解除拉黑
     *
     * @param userid
     */
    public void deleteFromBlackList(String userid) {
        List<String> list = new ArrayList<>();
        list.add(userid);
        friendshipManager.deleteFromBlackList(list, new V2TIMValueCallback<List<V2TIMFriendOperationResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMFriendOperationResult> v2TIMFriendOperationResults) {

            }
        });
    }

    /**
     * 获取黑名单列表
     */
    public void getBlackList() {
        List<V2TIMFriendInfo> list = new ArrayList<>();
        V2TIMFriendInfo v2TIMFriendInfo = new V2TIMFriendInfo();
        list.add(v2TIMFriendInfo);
        friendshipManager.getBlackList((V2TIMValueCallback<List<V2TIMFriendInfo>>) list);
    }

    /**
     * 3.5 禁言（只有管理员或群主能够调用）
     */
    public void muteGroupMember() {
        String groupID = "123456"; //群主ID
        String userID = "2201";    //用户
        groupManager.muteGroupMember(groupID, userID, 20, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: " + "设置禁语成功");

            }
        });
    }

    /**
     * 1.2 获取当前用户已经加入的群列表
     */
    void getJoinedGroupList() {
    }

    /**
     * 2.1 拉取群资料
     */
    void getGroupsInfo() {
    }

    /**
     * 3.1 获取群成员列表
     */
    void getGroupMemberList() {

    }

    /**
     * 3.2 获取指定的群成员资料
     */
    void getGroupMembersInfo() {

    }

    /**
     * 3.4 修改指定的群成员资料
     */
    void setGroupMemberInfo() {

    }

    /**
     * 3.6 邀请他人入群
     */
    void inviteUserToGroup() {
    }

    /**
     * 3.7 踢人
     */
    void kickGroupMember() {
    }

    /**
     * 3.8 切换群成员的角色
     */
    void setGroupMemberRole() {
    }

    /**
     * 3.9 转让群主
     */
    void transferGroupOwner() {
    }

    /**
     * 4.1 获取加群申请列表
     */
    void getGroupApplicationList() {
    }

    /**
     * 4.2 同意某一条加群申请
     **/
    void acceptGroupApplication() {
    }

    /**
     * 4.3 拒绝某一条加群申请
     */
    void refuseGroupApplication() {
    }

    /**
     * 4.4 标记申请列表为已读
     */
    void setGroupApplicationRead() {
    }



}
