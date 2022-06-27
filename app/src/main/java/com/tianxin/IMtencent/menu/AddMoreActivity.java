package com.tianxin.IMtencent.menu;

import static com.tencent.imsdk.v2.V2TIMFriendInfo.V2TIM_FRIEND_TYPE_SINGLE;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.imsdk.v2.V2TIMFriendCheckResult;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.chta.ChatV2TIMFriendInfoResult;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tencent.imsdk.BaseConstants;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMFriendAddApplication;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.utils.SoftKeyBoardUtil;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

/**
 * 添加聊天好友
 */
public class AddMoreActivity extends BaseActivity {
    private static final String TAG = AddMoreActivity.class.getSimpleName();
    private TitleBarLayout mTitleBar;
    private EditText mUserID;
    private EditText mAddWording, add_wording2;
    private boolean mIsGroup;
    private TextView tv1, tv2;
    private UserInfo userInfo;
    private ChatV2TIMFriendInfoResult chatV2TIMFriendInfoResult;
    private Datamodule datamodule;
    private member member;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mIsGroup = getIntent().getExtras().getBoolean(TUIKitConstants.GroupType.GROUP);
        }
        setContentView(R.layout.contact_add_activity);
        chatV2TIMFriendInfoResult = new ChatV2TIMFriendInfoResult();
        datamodule = new Datamodule(this);

        mTitleBar = findViewById(R.id.add_friend_titlebar);
        mTitleBar.setTitle(mIsGroup ? getResources().getString(R.string.add_group) : getResources().getString(R.string.add_friend), TitleBarLayout.POSITION.LEFT);
        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.getRightGroup().setVisibility(View.GONE);
        mUserID = findViewById(R.id.user_id);
        mAddWording = findViewById(R.id.add_wording);
        add_wording2 = findViewById(R.id.add_wording2);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        userInfo = UserInfo.getInstance();
        if (mIsGroup) {
            tv1.setText(getString(R.string.hint_add_user_id2));
            tv2.setText(getString(R.string.hint_add_wording));
        }

    }

    public void add(View view) {
        if (mIsGroup) {
            addGroup(view);
        } else {
            addFriend(view);
        }
    }

    /**
     * 加朋友
     *
     * @param view
     */
    public void addFriend(View view) {
        String id = mUserID.getText().toString();

        if (TextUtils.isEmpty(id)) {
            Toashow.show(getString(R.string.tv_msg_tt));
            return;
        }

        if (userInfo.getUserId().equals(id) || userInfo.getPhone().equals(id)) {
            Toashow.show(getString(R.string.tv_msg_tt2));
            return;
        }

        //先执行发送到网站查询内容，然回调后再执行下面添加好友
        datamodule.AddMore(id, paymnets);
    }

    /**
     * 加群
     *
     * @param view
     */
    public void addGroup(View view) {
        String id = mUserID.getText().toString();
        if (TextUtils.isEmpty(id)) {
            Toashow.show(getString(R.string.tv_msg_tt4));
            return;
        }
        V2TIMManager.getInstance().joinGroup(id, mAddWording.getText().toString(), new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
                switch (code) {
                    case 10002:
                        ToastUtil.toastShortMessage("服务端内部错误，请重试。");
                        break;
                    case 10003:
                        ToastUtil.toastShortMessage("请求中的接口名称错误，请核对接口名称并重试。");
                        break;
                    case 10004:
                        ToastUtil.toastShortMessage("参数错误，请根据错误描述检查请求是否正确。");
                        break;
                    case 10005:
                        ToastUtil.toastShortMessage("请求包体中携带的数量过多。");
                        break;
                    case 10006:
                        ToastUtil.toastShortMessage("操作限制，请尝试降低调用频率。");
                        break;
                    case 10007:
                        ToastUtil.toastShortMessage("操作权限不足（例如公共场所中普通成员试验踢人操作，但只有应用管理员才能操作；非群成员）。");
                        break;
                    case 10009:
                        ToastUtil.toastShortMessage("该群拒绝群主主动退出。");
                        break;
                    case 10010:
                        ToastUtil.toastShortMessage("身份不存在，或者曾经存在过，但现在已经被解散了。");
                        break;
                    case 10011:
                        ToastUtil.toastShortMessage("解析JSON包体失败，请检查包体的格式是否符合JSON格式。");
                        break;
                    case 10012:
                        ToastUtil.toastShortMessage("发起操作的UserID错误，请检查发起操作的用户UserID是否正确。");
                        break;
                    case 10013:
                        ToastUtil.toastShortMessage("用户已经是群成员。");
                        break;
                    case 10014:
                        ToastUtil.toastShortMessage("群满员，无法将请求中的用户加入社区，如果已经是集体加人，可以尝试减少加入的用户数量。");
                        break;
                    case 10015:
                        ToastUtil.toastShortMessage("请检查群号ID是否正确");
                        break;
                    case 10016:
                        ToastUtil.toastShortMessage("App后台通过用户拒绝本次操作。");
                        break;
                    case 10017:
                        ToastUtil.toastShortMessage("因被禁言而不能发送消息，请检查发送者是否被设置禁言。");
                        break;
                    case 10018:
                        ToastUtil.toastShortMessage("一次包过最长的包长（1MB），请求的内容过多，请试过一次次请求的数量。");
                        break;
                    case 10019:
                        ToastUtil.toastShortMessage("请求的用户账号不存在。");
                        break;
                    case 10021:
                        ToastUtil.toastShortMessage("群组 ID 已使用，请选择其他的群组 ID。");
                        break;
                    case 10023:
                        ToastUtil.toastShortMessage("发消息的频率超限，请发消息时间的间隔。");
                        break;
                    case 10024:
                        ToastUtil.toastShortMessage("此邀请或请求已被处理。");
                        break;
                    case 10025:
                        ToastUtil.toastShortMessage("公众号ID已被使用，并且操作者为群主，可以直接使用。");
                        break;
                    case 10026:
                        ToastUtil.toastShortMessage("该 SDKAppID 请求的命令字已被禁用。");
                        break;
                    case 10030:
                        ToastUtil.toastShortMessage("请求撤回的消息不存在。");
                        break;
                    case 10031:
                        ToastUtil.toastShortMessage("消息撤回超过了时间限制（默认2分钟）。");
                        break;
                    case 10032:
                        ToastUtil.toastShortMessage("请求撤回的消息不支持撤回操作。");
                        break;
                    case 10033:
                        ToastUtil.toastShortMessage("社区类型不支持消息撤回操作。");
                        break;
                    case 10034:
                        ToastUtil.toastShortMessage("该消息类型不支持删除操作。");
                        break;
                    case 10035:
                        ToastUtil.toastShortMessage("音视频群聊天室和在线成员广播大不支持删除消息。");
                        break;
                    case 10036:
                        ToastUtil.toastShortMessage("音视频聊天室创建数量超过限制，请参考价格说明购买预付费套餐“IM音视频聊天室”。");
                        break;
                    case 10037:
                        ToastUtil.toastShortMessage("单个用户可创建和加入的用户数量超过限制，请参考价格说明购买或升级预付费套餐“单人可创建并加入群组数”。");
                        break;
                    case 10038:
                        ToastUtil.toastShortMessage("群成员数量限制，超过价格说明购买或升级预付费套餐“扩大群人数配置”。（升级后需要调用修改群资料接口修改群人数）");
                        break;
                    case 10041:
                        ToastUtil.toastShortMessage("该应用（SDKAppID）已配置不支持群撤回。");
                        break;
                    case 10045:
                        ToastUtil.toastShortMessage("自定义属性键超过大小限制32字节。");
                        break;
                    case 10046:
                        ToastUtil.toastShortMessage("自定义属性数量超过了大小限制4000字节。");
                        break;
                    case 10047:
                        ToastUtil.toastShortMessage("自定义属性键数量超过限制16。");
                        break;
                    case 10048:
                        ToastUtil.toastShortMessage("自定义所有属性键对应的 val 大小之和超过 16000 字节。");
                        break;
                    case 10049:
                        ToastUtil.toastShortMessage("自定义属性写操作触发频控。");
                        break;
                    case 10050:
                        ToastUtil.toastShortMessage("删除不存在的自定义属性。");
                        break;
                    case 10051:
                        ToastUtil.toastShortMessage("消息删除超过最大范围限制。");
                        break;
                    case 10052:
                        ToastUtil.toastShortMessage("消息删除时群里不存在消息。");
                        break;
                    case 10053:
                        ToastUtil.toastShortMessage("群@数量超过30。");
                        break;
                    case 10054:
                        ToastUtil.toastShortMessage("群成员过多，请分页拉取。");
                        break;
                    case 10056:
                        ToastUtil.toastShortMessage("自定义属性写操作冲突，请获取最新的自定义属性，然后进行写操作。");
                        break;
                }
            }

            @Override
            public void onSuccess() {
                ToastUtil.toastShortMessage("加群请求已发送");
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        SoftKeyBoardUtil.hideKeyBoard(mUserID.getWindowToken());
    }

    private Paymnets paymnets = new Paymnets() {

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }


        @Override
        public void onSuccess(Object object) {
            member = (com.tencent.opensource.model.member) object;
            int sex0 = member.getSex();
            int sex1 = Integer.parseInt(userInfo.getSex());
            onSuccess();
        }

        @Override
        public void onSuccess() {
            /**
             * 添加朋友申请
             */
            String userid = String.valueOf(member.getId());
            String friendRemark = add_wording2.getText().toString();
            V2TIMFriendAddApplication v2TIMFriendAddApplication = new V2TIMFriendAddApplication(userid);
            v2TIMFriendAddApplication.setAddWording(mAddWording.getText().toString());
            v2TIMFriendAddApplication.setAddSource("android");
            V2TIMManager.getFriendshipManager().addFriend(v2TIMFriendAddApplication, new V2TIMValueCallback<V2TIMFriendOperationResult>() {
                @Override
                public void onError(int code, String desc) {
                    Log.d(TAG, "code=" + code + "desc=" + desc);
                    ToastUtil.toastLongMessage("code=" + code + "desc=" + desc);
                }

                @Override
                public void onSuccess(V2TIMFriendOperationResult v2TIMFriendOperationResult) {
                    switch (v2TIMFriendOperationResult.getResultCode()) {
                        case BaseConstants.ERR_SUCC:
                            ToastUtil.toastShortMessage("添加成功");
                            if (!TextUtils.isEmpty(friendRemark)) {
                                //可修改好友备注等资料
                                chatV2TIMFriendInfoResult.setFriendInfo(userid, friendRemark);
                            }
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_INVALID_PARAMETERS:
                            if (TextUtils.equals(v2TIMFriendOperationResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
                                ToastUtil.toastShortMessage("对方已是您的好友");
                                break;
                            }
                        case BaseConstants.ERR_SVR_FRIENDSHIP_COUNT_LIMIT:
                            ToastUtil.toastShortMessage("您的好友数已达系统上限");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_PEER_FRIEND_LIMIT:
                            ToastUtil.toastShortMessage("对方的好友数已达系统上限");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_IN_SELF_BLACKLIST:
                            ToastUtil.toastShortMessage("被加好友在自己的黑名单中");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_ALLOW_TYPE_DENY_ANY:
                            ToastUtil.toastShortMessage("对方已禁止加好友");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_IN_PEER_BLACKLIST:
                            ToastUtil.toastShortMessage("您已被被对方设置为黑名单");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_ALLOW_TYPE_NEED_CONFIRM:
                            ToastUtil.toastShortMessage("等待好友审核同意");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_ACCOUNT_NOT_FOUND:
                            ToastUtil.toastLongMessage("请求的用户账号不存在。");
                            break;
                        case 30002:
                            ToastUtil.toastLongMessage("SDKAppID 不匹配。");
                            break;
                        case 30004:
                            ToastUtil.toastLongMessage("请求需要App管理员权限。");
                            break;
                        case 30005:
                            ToastUtil.toastLongMessage("关系链领域中包含敏感词。");
                            break;
                        case 30006:
                            ToastUtil.toastLongMessage("服务端内部错误，请重试。");
                            break;
                        case 30007:
                            ToastUtil.toastLongMessage("网络，请稍等重试。");
                            break;
                        case 30008:
                            ToastUtil.toastLongMessage("同时写导致写冲突，建议使用示范方式。");
                            break;
                        case 30009:
                            ToastUtil.toastLongMessage("后台禁止该用户发起加好友请求。");
                            break;
                        case 30011:
                            ToastUtil.toastLongMessage("完美已达系统服务。");
                            break;
                        case 30012:
                            ToastUtil.toastLongMessage("未决数已达系统物业。");
                            break;
                        case 30013:
                            ToastUtil.toastLongMessage("黑名单数已达系统隐私。");
                            break;
                        case 30540:
                            ToastUtil.toastLongMessage("添加好友请求被安全策略解决，请勿邀请好友发起添加请求。");
                            break;
                        case 30614:
                            ToastUtil.toastLongMessage("请求的未决不存在。");
                            break;
                        case 31704:
                            ToastUtil.toastLongMessage("与请求删除的账号之间不存在好友关系。");
                            break;
                        case 31707:
                            ToastUtil.toastLongMessage("删除好友请求被安全策略解决，请勿邀请发起好友请求。");
                            break;
                        case 31804:
                            ToastUtil.toastLongMessage("请求的用户账号不存在。");
                            break;
                        default:
                            ToastUtil.toastLongMessage(v2TIMFriendOperationResult.getResultCode() + " " + v2TIMFriendOperationResult.getResultInfo());
                            break;
                    }
                    finish();
                }
            });

        }
    };


    /**
     * 添加好友
     *
     * @param userid
     * @param friendRemark
     */
    public static void myaddfriend(String userid, String friendRemark) {
        V2TIMFriendAddApplication v2TIMFriendAddApplication = new V2TIMFriendAddApplication(userid);
        v2TIMFriendAddApplication.setAddWording(UserInfo.getInstance().getName());
        v2TIMFriendAddApplication.setAddSource("android");
        V2TIMManager.getFriendshipManager().addFriend(v2TIMFriendAddApplication, new V2TIMValueCallback<V2TIMFriendOperationResult>() {
            @Override
            public void onError(int code, String desc) {
                Log.d(TAG, "code=" + code + "desc=" + desc);
                ToastUtil.toastLongMessage("code=" + code + "desc=" + desc);
            }

            @Override
            public void onSuccess(V2TIMFriendOperationResult v2TIMFriendOperationResult) {

                switch (v2TIMFriendOperationResult.getResultCode()) {
                    case BaseConstants.ERR_SUCC:
                        if (!TextUtils.isEmpty(friendRemark)) {
                            new ChatV2TIMFriendInfoResult().setFriendInfo(userid, friendRemark);
                        }
                        break;
                    case 30001:
                        Log.d(TAG, "30001请求参数错误，请根据错误描述检查请求是否正确: ");
                        break;
                    default:
                        Log.d(TAG, "getResultCode: " + v2TIMFriendOperationResult.getResultCode());
                        break;
                }
            }
        });
    }

    /**
     * 添加好友
     *
     * @param userid
     */
    public static void myaddfriend(String userid) {
        V2TIMFriendAddApplication v2TIMFriendAddApplication = new V2TIMFriendAddApplication(userid);
        v2TIMFriendAddApplication.setAddWording(UserInfo.getInstance().getName());
        v2TIMFriendAddApplication.setAddSource("android");
        V2TIMManager.getFriendshipManager().addFriend(v2TIMFriendAddApplication, new V2TIMValueCallback<V2TIMFriendOperationResult>() {
            @Override
            public void onError(int code, String desc) {
                Log.d(TAG, "code=" + code + "desc=" + desc);
            }

            @Override
            public void onSuccess(V2TIMFriendOperationResult v2TIMFriendOperationResult) {
                Log.d(TAG, "onSuccess: ");
            }
        });
    }


    /**
     * 检查是否为好友
     * https://im.sdk.qcloud.com/doc/zh-cn/classcom_1_1tencent_1_1imsdk_1_1v2_1_1V2TIMFriendshipManager.html
     */
    public static void checkFriend(List<String> list) {
        V2TIMManager.getFriendshipManager().checkFriend(list, V2TIM_FRIEND_TYPE_SINGLE, new V2TIMValueCallback<List<V2TIMFriendCheckResult>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<V2TIMFriendCheckResult> v2TIMFriendCheckResults) {
                for (int i = 0; i < v2TIMFriendCheckResults.size(); i++) {
                    V2TIMFriendCheckResult checkResult = v2TIMFriendCheckResults.get(i);
                    switch (checkResult.getResultCode()) {
                        case BaseConstants.ERR_SUCC:
                            int resultType = checkResult.getResultType();
                            if (resultType == 0) {
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                myaddfriend(checkResult.getUserID(), "平台客服0" + (i + 1));
                            }
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_INVALID_PARAMETERS:
                            if (TextUtils.equals(checkResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
                                Log.d(TAG, "对方已是您的好友");
                                break;
                            }
                        case BaseConstants.ERR_SVR_FRIENDSHIP_COUNT_LIMIT:
                            Log.d(TAG, "您的好友数已达系统上限");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_PEER_FRIEND_LIMIT:
                            Log.d(TAG, "对方的好友数已达系统上限");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_IN_SELF_BLACKLIST:
                            Log.d(TAG, "被加好友在自己的黑名单中");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_ALLOW_TYPE_DENY_ANY:
                            Log.d(TAG, "对方已禁止加好友");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_IN_PEER_BLACKLIST:
                            Log.d(TAG, "您已被被对方设置为黑名单");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_ALLOW_TYPE_NEED_CONFIRM:
                            Log.d(TAG, "等待好友审核同意");
                            break;
                        case BaseConstants.ERR_SVR_FRIENDSHIP_ACCOUNT_NOT_FOUND:
                            Log.d(TAG, "请求的用户账号不存在。");
                            break;
                        case 30002:
                            Log.d(TAG, "SDKAppID 不匹配。");
                            break;
                        case 30004:
                            Log.d(TAG, "请求需要App管理员权限。");
                            break;
                        case 30005:
                            Log.d(TAG, "关系链领域中包含敏感词。");
                            break;
                        case 30006:
                            Log.d(TAG, "服务端内部错误，请重试。");
                            break;
                        case 30007:
                            Log.d(TAG, "网络，请稍等重试。");
                            break;
                        case 30008:
                            Log.d(TAG, "同时写导致写冲突，建议使用示范方式。");
                            break;
                        case 30009:
                            Log.d(TAG, "后台禁止该用户发起加好友请求。");
                            break;
                        case 30011:
                            Log.d(TAG, "完美已达系统服务。");
                            break;
                        case 30012:
                            Log.d(TAG, "未决数已达系统物业。");
                            break;
                        case 30013:
                            Log.d(TAG, "黑名单数已达系统隐私。");
                            break;
                        case 30540:
                            Log.d(TAG, "添加好友请求被安全策略解决，请勿邀请好友发起添加请求。");
                            break;
                        case 30614:
                            Log.d(TAG, "请求的未决不存在。");
                            break;
                        case 31704:
                            Log.d(TAG, "与请求删除的账号之间不存在好友关系。");
                            break;
                        case 31707:
                            Log.d(TAG, "删除好友请求被安全策略解决，请勿邀请发起好友请求。");
                            break;
                        case 31804:
                            Log.d(TAG, "请求的用户账号不存在。");
                            break;
                        default:
                            Log.d(TAG, checkResult.getResultCode() + " " + checkResult.getResultInfo());
                            break;
                    }
                }

            }
        });

    }

}
