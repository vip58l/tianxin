/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/27 0027
 */


package com.tianxin.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tianxin.R;
import com.tencent.opensource.model.UserInfo;

public class headimage extends FrameLayout {
    private TextView name, title;
    public ImageView icon;

    public headimage(@NonNull Context context) {
        super(context);
        init();
    }

    public headimage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public headimage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public headimage(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_edit_msg0, this);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        title = findViewById(R.id.title);

        UserInfo userInfo = UserInfo.getInstance();
        setIcon(userInfo.getSex().equals("1") ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            setIcon(userInfo.getAvatar());
        }

        String app_name = getContext().getString(R.string.app_name);
        String userId = userInfo.getUserId();
        String format = String.format(getContext().getString(R.string.tv_msg115), app_name, userId);
        //name.setText(format);
        title.setVisibility(GONE);
    }

    public void setName(String msg) {
        name.setText(msg);
    }

    public void setTitle(String stitle) {
        title.setText(stitle);
    }

    public void setIcon(int icon) {
        this.icon.setImageResource(icon);
    }

    public void setIcon(String icon) {
        Glide.with(getContext()).load(icon).into(this.icon);
    }

    public void setIcon(Bitmap icon) {
        this.icon.setImageBitmap(icon);
    }


}
