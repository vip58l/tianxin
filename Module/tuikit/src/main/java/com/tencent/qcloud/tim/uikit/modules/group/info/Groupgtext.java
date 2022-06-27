package com.tencent.qcloud.tim.uikit.modules.group.info;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.tencent.opensource.model.info;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.component.SelectionActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.group.member.GroupMemberInviteLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

import java.io.Serializable;
import java.util.ArrayList;

public class Groupgtext extends BaseFragment {
    private View mBaseView;
    private GroupInfo mGroupInfo;
    private RadioGroup radioGroup;
    private EditText input;
    private int mSelectionType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.selection_activity, container, false);
        init();
        return mBaseView;
    }

    private void init() {
        final TitleBarLayout titleBar = mBaseView.findViewById(R.id.edit_title_bar);
        radioGroup = mBaseView.findViewById(R.id.content_list_rg);
        input = mBaseView.findViewById(R.id.edit_content_et);

        Bundle bundle = getArguments();
        mGroupInfo = (GroupInfo) bundle.getSerializable(TUIKitConstants.Group.GROUP_INFO);
        String defaultString = bundle.getString(TUIKitConstants.Selection.INIT_CONTENT);
        String userid = bundle.getString("userid");
        int limit = bundle.getInt(TUIKitConstants.Selection.LIMIT);
        if (!TextUtils.isEmpty(defaultString)) {
            input.setText(defaultString);
            input.setSelection(defaultString.length());
        }
        if (limit > 0) {
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(limit)});
        }
        final String title = bundle.getString(TUIKitConstants.Selection.TITLE);
        titleBar.setTitle(title, TitleBarLayout.POSITION.MIDDLE);
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backward();
            }
        });
        titleBar.getRightIcon().setVisibility(View.GONE);
        titleBar.getRightTitle().setText(getResources().getString(R.string.sure));
        titleBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = input.getText().toString().trim();
                if (TextUtils.isEmpty(s)) {
                    s = "0";
                }
                int i = (int) Double.parseDouble(s);
                if (i > 1576800000) {
                    i = 1576800000;
                }
                GroupInfoAdapter.muteGroupMembers(mGroupInfo.getId(), userid, TextUtils.isEmpty(s) ? 180 : i);
                backward();
            }
        });
    }
}
