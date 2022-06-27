package com.tianxin.Fragment.page5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.activity.Aboutus.activity_joinin;
import com.tianxin.activity.Aboutus.activity_viecode;
import com.tianxin.activity.Memberverify.activity_livebroadcast;
import com.tianxin.activity.Web.Webviewactivity;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.activity.ZYservices.activity_myper;
import com.tianxin.activity.activity_chat_settings;
import com.tianxin.activity.browse.activity_browse;
import com.tianxin.activity.game.Game_Activity_show;
import com.tianxin.activity.party.activity_party_list;
import com.tianxin.activity.picenter.banner;
import com.tianxin.activity.videoalbum.activity_list_Images;
import com.tianxin.activity.videoalbum.activity_list_Video;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.LatestNews.activity_trend;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.activity.activity_mylikeyou;
import com.tianxin.dialog.Dialog_fenxing;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.widget.fragment_page5;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.IMtencent.scenes.LiveRoomAnchorActivity;
import com.tianxin.R;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.activity.activity_follow;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.activity.edit.activity_nickname3;
import com.tianxin.activity.edit.activity_uploadavatar;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Loading;
import com.tianxin.getHandler.PostModule;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.costransferpractice.common.FilePathHelper;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tianxin.Util.Imagecompressiontool.dataDir;
import static com.tianxin.Util.Config.getFileName;

/**
 * 个人中心
 */
public class page5 extends BasFragment {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.truename)
    TextView truename;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.sex1)
    TextView sex1;
    @BindView(R.id.vip)
    TextView vip;
    @BindView(R.id.username)
    TextView musername;
    @BindView(R.id.pesigntext)
    TextView pesigntext;
    @BindView(R.id.tv_eorr_msg)
    TextView tv_eorr_msg;
    @BindView(R.id.useLogo)
    ImageView useLogo;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.layfc)
    LinearLayout layfc;
    @BindView(R.id.layoutee)
    LinearLayout layoutee;
    @BindView(R.id.reward)
    TextView reward;
    @BindView(R.id.starts)
    TextView starts;
    @BindView(R.id.mytrole)
    TextView mytrole;
    @BindView(R.id.gameActivity)
    LinearLayout gameActivity;
    @BindView(R.id.layouta2101)
    fragment_page5 layouta2101;
    @BindView(R.id.layouta2102)
    fragment_page5 layouta2102;
    @BindView(R.id.tvinreview)
    TextView tvinreview;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.banner2)
    LinearLayout banner2;
    List<View> mlist = new ArrayList<>();
    Dialog_Loading dialogLoading;
    private String TAG = page5.class.getSimpleName();
    private pageadapter pageadapter;
    private com.tianxin.activity.picenter.banner banner;
    private int nnanner;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_personal, null);
    }

    @Override
    public void iniview() {
        usergetAvatar();
    }

    @Override
    public void initData() {
        viewpager.setAdapter(pageadapter = new pageadapter(context, mlist));
        banner = new banner(context, viewpager, mlist);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:  //请求暂停轮播
                        banner.sendEmptyMessage();
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:  //请求恢复轮播
                        banner.sendEmptyMessageDelayed();
                        break;
                }
            }

        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                datamodule.loadpage5(paymnets);

            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败

            }
        });
        datamodule.loadpage5(paymnets);

        //广告获取滚动图片
        datamodule.getdsbanner(2, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object object) {
                List<com.tianxin.Module.api.banner> AdList = (List<com.tianxin.Module.api.banner>) object;
                banner2.setVisibility(View.VISIBLE);
                mlist.clear();
                for (com.tianxin.Module.api.banner banner1 : AdList) {
                    ImageView image = new ImageView(context);
                    image.setScaleType(ImageView.ScaleType.CENTER);
                    ImageLoadHelper.glideShowCornerImageWithUrl(context, banner1.getPicture(), image);
                    if (!TextUtils.isEmpty(banner1.getPath())) {
                        //包含指定字符串
                        if (banner1.getPath().toLowerCase().contains("invitation")) {
                            String path = String.format("%s/index.php/index/invitation?uid=%s&id=%s", Webrowse.HttpWEB, userInfo.getUserId(), userInfo.getToken());
                            image.setOnClickListener(v -> Webviewactivity.setAction(context, path.trim()));
                        } else {
                            image.setOnClickListener(v -> DyWebActivity.starAction(context, banner1.getPath().trim()));
                        }
                    }
                    mlist.add(image);
                }
                pageadapter.notifyDataSetChanged();
                if (nnanner == 0) {
                    nnanner = 1;
                    onResume();
                }
            }

            @Override
            public void onSuccess(String msg) {
                banner2.setVisibility(View.VISIBLE);
            }
        });


    }

    @OnClick({R.id.layoutaq, R.id.gameActivity, R.id.list1,
            R.id.tv_upedit, R.id.layouta1, R.id.oneconok,
            R.id.list4, R.id.layout40,
            R.id.layout41, R.id.layout42, R.id.layout43,
            R.id.layout44, R.id.layout45, R.id.layoutaa,
            R.id.layoutbb, R.id.layoutcc, R.id.layoutdd,
            R.id.useLogo, R.id.layoutee, R.id.itemrigle,
            R.id.layfc, R.id.cotyid, R.id.layout455, R.id.layouta2101, R.id.layouta2102, R.id.party})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.useLogo:
                //上传头像
                startActivityForResult(new Intent(context, activity_uploadavatar.class), Config.sussess);
                break;
            case R.id.tv_upedit:
            case R.id.itemrigle:
                //辑编资料
                startActivityForResult(new Intent(context, activity_updateedit.class), Config.sussess);
                break;
            case R.id.layout40:
                //实名认证
                startActivity(new Intent(context, activity_livebroadcast.class).putExtra(Constants.TYPE, 2));
                break;
            case R.id.layout41:
            case R.id.oneconok:
                //分享邀请好友
                String path = String.format("%s/index.php/index/invitation?uid=%s&id=%s", Webrowse.HttpWEB, userInfo.getUserId(), userInfo.getToken());
                Webviewactivity.setAction(context, path);
                break;
            case R.id.layout42:
                //视频开播
                createRoom();
                break;
            case R.id.layoutaq:
                //最新动态
                activity_trend.starsetAction(context);
                break;
            case R.id.layout43:
                //意见反馈
                activity_nickname3.setAction(context);
                break;
            case R.id.layout44:
                //联系我们
                Intent date = new Intent(context, DyWebActivity.class);
                date.putExtra(Constants.VIDEOURL, Webrowse.invitefriends + "?type=2&userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken());
                startActivity(date);
                break;
            case R.id.layout45:
                //设置
                startActivity(new Intent(context, activity_sesemys.class));
                break;
            case R.id.layoutaa:
            case R.id.layouta1:
                //视频列表
                activity_list_Video.starsetAction(context);
                break;
            case R.id.list4:
                //相册列表
                activity_list_Images.starsetAction(context);
                break;
            case R.id.layoutdd:
                //我的金币
                startActivity(new Intent(context, Detailedlist.class));
                break;
            case R.id.layoutee:
                //startActivity(new Intent(context,activity_home.class));  //咨询师服务页
                startActivity(new Intent(context, References.class));      //去赚钱
                break;
            case R.id.layoutcc:
                //我的关注
                startActivity(new Intent(context, activity_follow.class));  //我的关注
                break;
            case R.id.layfc: { //我的粉丝
                activity_mylikeyou.setAction(context);
            }
            break;
            case R.id.layoutbb: {
                //谁看了我访客{
                activity_browse.setAction(context);
            }
            break;
            case R.id.list1:
                //个人主页
                startActivityForResult(new Intent(context, BuildConfig.TYPE == Constants.TENCENT ? activity_picenter.class : activity_myper.class).putExtra(Constants.USERID, userInfo.getUserId()), Config.sussess);
                break;
            case R.id.gameActivity:
                //我的技能
                startActivity(new Intent(context, Game_Activity_show.class));
                break;
            case R.id.cotyid:
                Dialog_fenxing.paramcopy(context, userInfo.getUserId());
                Toashow.show(getString(R.string.tm89));
                break;
            case R.id.layout455:
                //合作加盟
                activity_joinin.starAction(context);
                break;
            case R.id.layouta2101: {
                //我的等级
                String url = BuildConfig.HTTP_API + "/invitefriends?type=%s&userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken();
                activity_viecode.WebbookUrl(context, String.format(url, 8));
                break;
            }

            case R.id.layouta2102: {
                //收费设置
                activity_chat_settings.starAction(context);
                break;
            }
            case R.id.party:
                //聚会管理
                activity_party_list.setAction(context);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.sussess:
                if (resultCode == Config.setResult && data != null) {
                    datamodule.loadpage5(paymnets);
                }
                break;
            case OPEN_FILE_CODE:
                opengetfile(resultCode, data);
                break;
        }
    }

    /**
     * 红娘申请
     */
    private void getpost() {
        dialogLoading = new Dialog_Loading(context, "正在提交");
        dialogLoading.setCanceledOnTouchOutside(false);
        dialogLoading.show();
        datamodule.matchmaker(paymnets);
    }

    /**
     * 创建房间
     */
    private void createRoom() {
        LiveRoomAnchorActivity.start(context, "");
    }

    /******************** 打开相册 ***********************************************/

    /**
     * 用于测试图片压缩处理
     */
    private void openfile() {
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        //intent.setType("*/*"); //全部类型 意图类型
        String[] gettype = gettype();
        intent.setType(gettype[0]); //打开图片类型
        startActivityForResult(intent, OPEN_FILE_CODE);

        //Intent 中重要方法：setAction()、setData()、setType()、putExtra()
        //二、隐式Intent它不明确指出我们想要启动哪一个活动，而是指定了一系列更为抽象的action和category等信息来过滤，找到符合条件的Activity。 intent.setAction(Intent.ACTION_DIAL);
        //1.setAction()：表明我们想要启动能够响应设置的这个action的活动，并在清单文件AndroidManifest.xml中设置action属性。如:(打开一个拨号界面)
        //2.setData()：通常是URI格式定义的操作数据。（只要设置setAction ()就要在清单文件AndroidManifest.xml中设置action属性）例如：tel: intent.setAction(Intent.ACTION_DIAL); intent.setData(Uri.parse("tel:10086"));
        //3.setType()：指定数据类型，选出适合的应用来。（只要设置setAction ()就要在清单文件AndroidManifest.xml中设置action属性）例如： intent.setAction(Intent.ACTION_SEND); intent.setType("text/plain");
        //4.putExtra()：把要传递的数据暂存在Intent。

    }

    /**
     * 您可以在setType()中设置的常见MIME类型的列表：
     *
     * @return
     */
    private String[] gettype() {
        String[] type = {
                "image/jpeg",
                "audio/mpeg4 - generic",
                "text/html",
                "audio/mpeg",
                "audio/aac",
                "audio/wav",
                "audio/ogg",
                "audio/midi",
                "audio/x - ms - wma",
                "video/mp4",
                "video/x - msvideo",
                " video/x - ms - wmv",
                "image/png",
                " image/jpeg",
                "image/gif",
                ".xml->text/xml",
                ".txt->text/plain",
                ".cfg->text/plain",
                ".csv->text/plain",
                ".conf->text/plain",
                ".rc->text/plain",
                ".htm->text/html",
                ".html->text/html",
                ".pdf->application/pdf",
                ".apk->application/vnd.android.package-archive",
        };

        return type;
    }

    /******************** 打开相册结束 ***********************************************/

    /**
     * 处理压缩图片
     *
     * @param resultCode
     * @param data
     */
    public void opengetfile(int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {
            String path = FilePathHelper.getAbsPathFromUri(DemoApplication.instance(), data.getData());
            if (TextUtils.isEmpty(path)) {
                return;
            }
            File file = new File(path);
            if (file.length() == 0) {
                Toashow.toastMessage("上传图片无效");
                return;
            }

            //直接用选择的文件URI去展示图片 解析图片
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                useLogo.setImageBitmap(bitmap);
            }


            //创建新的文件保存地址
            File dataDir = dataDir();
            //质量压缩图片
            //Imagecompressiontool.qualityCompress(bitmap, dataDir);

            //插入到相册中
            basestartActivity.displayToGallery(context, dataDir);

            //图片不压缩
            //Bitmap bitmap1 = Imagecompressiontool.oNqualityCompress(bitmap, dataDir);
            //减少像素来降低图片
            //Bitmap bitmap1 = Imagecompressiontool.qualityCompress(file, dataDir);
            //采样率压缩 可以根据需求计算出合理的inSampleSize
            //Bitmap bitmap1 = Imagecompressiontool.qualityCompress(file, dataDir, 5);

            //尺寸压缩（通过缩放图片像素来减少图片占用内存大小） 文件大于500KB
            if (file.length() > 1024 * 500) {
                Imagecompressiontool.sizeCompress(bitmap, dataDir, 10);
            } else {
                //图片不压缩
                Imagecompressiontool.oNqualityCompress(bitmap, dataDir);
            }

            //打印相关信息
            Log.d(TAG, path);
            Log.d(TAG, "原文件地址：" + file.getPath());
            Log.d(TAG, "压缩前大小：" + file.length());
            Log.d(TAG, "文件名称：" + getFileName(file.getPath()));
            Log.d(TAG, "*********************分割线************************");
            Log.d(TAG, "压缩后地址 dataDir: " + dataDir.getPath());
            Log.d(TAG, "压缩后大小 dataDir: " + dataDir.length());
            Log.d(TAG, "文件名称：" + getFileName(dataDir.getPath()));


            //可以提交上文件地址
            uploads(dataDir);


        }

        if (resultCode == Config.sussess) {
            Glide.with(context).load(userInfo.getAvatar()).into(useLogo);
        }


    }

    /**
     * 上传头像保存到服务器
     */
    private void uploads(File file) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(context
                    .getResources().getString(R.string.eorrfali2));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("path", BuildConfig.HTTP_API + "/uploads");
        params.put("userid", userInfo.getUserId());
        params.put("token", userInfo.getToken());
        PostModule.okhttpimage(params, file, new Paymnets() {
            @Override
            public void success(String date) {

                Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                Toashow.show(mesresult.getMsg());
                if (mesresult.getStatus().equals("1")) {
                    file.delete();
                    userInfo.setAvatar(mesresult.getPicture());
                    Log.d(TAG, "success: " + mesresult.toString());

                    //提交给腾讯IM修改个人资料
                    activity_sesemys.updateProfile(userInfo);
                }

            }

            @Override
            public void fall(int code) {
                Log.d(TAG, "fall: ");
            }
        });

       /* Map<String,String> params = new HashMap<>();
        params.put("userid", instance.getUserId());
        params.put("image", file.getPath());
        String posturl = BuildConfig.HTTP_API + "/uploads";
        HttpUtils.RequestPost(posturl, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                Log.d(TAG, "RequestPost:" + response);

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "message:" + message);
            }
        });*/
    }

    /**
     * 更新UI头像相关信息
     */
    private void usergetAvatar() {
        if (!TextUtils.isEmpty(userInfo.getSex())) {
            sex = Integer.parseInt(userInfo.getSex());
            sex1.setText(String.format("%s：%s", getString(R.string.tv_mst99), sex == Constants.TENCENT ? getString(R.string.tv_msg187) : getString(R.string.tv_msg188)));
        }
        layfc.setVisibility(BuildConfig.TYPE == Constants.TENCENT ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Glide.with(context).load(userInfo.getAvatar()).into(useLogo);
        } else {
            Glide.with(context).load(sex == Constants.TENCENT ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose).into(useLogo);
        }
        name.setText(TextUtils.isEmpty(userInfo.getGivenname()) ? userInfo.getName() : userInfo.getGivenname());
        truename.setText(String.format("(%s)", userInfo.getName()));
        if (TextUtils.isEmpty(userInfo.getGivenname())) {
            truename.setVisibility(View.GONE);
        }
        musername.setText(String.format(getString(R.string.tv_msg189) + "", "ID", userInfo.getUserId()));
        if (!TextUtils.isEmpty(userInfo.getPesigntext())) {
            pesigntext.setText(userInfo.getPesigntext());
        }
        vip.setVisibility(userInfo.getVip() == Constants.TENCENT ? View.VISIBLE : View.GONE);
        tv_eorr_msg.setVisibility(userInfo.getState() >= 3 ? View.VISIBLE : View.GONE);
        pesigntext.setSelected(true);

        //头像审核中
        tvinreview.setVisibility(userInfo.getInreview() == 0 ? View.GONE : View.VISIBLE);
        if (userInfo.getSex().equals("2")) {
            layouta2101.setVisibility(View.VISIBLE);
            layouta2102.setVisibility(View.VISIBLE);
        } else {
            layouta2101.setVisibility(View.GONE);
            layouta2102.setVisibility(View.GONE);
        }
        switch (userInfo.getState()) {
            case 0:
            case 1:
                starts.setText(R.string.stars_t1);
                reward.setVisibility(sex == 1 ? View.GONE : View.VISIBLE);
                break;
            case 2:
                starts.setText(R.string.stars_t2);
                reward.setVisibility(View.GONE);
                break;
            default:
                starts.setVisibility(View.GONE);
                reward.setVisibility(View.GONE);
                break;
        }
        //显示为主播
        mytrole.setVisibility(userInfo.gettRole() == 0 ? View.GONE : View.VISIBLE);
        //游戏显示
        gameActivity.setVisibility(userInfo.getGame() == 1 ? View.GONE : View.VISIBLE);

        //检查TIM信息
        activity_sesemys.checkTIMInfo(userInfo.getName(), userInfo.getAvatar());


    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            if (dialogLoading != null) {
                dialogLoading.dismiss();
            }
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            if (dialogLoading != null) {
                dialogLoading.dismiss();
            }
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(String msg) {
            if (dialogLoading != null) {
                dialogLoading.dismiss();
            }
            ToastUtil.toastLongMessage(msg);
        }

        @Override
        public void onSuccess(Object object) {
            usergetAvatar();
        }

        @Override
        public void onSuccess() {
            getpost();
        }

        @Override
        public void onError() {

        }

    };


    /**
     * =========== 和广告相关 ==========================
     **/
    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.onPause();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.onRestart();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (banner != null) {
            banner.removeCallback();

        }
    }
}
