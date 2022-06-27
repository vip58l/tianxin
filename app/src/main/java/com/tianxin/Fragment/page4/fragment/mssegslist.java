/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/27 0027
 */


package com.tianxin.Fragment.page4.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.listener.Paymnets;
import com.tianxin.R;
import com.tianxin.pullableview.PullToRefreshLayout;
import com.tencent.opensource.model.sysmessage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class mssegslist extends BasFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = mssegslist.class.getSimpleName();
    @BindView(R.id.swiprefresh)
    PullToRefreshLayout swiprefresh;
    @BindView(R.id.listview)
    ListView listview;
    private setpullListView setpullListView;
    private final List<sysmessage> list = new ArrayList<>();
    private final static String key = "key";
    private final static String page = "page";
    private int type;
    private int totpage;
    private int isradposition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        String title = arguments.getString(key);
        type = arguments.getInt(page);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        swiprefresh.setOnRefreshListener(pullToRefreshLayout);
        setpullListView = new setpullListView();
        listview.setAdapter(setpullListView);
        listview.setDividerHeight(0);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        totpage++;
        datamodule.sysmessage(totpage, paymnets);
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sysmessage sysmessage = list.get(position);
        sysmessage.setIsread(1);
        //刷新通知
        setpullListView.notifyDataSetChanged();
        DyWebActivity.starAction(activity, sysmessage);
    }

    public static Fragment getfragment(int i, String s) {
        Bundle bundle = new Bundle();
        bundle.putString(key, s);
        bundle.putInt(page, i);
        mssegslist mssegslist = new mssegslist();
        mssegslist.setArguments(bundle);
        return mssegslist;
    }

    /**
     * 下拉或上拉获取数据
     */
    private final PullToRefreshLayout.OnRefreshListener pullToRefreshLayout = new PullToRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            if (!Config.isNetworkAvailable()) {
                Toashow.show(getString(R.string.eorrfali2));
                return;
            }
            list.clear();
            totpage = 0;
            setpullListView.notifyDataSetChanged();
            initData();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            if (!Config.isNetworkAvailable()) {
                Toashow.show(getString(R.string.eorrfali2));
                return;
            }
            initData();
        }


    };

    private class setpullListView extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                switch (type) {
                    case 1:
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_msegs2, null);
                        convertView.setTag(new item_msegs(convertView));
                        break;
                    case 2:
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_msegs1, null);
                        break;
                    case 3:
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_msegs3, null);
                        break;
                    case 4:
                        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_msegs4, null);
                        convertView.setTag(new item_msegs(convertView));
                        break;
                }
            }
            item_msegs tag = (item_msegs) convertView.getTag();
            tag.bind(list.get(position));
            return convertView;
        }

    }

    private class item_msegs {
        private ImageView icon;
        private TextView tv1, tv2, tv3, time;
        private RelativeLayout layoutcc;

        public item_msegs(View itemview) {
            icon = itemview.findViewById(R.id.icon);
            tv1 = itemview.findViewById(R.id.tv1);
            tv2 = itemview.findViewById(R.id.tv2);
            tv3 = itemview.findViewById(R.id.tv3);
            time = itemview.findViewById(R.id.time);
            layoutcc = itemview.findViewById(R.id.layoutcc);
        }

        public void bind(sysmessage sysmessage) {
            icon.setImageResource(R.mipmap.ic_handsfree_disable);
            tv1.setText(sysmessage.getTitle());
            tv2.setText(sysmessage.getContent());
            tv3.setVisibility(sysmessage.getIsread() == 0 ? View.VISIBLE : View.GONE);
            if (!TextUtils.isEmpty(sysmessage.getDatetime())) {
                time.setText(Config.stampToDate(sysmessage.getDatetime()));
            }

        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {
            try {
                Toashow.show(getString(R.string.eorrfali3));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void isNetworkAvailable() {
            try {
                Toashow.show(getString(R.string.eorrfali2));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onSuccess(Object object) {
            List<sysmessage> sysmessagels = (List<sysmessage>) object;
            list.addAll(sysmessagels);
            setpullListView.notifyDataSetChanged();
        }

        @Override
        public void ToKen(String msg) {
            //mssegslist.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            totpage--;
            if (totpage > 1) {
                Toashow.show(msg);
            }
        }
    };
}
