package com.tencent.qcloud.tim.uikit.modules.message;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;

import java.util.ArrayList;
import java.util.List;


public class MessageRevokedManager extends V2TIMAdvancedMsgListener {
    private String TAG = MessageRevokedManager.class.getSimpleName();
    private static final MessageRevokedManager instance = new MessageRevokedManager();
    private final List<MessageRevokeHandler> mHandlers = new ArrayList<>();

    private MessageRevokedManager() {
    }

    public static MessageRevokedManager getInstance() {
        return instance;
    }

    @Override
    public void onRecvMessageRevoked(String msgID) {
        Log.d(TAG, "onRecvMessageRevoked: ");
        for (int i = 0; i < mHandlers.size(); i++) {
            mHandlers.get(i).handleInvoke(msgID);
        }
    }

    public void addHandler(MessageRevokeHandler handler) {
        Log.d(TAG, "addHandler: ");
        if (!mHandlers.contains(handler)) {
            mHandlers.add(handler);
        }
    }

    public void removeHandler(MessageRevokeHandler handler) {
        Log.d(TAG, "removeHandler: ");
        mHandlers.remove(handler);
    }

    public interface MessageRevokeHandler {
        void handleInvoke(String msgID);
    }
}
