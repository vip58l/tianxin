package com.steven.selectimage.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.steven.selectimage.R;
import com.steven.selectimage.model.Image;
import com.steven.selectimage.widget.recyclerview.CommonRecycleAdapter;
import com.steven.selectimage.widget.recyclerview.CommonViewHolder;
import com.steven.selectimage.widget.recyclerview.OnAddPicturesListener;
import com.steven.selectimage.widget.recyclerview.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器 传递给父类->抽像类
 */
public class SelectedImageAdapter extends CommonRecycleAdapter<Image> {
    private final Context mContext;
    private OnItemClickListener mItemClickListener;

    public SelectedImageAdapter(Context context, List<Image> data, int layoutId) {
        super(context, data, R.layout.selected_image_item);
        this.mContext = context;
    }

    @Override
    protected void convert(CommonViewHolder holder, Image image, int position) {
        ImageView ivThum = holder.getView(R.id.iv_selected_image);
        ImageView ivAdd = holder.getView(R.id.iv_add);
        RelativeLayout show_layout = holder.getView(R.id.show_layout);
        ImageView deltaRelative = holder.getView(R.id.deltaRelative);

        if (TextUtils.isEmpty(image.getPath())) {
            //item添加按钮+
            ivThum.setVisibility(View.GONE);
            show_layout.setVisibility(View.GONE);
            ivAdd.setVisibility(View.VISIBLE);
            ivAdd.setOnClickListener(v -> mItemClickListener.onAdd());   //添加更多图片回调
        } else {
            //item普通显示
            ivThum.setVisibility(View.VISIBLE);
            show_layout.setVisibility(View.VISIBLE);
            ivAdd.setVisibility(View.GONE);
            ivThum.setOnClickListener(v -> mItemClickListener.onItemClick(position)); //浏览图片
            deltaRelative.setOnClickListener(v -> mItemClickListener.delonItemClick(position));  //删除图片
        }
        Glide.with(mContext).load(image.getPath()).into(ivThum);
    }

    public void setmItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}