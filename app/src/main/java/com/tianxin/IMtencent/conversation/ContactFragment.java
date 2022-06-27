/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/21 0021
 */


package com.tianxin.IMtencent.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tianxin.IMtencent.FriendProfileActivity;
import com.tianxin.IMtencent.contact.BlackListActivity;
import com.tianxin.IMtencent.contact.GroupListActivity;
import com.tianxin.IMtencent.contact.NewFriendActivity;
import com.tianxin.IMtencent.menu.Menu;
import com.tianxin.IMtencent.utils.DemoLog;
import com.tianxin.R;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactLayout;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * 从布局文件中获取通讯录面板
 */
public class ContactFragment extends Fragment {
    String TAG = ContactFragment.class.getName();
    private ContactLayout mContactLayout;
    private Menu mMenu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View baseView = inflater.inflate(R.layout.contact_fragment, container, false);
        initViews(baseView);
        return baseView;
    }

    //通讯录
    private void initViews(View view) {
        mContactLayout = view.findViewById(R.id.contact_layout);
        mMenu = new Menu(getActivity(), mContactLayout.getTitleBar(), Menu.MENU_TYPE_CONTACT);
        mContactLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });

        //隐藏标题
        mContactLayout.getTitleBar().setVisibility(View.GONE);

        //通讯录
        mContactLayout.getContactListView().setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                if (position == 0) {
                    Intent intent = new Intent(DemoApplication.instance(), NewFriendActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(DemoApplication.instance(), GroupListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(DemoApplication.instance(), BlackListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                } else {
                    Intent intent = new Intent(DemoApplication.instance(), FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                   startActivity(intent);
                }
            }
        });
    }

    private void refreshData() {
        // 通讯录面板的默认UI和交互初始化
        mContactLayout.initDefault();
    }

    @Override
    public void onResume() {
        super.onResume();
        DemoLog.i(TAG, "onResume");
        refreshData();
    }
}
