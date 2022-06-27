/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/1 0001
 */


package com.tianxin.adapter.Tiktokholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.Module.api.misc;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class dialogadapter extends RecyclerView.Adapter<dialogadapter.MyViewHolder> {
    private final Context context;
    private final List<misc> list;
    private Paymnets paymnets;
    private int msposition;
    private boolean getMsposition = false;

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public dialogadapter(Context context, List<misc> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog_misc, null));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        misc misc = list.get(position);
        holder.title.setText(misc.getTitle());
        holder.name.setText(misc.getTag());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymnets.status(position);
                msposition = position;
                getMsposition = true;
                notifyDataSetChanged();
            }
        });
        if (getMsposition && msposition == position) {
            holder.title.setTextColor(context.getResources().getColor(R.color.c_fu));
            holder.title.setTextSize(20);
            holder.name.setTextSize(18);
            holder.name.setTextColor(context.getResources().getColor(R.color.c_fu));
        } else {
            holder.title.setTextSize(14);
            holder.name.setTextSize(14);
            holder.title.setTextColor(context.getResources().getColor(R.color.homeback));
            holder.name.setTextColor(context.getResources().getColor(R.color.home));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.name)
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
