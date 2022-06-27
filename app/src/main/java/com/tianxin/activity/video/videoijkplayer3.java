package com.tianxin.activity.video;

import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.activity.activity_follow;
import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.JsonUitl;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.videolist;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 测试播放器
 */
public class videoijkplayer3 extends BasActivity2 {
    private static final String TAG = videoijkplayer3.class.getSimpleName();
    @BindView(R.id.viewPager2)
    ViewPager2 viewPager2;
    private PLVideoView plVideoView;

    @Override
    protected int getview() {
        return R.layout.activity_video_page;
    }

    @Override
    public void iniview() {
        //在Activity中停止Service
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(context, MyService.class));
        }
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    public void initData() {
        //plVideoView = new PLVideoView(context);
        //plVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        //plVideoView.setLooping(true);

        list = mygetIntent();
        viewPager2.setAdapter(radapter = new Radapter(context, list, Radapter.Video_page));
        viewPager2.setCurrentItem(mCurrentItem);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentItem == 0) {
                    startPlay(mCurrentItem);
                }
            }
        });
    }

    /**
     * 获取列表数据
     *
     * @return
     */
    private List<Object> mygetIntent() {
        Intent intent = getIntent();
        String json = intent.getStringExtra(Constants.JSON);
        String getjson = getjson(intent);
        json = TextUtils.isEmpty(getjson) ? json : getjson;
        List<videolist> video = JsonUitl.stringToList(json, videolist.class);
        mCurrentItem = intent.getIntExtra(Constants.POSITION, -1);
        totalPage = intent.getIntExtra(Constants.TOTALPAGE, -1);
        TYPE = intent.getIntExtra(Constants.TYPE, -1);
        list.addAll(video);
        return list;
    }

    //为了兼容其他方法传递数据，所以但单处理
    private String getjson(Intent intent) {
        List<item> items = (List<item>) intent.getSerializableExtra(Constants.dadelist);
        if (items.size() == 0) {
            return "";

        }

        List<videolist> list = new ArrayList<>();
        for (item item : items) {
            videolist video = (videolist) item.object;
            list.add(video);
        }
        String json = new Gson().toJson(list);
        return json;

    }

    /**
     * 设置播放处理
     */
    private void startPlay(int position) {
        mCurrentItem = position;
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
}