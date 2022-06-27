package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupApplication;
import com.tencent.imsdk.v2.V2TIMGroupApplicationResult;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupManager;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupMemberOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupApplyInfo;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 参考资料
 * https://cloud.tencent.com/document/product/269/44498#.E7.BE.A4.E7.BB.84.E7.9B.B8.E5.85.B3.E6.8E.A5.E5.8F.A3
 */
public class GroupInfoProvider {

    private static final String TAG = GroupInfoProvider.class.getSimpleName();

    private static final int PAGE = 50;

    private GroupInfo mGroupInfo;
    private GroupMemberInfo mSelfInfo;
    private List<GroupMemberInfo> mGroupMembers = new ArrayList<>();
    private List<GroupApplyInfo> mApplyList = new ArrayList<>();

    private void reset() {
        mGroupInfo = new GroupInfo();
        mGroupMembers = new ArrayList<>();
        mSelfInfo = null;
    }

    public void loadGroupInfo(GroupInfo info) {
        mGroupInfo = info;
        mGroupMembers = info.getMemberDetails();
    }

    public void loadGroupInfo(final String groupId, final IUIKitCallBack callBack) {

        reset();

        // 串行异步加载群组信息
        loadGroupPublicInfo(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {

                // 设置群的一般信息，比如名称、类型等
                mGroupInfo.covertTIMGroupDetailInfo((V2TIMGroupInfoResult) data);

                // 设置是否为置顶聊天
                boolean isTop = ConversationManagerKit.getInstance().isTopConversation(groupId);
                mGroupInfo.setTopChat(isTop);

                // 异步获取群成员
                loadGroupMembers(0, callBack);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e(TAG, "loadGroupPublicInfo failed, code: " + errCode + "|desc: " + errMsg);
                if (callBack != null) {
                    callBack.onError(module, errCode, errMsg);
                }
            }
        });
    }

    public void deleteGroup(final IUIKitCallBack callBack) {
        V2TIMManager.getInstance().dismissGroup(mGroupInfo.getId(), new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                callBack.onError(TAG, code, desc);
                TUIKitLog.e(TAG, "deleteGroup failed, code: " + code + "|desc: " + desc);
            }

            @Override
            public void onSuccess() {
                callBack.onSuccess(null);
                ConversationManagerKit.getInstance().deleteConversation(mGroupInfo.getId(), true);
                GroupChatManagerKit.getInstance().onGroupForceExit();
            }
        });
    }

    /**
     * 加载内容
     *
     * @param groupId
     * @param callBack
     */
    public void loadGroupPublicInfo(String groupId, final IUIKitCallBack callBack) {

        V2TIMManager.getGroupManager().getGroupsInfo(Arrays.asList(groupId), new V2TIMValueCallback<List<V2TIMGroupInfoResult>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "loadGroupPublicInfo failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess(List<V2TIMGroupInfoResult> v2TIMGroupInfoResults) {
                if (v2TIMGroupInfoResults.size() > 0) {
                    V2TIMGroupInfoResult infoResult = v2TIMGroupInfoResults.get(0);
                    // TODO toString打印
                    TUIKitLog.i(TAG, infoResult.toString());
                    callBack.onSuccess(infoResult);
                }
            }
        });
    }

    /**
     * 加载数据
     *
     * @param nextSeq
     * @param callBack
     */
    public void loadGroupMembers(long nextSeq, final IUIKitCallBack callBack) {
        V2TIMManager.getGroupManager().getGroupMemberList(mGroupInfo.getId(), V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_FILTER_ALL, nextSeq, new V2TIMValueCallback<V2TIMGroupMemberInfoResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "loadGroupMembers failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess(V2TIMGroupMemberInfoResult v2TIMGroupMemberInfoResult) {
                List<GroupMemberInfo> members = new ArrayList<>();
                for (int i = 0; i < v2TIMGroupMemberInfoResult.getMemberInfoList().size(); i++) {
                    GroupMemberInfo member = new GroupMemberInfo();
                    members.add(member.covertTIMGroupMemberInfo(v2TIMGroupMemberInfoResult.getMemberInfoList().get(i)));
                }
                mGroupMembers.addAll(members);
                mGroupInfo.setMemberDetails(mGroupMembers);
                if (v2TIMGroupMemberInfoResult.getNextSeq() != 0) {
                    loadGroupMembers(v2TIMGroupMemberInfoResult.getNextSeq(), callBack);
                } else {
                    callBack.onSuccess(mGroupInfo);
                }
            }
        });
    }

    public List<GroupMemberInfo> getGroupMembers() {
        return mGroupMembers;
    }

    public void modifyGroupInfo(final Object value, final int type, final IUIKitCallBack callBack) {
        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
        v2TIMGroupInfo.setGroupID(mGroupInfo.getId());
        if (type == TUIKitConstants.Group.MODIFY_GROUP_NAME) {
            v2TIMGroupInfo.setGroupName(value.toString());
        } else if (type == TUIKitConstants.Group.MODIFY_GROUP_NOTICE) {
            v2TIMGroupInfo.setNotification(value.toString());
        } else if (type == TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE) {
            v2TIMGroupInfo.setGroupAddOpt((Integer) value);
        }
        V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.i(TAG, "modifyGroupInfo faild tyep| value| code| desc " + value + ":" + type + ":" + code + ":" + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess() {
                if (type == TUIKitConstants.Group.MODIFY_GROUP_NAME) {
                    mGroupInfo.setGroupName(value.toString());
                    mGroupInfo.setChatName(value.toString());

                } else if (type == TUIKitConstants.Group.MODIFY_GROUP_NOTICE) {
                    mGroupInfo.setNotice(value.toString());
                } else if (type == TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE) {
                    mGroupInfo.setJoinType((Integer) value);
                }
                callBack.onSuccess(value);
            }
        });


    }

    public void modifyMyGroupNickname(final String nickname, final IUIKitCallBack callBack) {
        if (mGroupInfo == null) {
            ToastUtil.toastLongMessage("修改我的组昵称 失败 没有找到该群");
        }

        V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo = new V2TIMGroupMemberFullInfo();
        v2TIMGroupMemberFullInfo.setUserID(V2TIMManager.getInstance().getLoginUser());
        v2TIMGroupMemberFullInfo.setNameCard(nickname);
        V2TIMManager.getGroupManager().setGroupMemberInfo(mGroupInfo.getId(), v2TIMGroupMemberFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                callBack.onError(TAG, code, desc);
                ToastUtil.toastLongMessage("修改我的组昵称 失败: " + code + "=" + desc);
            }

            @Override
            public void onSuccess() {
                callBack.onSuccess(null);
            }
        });
    }

    public GroupMemberInfo getSelfGroupInfo() {
        if (mSelfInfo != null) {
            return mSelfInfo;
        }
        for (int i = 0; i < mGroupMembers.size(); i++) {
            GroupMemberInfo memberInfo = mGroupMembers.get(i);
            if (TextUtils.equals(memberInfo.getUserid(), V2TIMManager.getInstance().getLoginUser())) {
                mSelfInfo = memberInfo;
                return memberInfo;
            }

        }
        return null;
    }

    public void setTopConversation(boolean flag) {
        ConversationManagerKit.getInstance().setConversationTop(mGroupInfo.getId(), flag);
    }

    public void quitGroup(final IUIKitCallBack callBack) {
        V2TIMManager.getInstance().quitGroup(mGroupInfo.getId(), new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "quitGroup failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess() {
                ConversationManagerKit.getInstance().deleteConversation(mGroupInfo.getId(), true);
                GroupChatManagerKit.getInstance().onGroupForceExit();
                callBack.onSuccess(null);
                reset();
            }
        });
    }

    /**
     * 批量邀请成员
     * 工作群（Work）：群里的任何人都可以邀请其他人进群。
     * 会议群（Meeting）和公开群（Public）：只有通过rest api 使用 App 管理员身份才可以邀请其他人进群。
     * 直播群（AVChatRoom）：不支持此功能。
     *
     * @param addMembers
     * @param callBack
     */
    public void inviteGroupMembers(List<String> addMembers, final IUIKitCallBack callBack) {
        if (addMembers == null || addMembers.size() == 0) {
            return;
        }

        /**
         * 会议群（Meeting）和公开群（Public）：只有通过rest
         */
        if (mGroupInfo.getType() == 2) {
            //通过服务端添加好友进群
            GroupUpdate.addgroupmember(mGroupInfo, addMembers, new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    final List<String> adds = new ArrayList<>();
                    Groupresult groupresult = (Groupresult) data;
                    List<MemberList> memberList = groupresult.getMemberList();
                    for (MemberList list : memberList) {
                        int result = list.getResult();
                        if (result == 1) {
                            adds.add(list.getMember_Account());
                        }
                    }
                    if (adds.size() > 0) {
                        loadGroupMembers(0, callBack);
                    } else {
                        callBack.onSuccess("好友已在群里");
                    }
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    callBack.onError(module, errCode, errMsg);
                }
            });
            return;
        }

        V2TIMManager.getGroupManager().inviteUserToGroup(mGroupInfo.getId(), addMembers, new V2TIMValueCallback<List<V2TIMGroupMemberOperationResult>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "addGroupMembers failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess(List<V2TIMGroupMemberOperationResult> v2TIMGroupMemberOperationResults) {
                final List<String> adds = new ArrayList<>();
                if (v2TIMGroupMemberOperationResults.size() > 0) {
                    for (int i = 0; i < v2TIMGroupMemberOperationResults.size(); i++) {
                        V2TIMGroupMemberOperationResult res = v2TIMGroupMemberOperationResults.get(i);
                        if (res.getResult() == V2TIMGroupMemberOperationResult.OPERATION_RESULT_PENDING) {
                            callBack.onSuccess(TUIKit.getAppContext().getString(R.string.request_wait));
                            return;
                        }
                        if (res.getResult() > 0) {
                            adds.add(res.getMemberID());
                        }
                    }
                }
                if (adds.size() > 0) {
                    loadGroupMembers(0, callBack);
                }
            }
        });
    }

    /**
     * 3.7 踢人
     *
     * @param delMembers
     * @param callBack
     */
    public void removeGroupMembers(List<GroupMemberInfo> delMembers, final IUIKitCallBack callBack) {
        if (delMembers == null || delMembers.size() == 0) {
            return;
        }
        List<String> members = new ArrayList<>();
        for (int i = 0; i < delMembers.size(); i++) {
            GroupMemberInfo groupMemberInfo = delMembers.get(i);
            members.add(groupMemberInfo.getUserid());
        }

        //工作群（Work）：只有群主或 APP 管理员可以踢人。
        //公开群（Public）、会议群（Meeting）：群主、管理员和 APP 管理员可以踢人
        //直播群（AVChatRoom）不支持踢人。
        V2TIMManager.getGroupManager().kickGroupMember(mGroupInfo.getId(), members, "", new V2TIMValueCallback<List<V2TIMGroupMemberOperationResult>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "removeGroupMembers failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess(List<V2TIMGroupMemberOperationResult> v2TIMGroupMemberOperationResults) {
                List<String> dels = new ArrayList<>();
                for (int i = 0; i < v2TIMGroupMemberOperationResults.size(); i++) {
                    V2TIMGroupMemberOperationResult res = v2TIMGroupMemberOperationResults.get(i);
                    if (res.getResult() == V2TIMGroupMemberOperationResult.OPERATION_RESULT_SUCC) {
                        dels.add(res.getMemberID());
                    }
                }
                for (int i = 0; i < dels.size(); i++) {
                    for (int j = mGroupMembers.size() - 1; j >= 0; j--) {
                        if (mGroupMembers.get(j).getUserid().equals(dels.get(i))) {
                            mGroupMembers.remove(j);
                            break;
                        }
                    }
                }
                mGroupInfo.setMemberDetails(mGroupMembers);
                callBack.onSuccess(dels);
            }
        });
    }

    /**
     * 群主或管理员也可以通过setGroupInfo接口对整个群进行禁言
     */
    public void setGroupInfo(boolean b, String msg) {
        V2TIMGroupInfo v2TIMGroupInfo = new V2TIMGroupInfo();
        v2TIMGroupInfo.setGroupID(mGroupInfo.getId());
        //v2TIMGroupInfo.setGroupType(mGroupInfo.getGroupType());
        //v2TIMGroupInfo.setGroupName(mGroupInfo.getGroupName());
        v2TIMGroupInfo.setAllMuted(b); //是否全员禁言
        //v2TIMGroupInfo.setNotification(msg);
        //v2TIMGroupInfo.setIntroduction("设置群简介，创建群和修改群信息都可以设置");
        //v2TIMGroupInfo.setGroupAddOpt(V2TIM_GROUP_ADD_ANY);//设置加群选项，创建群和修改群信息都可以设置
        V2TIMManager.getGroupManager().setGroupInfo(v2TIMGroupInfo, null);

        //向服务器写入禁言状态
        modifygroupbaseinfo(b);

    }

    /**
     * 暂时无用不到 备用 查询是否禁言状态
     */
    public void selectgroupchat(GroupInfoLayout layout) {
        GroupUpdate.selectgroupchat(mGroupInfo, new Postdeduction.Callback() {
            @Override
            public void onSuccess() {
                try {
                    layout.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout.forbidden_words.setChecked(true);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed() {

            }
        });


    }

    /**
     * 写到服务器禁状态
     */
    public void modifygroupbaseinfo(boolean b) {
        if (mGroupInfo == null || TextUtils.isEmpty(mGroupInfo.getId())) {
            return;
        }
        Map<String, String> params = GroupUpdate.getRequestPost(mGroupInfo, b ? 1 : 0);
        Postdeduction.getapi(params, Postdeduction.modifygroupbaseinfo, new Postdeduction.Callback() {
            @Override
            public void onFailed() {

            }

            @Override
            public void onSuccess(String msg) {
                try {
                    Groupresult groupresult = new Gson().fromJson(msg, Groupresult.class);
                    if (groupresult.getActionStatus().equals("OK")) {
                        Log.d(TAG, "onSuccess: " + msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 转让群主
     * 普通类型的群（Work、Public、Meeting）：只有群主才有权限进行群转让操作
     */
    public void transferGroupOwner(String userID) {
        V2TIMManager.getGroupManager().transferGroupOwner(mGroupInfo.getId(), userID, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    /**
     * 添加管理员切换群成员的角色
     * 只有群主才能对群成员进行普通成员和管理员之间的角色切换
     *
     * @param userID
     * @param role   切换的角色支持普通群成员（V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_MEMBER）和管理员（V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_ADMIN）
     */
    public void setGroupMemberRole(String userID, int role) {
        V2TIMManager.getGroupManager().setGroupMemberRole(mGroupInfo.getId(), userID, role, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    /**
     * 删除聊天记录
     */
    public void deleteConversation(V2TIMCallback callback) {
        V2TIMManager.getConversationManager().deleteConversation(String.format("group_%s", mGroupInfo.getId()), callback);
    }

    /**
     * 设置会话草稿
     */
    public void setConversationDraft(String msg) {
        V2TIMManager.getConversationManager().setConversationDraft(String.format("group_%s", mGroupInfo.getId()), msg, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    public List<GroupApplyInfo> getApplyList() {
        return mApplyList;
    }

    public void loadGroupApplies(final IUIKitCallBack callBack) {
        loadApplyInfo(new IUIKitCallBack() {

            @Override
            public void onSuccess(Object data) {
                if (mGroupInfo == null) {
                    callBack.onError(TAG, 0, "no groupInfo");
                    return;
                }
                String groupId = mGroupInfo.getId();
                List<GroupApplyInfo> allApplies = (List<GroupApplyInfo>) data;
                List<GroupApplyInfo> applyInfos = new ArrayList<>();
                for (int i = 0; i < allApplies.size(); i++) {
                    GroupApplyInfo applyInfo = allApplies.get(i);
                    if (groupId.equals(applyInfo.getGroupApplication().getGroupID())
                            && applyInfo.getGroupApplication().getHandleStatus() == V2TIMGroupApplication.V2TIM_GROUP_APPLICATION_HANDLE_STATUS_UNHANDLED) {
                        applyInfos.add(applyInfo);
                    }
                }
                mApplyList = applyInfos;
                callBack.onSuccess(applyInfos);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e(TAG, "loadApplyInfo failed, code: " + errCode + "|desc: " + errMsg);
                callBack.onError(module, errCode, errMsg);
            }
        });
    }

    private void loadApplyInfo(final IUIKitCallBack callBack) {
        final List<GroupApplyInfo> applies = new ArrayList<>();

        V2TIMManager.getGroupManager().getGroupApplicationList(new V2TIMValueCallback<V2TIMGroupApplicationResult>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "getGroupPendencyList failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess(V2TIMGroupApplicationResult v2TIMGroupApplicationResult) {
                List<V2TIMGroupApplication> v2TIMGroupApplicationList = v2TIMGroupApplicationResult.getGroupApplicationList();
                for (int i = 0; i < v2TIMGroupApplicationList.size(); i++) {
                    GroupApplyInfo info = new GroupApplyInfo(v2TIMGroupApplicationList.get(i));
                    info.setStatus(0);
                    applies.add(info);
                }
                callBack.onSuccess(applies);
            }
        });
    }

    public void acceptApply(final GroupApplyInfo item, final IUIKitCallBack callBack) {
        V2TIMManager.getGroupManager().acceptGroupApplication(item.getGroupApplication(), "", new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "acceptApply failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess() {
                item.setStatus(GroupApplyInfo.APPLIED);
                callBack.onSuccess(null);
            }
        });
    }

    public void refuseApply(final GroupApplyInfo item, final IUIKitCallBack callBack) {
        V2TIMManager.getGroupManager().refuseGroupApplication(item.getGroupApplication(), "", new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.e(TAG, "refuseApply failed, code: " + code + "|desc: " + desc);
                callBack.onError(TAG, code, desc);
            }

            @Override
            public void onSuccess() {
                item.setStatus(GroupApplyInfo.REFUSED);
                callBack.onSuccess(null);
            }
        });
    }

    /**
     * 群组相关
     */
    public void getGroupManager() {
//        V2TIMManager.getGroupManager().initGroupAttributes();
//        V2TIMManager.getGroupManager().setGroupAttributes();
//        V2TIMManager.getGroupManager().deleteGroupAttributes();
//        V2TIMManager.getGroupManager().getGroupAttributes();
//        V2TIMManager.getGroupManager().getGroupOnlineMemberCount();
//        V2TIMManager.getGroupManager().initGroupAttributes();
//        V2TIMManager.getGroupManager().getGroupMemberList();
//        V2TIMManager.getGroupManager().getGroupMembersInfo();
//        V2TIMManager.getGroupManager().setGroupMemberInfo();
//        V2TIMManager.getGroupManager().muteGroupMember();
//        V2TIMManager.getGroupManager().kickGroupMember();
//        V2TIMManager.getGroupManager().setGroupMemberRole();
//        V2TIMManager.getGroupManager().transferGroupOwner();
//        V2TIMManager.getGroupManager().inviteUserToGroup();
//        V2TIMManager.getGroupManager().getGroupApplicationList();
//        V2TIMManager.getGroupManager().acceptGroupApplication();
//        V2TIMManager.getGroupManager().setGroupApplicationRead();
    }
}
