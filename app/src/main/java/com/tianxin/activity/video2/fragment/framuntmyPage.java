package com.tianxin.activity.video2.fragment;

import static com.blankj.utilcode.util.ServiceUtils.stopService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.McallBack;
import com.tianxin.R;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.adapter.itemholder.tiokeholder2;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Mediaplay;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.videolist;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * player播放器
 */
public class framuntmyPage extends BasFragment implements Mediaplay, Paymnets {
    private String TAG = framuntmyPage.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private player video;
    private ImageView image, mPlay, circleimage;
    private LinearLayout relayout3;
    private videolist mplay;
    private SeekBar seekBar;
    private TextView daytetime;
    private static int Currentpage = 1;
    private mhandler mhandler = new mhandler();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        mCurrentItem = arguments.getInt(Constants.POSITION, 0);
        type = arguments.getInt(Constants.TYPE, 0);
        totalPage = arguments.getInt(Constants.TOTALPAGE, 0);
        Serializable serializable = arguments.getSerializable(Constants.JSON);
        List<videolist> mlist = (List<videolist>) serializable;
        list2.addAll(mlist);

    }

    public static Fragment show(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        framuntmyPage framuntpage = new framuntmyPage();
        framuntpage.setArguments(args);
        return framuntpage;
    }

    public static Fragment show(Bundle bundle) {
        framuntmyPage framuntpage = new framuntmyPage();
        framuntpage.setArguments(bundle);
        return framuntpage;
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_tiktok_index2, null);
    }

    @Override
    public void iniview() {

        //设置Adapter
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
                Currentpage = position;
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
        // 滑动到顶部，具有滚动效果
        //recyclerView.smoothScrollToPosition(mCurrentItem);

        //recyclerView.setFocusableInTouchMode(false);
        //recyclerView.requestFocus();

        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list.clear();
                mAdapter.notifyDataSetChanged();
                toinitData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
                toinitData();
            }
        });

        //监听播放完成回调自动滑动
        mAdapter.setCallback(new Callback() {
            @Override
            public void onSuccess(String msg) {

            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Currentpage++;
                if (Currentpage < list2.size()) {
                    //具有滚动效果
                    recyclerView.smoothScrollToPosition(Currentpage);
                }
            }

            @Override
            public void onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                mhandler.sendEmptyMessage(OPEN_FILE_CODE);
            }
        });

        if (list2.size() == 1 || list2.size() == 0) {
            toinitData();
        }

    }

    @Override
    public void initData() {
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(context, MyService.class));
        }
    }

    /**
     * 更多加载视频
     */
    private void toinitData() {
        totalPage++;
        switch (type) {
            case 0:
                //我相册视频
                datamodule.getvidel(totalPage, this);
                break;
            case 1:
                //其他人主页视频
                videolist user = (videolist) list2.get(0);
                datamodule.totalpage(user.getUserid(), totalPage, this);
                break;
            case 2:
                //所有人朋友圈视频
                datamodule.playvideotrend(totalPage, this);
                break;
            default:
                break;
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

    private class mhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OPEN_FILE_CODE:
                    if (video == null || !video.isPlaying()) {
                        return;
                    }
                    long pos = setProgress();
                    if (pos == -1) {
                        return;
                    }
                    msg = obtainMessage(OPEN_FILE_CODE);
                    sendMessageDelayed(msg, 1000);
                    break;
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
            image = view.findViewById(R.id.mThumb);
            seekBar = view.findViewById(R.id.seekBar);
            daytetime = view.findViewById(R.id.daytetime);
            mPlay = view.findViewById(R.id.mPlay);
            relayout3 = view.findViewById(R.id.relayout3);
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        daytetime.setText(String.format("%s/%s", Config.generateTime(seekBar.getProgress()), Config.generateTime(seekBar.getMax())));
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    daytetime.setVisibility(View.VISIBLE);
                    mhandler.removeMessages(OPEN_FILE_CODE);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    daytetime.setVisibility(View.GONE);
                    video.seekTo(progress);
                    mhandler.sendEmptyMessage(OPEN_FILE_CODE);
                }

            });
            image.setOnClickListener(v -> play());
            mPlay.setOnClickListener(v -> play());
            video = view.findViewById(R.id.video_view);
            mhandler.sendEmptyMessageDelayed(OPEN_FILE_CODE, 1000);


            //控制非VIP会员 不允许播放
            int position = (int) image.getTag();
            mplay = (videolist) video.getTag();
            relayout3.setOnClickListener(v -> tiokeholder2.fenxing(context, mplay, mAdapter, list2));
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
            ImageView circleimage = view.findViewById(R.id.circleimageview);
            circleimage.clearAnimation();      //停止动画
            ImageView mThumb = view.findViewById(R.id.mThumb);
            mThumb.animate().alpha(1f).setDuration(200).start();
            mhandler.removeMessages(OPEN_FILE_CODE);
        }
    }

    @Override
    public void play() {
        if (video.isPlaying()) {
            UsermPause();
        } else {
            //type = 1 vip = 0 not userid
            if (mplay.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0 && !userInfo.getUserId().equals(mplay.getUserid())) {
                UsermPause();
                dialog_msg_svip.dialogmsgsvip(context, getString(R.string.dialog_msg_svip1), getString(R.string.tv_msg228), getString(R.string.tv_msg153), new Paymnets() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(context, activity_svip.class));
                    }

                    @Override
                    public void onRefresh() {
                        McallBack.starsetAction(context);
                    }

                });
            } else {
                videomPlay();
            }
        }
    }

    private void UsermPause() {
        mPlay.animate().alpha(1f).setDuration(200).start();
        video.pause();
        circleimage.clearAnimation();
        mhandler.removeMessages(OPEN_FILE_CODE);
    }

    /**
     * 播放进度
     *
     * @return
     */
    @Override
    public long setProgress() {
        long position = video.getCurrentPosition();
        long duration = video.getDuration();
        seekBar.setProgress((int) position);
        daytetime.setText(String.format("%s/%s", Config.generateTime(position), Config.generateTime(duration)));
        return position;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (video != null && mPlay != null) {
            video.pause();
            mPlay.animate().alpha(1f).setDuration(200).start();
            mhandler.removeMessages(OPEN_FILE_CODE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (video != null && mPlay != null) {
            if (mplay.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0 && !userInfo.getUserId().equals(mplay.getUserid())) {
                return;
            }
            video.start();
            mPlay.animate().alpha(0f).setDuration(200).start();
            image.animate().alpha(0f).setDuration(500).start();
            mhandler.sendEmptyMessageDelayed(OPEN_FILE_CODE, 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (video != null) {
            video.stop();
            video.release();
            video = null;
            list2.clear();
            mhandler.removeMessages(OPEN_FILE_CODE);
        }
    }

    @Override
    public void isNetworkAvailable() {
        Toashow.show(getString(R.string.eorrfali2));
        totalPage--;
    }

    @Override
    public void onFail() {
        Toashow.show(getString(R.string.eorrfali3));
        totalPage--;
    }

    @Override
    public void onSuccess(Object object) {
        List<videolist> mlist = (List<videolist>) object;
        list2.addAll(mlist);
        mAdapter.notifyDataSetChanged();
        if (totalPage == 1) {
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.scrollToPosition(0);
        }

    }

    @Override
    public void onSuccess(String msg) {
        totalPage--;
        Toashow.show(msg);
    }

    @Override
    public void onFail(String msg) {
        totalPage--;
        Toashow.show(msg);
    }

    private void circleimageanimation(ImageView circleimageview) {
        //360动画转动走起
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.playmisc);
        circleimageview.startAnimation(animation); //播放动画
    }

    /**
     * 点击播放视频
     */
    private void videomPlay() {
        mPlay.animate().alpha(0f).setDuration(200).start();
        video.start();
        circleimageanimation(circleimage);
        mhandler.sendEmptyMessageDelayed(OPEN_FILE_CODE, 1000);
    }
}
