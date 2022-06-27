/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/21 0021
 */


package com.tianxin.IMtencent.chta;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.location.AMapLocation;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.FriendProfileActivity;
import com.tianxin.IMtencent.scenes.LiveRoomAnchorActivity;
import com.tianxin.IMtencent.utils.DemoLog;
import com.tianxin.Module.McallBack;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.dialog.dialgo_chat;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_show_activity;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.chat;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.present;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.SystemUtil;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.activity.ZYservices.activity_myper;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.activity_svip;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.amap.baidumap;
import com.tianxin.amap.gpsmap;
import com.tianxin.amap.lbsamap;
import com.tianxin.dialog.dialog_del_notice;
import com.tianxin.dialog.dialog_item_gift;
import com.tianxin.dialog.dialog_item_Avatar;
import com.tianxin.dialog.dialog_itemmap;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_list_msg;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.dialog.dialog_servicemusic;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.AudioPlayer;
import com.tencent.qcloud.tim.uikit.component.NoticeLayout;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.AbsChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.BaseInputFragment;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.interfaces.CallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.StartGroupMemberSelectActivity;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 加入到ACTIVITY显示
 * 对接文档说明
 * https://im.sdk.qcloud.com/doc/zh-cn/md_introduction_Android%E6%A6%82%E8%A7%88.html
 */
public class ChatFragment extends Fragment {
    private String TAG = ChatFragment.class.getName();
    private View mBaseView;
    private ChatLayout mChatLayout;
    private TitleBarLayout mTitleBar;
    private ChatInfo mChatInfo;
    private UserInfo userInfo;
    private dialog_list_msg dialogListMsg;
    public static int PERMISSIONS = 10001;
    private List<UserModel> scontactList;
    private int sTYPE;
    private Datamodule datamodule;
    private Context context;
    private Activity activity;
    private cs_alipay csAlipay;
    private info infos;
    private Allcharge allcharge;
    private com.tianxin.amap.gpsmap gpsmap = new gpsmap();
    private MyOpenhelper openhelper;
    private personal personal; //我的个人资料
    private member member;     //对方的个人资料

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = getActivity();
        this.userInfo = UserInfo.getInstance();

        Bundle bundle = getArguments();
        //接解析对方的数据
        mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
        csAlipay = new cs_alipay(context, paymnets);         //支付宝初始化
        datamodule = new Datamodule(getContext(), paymnets); //请求公共类
        openhelper = MyOpenhelper.getOpenhelper();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mBaseView = LayoutInflater.from(getActivity()).inflate(R.layout.chatfragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getContext();
        // 从布局文件中获取聊天面板
        mChatLayout = view.findViewById(R.id.chat_layout);
        // 单聊面板的默认 UI 和交互初始化
        mChatLayout.initDefault();
        // 传入 ChatInfo 的实例，这个实例必须包含必要的聊天信息，一般从调用方传入 单聊 群聊初始化
        mChatLayout.setChatInfo(mChatInfo);
        //获取单聊面板的标题栏
        mTitleBar = mChatLayout.getTitleBar();
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        mTitleBar.setOnLeftClickListener(v -> getActivity().finish());
        //单聊C2C聊天
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            //聊天窗口右上角图标事件
            mTitleBar.setOnRightClickListener(v -> starinten());
        }
        //群组聊天
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            mChatLayout.getInputLayout().disableSendgift(true);//礼物隐藏图标
            V2TIMManager.getConversationManager().getConversation(mChatInfo.getId(), new V2TIMValueCallback<V2TIMConversation>() {
                @Override
                public void onError(int code, String desc) {
                    Log.e(TAG, "getConversation error:" + code + ", desc:" + desc);
                }

                @Override
                public void onSuccess(V2TIMConversation v2TIMConversation) {
                    if (v2TIMConversation == null) {
                        DemoLog.d(TAG, "getConversation failed");
                        return;
                    }
                    mChatInfo.setAtInfoList(v2TIMConversation.getGroupAtInfoList());
                    V2TIMMessage lastMessage = v2TIMConversation.getLastMessage();
                    updateAtInfoLayout();
                    mChatLayout.getAtInfoLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final List<V2TIMGroupAtInfo> atInfoList = mChatInfo.getAtInfoList();
                            if (atInfoList == null || atInfoList.isEmpty()) {
                                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                                return;
                            } else {
                                mChatLayout.getChatManager().getAtInfoChatMessages(atInfoList.get(atInfoList.size() - 1).getSeq(), lastMessage, new IUIKitCallBack() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        mChatLayout.getMessageLayout().scrollToPosition((int) atInfoList.get(atInfoList.size() - 1).getSeq());
                                        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mChatLayout.getMessageLayout().getLayoutManager();
                                        mLayoutManager.scrollToPositionWithOffset((int) atInfoList.get(atInfoList.size() - 1).getSeq(), 0);
                                        atInfoList.remove(atInfoList.size() - 1);
                                        mChatInfo.setAtInfoList(atInfoList);
                                        updateAtInfoLayout();
                                    }

                                    @Override
                                    public void onError(String module, int errCode, String errMsg) {
                                        DemoLog.d(TAG, "getAtInfoChatMessages failed");
                                    }
                                });
                            }
                        }
                    });

                }
            });
        }

        //点击聊天头像
        mChatLayout.getMessageLayout().setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                //因为adapter中第一条为加载条目，位置需减1
                mChatLayout.getMessageLayout().showItemPopMenu(position - 1, messageInfo, view);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
                if (null == messageInfo) {
                    return;
                }
                //FriendProfileActivity(messageInfo);
                FriendProfileActivity(messageInfo, 0);
            }
        });

        //群成员
        mChatLayout.getInputLayout().setStartActivityListener(new InputLayout.onStartActivityListener() {
            @Override
            public void onStartGroupMemberSelectActivity() {
                Intent intent = new Intent(context, StartGroupMemberSelectActivity.class);
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.setId(mChatInfo.getId());
                groupInfo.setChatName(mChatInfo.getChatName());
                intent.putExtra(TUIKitConstants.Group.GROUP_INFO, groupInfo);
                startActivityForResult(intent, 1);
            }

            @Override
            public boolean handleStartGroupLiveActivity() {
                // 打开群直播创建直播房间
                LiveRoomAnchorActivity.start(context, mChatInfo.getId());
                return true;
            }
        });

        //定位功能监听地图功能暂未开放
        mChatLayout.getMessageLayout().setgetonMessagegps(new MessageLayout.OnMessagegps() {
            @Override
            public void OnItemClickListener(String Desc, double latitude, double longitude) {
                Log.d(TAG, String.format("%s %s %s", Desc, latitude, longitude));
                gpsmap.setDesc(Desc);
                gpsmap.setLatitude(latitude);
                gpsmap.setLongitude(longitude);

                //弹出选择定位导航
                dialog_itemmap.myshow(getContext(), paymnets);

                //https://www.jianshu.com/p/12c6253f1851
                //安卓有两种方式启动Activity，一种是显示启动，另外一种是隐式启动
                //主app com.paixide.activity.Memberverify.activity.Memberverify.Activity_livebroadcast
                //通过隐式启动主app的 activity

//                Intent intent = new Intent();
//                intent.setAction("livebroadcastActivity");
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().startActivity(intent);

            }
        });

        //现在插播一条聊天顶部TOP广告
        getNoticeLayouts();
        //隐藏不要的功能
        //hideChatLayout();
        //增加自定义的功能
        showhideChatLayout();
        //替换点击“+”的事件
        //showhideChatLayout2();
        //替换点击“+”的事件
        //showhideChatLayout3();

        //初始化网络数据
        setmBaseView();

        //子模块下的组件 com.tencent.qcloud.tim.uikit
        showuikit();
    }

    private void showuikit() {
        //子模块下的组件 com.tencent.qcloud.tim.uikit
        com.tencent.qcloud.tim.uikit.modules.chat.layout.input.TIMMentionEditText chat_message_input = getView().findViewById(R.id.chat_message_input);
        chat_message_input.setTextSize(16);
        chat_message_input.setHint(userInfo.getSex().equals("2") ? R.string.chat_sex2 : R.string.chat_sex1);

        try {
            //编辑框输入内容
            int conuntQuery = MyOpenhelper.getOpenhelper().conuntQuery(MyOpenhelper.cahtreply, 1);
            if (conuntQuery <= 15) {
                MyOpenhelper.getOpenhelper().insert(String.format("insert into %s(title,type) values('%s',%s)", MyOpenhelper.cahtreply, "caht查看聊天窗口", 1));
            } else {
                chat_message_input.setHint("");
            }

            //提示聊天可以赚钱
            if (userInfo.getSex().equals("2") && !Config_Msg.getInstance().isChat()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialgo_chat.star(context);
                    }
                }, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 点击聊天窗口头像
     *
     * @param messageInfo
     */
    private void FriendProfileActivity(MessageInfo messageInfo) {
        ChatInfo info = new ChatInfo();
        info.setType(mChatInfo.getType());
        info.setId(messageInfo.getFromUser());
        Intent intent = new Intent(DemoApplication.instance(), FriendProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, info);
        startActivity(intent);
    }

    /**
     * 点击聊天窗口头像
     *
     * @param messageInfo
     */
    private void FriendProfileActivity(MessageInfo messageInfo, int type) {
        Intent intent = new Intent(DemoApplication.instance(), BuildConfig.TYPE == Constants.TENCENT2 ? activity_myper.class : activity_picenter.class);
        intent.putExtra(Constants.USERID, messageInfo.getFromUser());
        startActivity(intent);
    }

    /**
     * 现在插播一条广告
     */
    private void getNoticeLayouts() {
        if (userInfo.getVip() == Constants.TENCENT || userInfo.getSex().equals("2")) {
            //VIP会员或女性无广告
            return;
        }
        /**
         * 获取聊天窗口 Notice 区域 Layout
         * @return
         */
        //NoticeLayout getNoticeLayout();
        /**
         * 获取聊天窗口 Message 区域 Layout
         * @return
         */
        //MessageLayout getMessageLayout();
        /**
         * 获取聊天窗口 Input 区域 Layout
         * @return
         */
        //InputLayout getInputLayout();

        // 从 ChatLayout 里获取 NoticeLayout
        NoticeLayout noticeLayout = mChatLayout.getNoticeLayout();
        // 可以使通知区域一致展示
        noticeLayout.alwaysShow(true);
        // 设置通知主题
        noticeLayout.getContent().setText(R.string.tv_msg184);
        // 设置通知提醒文字
        noticeLayout.getContentExtra().setText(R.string.tv_msg185);
        // 设置通知的点击事件
        noticeLayout.setOnNoticeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeLayout.alwaysShow(false);
                noticeLayout.removeAllViews();
                startActivity(new Intent(context, activity_svip.class));

                //充值金币
                //Dialog_bottom dialogBottom = new Dialog_bottom(DemoApplication.instance());
                //dialogBottom.setCanceledOnTouchOutside(false);
                //dialogBottom.show();
            }
        });


        //从ChatLayout 里获取 MessageLayout
        //MessageLayout messageLayout = mChatLayout.getMessageLayout();
        //设置聊天背景
        //messageLayout.setBackground(new ColorDrawable(0xB0E2FF00));


    }

    /**
     * 隐藏不要的功能
     */
    private void hideChatLayout() {
        InputLayout inputLayout = mChatLayout.getInputLayout();
        //隐藏拍照并发送
        inputLayout.disableCaptureAction(true);
        //隐藏发送文件
        inputLayout.disableSendFileAction(true);
        //隐藏发送图片
        inputLayout.disableSendPhotoAction(true);
        //隐藏摄像并发送
        inputLayout.disableVideoRecordAction(true);
        //隐藏音乐
        inputLayout.disablemusic(true);
        //隐藏快捷消息
        inputLayout.disableQuickmessage(true);
        //隐藏定位
        inputLayout.disablelocation(true);
        //隐藏礼物控制面板
        inputLayout.disableSendgift(true);
    }

    /**
     * 增加自定义的功能
     */
    private void showhideChatLayout() {
        InputLayout inputLayout = mChatLayout.getInputLayout();
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_C2C) {
            inputLayout.enableAudioCall(); //启用音频呼叫
            inputLayout.enableVideoCall(); //启用视频通话
        }
        if (mChatInfo.getType() == V2TIMConversation.V2TIM_GROUP) {
            inputLayout.disablemusic(true); //隐藏文件发送
        }
        inputLayout.disableSendFileAction(true); //隐藏文件发送
        inputLayout.disableVideoRecordAction(true); //隐藏摄像
        inputLayout.disableSendFileAction(true); //隐藏文件发送

        //注意图标排序位置于当前增加图标数量目前 音乐 快捷词语 定位功能1-3
        // inputLayout.addAction(action); inputLayout.addAction(1,action);

        /**
         * 面板监听事件回调
         */
        inputLayout.setOngiftListener(ongiftListener);
    }

    /**
     * 弹出音乐播放
     */
    public void startplaymisc() {
        dialog_servicemusic.playmisc(context); //弹出音乐DIALOG
    }

    /**
     * 发送定位位置
     */
    private void LocationMessage() {

        //检查聊天限制
        if (openhelper.chatQuerychat()) {
            showdialog();
            return;
        }

        //快捷消息发送
        if (userInfo.getState() >= Constants.TENCENT3) {
            callBack.fengjin();
            return;
        }

        //要求用户资料补全
        if (personal != null && callBack != null) {
            boolean dislog = AbsChatLayout.dislog(personal);
            if (dislog) {
                callBack.Completedata();
                return;
            }
        }


        //检查定位功能并申请定位权限
        if (!ActivityLocation.checkpermissions(getActivity())) {
            return;
        }

        //发送定位
        lbsamap.getmyLocation(callback);
    }

    /**
     * 发送礼物内容
     *
     * @param giftInfo
     */
    private void GiftMessage(GiftInfo giftInfo) {
        if (TextUtils.isEmpty(giftInfo.title) || TextUtils.isEmpty(giftInfo.giftPicUrl)) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali5));
            return;
        }
        MessageInfo info = MessageInfoUtil.LocationMessage(String.format("%s|%s|%s", giftInfo.title, giftInfo.giftPicUrl, giftInfo.price), 0, 0);
        mChatLayout.sendMessage(info, false);
    }

    /**
     * 定位位置
     */
    private void getTIMLocationElem() {
        TIMLocationElem elem = new TIMLocationElem();
        TIMMessage message = new TIMMessage();
        elem.setLatitude(110.79);   //设置纬度
        elem.setLongitude(32.65);   //设置经度
        elem.setDesc("我的位置");
        message.addElement(elem);
    }

    /**
     * 替换点击“+”的事件
     */
    private void showhideChatLayout2() {
        // 从 ChatLayout 里获取 InputLayout
        InputLayout inputLayout = mChatLayout.getInputLayout();
        // 可以用自定义的事件来替换更多功能的入口
        inputLayout.replaceMoreInput(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toastShortMessage("自定义的更多功能按钮事件");
                MessageInfo info = MessageInfoUtil.buildTextMessage("自定义的消息");
                mChatLayout.sendMessage(info, false);
            }
        });

    }

    /**
     * 替换点击“+”弹出的面板
     */
    private void showhideChatLayout3() {
        //从 ChatLayout 里获取 InputLayout
        InputLayout inputLayout = mChatLayout.getInputLayout();
        //可以用自定义的 fragment 来替换更多功能
        inputLayout.replaceMoreInput(new CustomInputFragment());
    }

    private void updateAtInfoLayout() {
        int atInfoType = getAtInfoType(mChatInfo.getAtInfoList());
        switch (atInfoType) {
            case V2TIMGroupAtInfo.TIM_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(DemoApplication.instance().getString(R.string.ui_at_me));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(DemoApplication.instance().getString(R.string.ui_at_all));
                break;
            case V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME:
                mChatLayout.getAtInfoLayout().setVisibility(VISIBLE);
                mChatLayout.getAtInfoLayout().setText(DemoApplication.instance().getString(R.string.ui_at_all_me));
                break;
            default:
                mChatLayout.getAtInfoLayout().setVisibility(GONE);
                break;

        }
    }

    private int getAtInfoType(List<V2TIMGroupAtInfo> atInfoList) {
        int atInfoType = 0;
        boolean atMe = false;
        boolean atAll = false;

        if (atInfoList == null || atInfoList.isEmpty()) {
            return V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        for (V2TIMGroupAtInfo atInfo : atInfoList) {
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ME) {
                atMe = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL) {
                atAll = true;
                continue;
            }
            if (atInfo.getAtType() == V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME) {
                atMe = true;
                atAll = true;
                continue;
            }
        }

        if (atAll && atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL_AT_ME;
        } else if (atAll) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ALL;
        } else if (atMe) {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_ME;
        } else {
            atInfoType = V2TIMGroupAtInfo.TIM_AT_UNKNOWN;
        }

        return atInfoType;

    }

    @Override
    public void onPause() {
        super.onPause();
        AudioPlayer.getInstance().stopPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatLayout != null) {
            mChatLayout.exitChat();
        }
    }

    /**
     * 跳转到个人主页详情
     */
    public void starinten() {
        Intent intent = new Intent(DemoApplication.instance(), BuildConfig.TYPE == Constants.TENCENT2 ? activity_myper.class : activity_picenter.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.USERID, mChatInfo.getId());
        startActivity(intent);
    }

    /**
     * 转到个人详情页置顶个人资料展示页
     */
    public void starintens() {
        Intent intent = new Intent(context, FriendProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(TUIKitConstants.ProfileType.CONTENT, mChatInfo);
        DemoApplication.instance().startActivity(intent);
    }

    /**
     * 申请读写权限
     */
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);                            //相机权限
            list.add(Manifest.permission.RECORD_AUDIO);                       //SD卡写入
            for (String permission : list) {
                int star = ContextCompat.checkSelfPermission(getContext(), permission);
                if (star != PackageManager.PERMISSION_GRANTED) {
                    SystemUtil.getPermission(getActivity(), list, PERMISSIONS);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 3) {
            String result_id = data.getStringExtra(TUIKitConstants.Selection.USER_ID_SELECT);
            String result_name = data.getStringExtra(TUIKitConstants.Selection.USER_NAMECARD_SELECT);
            mChatLayout.getInputLayout().updateInputText(result_name, result_id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS) {
            for (String permission : permissions) {
                int granted = ContextCompat.checkSelfPermission(context, permission);
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    Toashow.show(getString(R.string.ts_dialog_call));
                    return;
                }
            }
            switch (sTYPE) {
                case 1:
                    //进入拨打语音通话聊天
                    TRTCAudioCallActivity.startCallSomeone(DemoApplication.instance(), scontactList);
                    break;
                case 2:
                    //进入拨打视频通话聊天
                    TRTCVideoCallActivity.startCallSomeone(DemoApplication.instance(), scontactList);
                    break;
            }
        }
    }

    public static class CustomInputFragment extends BaseInputFragment {
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View baseView = inflater.inflate(R.layout.test_chat_input_custom_fragment, container, false);
            baseView.findViewById(R.id.layout1).setOnClickListener(v -> Toashow.show(getContext(), "点击事件1"));
            baseView.findViewById(R.id.layout2).setOnClickListener(v -> Toashow.show(getContext(), "点击事件2"));
            baseView.findViewById(R.id.layout3).setOnClickListener(v -> Toashow.show(getContext(), "点击事件3"));
            baseView.findViewById(R.id.layout4).setOnClickListener(v -> Toashow.show(getContext(), "点击事件4"));
            return baseView;
        }

    }


    /**
     * 联网获取初始化聊天VIP限制功能
     * 判断对像用于判断是否允许聊天
     */
    public void setmBaseView() {
        String Chat_userid = mChatInfo.getId();
        mChatLayout.setinitListener(callBack);                            //AbsChatLayout监听接口回调
        datamodule.getallcharge(Chat_userid, llcharge);                   //初始化对方通话扣费配置
        datamodule.Personalcontent(Chat_userid, members);                 //初始化对方个人详情资料

        datamodule.getbalance(balance);             //初始化本人金币余额
        datamodule.getvip(paymnets);                //初始化本人最新VIP状态
        datamodule.chatlimit(Nochatting, 1);   //初始化本人聊天次数服务端判断
        datamodule.personal(personallisten);        //初始化本人相关资料
    }

    /**
     * 弹出购买VIP内容  今日聊天次数已达上限 提示购买VIP或充值金币
     */
    public void showdialog() {
        String msg = Config_Msg.getInstance().getMessage();
        dialog_msg_svip.dialogmsgsvip(context, !TextUtils.isEmpty(msg) ? msg : getString(R.string.tv_msg223), getString(R.string.tv_msg228), getString(R.string.tv_msg154), buyJinbi);
    }


    /**
     * 弹出购买VIP内容余额不足
     */
    public void showmoneydialog() {
        dialog_msg_svip.dialogmsgsvip(getContext(), getString(R.string.tv_msg224), getString(R.string.tv_msg228), getString(R.string.tv_msg154), buyJinbi);
    }

    /**
     * 快捷消息发送
     */
    public void senddialogListMsg() {
        dialogListMsg = dialog_list_msg.showlistmsg(context, paymnets);
    }

    /**
     * 获取用户最新状态
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void returnltonItemClick(Object object, int TYPE) {
            switch (TYPE) {
                case 1:
                    infos = (info) object;
                    mChatLayout.setInfos(infos);
                    Log.d(TAG, "初始化用金币余额对像");
                    break;
                case 2:
                    allcharge = (Allcharge) object;
                    mChatLayout.setAllcharge(allcharge);
                    Log.d(TAG, "初始化视频或语扣费设置");
                    break;
            }
        }

        @Override
        public void onLoadMore() {
            ToastUtil.toastLongMessage(DemoApplication.instance().getString(R.string.tv_msg1933));
            BaseActivity.logout(getContext());
        }

        @Override
        public void onRefresh() {
            //帐户封禁通知
            dialog_Blocked.myshow(getContext());
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "初始化用户当前状态");
            //检查TIM信息更自己的头像和名称
            activity_sesemys.checkTIMInfo(userInfo.getName(), userInfo.getAvatar());
        }

        @Override
        public void cancellation() {
            //用户不存在了 找不到用户
            dialog_del_notice.myshow(context, new Paymnets() {
                @Override
                public void onSuccess() {
                    BaseActivity.logout(getContext());
                }
            });
        }

        @Override
        public void onFail() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });
        }

        @Override
        public void activity(String str) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);
                }
            });
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            switch (TYPE) {
                case 1:
                    //发起支付宝请求
                    csAlipay.Paymoney(moneylist);
                    break;
                case 2:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }
        }

        @Override
        public void success(String date) {
            dialogListMsg.dismiss();

            //快捷消息发送 回调
            if (userInfo.getState() >= 3) {
                callBack.fengjin();
                return;
            }

            //要求用户资料补全
            if (personal != null && callBack != null) {
                boolean dislog = AbsChatLayout.dislog(personal);
                if (dislog) {
                    callBack.Completedata();
                    return;
                }
            }

            //判断对方是否为可服
            if (!mChatInfo.isKefu()) {
                //自己不是主播 不是VIP会员 不是女性会员
                if (userInfo.getReale() != 1 && userInfo.getVip() != 1 && !userInfo.getSex().equals("2")) {
                    if (!userInfo.isNouse()) {
                        if (userInfo.getJinbi() < allcharge.getMoney()) {
                            showdialog();
                            return;
                        } else {
                            //扣除金币操作 返回对方和ID号
                            callBack.reducemoney();
                        }
                    }
                }
                callBack.sendMessage(); //刷新限制消息条数
            }


            MessageInfo info = MessageInfoUtil.buildTextMessage(date);
            mChatLayout.sendMessage(info, false);
        }

        @Override
        public void status(int position) {
            switch (position) {
                case 1://打开高德地图导航功能
                    baidumap.openGaoDeNavi(0, 0, "我的位置", gpsmap.getLatitude(), gpsmap.getLongitude(), gpsmap.getDesc());
                    break;
                case 2://打开百度地图导航功能
                    baidumap.openBaiDuNavi(0, 0, "我的位置", gpsmap.getLatitude(), gpsmap.getLongitude(), gpsmap.getDesc());
                    break;
                case 3://打开腾讯地图
                    baidumap.openTencentMap(0, 0, "我的位置", gpsmap.getLatitude(), gpsmap.getLongitude(), gpsmap.getDesc());
                    break;
            }
        }

        @Override
        public void onSuccess(Object object) {
            infos = (info) object;
        }
    };

    /**
     * 获取用户金币余额
     */
    private Paymnets balance = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            infos = (info) object;
            userInfo.setJinbi(infos.getMoney());
            mChatLayout.setInfos(infos);
        }
    };

    /**
     * 获取通话扣费配置
     */
    private Paymnets llcharge = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            allcharge = (Allcharge) object;
            mChatLayout.setAllcharge(allcharge);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }


    };

    /**
     * 金币充值
     */
    private Paymnets buyJinbi = new Paymnets() {
        @Override
        public void onSuccess() {
            //金币不足充值金币
            Detailedlist.starsetAction(context);
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context); //转到赚钱任务页
        }
    };

    /**
     * VIP提示点击跳转
     */
    private Paymnets buyVip = new Paymnets() {
        @Override
        public void onSuccess() {
            //开通VIP会员
            activity_svip.starsetAction(context);
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context); //转到赚钱任务页
        }
    };

    /**
     * 聊天上限次数
     */
    private Paymnets chatbcallback = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(Object object) {
            chat ch = (chat) object;
            if (!TextUtils.isEmpty(ch.getMessage())) {
                Config_Msg.getInstance().setMessage(ch.getMessage());
            }
            if (!openhelper.check(ch.getId())) {
                //首次本地数据sqlist插入
                openhelper.add(ch.getId(), ch.getFree());
                Log.d(TAG, "onSuccess: 首次插入" + ch.getFree());
            } else {
                //N次更新本地数据sqlist
                openhelper.upchat(ch.getFree());
                Log.d(TAG, "onSuccess:N次更新 " + ch.getFree());
            }
            Log.d(TAG, "设置是限制聊天次数" + ch.getFree());

            mChatLayout.setmessage(openhelper.chatQuerychat2());
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "不限制聊天消息");
            openhelper.delete3(MyOpenhelper.paidchat);
            mChatLayout.setmessage(false);
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }
    };

    /**
     * AbsChatLayout 回调事件处理
     */
    private CallBack callBack = new CallBack() {
        @Override
        public void onsuccess() {
            showdialog();//提示购买VIP内容
        }

        @Override
        public void onmoney() {
            showmoneydialog();//提示金币余额不足
        }

        @Override
        public void onfall() {
            //对像金币内容空消息发送失败
            Toashow.show(getContext().getString(R.string.tv_msg186));
        }

        @Override
        public void avatar() {
            //提示上传头像
            dialog_item_Avatar.dialogitemmsgpic(context);
        }

        @Override
        public void fengjin() {
            //封号提示
            dialog_Blocked.myshow(getContext(), paymnets);
        }

        @Override
        public void chta() {
            //今日聊天次数已达上限 提示购买VIP或充值金币
            showdialog();
        }

        @Override
        public void sendMessage() {
            Log.d(TAG, "成功发送后 刷新返回聊天次数");
            datamodule.chatlimit(Nochatting, 2);

        }

        @Override
        public void videoCall(Object object, int TYPE) {
            List<String> permissionList = new ArrayList<>();
            switch (TYPE) {
                case AbsChatLayout.AUDIO:
                    permissionList.add(Manifest.permission.RECORD_AUDIO);
                    break;
                case AbsChatLayout.VIDEO:
                    permissionList.add(Manifest.permission.CAMERA);
                    break;
            }
            SystemUtil.getPermission(getActivity(), permissionList);
        }

        @Override
        public void reducemoney() {
            datamodule.chatjinbi(mChatInfo.getId(), getString(R.string.tm100) + mChatInfo.getChatName(), new Paymnets() {
                @Override
                public void onSuccess(String msg) {
                    Log.d(TAG, msg);
                }

                @Override
                public void onFail(String msg) {
                    Toashow.show(msg);
                    Log.d(TAG, "onFail: " + userInfo.getJinbi());
                }
            });
        }

        @Override
        public void moneyshow() {
            dialog_msg_svip.dialogmsgsvip(context, getString(R.string.frag3), getString(R.string.frag1), getString(R.string.frag2), paymnets1);
        }

        @Override
        public void Completedata() {
            //补全资料
            dialog_show_activity.show(context, new Paymnets() {
                @Override
                public void onSuccess() {
                    activity_updateedit.starsetAction(context);
                }

                @Override
                public void onFail() {

                }
            });
        }

        @Override
        public void Sendpictures() {
            //发送图片
            dialog_msg_svip.dialogmsgsvip(context, getString(R.string.chat01), getString(R.string.tv_msg228), getString(R.string.tv_msg153), buyVip);
        }

        @Override
        public void Sendvideo() {
            //发送视频
            dialog_msg_svip.dialogmsgsvip(context, getString(R.string.chat02), getString(R.string.tv_msg228), getString(R.string.tv_msg153), buyVip);
        }

        @Override
        public void Sendvoice() {
            //发送语音
            dialog_msg_svip.dialogmsgsvip(context, getString(R.string.chat03), getString(R.string.tv_msg228), getString(R.string.tv_msg153), buyVip);
        }

        @Override
        public void Sendlocation() {
            //发送位置
            Toashow.show("发送位置");
        }

        @Override
        public void Sendgifts() {
            //发送礼物
        }

    };

    /**
     * 调起礼物 音乐 快捷消息 发送定位 面板
     */
    private InputLayout.ongiftListener ongiftListener = new InputLayout.ongiftListener() {
        @Override
        public void mEnablegift() {
            present present = new present();
            present.setTYPE(2); //聊天窗口2
            present.setTouserid(mChatInfo.getId());
            present.setUserid(userInfo.getUserId());
            dialog_item_gift.dialogitemgift(context, present, giftPanelDelegate, paymnets);
        }

        @Override
        public void mLocation() {
            LocationMessage();
        }

        @Override
        public void mplaymisc() {
            startplaymisc();
        }

        @Override
        public void mQuickmessage() {
            senddialogListMsg();
        }


    };

    /**
     * 回调监听事件选择礼物事事件处理
     */
    private GiftPanelDelegate giftPanelDelegate = new GiftPanelDelegate() {
        @Override
        public void onGiftItemClick(GiftInfo giftInfo) {
            Toashow.show(String.format(getString(R.string.tm73), giftInfo.title));

            //im发送礼物消息
            GiftMessage(giftInfo);
        }

        @Override
        public void onChargeClick() {
            Log.d(TAG, "充值点击事件: ");
        }
    };

    /**
     * 监听定位回调并发送一条定位消息
     */
    private Callback callback = new Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onSuccess(AMapLocation amapLocation) {
            amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表 https://developer.amap.com/api/android-location-sdk/guide/utilities/location-type
            amapLocation.getLongitude();//获取经度
            amapLocation.getLatitude();//获取纬度
            amapLocation.getAccuracy();//获取精度信息 米
            amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
            amapLocation.getCountry();//国家信息
            amapLocation.getProvince();//省信息
            amapLocation.getCity();//城市信息
            amapLocation.getDistrict();//城区信息
            amapLocation.getStreet();//街道信息
            amapLocation.getStreetNum();//街道门牌号信息
            amapLocation.getCityCode();//城市编码
            amapLocation.getAdCode();//地区编码
            amapLocation.getAoiName();//获取当前定位点的AOI信息
            amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
            amapLocation.getFloor();//获取当前室内定位的楼层
            amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态

            //发送一条定位消息
            MessageInfo info = MessageInfoUtil.LocationMessage(amapLocation.getAddress(), amapLocation.getLongitude(), amapLocation.getLatitude());
            mChatLayout.sendMessage(info, false);
        }
    };

    /**
     * 每天限制聊天次数
     */
    private Paymnets Nochatting = new Paymnets() {
        @Override
        public void onSuccess() {
            mChatLayout.setmessage(true);
            userInfo.setNouse(true);
        }

        @Override
        public void onFail(String msg) {
            Config_Msg.getInstance().setMessage(msg);
            mChatLayout.setmessage(false);
            userInfo.setNouse(false);
        }

    };

    /**
     * 是否允许扣除金币
     */
    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void onRefresh() {
            //我不同意扣除金币
            userInfo.setGojinbi(true);
        }

        @Override
        public void onSuccess() {
            //同意允许扣除金币
            userInfo.setGojinbi(true);
        }

    };

    /**
     * 我自己的资料
     */
    private Paymnets personallisten = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void ToKen(String msg) {
            Log.d(TAG, "ToKen: ");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail: ");
        }

        @Override
        public void onSuccess(String msg) {
            Log.d(TAG, "onSuccess: ");
        }

        @Override
        public void onSuccess(Object object) {
            //把资料传递给聊天控件-->AbsChatLayout
            personal = (com.tencent.opensource.model.personal) object;
            mChatLayout.setPersonal(personal);
        }
    };

    /**
     * 对方身份资料
     */
    private Paymnets members = new Paymnets() {
        @Override
        public void onSucces(Object object) {
            //把资料传递给聊天控件-->AbsChatLayout
            member = (com.tencent.opensource.model.member) object;
            mChatLayout.setMember(member);
        }

        @Override
        public void onFail(String msg) {
            Log.d(TAG, "onFail: " + msg);
        }
    };

}