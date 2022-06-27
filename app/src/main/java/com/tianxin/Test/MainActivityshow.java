package com.tianxin.Test;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivityshow extends BasActivity2 {
    @BindView(R.id.ll_gift_group)
    LinearLayout ll_gift_group;

    NumberAnim giftNumberAnim;
    TranslateAnimation inAnim;
    TranslateAnimation outAnim;

    @Override
    protected int getview() {
        return R.layout.activity_main_activityshow;
    }

    @Override
    public void iniview() {
        //初始动化
        initAnim();
        //清理礼物
        clearTiming(ll_gift_group);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.sned1, R.id.sned2, R.id.sned3, R.id.sned4, R.id.sned5})
    @Override
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.sned1:
                showGift("1");
                break;
            case R.id.sned2:
                showGift("2");
                break;
            case R.id.sned3:
                showGift("3");
                break;
            case R.id.sned4:
                showGift("4");
                break;
            case R.id.sned5:
                showGift("5");
                break;
        }
    }

    @Override
    public void OnEorr() {

    }


    /**======================== 礼物处理 ======================================**/
    /**
     * 初始化动画
     */
    private void initAnim() {
        giftNumberAnim = new NumberAnim();// 初始化数字动画
        inAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_in);    // 礼物进入时动画
        outAnim = (TranslateAnimation) AnimationUtils.loadAnimation(this, R.anim.gift_out);  // 礼物退出时动画
    }

    /**
     * 送的礼物后面的数字动画X1 x2 放大
     */
    private class NumberAnim {
        private Animator lastAnimator;
        public void showAnimator(View v) {
            if (lastAnimator != null) {
                lastAnimator.removeAllListeners();
                lastAnimator.cancel();
                lastAnimator.end();
            }
            ObjectAnimator animScaleX = ObjectAnimator.ofFloat(v, "scaleX", 3.0f, 1.0f);
            ObjectAnimator animScaleY = ObjectAnimator.ofFloat(v, "scaleY", 3.0f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.playTogether(animScaleX, animScaleY);
            animSet.setDuration(200);
            lastAnimator = animSet;
            animSet.start();
        }

    }

    /**
     * 刷礼物
     */
    private void showGift(String TYPE) {
        View newGiftView = ll_gift_group.findViewWithTag(TYPE);

        // 是否有该tag类型的礼物
        if (newGiftView == null) {

            // 判断礼物列表是否已经有3个了，如果有那么删除掉一个没更新过的, 然后再添加新进来的礼物，始终保持只有3个
            if (ll_gift_group.getChildCount() >= 3) {

                // 获取前2个元素的最后更新时间 1
                View giftView01 = ll_gift_group.getChildAt(0);
                ImageView iv_gift01 = giftView01.findViewById(R.id.iv_gift);
                long lastTime1 = (long) iv_gift01.getTag();

                // 获取前2个元素的最后更新时间 2
                View giftView02 = ll_gift_group.getChildAt(1);
                ImageView iv_gift02 = giftView02.findViewById(R.id.iv_gift);
                long lastTime2 = (long) iv_gift02.getTag();

                //谁的时间长就清理谁
                removeGiftView(lastTime1 > lastTime2 ? 1 : 0, ll_gift_group);

            }

            // 获取礼物
            newGiftView = getNewGiftView(TYPE);

            //加入新的布局文件
            ll_gift_group.addView(newGiftView);

            // 播放动画物特效
            newGiftView.startAnimation(inAnim);
            TextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            TextView tag_name = newGiftView.findViewById(R.id.tag_name);
            inAnim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    giftNumberAnim.showAnimator(mtv_giftNum);
                }
            });
        } else {

            // 如果列表中已经有了该类型的礼物，则不再新建，直接拿出
            // 更新标识，记录最新修改的时间，用于回收判断
            ImageView iv_gift = newGiftView.findViewById(R.id.iv_gift);
            iv_gift.setTag(System.currentTimeMillis());

            // 更新标识，更新记录礼物个数
            TextView mtv_giftNum = newGiftView.findViewById(R.id.mtv_giftNum);
            int giftCount = (int) mtv_giftNum.getTag() + 1; // 递增
            mtv_giftNum.setText("x" + giftCount);
            mtv_giftNum.setTag(giftCount);
            giftNumberAnim.showAnimator(mtv_giftNum);
        }
    }


    /**
     * 获取礼物
     */
    private View getNewGiftView(String TYPE) {

        //添加布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.item_gift, null);
        //布局标识
        inflate.setTag(TYPE);

        //添加标识, 记录生成时间，回收时用于判断是否是最新的，回收最老的 送出的礼物图片
        ImageView iv_gift = inflate.findViewById(R.id.iv_gift);
        iv_gift.setTag(System.currentTimeMillis());

        switch (TYPE) {
            case "1":
                iv_gift.setImageResource(R.mipmap.ic_man_choose);
                break;
            case "2":
                iv_gift.setImageResource(R.mipmap.icon_woman_choose);
                break;
            case "3":
                iv_gift.setImageResource(R.mipmap.sf_icon_close_dialog_vip);
                break;
            case "4":
                iv_gift.setImageResource(R.mipmap.sr);
                break;
            case "5":
                iv_gift.setImageResource(R.mipmap.sr);
                break;
        }

        // 添加标识，记录礼物个数 x1 x2 x3 x4
        TextView mtv_giftNum = inflate.findViewById(R.id.mtv_giftNum);
        mtv_giftNum.setTag(1);
        mtv_giftNum.setText("x1");

        ImageView cv_send_gift_userIcon = inflate.findViewById(R.id.cv_send_gift_userIcon);
        TextView tag_name = inflate.findViewById(R.id.tag_name);

        tag_name.setText("小清新");
        cv_send_gift_userIcon.setBackgroundResource(R.mipmap.ic_man_choose);
        cv_send_gift_userIcon.setImageResource(R.mipmap.ic_man_choose);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 10;
        inflate.setLayoutParams(params);
        return inflate;
    }

    /**
     * 定时清理礼物列表信息
     */
    private void clearTiming(LinearLayout layout) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //获取layout列表礼物总数view
                int childCount = layout.getChildCount();
                //获得系统的时间，单位为毫秒
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < childCount; i++) {
                    //获得列表里的布局view视图
                    View childView = layout.getChildAt(i);
                    //获取视图对应的图片
                    ImageView iv_gift = childView.findViewById(R.id.iv_gift);
                    //获取加入的时间
                    long lastUpdateTime = (long) iv_gift.getTag();

                    // 当前时间-更新超过3秒就刷新
                    if (nowTime - lastUpdateTime >= 3000) {
                        //移除视图view
                        removeGiftView(i, layout);
                    }
                }
            }
        }, 0, 3000);
    }

    /**
     * 移除礼物列表里的giftView
     */
    private void removeGiftView(int index, LinearLayout layout) {
        // 移除列表，外加退出动画
        View removeGiftView = layout.getChildAt(index);
        outAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.removeViewAt(index);
            }
        });
        // 开启动画，因为定时原因，所以可能是在子线程
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeGiftView.startAnimation(outAnim);
            }
        });
    }

    /**====================== 礼物结束 ===========================================**/
}