package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.LiveRoom;
import com.tencent.opensource.model.member;

import butterknife.BindView;

public class sLiveTuivoiceRoom extends BaseHolder {
    @BindView(R.id.item_sex_img)
    ImageView item_sex_img;
    @BindView(R.id.item_sex_tv_title)
    TextView item_sex_tv_title;
    @BindView(R.id.item_sex_tv_date)
    TextView item_sex_tv_date;
    @BindView(R.id.item_sex_tv_readnum)
    TextView item_sex_tv_readnum;

    public sLiveTuivoiceRoom(View inflate) {
        super(inflate);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        LiveRoom liveRooms = (LiveRoom) object;
        member member = liveRooms.getMember();
        Glideload.loadImage(item_sex_img, member.getPicture(), 6);
        item_sex_tv_title.setText(String.format("%s 的房间号 %s", member.getTruename(), String.valueOf(liveRooms.getUserid()).hashCode()));
        item_sex_tv_date.setText(String.format("语音聊天群 %s-%s", (TextUtils.isEmpty(member.getProvince()) ? "" : member.getProvince()), (TextUtils.isEmpty(member.getCity()) ? "" : member.getCity())));
        item_sex_tv_readnum.setText("");
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
