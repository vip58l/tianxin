package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;

/**
 * 直播播放器PK和视频调整
 */
public class LiveVideoManagerLayout extends ConstraintLayout implements LiveAnchorOfflineView.AnchorOfflineCallback {

    private static final String TAG = "LiveVideoManagerLayout";
    private Context mContext;
    private TRTCLiveRoom mLiveRoom;
    public ConstraintLayout mLayoutRoot;
    private TXCloudVideoView mVideoViewAnchor;
    private LiveVideoView mVideoViewPKAnchor;
    private RelativeLayout mLayoutPKContainer;
    private ConstraintLayout mLayoutLinkContainer;
    private ImageView mImagePkLayer;
    private TextView tv_pk;
    private LiveAnchorOfflineView mLiveAnchorOfflineView;
    private final ArrayList<LiveVideoView> mLinkMicVideoViewList = new ArrayList<>();
    private VideoManagerLayoutDelegate mVideoManagerLayoutDelegate;

    private int mRoomStatus = TRTCLiveRoomDef.ROOM_STATUS_NONE;

    public interface VideoManagerLayoutDelegate {
        void onClose();
    }

    public LiveVideoManagerLayout(Context context) {
        super(context);
        initView(context);
    }

    public LiveVideoManagerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LiveVideoManagerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setVideoManagerLayoutDelegate(LiveVideoManagerLayout.VideoManagerLayoutDelegate videoManagerLayoutDelegate) {
        mVideoManagerLayoutDelegate = videoManagerLayoutDelegate;
    }

    private void initView(Context context) {
        mContext = context;
        mLiveRoom = TRTCLiveRoom.sharedInstance(context.getApplicationContext());
        inflate(context, R.layout.live_layout_live_video_manager, this);
        mLayoutRoot = findViewById(R.id.cl_video_manager);
        mVideoViewAnchor = findViewById(R.id.video_view_anchor);
        mLayoutPKContainer = findViewById(R.id.rl_pk_container);
        mLayoutLinkContainer = findViewById(R.id.cl_link_mic_container);
        mImagePkLayer = findViewById(R.id.iv_pk_layer);
        tv_pk = findViewById(R.id.tv_pk);
        mLiveAnchorOfflineView = findViewById(R.id.layout_anchor_offline);
        mLiveAnchorOfflineView.setAnchorOfflineCallback(this);
        mLinkMicVideoViewList.add((LiveVideoView) findViewById(R.id.video_link_mic_audience_1));
        mLinkMicVideoViewList.add((LiveVideoView) findViewById(R.id.video_link_mic_audience_2));
        mLinkMicVideoViewList.add((LiveVideoView) findViewById(R.id.video_link_mic_audience_3));
    }

    public void updateRoomStatus(int roomStatus) {
        // 主播进入PK
        if (roomStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
            mImagePkLayer.setVisibility(View.VISIBLE);                  //PK图片显示
            tv_pk.setVisibility(View.VISIBLE);                          //PK图片显示
            updateAnchorVideoView(true);
            LiveVideoView videoView = getPKAnchorVideoView();
            videoView.removeView(videoView.getPlayerVideo());           //移除大播放器
            mLayoutPKContainer.addView(videoView.getPlayerVideo());     //加入小的播放器
        }

        // 主播退出PK
        if (roomStatus != TRTCLiveRoomDef.ROOM_STATUS_PK && mRoomStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
            mImagePkLayer.setVisibility(GONE);                              //PK图片隐藏
            tv_pk.setVisibility(GONE);                                       //PK图片隐藏
            updateAnchorVideoView(false);
            LiveVideoView videoView = getPKAnchorVideoView();
            if (mLayoutPKContainer.getChildCount() != 0) {
                mLayoutPKContainer.removeView(videoView.getPlayerVideo());  //移除播放器
                videoView.addView(videoView.getPlayerVideo());             //重新加载大播放器
                mVideoViewPKAnchor = null;
            }
        }
        mRoomStatus = roomStatus;
    }

    public void startAnchorVideo(@NonNull String userId, boolean isOwnerAnchor, final TRTCLiveRoomCallback.ActionCallback callback) {
        TUILiveLog.d(TAG, "userId: " + userId + " isOwnerAnchor: " + isOwnerAnchor + " mRoomStatus: " + mRoomStatus);
        if (isOwnerAnchor) {
            setAnchorOfflineViewVisibility(View.GONE);
            mLiveRoom.startPlay(userId, mVideoViewAnchor, new TRTCLiveRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    if (callback != null) {
                        callback.onCallback(code, msg);
                    }
                }
            });
            return;
        }

        LiveVideoView videoView = applyVideoView(userId);
        if (videoView == null) {
            //主播端连麦人数超过最大限制
            Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        videoView.showKickoutBtn(false);
        mLiveRoom.startPlay(userId, videoView.getPlayerVideo(), null);
    }

    public void stopAnchorVideo(@NonNull String userId, boolean isOwnerAnchor, final TRTCLiveRoomCallback.ActionCallback callback) {
        if (isOwnerAnchor) {
            mLiveRoom.stopPlay(userId, null);
            setAnchorOfflineViewVisibility(View.VISIBLE);
            mVideoViewAnchor.setVisibility(GONE);
            return;
        }

        // 这里PK也会回收，但是没关系，因为我们有保护
        recycleVideoView(userId);
        mLiveRoom.stopPlay(userId, null);
    }

    /**
     * 设置主播背景
     *
     * @param coverUrl
     */
    public void updateAnchorOfflineViewBackground(String coverUrl) {
        if (mLiveAnchorOfflineView != null && !TextUtils.isEmpty(coverUrl)) {
            mLiveAnchorOfflineView.setImageBackground(coverUrl);
        }
    }

    public void setAnchorOfflineViewVisibility(int visibility) {
        if (mLiveAnchorOfflineView != null) {
            mLiveAnchorOfflineView.setVisibility(visibility);
        }
    }

    public synchronized LiveVideoView applyVideoView(String userId) {
        if (userId == null) {
            return null;
        }

        if (mVideoViewPKAnchor != null) {
            mVideoViewPKAnchor.setUsed(true);
            mVideoViewPKAnchor.showKickoutBtn(false);
            mVideoViewPKAnchor.userId = userId;
            return mVideoViewPKAnchor;
        }

        for (LiveVideoView item : mLinkMicVideoViewList) {
            if (!item.isUsed) {
                item.setUsed(true);
                item.userId = userId;
                return item;
            } else {
                if (item.userId != null && item.userId.equals(userId)) {
                    item.setUsed(true);
                    return item;
                }
            }
        }
        return null;
    }

    public synchronized void recycleVideoView(String userId) {
        for (LiveVideoView item : mLinkMicVideoViewList) {
            if (item.userId != null && item.userId.equals(userId)) {
                item.userId = null;
                item.setUsed(false);
            }
        }
    }

    private synchronized LiveVideoView getPKAnchorVideoView() {
        if (mVideoViewPKAnchor != null) {
            return mVideoViewPKAnchor;
        }
        boolean foundUsed = false;
        for (LiveVideoView item : mLinkMicVideoViewList) {
            if (item.isUsed) {
                foundUsed = true;
                mVideoViewPKAnchor = item;
                break;
            }
        }
        if (!foundUsed) {
            mVideoViewPKAnchor = mLinkMicVideoViewList.get(0);
        }
        return mVideoViewPKAnchor;
    }

    /**
     * 根据PK状态，动态调整主播视图位置、高度
     *
     * @param isPKStatus
     */
    private void updateAnchorVideoView(boolean isPKStatus) {
        if (isPKStatus) {
            ConstraintSet set = new ConstraintSet();
            set.clone(mLayoutRoot);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.TOP, R.id.rl_pk_container, ConstraintSet.TOP);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.START, R.id.cl_video_manager, ConstraintSet.START);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.BOTTOM, R.id.rl_pk_container, ConstraintSet.BOTTOM);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.END, R.id.gl_vertical, ConstraintSet.END);
            set.applyTo(mLayoutRoot);

        } else {
            ConstraintSet set = new ConstraintSet();
            set.clone(mLayoutRoot);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            set.applyTo(mLayoutRoot);
        }

        // 在悬浮窗模式，根据PK状态动态调整悬浮窗大小；
        int windowMode = FloatWindowLayout.getInstance().mWindowMode;
        if (windowMode == Constants.WINDOW_MODE_FLOAT) {
            int screenWidth = UIUtil.getScreenWidth(mContext);
            if (isPKStatus) {
                FloatWindowLayout.FloatWindowRect rect = new FloatWindowLayout.FloatWindowRect(screenWidth - 800, 0, 800, 600);
                FloatWindowLayout.getInstance().updateFloatWindowSize(rect);
            } else {
                FloatWindowLayout.FloatWindowRect rect = new FloatWindowLayout.FloatWindowRect(screenWidth - 400, 0, 400, 600);
                FloatWindowLayout.getInstance().updateFloatWindowSize(rect);
            }
            mImagePkLayer.setVisibility(View.GONE);
            tv_pk.setVisibility(View.GONE);
        }

    }


    /**
     * 根据窗口状态，动态调整PK Container的Margin距离
     * 根据窗口状态，动态调整主播不在背景的关闭按钮显示
     */
    public void updateVideoLayoutByWindowStatus() {
        int windowMode = FloatWindowLayout.getInstance().mWindowMode;
        // 根据窗口状态，动态调整PK Container的Margin距离
        ConstraintSet set = new ConstraintSet();
        set.clone(mLayoutRoot);
        if (windowMode == Constants.WINDOW_MODE_FLOAT) {
            set.setMargin(R.id.rl_pk_container, ConstraintSet.TOP, 0);
            set.connect(R.id.rl_pk_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            set.setMargin(R.id.cl_link_mic_container, ConstraintSet.BOTTOM, 10);
        } else {
            set.setMargin(R.id.rl_pk_container, ConstraintSet.TOP, 300);
            set.setMargin(R.id.cl_link_mic_container, ConstraintSet.BOTTOM, 200);
        }
        set.applyTo(mLayoutRoot);
        // 根据窗口状态，动态调整主播不在背景的关闭按钮显示
        // mLiveAnchorOfflineView.setCloseBtnVisibility(windowMode == Constants.WINDOW_MODE_FLOAT ? View.GONE : View.VISIBLE);
        mLiveAnchorOfflineView.setCloseBtnVisibility(View.GONE);

        // 根据窗口状态，
        if (windowMode == Constants.WINDOW_MODE_FLOAT) {
            int screenWidth = UIUtil.getScreenWidth(mContext);
            if (mRoomStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
                FloatWindowLayout.FloatWindowRect rect = new FloatWindowLayout.FloatWindowRect(screenWidth - 800, 0, 800, 600);
                FloatWindowLayout.getInstance().updateFloatWindowSize(rect);
                mImagePkLayer.setVisibility(View.GONE);
                tv_pk.setVisibility(View.GONE);
            } else {
                FloatWindowLayout.FloatWindowRect rect = new FloatWindowLayout.FloatWindowRect(screenWidth - 400, 0, 400, 600);
                FloatWindowLayout.getInstance().updateFloatWindowSize(rect);
            }
        }
    }


    @Override
    public void onClose() {
        if (mVideoManagerLayoutDelegate != null) {
            mVideoManagerLayoutDelegate.onClose();
        }
    }
}
