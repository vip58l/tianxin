package com.tianxin.IMtencent.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.IMtencent.menu.ConversationLayoutHelper;
import com.tianxin.IMtencent.menu.Menu;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.ITitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.action.PopActionClickListener;
import com.tencent.qcloud.tim.uikit.component.action.PopDialogAdapter;
import com.tencent.qcloud.tim.uikit.component.action.PopMenuAction;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示会话列表
 */
public class ConversationFragment extends BaseFragment {
    private static final String TAG = ConversationFragment.class.getSimpleName();
    private SmartRefreshLayout smartRefreshLayout;
    private View mBaseView;
    private ConversationLayout mConversationLayout;
    private ListView mConversationPopList;           //聊天窗口顶置菜单
    private PopDialogAdapter mConversationPopAdapter;//聊天窗口顶置菜适配器
    private PopupWindow mConversationPopWindow;
    private final List<PopMenuAction> mConversationPopActions = new ArrayList<>();
    private Menu mMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.conversation_fragment, container, false);
        return mBaseView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        // 从布局文件中获取会话列表面板
        smartRefreshLayout = mBaseView.findViewById(R.id.smartRefreshLayout);
        mConversationLayout = mBaseView.findViewById(R.id.conversation_layout);
        mMenu = new Menu(getActivity(), (TitleBarLayout) mConversationLayout.getTitleBar(), Menu.MENU_TYPE_CONVERSATION);

        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault();

        // 通过API设置ConversataonLayout各种属性的样例，开发者可以打开注释，体验效果
        ConversationLayoutHelper.customizeConversation(mConversationLayout);
        //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
        mConversationLayout.getConversationList().setOnItemClickListener((view, position, conversationInfo) -> startChatActivity(conversationInfo));
        //长按点击事件
        mConversationLayout.getConversationList().setOnItemLongClickListener((View view, int position, ConversationInfo conversationInfo) -> startPopShow(view, position, conversationInfo));

        //设置会话Item头像圆角
        mConversationLayout.getConversationList().setItemAvatarRadius(10);

        //设置会话Item顶部字体大小
        mConversationLayout.getConversationList().setItemTopTextSize(16);

        //设置会话Item底部字体大小
        //mConversationLayout.getConversationList().setItemBottomTextSize(18);

        //mConversationLayout.getConversationList().getListLayout();
        //mConversationLayout.getConversationList().getAdapter();
        //mConversationLayout.getConversationList().disableItemUnreadDot(false);

        //头像导航 最新消息 图标导航 等...
        TitleBarLayout titleBar = mConversationLayout.getTitleBar();
        titleBar.setTitle(getString(R.string.titlenewmsg), ITitleBarLayout.POSITION.MIDDLE);
        titleBar.setVisibility(View.GONE);
        titleBar.getMiddleTitle().setVisibility(View.GONE);//隐藏 头部标题 最新消息
        titleBar.getRightGroup().setVisibility(View.GONE);

        //菜单显示或隐藏
        initTitleAction();
        //长按提示顶置删除
        initPopMenuAction();
        //刷新列表
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                initView();
            }
        });
        //上拉加载
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });

    }

    /**
     * 菜单显示或隐藏
     */
    private void initTitleAction() {
        mConversationLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMenu.isShowing()) {
                    mMenu.hide();
                } else {
                    mMenu.show();
                }
            }
        });
    }

    /**
     * 长按提示顶置删除
     */
    private void initPopMenuAction() {

        // 设置长按conversation显示PopAction
        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName(getResources().getString(R.string.chat_top));
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mConversationLayout.setConversationTop(position, (ConversationInfo) data);
            }
        });
        conversationPopActions.add(action);
        action = new PopMenuAction();
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mConversationLayout.deleteConversation(position, (ConversationInfo) data);
            }
        });
        action.setActionName(getResources().getString(R.string.chat_delete));
        conversationPopActions.add(action);
        mConversationPopActions.clear();
        mConversationPopActions.addAll(conversationPopActions);
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenuAction action = mConversationPopActions.get(position);
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener().onActionClick(index, conversationInfo);
                }
                mConversationPopWindow.dismiss();
            }
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals(getResources().getString(R.string.chat_top))) {
                    action.setActionName(getResources().getString(R.string.quit_chat_top));
                }
            } else {
                if (action.getActionName().equals(getResources().getString(R.string.quit_chat_top))) {
                    action.setActionName(getResources().getString(R.string.chat_top));
                }

            }
        }

        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, mBaseView, (int) locationX, (int) locationY);
        mBaseView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 10000); // 10s后无操作自动消失
    }

    /**
     * 长按会话item弹框
     *
     * @param view
     * @param position
     * @param info
     */
    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }

    /**
     * 打开聊天窗口
     *
     * @param conversationInfo
     */
    private void startChatActivity(ConversationInfo conversationInfo) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(conversationInfo.isGroup() ? V2TIMConversation.V2TIM_GROUP : V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());
        if (!conversationInfo.isGroup()) {
            chatInfo.setIconUrlList(conversationInfo.getIconUrlList().size() > 0 ? conversationInfo.getIconUrlList().get(0).toString() : "");
        }
        Intent intent = new Intent(DemoApplication.instance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
