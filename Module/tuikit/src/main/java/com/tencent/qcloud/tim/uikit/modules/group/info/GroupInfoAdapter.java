package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberInfo;
import com.tencent.qcloud.tim.uikit.modules.group.member.IGroupMemberRouter;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 群成员列表适配器
 */
public class GroupInfoAdapter extends BaseAdapter {

    private static final int ADD_TYPE = -100;
    private static final int DEL_TYPE = -101;
    private static final int OWNER_PRIVATE_MAX_LIMIT = 10;  //讨论组,owner可以添加成员和删除成员，
    private static final int OWNER_PUBLIC_MAX_LIMIT = 11;   //公开群,owner不可以添加成员，但是可以删除成员
    private static final int OWNER_CHATROOM_MAX_LIMIT = 11; //聊天室,owner不可以添加成员，但是可以删除成员
    private static final int NORMAL_PRIVATE_MAX_LIMIT = 11; //讨论组,普通人可以添加成员
    private static final int NORMAL_PUBLIC_MAX_LIMIT = 12;  //公开群,普通人没有权限添加成员和删除成员
    private static final int NORMAL_CHATROOM_MAX_LIMIT = 12; //聊天室,普通人没有权限添加成员和删除成员
    private static final String TAG = "GroupInfoAdapter";

    private final List<GroupMemberInfo> mGroupMembers = new ArrayList<>();
    private IGroupMemberRouter mTailListener;
    private GroupInfo mGroupInfo;

    public void setManagerCallBack(IGroupMemberRouter listener) {
        mTailListener = listener;
    }

    @Override
    public int getCount() {
        return mGroupMembers.size();
    }

    @Override
    public GroupMemberInfo getItem(int i) {
        return mGroupMembers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        MyViewHolder holder;
        if (view == null) {
            //聊天列表头像和名称
            view = LayoutInflater.from(TUIKit.getAppContext()).inflate(R.layout.group_member_adpater, viewGroup, false);
            holder = new MyViewHolder();
            holder.memberIcon = view.findViewById(R.id.group_member_icon);
            holder.memberName = view.findViewById(R.id.group_member_name);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }
        holder.bind(view, getItem(i));
        return view;
    }

    public void setDataSource(GroupInfo info) {
        mGroupInfo = info;
        mGroupMembers.clear();
        List<GroupMemberInfo> members = info.getMemberDetails();
        if (members != null) {
            int shootMemberCount = 0;
            if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_PRIVATE) || TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_WORK)) {
                if (info.isOwner()) {
                    shootMemberCount = members.size() > OWNER_PRIVATE_MAX_LIMIT ? OWNER_PRIVATE_MAX_LIMIT : members.size();
                } else {
                    shootMemberCount = members.size() > NORMAL_PRIVATE_MAX_LIMIT ? NORMAL_PRIVATE_MAX_LIMIT : members.size();
                }
            } else if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_PUBLIC)) {
                if (info.isOwner()) {
                    shootMemberCount = members.size() > OWNER_PUBLIC_MAX_LIMIT ? OWNER_PUBLIC_MAX_LIMIT : members.size();
                } else {
                    shootMemberCount = members.size() > NORMAL_PUBLIC_MAX_LIMIT ? NORMAL_PUBLIC_MAX_LIMIT : members.size();
                }
            } else if (TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_CHAT_ROOM)
                    || TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_MEETING)) {
                if (info.isOwner()) {
                    shootMemberCount = members.size() > OWNER_CHATROOM_MAX_LIMIT ? OWNER_CHATROOM_MAX_LIMIT : members.size();
                } else {
                    shootMemberCount = members.size() > NORMAL_CHATROOM_MAX_LIMIT ? NORMAL_CHATROOM_MAX_LIMIT : members.size();
                }
            }

            for (int i = 0; i < shootMemberCount; i++) {
                mGroupMembers.add(members.get(i));
            }

            if (TextUtils.equals(info.getGroupType(),
                    TUIKitConstants.GroupType.TYPE_PRIVATE)
                    || TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_WORK)
                    || TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_PUBLIC)
                    || TextUtils.equals(info.getGroupType(), TUIKitConstants.GroupType.TYPE_MEETING)) {
                // 公开群/聊天室 只有APP管理员可以邀请他人入群
                GroupMemberInfo add = new GroupMemberInfo();
                add.setMemberType(ADD_TYPE);
                mGroupMembers.add(add);
            }
            GroupMemberInfo self = null;
            for (int i = 0; i < mGroupMembers.size(); i++) {
                GroupMemberInfo memberInfo = mGroupMembers.get(i);
                if (TextUtils.equals(memberInfo.getAccount(), V2TIMManager.getInstance().getLoginUser())) {
                    self = memberInfo;
                    break;
                }
            }
            if (info.isOwner() || (self != null && self.getMemberType() == V2TIMGroupMemberFullInfo.V2TIM_GROUP_MEMBER_ROLE_ADMIN)) {
                GroupMemberInfo del = new GroupMemberInfo();
                del.setMemberType(DEL_TYPE);
                mGroupMembers.add(del);
            }
            BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

    }

    private class MyViewHolder {
        private ImageView memberIcon;
        private TextView memberName;

        public void bind(View view, GroupMemberInfo info) {
            if (!TextUtils.isEmpty(info.getIconUrl())) {
                GlideEngine.loadImage(memberIcon, info.getIconUrl(), null);
                memberIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (muteGroupMember != null) {
                            //设置禁言时长
                            muteGroupMember.muteGroupMember(mGroupInfo.getId(), info.getUserid(), 180);
                        }
                    }
                });
            }
            if (!TextUtils.isEmpty(info.getAccount())) {
                memberName.setText(info.getAccount()); //昵称
            } else if (!TextUtils.isEmpty(info.getFriendRemark())) {
                memberName.setText(info.getFriendRemark());
            } else {
                memberName.setText(info.getUserid());
            }

            //添加成员或移除成员
            view.setOnClickListener(null);
            memberIcon.setBackground(null);

            Log.d(TAG, "bind: " + info.getMemberType());

            if (info.getMemberType() == ADD_TYPE) {
                memberIcon.setImageResource(R.drawable.add_group_member);
                memberIcon.setBackgroundResource(R.drawable.bottom_action_border);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTailListener != null) {
                            mTailListener.forwardAddMember(mGroupInfo);
                        }
                    }
                });

            } else if (info.getMemberType() == DEL_TYPE) {
                memberIcon.setImageResource(R.drawable.del_group_member);
                memberIcon.setBackgroundResource(R.drawable.bottom_action_border);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTailListener != null) {
                            mTailListener.forwardDeleteMember(mGroupInfo);
                        }
                    }
                });
            }

        }

    }

    /**
     * 设置禁言
     *
     * @param groupID
     * @param userID
     * @param var3
     */
    public static void muteGroupMembers(String groupID, String userID, int var3) {
        V2TIMManager.getGroupManager().muteGroupMember(groupID, userID, var3, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                ToastUtil.toastLongMessage("onError: " + i + s);
                Log.d(TAG, "onError: " + i + s);
            }

            @Override
            public void onSuccess() {
                if (var3 == 0) {
                    ToastUtil.toastLongMessage(TUIKit.getAppContext().getString(R.string.tv_msg_t2));
                } else {
                    ToastUtil.toastLongMessage(TUIKit.getAppContext().getString(R.string.tv_msg_t1));
                }
            }
        });
    }

    public interface muteGroupMember {
        void muteGroupMember(String groupID, String userID, int var3);

    }

    private muteGroupMember muteGroupMember;

    public void setMuteGroupMember(GroupInfoAdapter.muteGroupMember muteGroupMember) {
        this.muteGroupMember = muteGroupMember;
    }
}
