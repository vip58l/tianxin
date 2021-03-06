package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.liteav.demo.beauty.BeautyParams;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.actionSheetDialog;
import com.tencent.opensource.dialog.dialog_live_item_end;
import com.tencent.opensource.dialog.dialogfoll;
import com.tencent.opensource.listener.Callback;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.BaseFragment;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.bottombar.BottomToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.component.danmaku.DanmakuManager;
import com.tencent.qcloud.tim.tuikit.live.component.floatwindow.FloatWindowLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.IGiftPanelView;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapterImp;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftAnimatorLayout;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoDataHandler;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftPanelViewImp;
import com.tencent.qcloud.tim.tuikit.live.component.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.component.like.HeartLayout;
import com.tencent.qcloud.tim.tuikit.live.component.like.LikeFrequencyControl;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatEntity;
import com.tencent.qcloud.tim.tuikit.live.component.message.ChatLayout;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.TopToolBarLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAudienceLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoom;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomCallback;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDelegate;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoManagerLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui.widget.LiveVideoView;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.TUILiveLog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import master.flame.danmaku.controller.IDanmakuView;

import static com.tencent.qcloud.tim.tuikit.live.base.Constants.REQUEST_LINK_MIC_TIME_OUT;

/**
 * ?????????????????????2
 */
public class LiveRoomAudienceFragment extends BaseFragment {
    private static final String TAG = "LiveAudienceFragment";
    private static final long LINK_MIC_INTERVAL = 3 * 1000;      //??????????????????
    private Context mContext;                                    //activity?????????
    private TopToolBarLayout mLayoutTopToolBar;                  //?????????????????????????????????
    private BottomToolBarLayout mLayoutBottomToolBar;            //??????????????????
    private ChatLayout mLayoutChatMessage;                       //?????????????????????
    private IDanmakuView mDanmakuView;                           //?????????????????????
    private DanmakuManager mDanmakuManager;                      //??????????????????
    private AlertDialog mDialogError;                            //???????????????Dialog
    private TRTCLiveRoom mLiveRoom;                              //?????????
    private LikeFrequencyControl mLikeFrequencyControl;          //????????????????????????
    private GiftAnimatorLayout mGiftAnimatorLayout;              //???????????????????????????
    private ConstraintLayout mRootView;
    private LiveVideoManagerLayout mLayoutVideoManager;          //???????????????
    private HeartLayout mHeartLayout;                            //???????????????????????????
    private CircleImageView mButtonLink;                         //??????????????????
    private ImageView mImagePkLayer;                             //PK
    private TextView mStatusTipsView;                            //??????????????????
    private Button mButtonReportUser;                            //????????????
    private FloatWindowLayout mLayoutFloatWindow;                //???????????????

    private boolean isEnterRoom = false;                         // ????????????????????????????????????
    private boolean isUseCDNPlay = false;                        // ?????????????????????CDN??????
    private boolean mIsAnchorEnter = false;                      // ??????????????????????????????
    private boolean mIsBeingLinkMic = false;                     // ?????????????????????????????????
    private boolean isFloatRecovery = false;                     // ??????????????????????????????????????????
    private int mCurrentStatus = TRTCLiveRoomDef.ROOM_STATUS_NONE;
    private int mRoomId;                                             //????????????ID???
    private long mLastLinkMicTime;                                   //????????????
    private String mAnchorId;                                        //??????id
    private String mSelfUserId;                                      //????????????ID
    private String mCdnUrl;                                          //cdn??????
    private String msg;                                              //???????????????
    private GiftAdapter mGiftAdapter;                                //??????????????????????????? ?????????????????????????????????
    private GiftInfoDataHandler mGiftInfoDataHandler;                //????????????
    private DefaultGiftAdapter defaultGiftAdapter;                   //???????????????
    private SVGAImageView svgaImageView;                             //??????
    private SVGAParser parser;                                       //????????????
    private LinkedList<String> mAnimationUrlList;                    //????????????
    private boolean mIsPlaying;                                      //??????????????????

    private Runnable mGetAudienceRunnable;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable mShowAnchorLeave = new myRunnable();
    //????????????????????????
    private TUILiveRoomAudienceLayout.TUILiveRoomAudienceDelegate mLiveRoomAudienceDelegate;
    //???????????????????????????
    private final TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();
    //?????????????????????????????????
    private final TRTCLiveRoomDef.LiveAudienceInfo mAudienceentry = new TRTCLiveRoomDef.LiveAudienceInfo();

    /**
     * ????????????????????????????????????
     */
    private final TRTCLiveRoomDelegate mTRTCLiveRoomDelegate = new TRTCLiveRoomDelegate() {
        @Override
        public void onError(int code, String message) {
            if (mLiveRoomAudienceDelegate != null) {
                mLiveRoomAudienceDelegate.onError(code, message);
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
            mCurrentStatus = roomInfo.roomStatus;
            // ??????CDN?????????????????????????????????????????????????????????????????????
            if (isUseCDNPlay) {
                //??????????????????3?????????
                mImagePkLayer.setVisibility(mCurrentStatus != TRTCLiveRoomDef.ROOM_STATUS_PK ? View.GONE : View.VISIBLE);
                return;
            }
            mLayoutVideoManager.updateRoomStatus(roomInfo.roomStatus);
            TUILiveLog.d(TAG, "onRoomInfoChange: " + mCurrentStatus);
        }

        @Override
        public void onRoomDestroy(String roomId) {
            if (FloatWindowLayout.getInstance().mWindowMode == Constants.WINDOW_MODE_FLOAT) {
                FloatWindowLayout.getInstance().closeFloatWindow();
                return;
            }

            //????????????????????????
            dialog_live_item_end.dialogliveend(mContext, mAnchorInfo, new mydialogBottomSheetDialog());

            //showErrorAndQuit(0, getString(R.string.live_warning_room_disband));//??????????????????
        }

        @Override
        public void onAnchorEnter(final String userId) {

            if (userId.equals(mAnchorId)) {
                // ???????????????????????????
                mIsAnchorEnter = true;
                mButtonReportUser.setVisibility(View.GONE);
                mHandler.removeCallbacks(mShowAnchorLeave);
                mLayoutVideoManager.startAnchorVideo(userId, true, new TRTCLiveRoomCallback.ActionCallback() {
                    @Override
                    public void onCallback(int code, String msg) {
                        if (code != 0) {
                            onAnchorExit(userId);
                        }
                    }
                });
                inidateroom();
                initBottomToolBar();
                updateFollowView(userId);
            } else {
                mLayoutVideoManager.startAnchorVideo(userId, false, null);
            }
        }

        @Override
        public void onAnchorExit(String userId) {
            if (userId.equals(mAnchorId)) {
                mLayoutVideoManager.stopAnchorVideo(userId, true, null);
                mLayoutBottomToolBar.setVisibility(View.GONE);
                mButtonReportUser.setVisibility(View.GONE);
            } else {
                mLayoutVideoManager.stopAnchorVideo(userId, false, null);
            }
        }

        @Override
        public void onAudienceEnter(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            //????????????????????????????????? ?????????????????????
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(mContext.getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(mContext.getString(R.string.live_user_join_live, userInfo.userId));
            } else {
                entity.setContent(mContext.getString(R.string.live_user_join_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_ENTER);
            entity.setUserid(userInfo.userId);
            updateIMMessageList(entity);     //??????????????????
            addAudienceListLayout(userInfo); //????????????????????????
        }

        @Override
        public void onAudienceExit(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
            ChatEntity entity = new ChatEntity();
            entity.setSenderName(mContext.getString(R.string.live_notification));
            if (TextUtils.isEmpty(userInfo.userName)) {
                entity.setContent(mContext.getString(R.string.live_user_quit_live, userInfo.userId));
            } else {
                entity.setContent(mContext.getString(R.string.live_user_quit_live, userInfo.userName));
            }
            entity.setType(Constants.MEMBER_EXIT);
            entity.setUserid(userInfo.userId);
            updateIMMessageList(entity);        //??????????????????
            removeAudienceListLayout(userInfo); //??????????????????
        }

        @Override
        public void onRequestJoinAnchor(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String reason) {
            Log.d(TAG, "onRequestJoinAnchor: " + userInfo.toString());
            Log.d(TAG, "onRequestJoinAnchor: " + reason);
        }

        @Override
        public void onAudienceRequestJoinAnchorTimeout(String userId) {

        }

        @Override
        public void onAudienceCancelRequestJoinAnchor(String userId) {

        }

        @Override
        public void onKickoutJoinAnchor() {
            stopLinkMic();
        }

        @Override
        public void onRequestRoomPK(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {

        }

        @Override
        public void onAnchorCancelRequestRoomPK(String userId) {

        }

        @Override
        public void onAnchorRequestRoomPKTimeout(String userId) {

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
                    handlePraiseMsg(userInfo);
                    break;
                case Constants.IMCMD_DANMU:
                    handleDanmuMsg(userInfo, message);
                    break;
                case Constants.IMCMD_GIFT:
                    handleGiftMsg(userInfo, message);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * ??????????????????????????????
     *
     * @param liveRoomAudienceDelegate
     */
    public void setLiveRoomAudienceDelegate(TUILiveRoomAudienceLayout.TUILiveRoomAudienceDelegate liveRoomAudienceDelegate) {
        mLiveRoomAudienceDelegate = liveRoomAudienceDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.live_fragment_live_room_audience, container, false);
        initView(rootView);
        //????????????
        PermissionUtils.checkLivePermission(getActivity());
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isFloatRecovery) {
            mLiveRoom.exitRoom(new TRTCLiveRoomCallback.ActionCallback() {
                @Override
                public void onCallback(int code, String msg) {
                    enterRoom();
                }
            });
        } else {
            if (isEnterRoom) {
                enterRoom();
            }
            enterRoom();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mShowAnchorLeave);
        mHandler.removeCallbacks(mGetAudienceRunnable);
    }

    /**
     * ??????VIEW
     *
     * @param rootView
     */
    private void initView(View rootView) {
        updateLiveWindowMode(Constants.WINDOW_MODE_FULL); // ????????????????????????
        mLayoutTopToolBar = rootView.findViewById(R.id.layout_top_toolbar);
        mLayoutBottomToolBar = rootView.findViewById(R.id.layout_bottom_toolbar);
        mLayoutChatMessage = rootView.findViewById(R.id.layout_chat);
        mGiftAnimatorLayout = rootView.findViewById(R.id.lottie_animator_layout);
        mDanmakuView = rootView.findViewById(R.id.view_danmaku);
        mDanmakuManager.setDanmakuView(mDanmakuView);
        mRootView = rootView.findViewById(R.id.root);
        mHeartLayout = rootView.findViewById(R.id.heart_layout);
        mImagePkLayer = rootView.findViewById(R.id.iv_pk_layer);
        mStatusTipsView = rootView.findViewById(R.id.state_tips);
        mLayoutTopToolBar.setTopToolBarDelegate(new myTopToolBarDelegate());
        mButtonReportUser = rootView.findViewById(R.id.report_user);
        mLayoutVideoManager = rootView.findViewById(R.id.ll_video_view);
        mLayoutVideoManager.updateVideoLayoutByWindowStatus();
        mLayoutVideoManager.setVideoManagerLayoutDelegate(new MyLiveVide());
        mButtonReportUser.setOnClickListener(new myViewOnClickListener()); //??????
        svgaImageView = rootView.findViewById(R.id.svgaImage);
        parser = new SVGAParser(getContext());
        mAnimationUrlList = new LinkedList<>();
    }

    /**
     * ????????????????????????
     *
     * @param context
     */
    private void initData(Context context) {
        mContext = context;
        mSelfUserId = V2TIMManager.getInstance().getLoginUser(); //??????????????????
        mLiveRoom = TRTCLiveRoom.sharedInstance(context);
        mLiveRoom.setDelegate(mTRTCLiveRoomDelegate);
        mDanmakuManager = new DanmakuManager(mContext);         //??????

        //????????????mGiftAdapter??????
        mGiftAdapter = new DefaultGiftAdapterImp();
        mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);

        //????????????
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            mRoomId = bundle.getInt(Constants.ROOM_ID);           //??????ID
            isUseCDNPlay = bundle.getBoolean(Constants.USE_CDN);  //??????CDN ?????????????????????CDN?????? true false
            mCdnUrl = bundle.getString(Constants.CDN_URL);        //cdn??????
            mAnchorId = bundle.getString(Constants.ANCHOR_ID);    //??????ID
            msg = bundle.getString(Constants.MSG);                //??????
        }

    }

    /**
     * ???????????????
     */
    private void enterRoom() {
        if (isEnterRoom) {
            return;
        }
        mLiveRoom.enterRoom(mRoomId, isUseCDNPlay, mCdnUrl, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    isEnterRoom = true;
                    updateTopToolBar();
                } else {
                    exitRoom();
                }
            }
        });
        mHandler.postDelayed(mShowAnchorLeave, 3000);
    }

    /**
     * ???????????????
     */
    private void exitRoom() {
        if (isEnterRoom && mLiveRoom != null) {
            mLiveRoom.exitRoom(null);
            isEnterRoom = false;
        }
    }

    /**
     * ???????????????
     */
    private void initBottomToolBar() {
        mLayoutBottomToolBar.setVisibility(View.VISIBLE);
        mLayoutBottomToolBar.setOnTextSendListener(new InputTextMsgDialog.OnTextSendDelegate() {
            @Override
            public void onTextSend(String msg, boolean tanmuOpen) {
                ChatEntity entity = new ChatEntity();
                entity.setSenderName(mContext.getString(R.string.live_message_me));
                entity.setContent(msg);
                entity.setType(Constants.TEXT_TYPE);
                updateIMMessageList(entity);

                if (tanmuOpen) {
                    if (mDanmakuManager != null) {
                        mDanmakuManager.addDanmu(TUIKitLive.getLoginUserInfo().getFaceUrl(), TUIKitLive.getLoginUserInfo().getNickName(), msg);
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
                                //??????????????????
                                //Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                                Toast.makeText(TUIKitLive.getAppContext(), String.format("??????????????????%s %s", code, msg), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        updateBottomFunctionLayout();
    }

    /**
     * ?????????????????????
     */
    private void updateBottomFunctionLayout() {
        mButtonLink = new CircleImageView(mContext);
        mButtonLink.setImageResource(mIsBeingLinkMic ? R.drawable.live_linkmic_off : R.drawable.live_linkmic_on);
        mButtonLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() == null) {
                    TUILiveLog.d(TAG, "getActivity is null");
                    return;
                }
                if (!mIsBeingLinkMic) {
                    long curTime = System.currentTimeMillis();
                    if (curTime < mLastLinkMicTime + LINK_MIC_INTERVAL) {
                        Toast.makeText(getActivity(), R.string.live_tips_rest, Toast.LENGTH_SHORT).show();
                    } else {
                        mLastLinkMicTime = curTime;
                        requestPermissions(PermissionUtils.getLivePermissions(), new OnPermissionGrandCallback() {
                            @Override
                            public void onAllPermissionsGrand() {
                                startLinkMic();
                            }
                        });
                    }
                } else {
                    stopLinkMic();
                }
            }
        });

        // ?????????????????????
        CircleImageView buttonLike = new CircleImageView(mContext);
        buttonLike.setImageResource(R.drawable.live_bottom_bar_like);
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHeartLayout != null) {
                    mHeartLayout.addFavor();
                }
                //????????????????????????
                if (mLikeFrequencyControl == null) {
                    mLikeFrequencyControl = new LikeFrequencyControl();
                    mLikeFrequencyControl.init(2, 1);
                }
                if (mLikeFrequencyControl.canTrigger()) {
                    //???ChatRoom??????????????????
                    mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_PRAISE), "", null);
                }
            }
        });

        // ?????????????????????
        CircleImageView buttonGift = new CircleImageView(mContext);
        buttonGift.setImageResource(R.drawable.live_gift_btn_icon);
        buttonGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGiftPanel();
            }
        });

        // ??????????????????????????????
        CircleImageView buttonSwitchCam = new CircleImageView(TUIKitLive.getAppContext());
        buttonSwitchCam.setImageResource(R.drawable.live_ic_switch_camera_on);
        buttonSwitchCam.setVisibility(mIsBeingLinkMic ? View.VISIBLE : View.GONE);
        buttonSwitchCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLiveRoom.switchCamera();
            }
        });

        // ???????????????????????????
        CircleImageView buttonExitRoom = new CircleImageView(mContext);
        buttonExitRoom.setImageResource(R.drawable.live_exit_room);
        buttonExitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitRoom();
                mLiveRoomAudienceDelegate.onClose();
            }
        });

        //??????????????????
        mLayoutBottomToolBar.setRightButtonsLayout(Arrays.asList(mButtonLink, buttonLike, buttonGift, buttonSwitchCam, buttonExitRoom));
    }

    /**
     * ????????????
     */
    private void startLinkMic() {
        mButtonLink.setEnabled(false);
        mButtonLink.setImageResource(R.drawable.live_linkmic_off);
        mStatusTipsView.setText(R.string.live_wait_anchor_accept);
        mStatusTipsView.setVisibility(View.VISIBLE);
        mLiveRoom.requestJoinAnchor(getString(R.string.live_request_link_mic_anchor, mSelfUserId), REQUEST_LINK_MIC_TIME_OUT, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (getActivity() == null) {
                    TUILiveLog.d(TAG, "??????????????????");
                    return;
                }

                mStatusTipsView.setText("");
                mStatusTipsView.setVisibility(View.GONE);
                if (code == 0) {
                    joinPusher();
                    return;
                }
                if (code == -1) {
                    // ????????????
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } else if (code == -2) {
                    // ?????????????????????
                    Toast.makeText(getActivity(), getString(R.string.live_request_time_out), Toast.LENGTH_SHORT).show();
                } else {
                    //????????????
                    Toast.makeText(getActivity(), getString(R.string.live_error_request_link_mic, msg), Toast.LENGTH_SHORT).show();
                }
                mButtonLink.setEnabled(true);
                //                hideNoticeToast();
                mIsBeingLinkMic = false;
                mButtonLink.setImageResource(R.drawable.live_linkmic_on);
            }
        });
    }

    /**
     * ????????????
     */
    private void joinPusher() {
        LiveVideoView videoView = mLayoutVideoManager.applyVideoView(mSelfUserId);
        if (videoView == null) {
            Toast.makeText(getActivity(), R.string.live_anchor_view_error, Toast.LENGTH_SHORT).show();
            return;
        }
        //????????????
        BeautyParams beautyParams = new BeautyParams();
        mLiveRoom.getBeautyManager().setBeautyStyle(beautyParams.mBeautyStyle);
        mLiveRoom.getBeautyManager().setBeautyLevel(beautyParams.mBeautyLevel);
        mLiveRoom.getBeautyManager().setWhitenessLevel(beautyParams.mWhiteLevel);
        mLiveRoom.getBeautyManager().setRuddyLevel(beautyParams.mRuddyLevel);
        mLiveRoom.startCameraPreview(true, videoView.getPlayerVideo(), new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    mLiveRoom.startPublish("", new TRTCLiveRoomCallback.ActionCallback() {
                        @Override
                        public void onCallback(int code, String msg) {
                            if (getActivity() == null) {
                                TUILiveLog.d(TAG, "getActivity is null");
                                return;
                            }

                            if (code == 0) {
                                mButtonLink.setEnabled(true);
                                mIsBeingLinkMic = true;
                                updateBottomFunctionLayout();
                            } else {
                                stopLinkMic();
                                mButtonLink.setEnabled(true);
                                mButtonLink.setImageResource(R.drawable.live_linkmic_on);
                                Toast.makeText(getActivity(), getString(R.string.live_fail_link_mic, msg), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * ????????????
     */
    private void stopLinkMic() {
        mIsBeingLinkMic = false;
        mLiveRoom.stopCameraPreview();
        mLiveRoom.stopPublish(null);
        if (mLayoutVideoManager != null) {
            mLayoutVideoManager.recycleVideoView(mSelfUserId);
        }
        updateBottomFunctionLayout();
    }

    /**
     * ?????????????????? ????????????
     */
    private void showGiftPanel() {
        //?????????????????????ID???????????????id?????????????????? ????????????????????????dialog
        IGiftPanelView giftPanelView = new GiftPanelViewImp(getContext());
        giftPanelView.init(mGiftInfoDataHandler); //??????????????????
        giftPanelView.setGiftPanelUser(mAnchorInfo, mSelfUserId); // mAnchorInfo//???????????? //??????mSelfUserId
        //???????????????????????????????????????
        giftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                sendGift(giftInfo);
                //????????????????????????
                if (mLiveRoomAudienceDelegate != null) {
                    Log.d(TAG, "onChargeClick: ????????????");
                    mLiveRoomAudienceDelegate.onGiftItemClick();
                    //??????????????????
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

            @Override
            public void onChargeClick() {
                //????????????????????????
                if (mLiveRoomAudienceDelegate != null) {
                    Log.d(TAG, "onChargeClick: ??????dialog??????");
                    mLiveRoomAudienceDelegate.onChargeClick();
                }
            }
        });
        giftPanelView.show();
    }

    /**
     * ?????????????????????????????????????????????????????????
     *
     * @param giftInfo
     */
    private void sendGift(GiftInfo giftInfo) {
        GiftInfo giftInfoCopy = giftInfo.copy();
        giftInfoCopy.sendUser = mContext.getString(R.string.live_message_me);
        giftInfoCopy.sendUserHeadIcon = TUIKitLive.getLoginUserInfo().getFaceUrl();
        giftInfo.userid = TUIKitLive.getLoginUserInfo().getUserID();
        mGiftAnimatorLayout.show(giftInfoCopy); //??????????????????
        mLiveRoom.sendRoomCustomMsg(String.valueOf(Constants.IMCMD_GIFT), giftInfoCopy.giftId, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {//??????????????????
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_message_send_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * ??????2???????????????????????????
     */
    private void updateTopToolBar() {
        if (!isEnterRoom) {
            return;
        }
        mGetAudienceRunnable = new Runnable() {
            @Override
            public void run() {
                updateTopAnchorInfo();
                updateTopAudienceInfo();
            }
        };
        //???????????????????????????????????????????????????????????????????????????
        mHandler.postDelayed(mGetAudienceRunnable, 2000);
    }

    /**
     * ????????????????????????
     */
    private void updateTopAnchorInfo() {
        mLiveRoom.getAnchorList(new TRTCLiveRoomCallback.UserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
                if (code == 0) {
                    //????????????????????????
                    for (TRTCLiveRoomDef.TRTCLiveUserInfo info : list) {
                        if (info.userId.equals(mAnchorId)) {
                            mAnchorInfo.userId = info.userId;
                            mAnchorInfo.userName = info.userName;
                            mAnchorInfo.avatarUrl = info.avatarUrl;
                            mLayoutTopToolBar.setAnchorInfo(mAnchorInfo);
                            //??????????????????
                            mLayoutVideoManager.updateAnchorOfflineViewBackground(mAnchorInfo.avatarUrl);
                        }
                    }
                } else {
                    Log.e(TAG, "code: " + code + " msg: " + msg + " list size: " + list.size());
                }
            }
        });
    }

    /**
     * ????????????????????????
     */
    private void updateTopAudienceInfo() {
        mLiveRoom.getAudienceList(new TRTCLiveRoomCallback.UserListCallback() {
            @Override
            public void onCallback(int code, String msg, List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
                if (code == 0) {
                    addAudienceListLayout(list);
                }
            }
        });
    }

    /**
     * ??????????????????
     *
     * @param list
     */
    private void addAudienceListLayout(List<TRTCLiveRoomDef.TRTCLiveUserInfo> list) {
        mLayoutTopToolBar.addAudienceListUser(list);
    }

    /**
     * ????????????????????????
     *
     * @param userInfo
     */
    private void addAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.addAudienceListUser(userInfo);
    }

    /**
     * ??????????????????
     *
     * @param userInfo
     */
    private void removeAudienceListLayout(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mLayoutTopToolBar.removeAudienceUser(userInfo);
    }

    /**
     * ??????????????????
     */
    private void updateIMMessageList(ChatEntity entity) {
        mLayoutChatMessage.addMessageToList(entity);
    }

    /**
     * ??????????????????
     */
    public void handlePraiseMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        ChatEntity entity = new ChatEntity();

        entity.setSenderName(mContext.getString(R.string.live_notification));
        if (TextUtils.isEmpty(userInfo.userName)) {
            entity.setContent(mContext.getString(R.string.live_user_click_like, userInfo.userId));
        } else {
            entity.setContent(mContext.getString(R.string.live_user_click_like, userInfo.userName));
        }
        if (mHeartLayout != null) {
            mHeartLayout.addFavor();
        }
        entity.setType(Constants.MEMBER_ENTER);
        entity.setUserid(userInfo.userId);
        updateIMMessageList(entity);
    }

    /**
     * ??????????????????
     */
    public void handleDanmuMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        handleTextMsg(userInfo, text);
        if (mDanmakuManager != null) {
            //?????????????????????????????????????????????????????????????????????
            mDanmakuManager.addDanmu(userInfo.avatarUrl, userInfo.userName, text);
        }
    }

    /**
     * ????????????????????????
     */
    private void handleGiftMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String giftId) {
        if (mGiftInfoDataHandler != null) {
            GiftInfo giftInfo = mGiftInfoDataHandler.getGiftInfo(giftId);
            if (giftInfo != null) {
                if (userInfo != null) {
                    giftInfo.sendUserHeadIcon = userInfo.avatarUrl;
                    giftInfo.userid = userInfo.userId;
                    if (!TextUtils.isEmpty(userInfo.userName)) {
                        giftInfo.sendUser = userInfo.userName;
                    } else {
                        giftInfo.sendUser = userInfo.userId;
                    }
                }
                mGiftAnimatorLayout.show(giftInfo);
            }
        }
    }

    /**
     * ??????????????????
     */
    public void handleTextMsg(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo, String text) {
        ChatEntity entity = new ChatEntity();
        if (TextUtils.isEmpty(userInfo.userName)) {
            entity.setSenderName(userInfo.userId);
        } else {
            entity.setSenderName(userInfo.userName);
        }
        entity.setContent(text);
        entity.setType(Constants.TEXT_TYPE);
        entity.setUserid(userInfo.userId);
        updateIMMessageList(entity);
    }

    @Override
    public void onBackPressed() {
        if (mDanmakuManager != null) {
            mDanmakuManager.destroy();
            mDanmakuManager = null;
        }
        updateLiveWindowMode(Constants.WINDOW_MODE_FLOAT);
    }

    /**
     * ????????????????????????
     *
     * @param mode
     */
    protected void updateLiveWindowMode(int mode) {
        if (FloatWindowLayout.getInstance().mWindowMode == mode) return;
        if (mode == Constants.WINDOW_MODE_FLOAT) {
            FloatWindowLayout.IntentParams intentParams = new FloatWindowLayout.IntentParams(getActivity().getClass(), mRoomId, mAnchorId, isUseCDNPlay, mCdnUrl);
            boolean result = FloatWindowLayout.getInstance().showFloatWindow(mLayoutVideoManager.mLayoutRoot, intentParams);
            if (!result) {
                exitRoom();
                return;
            }
            FloatWindowLayout.getInstance().mWindowMode = mode;
            mLayoutVideoManager.updateVideoLayoutByWindowStatus();
            FloatWindowLayout.getInstance().setFloatWindowLayoutDelegate(new FloatWindowLayout.FloatWindowLayoutDelegate() {
                @Override
                public void onClose() {
                    exitRoom();
                    if (mLiveRoomAudienceDelegate != null) {
                        mLiveRoomAudienceDelegate.onClose();
                    }

                }
            });
        } else if (mode == Constants.WINDOW_MODE_FULL) {
            FloatWindowLayout.getInstance().closeFloatWindow();
            FloatWindowLayout.getInstance().mWindowMode = mode;
            isFloatRecovery = true;
        }
    }


    /**
     * ??????????????????????????????
     *
     * @param errorCode
     * @param errorMsg
     */
    protected void showErrorAndQuit(int errorCode, String errorMsg) {
        if (mDialogError == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.TUILiveDialogTheme)
                    .setTitle(R.string.dialogeorrmsg)
                    .setMessage(errorMsg)
                    .setNegativeButton(R.string.live_get_it, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialogError.dismiss();
                            exitRoom();
                            if (mLiveRoomAudienceDelegate != null) {
                                mLiveRoomAudienceDelegate.onClose();
                            }
                        }
                    });

            mDialogError = builder.create();
        }
        if (mDialogError.isShowing()) {
            mDialogError.dismiss();
        }
        mDialogError.show();
    }


    /**
     * ??????????????????
     *
     * @param userId
     */
    private void followAnchor(final String userId) {
        mLiveRoom.followAnchor(userId, new TRTCLiveRoomCallback.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code == 0) {
                    //???????????????????????????????????????
                    mLayoutTopToolBar.setHasFollowed(true);
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_follow_anchor_success, Toast.LENGTH_SHORT).show();
                    getfollowAnchor(userId);

                }
            }
        });
    }

    /**
     * ?????????????????????????????????
     *
     * @param userId
     */
    private void updateFollowView(String userId) {
        mLiveRoom.checkFollowAnchorState(userId, new TRTCLiveRoomCallback.RoomFollowStateCallback() {
            @Override
            public void isFollowed() {
                mLayoutTopToolBar.setHasFollowed(true);
            }

            @Override
            public void isNotFollowed() {
                mLayoutTopToolBar.setHasFollowed(false);
                Toast.makeText(getContext(), "????????????", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String errorMessage) {
                mLayoutTopToolBar.setHasFollowed(true);
            }
        });
    }

    /**
     * ????????????????????????
     */
    private void inidateroom() {
        ChatEntity entity = new ChatEntity();
        entity.setSenderName("????????????");
        entity.setContent(msg);
        entity.setType(Constants.TEXT_TYPE);
        updateIMMessageList(entity);
    }

    /**
     * ??????????????????
     *
     * @param userId
     */
    public void getfollowAnchor(String userId) {
        DefaultGiftAdapter defaultGiftAdapter = new DefaultGiftAdapter();
        defaultGiftAdapter.getHttpGet(String.format("/gofollowlist?userid=%s&touserid=%s", UserInfo.getInstance().getUserId(), userId), new DefaultGiftAdapter.callback() {
            @Override
            public void success(String response) {
                try {
                    Mesresult mesresult = new Gson().fromJson(response, Mesresult.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });

    }


    /**
     * ??????????????????????????????
     */
    class myTopToolBarDelegate implements TopToolBarLayout.TopToolBarDelegate {

        @Override
        public void onClickAnchorAvatar() {
            String userId = mAnchorInfo.userId;
            if (!TextUtils.isEmpty(userId)) {
                dialogfoll.mydialogfoll(mContext, userId, new Callback() {
                    @Override
                    public void onSuccess() {
                        mLayoutBottomToolBar.getmTextMessage().performClick();
                    }
                });
            }
        }

        @Override
        public void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo) {
            if (liveAnchorInfo != null) {
                followAnchor(liveAnchorInfo.userId);
            }

        }

        @Override
        public void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
            if (audienceInfo != null) {
                dialogfoll.mydialogfoll(mContext, audienceInfo.userId, new Callback() {
                    @Override
                    public void onSuccess() {
                        mLayoutBottomToolBar.getmTextMessage().performClick();
                    }
                });
            }
        }

        @Override
        public void onClickOnlineNum() {
            Log.d(TAG, "onClickOnlineNum: ");
        }

        @Override
        public void onCloseexit() {
            exitRoom();
            mLiveRoomAudienceDelegate.onClose();

        }
    }

    /**
     * ????????????????????????????????????
     */
    class mydialogBottomSheetDialog implements dialog_live_item_end.dialogBottomSheetDialog {

        @Override
        public void ClickListener() {
            exitRoom();
            if (mLiveRoomAudienceDelegate != null) {
                mLiveRoomAudienceDelegate.onClose();
            }
        }

        @Override
        public void LongClickListener() {

        }
    }

    class MyLiveVide implements LiveVideoManagerLayout.VideoManagerLayoutDelegate {

        @Override
        public void onClose() {
            if (mLiveRoomAudienceDelegate != null) {
                mLiveRoomAudienceDelegate.onClose();
            }
        }
    }

    /**
     * ????????????
     */
    class myViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            actionSheetDialog.actionSheetshow(mContext, mAnchorInfo.userId, String.valueOf(mRoomId));
        }
    }

    /**
     * ????????????????????????????????????  ????????????
     */
    class myRunnable implements Runnable {
        @Override
        public void run() {
            mLayoutVideoManager.setAnchorOfflineViewVisibility(mIsAnchorEnter ? View.GONE : View.VISIBLE);   //???????????????
            mLayoutBottomToolBar.setVisibility(mIsAnchorEnter ? View.VISIBLE : View.GONE);                   // ????????????
            mButtonReportUser.setVisibility(mIsAnchorEnter ? View.VISIBLE : View.GONE);                      //??????
        }
    }

    /**
     * ???????????????
     */
    private void paySVGAParser() {
        try {
            String lottieUrl = mAnimationUrlList.getFirst();
            if (!TextUtils.isEmpty(lottieUrl)) {
                mAnimationUrlList.removeFirst();
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

}
