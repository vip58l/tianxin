package com.tianxin.activity.picenter;

import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_IMG;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIDEO;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import com.google.zxing.activity.CaptureActivity;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.Fragment.page3.fragment.page3_1;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.IMtencent.chta.ChatActivityMessage;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.activity.Memberverify.activity_Namecenter;
import com.tianxin.activity.Memberverify.activity_livebroadcast;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.picenter.fragment.per1;
import com.tianxin.activity.picenter.fragment.per3;
import com.tianxin.activity.picenter.fragment.per4;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.present;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.activity.LatestNews.activity_trend;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.activity_album;
import com.tianxin.activity.picenter.module.Manager;
import com.tianxin.dialog.Dialog_fenxing;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_buymoney;
import com.tianxin.dialog.dialog_item_qrcode;
import com.tianxin.dialog.dialog_select_wx_phone;
import com.tianxin.dialog.dialog_success;
import com.tianxin.dialog.dialog_video_message;
import com.tianxin.dialog.dialog_videocall;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.widget.item_chid_play;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.videolist;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.Util.Toashow;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.dialog.dialog_item_gift;
import com.tianxin.dialog.dialog_item_rs;
import com.tianxin.widget.Footer;
import com.tianxin.widget.item_view_t1;
import com.tencent.opensource.model.perimg;
import com.tencent.opensource.model.personal;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.SystemUtil;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Loading;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ??????????????????
 */
public class activity_picenter extends BasActivity2 {
    @BindView(R.id.viewPager)
    ViewPager bannerpager;
    @BindView(R.id.v1)
    ViewPager fragmenpage;
    @BindView(R.id.online)
    LinearLayout online;
    @BindView(R.id.tv_muncunt)
    TextView tv_muncunt;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.follow)
    ImageView follow;
    @BindView(R.id.mssge)
    TextView mssge;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.t1)
    item_view_t1 t1;
    @BindView(R.id.footer)
    Footer footer;
    @BindView(R.id.editcontentet)
    TextView editcontentet;
    @BindView(R.id.app_bar)
    AppBarLayout app_bar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsing_toolbar_layout;
    @BindView(R.id.stitle)
    TextView stitle;
    @BindView(R.id.tv_back)
    TextView tv_back;
    @BindView(R.id.telid)
    TextView telid;
    @BindView(R.id.RefreshL)
    ImageView IvRefreshL;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.svip)
    ImageView svip;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.svgaImage)
    SVGAImageView svgaImageView;
    @BindView(R.id.iv_selected_rel)
    RelativeLayout iv_selected_rel;
    @BindView(R.id.chat_view)
    LinearLayout chat_view;

    private pageadapter pageadapter;
    private setViewPager FragmenViewPager;
    private Dialog_Loading dialogLoading = null;
    private static final int fadingHeight = 200;
    private final String TAG = activity_picenter.class.getName();
    private final List<View> list = new ArrayList<>();
    public List<perimg> listimg = new ArrayList<>();
    private List<Fragment> fragmentlist;
    private cs_alipay csAlipay;
    private banner banner;

    private SVGAParser parser;                                       //????????????
    private LinkedList<String> mAnimationUrlList;                    //????????????
    private boolean mIsPlaying;                                      //??????????????????
    String[] stringArray;

    /**
     * ????????????ID
     */
    public static void setActionactivity(Context context, String touserid) {
        //??????????????????
        activity_picenter.open(context, touserid);
        //?????????????????????
        //activity_data.starsetAction(context, touserid);
        //?????????????????????
        //activity_personalhome.starsetAction(context, touserid);
    }


    /**
     * ????????????ID
     *
     * @param context
     * @param userid
     */
    public static void open(Context context, String userid) {
        Intent intent = new Intent(context, activity_picenter.class);
        intent.putExtra(Constants.USERID, userid);
        context.startActivity(intent);

    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity, 2);
        return R.layout.activity_perain;
    }

    @Override
    public void iniview() {
        csAlipay = new cs_alipay(context, paymnets);
        parser = new SVGAParser(context);
        mAnimationUrlList = new LinkedList<>();
        stringArray = getResources().getStringArray(R.array.tabs7);
        getuserid = getIntent().getStringExtra(Constants.USERID);
        mssge.setSelected(true);

        //??????????????????
        bannerpager.setAdapter(pageadapter = new pageadapter(context, list));
        bannerpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int page = i + 1;
                pageshow(page);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:  //??????????????????
                        banner.sendEmptyMessage();
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:  //??????????????????
                        banner.sendEmptyMessageDelayed();
                        break;
                }
            }

        });
        banner = new banner(context, bannerpager, list);

        //Fragment
        fragmenpage.setAdapter(FragmenViewPager = new setViewPager(getSupportFragmentManager(), getFragmentlist(), setViewPager.picenter));
        fragmenpage.setOffscreenPageLimit(4);
        fragmenpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                if (getuserid.equals(userInfo.getUserId())) {
                    footer.setImagetext(position + 1, stringArray[position]);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.setupWithViewPager(fragmenpage);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Tabtext(tab);
//                ????????????????????????


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null); //?????????????????????
            }

            //???????????????????????????
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //???????????????????????????
            }
        });
        Tabtext(tab.getTabAt(0));
        if (userInfo.getUserId().equals(getuserid)) {
            follow.setVisibility(View.GONE);
            iv_selected_rel.setVisibility(View.GONE);
            editcontentet.setVisibility(View.VISIBLE);
        } else {
            iv_selected_rel.setVisibility(View.VISIBLE);
            editcontentet.setVisibility(View.GONE);
            follow.setVisibility(View.VISIBLE);
        }
        app_bar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //????????????
                    stitle.animate().alpha(0f).setDuration(200).start();
                    IvRefreshL.setImageResource(R.mipmap.dy_play_more);
                    back.setImageResource(R.mipmap.authsdk_return_bg);
                    tv_back.setTextColor(getResources().getColor(R.color.white));

                } else if (state == State.COLLAPSED) {
                    //????????????
                    stitle.animate().alpha(1f).setDuration(200).start();
                    IvRefreshL.setImageResource(R.mipmap.book_setting);
                    back.setImageResource(R.mipmap.icon_lent);
                    tv_back.setTextColor(getResources().getColor(R.color.c_font_2));

                } else {
                    //????????????
                    Log.d(TAG, "onStateChanged: ????????????33");
                }
            }

            @Override
            protected void finalize() throws Throwable {

            }
        });
    }

    @Override
    public void initData() {
        //????????????????????????
        datamodule.picenterHashMap(getuserid, paymnets);
        //???????????????????????????logs
        datamodule.addlistviewuser(getuserid, null);

        boolean checkpermissions = ActivityLocation.checkpermissions(activity);//??????????????????
        if (mapLocation == null && checkpermissions) {
            lbsamap.getmyLocation(callback);
        }

        Manager.getInstance(context).addSignalingListener();

        //??????????????????????????????????????????
        switch (userInfo.getState()) {
            case 0:
            case 1:
                //??????????????????userInfo.getReale() == 1
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
                //????????????
                dialog_Blocked.myshow(context);
                break;
        }

        checkinidatecamerrecord();
    }

    @Override
    protected void onPause() {
        super.onPause();
        banner.sendEmptyMessage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        banner.sendEmptyMessageDelayed();
    }

    @Override
    public void OnEorr() {

    }

    @OnClick({R.id.follow, R.id.RefreshL, R.id.layoutvideocall, R.id.message, R.id.guard, R.id.shouhu, R.id.editcontentet, R.id.back, R.id.tv_back, R.id.iv_selected_rel, R.id.messagezhuo})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layoutvideocall:
                if (userInfo.getUserId().equals(getuserid)) {
                    switch (mCurrentItem) {
                        case 0:
                            ToastUtil.toastLongMessage(getString(R.string.tv_msg82));
                            break;
                        case 1:
                            //????????????
                            startActivityForResult(new Intent(context, activity_trend.class), Config.sussess);
                            break;
                        case 2:
                            //????????????
                            sUploadActivity(ACTIVITY_VIDEO);
                            break;
                        case 3:
                            //????????????
                            sUploadActivity(ACTIVITY_IMG);
                            break;

                    }
                } else {
                    //?????????????????????
                    starinidate1();
                }
                break;
            case R.id.iv_selected_rel:
                //Manager.getInstance(context).invite(getuserid, gson.toJson(member), "????????????????????????title", "????????????????????????desc");
                ChatActivityMessage.setAction(context);
                break;
            case R.id.message:
                //????????????
                startChatActivity();
                break;
            case R.id.guard:
                //??????
                //starinidate3();
                chatwx();
                break;
            case R.id.shouhu:
                //????????????????????????
                starinidate4();
                break;
            case R.id.follow:
                //???????????????????????????
                datamodule.gofollowlist(getuserid, paymnets);
                break;
            case R.id.RefreshL:
                //????????????
                dialog_item_rs.dialogitemrs(context, paymnets).sethideshow(getuserid.equals(userInfo.getUserId()) ? View.VISIBLE : View.GONE);
                break;
            case R.id.editcontentet:
                //????????????
                startActivityForResult(new Intent(context, activity_updateedit.class), Config.sussess);
                break;
            case R.id.back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.messagezhuo:
                //?????????TA
                //dialog_sayhello.senddmessage(getuserid, "??????,???????????????");
                datamodule.getNotmessage(getuserid, new Paymnets() {
                    @Override
                    public void onSuccess() {
                        Toashow.show(getString(R.string.tm95));
                    }

                    @Override
                    public void onFail(String msg) {
                        Toashow.show(msg);
                    }
                });
                break;
        }
    }

    /**
     * ??????????????????
     */
    private void chatwx() {
        if (member == null) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }

        if (userInfo.getUserId().equals(getuserid) || userInfo.getVip() == 1 || member.getMemberlogs() > 0) {
            //????????????????????????
            dialog_select_wx_phone.show(context, member);
        } else {
            //???????????????????????????
            dialog_buymoney.show(context, member, new Paymnets() {
                @Override
                public void onFail() {

                }

                @Override
                public void onItemClick(int position) {
                    switch (position) {
                        case 1:
                        case 4:
                            break;
                        case 2:
                            //??????VIP??????
                            activity_svip.starsetAction(context);
                            break;
                        case 3:
                            //????????????
                            Detailedlist.starsetAction(context);
                            break;


                    }

                }

                @Override
                public void onSucces(Object obj1, Object obj2) {
                    Allcharge allcharge = (Allcharge) obj1;
                    com.tencent.opensource.model.info info = (com.tencent.opensource.model.info) obj2;
                    if (allcharge.getContact() > info.getMoney()) {
                        Toashow.show(getString(R.string.buymoney));
                        Detailedlist.starsetAction(context);

                    } else {
                        String name = String.format(getString(R.string.selectqu), member.getTruename());
                        datamodule.chatjinbi(String.valueOf(member.getId()), name, 1, new Paymnets() {
                            @Override
                            public void onSuccess(String msg) {
                                dialog_select_wx_phone.show(context, member);
                            }

                            @Override
                            public void onFail(String msg) {
                                Toashow.show(msg);
                            }
                        });
                    }
                }
            });
        }

    }

    /**
     * call_video ????????????
     * ???????????????????????????
     */
    private void starinidate1() {
        if (userInfo.getSex().equals(String.valueOf(member.getSex()))) {
            Toashow.show(getString(R.string.picerter));
            return;
        }
        //??????????????????????????? tRole=0 sex=2 dialog_call=false
        if (!Config_Msg.getInstance().isDialog_call() && userInfo.getSex().equals("2") && userInfo.gettRole() == 0) {
            dialog_video_message.show(context, new Paymnets() {
                @Override
                public void onSuccess() {
                    //??????????????????
                    activity_livebroadcast.setAction(context);
                }

                @Override
                public void onFail() {
                    call_video_audio();
                }
            });
        } else {
            call_video_audio();
        }

    }

    /**
     * call_video ????????????
     */
    private void call_video_audio() {
        if (userInfo.getUserId().equals(getuserid)) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg132));
            return;
        }
        if (checkSel()) {
            dialog_videocall.videocall(context, member, paymnets);
        }
    }

    /**
     * ???????????????
     */
    private void starinidate3() {
        if (member == null) {
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.dialog_as1));
            return;
        }

        //????????????
        //follow.animate().alpha(0).scaleXBy(2.0f).scaleYBy(2.0f).setDuration(1500).start();
        //follow.animate().scaleY(3f).scaleX(3f).alpha(0).setDuration(1500).translationY(500).start();
        //follow.animate().scaleY(5f).scaleX(5f).alpha(0).rotation(360).setDuration(1500).start();
        //follow.animate().alpha(1).scaleY(0f).scaleX(0f).start();

    }

    /**
     * ????????????
     */
    private void starinidate4() {
        if (member == null) {
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg180));
            return;
        }
        present present = new present();
        present.setTYPE(1); //????????????1
        present.setTouserid(String.valueOf(member.getId()));
        present.setUserid(userInfo.getUserId());

        //????????????
        dialog_item_gift.dialogitemgift(context, present, giftPanelDelegate, paymnets);
    }

    /**
     * ??????????????????????????????????????????
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
            Toashow.show(getString(R.string.picerter));
            return;
        }
        ChatActivity.setAction(context, member);
    }

    /**
     * ????????????????????????
     *
     * @param page
     */
    private void pageshow(int page) {
        tv_muncunt.setText(String.format("%s/%s", page, pageadapter.getCount()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        listimg.clear();
        fragmentlist.clear();
        banner.removeCallback();
        if (svgaImageView != null) {
            svgaImageView.stopAnimation();
        }

        Manager.getInstance(context).removeSignalingListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            refreshL();
        }

        if (Constants.REQUEST_CODE == requestCode && data != null) {
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            setCapture(result);
        }
    }

    /**
     * ??????????????????
     */
    private boolean checkSel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);       //????????????????????????
            list.add(Manifest.permission.RECORD_AUDIO); //??????????????????????????????
            int permissionGranted = PackageManager.PERMISSION_GRANTED;
            int READ_PHONE_STATE = ContextCompat.checkSelfPermission(context, list.get(0));//????????????
            int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(context, list.get(1));//SD?????????
            //????????????
            if (READ_PHONE_STATE != permissionGranted || WRITE_EXTERNAL_STORAGE != permissionGranted) {
                SystemUtil.getPermission(activity, list);
                return false;
            }
        }
        return true;
    }

    /**
     * ????????????
     *
     * @param position
     */
    private void onstartActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(context, activity_picbage.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.perimg, (Serializable) listimg);
        startActivity(intent);
    }

    /**
     * ??????????????????
     */
    private void refreshL() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        dialogLoading = Dialog_Loading.dialogLoading(context, getString(R.string.xw_ptr_refreshing));
        list.clear();
        listimg.clear();
        pageadapter.notifyDataSetChanged();

        fragmenpage.setAdapter(FragmenViewPager = new setViewPager(getSupportFragmentManager(), getFragmentlist(), setViewPager.picenter));
        initData();
    }

    /**
     * ????????????????????????
     */
    private void refreshdata() {
        //???????????????
        TopImagemember();

        //??????????????????
        stitle.setText(member.getTruename());
        //????????????
        name.setText(member.getTruename());

        if (member.getVip() == 1) {
            name.setTextColor(getResources().getColor(R.color.colorAccent));
            stitle.setTextColor(getResources().getColor(R.color.colorAccent));
            svip.setVisibility(View.VISIBLE);
        } else {
            name.setTextColor(getResources().getColor(R.color.homeback));
            stitle.setTextColor(getResources().getColor(R.color.homeback));
            svip.setVisibility(View.GONE);
        }

        //????????????
        online.setVisibility(member.getOnline() == 1 ? View.VISIBLE : View.GONE);
        //??????????????????
        iv_image.setVisibility(member.getStatus() == 2 ? View.VISIBLE : View.GONE);

        personal personal1 = member.getPersonal();
        if (personal1 != null) {
            mssge.setText(personal1.getPesigntext());
            if (TextUtils.isEmpty(personal1.getPree())) {
                telid.setText(String.format("ID:%s", member.getId()));
            } else {
                telid.setText(String.format(getString(R.string.tm148) + "", member.getId(), personal1.getFeeling()));
            }

        }

        if (member.getFollowlist() != null) {
            follow.setImageResource(R.mipmap.ahy2);
        }

        t1.follow(String.valueOf(member.getMyconun())); //??????
        t1.fans(String.valueOf(member.getTacount()));   //??????
        t1.give(String.valueOf(member.getGivecount())); //??????

        //????????????????????????
        if (TextUtils.isEmpty(member.getWx())) {
            chat_view.setVisibility(View.GONE);
        }

        //Fragment
        fragmenpage.setAdapter(FragmenViewPager = new setViewPager(getSupportFragmentManager(), getFragmentlist(), setViewPager.picenter));
    }

    /**
     * ???????????????????????????
     */
    private void TopImagemember() {

        List<perimg> perimg1 = member.getPerimg();
        if (!TextUtils.isEmpty(member.getPicture())) {
            perimg image = banner.newimage(member);
            perimg1.add(0, image);
        }
        listimg.addAll(perimg1);

        //???????????????5?????????
        for (int i = 0; i < listimg.size(); i++) {
            if (i >= 5) {
                break;
            }
            perimg perimg = listimg.get(i);
            ImageView img = new ImageView(context);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setOnClickListener(new myonclick(i));
            Glideload.loadImage(img, perimg.getPic());
            list.add(img);
        }

        //??????????????????
        if (listimg.size() == 0 && list.size() == 0) {
            View view_images = banner.newimage(member, 0);
            list.add(view_images);
        }

        pageadapter.notifyDataSetChanged();
        if (pageadapter.getCount() > 1) {
            banner.sendEmptyMessageDelayed(); //3??????????????????????????????
        }

        pageshow(1); //????????????????????????
    }

    /**
     * ?????????????????????????????????
     */
    private class myonclick implements View.OnClickListener {
        private final int pos;

        public myonclick(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            onstartActivity(pos);
        }

    }

    /**
     * ????????????
     */
    public void reportUser() {
        videolist videolist = new videolist();
        videolist.setUserid(getuserid);
        videolist.setId(getString(R.string.tv_msg237) + getuserid);
        item_chid_play.reportUser(context, videolist, 2);
    }

    /**
     * ????????????
     */
    private void mydismiss() {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    private List<Fragment> getFragmentlist() {
        if (fragmentlist != null) {
            fragmentlist.clear();
        }
        fragmentlist = new ArrayList<>();
        fragmentlist.add(per1.perview(getuserid));               //??????
        fragmentlist.add(page3_1.showfmessage(2, getuserid)); //??????
        fragmentlist.add(per3.perview(getuserid));               //??????
        fragmentlist.add(per4.perview(getuserid));               //??????
        return fragmentlist;
    }

    /**
     * ??????????????????
     *
     * @param tab
     */
    @Override
    public void Tabtext(TabLayout.Tab tab) {
        TextView textView = new TextView(context);
        textView.setTextSize(18);
        textView.setTextColor(Color.RED);
        textView.setText(tab.getText());
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        tab.setCustomView(textView);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        banner.removeCallback();
        Log.d(TAG, "onDetachedFromWindow:???????????????");
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        banner.sendEmptyMessageDelayed();
        Log.d(TAG, "onAttachedToWindow:??????????????????");
    }

    /**
     * ??????????????????
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });
        }

        @Override
        public void activity(String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);
                }
            });
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            switch (TYPE) {
                case 1:
                    //?????????????????????
                    csAlipay.Paymoney(moneylist);
                    break;
                case 2:
                    //??????????????????
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }
        }

        @Override
        public void onSuccess(Object object) {
            member = (com.tencent.opensource.model.member) object;
            refreshdata();
            mydismiss();
        }

        @Override
        public void fall(int code) {
            mydismiss();
            ToastUtil.toastLongMessage("??????????????????" + code);
        }

        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            mydismiss();
        }

        @Override
        public void status(int position) {
            switch (position) {
                case 1:
                    //????????????
                    refreshL();
                    break;
                case 2:
                    //????????????
                    startActivity(new Intent(context, activity_updateedit.class));
                    break;
                case 3:
                    //????????????
                    startActivity(new Intent(context, activity_album.class).putExtra(Constants.USERID, getuserid));
                    break;
                case 4:
                    //????????????
                    reportUser();
                    break;
                case 6:
                    //??????
                    dialog_item_qrcode.qrcodes(context, member);
                    break;
                case 7:
                    //??????
                    if (checkinidate()) {
                        CaptureActivity();
                    }
                    break;
            }
        }

        @Override
        public void onClick() {
            finish();

        }

        @Override
        public void dismiss() {
            finish();
        }


        @Override
        public void payens() {
            member.setTacount(member.getTacount() + 1);
            Toashow.show(getString(R.string.gzok));
            follow.setImageResource(R.mipmap.ahy2);
            t1.fans(String.valueOf(member.getTacount())); //??????
        }

        @Override
        public void onError() {
            couneroo = member.getTacount() - 1;
            couneroo = couneroo <= 0 ? 0 : couneroo;
            member.setTacount(couneroo);

            t1.fans(String.valueOf(member.getTacount())); //??????
            Toashow.show(getString(R.string.gzon));
            follow.setImageResource(R.mipmap.ahy);
        }

        @Override
        public void payonItemClick(String msg, int type) {
            Toashow.show(msg); //??????????????????
        }

        @Override
        public void ToKen(String msg) {
            activity_picenter.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }
    };

    /**
     * ????????????????????????
     */
    private GiftPanelDelegate giftPanelDelegate = new GiftPanelDelegate() {
        @Override
        public void onGiftItemClick(GiftInfo giftInfo) {
            Toashow.show(String.format(getString(R.string.gif_title) + "", giftInfo.title));
            boolean svga = giftInfo.lottieUrl.toLowerCase().endsWith(".svga");
            if (svga) {
                mAnimationUrlList.addLast(giftInfo.lottieUrl);
                if (!mIsPlaying) {
                    SVGAImageView();
                }

            }
        }

        @Override
        public void onChargeClick() {
            Log.d(TAG, "??????????????????: ");

        }
    };

    /**
     * ???????????????????????????????????????
     */
    private boolean checkinidate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);                            //????????????
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);            //SD?????????
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
     * ??????CaptureActivity?????????
     */
    private void CaptureActivity() {
        startActivityForResult(new Intent(context, CaptureActivity.class), Constants.REQUEST_CODE);
    }

    /**
     * ??????????????????
     *
     * @param result
     */
    private void setCapture(String result) {
        boolean b1 = result.toLowerCase().startsWith("http://");
        boolean b2 = result.toLowerCase().startsWith("https://");
        boolean b3 = result.toLowerCase().endsWith(".apk");
        if (b1 || b2) {
            //???????????????
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
            case Config.sussess:
                starinidate1();
                break;
            case Constants.REQUEST_CODE:
                CaptureActivity();
                break;
            case Constants.requestCode:
                break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    /**
     * ???????????????
     */
    public abstract static class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

        public enum State {
            EXPANDED,
            COLLAPSED,
            IDLE
        }

        private State mCurrentState = State.IDLE;

        @Override
        public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(appBarLayout, State.EXPANDED);
                }
                mCurrentState = State.EXPANDED;
            } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(appBarLayout, State.COLLAPSED);
                }
                mCurrentState = State.COLLAPSED;
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(appBarLayout, State.IDLE);
                }
                mCurrentState = State.IDLE;
            }
        }

        public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
    }

    /**
     * ?????????????????????????????????
     */
    private void SVGAImageView() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    if (mAnimationUrlList.size() == 0 || mAnimationUrlList.isEmpty()) {
                        return;
                    }

                    String lottieUrl = mAnimationUrlList.getFirst();
                    if (!TextUtils.isEmpty(lottieUrl)) {
                        //???????????????????????????
                        mAnimationUrlList.removeFirst();
                    }
                    parser.decodeFromURL(new URL(lottieUrl), new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(SVGAVideoEntity videoItem) {
                            SVGADrawable drawable = new SVGADrawable(videoItem);
                            //??????????????????
                            if (svgaImageView != null) {
                                mIsPlaying = true;
                                svgaImageView.setImageDrawable(drawable);
                                svgaImageView.startAnimation();
                                svgaImageView.setCallback(new SVGACallback() {
                                    @Override
                                    public void onPause() {
                                        Log.d(TAG, "onPause: ");

                                    }

                                    @Override
                                    public void onFinished() {
                                        Log.d(TAG, "onFinished: ");
                                        //?????????????????????
                                        if (mAnimationUrlList.isEmpty() && svgaImageView != null) {
                                            svgaImageView.stopAnimation();
                                            mIsPlaying = false;
                                        } else {
                                            SVGAImageView();
                                        }

                                    }

                                    @Override
                                    public void onRepeat() {
                                        Log.d(TAG, "onRepeat: ");

                                    }

                                    @Override
                                    public void onStep(int i, double v) {
                                        Log.d(TAG, "onStep: ");

                                    }
                                });
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                }


            }

        }.start();

    }

    /**
     * ???????????????
     */
    private void coypgewx() {
        if (!TextUtils.isEmpty(member.getWx())) {
            if (!WXpayObject.isWeixinAvilible()) {
                Toashow.show(DemoApplication.instance().getString(R.string.tm80));
                return;
            }
            Toashow.show(String.format(getString(R.string.wxcoyp), member.getWx()));
            Dialog_fenxing.paramcopy(context, member.getWx());
            Dialog_fenxing.getDialogFenxing(context).getWechatApi();
        } else {
            Toashow.show(getString(R.string.tt11));
        }
    }


    /**
     * ???????????????????????????????????????
     */
    private boolean checkinidatecamerrecord() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);                            //????????????
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);            //SD?????????
            list.add(Manifest.permission.RECORD_AUDIO);                      // ????????????
            for (String permission : list) {
                int star = ContextCompat.checkSelfPermission(context, permission);
                if (star != PackageManager.PERMISSION_GRANTED) {
                    SystemUtil.getPermission(activity, list, Constants.requestCode);
                    return false;
                }
            }
        }
        return true;
    }


}
