package com.savglotti.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.savglotti.BaseActivity.BaseActivity;
import com.savglotti.R;

public class ActivitySavg extends BaseActivity {
    private String TAG = ActivitySavg.class.getSimpleName();
    private SVGAImageView svgaimageview;
    private PlaySavgLotti playsavglotti;
    private Button cancel_button;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ActivitySavg.class);
        context.startActivity(intent);
    }

    @Override
    protected int contentview() {
        return R.layout.activity_savg;
    }

    @Override
    protected void iniview() {
        svgaimageview = findViewById(R.id.svgaimageview);
        playsavglotti = findViewById(R.id.playsavglotti);
        cancel_button = findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected void inidate() {
        playGiftAnimation();
    }


    /**
     * 播放礼物动画
     * //assets/svga 动画资源
     */
    private void playGiftAnimation() {
        SVGAParser mSVGAParse = new SVGAParser(context);
        String assetsName = "svga/gift_gif_7.svga";
        mSVGAParse.parse(assetsName, new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                svgaimageview.setVisibility(View.VISIBLE);
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                svgaimageview.setImageDrawable(drawable);
                svgaimageview.startAnimation();
            }

            @Override
            public void onError() {

            }
        });

        /**
         * 礼物动画播放监听
         */
        svgaimageview.setCallback(svgaCallback);
    }

    /**
     * 监听回调
     */
    private SVGACallback svgaCallback = new SVGACallback() {
        @Override
        public void onPause() {
            Log.d(TAG, "onPause: ");

        }

        @Override
        public void onFinished() {
            Log.d(TAG, "onFinished: ");
        }

        @Override
        public void onRepeat() {
            Log.d(TAG, "onRepeat: ");

        }

        @Override
        public void onStep(int i, double v) {
            Log.d(TAG, "onStep: ");

        }
    };


}