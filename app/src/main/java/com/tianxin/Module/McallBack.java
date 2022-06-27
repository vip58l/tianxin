package com.tianxin.Module;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.UserInfo;

/**
 * 引用此类判断服务器是否正常
 */
public class McallBack {
    private static McallBack mcallBack;
    private String TAG = McallBack.class.getName();
    private Callback callback;
    private UserInfo userInfo;
    private Context context;
    private String msg;
    private Gson gson = new Gson();
    private static Datamodule datamodule;

    public McallBack(Context context) {
        this.context = context;
        this.userInfo = UserInfo.getInstance();
        this.datamodule = new Datamodule(context);

    }

    public static McallBack query(Context context, Callback callback) {
        if (mcallBack == null) {
            mcallBack = new McallBack(context);
        }
        mcallBack.setCallback(callback);
        return mcallBack;
    }

    public static McallBack query(Context context, String msg, Callback callback) {
        if (mcallBack == null) {
            mcallBack = new McallBack(context);
        }
        mcallBack.setCallback(callback, msg);
        return mcallBack;
    }

    /**
     * 转到邀请好友
     * Activity
     *
     * @param context
     */
    public static void starsetAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, References.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 删除服务器的数据
     */
    public static void del(int id) {
        if (datamodule == null) {
            datamodule = new Datamodule(DemoApplication.instance());
        }
        datamodule.deletetrend(String.valueOf(id));
    }

    public void chehck(String msg) {
        /**
         * 封号帐号无权发布
         */
        if (userInfo.getState() >= Constants.TENCENT3) {
            callback.onSuccess(context.getString(R.string.tv_msg192));
            return;
        }

        if (TextUtils.isEmpty(msg)) {
            callback.onSuccess(context.getString(R.string.tv_ss1));
            return;
        }

        if (msg.length() < 5) {
            callback.onSuccess(context.getString(R.string.tv_ss2));
            return;
        }

        //服务器是否能打开
        datamodule.member_user(callback);
    }

    /**
     * 监听事件
     *
     * @param callback
     */
    public void setCallback(Callback callback) {
        this.callback = callback;
        datamodule.member_user(callback);
    }

    /**
     * 监听事件
     *
     * @param callback
     * @param msg
     */
    public void setCallback(Callback callback, String msg) {
        this.callback = callback;
        this.msg = msg;
        chehck(msg);

    }

    /**
     * 生成哈希值
     */
    public String HashCode(String str) {
        int i1 = str.hashCode() & 0x7FFFFFFF;//0x7fffffff代表int的最大值
        return String.valueOf(i1);
    }

    public int HashCode(String str, int TYPE) {
        int i1 = str.hashCode() & 0x7FFFFFFF;//0x7fffffff代表int的最大值
        return i1;
    }

}
