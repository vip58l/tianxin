package com.tianxin.activity.loadmatching.adapte;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.moxun.tagcloudlib.view.TagsAdapter;
import com.tianxin.R;
import com.tianxin.activity.loadmatching.model.StarModel;

import java.util.List;

/**
 * 3d星球View适配器
 */
public class TagCloudViewAdapter extends TagsAdapter {

    private final Context mContext;
    private final List<StarModel> mList;
    private final LayoutInflater inflater;

    public TagCloudViewAdapter(Context mContext, List<StarModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 返回Tag数量
     *
     * @return
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * 返回每个Tag实例
     *
     * @param context
     * @param position
     * @param parent
     * @return
     */
    @Override
    public View getView(Context context, int position, ViewGroup parent) {
        StarModel model = mList.get(position);
        View mView = null;
        ViewHolder viewHolder = null;
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_3d_item, null);
            viewHolder = new ViewHolder(mView);
            mView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) mView.getTag();
        }
        viewHolder.bain(model);

        return mView;
    }

    /**
     * 返回Tag数据
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    /**
     * 针对每个Tag返回一个权重值，该值与ThemeColor和Tag初始大小有关；
     * 一个简单的权重值生成方式是对一个数N取余或使用随机数
     *
     * @param position
     * @return
     */
    @Override
    public int getPopularity(int position) {
        return 7;
    }

    /**
     * Tag主题色发生变化时会回调该方法
     *
     * @param view
     * @param themeColor
     */
    @Override
    public void onThemeColorChanged(View view, int themeColor) {

    }

    private class ViewHolder {
        private final ImageView iv_star_icon;
        private final TextView tv_star_name;

        public ViewHolder(View mView) {
            iv_star_icon = mView.findViewById(R.id.iv_star_icon);
            tv_star_name = mView.findViewById(R.id.tv_star_name);
        }

        public void bain(StarModel model) {

            if (!TextUtils.isEmpty(model.getPhotoUrl())) {
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.ic_man_choose).error(R.mipmap.ic_man_choose);
                Glide.with(mContext).load(model.getPhotoUrl()).apply(options).into(iv_star_icon);
            } else {
                iv_star_icon.setImageResource(R.mipmap.ic_man_choose);
            }

            tv_star_name.setText(model.getNickName());
        }
    }

}
