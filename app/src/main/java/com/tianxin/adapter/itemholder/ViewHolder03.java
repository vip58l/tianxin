package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.listener.Callback;

import butterknife.BindView;

public class ViewHolder03 extends BaseHolder {
    @BindView(R.id.item_search_one_nums)
    TextView item_search_one_nums;
    @BindView(R.id.item_search_one_con)
    TextView item_search_one_con;

    public ViewHolder03(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.cool_item_search_grid_one, null));

    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        item_search_one_nums.setText(String.valueOf(position + 1));
        item_search_one_con.setText(object.toString());
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
