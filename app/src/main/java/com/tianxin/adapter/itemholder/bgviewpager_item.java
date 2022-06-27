package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.DensityUtil;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.ShareDate;

import butterknife.BindView;

public class bgviewpager_item extends BaseHolder {
    @BindView(R.id.backstage)
    ImageView backstage;
    @BindView(R.id.code)
    ImageView code;
    @BindView(R.id.userid)
    TextView userid;

    public bgviewpager_item(Context content, ViewGroup parent) {
        super(LayoutInflater.from(content).inflate(R.layout.item_share_backstage, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        ShareDate shareDate1 = (ShareDate) object;
        ImageLoadHelper.glideShowCornerImageWithUrl(context, shareDate1.getT_img_path(), backstage);
        Bitmap bitmap = createCode(context, shareDate1.getShareUrl());
        code.setImageBitmap(bitmap);
        userid.setText(userInfo.getUserId());
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }

    /**
     * 生成二维码1
     */
    public static Bitmap createCode(Context context, String codeUrl) {
        try {
            int width = DensityUtil.dp3px(context, 160);
            int height = DensityUtil.dp3px(context, 160);
            final Bitmap codeBitmap = DensityUtil.createQRImage(codeUrl, width, height);
            return codeBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
