package com.tianxin.Fragment.page6.fragment;

import static android.view.View.GONE;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.itemholder.tiokeholder2;
import com.tianxin.listener.Mediaplay;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 首页短视频滑动页
 */
public class pagevideo extends BasFragment implements Mediaplay {
    String TAG = pagevideo.class.getSimpleName();
    @BindView(R.id.hangup)
    LinearLayout hangup;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    TiktokAdapter tiktokAdapter;
    com.tianxin.activity.video2.widget.player video;
    ImageView image, play, circleimage;
    LinearLayout relayout3;
    SVGAImageView follow;
    SVGAParser parser;  //动画状态
    boolean isVisibleToUser;
    videolist videolist;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        type = arguments.getInt(Constants.TYPE);
        Log.d(TAG, "onAttach: " + type);

    }

    public static pagevideo show(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        pagevideo pagevideo = new pagevideo();
        pagevideo.setArguments(args);
        return pagevideo;

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        this.isVisibleToUser = menuVisible;
        if (menuVisible) {
            Log.d(TAG, "播放视频------>: " + type);
            if (video != null) {
                video.start();
                image.animate().alpha(0f).setDuration(500).start();
                play.animate().alpha(0f).setDuration(200).start();
            }
        } else {
            Log.d(TAG, "暂停播放:----->:" + type);
            if (video != null) {
                video.pause();
                image.animate().alpha(1f).setDuration(500).start();
            }
        }


    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home_one, null);
    }

    @Override
    public void iniview() {
        hangup.setBackgroundColor(Color.BLACK);
        parser = new SVGAParser(context);
        //分页page一次只能滑一页
        PagerManager mLayoutManager = new PagerManager(getContext(), OrientationHelper.VERTICAL);
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
        recyclerView.setAdapter(tiktokAdapter = new TiktokAdapter(context, list2, TiktokAdapter.TYPE8));
        recyclerView.addItemDecoration(new MyDecoration(context, LinearLayout.HORIZONTAL));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                loadinitData();
            }
        });
    }

    @Override
    public void initData() {
        if (list2.size() == 0) {
            loadinitData();
        }
    }

    void loadinitData() {
        totalPage++;
        datamodule.freevideo(totalPage, type, paymnets);
    }

    public void loadMoreData() {
        if (Config.isNetworkAvailable()) {
            totalPage = 0;
            list2.clear();
            tiktokAdapter.notifyDataSetChanged();
            loadinitData();
        } else {
            Toashow.show(getString(R.string.eorrfali2));
        }


    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        loadMoreData();
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public long setProgress() {
        return 0;
    }

    @Override
    public void play() {
        if (isVisibleToUser) {
            if (video.isPlaying()) {
                play.animate().alpha(1f).setDuration(200).start();
                video.pause();
                circleimage.clearAnimation();

            } else {
                play.animate().alpha(0f).setDuration(200).start();
                video.start();
                circleimageanimation(context, circleimage);
            }
        }
    }

    /**
     * 播放视频
     */
    @Override
    public void playVideo(View view) {
        if (view != null) {
            circleimage = view.findViewById(R.id.circleimageview);
            video = view.findViewById(R.id.video_view);
            play = view.findViewById(R.id.mPlay);
            play.animate().alpha(0f).start();
            image = view.findViewById(R.id.mThumb);
            follow = view.findViewById(R.id.follow);
            relayout3 = view.findViewById(R.id.relayout3);
            image.setOnClickListener(v -> play());
            play.setOnClickListener(v -> play());
            videolist = (com.tencent.opensource.model.videolist) video.getTag();
            video.setListener(new player.VideoPlayerListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    if (isVisibleToUser) {
                        Log.d(TAG, "播放完更新记录: ");
                        datamodule.senvideo(videolist, 1);
                        iMediaPlayer.start();
                    }
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
                            play.animate().alpha(0f).setDuration(500).start();
                            image.animate().alpha(0f).setDuration(1000).start();
                            circleimageanimation(context, circleimage);
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
                    if (!isVisibleToUser) {
                        iMediaPlayer.pause();
                        Log.d(TAG, "playVideo: 暂停播放视频");
                    }

                }
            });
            follow.setOnClickListener(v -> {
                parser.decodeFromAssets("user_transition_follow.svga", new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem);
                        //正在播放动画
                        if (follow != null) {
                            follow.setImageDrawable(drawable);
                            follow.startAnimation();
                            follow.setCallback(new SVGACallback() {
                                @Override
                                public void onPause() {
                                    Log.d(TAG, "onPause: ");
                                }

                                @Override
                                public void onFinished() {
                                    follow.stopAnimation();
                                    follow.setVisibility(GONE);
                                    datamodule.gofollowlist(videolist.getUserid(), paymnets);
                                }

                                @Override
                                public void onRepeat() {
                                    Log.d(TAG, "onRepeat: ");

                                }

                                @Override
                                public void onStep(int i, double v) {
                                    Log.d(TAG, "onStep: ");

                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            });
            if (!TextUtils.isEmpty(videolist.getUserid())) {
                if (videolist.getUserid().equals(userInfo.getUserId())) {
                    follow.setVisibility(GONE);
                } else {
                    //查询对方是否已被关注
                    datamodule.getfollowok(videolist.getUserid(), getPaymnets);
                }
            }
            relayout3.setOnClickListener(v -> tiokeholder2.fenxing(context, videolist, tiktokAdapter, list2));
        }
    }

    /**
     * 停止播放
     */
    @Override
    public void releaseVideo(View view) {
        if (view != null) {
            player videoView = view.findViewById(R.id.video_view);
            videoView.pause();
            videoView.stop();
            videoView.reset();
            videoView.release();
            ImageView mThumb = view.findViewById(R.id.mThumb);
            mThumb.animate().alpha(1f).setDuration(200).start();
            ImageView circleimage = view.findViewById(R.id.circleimageview);
            circleimage.clearAnimation();      //停止动画


        }
    }

    /**
     * 360动画转动走起
     *
     * @param image
     */
    public static void circleimageanimation(Context context, ImageView image) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.playmisc);
        image.startAnimation(animation);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            OnEorr();
            Toashow.show(getString(R.string.eorrfali2));
            if (totalPage > 1) {
                totalPage--;
            }

        }

        @Override
        public void onSuccess(Object object) {
            List<videolist> videlistList = (List<videolist>) object;
            list2.addAll(videlistList);
            tiktokAdapter.notifyDataSetChanged();
            if (videlistList.size() == 0) {
                totalPage--;
            }
            OnEorr();
        }

        @Override
        public void onFail(String msg) {
            if (totalPage > 1) {
                totalPage--;
            }
            OnEorr();
            Toashow.show(msg);

        }
    };

    private Paymnets getPaymnets = new Paymnets() {

        @Override
        public void payens() {
            follow.setVisibility(GONE);
            videolist.setFollow(1);
        }

        @Override
        public void onError() {
            follow.setVisibility(View.VISIBLE);
            videolist.setFollow(0);
        }

        @Override
        public void payonItemClick(String msg, int i) {
            Toashow.show(msg);
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + isVisibleToUser);
        if (isVisibleToUser) {
            if (video != null && play != null) {
                video.start();
                play.animate().alpha(0f).setDuration(200).start();
                image.animate().alpha(0f).setDuration(500).start();

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + isVisibleToUser);
        if (isVisibleToUser) {
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
        Log.d(TAG, "onPause: ");
        if (video != null && play != null) {
            video.pause();
            play.animate().alpha(1f).setDuration(200).start();


        }
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        if (video != null) {
            video.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (video != null) {
            video.release();
        }
    }

}
