package com.tianxin.Test;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianxin.R;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.widget.PLVideoView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class playviewactivity extends AppCompatActivity {
    @BindView(R.id.videoview)
    PLVideoView mVideoView;
    String TAG = "playviewactivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playviewactivity);
        ButterKnife.bind(this);
        playshow();
    }

    public void playshow() {
        mVideoView.setVideoPath("http://mvvideoshare2.meitudata.com/5dac2df4a9d18tt0o9roqn2406_H264_MP5dac39.mp4?k=e16867f68a5f63f21b0f51da681fb3b7&t=5dc5676d");
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_LOG_LEVEL, 0);
        mVideoView.setAVOptions(options);
        mVideoView.start();

        //设置屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


    }

    private final PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {

        }
    };

}
