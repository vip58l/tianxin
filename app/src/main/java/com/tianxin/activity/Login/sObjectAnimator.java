package com.tianxin.activity.Login;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

/**
 * 动画
 */
public class sObjectAnimator {
    public void play(ImageView mBgView1, ImageView mBgView2, ImageView mBgView3, ImageView mBgView4) {
        //A -> B:
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mBgView1, "alpha", 1.0f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mBgView2, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale1 = ObjectAnimator.ofFloat(mBgView1, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale2 = ObjectAnimator.ofFloat(mBgView1, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.setDuration(5000);
        animatorSet1.play(animator1).with(animator2).with(animatorScale1).with(animatorScale2);

        //B->C:
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mBgView2, "alpha", 1.0f, 0f);
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mBgView3, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale3 = ObjectAnimator.ofFloat(mBgView2, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale4 = ObjectAnimator.ofFloat(mBgView2, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(5000);
        animatorSet2.play(animator3).with(animator4).with(animatorScale3).with(animatorScale4);

        //C->D:
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mBgView3, "alpha", 1.0f, 0f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(mBgView4, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale5 = ObjectAnimator.ofFloat(mBgView3, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale6 = ObjectAnimator.ofFloat(mBgView3, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setDuration(5000);
        animatorSet3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //放大的View复位 将放大的View先复位到原大小
                mBgView1.setScaleX(1.0f);
                mBgView1.setScaleY(1.0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet3.play(animator5).with(animator6).with(animatorScale5).with(animatorScale6);

        //D->A:
        ObjectAnimator animator7 = ObjectAnimator.ofFloat(mBgView4, "alpha", 1.0f, 0f);
        ObjectAnimator animator8 = ObjectAnimator.ofFloat(mBgView1, "alpha", 0f, 1.0f);
        ObjectAnimator animatorScale7 = ObjectAnimator.ofFloat(mBgView4, "scaleX", 1.0f, 1.3f);
        ObjectAnimator animatorScale8 = ObjectAnimator.ofFloat(mBgView4, "scaleY", 1.0f, 1.3f);
        AnimatorSet animatorSet4 = new AnimatorSet();
        animatorSet4.setDuration(5000);
        animatorSet4.play(animator7).with(animator8).with(animatorScale7).with(animatorScale8);

        //需要将这4组动画按照顺序链接起来
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animatorSet1, animatorSet2, animatorSet3, animatorSet4);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //放大的View复位 将放大的View先复位到原大小
                mBgView2.setScaleX(1.0f);
                mBgView2.setScaleY(1.0f);
                mBgView3.setScaleX(1.0f);
                mBgView3.setScaleY(1.0f);
                mBgView4.setScaleX(1.0f);
                mBgView4.setScaleY(1.0f);
                // 循环播放
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }
}
