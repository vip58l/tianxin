/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/16 0016
 */

package com.tianxin.activity.palyzb;

import android.view.View;
import android.widget.ListView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.adapter.setAdapter;
import com.tencent.opensource.model.item;
import com.tianxin.Module.api.shouchangmember;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.pullableview.MyListener;
import com.tianxin.pullableview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class activity_list_video extends BasActivity2 {
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshLayout;
    @BindView(R.id.pullGridview)
    ListView pullGridview;
    @BindView(R.id.imgback)
    itembackTopbr item_back;
    List<item> list = new ArrayList();


    @Override
    protected int getview() {
        return R.layout.activity_list1;
    }

    @Override
    public void iniview() {
        item_back.settitle("视频列表", "");
        refreshLayout.setOnRefreshListener(new MyListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        pullGridview.setAdapter(adappter = new setAdapter(this, list));
    }

    @Override
    public void initData() {
        for (int i = 0; i < 20; i++) {
            shouchangmember shou = new shouchangmember();
            shou.setId(i + "");
            shou.setUsername("小清校招");
            shou.setPic("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1663879126,2733636093&fm=26&gp=0.jpg");
            shou.setTitle("1111");
            shou.setUserid("2201");
            shou.setTruename("小清校招");
            shou.setType(11);
            item item = new item();
            item.object = setAdapter.shouchang;
            item.object = shou;
        }
        adappter.notifyDataSetChanged();
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }


}
