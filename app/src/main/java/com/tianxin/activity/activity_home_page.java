package com.tianxin.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.tianxin.activity.picenter.activity_picbage;
import com.tianxin.adapter.Radapter;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.Fragment.fragment.fragment_presentation;
import com.tianxin.activity.ZYservices.activity_zshouxianming;
import com.tianxin.getHandler.PostModule;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.widget.item_chid_play;
import com.tencent.opensource.AES.Resultinfo;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import com.tianxin.BasActivity.BasActivity2;
import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.activity.edit.activity_nickname2;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Loading;
import com.tianxin.dialog.dialog_item_recy_view;
import com.tianxin.dialog.dialog_item_rs;
import com.tianxin.widget.MTextView;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.gethelp;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.perimg;
import com.tencent.opensource.model.personal;
import com.tencent.opensource.model.videotype;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 心理咨询详情页
 */
public class activity_home_page extends BasActivity2 implements Paymnets {
    private static final String TAG = "activity_home_page";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ivbgmplay)
    ImageView ivbgmplay;
    @BindView(R.id.name)
    TextView username;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.pesigntext)
    TextView pesigntext;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.iv_image)
    ImageView iv_image;
    @BindView(R.id.tv_fall)
    TextView tv_fall;
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
    @BindView(R.id.tag)
    LinearLayout tag;
    @BindView(R.id.contentPanel)
    LinearLayout contentPanel;
    @BindView(R.id.tv_mtextiew)
    MTextView mTextView;
    @BindView(R.id.edit_t)
    TextView edit_t;
    @BindView(R.id.toolbar)
    ImageView toolbar;
    String getPesigntext;
    Dialog_Loading dialogLoading;

    private activity_home_page activity;


    @Override
    protected int getview() {
        return R.layout.activity_homepage;
    }

    @Override
    public void iniview() {
        getuserid = getIntent().getStringExtra(Constants.USERID);
        radapter = new Radapter(this, list, Radapter.activity_homepage);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int result) {
                onstartActivity(result);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter);
        edit_t.setVisibility(getuserid.equals(userInfo.getUserId()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initData() {
        getjson();
        presentationlist();
    }

    @Override
    @OnClick({R.id.back, R.id.restart, R.id.layout1, R.id.chatactivity, R.id.clpkselect, R.id.layout10, R.id.edit_t, R.id.toolbar, R.id.pesigntext})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_t:
                startActivityForResult(new Intent(context, activity_nickname2.class).putExtra(Constants.POSITION, 13), Config.sussess);
                break;
            case R.id.restart:
                dialog_item_rs dialogitemrs = dialog_item_rs.dialogitemrs(context, this);
                dialogitemrs.sethideshow(getuserid.equals(userInfo.getUserId()) ? View.VISIBLE : View.GONE);
                dialogitemrs.sethide(0);
                break;
            case R.id.chatactivity:
                startChatActivity();
                break;
            case R.id.clpkselect:
                if (!Config.isNetworkAvailable()) {
                    Toashow.show(getString(R.string.eorrfali2));
                    return;
                }
                dialog_item_recy_view.dialogshow(this, getuserid);
                break;
            case R.id.layout1:
                setFollow();
                break;
            case R.id.layout10:
                sevaluates();
                break;
            case R.id.toolbar:
            case R.id.pesigntext:
                if (userInfo.getUserId().equals(getuserid)) {
                    return;
                }
                Intent intent = new Intent(this, activity_zshouxianming.class);
                intent.putExtra(Constants.USERID, getuserid);
                intent.putExtra(Constants.TITLE, TextUtils.isEmpty(getPesigntext) ? pesigntext.getText().toString() : getPesigntext);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    /**
     * 获取数据
     */
    private void getjson() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            if (dialogLoading != null) {
                dialogLoading.dismiss();
            }
            return;
        }
        PostModule.getModule(String.format(BuildConfig.HTTP_API + "/posmember?userid=%s&touserid=%s&token=%s", userInfo.getUserId(), getuserid, userInfo.getToken()), this);
    }

    /**
     * 正在刷新数据
     */
    private void refreshL() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        dialogLoading = Dialog_Loading.dialogLoading(this, getString(R.string.tv_msg103));
        list.clear();
        contentPanel.removeAllViews();
        getjson();
        presentationlist();
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
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg82));
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
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);
    }

    public void setFollow() {
        if (member == null) {
            Toashow.show(getString(R.string.eorrfali3));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg147));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getResources().getString(R.string.eorrfali2));
            return;
        }
    PostModule.getModule(Webrowse.gofollowlist+"?userid=" + userInfo.getUserId() + "&touserid=" + getuserid + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        ToastUtils.showShort(getString(R.string.tv_fall));
                        tv_fall.setText(getString(R.string.tv_fall));
                        Glide.with(activity_home_page.this).load(R.mipmap.a_k2).into(iv_image);
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
     * 更新UI
     *
     * @param member
     */
    private void setiniview(member member) {
        if (TextUtils.isEmpty(member.getPicture())) {
            Glideload.loadImage(ivbgmplay, member.getSex() == Constants.TENCENT ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        } else {
            Glideload.loadImage(ivbgmplay, member.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture());
        }
        personal personal = member.getPersonal();
        List<videotype> videotype = member.getVideotype();
        if (videotype.size() > 0) {
            tag.removeAllViews();
            for (videotype videotype1 : videotype) {
                if (TextUtils.isEmpty(videotype1.getTitle())) {
                    continue;
                }
                if (tag.getChildCount() > 4) {
                    break;
                }
                TextView s = new TextView(this);
                s.setBackground(getDrawable(R.drawable.acitvity07));
                s.setPadding(10, 2, 10, 2);
                s.setTextSize(12);
                s.setTextColor(getResources().getColor(R.color.white));
                s.setText(videotype1.getTitle());
                LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lastTxtParams.setMargins(20, 0, 0, 0);
                s.setLayoutParams(lastTxtParams);
                s.invalidate();
                tag.addView(s);
            }
        }
        username.setText(!TextUtils.isEmpty(member.getTvname()) ? member.getTvname() : member.getTruename());
        String getprocity1 = String.format("%s.%s", member.getProvince(), member.getCity());
        String tvtype = personal == null ? getprocity1 : String.format("%s.%s", personal.getProvince(), personal.getCity());
        String s1 = tvtype.replace("省", "").replace("市", "");
        tv_type.setText(s1);
        if (member.getFollowlist() != null) {
            Glideload.loadImage(iv_image, R.mipmap.a_k2);
            tv_fall.setText(getString(R.string.tv_fall));
        }
        if (!TextUtils.isEmpty(member.getConfigmsg())) {
            mTextView.setText(Html.fromHtml(member.getConfigmsg()));
        }
        if (personal != null && TextUtils.isEmpty(personal.getPesigntext())) {
            pesigntext.setText(personal.getPesigntext());
            getPesigntext = personal.getPesigntext();
        }
        List<perimg> perimg = member.getPerimg();
        if (perimg.size() == 0) {
            layout2.setVisibility(View.GONE);
        }
        for (perimg perimg1 : perimg) {
            if (!TextUtils.isEmpty(perimg1.getPic())) {
                list.add(perimg1);
            }
        }
        radapter.notifyDataSetChanged();
    }

    /**
     * 更新UI
     */
    private void sethelp(gethelp gethelp) {
        if (gethelp.getId() == 0) {
            return;
        }
        a1.setText(String.valueOf(gethelp.getDeqy()));
        a2.setText(String.valueOf(gethelp.getCertificates()));
        a3.setText(String.valueOf(gethelp.getPeople()));
        a4.setText(String.valueOf(gethelp.getDuration()));
        a5.setText(String.format("%s人评价", gethelp.getEvaluate()));
    }

    private void onstartActivity(int position) {
        Intent intent = new Intent();
        intent.setClass(this, activity_picbage.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra("perimg", (Serializable) list);
        startActivity(intent);
    }

    /**
     * 展示个人简介内容
     */
    private void presentationlist() {
        getSupportFragmentManager().beginTransaction().replace(R.id.contentPanel, fragment_presentation.frpresentation(getuserid)).commit();
    }

    /**
     * 打开评论页
     */
    private void sevaluates() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        Intent intent = new Intent(this, activity_sevaluate.class);
        intent.putExtra(com.tianxin.utils.Constants.USERID, getuserid);
        startActivity(intent);
    }

    @Override
    public void status(int position) {
        switch (position) {
            case 1:
                //刷新数据
                refreshL();
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
            case 5:
                break;
            case 6:
                break;
            default:
                break;
        }

    }

    @Override
    public void success(String str) {
        try {
            String decrypt = Resultinfo.decrypt(str);
            member = gson.fromJson(decrypt, member.class);
            setiniview(member);
            sethelp(member.getHelp());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
    }

    @Override
    public void fall(int code) {
        if (dialogLoading != null) {
            dialogLoading.dismiss();
        }
        ToastUtil.toastLongMessage("获取数据失败" + code);
    }

    /**
     * 举报投诉
     */
    private void reportUser() {
        videolist videolist = new videolist();
        videolist.setUserid(getuserid);
        videolist.setId(getuserid);
        item_chid_play.reportUser(context, videolist, 2);
    }

}

