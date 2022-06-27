package com.tianxin.Module;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.opensource.model.byprice;
import com.tencent.opensource.model.mpayurl;
import com.tianxin.Module.api.BaseClass;
import com.tianxin.Module.api.amainPage;
import com.tianxin.Module.api.banner;
import com.tianxin.Module.api.buypaymoney;
import com.tianxin.Module.api.comment;
import com.tianxin.Module.api.goldcoin;
import com.tianxin.Module.api.memberparent;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.order;
import com.tianxin.Module.api.order_moeny;
import com.tianxin.Module.api.qrcode;
import com.tianxin.Module.api.qrinfo;
import com.tianxin.Module.api.reward;
import com.tianxin.Module.api.Share;
import com.tianxin.Module.api.shouchangmember;
import com.tianxin.Module.api.wmoney;
import com.tianxin.Receiver.videoService;
import com.tianxin.Util.Logi;
import com.tianxin.Util.SystemUtil;
import com.tianxin.activity.register.activity_reg;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.dialog.dialog_item_view_yhk;
import com.tianxin.widget.Banner;
import com.tianxin.wxapi.sdk.WXPlay;
import com.tencent.opensource.model.BackCallResult;
import com.tencent.opensource.model.Basicconfig;
import com.tencent.opensource.model.Gamefinish;
import com.tencent.opensource.model.Gametitle;
import com.tencent.opensource.model.GetRoomHotNewSome;
import com.tencent.opensource.model.LiveRoom;
import com.tencent.opensource.model.Live_RoomSelect;
import com.tencent.opensource.model.Livevideo;
import com.tencent.opensource.model.Renzheng;
import com.tencent.opensource.model.ShareDate;
import com.tencent.opensource.model.amountconfig;
import com.tencent.opensource.model.chat;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.Module.api.getotalPage;
import com.tianxin.Module.api.message;
import com.tianxin.Module.api.present;
import com.tianxin.Module.api.reguserinfo;
import com.tianxin.Module.api.sainPage;
import com.tianxin.Module.api.version;
import com.tianxin.Module.api.videotitle;
import com.tianxin.R;
import com.tianxin.Util.Comms;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.adapter.setAdapter;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.getHandler.PostModule;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.getlist.HotRooms;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.AES.Resultinfo;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.opensource.model.HttpUtils;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.Citychat;
import com.tencent.opensource.model.Citydate;
import com.tencent.opensource.model.chatmoney;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.Gamelist;
import com.tencent.opensource.model.givelist;
import com.tencent.opensource.model.imglist;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.moneyLog;
import com.tencent.opensource.model.Music;
import com.tencent.opensource.model.navigation;
import com.tencent.opensource.model.orderdetailed;
import com.tencent.opensource.model.Party;
import com.tencent.opensource.model.partyname;
import com.tencent.opensource.model.partyname2;
import com.tencent.opensource.model.personal;
import com.tencent.opensource.model.sysmessage;
import com.tencent.opensource.model.tpconfig;
import com.tencent.opensource.model.trend;
import com.tencent.opensource.model.trend_comment;
import com.tencent.opensource.model.tupianzj;
import com.tencent.opensource.model.videoPush;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.costransferpractice.common.base.BaseModeul;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.Constants.id;
import static com.tianxin.getHandler.JsonUitl.stringToList;
import static com.tencent.liteav.debug.GenerateTestUserSig.SDKAPPID;
import static com.tencent.liteav.model.ITRTCAVCall.TYPE_VIDEO_CALL;

import org.json.JSONObject;

/**
 * 封装请求参数类
 */
public class Datamodule<T> {
    private static final String TAG = Datamodule.class.getName();
    private static Datamodule Instance;
    private Context context;
    private UserInfo userInfo;
    private BaseClass baseClass;
    private int page;
    private Callback callback;
    private Paymnets paymnets;
    public Mesresult mesresult;
    private AMapLocation mapLocation;
    private UserModel mSelfModel;    //表示当前用户的UserModel
    private UserModel mSearchModel;  //表示当前对方的usermodel
    private Gson gson;

    /**
     * 初始化
     *
     * @param
     * @return
     */
    public static Datamodule getInstance() {
        if (Instance == null) {
            Instance = new Datamodule(DemoApplication.instance());
        }
        return Instance;
    }

    /**
     * 初始化
     *
     * @param context
     * @return
     */
    public static Datamodule getInstance(Context context) {

        if (Instance == null) {
            Instance = new Datamodule(context);
        }
        return Instance;
    }

    public Datamodule(Context context) {
        this.context = context;
        this.userInfo = UserInfo.getInstance(context);
        this.gson = new Gson();
    }

    public Datamodule(Context context, Paymnets paymnets) {
        this.context = context;
        this.userInfo = UserInfo.getInstance();
        this.gson = new Gson();
        this.paymnets = paymnets;
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    /**
     * 我的关注
     *
     * @param page
     * @param paymnets
     */
    public void listowlist(int page, Paymnets paymnets) {
        String path = String.format(Webrowse.listowlist + "?userid=%s&page=%s&token=%s", userInfo.getUserId(), page, userInfo.getToken());
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<followlist> listm = stringToList(decrypt, followlist.class);
                        paymnets.onSuccess(listm);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

        });
    }

    /**
     * Loin登录APP
     *
     * @param user
     * @param pwd
     * @param paymnets
     */
    public void login(String user, String pwd, int delelogout, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.username, user)
                .add(Constants.password, pwd)
                .add(Constants.login, String.valueOf(delelogout))
                .add(Constants.viecode, String.valueOf(Config.getVersionCode()))
                .add(Constants.model, TextUtils.isEmpty(SystemUtil.showlog(DemoApplication.instance())) ? "" : SystemUtil.showlog(DemoApplication.instance()))
                .build();

        PostModule.postModule(Webrowse.memberlogin2, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                Logi.d(TAG, "success: " + date);
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        try {
                            Mesresult mesresult2 = gson.fromJson(mesresult1.getData(), Mesresult.class);
                            paymnets.onSuccess(mesresult2);
                        } catch (Exception e) {
                            e.printStackTrace();
                            paymnets.onFail(e.getMessage());
                        }
                    } else {
                        switch (mesresult1.getStatus()) {
                            case "-999":
                                //注销申请存在
                                paymnets.onRefresh();
                                break;
                            case "-3":
                                paymnets.onError();
                                break;
                            default:
                                paymnets.onFail(mesresult1.getMsg());
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail(e.getMessage());

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail(getString(R.string.isNetworkAvailable));
            }

            @Override
            public void onFail() {
                paymnets.onFail(getString(R.string.isNetworkAvailable));
            }
        });

    }

    /**
     * 获取用户VIP状态 头像状态 等级
     *
     * @param paymnets
     */
    public void getvip(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.getvip + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    switch (mesresult.getStatus()) {
                        case "-1":
                            if (paymnets != null) {
                                paymnets.onLoadMore();
                            }
                            break;
                        case "3":
                            if (paymnets != null) {
                                userInfo.setState(Integer.parseInt(mesresult.getStatus()));
                                paymnets.onRefresh();
                            }
                            break;
                        default:
                            if (mesresult.isSuccess()) {
                                //获取最新的状态
                                Mesresult mesresult1 = gson.fromJson(mesresult.getData(), Mesresult.class);
                                activity_sesemys.user_save_update(mesresult1);

                                if (paymnets != null) {
                                    paymnets.onSuccess();
                                }
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void isNetworkAvailable() {

            }

        });
    }

    /**
     * 关注对方
     *
     * @param getuserid
     * @param paymnets
     */
    public void gofollowlist(String getuserid, Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule(Webrowse.gofollowlist + "?userid=" + userInfo.getUserId() + "&tid=" + getuserid + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    switch (mesresult.getStatus()) {
                        case "1":
                            //关注成功
                            paymnets.payens();
                            break;
                        case "0":
                            //取消关注
                            paymnets.onError();
                            break;
                        case "-3":
                            //关注已达上限
                            paymnets.payonItemClick(mesresult.getMsg(), 0);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 查询关注状态
     *
     * @param getuserid
     * @param paymnets
     */
    public void getfollowok(String getuserid, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.touserid, getuserid);
        String path = Webrowse.getfollowok + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    switch (mesresult.getStatus()) {
                        case "1":
                            paymnets.payens();
                            break;
                        case "0":
                            paymnets.onError();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取个人详情
     *
     * @param getuserid
     */
    public void getHashmap(String getuserid, Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> key = new HashMap();
        key.put(Constants.USERID, userInfo.getUserId());
        key.put(Constants.touserid, getuserid);
        key.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.member + "?" + Webrowse.getMap(key, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        if (!TextUtils.isEmpty(decrypt)) {
                            member member = gson.fromJson(decrypt, member.class);
                            paymnets.onSuccess(member);
                        }
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 最新动态
     *
     * @param getUserId
     * @param totalPage
     * @param TYPE
     * @param paymnets
     */
    public void fmessage(String getUserId, int totalPage, int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(TYPE));
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.sex, userInfo.getSex().equals("1") ? "2" : "1");
        map.put(Constants.touserid, getUserId);
        String path = Webrowse.trend + "?" + Comms.getStringBuffer(map, 1);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<trend> ts = JsonUitl.stringToList(decrypt, trend.class);
                        paymnets.onSuccess(ts);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 最新动态
     *
     * @param getUserId
     * @param totalPage
     * @param TYPE
     * @param paymnets
     */
    public void fmessages(String getUserId, int totalPage, int TYPE, Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(TYPE));
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.sex, userInfo.getSex().equals("1") ? "2" : "1");
        map.put(Constants.touserid, TextUtils.isEmpty(getUserId) ? "" : getUserId);
        String path = Webrowse.trends + "?" + Comms.getStringBuffer(map, 1);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<trend> ts = JsonUitl.stringToList(decrypt, trend.class);
                        paymnets.onSuccess(ts);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取首页列表
     *
     * @param totalPage
     * @param mapLocation
     * @param type
     * @param paymnets
     */
    public void one2list(int totalPage, AMapLocation mapLocation, int type, Paymnets paymnets) {
        this.paymnets = paymnets;
        String province = null;
        String city = null;
        String district = null;
        if (mapLocation != null) {
            province = mapLocation.getProvince();
            city = mapLocation.getCity();
            district = mapLocation.getDistrict();
        }
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.PAGE, String.valueOf(totalPage))
                .add(Constants.TYPE, String.valueOf(type))
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.province, TextUtils.isEmpty(province) ? "" : province)
                .add(Constants.city, TextUtils.isEmpty(city) ? "" : city)
                .add(Constants.district, TextUtils.isEmpty(district) ? "" : district)
                .build();
        PostModule.postModule(Webrowse.memberlist, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        if (!TextUtils.isEmpty(decrypt)) {
                            List<member> members = stringToList(decrypt, member.class);
                            paymnets.onSuccess(members);
                        }
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onError();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    /**
     * 初始化用户金币
     */
    public void getbalance(Paymnets paymnets) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        PostModule.getModule(String.format("%s?%s", Webrowse.goldcoin, Webrowse.getMap(map, 3)), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    //成功或失败都会有数据返回
                    paymnets.onSuccess(gson.fromJson(mesresult.getData(), info.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 获取语音或视频通话扣费配置
     */
    public void getallcharge(Paymnets paymnets) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        PostModule.getModule(String.format("%s?%s", Webrowse.allcharge, Webrowse.getMap(map, 3)), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    switch (mesresult.getStatus()) {
                        case "1":
                            Allcharge allcharge = gson.fromJson(mesresult.getData(), Allcharge.class);
                            paymnets.onSuccess(allcharge);
                            break;
                        default:
                            paymnets.onSuccess(mesresult.getMsg());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取语音或视频通话扣费配置
     */
    public void getallcharge(String touserid, Paymnets paymnets) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.touserid, touserid);
        PostModule.getModule(String.format("%s?%s", Webrowse.allcharge, Webrowse.getMap(map, 3)), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    switch (mesresult.getStatus()) {
                        case "1":
                            Allcharge allcharge = gson.fromJson(mesresult.getData(), Allcharge.class);
                            paymnets.onSuccess(allcharge);
                            break;
                        default:
                            paymnets.onSuccess(mesresult.getMsg());
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 搜索手机号
     *
     * @param phoneNumber
     * @param type
     */
    public void searchContactsByPhone(String phoneNumber, int type, member member) {

        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }

        /**
         * 搜索ID号码
         */
        ProfileManager.getInstance().getUserInfoByUserId(phoneNumber, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {
                //拿到查询回调的数据
                mSearchModel = model;
                if (!TextUtils.isEmpty(member.getTruename())) {
                    mSearchModel.userName = member.getTruename();
                }
                if (!TextUtils.isEmpty(member.getPicture())) {
                    mSearchModel.userAvatar = member.getPicture();
                }
                mSearchModel.Gender = member.getSex(); //性别
                mSearchModel.tRole = 1;     //角色 0接收 1拨打
                mSearchModel.Level = 0;     //级别
                mSearchModel.AllowType = 0; //允许类型
                startCallSomeone(type); //开始呼叫对方
            }

            @Override
            public void onFailed(int code, String msg) {
                Toashow.show(getString(R.string.tv_msg167));
            }
        });
    }

    /**
     * 开始呼叫某人
     */
    private void startCallSomeone(int mType) {
        //创建自己的资料
        mSelfModel = ProfileManager.getInstance().getUserModel();
        UserModel selfInfo = new UserModel();
        selfInfo.userId = mSelfModel.userId;
        selfInfo.userAvatar = mSelfModel.userAvatar;
        selfInfo.userName = mSelfModel.userName;
        selfInfo.token = userInfo.getToken();
        ProfileManager.getInstance().setUserModel(selfInfo);


        //创建对方的资料
        List<UserModel> models = new ArrayList<>();
        UserModel callUserInfo = new UserModel();
        callUserInfo.userId = mSearchModel.userId;
        callUserInfo.userAvatar = mSearchModel.userAvatar;
        callUserInfo.userName = mSearchModel.userName;
        models.add(callUserInfo);

        if (mType == TYPE_VIDEO_CALL) {
            ToastUtils.showShort(getString(R.string.tv_msg168) + callUserInfo.userName);
            TRTCVideoCallActivity.startCallSomeone(DemoApplication.instance(), models);
            //声网视频
            //CallVideo.startCallSomeone(getContext(), models);
        } else {
            ToastUtils.showShort(getString(R.string.tv_msg169) + callUserInfo.userName);
            TRTCAudioCallActivity.startCallSomeone(DemoApplication.instance(), models);
        }

    }

    /**
     * 获取个人主页信息
     */
    public void picenterHashMap(String getuserid, Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.touserid, getuserid);
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.member + "?" + Comms.getStringBuffer(map, 1);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.fall(code);
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        member member = gson.fromJson(decrypt, member.class);
                        paymnets.onSuccess(member);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获取对方的个人主页信息
     */
    public void Personalcontent(String getuserid, Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.touserid, getuserid);
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.member + "?" + Comms.getStringBuffer(map, 1);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.fall(code);
            }

            @Override
            public void success(String date) {

                try {
                    Type type = new TypeToken<BackCallResult<Object>>() {
                    }.getType();

                    BackCallResult backCallResult = gson.fromJson(date, type);
                    String src = (String) backCallResult.getData();

                    if (backCallResult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(src);
                        member member = gson.fromJson(decrypt, member.class);
                        paymnets.onSucces(member);
                    } else {
                        paymnets.onFail(backCallResult.getMsg());
                        Toashow.show(backCallResult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获取相册列表
     */
    public void getimglist(String getuserid, Paymnets paymnets, int page) {
        this.paymnets = paymnets;
        this.page = page;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.touserid, getuserid);
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(page));
        String path = Webrowse.imglist + "?" + Comms.getStringBuffer(map, 1);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        Type objectType = new TypeToken<BaseClass<imglist>>() {
                        }.getType();
                        baseClass = gson.fromJson(decrypt, objectType);
                        List<imglist> listdata = baseClass.getData();
                        paymnets.onSuccess(listdata);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 提交反馈
     */
    public void PostModule(String msg, String mphone, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, "00")
                .add("video", "0")
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.hostuserid, userInfo.getUserId())
                .add(Constants.reason, msg)
                .add(Constants.TYPE, "8")
                .add(Constants.phone, mphone).build();
        PostModule.postModule(Webrowse.report, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    paymnets.onSuccess(mesresult);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 跟据关建字搜索视频内容
     *
     * @param selectkey
     * @param totalPage
     */
    public void getdate(String selectkey, String totalPage, Paymnets paymnets) {
        Map<String, String> key = new HashMap();
        key.put(Constants.PAGE, String.valueOf(totalPage));
        key.put(Constants.USERID, userInfo.getUserId());
        key.put(Constants.Token, userInfo.getToken());
        key.put(Constants.Token, userInfo.getToken());
        key.put(Constants.key, selectkey);
        String path = Webrowse.selectvideolist + "?" + Webrowse.getMap(key, 3);
        PostModule.getModule(path, new Paymnets() {

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {

                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        if (paymnets != null) {
                            paymnets.success(mesresult.getData());
                        }
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }
        });
    }

    /**
     * 联网请求获取数据
     */
    public void getdatehttp(int totalPage, Paymnets paymnets) {
        this.page = totalPage;
        this.paymnets = paymnets;
        Map<String, String> key = new HashMap();
        key.put(Constants.PAGE, String.valueOf(totalPage));
        key.put(Constants.USERID, userInfo.getUserId());
        key.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.mypicturelist + "?" + Webrowse.getMap(key, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        //复杂的泛型：T
                        Type objectType = new TypeToken<BaseClass<imglist>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(decrypt, objectType);
                        List<imglist> data = baseClass.getData();
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });

    }

    /**
     * 第三方采集直播源1
     * https://wap.yequ.live/api/mainpage/mainPage/hotRooms
     *
     * @param paymnets
     */
    public void getallchargess(Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule(String.format("%s/hotRooms", BuildConfig.wapyequlive), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<item> list = new ArrayList<>();
                    amainPage amainPage = gson.fromJson(date, amainPage.class);
                    sainPage data = amainPage.data;
                    List<HotRooms> datas = data.datas;
                    for (HotRooms rooms : datas) {
                        item item = new item();
                        item.type = setAdapter.two;
                        item.object = rooms;
                        list.add(item);
                    }
                    paymnets.onSuccess(list);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 第三方采集直播源2
     * https://wap.yequ.live/api/mainpage/mainPage/hotRooms
     *
     * @param paymnets
     */
    public void GetRoomHotNewSome(Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule("http://www.miaobolive.com/Ajax/Home/GetRoomHotNewSome.ashx?&page=1", new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    JSONObject jsonObject = new JSONObject(date);
                    String table = jsonObject.getString("table");
                    List<GetRoomHotNewSome> getRoomHotNewSomes = stringToList(table, GetRoomHotNewSome.class);
                    List<item> list = new ArrayList<>();
                    for (GetRoomHotNewSome getRoomHotNewSome : getRoomHotNewSomes) {
                        item item = new item();
                        HotRooms rooms = new HotRooms();
                        rooms.setId(String.valueOf(getRoomHotNewSome.getUseridx()));
                        rooms.setHdlUrl(getRoomHotNewSome.getFlv());
                        rooms.setHlsUrl(getRoomHotNewSome.getRtmp());
                        rooms.setChargeCoinBalance(getRoomHotNewSome.getSmallpic());
                        rooms.setBigImageOriginal(getRoomHotNewSome.getBigpic());
                        rooms.setHeaderImageOriginal(getRoomHotNewSome.getBigpic());
                        rooms.setNickName(getRoomHotNewSome.getMyname());
                        rooms.setName(getRoomHotNewSome.getMyname());
                        rooms.setGps(getRoomHotNewSome.getGps());
                        rooms.setFamilyName(getRoomHotNewSome.getFamilyName());
                        item.type = setAdapter.two;
                        item.object = rooms;
                        list.add(item);
                    }
                    paymnets.onSuccess(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 第三方采集直播源3
     * https://wap.yequ.live/api/mainpage/mainPage/hotRooms
     *
     * @param paymnets
     */
    public void Live_RoomSelect(Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule("http://www.miaobolive.com/Ajax/live/Live_RoomSelect.aspx", new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<Live_RoomSelect> ts = stringToList(date, Live_RoomSelect.class);
                    List<item> list = new ArrayList<>();
                    for (Live_RoomSelect roomSelect : ts) {
                        item item = new item();
                        HotRooms rooms = new HotRooms();
                        rooms.setId(String.valueOf(roomSelect.getUseridx()));
                        rooms.setHdlUrl(roomSelect.getFlv());
                        rooms.setHlsUrl(roomSelect.getRtmp());
                        rooms.setChargeCoinBalance(roomSelect.getSmallpic());
                        rooms.setBigImageOriginal(roomSelect.getBigpic());
                        rooms.setHeaderImageOriginal(roomSelect.getBigpic());
                        rooms.setNickName(roomSelect.getMyname());
                        rooms.setName(roomSelect.getMyname());
                        rooms.setGps(roomSelect.getGps());
                        rooms.setFamilyName(roomSelect.getFamilyname());
                        item.type = setAdapter.two;
                        item.object = rooms;
                        list.add(item);
                    }
                    paymnets.onSuccess(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 直播源获取
     *
     * @param paymnets
     */
    public void getaLivevideo(int totalPage, Paymnets paymnets) {
        Map<String, String> key = new HashMap();
        key.put(Constants.PAGE, String.valueOf(totalPage));
        key.put(Constants.USERID, userInfo.getUserId());
        key.put(Constants.Token, userInfo.getToken());
        key.put(Constants.TYPE, "1");
        key.put(Constants.status, "0");
        key.put(Constants.touserid, "");
        String path = Webrowse.Livevideo + "?" + Webrowse.getMap(key, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<Livevideo> livevideos = stringToList(decrypt, Livevideo.class);
                        List<item> list = new ArrayList<>();
                        for (Livevideo livevideo : livevideos) {
                            item item = new item();
                            HotRooms rooms = new HotRooms();
                            rooms.setId(livevideo.getId());
                            rooms.setHdlUrl(livevideo.getTencent() == 1 ? DemoApplication.presignedURL(livevideo.getPlayurl()) : TextUtils.isEmpty(livevideo.getPlaytest()) ? livevideo.getPlayurl() : livevideo.getPlaytest());
                            rooms.setHlsUrl(livevideo.getTencent() == 1 ? DemoApplication.presignedURL(livevideo.getPlayurl()) : TextUtils.isEmpty(livevideo.getPlaytest()) ? livevideo.getPlayurl() : livevideo.getPlaytest());
                            rooms.setChargeCoinBalance(livevideo.getTencent() == 1 ? DemoApplication.presignedURL(livevideo.getBigpicurl()) : livevideo.getBigpicurl());
                            rooms.setBigImageOriginal(livevideo.getTencent() == 1 ? DemoApplication.presignedURL(livevideo.getBigpicurl()) : livevideo.getBigpicurl());
                            rooms.setHeaderImageOriginal(livevideo.getTencent() == 1 ? DemoApplication.presignedURL(livevideo.getBigpicurl()) : livevideo.getBigpicurl());
                            rooms.setNickName(livevideo.getTitle());
                            item.type = com.tianxin.adapter.setAdapter.two;
                            item.object = rooms;
                            list.add(item);
                        }
                        paymnets.onSuccess(list);
                    } else {
                        paymnets.onError();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }


        });
    }

    /**
     * 获取验证码
     *
     * @param mobile
     * @param paymnets
     */
    public void postcode(String mobile, Paymnets paymnets) {
        this.paymnets = paymnets;
        if (TextUtils.isEmpty(mobile)) {
            paymnets.onError();
            return;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.mobile, mobile)
                .build();
        PostModule.postModule(Webrowse.postcode, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                        if (!TextUtils.isEmpty(mesresult.getCode())) {
                            paymnets.onSuccess(mesresult.getCode());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.cancellation();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 验证码请求
     */
    public void postcode1(reguserinfo info, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.mobile, info.getUsername())
                .build();
        PostModule.postModule(Webrowse.code, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult);
                        if (!TextUtils.isEmpty(mesresult.getCode())) {
                            paymnets.onSuccess(mesresult.getCode());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 验证码请求
     */
    public void postcode1(String mobile, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.mobile, mobile)
                .build();
        PostModule.postModule(Webrowse.code, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult);
                        if (!TextUtils.isEmpty(mesresult.getCode())) {
                            paymnets.onSuccess(mesresult.getCode());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }


    /**
     * 验证码请求
     */
    public void postcode2(reguserinfo info, Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule(Webrowse.code + "?mobile=" + info.getUsername(), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult);
                        if (!TextUtils.isEmpty(mesresult.getCode())) {
                            paymnets.onSuccess(mesresult.getCode());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param mobile
     * @param paymnets
     */
    public void getcode(String mobile, Paymnets paymnets) {
        this.paymnets = paymnets;
        if (TextUtils.isEmpty(mobile)) {
            paymnets.onError();
            return;
        }
        PostModule.getModule(Webrowse.code + "?mobile=" + mobile, new Paymnets() {

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                        if (!TextUtils.isEmpty(mesresult.getCode())) {
                            paymnets.onSuccess(mesresult.getCode());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.cancellation();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });

    }

    /**
     * 带有图片上传
     * 发布最新动态 朋友圈
     *
     * @param paymnets
     * @param msgtext
     * @param image
     * @param video
     */
    public void upselecttext(Paymnets paymnets, String msgtext, String image, String video) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TITLE, msgtext);
        map.put(Constants.content, "");
        map.put(Constants.image, TextUtils.isEmpty(image) ? "" : image);
        map.put(Constants.playvideo, TextUtils.isEmpty(video) ? "" : video);
        map.put(Constants.Msg, "");
        map.put(Constants.sex, userInfo.getSex());
        map.put(Constants.love, "1");
        map.put(Constants.tencent, String.valueOf(Constants.TENCENT));
        String path = Webrowse.addtrend + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }


        });
    }


    /**
     * POST请求数据 保存到服务端
     */
    public void addvideolist(String accessUrl, String title, Paymnets paymnets) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userInfo.getUserId());
        params.put(Constants.Token, userInfo.getToken());
        params.put("title", title);
        params.put("playurl", accessUrl);
        params.put("bigpicurl", accessUrl);
        params.put("status", "0");
        params.put("video", "0");
        params.put("tencent", "1");
        String posturl = Webrowse.addvideolist + "?" + Webrowse.getMap(params, 3);
        PostModule.getModule(posturl, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail(mesresult.getMsg());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 礼物列表
     */
    public void giftlist() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.giftinfo + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void success(String date) {
                try {
                    if (paymnets != null) {
                        paymnets.activity(date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }
        });
    }

    /**
     * 礼物赠送扣除金币
     *
     * @param
     */
    public void getpost(present present, GiftInfo giftInfo, GiftPanelDelegate giftPanelDelegate) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.gif, giftInfo.giftId);
        map.put(Constants.NAME, TextUtils.isEmpty(present.getName()) ? "" : present.getName());
        map.put(Constants.money, String.valueOf(giftInfo.price));
        map.put(Constants.anchorid, TextUtils.isEmpty(present.getTouserid()) ? "" : present.getTouserid());
        map.put(Constants.TYPE, present.getTYPE() == 0 ? "" : String.valueOf(present.getTYPE()));
        String path = Webrowse.jbdsgoldcoin + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        if (paymnets != null) {
                            paymnets.success(String.valueOf(mesresult.getMoney()));
                            getbalance(paymnets);//刷新查询金币
                            //金币扣成功 回调允许动画特效
                            giftPanelDelegate.onGiftItemClick(giftInfo);
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.msg(mesresult.getMsg());
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }
        });
    }

    /**
     * 删除服务器图片数据
     *
     * @param imglist
     * @param paymnets
     */
    public void delete(imglist imglist, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(id, imglist.getId())
                .add(Constants.USERID, imglist.getUserid())
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.deleteimg, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void success(String date) {
                try {
                    mesresult = gson.fromJson(date, Mesresult.class);
                    if (paymnets != null) {
                        paymnets.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();

                }
            }
        });
    }

    /**
     * 删除平台视频数据
     *
     * @param videolist
     */
    public void delete(videolist videolist, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(id, videolist.getId())
                .add(Constants.USERID, videolist.getUserid())
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.deletevideo, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (paymnets != null) {
                        paymnets.onSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

                if (paymnets != null) {
                    paymnets.onFail();
                }

            }
        });
    }


    /**
     * 保存头像
     *
     * @param accessUrl
     */
    public void Postsave(String accessUrl, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.picture, accessUrl)
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.uploadimg, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(accessUrl);
                    }
                } catch (Exception e) {
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }

    /**
     * 红娘申请
     */
    public void matchmaker(Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.matchmaker, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    paymnets.success(mesresult.getMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 更新个人粢料
     *
     * @param paymnets
     */
    public void loadpage5(Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.getmember + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    reguserinfo info = gson.fromJson(date, reguserinfo.class);
                    activity_sesemys.user_save_update1(info);
                    paymnets.onSuccess(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });

    }

    /**
     * 直播
     *
     * @param paymnets
     */
    public void loadpage2(Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, "0");
        String path = Webrowse.configmsg + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Config_Msg configmsg = gson.fromJson(mesresult.getData(), Config_Msg.class);
                        Config_Msg.getInstance().setMsg(configmsg.getMsg());
                        paymnets.onSuccess(configmsg);
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 首页导航图标
     *
     * @param paymnets
     */
    public void navigation(Paymnets paymnets) {
        PostModule.getModule(Webrowse.navigation, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<navigation> navigations = JsonUitl.stringToList(mesresult.getData(), navigation.class);
                        paymnets.onSuccess(navigations);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }

    /**
     * 找回密码
     */
    public void Retrievepassword(String mobile, String code, Paymnets paymnets) {
        if (TextUtils.isEmpty(mobile)) {
            paymnets.onError();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            paymnets.onClick();
            return;

        }

        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.mobile, mobile.trim())
                .add(Constants.code, code.trim())
                .build();
        PostModule.postModule(Webrowse.Retrievepassword, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {

                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });

    }

    /**
     * 注册会员
     *
     * @param info
     * @param mapLocation
     * @param paymnets
     */
    public void RegUser(reguserinfo info, AMapLocation mapLocation, Paymnets paymnets) {
        this.paymnets = paymnets;
        this.mapLocation = mapLocation;
        String province = "";
        String city = "";
        String district = "";
        String address = "";
        String jwd = "";

        if (mapLocation != null) {
            province = mapLocation.getProvince();
            city = mapLocation.getCity();
            district = mapLocation.getDistrict();
            address = mapLocation.getAddress();
            jwd = String.format("%s,%s", mapLocation.getLongitude(), mapLocation.getLatitude());
        }

        String st = Config.isMobileNO(BaseModeul.getClipContent(context)) ? BaseModeul.getClipContent(context) : "";
        RequestBody requestBody = new FormBody.Builder()
                .add("truename", info.getTruename())
                .add("username", info.getUsername())
                .add("password", info.getPassword())
                .add("password1", info.getPassword2())
                .add("wx", info.getWx())
                .add("qq", info.getQq())
                .add("sex", String.valueOf(info.getSex()))
                .add("code", TextUtils.isEmpty(info.getCode()) ? "" : info.getCode())
                .add("parent", TextUtils.isEmpty(info.getParent()) ? st : info.getParent()) //获取剪切板内容
                .add("province", province)
                .add("city", city)
                .add("district", district)
                .add("address", address)
                .add("jwd", jwd)
                .add(Constants.viecode, String.valueOf(Config.getVersionCode()))
                .add("model", TextUtils.isEmpty(info.getModel()) ? "" : info.getModel())
                .build();
        PostModule.postModule(Webrowse.regmember, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    paymnets.onSuccess(e.getMessage());
                }
            }
        });
    }


    /**
     * 注册会员
     *
     * @param info
     * @param mapLocation
     * @param paymnets
     */
    public void RegUser2(reguserinfo info, AMapLocation mapLocation, Paymnets paymnets) {
        this.paymnets = paymnets;
        this.mapLocation = mapLocation;
        String province = "";
        String city = "";
        String district = "";
        String address = "";
        String jwd = "";

        if (mapLocation != null) {
            province = mapLocation.getProvince();
            city = mapLocation.getCity();
            district = mapLocation.getDistrict();
            address = mapLocation.getAddress();
            jwd = String.format("%s,%s", mapLocation.getLongitude(), mapLocation.getLatitude());
        }

        String st = Config.isMobileNO(BaseModeul.getClipContent(context)) ? BaseModeul.getClipContent(context) : "";
        RequestBody requestBody = new FormBody.Builder()
                .add("truename", info.getTruename())
                .add("username", info.getUsername())
                .add("password", info.getPassword())
                .add("password1", info.getPassword2())
                .add("wx", info.getWx())
                .add("qq", info.getQq())
                .add("sex", String.valueOf(info.getSex()))
                .add("code", TextUtils.isEmpty(info.getCode()) ? "" : info.getCode()) //验证码
                .add("parent", TextUtils.isEmpty(info.getParent()) ? st : info.getParent()) //获取剪切板内容邀请码
                .add("province", province)
                .add("city", city)
                .add("district", district)
                .add("address", address)
                .add("jwd", jwd)
                .add(Constants.viecode, String.valueOf(Config.getVersionCode()))
                .add("model", TextUtils.isEmpty(info.getModel()) ? "" : info.getModel())
                .build();
        PostModule.postModule(Webrowse.regmember2, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                Logi.d(TAG, "success: " + date);
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        Mesresult mesresult1 = gson.fromJson(mesresult.getData(), Mesresult.class);
                        mesresult1.setMsg(mesresult.getMsg());
                        paymnets.onSuccess(mesresult1);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    paymnets.onSuccess(e.getMessage());
                }
            }
        });
    }

    /**
     * 修改密码
     *
     * @param mobile
     * @param session
     * @param pwd1
     * @param pwd2
     * @param paymnets
     */
    public void editpwd(String mobile, String session, String pwd1, String pwd2, Paymnets paymnets) {

        if (TextUtils.isEmpty(mobile)) {
            paymnets.fall(1);
            return;
        }
        if (TextUtils.isEmpty(pwd1)) {
            paymnets.fall(2);
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            paymnets.fall(3);
            return;
        }
        if (!pwd1.equals(pwd2)) {
            paymnets.fall(4);
            return;
        }
        if (TextUtils.isEmpty(session)) {
            paymnets.fall(5);
            return;
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("mobile", mobile)
                .add("session", session)
                .add("pwd1", pwd1)
                .add("pwd2", pwd2)
                .build();
        PostModule.postModule(Webrowse.editpwd, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    paymnets.onSuccess(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });

    }

    /**
     * 随机匹配
     *
     * @param paymnets
     */
    public void loadmatching(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.sex, userInfo.getSex());
        PostModule.getModule(Webrowse.listmember + "?" + Webrowse.getMap(map, 3), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        Type objectType = new TypeToken<BaseClass<member>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(decrypt, objectType);
                        List<member> member = baseClass.getData();
                        paymnets.onSuccess(member);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }

    /**
     * 加载图片
     *
     * @param page
     * @param paymnets
     */
    public void activity_img_cover(int page, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(page));
        String path = Webrowse.perimgpic + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        Type objectType = new TypeToken<BaseClass<tupianzj>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(decrypt, objectType);
                        List<tupianzj> data = baseClass.getData();
                        paymnets.onClick(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 加载分类内容
     *
     * @param paymnets
     */
    public void videotitle(Paymnets paymnets) {
        PostModule.getModule(HttpUtils.videotitle, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<videotitle> videotitles = stringToList(mesresult.getData(), videotitle.class);
                        paymnets.onSuccess(videotitles);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 系统消息列表
     *
     * @param paymnets
     */
    public void sysmessage(int totpage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totpage));
        map.put(Constants.TYPE, "1");
        map.put(Constants.code, String.valueOf(Config.getVersionCode()));
        map.put("text", "news");
        String path = Webrowse.sysmessage + "?" + Webrowse.getMap(map, 3);

        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<sysmessage> list = JsonUitl.stringToList(mesresult.getData(), sysmessage.class);
                        paymnets.onSuccess(list);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 一建打招听对像会员
     *
     * @param paymnets
     */
    public void Randomgreet(Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.sex, userInfo.getSex())
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.randomgreet, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<member> members = JsonUitl.stringToList(decrypt, member.class);
                        paymnets.onSuccess(members);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取内置聊天消息内容
     */
    public void message(Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule(Webrowse.message, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {

                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<message> messages = stringToList(decrypt, message.class);
                        paymnets.onSuccess(messages);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }

    /**
     * 我的粉丝
     *
     * @param totalPage
     * @param paymnets
     */
    public void mylikeyout(int totalPage, Paymnets paymnets) {
        this.paymnets = paymnets;
        RequestBody requestBody = new FormBody.Builder()
                .add("sex", UserInfo.getInstance().getSex())
                .add("userid", UserInfo.getInstance().getUserId())
                .add("token", UserInfo.getInstance().getToken())
                .add("page", String.valueOf(totalPage))
                .build();
        PostModule.postModule(Webrowse.myfollowlist, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<followlist> followlistList = JsonUitl.stringToList(decrypt, followlist.class);
                        paymnets.onSuccess(followlistList);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 街拍视频分类
     *
     * @param totalPage
     * @param type
     * @param paymnets
     */
    public void sbaseadapter(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(type));
        map.put(Constants.status, String.valueOf(Constants.sText));
        String path = Webrowse.videolist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<videolist> data = JsonUitl.stringToList(decrypt, videolist.class);
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 我的视频列表内容
     *
     * @param totalPage
     * @param paymnets
     */
    public void getvidel(int totalPage, Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> key = new HashMap();
        key.put(Constants.PAGE, String.valueOf(totalPage));
        key.put(Constants.USERID, userInfo.getUserId());
        key.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.myvideolist + "?" + Webrowse.getMap(key, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<videolist> data = JsonUitl.stringToList(decrypt, videolist.class);
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 他的视频列表内容
     *
     * @param getUserId
     * @param totalPage
     * @param paymnets
     */
    public void totalpage(String getUserId, int totalPage, Paymnets paymnets) {
        Map<String, String> key = new HashMap();
        key.put(Constants.PAGE, String.valueOf(totalPage));
        key.put(Constants.TYPE, String.valueOf(BuildConfig.TYPE));
        key.put(Constants.USERID, userInfo.getUserId());
        key.put(Constants.touserid, getUserId);
        key.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.myvideolist + "?" + Webrowse.getMap(key, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<videolist> data = JsonUitl.stringToList(decrypt, videolist.class);
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    public void imgpage(int totalPage, tupianzj tupianzj, Paymnets paymnets) {
        this.paymnets = paymnets;
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.TITLE, tupianzj.getTitle());
        map.put(Constants.PERIMG, String.valueOf(tupianzj.getPerimg()));
        map.put(Constants.zuserid, String.valueOf(tupianzj.getUserid()));
        String path = Webrowse.perimgpic2 + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {

                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        Type objectType = new TypeToken<BaseClass<tupianzj>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(decrypt, objectType);
                        List<tupianzj> data = baseClass.getData();
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 提交扣费操作
     *
     * @param paymnets3
     * @param videolist
     */
    public void jinbi(Paymnets paymnets3, videolist videolist) {
        this.paymnets = paymnets3;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.jinbi, String.valueOf(videolist.getJinbi()))
                .add(Constants.tid, videolist.getId()) //收费ID号
                .build();
        PostModule.postModule(Webrowse.jinbi, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    paymnets3.onFail(mesresult.getMsg());

                    if (mesresult.getStatus().equals("1")) {
                        paymnets3.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void isNetworkAvailable() {
                paymnets3.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets3.onFail();
            }
        });


    }

    /**
     * 加入随机匹配中
     *
     * @param paymnets
     */
    public void DiagnoseRadarView(Paymnets paymnets, int TYPE) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.sex, userInfo.getSex());
        map.put(Constants.TYPE, String.valueOf(TYPE));
        String path = Webrowse.addrmbers + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }

            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }

            }

            @Override
            public void success(String date) {
                if (paymnets != null) {
                    paymnets.onSuccess(date);
                }
            }
        });
    }

    /**
     * 退出匹配
     */
    public void exitclert() {
        PostModule.getModule(Webrowse.rmbersdelete + "?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void success(String date) {

            }

            @Override
            public void fall(int code) {

            }
        });
    }

    /**
     * 查询匹配会员信息
     *
     * @param paymnets
     */
    public void selectaq(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.sex, userInfo.getSex().equals("1") ? "2" : "1");
        String path = Webrowse.getrmbers + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess(mesresult.getData());
                    } else {
                        paymnets.onSuccess(mesresult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });

    }

    /**
     * 快捷回复
     *
     * @param paymnets
     */
    public void dialoglistmsg(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String param = Webrowse.param(Webrowse.message, map);
        PostModule.getModule(param, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {

                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<message> messages = JsonUitl.stringToList(decrypt, message.class);
                        paymnets.onSuccess(messages);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 6房间视频
     *
     * @param totalPage
     * @param paymnets
     */
    public void fragment6v(int totalPage, Paymnets paymnets) {
        PostModule.getModule(BuildConfig.v6 + totalPage, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    getotalPage getotalPage = gson.fromJson(date, getotalPage.class);
                    paymnets.onSuccess(getotalPage);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 检查是否有最新版本的APP更新
     *
     * @param paymnets
     */
    public void version(Paymnets paymnets) {
        PostModule.getModule(Webrowse.version, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        version version = gson.fromJson(mesresult.getData(), version.class);
                        if (mesresult.getCode().equals("404")) {
                            paymnets.ToKen(mesresult.getMsg());
                        } else if (version != null) {
                            paymnets.onSuccess(version);
                        } else {
                            paymnets.onSuccess(mesresult.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

        });
    }

    /**
     * IM聊天限制数
     *
     * @param paymnets
     */
    public void dschat(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.sex, userInfo.getSex());
        String path = Webrowse.dschat + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        chat chat = gson.fromJson(mesresult.getData(), chat.class);
                        paymnets.onSuccess(chat);
                    } else {
                        paymnets.onSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

        });

    }

    /**
     * 金币转换
     *
     * @param paymnets
     */
    public void goldcoinconversion(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.sex, userInfo.getSex());
        String path = Webrowse.goldcoinconversion + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult.getMsg());
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });

    }

    /**
     * 绑定推荐人
     *
     * @param Parent
     * @param paymnets
     */
    public void Gethttp(String Parent, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.Parent, Parent);

        String path = Webrowse.binding_recommendation + "?" + Webrowse.getMap(map, 3);
        Log.d(TAG, "Gethttp: " + path);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.success(mesresult.getData());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }
            }

        });
    }

    /**
     * 任务列表
     */
    public void tasklist(int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(com.tianxin.utils.Constants.USERID, userInfo.getUserId());
        map.put(com.tianxin.utils.Constants.TOKEN, userInfo.getToken());
        map.put(com.tianxin.utils.Constants.Page, "1");
        String path = Webrowse.tasklist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.payonItemClick(mesresult.getData(), TYPE);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

    }

    /**
     * 查询我的金额
     */
    public void reward(int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(com.tianxin.utils.Constants.USERID, userInfo.getUserId());
        map.put(com.tianxin.utils.Constants.TOKEN, userInfo.getToken());
        String path = Webrowse.reward + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.payonItemClick(mesresult.getData(), TYPE);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });

    }

    /**
     * 每日写入签到
     */
    public void qiandaoadd(int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userInfo.getUserId());
        map.put("token", userInfo.getToken());
        String path = Webrowse.qiandaoadd + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                paymnets.payonItemClick(date, TYPE);
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });

    }

    /**
     * 任务是否绑定好友信息
     */
    public void bindlog(int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(com.tianxin.utils.Constants.USERID, userInfo.getUserId());
        map.put(com.tianxin.utils.Constants.TOKEN, userInfo.getToken());
        String path = Webrowse.selecttasklist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                paymnets.payonItemClick(date, TYPE);
            }

        });

    }

    /**
     * 提交编辑视频
     *
     * @param title
     * @param alias
     * @param videolist
     * @param payments
     */
    public void dialogvideotitle(String title, String alias, videolist videolist, Paymnets payments) {
        this.paymnets = payments;
        if (TextUtils.isEmpty(title)) {
            Toashow.show(getString(R.string.tv_msg29));
            payments.onError();
            return;
        }

        RequestBody requestBody = new FormBody.Builder()
                .add("id", videolist.getId())
                .add("userid", userInfo.getUserId())
                .add("token", userInfo.getToken())
                .add("title", title)
                .add("alias", alias)
                .add("msg", alias)
                .add("type", String.valueOf(videolist.getType()))
                .add("fenleijb", String.valueOf(videolist.getFenleijb()))
                .add("jinbi", String.valueOf(videolist.getJinbi()))
                .build();
        PostModule.postModule(Webrowse.videolistedit, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                payments.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        payments.onSuccess();
                    }
                    Toashow.show(mesresult.getMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                payments.onFail();
            }
        });
    }

    /**
     * 获取分类内容
     */
    public void dialogvideotitle(Paymnets paymnets) {
        this.paymnets = paymnets;
        PostModule.getModule(Webrowse.videotitle + "&userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {


                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<videotitle> lcs = stringToList(mesresult.getData(), videotitle.class);
                        paymnets.onSuccess(lcs);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取余额动态
     */
    public void moneylog(int totalPage, int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.TYPE, String.valueOf(TYPE));
        String path = Webrowse.getmoneylog + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {

                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<moneyLog> data = JsonUitl.stringToList(decrypt, moneyLog.class);
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 查询评论
     */
    public void quertrendcomment(int totalPage, String trendid, Paymnets payments) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.trendid, trendid)
                .add(Constants.PAGE, String.valueOf(totalPage))
                .build();
        PostModule.postModule(Webrowse.quertrendcomment, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                payments.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {

                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<trend_comment> trend_comments = stringToList(decrypt, trend_comment.class);
                        payments.onSuccess(trend_comments);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                payments.onFail();
            }
        });
    }

    /**
     * 发评评论
     *
     * @param payments
     */
    public void addtrendcomment(String trendid, String suserid, String title, Paymnets payments) {
        this.paymnets = payments;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.trendid, trendid)
                .add(Constants.suserid, suserid)
                .add(Constants.mcontent, title)
                .add(Constants.TYPE, "0")
                .add(Constants.status, "0")
                .build();
        PostModule.postModule(Webrowse.addtrendcomment, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                payments.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        payments.onSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                payments.onFail();
            }
        });
    }

    /**
     * 删除发评评论
     *
     * @param payments
     */
    public void deletecomment(String id, Paymnets payments) {
        this.paymnets = payments;
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.id, id)
                .build();
        PostModule.postModule(Webrowse.deletecomment, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                payments.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        payments.onSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                payments.onFail();
            }
        });
    }

    /**
     * 请求首页滚动大图数据源
     */
    public void getdsbanner(int TYPE, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(TYPE));
        String path = Webrowse.dsbanner + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                Logi.d(TAG, "请求成功: " + date);
                try {
                    Type type = new TypeToken<BackCallResult<String>>() {
                    }.getType();
                    BackCallResult backCallResult = gson.fromJson(date, type);
                    if (mesresult.isSuccess()) {
                        String strdata = (String) backCallResult.getData();
                        List<banner> banners = JsonUitl.stringToList(strdata, banner.class);
                        if (banners.size() == 0) {
                            //获取广告失败
                            Logi.d(TAG, "广告失败 1111");
                            paymnets.onFail();
                            return;
                        }
                        paymnets.onSuccess(banners);
                    } else if (backCallResult.getCode() == 404) {
                        paymnets.ToKen(backCallResult.getMsg());
                    } else {
                        Logi.d(TAG, "广告失败 2222");
                        paymnets.onFail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "广告失败: " + e.getMessage());
                    paymnets.onFail();
                }

            }


        });
    }

    /**
     * 模糊查询数据
     *
     * @param key
     * @param totalPage
     * @param paymnets
     */
    public void selectvideolist(String key, int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.key, key);
        map.put(Constants.PAGE, String.valueOf(totalPage));
        String path = Webrowse.selectvideolist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        //复杂的泛型：T
                        Type objectType = new TypeToken<BaseClass<videolist>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(mesresult.getData(), objectType);
                        List<videolist> data = baseClass.getData();
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail() {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 搜索推荐
     *
     * @param paymnets
     */
    public void activitselect(Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.sex, userInfo.getSex())
                .build();
        PostModule.postModule(Webrowse.activitselect, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<member> members = JsonUitl.stringToList(mesresult.getData(), member.class);
                        paymnets.onSuccess(members);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 更新资料
     *
     * @param paymnets
     */
    public void personal(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.personal + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        Type objectType = new TypeToken<BaseClass<personal>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(decrypt, objectType);
                        List<personal> date1 = baseClass.getData();
                        personal personal = date1.get(0);
                        paymnets.onSuccess(personal);
                    } else if (mesresult.equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 获取VIP购买
     *
     * @param paymnets
     */
    public void buypaymoney(Paymnets paymnets) {
        PostModule.getModule(Webrowse.buypaymoney, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<buypaymoney> buypaymonies = stringToList(mesresult.getData(), buypaymoney.class);
                        paymnets.onSuccess(buypaymonies);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }

    /**
     * 查询充值记录
     *
     * @param totalPage
     * @param type
     * @param paymnets
     */
    public void getorderidlist(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userInfo.getUserId());
        map.put("page", String.valueOf(totalPage));
        map.put("type", String.valueOf(type));
        map.put("token", userInfo.getToken());
        String path = Webrowse.getorderidlist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<order> data = JsonUitl.stringToList(mesresult.getData(), order.class);
                        if (data.size() == 0) {
                            paymnets.onSuccess(mesresult.getMsg());
                            return;
                        }
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        });
    }

    /**
     * 查询我的消费记录
     *
     * @param totalPage
     * @param type
     * @param paymnets
     */
    public void querdetailedlist(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.TYPE, String.valueOf(type));
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.querdetailedlist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        //复杂的泛型：T
                        Type objectType = new TypeToken<BaseClass<orderdetailed>>() {
                        }.getType();
                        BaseClass baseClass = gson.fromJson(mesresult.getData(), objectType);
                        List<orderdetailed> data = baseClass.getData();
                        if (data.size() == 0) {
                            paymnets.onSuccess(mesresult.getMsg());
                            return;
                        }
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 查询我推荐的人
     *
     * @param totalPage
     * @param paymnets
     */
    public void memberparent(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(totalPage));
        map.put("mobile", userInfo.getPhone());
        map.put("userid", userInfo.getUserId());
        map.put("token", userInfo.getToken());
        String path = Webrowse.memberparent + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<memberparent> data = JsonUitl.stringToList(mesresult.getData(), memberparent.class);
                        paymnets.onSuccess(data);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 提现余额
     *
     * @param paymnets
     */
    public void wmoney(Paymnets paymnets) {
        PostModule.getModule(Webrowse.wmoney, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<wmoney> ts = JsonUitl.stringToList(mesresult.getData(), wmoney.class);
                        paymnets.onSuccess(ts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    /**
     * 查询我的金额
     *
     * @param paymnets
     */
    public void reward(Paymnets paymnets) {
        PostModule.getModule(Webrowse.reward + "?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        reward reward = gson.fromJson(mesresult.getData(), reward.class);
                        paymnets.onSuccess(reward);
                    } else {
                        reward reward = new reward();
                        reward.setMoney("0");
                        reward.setBalance("0");
                        paymnets.onSuccess(reward);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }

            @Override
            public void fall(int code) {

            }
        });
    }

    /**
     * 读取收款人二维码
     *
     * @param paymnets3
     */
    public void getonCreate(Paymnets paymnets3) {
        PostModule.getModule(Webrowse.qrcode + "?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        qrcode qrcode = gson.fromJson(mesresult.getData(), qrcode.class);
                        paymnets3.onSuccess(qrcode);
                    } else {
                        dialog_item_view_yhk.yhkshow(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void isNetworkAvailable() {

            }
        });
    }

    /**
     * 提交申请结算
     *
     * @param m
     * @param paymnets4
     */
    public void addreward(int m, Paymnets paymnets4) {
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserInfo.getInstance().getUserId())
                .add("money", String.valueOf(m))
                .add("msg", getString(R.string.tv_msg59))
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.addreward, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets4.onSuccess();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 提现记录
     *
     * @param totalPage
     * @param paymnets
     */
    public void ordermoeny(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(totalPage));
        map.put("userid", userInfo.getUserId());
        map.put("token", userInfo.getToken());
        String path = Webrowse.ordermoeny + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<order_moeny> orderMoenyList = JsonUitl.stringToList(mesresult.getData(), order_moeny.class);
                        paymnets.onSuccess(orderMoenyList);
                    } else if (mesresult.getCode().equals("404")) {
                        paymnets.ToKen(mesresult.getMsg());
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.onFail();
            }
        });

    }

    /**
     * 我的金币列表
     *
     * @param paymnets
     */
    public void moneylist(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.moneylist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void success(String date) {
                try {
                    List<moneylist> list = stringToList(date, moneylist.class);
                    paymnets.onSuccess(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));

            }
        });
    }

    /**
     * //我的收藏
     *
     * @param totalPage
     * @param paymnets
     */
    public void collection(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.collection + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<shouchangmember> rs = JsonUitl.stringToList(mesresult.getData(), shouchangmember.class);
                        paymnets.onSuccess(rs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 实名认证状态
     *
     * @param paymnets
     */
    public void getusername(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.username, userInfo.getUsername());
        String path = Webrowse.getusername + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = com.tencent.opensource.AES.Resultinfo.decrypt(mesresult.getData());
                        Renzheng renzheng = gson.fromJson(decrypt, Renzheng.class);
                        paymnets.onSuccess(renzheng);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 读取信息主播入驻认证类型
     *
     * @param paymnets
     */
    public void getrmard(boolean booleantype, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.getrmard + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (booleantype) {
                        paymnets.onSuccess(mesresult);
                    } else {
                        paymnets.onSuccess(mesresult.getOk());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }
        });
    }

    /**
     * 查看我的/我查看的
     */
    public void view_user(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.view_user + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
                paymnets.onFail();
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<followlist> toList = JsonUitl.stringToList(mesresult.getData(), followlist.class);
                        paymnets.onSuccess(toList);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 插入或更新查看记录
     */
    public void addlistviewuser(String touserid, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.touserid, touserid);
        String path = Webrowse.addlistviewuser + "?" + Webrowse.getMap(map, 3);

        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (paymnets != null) {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 读取收款人二维码
     *
     * @param paymnets
     */
    public void qrcode(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.qrcode + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        qrcode qrcode1 = gson.fromJson(mesresult.getData(), qrcode.class);
                        paymnets.onSuccess(qrcode1);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });

    }

    /**
     * 保存二维码
     *
     * @param info
     * @param paymnets
     */
    public void postsetQrcode(qrinfo info, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.NAME, info.getName())
                .add(Constants.ACCOUNT, info.getAccount())
                .add(Constants.QRCODE, info.getQrcode())
                .add(Constants.TYPE, "1")
                .add(Constants.Token, userInfo.getToken())
                .build();

        PostModule.postModule(Webrowse.uploads3, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }


    /**
     * 上传头像保存到服务器
     */
    private void uploads(File file, qrinfo info, Paymnets paymnets) {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PATH, Webrowse.uploads2);
        params.put(Constants.USERID, info.getUserid());
        params.put(Constants.NAME, info.getName());
        params.put(Constants.ACCOUNT, info.getAccount());
        params.put(Constants.TYPE, String.valueOf(info.getType()));
        params.put(Constants.Token, userInfo.getToken());

        //提交文件上传
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), fileBody)
                .addFormDataPart("userid", params.get("userid"))
                .addFormDataPart("name", params.get("name"))
                .addFormDataPart("account", params.get("account"))
                .addFormDataPart("type", params.get("type"))
                .build();

        PostModule.okhttpimage(params, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        if (file != null) {
                            file.delete();
                        }
                        paymnets.onSuccess();
                    }
                } catch (Exception e) {
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });


    }

    /**
     * 评论消息
     *
     * @param totalPage
     * @param uid
     * @param paymnets
     */
    public void comment(int totalPage, String uid, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.uid, uid);
        map.put(Constants.PAGE, String.valueOf(totalPage));
        String path = Webrowse.comment + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<comment> comments = JsonUitl.stringToList(mesresult.getData(), comment.class);
                        paymnets.onSuccess(comments);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 评论提交
     *
     * @param paymnets
     */
    public void addcomment(String key, String uid, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.uid, uid)
                .add(Constants.key, key)
                .build();

        PostModule.postModule(Webrowse.addcomment, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));

            }
        });
    }

    /**
     * 删除评论内容
     *
     * @param id
     * @param paymnets
     */
    public void delcomment(String id, String uid, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.id, id);
        map.put(Constants.uid, uid);
        String path = Webrowse.delcomment + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess(mesresult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 查询会员信息回调
     *
     * @param userid
     * @param paymnets
     */
    public void AddMore(String userid, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.touserid, userid);
        String path = Webrowse.AddMore + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        member member = gson.fromJson(mesresult.getData(), member.class);
                        paymnets.onSuccess(member);
                    } else {
                        Toashow.show(mesresult.getMsg());
                        paymnets.onFail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 开播或下播日志
     *
     * @param roomInfo
     * @param type
     */
    public void livecreate(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int type) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, TextUtils.isEmpty(roomInfo.ownerId) ? "" : roomInfo.ownerId)
                .add(Constants.roomName, TextUtils.isEmpty(roomInfo.ownerName) ? "" : roomInfo.ownerName)
                .add(Constants.roomnumber, String.valueOf(roomInfo.roomId))
                .add(Constants.TYPE, String.valueOf(type)) //视频
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(type == 1 ? Webrowse.livecreate : Webrowse.livenextplay, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });

    }

    /**
     * 获取聊天语音
     *
     * @param paymnets
     */
    public void getLiveRoom(int totalPage, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.PAGE, String.valueOf(totalPage))
                .build();
        PostModule.postModule(Webrowse.liveRoom, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<LiveRoom> liveRoom = JsonUitl.stringToList(mesresult.getData(), LiveRoom.class);
                        paymnets.onSuccess(liveRoom);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 查询电影
     *
     * @param totalPage 分页
     * @param fenleijb  分类
     * @param paymnets  监听
     * @param random    随机
     */
    public void dyvideolist(int totalPage, int fenleijb, int status, String all, int random, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.PAGE, String.valueOf(totalPage))
                .add(Constants.TYPE, String.valueOf(fenleijb))
                .add(Constants.status, String.valueOf(status))
                .add(Constants.ALL, all)
                .add(Constants.random, String.valueOf(random))
                .build();
        PostModule.postModule(Webrowse.dyvideolist, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<videolist> liveRoom = JsonUitl.stringToList(decrypt, videolist.class);
                        paymnets.onSuccess(liveRoom);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });

    }

    /**
     * 消费榜
     *
     * @param totalPage
     * @param type
     * @param paymnets
     */
    public void getallchargessss(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", userInfo.getUserId());
        map.put("type", String.valueOf(type));
        map.put("page", String.valueOf(totalPage));
        String path = Webrowse.goldcoinlist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<goldcoin> goldcoins = stringToList(decrypt, goldcoin.class);
                        paymnets.onSuccess(goldcoins);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }
        });
    }

    /**
     * 课程列表
     *
     * @param totalPage
     * @param paymnets
     */
    public void curriculum(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.ALL, "all");
        String path = Webrowse.curriculum + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        //String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<videolist> videolist = JsonUitl.stringToList(mesresult.getData(), videolist.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(videolist);
                        }
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 直敀封面
     *
     * @param totalPage
     * @param paymnets
     */
    public void getLivevimage(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        String path = Webrowse.Livevimage + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<videolist> videolist = JsonUitl.stringToList(decrypt, videolist.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(videolist);
                        }
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 城市列表
     *
     * @param totalPage
     * @param paymnets
     */
    public void getcitydate(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        String path = Webrowse.citydate + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<Citydate> citydateList = JsonUitl.stringToList(decrypt, Citydate.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(citydateList);
                        }
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 城市详情
     *
     * @param totalPage
     * @param paymnets
     */
    public void getcitychat(int totalPage, Citydate citydate, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.key, citydate.getTitle());
        map.put(Constants.cityid, String.valueOf(citydate.getId()));
        String path = Webrowse.citychat + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        List<Citychat> citychat = JsonUitl.stringToList(decrypt, Citychat.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(citychat);
                        }
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取配置
     *
     * @param paymnets
     */
    public void getbasicconfig(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, "1");
        String path = Webrowse.basicconfig + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Basicconfig basicconfig = gson.fromJson(mesresult.getData(), Basicconfig.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(basicconfig, 1);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 获取基本资料
     *
     * @param paymnets
     */
    public void basicconfig(int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.basicconfig + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Basicconfig basicconfig = gson.fromJson(mesresult.getData(), Basicconfig.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(basicconfig);
                        }
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 帐号注销申请
     *
     * @param paymnets
     */
    public void getlogout(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.alias, context.getString(R.string.tv_msg_tm3));
        map.put(Constants.Msg, context.getString(R.string.tv_msg_tm3));
        String path = Webrowse.logout + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 游戏列表
     *
     * @param totalPage
     * @param paymnets
     */
    public void gamelist(int totalPage, Paymnets paymnets, boolean myame) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        String path = (myame ? Webrowse.gamelistAPI : Webrowse.gamelist) + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<Gamelist> gamelists = stringToList(mesresult.getData(), Gamelist.class);
                        for (Gamelist gamelist : gamelists) {
                            member member = gamelist.getMember();
                            if (member == null) {
                                gamelists.remove(gamelist);
                                continue;
                            }
                        }
                        paymnets.onSuccess(gamelists);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }


    /**
     * 游戏列表
     *
     * @param totalPage
     * @param paymnets
     */
    public void gamelist(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.GameActivitylist + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<Gamefinish> gamelists = stringToList(mesresult.getData(), Gamefinish.class);
                        paymnets.onSuccess(gamelists);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }


    /**
     * 检测网络是否正常
     * 服务器是否能打开
     */
    public void member_user(Callback callback) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        Webrowse.getMap(map, 3);
        PostModule.getModule(Webrowse.member_user + "?" + Webrowse.getMap(map, 3), new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                callback.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        member member = gson.fromJson(mesresult.getData(), member.class);
                        activity_sesemys.user_save_update2(member);
                        if (TextUtils.isEmpty(member.getPicture())) {
                            callback.onSuccess(context.getString(R.string.tm92));
                            return;
                        }

                        switch (member.getStatus()) {
                            // case 0:
                            //case 1:
                            //callback.onSuccess(context.getString(R.string.tv_msg18));
                            //return;

                            case 3:
                                //帐户封禁通知
                                callback.Blockedaccount();
                                return;
                        }

                        //判断有否有权发布消息
                        if (member.getAllow() == 1) {
                            callback.onSuccess(context.getString(R.string.tv_msg_tm9));
                            return;
                        }

                        callback.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                callback.onFall();
            }

        });
    }

    /**
     * 删除服务器的数据
     */
    public void deletetrend(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.id, id);
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.deletetrend + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void success(String date) {

            }
        });

    }

    /**
     * 提交游戏下单
     *
     * @param gamelist
     * @param quantity
     * @param msg
     * @param paymnets
     */
    public void game_order(Gamelist gamelist, int quantity, String msg, String datetime, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(id, String.valueOf(gamelist.getId()));
        map.put(Constants.quantity, String.valueOf(quantity));
        map.put(Constants.Msg, msg);
        map.put(Constants.bookdatetime, datetime);
        String path = Webrowse.gameorder + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String id = mesresult.getData();
                        paymnets.success(id);
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 游戏分类
     *
     * @param totalPage
     * @param paymnets
     */
    public void gametitle(int totalPage, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.PAGE, String.valueOf(totalPage));

        String path = Webrowse.gametitle + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<Gametitle> gametitles = stringToList(mesresult.getData(), Gametitle.class);
                        paymnets.onSuccess(gametitles);

                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 游戏技能写入
     *
     * @param gametitle
     * @param moneys
     * @param msg
     * @param paymnets
     */
    public void gameupdate(Gametitle gametitle, String moneys, String msg, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(id, String.valueOf(gametitle.getId()));
        map.put(Constants.Msg, msg);
        map.put(Constants.money, moneys);
        map.put(Constants.NAME, gametitle.getName());
        map.put(Constants.descshow, gametitle.getMsg());
        String path = Webrowse.gameupdate + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {

                        paymnets.onSuccess();
                    } else {
                        paymnets.onSuccess(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 删除游戏技能
     *
     * @param gamelist
     */
    public void gamedelete(Gamelist gamelist, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(id, String.valueOf(gamelist.getId()));
        String path = Webrowse.gamedelete + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess(mesresult.getMsg());
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }
        });
    }

    public void GameActivityfinish(String id, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.id, id);
        String path = Webrowse.gameActivityfinish + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Gamefinish gamefinish = gson.fromJson(mesresult.getData(), Gamefinish.class);
                        paymnets.onSuccess(gamefinish);
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }
        });

    }

    public void gamefinishid(Gamefinish gamefinish, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(id, String.valueOf(gamefinish.getId()));
        String path = Webrowse.gamebunttin + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.success(mesresult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }
        });
    }

    /**
     * 修改签名
     */
    public void myPostModule(String name, int TYPE, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.TYPE, String.valueOf(TYPE))
                .add(Constants.truename, name).build();
        PostModule.postModule(Webrowse.toeditmember, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    ToastUtils.showShort(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        if (paymnets != null) {
                            paymnets.onSuccess(mesresult);
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onSuccess(mesresult.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

        });
    }

    /**
     * 获取昵称
     *
     * @param paymnets
     */
    public void mesresultsss(Paymnets paymnets, int type1) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = (type1 == 1 ? Webrowse.getpesigntext : Webrowse.getpesigntext2) + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess(mesresult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 提交更新数据
     */
    public void myupdatepost(AMapLocation mapLocation, String name, int result, Paymnets paymnets) {
        this.mapLocation = mapLocation;
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.isNetworkAvailable));
            return;
        }
        String province = "";
        String city = "";
        String district = "";
        String address = "";
        String jwd = "";
        if (mapLocation != null) {
            province = mapLocation.getProvince();
            city = mapLocation.getCity();
            district = mapLocation.getDistrict();
            address = mapLocation.getAddress();
            jwd = String.format("%s,%s", mapLocation.getLongitude(), mapLocation.getLatitude());
        }

        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.TYPE, String.valueOf(result))
                .add(Constants.Token, userInfo.getToken())
                .add("province", province)
                .add("city", city)
                .add("district", district)
                .add("address", address)
                .add("jwd", jwd)
                .add("truename", name).build();
        PostModule.postModule(Webrowse.toeditmember, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    ToastUtil.toastShortMessage(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.toastShortMessage("Eorr保存失败");
                }
            }

            @Override
            public void fall(int code) {
                ToastUtil.toastShortMessage("Eorr提交失败");
            }
        });
    }


    /**
     * 每天允许聊天最大条数
     */
    public void chatlimit(Paymnets paymnets, int type) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.chatlimit + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 获取在线用户
     *
     * @param paymnets
     */
    public void getlistmember(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.getlistmember + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        member member = gson.fromJson(mesresult.getData(), member.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(member);
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail(mesresult.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 微信授权登录注册
     *
     * @param paymnets
     */
    public void openwexin(String openId, String username, String nickname, String headimgurl, String code, int sex, AMapLocation mapLocation, Paymnets paymnets) {
        String province = "";
        String city = "";
        String district = "";
        String address = "";
        String jwd = "";
        String Model = SystemUtil.showlog(DemoApplication.instance());
        if (mapLocation != null) {
            province = mapLocation.getProvince();
            city = mapLocation.getCity();
            district = mapLocation.getDistrict();
            address = mapLocation.getAddress();
            jwd = String.format("%s,%s", mapLocation.getLongitude(), mapLocation.getLatitude());
        }
        Map<String, String> map = new HashMap<>();
        map.put("openid", openId);
        map.put("username", username);
        map.put("nickname", nickname);
        map.put("headimgurl", headimgurl);
        map.put("sex", String.valueOf(sex));
        map.put("province", province);
        map.put("city", city);
        map.put("district", district);
        map.put("address", address);
        map.put("jwd", jwd);
        map.put("code", code);
        map.put("viecode", String.valueOf(Config.getVersionCode()));
        map.put("parent", activity_reg.getinviteuid());
        map.put("model", TextUtils.isEmpty(Model) ? "" : Model);
        String path = Webrowse.openwexin + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Mesresult mesresult1 = gson.fromJson(mesresult.getData(), Mesresult.class);
                        paymnets.onSuccess(mesresult1);
                    } else {
                        Toashow.show(mesresult.getMsg());
                        paymnets.onFail();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail();
                    Toashow.show(e.getMessage());

                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
                paymnets.onFail();
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
                paymnets.onFail();
            }
        });
    }

    /**
     * 微信授权登录
     *
     * @param openId
     * @param accesstoken
     * @param login
     * @param paymnets
     */
    public void openwxinlogin(String openId, String accesstoken, int login, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put("openid", openId);
        map.put("accesstoken", accesstoken);
        map.put("model", SystemUtil.showlog(DemoApplication.instance()));
        map.put("login", String.valueOf(login));
        map.put(Constants.viecode, String.valueOf(Config.getVersionCode()));
        String path = Webrowse.openwxinlogin + "?" + Webrowse.getMap(map, 3);

        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {

                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        try {
                            Mesresult mesresult1 = gson.fromJson(mesresult.getData(), Mesresult.class);
                            paymnets.onSuccess(mesresult1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toashow.show(e.getMessage());
                        }
                    } else {
                        switch (mesresult.getStatus()) {
                            case "-999":
                                //注销申请存在
                                paymnets.onRefresh();
                                break;
                            case "-3":
                                //帐户已被封号
                                paymnets.onError();
                                break;
                            case "-5":
                                //进入注册
                                paymnets.onFail();
                                break;
                            default:
                                //其他状态
                                paymnets.onFail(mesresult.getMsg());
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }


            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 获取分享数据
     *
     * @param paymnets
     */
    public void Sharewchat(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.Sharewchat + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Share share = gson.fromJson(mesresult.getData(), Share.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(share);
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail(mesresult.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 随机匹配会员
     *
     * @param paymnets
     */
    public void Randmember_User(AMapLocation aMapLocation, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        if (aMapLocation != null) {
            map.put(Constants.province, TextUtils.isEmpty(aMapLocation.getProvince()) ? "" : aMapLocation.getProvince());
            map.put(Constants.city, TextUtils.isEmpty(aMapLocation.getCity()) ? "" : aMapLocation.getCity());
            map.put(Constants.district, TextUtils.isEmpty(aMapLocation.getDistrict()) ? "" : aMapLocation.getDistrict());
            map.put(Constants.address, TextUtils.isEmpty(aMapLocation.getAddress()) ? "" : aMapLocation.getAddress());
        }
        String path = Webrowse.Randmember_User + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        if (!TextUtils.isEmpty(decrypt)) {
                            member member = gson.fromJson(decrypt, member.class);
                            if (paymnets != null) {
                                paymnets.onSuccess(member);
                            }
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                            Toashow.show(mesresult.getMsg());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                    Toashow.show(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });

    }

    /**
     * 随机获取背景音乐
     *
     * @param paymnets
     */
    public void playmusic(Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .build();
        PostModule.postModule(Webrowse.playmusic, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        Music music = gson.fromJson(mesresult1.getData(), Music.class);
                        paymnets.onSuccess(music);
                    } else {
                        paymnets.onFail(getString(R.string.eorrfali));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail(e.getMessage());

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail(getString(R.string.isNetworkAvailable));
            }

            @Override
            public void onFail() {
                paymnets.onFail(getString(R.string.isNetworkAvailable));
            }
        });


    }

    /**
     * 自动登录IM
     *
     * @param paymnets
     */
    public void welcomeLogin(Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.login, "0")
                .add(Constants.viecode, String.valueOf(Config.getVersionCode()))
                .add(Constants.model, TextUtils.isEmpty(SystemUtil.showlog(context)) ? "" : SystemUtil.showlog(context))
                .build();
        PostModule.postModule(Webrowse.WelcomeLogin, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        Mesresult mesresult2 = gson.fromJson(mesresult1.getData(), Mesresult.class);
                        paymnets.onSuccess(mesresult2);
                    } else {
                        paymnets.onFail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail(e.getMessage());

                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail(getString(R.string.isNetworkAvailable));
            }

            @Override
            public void onFail() {
                paymnets.onFail(getString(R.string.isNetworkAvailable));
            }
        });

    }

    /**
     * 获取微信支付接口内容
     */
    public void Wxapppay(int type, moneylist money, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(type));
        map.put(Constants.money, money.getMoney());
        map.put(id, String.valueOf(money.getId()));
        map.put(Constants.VIP, String.valueOf(money.getVip()));
        String path = Webrowse.param(Webrowse.wxapppay, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        WXPlay wxPlay = gson.fromJson(mesresult.getData(), WXPlay.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(wxPlay);
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                        }
                        Toashow.show(mesresult.getMsg());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show("微信支付错误,请检查配置是否正确");
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                    Toashow.show(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }
        });
    }

    /**
     * 请求随机消息发送给
     */
    public void activemessage(Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .build();
        PostModule.postModule(Webrowse.activemessage, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }

            }

            @Override
            public void success(String date) {
                if (paymnets != null) {
                    paymnets.onSuccess();
                }

            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.isNetworkAvailable));
                }

            }

            @Override
            public void onFail() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.isNetworkAvailable));
                }

            }
        });
    }

    /**
     * 戳一下对方消息回调给我
     *
     * @param paymnets
     */
    public void getNotmessage(String touserid, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.touserid, touserid)
                .add(Constants.message, touserid)
                .build();
        PostModule.postModule(Webrowse.getNotmessage, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali2));
                }

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (paymnets != null) {
                        paymnets.onFail(e.getMessage());
                    }
                }
            }


            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void onFail() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }
        });

    }

    /**
     * 请求扣除金币
     *
     * @param touserid
     */
    public void chatjinbi(String touserid, String name, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.NAME, name)
                .add(Constants.touserid, TextUtils.isEmpty(touserid) ? "" : touserid)
                .build();
        PostModule.postModule(Webrowse.chatjinbi, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali2));
                }

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        userInfo.setJinbi(Double.valueOf(mesresult.getData()));
                        if (paymnets != null) {
                            paymnets.onSuccess(mesresult.getMsg());
                        }

                    } else {
                        if (paymnets != null) {
                            paymnets.onFail(mesresult.getMsg());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (paymnets != null) {
                        paymnets.onFail(e.getMessage());
                    }
                }
            }


            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void onFail() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }
        });

    }

    /**
     * 请求扣除金币
     *
     * @param touserid
     */
    public void chatjinbi(String touserid, String name, int type, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.NAME, name)
                .add(Constants.TYPE, String.valueOf(type))
                .add(Constants.touserid, TextUtils.isEmpty(touserid) ? "" : touserid)
                .build();
        PostModule.postModule(Webrowse.chatjinbi, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali2));
                }

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        userInfo.setJinbi(Double.valueOf(mesresult.getData()));
                        if (paymnets != null) {
                            paymnets.onSuccess(mesresult.getMsg());
                        }

                    } else {
                        if (paymnets != null) {
                            paymnets.onFail(mesresult.getMsg());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (paymnets != null) {
                        paymnets.onFail(e.getMessage());
                    }
                }
            }


            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void onFail() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }
        });

    }

    /**
     * 合作提交
     */
    public void cooperationmode(int type, String message, String phone, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.TYPE, String.valueOf(type))
                .add(Constants.message, message)
                .add(Constants.phone, phone)
                .build();
        PostModule.postModule(Webrowse.cooperationmode, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali2));
                }

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess(mesresult.getMsg());
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (paymnets != null) {
                        paymnets.onFail(e.getMessage());
                    }
                }
            }


            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }

            @Override
            public void onFail() {
                if (paymnets != null) {
                    paymnets.onFail(getString(R.string.eorrfali3));
                }

            }
        });

    }

    /**
     * 用户申请主播入驻类型
     *
     * @param uid
     * @param msg
     * @param paymnets
     */
    public void addrmard(int uid, String msg, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add("uid", String.valueOf(uid))
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add("guild", msg)
                .build();
        PostModule.postModule(Webrowse.addrmard, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    ToastUtil.toastShortMessage(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 初始化SDK_APPID
     * 保存IMSDKAPPID
     *
     * @param configs
     */
    public void SDKAPPID(Context context, TUIKitConfigs configs) {
        PostModule.getModule(Webrowse.APISDKAPPID, new Paymnets() {
            @Override
            public void fall(int code) {
                SDKAPPID(context, SDKAPPID, configs);
            }

            @Override
            public void isNetworkAvailable() {
                SDKAPPID(context, SDKAPPID, configs);
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        int mySDKAPPID = Integer.parseInt(mesresult.getData());
                        SDKAPPID(context, mySDKAPPID, configs);
                    } else {
                        SDKAPPID(context, SDKAPPID, configs);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SDKAPPID(context, SDKAPPID, configs);
                }

            }
        });
    }

    /**
     * 初始化
     *
     * @param context
     * @param SDKAPPID
     * @param configs
     */
    private static void SDKAPPID(Context context, int SDKAPPID, TUIKitConfigs configs) {
        TUIKit.init(context, SDKAPPID, configs);
        //打开/关闭群直播 true：开启；false：关闭
        TUIKit.getConfigs().setEnableGroupLiveEntry(false);
        UserInfo.getInstance(context).setSDKAPPID(SDKAPPID);
    }

    /**
     * 动态视频
     *
     * @param paymnets
     */
    public void playvideotrend(int totalPage, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.PAGE, String.valueOf(totalPage))
                .build();
        PostModule.postModule(Webrowse.playvideotrend, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        List<trend> trends = stringToList(mesresult1.getData(), trend.class);
                        List<videolist> mlist = new ArrayList<>();
                        for (trend mytrend : trends) {
                            videolist video = new videolist();
                            video.setUserid(String.valueOf(mytrend.getUserid()));
                            video.setPicuser(mytrend.getImage());
                            video.setPicurl(mytrend.getImage());
                            video.setBigpicurl(mytrend.getImage());
                            video.setPlaytest(mytrend.getVideotest());
                            video.setPlayurl(mytrend.getVideo());
                            video.setTitle(mytrend.getTitle());
                            video.setFnum(String.valueOf(mytrend.getLove()));
                            video.setPnum("0");
                            video.setFollow(0);
                            video.setTencent(mytrend.getTencent());
                            mlist.add(video);
                        }
                        paymnets.onSuccess(mlist);
                    } else {
                        paymnets.onFail(mesresult1.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 自动随机给自己拨打视频
     */
    public void tovideocall(Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .build();
        PostModule.postModule(Webrowse.tovideocall, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        videolist videolist = JSON.parseObject(mesresult1.getData(), videolist.class);
                        if (videolist.getStatus() == 1) {
                            context.stopService(new Intent(context, videoService.class));
                        } else {
                            paymnets.onSuccess(mesresult1.getData());
                        }
                    } else {
                        paymnets.onFail(mesresult1.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }

    /**
     * 发送点赞
     *
     * @param paymnets
     */
    public void givelist(int tid, int touserid, int type, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, UserInfo.getInstance().getUserId())
                .add(Constants.Token, UserInfo.getInstance().getToken())
                .add(Constants.touserid, String.valueOf(touserid))
                .add(Constants.tid, String.valueOf(tid))
                .add(Constants.TYPE, String.valueOf(type))
                .build();
        PostModule.postModule(Webrowse.givelist, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        givelist givelist = gson.fromJson(mesresult1.getData(), givelist.class);
                        paymnets.onSuccess(givelist);
                    } else {
                        paymnets.onFail(mesresult1.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


    /**
     * 获取微信开放平台openweixi()
     *
     * @param paymnets
     */
    public void Openweixi(Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, TextUtils.isEmpty(UserInfo.getInstance().getUserId()) ? "" : UserInfo.getInstance().getUserId())
                .add(Constants.Token, TextUtils.isEmpty(UserInfo.getInstance().getToken()) ? "" : UserInfo.getInstance().getToken())
                .add(Constants.TYPE, "8")
                .build();
        PostModule.postModule(Webrowse.openweixi, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        tpconfig tpconfig = gson.fromJson(mesresult1.getData(), tpconfig.class);
                        paymnets.onSuccess(tpconfig);
                    } else {
                        paymnets.onFail(mesresult1.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    /**
     * 上传用户定位位置
     *
     * @param mapLocation
     */
    public void toAMapLocation(AMapLocation mapLocation, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        if (mapLocation != null) {
            map.put(Constants.province, TextUtils.isEmpty(mapLocation.getProvince()) ? "" : mapLocation.getProvince());
            map.put(Constants.city, TextUtils.isEmpty(mapLocation.getCity()) ? "" : mapLocation.getCity());
            map.put(Constants.district, TextUtils.isEmpty(mapLocation.getDistrict()) ? "" : mapLocation.getDistrict());
            map.put(Constants.address, TextUtils.isEmpty(mapLocation.getAddress()) ? "" : mapLocation.getAddress());
            map.put(Constants.jwd, String.format("%s,%s", mapLocation.getLongitude(), mapLocation.getLatitude()));
        }
        String path = Webrowse.toAMapLocation + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    /**
     * 收费设置列表
     *
     * @param paymnets
     */
    public void chatmoney(Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        String path = Webrowse.chatmoney + "?" + Webrowse.getMap(map, 3);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        if (paymnets != null) {
                            List<chatmoney> chatmonies = stringToList(mesresult1.getData(), chatmoney.class);
                            paymnets.onSuccess(chatmonies);
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    /**
     * 收费设置
     *
     * @param paymnets
     */
    public void chatsetpost(int type, String money, Paymnets paymnets) {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        map.put(Constants.TYPE, String.valueOf(type));
        map.put(Constants.money, money);
        String path = Webrowse.param(Webrowse.chatsetpost, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }


    /**
     * 请求免费短视频资源
     *
     * @param totalpage
     * @param paymnets
     */
    public void freevideo(int totalpage, int type, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.PAGE, String.valueOf(totalpage));
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.param(Webrowse.freevideo, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }

            @Override
            public void success(String date) {

                try {
                    Mesresult mesresult1 = gson.fromJson(date, Mesresult.class);
                    if (mesresult1.isSuccess()) {
                        if (paymnets != null) {
                            String decrypt = Resultinfo.decrypt(mesresult1.getData());
                            List<videolist> ts = stringToList(decrypt, videolist.class);
                            paymnets.onSuccess(ts);
                        }
                    } else {
                        paymnets.onFail(mesresult1.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show("请求数据失败");
                }


            }
        });
    }


    /**
     * 返回提交用户user token
     *
     * @return
     */
    private Map<String, String> getinfo() {
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, userInfo.getUserId());
        map.put(Constants.Token, userInfo.getToken());
        return map;
    }

    /**
     * 播放完成记录
     *
     * @param videolist
     */
    public void senvideo(videolist videolist, int type) {
        Map<String, String> map = getinfo();
        map.put(id, String.valueOf(videolist.getId()));
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.param(Webrowse.senvideo, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void success(String date) {
                Log.d(TAG, "success: " + date);
            }
        });

    }


    /**
     * 隐私设置
     *
     * @param type
     * @param b
     */
    public void editvideoPush(int type, boolean b) {
        Map<String, String> map = getinfo();
        map.put(Constants.TYPE, String.valueOf(type));
        map.put(Constants.status, b ? "1" : "0");
        String path = Webrowse.param(Webrowse.videopushedit, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void success(String date) {
                Log.d(TAG, "success: " + date);
            }
        });
    }

    /**
     * 隐私设置
     */
    public void videoPush(Paymnets paymnets) {
        Map<String, String> map = getinfo();
        String path = Webrowse.param(Webrowse.videopush, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        videoPush videoPush = gson.fromJson(mesresult.getData(), videoPush.class);
                        paymnets.onSuccess(videoPush);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(e.getMessage());
                }

            }
        });
    }

    /**
     * 视频置顶
     *
     * @param videolist
     * @param code
     */
    public void senvideotop(videolist videolist, int code) {
        Map<String, String> map = getinfo();
        map.put(id, String.valueOf(videolist.getId()));
        map.put(Constants.status, String.valueOf(code));
        String path = Webrowse.param(Webrowse.senvideotop, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void fall(int code) {

            }

            @Override
            public void success(String date) {
                Log.d(TAG, "success: " + date);

            }
        });
    }

    /**
     * 活动聚会
     *
     * @param totalPage
     * @param paymnets
     */
    public void partylist(int totalPage, int type, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.param(Webrowse.partylist, map);
        Log.d(TAG, "partylist: " + path);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<Party> parties = stringToList(mesresult.getData(), Party.class);
                        paymnets.onSuccess(parties);
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 写入聚会活动
     *
     * @param party
     * @param paymnets
     */
    public void addpartyl(Party party, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put("title", party.getTitle());
        map.put("tdesc", party.getTdesc());
        map.put("partytime", party.getPartytime());
        map.put("address", party.getAddress());
        map.put("partynumbe", String.valueOf(party.getPartynumbe()));
        map.put("partyadvanced", party.getPartyadvanced());
        map.put("msg", party.getMsg());
        map.put("code", String.valueOf(party.getCode()));

        String path = Webrowse.param(Webrowse.addpartyl, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess(mesresult.getData());
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 获聚会内容
     *
     * @param id
     * @param paymnets
     */
    public void partyfind(String id, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.id, id);
        String path = Webrowse.param(Webrowse.partyfind, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        Party party = gson.fromJson(mesresult.getData(), Party.class);
                        paymnets.onSuccess(party);
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 聚会报名
     */
    public void partyname(partyname p, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.tid, String.valueOf(p.getTid()));
        map.put(Constants.remarks, String.valueOf(p.getRemarks()));
        map.put(Constants.Msg, p.getMsg());
        String path = Webrowse.param(Webrowse.partyname, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 关闭活动
     *
     * @param id
     * @param paymnets
     */
    public void patrfinish(String id, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.id, id);
        String path = Webrowse.param(Webrowse.patrfinish, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 列表
     *
     * @param id
     * @param paymnets
     */
    public void partynameUser(int totalPage, String id, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.PAGE, String.valueOf(totalPage));
        map.put(Constants.tid, id);
        String path = Webrowse.param(Webrowse.partynameUser, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<partyname> partynames = stringToList(mesresult.getData(), partyname.class);
                        paymnets.onSuccess(partynames);
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /**
     * 删除数据
     *
     * @param id
     * @param paymnets
     */
    public void delpartyl(String id, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.id, id);
        String path = Webrowse.param(Webrowse.delpartyl, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * @param totalPage
     * @param paymnets
     */
    public void partynamelist(int totalPage, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.PAGE, String.valueOf(totalPage));
        String path = Webrowse.param(Webrowse.partynamelist, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<partyname2> partyname2s = stringToList(mesresult.getData(), partyname2.class);
                        paymnets.onSuccess(partyname2s);
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 搜索首页用户
     *
     * @param key
     * @param paymnets
     */
    public void selaercch(String key, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.Key, key);
        String path = Webrowse.param(Webrowse.userselect, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        if (!TextUtils.isEmpty(decrypt)) {
                            List<member> members = stringToList(decrypt, member.class);
                            paymnets.onSuccess(members, 0);
                        }
                    } else {
                        Toashow.show(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    /**
     * 分享海报
     *
     * @param paymnets
     */
    public void share(int type, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.TYPE, String.valueOf(type));
        String path = Webrowse.param(Webrowse.share, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        List<ShareDate> shareDates = stringToList(mesresult.getData(), ShareDate.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(shareDates);
                        }
                    } else {
                        Toashow.show(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获取比例调整
     *
     * @param paymnets
     */
    public void getamountconfig(String touserid, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.touserid, touserid);
        String path = Webrowse.param(Webrowse.getamountconfig, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }

            @Override
            public void success(String date) {
                try {

                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        amountconfig amountconfig = gson.fromJson(mesresult.getData(), amountconfig.class);
                        if (paymnets != null) {
                            paymnets.onSuccess(amountconfig);
                        }
                    } else {
                        Toashow.show(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 获取比例调整
     *
     * @param paymnets
     */
    public void editamountconfig(String touserid, int money, Paymnets paymnets) {
        Map<String, String> map = getinfo();
        map.put(Constants.touserid, touserid);
        map.put(Constants.agent, String.valueOf(money));
        String path = Webrowse.param(Webrowse.editamountconfig, map);
        PostModule.getModule(path, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }
            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.isSuccess()) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 编辑资料
     *
     * @param name
     * @param wx
     * @param qq
     */
    public void activityedits(String name, String wx, String qq, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .add(Constants.truename, name)
                .add(Constants.wxpay, wx)
                .add(Constants.qq, qq)
                .build();
        PostModule.postModule(Webrowse.editmember, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                paymnets.isNetworkAvailable();
            }

            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        userInfo.setName(name);
                        paymnets.success(mesresult.getMsg());
                        //交给腾讯IM修改个人资料
                        activity_sesemys.updateProfile(userInfo);
                    } else {
                        paymnets.onFail(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                try {
                    paymnets.onFail(getString(R.string.tv_msg264));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取基本资料
     *
     * @param paymnets
     */
    public void getmember(Paymnets paymnets) {
        PostModule.getModule(Webrowse.getmember + "?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    reguserinfo reguserinfo = gson.fromJson(date, reguserinfo.class);
                    if (!TextUtils.isEmpty(reguserinfo.getTruename())) {
                        userInfo.setName(reguserinfo.getTruename());
                    }
                    userInfo.setQq(reguserinfo.getQq());
                    userInfo.setWx(reguserinfo.getWx());
                    userInfo.setSex(String.valueOf(reguserinfo.getSex()));
                    paymnets.onSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {
                paymnets.onFail();

            }
        });
    }

    /**
     * 客户获取支付列表
     *
     * @param paymnets
     */
    public void playlist(int money, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.price, String.valueOf(money))
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.playmoneylist, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (paymnets != null) {
                    paymnets.isNetworkAvailable();
                }

            }

            @Override
            public void success(String date) {
                Logi.d(TAG, "响应请求-->>支付列表: " + date);
                try {
                    Type type = new TypeToken<BackCallResult<List<byprice>>>() {
                    }.getType();
                    BackCallResult backCallResult = gson.fromJson(date, type);
                    if (backCallResult.getCode() == 0) {
                        List<byprice> byprice = (List<com.tencent.opensource.model.byprice>) backCallResult.getData();
                        if (paymnets != null) {
                            paymnets.onSuccess(byprice);
                        }
                    } else {
                        Toashow.toastMessage(backCallResult.getMsg());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                if (paymnets != null) {
                    paymnets.onFail();
                }

            }
        });
    }

    /**
     * 客户支付金额
     */
    public void playmoney(int id, int amount, int channelid, int type, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.amount, String.valueOf(amount))
                .add(Constants.channelid, String.valueOf(channelid))
                .add(Constants.TYPE, String.valueOf(type))
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.id, String.valueOf(id))
                .build();
        PostModule.postModule(Webrowse.playlist, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (callback != null) {
                    callback.isNetworkAvailable();
                }

            }

            @Override
            public void fall(int code) {
                try {
                    paymnets.onFail(getString(R.string.tv_msg264));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void success(String date) {
                try {
                    Type type = new TypeToken<BackCallResult<mpayurl>>() {
                    }.getType();
                    BackCallResult backCallResult = gson.fromJson(date, type);
                    if (backCallResult.getCode() == 100000) {
                        if (paymnets != null) {
                            paymnets.onSuccess(backCallResult.getData());
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                        }
                        Toashow.toastMessage(backCallResult.getMsg());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 客户支付查询
     */
    public void querselect(String orderid, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.Orderid, orderid)
                .add(Constants.USERID, userInfo.getUserId())
                .build();
        PostModule.postModule(Webrowse.querselect, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (callback != null) {
                    callback.isNetworkAvailable();
                }

            }

            @Override
            public void fall(int code) {
                try {
                    paymnets.onFail(getString(R.string.tv_msg264));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void success(String date) {
                try {
                    Type type = new TypeToken<BackCallResult<Object>>() {
                    }.getType();
                    BackCallResult backCallResult = gson.fromJson(date, type);
                    Toashow.toastMessage(backCallResult.getMsg());
                    if (backCallResult.getStatus() == 1) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    } else {
                        if (paymnets != null) {
                            paymnets.onFail();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 绑定手机号
     *
     * @param mobile
     * @param code
     * @param paymnets
     */
    public void BindPhone(String mobile, String code, Paymnets paymnets) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.mobile, mobile)
                .add(Constants.code, code)
                .add(Constants.USERID, userInfo.getUserId())
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.bindphone, requestBody, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                if (callback != null) {
                    callback.isNetworkAvailable();
                }

            }

            @Override
            public void fall(int code) {
                try {
                    paymnets.onFail(getString(R.string.tv_msg264));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void success(String date) {
                Logi.d(TAG, "success: " + date);
                try {
                    Type type = new TypeToken<BackCallResult<Object>>() {
                    }.getType();
                    BackCallResult backCallResult = gson.fromJson(date, type);
                    Toashow.toastMessage(backCallResult.getMsg());
                    if (backCallResult.getStatus() == 1) {
                        if (paymnets != null) {
                            paymnets.onSuccess();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }


}