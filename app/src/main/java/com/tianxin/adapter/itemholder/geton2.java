package com.tianxin.adapter.itemholder;

import static com.tianxin.Module.GildeRoundTransform.getoptions;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BasitemBaseAdapter;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.opensource.model.videoPush;

import butterknife.BindView;

public class geton2 extends BasitemBaseAdapter {

    private static final String TAG = geton2.class.getSimpleName();
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.gzt)
    TextView gzt;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.name2)
    TextView name2;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.onecon)
    TextView onecon;
    @BindView(R.id.vipimg)
    ImageView vipimg;
    @BindView(R.id.online)
    LinearLayout online;
    @BindView(R.id.chuoyixia)
    ImageView chuoyixia;
    @BindView(R.id.svip)
    ImageView svip;

    public static View inflater(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_msges, null);
    }

    public geton2(View itemview) {
        super(itemview);
    }

    public void bind(Object object, int position) {


    }

    @Override
    public void bind(Object object, int position, Paymnets paymnets) {

    }

    @Override
    public void bind(Context context, Object object, int position, Paymnets paymnets) {

    }

    @Override
    public void bind(Context context, Object object, int position, int ii, Paymnets paymnets, AMapLocation aMapLocation) {
        this.samapLocation = aMapLocation;
        this.paymnets = paymnets;
        this.context = context;
        this.object = object;
        this.position = position;

        member member = (member) object;
        personal personal = member.getPersonal();
        videoPush videoPush = member.getVideoPush();
        if (member.getVip() == 1) {
            name.setTextColor(context.getResources().getColor(R.color.colorAccent));
            svip.setVisibility(View.VISIBLE);
        } else {
            name.setTextColor(context.getResources().getColor(R.color.teal006));
            svip.setVisibility(View.GONE);
        }
        name.setText(member.getTruename());
        tv1.setText(appgetCity(member, personal, ii));
        name2.setVisibility(member.getStatus() == 2 ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(member.getPicture())) {
            int iicons = member.getSex() == Constants.TENCENT ? R.mipmap.a1 : R.mipmap.a2;
            ImageLoadHelper.glideShowCornerImageWithUrl(context, iicons, icon);
        } else {
            ImageLoadHelper.glideShowCornerImageWithUrl(context, member.getPicture(), icon);
        }
        online.setVisibility(member.getOnline() == 1 ? View.VISIBLE : View.GONE);
        if (this.paymnets != null) {
            gzt.setOnClickListener(v -> paymnets.status(this.position));
        }
        if (samapLocation != null) {
            String jwd = lbsamap.scalculateLineDistance(samapLocation, personal != null ? (!TextUtils.isEmpty(personal.getJwd()) ? personal.getJwd() : member.getJwd()) : member.getJwd());

            if (videoPush != null) {
                if (videoPush.getPush1() == 0) {
                    tv3.setText(context.getString(R.string.gpsdw));
                } else {
                    tv3.setText(TextUtils.isEmpty(jwd) ? context.getString(R.string.tv_onet2_msg_onlinv) + "" : jwd);
                }
            } else {
                tv3.setText(TextUtils.isEmpty(jwd) ? context.getString(R.string.tv_onet2_msg_onlinv) + "" : jwd);
            }


        }
    }

    private String appgetCity(member member, personal personal, int type) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(member.getProvince())) {
            switch (type) {
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
                switch (type) {
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
                    sb.append(personal.getAge() + " ???");
                } else {
                    sb.append(" | " + personal.getAge() + " ???");
                }
            }
            if (!TextUtils.isEmpty(String.valueOf(personal.getHeight()))) {
                sb.append(" | " + personal.getHeight() + " cm");
            }
            if (!TextUtils.isEmpty(personal.getOccupation())) {
                sb.append(" | " + personal.getOccupation());
            }
            tv2.setText(personal.getPesigntext());
        }
        return sb.toString();
    }

}

