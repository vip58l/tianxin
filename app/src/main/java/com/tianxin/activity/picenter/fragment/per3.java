package com.tianxin.activity.picenter.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.McallBack;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.video2.activity.activitymyplay;
import com.tianxin.adapter.Radapter;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.dialog.dialog_delete_video;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.dialog.dialog_videotitle;
import com.tianxin.listener.Callback;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.Constants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.Config.getFileName;

/**
 * 视视列表
 */
public class per3 extends BasFragment {
    private static final String TAG = "per3";
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Bundle arguments = getArguments();
        if (arguments == null) return;
        getUserId = arguments.getString(Constants.USERID);

        if (isVisibleToUser && list2.size() == 0) {
            toinitData();
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return rootView = inflater.inflate(R.layout.item_view_per3, null);
    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                loadMoreData();
                break;
        }

    }

    @Override
    public void iniview() {
        GridLayoutManager manager = new GridLayoutManager(context, 3);
        radapter = new Radapter(context, list2, Radapter.per3, callback);
        radapter.setPaymnets(paymnets);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(radapter);
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                toinitData();
            }
        });
    }

    /**
     * 联网请求获取数据
     */
    public void initData() {

    }

    public void toinitData() {
        totalPage++;
        datamodule.totalpage(getUserId, totalPage, paymnets);
    }

    @Override
    public void OnEorr() {

        if (refreshlayout != null) {
            refreshlayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static per3 perview(String getuserid) {
        Bundle args = new Bundle();
        per3 per3 = new per3();
        args.putString(Constants.USERID, getuserid);
        per3.setArguments(args);
        return per3;
    }

    public void loadMoreData() {
        list2.clear();
        totalPage = 0;
        radapter.notifyDataSetChanged();
        toinitData();
    }

    private Paymnets paymnets = new Paymnets() {

        @Override
        public void isNetworkAvailable() {
            Toashow.show(context, getString(R.string.eorrfali2));
            OnEorr();
            totalPage--;
        }

        @Override
        public void onFail() {
            Toashow.show(context, getString(R.string.eorrfali3));
            OnEorr();
            totalPage--;
        }

        @Override
        public void onSuccess(Object object) {
            List<videolist> data = (List<videolist>) object;
            if (data.size() > 0) {
                list2.addAll(data);
                radapter.notifyDataSetChanged();
            }
            OnEorr();
        }

        @Override
        public void onSuccess() {
            startActivity(new Intent(context, activity_svip.class));
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context);
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            per3.super.paymnets.ToKen(msg);
        }
    };

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            Object o = list2.get(position);
            videolist videolist = (com.tencent.opensource.model.videolist) o;
            if (!userInfo.getUserId().equals(videolist.getUserid()) && userInfo.getVip() == 0 && videolist.getType() == 1) {
                dialog_msg_svip.dialogmsgsvip(context, getString(R.string.dialog_msg_svip1), getString(R.string.tv_msg228), getString(R.string.tv_msg153), paymnets);
                return;
            }
            List<videolist> mlist = new ArrayList<>();
            for (Object o1 : list2) {
                if (o1 instanceof videolist) {
                    videolist videolist1 = (com.tencent.opensource.model.videolist) o1;
                    mlist.add(videolist1);
                }
            }

            //TYPE=0我的视频 |1别人的视频|2朋友圈动态视频
            activitymyplay.starsetAction(context, mlist, position, totalPage, userInfo.getUserId().equals(videolist.getUserid()) ? 0 : 1);
        }

        @Override
        public void LongClickListener(int position) {
            if (userInfo.getUserId().equals(getUserId)) {
                videolist videolist = (videolist) list2.get(position);
                //提示删除功能
                dialog_delete_video.video(context, videolist.getTop(), new Paymnets() {
                    @Override
                    public void onSuccess() {
                        deletevideo(position);
                    }

                    @Override
                    public void onRefresh() {
                        videolist videolist = (videolist) list2.get(position);
                        dialog_videotitle.dialog_videotitle(context, videolist, new Paymnets() {
                            @Override
                            public void onSuccess() {
                                loadMoreData();
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
                        radapter.notifyDataSetChanged();
                        datamodule.senvideotop(videolist,code);
                    }

                });
            }
        }

    };

    private void deletevideo(int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(context.getString(R.string.tv_msg221));
        dialogGame.setKankan(context.getString(R.string.btn_ok));
        dialogGame.setTextColor(context.getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(context.getResources().getColor(R.color.c_fu));
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
        videolist videolist = (com.tencent.opensource.model.videolist) list2.get(position);
        list2.remove(videolist);
        radapter.notifyDataSetChanged();

        //删除多个文件1到N个文件
        if (videolist.getTencent() == com.tianxin.Util.Constants.TENCENT) {
            String videofileName = getFileName(videolist.getPlayurl(), ".com/");
            String imagefileName = getFileName(videolist.getBigpicurl(), ".com/");
            List<String> objectList = Arrays.asList(videofileName, imagefileName);
            MySessionCredentialProvider.DELETEObject(objectList);
            MySessionCredentialProvider.setPaymnets(new Paymnets() {
                @Override
                public void onSuccess() {
                    deletevidel(videolist);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "onFail:");
                }
            });
        } else {
            deletevidel(videolist);
        }
    }

    private void deletevidel(videolist videolist) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datamodule.delete(videolist, null);
            }
        });

    }


}
