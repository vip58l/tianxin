package com.tianxin.activity.video.live.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianxin.R;

import java.util.List;

/**
 * 评论列表适配器
 *
 * @author 刘洋巴金
 * @date 2017-5-3
 */
public class MessageAdapter extends BaseAdapter {

    private final Context mContext;
    private List<String> data;

    public MessageAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {

        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {

        return data.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_messageadapter, null);
        }

        // 评论
        TextView tv_msg = convertView.findViewById(R.id.tv_msg);
        tv_msg.setText(data.get(position));
        return convertView;
    }


    //通知刷新
    public void NotifyAdapter(List<String> data) {
        //删除最早的一条保存1000数据
        while (data.size() > 1000) {
            data.remove(0);
        }
        this.data = data;
        notifyDataSetChanged();
    }

}