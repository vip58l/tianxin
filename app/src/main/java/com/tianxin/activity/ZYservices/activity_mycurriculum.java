package com.tianxin.activity.ZYservices;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.Webrowse;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.activity.video.videoijkplayer4;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_item_rs;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.getHandler.PostModule;
import com.tianxin.widget.EndLessOnScrollListener;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.curriculum;
import com.tencent.qcloud.costransferpractice.transfer.Api;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.tianxin.Util.Config.getFileName;
import static com.tianxin.tencent.cos.MySessionCredentialProvider.DELETEMultipleObject;

/**
 * 我的课程
 */
public class activity_mycurriculum extends BasActivity2 implements SwipeRefreshLayout.OnRefreshListener, Paymnets {
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    int itemposition;


    @Override
    protected int getview() {
        return R.layout.activity_mycurriculum;
    }

    @Override
    public void iniview() {
        userInfo = UserInfo.getInstance();
        itemback.settitle(getString(R.string.tv_msg77));
        itemback.righttext.setText(R.string.tv_msg78);
        itemback.righttext.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        radapter = new Radapter(this, list, Radapter.activity_mycurriculum);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter);
        recyclerview.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData();
            }
        });
        radapter.setPaymnets(this);
    }

    @Override
    public void initData() {
        totalPage++;
    PostModule.getModule(BuildConfig.HTTP_API + "/curriculum?userid=" + userInfo.getUserId() + "&page=" + totalPage + "&token=" + userInfo.getToken(), this);
    }

    @Override
    @OnClick({R.id.eorr, R.id.tv3title})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                initData();
                break;
            case R.id.tv3title:
                radapter.setDelshow(!radapter.isDelshow());
                itemback.righttext.setText(radapter.isDelshow() ? getString(R.string.tv_msg78) : getString(R.string.dialog_cols));
                break;
        }
    }

    @Override
    public void OnEorr() {
        swipeRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        totalPage = 0;
        list.clear();
        radapter.notifyDataSetChanged();
        initData();

    }


    private void deletevideo(int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(this);
        dialogGame.setTitle(getString(R.string.tv_msg221));
        dialogGame.setKankan(getString(R.string.btn_ok));
        dialogGame.setTextColor(getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(new Paymnets() {
            @Override
            public void activity() {
                curriculum curriculum = (curriculum) list.get(position);
                delcurriculum(position);
                //删除多个文件 腾讯云数据
                if (curriculum.getTencent() == Constants.TENCENT) {
                    String videofileName = Api.getFileName(curriculum.getPic());
                    String imagefileName = Api.getFileName(curriculum.getVideo());
                    List<String> objectList = Arrays.asList(videofileName, imagefileName);
                    DELETEMultipleObject(objectList);
                }


            }
        });
    }

    /**
     * 删除服务端数据
     *
     * @param position
     */
    public void delcurriculum(int position) {
        curriculum curr = (curriculum) list.get(position);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(curr.getId()))
                .add("userid", curr.getUserid())
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.delcurriculum, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        list.remove(position);
                        radapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {

            }
        });
    }

    /**
     * 跳转播放页
     *
     * @param position
     */
    public void gostartActivity(int position, Class<?> cls, int s) {
        curriculum curriculum = (curriculum) list.get(position);
        Intent intent = new Intent(this, cls);
        intent.putExtra(Constants.ROOM, curriculum);
        if (s == 1) {
            startActivityForResult(intent, Config.sussess);
        } else {
            startActivity(intent);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (radapter != null && keyCode == KeyEvent.KEYCODE_BACK) {
            if (radapter.isDelshow()) {
                radapter.setDelshow(false);
                itemback.righttext.setText(getString(R.string.tv_msg78));
                radapter.notifyDataSetChanged();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            totalPage = 0;
            list.clear();
            initData();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (radapter.isDelshow()) {
            deletevideo(position);
        } else {
            gostartActivity(position, videoijkplayer4.class, 0);
        }
    }

    @Override
    public void status(int position) {
        itemposition = position;
        dialog_item_rs dialogitemrs = dialog_item_rs.dialogitemrs(this, paymnets);
        dialogitemrs.sethide();
    }

    @Override
    public void success(String date) {
        try {
            List<curriculum> curricula = JsonUitl.stringToList(date, curriculum.class);
            if (curricula.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    Toashow.show(getString(R.string.eorrtext));
                }
                OnEorr();
                return;
            }
            list.addAll(curricula);
            radapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OnEorr();
    }

    @Override
    public void fall(int code) {
        OnEorr();
    }

    private final Paymnets paymnets = new Paymnets() {
        @Override
        public void status(int position) {
            switch (position) {
                case 1:
                    activity_mycurriculum.this.onRefresh();
                    break;
                case 2:
                    gostartActivity(itemposition, activity_courseedit.class, 1);
                    break;

            }
        }
    };

}
