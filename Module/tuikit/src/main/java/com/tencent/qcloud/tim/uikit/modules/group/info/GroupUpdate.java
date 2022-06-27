package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.LongDef;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.trtcvoiceroom.model.impl.base.TRTCLogger;
import com.tencent.liteav.trtcvoiceroom.model.impl.base.TXCallback;
import com.tencent.opensource.listener.Callback;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupUpdate {
    public static String TAG = GroupUpdate.class.getSimpleName();

    /**
     * 2.5 设置群属性。已有该群属性则更新其 value 值，没有该群属性则添加该属性
     */
    public static void setGroupAttributes(String groupID) {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put("level", "1");
        attributes.put("notavailable", "1");
        V2TIMManager.getGroupManager().setGroupAttributes(groupID, attributes, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastLongMessage("操作权限不足 全体言禁错误" + i + " " + s);
            }

            @Override
            public void onSuccess() {
                ToastUtil.toastLongMessage("设置全体言禁成功");

            }
        });
    }

    /**
     * 2.6 删除指定群属性，keys 传 null 则清空所有群属性
     */
    public static void deleteGroupAttributes(String groupID) {
        V2TIMManager.getGroupManager().deleteGroupAttributes(groupID, null, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastLongMessage("删除操作权限不足 全体言禁错误" + i + " " + s);

            }

            @Override
            public void onSuccess() {
                ToastUtil.toastLongMessage("删除 取消全体言禁成功");
            }
        });

    }

    /**
     * 2.7 获取指定群属性，键传空则获取所有群属性
     *
     * @param groupID
     */
    public static void getGroupAttributes(String groupID) {
        V2TIMManager.getGroupManager().getGroupAttributes(groupID, null, new V2TIMValueCallback<Map<String, String>>() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastLongMessage("获取所有群属性 操作权限不足 全体言禁错误" + i + " " + s);
            }

            @Override
            public void onSuccess(Map<String, String> stringStringMap) {
                Log.d(TAG, "onSuccess: " + stringStringMap.toString());
                ToastUtil.toastLongMessage("获取所有群属性 操作权限不足 全体言禁错误" + stringStringMap.toString());

            }
        });
    }

    /**
     * 3.6 邀请他人入群
     *
     * @param groupID
     */
    public static void inviteUserToGroup(String groupID, String userid) {
        List<String> list = Arrays.asList(userid);
        V2TIMManager.getGroupManager().inviteUserToGroup(groupID, list, new V2TIMValueCallback<List<V2TIMGroupMemberOperationResult>>() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastLongMessage("onError: " + i + s);
                Log.d(TAG, "onError: " + i + s);

            }

            @Override
            public void onSuccess(List<V2TIMGroupMemberOperationResult> v2TIMGroupMemberOperationResults) {
                for (V2TIMGroupMemberOperationResult result : v2TIMGroupMemberOperationResults) {
                    Log.d(TAG, "onSuccess: " + result.getResult());
                }

            }
        });
    }

    /**
     * 初始化请求数据
     *
     * @param mGroupInfo
     * @param type
     * @return
     */
    public static Map<String, String> getRequestPost(GroupInfo mGroupInfo, int type) {
        String id = mGroupInfo.getId();
        Log.d(TAG, "getRequestPost: " + id);
        String[] split = id.split("#");
        String Groupid = split[1];
        Map<String, String> params = new HashMap<>();
        params.put(Constants.groupId, Groupid);
        params.put(Constants.TYPE, String.valueOf(type));
        params.put(Constants.nickname, TextUtils.isEmpty(mGroupInfo.getGroupName()) ? "" : mGroupInfo.getGroupName());
        params.put(Constants.USERID, UserInfo.getInstance().getUserId());
        params.put(Constants.TOKEN, UserInfo.getInstance().getToken());
        return params;
    }

    /**
     * 初始化请求数据
     *
     * @param mGroupInfo
     * @param type
     * @return
     */
    public static Map<String, String> getRequestPost(GroupInfo mGroupInfo, int type, List<String> addMembers) {
        String id = mGroupInfo.getId();
        String[] split = id.split("#");
        String Groupid = split[1];
        Map<String, String> params = new HashMap<>();
        params.put(Constants.groupId, Groupid);
        params.put(Constants.TYPE, String.valueOf(type));
        params.put(Constants.nickname, TextUtils.isEmpty(mGroupInfo.getGroupName()) ? "" : mGroupInfo.getGroupName());
        params.put(Constants.USERID, UserInfo.getInstance().getUserId());
        params.put(Constants.TOKEN, UserInfo.getInstance().getToken());
        params.put(Constants.TOUSERID, new Gson().toJson(addMembers));
        return params;
    }

    /**
     * 当前组状态禁言
     */
    public static void selectgroupchat(GroupInfo mGroupInfo, Postdeduction.Callback callback) {
        if (mGroupInfo == null) {
            return;
        }
        Map<String, String> params = getRequestPost(mGroupInfo, 0);
        Postdeduction.getapi(params, Postdeduction.groupchat, new Postdeduction.Callback() {
            @Override
            public void onFailed() {
                callback.onFall();
            }

            @Override
            public void onSuccess(String msg) {
                try {
                    Mesresult mesresult = new Gson().fromJson(msg, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Groupgroupchat groupgroupchat = new Gson().fromJson(mesresult.getData(), Groupgroupchat.class);
                        if (groupgroupchat.getType() == 1) {
                            callback.onSuccess();
                        } else {
                            callback.onFall();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 删除群组已解散下架
     */
    public static void desdeletegroup(GroupInfo mGroupInfo, Postdeduction.Callback callback) {
        if (mGroupInfo == null) {
            return;
        }
        Map<String, String> params = getRequestPost(mGroupInfo, 0);
        Postdeduction.getapi(params, Postdeduction.desdeletegroup, new Postdeduction.Callback() {
            @Override
            public void onFailed() {
                if (callback != null) {
                    callback.onFall();
                }
            }

            @Override
            public void onSuccess(String msg) {
                try {
                    Mesresult mesresult = new Gson().fromJson(msg, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        if (callback != null) {
                            callback.onSuccess();
                        }
                    } else {
                        if (callback != null) {
                            callback.onFall();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 服务器添加好友
     *
     * @param mGroupInfo
     * @param addMembers
     * @param callBack
     */
    public static void addgroupmember(GroupInfo mGroupInfo, List<String> addMembers, IUIKitCallBack callBack) {
        if (mGroupInfo == null) {
            return;
        }
        Map<String, String> params = getRequestPost(mGroupInfo, 0, addMembers);
        Postdeduction.getapi(params, Postdeduction.addgroupmember, new Postdeduction.Callback() {
            @Override
            public void onFailed() {
                if (callBack != null) {
                    callBack.onError("请求失败", 10007, "服务请求失败");
                }
            }

            @Override
            public void onSuccess(String msg) {
                try {
                    Groupresult groupresult = new Gson().fromJson(msg, Groupresult.class);
                    if (groupresult.getActionStatus().equals("OK") && groupresult.getErrorCode() == 0) {
                        callBack.onSuccess(groupresult);
                        Log.d(TAG, "onSuccess: " + groupresult.toString());
                    } else {
                        callBack.onError(groupresult.getActionStatus(), groupresult.getErrorCode(), msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 发送群聊自定义（信令）消息
     * https://im.sdk.qcloud.com/doc/zh-cn/classcom_1_1tencent_1_1imsdk_1_1v2_1_1V2TIMManager.html#afbce8ff97be0a3a42c7dc826d316f2c2
     *
     * @param mGroupInfo
     * @param text
     */
    public static void sendGroupCustomMessage(String text, GroupInfo mGroupInfo) {
        V2TIMManager.getInstance().sendGroupCustomMessage(text.getBytes(), mGroupInfo.getId(), V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {


            }
        });
    }

    /**
     * 发送群聊普通文本消息
     *
     * @param msg
     * @param mGroupInfo
     */
    public static void sendRoomTextMsg(String msg, GroupInfo mGroupInfo) {
        V2TIMManager.getInstance().sendGroupTextMessage(msg, mGroupInfo.getId(), V2TIMMessage.V2TIM_PRIORITY_NORMAL, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {

            }
        });

    }


}
