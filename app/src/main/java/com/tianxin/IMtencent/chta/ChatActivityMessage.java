package com.tianxin.IMtencent.chta;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Config_User;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.present;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.party.activity_party_list;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.picenter.activity_picture;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.adapter.Radapter;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_item_gift;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.MySocket;
import com.tianxin.widget.Backtitle;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.Socket.body;
import com.tencent.opensource.model.Socket.data;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import butterknife.OnClick;

/**
 * 会活聊天群 聊天SOKET
 */
public class ChatActivityMessage extends BasActivity2 {
    private static final String TAG = ChatActivityMessage.class.getSimpleName();
    private EditText editsend;
    private TextView btnsend;
    private RecyclerView recyclerview;
    private Backtitle backtitle;
    private SVGAImageView svgaImageView;
    private myHandler myHandler = new myHandler();
    private Handler handlerZ;

    private SVGAParser parser;                                       //动画状态
    private LinkedList<String> mAnimationUrlList;                    //动画链接
    private boolean mIsPlaying;                                      //是否正在播放
    private cs_alipay csAlipay;                                      //支付宝pay

    public static void setAction(Context context) {

        context.startActivity(new Intent(context, ChatActivityMessage.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_cath;
    }

    @Override
    public void iniview() {
        editsend = findViewById(R.id.editsend);
        btnsend = findViewById(R.id.btnsend);
        backtitle = findViewById(R.id.backtitle);
        svgaImageView = findViewById(R.id.svgaImage);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(radapter = new Radapter(context, list, Radapter.party_item03, callback));
        editsend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnsend.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        backtitle.setconter(String.format("%s(%s)", getString(R.string.Tcaht), 0));
        backtitle.setBackright(getString(R.string.pages5));
        backtitle.backright.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void initData() {
        csAlipay = new cs_alipay(context, paymnets);
        parser = new SVGAParser(context);
        mAnimationUrlList = new LinkedList<>();

        MySocket.getMySocket(paymnets);
        new Thread(new myRunnable1()).start();
        editsend.postDelayed(new Runnable() {
            @Override
            public void run() {
                //进入聊天窗口系统提示消息
                Message message = new Message();
                message.what = 1;
                message.obj = context.getString(R.string.Tcahtsment);
                handlerZ.sendMessage(message);

            }
        }, 500);
    }

    @Override
    @OnClick({R.id.btnsend, R.id.backright, R.id.gift, R.id.chat})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.btnsend: {
                String msg = editsend.getText().toString();
                editsend.setText(null);
                if (UserInfo.getInstance().getState() == 3) {
                    dialog_Blocked.myshow(context);
                    return;
                }
                if (!TextUtils.isEmpty(msg)) {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = msg;
                    handlerZ.sendMessage(message);
                }
                break;
            }
            case R.id.backright: {
                activity_party_list.setAction(context);
                break;
            }
            case R.id.gift: {
                starinidate();
                break;
            }
            case R.id.chat: {
                Toashow.show(getString(R.string.chatkk));
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void OnEorr() {

    }

    private Paymnets paymnets = new Paymnets() {

        @Override
        public void onSuccess(String msg) {
            Message message = new Message();
            message.obj = msg;
            myHandler.sendMessage(message);
        }

        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });
        }

        @Override
        public void activity(String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);
                }
            });
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            switch (TYPE) {
                case 1:
                    //发起支付宝请求
                    csAlipay.Paymoney(moneylist);
                    break;
                case 2:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }
        }

    };

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            data data = (data) list.get(position);
            switch (data.getCode()) {
                case 1000: {

                    break;
                }
                case 2000: {
                    break;
                }
                case 3000: {
                    break;
                }
                case 4000: {
                    if (!TextUtils.isEmpty(data.getVideo())) {
                        videoijkplayer0.setAction(context, getString(R.string.video_saa), data.getVideo(), data.getImges());
                    } else {
                        activity_picture.setAction(activity, 0, data.getName(), data.getImges());
                    }
                    break;
                }
            }

        }

        @Override
        public void LongClickListener(int position) {
            data data = (data) list.get(position);
        }

        @Override
        public void OndeleteListener(int position) {
            //转到聊天窗口
            data data = (data) list.get(position);
            activity_picenter.setActionactivity(context, String.valueOf(data.getUserid()));

        }
    };

    private class myRunnable1 implements Runnable {
        @Override
        public void run() {
            //1.创建Looper对象
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            //2.实例化Handler
            handlerZ = new handlerZ();
            //3.循环读取MessageQueue
            Looper.loop();
        }

    }

    /**
     * 刷新UI主线程
     */
    class myHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String message = msg.obj.toString();
            try {
                Type listType = new TypeToken<body<data>>() {
                }.getType();
                body<data> body = gson.fromJson(message, listType);
                data data = body.getData();
                data.setCode(body.getCode());
                backtitle.setconter(String.format("%s(%s)", getString(R.string.Tcaht), data.getOnline()));
                switch (body.getCode()) {
                    case 1000: {
                        try {
                            if (data.isUpdate()) {
                                if (userInfo.getAllow() != data.getAllow()) {
                                    userInfo.setAllow(data.getAllow());
                                }
                                if (userInfo.getReale() != data.getReale()) {
                                    userInfo.setReale(data.getReale());
                                }
                                if (userInfo.gettRole() != data.gettRole()) {
                                    userInfo.settRole(data.gettRole());
                                }
                                if (userInfo.getState() != data.getStatus()) {
                                    userInfo.setState(data.getStatus());
                                }
                                if (userInfo.getLevel() != data.getLevel()) {
                                    userInfo.setLevel(data.getLevel());
                                }
                                if (userInfo.getVip() != data.getVip()) {
                                    userInfo.setVip(data.getVip());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 2000: {
                        //保持通信消息转发所有人
                        if (userInfo.getUserId().equals(String.valueOf(data.getUserid()))) {
                            data.setType(2);
                            //右边显示
                        } else {
                            //左边显示
                            data.setType(1);
                        }
                        mynotifyDataSetChanged(data);
                        break;
                    }
                    case 3000: {
                        //群消息提醒-->保持通信消息转发所有人
                        data.setType(3);
                        mynotifyDataSetChanged(data);
                        break;
                    }
                    case 4000: {
                        //保持通信消息转发所有人
                        if (userInfo.getUserId().equals(String.valueOf(data.getUserid()))) {
                            data.setType(5);
                            //右边显示
                        } else {
                            //左边显示
                            data.setType(4);
                        }
                        mynotifyDataSetChanged(data);
                        break;
                    }
                    case 5000: {
                        //保持通信消息转发所有人
                        if (userInfo.getUserId().equals(String.valueOf(data.getUserid()))) {
                            data.setType(7);
                            //我的右边显示
                        } else {
                            //所有人的左边显示
                            data.setType(6);
                        }
                        mynotifyDataSetChanged(data);
                        //开始播放动画
                        GiftInfo giftInfo = gson.fromJson(data.getMessage(), GiftInfo.class);
                        playgift(giftInfo);
                        break;
                    }
                    default: {
                        data.setType(3);
                        mynotifyDataSetChanged(data);
                        break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传递给子线程
     */
    class handlerZ extends Handler {
        public void handleMessage(Message msg) {
            MySocket mySocket = MySocket.getMySocket(paymnets);
            switch (msg.what) {
                case 1: {
                    //打招呼
                    mySocket.sendselrver();
                    break;
                }
                case 2: {
                    //闲谈聊天
                    mySocket.sendselrver(msg.obj.toString());
                    break;
                }
                case 3: {
                    //发图片OR视频
                    String img = "https://oss.51dengta.net/upload/images/geren_touxiang/0fad67227cc5c14f360306cb556b8ab1.png";
                    String video = "http://vod.51dengta.net/24c975fbvodcq1302331714/445052423701925921026685614/ipFJLPkks1kA.mp4";
                    mySocket.sendselrver(img, video, 4000);
                    break;
                }
                case 4: {
                    //发送礼物
                    mySocket.sendselrver(msg.obj.toString(), 5000);
                    break;
                }
            }
        }
    }

    /**
     * 礼物赠送
     */
    private void starinidate() {
        Config_User user = Config_User.getInstance();
        present present = new present();
        present.setTYPE(1); //chat聊天界面
        present.setTouserid(!TextUtils.isEmpty(user.getUserid()) ? user.getUserid() : "2200");
        present.setName(getString(R.string.chatmessage));
        present.setUserid(userInfo.getUserId());

        dialog_item_gift.dialogitemgift(context, present, new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                //进入聊天窗口系统提示消息
                Message message = new Message();
                message.what = 4;
                message.obj = gson.toJson(giftInfo);
                handlerZ.sendMessage(message);
                Toashow.show(String.format(getString(R.string.gif_title) + "", giftInfo.title));
            }

            @Override
            public void onChargeClick() {
                Log.d(TAG, "点击充值金币: ");

            }
        }, paymnets);
    }

    /**
     * 播放礼物动画
     *
     * @param giftInfo
     */
    private void playgift(GiftInfo giftInfo) {
        boolean svga = giftInfo.lottieUrl.toLowerCase().endsWith(".svga");
        if (svga) {
            mAnimationUrlList.addLast(giftInfo.lottieUrl);
            if (!mIsPlaying) {
                SVGAImageView();
            }

        }
    }

    /**
     * 加载远端服务器中的动画
     */
    private void SVGAImageView() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if (mAnimationUrlList.size() == 0 || mAnimationUrlList.isEmpty()) {
                        return;
                    }
                    String lottieUrl = mAnimationUrlList.getFirst();
                    if (!TextUtils.isEmpty(lottieUrl)) {
                        //移除最早的一条数据
                        mAnimationUrlList.removeFirst();
                    }
                    parser.decodeFromURL(new URL(lottieUrl), new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(SVGAVideoEntity videoItem) {
                            SVGADrawable drawable = new SVGADrawable(videoItem);
                            //正在播放动画
                            if (svgaImageView != null) {
                                mIsPlaying = true;
                                svgaImageView.setImageDrawable(drawable);
                                svgaImageView.startAnimation();
                                svgaImageView.setCallback(new SVGACallback() {
                                    @Override
                                    public void onPause() {
                                        Log.d(TAG, "onPause: ");

                                    }

                                    @Override
                                    public void onFinished() {
                                        //暂无连接停止播
                                        if (mAnimationUrlList.isEmpty() && svgaImageView != null) {
                                            svgaImageView.stopAnimation();
                                            mIsPlaying = false;
                                        } else {
                                            SVGAImageView();
                                        }

                                    }

                                    @Override
                                    public void onRepeat() {
                                        Log.d(TAG, "onRepeat: ");

                                    }

                                    @Override
                                    public void onStep(int i, double v) {
                                        Log.d(TAG, "onStep: ");

                                    }
                                });
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            }

        }.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        if (svgaImageView != null) {
            svgaImageView.stopAnimation();
        }

    }

    /**
     * 刷新radapter数据
     */
    private void mynotifyDataSetChanged(data data) {
        list.add(data);
        if (list.size() > 900) {
            list.remove(0);
        }
        radapter.notifyDataSetChanged();
        recyclerview.scrollToPosition(radapter.getItemCount() - 1);
    }
}