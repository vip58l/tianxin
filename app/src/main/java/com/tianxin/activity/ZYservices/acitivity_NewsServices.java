package com.tianxin.activity.ZYservices;

import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Config_list;
import com.tianxin.Util.Config;
import com.tianxin.dialog.dialog_item_cn1;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_item_recy_view;
import com.tianxin.getHandler.PostModule;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.getservice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 发布服务
 */
public class acitivity_NewsServices extends BasActivity2 implements TextWatcher, Paymnets {
    String TAG = "acitivity_NewsServices";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.msg)
    EditText msg;
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.second)
    TextView second;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.standard)
    TextView standard;
    service service;
    UserInfo userInfo;
    TextView tv;

    @Override
    protected int getview() {
        return R.layout.activity_acitivity__news_services;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg3));
        itemback.righttext.setText(getString(R.string.tv_msg1));
        itemback.righttext.setVisibility(View.VISIBLE);
        service = new service();
        service.setSecond(1);
        service.setDuration(30);
        userInfo = UserInfo.getInstance();
        money.addTextChangedListener(this);

    }

    @Override
    public void initData() {
        configmsg();
    }

    @OnClick({R.id.boobutton, R.id.myoney1, R.id.myoney2, R.id.myoney3, R.id.myoney4, R.id.layout4, R.id.layout5, R.id.lookup, R.id.tv3title})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.myoney1:
            case R.id.myoney2:
            case R.id.myoney3:
            case R.id.myoney4:
                money.setText(null);
                mymoney(v);
                break;
            case R.id.boobutton:
                gettitles();
                break;
            case R.id.layout4:
                selectsecond();
                break;
            case R.id.layout5:
                selectduration();
                break;
            case R.id.lookup:
                dialog_item_recy_view.dialogshow(this, userInfo.getUserId());
                break;
            case R.id.tv3title:
                startActivity(new Intent(this, activity_servicetitle.class));
                break;
        }


    }

    /**
     * 发送数据写入
     */
    public void gettitles() {
        service.setTitle(TextUtils.isEmpty(title.getText().toString().trim()) ? "" : title.getText().toString().trim());
        service.setMsg(TextUtils.isEmpty(msg.getText().toString().trim()) ? "" : msg.getText().toString().trim());
        if (!TextUtils.isEmpty(money.getText().toString().trim())) {
            service.setMoney(Double.parseDouble(money.getText().toString().trim()));
        }
        if (TextUtils.isEmpty(service.getTitle())) {
            Toashow.show("请输入服务标题");
            title.setEnabled(true);
            title.setFocusable(true);
            title.setFocusableInTouchMode(true);
            title.requestFocus();
            title.setSelection(title.length());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return;
        }
        if (TextUtils.isEmpty(service.getMsg())) {
            Toashow.show("请输入服务内容简介");
            msg.setEnabled(true);
            msg.setFocusable(true);
            msg.setFocusableInTouchMode(true);
            msg.requestFocus();
            msg.setSelection(msg.length());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

            return;
        }
        if (service.getMoney() <= 0) {
            Toashow.show(getString(R.string.Toast_msg1));
            return;
        }
        if (service.getSecond() <= 0) {
            Toashow.show(getString(R.string.Toast_msg2));
            return;
        }
        if (service.getDuration() <= 0) {
            Toashow.show(getString(R.string.Toast_msg3));
            return;
        }

        getservice g = new getservice();
        g.setMoney(String.valueOf(service.getMoney()));
        g.setSecond(service.getSecond());
        g.setTitle(service.getTitle());
        g.setDuration(service.getDuration());
        g.setMsg(service.getMsg());
        g.setTitle(service.getTitle());
        g.setUserid(Integer.valueOf(userInfo.getUserId()));
        dialog_item_cn1 dialogItemCn1 = dialog_item_cn1.dialogitemcn1(this, g, 1);
        dialogItemCn1.setPaymnets(new Paymnets() {
            @Override
            public void onSuccess() {
                PostrequestBody();
            }
        });
    }

    /**
     * 发送到服务端
     */
    private void PostrequestBody() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("title", service.getTitle())
                .add("msg", service.getMsg())
                .add("money", String.valueOf(service.getMoney()))
                .add("second", String.valueOf(service.getSecond()))
                .add("duration", String.valueOf(service.getDuration()))
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(BuildConfig.HTTP_API + "/addservice", requestBody, this);
    }

    private void mymoney(View v) {
        if (tv != null) {
            tv.setBackground(getDrawable(R.drawable.diis_bg3));
        }
        tv = (TextView) v;
        tv.setBackground(getDrawable(R.drawable.diis_bg2));
        String trim = tv.getText().toString().trim();
        service.setMoney(Double.parseDouble(trim));
    }

    @Override
    public void OnEorr() {

    }

    class service {
        private int id;
        private String title;
        private String msg;
        private String standard;
        private double money;
        private int second;
        private int duration;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

    }

    private void selectsecond() {
        List<Integer> mOptionsItems1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mOptionsItems1.add(i + 1);
        }
        initTimePicker(mOptionsItems1, 1);
    }

    private void selectduration() {
        List<Integer> mOptionsItems1 = new ArrayList<>();
        int conunt = 0;
        for (int i = 0; i < 8; i++) {
            conunt = conunt + 30;
            mOptionsItems1.add(conunt);
        }
        initTimePicker(mOptionsItems1, 2);
    }

    /**
     * 条件选择器
     */
    private void initTimePicker(List<Integer> mOptionsItems1, int result) {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                Log.d(TAG, "options1:" + options1);
//                Log.d(TAG, "options2:" + options2);
//                Log.d(TAG, "options3:" + options3);
                setselecttext(mOptionsItems1.get(options1), result);


            }
        }).setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText("选择内容")
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }

    private void setselecttext(int text, int position) {
        switch (position) {
            case 1:
                service.setSecond(text);
                second.setText(text + "/次");
                break;
            case 2:
                service.setDuration(text);
                duration.setText(text + "/分钟");

                break;
        }

    }

    private void configmsg() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
    PostModule.getModule(BuildConfig.HTTP_API + "/configmsg?type=3", new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Config_list configmsg = new Gson().fromJson(date, Config_list.class);
                    standard.setText(Html.fromHtml(configmsg.getMsg()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    private void myfindViewById() {
        findViewById(R.id.myoney1).setBackground(getDrawable(R.drawable.diis_bg3));
        findViewById(R.id.myoney2).setBackground(getDrawable(R.drawable.diis_bg3));
        findViewById(R.id.myoney3).setBackground(getDrawable(R.drawable.diis_bg3));
        findViewById(R.id.myoney4).setBackground(getDrawable(R.drawable.diis_bg3));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "文本更改前" + s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "文本更改时:" + s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "文本更改后: " + s);
        try {
            myfindViewById();
            int si = Integer.parseInt(s.toString().trim());
            if (!TextUtils.isEmpty(s.toString().trim()) && si > 0) {
                service.setMoney(si);
            }
        } catch (Exception e) {
            e.printStackTrace();
            service.setMoney(0);
        }

    }

    @Override
    public void success(String date) {
        try {
            Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
            Toashow.show(mesresult.getMsg());
            if (mesresult.getStatus().equals("1")) {
                title.setText(null);
                msg.setText(null);
                service.setMoney(0);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void fall(int code) {

    }
}