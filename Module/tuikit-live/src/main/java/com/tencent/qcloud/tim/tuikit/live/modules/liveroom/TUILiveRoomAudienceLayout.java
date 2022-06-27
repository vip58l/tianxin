package com.tencent.qcloud.tim.tuikit.live.modules.liveroom;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.LiveRoomAudienceFragment;

/**
 * 观众进入房间1
 */
public class TUILiveRoomAudienceLayout extends FrameLayout {
    private String msg;
    private static final String TAG = TUILiveRoomAudienceLayout.class.getSimpleName();
    private FragmentManager mFragmentManager;
    private LiveRoomAudienceFragment mLiveRoomAudienceFragment;

    public TUILiveRoomAudienceLayout(@NonNull Context context) {
        super(context);
        inidate();
    }

    public TUILiveRoomAudienceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inidate();
    }

    private void inidate() {
        inflate(getContext(), R.layout.live_layout_live_room_audience, this);
        mLiveRoomAudienceFragment = new LiveRoomAudienceFragment();
    }


    /**
     * 通过 roomId 初始化观众端
     *
     * @param fragmentManager 用于管理fragment，activity 请通过 getSupportFragmentManager() 传入
     * @param roomId          观众端会自动进入该房间
     * @param anchorId
     * @param useCdn          是否使用CDN进行播放
     * @param cdnURL          CDN 播放链接，您可以在直播[控制台](https://console.cloud.tencent.com/live) 配置您的播放域名。
     */
    public void initWithRoomId(FragmentManager fragmentManager, int roomId, String anchorId, boolean useCdn, String cdnURL) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ROOM_ID, roomId);
        bundle.putString(Constants.ANCHOR_ID, anchorId);
        bundle.putBoolean(Constants.USE_CDN, useCdn);
        bundle.putString(Constants.CDN_URL, cdnURL);
        bundle.putString(Constants.MSG, msg);
        mLiveRoomAudienceFragment.setArguments(bundle);
        mFragmentManager = fragmentManager;
        mFragmentManager.beginTransaction().add(R.id.live_audience_container, mLiveRoomAudienceFragment, "tuikit-live-audience-fragment").commit();
    }

    /**
     * 初始评论内容
     *
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 设置 UI 回调接口
     * 设置监听处理
     * @param liveRoomAudienceDelegate
     */
    public void setLiveRoomAudienceDelegate(TUILiveRoomAudienceDelegate liveRoomAudienceDelegate) {
        mLiveRoomAudienceFragment.setLiveRoomAudienceDelegate(liveRoomAudienceDelegate);
    }


    /**
     * 请在 Activity 的 onBackPress 函数中调用该函数，会主动结束房间
     */
    public void onBackPressed() {
        if (mLiveRoomAudienceFragment != null) {
            mLiveRoomAudienceFragment.onBackPressed();
        }
    }

    //自定义接口
    public interface TUILiveRoomAudienceDelegate {
        /**
         * 点击界面中的关闭按钮等会回调该通知，可以在Activity中调用finish方法
         */
        void onClose();

        /**
         * UI 组件内部产生错误会通过该接口回调出来
         *
         * @param errorCode 错误码
         * @param message   错误信息
         */
        void onError(int errorCode, String message);

        /**
         * 回调礼物处理
         */
        void onGiftItemClick();

        /**
         * 回调充值购买金币
         */
        void onChargeClick();
    }
}
