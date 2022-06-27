package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BasitemBaseAdapter;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.getlist.minivideo;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;

public class dyv6 extends BasitemBaseAdapter {

    @BindView(R.id.item_dy_img_con)
    ImageView item_dy_img_con;
    @BindView(R.id.bg_shade_bom)
    ImageView bg_shade_bom;
    @BindView(R.id.item_dy_tv_ms)
    TextView item_dy_tv_ms;
    @BindView(R.id.item_dy_tv_nums)
    TextView item_dy_tv_nums;

    public static View view(Context context) {
        return View.inflate(context, R.layout.item_dy, null);
    }

    public dyv6(View itemview) {
        super(itemview);
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
        minivideo minivideo = (com.tianxin.getlist.minivideo) object;
        ImageLoadHelper.glideShowCornerImageWithUrl(context, minivideo.bigpicurl, item_dy_img_con);
        item_dy_tv_ms.setText(minivideo.title);
        item_dy_tv_nums.setText(minivideo.znum);
    }
}
