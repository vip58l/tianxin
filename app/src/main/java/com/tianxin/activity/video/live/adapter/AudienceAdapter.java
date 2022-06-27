package com.tianxin.activity.video.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tianxin.R;


/**
 * 观众列表适配器
 *
 * @author 刘洋巴金
 * @date 2017-5-3
 */
public class AudienceAdapter extends BaseAdapter {

    private final Context mContext;

    public AudienceAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return 1000;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_audienceadapter, null);
        }

        ImageView cv_audience_icon = convertView.findViewById(R.id.cv_audience_icon);

        return convertView;
    }

}