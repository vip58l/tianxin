package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.Module.api.Config_User;
import com.tianxin.Module.api.message;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Paymnets;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_sayhello extends BaseDialog {
    private static final String TAG = dialog_sayhello.class.getSimpleName();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Radapter radapter;
    private static List<message> messageList = new ArrayList<>();

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_sayhello dialog_sayhello = new dialog_sayhello(context, paymnets);
        dialog_sayhello.show();
    }

    public dialog_sayhello(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
        recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        radapter = new Radapter(context, list, Radapter.Randomgreet);
        recyclerview.setAdapter(radapter);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        inidate();
    }

    private void inidate() {
        //获取内置聊天消息内容
        if (messageList.size() == 0) {
            datamodule.message(paymnets3);
        }

        //一建打招听对像会员
        if (list.size() == 0) {
            datamodule.Randomgreet(paymnets2);
        }

    }

    @Override
    public int getview() {
        return R.layout.item_randomgreet;
    }

    @OnClick({R.id.send1, R.id.send2, R.id.colas})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                chatmsg();
                if (paymnets != null) {
                    //一键发送消息
                    paymnets.onSuccess();
                }
                dismiss();
                break;
            case R.id.send2:
                datamodule.Randomgreet(paymnets2);
                if (paymnets != null) {
                    //换一批
                    paymnets.onFail();
                }
                break;
            case R.id.colas:
                dismiss();
                break;

        }

    }

    /**
     * 一键批量发送消息打招呼
     */
    public void chatmsg() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(context.getString(R.string.eorrfali2));
            return;
        }
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            dialog_item_Avatar.dialogitemmsgpic(context);
            return;
        }

        for (Object o : list) {
            int x = (int) (Math.random() * messageList.size());
            message message = messageList.get(x); //随机获取消息内容
            if (message == null) {
                continue;
            }
            member member = (member) o;
            senddmessage(String.valueOf(member.getId()), message.getMsg());
        }

        if (paymnets != null) {
            paymnets.onClick();
        }

        //保存显示已发送打招呼以便下次不再显示一键打招呼功能
        Config_User config_user = Config_User.getInstance();
        config_user.setSendout(true);

    }

    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(context.getString(R.string.eorrfali2));

        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(context.getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            List<member> members = (List<member>) object;
            if (members.size() > 0) {
                list.clear();
                list.addAll(members);
                radapter.notifyDataSetChanged();
            }
        }

        @Override
        public void ToKen(String msg) {
            paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

    };

    private Paymnets paymnets3 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void ToKen(String msg) {
            paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object object) {
            messageList = (List<message>) object;
        }
    };

    /**
     * IM发送聊天消息给指定对像
     */
    public static void senddmessage(String Touserid, String message) {
        V2TIMManager v2TIMManager = V2TIMManager.getInstance();
        v2TIMManager.sendC2CTextMessage(message, Touserid, new V2TIMValueCallback<V2TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Log.d(TAG, "onError: 发送失败");
            }

            @Override
            public void onSuccess(V2TIMMessage v2TIMMessage) {
                V2TIMTextElem textElem = v2TIMMessage.getTextElem();
                //v2TIMMessage.getUserID();
                //v2TIMMessage.getNickName();
                //v2TIMMessage.getElemType();
                //v2TIMMessage.getFaceUrl();
                //v2TIMMessage.getStatus();
                Log.d(TAG, v2TIMMessage.getNickName() + "" + textElem.getText());

            }
        });
    }
}
