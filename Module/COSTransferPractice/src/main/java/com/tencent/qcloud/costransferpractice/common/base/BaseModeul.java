package com.tencent.qcloud.costransferpractice.common.base;

import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIDEO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.costransferpractice.R;
import com.tencent.qcloud.costransferpractice.dialog.dialog_show1;
import com.tencent.qcloud.costransferpractice.transfer.UploadActivity;
import com.tencent.qcloud.costransferpractice.utils.Constants;
import com.tencent.qcloud.costransferpractice.utils.HttpUtils;
import com.tencent.qcloud.costransferpractice.utils.JsonUitl;
import com.tencent.qcloud.costransferpractice.utils.videotitle;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapter;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BaseModeul {
    private String TAG = BaseModeul.class.getSimpleName();
    private Context context;
    private Activity activity;
    private Gson gson;
    private BUserinfo bUserinfo;
    private int UserSTATE;                           //实名状态
    private int UserVIP;
    private String folderName;
    private String Userid;
    private int TYPE;
    private String title;
    private boolean isvupcuimg = false;              //表示图片已经过压缩标记
    private String videoTime;                        //标记视频播放时长
    private videotitle svideo;                       //分类对像
    private Long vedioTotalTime;//播放时长
    private List<videotitle> mygetdata;
    private String TOKEN;
    private UserInfo userInfo;
    private UploadActivity uploadActivity;
    private double jinbi;
    private String province;
    private String city;
    private String district;
    private String address;
    private String jwd;
    private String[] tabs1;
    private String[] tabs2;
    private String[] tabs3;
    private int checkedItem1;  //是否收费
    private int checkedItem2; //默认金币
    private int checkedItem3; //默认金币

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJwd() {
        return jwd;
    }

    public void setJwd(String jwd) {
        this.jwd = jwd;
    }

    public double getJinbi() {
        return jinbi;
    }

    public void setJinbi(double jinbi) {
        this.jinbi = jinbi;
    }

    public UploadActivity getUploadActivity() {
        return uploadActivity;
    }

    public videotitle getSvideo() {
        return svideo;
    }

    public void setSvideo(videotitle svideo) {
        this.svideo = svideo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIsvupcuimg() {
        return isvupcuimg;
    }

    public void setIsvupcuimg(boolean isvupcuimg) {
        this.isvupcuimg = isvupcuimg;
    }

    public String getVideoTime() {
        return videoTime;
    }

    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    public Long getVedioTotalTime() {
        return vedioTotalTime;
    }

    public void setVedioTotalTime(Long vedioTotalTime) {
        this.vedioTotalTime = vedioTotalTime;
    }

    public BUserinfo getbUserinfo() {
        return bUserinfo;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public void setUserSTATE(int userSTATE) {
        UserSTATE = userSTATE;
    }

    public void setUserVIP(int userVIP) {
        UserVIP = userVIP;
    }

    public int getUserSTATE() {
        return UserSTATE;
    }

    public int getUserVIP() {
        return UserVIP;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }


    public BaseModeul(Context context, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.gson = new Gson();
        this.userInfo = UserInfo.getInstance();
        this.tabs1 = context.getResources().getStringArray(R.array.tabs1);
        this.tabs2 = context.getResources().getStringArray(R.array.tabs2);
        this.tabs3 = context.getResources().getStringArray(R.array.tabs33);
    }


    /**
     * 读取保存的列表
     *
     * @return
     */
    public synchronized List<videotitle> mygetdata() {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        String json = shareInfo.getString(Constants.THREE, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return JsonUitl.stringToList(json, videotitle.class);
    }

    /**
     * 保存数据到本地
     */
    public void setUserInfo(List<videotitle> data) {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.THREE, gson.toJson(data));
        editor.commit();
    }

    /**
     * 分类对像保存
     */
    public void addvideotitle(videotitle svideo) {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.VIDEOTITLE, gson.toJson(svideo));
        editor.commit();
    }

    /**
     * 读取本地分类对像
     */
    public synchronized videotitle getvideotitle() {
        SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
        String json = shareInfo.getString(Constants.VIDEOTITLE, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, videotitle.class);
    }

    /**
     * 请求网络获取分类标题
     */
    public void Request() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, Userid);
        String posturl = com.tencent.opensource.model.HttpUtils.videotitle;
        HttpUtils.RequestPost(posturl, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success: " + response);
                try {
                    Mesresult mesresult = gson.fromJson(response, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        mygetdata = JsonUitl.stringToList(mesresult.getData(), videotitle.class);
                        setUserInfo(mygetdata);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "message:" + message);
            }
        });
    }

    /**
     * 获取本地分类
     */
    public void getsetTransferManager() {
        mygetdata = mygetdata();
        if (mygetdata == null) {
            Request();
        }

    }

    /**
     * 联网初始会员实名状态余额
     */
    public void inidateRequestPost() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, getUserid());
        params.put(Constants.TOKEN, getTOKEN());
        String path = com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API + "/goldcoin";
        HttpUtils.RequestPost(path, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                try {
                    Mesresult mesresult = gson.fromJson(response, Mesresult.class);
                    bUserinfo = gson.fromJson(mesresult.getData(), BUserinfo.class);
                    setUserSTATE(bUserinfo.getStatus());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "message:" + message);
            }
        });
    }

    public String getaccessUrl(String accessUrl, String type, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", Userid);
        map.put("title", title);
        map.put("bigpicurl", accessUrl);
        map.put("playurl", accessUrl);
        map.put("type", type);
        map.put("status", status);
        map.put("video", String.valueOf(TYPE));
        map.put("tencent", "1");
        return com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API + "/addvideolist?" + DefaultGiftAdapter.getMap(map, 3);
    }

    /**
     * POST请求数据 保存到服务端
     */
    public void RequestPost(String accessUrl, String imgCurrentUploadPath) {
        Map<String, String> params = new HashMap<>();
        params.put("tencent", "1");
        params.put("userid", Userid);
        params.put("title", title);
        params.put("bigpicurl", TYPE == ACTIVITY_VIDEO ? (TextUtils.isEmpty(imgCurrentUploadPath) ? "" : imgCurrentUploadPath) : accessUrl);
        params.put("playurl", accessUrl);
        params.put("status", String.valueOf(checkedItem1));
        params.put("type", jinbi > 0 ? "1" : "0"); //分费类型
        params.put("jinbi", String.valueOf(jinbi)); //分费类型
        params.put("video", String.valueOf(TYPE)); //用来区分视频或图片
        params.put("time", TextUtils.isEmpty(videoTime) ? "" : videoTime);
        params.put("token", TextUtils.isEmpty(TOKEN) ? userInfo.getToken() : TOKEN);
        params.put("fenleijb", String.valueOf(svideo.getId())); //分类内容
        if (checkedItem3 > 0) {
            params.put("province", TextUtils.isEmpty(province) ? "" : province);
            params.put("city", TextUtils.isEmpty(city) ? "" : city);
            params.put("district", TextUtils.isEmpty(district) ? "" : district);
            params.put("address", TextUtils.isEmpty(address) ? "" : address);
            params.put("jwd", TextUtils.isEmpty(jwd) ? "" : jwd);
        }

        String posturl = com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API + "/addvideolist";

        HttpUtils.RequestPost(posturl, params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.getInt("status");
                    String msg = jsonObject.getString("msg");
                    uploadActivity.toastMessage(msg);
                    uploadActivity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "message:" + message);
            }
        });
    }

    /**
     * 弹窗提交分类
     */
    public void dialogshow(UploadActivity upload) {
        uploadActivity = upload;
        dialog_show1.myshow(context, this);
    }

    /**
     * 实现文本复制功能
     *
     * @param content 复制的文本
     */
    public static void copy(String content, Context context) {
        if (!TextUtils.isEmpty(content)) {
            //得到剪贴板管理器
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("newPlainTextLabel", content);
            cmb.setPrimaryClip(clipData);
            //清空剪贴板管理器
            ClipData clipData2 = ClipData.newPlainText(null, content);
            cmb.setPrimaryClip(clipData2);
        }
    }

    /**
     * 获取系统剪贴板内容
     */
    public static String getClipContent(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * 清空剪贴板内容
     */
    public static void clearClipboard(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            try {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setText(null);
            } catch (Exception e) {

            }
        }
    }

    public void startActivity(TextView tv_url) {
        String path = tv_url.getText().toString();
        if (!path.isEmpty()) {
            //第1种文方式
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

            //第2种文方式
            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setData(uri);
            //context.startActivity(intent);
        }
    }

    /**
     * 获取视频缩略图 在Android里获取视频的信息主要依靠MediaMetadataRetriever实现
     *
     * @param vedioFile
     * @return
     */
    public static Bitmap getVedioThumbnail(File vedioFile) {
        if (!vedioFile.exists()) {
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(vedioFile.getAbsolutePath());
        Bitmap bitmap = retriever.getFrameAtTime();
        //Bitmap bitmap = retriever.getFrameAtTime(1000);//参数为毫秒,就是返回靠近这个时间的帧图
        return bitmap;
    }

    /**
     * 返回视频播放总时长
     *
     * @param vedioFile
     * @return
     */
    public static Long getVedioTotalTime(File vedioFile) {
        if (!vedioFile.exists()) {
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(vedioFile.getAbsolutePath());
        String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        Long time = Long.valueOf(timeString);
        return time;

    }

    /**
     * 选择是否收费
     */
    public void sAlertDialog(TextView tv) {
        if (tv == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.tv_ss37);
        builder.setSingleChoiceItems(tabs1, checkedItem1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                tv.setText(tabs1[which]);

                checkedItem1 = which;
            }
        });
        builder.show();

    }

    /**
     * 是否显示位置信息
     */
    public void sAlertDialogpgs(TextView tv) {
        if (tv == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.tv_ss39);
        builder.setSingleChoiceItems(tabs3, checkedItem3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (which == 1) {
                    tv.setText(tabs3[which]);
                } else {
                    tv.setText(!TextUtils.isEmpty(province) ? province + "-" + city : context.getString(R.string.tv_aa_1) + "");
                }

                checkedItem3 = which;
            }
        });
        builder.show();

    }

    /**
     * 选择收费金币
     */
    public void sAlertDialogjinbi(TextView tv2) {
        if (tv2 == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.tv_ss38);
        builder.setSingleChoiceItems(tabs2, checkedItem2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                double v = Double.parseDouble(tabs2[which]);
                setJinbi(v);
                tv2.setText(v > 0 ? tabs2[which] + " 金币" : "免费");
                checkedItem2 = which;
            }
        });
        builder.show();

    }

    /**
     * 当前年月日时分秒+五位随机数
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + rannum;// 当前时间 + 系统5随机生成位数
    }

    @Override
    public String toString() {
        return "BaseModeul{" +
                "UserSTATE=" + UserSTATE +
                ", UserVIP=" + UserVIP +
                ", folderName='" + folderName + '\'' +
                ", Userid='" + Userid + '\'' +
                ", TYPE=" + TYPE +
                ", title='" + title + '\'' +
                ", isvupcuimg=" + isvupcuimg +
                ", videoTime='" + videoTime + '\'' +
                ", svideo=" + svideo +
                ", vedioTotalTime=" + vedioTotalTime +
                ", TOKEN='" + TOKEN + '\'' +
                ", jinbi=" + jinbi +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", jwd='" + jwd + '\'' +
                '}';
    }
}
