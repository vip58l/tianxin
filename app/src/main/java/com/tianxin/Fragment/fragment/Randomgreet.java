/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/26 0026
 */


package com.tianxin.Fragment.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Paymnets;
import com.tencent.imsdk.v2.V2TIMTextElem;
import com.tianxin.Module.api.Config_User;
import com.tencent.opensource.model.member;
import com.tianxin.Module.api.message;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.dialog.dialog_item_Avatar;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 一键打招呼
 */
public class Randomgreet extends BasFragment {
    private static final String TAG = Randomgreet.class.getSimpleName();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    List<message> message;
    private Paymnets paymnets;

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.item_randomgreet, null);

    }

    @OnClick({R.id.send1, R.id.send2, R.id.colas})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                chatmsg();
                break;
            case R.id.send2:
                datamodule.Randomgreet(paymnets2);
                break;
        }

    }

    @Override
    public void iniview() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        radapter = new Radapter(getActivity(), list2, Radapter.Randomgreet);
        recyclerView.setAdapter(radapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线，下面定义的是水平的线
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL_LIST));


    }

    @Override
    public void initData() {

        if (message == null) {
            datamodule.message(paymnets3);
        }

        if (list.size() == 0) {
            datamodule.Randomgreet(paymnets2);
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    /**
     * 一键批量发送消息打招呼
     */
    public void chatmsg() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
            return;
        }

        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            dialog_item_Avatar.dialogitemmsgpic(getActivity());
            return;
        }

        for (Object o : list2) {
            int x = (int) (Math.random() * message.size());
            message message = this.message.get(x);
            if (message == null) {
                Log.d(TAG, "chatmsg: 没有消息文本");
                return;
            }
            member member = (member) o;
            V2TIMManager v2TIMManager = V2TIMManager.getInstance();
            v2TIMManager.sendC2CTextMessage(message.getMsg(), String.valueOf(member.getId()), new V2TIMValueCallback<V2TIMMessage>() {
                @Override
                public void onError(int code, String desc) {
                    Log.d(TAG, "onError: 发送失败");
                }

                @Override
                public void onSuccess(V2TIMMessage v2TIMMessage) {
                    V2TIMTextElem textElem = v2TIMMessage.getTextElem();
                    Log.d(TAG, "onSuccess: " + textElem.getText());
                }
            });
        }

        if (paymnets != null) {
            paymnets.onClick();
        }

        //保存显示已发送打招呼以便下次不再显示一键打招呼功能
        Config_User instance = Config_User.getInstance();
        instance.setSendout(true);
    }

    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));

        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            List<member> members = (List<com.tencent.opensource.model.member>) object;
            if (members.size() > 0) {
                list2.clear();
                list2.addAll(members);
                radapter.notifyDataSetChanged();
            }

        }

        @Override
        public void ToKen(String msg) {
            Randomgreet.super.paymnets.ToKen(msg);
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
            Randomgreet.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object object) {
            message = (List<message>) object;
        }
    };
}
