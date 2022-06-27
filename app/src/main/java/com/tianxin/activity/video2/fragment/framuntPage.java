package com.tianxin.activity.video2.fragment;

import static com.blankj.utilcode.util.ServiceUtils.stopService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.listener.Mediaplay;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.videolist;

import java.util.List;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class framuntPage extends BasFragment implements Mediaplay {
    private String TAG = framuntPage.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private PLVideoView video;
    private ImageView image, mPlay;
    private boolean isVisibleToUser;

    public static Fragment show(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        framuntPage framuntpage = new framuntPage();
        framuntpage.setArguments(args);
        return framuntpage;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        type = getArguments().getInt(Constants.TYPE, 1);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_tiktok_index2, null);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        this.isVisibleToUser = menuVisible;
        if (menuVisible) {
            Log.d(TAG, "播放视频------>: " + type);
            if (video != null && image != null) {
                video.start();
                image.animate().alpha(0f).setDuration(500).start();
                mPlay.animate().alpha(0f).setDuration(200).start();

            }
        } else {
            Log.d(TAG, "暂停播放:----->:" + type);
            if (video != null && image != null) {
                video.pause();
                image.animate().alpha(1f).setDuration(500).start();
            }
        }
    }

    @Override
    public void iniview() {

        //设置Adapter
        List<videolist> date = TiktokAdapter.date();
        list2.addAll(date);
        mAdapter = new TiktokAdapter(context, list2, TiktokAdapter.TYPE1);
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
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list.clear();
                mAdapter.notifyDataSetChanged();
                initData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                initData();
            }
        });

    }

    @Override
    public void initData() {
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(context, MyService.class));
        }

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    /**
     * 播放视频
     */
    @Override
    public void playVideo(View view) {
        if (view != null) {
            image = view.findViewById(R.id.mThumb);
            image.setOnClickListener(v -> play());
            mPlay = view.findViewById(R.id.mPlay);
            video = view.findViewById(R.id.video_view);
            video.setLooping(true);
            play();
            video.setOnInfoListener(new PLOnInfoListener() {
                @Override
                public void onInfo(int i, int i1, Object o) {
                    switch (i) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                            Log.d(TAG, "媒体信息视频渲染开始: ");
                            if (isVisibleToUser) {
                                //Android属性动画(Animator)
                                //translationX和translationY	x轴和y轴的偏移量
                                //rotation、rotationX和rotationY	围绕支点旋转
                                //rotation(360) 围绕支点旋转
                                //scaleX和scaleY	缩放
                                //alpha	透明度
                                //x(5000).y(5000)  移动
                                //xBy(500).xBy(500) 移动
                                //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                                mPlay.animate().alpha(0f).setDuration(200).start();
                                image.animate().alpha(0f).setDuration(500).start();
                            }
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                            Log.d(TAG, "媒体信息视频跟踪滞后: ");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                            Log.d(TAG, " 媒体信息缓冲启动");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                            Log.d(TAG, "媒体信息缓冲结束");
                            if (isVisibleToUser) {
                                mPlay.animate().alpha(0f).setDuration(200).start();
                                image.animate().alpha(0f).setDuration(1000).start();
                            }
                            break;
                    }
                }
            });
            if (isVisibleToUser && video.isPlaying()) {
                image.animate().alpha(0F).setDuration(1000).start();
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
            videoView.stop();
            videoView.stopPlayback();
            ImageView mThumb = view.findViewById(R.id.mThumb);
            mThumb.animate().alpha(1f).setDuration(200).start();
        }
    }

    @Override
    public long setProgress() {
        return 0;
    }

    /**
     * 暂停或播放
     */
    @Override
    public void play() {
        if (isVisibleToUser) {
            if (video.isPlaying()) {
                mPlay.animate().alpha(1f).setDuration(200).start();
                video.pause();
            } else {
                mPlay.animate().alpha(0f).setDuration(200).start();
                video.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + type);
        if (video != null && mPlay != null) {
            video.pause();
            mPlay.animate().alpha(1f).setDuration(200).start();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + type);
        if (isVisibleToUser) {
            if (video != null && mPlay != null) {
                video.start();
                mPlay.animate().alpha(0f).setDuration(200).start();
                image.animate().alpha(0f).setDuration(500).start();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + type);
        if (isVisibleToUser) {
            if (video != null && mPlay != null) {
                video.start();
                mPlay.animate().alpha(0f).setDuration(200).start();
                image.animate().alpha(0f).setDuration(500).start();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (video != null) {
            video.stop();
            video = null;
            list2.clear();
        }
    }
}
