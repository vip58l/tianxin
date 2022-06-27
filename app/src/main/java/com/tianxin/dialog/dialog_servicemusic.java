/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/1 0001
 */


package com.tianxin.dialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianxin.adapter.itemdecoration.RecycleViewDivider;
import com.tianxin.adapter.Tiktokholder.dialogadapter;
import com.tianxin.Module.api.BaseClass;
import com.tianxin.getHandler.PostModule;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.Receiver.MyService;
import com.tianxin.Module.api.misc;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.Util.log;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.widget.EndLessOnScrollListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 采集音乐来源于
 * https://www.9ku.com/music/t_allhits.htm
 */
public class dialog_servicemusic extends BottomSheetDialog {
    String TAG = dialog_servicemusic.class.getSimpleName();
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.playicon)
    ImageView playicon;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    dialogadapter adapter;
    List<misc> list = new ArrayList<>();
    Handler handler = new Handlers();
    MyService.MyBinder mediaPlayer;
    MyServiceConnection myServiceConnection = new MyServiceConnection();
    private Paymnets paymnets;
    private int totpage;
    private BaseClass baseClass;


    public dialog_servicemusic(Context context) {
        super(context, R.style.fei_style_dialog);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.servicemusic, null);
        setContentView(inflate);
        ButterKnife.bind(this);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inflate.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 1.5f);
        inflate.setLayoutParams(params);

        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getContext().getResources().getColor(R.color.home3));
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter = new dialogadapter(getContext(), list));
        recyclerview.addItemDecoration(recycleViewDivider);
        recyclerview.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });
        adapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int result) {
                if (list.size() > 0) {
                    misc misc = list.get(result);
                    mediaPlayer.setmisc(misc);
                    mediaPlayer.setPath(misc.getUrl());
                    handler.sendEmptyMessageDelayed(Config.sussess, 1000);
                    if (paymnets != null) {
                        paymnets.onSuccess();
                    }


                }
            }
        });

        starmiscpalay();
        show();
        loadMoreData();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public static dialog_servicemusic playmisc(Context context) {
        return new dialog_servicemusic(context);
    }

    public static void playmisc(Context context, Paymnets p) {
        dialog_servicemusic servicemusic = new dialog_servicemusic(context);
        servicemusic.setPaymnets(p);
        servicemusic.show();
    }

    @OnClick({R.id.playicon})
    public void OnClick(View v) {
        if (paymnets != null) {
            paymnets.onSuccess();
        }

        switch (v.getId()) {
            case R.id.playicon:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playicon.setImageResource(R.mipmap.icon_sy_topbf);
                    handler.removeMessages(Config.sussess);
                } else {
                    mediaPlayer.start();
                    playicon.setImageResource(R.mipmap.puase);
                    handler.sendEmptyMessage(Config.sussess);

                }

                break;
        }
    }

    /**
     * 启动服务可以让服务在后台一直运行
     */
    public void starmiscpalay() {
        Intent intent = new Intent(getContext(), MyService.class);
        //启动服务运行
        DemoApplication.instance().startService(intent);
        //绑定到前台操作
        DemoApplication.instance().bindService(intent, myServiceConnection, BIND_AUTO_CREATE);
    }

    /**
     * 绑定到前台操作
     */
    public class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            inidateiBinder((MyService.MyBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    }

    /**
     * 初始化绑定数据进行播放操作前台操作
     *
     * @param mediaPlay
     */
    private void inidateiBinder(MyService.MyBinder mediaPlay) {
        this.mediaPlayer = mediaPlay;
        //读取上次打开读内容
        misc misc = mediaPlayer.getmisc();
        if (misc != null) {
            String title = misc.getTitle();
            String url = misc.getUrl();
            String tag = misc.getTag();
            String bg = misc.getPicture();
        }
        if (mediaPlayer.isPlaying()) {
            handler.sendEmptyMessage(Config.sussess);
            playicon.setImageResource(R.mipmap.ic_action_pause);
        }

        seekbar.setMax(mediaPlayer.getmyDuration()); //进度条最大时间
        seekbar.setProgress(mediaPlayer.getcurrentposition()); //当前进度
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    String star = Config.generateTime(progress);
                    String end = Config.generateTime(seekBar.getMax());
                    time.setText(String.format("%s/%s", star, end));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(Config.sussess);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                handler.sendEmptyMessage(Config.sussess);
            }
        });
        String star = Config.generateTime(mediaPlayer.getcurrentposition());
        String end = Config.generateTime(mediaPlayer.getmyDuration());
        time.setText(String.format("%s/%s", star, end));

        MediaPlayer Player = mediaPlayer.getMediaPlayer();
        Player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                if (mp.isPlaying()) {
                    handler.sendEmptyMessage(Config.sussess); //发通知
                }
            }
        });
        Player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekbar.setProgress(0);
                time.setText("00:00");

            }
        });
        Player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                //myBinder.reset();
                return true;
            }
        });
        Player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                log.d("setOnInfoListener");
                return false;
            }
        });
        Player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                log.d("setOnSeekCompleteListener");
            }
        });
    }


    /**
     * 监听播放进度
     */
    private class Handlers extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (handler.hasMessages(Config.sussess)) ;
            {
                handler.removeMessages(Config.sussess); //移除未发送的通知
            }

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekbar.setProgress(mediaPlayer.getcurrentposition());
                seekbar.setMax(mediaPlayer.getmyDuration());
                String star = Config.generateTime(mediaPlayer.getcurrentposition());
                String end = Config.generateTime(mediaPlayer.getmyDuration());
                time.setText(String.format("%s/%s", star, end));
                handler.sendEmptyMessageDelayed(Config.sussess, 1000);
            }
            if (mediaPlayer != null) {
                playicon.setImageResource(mediaPlayer.isPlaying() ? R.mipmap.puase : R.mipmap.icon_sy_topbf);
            }

        }
    }

    /**
     * 联网获取音乐数据
     * https://blog.csdn.net/weixin_30826095/article/details/95779710?utm_term=gson%E6%B3%9B%E5%9E%8B%E8%A7%A3%E6%9E%90&utm_medium=distribute.pc_aggpage_search_result.none-task-blog-2~all~sobaiduweb~default-5-95779710&spm=3001.4430
     */
    private void loadMoreData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getContext().getResources().getString(R.string.eorrfali2));
            return;
        }
        totpage++;
        if (baseClass != null && totpage > baseClass.getLast_page()) {
            totpage--;
            Toashow.show(getContext().getString(R.string.eorrtext));
            return;
        }
        PostModule.getModule(Webrowse.music + "?page=" + totpage, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void success(String date) {
                //复杂的泛型：T
                Type objectType = new TypeToken<BaseClass<misc>>() {
                }.getType();
                baseClass = new Gson().fromJson(date, objectType);
                List<misc> data = baseClass.getData();
                list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fall(int code) {

            }
        });

    }

    @Override
    public void dismiss() {
        super.dismiss();
        DemoApplication.instance().unbindService(myServiceConnection);
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 使用Gson来出来JSON，result为json字符串
     */
    public void Gsondemo(String result) {
        Gson gson = new Gson();
        //1简单对象我们传入对象Class来将JSON字符串转为对象
        misc misc = gson.fromJson(result, misc.class);
        //2复杂的泛型：复杂的泛型需要构建TypeToken
        Type type = new TypeToken<BaseClass<misc>>() {
        }.getType();
        BaseClass<misc> pageList = gson.fromJson(result, type);


    }
}
