/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/5 0005
 */


package com.tianxin.Test;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_item_gift;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 直播动画
 */
public class live_animation extends BasActivity2 {
    private final String TAG = "live_animation";
    private boolean mIsPlaying;
    private LinkedList<String> mAnimationUrlList;
    private Runnable mTimeRunnable;
    private int mTimeCount;
    private int duration;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;
    private Allcharge allcharge;

    @BindView(R.id.lottie_view)
    LottieAnimationView lottie_view;
    @BindView(R.id.svgaImage)
    SVGAImageView svgaImage;
    @BindView(R.id.loading)
    ImageView loading;
    @BindView(R.id.viewdiagnose)
    ImageView viewdiagnose;

    @Override
    protected int getview() {
        return R.layout.myitmlottieanimationview;
    }

    @Override
    public void iniview() {
        allcharge = Allcharge.getInstance();
        mAnimationUrlList = new LinkedList<>();
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        setanimation();
    }

    @Override
    public void initData() {

    }


    /*
     * 开始动画
     */
    private void startAnima() {
        //判断动画是否在运行
        boolean inPlaying = lottie_view.isAnimating();
        if (!inPlaying) {
            lottie_view.setProgress(0f);//设置开始时的进度值
            lottie_view.playAnimation();
        }
    }

    /*
     * 停止动画
     */
    private void stopAnima() {
        //判断动画是否在运行
        boolean inPlaying = lottie_view.isAnimating();
        if (inPlaying) {
            lottie_view.cancelAnimation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        lottie_view.cancelAnimation();
    }


    /**
     * 播放动画特效显示
     */
    private void playLottieAnimation() {
        String animationUrl = "https://assets5.lottiefiles.com/packages/lf20_t9v3tO.json";
        mAnimationUrlList.addLast(animationUrl);
        String lottieUrl = mAnimationUrlList.getFirst();
        if (!TextUtils.isEmpty(lottieUrl)) {
            mAnimationUrlList.removeFirst();
            lottie_view.setVisibility(VISIBLE);
            lottie_view.setAnimationFromUrl(animationUrl);
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

    /**
     * 播放动画特效1
     */
    private void paySVGAParser0() {
        SvgaUtils svgaUtils = new SvgaUtils(this, svgaImage);
        svgaUtils.initAnimator();
        svgaUtils.startAnimator("火箭发射");
    }

    /**
     * 播放动画特效2
     */
    private void paySVGAParser1() {
        SVGAParser parser = new SVGAParser(this);
        parser.decodeFromAssets("游艇.svga", new SVGAParser.ParseCompletion() {

            @Override
            public void onError() {

            }

            @Override
            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                svgaImage.setImageDrawable(drawable);
                svgaImage.startAnimation();
            }
        });
    }

    private void paySVGAParser2() {
        SVGAParser parser = new SVGAParser(this);
        parser.decodeFromAssets("汽车飞机.svga", new SVGAParser.ParseCompletion() {

            @Override
            public void onError() {

            }

            @Override
            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                svgaImage.setImageDrawable(drawable);
                svgaImage.startAnimation();
            }
        });
    }

    private void paySVGAParser3() {
        SVGAParser parser = new SVGAParser(this);
        parser.decodeFromAssets("战斗机.svga", new SVGAParser.ParseCompletion() {

            @Override
            public void onError() {

            }

            @Override
            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                svgaImage.setImageDrawable(drawable);
                svgaImage.startAnimation();
            }
        });
    }

    private void paySVGAParser4() {
        SVGAParser parser = new SVGAParser(this);
        parser.decodeFromAssets("一箭穿心.svga", new SVGAParser.ParseCompletion() {

            @Override
            public void onError() {

            }

            @Override
            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                svgaImage.setImageDrawable(drawable);
                svgaImage.startAnimation();
            }
        });
    }

    private void paySVGAParser5(String path) {
        SVGAParser parser = new SVGAParser(this);
        try {
            parser.decodeFromURL(new URL(path), new SVGAParser.ParseCompletion() {

                @Override
                public void onError() {

                }

                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    svgaImage.setImageDrawable(drawable);
                    svgaImage.startAnimation();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.loading})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                // paySVGAParser5("https://img-1256929999.cos.ap-chengdu.myqcloud.com/backstage/20190828/201908281056444248.svga");
                playLottieAnimation();
                dialog_item_gift.dialogitemgift(context,null, null, null);
                break;
            case R.id.button2:
                paySVGAParser5("https://img-1256929999.cos.ap-chengdu.myqcloud.com/backstage/20190826/201908260452007380.svga");
                break;
            case R.id.button3:
                paySVGAParser5("https://img-1256929999.cos.ap-chengdu.myqcloud.com/backstage/20190828/201908281057484914.svga");
                break;
            case R.id.button4:
                paySVGAParser5("https://img-1256929999.cos.ap-chengdu.myqcloud.com/backstage/20190826/201908260529443351.svga");
                break;
            case R.id.button5:
                paySVGAParser5("https://img-1256929999.cos.ap-chengdu.myqcloud.com/backstage/20191019/201910190247218177.svga");
                break;
            case R.id.button6:
                paySVGAParser5("https://img-1256929999.cos.ap-chengdu.myqcloud.com/backstage/20190826/201908260510088772.svga");
                break;
            case R.id.loading:
                viewAnimator();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);

        }
        if (mTimeHandlerThread != null) {
            mTimeHandlerThread.quit();
        }
    }


    @Override
    public void OnEorr() {

    }


    private void viewAnimator() {
        // ViewPropertyAnimator animate =loading.animate();
        //loading.animate().alpha(0).scaleXBy(2.0f).scaleYBy(2.0f).setDuration(2000).start();
        //loading.animate().alpha(0).scaleY(2.0f).scaleX(2.0f).setDuration(2000).start();
        //loading.animate().rotation(360).scaleYBy(2.0f).setDuration(2000).start();
        //loading.animate().rotationBy(180).setDuration(2000).start();

        //getRotateAnimation(loading, 2000);//重复转动用于加载中效果

        // 平移动画
        /*    loading.animate().translationXBy(100)
                    .setInterpolator(new OvershootInterpolator(4)) // 超过一点再回来
                    .setInterpolator(new CycleInterpolator(4)) // 左右晃动
                    .setInterpolator(new BounceInterpolator()) // 像球落地一样的感觉
                    .setDuration(350).start();*/

           /* loading.animate()//获取ViewPropertyAnimator对象
                    //动画持续时间
                    .setDuration(5000)

                    //透明度
                    .alpha(0)
                    .alphaBy(0)

                    //旋转
                    .rotation(360)
                    .rotationBy(360)
                    .rotationX(360)
                    .rotationXBy(360)
                    .rotationY(360)
                    .rotationYBy(360)

                    //缩放
                    .scaleX(1)
                    .scaleXBy(1)
                    .scaleY(1)
                    .scaleYBy(1)

                    //平移
                    .translationX(100)
                    .translationXBy(100)
                    .translationY(100)
                    .translationYBy(100)
                    .translationZ(100)
                    .translationZBy(100)

                    //更改在屏幕上的坐标
                    .x(10)
                    .xBy(10)
                    .y(10)
                    .yBy(10)
                    .z(10)
                    .zBy(10)

                    //插值器
                    .setInterpolator(new BounceInterpolator())//回弹
                    .setInterpolator(new AccelerateDecelerateInterpolator())//加速再减速
                    .setInterpolator(new AccelerateInterpolator())//加速
                    .setInterpolator(new DecelerateInterpolator())//减速
                    .setInterpolator(new LinearInterpolator())//线性

                    //动画延迟
                    .setStartDelay(1000)

                    //是否开启硬件加速
                    .withLayer();*/


        /*loading.animate().setInterpolator(new BounceInterpolator())//回弹
                .setInterpolator(new AccelerateDecelerateInterpolator())//加速再减速
                .setInterpolator(new AccelerateInterpolator())//加速
                .setInterpolator(new DecelerateInterpolator())//减速
                .setInterpolator(new LinearInterpolator());//线性*/
    }


    /**
     * 动画匹配转动起来
     */
    private void getRotateAnimation(View view, long Duration) {
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatCount(Animation.INFINITE);
        ra.setDuration(Duration);
        view.startAnimation(ra);
        ra.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd: ");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "onAnimationRepeat: ");
            }
        });
    }


    /**
     * 动画播放
     */
    private void setanimation() {
        Animation animation = AnimationUtils.loadAnimation(DemoApplication.instance(), R.anim.playmisc);
        //动画转动播放
        //loading.startAnimation(animation);
        //viewdiagnose.startAnimation(animation);
        //动画转动停止
        //loading.clearAnimation();
        getRotateAnimation(loading, 1500);
        getRotateAnimation(viewdiagnose, 1500);
    }
}
