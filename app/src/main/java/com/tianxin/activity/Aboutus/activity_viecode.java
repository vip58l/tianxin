package com.tianxin.activity.Aboutus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.tianxin.Util.Constants;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.dialog.dialog_Cancel_account;
import com.tianxin.getlist.okhttp;
import com.tencent.opensource.model.Basicconfig;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.version;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

import static com.tianxin.Util.update.getFileName;

/**
 * 关于我们
 */
public class activity_viecode extends BasActivity2 {

    private static final String TAG = activity_viecode.class.getSimpleName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    String[] packageversion;
    version version;
    @BindView(R.id.VersionName)
    TextView VersionName;
    @BindView(R.id.tm_m2)
    TextView tm_m2;
    @BindView(R.id.tm_m1)
    TextView tm_m1;
    @BindView(R.id.tm_m3)
    TextView tm_m3;

    public static void starsetAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_viecode.class);
        context.startActivity(intent);
    }


    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.home3));
        return R.layout.activity_activityviecode;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg112));
        itemback.setHaidtopBackgroundColor(true);
        packageversion = Config.getPackageversion(context);
        VersionName.setText(getString(R.string.ts4) + packageversion[0]);
    }

    @Override
    public void initData() {
        datamodule.getbasicconfig(paymnets);
    }


    @Override
    public void OnEorr() {

    }

    @Override
    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout456})
    public void OnClick(View v) {
        String url = BuildConfig.HTTP_API + "/invitefriends?type=%s&userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken();
        switch (v.getId()) {
            case R.id.layout1://
                //读取系统检查是否有最新版本的APP更新
                datamodule.version(paymnets);
                break;
            case R.id.layout2://
                WebbookUrl(context, String.format(url, 2));
                break;
            case R.id.layout3://
                WebbookUrl(context, String.format(url, 3));
                break;
            case R.id.layout4://
                WebbookUrl(context, String.format(url, 4));
                break;
            case R.id.layout5:
                //联系客服
                WebbookUrl(context, String.format(url, 5));
                break;
            case R.id.layout456:
                //注销帐号
                dialog_Cancel_account.myshow(context, CallBack);
                break;
        }
    }

    /**
     * 提示下载
     *
     * @param
     */
    public static void showUpdaloadDialog2(Context context, version version) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_item_upmsg, null);
        TextView title = inflate.findViewById(R.id.title);
        TextView message = inflate.findViewById(R.id.message);
        TextView send = inflate.findViewById(R.id.send);
        title.setText(context.getString(R.string.dialog_tv1));
        message.setText(Html.fromHtml(version.getTitle()));
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.fei_style_dialog);
        builder.setView(inflate);
        builder.setPositiveButton(context.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载最新的版本APK
                if (version.getPath().toLowerCase().endsWith(".apk")) {
                    //应用内下载并安装
                    loadUploadshow(context, version.getPath());
                } else {
                    //打开外部浏览器
                    updatedown(context, version.getPath());
                }

            }
        }).setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 提示更新版本
     *
     * @param context
     * @param version
     */
    public static void showUpdaloadDialog(Context context, version version) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_item_upmsg, null);
        TextView title = inflate.findViewById(R.id.title);
        TextView message = inflate.findViewById(R.id.message);
        TextView send = inflate.findViewById(R.id.send);
        title.setText(context.getString(R.string.dialog_tv1));
        message.setText(Html.fromHtml(version.getTitle()));
        Dialog dialog = new Dialog(context, R.style.fei_style_dialog);
        dialog.setContentView(inflate);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loadUpload(context, version.getPath());
            }
        });
    }

    /**
     * 下载进度条创建
     *
     * @param
     */
    public static void loadUploadshow(Context context, String path) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(context.getString(R.string.dialog_tv2));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        //创建子线程
        new Thread() {
            @Override
            public void run() {
                super.run();
                Downloadload(context, path.toLowerCase().trim(), progressDialog);//执行下载
            }
        }.start();


    }

    /**
     * 执行下载文件
     *
     * @param path
     * @param progressDialog
     */
    private static void Downloadload(Context context, String path, ProgressDialog progressDialog) {
        String save = createFile(context);
        Log.d(TAG, "Downloadload: " + save);
        String filename = getFileName(path);

        //创建本地自定义保存目录
        File dir = new File(save);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //创建文件对像
        File file = new File(save, filename);
        try {
            //这里OKHTTP很重要的一句话 开启子线程否则下载失败
            Response response = okhttp.response(path);
            if (response != null) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(file); //输出流
                long length = response.body().contentLength();              //下载文件大小
                progressDialog.setMax((int) ((length * 100) / length));     //最大值
                byte[] buf = new byte[10240];
                int len = -1;
                int process = 0;
                while ((len = inputStream.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                    process += len;
                    long proces = (process * 100) / length;
                    progressDialog.setProgress((int) proces);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                response.body().close();
                response.close();
                progressDialog.dismiss();
            }
            //文件下载完成发启安装
            openAPK(context, file);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 下载完成安装apk，给系统发送一个intent
     * 发起安装提示1
     *
     * @param context
     * @param
     */
    private static void openAPK(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0以上正在安装应用
            instarapk(context, file);
        } else {
            //8.0以下正在安装应用
            installApk(context, file);
        }
    }

    /**
     * 下载最新的版本APK
     *
     * @param context
     * @param path
     */
    public static void loadUpload(Context context, String path) {
        if (path.toLowerCase().endsWith(".apk")) {
            //应用内下载并安装
            loadUploadshow(context, path);
        } else {
            //打开外部浏览器
            updatedown(context, path);
        }
    }

    //android 8.0以上执行安装应用
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void instarapk(Context context, File file) {
        //别忘了在清单文件增加fileprovider 自动安装应用权限 AndroidManifest.xml
        //是否获得未知安装权限 判断应用是否有权限安装apk
     /*   boolean Installs = context.getPackageManager().canRequestPackageInstalls();
        if (!Installs) {
            openUnknownpermission(context);
            return;
        }

        */
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //执行android8.0 以下应用安装
    private static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    //去打开未知应用权限
    private static void openUnknownpermission(Context context) {
        //获取应用包名称
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        //去打开未知应用权限
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        Activity activity = (Activity) context;
        activity.startActivityForResult(intent, Config.sussess);

        //请求安装未知应用来源的权限
        //ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, config.sussess);

    }

    /**
     * 调用系统浏览器下载文件
     */
    public static void updatedown(Context context, String path) {
        if (!path.isEmpty()) {
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setData(uri);
            //context.startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.sussess) {
            //有权限且用户允许安装
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                //将用户引导至安装未知应用界面。
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                startActivityForResult(intent, Config.sussess);
            }
        }
    }

    /**
     * 打开浏览器
     *
     * @param context
     * @param url
     */
    public static void WebbookUrl(Context context, String url) {
        Intent intent = new Intent(context, DyWebActivity.class);
        intent.putExtra(Constants.VIDEOURL, url);
        context.startActivity(intent);
    }


    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            version = (com.tianxin.Module.api.version) object;
            int Version = version.getVersion();
            int versionCode = Config.getVersionCode();
            if (Version > versionCode) {
                showUpdaloadDialog(activity, activity_viecode.this.version);
            } else {
                Toashow.show(getString(R.string.tma12));
            }
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(getString(R.string.ts7));
        }

        @Override
        public void onSuccess(Object object, int Type) {
            Basicconfig basicconfig = (Basicconfig) object;
            tm_m2.setText(basicconfig.getCompany());
            tm_m3.setText(basicconfig.getPhone());
            tm_m1.setText(String.format(getString(R.string.tv_ms_m1) + "", basicconfig.getWebsite(), basicconfig.getIcp()));
        }
    };

    private static String createFile(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R ? context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() : Environment.getExternalStorageDirectory().getAbsolutePath() + "/paixide";
    }

    /**
     * 申请注销回调
     */
    private Paymnets CallBack = new Paymnets() {
        @Override
        public void onSuccess() {
            Activity_logout.starAction(context);
        }

        @Override
        public void onFail(String msg) {
            Toashow.show(msg);
        }
    };

}
