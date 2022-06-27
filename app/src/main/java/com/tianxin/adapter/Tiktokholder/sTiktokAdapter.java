/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/12 0012
 */


package com.tianxin.adapter.Tiktokholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.Module.api.memberparent;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.activity.picenter.activity_picenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 邀请好友列表
 */
public class sTiktokAdapter extends RecyclerView.Adapter<sTiktokAdapter.mysTiktokAdapter> {

    private Context context;
    private List<memberparent> list;

    public sTiktokAdapter(Context context, List<memberparent> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public mysTiktokAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new mysTiktokAdapter(LayoutInflater.from(context).inflate(R.layout.item_share, null));
    }

    @Override
    public void onBindViewHolder(@NonNull mysTiktokAdapter holder, int position) {
        memberparent memberparent = list.get(position);
        holder.bind(memberparent);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class mysTiktokAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.circleimageview)
        ImageView circleimageview;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.tv_money)
        TextView tv_money;
        @BindView(R.id.datetime)
        TextView datetime;

        public mysTiktokAdapter(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(memberparent memberparent) {
            name.setText(memberparent.getTruename());
            datetime.setText(Config.stampToDate(memberparent.getDatetime()));
            if (!TextUtils.isEmpty(memberparent.getPicture())) {
                ImageLoadHelper.glideShowCornerImageWithUrl(context, memberparent.getPicture(), circleimageview);
            }

            //隐藏手机号中间四位
            if (memberparent.getMobile().length() >= 11) {
                String s = memberparent.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                phone.setText(s);
            } else {
                phone.setText(memberparent.getMobile());
            }

            if (!TextUtils.isEmpty(memberparent.getMoney())) {
                float f = Float.valueOf(memberparent.getMoney());
                if (f > 0) {
                    tv_money.setText(String.format(context.getString(R.string.tv_msg199) + "", memberparent.getMoney()));
                } else {
                    tv_money.setText(null);
                }
            } else {
                tv_money.setText(null);
            }

            //打开个人主页头像
            itemView.setOnClickListener(v -> activity_picenter.setActionactivity(context, String.valueOf(memberparent.getId())));

        }
    }


}
