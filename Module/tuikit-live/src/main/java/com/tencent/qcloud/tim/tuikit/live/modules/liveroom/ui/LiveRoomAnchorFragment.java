package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.liteav.audiosettingkit.AudioEffectPanel;
import com.tencent.liteav.demo.beauty.model.BeautyInfo;
import com.tencent.liteav.demo.beauty.utils.BeautyUtils;
import com.tencent.liteav.demo.beauty.view.BeautyPanel;
import com.tencent.opensource.dialog.dialogfoll;
import com.tencent.opensource.listener.Callback;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.BaseFragment;
import com.tencent.qcloud.tim.tuikit.live.base.Config;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.bottombar.BottomToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.common.SelectMemberView;
import com.tencent.qcloud.tim.tuikit.live.component.countdown.CountDownTimerView;
import com.tencent.qcloud.tim.tuikit.live.component.countdown.ICountDownTimerView;
import com.tencent.qcloud.tim.tuikit.live.component.danmaku.DanmakuManager;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapterImp;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftAnimatorLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoDataHandler;
import com.tencent.qcloud.tim.tuikit.live.component.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.component.like.HeartLayout;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatLayout;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXUserInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.base.TXUserListCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.impl.room.impl.TXRoomService;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.ExitConfirmDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LinkMicListDialog;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoView;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.VideoViewController;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import master.flame.danmaku.controller.IDanmakuView;

import static com.tencent.qcloud.tim.tuikit.live.base.Constants.MAX_LINK_MIC_SIZE;
import static com.tencent.qcloud.tim.tuikit.live.base.Constants.REQUEST_PK_TIME_OUT;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.ANCHOR_HEART_COUNT;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.LIVE_TOTAL_TIME;
import static com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.FinishDetailDialog.TOTAL_AUDIENCE_COUNT;

/**
 * 实现了直播间的主播端界面2
 */
public class LiveRoomAnchorFragment extends BaseFragment {
    private String msg;
    private static final String TAG = LiveRoomAnchorFragment.class.getName();
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private ConstraintLayout mRootView;                     //面板管理
    private BeautyPanel mLayoutBeautyPanel;                 //美化面板
    private TXCloudVideoView mVideoViewAnchor;              //视频播面板
    private ImageView mImagePkLayer;                        //PK图片
    private TextView tv_pk;                                 //PK图片
    private AudioEffectPanel mAnchorAudioPanel;             //音频面板
    private LiveRoomPreviewLayout mLayoutPreview;           //预览页的Layout
    private VideoViewController mTUIVideoViewController;    //视频面板
    private AlertDialog mErrorDialog;                       //弹窗面板
    private TopToolBarLayout mLayoutTopToolBar;             //头像面板
    private BottomToolBarLayout mBottomToolBarLayout;       //底部面板
    private ChatLayout mChatLayout;                         //聊天滚动
    private IDanmakuView mDanmakuView;                      //负责弹幕的显示
    private DanmakuManager mDanmakuManager;                 //弹幕的管理类
    private TRTCLiveRoom mLiveRoom;                         //房间面板
    private TXCloudVideoView mVideoViewPKAnchor;            //视频播器
    private RelativeLayout mLayoutPKContainer;              //PK内容
    private SelectMemberView mSelectMemberView;             //用于选取邀请人
    private TextView mBottomStopPkBtn;                      //PK按键
    private Group mGroupAfterLive;                          //管理组1
    private Group mGroupButtomView;                         //按键管理组2
    private HeartLayout mHeartLayout;                       //飘心动画界面布局类
    private GiftAnimatorLayout mGiftAnimatorLayout;         //礼物动画和礼物弹幕的显示
    private TextView mStatusTipsView;                       //状态提示view
    private CircleImageView mButtonInvitationPk;            //邀请pk按钮
    private LinkMicListDialog mLinkMicListDialog;           //用于展示等待连麦的观众
    private AlertDialog mRoomPKRequestDialog;               //弹窗管理
    private ImageView mImageRedDot;                         //点击图片
    private CountDownTimerView mCountDownTimerView;         //开播倒计时动画View
    private TUILiveRoomAnchorLayoutDelegate mLiveRoomAnchorLayoutDelegate; //接口监听事件
    private GiftAdapter mGiftAdapter;                       //查询礼物信息
    private GiftInfoDataHandler mGiftInfoDataHandler;       //礼物处理
    private final TRTCLiveRoomDef.TRTCLiveRoomInfo mRoomInfo = new TRTCLiveRoomDef.TRTCLiveRoomInfo();
    private final TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
    private final List<String> mAnchorUserIdList = new ArrayList<>();
    private Runnable mGetAudienceRunnable;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private long mTotalMemberCount = 0;            //总进房观众数量
    private final long mCurrentMemberCount = 0;    //当前观众数量
    private long mHeartCount = 0;                  //点赞数量
    private long mStartTime = 0;                   //主播开始直播时间
    private String mOwnerUserId;
    private int mRoomId;
    private String mRoomName;
    private boolean mIsEnterRoom;
    private int mCurrentStatus = TRTCLiveRoomDef.ROOM_STATUS_NONE; //人数房间人数
    private boolean mIsPkStatus;                           //当前正在pk
    private boolean mIsLinkMicStatus;                      //当前正在连麦
    private SVGAImageView svgaImageView;                   //直播动画播放组件
    private SVGAParser parser;                             //动画状态
    private LinkedList<String> mAnimationUrlList;          //动画链接
    private boolean mIsPlaying;                            //是否正在播放

    /**
     * 相关直播接口回调监听处理
     */
    private final TRTCLiveRoomDelegate mTRTCLiveRoomDelegate = new TRTCLiveRoomDelegate() {
        @Override
        public void onError(int code, String message) {
            TUILiveLog.e(TAG, "onError: " + code + " " + message);
            if (mLiveRoomAnchorLayoutDelegate != null) {
                mLiveRoomAnchorLayoutDelegate.onError(mRoomInfo, code, message);
            }
        }

        @Override
        public void onWarning(int code, String message) {

        }

        @Override
        public void onDebugLog(String message) {

        }

        @Override
        public void onRoomInfoChange(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {
            //房间信息改变的通知
            int oldStatus = mCurrentStatus;
            mCurrentStatus = roomInfo.roomStatus;
            setAnchorViewFull(mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK);
            if (oldStatus == TRTCLiveRoomDef.ROOM_STATUS_PK && mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                // 上一个状态是PK，需要将界面中的元素恢复
                mBottomStopPkBtn.setVisibility(View.VISIBLE);
                mBottomStopPkBtn.setText(R.string.live_wait_link);
                judgeRedDotShow();
                LiveVideoView videoView = mTUIVideoViewController.getPKUserView();
                mVideoViewPKAnchor = videoView.getPlayerVideo();
                if (mLayoutPKContainer.getChildCount() != 0) {
                    mLayoutPKContainer.removeView(mVideoViewPKAnchor);
                    videoView.addView(mVideoViewPKAnchor);
                    mTUIVideoViewController.clearPKView();
                    mVideoViewPKAnchor = null;
                }
                mImagePkLayer.setVisibility(View.GONE);
                tv_pk.setVisibility(View.GONE);
                mButtonInvitationPk.setEnabled(true);
                mIsPkStatus = false;
            } else if (oldStatus == TRTCLiveRoomDef.ROOM_STATUS_LINK_MIC && mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_LINK_MIC) {
                //连麦
                mIsLinkMicStatus = false;
            } else if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
                // 本次状态是PK，需要将一个PK的view挪到右上角
                mImagePkLayer.setVisibility(View.VISIBLE);
                tv_pk.setVisibility(View.VISIBLE);
                mBottomStopPkBtn.setVisibility(View.VISIBLE);
                judgeRedDotShow();
                mBottomStopPkBtn.setText(R.string.live_stop_pk);
                LiveVideoView videoView = mTUIVideoViewController.getPKUserView();
                videoView.showKickoutBtn(false);
                mVideoViewPKAnchor = videoView.getPlayerVideo();
                videoView.removeView(mVideoViewPKAnchor);
                mLayoutPKContainer.addView(mVideoViewPKAnchor);
                mButtonInvitationPk.setEnabled(false);
            }
        }

        @Override
        public void onRoomDestroy(String roomId) {

        }

        @Override
        public void onAnchorEnter(final String userId) {
            if (getContext() == null) {
                TUILiveLog.d(TAG, "getContext is null!");
                return;
            }
            mAnchorUserIdList.add(userId);
            final LiveVideoView view = mTUIVideoViewController.applyVideoView(userId);
            if (view == null) {
                //主播端连麦人数超过最大限制
                Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
                return;
            }
            if (mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                view.startLoading();
            }
            mLiveRoom.startPlay(userId, view.getPlayerVideo(), new TRTCLiveRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    if (code == 0) {
                        Log.d(TAG, userId + "");
                        if (mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK) {
                            view.stopLoading(true);
                        }
                    }
                }
            });
        }

        @Override
        public void onAnchorExit(String userId) {
            mAnchorUserIdList.remove(userId);
            mLiveRoom.stopPlay(userId, null);
            mTUIVideoViewController.recycleVideoView(userId);
        }

        @Override
        public void onAudienceEnter(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            mTotalMemberCount++;
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(getString(R.string.live_user_join_live, userInfo.userId));
            } else {
                entity.setContent(getString(R.string.live_user_join_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_ENTER);
            entity.setUserid(userInfo.userId);
            updateIMMessageList(entity);
            addAudienceListLayout(userInfo);
        }

        @Override
        public void onAudienceExit(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(getString(R.string.live_user_quit_live, userInfo.userId));
            } else {
                entity.setContent(getString(R.string.live_user_quit_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_EXIT);
            entity.setUserid(userInfo.userId);
            updateIMMessageList(entity);
            removeAudienceListLayout(userInfo);
            mLinkMicListDialog.removeMemberEntity(userInfo.userId);
            judgeRedDotShow();
        }

        @Override
        public void onRequestJoinAnchor(final TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String reason) {
            if (getContext() == null) {
                TUILiveLog.d(TAG, "getContext is null!");
                return;
            }

            if (mAnchorUserIdList != null && mAnchorUserIdList.size() >= MAX_LINK_MIC_SIZE) {
                mLiveRoom.responseJoinAnchor(userInfo.userId, false, getString(R.string.live_warning_link_user_max_limit));
                return;
            }
            LinkMicListDialog.MemberEntity memberEntity = new LinkMicListDialog.MemberEntity();
            memberEntity.userId = userInfo.userId;
            memberEntity.userAvatar = userInfo.avatarUrl;
            memberEntity.userName = userInfo.userName;
            mLinkMicListDialog.addMemberEntity(memberEntity);
            judgeRedDotShow();
            Toast.makeText(getContext(), getContext().getString(R.string.live_request_link_mic, userInfo.userName), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAudienceRequestJoinAnchorTimeout(String userId) {
            mLinkMicListDialog.removeMemberEntity(userId);
            judgeRedDotShow();
        }

        @Override
        public void onAudienceCancelRequestJoinAnchor(String userId) {
            mLinkMicListDialog.removeMemberEntity(userId);
            judgeRedDotShow();
        }

        @Override
        public void onKickoutJoinAnchor() {

        }

        @Override
        public void onRequestRoomPK(final TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            //弹出是否接收PK跳战
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setTitle(R.string.live_tips)
                    .setMessage(getString(R.string.live_request_pk, userInfo.userName))
                    .setPositiveButton(R.string.live_accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (getContext() == null) {
                                TUILiveLog.d(TAG, "getContext is null!");
                                return;
                            }

                            if (mIsLinkMicStatus) {
                                Toast.makeText(getContext(), R.string.live_link_mic_status, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            dialog.dismiss();
                            //响应跨房接收 PK 请求
                            mLiveRoom.responseRoomPK(userInfo.userId, true, "");
                            mIsPkStatus = true;
                        }
                    })
                    .setNegativeButton(R.string.live_refuse, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mLiveRoom.responseRoomPK(userInfo.userId, false, getString(R.string.live_anchor_refuse_pk_request));
                        }
                    });

            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mRoomPKRequestDialog != null) {
                        mRoomPKRequestDialog.dismiss();
                    }
                    mRoomPKRequestDialog = builder.create();
                    mRoomPKRequestDialog.setCancelable(false);
                    mRoomPKRequestDialog.setCanceledOnTouchOutside(false);
                    mRoomPKRequestDialog.show();
                }
            });
        }

        @Override
        public void onAnchorCancelRequestRoomPK(String userId) {
            if (mRoomPKRequestDialog != null) {
                mRoomPKRequestDialog.dismiss();
            }
        }

        @Override
        public void onAnchorRequestRoomPKTimeout(String userId) {
            if (mRoomPKRequestDialog != null) {
                mRoomPKRequestDialog.dismiss();
            }
        }

        @Override
        public void onQuitRoomPK() {
        }

        @Override
        public void onRecvRoomTextMsg(String message, TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            handleTextMsg(userInfo, message);
        }

        @Override
        public void onRecvRoomCustomMsg(String cmd, String message, TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            int type = Integer.valueOf(cmd);
            switch (type) {
                case Constants.IMCMD_PRAISE:
                    mHeartCount++;
                    handlePraiseMsg(userInfo);
                    break;
                case Constants.IMCMD_DANMU:
                    handleDanmuMsg(userInfo, message);
                    break;
                case Constants.IMCMD_GIFT:
                    //观众送出礼物
                    handleGiftMsg(userInfo, message);
                    break;
                default:
                    break;
            }
        }
    };

    public void setLiveRoomAnchorLayoutDelegate(TUILiveRoomAnchorLayoutDelegate liveRoomAnchorLayoutDelegate) {
        mLiveRoomAnchorLayoutDelegate = liveRoomAnchorLayoutDelegate;

    }

    /**
     * 停止直播
     */
    public void stopLive() {
        if (mIsEnterRoom) {
            destroyRoom();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsEnterRoom = false;
        mLiveRoom = TRTCLiveRoom.sharedInstance(getContext());
        mLiveRoom.setDelegate(mTRTCLiveRoomDelegate);
        mDanmakuManager = new DanmakuManager(getContext());
        mOwnerUserId = V2TIMManager.getInstance().getLoginUser();

        //获取主播的详细信息
        updateAnchorInfo();

        // 如果当前观众页悬浮窗，关闭并退房
        if (FloatWindowLayout.getInstance().mWindowMode == Constants.WINDOW_MODE_FLOAT) {
            FloatWindowLayout.getInstance().closeFloatWindow();
            mLiveRoom.exitRoom(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        preExitRoom();
    }

    /**
     * 退出直播应用
     */
    private void preExitRoom() {
        if (mIsEnterRoom) {
            showExitInfoDialog(getContext().getString(R.string.live_warning_anchor_exit_room), false);
        } else {
            finishRoom();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_fragment_live_room_anchor, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRootView = view.findViewById(R.id.root);
        initBeauty();
        mVideoViewAnchor = view.findViewById(R.id.video_view_anchor);
        mImagePkLayer = view.findViewById(R.id.iv_pk_layer);
        tv_pk = view.findViewById(R.id.tv_pk);
        mAnchorAudioPanel = new AudioEffectPanel(getContext());
        mLayoutPreview = view.findViewById(R.id.layout_preview);
        mLayoutPKContainer = view.findViewById(R.id.layout_pk_container);
        mLayoutTopToolBar = view.findViewById(R.id.layout_top_toolbar);
        mChatLayout = view.findViewById(R.id.layout_chat);
        mDanmakuView = view.findViewById(R.id.view_danmaku);
        mGiftAnimatorLayout = view.findViewById(R.id.lottie_animator_layout);
        mDanmakuManager.setDanmakuView(mDanmakuView);
        mGroupAfterLive = view.findViewById(R.id.group_after_live);
        mGroupButtomView = view.findViewById(R.id.group_bottom_view);
        mHeartLayout = view.findViewById(R.id.heart_layout);
        mStatusTipsView = view.findViewById(R.id.state_tips);
        initBottomToolBar(view);
        mAnchorAudioPanel.setAudioEffectManager(mLiveRoom.getAudioEffectManager());
        mAnchorAudioPanel.initPanelDefaultBackground();
        List<LiveVideoView> tuiVideoViewList = new ArrayList<>();
        tuiVideoViewList.add((LiveVideoView) view.findViewById(R.id.video_view_link_mic_1));
        tuiVideoViewList.add((LiveVideoView) view.findViewById(R.id.video_view_link_mic_2));
        tuiVideoViewList.add((LiveVideoView) view.findViewById(R.id.video_view_link_mic_3));
        //GlideEngine.loadImages(roomchangebg, R.drawable.room_change_bg, 10, 25);
        mTUIVideoViewController = new VideoViewController(tuiVideoViewList, new LiveVideoView.OnRoomViewListener() {
            @Override
            public void onKickUser(String userId) {
                if (userId != null) {
                    mLiveRoom.kickoutJoinAnchor(userId, null);
                }
            }
        });

        mBottomStopPkBtn = view.findViewById(R.id.btn_bottom_stop_pk);
        mImageRedDot = view.findViewById(R.id.img_badge);
        mBottomStopPkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
                    mLiveRoom.quitRoomPK(null);
                } else {
                    //排麦需求
                    mLinkMicListDialog.show();
                }
            }
        });

        //PK展示界面 等待主播接受
        mSelectMemberView = new SelectMemberView(getContext());
        mSelectMemberView.setOnSelectedCallback(new SelectMemberView.onSelectedCallback() {
            @Override
            public void onSelected(int seatIndex, SelectMemberView.MemberEntity memberEntity) {
                mLiveRoom.requestRoomPK(memberEntity.roomId, memberEntity.userId, REQUEST_PK_TIME_OUT, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (getContext() == null) {
                            TUILiveLog.d(TAG, "getContext is null!");
                            return;
                        }

                        mStatusTipsView.setText("");
                        mStatusTipsView.setVisibility(View.GONE);
                        if (code == 0) {
                            // 主播已经接受
                            Toast.makeText(getContext(), R.string.live_anchor_accept_pk, Toast.LENGTH_SHORT).show();
                        } else if (code == -2) {
                            Toast.makeText(getContext(), R.string.live_anchor_accept_pk_timeout, Toast.LENGTH_SHORT).show();
                        } else {
                            // 主播拒绝
                            Toast.makeText(getContext(), R.string.live_anchor_reject_pk, Toast.LENGTH_SHORT).show();
                        }
                        mSelectMemberView.dismiss();
                    }
                });
                mSelectMemberView.dismiss();
                mStatusTipsView.setText(R.string.live_wait_anchor_accept);
                mStatusTipsView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                mSelectMemberView.dismiss();
            }
        });

        //申请连麦列表
        mLinkMicListDialog = new LinkMicListDialog(getContext());
        mLinkMicListDialog.setTitle(getContext().getString(R.string.live_audience_link_mic));
        mLinkMicListDialog.setOnSelectedCallback(new LinkMicListDialog.onSelectedCallback() {
            @Override
            public void onItemAgree(LinkMicListDialog.MemberEntity memberEntity) {
                if (getContext() == null) {
                    TUILiveLog.d(TAG, "getContext is null!");
                    return;
                }

                if (mIsPkStatus) {
                    Toast.makeText(getContext(), R.string.live_pk_status, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAnchorUserIdList != null && mAnchorUserIdList.size() >= MAX_LINK_MIC_SIZE) {
                    Toast.makeText(getContext(), R.string.live_warning_link_user_max_limit, Toast.LENGTH_SHORT).show();
                    return;
                }
                mLiveRoom.responseJoinAnchor(memberEntity.userId, true, getString(R.string.live_anchor_accept));
                mLinkMicListDialog.removeMemberEntity(memberEntity.userId);
                mLinkMicListDialog.dismiss();
                judgeRedDotShow();
                mIsLinkMicStatus = true;
            }

            @Override
            public void onItemReject(LinkMicListDialog.MemberEntity memberEntity) {
                mLiveRoom.responseJoinAnchor(memberEntity.userId, false, getString(R.string.live_anchor_reject));
                mLinkMicListDialog.removeMemberEntity(memberEntity.userId);
                mLinkMicListDialog.dismiss();
                judgeRedDotShow();
            }

            @Override
            public void onCancel() {
                mLinkMicListDialog.dismiss();
            }
        });

        //打开预览页并回调数据
        mLayoutPreview.setPreviewCallback(new LiveRoomPreviewLayout.PreviewCallback() {
            @Override
            public void onClose() {
                // 点击预览页的关闭按钮
                finishRoom();
            }

            @Override
            public void onBeautyPanel() {
                mLayoutBeautyPanel.show();
            }

            @Override
            public void onSwitchCamera() {
                mLiveRoom.switchCamera();
            }

            @Override
            public void onStartLive(final String roomName, final int audioQualityType) {
                requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
                    @Override
                    public void onAllPermissionsGrand() {
                        startPreview();   // 打开本地预览，传入预览的 View
                        startLiveCountDown(roomName, audioQualityType);
                    }
                });
            }

            @Override
            public void onshowExitInfoDialog() {
                //先去完成实名认证
                showYesInfoDialog(getString(R.string.shimingrenzheng), false);
            }

            @Override
            public void Nolivebroadcast(int time) {
                showNolivebroadcastDialog(getString(R.string.showNolivebroadcastDialog), false);
            }

            @Override
            public void unknownerror() {
                showunknownerrorDialog(getString(R.string.showunknownerrorDialog), false);
            }

            @Override
            public void Networkabnormality() {

                showshowNetworkabnormalityDialogDialog(getString(R.string.showNetworkabnormalityDialog), false);

            }

            @Override
            public void ontobereviewedDialog() {
                //待审核
                showYesInfoDialog(getString(R.string.shimingrenzheng), false);
            }
        });

        mLayoutBeautyPanel.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mIsEnterRoom) {
                    mGroupButtomView.setVisibility(View.VISIBLE);
                } else {
                    mLayoutPreview.setBottomViewVisibility(View.VISIBLE);
                }
            }
        });
        mLayoutBeautyPanel.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (mIsEnterRoom) {
                    mGroupButtomView.setVisibility(View.GONE);
                } else {
                    mLayoutPreview.setBottomViewVisibility(View.GONE);
                }
            }
        });
        requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
            @Override
            public void onAllPermissionsGrand() {
                startPreview();
            }
        });
        mCountDownTimerView = view.findViewById(R.id.countdown_timer_view);

        svgaImageView = view.findViewById(R.id.svgaImage);
        parser = new SVGAParser(getContext());
        mAnimationUrlList = new LinkedList<>();

        //头部事件监听
        mLayoutTopToolBar.setTopToolBarDelegate(new myTopToolBarDelegate());
    }


    /**
     * //创建直播房间
     *
     * @param roomName
     * @param audioQualityType
     */
    private void startLiveCountDown(final String roomName, final int audioQualityType) {
        mLayoutPreview.setVisibility(View.GONE);
        mCountDownTimerView.countDownAnimation(CountDownTimerView.DEFAULT_COUNTDOWN_NUMBER);
        mCountDownTimerView.setOnCountDownListener(new ICountDownTimerView.ICountDownListener() {
            @Override
            public void onCountDownComplete() {
                startLive(roomName, audioQualityType);
            }
        });
    }

    /**
     * // 开始创建房间
     *
     * @param roomName
     * @param audioQualityType
     */
    private void startLive(final String roomName, final int audioQualityType) {
        TRTCLiveRoomDef.TRTCCreateRoomParam roomParam = new TRTCLiveRoomDef.TRTCCreateRoomParam();
        roomParam.roomName = roomName;
        roomParam.coverUrl = mAnchorInfo.avatarUrl;
        mLiveRoom.createRoom(mRoomId, roomParam, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getContext() == null) {
                    TUILiveLog.d(TAG, "getContext is null!");
                    return;
                }

                if (code == 0) {
                    mIsEnterRoom = true;
                    onCreateRoomSuccess(audioQualityType);
                    if (mLiveRoomAnchorLayoutDelegate != null) {
                        mRoomInfo.roomName = roomName;
                        mRoomInfo.roomId = mRoomId;
                        mRoomInfo.ownerId = mOwnerUserId;
                        mRoomInfo.ownerName = TUIKitLive.getLoginUserInfo().getNickName();
                        mLiveRoomAnchorLayoutDelegate.onRoomCreate(mRoomInfo);
                    }
                    //Toast.makeText(getContext(), R.string.live_create_room_success, Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getContext(), R.string.live_create_room_fail, Toast.LENGTH_SHORT).show();
                    showErrorAndQuit(code, msg);
                }
            }
        });
    }

    private void initData() {
        mGiftInfoDataHandler = new GiftInfoDataHandler();       //初始化礼物对像
        mGiftAdapter = new DefaultGiftAdapterImp();             //重点获取礼物适配器内空
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            this.mRoomId = bundle.getInt(Constants.ROOM_ID);
            this.msg = bundle.getString(Constants.MSG);
            inidateroom();
        }
    }

    private void initBeauty() {
        mLayoutBeautyPanel = new BeautyPanel(getContext());
        String beautyJson = BeautyUtils.readAssetsFile("live_beauty_data.json");
        BeautyInfo beautyInfo = BeautyUtils.createBeautyInfo(beautyJson);
        mLayoutBeautyPanel.setBeautyInfo(beautyInfo);
        mLayoutBeautyPanel.setBeautyManager(mLiveRoom.getBeautyManager());
        mLayoutBeautyPanel.setCurrentFilterIndex(1);
        mLayoutBeautyPanel.setCurrentBeautyIndex(0);
    }

    /**
     * 底部图标初始化
     *
     * @param view
     */
    private void initBottomToolBar(View view) {

        mBottomToolBarLayout = view.findViewById(R.id.layout_bottom_toolbar);
        //消息聊天内容
        mBottomToolBarLayout.setOnTextSendListener(new InputTextMsgDialog.OnTextSendDelegate() {
            @Override
            public void onTextSend(String msg, boolean tanmuOpen) {
                ChatEntity entity = new ChatEntity();
                entity.setSenderName(getString(R.string.live_message_me));
                entity.setContent(msg);
                entity.setType(Constants.TEXT_TYPE);
                updateIMMessageList(entity);
                if (tanmuOpen) {
                    if (mDanmakuManager != null) {
                        mDanmakuManager.addDanmu(mAnchorInfo.avatarUrl, mAnchorInfo.userName, msg);
                    }

                    mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_DANMU), msg, new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(int code, String msg) {

                        }
                    });
                } else {
                    mLiveRoom.sendRoomTextMsg(msg, new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(int code, String msg) {
                            if (code != 0) {
                                Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        // 初始化PK按钮
        mButtonInvitationPk = new CircleImageView(getContext());
        mButtonInvitationPk.setImageResource(R.drawable.live_pk_start);
        mButtonInvitationPk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
                    @Override
                    public void onAllPermissionsGrand() {
                        showPKView();
                    }
                });
            }
        });

        //判断是否播主，播主则显示PK图标
        mButtonInvitationPk.setVisibility(Config.getPKButtonStatus() ? View.VISIBLE : View.GONE);

        // 初始化音乐按钮
        CircleImageView btnMusic = new CircleImageView(getContext());
        btnMusic.setImageResource(R.drawable.live_ic_music);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnchorAudioPanel != null) {
                    mAnchorAudioPanel.show();
                }
            }
        });

        // 初始化美颜按钮
        CircleImageView buttonBeauty = new CircleImageView(getContext());
        buttonBeauty.setImageResource(R.drawable.live_ic_beauty);
        buttonBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayoutBeautyPanel != null) {
                    mLayoutBeautyPanel.show();
                }
            }
        });

        // 初始化切换摄像头按钮
        CircleImageView buttonSwitchCam = new CircleImageView(getContext());
        buttonSwitchCam.setImageResource(R.drawable.live_ic_switch_camera_on);
        buttonSwitchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLiveRoom.switchCamera();

            }
        });

        // 初始化退出房间按钮
        CircleImageView buttonExitRoom = new CircleImageView(getContext());
        buttonExitRoom.setImageResource(R.drawable.live_exit_room);
        buttonExitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preExitRoom();
            }
        });

        //集合list传入底部相关的按钮图标
        List<CircleImageView> circleImageViews = Arrays.asList(mButtonInvitationPk, btnMusic, buttonBeauty, buttonSwitchCam, buttonExitRoom);
        mBottomToolBarLayout.setRightButtonsLayout(circleImageViews);


    }

    /**
     * 展示PK相关功能
     */
    private void showPKView() {
        if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
            return;
        }
        if (mLiveRoomAnchorLayoutDelegate != null) {
            mSelectMemberView.show();
            mSelectMemberView.refreshView();
            mLiveRoomAnchorLayoutDelegate.getRoomPKList(new TUILiveRoomAnchorLayout.OnRoomListCallback() {
                @Override
                public void onSuccess(List<String> roomIdList) {
                    List<Integer> roomList = new ArrayList<>();
                    for (String id : roomIdList) {
                        try {
                            roomList.add(Integer.parseInt(id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mLiveRoom.getRoomInfos(roomList, new TRTCLiveRoomCallback.RoomInfoCallback() {
                        @Override
                        public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveRoomInfo> list) {
                            if (code == 0) {
                                List<SelectMemberView.MemberEntity> memberEntityList = new ArrayList<>();
                                for (TRTCLiveRoomDef.TRTCLiveRoomInfo info : list) {
                                    //过滤哪些没有 userId 的房间（主播不在线）
                                    if (info.roomId == mRoomId || TextUtils.isEmpty(info.ownerId)) {
                                        continue;
                                    }
                                    SelectMemberView.MemberEntity memberEntity = new SelectMemberView.MemberEntity();
                                    memberEntity.userId = info.ownerId;
                                    memberEntity.userAvatar = info.coverUrl;
                                    memberEntity.userName = info.roomName;
                                    memberEntity.roomId = info.roomId;
                                    memberEntityList.add(memberEntity);
                                }
                                if (memberEntityList.size() == 0) {
                                    mSelectMemberView.setTitle("暂无可PK主播");
                                } else {
                                    mSelectMemberView.setTitle("PK列表");
                                }
                                mSelectMemberView.setList(memberEntityList);
                            }
                        }
                    });
                }

                @Override
                public void onFailed() {

                }
            });
        }
    }

    /**
     * 输入内空聊天滚动处理listview
     *
     * @param entity
     */
    private void updateIMMessageList(ChatEntity entity) {
        mChatLayout.addMessageToList(entity);
    }

    /**
     * 处理点赞消息显示
     */
    public void handlePraiseMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        ChatEntity entity = new ChatEntity();
        entity.setSenderName(getString(R.string.live_notification));
        if (TextUtils.isEmpty(userInfo.userName)) {
            entity.setContent(getString(R.string.live_user_click_like, userInfo.userId));
        } else {
            entity.setContent(getString(R.string.live_user_click_like, userInfo.userName));
        }
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        //输入内空聊天滚动处理listview
        entity.setType(Constants.MEMBER_ENTER);
        entity.setUserid(userInfo.userId);
        updateIMMessageList(entity);
    }

    /**
     * 处理弹幕消息的显示
     */
    public void handleDanmuMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        handleTextMsg(userInfo, text);
        if (mDanmakuManager != null) {
            //这里是弹屏显示
            //这里暂时没有头像，所以用默认的一个头像链接代替
            mDanmakuManager.addDanmu(userInfo.avatarUrl, userInfo.userName, text);
        }
    }

    /**
     * 处理礼物弹幕消息
     */
    private void handleGiftMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String giftId) {

        if (mGiftInfoDataHandler != null) {
            GiftInfo giftInfo = mGiftInfoDataHandler.getGiftInfo(giftId);
            if (giftInfo != null) {
                if (userInfo != null) {
                    giftInfo.sendUserHeadIcon = userInfo.avatarUrl;
                    if (!TextUtils.isEmpty(userInfo.userName)) {
                        giftInfo.sendUser = userInfo.userName;
                    } else {
                        giftInfo.sendUser = userInfo.userId;
                    }
                }

                //向局部屏幕展示礼物
                mGiftAnimatorLayout.show(giftInfo);

                //展示大动画特效
                if (!TextUtils.isEmpty(giftInfo.lottieUrl) && giftInfo.type == 1) {
                    boolean svga = giftInfo.lottieUrl.toLowerCase().endsWith(".svga");
                    if (svga) {
                        mAnimationUrlList.addLast(giftInfo.lottieUrl);
                        if (!mIsPlaying) {
                            paySVGAParser();
                        }
                    }
                }


            }
        }
    }

    /**
     * 处理文本消息显示
     */
    public void handleTextMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        ChatEntity entity = new ChatEntity();
        if (TextUtils.isEmpty(userInfo.userName)) {
            entity.setSenderName(userInfo.userId);
        } else {
            entity.setSenderName(userInfo.userName);
        }
        //输入隐天内容
        entity.setContent(text);
        entity.setType(Constants.TEXT_TYPE);
        entity.setUserid(userInfo.userId);
        updateIMMessageList(entity);
    }

    /**
     * 开启本地视频的预览画面
     * true：前置摄像头；false：后置摄像头。
     * callback 操作回调
     */
    protected void startPreview() {
        mVideoViewAnchor.setVisibility(View.VISIBLE);
        mLiveRoom.startCameraPreview(true, mVideoViewAnchor, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                Log.d(TAG, "onCallback: " + code + msg);
            }
        });
    }

    /**
     * 退出直播
     */
    private void finishRoom() {
        mLayoutBeautyPanel.clear();
        mLiveRoom.stopCameraPreview();
        if (mLiveRoomAnchorLayoutDelegate != null) {
            mLiveRoomAnchorLayoutDelegate.onClose();
        }
    }

    /**
     * 退清理数据
     */
    protected void destroyRoom() {
        mIsEnterRoom = false;
        mLayoutBeautyPanel.clear();
        mLiveRoom.stopCameraPreview();

        mLiveRoom.destroyRoom(new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    TUILiveLog.d(TAG, "destroy room ");
                } else {
                    TUILiveLog.d(TAG, "destroy room failed:" + msg);
                }
            }
        });

        mLiveRoom.setDelegate(null);
        if (mLiveRoomAnchorLayoutDelegate != null) {
            mLiveRoomAnchorLayoutDelegate.onRoomDestroy(mRoomInfo);
        }

        // 退房后，清理AudioPanel的相关设置
        if (mAnchorAudioPanel != null) {
            mAnchorAudioPanel.unInit();
        }
    }

    /**
     * 创建房间成功
     *
     * @param audioQualityType
     */
    private void onCreateRoomSuccess(int audioQualityType) {
        mGroupAfterLive.setVisibility(View.VISIBLE);
        mGroupButtomView.setVisibility(View.VISIBLE);
        updateTopToolBar();
        mLiveRoom.setAudioQuality(audioQualityType);
        // 创建房间成功，开始推流
        mLiveRoom.startPublish("", new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getContext() == null) {
                    //Toast.makeText(getContext(), "getContext is null!:" + msg, Toast.LENGTH_SHORT).show();
                    TUILiveLog.d(TAG, "getContext is null!");
                    return;
                }

                if (code == 0) {
                    mStartTime = System.currentTimeMillis();
                    TUILiveLog.d(TAG, "start live success");
                } else {
                    //Toast.makeText(getContext(), "推流失败:" + msg, Toast.LENGTH_SHORT).show();
                    TUILiveLog.e(TAG, "start live failed:" + msg);
                }
            }
        });
    }

    /**
     * 查询用户资料
     */
    private void updateAnchorInfo() {
        TXRoomService.getInstance().getUserInfo(Arrays.asList(mOwnerUserId), new TXUserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TXUserInfo> list) {
                if (code == 0) {
                    for (TXUserInfo info : list) {
                        if (info.userId.equals(mOwnerUserId)) {
                            mAnchorInfo.userId = info.userId;                   //主播唯一标识
                            mAnchorInfo.userName = info.userName;               //主播昵称
                            mAnchorInfo.avatarUrl = info.avatarURL;             //主播头像
                            mAnchorInfo.empiricalUnitName = 1;                  //【字段含义】主播经验值对应的计量单位名/标签，如：星光/经验
                            mAnchorInfo.empiricalValue = 1;                     //主播经验值
                            mLayoutTopToolBar.setAnchorInfo(mAnchorInfo);       //设置主播头像相关内容
                            mLayoutTopToolBar.setHasFollowed(true);             //设置关注
                        }
                    }
                } else {
                    TUILiveLog.e(TAG, "code: " + code + " msg: " + msg);
                }
            }
        });
    }

    /**
     * 显示直播结果的弹窗，如：观看数量、点赞数量、直播时长数
     */
    protected void showPublishFinishDetailsDialog() {
        //确认则显示观看弹窗
        FinishDetailDialog dialog = new FinishDetailDialog();
        Bundle args = new Bundle();
        long second = 0;
        if (mStartTime != 0) {
            second = (System.currentTimeMillis() - mStartTime) / 1000;
        }
        args.putString(LIVE_TOTAL_TIME, UIUtil.formattedTime(second));
        args.putString(ANCHOR_HEART_COUNT, String.format(Locale.CHINA, "%d", mHeartCount));
        args.putString(TOTAL_AUDIENCE_COUNT, String.format(Locale.CHINA, "%d", mTotalMemberCount));
        dialog.setArguments(args);
        dialog.setCancelable(false);
        if (dialog.isAdded()) {
            dialog.dismiss();
        } else {
            dialog.show(getFragmentManager(), "");
        }
    }

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showExitInfoDialog(String msg, Boolean isError) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        if (isError) {
            destroyRoom();
            dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
                @Override
                public void onClick() {
                    dialogFragment.dismiss();
                    //显示直播结果的弹窗，如：观看数量、点赞数量、直播时长数
                    showPublishFinishDetailsDialog();
                }
            });

            dialogFragment.show(getFragmentManager(), "ExitConfirmDialog");
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                destroyRoom();
                //显示直播结果的弹窗，如：观看数量、点赞数量、直播时长数
                showPublishFinishDetailsDialog();
            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();

            }
        });

        dialogFragment.show(getFragmentManager(), "ExitConfirmDialog");
    }

    /**
     * 显示实名确认处理
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showYesInfoDialog(String msg, Boolean isError) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        if (isError) {
            dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();

                //在接收项目清单文件里自定义Activity动作名称 主项目com.paixide
                //AndroidManifest.xml
                Intent intent = new Intent();
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setAction("livebroadcastActivity");
                getContext().startActivity(intent);
                getActivity().finish();
            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();

            }
        });

        dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
    }

    /**
     * 封禁主播开直播
     *
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showNolivebroadcastDialog(String msg, Boolean isError) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        if (isError) {
            dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                //退出直播间
                destroyRoom();
                finishRoom();


            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();

            }
        });

        dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
    }

    /**
     * 未知错误
     *
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showunknownerrorDialog(String msg, Boolean isError) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        if (isError) {
            dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                //退出直播间
                destroyRoom();
                finishRoom();


            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();

            }
        });

        dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
    }

    /**
     * 连接服务器不稳定，请稍后再试
     *
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showshowNetworkabnormalityDialogDialog(String msg, Boolean isError) {
        final ExitConfirmDialog dialogFragment = new ExitConfirmDialog();
        dialogFragment.setCancelable(false);
        dialogFragment.setMessage(msg);

        if (dialogFragment.isAdded()) {
            dialogFragment.dismiss();
            return;
        }

        if (isError) {
            dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
            return;
        }

        dialogFragment.setPositiveClickListener(new ExitConfirmDialog.PositiveClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();
                //退出直播间
                destroyRoom();
                finishRoom();


            }
        });

        dialogFragment.setNegativeClickListener(new ExitConfirmDialog.NegativeClickListener() {
            @Override
            public void onClick() {
                dialogFragment.dismiss();

            }
        });

        dialogFragment.show(getFragmentManager(), "YesConfirmDialog");
    }


    /**
     * 显示错误并且退出直播的弹窗
     *
     * @param errorCode
     * @param errorMsg
     */
    protected void showErrorAndQuit(int errorCode, String errorMsg) {
        if (mErrorDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TUILiveDialogTheme)
                    .setTitle(R.string.live_error + " " + errorCode)
                    .setMessage(errorMsg)
                    .setNegativeButton(R.string.live_get_it, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mErrorDialog.dismiss();
                            destroyRoom();
                            finishRoom();
                        }
                    });
            mErrorDialog = builder.create();
        }
        if (mErrorDialog.isShowing()) {
            mErrorDialog.dismiss();
        }
        mErrorDialog.show();
    }

    private void setAnchorViewFull(boolean isFull) {
        if (isFull) {
            ConstraintSet set = new ConstraintSet();
            set.clone(mRootView);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            set.applyTo(mRootView);
        } else {
            ConstraintSet set = new ConstraintSet();
            set.clone(mRootView);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.TOP, R.id.layout_pk_container, ConstraintSet.TOP);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.BOTTOM, R.id.layout_pk_container, ConstraintSet.BOTTOM);
            set.connect(mVideoViewAnchor.getId(), ConstraintSet.END, R.id.gl_vertical, ConstraintSet.END);
            set.applyTo(mRootView);
        }
    }

    /**
     * 检查更新数据
     */
    private void updateTopToolBar() {
        updateAnchorInfo();
        updateTopAudienceInfo();
    }

    /**
     * 如果一段时间内主播没出现  隐藏图标
     */
    private void updateTopAudienceInfo() {
        mGetAudienceRunnable = new Runnable() {
            @Override
            public void run() {
                //获取房间内所有的观众信息，enterRoom() 成功后调用才有效。
                mLiveRoom.getAudienceList(new TRTCLiveRoomCallback.UserListCallback() {
                    @Override
                    public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
                        if (code == 0) {
                            addAudienceListLayout(list);
                        } else {
                            mHandler.postDelayed(mGetAudienceRunnable, 2000);
                        }
                    }
                });
            }
        };
        // 为了防止进房后立即获取列表不全，所以增加了一个延迟
        mHandler.postDelayed(mGetAudienceRunnable, 2000);
    }

    /**
     * 消息加入列表
     *
     * @param list
     */
    private void addAudienceListLayout(List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
        mLayoutTopToolBar.addAudienceListUser(list);
    }

    /**
     * 消息加入列表
     *
     * @param userInfo
     */
    private void addAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.addAudienceListUser(userInfo);
    }

    /**
     * //移除在线人数
     *
     * @param userInfo
     */
    private void removeAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.removeAudienceUser(userInfo);
    }


    private void judgeRedDotShow() {
        if (mCurrentStatus == TRTCLiveRoomDef.ROOM_STATUS_PK) {
            mImageRedDot.setVisibility(View.GONE);
        } else {
            if (mLinkMicListDialog.getList().size() > 0) {
                mImageRedDot.setVisibility(View.VISIBLE);
            } else {
                mImageRedDot.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 客户端初始化评论
     */
    private void inidateroom() {
        ChatEntity entity = new ChatEntity();
        entity.setSenderName("系统提示");
        entity.setContent(msg);
        entity.setType(Constants.TEXT_TYPE);
        updateIMMessageList(entity);
    }

    /**
     * 头像部份点击事件监听回调
     */
    class myTopToolBarDelegate implements TopToolBarLayout.TopToolBarDelegate {

        @Override
        public void onClickAnchorAvatar() {
            //点击主播头像
            String userId = mAnchorInfo.userId;
            if (!TextUtils.isEmpty(userId)) {
                dialogfoll.mydialogfoll(getContext(), userId, new Callback() {
                    @Override
                    public void onSuccess() {
                        mBottomToolBarLayout.getmTextMessage().performClick();
                    }
                });
            }
        }

        @Override
        public void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo) {
            if (liveAnchorInfo != null) {
                //点击关注主播
                if (!TextUtils.isEmpty(liveAnchorInfo.userId)) {
                    dialogfoll.mydialogfoll(getContext(), liveAnchorInfo.userId, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBottomToolBarLayout.getmTextMessage().performClick();
                        }
                    });
                }


            }

        }

        @Override
        public void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
            if (audienceInfo != null) {
                if (!TextUtils.isEmpty(audienceInfo.userId)) {
                    dialogfoll.mydialogfoll(getContext(), audienceInfo.userId, new Callback() {
                        @Override
                        public void onSuccess() {
                            mBottomToolBarLayout.getmTextMessage().performClick();
                        }
                    });
                }
            }
        }

        @Override
        public void onClickOnlineNum() {
            Log.d(TAG, "在线访问人数: ");
        }

        @Override
        public void onCloseexit() {
            preExitRoom();
        }
    }


    /**
     * 播放大动画
     */
    private void paySVGAParser() {
        try {
            String lottieUrl = mAnimationUrlList.getFirst();
            if (!TextUtils.isEmpty(lottieUrl)) {
                mAnimationUrlList.removeFirst();
            }
            if (parser == null) {
                parser = new SVGAParser(getContext());
            }
            parser.decodeFromURL(new URL(lottieUrl), new SVGAParser.ParseCompletion() {

                @Override
                public void onError() {
                }

                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    if (svgaImageView != null) {
                        svgaImageView.setImageDrawable(drawable);
                        svgaImageView.startAnimation();
                        mIsPlaying = true;
                        svgaImageView.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {

                            }

                            @Override
                            public void onFinished() {
                                if (mAnimationUrlList.isEmpty()) {
                                    if (svgaImageView != null) {
                                        svgaImageView.stopAnimation();
                                        mIsPlaying = false;
                                        Log.d(TAG, "onFinished: ");
                                    }
                                } else {
                                    paySVGAParser();
                                }
                            }

                            @Override
                            public void onRepeat() {

                            }

                            @Override
                            public void onStep(int i, double v) {


                            }
                        });
                    }
                }

            });


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mGetAudienceRunnable);
        mAnchorAudioPanel = null;
    }


}
