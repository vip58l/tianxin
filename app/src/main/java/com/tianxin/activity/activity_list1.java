package com.tianxin.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.adapter.setAdapter;
import com.tianxin.Module.api.shouchangmember;
import com.tencent.opensource.model.item;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.pullableview.PullToRefreshLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 收藏
 */
public class activity_list1 extends BasActivity2 {

    @BindView(R.id.swiprefresh)
    PullToRefreshLayout swiprefresh;
    @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.imgback)
    itembackTopbr item_back;
    @BindView(R.id.eorr)
    ImageView eorr;
    List<item> list = new ArrayList<>();

    @Override
    protected int getview() {
        return R.layout.activity_list1;
    }

    @Override
    public void iniview() {
        item_back.settitle(getString(R.string.tv_msg_title_t1));
        swiprefresh.setOnRefreshListener(pullToRefreshLayout);
        listview.setAdapter(adappter = new setAdapter(this, list));
        getdatehttp();
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }


    /**
     * 下拉或上拉获取数据
     */
    PullToRefreshLayout.OnRefreshListener pullToRefreshLayout = new PullToRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            ToastUtil.toastShortMessage(getString(R.string.eorrtext));
        }


    };

    public Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            totalPage--;
        }

        @Override
        public void onFail() {
            totalPage--;
        }

        @Override
        public void onSuccess(Object object) {
            List<shouchangmember> rs = (List<shouchangmember>) object;
            for (shouchangmember r : rs) {
                item item = new item();
                item.type = setAdapter.shouchang;
                item.object = r;
                list.add(item);
            }
            adappter.notifyDataSetChanged();
        }
    };

    /**
     * 联网请求获取数据
     */
    public void getdatehttp() {
        totalPage++;
        datamodule.collection(totalPage, paymnets);
    }

}