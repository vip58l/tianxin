/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/23 0023
 */


package com.tianxin.activity.video;

import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.widget.DyVideoPlay;
import com.tencent.opensource.model.curriculum;

import butterknife.BindView;

/**
 * 课程播放器
 */
public class videoijkplayer4 extends BasActivity2 {
    private static final String TAG = videoijkplayer4.class.getSimpleName();
    @BindView(R.id.dyvideopaly)
    DyVideoPlay dyvideopaly;
    curriculum curriculum;

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        return R.layout.video_play_mp;
    }

    @Override
    public void iniview() {
        curriculum = (com.tencent.opensource.model.curriculum) getIntent().getSerializableExtra(Constants.ROOM);
        dyvideopaly.setVideoPath(curriculum);
    }

    @Override
    public void initData() {
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        dyvideopaly.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //获取屏幕状态横屏|竖屏
        int portrait = getResources().getConfiguration().orientation;
        if (portrait == ActivityInfo.SCREEN_ORIENTATION_USER) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   //坚屏
            return true;
        }

        dyvideopaly.stopPlayback();
        return super.onKeyDown(keyCode, event);
    }
}
