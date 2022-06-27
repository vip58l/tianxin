package com.tianxin.BasActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tianxin.listener.OnItemChildClickListener;
import com.tianxin.listener.Paymnets;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import butterknife.ButterKnife;

/**
 * 用于实现方法的抽像类
 */
public abstract class BaseHolder extends RecyclerView.ViewHolder {
    public Callback callback;
    public Context context;
    public int position;
    public int TYPE;
    public Object object;
    public UserInfo userInfo;
    public Paymnets paymnets;
    public OnItemChildClickListener onItemChildClickListener;
    public AMapLocation samapLocation;

    public BaseHolder(@NonNull View itemView) {
        super(itemView);
        userInfo = UserInfo.getInstance();
        context = DemoApplication.instance();
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(Object object, int position, Callback callback);

    public abstract void bind(Context context, Object object, int position, Callback callback);

    public abstract void OnClick(View v);

    public void bind(Context context, Object object, int position, Callback callback, int TYPE, OnItemChildClickListener onItemChildClickListener) {

    }

    public void setGridview(ImageView img, String path, RecyclerView recyclerview) {

    }

    public void setsuper(Context context, Object object, int position, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.position = position;
        this.object = object;

    }

    public void setsuper(Context context, Object object, int position, Callback callback, Paymnets paymnets) {
        this.context = context;
        this.callback = callback;
        this.position = position;
        this.object = object;
        this.paymnets = paymnets;
    }

    public void playVideo() {
    }

    public void showtext() {
    }

    /**
     * 这里需要用户上传头像才能聊天
     */
    public void startChatActivity(String getuserid, String name) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(getuserid);
        chatInfo.setChatName(name);
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void startChatActivity(Class<?> cls, String strend, String suserid) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.trendid, strend);
        intent.putExtra(Constants.suserid, suserid);
        context.startActivity(intent);
    }


}
