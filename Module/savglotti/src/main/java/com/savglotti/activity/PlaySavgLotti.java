package com.savglotti.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.savglotti.BaseActivity.BaseFrameLayout;
import com.savglotti.R;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class PlaySavgLotti extends BaseFrameLayout {
    private static final String TAG = PlaySavgLotti.class.getSimpleName();
    private LottieAnimationView lottie_view;
    private SVGAImageView svgaimageview;
    private SVGAParser mSVGAParse;
    private LinkedList<String> mAnimationUrlList; //动画链接
    public LinkedList<String> mAnimationUrlList2;
    private boolean mIsPlaying; //动画链接
    private boolean mIsPlaying2;

    private NumberAnim giftNumberAnim;  //礼物数量
    private LinearLayout ll_gift_group; //插入礼物
    private TranslateAnimation outAnim; //退出动画
    private TranslateAnimation inAnim;  //进入动画
    private Timer timer;
    private TimerTask task;

    public PlaySavgLotti(@NonNull Context context) {
        super(context);
        init();
    }

    public PlaySavgLotti(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlaySavgLotti(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PlaySavgLotti(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void init() {
        context = getContext();
        activity = (Activity) getContext();
        inflate(getContext(), R.layout.play, this);
        lottie_view = findViewById(R.id.animation_view1);
        svgaimageview = findViewById(R.id.svgaimageview);
        ll_gift_group = findViewById(R.id.ll_gift_group);
        mSVGAParse = new SVGAParser(context);
        mAnimationUrlList = new LinkedList<>();
        mAnimationUrlList2 = new LinkedList<>();

        initAnim();                         //初始化动画
        clearTiming();                      //定时清理礼物列表信息
    }

    /**
     * 播放动画
     *
     * @param lottieUrl
     */
    public void play(String lottieUrl) {
        if (lottieUrl.toLowerCase().endsWith(".svga")) {
            mAnimationUrlList.addLast(lottieUrl);
            if (!mIsPlaying) {
                playGiftAnimation();
            }
        }
    }

    /**
     * 播放lottie JSON
     *
     * @param lottieUrl
     */
    private void playlottie(String lottieUrl) {
        if (lottieUrl.toLowerCase().endsWith(".json")) {
            mAnimationUrlList2.addLast(lottieUrl);
            if (!mIsPlaying2) {
                playLottieAnimation();
            }
        }
    }

    /**
     * 播放礼物动画
     * //assets/svga 动画资源
     */
    private void playGiftAnimation() {
        try {
            if (mAnimationUrlList.size() == 0 || mAnimationUrlList.isEmpty()) {
                return;
            }
            String lottieUrl = mAnimationUrlList.getFirst();
            if (!TextUtils.isEmpty(lottieUrl)) {
                //移除最早的一条数据
                mAnimationUrlList.removeFirst();
            }

            //加载网络动画播放
            mSVGAParse.decodeFromURL(new URL(lottieUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {

                    SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                    if (svgaimageview != null) {
                        mIsPlaying = true;
                        svgaimageview.setVisibility(VISIBLE);
                        svgaimageview.setImageDrawable(drawable);
                        svgaimageview.startAnimation();
                    }

                }

                @Override
                public void onError() {

                }
            });

            //礼物动画播放监听
            svgaimageview.setCallback(svgaCallback);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听回调
     */
    private SVGACallback svgaCallback = new SVGACallback() {
        @Override
        public void onPause() {
            Log.d(TAG, "暂停");
        }

        @Override
        public void onFinished() {
            Log.d(TAG, "完成");
            if (mAnimationUrlList.isEmpty() && svgaimageview != null) {
                svgaimageview.setVisibility(GONE);
                svgaimageview.stopAnimation();
                mIsPlaying = false;
            } else {
                playGiftAnimation();
            }

        }

        @Override
        public void onRepeat() {
            Log.d(TAG, "重复");

        }

        @Override
        public void onStep(int i, double v) {

        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow: ");
        if (svgaimageview != null) {
            svgaimageview.stopAnimation();
        }
        mAnimationUrlList.clear();
        mAnimationUrlList2.clear();
        task.cancel();
        timer.cancel();
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
     * 定时清理礼物列表信息
     */
    private void clearTiming() {
        timer=new Timer();
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

        timer.schedule(task, 1000, 3000);
    }

    /**
     * 刷礼物 传入对应的字符串
     *
     * @param giftInfo
     */
    public void showGift(GiftInfo giftInfo) {
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
            //大屏动画特效SVGAPlayer
            play(giftInfo.lottieUrl);

            //大屏动画特效JSON
            playlottie(giftInfo.lottieUrl);


        }
    }

    /**
     * 获取礼物返回布局
     */
    private View getNewGiftView(GiftInfo giftInfo) {
        // 添加标识, 该view若在layout中存在，就不在生成（用于findViewWithTag判断是否存在）
        View giftView = inflate(getContext(), R.layout.item_gift, null);
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
        String avatar = giftInfo.sendUserHeadIcon;
        String Name = giftInfo.sendUser;
        //图片如果不是空的
        if (!TextUtils.isEmpty(avatar)) {
            Glide.with(context).load(avatar).into(icon);
        }
        tag_name.setText(Name);
        gifname.setText("送出" + giftInfo.title);
        Glide.with(context).load(giftInfo.giftPicUrl).into(iv_gift);

        //添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的
        iv_gift.setTag(System.currentTimeMillis());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        giftView.setLayoutParams(params);
        return giftView;
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

    /**
     * 播放动画特效显示
     */
    private void playLottieAnimation() {
        String lottieUrl = mAnimationUrlList2.getFirst();
        if (!TextUtils.isEmpty(lottieUrl)) {
            mAnimationUrlList2.removeFirst();
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
                            mIsPlaying2 = false;
                        } else {
                            playLottieAnimation();
                        }
                    }
                }
            });
            lottie_view.playAnimation();
            mIsPlaying2 = true;
        }
    }


}
