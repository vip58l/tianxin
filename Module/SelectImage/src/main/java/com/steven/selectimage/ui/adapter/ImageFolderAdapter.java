package com.steven.selectimage.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.steven.selectimage.R;
import com.steven.selectimage.widget.recyclerview.CommonRecycleAdapter;
import com.steven.selectimage.widget.recyclerview.CommonViewHolder;
import com.steven.selectimage.model.ImageFolder;

import java.util.List;

/**
 * Description:
 * Data：9/4/2018-3:14 PM
 *
 * @author yanzhiwen
 */
public class ImageFolderAdapter extends CommonRecycleAdapter<ImageFolder> {
    private final Context mContext;

    public ImageFolderAdapter(Context context, List<ImageFolder> imageFolders, int layoutId) {
        super(context, imageFolders, layoutId);
        this.mContext = context;
    }

    @Override
    protected void convert(CommonViewHolder holder, ImageFolder imageFolder, int potion) {
        holder.setText(R.id.tv_folder_name, imageFolder.getName())
                .setText(R.id.tv_size, imageFolder.getImages().size() + "张");
        ImageView iv_folder = holder.getView(R.id.iv_folder);
        Glide.with(mContext)
                .load(imageFolder.getAlbumPath())
                .into(iv_folder);

    }
}
