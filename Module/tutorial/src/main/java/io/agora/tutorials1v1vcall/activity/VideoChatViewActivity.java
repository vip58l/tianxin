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
    private RtcEngine mRtcEngine;  //类包含应用程序调用的主要方法
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

    //动画特效
    private com.opensource.svgaplayer.SVGAImageView svgaImage;
    private SVGAParser parser;
    private boolean mIsPlaying;
    public LinkedList<String> mAnimationUrlList = new LinkedList<>();


    /**
     * 类用于向应用程序发送回调通知
     */
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        //接收到对方数据流消息的回调
        public void onStreamMessage(final int uid, final int streamId, final byte[] data) {
            super.onStreamMessage(uid, streamId, data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //接收礼物或消息展示
                    String Message = new String(data);   //消息内容
                    String userid = String.valueOf(uid);  //发送方的ID
                    gifinfo gifinfo = gson.fromJson(Message, gifinfo.class);
                    mLogView.logI(gifinfo.getName());

                    //可以这里判断做直播或送礼物展示业务逻辑
                    showSVGAP(gifinfo.getPath());
                }
            });
        }

        @Override
        //接收对方数据流消息发生错误的回调
        public void onStreamMessageError(int uid, int streamId, int error, int missed, int cached) {
            super.onStreamMessageError(uid, streamId, error, missed, cached);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("接收消息失败");
                }
            });
        }

        @Override
        //当前通话统计回调
        public void onRtcStats(RtcStats stats) {
            super.onRtcStats(stats);

            Log.d(TAG, "onRtcStats: " + stats.toString());
        }

        @Override
        //网络上下行质量报告回调
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
            super.onNetworkQuality(uid, txQuality, rxQuality);
        }

        @Override
        //通话中本地音频流统计信息回调
        public void onLocalAudioStats(LocalAudioStats stats) {
            super.onLocalAudioStats(stats);
        }

        @Override
        //通话中远端音频流的统计信息回调
        public void onRemoteAudioStats(RemoteAudioStats stats) {
            super.onRemoteAudioStats(stats);
        }

        @Override
        //通话中远端视频流统计信息回调
        public void onRemoteVideoStats(RemoteVideoStats stats) {
            super.onRemoteVideoStats(stats);
        }

        @Override
        //通话中本地视频流统计信息回调
        public void onLocalVideoStats(LocalVideoStats stats) {
            super.onLocalVideoStats(stats);
        }

        @Override
        // 本地用户成功加入频道时，会触发该回调。
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("视频初始化成功");
                }
            });
        }

        @Override
        // 远端用户成功加入频道时，会触发该回调。
        // 可以在该回调中调用 setupRemoteVideo 方法设置远端视图
        public void onUserJoined(final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("对方与视频连接成功");
                    setupRemoteVideo(uid); //设置远端视图
                }
            });
        }

        @Override
        //重新加入频道回调
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onRejoinChannelSuccess(channel, uid, elapsed);
            mLogView.logI("重新尝试重连成功后: ");

        }

        @Override
        //用户角色已切换回调
        public void onClientRoleChanged(int oldRole, int newRole) {
            super.onClientRoleChanged(oldRole, newRole);
        }

        @Override
        //离开频道回调
        public void onLeaveChannel(RtcStats stats) {
            super.onLeaveChannel(stats);
        }

        @Override
        // 远端用户离开频道或掉线时，会触发该回调。
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("对方已断开视频连接");
                    Toast.makeText(getBaseContext(), "对方已断开视频连接", Toast.LENGTH_SHORT).show();
                    onRemoteUserLeft(uid);
                    finish();
                }
            });


        }

        @Override
        //Token 已过期回调
        public void onRequestToken() {
            super.onRequestToken();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), "Token 已过期回调", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        @Override
        //Token 服务即将过期回调
        public void onTokenPrivilegeWillExpire(String token) {
            super.onTokenPrivilegeWillExpire(token);
            Log.d(TAG, "该方法用于更新 Token回调:Token 服务即将过期回调 ");
        }

        @Override
        //网络连接状态已改变回调
        public void onConnectionStateChanged(int state, int reason) {
            super.onConnectionStateChanged(state, reason);
            switch (state) {
                case CONNECTION_STATE_DISCONNECTED:
                    mLogView.logE("网络连接断开");
                    break;
                case CONNECTION_STATE_CONNECTING:
                    mLogView.logE("建立网络连接中");
                    break;
                case CONNECTION_STATE_CONNECTED:
                    mLogView.logE("网络已连接");
                    break;
                case CONNECTION_STATE_RECONNECTING:
                    mLogView.logE("重新建立网络连接中");
                    break;
                case CONNECTION_STATE_FAILED:
                    mLogView.logE("网络连接失败");
                    break;
            }

        }

        @Override
        //本地网络类型发生改变回调
        public void onNetworkTypeChanged(int type) {
            super.onNetworkTypeChanged(type);
        }

        @Override
        //网络连接丢失回调
        public void onConnectionLost() {
            super.onConnectionLost();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLogView.logI("当前网络不稳定");
                    Toast.makeText(getBaseContext(), "当前网络不稳定", Toast.LENGTH_SHORT).show();


                }
            });
        }


        @Override
        public void onError(int err) {
            super.onError(err);
            mLogView.logW("错误代码：" + err + "");
        }

        @Override
        // 获取瞬时说话音量最高的几个用户（即说话者）的用户 ID、他们的音量及本地用户是否在说话。
        // @param speakers 为一个数组，包含说话者的用户 ID 、音量及本地用户人声状态。音量的取值范围为 [0,255]。
        // @param totalVolume 指混音后频道内的总音量，取值范围为 [0,255]。
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
            super.onAudioVolumeIndication(speakers, totalVolume);
        }

        @Override
        // 获取当前时间段声音最大的用户 ID（仅 1 个）
        public void onActiveSpeaker(int uid) {
        }
    };


    //提供在指定频道内实现实时音视频功能的方法
    //RtcChannel rtcChannel=new RtcChannel();

    /**
     * 类监听和报告指定频道的事件和数据
     */
    IRtcChannelEventHandler iRtcChannelEventHandler = new IRtcChannelEventHandler() {


    };

    //类提供管理音效文件的方法
    //IAudioEffectManager iAudioEffectManager=new IAudioEffectManager(){};

    /**
     * 初始化远端用户视图
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
        // 创建一个 SurfaceView 对象。
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(parent == mLocalContainer);
        parent.addView(view);

        mRemoteVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid); // 设置远端视图。
        mRtcEngine.setupRemoteVideo(mRemoteVideo); //初始化远端用户视图
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
     * 初化控件
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
     * 检查权限
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
     * 初始化数据
     */
    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    //设置采集音量
    private void setadjustRecordingSignalVolume() {
        int volume = 200;
        // 设置本地播放的所有远端用户音量。
        mRtcEngine.adjustRecordingSignalVolume(volume);
        //设置本地播放的指定远端用户混音后的音量。
        mRtcEngine.adjustUserPlaybackSignalVolume(Integer.parseInt(userInfo.getUserId()), volume);
        // 开启耳返监听功能。
        mRtcEngine.enableInEarMonitoring(true);
        // 设置耳返音量。
        mRtcEngine.setInEarMonitoringVolume(volume);
    }

    /**
     * https://docs.agora.io/cn/Video/start_call_android?platform=Android#4-初始化-rtcengine
     * 初始化 RtcEngine 对象
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
     * 设置视频编码属性
     */
    private void setupVideoConfig() {
        // 根据用户在界面的选择设置视频编码的分辨率、帧率、码率与横竖屏方向模式
        VideoEncoderConfiguration videoEncoderConfiguration = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_640x360, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15, VideoEncoderConfiguration.STANDARD_BITRATE, VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        mRtcEngine.enableVideo();                                                  //启用视频模块
        mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);        //设置视频编码配置

//        mRtcEngine.enableLocalVideo(true);           //：开启/关闭本地视频采集。
//        mRtcEngine.muteLocalVideoStream(true);      //：停止/恢复发送本地视频流。
//        mRtcEngine.muteRemoteVideoStream(Integer.parseInt(userInfo.getUserId()), true);     //：停止/恢复接收指定视频流。
//        mRtcEngine.muteAllRemoteVideoStreams(true); //：停止/恢复接收所有视频流。


        //多人视频场景
        //mRtcEngine.enableDualStreamMode(true);
    }

    /**
     * 设置本地视图
     */
    private void setupLocalVideo() {
        // 创建 SurfaceView 对象。
        SurfaceView view = RtcEngine.CreateRendererView(getBaseContext());
        view.setZOrderMediaOverlay(true);
        mLocalContainer.addView(view);
        // 设置本地视图
        mLocalVideo = new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        mRtcEngine.setupLocalVideo(mLocalVideo);
    }

    /**
     * 进入频道房间
     * https://docs.agora.io/cn/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#a8b308c9102c08cb8dafb4672af1a3b4c
     */
    private void joinChannel() {
        //重服务端获取最新的token
        final String RoomId = String.valueOf((userInfo.getUserId()).hashCode() & 0x7FFFFFFF); //邀请人ID生成指定HASH频道编号
        final String channelName = "channelName1";  //暂时定义channelName1 频道不变                //频道名称+房间号 liev+12345 组合成的频道号

        //联网获取数据
        Webrowse.Http(Webrowse.get, persenter.PostDate(channelName, userInfo), new callback() {
            @Override
            public void onSuccess(final String date) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            getbackdate agora = new Gson().fromJson(date, getbackdate.class);
                            //加入 频道名称channelName1 + 用户uid
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

        // 调用 joinChannel 方法 加入频道。
        //mRtcEngine.joinChannel(YOUR_TOKEN, "demoChannel1", "Extra Optional Data", 0);
        //mRtcEngine.renewToken(token);
        //mRtcEngine.setClientRole(CLIENT_ROLE_BROADCASTER);//直播频道中的主播，可以发布和接收音视频流
        //mRtcEngine.setClientRole(CLIENT_ROLE_AUDIENCE);   // 直播频道中的观众，只可以接收音视频流

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
        //销毁 RtcEngine 实例
        RtcEngine.destroy();
        permissionlist.clear();
        svgaImage.stopAnimation();
        svgaImage.clear();
    }

    /**
     * 退出当前通话 离开当前频道
     * 根据场景需要，如结束通话、关闭 App 或 App 切换至后台时，调用 leaveChannel 离开当前通话频道
     */
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    /**
     * 关闭或打开音频 停止发送本地音频流
     * 停止或恢复发送本地音频流，实现或取消本地静音
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
     * 切换摄像头
     * 调用 switchCamera 方法切换摄像头的方向。
     *
     * @param view
     */
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }


    /**
     * 打开视频接听
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
     * 打开视频
     */
    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    /**
     * 停止视频
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
     * 移险视频
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
     * 视频交换
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
     * 切换视频大小屏幕
     *
     * @param view
     */
    public void onLocalContainerClick(View view) {
        switchView(mLocalVideo);
        switchView(mRemoteVideo);
    }


    /**
     * 开启/关闭本地视频采集
     *
     * @param view
     */
    public void enableLocalVideo1(View view) {
        mCallEnd1 = !mCallEnd1;
        mRtcEngine.enableLocalVideo(mCallEnd1);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn1.setImageResource(res);
        Toast.makeText(this, "开启/关闭本地视频采集", Toast.LENGTH_SHORT).show();

    }

    /**
     * 停止/恢复发送本地视频流
     *
     * @param view
     */
    public void enableLocalVideo2(View view) {
        mCallEnd2 = !mCallEnd2;
        mRtcEngine.muteLocalVideoStream(mCallEnd2);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn2.setImageResource(res);
        Toast.makeText(this, "停止/恢复发送本地视频流", Toast.LENGTH_SHORT).show();
    }

    /**
     * //：停止/恢复接收指定视频流。
     *
     * @param view
     */
    public void enableLocalVideo3(View view) {
        mCallEnd3 = !mCallEnd3;
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn3.setImageResource(res);
        mRtcEngine.muteRemoteVideoStream(Integer.parseInt(userInfo.getUserId()), mCallEnd3);
        Toast.makeText(this, "停止/恢复接收指定视频流", Toast.LENGTH_SHORT).show();

    }

    /**
     * 停止/恢复接收所有视频流。
     *
     * @param view
     */
    public void enableLocalVideo4(View view) {
        mCallEnd4 = !mCallEnd4;
        mRtcEngine.muteAllRemoteVideoStreams(mCallEnd4);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn4.setImageResource(res);
        Toast.makeText(this, "停止/恢复接收所有视频流", Toast.LENGTH_SHORT).show();
    }

    /**
     * 多人视频场景
     *
     * @param view
     */
    public void enableLocalVideo5(View view) {
        mCallEnd5 = !mCallEnd5;
        mRtcEngine.enableDualStreamMode(mCallEnd5);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn5.setImageResource(res);
        Toast.makeText(this, "多人视频场景", Toast.LENGTH_SHORT).show();
    }


    public void endbaldialogboot(View view) {
        persenter.startActivity(this);
    }

    //创建数据流 发送数据流
    public void tosendStreamMessage(View v) {

        gifinfo gifinfo = new gifinfo();
        gifinfo.setName("送给你烟花");
        gifinfo.setPath("http://192.168.0.196:802/images/svga/烟花.svga");
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

    //判断地址是否播放动画文件
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
     * SVGAParser播放大动画
     */
    private void paySVGAParser() {
        try {
            String lottieUrl = mAnimationUrlList.getFirst();
            if (!TextUtils.isEmpty(lottieUrl)) {
                mAnimationUrlList.removeFirst();  //清删除一条数据
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
