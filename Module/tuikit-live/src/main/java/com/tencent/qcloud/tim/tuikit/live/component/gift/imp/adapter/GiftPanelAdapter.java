package com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.RecyclerViewController;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import java.util.List;

//RecyclerView适配器
public class GiftPanelAdapter extends RecyclerView.Adapter<GiftPanelAdapter.ViewHolder> {
    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private RecyclerViewController mRecyclerViewController;
    private final int mPageIndex;
    private final List<GiftInfo> mSelectGiftInfoList;
    private final List<GiftInfo> mGiftInfoList;
    private OnItemClickListener mOnItemClickListener;

    public GiftPanelAdapter(RecyclerView recyclerView, int pageIndex, List<GiftInfo> list, Context context, List<GiftInfo> selectGiftInfoList) {
        super();
        mRecyclerView = recyclerView;
        mGiftInfoList = list;
        mContext = context;
        mPageIndex = pageIndex;
        mSelectGiftInfoList = selectGiftInfoList;
        recyclerViewClickListener(list, mContext);
    }

    private void recyclerViewClickListener(final List<GiftInfo> list, Context mContext) {
        mRecyclerViewController = new RecyclerViewController(mContext, mRecyclerView);
        mRecyclerViewController.setOnItemClickListener(new RecyclerViewController.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                final GiftInfo giftModel = list.get(position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, giftModel, position, mPageIndex);
                }
                clearSelectState();
                giftModel.isSelected = true;
                mSelectGiftInfoList.add(giftModel);
                notifyDataSetChanged();
            }
        });
    }

    //取消选中
    private void clearSelectState() {
        for (GiftInfo giftInfo : mSelectGiftInfoList) {
            giftInfo.isSelected = false;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_recycle_item_gift_panel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GiftInfo giftInfo = mGiftInfoList.get(position);
        //图片
        GlideEngine.loadImage(holder.mImageGift, giftInfo.giftPicUrl);
//        标题
        holder.mTextGiftName.setText(giftInfo.title);
//        金额
        holder.mTextGiftPrice.setText(String.format(mContext.getString(R.string.live_gift_game_currency), giftInfo.price));
        if (giftInfo.isSelected) {
//            增加背景
            holder.mLayoutRootView.setBackgroundResource(R.drawable.live_gift_shape_normal);
             holder.mTextGiftName.setVisibility(View.GONE);
//            holder.mTextSendBtn.setVisibility(View.VISIBLE);
        } else {
            holder.mLayoutRootView.setBackground(null);
             holder.mTextGiftName.setVisibility(View.VISIBLE);
//            holder.mTextSendBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mGiftInfoList.size();
    }

    //刷新数据
    public void clearSelection(int pageIndex) {
        if (mPageIndex != pageIndex) {
            notifyDataSetChanged();
        }
    }

    //内部组件
    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayoutRootView;
        ImageView mImageGift;
        TextView mTextGiftName;
        TextView mTextGiftPrice;
        TextView mTextSendBtn;

        public ViewHolder(View view) {
            super(view);
            mLayoutRootView = view.findViewById(R.id.ll_gift_root);
            mImageGift = view.findViewById(R.id.iv_gift_icon);
            mTextGiftName = view.findViewById(R.id.tv_gift_name);
            mTextGiftPrice = view.findViewById(R.id.tv_gift_price);
            mTextSendBtn = view.findViewById(R.id.tv_send);
        }
    }

    //定义接口
    public interface OnItemClickListener {
        void onItemClick(View view, GiftInfo giftInfo, int position, int pageIndex);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}
