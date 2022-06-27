package com.tianxin.adapter.setAdapterholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.app.DemoApplication;
import com.tianxin.getlist.HotRooms;

import butterknife.BindView;

public class item_view_one extends BaseItem {
    @BindView(R.id.nmun)
    TextView nmun;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bgsimg)
    ImageView bgsimg;
    @BindView(R.id.bg_shade_bom)
    ImageView bg_shade_bom;
    @BindView(R.id.circleimageview)
    ImageView circleimageview;

    public item_view_one(View itemview) {
        super(itemview);
    }

    public static View view(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_view_one, null);
    }

    @Override
    public void bind(Object object) {
        HotRooms hotRooms2 = (HotRooms) object;
        name.setText(hotRooms2.getNickName());
        title.setText(hotRooms2.getSign());
        nmun.setText(String.format("%s人气", Config.random()));
        ImageLoadHelper.glideShowCornerImageWithUrl(DemoApplication.instance(), hotRooms2.getBigImageOriginal(), bgsimg);
        ImageLoadHelper.glideShowImageWithUrl(DemoApplication.instance(), hotRooms2.getBigImageOriginal(), circleimageview);
    }

}

