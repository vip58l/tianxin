/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/8 0008
 */


package com.tencent.qcloud.costransferpractice.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.multimedia.liteav.audiokit.utils.Constant;
import com.tencent.qcloud.costransferpractice.R;

import java.util.List;

public class myadapter extends RecyclerView.Adapter<myadapter.MyViewHolder> {

    private final String TAG = "myadapter";
    private final List<videotitle> list;
    private final Context context;
    private showBackCall showBackCall;

    public myadapter(Context context, List<videotitle> list, showBackCall showBackCall) {
        this.context = context;
        this.list = list;
        this.showBackCall = showBackCall;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_msg, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final videotitle video = list.get(position);
        holder.name.setText(video.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBackCall.videotitle(video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
        }
    }

    public interface showBackCall {
        void videotitle(videotitle video);
    }
}


