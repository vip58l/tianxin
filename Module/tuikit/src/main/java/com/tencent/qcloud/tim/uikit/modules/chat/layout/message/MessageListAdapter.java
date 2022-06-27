package com.tencent.qcloud.tim.uikit.modules.chat.layout.message;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IChatProvider;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.GroupMessageHelper;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IGroupMessageClickListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageContentHolder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageEmptyHolder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageHeaderHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天窗口消息内容适配器
 * RecyclerView.Adapter
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    public static final int MSG_TYPE_HEADER_VIEW = -99;
    private static final String TAG = MessageListAdapter.class.getSimpleName();
    private boolean mLoading = true;
    private MessageLayout mRecycleView;
    private List<MessageInfo> mDataSource = new ArrayList<>();
    private MessageLayout.OnItemClickListener mOnItemClickListener;
    private IOnCustomMessageDrawListener mOnCustomMessageDrawListener;
    private IGroupMessageClickListener mIGroupMessageClickListener;
    private MessageLayout.OnMessagegps onMessagegps;

    /*********************    传接接口调用方法 **************************************/
    public void setOnCustomMessageDrawListener(IOnCustomMessageDrawListener listener) {
        mOnCustomMessageDrawListener = listener;
    }

    public void setIGroupMessageClickListener(IGroupMessageClickListener IGroupMessageClickListener) {
        mIGroupMessageClickListener = IGroupMessageClickListener;
    }

    public void setOnItemClickListener(MessageLayout.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MessageLayout.OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public void setOnMessagegps(MessageLayout.OnMessagegps onMessagegps) {
        this.onMessagegps = onMessagegps;
    }

    public MessageLayout.OnMessagegps getOnMessagegpsListener() {
        return this.onMessagegps;
    }


    /*********************    传接接口调用方法结束 **************************************/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //设置聊天界面布局文件
        RecyclerView.ViewHolder holder = MessageEmptyHolder.Factory.getInstance(parent, this, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        //绑定显示的内容
        final MessageInfo msg = getItem(position);
        //实现抽像类
        MessageBaseHolder baseHolder = (MessageBaseHolder) holder;
        //传入item监听点击事件
        baseHolder.setOnItemClickListener(mOnItemClickListener);
        //传入item定位监听事件
        baseHolder.setOnMessagegps(onMessagegps);

        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case MSG_TYPE_HEADER_VIEW:
                ((MessageHeaderHolder) baseHolder).setLoadingStatus(mLoading);
                break;
            case MessageInfo.MSG_TYPE_TEXT:
            case MessageInfo.MSG_TYPE_IMAGE:
            case MessageInfo.MSG_TYPE_VIDEO:
            case MessageInfo.MSG_TYPE_CUSTOM_FACE:
            case MessageInfo.MSG_TYPE_AUDIO:
            case MessageInfo.MSG_TYPE_FILE:
            case MessageInfo.MSG_TYPE_LOCATION:
                break;

        }

        //设置实现抽像类消息加载
        baseHolder.layoutViews(msg, position);

        // 对于自定义消息，需要在正常布局之后，交给外部调用者重新加载渲染
        if (itemViewType == MessageInfo.MSG_TYPE_CUSTOM) {
            MessageCustomHolder customHolder = (MessageCustomHolder) holder;
            if (MessageInfoUtil.isLive(msg)) {
                new GroupMessageHelper(mIGroupMessageClickListener).onDraw(customHolder, msg);
            } else if (mOnCustomMessageDrawListener != null) {
                mOnCustomMessageDrawListener.onDraw(customHolder, msg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSource.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MSG_TYPE_HEADER_VIEW;
        }
        MessageInfo msg = getItem(position);
        msg.getMsgType();
        return msg.getMsgType();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //关于附加到RecyclerView的
        mRecycleView = (MessageLayout) recyclerView;
        mRecycleView.setItemViewCacheSize(5);
    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        //当前item被回收时调用，可用来释放绑定在view上的大数据，比方说Bitmap
        //可监听view的销毁
        if (holder instanceof MessageContentHolder) {
            ((MessageContentHolder) holder).msgContentFrame.setBackground(null);
        }
    }


    /**
     * 设置并刷新数据通知展示加载中
     */
    public void showLoading() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        notifyItemChanged(0);
    }

    /**
     * 设置并刷新数据通知
     *
     * @param type
     * @param value
     */
    public void notifyDataSourceChanged(final int type, final int value) {
        BackgroundTasks.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoading = false;
                if (type == MessageLayout.DATA_CHANGE_TYPE_REFRESH) {
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_ADD_BACK) {
                    notifyItemRangeInserted(mDataSource.size() + 1, value);
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_UPDATE) {
                    notifyItemChanged(value + 1);
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_LOAD || type == MessageLayout.DATA_CHANGE_TYPE_ADD_FRONT) {
                    //加载条目为数0，只更新动画
                    if (value == 0) {
                        notifyItemChanged(0);
                    } else {
                        //加载过程中有可能之前第一条与新加载的最后一条的时间间隔不超过5分钟，时间条目需去掉，所以这里的刷新要多一个条目
                        if (getItemCount() > value) {
                            notifyItemRangeInserted(0, value);
                        } else {
                            notifyItemRangeInserted(0, value);
                        }
                    }
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_DELETE) {
                    notifyItemRemoved(value + 1);
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                }
            }
        }, 100);
    }

    /**
     * 设置数据源并刷新通知
     *
     * @param provider
     */
    public void setDataSource(IChatProvider provider) {
        if (provider == null) {
            mDataSource.clear();
        } else {
            mDataSource = provider.getDataSource();
            //设置监听事件
            provider.setAdapter(this);
        }
        notifyDataSourceChanged(MessageLayout.DATA_CHANGE_TYPE_REFRESH, getItemCount());
    }

    public MessageInfo getItem(int position) {
        if (position == 0 || mDataSource.size() == 0) {
            return null;
        }
        MessageInfo info = mDataSource.get(position - 1);
        return info;
    }


    /*****************定义加入的 暂不需要处理 默认不用管它 2021-9-29 *******************/
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d(TAG, "onViewAttachedToWindow:  附加窗口");

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d(TAG, "onDetachedFromRecyclerView: 释放掉");
    }

    /*****************定义加入的 暂不需要处理 *******************/

}