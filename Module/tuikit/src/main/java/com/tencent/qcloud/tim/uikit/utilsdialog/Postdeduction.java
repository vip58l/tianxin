/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/9 0009
 */


package com.tencent.qcloud.tim.uikit.utilsdialog;

import android.util.Log;

import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.utils.HttpUtils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询金币类
 */
public class Postdeduction {

    /**
     * 请求API地址
     */
    private static String http = com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API;
    /**
     * 修改群基础资料
     */
    public static String modifygroupbaseinfo = http + "/modifygroupbaseinfo";
    /**
     * 当前组状态禁言
     */
    public static String groupchat = http + "/groupchat";
    /**
     * 删除群组已解散下架
     */
    public static String desdeletegroup = http + "/desdeletegroup";
    /**
     * #App管理员可以通过该接口向指定的群中添加新成员
     */
    public static String addgroupmember = http + "/addgroupmember";
    /**
     * 获取在线用户
     */
    public static String getlistmember = http + "/getlistmember";
    /**
     * 记录双方的ID消息
     */
    public static String callvideo = http + "/callvideo";
    //用户金额查询
    public static String goldcoin = http + "/goldcoin";

    private static final String TAG = Postdeduction.class.getSimpleName();

    /**
     * 向服务器发送扣费
     */
    public static void deduction(boolean callingparty, String userModel, String mSponsorUserModel, String money, int TYPE) {
        UserInfo userInfo = UserInfo.getInstance();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userInfo.getUserId());                         //主叫
        params.put(Constants.TOKEN, userInfo.getToken());                           //主叫token
        params.put("sendoutid", callingparty ? userModel : mSponsorUserModel);      //主叫
        params.put("receiveid", callingparty ? mSponsorUserModel : userModel);      //被叫
        params.put("type", String.valueOf(TYPE));                                   //视频或语音
        params.put("money", money);                                                 //扣费金币
        params.put("call", callingparty ? "1" : "2");                               //谁是发起方
        HttpUtils.RequestPost(com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API + "/deduction", params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success: " + response);

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "message: " + message);
            }
        });
    }

    /**
     * 被叫方向服务器发送通话记录
     */
    public static void setcallvideo(int TYPE, String userModel, String mSponsorUserModel) {
        UserInfo userInfo = UserInfo.getInstance();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userInfo.getUserId());                  //被叫
        params.put(Constants.TOKEN, userInfo.getToken());                    //被叫token
        params.put("sendoutid", mSponsorUserModel);                          //主叫
        params.put("receiveid", userModel);                                   //被叫
        params.put("type", String.valueOf(TYPE));                    //视频或语音
        HttpUtils.RequestPost(callvideo, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success: " + response);

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "message: " + message);
            }
        });
    }

    /**
     * 向服务查询金币情况
     */
    public static void goldcoin(String userid, String token, String touserid, Callback callback) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userid);
        params.put(Constants.TOKEN, token);
        params.put(Constants.TOUSERID, touserid);
        HttpUtils.RequestPost(goldcoin, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });
    }

    /**
     * 查询自己的VIP状态
     */
    public static void getjson(Map<String, String> params, String path, final Callback callback) {
        HttpUtils.RequestPost(com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API + path, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "onFailed: " + message);
            }
        });
    }

    /**
     * 查询自己的VIP状态
     */
    public static void getapi(Map<String, String> params, String path, final Callback callback) {
        HttpUtils.RequestPost(path, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                if (callback != null) {
                    callback.onSuccess(response);
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.toastLongMessage(message);
                callback.onFailed();
            }
        });
    }

    /**
     * 向服务器查询在线用户
     */
    public static void getlistmember(HttpUtils.HttpListener httpListener) {
        UserInfo userInfo = UserInfo.getInstance();
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userInfo.getUserId());                  //主叫
        params.put(Constants.TOKEN, userInfo.getToken());                    //主叫token
        HttpUtils.RequestPost(getlistmember, params, httpListener);
    }

    public interface Callback {
        default void onSuccess(String msg) {
        }

        default void onSuccess() {
        }

        default void onFall() {
        }

        void onFailed();
    }


}
