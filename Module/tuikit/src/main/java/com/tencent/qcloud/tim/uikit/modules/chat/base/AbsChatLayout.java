package com.tencent.qcloud.tim.uikit.modules.chat.base;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.CallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.IChatProvider;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageListAdapter;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.util.ArrayList;
import java.util.List;

/**
 * 非VIP会员设置弹出提示消息
 */
public abstract class AbsChatLayout extends ChatLayoutUI implements IChatLayout {
    String TAG = AbsChatLayout.class.getSimpleName();
    public final static int AUDIO = 1;                       //音频
    public final static int VIDEO = 2;                       //视频
    public final static int abnormal = 3;                    //封号
    private info infos;                                      //用户金币余额情况
    private Allcharge allcharge;                             //视频 音频 扣费限制 对像
    protected MessageListAdapter mAdapter;                   //聊天适配器
    private AnimationDrawable mVolumeAnim;
    private Runnable mTypingRunnable = null;
    private UserInfo userInfo;
    private boolean message;

    private MyOpenhelper openhelper = MyOpenhelper.getOpenhelper();
    private personal personal; //自己的资料
    private member member;     //对方的资料


    public AbsChatLayout(Context context) {
        super(context);
    }

    public AbsChatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public AbsChatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private ChatProvider.TypingListener mTypingListener = new ChatProvider.TypingListener() {
        @Override
        public void onTyping() {
            final String oldTitle = getTitleBar().getMiddleTitle().getText().toString();
            getTitleBar().getMiddleTitle().setText(R.string.typing);
            if (mTypingRunnable == null) {
                mTypingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        getTitleBar().getMiddleTitle().setText(oldTitle);
                    }
                };
            }
            getTitleBar().getMiddleTitle().removeCallbacks(mTypingRunnable);
            getTitleBar().getMiddleTitle().postDelayed(mTypingRunnable, 3000);
        }

    };


    /**
     * 监听消息发送回调
     */
    private void initListener() {
        getMessageLayout().setPopActionClickListener(new MessageLayout.OnPopActionClickListener() {
            @Override
            public void onCopyClick(int position, MessageInfo msg) {
                //复制消息
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard == null || msg == null) {
                    return;
                }
                if (msg.getMsgType() == MessageInfo.MSG_TYPE_TEXT) {
                    V2TIMTextElem textElem = msg.getTimMessage().getTextElem();
                    String copyContent;
                    if (textElem == null) {
                        copyContent = (String) msg.getExtra();
                    } else {
                        copyContent = textElem.getText();
                    }
                    // 复制文本消息
                    ClipData clip = ClipData.newPlainText("message", copyContent);
                    clipboard.setPrimaryClip(clip);
                }
            }

            @Override
            public void onSendMessageClick(MessageInfo msg, boolean retry) {
                sendMessage(msg, retry); //发送消息
            }

            @Override
            public void onDeleteMessageClick(int position, MessageInfo msg) {
                deleteMessage(position, msg); //删除消息
            }

            @Override
            public void onRevokeMessageClick(int position, MessageInfo msg) {
                revokeMessage(position, msg);//撤回消息
            }
        });
        getMessageLayout().setLoadMoreMessageHandler(new MessageLayout.OnLoadMoreHandler() {
            @Override
            public void loadMore() {
                loadMessages();
            }
        });
        getMessageLayout().setEmptySpaceClickListener(new MessageLayout.OnEmptySpaceClickListener() {
            @Override
            public void onClick() {
                getInputLayout().hideSoftInput();
            }
        });
        /**
         * 设置消息列表空白处点击处理
         */
        getMessageLayout().addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child == null) {
                        getInputLayout().hideSoftInput();
                    } else if (child instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) child;
                        final int count = group.getChildCount();
                        float x = e.getRawX();
                        float y = e.getRawY();
                        View touchChild = null;
                        for (int i = count - 1; i >= 0; i--) {
                            final View innerChild = group.getChildAt(i);
                            int[] position = new int[2];
                            innerChild.getLocationOnScreen(position);
                            if (x >= position[0]
                                    && x <= position[0] + innerChild.getMeasuredWidth()
                                    && y >= position[1]
                                    && y <= position[1] + innerChild.getMeasuredHeight()) {
                                touchChild = innerChild;
                                break;
                            }
                        }
                        if (touchChild == null) {
                            getInputLayout().hideSoftInput();
                        }
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        getInputLayout().setChatInputHandler(new InputLayout.ChatInputHandler() {
            @Override
            public void onInputAreaClick() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd();
                    }
                });
            }

            @Override
            public void onRecordStatusChanged(int status) {
                switch (status) {
                    case RECORD_START:
                        startRecording();
                        break;
                    case RECORD_STOP:
                        stopRecording();
                        break;
                    case RECORD_CANCEL:
                        cancelRecording();
                        break;
                    case RECORD_TOO_SHORT:
                    case RECORD_FAILED:
                        stopAbnormally(status);
                        break;
                    default:
                        break;
                }
            }

            private void startRecording() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        AudioPlayer.getInstance().stopPlay();
                        mRecordingGroup.setVisibility(View.VISIBLE);
                        mRecordingIcon.setImageResource(R.drawable.recording_volume);
                        mVolumeAnim = (AnimationDrawable) mRecordingIcon.getDrawable();
                        mVolumeAnim.start();
                        mRecordingTips.setTextColor(Color.WHITE);
                        mRecordingTips.setText(TUIKit.getAppContext().getString(R.string.down_cancle_send));
                    }
                });
            }

            private void stopRecording() {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVolumeAnim.stop();
                        mRecordingGroup.setVisibility(View.GONE);
                    }
                }, 500);
            }

            private void stopAbnormally(final int status) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mVolumeAnim.stop();
                        mRecordingIcon.setImageResource(R.drawable.ic_volume_dialog_length_short);
                        mRecordingTips.setTextColor(Color.WHITE);
                        if (status == RECORD_TOO_SHORT) {
                            mRecordingTips.setText(TUIKit.getAppContext().getString(R.string.say_time_short));
                        } else {
                            mRecordingTips.setText(TUIKit.getAppContext().getString(R.string.record_fail));
                        }
                    }
                });
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecordingGroup.setVisibility(View.GONE);
                    }
                }, 1000);
            }

            private void cancelRecording() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mRecordingIcon.setImageResource(R.drawable.ic_volume_dialog_cancel);
                        mRecordingTips.setText(TUIKit.getAppContext().getString(R.string.up_cancle_send));
                    }
                });
            }
        });
    }

    @Override
    public void initDefault() {
        //初始化面板数据
        getTitleBar().getLeftGroup().setVisibility(View.VISIBLE);
        //返回关闭
        getTitleBar().setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });

        //提交发送消息内容 在这里监听判断用户的行为是否合法
        getInputLayout().setMessageHandler(new InputLayout.MessageHandler() {
            @Override
            public void sendMessage(MessageInfo msg) {
                if (getMessageInfo(msg)) {
                    AbsChatLayout.this.sendMessage(msg, false);
                }
            }
        });

        //清空输入消息
        getInputLayout().clearCustomActionList();
        if (getMessageLayout().getAdapter() == null) {
            mAdapter = new MessageListAdapter();
            getMessageLayout().setAdapter(mAdapter);
        }

        //监听消息发送回调
        initListener();
    }

    @Override
    public void setParentLayout(Object parentContainer) {

    }

    /**
     * 滚动到底部
     */
    public void scrollToEnd() {
        getMessageLayout().scrollToEnd();
    }

    public void setDataProvider(IChatProvider provider) {
        if (provider != null) {
            ((ChatProvider) provider).setTypingListener(mTypingListener);
        }
        if (mAdapter != null) {
            mAdapter.setDataSource(provider);
            getChatManager().setLastMessageInfo(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
        }
    }

    public abstract ChatManagerKit getChatManager();

    @Override
    public void loadMessages() {
        loadChatMessages(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
    }

    /**
     * 载入聊天消息
     *
     * @param lastMessage
     */
    public void loadChatMessages(final MessageInfo lastMessage) {
        getChatManager().loadChatMessages(lastMessage, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (lastMessage == null && data != null) {
                    setDataProvider((ChatProvider) data);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

                ToastUtil.toastLongMessage(module + "===== " + errCode + "===== " + errMsg);
                if (lastMessage == null) {
                    setDataProvider(null);
                }
            }
        });
    }

    /**
     * 删除消息
     *
     * @param position
     * @param msg
     */
    protected void deleteMessage(int position, MessageInfo msg) {
        getChatManager().deleteMessage(position, msg);
    }

    /**
     * 撤回消息
     *
     * @param position
     * @param msg
     */
    protected void revokeMessage(int position, MessageInfo msg) {
        getChatManager().revokeMessage(position, msg);
    }

    /**
     * 消息发送出去操作
     * @param msg   消息
     * @param retry 是否重试
     */
    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {
        getChatManager().sendMessage(msg, retry, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd(); //消息发送成功
                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                switch (errCode) {
                    case 10017:
                        ToastUtil.toastLongMessage(getContext().getString(R.string.tv_msg_tt21));
                        break;
                    default:
                        Log.d(TAG, "errMsg:" + errMsg + "errCode:" + errCode + "module:" + module);
                        ToastUtil.toastLongMessage("errMsg:" + errMsg + "errCode:" + errCode + "module:" + module);
                        break;
                }


            }
        });
    }

    @Override
    public void exitChat() {
        getTitleBar().getMiddleTitle().removeCallbacks(mTypingRunnable);
        AudioPlayer.getInstance().stopRecord();
        AudioPlayer.getInstance().stopPlay();
        if (getChatManager() != null) {
            getChatManager().destroyChat();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        exitChat();
    }


    /********************** 自定义聊天VIP限制功能 ********************************************/
    //用来控制是否VIp用户
    private com.tencent.qcloud.tim.uikit.modules.chat.interfaces.CallBack callBack;

    public void setinitListener(com.tencent.qcloud.tim.uikit.modules.chat.interfaces.CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 初始化传入用户金币余额
     */
    public void setInfos(info info) {
        this.infos = info;

    }

    /**
     * 初始化获取金币扣费配置
     *
     * @param allcharge
     */
    public void setAllcharge(Allcharge allcharge) {
        this.allcharge = allcharge;
    }

    /**
     * 初始化发送消息限制
     */
    public void setmessage(boolean message) {
        this.message = message;
    }

    /**
     * 初始化对方的资料
     * @param member
     */
    public void setMember(com.tencent.opensource.model.member member) {
        this.member = member;
    }

    /**
     * 初始化自己的资料
     * @param personal
     */
    public void setPersonal(com.tencent.opensource.model.personal personal) {
        this.personal = personal;
    }

    /**
     * ChAT用户发消息处理
     *拦截回调等操作
     * @param msg
     */
    protected boolean getMessageInfo(MessageInfo msg) {

        /**
         * 聊天窗口提示补全资料
         */
        if (dislog(personal)) {
            callBack.Completedata();
            return false;
        }

        //判断封号状态
        userInfo = UserInfo.getInstance();
        if (userInfo.getState() >= abnormal) {
            callBack.fengjin();
            return false;
        }

        //未上传头像
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            callBack.avatar();
            Log.d(TAG, "请你选上传一张头像: ");
            return false;
        }


        //如果对方是客服号不限制与客服沟通
        if (member!=null&&member.getKefu()==1){
            return true;
        }


        //最低消费金币空 返回发送失败
        allcharge = allcharge == null ? Allcharge.getInstance() : allcharge;
        if (allcharge == null) {
            if (callBack != null) {
                callBack.onfall(); //对像获取空值 回调
            }
            return false;
        }

        List<UserModel> contactList = msg.getContactList();
        int audio = allcharge != null ? (int) allcharge.getAudio() : 0;       //语音通话最低消费
        int video = allcharge != null ? (int) allcharge.getVideo() : 0;       //视频通话最低消费
        int money = infos != null ? (int) infos.getMoney() : 0;               //用户金币余额情况

        //判断用户身份状态 普通屌丝用户
        boolean isonvip = userInfo.getVip() == 0 && userInfo.getSex().equals("1") && userInfo.gettRole() == 0 ? true : false;

        switch (msg.getTYPE()) {
            case AUDIO: //音频通话【主播tRole=1】
                if (money > audio || userInfo.gettRole() == 1) {
                    //先判断电话是否有权限
                    if (!permission()) {
                        callBack.videoCall(contactList, msg.getTYPE()); //回调申请权限
                        return false;
                    }

                    //进入拨打语音通话聊天
                    TRTCAudioCallActivity.startCallSomeone(getContext().getApplicationContext(), contactList);
                } else {
                    //金币余额不足 回调数据给外部
                    callBack.onmoney();
                }
                return false;
            case VIDEO: //视频通话 【主播tRole=1】
                if (money > video || userInfo.gettRole() == 1) {
                    //先判断电话是否有权限
                    if (!permission()) {
                        callBack.videoCall(contactList, msg.getTYPE()); //回调申请权限
                        return false;
                    }
                    //进入拨打视频通话聊天
                    TRTCVideoCallActivity.startCallSomeone(getContext().getApplicationContext(), contactList);
                } else {
                    //金币余额不足 回调数据给上级
                    callBack.onmoney();
                }
                return false;
            case 11: //拍照
                return false;
        }

        //非VIP会员不允许发布图片，视频 音频 等文件
        switch (msg.getMsgType()) {
            case MessageInfo.MSG_TYPE_IMAGE:
                if (isonvip) {
                    callBack.Sendpictures();
                    return false;
                }
            case MessageInfo.MSG_TYPE_AUDIO:
                if (isonvip) {
                    callBack.Sendvoice();
                    return false;
                }
            case MessageInfo.MSG_TYPE_VIDEO:
                if (isonvip) {
                    callBack.Sendvideo();
                    return false;
                }
            case MessageInfo.MSG_TYPE_LOCATION:
                if (isonvip) {
                    callBack.Sendlocation();
                    return false;
                }
        }

        //我还不是【主播tRole=1】
        if (userInfo.gettRole() == 0) {
            //我不是VIP会员sex=1  女性会员无限制sex=2
            if (userInfo.getVip() == 0 && userInfo.getSex().equals("1") && !message) {
                Log.d(TAG, "判断是否有金币，如果有金币执行扣币");
                //判断是否有金币，如果有金币执行扣币
                if (userInfo.getJinbi() >= allcharge.getMoney()) {
                    return showMessage3();
                } else {
                    //提示用户帐户没有金币不足
                    return showMessage2();
                }
            }

            //我不是VIP会员sex=1  女性会员无限制sex=2
            if (isonvip) {
                Log.d(TAG, "允许发送消息并向服务器查询剩于条数: ");
                callBack.sendMessage();
                return true;
            }
        }

        return true;
    }

    /********************** 自定义聊天VIP限制功能 ********************************************/

    /**
     * 循环检测是否有相关权限
     *
     * @return
     */
    public boolean permission() {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.CAMERA);
        list.add(Manifest.permission.RECORD_AUDIO);
        for (String permission : list) {
            int granted = ContextCompat.checkSelfPermission(getContext(), permission);
            if (granted != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 提示用户是否允许我们扣除金币
     *
     * @return
     */
    private boolean showMessage1() {
        //用户已同意扣除金币
        if (userInfo.isGojinbi()) {
            callBack.reducemoney();
            return true;
        } else {
            //询问是否同意扣除金币
            callBack.moneyshow();
            return false;
        }
    }

    /**
     * 提示用户金币不足 请充值
     * 日聊天次数已达上限 提示购买VIP或充值金币
     *
     * @return
     */
    private boolean showMessage2() {
        callBack.chta();
        return false;
    }

    /**
     * 扣除金币余额
     *
     * @return
     */
    private boolean showMessage3() {
        callBack.reducemoney();
        return true;
    }

    /**
     * 完善个人信息，才能解锁更多功能哦
     *
     * @param personal
     * @return
     */
    public static boolean dislog(personal personal) {
        if (personal == null) {
            return false;
        } else if (personal.getAge() < 18) {
            return true;
        } else if (personal.getHeight() <= 100) {
            return true;
        } else if (personal.getWeight() <= 10) {
            return true;
        } else if (TextUtils.isEmpty(personal.getEducation())) {
            return true;
        } else if (TextUtils.isEmpty(personal.getOccupation())) {
            return true;
        } else if (TextUtils.isEmpty(personal.getPree())) {
            return true;
        } else if (TextUtils.isEmpty(personal.getCforsds())) {
            return true;
        } else if (TextUtils.isEmpty(personal.getPesigntext())) {
            return true;
        }
        return false;
    }

}
