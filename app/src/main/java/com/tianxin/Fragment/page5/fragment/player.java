package com.tianxin.Fragment.page5.fragment;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasFragment;
import com.tencent.opensource.model.item;
import com.tianxin.R;
import com.tianxin.dialog.Dialog_bottom;
import com.tianxin.getlist.listSmallVideos;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoView;



import java.io.Serializable;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 增加播放展示VIEW
 */
public class player extends BasFragment {
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.videoview)
    PLVideoView videoview;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.zbbg)
    ImageView zbbg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mhde_tv_title)
    TextView mhde_tv_title;

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    listSmallVideos video;
    //Fragment的View加载完毕的标记
    private boolean isViewCreated = false;
    //Fragment对用户可见的标记
    private final boolean isUIVisible = false;
    private final boolean isplay = true;
    private int type; //播放类型
    FragmentActivity player;
    private View rootView;


    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_movie_zbativity, null);
    }

    @Override
    @OnClick({R.id.mhde_img_back, R.id.refresh_view,R.id.play})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.mhde_img_back:
                player.finish();
                break;
            case R.id.refresh_view:
                break;
            case R.id.play:
                star(true);
                break;
        }
    }

    @Override
    public void iniview() {
        isViewCreated = true;
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  List<item> list = (List<item>) getArguments().getSerializable("list");
      //  video = list.get(getArguments().getInt("position")).mallVideo;
       // activity_player = getActivity();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated) {
            isViewCreated = false;
            star(true);
        } else {
            if (videoview != null) {
                star(false);
            }
        }

    }

    private void dataView() {

        play.setAlpha(0.2F);//透明度
        mhde_tv_title.setText("");
        title.setText(video.desc);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this).load(video.coverImage).into(zbbg);

        int index1 = new Random().nextInt(100) * 99;
        int index2 = new Random().nextInt(100) * 99;
        int index3 = new Random().nextInt(10) * 99;
        tv1.setText(String.valueOf(index1));
        tv2.setText(String.valueOf(index2));
        tv3.setText(String.valueOf(index3));


        //播放器开始
        videoview.setVideoURI(Uri.parse(video.playUrl));
        videoview.setOnPreparedListener(new onPreparedener());
        videoview.setOnCompletionListener(new PLOnCompletionListener() {
            @Override
            public void onCompletion() {
                star(true);
            }
        });
        videoview.requestFocus();
        videoview.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);//满屏显示
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoview != null) {
            videoview.pause();
            videoview.stopPlayback();
            videoview = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoview.isPlaying()) {
            star(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (videoview != null) {
            if (getUserVisibleHint()) {
                star(true);
            }
        }

    }

    /**
     * 开始播放了
     *
     * @param isplay
     */
    private void star(boolean isplay) {
        if (isplay) {
            videoview.start();
            play.setVisibility(View.GONE);
            zbbg.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            videoview.pause();
            play.setVisibility(View.VISIBLE);
        }

    }

    public static player palwy(List<item> list, int i) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) list);
        bundle.putInt("position", i);
        player player = new player();
        player.setArguments(bundle);
        return player;
    }

    //退出应用：onPause()->onStop()->onDestroyView()->onDestroy()->onDetach()（注意退出不会调用onSaveInstanceState方法，因为是人为退出，没有必要再保存数据）；
    //Fragment被回收又重新创建：被回收执行onPause()->onSaveInstanceState()->onStop()->onDestroyView()->onDestroy()->onDetach()，重新创建执行onAttach()->onCreate()->onCreateView()->onActivityCreated()->onStart()->onResume()->setUserVisibleHint()；

    /**
     * 弹出购买信息
     */
    public void show() {
        Dialog_bottom bottomDialog = new Dialog_bottom(getContext());
        bottomDialog.show();
    }

    /**
     * 爱心点亮
     */
    public void show3() {
        //Intent intent = new Intent();
        //intent.setClass(activity_player, VideoActivity.class);
        //startActivity(intent);
        //star(false);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.book_dialog_enter);
        //img.startAnimation(animation);
    }

    /**
     * 准备完成
     */
    class onPreparedener implements PLOnPreparedListener {

        @Override
        public void onPrepared(int i) {
            progressBar.setVisibility(View.GONE);
            if (isViewCreated && getUserVisibleHint()) {
                star(true);
            }
        }
    }



}
