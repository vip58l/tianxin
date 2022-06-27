package com.tianxin.activity.video;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.activity.activity_follow;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.Receiver.MyService;
import com.tianxin.Module.api.content;
import com.tianxin.Module.api.getotalPage;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.getlist.minivideo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.costransferpractice.object.ObjectActivity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 第采集三方视频播放器6房间
 */
public class videoijkplayer2 extends BasActivity2 {
    String TAG = videoijkplayer2.class.getSimpleName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    com.tianxin.activity.video2.widget.player video;
    ImageView mThumb, mPlay;

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        StatusBarUtil.setNavigationBarColor(activity);
        return R.layout.activity_video_play2;
    }

    @Override
    public void iniview() {
        //在Activity中停止Service
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(context, MyService.class));
        }
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mapLocation == null && ActivityLocation.checkPermissions(activity)) {
            lbsamap.getmyLocation(callback);
        }
    }

    @Override
    public void initData() {
        Intent dade = getIntent();
        mCurrentItem = dade.getIntExtra(Constants.POSITION, 0);
        totalPage = dade.getIntExtra(Constants.TOTALPAGE, 0);
        endtotalPage = dade.getIntExtra(Constants.ENDTOTALPAGE, 0);
        videoUrl = dade.getStringExtra(Constants.VIDEOURL);
        List<item> items = (List<item>) dade.getSerializableExtra("list");
        for (item item : items) {
            minivideo minivideo = (minivideo) item.object;
            videolist video = new videolist();
            video.setId(minivideo.getUid());
            video.setBigpicurl(minivideo.getBigpicurl());
            video.setPicurl(minivideo.getPicuser());
            video.setPicuser(minivideo.getPicuser());
            video.setTitle(minivideo.getTitle());
            video.setFnum(minivideo.getZnum());
            video.setPnum(minivideo.getVnum());
            video.setAnum(minivideo.getSec());
            video.setAlias(minivideo.getAlias());
            video.setPlayurl(minivideo.getPlayurl());
            video.setTencent(0);
            list.add(video);
        }

        mAdapter = new TiktokAdapter(context, list, TiktokAdapter.TYPE7);
        PagerManager mLayoutManager = new PagerManager(context, OrientationHelper.VERTICAL);
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

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        //滚动到指定位置
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.scrollToPosition(mCurrentItem);
        //recyclerview.smoothScrollToPosition(mCurrentItem);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list.clear();
                mAdapter.notifyDataSetChanged();
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(100/*,false*/);//传入false表示加载失败
                loadMoreData();
            }
        });
    }

    /**
     * 播放视频
     */
    private void playVideo(View view) {
        if (view != null) {
            mPlay = view.findViewById(R.id.mPlay);
            mPlay.animate().alpha(0f).start();
            video = view.findViewById(R.id.video_view);
            mThumb = view.findViewById(R.id.mThumb);
            mThumb.setOnClickListener(v -> play());
            video.setListener(new player.VideoPlayerListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onCompletion: ");

                }

                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                    Log.d(TAG, "onError: ");
                    return false;
                }

                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                    switch (i) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                            Log.d(TAG, "媒体信息视频渲染开始: ");
                            //Android属性动画(Animator)
                            //translationX和translationY	x轴和y轴的偏移量
                            //rotation、rotationX和rotationY	围绕支点旋转
                            //rotation(360) 围绕支点旋转
                            //scaleX和scaleY	缩放
                            //alpha	透明度
                            //x(5000).y(5000)  移动
                            //xBy(500).xBy(500) 移动
                            //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                            mPlay.animate().alpha(0f).setDuration(500).start();
                            mThumb.animate().alpha(0f).setDuration(500).start();
                            video.setLooping(true);//循环播放
                            return true;
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                            Log.d(TAG, "媒体信息视频跟踪滞后: ");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                            Log.d(TAG, " 媒体信息缓冲启动");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                            Log.d(TAG, "媒体信息缓冲结束");
                            return true;
                    }
                    return false;
                }

                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onPrepared: ");
                }
            });
        }
    }

    /**
     * 停止播放
     */
    private void releaseVideo(View view) {
        if (view != null) {
            com.tianxin.activity.video2.widget.player mVideoView = view.findViewById(R.id.video_view);
            ImageView image = view.findViewById(R.id.mThumb);
            image.animate().alpha(1f).setDuration(200).start();
            if (mVideoView.isPlaying()) {
                mVideoView.stop();
                mVideoView.reset();
                mVideoView.release();
            }
        }
    }

    /**
     * 暂停或播放
     */
    private void play() {
        if (video.isPlaying()) {
            mPlay.animate().alpha(1f).setDuration(200).start();
            video.pause();
        } else {
            mPlay.animate().alpha(0f).setDuration(200).start();
            video.start();
            video.setListener(new player.VideoPlayerListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onCompletion: ");
                }

                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                    Log.d(TAG, "onError: ");
                    return false;
                }

                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                    switch (i) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                            Log.d(TAG, "媒体信息视频渲染开始: ");
                            //Android属性动画(Animator)
                            //translationX和translationY	x轴和y轴的偏移量
                            //rotation、rotationX和rotationY	围绕支点旋转
                            //rotation(360) 围绕支点旋转
                            //scaleX和scaleY	缩放
                            //alpha	透明度
                            //x(5000).y(5000)  移动
                            //xBy(500).xBy(500) 移动
                            //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                            mPlay.animate().alpha(0f).setDuration(500).start();
                            mThumb.animate().alpha(0f).setDuration(500).start();
                            return true;
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                            Log.d(TAG, "媒体信息视频跟踪滞后: ");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                            Log.d(TAG, " 媒体信息缓冲启动");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                            Log.d(TAG, "媒体信息缓冲结束");
                            return true;
                    }
                    return false;
                }

                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onPrepared: ");
                }
            });
        }
    }

    @Override
    @OnClick({R.id.selectwquer, R.id.gzt, R.id.puse, R.id.hot, R.id.tv1, R.id.tv2, R.id.lay3, R.id.tv4, R.id.tv5})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.selectwquer:
                SearchActivity.starsetAction(context);
                break;
            case R.id.gzt:
                activity_follow.starsetAction(context);
                break;
            case R.id.hot:
                break;
            case R.id.puse:
                break;
            case R.id.tv1:
                starmainActivity(1);
                break;
            case R.id.tv2:
                starmainActivity(3);
                break;
            case R.id.lay3:
                sUploadActivity(ObjectActivity.ACTIVITY_VIDEO);
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
    public void loadMoreData() {
        totalPage++;
        datamodule.fragment6v(totalPage, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Toashow.show(getString(R.string.eorrfali2));
            }

            @Override
            public void onFail() {
                totalPage--;
                Toashow.show(getString(R.string.eorrfali3));
            }

            @Override
            public void onSuccess(Object object) {
                getotalPage getotalPage = (com.tianxin.Module.api.getotalPage) object;
                content content = getotalPage.content;
                for (minivideo minivideo : content.list) {
                    videolist video = new videolist();
                    video.setId(minivideo.getUid());
                    video.setBigpicurl(minivideo.getBigpicurl());
                    video.setPicurl(minivideo.getPicuser());
                    video.setPicuser(minivideo.getPicuser());
                    video.setTitle(minivideo.getTitle());
                    video.setFnum(minivideo.getZnum());
                    video.setPnum(minivideo.getVnum());
                    video.setAnum(minivideo.getSec());
                    video.setAlias(minivideo.getAlias());
                    video.setPlayurl(minivideo.getPlayurl());
                    video.setTencent(0);
                    list.add(video);
                }
                if (content.list.size() > 0) {
                    mAdapter.notifyDataSetChanged();
                }

                if (totalPage == 0 || totalPage == 1) {
                    Collections.shuffle(list);
                }

            }
        });
    }

    private void starmainActivity(int TYPE) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("TYPE", TYPE);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fade, R.anim.hold);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("RESULT", TYPE);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (video != null) {
            video.start();
            video.setListener(new player.VideoPlayerListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onCompletion: ");
                }

                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                    Log.d(TAG, "onError: ");
                    return false;
                }

                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                    switch (i) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                            Log.d(TAG, "媒体信息视频渲染开始: ");
                            //Android属性动画(Animator)
                            //translationX和translationY	x轴和y轴的偏移量
                            //rotation、rotationX和rotationY	围绕支点旋转
                            //rotation(360) 围绕支点旋转
                            //scaleX和scaleY	缩放
                            //alpha	透明度
                            //x(5000).y(5000)  移动
                            //xBy(500).xBy(500) 移动
                            //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                            mPlay.animate().alpha(0f).setDuration(500).start();
                            mThumb.animate().alpha(0f).setDuration(1000).start();
                            return true;
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                            Log.d(TAG, "媒体信息视频跟踪滞后: ");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                            Log.d(TAG, " 媒体信息缓冲启动");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                            Log.d(TAG, "媒体信息缓冲结束");
                            return true;
                    }
                    return false;
                }

                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onPrepared: ");
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video != null) {
            video.pause();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (video != null) {
            video.stop();
            video.reset();
            video.release();
            video = null;
            list.clear();
        }
    }
}

