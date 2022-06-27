/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/27 0027
 */


package com.tianxin.Fragment.page2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.video2.activity.activityplayer;
import com.tianxin.adapter.setAdapter;
import com.tianxin.dialog.dialog_delete_video;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_videotitle;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.getHandler.PostModule;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.Config.getFileName;

/**
 * 街拍视频列表listview
 */
public class fragment_jp extends BasFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    String TAG = fragment_jp.class.getSimpleName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.pullGridview)
    GridView pullGridview;
    private final List<item> list = new ArrayList<>();
    private setAdapter setAdapter;
    private static final String title = "title";
    private View rootView;
    private int totalPage;
    private int type;

    /**
     * 返回Fragment
     *
     * @param type
     * @return
     */
    public static Fragment show(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        fragment_jp fragmentVideo = new fragment_jp();
        fragmentVideo.setArguments(bundle);
        return fragmentVideo;
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<videolist> data = (List<videolist>) object;
            for (videolist videolist : data) {
                item item = new item();
                item.type = com.tianxin.adapter.setAdapter.sbaseadapter;
                item.object = videolist;
                list.add(item);
            }
            setAdapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                Toashow.show(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            fragment_jp.super.paymnets.ToKen(msg);
        }
    };

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        type = getArguments().getInt(Constants.TYPE);
        if (menuVisible && list.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.autoRefresh();
                }
            }, 100);
        }
    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                getdatehttp();
                break;
        }

    }

    @Override
    public void iniview() {
        pullGridview.setAdapter(setAdapter = new setAdapter(context, list));
        pullGridview.setOnItemClickListener(this);
        pullGridview.setOnItemLongClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                loadgetdatehttp();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(100/*,false*/);//传入false表示加载失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                getdatehttp();
            }
        });
    }

    @Override
    public void initData() {


    }

    @Override
    public View getview(LayoutInflater inflater) {
        return rootView = inflater.inflate(R.layout.fragment_dy, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //sstartActivityForResult(videoijkplayer1.class, position, totalPage, type, list);

        /**
         * 街拍短视频
         */
        sstartActivityForResult(activityplayer.class, position, totalPage, type, list);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        UserInfo userInfo = UserInfo.getInstance();
        videolist videolist = (videolist) list.get(position).object;
        if (userInfo.getUserId().equals(videolist.getUserid()) && videolist != null) {
            dialog_delete_video.video(context, videolist.getTop(), new Paymnets() {
                @Override
                public void onSuccess() {
                    deletevideo(position);
                }

                @Override
                public void onRefresh() {
                    dialog_videotitle.dialog_videotitle(context, videolist, new Paymnets() {
                        @Override
                        public void onSuccess() {
                            loadgetdatehttp();
                        }
                    });
                }

                @Override
                public void onLoadMore() {
                    Log.d(TAG, "onLoadMore: ");
                }

                @Override
                public void fall(int code) {
                    //置顶处理
                    videolist.setTop(code);
                    setAdapter.notifyDataSetChanged();
                    datamodule.senvideotop(videolist, code);
                }


            });
            return true;
        } else {
            return false;
        }
    }

    /**
     * 联网请求获取数据
     */
    public void getdatehttp() {
        totalPage++;
        datamodule.sbaseadapter(totalPage, type, paymnets);
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 刷新数据
     */
    private void loadgetdatehttp() {
        totalPage = 0;
        list.clear();
        setAdapter.notifyDataSetChanged();
        getdatehttp();
    }

    /**
     * 弹出提示消息
     *
     * @param position
     */
    private void deletevideo(int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(getString(R.string.tv_msg221));
        dialogGame.setKankan(getString(R.string.btn_ok));
        dialogGame.setTextColor(getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(new Paymnets() {
            @Override
            public void activity() {
                deleteitem(position);
            }
        });
    }

    /**
     * 删除文件
     *
     * @param position
     */
    private void deleteitem(int position) {
        item item = list.get(position);
        videolist videolist = (videolist) item.object;
        //移除list内容
        list.remove(position);
        setAdapter.notifyDataSetChanged();
        //删除多个文件1到N个文件
        if (videolist.getTencent() == Constants.TENCENT) {
            String videofileName = getFileName(videolist.getPlayurl(), ".com/");
            String imagefileName = getFileName(videolist.getBigpicurl(), ".com/");
            List<String> objectList = Arrays.asList(videofileName, imagefileName);
            MySessionCredentialProvider.DELETEObject(objectList);
            MySessionCredentialProvider.setPaymnets(new Paymnets() {
                @Override
                public void onSuccess() {
                    delete(videolist);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "onFail:");
                }
            });
        } else {
            delete(videolist);
        }
    }

    /**
     * 删除平台数据
     *
     * @param videolist
     */
    private void delete(videolist videolist) {
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.id, videolist.getId())
                .add(Constants.USERID, videolist.getUserid())
                .add(Constants.Token, userInfo.getToken())
                .build();

        //请求2
        PostModule.postModule(Webrowse.deletevideo, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                ToastUtils.showShort(mesresult.getMsg());
                loadgetdatehttp();
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Config.fail && data != null) {
            int intExtra = data.getIntExtra(Constants.POSITION, -1);
            int position = intExtra > list.size() ? list.size() - 1 : intExtra;


            //移动到指定位置 带滚动效果
            //pullGridview.smoothScrollToPositionFromTop(position, position);

            //移动到指定位置 不带滚动效果
            pullGridview.setSelection(position);
        }
    }

}
