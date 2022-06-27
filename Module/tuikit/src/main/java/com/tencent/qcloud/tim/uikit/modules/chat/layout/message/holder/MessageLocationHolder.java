package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMFaceElem;
import com.tencent.imsdk.v2.V2TIMFileElem;
import com.tencent.imsdk.v2.V2TIMGroupTipsElem;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMLocationElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.liteav.login.UserModel;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.face.FaceManager;
import com.tencent.qcloud.tim.uikit.component.photoview.PhotoViewActivity;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

/**
 * 实现抽你类 定位适配器
 */
public class MessageLocationHolder extends MessageContentHolder {
    private static final String TAG = MessageLocationHolder.class.getSimpleName();
    private TextView msgBodyText;
    private ImageView loationimges;
    private LinearLayout l1;
    private LinearLayout l2;

    private ImageView gift;
    private TextView giftitle;

    public MessageLocationHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_loation;
    }

    @Override
    public void initVariableViews() {
        l1 = rootView.findViewById(R.id.l1);
        l2 = rootView.findViewById(R.id.l2);
        msgBodyText = rootView.findViewById(R.id.msg_body_tv);
        loationimges = rootView.findViewById(R.id.loation_imges);
        gift = rootView.findViewById(R.id.gift);
        giftitle = rootView.findViewById(R.id.giftitle);
    }

    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
        l1.setVisibility(View.VISIBLE);
        l2.setVisibility(View.VISIBLE);
        msgContentFrame.setBackground(null);

        V2TIMMessage timMessage = msg.getTimMessage();
        V2TIMCustomElem customElem = timMessage.getCustomElem();        //获取自定义数据
        V2TIMFaceElem faceElem = timMessage.getFaceElem();              //获取表情索引
        V2TIMImageElem imageElem = timMessage.getImageElem();           //获取原图本地文件路径，只对消息发送方有效
        V2TIMFileElem fileElem = timMessage.getFileElem();              //获取文件路径（只有发送方才能获取到）
        V2TIMGroupTipsElem groupTipsElem = timMessage.getGroupTipsElem();
        V2TIMOfflinePushInfo offlinePushInfo = timMessage.getOfflinePushInfo();

        if (timMessage.getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION) {
            return;
        }
        V2TIMLocationElem locationElem = timMessage.getLocationElem();  //获取位置描述
        String desc = locationElem.getDesc();
        double latitude = locationElem.getLatitude();
        double longitude = locationElem.getLongitude();
        //礼物展示
        if (latitude == 0 && longitude == 0) {
            try {
                //赠送礼物
                String[] split = desc.split("\\|");
                if (!TextUtils.isEmpty(split[0])) {
                    giftitle.setText(split[0]);
                }
                if (!TextUtils.isEmpty(split[1])) {
                    GlideEngine.loadImage(gift, split[1]);
                    gift.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onMessageLongClick(view, position, msg);
                            }
                            return true;
                        }
                    });
                }
                loationimges.setOnClickListener(null);
                itemView.setBackground(null);
                l1.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
                layoutVariableViews(msg, position, desc, latitude, longitude);
            }
        } else {
            layoutVariableViews(msg, position, desc, latitude, longitude);
        }
    }

    private void layoutVariableViews(MessageInfo msg, int position, String desc, double latitude, double longitude) {
        l2.setVisibility(View.GONE);
        msgBodyText.setText(desc);
        loationimges.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMessageLongClick(view, position, msg);
                }
                return true;
            }
        });
        //这里可以发送一条广播或隐式启动ACTIVITY 打开地图功能

        //Gps定位回调给主APP接口 onMessagegps 回调到监听事件处理
        if (onMessagegps != null) {
            loationimges.setOnClickListener(v -> onMessagegps.OnItemClickListener(desc, latitude, longitude));
        }
    }

}
