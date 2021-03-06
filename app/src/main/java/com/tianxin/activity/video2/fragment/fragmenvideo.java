package com.tianxin.activity.video2.fragment;

import static com.blankj.utilcode.util.ServiceUtils.stopService;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.Config;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.listener.Mediaplay;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tencent.opensource.model.videolist;

import java.util.List;

import butterknife.BindView;


public class fragmenvideo extends BasFragment implements Mediaplay {
    String TAG = fragmenvideo.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private final static String tTYPE = "type";
    VideoView video;
    ImageView image, play;
    boolean isVisibleToUser;

    public static Fragment show(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(tTYPE, type);
        fragmenvideo fragment = new fragmenvideo();
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

        //???????????????1
        //PagerSnapHelpervideo(getView());

        //???????????????2
        PagerLayoutManagervideo(getView());
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

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        this.isVisibleToUser = menuVisible;
        if (menuVisible) {
            if (video != null && image != null) {
                Log.d(TAG, "????????????------>: " + type);
                video.start();
                image.animate().alpha(0f).setDuration(500).start();
                play.animate().alpha(0f).setDuration(200).start();
            }
        } else {
            if (video != null && image != null) {
                Log.d(TAG, "????????????:----->:" + type);
                video.pause();
                image.animate().alpha(1f).setDuration(500).start();
            }
        }
    }

    /**
     * ????????????1
     *
     * @param view
     */
    @Override
    public void PagerSnapHelpervideo(View view) {
        //??????Adapter
        List<videolist> date = TiktokAdapter.date();
        list2.addAll(date);
        mAdapter = new TiktokAdapter(context, list2, TiktokAdapter.TYPE4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerview = view.findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(mAdapter);

        //??????page?????????????????????
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerview);

        //??????????????????????????????RecyclerView???????????????????????????????????????????????????
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://????????????
                        View view = snapHelper.findSnapView(layoutManager);
                        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
                        if (viewHolder != null) {
                            Log.d(TAG, "???????????????????????????: ");
                            playVideo(viewHolder.itemView);
                        }
                        break;

                    case RecyclerView.SCROLL_STATE_DRAGGING://??????
                        Log.d(TAG, "SCROLL_STATE_DRAGGING: ");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://???????????????????????????
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
         * ??????????????????item????????????????????????
         */
        recyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            /**
             * itemView??????Window
             * ????????????View?????????
             */
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (recyclerview.getChildCount() == 1) {
                    Log.d(TAG, "onChildViewAttachedToWindow: ");
                    playVideo(view);
                }
            }

            /**
             * ????????????View?????????
             *itemView??????Window
             */
            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Log.d(TAG, "onChildViewDetachedFromWindow: ");
                releaseVideo(view);
            }
        });
    }

    /**
     * ????????????2
     *
     * @param view
     */
    @Override
    public void PagerLayoutManagervideo(View view) {
        //??????Adapter
        List<videolist> date = TiktokAdapter.date();
        list2.addAll(date);
        mAdapter = new TiktokAdapter(context, list2, TiktokAdapter.TYPE5);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(mAdapter);

        //??????page?????????????????????
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
     * ????????????
     */
    @Override
    public void playVideo(View view) {
        if (view != null) {
            video = view.findViewById(R.id.video_view);
            play = view.findViewById(R.id.play);
            play.animate().alpha(0f).start();
            image = view.findViewById(R.id.image);
            /**
             * ??????????????????
             */
            image.setOnClickListener(v -> play());
            play();
            video.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //??????????????????????????????
                            Log.d(TAG, "??????????????????????????????: ");
                            if (isVisibleToUser) {
                                //Android????????????(Animator)
                                //translationX???translationY	x??????y???????????????
                                //rotation???rotationX???rotationY	??????????????????
                                //rotation(360) ??????????????????
                                //scaleX???scaleY	??????
                                //alpha	?????????
                                //x(5000).y(5000)  ??????
                                //xBy(500).xBy(500) ??????
                                //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                                play.animate().alpha(0f).setDuration(500).start();
                                image.animate().alpha(0f).setDuration(1000).start();
                            }
                            return true;
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //??????????????????????????????
                            Log.d(TAG, "??????????????????????????????: ");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:       //????????????????????????
                            Log.d(TAG, " ????????????????????????");
                            return true;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:         //????????????????????????
                            Log.d(TAG, "????????????????????????");
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * ????????????
     */
    @Override
    public void releaseVideo(View view) {
        if (view != null) {
            VideoView mVideoView = view.findViewById(R.id.video_view);
            ImageView image = view.findViewById(R.id.image);
            image.animate().alpha(1f).setDuration(200).start();
            try {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mVideoView.stopPlayback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long setProgress() {
        return 0;
    }

    /**
     * ????????????????????????
     */
    @Override
    public void play() {
        if (isVisibleToUser) {
            if (video.isPlaying()) {
                video.pause();
                play.animate().alpha(0.3f).setDuration(500).start();
            } else {
                video.start();
                play.animate().alpha(0f).setDuration(500).start();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + type);
        if (isVisibleToUser) {
            if (video != null && play != null) {
                video.start();
                play.animate().alpha(0f).setDuration(200).start();
                image.animate().alpha(0f).setDuration(500).start();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
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
        Log.d(TAG, "onPause: " + type);
        if (video != null && play != null) {
            video.pause();
            play.animate().alpha(1f).setDuration(200).start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        if (video != null) {
            video.pause();
            video.stopPlayback();
            video = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + type);
    }

}
