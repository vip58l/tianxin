package com.tianxin.adapter.itemholder;

import static android.view.View.GONE;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.listener.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class mvidewloder15 extends BaseHolder {
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.v1)
    View v1;

    public mvidewloder15(View inflate) {
        super(inflate);
        ButterKnife.bind(this, inflate);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    public void bind(Context context, List<Object> list, Object object, int position, Callback callback) {
        this.setsuper(context, object, position, callback);

        String na1 = (String) object;
        tv.setText(na1);
        if (list.size() - 1 == position) {
            v1.setVisibility(GONE);
        }

        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }

    }

    @Override
    public void OnClick(View v) {

    }

}
