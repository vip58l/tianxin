/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/23 0023
 */


package com.tianxin.activity.video;

import static android.view.View.GONE;
import static com.blankj.utilcode.util.StringUtils.getString;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.activity.activity_follow;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.video2.manager.PagerListener;
import com.tianxin.activity.video2.manager.PagerManager;
import com.tianxin.adapter.Tiktokholder.TiktokAdapter;
import com.tianxin.adapter.itemholder.tiokeholder2;
import com.tianxin.dialog.ddialog_jinbi;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.dialog.dialog_item_vip2;
import com.tianxin.Receiver.MyService;
import com.pili.pldroid.player.PLOnInfoListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.videolist;

import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 七牛播放器街拍视频页VIP收费模块
 */
public class videoijkplayer1 extends BasActivity2 {
    private final String TAG = videoijkplayer1.class.getName();
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private PLVideoView plMediaPlayer;
    private ImageView bgmplay, getPlay;
    private SVGAImageView follow, axicone;
    private SVGAParser parser;                      //动画状态
    private TextView viptype1title, viptype2title, title;  //动画状态
    private RelativeLayout show_vip;
    private videolist videolist;

    @Override
    protected int getview() {
        StatusBarUtil.mSystemUiVisibility(activity, true);
        Config.AsetctivityBLACK(activity);
        return R.layout.activity_video_play;
    }

    @Override
    public void iniview() {
        //在Activity中停止后台服务
        if (Config.isServiceRunning(Constants.className)) {
            stopService(new Intent(DemoApplication.instance(), MyService.class));
        }
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initData() {
        list = getinidate();
        parser = new SVGAParser(context);
        //设置Adapter
        mAdapter = new TiktokAdapter(context, list, TiktokAdapter.TYPE6);
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
        //recyclerView.smoothScrollToPosition(mCurrentItem);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list.clear();
                mAdapter.notifyDataSetChanged();
                loadgetdate();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishLoadMore(100/*,false*/);//传入false表示加载失败
                loadgetdate();
            }
        });
    }

    /**
     * 初始化接收的数据
     *
     * @return
     */
    private List<Object> getinidate() {
        List<item> itemList = (List<item>) getIntent().getSerializableExtra(Constants.dadelist);
        mCurrentItem = getIntent().getIntExtra(Constants.POSITION, 0);
        totalPage = getIntent().getIntExtra(Constants.TOTALPAGE, 0);
        TYPE = getIntent().getIntExtra(Constants.TYPE, 0);
        for (item item : itemList) {
            videolist video = (videolist) item.object;
            list.add(video);
        }
        return list;
    }

    /**
     * 播放视频
     */
    private void playVideo(View view) {
        if (view != null) {
            bgmplay = view.findViewById(R.id.bgmplay);
            getPlay = view.findViewById(R.id.play);
            follow = view.findViewById(R.id.follow);
            axicone = view.findViewById(R.id.axicone);
            plMediaPlayer = view.findViewById(R.id.video_view);
            plMediaPlayer.setLooping(true);
            plMediaPlayer.setAVOptions(videoijkplayer5.getoptions(context));
            videolist play = (com.tencent.opensource.model.videolist) plMediaPlayer.getTag();

            viptype1title = view.findViewById(R.id.viptype1title);
            viptype2title = view.findViewById(R.id.viptype2title);
            show_vip = view.findViewById(R.id.show_vip);
            videolist = (com.tencent.opensource.model.videolist) plMediaPlayer.getTag();
            bgmplay.setOnClickListener(v -> starplay(videolist));

            view.findViewById(R.id.icon).setOnClickListener(v -> {
                if (!TextUtils.isEmpty(videolist.getUserid())) {
                    activity_picenter.setActionactivity(context, videolist.getUserid());
                }
            });
            view.findViewById(R.id.relayout1).setOnClickListener(v -> {
                ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234));
                parser.decodeFromAssets("svag_dynamic_accost.svga", new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem);
                        //正在播放动画
                        if (axicone != null) {
                            axicone.setImageDrawable(drawable);
                            axicone.startAnimation();
                            axicone.setCallback(new SVGACallback() {
                                @Override
                                public void onPause() {
                                    Log.d(TAG, "onPause: ");

                                }

                                @Override
                                public void onFinished() {
                                    Log.d(TAG, "onFinished: ");
                                    follow.setVisibility(GONE);
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
            view.findViewById(R.id.relayout2).setOnClickListener(v -> dialog_Config.Pinglun(context, videolist.getId()));   //视频评论
            view.findViewById(R.id.relayout3).setOnClickListener(v -> tiokeholder2.fenxing(context, videolist, mAdapter, list));//分享
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
                                    Log.d(TAG, "onFinished: ");
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
            viptype2title.setOnClickListener(v -> activity_svip.starsetAction(context));
            show(videolist);
            starplay(videolist);
        }
    }

    /**
     * 停止播放
     */
    private void releaseVideo(View view) {
        if (view != null) {
            PLVideoView videoView = view.findViewById(R.id.video_view);
            videoView.stop();
            videoView.stopPlayback();
            ImageView mThumb = view.findViewById(R.id.bgmplay);
            mThumb.animate().alpha(1f).setDuration(200).start();
        }
    }

    /**
     * 执行播放视频
     *
     * @param videolist
     */
    private void starplay(videolist videolist) {
        if (plMediaPlayer != null) {
            //本地数据库找到该条视频说明已经付费
            if (MyOpenhelper.getOpenhelper().Query(MyOpenhelper.videolist, Integer.parseInt(videolist.getId()), userInfo.getUserId(), 1).size() > 0) {
                show_vip.animate().alpha(0f).setDuration(100).start();
                videoplay();
                return;
            }

            //收费视频类型 VIP会员付费视频 普通会员无权浏览
            //video.getType()   收费会员
            //userInfo.getVip() 会员不是VIP
            //!userInfo.getUserId().equals(video.getUserid()) 不是自己的视频
            //video.getJinbi() > 0 //金币大于0的
            if (videolist.getJinbi() > 0 || videolist.getType() == Constants.TENCENT) {
                if (userInfo.getVip() == 0 && !userInfo.getUserId().equals(videolist.getUserid())) {
                    dialogShowVideo(videolist); //播放视频提示升级VIP会员
                    return;
                } else {
                    show_vip.animate().alpha(0f).setDuration(100).start();
                }
            }

            videoplay();
        }
    }

    /**
     * 播放视频提示升级VIP会员
     * 弹出升级VIP提示
     */
    private void dialogShowVideo(videolist video) {
        show_vip.setVisibility(View.VISIBLE);
        viptype1title.setText(R.string.svip1);
        viptype2title.setText(R.string.svip2);
        dialog_item_vip2.dialogitemvip(context, buypaymnets, video);
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

    /**
     * 联网加载更多数据
     */
    private void loadgetdate() {
        totalPage++;
        datamodule.sbaseadapter(totalPage, TYPE, paymnets);
    }

    private final Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {
            totalPage--;
            Toashow.show(getString(R.string.eorrfali3));

        }

        @Override
        public void onSuccess(Object object) {
            List<videolist> data = (List<videolist>) object;
            if (data.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    Toashow.show(getString(R.string.eorrtext));
                }
                return;
            }
            //与之前的旧数据合并
            list.addAll(data);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void isNetworkAvailable() {
            totalPage--;
            Toashow.show(getString(R.string.eorrfali2));
        }
    };

    private void starmainActivity(int TYPE) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("RESULT", TYPE);
        startActivity(intent);
        overridePendingTransition(R.anim.fade, R.anim.hold);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (plMediaPlayer != null) {
            starplay(videolist);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        if (plMediaPlayer != null) {
            plMediaPlayer.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (plMediaPlayer != null) {
            plMediaPlayer.pause();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (plMediaPlayer != null) {
            plMediaPlayer.stop();
            plMediaPlayer.stopPlayback();
            plMediaPlayer = null;
            list.clear();
        }
    }

    /**
     * 检查是否已关注她
     *
     * @param video
     */
    private void show(videolist video) {
        if (!TextUtils.isEmpty(video.getUserid())) {
            if (video.getUserid().equals(userInfo.getUserId())) {
                follow.setVisibility(GONE);
            } else {
                //查询对方是否已被关注
                datamodule.getfollowok(video.getUserid(), paymnets4);
            }
        } else {
            follow.setVisibility(GONE);
        }
    }

    /**
     * 点击购买提示回调
     */
    private Paymnets buypaymnets = new Paymnets() {
        @Override
        public void onClick() {
            activity_svip.starsetAction(context); //传到购买VIP会员
        }

        @Override
        public void onSuccess() {
            //支付金币
            ddialog_jinbi.myshow(context, paymnets2, videolist);
        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void payens() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toashow.show(context.getString(R.string.tm94));
                    //转到个人主页
                    activity_picenter.setActionactivity(context, videolist.getUserid());
                }
            });
        }

        @Override
        public void onError() {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toashow.show("已取消息关注");
                }
            });

        }

    };

    /**
     * 支付金币成功回调
     */
    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void onFail() {
            Toashow.show(context.getString(R.string.tv_paylay));
        }

        @Override
        public void onSuccess() {
            if (videolist != null) {
                datamodule.jinbi(paymnets3, videolist);
            }
        }
    };

    /**
     * 支付金币成功回调
     */
    private Paymnets paymnets3 = new Paymnets() {
        @Override
        public void onFail() {
            Toashow.show(context.getString(R.string.eorrfali3));
        }

        @Override
        public void isNetworkAvailable() {
            Toashow.show(context.getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess() {
            //保存支付成功视频标识ID在本地中 更换手机需要重新支付金币
            if (MyOpenhelper.getOpenhelper().Query(MyOpenhelper.videolist, Integer.parseInt(videolist.getId()), userInfo.getUserId(), 1).size() == 0) {
                MyOpenhelper.getOpenhelper().insert1(MyOpenhelper.videolist, userInfo.getUserId(), Integer.parseInt(videolist.getId()), 1);
                show_vip.animate().alpha(0f).setDuration(100).start();
            }
            videoplay();
        }

        @Override
        public void onFail(String msg) {
            Toashow.show(msg);
        }
    };

    private Paymnets paymnets4 = new Paymnets() {
        @Override
        public void payens() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    follow.setVisibility(GONE);
                    Log.d(TAG, "已关注");
                    videolist.setFollow(1);
                }
            });
        }

        @Override
        public void onError() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    follow.setVisibility(View.VISIBLE);
                    Log.d(TAG, "未关注");
                    videolist.setFollow(0);
                }
            });

        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }


    };

    private void videoplay() {
        if (plMediaPlayer.isPlaying()) {
            plMediaPlayer.pause();
            getPlay.animate().alpha(1f).setDuration(500).start();
        } else {
            plMediaPlayer.start();
            getPlay.animate().alpha(0f).setDuration(500).start();
            plMediaPlayer.setOnInfoListener(new PLOnInfoListener() {
                @Override
                public void onInfo(int i, int i1, Object o) {
                    switch (i) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                            Log.d(TAG, "媒体信息视频渲染开始: ");
                            getPlay.animate().alpha(0f).setDuration(200).start();
                            bgmplay.animate().alpha(0f).setDuration(500).start();
                            break;
                        case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                            Log.d(TAG, "媒体信息视频跟踪滞后: ");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                            Log.d(TAG, " 媒体信息缓冲启动");
                            break;
                        case IMediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                            Log.d(TAG, "媒体信息缓冲结束");
                            break;
                    }
                }
            });
        }
    }


}
