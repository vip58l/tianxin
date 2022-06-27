package com.tianxin.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import com.tianxin.activity.Memberverify.activity_livebroadcast;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.ZYservices.activity_photo_album;
import com.tianxin.activity.matching.activity_thesamecity_speed;
import com.tianxin.activity.meun.MEUN_MainActivity;
import com.tianxin.adapter.Radapter;
import com.tianxin.IMtencent.scenes.LiveRoomAnchorActivity;
import com.tianxin.utils.AES.AES;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Test.live_animation;
import com.tianxin.activity.Withdrawal.Withdrawals;
import com.tianxin.activity.ZYservices.acitivity_NewsServices;
import com.tianxin.activity.ZYservices.activity_categories;
import com.tianxin.activity.ZYservices.activity_courses;
import com.tianxin.activity.ZYservices.activity_editing_more;
import com.tianxin.activity.ZYservices.activity_servicetitle;
import com.tianxin.activity.DouYing.activity_jsonvideo;
import com.tianxin.activity.edit.activity_nickname2;
import com.tianxin.activity.edit.activity_uploadavatar;
import com.tianxin.activity.matching.activity_audio_speed;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_item_show_home;
import com.tianxin.dialog.dialog_realname;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.getHandler.PostModule;
import com.tianxin.utils.Constants;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.gethelp;
import com.tencent.opensource.model.navigation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 咨询服务主页
 */
public class activity_home extends BasActivity2 {

    @BindView(R.id.bgsimg)
    ImageView bgsimg;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.home)
    TextView home;
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
    @BindView(R.id.tv_show)
    TextView tv_show;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    String TAG = "activity_home";
    @Override
    protected int getview() {
        return R.layout.activity_home;
    }

    @Override
    public void iniview() {
        radapter = new Radapter(this, list, Radapter.activity_home);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int result) {
                startActivitygo(result);
            }
        });
        recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerview.setAdapter(radapter);
        recyclerview.setFocusable(false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);


        //跳转的Activity
        clslist.add(acitivity_NewsServices.class);//发布服务
        clslist.add(activity_courses.class);      //发布课程
        clslist.add(activity_servicetitle.class); //添加简介
        clslist.add(activity_categories.class);   //添加分类
        clslist.add(Withdrawals.class);           //我的提现
        clslist.add(activity_photo_album.class);  //发布课程
        clslist.add(activity_sevaluate.class);    //我的评价
        clslist.add(activity_mylikeyou.class);    //关注我的人
        clslist.add(activity_nickname2.class);    //个性签名
        clslist.add(activity_jsonvideo.class);    //抖音解析
        clslist.add(activity_audio_speed.class);  //美女1v1匹配
        clslist.add(activity_thesamecity_speed.class);     //电话拨打
        clslist.add(MEUN_MainActivity.class);      //这里一个主页
        clslist.add(live_animation.class);        //直播动画
        setTextUtilsvide();


    }

    @Override
    public void initData() {
        PostModule.getModule(BuildConfig.HTTP_API + "/homegethelp?userid=" + userInfo.getUserId()+"&token="+userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    gethelp help = new Gson().fromJson(date, gethelp.class);
                    if (help.getId() > 0) {
                        a1.setText(TextUtils.isEmpty(String.valueOf(help.getDeqy())) ? "0" : String.valueOf(help.getDeqy()));
                        a2.setText(String.valueOf(help.getCertificates()));
                        a3.setText(String.valueOf(help.getPeople()));
                        a4.setText(String.valueOf(help.getDuration()));
                        a5.setText(String.format("%s人评论", help.getEvaluate()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
        PostModule.getModule(BuildConfig.HTTP_API + "/navigation?type=1", new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<navigation> navigations = JsonUitl.stringToList(date, navigation.class);
                    list.addAll(navigations);
                    radapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    @OnClick({R.id.tv_show, R.id.icon, R.id.layout, R.id.layout2, R.id.user_edit, R.id.natitle})
    public void OnClick(View v) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        switch (v.getId()) {
            case R.id.tv_show:
                Intent intent = new Intent(this, activity_home_page.class);
                intent.putExtra(Constants.USERID, userInfo.getUserId());
                startActivity(intent);
                break;
            case R.id.icon:
                startActivityForResult(new Intent(this, activity_uploadavatar.class), Config.sussess);
                break;
            case R.id.layout:
                startActivityForResult(new Intent(this, activity_sevaluate.class), Config.sussess);

//                Intent intent1 = new Intent(this, activity_servicepj.class);
//                intent1.putExtra(Constants.USERID, userInfo.getUserId());
//                intent1.putExtra(Constants.Orderid,config.getRandomFileName());
//                startActivity(intent1);
                break;
            case R.id.layout2:
                initTimePicker(listage(), certificates);
                break;
            case R.id.user_edit:
                startActivityForResult(new Intent(this, activity_editing_more.class), Config.sussess);
                break;
            case R.id.natitle:
                dialog_item_show_home.showhome(this);
                break;
        }

    }

    /**
     * 跳转指定页
     *
     * @param position
     */
    private void startActivitygo(int position) {
        navigation navigation = (navigation) list.get(position);
        int activity = navigation.getActivity();
        if (activity == 0) {
            if (TextUtils.isEmpty(navigation.getPath())) {
                return;
            }
            Intent intent = new Intent(this, DyWebActivity.class);
            intent.putExtra(Constants.VIDEOURL, navigation.getPath());
            startActivity(intent);
        } else {
            if (position > clslist.size() - 1) {
                return;
            }
            Class<?> cls = (Class<?>) clslist.get(position);
            Intent intent = new Intent(this, cls);
            intent.putExtra(Constants.USERID, userInfo.getUserId());
            intent.putExtra(Constants.POSITION, position);
            if (position == 5) {
                //立即开播
                LiveRoomAnchorActivity.start(this, "");
            } else {
                startActivity(intent);
            }
        }


    }

    @Override
    public void OnEorr() {

    }

    /**
     * 条件选择器
     */
    private void initTimePicker(List<String> mOptionsItems1, int result) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String age = mOptionsItems1.get(options1);
                FormBody(age);
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

    private List<String> listage() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    public void FormBody(String age) {
        String token = AES.getStringkey(userInfo.getUserId());
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("age", age)
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(BuildConfig.HTTP_API + "/addhelp", requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        initData();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            setTextUtilsvide();

            list.clear();
            radapter.notifyDataSetChanged();
            initData();
        }
    }

    public void setTextUtilsvide() {
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Glideload.loadImage(icon, userInfo.getAvatar());
        } else {
            Glideload.loadImage(icon, userInfo.getSex().equals("1") ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        }
        name.setText(!TextUtils.isEmpty(userInfo.getGivenname()) ? userInfo.getGivenname() : userInfo.getName());
        home.setText(String.format(getString(R.string.tv_msg100), getString(R.string.app_name), userInfo.getUserId()));
        tv_show.setText(String.format(getString(R.string.tv_msg99), getString(R.string.app_name)));
        switch (userInfo.getState()) {
            case 0:
            case 1:
                bgsimg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog_realname.Listener(activity_home.this, new Paymnets() {
                            @Override
                            public void onError() {
                                Log.d(TAG, "onError: ");
                                finish();
                            }

                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(activity_home.this, activity_livebroadcast.class);
                                intent.putExtra(com.tianxin.Util.Constants.TYPE, 1);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onRefresh() {
                                Log.d(TAG, "onRefresh: ");
                                finish();
                            }
                        });
                    }
                }, 200);
                break;
            case 2:
                break;
            default:
                Toashow.show(getString(R.string.tv_msg21));
                finish();
                break;
        }

    }
}