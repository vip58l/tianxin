package com.tianxin.activity.edit;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
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
import com.tianxin.R;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.PostModule;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.costransferpractice.utils.CacheUtil;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;
import static com.tianxin.Util.Config.getFileName;
import static com.tianxin.Util.Config.setResult;
import static com.tianxin.app.DemoApplication.cosXmlService;
import static com.tianxin.tencent.cos.MySessionCredentialProvider.DELETEMultipleObject;

/**
 * 上传头像
 */
public class activity_uploadavatar extends BasActivity2 {
    private String TAG = activity_uploadavatar.class.getSimpleName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.titlemsg)
    TextView titlemsg;
    @BindView(R.id.avatar_msg)
    TextView avatar_msg;
    private Uri imageUri;

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.transparent));
        return R.layout.activity_uploadavatar;
    }

    @Override
    public void iniview() {
        context = this;
        itemback.settitle(getString(R.string.picavatar));
        avatar_msg.setText(userInfo.getInreview() == 0 ? getString(R.string.aa1) : getString(R.string.aa2));
        avatar_msg.setTextColor(userInfo.getInreview() == 0 ? getResources().getColor(R.color.homeback) : getResources().getColor(R.color.colorAccent));

        itemback.setHaidtopBackgroundColor(true);
        userInfo = UserInfo.getInstance();
        TYPE = getIntent().getIntExtra(Constants.TYPE, -1);
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            ImageLoadHelper.glideShowCornerImageWithUrl(context, userInfo.getSex().equals("1") ? R.mipmap.avatar : R.mipmap.avatar2, avatar);
        } else {
            //设置控件宽度
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            ViewGroup.LayoutParams params = avatar.getLayoutParams();
            params.width = (int) (displayMetrics.widthPixels * 0.8F);
            params.height = (int) (displayMetrics.widthPixels * 0.8F);
            avatar.setLayoutParams(params);
            ImageLoadHelper.glideShowCornerImageWithUrl(context, userInfo.getAvatar(), avatar);
        }
        if (!TextUtils.isEmpty(userInfo.getSex())) {
            titlemsg.setText(String.format(titlemsg.getText().toString(), userInfo.getSex().equals("1") ? getString(R.string.sex2) : getString(R.string.sex3)));
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.upload, R.id.contentcll})
    public void OnClick(View v) {
        if (userInfo.getState() == 3) {
            dialog_Blocked.myshow(context);
            return;
        }
        switch (v.getId()) {
            case R.id.upload:
                if (requestPermissions(1))
                    toPicture();
                break;
            case R.id.contentcll:
                if (requestPermissions(2))
                    toCamera();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPEN_FILE_CODE: //相册选择回调
                    startUCrop(data.getData());
                    break;
                case OPEN_CAMERA_CODE://相机拍照回调
                    // 通过存储的uri 查询File
                    starPhotoZoom(imageUri);
                    break;
                case IMAGE_CROP_CODE:
                    SetImageToView(data); //裁剪成功回调
                    break;
                case UCrop.REQUEST_CROP:
                    //第三方裁剪库回调
                    startUCrop(data);
                    break;
            }
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
     * 获取视频图片和播放时长
     *
     * @param file
     */
    private static void FileVideo(File file) {
        //视频获取图片
        Bitmap bitmap = getVedioThumbnail(file);
        //返回视频播放总时长
        Long vedioTotalTime = getVedioTotalTime(file);
        String time = Config.generateTime(vedioTotalTime);
    }

    /**
     * 保存图片文件到本地
     */
    public void saveBitmapfile(String path) throws ExecutionException, InterruptedException {
        //通过Glide保存图片
        File file = Glide.with(activity).asFile().load(path).submit().get();
        //插入到相册
        displayToGallery(context, file);
    }

    /**
     * 将图片文件插入到相册
     *
     * @param context
     * @param file
     */
    public void displayToGallery(Context context, File file) {
        if (!file.exists()) {
            return;
        }
        String photoPath = file.getAbsolutePath();
        String photoName = file.getName();
        try {
            //把文件插入到系统图库
            //ContentResolver contentResolver = context.getContentResolver();
            //MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);

            //通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 上传头像保存到服务器
     */
    private void uploads(File file) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getResources().getString(R.string.eorrfali2));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put(Constants.PATH, Webrowse.uploads);
        params.put(Constants.USERID, userInfo.getUserId());
        params.put(Constants.Token, userInfo.getToken());
        PostModule.okhttpimage(params, file, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        file.delete();
                        if (file != null) {
                            file.delete();
                        }
                        userInfoAvatar(mesresult.getPicture());
                    }
                } catch (Exception e) {
                    Toashow.show(e.getMessage());
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
     * 头像保存到腾讯云端
     */
    public void uploadavatar(File file) {
        try {
            //初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
            TransferConfig transferConfig = new TransferConfig.Builder().build();
            //初始化 TransferManager
            TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
            String bucket = DemoApplication.bucket.name;  //存储桶，格式：BucketName-APPID
            String cosPath = String.format("avatar/%s/%s", Config.DateTime(false), file.getName());   //对象在存储桶中的位置标识符，即称对象键
            String srcPath = file.getPath(); //本地文件的绝对路径

            //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
            String uploadId = null;
            // 上传文件
            COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
            //设置返回结果回调
            cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                    String accessUrl = cOSXMLUploadTaskResult.accessUrl;
                    if (file != null) {
                        file.delete();

                        try {
                            String avatar = userInfo.getAvatar();
                            if (!TextUtils.isEmpty(avatar)) {
                                String fileName = getFileName(avatar, ".com/");
                                List<String> objectList = Arrays.asList(fileName);
                                DELETEMultipleObject(objectList);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    updateaccessUrl(accessUrl);
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                    if (clientException != null) {
                        clientException.printStackTrace();
                    } else {
                        serviceException.printStackTrace();
                    }
                    dialogLoadings();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            toastLongMessage(getString(R.string.regs1upate));
            dialogLoadings();
        }
    }

    /**
     * 转到相机拍照
     */
    private void toCamera() {
        imageUri = createImageFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //拍照图片保存到指定的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, OPEN_CAMERA_CODE);
        }

    }

    /**
     * 转到相册
     */
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_FILE_CODE);
    }

    /**
     * 保存文件位置 拍照图片保存到指定的路径
     *
     * @return
     */
    private Uri createImageFile() {
        String FileName = System.currentTimeMillis() + ".jpg";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            //APP私有目录 外部存储设备上的应用专用目录
            File cropPhoto = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), FileName);
            return FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", cropPhoto);
        } else {
            //APP私有目录 外部存储设备上的应用专用目录
            File cropPhoto = new File(getExternalCacheDir(), FileName);
            return Uri.fromFile(cropPhoto);
        }
    }

    /**
     * 图片裁截处理
     *
     * @param uri
     */
    private void starPhotoZoom(Uri uri) {
        if (uri == null) {
            Toashow.show("拍照数据返回空");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");  //设置裁截
        intent.putExtra("aspectX", Build.MANUFACTURER.equals("HUAWEI") ? 1 : 1);
        intent.putExtra("aspectY", Build.MANUFACTURER.equals("HUAWEI") ? 1 : 1);
        intent.putExtra("outputX", 300); //宽尺寸
        intent.putExtra("outputY", 300); //高尺寸
        intent.putExtra("return-data", true);//剪裁后，是否返回 Bitmap
        intent.putExtra("scale ", true);     //是否保留比例
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//图片的输出格式
        intent.putExtra("noFaceDetection", false);       //人脸识别
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);          //设置选择图片和裁剪图片的路径
        startActivityForResult(intent, IMAGE_CROP_CODE);
    }

    /**
     * 设置裁截图片保存上传
     *
     * @param data
     */
    private void SetImageToView(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap bitmap = (data == null || data.getData() == null) ? imageUriimageUri() : extras.getParcelable("data");
        avatar.setImageBitmap(bitmap);
        try {
            //android 11分区保存图片/storage/emulated/0/Pictures
            String FilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            Log.d(TAG, "FilePath: " + FilePath);

            //android 10以下 /storage/emulated/0
            String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d(TAG, "absolutePath: " + absolutePath);

            //storage/emulated/0/Android/data/com.paixide/files/Pictures
            String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            Log.d(TAG, "path: " + path);

            //storage/emulated/0/Android/data/com.paixide/cache/1646635494633.jpg
            File cropPhoto = new File(CacheUtil.getCacheDirectory(context, null), System.currentTimeMillis() + ".jpg");
            Log.d(TAG, "cropPhoto0: " + cropPhoto.getAbsolutePath());

            //storage/emulated/0/Android/data/com.paixide/files/Pictures/1646635494633.jpg
            File cropPhoto1 = new File(path + File.separator + System.currentTimeMillis() + ".jpg");
            Log.d(TAG, "cropPhoto1: " + cropPhoto1.getAbsolutePath());

            //保存到cropPhoto文件
            Imagecompressiontool.oNqualityCompress(bitmap, cropPhoto);

            //将照片添加到图库
            Log.d(TAG, "将照片添加到图库: ");
            galleryAddPic(cropPhoto.getAbsolutePath());

            Toashow.show(getString(R.string.pics) + cropPhoto.getAbsolutePath());

            setResult(setResult, data);

            //图片上传到服务端
            //uploads(cropPhoto);

            //图片上传头像到腾讯云端
            new Thread(new myThread(cropPhoto)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 动态申请相机权限
     *
     * @param type
     * @return
     */
    private boolean requestPermissions(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            for (String permission : list) {
                int star = ContextCompat.checkSelfPermission(context, permission);
                if (star != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, list.toArray(new String[list.size()]), type == 1 ? OPEN_SET_REQUEST_CODE : OPEN_CAMERA_CODE);
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case OPEN_SET_REQUEST_CODE:
                for (String permission : permissions) {
                    Log.d(TAG, "=====" + permission);
                    int STATE = ContextCompat.checkSelfPermission(context, permission);
                    if (STATE != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "获取授权失败 ");
                        return;
                    }
                }
                toPicture();
                break;
            case OPEN_CAMERA_CODE:
                for (String permission : permissions) {
                    Log.d(TAG, "=====" + permission);
                    int STATE = ContextCompat.checkSelfPermission(context, permission);
                    if (STATE != PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "获取授权失败 ");
                        return;
                    }
                }
                toCamera();
                break;
        }
    }

    /**
     * 提交给腾讯IM修改个人资料
     *
     * @param accessUrl
     */
    public void userInfoAvatar(String accessUrl) {
        //设置本地图片显示
        userInfo.setAvatar(accessUrl);
        //更新腾讯IM个人资料
        activity_sesemys.updateProfile(userInfo);

        //上传头像后返回指定ACTIVITY
        if (TYPE == Constants.TENCENT) {
            startActivity(new Intent(context, activity_updateedit.class));
        }
        dialogLoadings();
        finish();
    }

    /**
     * 上传头像回调事件
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            dialogLoadings();
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
            dialogLoadings();
        }

        @Override
        public void onSuccess(String accessUrl) {
            userInfoAvatar(accessUrl);
        }


    };

    /**
     * 启动线程上传头像到腾讯云端
     */
    private class myThread extends Thread {
        private File file;

        public myThread(File file) {
            this.file = file;

        }

        @Override
        public void run() {
            super.run();
            try {
                netonk = Config.loginByGet();
                uploadavatar(file);//上传到腾讯
            } catch (IOException e) {
                e.printStackTrace();
                dialogLoadings();
            }
        }

    }

    /**
     * 提交保存到服务端
     *
     * @param accessUrl
     */
    private void updateaccessUrl(String accessUrl) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datamodule.Postsave(accessUrl, paymnets);
            }
        });
    }

    /**
     * 打开相机拍照回调裁剪图片
     */
    private void startUCrop() {
        //裁剪后保存到文件中
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        Uri destinationUri = Uri.fromFile(new File(CacheUtil.getCacheDirectory(this, null), fileName));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //是否能调整裁剪框
        //options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1); //比例
        uCrop.start(this);
    }

    /**
     * 打开相册回调裁剪图片
     *
     * @param imageUri
     */
    private void startUCrop(Uri imageUri) {
        //裁剪后保存到文件中
        String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        Uri destinationUri = Uri.fromFile(new File(CacheUtil.getCacheDirectory(this, null), fileName));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //是否能调整裁剪框
//        options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.withAspectRatio(1, 1); //比例
        uCrop.start(this);
    }

    /**
     * 第三方裁剪库回调处理
     *
     * @param data
     */
    private void startUCrop(Intent data) {
        //回调到上级方便刷新UI
        setResult(setResult, data);

        final Uri croppedUri = UCrop.getOutput(data);
        try {
            dialogshow(getString(R.string.mbttavity));
            if (croppedUri != null) {
                Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
                avatar.setImageBitmap(bit);
                File file = new File(new URI(croppedUri.toString()));
                //图片上传头像到腾讯云端
                new Thread(new myThread(file)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 转到相机拍照
     */
    private void toCamera2() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile2();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", photoFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, OPEN_CAMERA_CODE);

        } else {
            Toashow.show("未找到可用相机");
        }
    }

    private File createImageFile2() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //APP私有目录
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }

    /**
     * 将照片添加到图库
     *
     * @param currentPhotoPath
     */
    private void galleryAddPic(String currentPhotoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    /**
     * 对调整后的图片进行解码
     *
     * @param currentPhotoPath
     */
    private void setPic(String currentPhotoPath) {
        // Get the dimensions of the View
        int targetW = avatar.getWidth();
        int targetH = avatar.getHeight();
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        avatar.setImageBitmap(bitmap);
    }

    public Bitmap imageUriimageUri() {
        try {
            //Bitmap与uri的相互转换
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));

            //Bitmap与uri的相互转换
            //Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}