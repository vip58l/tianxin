/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/28 0028
 */


package com.tianxin.adapter.Tiktokholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class mysetAdapter extends RecyclerView.Adapter<mysetAdapter.myViewHolder> {
    private final List<editinfo> list = new ArrayList<>();
    private final Context context;
    private Paymnets payment;

    public List<editinfo> getList() {
        return list;
    }

    public void setPayment(Paymnets payment) {
        this.payment = payment;
    }

    public mysetAdapter(Context context) {
        this.context = context;
        //List<String> mlist = Arrays.asList(context.getResources().getStringArray(R.array.arrayitem11));
        List<String> mlist = Arrays.asList(context.getResources().getStringArray(R.array.arrayitem10));
        for (int i = 0; i < mlist.size(); i++) {
            String s = mlist.get(i);
            editinfo editinfo = new editinfo();
            editinfo.setName(s);
            editinfo.setTitle("-");
            editinfo.setType(i);
            list.add(editinfo);
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.item_edit_msg, null));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.Bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.viewhtml)
        View viewhtml;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void Bind(int position) {

            editinfo editinfo = list.get(position);
            name.setText(editinfo.name);
            title.setText(editinfo.title);
            itemView.setOnClickListener(new myOnClickListener(position));
            switch (position) {
                case 0:
                case 4:
                case 8:
                case 12:
                    //添加分割线
                    ViewGroup.LayoutParams layoutParams = viewhtml.getLayoutParams();
                    layoutParams.height = 20;
                    viewhtml.setLayoutParams(layoutParams);
                    break;
            }
        }

    }

    class myOnClickListener implements View.OnClickListener {
        private final int position;

        public myOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (payment != null) {
                payment.status(position);
            }
        }
    }

    public class editinfo {
        String name;
        String title;
        int type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
