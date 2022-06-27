package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.info;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.HttpGetRequest;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapterImp;
import com.tencent.trtc.TRTCCloudDef;

import java.util.concurrent.ThreadPoolExecutor;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.tencent.qcloud.tim.tuikit.live.TUIKitLive.getLoginUserInfo;

/**
 * 预览页的Layout
 */
public class LiveRoomPreviewLayout extends ConstraintLayout {
    String TAG = LiveRoomPreviewLayout.class.getSimpleName();
    private EditText mEditLiveRoomName;
    private RadioButton mRbLiveRoomQualityNormal;
    private RadioButton mRbLiveRoomQualityMusic;
    private PreviewCallback mPreviewCallback;
    private ImageButton mButtonBeauty;
    private Button mButtonStartRoom;
    private ImageView img_live_room_cover;

    private DefaultGiftAdapterImp.GiftBeanThreadPool mGiftBeanThreadPool;
    private static final String GIFT_DATA_URL = BuildConfig.HTTP_API; //在build.gradle里配置了地址
    private info info;
    private Gson gson = new Gson();
    private UserInfo userInfo;


    public LiveRoomPreviewLayout(Context context) {
        this(context, null);
        initView(context);
    }

    public LiveRoomPreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LiveRoomPreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        usergoldcoin();
        inflate(context, R.layout.live_layout_live_room_preview, this);
        img_live_room_cover = findViewById(R.id.img_live_room_cover);
        mEditLiveRoomName = findViewById(R.id.et_live_room_name);
        mRbLiveRoomQualityNormal = findViewById(R.id.rb_live_room_quality_normal);
        mRbLiveRoomQualityMusic = findViewById(R.id.rb_live_room_quality_music);
        mButtonBeauty = findViewById(R.id.btn_beauty);

        mEditLiveRoomName.setFocusable(true);
        mEditLiveRoomName.setFocusableInTouchMode(true);
        mEditLiveRoomName.requestFocus();
        // mEditLiveRoomName.setSelection(mEditLiveRoomName.getText().length());

        mButtonBeauty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreviewCallback != null) {
                    mPreviewCallback.onBeautyPanel();
                }
            }
        });
        mButtonStartRoom = findViewById(R.id.btn_start_room);


        //确认打开视频直播
        mButtonStartRoom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (info == null) {
                    //无法获取到联网数居，所以值为空
                    mPreviewCallback.Networkabnormality();
                    return;
                }
                int status = info == null ? userInfo.getState() : info.getState();
                switch (status) {
                    case 0:
                        //未实名回调处理未实名无法开播
                        mPreviewCallback.onshowExitInfoDialog();
                        return;
                    case 1:
                        //待审核回调
                        mPreviewCallback.ontobereviewedDialog();
                        return;
                    case 2:
                        //正常会员
                        break;
                    case 3:
                        //禁止会员开播
                        mPreviewCallback.Nolivebroadcast(1);
                        return;
                    default:
                        //什么也不是
                        mPreviewCallback.unknownerror();
                        return;
                }
                String roomName = mEditLiveRoomName.getText().toString().trim();
                if (TextUtils.isEmpty(roomName)) {
                    Toast.makeText(TUIKitLive.getAppContext(), R.string.live_room_name_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEditLiveRoomName.getWindowToken(), 0);
                int audioQuality = TRTCCloudDef.TRTC_AUDIO_QUALITY_MUSIC;

                if (mRbLiveRoomQualityNormal.isChecked()) {
                    audioQuality = TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT;
                } else if (mRbLiveRoomQualityMusic.isChecked()) {
                    audioQuality = TRTCCloudDef.TRTC_AUDIO_QUALITY_MUSIC;
                }

                if (mPreviewCallback != null) {
                    mPreviewCallback.onStartLive(roomName, audioQuality);
                }
            }
        });

        //像机
        findViewById(R.id.btn_switch_cam).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreviewCallback != null) {
                    mPreviewCallback.onSwitchCamera();
                }
            }
        });

        //退出
        findViewById(R.id.btn_close_before_live).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPreviewCallback != null) {
                    mPreviewCallback.onClose();
                }
            }
        });


        //设置主播头像
        V2TIMUserFullInfo loginUserInfo = getLoginUserInfo();
        String faceUrl = loginUserInfo.getFaceUrl();
        String nickName = loginUserInfo.getNickName();

        if (!TextUtils.isEmpty(nickName)) {
            mEditLiveRoomName.setText(nickName);
            mEditLiveRoomName.setSelection(nickName.length());
        }
        if (!TextUtils.isEmpty(faceUrl)) {
            Glide.with(this).load(faceUrl).into(img_live_room_cover);
        }

    }

    public void setPreviewCallback(PreviewCallback previewCallback) {
        mPreviewCallback = previewCallback;
    }

    public void setBottomViewVisibility(int visibility) {
        mButtonBeauty.setVisibility(visibility);
        mButtonStartRoom.setVisibility(visibility);
    }


    public void usergoldcoin() {
        userInfo = UserInfo.getInstance();
        String nickName = getLoginUserInfo().getNickName();
        String getuserid = getLoginUserInfo().getUserID();
        String loginUser = V2TIMManager.getInstance().getLoginUser();

        //获取用户是否实名
        ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(GIFT_DATA_URL + "/goldcoin?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), httpListener);
        threadPoolExecutor.execute(request);
    }

    private synchronized ThreadPoolExecutor getThreadExecutor() {
        if (mGiftBeanThreadPool == null || mGiftBeanThreadPool.isShutdown()) {
            mGiftBeanThreadPool = new DefaultGiftAdapterImp.GiftBeanThreadPool(5);
        }
        return mGiftBeanThreadPool;
    }

    /**
     * 网络请求
     */
    private HttpGetRequest.HttpListener httpListener = new HttpGetRequest.HttpListener() {
        @Override
        public void success(String response) {
            Mesresult mesresult = gson.fromJson(response, Mesresult.class);
            if (mesresult.isSuccess()) {
                info = gson.fromJson(mesresult.getData(), info.class);
            } else {
                info = gson.fromJson(mesresult.getData(), info.class);
            }
        }

        @Override
        public void onFailed(String message) {
            Log.d(TAG, "onFailed: " + message);
            Toast.makeText(getContext(), "onFailed" + message, Toast.LENGTH_SHORT).show();

        }
    };

    //定义回调接口
    public interface PreviewCallback {
        void onClose();

        void onBeautyPanel();

        void onSwitchCamera();

        void onStartLive(String roomName, int audioQualityType);

        //新会员未实名接口
        void onshowExitInfoDialog();

        //新会员待审核接口
        void ontobereviewedDialog();

        //封号不允许会员开播 时间 time 多少天
        void Nolivebroadcast(int time);

        //未知错误
        void unknownerror();

        //服务器无法获取数据
        void Networkabnormality();
    }

}
