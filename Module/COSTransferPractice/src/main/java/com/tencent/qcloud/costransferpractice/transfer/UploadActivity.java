package com.tencent.qcloud.costransferpractice.transfer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.ListAllMyBuckets;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.costransferpractice.BuildConfig;
import com.tencent.qcloud.costransferpractice.CosServiceFactory;
import com.tencent.qcloud.costransferpractice.common.base.BaseModeul;
import com.tencent.qcloud.costransferpractice.dialog.Callback;
import com.tencent.qcloud.costransferpractice.dialog.dialog_show2;
import com.tencent.qcloud.costransferpractice.object.ObjectActivity;
import com.tencent.qcloud.costransferpractice.utils.Constants;
import com.tencent.qcloud.costransferpractice.utils.FileUtils;
import com.tencent.qcloud.costransferpractice.utils.Imagecompressiontool;
import com.tencent.qcloud.costransferpractice.R;
import com.tencent.qcloud.costransferpractice.common.FilePathHelper;
import com.tencent.qcloud.costransferpractice.common.Utils;
import com.tencent.qcloud.costransferpractice.common.base.BaseActivity;
import com.tencent.qcloud.costransferpractice.utils.StatusBarUtil;

import java.io.File;
import java.util.List;

import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_TOKEN;
import static com.tencent.qcloud.costransferpractice.utils.Constants.REQ_PERMISSION_CODE;
import static com.tencent.qcloud.costransferpractice.utils.FileUtils.isNetworkAvailable;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_EXTRA_BUCKET_NAME;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_EXTRA_FOLDER_NAME;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_EXTRA_REGION;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_IMG;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_STATE;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_TYPE;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_USERID;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIDEO;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIP;
import static com.tencent.qcloud.costransferpractice.transfer.Api.getFileName;

/**
 * Created by jordanqin on 2020/6/18.
 * 文件上传页面
 * <p>
 * Copyright (c) 2010-2020 Tencent Cloud. All rights reserved.
 */
public class UploadActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = UploadActivity.class.getSimpleName();
    private int PERMISSIONS = 10001;
    private int OPEN_FILE_CODE = 10001;
    private long feiliength = 1024 * 1024 * 50;    //限制上传文件大小
    private long sfeiliength1 = 1024 * 1024 * 12;  //压缩标准10M以下
    private long sfeiliength2 = 1024 * 1024;       //压缩标准1M以下
    private int uptime = 60;                       //上传视频不能超过2分钟
    private String UP_VIDEO = "video";
    private String UP_PICTURE = "picture";

    private RelativeLayout supdate;
    //图片文件本地显示
    private ImageView iv_image;
    //上传状态
    private TextView tv_state;
    //上传进度
    private TextView tv_progress;
    //上传进度条
    private ProgressBar pb_upload;
    //操作按钮（开始和取消）
    private TextView btn_left;
    public String bucketName;
    private String bucketRegion;
    /**
     * {@link CosXmlService} 是您访问 COS 服务的核心类，它封装了所有 COS 服务的基础 API 方法。
     * <p>
     * 每一个{@link CosXmlService} 对象只能对应一个 region，如果您需要同时操作多个 region 的
     * Bucket，请初始化多个 {@link CosXmlService} 对象。
     */
    public CosXmlService cosXmlService;
    /**
     * {@link TransferManager} 进一步封装了 {@link CosXmlService} 的上传和下载接口，当您需要
     * 上传文件到 COS 或者从 COS 下载文件时，请优先使用这个类。
     */
    private TransferManager transferManager;
    private COSXMLUploadTask cosxmlTask;
    /**
     * 上传时的本地和COS 路径
     */
    private String currentUploadPath;
    /**
     * 上传的本地压缩过的图片地址
     **/
    private String imgCurrentUploadPath;
    private TextView tev_show;
    private TextView hideshow;
    private boolean upfile = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(this);
        setContentView(R.layout.upload_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        bucketName = getIntent().getStringExtra(ACTIVITY_EXTRA_BUCKET_NAME);
        bucketRegion = getIntent().getStringExtra(ACTIVITY_EXTRA_REGION);
        baseModeul.setFolderName(getIntent().getStringExtra(ACTIVITY_EXTRA_FOLDER_NAME));

        baseModeul.setUserid(getIntent().getStringExtra(ACTIVITY_USERID));
        baseModeul.setTYPE(getIntent().getIntExtra(ACTIVITY_TYPE, -1));
        baseModeul.setUserVIP(getIntent().getIntExtra(ACTIVITY_VIP, -1));
        baseModeul.setUserSTATE(getIntent().getIntExtra(ACTIVITY_STATE, -1));
        baseModeul.setTOKEN(getIntent().getStringExtra(ACTIVITY_TOKEN));

        //GPS定位
        baseModeul.setProvince(getIntent().getStringExtra(ObjectActivity.province));
        baseModeul.setCity(getIntent().getStringExtra(ObjectActivity.city));
        baseModeul.setDistrict(getIntent().getStringExtra(ObjectActivity.district));
        baseModeul.setAddress(getIntent().getStringExtra(ObjectActivity.address));
        baseModeul.setJwd(getIntent().getStringExtra(ObjectActivity.jwd));

        baseModeul.getsetTransferManager();//初始分类
        baseModeul.inidateRequestPost();   //联网初始会员实名状态余额

        getSupportActionBar().setTitle(baseModeul.getTYPE() == 2 ? getString(R.string.img_call_1) : getString(R.string.videl_2));

        if (TextUtils.isEmpty(BuildConfig.COS_SECRET_ID) || TextUtils.isEmpty(BuildConfig.COS_SECRET_KEY)) {
            finish();
        }
        if (TextUtils.isEmpty(bucketName) || TextUtils.isEmpty(bucketRegion)) {
            getBuckets1();
        } else {
            getBuckets2();
        }

        iniview();
    }

    private void iniview() {
        iv_image = findViewById(R.id.iv_image);
        tv_state = findViewById(R.id.tv_state);
        tv_progress = findViewById(R.id.tv_progress);
        pb_upload = findViewById(R.id.pb_upload);
        btn_left = findViewById(R.id.btn_left);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        supdate = findViewById(R.id.supdate);
        hideshow = findViewById(R.id.hideshow);
        tev_show = findViewById(R.id.tev_show);
        supdate.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        findViewById(R.id.r1).setOnClickListener(v -> baseModeul.sAlertDialog(tv1));
        findViewById(R.id.r2).setOnClickListener(v -> baseModeul.sAlertDialogjinbi(tv2));
        findViewById(R.id.r3).setOnClickListener(v -> baseModeul.sAlertDialogpgs(tv3));
        btn_left.setVisibility(View.GONE);
        hideshow.setText(baseModeul.getTYPE() == 2 ? getString(R.string.img_call_1) : getString(R.string.videl_2));
        tev_show.setVisibility(baseModeul.getTYPE() == 2 ? View.GONE : View.VISIBLE);
        tv3.setText(!TextUtils.isEmpty(baseModeul.getProvince()) ? baseModeul.getProvince() + "." + baseModeul.getCity() : getString(R.string.gps_msg));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.choose_photo) {
            Intent intent = new Intent();
            intent.setAction("com.mywebview.activity.jsoup");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 刷新上传状态
     *
     * @param state 状态 {@link TransferState}
     */
    private void refreshUploadState(final TransferState state) {
        uiAction(new Runnable() {
            @Override
            public void run() {
                String s = state.toString();
                tv_state.setText(s);
            }
        });

    }

    /**
     * 刷新上传进度
     *
     * @param progress 已上传文件大小
     * @param total    文件总大小
     */
    private void refreshUploadProgress(final long progress, final long total) {
        uiAction(new Runnable() {
            @Override
            public void run() {
                pb_upload.setProgress((int) (100 * progress / total));
                tv_progress.setText(Utils.readableStorageSize(progress) + "/" + Utils.readableStorageSize(total));
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_left) {
            if ("暂停".contentEquals(btn_left.getText())) {
                if (cosxmlTask != null && cosxmlTask.getTaskState() == TransferState.IN_PROGRESS) {
                    cosxmlTask.pause();
                    btn_left.setText("恢复");
                } else {
                    toastMessage("操作失败");
                }
            }
            if ("恢复".contentEquals(btn_left.getText())) {
                if (cosxmlTask != null && cosxmlTask.getTaskState() == TransferState.PAUSED) {
                    cosxmlTask.resume();
                    btn_left.setText("取消");
                } else {
                    toastMessage("操作失败");
                }
            }
            if ("取消".contentEquals(btn_left.getText())) {
                if (cosxmlTask != null) {
                    cosxmlTask.cancel();
                    finish();
                } else {
                    toastMessage("操作失败");
                }
            }

            //上传文件之前判断下是否已经实名认证
            switch (baseModeul.getUserSTATE()) {
                case 0:
                    dialog_show2.mshow(context, getString(R.string.t1), callback);
                    break;
                case 1:
                    dialog_show2.mshow(context, getString(R.string.t4), getString(R.string.t8), 1, callback);
                    break;
                case 2:
                    upload();
                    break;
                case 3:
                    dialog_show2.mshow(context, getString(R.string.t5), getString(R.string.t7), 3, callback);
                    break;
                default:
                    dialog_show2.mshow(context, getString(R.string.t6), callback);
                    break;
            }
        }

        /**
         * 选择本地资源
         */
        if (v.getId() == R.id.supdate) {
            if (Constants.checkPermission(activity)) {
                if (cosxmlTask == null) {
                    sACTION_GET_CONTENT();
                } else {
                    toastMessage(getString(R.string.tv_msg_update));
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理反回的资源文件
         */
        myonActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION_CODE) {
            for (String permission : permissions) {
                int granted = ContextCompat.checkSelfPermission(context, permission);
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "打开相册失败 请重新打开", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //权限获取成功打开本地资源文件
            sACTION_GET_CONTENT();
        }

    }


    /**
     * 点击请先选择上传文件
     */
    public void upload() {
        if (!isNetworkAvailable(context)) {
            Toast.makeText(this, getString(R.string.tv1), Toast.LENGTH_LONG).show();
            return;
        }
        if (baseModeul.getbUserinfo() == null) {
            Toast.makeText(this, getString(R.string.tv12), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(currentUploadPath)) {
            toastMessage(getString(R.string.tv2));
            return;
        }
        if (cosxmlTask == null) {
            //获取本地文件
            File file = new File(currentUploadPath);
            //文件大于100M
            if (file.length() > feiliength) {
                toastMessage(String.format(getString(R.string.tv3) + "", (feiliength / 1024 / 1024)));
                tv_state.setText(String.format(getString(R.string.tv3) + "", feiliength / 1024 / 1024));
                return;
            }
            //是否视频
            boolean vedioFile = FileUtils.isVedioFile(file.getPath());
            //是否图片
            boolean img = FileUtils.getFileType(file.getPath());
            //***相关判断开始****/

            if (!vedioFile && !img) {
                toastMessage(getString(R.string.tv5));
                return;
            }
            if (!vedioFile && baseModeul.getTYPE() == ACTIVITY_VIDEO) {
                toastMessage(getString(R.string.tv6));
                return;
            }
            if (!img && baseModeul.getTYPE() == ACTIVITY_IMG) {
                toastMessage(getString(R.string.tv7));
                return;
            }

            //上传图片不可超过10M
            if (baseModeul.getTYPE() == ACTIVITY_IMG && file.length() > sfeiliength1) {
                toastMessage(getString(R.string.tv8));
                return;
            }

            //填写一个标题
            if (TextUtils.isEmpty(baseModeul.getTitle())) {
                baseModeul.dialogshow(this);
                return;
            }

            //***相关判断结束****/
            String cosPath = "";
            Bitmap bitmap = null;
            switch (baseModeul.getTYPE()) {
                case ACTIVITY_VIDEO:
                    if (baseModeul.getSvideo() == null) {
                        toastMessage("请选择一个分类标签");
                        baseModeul.dialogshow(this);
                        return;
                    }
                    bitmap = baseModeul.getVedioThumbnail(file);                          //获取视频预览图
                    baseModeul.setVedioTotalTime(baseModeul.getVedioTotalTime(file));    //返回视频播放总时长
                    baseModeul.setVideoTime(FileUtils.generateTime(baseModeul.getVedioTotalTime()));
                    if (baseModeul.getVedioTotalTime() / 1000 > uptime) {
                        String msg = String.format("上传视频不能超过%ss秒", uptime);
                        toastMessage(msg);
                        tv_state.setText(msg);
                        hideshow.setVisibility(View.GONE);
                        return;
                    }
                    cosPath = String.format("%s/%s/%s.mp4", UP_VIDEO, FileUtils.DateTime(false), !TextUtils.isEmpty(baseModeul.getUserid()) ? baseModeul.getUserid() + "_" + baseModeul.getRandomFileName() : baseModeul.getRandomFileName());
                    String newfileName = getFileName(String.format("%s.png", getFileName(cosPath, "\\.")), "/", true);
                    File videoImg = Imagecompressiontool.dataDir(newfileName);
                    Imagecompressiontool.qualityCompress(bitmap, videoImg); //Y压缩图片上传
                    imgCurrentUploadPath = videoupdate(videoImg);           //上传视频缩略图片并删除本地图片文件
                    break;
                case ACTIVITY_IMG:
                    bitmap = BitmapFactory.decodeFile(currentUploadPath);  //获取图片预览图
                    cosPath = String.format("%s/%s/%s.png", UP_PICTURE, FileUtils.DateTime(false), !TextUtils.isEmpty(baseModeul.getUserid()) ? baseModeul.getUserid() + "_" + baseModeul.getRandomFileName() : baseModeul.getRandomFileName());
                    //图片超过1M进行压缩
                    if (file.length() > sfeiliength2) {
                        File fileImg = Imagecompressiontool.dataDir(file.getName());
                        Imagecompressiontool.qualityCompress(bitmap, fileImg);
                        currentUploadPath = fileImg.getPath();
                        baseModeul.setIsvupcuimg(true);
                    }
                    break;
            }

            try {
                if (transferManager != null) {
                    //开始上传图片
                    cosxmlTask = transferManager.upload(bucketName, cosPath, currentUploadPath, null);
                    //设置上传进度回调刷新上载状态
                    cosxmlTask.setTransferStateListener(new TransferStateListener() {
                        @Override
                        public void onStateChanged(final TransferState state) {
                            refreshUploadState(state);
                        }
                    });
                    //设置返回结果回调 刷新上传进度
                    cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                        @Override
                        public void onProgress(final long complete, final long target) {
                            refreshUploadProgress(complete, target);
                        }
                    });
                    //设置任务状态回调, 可以查看任务过程
                    cosxmlTask.setCosXmlResultListener(new CosXmlResultListener() {
                        @Override
                        public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                            COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                            cosxmlTask = null;
                            //获取到网址保存起来
                            String accessUrl = cOSXMLUploadTaskResult.accessUrl;
                            setResult(RESULT_OK); //反回上一页刷新数据
                            uiAction(new Runnable() {
                                @Override
                                public void run() {
                                    toastMessage(getString(R.string.tv_udate_ok));
                                    btn_left.setVisibility(View.GONE);
                                    if (baseModeul.isIsvupcuimg()) {
                                        new File(currentUploadPath).delete(); //删除本地New复本文件
                                    }

                                    baseModeul.RequestPost(accessUrl, imgCurrentUploadPath);
                                }
                            });
                        }

                        @Override
                        public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                            if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                                cosxmlTask = null;
                                uiAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb_upload.setProgress(0);
                                        tv_progress.setText("");
                                        tv_state.setText("无");
                                    }
                                });
                            }
                            exception.printStackTrace();
                            serviceException.printStackTrace();
                        }
                    });
                    btn_left.setText("暂停");
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toastMessage("上传文件失败");
                            finish();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("上传文件失败");
                        finish();
                    }
                });
            }
        }
    }

    /**
     * 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
     */
    public void getBuckets2() {
        cosXmlService = CosServiceFactory.getCosXmlService(this, bucketRegion, BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, true);
        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        //初始化 TransferManager
        transferManager = new TransferManager(cosXmlService, transferConfig);
    }

    /**
     * 获取存存储桶列表名称
     */
    public void getBuckets1() {
        CosXmlService cosXmlService = CosServiceFactory.getCosXmlService(this, BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, false);
        cosXmlService.getServiceAsync(new GetServiceRequest(), new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                uiAction(new Runnable() {
                    @Override
                    public void run() {
                        List<ListAllMyBuckets.Bucket> buckets = ((GetServiceResult) result).listAllMyBuckets.buckets;
                        ListAllMyBuckets.Bucket bucket = buckets.get(buckets.size() - 1);
                        //获取列表第一个桶名称
                        bucketName = bucket.name;
                        bucketRegion = bucket.location;
                        baseModeul.setFolderName(null);
                        getBuckets2();
                    }
                });
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                setLoading(false);
                toastMessage("获取存储桶列表失败");
                exception.printStackTrace();
                serviceException.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cosXmlService != null) {
            cosXmlService.release();
            cosxmlTask = null;
            transferManager = null;
        }
    }

    /**
     * 选择本地上传文件回调处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void myonActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_FILE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            String path = FilePathHelper.getAbsPathFromUri(this, data.getData());
            File file = new File(path);
            if (file.length() > feiliength) {
                toastMessage(String.format(getString(R.string.tv3) + "", (feiliength / 1024 / 1024)));
                tv_state.setText(String.format(getString(R.string.tv3) + "", feiliength / 1024 / 1024));
                return;
            }
            if (TextUtils.isEmpty(path)) {
                iv_image.setImageBitmap(null);
            } else {
                //如果所选文件不是图片，则展示文件图标
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (bitmap != null) {
                        iv_image.setImageBitmap(bitmap);
                    } else {
                        //获取视频封面->频预览图
                        Bitmap vedioThumbnail = baseModeul.getVedioThumbnail(file);
                        if (vedioThumbnail != null) {
                            iv_image.setImageBitmap(vedioThumbnail);
                            //获取文件播放时长
                            baseModeul.setVedioTotalTime(baseModeul.getVedioTotalTime(file));
                            //视频资源不能超过XX秒
                            if (baseModeul.getVedioTotalTime() / 1000 > uptime) {
                                String msg = String.format(getString(R.string.tv_time) + "", uptime);
                                toastMessage(msg);
                                tv_state.setText(msg);
                                hideshow.setVisibility(View.GONE);
                                return;
                            }
                        } else {
                            //给上传资源设置默认图片
                            iv_image.setImageResource(R.drawable.file);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            setdeftopenpath(path);
        }
    }

    /**
     * 设置默认配置参数，一般不用管
     */
    private void setdeftopenpath(String path) {
        currentUploadPath = path;
        pb_upload.setProgress(0);
        tv_progress.setText("");
        tv_state.setText("等待上传");
        hideshow.setVisibility(View.GONE);
        baseModeul.setTitle("");
        baseModeul.setIsvupcuimg(false);
        upfile = true;
        btn_left.setVisibility(View.VISIBLE);
    }

    /**
     * 上传视频缩略图片
     */
    private String videoupdate(File file) {
        if (transferManager != null) {
            cosxmlTask = transferManager.upload(bucketName, UP_VIDEO + "/" + FileUtils.DateTime(false) + "/" + file.getName(), file.getPath(), null);
            //设置上传进度回调刷新上载状态
            cosxmlTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(final TransferState state) {

                }
            });
            //设置返回结果回调 刷新上传进度
            cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(final long complete, final long target) {

                }
            });
            //设置任务状态回调, 可以查看任务过程
            cosxmlTask.setCosXmlResultListener(new CosXmlResultListener() {
                @Override
                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                    COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                    cosxmlTask = null;
                    //获取到腾讯COS的地址保存起来
                    imgCurrentUploadPath = cOSXMLUploadTaskResult.accessUrl;
                    //上传成功后删除创建的本地文件
                    file.delete();

                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;

                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                }
            });
        } else {
            toastMessage("上传文件失败");
            finish();
        }
        return imgCurrentUploadPath;
    }

    /**
     * 回调去实名
     */
    private Callback callback = new Callback() {
        @Override
        public void onSuccess() {
            //去实名认证
            Intent intent = new Intent();
            intent.setAction("livebroadcastActivity");
            intent.addCategory("android.intent.category.DEFAULT");
            //意图的数据
            //intent.setData(Uri.parse("http://www.baidu.com"));
            startActivity(intent);
        }

        @Override
        public void onappeal() {
            /**
             * 封号意见反馈
             */
            Intent intent = new Intent();
            intent.setAction("com.paixide.activity.edit.activity_nickname3");
            intent.addCategory("android.intent.category.DEFAULT");
            startActivity(intent);
        }

        @Override
        public void onFall() {

        }
    };

    /**
     * 打开本地资源文件
     */
    public void sACTION_GET_CONTENT() {
        if (cosxmlTask == null) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            switch (baseModeul.getTYPE()) {
                case ACTIVITY_VIDEO:
                    intent.setType("video/*");
                    break;
                case ACTIVITY_IMG:
                    intent.setType("image/*");
                    break;
            }
            startActivityForResult(intent, OPEN_FILE_CODE);
        }
    }

}

