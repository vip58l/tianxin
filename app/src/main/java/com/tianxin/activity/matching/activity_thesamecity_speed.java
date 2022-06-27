/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.activity.matching;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tianxin.BasActivity.BasActivity;
import com.tianxin.R;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 她想和你聊天哟
 */
public class activity_thesamecity_speed extends BasActivity {

    @BindView(R.id.circleImageView)
    ImageView circleImageView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_msg)
    TextView tv_msg;


    @BindView(R.id.lottie_likeanim)
    LottieAnimationView lottieLike;
    @BindView(R.id.tv1)
    TextView tv1;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;


    public void playlottie() {
        // lottieLike.playAnimation();  //播放
//        lottieLike.pauseAnimation(); //暂停
//        lottieLike.cancelAnimation(); //取消
//        lottieLike.getDuration();  //获取动画时长

        lottieLike.addAnimatorListener(new Animator.AnimatorListener() { //添加动画监听
            @Override
            public void onAnimationStart(Animator animation) {
                animation.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private LinkedList<String> mAnimationUrlList;
    private boolean mIsPlaying;

    /**
     * 播放动画特效显示
     */
    private void playLottieAnimation() {
        String animationUrl = "https://assets5.lottiefiles.com/packages/lf20_t9v3tO.json";
        String animationUrl2 = "https://assets4.lottiefiles.com/packages/lf20_DnLK6k.json";
        mAnimationUrlList.addLast(animationUrl);
        mAnimationUrlList.addLast(animationUrl2);
        String lottieUrl = mAnimationUrlList.getFirst();
        if (!TextUtils.isEmpty(lottieUrl)) {
            mIsPlaying = true;
            mAnimationUrlList.removeFirst();
            lottieLike.setVisibility(VISIBLE);
            lottieLike.setAnimationFromUrl(lottieUrl);
            lottieLike.playAnimation();
            lottieLike.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    // 判断动画加载结束
                    if (valueAnimator.getAnimatedFraction() == 1f) {
                        if (mAnimationUrlList.isEmpty()) {
                            lottieLike.clearAnimation();
                            lottieLike.setVisibility(GONE);
                            mIsPlaying = false;
                        } else {
                            playLottieAnimation();
                        }
                    }
                }
            });

        }
    }

    @Override
    protected int getview() {
        return R.layout.thesamecity_speed;
    }

    @Override
    public void iniview() {
        name.setText("1111111");
        tv_msg.setText("222222");
    }


    @Override
    public void initData() {
        mAnimationUrlList = new LinkedList<>();
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
    }

    @Override
    @OnClick({R.id.img_close})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }
}
