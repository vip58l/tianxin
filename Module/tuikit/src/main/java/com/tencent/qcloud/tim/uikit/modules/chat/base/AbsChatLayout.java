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
 * ???VIP??????????????????????????????
 */
public abstract class AbsChatLayout extends ChatLayoutUI implements IChatLayout {
    String TAG = AbsChatLayout.class.getSimpleName();
    public final static int AUDIO = 1;                       //??????
    public final static int VIDEO = 2;                       //??????
    public final static int abnormal = 3;                    //??????
    private info infos;                                      //????????????????????????
    private Allcharge allcharge;                             //?????? ?????? ???????????? ??????
    protected MessageListAdapter mAdapter;                   //???????????????
    private AnimationDrawable mVolumeAnim;
    private Runnable mTypingRunnable = null;
    private UserInfo userInfo;
    private boolean message;

    private MyOpenhelper openhelper = MyOpenhelper.getOpenhelper();
    private personal personal; //???????????????
    private member member;     //???????????????


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
     * ????????????????????????
     */
    private void initListener() {
        getMessageLayout().setPopActionClickListener(new MessageLayout.OnPopActionClickListener() {
            @Override
            public void onCopyClick(int position, MessageInfo msg) {
                //????????????
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
                    // ??????????????????
                    ClipData clip = ClipData.newPlainText("message", copyContent);
                    clipboard.setPrimaryClip(clip);
                }
            }

            @Override
            public void onSendMessageClick(MessageInfo msg, boolean retry) {
                sendMessage(msg, retry); //????????????
            }

            @Override
            public void onDeleteMessageClick(int position, MessageInfo msg) {
                deleteMessage(position, msg); //????????????
            }

            @Override
            public void onRevokeMessageClick(int position, MessageInfo msg) {
                revokeMessage(position, msg);//????????????
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
         * ???????????????????????????????????????
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
        //?????????????????????
        getTitleBar().getLeftGroup().setVisibility(View.VISIBLE);
        //????????????
        getTitleBar().setOnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });

        //???????????????????????? ????????????????????????????????????????????????
        getInputLayout().setMessageHandler(new InputLayout.MessageHandler() {
            @Override
            public void sendMessage(MessageInfo msg) {
                if (getMessageInfo(msg)) {
                    AbsChatLayout.this.sendMessage(msg, false);
                }
            }
        });

        //??????????????????
        getInputLayout().clearCustomActionList();
        if (getMessageLayout().getAdapter() == null) {
            mAdapter = new MessageListAdapter();
            getMessageLayout().setAdapter(mAdapter);
        }

        //????????????????????????
        initListener();
    }

    @Override
    public void setParentLayout(Object parentContainer) {

    }

    /**
     * ???????????????
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
     * ??????????????????
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
     * ????????????
     *
     * @param position
     * @param msg
     */
    protected void deleteMessage(int position, MessageInfo msg) {
        getChatManager().deleteMessage(position, msg);
    }

    /**
     * ????????????
     *
     * @param position
     * @param msg
     */
    protected void revokeMessage(int position, MessageInfo msg) {
        getChatManager().revokeMessage(position, msg);
    }

    /**
     * ????????????????????????
     * @param msg   ??????
     * @param retry ????????????
     */
    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {
        getChatManager().sendMessage(msg, retry, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd(); //??????????????????
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


    /********************** ???????????????VIP???????????? ********************************************/
    //??????????????????VIp??????
    private com.tencent.qcloud.tim.uikit.modules.chat.interfaces.CallBack callBack;

    public void setinitListener(com.tencent.qcloud.tim.uikit.modules.chat.interfaces.CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * ?????????????????????????????????
     */
    public void setInfos(info info) {
        this.infos = info;

    }

    /**
     * ?????????????????????????????????
     *
     * @param allcharge
     */
    public void setAllcharge(Allcharge allcharge) {
        this.allcharge = allcharge;
    }

    /**
     * ???????????????????????????
     */
    public void setmessage(boolean message) {
        this.message = message;
    }

    /**
     * ????????????????????????
     * @param member
     */
    public void setMember(com.tencent.opensource.model.member member) {
        this.member = member;
    }

    /**
     * ????????????????????????
     * @param personal
     */
    public void setPersonal(com.tencent.opensource.model.personal personal) {
        this.personal = personal;
    }

    /**
     * ChAT?????????????????????
     *?????????????????????
     * @param msg
     */
    protected boolean getMessageInfo(MessageInfo msg) {

        /**
         * ??????????????????????????????
         */
        if (dislog(personal)) {
            callBack.Completedata();
            return false;
        }

        //??????????????????
        userInfo = UserInfo.getInstance();
        if (userInfo.getState() >= abnormal) {
            callBack.fengjin();
            return false;
        }

        //???????????????
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            callBack.avatar();
            Log.d(TAG, "???????????????????????????: ");
            return false;
        }


        //????????????????????????????????????????????????
        if (member!=null&&member.getKefu()==1){
            return true;
        }


        //????????????????????? ??????????????????
        allcharge = allcharge == null ? Allcharge.getInstance() : allcharge;
        if (allcharge == null) {
            if (callBack != null) {
                callBack.onfall(); //?????????????????? ??????
            }
            return false;
        }

        List<UserModel> contactList = msg.getContactList();
        int audio = allcharge != null ? (int) allcharge.getAudio() : 0;       //????????????????????????
        int video = allcharge != null ? (int) allcharge.getVideo() : 0;       //????????????????????????
        int money = infos != null ? (int) infos.getMoney() : 0;               //????????????????????????

        //???????????????????????? ??????????????????
        boolean isonvip = userInfo.getVip() == 0 && userInfo.getSex().equals("1") && userInfo.gettRole() == 0 ? true : false;

        switch (msg.getTYPE()) {
            case AUDIO: //?????????????????????tRole=1???
                if (money > audio || userInfo.gettRole() == 1) {
                    //??????????????????????????????
                    if (!permission()) {
                        callBack.videoCall(contactList, msg.getTYPE()); //??????????????????
                        return false;
                    }

                    //??????????????????????????????
                    TRTCAudioCallActivity.startCallSomeone(getContext().getApplicationContext(), contactList);
                } else {
                    //?????????????????? ?????????????????????
                    callBack.onmoney();
                }
                return false;
            case VIDEO: //???????????? ?????????tRole=1???
                if (money > video || userInfo.gettRole() == 1) {
                    //??????????????????????????????
                    if (!permission()) {
                        callBack.videoCall(contactList, msg.getTYPE()); //??????????????????
                        return false;
                    }
                    //??????????????????????????????
                    TRTCVideoCallActivity.startCallSomeone(getContext().getApplicationContext(), contactList);
                } else {
                    //?????????????????? ?????????????????????
                    callBack.onmoney();
                }
                return false;
            case 11: //??????
                return false;
        }

        //???VIP???????????????????????????????????? ?????? ?????????
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

        //?????????????????????tRole=1???
        if (userInfo.gettRole() == 0) {
            //?????????VIP??????sex=1  ?????????????????????sex=2
            if (userInfo.getVip() == 0 && userInfo.getSex().equals("1") && !message) {
                Log.d(TAG, "???????????????????????????????????????????????????");
                //???????????????????????????????????????????????????
                if (userInfo.getJinbi() >= allcharge.getMoney()) {
                    return showMessage3();
                } else {
                    //????????????????????????????????????
                    return showMessage2();
                }
            }

            //?????????VIP??????sex=1  ?????????????????????sex=2
            if (isonvip) {
                Log.d(TAG, "???????????????????????????????????????????????????: ");
                callBack.sendMessage();
                return true;
            }
        }

        return true;
    }

    /********************** ???????????????VIP???????????? ********************************************/

    /**
     * ?????????????????????????????????
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
     * ??????????????????????????????????????????
     *
     * @return
     */
    private boolean showMessage1() {
        //???????????????????????????
        if (userInfo.isGojinbi()) {
            callBack.reducemoney();
            return true;
        } else {
            //??????????????????????????????
            callBack.moneyshow();
            return false;
        }
    }

    /**
     * ???????????????????????? ?????????
     * ??????????????????????????? ????????????VIP???????????????
     *
     * @return
     */
    private boolean showMessage2() {
        callBack.chta();
        return false;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    private boolean showMessage3() {
        callBack.reducemoney();
        return true;
    }

    /**
     * ????????????????????????????????????????????????
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
