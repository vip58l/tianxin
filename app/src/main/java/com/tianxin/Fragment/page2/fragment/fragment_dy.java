package com.tianxin.Fragment.page2.fragment;

import static com.blankj.utilcode.util.StringUtils.getString;

import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.video.videoijkplayer2;
import com.tianxin.adapter.setAdapter;
import com.tianxin.Module.api.content;
import com.tianxin.Module.api.getotalPage;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.getlist.minivideo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 6V网络短视频
 */
public class fragment_dy extends BasFragment implements AdapterView.OnItemClickListener {
    String TAG = "four_item_dy";
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.pullGridview)
    GridView pullGridview;
    private int endtotalPage;

    public static Fragment show(int type) {
        fragment_dy fragmentDy = new fragment_dy();
        return fragmentDy;
    }


    @Override
    public View getview(LayoutInflater inflater) {
        return rootView = inflater.inflate(R.layout.fragment_dy, null);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        minivideo video = (minivideo) list.get(position).object;
        String playurl = String.format("http://m.v.6.cn/v/%s?vid=%s&referrer=", video.vid, video.vid);
        Intent intent = new Intent();
        intent.putExtra(Constants.VIDEOURL, playurl);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.TOTALPAGE, totalPage);
        intent.putExtra(Constants.ENDTOTALPAGE, endtotalPage);
        intent.putExtra(Constants.List, (Serializable) list);
        //intent.setClass(context, DyWebActivity.class); //网页访问
        intent.setClass(context, videoijkplayer2.class);
        startActivityForResult(intent, Config.sussess);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            if (data != null) {
                int position = data.getIntExtra(Constants.POSITION, 0);
                //移动到指定位置 带滚动效果
                pullGridview.smoothScrollToPositionFromTop(position, position);
                //移动到指定位置 不带滚动效果
                //pullGridview.setSelection(position);
            }
        }

    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                initData();
                break;
        }
    }

    @Override
    public void iniview() {
        setAdapter = new setAdapter(context, list);
        pullGridview.setAdapter(setAdapter);
        pullGridview.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                totalPage = 0;
                list.clear();
                setAdapter.notifyDataSetChanged();
                toinitData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                toinitData();
            }
        });
    }

    @Override
    public void initData() {

    }

    public void toinitData() {
        showDialogFrag();
        totalPage++;
        datamodule.fragment6v(totalPage, paymnets);
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
        dismissDialogFrag();
    }

    @Override
    public void onRefresh() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            getotalPage getotalPage = (com.tianxin.Module.api.getotalPage) object;
            content content = getotalPage.content;
            endtotalPage = Integer.parseInt(content.totalPage); //总页数
            List<minivideo> mlist = content.list;
            if (mlist.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrtext));
                }
                OnEorr();
                return;
            }
            for (minivideo video : mlist) {
                item it = new item();
                it.type = setAdapter.dy;
                it.object = video;
                list.add(it);
            }

            if (totalPage == 0 || totalPage == 0) {
                //打乱集合中的元素
                Collections.shuffle(list);
            }

            setAdapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void fall(int code) {
            Toashow.show(getString(R.string.eorrfali3));
            OnEorr();

        }
    };

}
