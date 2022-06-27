package com.tianxin.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.Module.Datamodule;
import com.tianxin.Module.McallBack;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.Module.api.present;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.activity.video.live.adapter.MessageAdapter;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_item_gift;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.getlist.HotRooms;
import com.tianxin.listener.Paymnets;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lib.homhomlib.view2.DivergeView;

/**
 * 点赞交互送礼物
 */
public class Movie_zbativity extends FrameLayout implements View.OnClickListener {
    public TextView title, conum, tvgz, tv_chat;
    public ListView lv_message;
    public LinearLayout line1, linmsess;
    public ImageView circular, home1, home2, home3, home4;
    public EditText Mesges;
    public Button senbnt;

    private PLVideoView plVideoView;      //外部传入的视频播放器
    private boolean getsetVolume = false; //静音或开启控制

    private NumberAnim giftNumberAnim;  //礼物数量
    private LinearLayout ll_gift_group; //插入礼物
    private TranslateAnimation outAnim; //退出动画
    private TranslateAnimation inAnim;  //进入动画
    public Timer timer;
    public TimerTask task;
    private Paymnets paymnets;
    private Context context;


    //聊天滚动消息
    private List<String> messageData;
    private MessageAdapter messageAdapter;
    //客户信息
    private Config_Msg instance;
    private UserInfo userInfo;
    //礼物动画
    public SVGAParser parser;
    public SVGAImageView svgaImageView;
    private static final String TAG = Movie_zbativity.class.getSimpleName();

    private HotRooms hotRooms;
    private DivergeView mDivergeView; //点赞
    private ArrayList<Bitmap> mList;  //图片
    private int mIndex = 0;


    public Movie_zbativity(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Movie_zbativity(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Movie_zbativity(Context context) {
        super(context);
        init();
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public void init() {
        inflate(getContext(), R.layout.activity_movie_zbativity2, this);
        iniview();
        ividate();
        Listener();

    }

    private void ividate() {
        userInfo = UserInfo.getInstance();
        instance = Config_Msg.getInstance();
        timer = new Timer();

        Log.d(TAG, "user_save_update_Profile: "+ UserInfo.getInstance().toString());

    }

    private void iniview() {
        context = getContext();
        line1 = findViewById(R.id.line1);
        title = findViewById(R.id.title);
        conum = findViewById(R.id.conum);
        lv_message = findViewById(R.id.lv_message);
        ll_gift_group = findViewById(R.id.ll_gift_group);
        circular = findViewById(R.id.circular);
        tv_chat = findViewById(R.id.tv_chat);
        tvgz = findViewById(R.id.tvgz);
        home1 = findViewById(R.id.home1);
        home2 = findViewById(R.id.home2);
        home3 = findViewById(R.id.home3);
        home4 = findViewById(R.id.home4);
        linmsess = findViewById(R.id.linmsess);
        Mesges = findViewById(R.id.Mesges);
        senbnt = findViewById(R.id.senbnt);

        //动画组件
        lottie_view = findViewById(R.id.lottie_view);
        svgaImageView = findViewById(R.id.svgaImage);
        mDivergeView = findViewById(R.id.divergeView);
    }

    private void Listener() {
        tv_chat.setOnClickListener(this::onClick);
        senbnt.setOnClickListener(this::onClick);
        circular.setOnClickListener(this::onClick);
        tvgz.setOnClickListener(this::onClick);
        home1.setOnClickListener(this::onClick);
        home2.setOnClickListener(this::onClick);
        home3.setOnClickListener(this::onClick);
        home4.setOnClickListener(this::onClick);
        lv_message.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                linmsess.setVisibility(GONE);
                return false;
            }
        });
    }


    public void Movie(HotRooms hotRooms) {
        this.hotRooms = hotRooms;
        title.setText(hotRooms.nickName);
        int random = (int) (Math.random() * 1000 + 1);
        conum.setText(String.valueOf(random));
        Glide.with(DemoApplication.instance()).load(hotRooms.headerImageOriginal).override(50, 50).into(circular);
        initMessage(hotRooms.getNickName());//初始化评论列表
        initAnim();                         //初始化动画
        clearTiming();                      //定时清理礼物列表信息

    }

    public boolean isGetsetVolume() {
        return getsetVolume;
    }

    public void setGetsetVolume(boolean getsetVolume) {
        this.getsetVolume = getsetVolume;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_chat: {
                //评论显示
                linmsess.setVisibility(VISIBLE);
                Mesges.setFocusable(true);
                break;
            }

            case R.id.senbnt: {
                //消息评论
                String trim = Mesges.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    ToastUtils.showLong(R.string.tv_msg170);
                    return;
                }
                messageData.add(getMesges());
                messageAdapter.NotifyAdapter(messageData);
                lv_message.setSelection(messageData.size());
                break;
            }

            case R.id.circular: {
                //头像点击
                Toashow.show(getContext(), getContext().getString(R.string.tv_msg171));
                break;
            }

            case R.id.tvgz: {
                //关注点击
                Toashow.show(getContext(), getContext().getString(R.string.tv_msg172));
                break;
            }

            case R.id.home1: {
                //若参数为 0f，则会将视频静音；若参数大于 1f，播放音量会大于视频原来的音量
                if (!getsetVolume) {
                    getsetVolume = true;
                    plVideoView.setVolume(0f, 0f);
                    Toast.makeText(getContext(), getContext().getString(R.string.tv_msg173), Toast.LENGTH_SHORT).show();
                } else {
                    getsetVolume = false;
                    plVideoView.setVolume(1f, 100f);
                    Toast.makeText(getContext(), getContext().getString(R.string.tv_msg174), Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case R.id.home2: {
                //点赞漂心
                if (userInfo.getJinbi() > 0 || userInfo.getVip() == 1) {
                    mDivergeViewstart();
                } else {
                    dialog_msg_svip.dialogmsgsvip(getContext(), context.getString(R.string.dialog_tv_msg224), context.getString(R.string.tv_msg228), context.getString(R.string.tv_msg154), new Paymnets() {
                        @Override
                        public void onSuccess() {
                            //跳转到购买金币充值
                            Detailedlist.starsetAction(context);
                        }

                        @Override
                        public void onRefresh() {
                            //转到赚钱任务页
                            McallBack.starsetAction(context);
                        }
                    });
                }
                break;
            }

            case R.id.home3: {
                //弹出礼物送礼监听
                present present = new present();
                present.setTYPE(3);                       //虚拟直播间3
                present.setTouserid("");                  //接收方ID
                present.setUserid(userInfo.getUserId());  //送出金币方名称
                present.setName(hotRooms.nickName);       //接收金币方名称
                //弹出赠送礼物动画事件 LiveVideo->paymnets
                dialog_item_gift.dialogitemgift(context, present, giftPanelDelegate, paymnets);
                break;
            }

            case R.id.home4: {
                //关闭退出
                Activity activity = (Activity) context;
                activity.finish();
                break;
            }

        }
    }


    /**
     * 初始化评论列表
     */
    private void initMessage(String nickName) {
        messageData = new LinkedList<>();
        try {
            messageData.add("系统提示:" + (TextUtils.isEmpty(instance.getMsg()) ? "" : instance.getMsg().replace("{username}", TextUtils.isEmpty(nickName) ? "" : nickName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        messageData.add((TextUtils.isEmpty(userInfo.getName()) ? userInfo.getUserId() : userInfo.getName()) + " 进入来了");
        messageAdapter = new MessageAdapter(getContext(), messageData);
        lv_message.setAdapter(messageAdapter);
        lv_message.setSelection(messageData.size());
    }

    /**
     * 获取消息
     *
     * @return
     */
    private String getMesges() {
        String Mesgesstr = Mesges.getText().toString().trim();
        Mesges.setText(null);
        return Mesgesstr;
    }


    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming() {
        task = new TimerTask() {
            @Override
            public void run() {
                int childCount = ll_gift_group.getChildCount();
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < childCount; i++) {
                    View childView = ll_gift_group.getChildAt(i);
                    ImageView iv_gift = childView.findViewById(R.id.iv_gift);
                    long lastUpdateTime = (long) iv_gift.getTag();
                    if (nowTime - lastUpdateTime >= 3000) {
                        RemoveGiftView(i); //移除礼物
                    }
                }
            }
        };

        timer.schedule(task, 0, 3000);
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        giftNumberAnim = new NumberAnim(); // 初始化数字动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_in); // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.gift_out); // 礼物退出时动画
    }

    /**
     * 传入视频播放器
     *
     * @param plVideoView
     */
    public void setPlay(PLVideoView plVideoView) {
        this.plVideoView = plVideoView;
    }

    /**
     * 送的礼物后面的数字动画
     */
    public class NumberAnim {
        private Animator lastAnimator;

        public void showAnimator(View v) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.cancel();
                lastAnimator.end();
            }
            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 2.5f, 1.3f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 2.5f, 1.3f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY);
            animSet.setDuration(200);
            lastAnimator = animSet;
            animSet.start();
        }
    }

    /**
     * 刷礼物 传入对应的字符串
     *
     * @param giftInfo
     */
    private void showGift(GiftInfo giftInfo) {
        //拿到布局视图是否存在 传入礼物ID号
        View newGiftView = ll_gift_group.findViewWithTag(giftInfo.giftId);
        // 是否有该tag类型的礼物
        if (newGiftView == null) {
            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group.getChildCount() >= 3) {
                // 获取前2个元素的最后更新时间1
                View giftView01 = ll_gift_group.getChildAt(0);
                ImageView iv_gift01 = giftView01.findViewById(R.id.iv_gift);
                long lastTime1 = (long) iv_gift01.getTag();

                // 获取前2个元素的最后更新时间2
                View giftView02 = ll_gift_group.getChildAt(1);
                ImageView iv_gift02 = giftView02.findViewById(R.id.iv_gift);
                long lastTime2 = (long) iv_gift02.getTag();

                //移除时间最长的一个礼物控件
                RemoveGiftView(lastTime1 > lastTime2 ? 1 : 0);
            }
            //获取布局VIEW获取礼物
            newGiftView = getNewGiftView(giftInfo);
            //加入到ll_gift_group
            ll_gift_group.addView(newGiftView);

            // 执行进入时播放动画
            newGiftView.startAnimation(inAnim);
            TextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            inAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mtv_giftNum != null) {
                        //数字放大动画
                        giftNumberAnim.showAnimator(mtv_giftNum);
                    }
                }

            });

        } else {
            // 更新修改的时间，用于判断回收
            ImageView iv_gift = newGiftView.findViewById(R.id.iv_gift);
            iv_gift.setTag(System.currentTimeMillis());

            // 更新记录礼物个数
            TextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            int giftCount = (int) mtv_giftNum.getTag() + 1; // 递增
            mtv_giftNum.setTag(giftCount);
            mtv_giftNum.setText("x" + giftCount);

            //赠送礼物后面的数字放大动画
            giftNumberAnim.showAnimator(mtv_giftNum);
        }

        //载入动画效果
        if (giftInfo.type == 1 && !TextUtils.isEmpty(giftInfo.lottieUrl)) {
            //全屏动画效果  进入播放动画特效
            boolean json = giftInfo.lottieUrl.toLowerCase().endsWith(".json");
            boolean svga = giftInfo.lottieUrl.toLowerCase().endsWith(".svga");

            //大屏动画特效SVGAPlayer
            if (svga) {
                mAnimationUrlList2.add(giftInfo.lottieUrl); //增加播放地址
                if (!mIsPlaying2) {
                    paySVGAParser();
                }
            }

            //大屏动画特效lottie
            if (json) {
                mAnimationUrlList.add(giftInfo.lottieUrl);  //增加播放地址
                if (!mIsPlaying) {
                    playLottieAnimation();
                }
            }

        }
    }

    /**
     * 获取礼物返回布局
     */
    private View getNewGiftView(GiftInfo giftInfo) {
        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        View giftView = LayoutInflater.from(getContext()).inflate(R.layout.item_gift, null);
        giftView.setTag(giftInfo.giftId);
        // 添加标识，记录礼物个数
        TextView mtv_giftNum = giftView.findViewById(R.id.mtv_giftNum);
        mtv_giftNum.setTag(1);
        mtv_giftNum.setText("x1");

        ImageView iv_gift = giftView.findViewById(R.id.iv_gift);
        ImageView icon = giftView.findViewById(R.id.cv_send_gift_userIcon);
        TextView tag_name = giftView.findViewById(R.id.tag_name);
        TextView gifname = giftView.findViewById(R.id.gifname);

        //头像和名称暂时取自己本地的  重点(注册来自其他会员的送礼处理)
        String avatar = UserInfo.getInstance().getAvatar();
        String Name = UserInfo.getInstance().getName();
        String sex = UserInfo.getInstance().getSex();
        //图片如果不是空的
        if (!TextUtils.isEmpty(avatar)) {
            Glide.with(DemoApplication.instance()).load(avatar).into(icon);
        } else {
            icon.setImageResource(sex.equals("1") ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        }

        tag_name.setText(Name);
        gifname.setText("送出" + giftInfo.title);
        Glide.with(DemoApplication.instance()).load(giftInfo.giftPicUrl).into(iv_gift);
        //添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        iv_gift.setTag(System.currentTimeMillis());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        giftView.setLayoutParams(params);
        return giftView;
    }


    /**
     * 移除礼物列表里的giftView
     */
    private void RemoveGiftView(int index) {
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int childCount = ll_gift_group.getChildCount();
                if (childCount > 0) {
                    ll_gift_group.removeViewAt(index);
                }
            }
        });
        final View removeGiftView = ll_gift_group.getChildAt(index);
        removeGiftView.startAnimation(outAnim);

        // 开启动画，因为定时原因，所以可能是在子线程
        Activity activity = (Activity) getContext();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeGiftView.startAnimation(outAnim);
            }
        });

    }

    private LottieAnimationView lottie_view;
    public LinkedList<String> mAnimationUrlList = new LinkedList<>();
    public LinkedList<String> mAnimationUrlList2 = new LinkedList<>();
    private boolean mIsPlaying = false;
    private boolean mIsPlaying2 = false;

    /**
     * 播放大动画
     */
    private void paySVGAParser() {
        try {
            String lottieUrl = mAnimationUrlList2.getFirst();
            if (!TextUtils.isEmpty(lottieUrl)) {
                mAnimationUrlList2.removeFirst();  //清删除一条数据
            }
            if (parser == null) {
                parser = new SVGAParser(DemoApplication.instance());
            }
            parser.decodeFromURL(new URL(lottieUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onError() {
                }

                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    if (svgaImageView != null) {
                        svgaImageView.setImageDrawable(drawable);
                        svgaImageView.startAnimation();
                        mIsPlaying2 = true;
                        svgaImageView.setCallback(new SVGACallback() {
                            @Override
                            public void onPause() {

                            }

                            @Override
                            public void onFinished() {
                                if (mAnimationUrlList2.isEmpty()) {
                                    if (svgaImageView != null) {
                                        svgaImageView.stopAnimation();
                                        mIsPlaying2 = false;
                                    }
                                } else {
                                    paySVGAParser();
                                }
                            }

                            @Override
                            public void onRepeat() {

                            }

                            @Override
                            public void onStep(int i, double v) {


                            }
                        });
                    }
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放动画特效显示
     */
    private void playLottieAnimation() {
        String lottieUrl = mAnimationUrlList.getFirst();
        if (!TextUtils.isEmpty(lottieUrl)) {
            mAnimationUrlList.removeFirst();
            lottie_view.setVisibility(VISIBLE);
            lottie_view.setAnimationFromUrl(lottieUrl);
            lottie_view.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // 判断动画加载结束
                    if (valueAnimator.getAnimatedFraction() == 1f) {
                        if (mAnimationUrlList.isEmpty()) {
                            lottie_view.clearAnimation();
                            lottie_view.setVisibility(GONE);
                            mIsPlaying = false;
                        } else {
                            playLottieAnimation();
                        }
                    }
                }
            });
            lottie_view.playAnimation();
            mIsPlaying = true;
        }
    }

    private ArrayList<Bitmap> getBitmapDrawable() {
        ArrayList<Bitmap> mList = new ArrayList<>();
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_praise_sm1, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_praise_sm2, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_praise_sm3, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_praise_sm4, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_praise_sm5, null)).getBitmap());
        mList.add(((BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_praise_sm6, null)).getBitmap());
        return mList;
    }

    /********************** 点赞特效 ****************************/
    /**
     * 点赞特效1
     */
    public void mDivergeViewstart() {
        mList = getBitmapDrawable();
        if (mIndex == 5) {
            mIndex = 0;
        }
        mDivergeView.startDiverges(mIndex);
        mIndex++;
        /*      if (mDivergeView.isRunning()) {
                    mDivergeView.stop();
                }*/
        post(new Runnable() {
            @Override
            public void run() {
                mDivergeView.setEndPoint(new PointF(mDivergeView.getMeasuredWidth() / 2, 0));
                mDivergeView.setDivergeViewProvider(new Provider());
            }
        });

        if (userInfo.getVip() == 0) {
            //点赞送出金币默认平台方接收金币
            Datamodule.getInstance().chatjinbi("", getContext().getString(R.string.tm99) + hotRooms.nickName, null);
        }
    }

    /**
     * 点赞特效2
     */
    private class Provider implements DivergeView.DivergeViewProvider {

        @Override
        public Bitmap getBitmap(Object obj) {
            return mList == null ? null : mList.get((int) obj);
        }
    }

    private GiftPanelDelegate giftPanelDelegate = new GiftPanelDelegate() {
        @Override
        public void onGiftItemClick(GiftInfo giftInfo) {
            showGift(giftInfo);//送出礼物回调
        }

        @Override
        public void onChargeClick() {
            //充值回调
        }
    };
}
