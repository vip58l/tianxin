package io.agora.tutorials1v1vcall.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.opensource.model.UserInfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.agora.rtc.IRtcChannelEventHandler;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.models.DataStreamConfig;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import io.agora.tutorials1v1vcall.Persenter.Persenter;
import io.agora.tutorials1v1vcall.model.getbackdate;
import io.agora.tutorials1v1vcall.model.gifinfo;
import io.agora.tutorials1v1vcall.R;
import io.agora.tutorials1v1vcall.Web.Webrowse;
import io.agora.tutorials1v1vcall.Web.callback;
import io.agora.tutorials1v1vcall.widgit.LoggerRecyclerView;


import static io.agora.rtc.Constants.CONNECTION_STATE_CONNECTED;
import static io.agora.rtc.Constants.CONNECTION_STATE_CONNECTING;
import static io.agora.rtc.Constants.CONNECTION_STATE_DISCONNECTED;
import static io.agora.rtc.Constants.CONNECTION_STATE_FAILED;
import static io.agora.rtc.Constants.CONNECTION_STATE_RECONNECTING;

public class VideoChatViewActivity extends AppCompatActivity {
    private static final String TAG = VideoChatViewActivity.class.getSimpleName();
    private static final int PERMISSION_REQ_ID = 22;
    private RtcEngine mRtcEngine;  //??????????????????????????????????????????
    private boolean mCallEnd;
    private boolean mMuted;
    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private VideoCanvas mLocalVideo;
    private VideoCanvas mRemoteVideo;
    private ImageView mCallBtn;
    private ImageView mMuteBtn, mMuteBtn1, mMuteBtn2, mMuteBtn3, mMuteBtn4, mMuteBtn5;
    private ImageView mSwitchCameraBtn;
    private LoggerRecyclerView mLogView;
    private final List<String> permissionlist = new ArrayList<>();
    private UserInfo userInfo;
    private final Gson gson = new Gson();
    private Persenter persenter;


    private boolean mCallEnd1;
    private boolean mCallEnd2;
    private boolean mCallEnd3;
    private boolean mCallEnd4;
    private boolean mCallEnd5;

    //????????????
    private com.opensource.svgaplayer.SVGAImageView svgaImage;
    private SVGAParser parser;
    private boolean mIsPlaying;
    public LinkedList<String> mAnimationUrlList = new LinkedList<>();


    /**
     * ??????????????????????????????????????????
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        //???????????????????????????????????????
        public void onStreamMessage(final int uid, final int streamId, final byte[] data) {
            super.onStreamMessage(uid, streamId, data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //???????????????????????????
                    String Message = new String(data);   //????????????
                    String userid = String.valueOf(uid);  //????????????ID
                    gifinfo gifinfo = gson.fromJson(Message, gifinfo.class);
                    mLogView.logI(gifinfo.getName());

                    //?????????????????????????????????????????????????????????
                    showSVGAP(gifinfo.getPath());
                }
            });
        }

        @Override
        //????????????????????????????????????????????????
        public void onStreamMessageError(int uid, int streamId, int error, int missed, int cached) {
            super.onStreamMessageError(uid, streamId, error, missed, cached);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("??????????????????");
                }
            });
        }

        @Override
        //????????????????????????
        public void onRtcStats(RtcStats stats) {
            super.onRtcStats(stats);

            Log.d(TAG, "onRtcStats: " + stats.toString());
        }

        @Override
        //?????????????????????????????????
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
            super.onNetworkQuality(uid, txQuality, rxQuality);
        }

        @Override
        //??????????????????????????????????????????
        public void onLocalAudioStats(LocalAudioStats stats) {
            super.onLocalAudioStats(stats);
        }

        @Override
        //?????????????????????????????????????????????
        public void onRemoteAudioStats(RemoteAudioStats stats) {
            super.onRemoteAudioStats(stats);
        }

        @Override
        //??????????????????????????????????????????
        public void onRemoteVideoStats(RemoteVideoStats stats) {
            super.onRemoteVideoStats(stats);
        }

        @Override
        //??????????????????????????????????????????
        public void onLocalVideoStats(LocalVideoStats stats) {
            super.onLocalVideoStats(stats);
        }

        @Override
        // ?????????????????????????????????????????????????????????
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("?????????????????????");
                }
            });
        }

        @Override
        // ?????????????????????????????????????????????????????????
        // ??????????????????????????? setupRemoteVideo ????????????????????????
        public void onUserJoined(final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("???????????????????????????");
                    setupRemoteVideo(uid); //??????????????????
                }
            });
        }

        @Override
        //????????????????????????
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onRejoinChannelSuccess(channel, uid, elapsed);
            mLogView.logI("???????????????????????????: ");

        }

        @Override
        //???????????????????????????
        public void onClientRoleChanged(int oldRole, int newRole) {
            super.onClientRoleChanged(oldRole, newRole);
        }

        @Override
        //??????????????????
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
        }

        @Override
        // ????????????????????????????????????????????????????????????
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("???????????????????????????");
                    Toast.makeText(getBaseContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
                    onRemoteUserLeft(uid);
                    finish();
                }
            });


        }

        @Override
        //Token ???????????????
        public void onRequestToken() {
            super.onRequestToken();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "Token ???????????????", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        @Override
        //Token ????????????????????????
        public void onTokenPrivilegeWillExpire(String token) {
            super.onTokenPrivilegeWillExpire(token);
            Log.d(TAG, "????????????????????? Token??????:Token ???????????????????????? ");
        }

        @Override
        //?????????????????????????????????
        public void onConnectionStateChanged(int state, int reason) {
            super.onConnectionStateChanged(state, reason);
            switch (state) {
                case CONNECTION_STATE_DISCONNECTED:
                    mLogView.logE("??????????????????");
                    break;
                case CONNECTION_STATE_CONNECTING:
                    mLogView.logE("?????????????????????");
                    break;
                case CONNECTION_STATE_CONNECTED:
                    mLogView.logE("???????????????");
                    break;
                case CONNECTION_STATE_RECONNECTING:
                    mLogView.logE("???????????????????????????");
                    break;
                case CONNECTION_STATE_FAILED:
                    mLogView.logE("??????????????????");
                    break;
            }

        }

        @Override
        //????????????????????????????????????
        public void onNetworkTypeChanged(int type) {
            super.onNetworkTypeChanged(type);
        }

        @Override
        //????????????????????????
        public void onConnectionLost() {
            super.onConnectionLost();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("?????????????????????");
                    Toast.makeText(getBaseContext(), "?????????????????????", Toast.LENGTH_SHORT).show();


                }
            });
        }


        @Override
        public void onError(int err) {
            super.onError(err);
            mLogView.logW("???????????????" + err + "");
        }

        @Override
        // ???????????????????????????????????????????????????????????????????????? ID???????????????????????????????????????????????????
        // @param speakers ?????????????????????????????????????????? ID ??????????????????????????????????????????????????????????????? [0,255]???
        // @param totalVolume ??????????????????????????????????????????????????? [0,255]???
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
            super.onAudioVolumeIndication(speakers, totalVolume);
        }

        @Override
        // ?????????????????????????????????????????? ID?????? 1 ??????
        public void onActiveSpeaker(int uid) {
        }
    };


    //????????????????????????????????????????????????????????????
    //RtcChannel rtcChannel=new RtcChannel();

    /**
     * ????????????????????????????????????????????????
     */
    IRtcChannelEventHandler iRtcChannelEventHandler = new IRtcChannelEventHandler() {


    };

    //????????????????????????????????????
    //IAudioEffectManager iAudioEffectManager=new IAudioEffectManager(){};

    /**
     * ???????????????????????????
     *
     * @param uid
     */
    private void setupRemoteVideo(int uid) {
        ViewGroup parent = mRemoteContainer;
        if (parent.indexOfChild(mLocalVideo.view) > -1) {
            parent = mLocalContainer;
        }
        if (mRemoteVideo != null) {
            return;
        }
        // ???????????? SurfaceView ?????????
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);

        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid); // ?????????????????????
        mRtcEngine.setupRemoteVideo(mRemoteVideo); //???????????????????????????
    }

    private void onRemoteUserLeft(int uid) {
        if (mRemoteVideo != null && mRemoteVideo.uid == uid) {
            removeFromParent(mRemoteVideo);
            mRemoteVideo = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
        userInfo = UserInfo.getInstance();
        persenter = new Persenter();
        initUI();
    }

    /**
     * ????????????
     */
    private void initUI() {
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mMuteBtn1 = findViewById(R.id.btn_mute1);
        mMuteBtn2 = findViewById(R.id.btn_mute2);
        mMuteBtn3 = findViewById(R.id.btn_mute3);
        mMuteBtn4 = findViewById(R.id.btn_mute4);
        mMuteBtn5 = findViewById(R.id.btn_mute5);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);
        mLogView = findViewById(R.id.log_recycler_view);
        svgaImage = findViewById(R.id.svgaImage);
        checkPermission();

    }

    /**
     * ????????????
     */
    private void checkPermission() {
        permissionlist.add(Manifest.permission.RECORD_AUDIO);
        permissionlist.add(Manifest.permission.CAMERA);
        permissionlist.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission(permissionlist.get(0), PERMISSION_REQ_ID) && checkSelfPermission(permissionlist.get(1), PERMISSION_REQ_ID) && checkSelfPermission(permissionlist.get(2), PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionlist.toArray(new String[permissionlist.size()]), requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_ID) {
            for (String permission : permissions) {
                if (!checkSelfPermission(permission, PERMISSION_REQ_ID)) {

                    return;
                }
            }
            initEngineAndJoinChannel();


        }
    }

    private void showLongToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoChatViewActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * ???????????????
     */
    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    //??????????????????
    private void setadjustRecordingSignalVolume() {
        int volume = 200;
        // ????????????????????????????????????????????????
        mRtcEngine.adjustRecordingSignalVolume(volume);
        //????????????????????????????????????????????????????????????
        mRtcEngine.adjustUserPlaybackSignalVolume(Integer.parseInt(userInfo.getUserId()), volume);
        // ???????????????????????????
        mRtcEngine.enableInEarMonitoring(true);
        // ?????????????????????
        mRtcEngine.setInEarMonitoringVolume(volume);
    }

    /**
     * https://docs.agora.io/cn/Video/start_call_android?platform=Android#4-?????????-rtcengine
     * ????????? RtcEngine ??????
     */
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    /**
     * ????????????????????????
     */
    private void setupVideoConfig() {
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????
        VideoEncoderConfiguration videoEncoderConfiguration = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15, VideoEncoderConfiguration.STANDARD_BITRATE, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        mRtcEngine.enableVideo();                                                  //??????????????????
        mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);        //????????????????????????

//        mRtcEngine.enableLocalVideo(true);           //?????????/???????????????????????????
//        mRtcEngine.muteLocalVideoStream(true);      //?????????/??????????????????????????????
//        mRtcEngine.muteRemoteVideoStream(Integer.parseInt(userInfo.getUserId()), true);     //?????????/??????????????????????????????
//        mRtcEngine.muteAllRemoteVideoStreams(true); //?????????/??????????????????????????????


        //??????????????????
        //mRtcEngine.enableDualStreamMode(true);
    }

    /**
     * ??????????????????
     */
    private void setupLocalVideo() {
        // ?????? SurfaceView ?????????
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        // ??????????????????
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    /**
     * ??????????????????
     * https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a8b308c9102c08cb8dafb4672af1a3b4c
     */
    private void joinChannel() {
        //???????????????????????????token
        final String RoomId = String.valueOf((userInfo.getUserId()).hashCode() & 0x7FFFFFFF); //?????????ID????????????HASH????????????
        final String channelName = "channelName1";  //????????????channelName1 ????????????                //????????????+????????? liev+12345 ?????????????????????

        //??????????????????
        Webrowse.Http(Webrowse.get, persenter.PostDate(channelName, userInfo), new callback() {
            @Override
            public void onSuccess(final String date) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getbackdate agora = new Gson().fromJson(date, getbackdate.class);
                            //?????? ????????????channelName1 + ??????uid
                            mRtcEngine.joinChannel(agora.getToken(), agora.getChannelName(), "Extra Optional Data", Integer.parseInt(agora.getUserid()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(VideoChatViewActivity.this, date, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            public void onFail() {
                finish();
            }
        });

        // ?????? joinChannel ?????? ???????????????
        //mRtcEngine.joinChannel(YOUR_TOKEN, "demoChannel1", "Extra Optional Data", 0);
        //mRtcEngine.renewToken(token);
        //mRtcEngine.setClientRole(CLIENT_ROLE_BROADCASTER);//????????????????????????????????????????????????????????????
        //mRtcEngine.setClientRole(CLIENT_ROLE_AUDIENCE);   // ??????????????????????????????????????????????????????

        mRtcEngine.enableFaceDetection(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
        }
        //?????? RtcEngine ??????
        RtcEngine.destroy();
        permissionlist.clear();
        svgaImage.stopAnimation();
        svgaImage.clear();
    }

    /**
     * ?????????????????? ??????????????????
     * ????????????????????????????????????????????? App ??? App ??????????????????????????? leaveChannel ????????????????????????
     */
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    /**
     * ????????????????????? ???????????????????????????
     * ??????????????????????????????????????????????????????????????????
     *
     * @param view
     */
    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }


    /**
     * ???????????????
     * ?????? switchCamera ?????????????????????????????????
     *
     * @param view
     */
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }


    /**
     * ??????????????????
     *
     * @param view
     */
    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
            mCallBtn.setImageResource(R.drawable.btn_startcall);
            Toast.makeText(this, getString(R.string.endcall_msg), Toast.LENGTH_SHORT).show();
            finish();
        }

        showButtons(!mCallEnd);
    }

    /**
     * ????????????
     */
    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    /**
     * ????????????
     */
    private void endCall() {
        removeFromParent(mLocalVideo);
        mLocalVideo = null;
        removeFromParent(mRemoteVideo);
        mRemoteVideo = null;
        leaveChannel();
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
    }

    /**
     * ????????????
     *
     * @param canvas
     * @return
     */
    private ViewGroup removeFromParent(VideoCanvas canvas) {
        if (canvas != null) {
            ViewParent parent = canvas.view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(canvas.view);
                return group;
            }
        }
        return null;
    }

    /**
     * ????????????
     *
     * @param canvas
     */
    private void switchView(VideoCanvas canvas) {
        ViewGroup parent = removeFromParent(canvas);
        if (parent == mLocalContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(false);
            }
            mRemoteContainer.addView(canvas.view);
        } else if (parent == mRemoteContainer) {
            if (canvas.view instanceof SurfaceView) {
                ((SurfaceView) canvas.view).setZOrderMediaOverlay(true);
            }
            mLocalContainer.addView(canvas.view);
        }
    }

    /**
     * ????????????????????????
     *
     * @param view
     */
    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }


    /**
     * ??????/????????????????????????
     *
     * @param view
     */
    public void enableLocalVideo1(View view) {
        mCallEnd1 = !mCallEnd1;
        mRtcEngine.enableLocalVideo(mCallEnd1);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn1.setImageResource(res);
        Toast.makeText(this, "??????/????????????????????????", Toast.LENGTH_SHORT).show();

    }

    /**
     * ??????/???????????????????????????
     *
     * @param view
     */
    public void enableLocalVideo2(View view) {
        mCallEnd2 = !mCallEnd2;
        mRtcEngine.muteLocalVideoStream(mCallEnd2);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn2.setImageResource(res);
        Toast.makeText(this, "??????/???????????????????????????", Toast.LENGTH_SHORT).show();
    }

    /**
     * //?????????/??????????????????????????????
     *
     * @param view
     */
    public void enableLocalVideo3(View view) {
        mCallEnd3 = !mCallEnd3;
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn3.setImageResource(res);
        mRtcEngine.muteRemoteVideoStream(Integer.parseInt(userInfo.getUserId()), mCallEnd3);
        Toast.makeText(this, "??????/???????????????????????????", Toast.LENGTH_SHORT).show();

    }

    /**
     * ??????/??????????????????????????????
     *
     * @param view
     */
    public void enableLocalVideo4(View view) {
        mCallEnd4 = !mCallEnd4;
        mRtcEngine.muteAllRemoteVideoStreams(mCallEnd4);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn4.setImageResource(res);
        Toast.makeText(this, "??????/???????????????????????????", Toast.LENGTH_SHORT).show();
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    public void enableLocalVideo5(View view) {
        mCallEnd5 = !mCallEnd5;
        mRtcEngine.enableDualStreamMode(mCallEnd5);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn5.setImageResource(res);
        Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
    }


    public void endbaldialogboot(View view) {
        persenter.startActivity(this);
    }

    //??????????????? ???????????????
    public void tosendStreamMessage(View v) {

        gifinfo gifinfo = new gifinfo();
        gifinfo.setName("???????????????");
        gifinfo.setPath("http://192.168.0.196:802/images/svga/??????.svga");
        String toJson = gson.toJson(gifinfo);
        String message = toJson;
        DataStreamConfig config = new DataStreamConfig();
        config.ordered = false;
        config.syncWithAudio = false;
        mRtcEngine.createDataStream(config);
        mRtcEngine.sendStreamMessage(1, message.getBytes());
        mLogView.logI(gifinfo.getName());
        showSVGAP(gifinfo.getPath());
    }

    //????????????????????????????????????
    private void showSVGAP(String path) {
        boolean svga = path.toLowerCase().endsWith(".svga");
        if (svga) {
            mAnimationUrlList.add(path);
            if (!mIsPlaying) {
                paySVGAParser();
            }
        }

    }

    /**
     * SVGAParser???????????????
     */
    private void paySVGAParser() {
        try {
            String lottieUrl = mAnimationUrlList.getFirst();
            if (!TextUtils.isEmpty(lottieUrl)) {
                mAnimationUrlList.removeFirst();  //?????????????????????
            }
            if (parser == null) {
                parser = new SVGAParser(this);
            }
            parser.decodeFromURL(new URL(lottieUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onError() {
                }

                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    if (svgaImage != null) {
                        svgaImage.setImageDrawable(drawable);
                        svgaImage.startAnimation();
                        mIsPlaying = true;
                        svgaImage.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {

                            }

                            @Override
                            public void onFinished() {
                                if (mAnimationUrlList.isEmpty()) {
                                    if (svgaImage != null) {
                                        svgaImage.stopAnimation();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Persenter.requestCode && data != null) {
            String GiftInfo = data.getStringExtra(Persenter.GiftInfo);


            Toast.makeText(this, GiftInfo, Toast.LENGTH_SHORT).show();
        }
    }

}
