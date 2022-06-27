package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;


public class GroupInfoPresenter {

    private static final String TAG = "GroupInfoPresenter";
    private final GroupInfoLayout mInfoLayout;
    private final GroupInfoProvider mProvider;

    public GroupInfoPresenter(GroupInfoLayout layout) {
        this.mInfoLayout = layout;
        mProvider = new GroupInfoProvider();
    }

    /**
     * 加载群组成员信息
     *
     * @param groupId
     * @param callBack
     */
    public void loadGroupInfo(String groupId, final IUIKitCallBack callBack) {
        mProvider.loadGroupInfo(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                callBack.onSuccess(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("loadGroupInfo", errCode + ":" + errMsg);
                callBack.onError(module, errCode, errMsg);
                ToastUtil.toastLongMessage(module + errCode + errMsg);
            }
        });
    }

    /**
     * 修改组名
     *
     * @param name
     */
    public void modifyGroupName(final String name) {
        mProvider.modifyGroupInfo(name, TUIKitConstants.Group.MODIFY_GROUP_NAME, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(name, TUIKitConstants.Group.MODIFY_GROUP_NAME);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                switch (errCode) {
                    case 10007:
                        ToastUtil.toastLongMessage("你没有权修改群组名称");
                        break;
                    default:
                        ToastUtil.toastLongMessage(errCode + ":" + errMsg);
                        break;
                }


            }
        });
    }

    /**
     * 修改团体通知
     *
     * @param notice
     */
    public void modifyGroupNotice(final String notice) {
        mProvider.modifyGroupInfo(notice, TUIKitConstants.Group.MODIFY_GROUP_NOTICE, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(notice, TUIKitConstants.Group.MODIFY_GROUP_NOTICE);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("modifyGroupNotice", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public String getNickName() {
        String nickName = "";
        if (mProvider.getSelfGroupInfo() != null) {
            nickName = mProvider.getSelfGroupInfo().getNameCard();
            if (TextUtils.isEmpty(nickName)) {
                nickName = mProvider.getSelfGroupInfo().getAccount();
            } else if (TextUtils.isEmpty(nickName)) {
                nickName = mProvider.getSelfGroupInfo().getUserid();
            }
        }
        return nickName == null ? "" : nickName;
    }

    /**
     * 修改我的组昵称
     *
     * @param nickname
     */
    public void modifyMyGroupNickname(final String nickname) {
        mProvider.modifyMyGroupNickname(nickname, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(nickname, TUIKitConstants.Group.MODIFY_MEMBER_NAME);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("modifyMyGroupNickname", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    /**
     * 您确认解散该群?
     */
    public void deleteGroup() {
        mProvider.deleteGroup(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ((Activity) mInfoLayout.getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("deleteGroup", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errCode + " " + errMsg);
            }
        });
    }

    /**
     * 机顶盒对话
     *
     * @param flag
     */
    public void setTopConversation(boolean flag) {
        mProvider.setTopConversation(flag);
    }

    /**
     * 退出组
     */
    public void quitGroup() {
        mProvider.quitGroup(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ((Activity) mInfoLayout.getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ((Activity) mInfoLayout.getContext()).finish();
                TUIKitLog.e("quitGroup", errCode + ":" + errMsg);
            }
        });
    }

    /**
     * 修改组信息
     *
     * @param value
     * @param type
     */
    public void modifyGroupInfo(int value, int type) {
        mProvider.modifyGroupInfo(value, type, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(data, TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("modifyGroupInfo fail :" + module + errCode + "=" + errMsg);
            }
        });
    }

    /**
     * 修改群资料
     * 群主或管理员也可以通过setGroupInfo接口对整个群进行禁言
     */
    public void setGroupInfo(boolean b, String msg) {
        mProvider.setGroupInfo(b, msg);
    }

    public GroupInfoProvider getmProvider() {
        return mProvider;
    }

    /**
     * 暂时无用不到 备用 查询是否禁言状态
     * @param layout
     */
    public void selectgroupchat(GroupInfoLayout layout){
        mProvider.selectgroupchat(layout);
    }


}
