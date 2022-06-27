package com.tianxin.adapter.itemholder;

import static com.tianxin.Module.GildeRoundTransform.getoptions;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import butterknife.BindView;

public class itemholderoNew extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.online)
    LinearLayout online;

    public static View viewholder(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_one_list, null);
    }

    public itemholderoNew(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    public void bind(Context context, Object object, int position, int city, Paymnets paymnets, AMapLocation aMapLocation) {
        this.paymnets = paymnets;
        this.context = context;
        this.object = object;
        this.position = position;
        member member = (member) object;
        personal personal = member.getPersonal();
        name.setText(member.getTruename());
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(member.getProvince())) {
            switch (city) {
                case 1:
                    String substring = TextUtils.isEmpty(member.getProvince()) ? "" : member.getProvince().substring(0, 2);
                    sb.append(substring);
                    break;
                case 2:
                    try {
                        sb.delete(0, sb.length());
                        sb.append(TextUtils.isEmpty(member.getCity()) ? "" : member.getCity().substring(0, 2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        sb.delete(0, sb.length());
                        sb.append(TextUtils.isEmpty(member.getDistrict()) ? "" : member.getDistrict().substring(0, 2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
        if (personal != null) {
            if (!TextUtils.isEmpty(personal.getProvince())) {
                switch (city) {
                    case 1:
                        sb.delete(0, sb.length());
                        String substring = TextUtils.isEmpty(personal.getProvince()) ? "" : personal.getProvince().substring(0, 2);
                        sb.append(substring);
                        break;
                    case 2:
                        sb.delete(0, sb.length());
                        sb.append(TextUtils.isEmpty(personal.getCity()) ? "" : personal.getCity().substring(0, 2));
                        break;
                    case 3:
                        sb.delete(0, sb.length());
                        sb.append(TextUtils.isEmpty(personal.getDistrict()) ? "" : personal.getDistrict().substring(0, 2));
                        break;
                }
            }
            if (!TextUtils.isEmpty(String.valueOf(personal.getAge()))) {
                if (TextUtils.isEmpty(sb.toString())) {
                    sb.append(personal.getAge() + " 岁");
                } else {
                    sb.append(" | " + personal.getAge() + " 岁");
                }
            }
            if (!TextUtils.isEmpty(String.valueOf(personal.getHeight()))) {
                sb.append(" | " + personal.getHeight() + " cm");
            }
            if (!TextUtils.isEmpty(personal.getOccupation())) {
                sb.append(" | " + personal.getOccupation());
            }
            title.setText(personal.getPesigntext());
        }
        msg.setText(sb.toString());

        if (!TextUtils.isEmpty(member.getPicture())) {
            Glide.with(context).load(member.getPicture()).apply(getoptions(6)).into(image);
        } else {
            Glide.with(context).load(member.getSex() == 1 ? R.mipmap.a1 : R.mipmap.a2).apply(getoptions(6)).into(image);
        }
        online.setVisibility(member.getOnline() == 1 ? View.VISIBLE : View.GONE);

        itemView.setOnClickListener(v -> tostartActivity(member));
        try {
            if (aMapLocation != null) {
                if (personal != null) {
                    if (!TextUtils.isEmpty(personal.getJwd())) {
                        String jwd = lbsamap.scalculateLineDistance(aMapLocation, personal.getJwd());

                    } else {
                        String jwd = lbsamap.scalculateLineDistance(aMapLocation, TextUtils.isEmpty(member.getJwd()) ? "" : member.getJwd());

                    }
                } else {
                    if (!TextUtils.isEmpty(member.getJwd())) {
                        String jwd = lbsamap.scalculateLineDistance(aMapLocation, member.getJwd());

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void tostartActivity(member member) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(context.getString(R.string.eorrfali2));
            return;
        }
        Intent intent = new Intent(context, activity_picenter.class);
        intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
        context.startActivity(intent);
    }

    /**
     * 转到对应用户聊天界面
     */
    private void startChatActivity(member member) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(context.getString(R.string.eorrfali2));
            return;
        }
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(String.valueOf(member.getId()));
        chatInfo.setChatName(member.getTruename());
        chatInfo.setIconUrlList(TextUtils.isEmpty(member.getPicture()) ? "" : member.getPicture());
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void OnClick(View v) {


    }
}
