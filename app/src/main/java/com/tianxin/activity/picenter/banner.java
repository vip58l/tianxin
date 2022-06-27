package com.tianxin.activity.picenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.perimg;

import java.util.List;

/**
 * 广告图片滚动处理
 */
public class banner extends Handler {
    String TAG = banner.class.getSimpleName();

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

    private ViewPager viewPager;
    private List<View> list;
    private Context context;

    public banner(Context context, ViewPager viewPager, List<View> list) {
        this.context = context;
        this.viewPager = viewPager;
        this.list = list;
    }

    public enum State {EXPANDED, COLLAPSED, IDLE}

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        //比如连续发送N条数据处理
        if (hasMessages(MSG_UPDATE_IMAGE)) {
            removeMessages(MSG_UPDATE_IMAGE);
        }
        switch (msg.what) {
            case MSG_UPDATE_IMAGE:
                //到送最后页
                if (viewPager.getCurrentItem() == list.size() - 1) {
                    //回到初始页
                    viewPager.setCurrentItem(preDotPosition);
                } else {
                    //页下滚动翻下页
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                //请求更新显示轮播 3秒后滚动
                sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_KEEP_SILENT:
                //请求暂停轮播
                break;
            case MSG_BREAK_SILENT:
                //请求恢复轮播 等待3秒+3秒后滚动 3+3=6秒后轮播
                sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
        }
    }

    /**
     * 清除滚动
     */
    public void removeCallback() {
        removeMessages(MSG_UPDATE_IMAGE);
        removeCallbacksAndMessages(null);
    }

    /**
     * 请求暂停轮播
     */
    public void sendEmptyMessage() {
        if (list.size() > 1) {
            sendEmptyMessage(MSG_KEEP_SILENT);
        }
    }

    /**
     * 请求暂停轮播
     */
    public void onPause() {
        if (list.size() > 1) {
            sendEmptyMessage(MSG_KEEP_SILENT);
        }
    }

    /**
     * 请求恢复轮播
     */
    public void onRestart() {
        if (list.size() > 1) {
            sendEmptyMessageDelayed(MSG_BREAK_SILENT, MSG_DELAY);
        }
    }

    /**
     * 请求恢复轮播
     */
    public void sendEmptyMessageDelayed() {
        if (list.size() > 1) {
            sendEmptyMessageDelayed(MSG_BREAK_SILENT, MSG_DELAY);
        }
    }


    /**
     * 返回头像对像
     *
     * @param member
     * @return
     */
    public perimg newimage(member member) {
        perimg image = new perimg();
        image.setId(String.valueOf(member.getId()));
        image.setUserid(String.valueOf(member.getId()));
        image.setBgpic(member.getPicture());
        image.setPic(member.getPicture());
        image.setTencent(member.getTencent());
        return image;
    }

    /**
     * 默认头像
     *
     * @param member
     * @param TYPE
     * @return
     */
    public View newimage(member member, int TYPE) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glideload.loadImage(image, member.getSex() == 1 ? R.mipmap.a1 : R.mipmap.a2);
        return image;
    }
}
