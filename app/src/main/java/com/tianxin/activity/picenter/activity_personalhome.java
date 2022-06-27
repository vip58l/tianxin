package com.tianxin.activity.picenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.zxing.activity.CaptureActivity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Fragment.page3.fragment.page3_1;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.SystemUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.Memberverify.activity_Namecenter;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.activity_album;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.activity.picenter.fragment.per1;
import com.tianxin.activity.picenter.fragment.per3;
import com.tianxin.activity.picenter.fragment.per4;
import com.tianxin.activity.picenter.widget.widgetactivity;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_item_qrcode;
import com.tianxin.dialog.dialog_item_rs;
import com.tianxin.dialog.dialog_success;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.item_chid_play;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.personal;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 抖音版个人主页
 */
public class activity_personalhome extends BasActivity2 {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.dfdfkldfkl)
    TextView dfdfkldfkl;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.back)
    ImageView authsdk_return_bg;
    @BindView(R.id.circleimageview)
    ImageView circleimageview;
    @BindView(R.id.widet1)
    widgetactivity widet1;
    @BindView(R.id.widet2)
    widgetactivity widet2;
    @BindView(R.id.widet3)
    widgetactivity widet3;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    setViewPager FragmenViewPager;
    String getuserid;

    public static void starsetAction(Context context, String getuserid) {
        Intent starter = new Intent(context, activity_personalhome.class);
        starter.putExtra(Constants.USERID, getuserid);
        context.startActivity(starter);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        return R.layout.activity_personalhome;
    }

    @Override
    public void iniview() {
        getuserid = getIntent().getStringExtra(Constants.USERID);
        viewPager.setAdapter(FragmenViewPager = new setViewPager(getSupportFragmentManager(), getFragmentlist(), setViewPager.picenter));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Tabtext(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null); //设置自定义视图
            }

            //在未选中的选项卡上
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //在选项卡上重新选择
            }
        });
        Tabtext(tabLayout.getTabAt(0));
        message.setSelected(true);
        if (!TextUtils.isEmpty(getuserid)) {
            if (getuserid.equals(UserInfo.getInstance().getUserId())) {
                dfdfkldfkl.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void initData() {
        //获取主页信息
        datamodule.picenterHashMap(getuserid, paymnets);
        //插入或更新查看记录logs
        datamodule.addlistviewuser(getuserid, null);

        boolean checkpermissions = ActivityLocation.checkpermissions(activity);//申请定位权限
        if (mapLocation == null && checkpermissions) {
            lbsamap.getmyLocation(callback);
        }

        //判断封号或者是否需要实名认证
        switch (userInfo.getState()) {
            case 0:
            case 1:
                //要求你实名认证
                if (userInfo.getReale() == 1 && !userInfo.getUserId().equals(getuserid)) {
                    dialog_success.show(context, new Paymnets() {
                        @Override
                        public void onSuccess() {
                            activity_Namecenter.starsetAction(context);
                        }

                        @Override
                        public void onFail() {
                            finish();
                        }
                    });
                }
                break;
            case 3:
                //封号提示
                dialog_Blocked.myshow(context);
                break;
        }
    }

    @Override
    @OnClick({R.id.back, R.id.RefreshL, R.id.dfdfkldfkl, R.id.chat0})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.dfdfkldfkl:
                //关注成功或取消关注
                datamodule.gofollowlist(getuserid, paymnets);
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.RefreshL:
                //举报刷新
                dialog_item_rs.dialogitemrs(context, paymnets).sethideshow(getuserid.equals(userInfo.getUserId()) ? View.VISIBLE : View.GONE);
                break;
            case R.id.chat0:
                startChatActivity();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }


    /**
     * 返回相关分类内容
     *
     * @return
     */
    private List<Fragment> getFragmentlist() {
        if (fragments != null) {
            fragments.clear();
        }
        fragments = new ArrayList<>();
        fragments.add(per1.perview(getuserid));                 //资料
        fragments.add(page3_1.showfmessage(2, getuserid)); //动态
        fragments.add(per3.perview(getuserid));                 //视频
        fragments.add(per4.perview(getuserid));                 //像册
        return fragments;
    }

    /**
     * 回调监听事件
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            member = (com.tencent.opensource.model.member) object;
            Glideload.loadImage(circleimageview, member.getPicture());
            Glideload.loadImage(image, member.getPicture(), 10, 25);
            widet1.setText(member.getMyconun());
            widet2.setText(member.getTacount());
            widet3.setText(member.getGivecount());
            name.setText(member.getTruename());
            tv_id.setText(String.format(getString(R.string.tm152) + "", member.getId()));
            if (member.getPersonal() != null) {
                personal personal = member.getPersonal();
                message.setText(personal.getPesigntext());

                if (TextUtils.isEmpty(personal.getPree())) {
                    //      telid.setText(String.format("ID:%s", member.getId()));
                } else {
                    // telid.setText(String.format(getString(R.string.tm148) + "", member.getId(), personal.getFeeling()));
                }

            }
            if (member.getFollowlist() != null) {
                dfdfkldfkl.setBackground(context.getDrawable(R.drawable.acitvity011));
                dfdfkldfkl.setText("已关注");
            }

        }

        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void ToKen(String msg) {
            activity_personalhome.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void status(int position) {
            switch (position) {
                case 1:
                    //刷新数据
                    initData();
                    break;
                case 2:
                    //编辑资料
                    startActivity(new Intent(context, activity_updateedit.class));
                    break;
                case 3:
                    //相册浏览
                    startActivity(new Intent(context, activity_album.class).putExtra(Constants.USERID, getuserid));
                    break;
                case 4:
                    //举报消息
                    reportUser();
                    break;
                case 6:
                    //分享
                    dialog_item_qrcode.qrcodes(context, member);
                    break;
                case 7:
                    //扫码
                    if (checkinidate()) {
                        CaptureActivity();
                    }
                    break;
            }
        }

        @Override
        public void payens() {
            member.setTacount(member.getTacount() + 1);
            Toashow.show(getString(R.string.gzok));
            dfdfkldfkl.setText("已关注");
            dfdfkldfkl.setBackground(context.getDrawable(R.drawable.acitvity02));
            widet2.setText(member.getTacount()); //粉丝
        }

        @Override
        public void onError() {
            couneroo = member.getTacount() - 1;
            couneroo = couneroo <= 0 ? 0 : couneroo;
            member.setTacount(couneroo);
            widet2.setText(member.getTacount()); //粉丝
            Toashow.show(getString(R.string.gzon));
            dfdfkldfkl.setBackground(context.getDrawable(R.drawable.activity011));
            dfdfkldfkl.setText("关注");
        }

    };

    /**
     * 举报投诉
     */
    public void reportUser() {
        videolist videolist = new videolist();
        videolist.setUserid(getuserid);
        videolist.setId(getString(R.string.tv_msg237) + getuserid);
        item_chid_play.reportUser(context, videolist, 2);
    }

    /**
     * 检查写入权限和访问相册权限
     */
    private boolean checkinidate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);                            //相机权限
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);            //SD卡写入
            for (String permission : list) {
                int star = ContextCompat.checkSelfPermission(context, permission);
                if (star != PackageManager.PERMISSION_GRANTED) {
                    SystemUtil.getPermission(activity, list, Constants.REQUEST_CODE);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 打开CaptureActivity扫一扫
     */
    private void CaptureActivity() {
        startActivityForResult(new Intent(context, CaptureActivity.class), Constants.REQUEST_CODE);
    }

    /**
     * 显示扫码结果
     *
     * @param result
     */
    private void setCapture(String result) {
        boolean b1 = result.toLowerCase().startsWith("http://");
        boolean b2 = result.toLowerCase().startsWith("https://");
        boolean b3 = result.toLowerCase().endsWith(".apk");
        if (b1 || b2) {
            //打开浏览器
            DyWebActivity.starAction(context, result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (String permission : permissions) {
            int granted = ContextCompat.checkSelfPermission(context, permission);
            if (granted != PackageManager.PERMISSION_GRANTED) {
                Toashow.show(getString(R.string.ts_dialog_call));
                return;
            }
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE:
                CaptureActivity();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            initData();
        }

        if (Constants.REQUEST_CODE == requestCode && data != null) {
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            setCapture(result);
        }
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
        if (userInfo.getSex().equals(String.valueOf(member.getSex()))) {
            Toashow.show(getString(R.string.tmcaht));
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


}