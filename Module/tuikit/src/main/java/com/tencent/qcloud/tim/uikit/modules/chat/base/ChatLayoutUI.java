package com.tencent.qcloud.tim.uikit.modules.chat.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

public abstract class ChatLayoutUI extends LinearLayout implements IChatLayout {
    protected NoticeLayout mGroupApplyLayout;
    protected View mRecordingGroup;
    protected ImageView mRecordingIcon;
    protected TextView mRecordingTips;
    private TitleBarLayout mTitleBar;
    private MessageLayout mMessageLayout; //内容展示消息列表list
    private InputLayout mInputLayout;     //自定义底部按钮控件
    private NoticeLayout mNoticeLayout;
    private ChatInfo mChatInfo;           //对方详情资料
    private TextView mChatAtInfoLayout;
    private RelativeLayout myalignParentBottom;

    public ChatLayoutUI(Context context) {
        super(context);
        initViews();
    }

    public ChatLayoutUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ChatLayoutUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.chat_layout, this);
        mTitleBar = findViewById(R.id.chat_title_bar);
        mMessageLayout = findViewById(R.id.chat_message_layout);
        mInputLayout = findViewById(R.id.chat_input_layout);
        mInputLayout.setChatLayout(this); //把 ChatLayoutUI 传进去
        mRecordingGroup = findViewById(R.id.voice_recording_view);
        mRecordingIcon = findViewById(R.id.recording_icon);
        mRecordingTips = findViewById(R.id.recording_tips);
        mGroupApplyLayout = findViewById(R.id.chat_group_apply_layout);
        mNoticeLayout = findViewById(R.id.chat_notice_layout);
        mChatAtInfoLayout = findViewById(R.id.chat_at_text_view);
        myalignParentBottom = findViewById(R.id.myalignParentBottom);
        init();
    }

    protected void init() {

    }

    @Override
    public InputLayout getInputLayout() {
        return mInputLayout;
    }

    @Override
    public MessageLayout getMessageLayout() {
        return mMessageLayout;
    }

    @Override
    public NoticeLayout getNoticeLayout() {
        return mNoticeLayout;
    }

    @Override
    public ChatInfo getChatInfo() {
        return mChatInfo;
    }

    @Override
    public TextView getAtInfoLayout() {
        return mChatAtInfoLayout;
    }

    @Override
    public void setChatInfo(ChatInfo chatInfo) {
        mChatInfo = chatInfo;
        mInputLayout.setChatInfo(chatInfo);
        if (chatInfo == null) {
            return;
        }

        //设置标题名称
        String chatTitle = chatInfo.getChatName();
        getTitleBar().setTitle(chatTitle, TitleBarLayout.POSITION.MIDDLE);
    }

    @Override
    public void exitChat() {

    }

    @Override
    public void initDefault() {

    }

    @Override
    public void loadMessages() {

    }

    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {

    }

    @Override
    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {

    }
}
