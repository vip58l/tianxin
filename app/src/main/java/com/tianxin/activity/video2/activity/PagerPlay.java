package com.tianxin.activity.video2.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.onplVideoViewpage;
import com.tianxin.R;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Config;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.activity.activity_follow;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Mediaplay;
import com.pili.pldroid.player.widget.PLVideoView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ACTIVITY当前面播放
 * 接收直播合子传递过来的数据
 * 视频播放处理 直播合子
 */
public class PagerPlay extends BasActivity2 implements Mediaplay {
    private static final String TAG = PagerPlay.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private player mVideoView;
    private ImageView img_thumb, mPlay;

    public static void starsetAction(Context context, List<item> itemList, int totalpage, int type) {
        Intent intent = new Intent(context, PagerPlay.class);
        intent.putExtra(Constants.JSON, (Serializable) itemList);
        intent.putExtra(Constants.POSITION, 0);
        intent.putExtra(Constants.TOTALPAGE, totalpage);
        intent.putExtra(Constants.TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 接收外部传递数据
     *
     * @return
     */
    private List<Object> mygetIntent() {
        Intent intent = getIntent();
        List<item> itemList = (List<item>) intent.getSerializableExtra(Constants.JSON);
        mCurrentItem = intent.getIntExtra(Constants.POSITION, -1);
        totalPage = intent.getIntExtra(Constants.TOTALPAGE, -1);
        TYPE = intent.getIntExtra(Constants.TYPE, -1);
        for (item item : itemList) {
            videolist video = (videolist) item.object;
            list.add(video);
        }
        return list;
    }

    @Override
    protected int getview() {
        StatusBarUtil.mSystemUiVisibility(activity, true);
        return R.layout.activity_tiktok_index;
    }

    @Override
    public void iniview() {
        //在Activity中停止后台服务
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(DemoApplication.instance(), MyService.class));
        }
        Config.AsetctivityBLACK(activity);

        //page分页滑动处理
        PagerManager mLayoutManager = new PagerManager(context, LinearLayoutManager.VERTICAL);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setOnViewPagerListener(new PagerListener() {

            @Override
            public void onInitComplete(View view) {
                playVideo(view);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom, View view) {
                playVideo(view);

            }

            @Override
            public void onPageRelease(boolean isNext, int position, View view) {
                releaseVideo(view);
            }

        });

        //适配器
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(mAdapter = new TiktokAdapter(context, mygetIntent(), TiktokAdapter.TYPE1));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.scrollToPosition(mCurrentItem);

        //下拉刷新
        //recyclerview.smoothScrollToPosition(mCurrentItem);
        refreshlayout = findViewById(R.id.refreshlayout);
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            }
        });
    }

    @Override
    public void initData() {
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    @OnClick({R.id.selectwquer, R.id.gzt, R.id.puse, R.id.hot, R.id.tv1, R.id.tv2, R.id.lay3, R.id.tv4, R.id.tv5})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.selectwquer:
                startActivity(new Intent(context, SearchActivity.class));
                break;
            case R.id.gzt:
                startActivity(new Intent(context, activity_follow.class));
                break;
            case R.id.hot:

                break;
            case R.id.puse:

                break;
            case R.id.tv1:
                starmainActivity(1);
                break;
            case R.id.tv2:
                starmainActivity(2);
                break;
            case R.id.lay3:
                starmainActivity(3);
                break;
            case R.id.tv4:
                starmainActivity(4);
                break;
            case R.id.tv5:
                starmainActivity(5);
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public long setProgress() {
        return 0;
    }

    @Override
    public void play() {
        if (mVideoView != null && mPlay != null) {
            if (mVideoView.isPlaying()) {
                mPlay.animate().alpha(1f).setDuration(100).start();
                mVideoView.pause();
            } else {
                mPlay.animate().alpha(0f).setDuration(100).start();
                img_thumb.animate().alpha(0f).setDuration(500).start();
                mVideoView.start();
            }
        }
    }

    /**
     * 播放视频
     */
    @Override
    public void playVideo(View view) {
        if (view != null) {
            mVideoView = view.findViewById(R.id.video_view);
            img_thumb = view.findViewById(R.id.mThumb);
            img_thumb.setOnClickListener(v -> play());
            mPlay = view.findViewById(R.id.mPlay);
            mVideoView.start();
            if (img_thumb != null && mVideoView.isPlaying()) {
                img_thumb.animate().alpha(0f).setDuration(200).start();
            }
        }
    }

    /**
     * 停止播放
     */
    @Override
    public void releaseVideo(View view) {
        if (view != null) {
            PLVideoView videoView = view.findViewById(R.id.video_view);
            videoView.stopPlayback();
            ImageView mThumb = view.findViewById(R.id.mThumb);
            mThumb.animate().alpha(1f).setDuration(200).start();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mVideoView != null) {
            mVideoView.start();
            mPlay.animate().alpha(0f).setDuration(200).start();
            img_thumb.animate().alpha(0f).setDuration(200).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stop();
            mVideoView.reset();
            mVideoView.release();
            mVideoView = null;
            list.clear();
        }
    }

    /**
     * 回到初始页切换页面
     *
     * @param TYPE
     */
    private void starmainActivity(int TYPE) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("RESULT", TYPE);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);

    }

    /**
     * 播放器监听
     */
    private final com.tianxin.Module.onplVideoViewpage onplVideoViewpage = new onplVideoViewpage() {
        @Override
        public void onInfo(int i, int i1, Object o) {
            switch (i) {
                case MEDIA_INFO_BUFFERING_START:
                    Log.d(TAG, "媒体信息缓冲启动");
                    break;
                case MEDIA_INFO_BUFFERING_END:
                    Log.d(TAG, "媒体信息缓冲结束 ");
                case MEDIA_INFO_VIDEO_RENDERING_START:
                    Log.d(TAG, "媒体信息视频渲染开始");
                case MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.d(TAG, "媒体信息视频帧渲染");
                    mVideoView.start();
                    img_thumb.animate().alpha(0f).setDuration(500).start();
                    break;

            }
        }

        @Override
        public boolean onError(int i, Object o) {
            switch (i) {
                case MEDIA_ERROR_UNKNOWN://	-1	未知错误
                    finish();
                    break;
                case ERROR_CODE_OPEN_FAILED: //-2	播放器打开失败
                    couneroo++;
                    if (couneroo < 5) {
                        mVideoView.start();
                    } else {
                        ToastUtil.toastLongMessage(getString(R.string.tv_msg236));
                        finish();
                    }
                    break;
                case ERROR_CODE_IO_ERROR://-3
                    // 网络异常
                    if (!Config.isNetworkAvailable()) {
                        mVideoView.pause();
                        ToastUtil.toastLongMessage(getString(R.string.tv_msg235));
                    }

                    break;
                case ERROR_CODE_SEEK_FAILED://	-4	拖动失败
                    break;
                case ERROR_CODE_CACHE_FAILED: //-5	预加载失败
                    break;
                case ERROR_CODE_HW_DECODE_FAILURE://	-2003	硬解失败
                    break;
                case ERROR_CODE_PLAYER_DESTROYED://-2008	播放器已被销毁，需要再次 setVideoURL 或 prepareAsync
                    break;
                case ERROR_CODE_PLAYER_VERSION_NOT_MATCH://-9527	so 库版本不匹配，需要升级
                    break;
                case ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED://-4410	AudioTrack 初始化失败，可能无法播放音频
                    break;

            }
            return false;
        }

        @Override
        public void onCompletion() {

        }

        @Override
        public void onImageCaptured(byte[] bytes) {

        }

        @Override
        public void onPrepared(int i) {
            mVideoView.start();
            img_thumb.animate().alpha(0f).setDuration(500).start();
            mVideoView.setLooping(true);
        }

        @Override
        public void onSeekComplete() {

        }

        @Override
        public void onVideoSizeChanged(int i, int i1) {

        }
    };

}
