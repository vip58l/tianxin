package com.tianxin.activity.picenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_fenxing;
import com.tianxin.listener.Paymnets;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 相亲版个人主页
 */
public class activity_data extends BasActivity2 {
    private static final String TAG = activity_data.class.getName();
    @BindView(R.id.a0)
    TextView a0;
    @BindView(R.id.a1)
    TextView a1;
    @BindView(R.id.a2)
    TextView a2;
    @BindView(R.id.a3)
    TextView a3;
    @BindView(R.id.a4)
    TextView a4;
    @BindView(R.id.a5)
    TextView a5;
    @BindView(R.id.a6)
    TextView a6;
    @BindView(R.id.a7)
    TextView a7;
    @BindView(R.id.a8)
    TextView a8;
    @BindView(R.id.a9)
    TextView a9;
    @BindView(R.id.a10)
    TextView a10;
    @BindView(R.id.a11)
    TextView a11;
    @BindView(R.id.sex_msg)
    TextView sex_msg;
    @BindView(R.id.stitle)
    TextView stitle;
    @BindView(R.id.a22)
    TextView a22;
    @BindView(R.id.chat1)
    TextView chat1;
    @BindView(R.id.chat2)
    TextView chat2;
    @BindView(R.id.chat3)
    TextView chat3;
    @BindView(R.id.chat4)
    TextView chat4;
    @BindView(R.id.chat5)
    TextView chat5;
    @BindView(R.id.image)
    ImageView image;
    personal personal;

    public static void starsetAction(Context context, String tousreid) {
        Intent intent = new Intent(context, activity_data.class);
        intent.putExtra(Constants.USERID, tousreid);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity, 2);
        return R.layout.activity_data;
    }

    @Override
    public void iniview() {
        getuserid = getIntent().getStringExtra(Constants.USERID);
        drawable(chat1, R.mipmap.ar2);
        drawable(chat2, R.mipmap.arp);
        drawable(chat3, R.mipmap.ar3);
        drawable(chat4, R.mipmap.are);
        drawable(chat5, R.mipmap.a_k);
    }

    @Override
    public void initData() {
        //获取主页信息
        datamodule.picenterHashMap(getuserid, paymnets);
        //插入或更新查看记录logs
        datamodule.addlistviewuser(getuserid, null);

    }

    @Override
    @OnClick({R.id.chat0, R.id.chat1, R.id.chat2, R.id.chat3, R.id.chat4, R.id.chat5, R.id.back, R.id.tv_back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.chat0:
                startChatActivity();
                break;
            case R.id.chat1:
                break;
            case R.id.chat2:
                if (TextUtils.isEmpty(member.getWx())) {
                    Dialog_fenxing.getWechatApi(context, member.getWx());
                }
                break;
            case R.id.chat3:
                if (TextUtils.isEmpty(member.getUsername())) {
                    Dialog_fenxing.getWechatApi(context, member.getUsername());
                }
                break;
            case R.id.chat4:
                if (TextUtils.isEmpty(member.getQq())) {
                    Dialog_fenxing.getWechatApi(context, member.getQq());
                }
                break;
            case R.id.chat5:

                startChatActivity(context);
                break;
            case R.id.back:
            case R.id.tv_back:
                finish();
                break;
        }
    }

    private void update() {
        personal = member.getPersonal();
        stitle.setText(!TextUtils.isEmpty(member.getTvname()) ? member.getTvname() : member.getTruename());
        String picture = member.getPicture();
        if (member.getTencent() == 1 && !TextUtils.isEmpty(member.getPicture())) {
            picture = DemoApplication.presignedURL(member.getPicture());
        }
        if (!TextUtils.isEmpty(member.getTvname())) {
            a0.setVisibility(View.VISIBLE);
            a0.setText(String.format(getString(R.string.a17), member.getTvname()));
        } else {
            a0.setVisibility(View.GONE);
        }
        Glideload.loadImage(image, picture);
        sex_msg.setText(member.getSex() == 1 ? getString(R.string.a13) : getString(R.string.a12));
        if (personal != null) {
            String city = "";
            String address = getString(R.string.a18);
            if (!TextUtils.isEmpty(personal.getCity())) {
                city = personal.getCity();
            } else if (!TextUtils.isEmpty(member.getCity())) {
                city = member.getCity();
            } else {
                city = "保密";
            }
            if (!TextUtils.isEmpty(personal.getAddress())) {
                address = personal.getPallow() == 1 ? personal.getAddress() : address;
            } else if (!TextUtils.isEmpty(member.getAddress())) {
                address = member.getAddress();
            }
            a1.setText(String.format(getString(R.string.data_a1), "" + personal.getAge()));
            a2.setText(String.format(getString(R.string.data_a2), "" + personal.getHeight()));
            if (!TextUtils.isEmpty(personal.getEducation())) {
                a3.setText(String.format(getString(R.string.data_a3), "" + personal.getEducation()));
            }
            a4.setText(String.format(getString(R.string.data_a4), "" + personal.getFeeling()));
            if (!TextUtils.isEmpty(personal.getOccupation())) {
                a5.setText(String.format(getString(R.string.data_a5), "" + personal.getOccupation()));
            }
            if (TextUtils.isEmpty(personal.getPree())) {
                a6.setText(String.format(getString(R.string.data_a6), "" + personal.getPree()));
            }
            a7.setText(String.format(getString(R.string.data_a7), "" + city));
            a8.setText(String.format(getString(R.string.data_a8), "" + address));
            if (personal.getAge2() >= 18) {
                a9.setText(String.format(getString(R.string.data_a9), "" + personal.getAge2(), "" + (personal.getAge2() + 10)));
            }
            if (personal.getHeight2() > 0) {
                a10.setText(String.format(getString(R.string.data_a10), "" + personal.getHeight2()));

            }
            if (personal.getWeight() > 0) {
                a22.setText(String.format(getString(R.string.data_a22), "" + personal.getWeight()));
            } else {
                a22.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(personal.getPesigntext2())) {
                a11.setText(personal.getPesigntext2());
            }
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 回调监听事件
     */
    private Paymnets paymnets = new Paymnets() {

        @Override
        public void fall(int code) {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(Object object) {
            member = (com.tencent.opensource.model.member) object;
            update();
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void ToKen(String msg) {
            Paymnets.super.ToKen(msg);

        }
    };

    private void drawable(TextView view, int id) {
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(10, 10, 10, 10);
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, drawable, null, null);
    }

    /**
     * 这里需要用户上传头像才能聊天
     */
    private void startChatActivity() {
        if (member == null) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg132));
            return;
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(getuserid);
        chatInfo.setChatName(member.getTruename());
        chatInfo.setIconUrlList(TextUtils.isEmpty(member.getPicture()) ? "" : member.getPicture());

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 这里需要用户上传头像才能聊天
     */
    private void startChatActivity(Context context) {
        if (member == null) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        if (member.getMatchmaker() == 0) {
            Toashow.show(String.format(getString(R.string.a15)));
            return;
        }

        Toashow.show(String.format(getString(R.string.a14)));
        if (userInfo.getUserId().equals(String.valueOf(member.getMatchmaker()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg132));
            return;
        }
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setChatName(getString(R.string.a16));
        chatInfo.setId(String.valueOf(member.getMatchmaker()));
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}