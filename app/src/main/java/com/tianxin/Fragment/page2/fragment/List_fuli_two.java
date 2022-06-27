package com.tianxin.Fragment.page2.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.adapter.setAdapter;
import com.tianxin.R;
import com.tianxin.activity.video.live.LiveVideo;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.utils.Constants;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

/**
 * 第三方直播放源
 */
public class List_fuli_two extends BasFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.pullGridview)
    GridView pullGridview;
    public String TAG = List_fuli_two.class.getSimpleName();

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_dy, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible && list.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.autoRefresh();
                }
            }, 100);
        }
    }

    @Override
    public void iniview() {
        currentPage = 0;
        setAdapter = new setAdapter(context, list);
        pullGridview.setAdapter(setAdapter);
        pullGridview.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                list.clear();
                totalPage = 0;
                currentPage = 0;
                Page_Date = false;
                setAdapter.notifyDataSetChanged();
                toinitData();
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
                if (Page_Date) {
                    currentPage++;
                    switch (currentPage) {
                        case 1:
                            datamodule.GetRoomHotNewSome(paymnets);
                            break;
                        case 2:
                            datamodule.Live_RoomSelect(paymnets);
                            break;
                    }
                    if (currentPage > 2) {
                        ToastUtil.toastLongMessage(getString(R.string.eorrtext));
                        return;
                    }

                }
                toinitData();
            }
        });
    }

    @Override
    public void initData() {

    }

    public void toinitData() {
        totalPage++;
        datamodule.getaLivevideo(totalPage, paymnets1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            if (data != null) {
                int position = data.getIntExtra(Constants.POSITION, 0);
                pullGridview.setSelection(position);
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, LiveVideo.class);
        intent.putExtra(Constants.list, (Serializable) list);
        intent.putExtra(Constants.POSITION, position);
        startActivityForResult(intent, Config.sussess);
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout!=null){
            smartRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {

    }

    private Paymnets paymnets1 = new Paymnets() {
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
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(Object object) {
            List<item> array = (List<item>) object;
            list.addAll(array);
            if (array.size() < 20) {
                Page_Date = true;
                datamodule.getallchargess(paymnets);
            } else {
                setAdapter.notifyDataSetChanged();
                OnEorr();
            }

        }

        @Override
        public void onError() {
            Page_Date = true;
            datamodule.getallchargess(paymnets);
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
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(Object object) {
            List<item> array = (List<item>) object;
            list.addAll(array);
            Log.d(TAG, "onSuccess: " + list.size());
            setAdapter.notifyDataSetChanged();
            OnEorr();
        }

    };
}
