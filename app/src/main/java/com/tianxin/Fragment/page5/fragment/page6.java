package com.tianxin.Fragment.page5.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.activity.activity_item.fragment_load;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;

/**
 * 发现列表
 */
public class page6 extends BasFragment {
    private static final String TAG = "fragmentfaxing";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.faragmentfaxing, null);
    }

    @Override
    public void iniview() {
        radapter = new Radapter(context, list2, Radapter.fragmentfaxing);
        radapter.setPaymnets(paymnets);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(radapter);
    }

    @Override
    public void initData() {
        if (list2.size() == 0) {
            list2.add("朋友圈");
            list2.add("短视频");
            list2.add("附近的人");
            list2.add("开直播");
            list2.add("去赚钱");
            radapter.notifyDataSetChanged();
        }

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void status(int position) {
            switch (position) {
                case 0:
                    tostartActivity(fragment_load.class, position, Constants.requestCode, "朋友圈");
                    break;
                case 1:
                    tostartActivity(fragment_load.class, position, Constants.requestCode, "短视频");
                    break;
                case 2:
                    Toashow.show("此功能仅限VIP会员开放");
                    break;
                case 3:
                    tostartActivity2();
                    break;
                default:
                    startActivity(new Intent(getContext(), References.class));//去赚钱
                    break;
            }
        }
    };


}
