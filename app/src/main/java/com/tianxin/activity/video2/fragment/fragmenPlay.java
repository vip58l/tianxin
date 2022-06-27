package com.tianxin.activity.video2.fragment;

import static com.blankj.utilcode.util.ServiceUtils.stopService;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.listener.Mediaplay;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.opensource.model.videolist;

import java.util.List;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;


public class fragmenPlay extends BasFragment implements Mediaplay {
    String TAG = fragmenPlay.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private final static String tTYPE = "type";
    com.tianxin.activity.video2.widget.player video;
    ImageView image, play;
    boolean isVisibleToUser;
    long currentPosition;

    public static Fragment show(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(tTYPE, type);
        fragmenPlay fragment = new fragmenPlay();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        type = getArguments().getInt(tTYPE, 1);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_tiktok_index2, null);
    }

    @Override
    public void iniview() {
        //初始化滑动1
        //PagerSnapHelpervideo(view);
        //初始化滑动2
        PagerLayoutManagervideo(getView());
    }

    @Override
    public void initData() {

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

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        this.isVisibleToUser = menuVisible;
        if (menuVisible) {
            Log.d(TAG, "播放视频------>: " + type);
            if (video != null && image != null) {
                video.setPath((String) video.getTag());
                video.start();
                image.animate().alpha(0f).setDuration(500).start();
                play.animate().alpha(0f).setDuration(200).start();
            }
        } else {
            Log.d(TAG, "暂停播放:----->:" + type);
            if (video != null && image != null) {
                video.pause();
                image.animate().alpha(1f).setDuration(500).start();
            }
        }
    }

    /**
     * 滑动播放1
     *
     * @param view
     */
    @Override
    public void PagerSnapHelpervideo(View view) {
        //设置Adapter
        List<videolist> date = TiktokAdapter.date();
        list2.addAll(date);
        mAdapter = new TiktokAdapter(context, list2, TiktokAdapter.TYPE4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(mAdapter);

        //分页page一次只能滑一页
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerview);

        //这样我们就只需要监听RecyclerView的滚动，然后就可以实现我们的逻辑了
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        View view = snapHelper.findSnapView(layoutManager);
                        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                        if (viewHolder != null) {
                            Log.d(TAG, "启动想要播放的视频: ");
                            playVideo(viewHolder.itemView);
                        }
                        break;

                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        Log.d(TAG, "SCROLL_STATE_DRAGGING: ");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://要移动到最后位置时
                        Log.d(TAG, "SCROLL_STATE_SETTLING: ");
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        /**
         * 当正在播放的item滑出界面时会回调
         */
        recyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            /**
             * itemView依赖Window
             * 当添加子View时回调
             */
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (recyclerview.getChildCount() == 1) {
                    Log.d(TAG, "onChildViewAttachedToWindow: ");
                    playVideo(view);
                }
            }

            /**
             * 当移除子View时回调
             *itemView脱离Window
             */
            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Log.d(TAG, "onChildViewDetachedFromWindow: ");
                releaseVideo(view);
            }
        });
    }

    /**
     * 滑动播放2
     *
     * @param view
     */
    @Override
    public void PagerLayoutManagervideo(View view) {
        //设置Adapter
        List<videolist> date = TiktokAdapter.date();
        list2.addAll(date);
        mAdapter = new TiktokAdapter(context, list2, TiktokAdapter.TYPE4);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mAdapter);

        //分页page一次只能滑一页
        PagerManager mLayoutManager = new PagerManager(getContext(), OrientationHelper.VERTICAL);
        recyclerview.setLayoutManager(mLayoutManager);
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
    }

    /**
     * 播放视频
     */
    @Override
    public void playVideo(View view) {
        if (view != null) {
            play = view.findViewById(R.id.play);
            play.animate().alpha(0f).start();
            video = view.findViewById(R.id.video_view);
            image = view.findViewById(R.id.image);
            /**
             * 点击事件监听
             */
            image.setOnClickListener(v -> play());
            play();
            video.setListener(new player.VideoPlayerListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    Log.d(TAG, "onCompletion: ");
                }

                @Override
                public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {

                    return false;
                }

                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                    switch (i) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
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
                                play.animate().alpha(0f).setDuration(500).start();
                                image.animate().alpha(0f).setDuration(1000).start();
                            }
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

                }
            });
        }
    }

    /**
     * 停止播放
     */
    @Override
    public void releaseVideo(View view) {
        if (view != null) {
            com.tianxin.activity.video2.widget.player mVideoView = view.findViewById(R.id.video_view);
            ImageView image = view.findViewById(R.id.image);
            image.animate().alpha(1f).setDuration(200).start();
            if (mVideoView.isPlaying()) {
                mVideoView.stop();
                mVideoView.reset();
                mVideoView.release();
            }
        }
    }

    @Override
    public long setProgress() {
        return 0;
    }

    /**
     * 播放或暂暂停播放
     */
    @Override
    public void play() {
        if (isVisibleToUser) {
            Log.d(TAG, "play: 可见状态" + isVisibleToUser + "----" + type);
            video.setPath((String) video.getTag());
            if (video.isPlaying()) {
                video.pause();
                play.animate().alpha(0.3f).setDuration(500).start();
            } else {
                video.start();
                play.animate().alpha(0f).setDuration(500).start();
                image.animate().alpha(0f).setDuration(500).start();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + type);
        if (isVisibleToUser) {
            Log.d(TAG, "onStart: " + isVisibleToUser);
            if (video != null && play != null) {
                video.start();
                play.animate().alpha(0f).setDuration(200).start();
                image.animate().alpha(0f).setDuration(500).start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + type);
        if (video != null && play != null) {
            video.pause();
            play.animate().alpha(1f).setDuration(200).start();
            currentPosition = video.getCurrentPosition();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if (video != null) {
            video.pause();
            video.stop();
            video.release();
            video = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + type);
    }

}
