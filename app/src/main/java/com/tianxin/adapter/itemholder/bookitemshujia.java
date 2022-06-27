package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BasitemBaseAdapter;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class bookitemshujia extends BasitemBaseAdapter {
    @BindView(R.id.item_sex_img)
    ImageView item_sex_img;
    @BindView(R.id.item_sex_tv_title)
    TextView item_sex_tv_title;
    @BindView(R.id.item_sex_tv_desc)
    TextView item_sex_tv_desc;
    @BindView(R.id.item_sex_tv_author)
    TextView item_sex_tv_author;

    public bookitemshujia(View itemview) {
        super(itemview);
    }

    public static View view(Context context) {
        return View.inflate(context, R.layout.book_item_shujia, null);
    }

    @Override
    public void bind(Object object, int position) {

    }

    @Override
    public void bind(Object object, int position, Paymnets paymnets) {

    }

    @Override
    public void bind(Context context, Object object, int position, Paymnets paymnets) {

    }

    @Override
    public void bind(Context context, Object object, int position, int ii, Paymnets paymnets, AMapLocation aMapLocation) {

    }

    public void bind(Object object) {
        videolist video = (videolist) object;
        String path = gnedURL(video.getTencent(), video.getBigpicurl());
        ImageLoadHelper.glideShowCornerImageWithUrl(context, path, item_sex_img);
        item_sex_tv_title.setText(video.getTitle());
        item_sex_tv_desc.setText(video.getAlias());
        item_sex_tv_author.setText(String.format("%s %s", context.getString(R.string.tv_msg149) + video.getTime(), context.getString(R.string.tv_msg148) + Config.stampToDate(video.getDadetime(), 0)));
    }

    public static String gnedURL(int type, String url) {
        try {
            return type == Constants.TENCENT ? DemoApplication.presignedURL(url) : url;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}

