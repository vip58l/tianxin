package com.tianxin.activity.activitycity;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;
import com.tencent.opensource.model.Citychat;
import com.tencent.opensource.model.Citydate;

import java.util.List;

import butterknife.BindView;

public class activity_cityshow extends BasActivity2 {
    private static final String TAG = activity_cityshow.class.getSimpleName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.msg)
    TextView msg;

    @Override
    protected int getview() {
        return R.layout.activity_cityshow;
    }

    @Override
    public void iniview() {
        backtitle.setconter("同城");
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(radapter = new Radapter(context, list, Radapter.itemcityshow, callback));
        msg.setText(R.string.city_msg2);
    }

    @Override
    public void initData() {
        String json = getIntent().getStringExtra(Constants.JSON);
        Citydate citydate = gson.fromJson(json, Citydate.class);
        totalPage++;
        datamodule.getcitychat(totalPage, citydate, paymnets);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            List<Citychat> citychat = (List<Citychat>) object;
            list.addAll(citychat);
            radapter.notifyDataSetChanged();
        }

    };

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {

        }

        @Override
        public void onSuccess() {

        }
    };
}
