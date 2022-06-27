package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfoAdapter;
import com.tencent.qcloud.tim.uikit.modules.group.info.Groupgtext;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * 群成员管理
 */
public class GroupMemberManagerFragment extends BaseFragment {

    private GroupMemberManagerLayout mMemberLayout;
    private View mBaseView;
    private GroupInfo mGroupInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.group_fragment_members, container, false);
        mMemberLayout = mBaseView.findViewById(R.id.group_member_grid_layout);
        init();
        return mBaseView;
    }

    private void init() {
        mGroupInfo = (GroupInfo) getArguments().getSerializable(TUIKitConstants.Group.GROUP_INFO);
        mMemberLayout.setDataSource(mGroupInfo);
        mMemberLayout.getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        mMemberLayout.setRouter(new IGroupMemberRouter() {
            @Override
            public void TimeMember(GroupInfo info, String userid) {
                Groupgtext fragment = new Groupgtext();
                Bundle bundle = new Bundle();
                bundle.putString(TUIKitConstants.Selection.TITLE, TUIKit.getAppContext().getResources().getString(R.string.group_no_chat));
                bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, String.valueOf(180));
                bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                bundle.putString("userid",userid);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardListMember(GroupInfo info) {

            }

            @Override
            public void forwardAddMember(GroupInfo info) {
                GroupMemberInviteFragment fragment = new GroupMemberInviteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }

            @Override
            public void forwardDeleteMember(GroupInfo info) {
                GroupMemberDeleteFragment fragment = new GroupMemberDeleteFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(TUIKitConstants.Group.GROUP_INFO, info);
                fragment.setArguments(bundle);
                forward(fragment, false);
            }
        });

    }
}
