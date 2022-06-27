package com.tianxin.activity;

import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.tianxin.utils.AES.AES;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.activity.ZYservices.activity_photo_album;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Exit;
import com.tianxin.getHandler.PostModule;
import com.tianxin.widget.itembackTopbr;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.gethelp;
import com.tencent.qcloud.costransferpractice.transfer.Api;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.tianxin.Util.Imagecompressiontool.dataDir;
import static com.tianxin.Util.Config.getFileName;
import static com.tianxin.Util.Config.setResult;
import static com.tianxin.app.DemoApplication.cosXmlService;
import static com.tianxin.tencent.cos.MySessionCredentialProvider.DELETEMultipleObject;

/**
 * 上传资格证书
 */
public class activit_Invitefriends extends BasActivity2 {
    private final String TAG = "activit_Invitefriends";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.senbnt)
    TextView senbnt;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.rlayout)
    RelativeLayout rlayout;

    gethelp g;
    public final int OPEN_FILE_CODE = 10001;
    public final int OPEN_CAMERA = 10002;
    public final int IMAGE_CROP_CODE = 10003;
    private File dataDir;
    private boolean isdatadirboot = false;

    @Override
    protected int getview() {
        return R.layout.activity_invitefriends;
    }

    @Override
    public void iniview() {
        itemback.setHaidtopBackgroundColor(true);
        itemback.settitle("我的资格认证");
        userInfo = UserInfo.getInstance();

    }

    @Override
    public void initData() {
    PostModule.getModule(BuildConfig.HTTP_API + "/homegethelp?userid=" + userInfo.getUserId()+"&token="+userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    g = new Gson().fromJson(date, gethelp.class);
                    name1.setText(TextUtils.isEmpty(g.getNametitle()) ? "" : g.getNametitle());
                    if (!TextUtils.isEmpty(g.getPic())) {
                        senbnt.setEnabled(false);
                        Glideload.loadImage(icon, g.getPic(), 6);
                        rlayout.setVisibility(View.VISIBLE);
                        switch (g.getType()) {
                            case 0:
                                senbnt.setText(R.string.livebr_tv3);
                                senbnt.setVisibility(View.GONE);
                                return;
                            case 1:
                                senbnt.setText(R.string.tv_msg8);
                                return;
                            case 2:
                                senbnt.setText(R.string.tv_msg126);
                                return;
                            case 3:
                            default:
                                break;

                        }

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
    @OnClick({R.id.icon, R.id.senbnt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.senbnt:
                if (dataDir != null && isdatadirboot) {
                    upatefile();
                } else {
                    toPicture();
                }
                break;
            case R.id.icon:
                if (g != null) {
                    Intent intent = new Intent(this, activity_photo_album.class);
                    intent.putExtra(Constants.PATHVIDEO, g.getPic());
                    startActivity(intent);
                }
                break;
        }

    }

    @Override
    public void OnEorr() {



    }


    //转到相册
    private void toPicture() {
        isdatadirboot = false;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_FILE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_FILE_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    starPhotoZoom(data.getData());
                }
                break;
            case IMAGE_CROP_CODE:
                //裁剪图片回调
                SetImageToView(data);
                break;

        }
    }

    //图片裁截处理
    private void starPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁截
        intent.putExtra("crop", "true");
        //裁截宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁截图片质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        intent.putExtra("scale ", true);  //是否保留比例
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());//图片的输出格式
        intent.putExtra("noFaceDetection", false);  //关闭面部识别
        //设置剪切的图片保存位置
        //Uri cropUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/paixide/" + System.currentTimeMillis() + ".png"));
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    //设置裁截图片
    private void SetImageToView(Intent data) {
        if (data == null || data.getExtras() == null) {
            return;
        }
        Bundle extras = data.getExtras();
        Bitmap bitmap = extras.getParcelable("data");
        if (bitmap != null) {
            rlayout.setVisibility(View.VISIBLE);
        }
        icon.setImageBitmap(bitmap);
        dataDir = dataDir();
        Imagecompressiontool.oNqualityCompress(bitmap, dataDir);
        setResult(setResult, data);
        isdatadirboot = true;

    }

    /**
     * 头像保存到腾讯云端
     */
    public void uploadavatar(Context context, File file) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(context.getResources().getString(R.string.eorrfali2));
            return;
        }
        //初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        //初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        String bucket = DemoApplication.bucket.name;  //存储桶，格式：BucketName-APPID
        String cosPath = String.format("card/%s/%s", Config.DateTime(false), file.getName());   //对象在存储桶中的位置标识符，即称对象键
        String srcPath = file.getPath(); //本地文件的绝对路径
        //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
        String uploadId = null;
        // 上传文件
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                String accessUrl = cOSXMLUploadTaskResult.accessUrl;
                Log.d(TAG, "accessUrl: " + accessUrl);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FormBody(accessUrl, 4);
                    }
                });

                if (file != null) {
                    file.delete();
                    try {
                        String pic = g.getPic();
                        if (!TextUtils.isEmpty(pic)) {
                            List<String> objectList = Arrays.asList(Api.getFileName(pic));
                            DELETEMultipleObject(objectList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if (clientException != null) {
                    clientException.printStackTrace();
                } else {
                    serviceException.printStackTrace();
                }
            }
        });

    }

    /**
     * 提交保存
     *
     * @param obj
     * @param i
     */
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

    public void upatefile() {
        Dialog_Exit.dialogshow(this, new Paymnets() {
            @Override
            public void activity() {

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            boolean netonk = Config.loginByGet();
                            //上传到腾讯云
                            uploadavatar(activit_Invitefriends.this, dataDir);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toashow.show(getString(R.string.eorrfali3));
                        }
                    }
                }.start();

            }

            @Override
            public void payens() {
            }
        });
    }

}