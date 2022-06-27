/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/18 0018
 */


package com.tianxin.activity.matching;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.BasActivity.BaseFrameLayout;
import com.tianxin.Module.McallBack;
import com.tianxin.Module.Datamodule;
import com.tianxin.Util.Config;
import com.tianxin.activity.activity_svip;
import com.tianxin.dialog.dialog_msg_svip;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utilsdialog.play;

import static com.blankj.utilcode.util.ActivityUtils.finishAllActivities;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 自定义匹配内容
 */
public class DiagnoseRadarView extends BaseFrameLayout implements View.OnTouchListener {
    private final String TAG = DiagnoseRadarView.class.getSimpleName();
    private final int STATUS_MARGIN = 20;
    private final int STATUS_ITEM_SPACING = 10;
    private final long delayMillis = 3000;
    private final int v1count = 100;
    private int Cumulative = 0;
    private ImageView view_diagnose_radar_bg;
    private LinearLayout statusContainer;
    private HorizontalScrollView statusScrollView;
    private int statusItemScrollPx;
    private int type;
    private int checkIndex;
    public Handler handler = new Handler();
    public SVGAImageView svgaimageview;
    public SVGAParser parser;
    public TextView totconunt;
    private play plays;

    public DiagnoseRadarView(@NonNull Context context) {
        super(context);
        init();
    }

    public DiagnoseRadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化控件
     */
    @Override
    public void init() {
        userInfo = UserInfo.getInstance();
        datamodule = new Datamodule(getContext());
        context = getContext();
        activity = (Activity) getContext();
        inflate(getContext(), R.layout.view_diagnose_radar, this);
        view_diagnose_radar_bg = findViewById(R.id.view_diagnose_radar_bg);
        statusContainer = findViewById(R.id.view_diagnose_radar_status_container);
        statusScrollView = findViewById(R.id.view_diagnose_radar_status_scroll);
        svgaimageview = findViewById(R.id.svgaimageview);
        totconunt = findViewById(R.id.totconunt);
        findViewById(R.id.view_diagnose_radar_back).setOnClickListener(myOnClickListener);
        statusScrollView.setOnTouchListener(this); //禁止水平滑动
    }

    public void setType(int type) {
        this.type = type;
        SVGAImageView1();
    }

    /**
     * 加载本地动画播
     */
    private void SVGAImageView1() {
        SVGAParser parser = new SVGAParser(getContext());
        parser.decodeFromAssets(type == 1 ? "speed_dating_video.svga" : "speed_dating_audio.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onError() {

            }

            @Override
            public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                svgaimageview.setImageDrawable(drawable);
                svgaimageview.startAnimation();
            }
        });
        //普通会员
        try {
            if (userInfo.getVip() == 0 && userInfo.getSex().equals("1") && userInfo.gettRole() == 0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onDestroy();
                        dialog_msg_svip.dialogmsgsvip(context, getContext().getString(R.string.dialog_msg_svip65), context.getString(R.string.tv_msg228), context.getString(R.string.tv_msg153), paymnets);
                    }
                }, delayMillis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 整状态项视图大小
     */
    public void initStatus() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(size);
        } else {
            size.x = display.getWidth();
            size.y = display.getHeight();
        }
        int margin = (int) (STATUS_MARGIN * dm.density + 0.5f);
        int spacing = (int) (STATUS_ITEM_SPACING * dm.density + 0.5f);
        int width = size.x - 2 * margin;
        int childCount = statusContainer.getChildCount();
        statusItemScrollPx = width + spacing;
        for (int i = 0; i < childCount; i++) {
            View v = statusContainer.getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
            lp.width = width;
            if (i == 0) {
                lp.leftMargin = margin;
                if (childCount == 2) {
                    lp.rightMargin = spacing;
                }
            } else if (i == childCount - 1) {
                lp.rightMargin = margin;
            } else {
                lp.leftMargin = spacing;
                lp.rightMargin = spacing;
            }
        }
    }


    /**
     * 正在检查中底部布局
     */
    private void inflateStatusItem() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.view_diagnose_radar_status, null);
        ImageView icon = inflate.findViewById(R.id.view_diagnose_radar_status_icon);
        TextView status = inflate.findViewById(R.id.view_diagnose_radar_status_status);
        ImageView loading = inflate.findViewById(R.id.view_diagnose_radar_status_loading);
        icon.setImageResource(R.mipmap.ic_man_choose);
        status.setText(R.string.tv_msg272);
        list.add(inflate);
        statusContainer.addView(inflate);
        loadview(loading, 1500);  //小图标360度转动
        inflate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getContext(), video_speed.class));
//                startActivity(new Intent(getContext(), audio_speed.class));
//                startActivity(new Intent(getContext(), thesamecity_speed.class));
                startActivity(new Intent(getContext(), activity_likeyou.class));
            }
        });
    }

    /**
     * 滚动指定位置条目
     *
     * @param status
     */
    public void onStatusChange(int status) {
        statusScrollView.smoothScrollTo(status * statusItemScrollPx, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate:完成加载");

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        plays = new play(context);
        plays.initMediaPlayer(com.tencent.qcloud.tim.uikit.R.raw.request_eliminate_against_separately);
        Log.d(TAG, "连接到窗口: 加入等待其他会员匹配");
        staranimationViews();

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            Log.d(TAG, "窗口焦点已更改 开始刷新在线匹配会员");

            if (plays != null) {
                plays.btnPlay();
            }
            //2秒向服务端发送一次请求，刷新在线匹配会员
            if (userInfo.getVip() == 1 || userInfo.getSex().equals("2")) {
                //handler.postDelayed(runCheckRunnable1, Dritem);
                handler.postDelayed(runCheckRunnable2, delayMillis);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "从窗口分离: 退出匹配会员");
        onDestroy();
    }


    /**
     * 开始动画 加入等待其他会员匹配
     */
    public void staranimationViews() {
        //view自定义动画转动
        loadview(view_diagnose_radar_bg, delayMillis);
        list.add(view_diagnose_radar_bg);

        //加入等待其他会员匹配
        if (userInfo.getVip() == 1 || userInfo.getSex().equals("2") || userInfo.gettRole() == 1) {
            datamodule.DiagnoseRadarView(paymnets, type);
        }

        int random = Config.random();
        totconunt.setText(String.format(getContext().getString(R.string.tot_nunt) + "", random));
        totconunt.setVisibility(GONE);
    }

    /**
     * view自定义动画转动
     * Duration转到的时间
     * 360度转动
     */
    public void loadview(View view, long Duration) {
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(Duration);
        view.startAnimation(animation);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    /**
     * 2秒后滑动滚动条
     */
    private final Runnable runCheckRunnable1 = new Runnable() {
        @Override
        public void run() {
            checkIndex++;
            inflateStatusItem();             //创建布局
            onStatusChange(checkIndex);      //设置滚动位置
            initStatus();                    //调整宽高
            handler.postDelayed(runCheckRunnable1, delayMillis);
        }
    };

    /**
     * 向服务器查询数据
     */
    private Runnable runCheckRunnable2 = new Runnable() {
        @Override
        public void run() {
            //正在查询1V1匹配会员
            datamodule.selectaq(paymnets1);
        }
    };

    private OnClickListener myOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Activity) getContext()).finish();
        }
    };

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess() {
            startActivity(new Intent(context, activity_svip.class));
            activity.finish();
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context);
            activity.finish();

        }

        @Override
        public void dismiss() {
            activity.finish();
        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(String msg) {
            try {
                Mesresult mesresult = new Gson().fromJson(msg, Mesresult.class);
                if (mesresult.isSuccess()) {
                    totconunt.setText(String.format(getContext().getString(R.string.tot_nunt), mesresult.getCode()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getContext().getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            ToastUtil.toastShortMessage(getContext().getResources().getString(R.string.eorrfali3));
            findViewById(R.id.view_diagnose_radar_back).performClick();
        }

        @Override
        public void onSuccess(String msg) {
            //匹配成功
            activity_likeyou.startsetAction(context, msg, type);
            if (plays != null) {
                plays.btnPause();
            }
        }

        @Override
        public void onSuccess(Object object) {
            Mesresult mesresult = (Mesresult) object;
            int random = Config.random();
            totconunt.setText(String.format(getContext().getString(R.string.tot_nunt), Integer.parseInt(mesresult.getCode()) == 0 ? String.valueOf(random) : mesresult.getCode()));
            totconunt.setVisibility(Integer.parseInt(mesresult.getCode()) > 5 ? VISIBLE : GONE);
            Cumulative++;
            if (Cumulative >= v1count) {
                ToastUtil.toastShortMessage(getContext().getResources().getString(R.string.eorrfali4));
                findViewById(R.id.view_diagnose_radar_back).performClick();
                Log.d(TAG, "当前匹配超时退出1 ");
            } else {
                Log.d(TAG, "继续等待匹配会员1: ");
                handler.postDelayed(runCheckRunnable2, delayMillis);
            }
        }


    };

    /**
     * 退出匹配会员
     */
    private void onDestroy() {
        svgaimageview.pauseAnimation();
        svgaimageview.stopAnimation();
        handler.removeCallbacksAndMessages(null);
        for (View view : list) {
            view.clearAnimation();
        }
        list.clear();
        datamodule.exitclert();
        if (plays != null) {
            plays.btnDestroy();
        }
    }

}
