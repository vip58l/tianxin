/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/7 0007
 */


package com.tianxin.Fragment.fragment;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Toashow;
import com.tianxin.Module.api.buypaymoney;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购买VIP列表
 */
public class svip extends BasFragment {
    String TAG = svip.class.getSimpleName();
    int ii;
    boolean bos = false;
    myadapter myadapter;
    RecyclerView recyclerView;
    private Paymnets paymnets;
    private List<buypaymoney> list = new ArrayList<>();

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @Override
    public View getview(LayoutInflater inflater) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myadapter = new myadapter());
        return recyclerView;
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        datamodule.buypaymoney(paymnets1);
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    public class myadapter extends RecyclerView.Adapter<myadapter.vhadapter> {

        @NonNull
        @Override
        public vhadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_money_svip, null);
            return new vhadapter(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull vhadapter holder, int position) {
            buypaymoney paymoney = list.get(position);
            holder.baind(paymoney, position);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }


        public class vhadapter extends RecyclerView.ViewHolder {
            @BindView(R.id.line)
            LinearLayout line;
            @BindView(R.id.tv1)
            TextView tv1;
            @BindView(R.id.tv2)
            TextView tv2;
            @BindView(R.id.tv3)
            TextView tv3;
            @BindView(R.id.tv4)
            TextView tv4;
            @BindView(R.id.tv5)
            TextView tv5;

            public vhadapter(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void baind(buypaymoney paymoney, int position) {
                if (bos) {
                    line.setBackground(getResources().getDrawable(ii == position ? R.drawable.acitvity06 : R.drawable.diis_bg3));
                }
                tv1.setText(paymoney.getTitle());
                tv2.setText(String.valueOf(paymoney.getMoney1()));
                tv3.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv3.setText(String.valueOf(paymoney.getMoney2()));
                tv4.setText(String.format(getString(R.string.resmoney), paymoney.getMoney3()));
                tv5.setText(null);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymnets.onClick(paymoney);
                        ii = position;
                        bos = true;
                        notifyDataSetChanged();
                    }
                });
            }
        }

    }

    @Override
    public void onDestroy() {
        if (recyclerView != null) {
            recyclerView = null;
            myadapter = null;
            list.clear();
        }
        super.onDestroy();
    }

    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object object) {
            List<buypaymoney> buypaymonies = (List<buypaymoney>) object;
            list.addAll(buypaymonies);
            myadapter.notifyDataSetChanged();
        }
    };
}
