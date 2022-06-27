package com.tencent.qcloud.tim.tuikit.live.component.message;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tencent.opensource.dialog.dialogfoll;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.util.ArrayList;

/**
 * 聊天列表信息滚动list
 */
public class ChatLayout extends LinearLayout implements AdapterView.OnItemClickListener {
    private static final String TAG = "TUILiveChatLayout";
    private Context mContext;
    private LinearLayout mLayoutRoot;
    private ListView mListIMMessage;
    private ChatMessageListAdapter mChatMsgListAdapter;
    private final ArrayList<ChatEntity> mArrayListChatEntity = new ArrayList<>();   // 消息列表集合
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public ChatLayout(Context context) {
        super(context);
        initView(context);
    }

    public ChatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChatLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutRoot = (LinearLayout) inflate(context, R.layout.live_layout_chat, this);
        mListIMMessage = mLayoutRoot.findViewById(R.id.lv_im_message);
        mListIMMessage.setOnItemClickListener(this);

        //设置聊天滚动适配器
        mChatMsgListAdapter = new ChatMessageListAdapter(context, mListIMMessage, mArrayListChatEntity);
        mListIMMessage.setAdapter(mChatMsgListAdapter);
    }

    //添加输入内容
    public void addMessageToList(final ChatEntity entity) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mArrayListChatEntity.size() > 1000) {
                    while (mArrayListChatEntity.size() > 900) {
                        mArrayListChatEntity.remove(0);
                    }
                }

                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ChatEntity chatEntity = mArrayListChatEntity.get(position);
        if (chatEntity == null || TextUtils.isEmpty(chatEntity.getUserid())) {
            return;
        }

        //展示其他人资料聊天列表信息滚动list
        //dialogfoll.mydialogfoll(mContext, chatEntity.getUserid(), null);


    }
}
