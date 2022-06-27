package com.tianxin.activity.ZYservices;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianxin.Module.api.BaseClass;
import com.tianxin.utils.AES.AES;
import com.tianxin.utils.AES.Resultinfo;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.activity.activit_Invitefriends;
import com.tianxin.activity.edit.activity_nickname2;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.Util.log;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.PostModule;
import com.tianxin.widget.LinearmyLinayout;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.gethelp;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class activity_editing_more extends BasActivity2 {
    private static final String TAG = "activity_editing_more";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.linayout1)
    LinearmyLinayout linayout1;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.qianming)
    TextView qianming;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.state)
    TextView state;
    int certificates;
    gethelp help;

    @Override
    protected int getview() {
        return R.layout.activity_editing_more;
    }

    @Override
    public void iniview() {
        userInfo = UserInfo.getInstance();
        itemback.settitle(getString(R.string.tv_msg123));
        itemback.setHaidtopBackgroundColor(true);
        qianming.setText(userInfo.getPesigntext());
        log.d(userInfo.toString());
    }

    @Override
    public void initData() {
        getPaymnet();
    }

    @Override
    @OnClick({R.id.linayout1, R.id.a2, R.id.a3, R.id.a4, R.id.a5, R.id.a6, R.id.a7, R.id.a8})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.linayout1:
                startActivityForResult(new Intent(this, activity_servicetitle.class), Config.sussess);
                break;
            case R.id.a2:
                startActivityForResult(new Intent(this, activit_Invitefriends.class), Config.sussess);
                break;
            case R.id.a3:
                initTimePicker(listage(), certificates, 2);//年限
                break;
            case R.id.a4:
                break;
            case R.id.a5:
                break;
            case R.id.a6:
                initTimePicker(Arrays.asList(getResources().getStringArray(R.array.question)), 0, 1); //名称
                break;
            case R.id.a7:
                initTimePicker(Arrays.asList(getResources().getStringArray(R.array.squestion)), 0, 3); //价格
                break;
            case R.id.a8:
                Intent intent = new Intent(this, activity_nickname2.class);
                intent.putExtra(Constants.POSITION, 13);
                startActivityForResult(intent, Config.sussess);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            getpersonal();
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 条件选择器
     */
    private void initTimePicker(List<String> mOptionsItems1, int result, int type) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String age = mOptionsItems1.get(options1);
                FormBody(age, type);
            }
        }).setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setSelectOptions(result)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText("选择内容")
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }

    public void FormBody(String obj, int i) {
        String token = AES.getStringkey(userInfo.getUserId());
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("nametitle", obj)
                .add("price", obj)
                .add("pic", obj)
                .add("age", obj)
                .add("token", userInfo.getToken())
                .add("type", String.valueOf(i))
                .build();
        PostModule.postModule(BuildConfig.HTTP_API + "/addhelp", requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        getPaymnet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    private List<String> listage() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    public void getPaymnet() {
        PostModule.getModule(BuildConfig.HTTP_API + "/homegethelp?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    help = new Gson().fromJson(date, gethelp.class);
                    if (help != null) {
                        if (!TextUtils.isEmpty(help.getPic())) {
                            state.setText(R.string.livebr_tv3);
                            state.setTextColor(getResources().getColor(R.color.rtc_green_bg));
                        }
                        age.setText(help.getCertificates() + " 年");
                        certificates = help.getCertificates();
                        tv_name.setText(TextUtils.isEmpty(help.getNametitle()) ? "" : help.getNametitle());
                        tv_price.setText(String.format("%s元起", TextUtils.isEmpty(help.getPrice()) ? "0" : help.getPrice()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    //重新绑定数据
    private void getpersonal() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getResources().getString(R.string.eorrfali2));
            return;
        }
        PostModule.getModule(BuildConfig.HTTP_API + "/personal?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    String decrypt = Resultinfo.decrypt(date);
                    //复杂的泛型：T
                    Type objectType = new TypeToken<BaseClass<personal>>() {
                    }.getType();
                    BaseClass baseClass = new Gson().fromJson(decrypt, objectType);
                    List<personal> date1 = baseClass.getData();
                    personal personal = date1.get(0);
                    qianming.setText(personal.getPesigntext());
                    userInfo.setPesigntext(personal.getPesigntext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }


}