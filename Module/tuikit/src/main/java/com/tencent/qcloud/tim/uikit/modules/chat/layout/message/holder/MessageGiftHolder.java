package com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMFaceElem;
import com.tencent.imsdk.v2.V2TIMFileElem;
import com.tencent.imsdk.v2.V2TIMGroupTipsElem;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMLocationElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.UnsupportedEncodingException;

/**
 * 自定义礼物消息
 */
public class MessageGiftHolder extends MessageContentHolder {
    private String TAG = MessageGiftHolder.class.getSimpleName();
    private TextView msgBodyText;
    private ImageView msgGiftImges;

    public MessageGiftHolder(View itemView) {
        super(itemView);
    }


    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_gift;
    }

    @Override
    public void initVariableViews() {
        msgGiftImges = rootView.findViewById(R.id.msg_gift_imges);
        msgBodyText = rootView.findViewById(R.id.msg_body_tv);
    }

    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
        msgGiftImges.setVisibility(View.VISIBLE);
        msgBodyText.setVisibility(View.VISIBLE);
        V2TIMMessage timMessage = msg.getTimMessage();
        V2TIMLocationElem locationElem = timMessage.getLocationElem();  //获取位置描述
        V2TIMCustomElem customElem = timMessage.getCustomElem();        //获取自定义数据
        V2TIMFaceElem faceElem = timMessage.getFaceElem();              //获取表情索引
        V2TIMImageElem imageElem = timMessage.getImageElem();           //获取原图本地文件路径，只对消息发送方有效
        V2TIMFileElem fileElem = timMessage.getFileElem();              //获取文件路径（只有发送方才能获取到）
        V2TIMGroupTipsElem groupTipsElem = timMessage.getGroupTipsElem();
        V2TIMOfflinePushInfo offlinePushInfo = timMessage.getOfflinePushInfo();
        msgGiftImges.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onMessageLongClick(view, position, msg);
                }
                return true;
            }
        });
    }

}
