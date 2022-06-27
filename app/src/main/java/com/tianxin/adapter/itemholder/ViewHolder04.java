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

public class ViewHolder04 extends BaseHolder {

    @BindView(R.id.item_search_two_con)
    TextView item_search_two_con;

    public void bind(Object object, int position) {
        item_search_two_con.setText(object.toString());
    }

    public ViewHolder04(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.cool_item_search_grid_two, null));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
