package com.tianxin.activity.ZYservices;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianxin.utils.AES.Resultinfo;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.activity.activity_sevaluate;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.PostModule;
import com.tianxin.utils.Constants;
import com.tianxin.widget.FlowLayout;
import com.tianxin.widget.itembackTopbr;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.gethelp;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.navigation;
import com.tencent.qcloud.costransferpractice.utils.JsonUitl;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 评论提交
 */
public class activity_servicepj extends BasActivity2 {
    private static final String TAG = "activity_servicepj";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.bg_img)
    ImageView bg_img;
    @BindView(R.id.FlowLayout)
    FlowLayout mFlowLayout;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.user)
    ImageView user;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.name2)
    TextView name2;
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
    String[] data = {"Java", "Android", "Ios", "前端", "C++", "软件工程师", "高级工程师", "数据库开发", "项目经理", "美工", "PHP", "数据分析", "网络工程师", "网站编辑", "Go"};
    String Orderid;
    Map<String, String> params = new HashMap<String, String>();


    @Override
    protected int getview() {
        return R.layout.activity_servicepj;
    }

    @Override
    public void iniview() {
        getuserid = getIntent().getStringExtra(Constants.USERID);
        Orderid = getIntent().getStringExtra(Constants.Orderid);
        userInfo = UserInfo.getInstance();
        itemback.settitle(getString(R.string.tv_msg101));
        itemback.contertext.setTextColor(getResources().getColor(R.color.white));
        itemback.tvback.setTextColor(getResources().getColor(R.color.white));
        itemback.setHaidtopBackgroundColor(getResources().getColor(R.color.full_transparent));

        itemback.setIv_back_img(R.mipmap.authsdk_return_bg);
        //Glideload.loadImage(user, R.mipmap.room_change_bg);
        Glideload.loadImage(bg_img, R.mipmap.room_change_bg, 10, 40);
    }

    @Override
    public void initData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        totalPage++;
    PostModule.getModule(BuildConfig.HTTP_API + "/listevaltype?page=" + totalPage, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<navigation> navigations = JsonUitl.stringToList(date, navigation.class);
                    listevaltype(navigations);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {

            }
        });
    PostModule.getModule(String.format(BuildConfig.HTTP_API + "/posmember?userid=%s&touserid=%s&token=", userInfo.getUserId(), getuserid,userInfo.getToken()), new Paymnets() {
            @Override
            public void success(String str) {
                try {
                    String decrypt = Resultinfo.decrypt(str);
                    member = new Gson().fromJson(decrypt, member.class);
                    showmember();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void fall(int code) {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali));
            }
        });

    }

    /**
     * 读取网络数据
     *
     * @param navigations
     */
    public void listevaltype(List<navigation> navigations) {
        for (navigation navigation : navigations) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_flowlayout, mFlowLayout, false);
            tv.setOnClickListener(this::OnClick);
            tv.setText(navigation.getTitle());
            tv.setTag(navigation);
            mFlowLayout.addView(tv);
        }
    }

    /**
     * 提交到服务端
     */
    public void evaluateadd() {
        if (member == null) {
            Toashow.show(getString(R.string.eorrfali3));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            Toashow.show(getString(R.string.tv_msg125));
            return;
        }
        StringBuffer sb = new StringBuffer();
        int is = 0;
        if (params.size() > 0) {
            for (String s : params.keySet()) {
                is++;
                sb.append(is < params.size() ? s + "," : s);
            }
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", getuserid)
                .add("customerid", userInfo.getUserId())
                .add("content", editText.getText().toString().trim())
                .add("type", "1")
                .add("orderid", Orderid)
                .add("token", userInfo.getToken())
                .add("evaltitle", TextUtils.isEmpty(sb.toString()) ? "" : sb.toString())
                .build();
        PostModule.postModule(BuildConfig.HTTP_API + "/evaluateadd", requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gostartActivity();
                            }
                        }, 1000);
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

    /**
     * 跳转评价页
     */
    private void gostartActivity() {
        startActivity(new Intent(this, activity_sevaluate.class).putExtra(Constants.USERID, getuserid));
        finish();
    }

    @Override
    @OnClick({R.id.tvsendbtn,R.id.sendbtn})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tvsendbtn:
                evaluateadd();
                break;
            default:
                paranselect(v);
                break;
            case R.id.sendbtn:
                startChatActivity();
                break;

        }


    }

    @Override
    public void OnEorr() {

    }

    public void paranselect(View v) {
        TextView textView = (TextView) v;
        navigation navigation = (com.tencent.opensource.model.navigation) textView.getTag();
        if (params.size() > 7 && navigation.activity == 0) {
            Toashow.show(getString(R.string.tv_msg102));
            return;
        }
        textView.setTextColor(navigation.activity == 1 ? getResources().getColor(R.color.home) : getResources().getColor(R.color.colorAccent));
        textView.setBackground(navigation.activity == 1 ? getDrawable(R.drawable.bg_flowlayout) : getDrawable(R.drawable.diis_bg7));
        if (navigation.activity == 0) {
            params.put(navigation.getTitle(), navigation.getTitle());
        } else {
            params.remove(navigation.getTitle());
        }
        navigation.activity = navigation.activity == 0 ? 1 : 0;
    }

    private void showmember() {
        name1.setText(TextUtils.isEmpty(member.getTvname()) ? null : member.getTvname());
        name2.setText(member.getProvince() + "." + member.getCity());
        //itemback.settitle(TextUtils.isEmpty(member.getTruename()) ? getString(R.string.tv_msg101) : member.getTruename());
        String path = member.getTencent() == 1 ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture();
        Glideload.loadImage(bg_img, path, 10, 40);
        Glideload.loadImage(user, path);
        sethelp(member.getHelp());
    }

    /**
     * 更新UI
     */
    private void sethelp(gethelp gethelp) {
        if (gethelp == null) {
            return;
        }
        a1.setText(String.valueOf(gethelp.getDeqy()));
        a2.setText(String.valueOf(gethelp.getCertificates()));
        a3.setText(String.valueOf(gethelp.getPeople()));
        a4.setText(String.valueOf(gethelp.getDuration()));
        a5.setText(String.format("%s人评价", gethelp.getEvaluate()));
    }

    /**
     * 测试取值
     */
    public void mytest() {
        for (String s : params.keySet()) {
            Log.d(TAG, "test: " + s);
        }


        //通过Map.entrySet遍历key和value
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
        }


        for (String value : params.values()) {
            Log.d(TAG, "mytest: " + value);
        }

    }


    /**
     * 转到消息聊天
     */
    private void startChatActivity() {

        if (member == null) {
            ToastUtil.toastLongMessage("获取数据失败 请刷新后重试...");
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage("无法发起消息，请刷新后重试...");
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage("自己不能给自己发消息");
            return;
        }
        if (member.getTruename().isEmpty()) {
            member.setTruename(String.valueOf(member.getId()));
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(getuserid);
        chatInfo.setChatName(member.getTruename());
        Intent intent = new Intent(DemoApplication.instance(), ChatActivity.class);
        intent.putExtra(com.tianxin.Util.Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);
    }


}
