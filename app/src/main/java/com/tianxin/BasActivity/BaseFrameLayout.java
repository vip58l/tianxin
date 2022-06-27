package com.tianxin.BasActivity;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.activity_item.fragment_load;
import com.tianxin.adapter.Radapter;
import com.tencent.opensource.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFrameLayout extends FrameLayout {
    public Datamodule datamodule;
    public Context context;
    public Activity activity;
    public UserInfo userInfo;
    public List<View> list = new ArrayList();
    public List<Object> mlist = new ArrayList();
    public List<Object> listobj = new ArrayList();
    public pageadapter banneradapter;
    public Radapter radapter;
    public String[] title;
    public int[] ivimgcon = {R.mipmap.aa22, R.mipmap.a22, R.mipmap.ic_praise_sm3, R.mipmap.aq5};
    public static final int A1 = 1;
    public static final int A2 = 2;
    public static final int A3 = 3;
    public static final int A4 = 4;

    /********** 自动广告轮播 *******************************/
    //初始页轮播
    public int preDotPosition = 0;
    //请求更新显示轮播
    public static final int MSG_UPDATE_IMAGE = 1;
    //请求暂停轮播
    public static final int MSG_KEEP_SILENT = 2;
    //请求恢复轮播
    public static final int MSG_BREAK_SILENT = 3;
    //轮播间隔时间
    public static final long MSG_DELAY = 3000;

    public BaseFrameLayout(@NonNull Context context) {
        super(context);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public abstract void init();


    public void tostartActivity(String videoUrl) {
        Intent intent = new Intent(context, DyWebActivity.class);
        intent.putExtra(Constants.VIDEOURL, videoUrl);
        intent.putExtra(Constants.JSON, "");
        startActivity(intent);
    }

    public void fragmentload(){
        Intent intent = new Intent(context, fragment_load.class);
        intent.putExtra(Constants.POSITION, 1);
        intent.putExtra(Constants.JSON, getContext().getString(R.string.play_videotitle));
        startActivity(intent);
    }
}
