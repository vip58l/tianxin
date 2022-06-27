/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/29 0029
 */


package com.tencent.opensource.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tencent.opensource.AES.Resultinfo;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.actionSheetDialog;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import java.util.HashMap;

public class dialogfoll extends BottomSheetDialog implements View.OnClickListener {
    ImageView iv_avatar;       //头像
    TextView usersms;          //其他
    TextView report_user;      //举报
    TextView name;             //名称
    TextView age;              //年龄
    TextView mapgps;           //地址
    TextView follow;           //关注
    TextView expanded;         //签名

    TextView followstate;      //关注
    TextView useridtag;        //@TA
    TextView et_user_name;     //主页
    String touserid;           //对方的ID
    public String TAG = dialogfoll.class.getSimpleName();
    private final UserInfo userInfo;
    private com.tencent.opensource.model.member member;
    private int sex;
    private Gson gson = new Gson();
    private DefaultGiftAdapter defaultGiftAdapter = new DefaultGiftAdapter();
    private com.tencent.opensource.listener.Callback callback;

    public static void mydialogfoll(Context context, String muserid, com.tencent.opensource.listener.Callback callback) {
        dialogfoll dialogfoll = new dialogfoll(context, muserid, callback);
        dialogfoll.show();
    }

    public dialogfoll(@NonNull Context context, String touserid, com.tencent.opensource.listener.Callback callback) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.live_dialog);
        this.callback = callback;
        this.touserid = touserid;
        this.userInfo = UserInfo.getInstance();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        iniview();
    }

    private void iniview() {
        iv_avatar = findViewById(R.id.iv_avatar);
        usersms = findViewById(R.id.usersms);
        report_user = findViewById(R.id.report_user);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        mapgps = findViewById(R.id.mapgps);
        follow = findViewById(R.id.follow);
        expanded = findViewById(R.id.expanded);
        followstate = findViewById(R.id.followstate);
        useridtag = findViewById(R.id.useridtag);
        et_user_name = findViewById(R.id.et_user_name);
        usersms = findViewById(R.id.usersms);

        iv_avatar.setOnClickListener(this);
        report_user.setOnClickListener(this);
        followstate.setOnClickListener(this);
        useridtag.setOnClickListener(this);
        et_user_name.setOnClickListener(this);
        member();
    }

    /**
     * 获取用户个人详情页
     */
    private void member() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.USERID, userInfo.getUserId());
        hashMap.put(Constants.TOKEN, userInfo.getToken());
        hashMap.put(Constants.TOUSERID, touserid);
        String parameter = "/member?" + DefaultGiftAdapter.getMap(hashMap, 3);
        defaultGiftAdapter.getHttpGet(parameter, new DefaultGiftAdapter.callback() {
            @Override
            public void success(String response) {
                try {
                    Mesresult mesresult = gson.fromJson(response, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        String decrypt = Resultinfo.decrypt(mesresult.getData());
                        member = gson.fromJson(decrypt, member.class);
                        iv_avatar.post(new Runnable() {
                            @Override
                            public void run() {
                                setUsersms(member);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });
    }

    /**
     * 关注对方
     */
    private void gofollowlist() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Constants.USERID, userInfo.getUserId());
        hashMap.put(Constants.TOKEN, userInfo.getToken());
        hashMap.put(Constants.TID, touserid);
        String parameter = "/gofollowlist?" + DefaultGiftAdapter.getMap(hashMap, 3);
        defaultGiftAdapter.getHttpGet(parameter, new DefaultGiftAdapter.callback() {
            @Override
            public void success(final String response) {
                try {
                    iv_avatar.post(new Runnable() {
                        @Override
                        public void run() {
                            Mesresult mesresult = new Gson().fromJson(response, Mesresult.class);
                            if (mesresult.getStatus().equals("1")) {
                                followstate.setText("已关注");
                                followstate.setTextColor(getContext().getResources().getColor(R.color.live_light_black));
                            } else {
                                followstate.setText("+关注");
                                followstate.setTextColor(getContext().getResources().getColor(R.color.live_primary));
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if (member == null) {
            return;
        }
        if (iv_avatar.equals(v) || et_user_name.equals(v)) {
            startmActivity(getContext(), String.valueOf(member.getId()));
        } else if (report_user.equals(v)) {
            actionSheetDialog.actionSheetshow(getContext(), String.valueOf(member.getId()));
            dismiss();
        } else if (followstate.equals(v)) {
            gofollowlist();
        } else if (useridtag.equals(v)) {
            if (callback != null) {
                callback.onSuccess();

            }
            dismiss();
        } else {
            Toast.makeText(getContext(), "点击有误", Toast.LENGTH_SHORT).show();
            dismiss();

        }

    }

    /**
     * 更新头像配置
     *
     * @param member
     */
    public void setUsersms(member member) {
        if (member == null) {
            return;
        }
        if (!TextUtils.isEmpty(member.getTruename())) {
            name.setText(member.getTruename());
        }
        if (!TextUtils.isEmpty(member.getCity())) {
            mapgps.setText(member.getCity());
        }
        if (TextUtils.isEmpty(member.getPicture())) {
            GlideEngine.loadImage(iv_avatar, member.getSex() == 1 ? R.drawable.ic_man_choose : R.drawable.icon_woman_choose);
        } else {
            GlideEngine.loadImage(iv_avatar, member.getPicture());
        }
        personal personal = member.getPersonal();
        followlist followlist = member.getFollowlist();

        if (personal != null) {
            age.setVisibility(personal.getAge() > 0 ? View.VISIBLE : View.GONE);
            age.setText(String.valueOf(personal.getAge()));
            if (!TextUtils.isEmpty(personal.getPesigntext())) {
                expanded.setText(personal.getPesigntext());
            }
            if (!TextUtils.isEmpty(personal.getCity())) {
                mapgps.setText(personal.getCity());
            }
        }

        if (followlist != null) {
            followstate.setBackgroundColor(getContext().getResources().getColor(R.color.white_alpha));
            follow.setText("关注" + member.getTacount());
            followstate.setText("已关注");

        }
    }

    /**
     * 打开个人主页
     *
     * @param context
     * @param userid
     */
    public static void startmActivity(Context context, String userid) {
        Intent intent = new Intent();
        intent.setAction("UIactivity.activity_picenter");
        intent.putExtra(Constants.USERID, userid);
        context.startActivity(intent);
    }


}