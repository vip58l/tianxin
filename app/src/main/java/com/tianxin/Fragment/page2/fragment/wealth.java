/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/18 0018
 */


package com.tianxin.Fragment.page2.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.setAdapter;
import com.tianxin.Module.McallBack;
import com.tencent.opensource.model.item;
import com.tianxin.Module.api.goldcoin;
import com.tencent.opensource.model.member;
import com.tianxin.R;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.activity_svip;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.pullableview.PullToRefreshLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 财富
 */
public class wealth extends BasFragment implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private static final String TAG = "wealth";
    @BindView(R.id.swiprefresh)
    public PullToRefreshLayout swiprefresh;
    @BindView(R.id.listview)
    public ListView listview;
    @BindView(R.id.iv_secondAvatar)
    public ImageView iv_secondAvatar;
    @BindView(R.id.iv_firstAvatar)
    public ImageView iv_firstAvatar;
    @BindView(R.id.iv_thirdAvatar)
    public ImageView iv_thirdAvatar;

    @BindView(R.id.tv_secondNick)
    public TextView tv_secondNick;
    @BindView(R.id.tv_secondValue)
    public TextView tv_secondValue;

    @BindView(R.id.tv_firstNick)
    public TextView tv_firstNick;
    @BindView(R.id.tv_firstValue)
    public TextView tv_firstValue;

    @BindView(R.id.tv_thirdNick)
    public TextView tv_thirdNick;
    @BindView(R.id.tv_thirdValue)
    public TextView tv_thirdValue;

    @BindView(R.id.eorr)
    public LinearLayout eorr;

    public static Fragment startActivity(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        wealth wealth = new wealth();
        wealth.setArguments(bundle);

        return wealth;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        type = arguments.getInt(Constants.TYPE, -1);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_wealth, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item item = list.get(position);
        member member = ((goldcoin) item.object).getMember();
        if (member == null) {
            return;
        }
        if (userInfo.getVip() != 1 && !userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            dialog_msg_svip.dialogmsgsvip(context, getString(R.string.dialog_msg_svip), getString(R.string.tv_msg228), getString(R.string.tv_msg153), paymnets);
            return;
        }
        Intent intent = new Intent(getContext(), activity_picenter.class);
        intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
        startActivity(intent);
    }


    @Override
    public void iniview() {
        swiprefresh.setOnRefreshListener(this);
        listview.setAdapter(setAdapter = new setAdapter(context, list));
        listview.setDividerHeight(1);
        listview.setOnItemClickListener(this);
        eorr.setOnClickListener(v -> initData());
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.getallchargessss(totalPage, type, paymnets1);
    }

    @Override
    public void OnEorr() {
        if (swiprefresh!=null){
            swiprefresh.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
            return;
        }
        loadoredata();
    }

    private void loadoredata() {
        totalPage = 0;
        list.clear();
        setAdapter.notifyDataSetChanged();
        initData();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        initData();
    }

    private final Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess() {
            startActivity(new Intent(context, activity_svip.class));
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context);
        }
    };

    private final Paymnets paymnets1 = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            List<goldcoin> goldcoins = (List<goldcoin>) object;
            for (goldcoin goldcoin : goldcoins) {
                item item = new item();
                item.type = com.tianxin.adapter.setAdapter.wealth;
                item.object = goldcoin;
                member member = goldcoin.getMember();
                if (member != null) {
                    list.add(item);
                }
            }
            setAdapter.notifyDataSetChanged();


            if (list.size() > 0) {
                goldcoin goldcoin = (goldcoin) list.get(0).object;
                Glideload.loadImage(iv_firstAvatar, goldcoin.getMember().getPicture());
                tv_firstNick.setText(goldcoin.getMember().getTruename());
                tv_firstValue.setText(String.valueOf(goldcoin.getMoney()));
            }

            if (list.size() > 1) {
                goldcoin goldcoin = (goldcoin) list.get(1).object;
                Glideload.loadImage(iv_secondAvatar, goldcoin.getMember().getPicture());
                tv_secondNick.setText(goldcoin.getMember().getTruename());
                tv_secondValue.setText(String.valueOf(goldcoin.getMoney()));
            }
            if (list.size() > 2) {
                goldcoin goldcoin = (goldcoin) list.get(2).object;
                Glideload.loadImage(iv_thirdAvatar, goldcoin.getMember().getPicture());
                tv_thirdNick.setText(goldcoin.getMember().getTruename());
                tv_thirdValue.setText(String.valueOf(goldcoin.getMoney()));
            }


        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }
    };
}
