package com.tencent.qcloud.tim.uikit.modules.chat.layout.input;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.chat.base.BaseInputFragment;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IInputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.inputmore.InputMoreActionUnit;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class InputLayoutUI extends LinearLayout implements IInputLayout {

    protected static final int CAPTURE = 1;
    protected static final int AUDIO_RECORD = 2;
    protected static final int VIDEO_RECORD = 3;
    protected static final int SEND_PHOTO = 4;
    protected static final int SEND_FILE = 5;
    private static final String TAG = InputLayoutUI.class.getSimpleName();
    /**
     * 语音/文字切换输入控件
     */
    protected ImageView mAudioInputSwitchButton;
    protected boolean mAudioInputDisable;

    /**
     * 表情按钮
     */
    protected ImageView mEmojiInputButton;
    protected boolean mEmojiInputDisable;

    /**
     * 更多按钮
     */
    protected ImageView mMoreInputButton;
    protected Object mMoreInputEvent;
    protected boolean mMoreInputDisable;

    /**
     * 消息发送按钮
     */
    protected Button mSendTextButton;

    /**
     * 语音长按按钮
     */
    protected Button mSendAudioButton;

    /**
     * 文本输入框
     */
    protected TIMMentionEditText mTextInput;

    protected AppCompatActivity mActivity;
    protected View mInputMoreLayout;
    //protected ShortcutArea mShortcutArea;
    protected View mInputMoreView;
    protected ChatInfo mChatInfo;
    protected List<InputMoreActionUnit> mInputMoreActionList = new ArrayList<>();
    protected List<InputMoreActionUnit> mInputMoreCustomActionList = new ArrayList<>(); //外部添加的图标
    private AlertDialog mPermissionDialog;
    private boolean mSendPhotoDisable;
    private boolean mCaptureDisable;
    private boolean mVideoRecordDisable;
    private boolean mSendFileDisable;
    private boolean mEnableAudioCall;
    private boolean mEnableVideoCall;
    private boolean mEnablegift;
    private boolean music;
    private boolean Quickmessage;
    private boolean location;

    public InputLayoutUI(Context context) {
        super(context);
        initViews();
    }

    public InputLayoutUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public InputLayoutUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mActivity = (AppCompatActivity) getContext();
        inflate(mActivity, R.layout.chat_input_layout, this);
        mInputMoreView = findViewById(R.id.more_groups);
        mSendAudioButton = findViewById(R.id.chat_voice_input);
        mAudioInputSwitchButton = findViewById(R.id.voice_input_switch);
        mEmojiInputButton = findViewById(R.id.face_btn);
        mMoreInputButton = findViewById(R.id.more_btn);
        mSendTextButton = findViewById(R.id.send_btn);
        mTextInput = findViewById(R.id.chat_message_input);
        init();
    }


    public Button getmSendAudioButton() {
        return mSendAudioButton;
    }

    /**
     * 添加自动义底部图标位置
     */
    protected void assembleActions() {
        mInputMoreActionList.clear();
        InputMoreActionUnit action;

        /**
         * 礼物
         */
        if (!mEnablegift) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.actor_chat_gift);
            action.setTitleId(R.string.ablegift);
            action.setOnClickListener(v -> startmEnablegift());
            mInputMoreActionList.add(action);
        }

        /**
         *  视频通话
         */
        if (mEnableVideoCall) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_video_call);
            action.setTitleId(R.string.video_call);
            action.setOnClickListener(v -> startVideoCall());
            mInputMoreActionList.add(action);
        }

        /**
         *  语音通话
         */
        if (mEnableAudioCall) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_audio_call);
            action.setTitleId(R.string.audio_call);
            action.setOnClickListener(v -> startAudioCall());
            mInputMoreActionList.add(action);
        }

        /**
         * 图片
         */
        if (!mSendPhotoDisable) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_picture);
            action.setTitleId(R.string.pic);
            action.setOnClickListener(v -> startSendPhoto());
            mInputMoreActionList.add(action);
        }

        /**
         * 拍照
         */
        if (!mCaptureDisable) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_camera);
            action.setTitleId(R.string.photo);
            action.setOnClickListener(v -> startCapture());
            mInputMoreActionList.add(action);
        }

        /**
         * 摄像
         */
        if (!mVideoRecordDisable) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_video);
            action.setTitleId(R.string.video);
            action.setOnClickListener(v -> startVideoRecord());
            mInputMoreActionList.add(action);
        }

        /**
         * 文件
         */
        if (!mSendFileDisable) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_file);
            action.setTitleId(R.string.file);
            action.setOnClickListener(v -> startSendFile());
            mInputMoreActionList.add(action);
        }


        /**
         * 添加快捷常用词语面板
         */
        if (!Quickmessage) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_video_call2);            // 设置单元的图标
            action.setTitleId(R.string.profilex);                               // 设置单元的文字标题
            action.setOnClickListener(v -> startQuickmessage());
            mInputMoreActionList.add(action);                                //排序位置
        }

        /**
         * 直播群
         */
        if (TUIKit.getConfigs().isEnableGroupLiveEntry() && mChatInfo != null && mChatInfo.getType() != V2TIMConversation.V2TIM_C2C) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_more_group_live);
            action.setTitleId(R.string.live_group_live);
            action.setOnClickListener(v -> startGroupLive());
            mInputMoreActionList.add(action);
        }

        /**
         * 添加一个定位功能
         */
        if (!location) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.where_red);           // 设置单元的图标
            action.setTitleId(R.string.gpsamp);                  // 设置单元的文字标题
            action.setOnClickListener(v -> startLocation());
            mInputMoreActionList.add(action);              //排序位置
        }

        /**
         * 添加音乐面板
         */
        if (!music) {
            action = new InputMoreActionUnit();
            action.setIconResId(R.drawable.ic_personal_member2);        // 设置单元的图标
            action.setTitleId(R.string.promisc);                        // 设置单元的文字标题
            action.setOnClickListener(v -> startplaymisc());
            mInputMoreActionList.add(action);
        }

        //添加全部list列表
        mInputMoreActionList.addAll(mInputMoreCustomActionList);
    }

    protected boolean checkPermission(int type) {
        if (!PermissionUtils.checkPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return false;
        }
        if (!PermissionUtils.checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return false;
        }
        if (type == SEND_FILE || type == SEND_PHOTO) {
            return true;
        } else if (type == CAPTURE) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.CAMERA);
        } else if (type == AUDIO_RECORD) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.RECORD_AUDIO);
        } else if (type == VIDEO_RECORD) {
            return PermissionUtils.checkPermission(mActivity, Manifest.permission.CAMERA) && PermissionUtils.checkPermission(mActivity, Manifest.permission.RECORD_AUDIO);
        }
        return true;
    }

    protected abstract void init();

    protected abstract void startSendPhoto();

    protected abstract void startCapture();

    protected abstract void startVideoRecord();

    protected abstract void startSendFile();

    protected abstract void startAudioCall();

    protected abstract void startVideoCall();

    protected abstract void startGroupLive();

    protected abstract void startmEnablegift();

    protected abstract void startplaymisc();

    protected abstract void startQuickmessage();

    protected abstract void startLocation();


    @Override
    public void disableAudioInput(boolean disable) {
        mAudioInputDisable = disable;
        if (disable) {
            mAudioInputSwitchButton.setVisibility(GONE);
        } else {
            mAudioInputSwitchButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void disableEmojiInput(boolean disable) {
        mEmojiInputDisable = disable;
        if (disable) {
            mEmojiInputButton.setVisibility(GONE);
        } else {
            mEmojiInputButton.setVisibility(VISIBLE);
        }
    }

    @Override
    public void disableMoreInput(boolean disable) {
        mMoreInputDisable = disable;
        if (disable) {
            mMoreInputButton.setVisibility(GONE);
            mSendTextButton.setVisibility(VISIBLE);
        } else {
            mMoreInputButton.setVisibility(VISIBLE);
            mSendTextButton.setVisibility(GONE);
        }
    }

    @Override
    public void disableSendPhotoAction(boolean disable) {
        mSendPhotoDisable = disable;
    }

    @Override
    public void disableCaptureAction(boolean disable) {
        mCaptureDisable = disable;
    }

    @Override
    public void disableVideoRecordAction(boolean disable) {
        mVideoRecordDisable = disable;
    }

    @Override
    public void disableSendFileAction(boolean disable) {
        mSendFileDisable = disable;
    }

    @Override
    public void disableSendgift(boolean disable) {
        mEnablegift = disable;
    }

    @Override
    public void replaceMoreInput(BaseInputFragment fragment) {
        mMoreInputEvent = fragment;
    }

    @Override
    public void replaceMoreInput(OnClickListener listener) {
        mMoreInputEvent = listener;
    }

    @Override
    public boolean enableAudioCall() {
        //启用音频呼叫
        if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
            return mEnableAudioCall = true;
        } else {
            return mEnableAudioCall = false;
        }
    }

    @Override
    public boolean enableVideoCall() {
        //启用视频通话
        if (TUIKitConfigs.getConfigs().getGeneralConfig().isSupportAVCall()) {
            return mEnableVideoCall = true;
        } else {
            return mEnableVideoCall = false;
        }
    }

    @Override
    public void disablemusic(boolean disable) {
        music = disable;
    }

    @Override
    public void disablelocation(boolean disable) {
        location = disable;
    }

    @Override
    public void disableQuickmessage(boolean disable) {
        Quickmessage = disable;
    }

    @Override
    public void addAction(InputMoreActionUnit action) {
        mInputMoreCustomActionList.add(action);
    }

    @Override
    public void addAction(int i, InputMoreActionUnit action) {
        mInputMoreCustomActionList.add(i, action);
    }

    @Override
    public EditText getInputText() {
        return mTextInput;
    }

    protected void showMoreInputButton(int visibility) {
        if (mMoreInputDisable) {
            return;
        }
        mMoreInputButton.setVisibility(visibility);
    }

    protected void showSendTextButton(int visibility) {
        if (mMoreInputDisable) {
            mSendTextButton.setVisibility(VISIBLE);
        } else {
            mSendTextButton.setVisibility(visibility);
        }
    }

    protected void showEmojiInputButton(int visibility) {
        if (mEmojiInputDisable) {
            return;
        }
        mEmojiInputButton.setVisibility(visibility);
    }

    public void clearCustomActionList() {
        mInputMoreCustomActionList.clear();
    }

    public void setChatInfo(ChatInfo chatInfo) {
        mChatInfo = chatInfo;
    }

    public ChatInfo getChatInfo() {
        return mChatInfo;
    }
}
