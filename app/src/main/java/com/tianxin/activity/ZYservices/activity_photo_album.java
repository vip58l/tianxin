/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/24 0024
 */


package com.tianxin.activity.ZYservices;

import android.view.View;
import android.widget.ImageView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.widget.itembackTopbr;

import butterknife.BindView;

/**
 * 图片浏览放大缩小比酌
 */
public class activity_photo_album extends BasActivity2 {
    @BindView(R.id.photo_view)
    ImageView zoomImageview;
    @BindView(R.id.itemback)
    itembackTopbr itemback;

    @Override
    protected int getview() {
        return R.layout.activity_coursetype;
    }

    @Override
    public void iniview() {
        itemback.settitle("相册图片");
        Glideload.loadImage(zoomImageview,getIntent().getStringExtra(Constants.PATHVIDEO),4);
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }
}
