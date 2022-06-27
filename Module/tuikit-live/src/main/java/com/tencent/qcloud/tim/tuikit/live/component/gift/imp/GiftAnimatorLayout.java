package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.qcloud.tim.tuikit.live.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

/**
 * 处理插入动画物效面
 */
public class GiftAnimatorLayout extends LinearLayout {

    String TAG = "GiftAnimatorLayout";
    private static final int MAX_SHOW_GIFT_BULLET_SIZE = 3; //礼物弹幕最多展示的个数
    private static final int MSG_PLAY_SCREEN_LOTTIE_ANIMATOR = 101;
    private Context mContext;
    private LottieAnimationView mLottieAnimationView;
    private LinearLayout mGiftBulletGroup;
    private LinkedList<String> mAnimationUrlList;
    private boolean mIsPlaying;

    public GiftAnimatorLayout(Context context) {
        super(context);
    }

    public GiftAnimatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.live_layout_lottie_animator, this, true);
        mLottieAnimationView = findViewById(R.id.lottie_view);
        mGiftBulletGroup = findViewById(R.id.gift_bullet_group);
        mAnimationUrlList = new LinkedList<>();
    }

    public void show(GiftInfo info) {
        if (info == null) {
            return;
        }

        //普通滚动礼物
        showGiftBullet(info);

        //进入全屏播放动画传入url
        if (info.type == GiftInfo.GIFT_TYPE_SHOW_ANIMATION_PLAY) {
            showLottieAnimation(info.lottieUrl); //全屏播
        }

    }

    public void hide() {
        mLottieAnimationView.clearAnimation();
        mLottieAnimationView.setVisibility(GONE);
    }

    /**
     * 全屏播礼物
     *
     * @param lottieSource
     */
    private void showLottieAnimation(String lottieSource) {
        if (!TextUtils.isEmpty(lottieSource)) {
            Message message = Message.obtain();
            message.obj = lottieSource;
            message.what = MSG_PLAY_SCREEN_LOTTIE_ANIMATOR;
            mHandler.sendMessage(message);
        }
    }

    /**
     * 播放动画特效显示.json
     */
    private void playLottieAnimation() {
        String lottieUrl = mAnimationUrlList.getFirst();
        if (!TextUtils.isEmpty(lottieUrl)) {
            mAnimationUrlList.removeFirst();
            mLottieAnimationView.setVisibility(VISIBLE);
            mLottieAnimationView.setAnimationFromUrl(lottieUrl);
            mLottieAnimationView.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // 判断动画加载结束
                    if (valueAnimator.getAnimatedFraction() == 1f) {
                        if (mAnimationUrlList.isEmpty()) {
                            mLottieAnimationView.clearAnimation();
                            mLottieAnimationView.setVisibility(GONE);
                            mIsPlaying = false;
                        } else {
                            playLottieAnimation();
                        }
                    }
                }
            });
            mLottieAnimationView.playAnimation();
            mIsPlaying = true;
        }
    }


    /**
     * 处理移除对应的对应的礼物列表
     *
     * @param info
     */
    private void showGiftBullet(GiftInfo info) {
        if (mGiftBulletGroup.getChildCount() >= MAX_SHOW_GIFT_BULLET_SIZE) {
            //如果礼物超过3个，就将第一个出现的礼物弹幕从界面上移除
            View firstShowBulletView = mGiftBulletGroup.getChildAt(0);
            if (firstShowBulletView != null) {
                GiftBulletFrameLayout bulletView = (GiftBulletFrameLayout) firstShowBulletView;
                bulletView.clearHandler();
                mGiftBulletGroup.removeView(bulletView);
            }
        }
        GiftBulletFrameLayout giftFrameLayout = new GiftBulletFrameLayout(mContext);
        mGiftBulletGroup.addView(giftFrameLayout);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mGiftBulletGroup.getLayoutParams();
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        if (giftFrameLayout.setGift(info)) {
            //播放动画特效
            giftFrameLayout.startAnimation();
        }
    }

    /**
     * 通知刷新消息
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_PLAY_SCREEN_LOTTIE_ANIMATOR) {
                String animationUrl = (String) msg.obj;
                boolean json = animationUrl.toLowerCase().endsWith(".json");
                boolean svga = animationUrl.toLowerCase().endsWith(".svga");
                if (json) {
                    mAnimationUrlList.addLast(animationUrl);
                    if (!mIsPlaying) {
                        //进入播放动画特效
                        playLottieAnimation();
                    }
                }

            }

        }
    };
}
