package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class mvidewloder20 extends BaseHolder {
    @BindView(R.id.item_dy_img_con)
    ImageView item_dy_img_con;
    @BindView(R.id.item_dy_tv_ms)
    TextView item_dy_tv_ms;
    @BindView(R.id.item_dy_img_nums)
    ImageView item_dy_img_nums;
    @BindView(R.id.item_dy_tv_nums)
    TextView item_dy_tv_nums;

    public mvidewloder20(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        videolist videolist = (com.tencent.opensource.model.videolist) object;
        ViewGroup.LayoutParams layoutParams = item_dy_img_con.getLayoutParams();
        layoutParams.height = 400;
        item_dy_img_con.setLayoutParams(layoutParams);
        item_dy_img_con.setPadding(10, 0, 10, 0);
        String bigpicurl = videolist.getBigpicurl();
        if (videolist.getTencent() == Constants.TENCENT) {
            bigpicurl = DemoApplication.presignedURL(bigpicurl);
        }
        Glideload.loadImage(item_dy_img_con, bigpicurl, 8);
        item_dy_tv_ms.setText(videolist.getTitle());
        item_dy_img_nums.setVisibility(View.GONE);
        item_dy_tv_nums.setVisibility(View.GONE);
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
