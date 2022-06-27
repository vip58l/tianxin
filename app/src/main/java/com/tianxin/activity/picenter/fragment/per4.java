package com.tianxin.activity.picenter.fragment;

import static com.tianxin.Util.Config.getFileName;

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
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picture;
import com.tianxin.adapter.Radapter;
import com.tianxin.R;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.dialog.dialog_delete_img;
import com.tianxin.dialog.dialog_game;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.imglist;
import com.tencent.opensource.model.item;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人主页相册
 */
public class per4 extends BasFragment {
    private static final String TAG = per4.class.getName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getUserId = arguments.getString(Constants.touserid);
            if (isVisibleToUser && list2.size() == 0) {
                toinitData();
            }
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.per2_item_view, null);
    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        initData();
    }

    @Override
    public void iniview() {
        radapter = new Radapter(context, list2, Radapter.per4, callback);
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerview.setAdapter(radapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                toinitData();
            }
        });
    }

    @Override
    public void initData() {

    }

    public void toinitData() {
        totalPage++;
        datamodule.getimglist(getUserId, paymnets, totalPage);
    }

    public void loadMoreData() {
        totalPage = 0;
        list2.clear();
        radapter.notifyDataSetChanged();
        toinitData();

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

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            Intent intent = new Intent(context, activity_picture.class);
            intent.putExtra(Constants.POSITION, position);
            intent.putExtra(Constants.dadelist, (Serializable) list);
            startActivityForResult(intent, Config.sussess);
        }

        @Override
        public void LongClickListener(int position) {
            if (!userInfo.getUserId().equals(getUserId)) {
                return;
            }
            //提示删除功能
            dialog_delete_img.dialogdeleteimg(context, new Paymnets() {
                @Override
                public void onSuccess() {
                    deletevideo(position);
                }

                @Override
                public void onRefresh() {
                    Toashow.show(getString(R.string.tv_msg222));
                }

                @Override
                public void onLoadMore() {
                    Log.d(TAG, "onLoadMore: ");
                }
            });

        }
    };

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void onError() {
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
            }
        }

        @Override
        public void ToKen(String msg) {
            per4.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(Object object) {
            List<imglist> data = (List<imglist>) object;
            if (data.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    ToastUtil.toastLongMessage(getString(R.string.tv_msg238));
                }
            }
            if (data.size() > 0) {
                list2.addAll(data);
                for (imglist imglist : data) {
                    item item = new item();
                    item.object = imglist;
                    list.add(item);
                }
                radapter.notifyDataSetChanged();
            }
            onError();

        }


    };

    public static per4 perview(String getuserid) {
        Bundle args = new Bundle();
        args.putString(Constants.touserid, getuserid);
        per4 per4 = new per4();
        per4.setArguments(args);
        return per4;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        list.clear();
        list2.clear();
    }

    private void deletevideo(int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(getString(R.string.tv_msg261));
        dialogGame.setKankan(getString(R.string.tv_msg113));
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
        imglist imglist = (com.tencent.opensource.model.imglist) list2.get(position);
        //移除list内容
        list2.remove(position);
        radapter.notifyDataSetChanged();

        //删除平台数据
        if (imglist.getTencent() == Constants.TENCENT) {
            MySessionCredentialProvider.DELETEObject(getFileName(imglist.getPic(), ".com/"));
            MySessionCredentialProvider.setPaymnets(new Paymnets() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess: ");
                    deleteimglist(imglist);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "onFail:");
                }
            });
        } else {
            deleteimglist(imglist);
        }

    }

    private void deleteimglist(imglist imglist) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datamodule.delete(imglist, null);
            }
        });
    }

}
