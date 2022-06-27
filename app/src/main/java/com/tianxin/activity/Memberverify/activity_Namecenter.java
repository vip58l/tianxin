package com.tianxin.activity.Memberverify;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.getHandler.Webrowse;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Buckets;
import com.tianxin.activity.ZYservices.activity_photo_album;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Exit;
import com.tianxin.getHandler.PostModule;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.Dialog_Loading;
import com.tencent.opensource.model.Renzheng;
import com.tencent.opensource.model.attestation;
import com.tencent.qcloud.costransferpractice.CosServiceFactory;
import com.tencent.qcloud.costransferpractice.common.FilePathHelper;
import com.tencent.qcloud.costransferpractice.common.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.tianxin.Util.Imagecompressiontool.dataDir;
import static com.tianxin.Util.Config.getFileName;

/**
 * 实名认证上传身份证资料
 */
public class activity_Namecenter extends BasActivity2 {
    private static final String TAG = "namecenter";
    @BindView(R.id.itemback)
    itembackTopbr itembackTopbr;
    @BindView(R.id.lin10)
    LinearLayout lin10;
    @BindView(R.id.ename)
    EditText ename;
    @BindView(R.id.eidnumber)
    EditText eidnumber;
    @BindView(R.id.senbnt)
    TextView senbnt;
    @BindView(R.id.iv_img1)
    ImageView iv_img1;
    @BindView(R.id.iv_img2)
    ImageView iv_img2;
    @BindView(R.id.iv_img3)
    ImageView iv_img3;
    @BindView(R.id.delete1)
    ImageView delete1;
    @BindView(R.id.delete2)
    ImageView delete2;
    @BindView(R.id.sex1)
    TextView sex1;
    Dialog_Loading dialog_loading;
    private Renzheng renzheng;
    int DRTIME = 1000;
    private final static int OPEN_VIDEO_CODE1 = 101;
    private final static int OPEN_VIDEO_CODE2 = 102;
    private final static int OPEN_VIDEO_CODE3 = 103;
    public final static int PERMISSIONS = 10001;
    public final static int s0 = 0;
    public final static int s1 = 1;
    public final static int s2 = 2;
    public final static int s3 = 3;
    public final static int s4 = 4;
    private Intent intent;
    private attestation attestation;
    private CosXmlService cosXmlService;
    private COSXMLUploadTask cosxmlTask;
    private TransferManager transferManager;

    public static void starsetAction(Context context) {
        Activity activity = (Activity) context;
        activity.startActivityForResult(new Intent(context, activity_Namecenter.class), Config.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.activity_namecenter;
    }

    @Override
    public void iniview() {
        requestPermissions();
        itembackTopbr.settitle(getString(R.string.name_msg_center));
        intent = new Intent(context, activity_photo_album.class);
        attestation = new attestation();
        attestation.setUserid(userInfo.getUserId());
        delete1.setOnClickListener(v -> {
            delete1.setVisibility(View.GONE);
            iv_img1.setImageResource(R.mipmap.ly_zrrz_sfzrxm_img);
            attestation.setCard1("");
        });
        delete2.setOnClickListener(v -> {
            delete2.setVisibility(View.GONE);
            iv_img2.setImageResource(R.mipmap.ly_zrrz_sfzghm_img);
            attestation.setCard2("");
        });
        switch (Integer.parseInt(userInfo.getSex())) {
            case 1:
                sex1.setText(R.string.tv_namecenter3);
                break;
            case 2:
                sex1.setText(R.string.tv_namecenter2);
                break;
        }
    }

    @Override
    public void initData() {
        datamodule.getusername(paymnets);
    }

    @OnClick({R.id.iv_img1, R.id.iv_img2, R.id.iv_img3, R.id.senbnt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_img1:
                if (TextUtils.isEmpty(attestation.getCard1())) {
                    OpenVideo(OPEN_VIDEO_CODE1);
                } else {
                    String s1 = attestation.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(attestation.card1) : attestation.getCard1();
                    intent.putExtra(Constants.PATHVIDEO, s1);
                    startActivity(intent);
                }
                break;
            case R.id.iv_img2:
                if (TextUtils.isEmpty(attestation.getCard2())) {
                    OpenVideo(OPEN_VIDEO_CODE2);
                } else {
                    String s2 = attestation.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(attestation.card2) : attestation.getCard2();
                    intent.putExtra(Constants.PATHVIDEO, s2);
                    startActivity(intent);
                }
                break;
            case R.id.iv_img3:
                if (TextUtils.isEmpty(attestation.getCard3())) {
                    OpenVideo(OPEN_VIDEO_CODE3);
                } else {
                    String s = attestation.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(attestation.card3) : attestation.getCard3();
                    intent.putExtra(Constants.PATHVIDEO, s);
                    startActivity(intent);
                }
                break;
            case R.id.senbnt:
                PostsetName();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }


    /**
     * 打开图片文件
     */
    public void OpenVideo(int openResult) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*"); //全部类型 意图类型
        startActivityForResult(intent, openResult);
    }


    /**
     * 提交数据到服务端
     */
    private void PostsetName() {
        String tvname = ename.getText().toString().trim(); //姓名
        String tvidnumber = eidnumber.getText().toString().trim(); //身份证号
        if (TextUtils.isEmpty(tvname)) {
            Toashow.show(getString(R.string.show_tv1));
            return;
        }
        if (TextUtils.isEmpty(tvidnumber)) {
            Toashow.show(getString(R.string.Toast_msg15));
            return;
        }
        if (tvidnumber.length() < 15) {
            Toashow.show(getString(R.string.show_tv2));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        if (TextUtils.isEmpty(attestation.getCard1())) {
            Toashow.show(getString(R.string.tv_msg15));
            return;
        }
        if (TextUtils.isEmpty(attestation.getCard2())) {
            Toashow.show(getString(R.string.tv_msg16));
            return;
        }

      /*  if (TextUtils.isEmpty(attestation.getCard3())) {
            Toashow.show(getString(R.string.tv_msg17));
            return;
        }*/
        Dialog_Exit.dialogshow(context, new Paymnets() {
            @Override
            public void activity() {

                //确认需要上传图片
                postmethow(tvname, tvidnumber);
            }

            @Override
            public void payens() {
                Log.d(TAG, "payens: ");
            }
        });
    }

    /**
     * 提交上传确认信息
     *
     * @param name
     * @param username
     */
    private void postmethow(String name, String username) {
        dialogshow(getString(R.string.dialog_load));
        RequestBody requestBody = new FormBody.Builder()
                .add(Constants.USERID, userInfo.getUserId())
                .add("username", userInfo.getUsername())
                .add("tvname", name)
                .add("tvidnumber", username)
                .add(Constants.Token, userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.editusername, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    //执行图片上传
                    if (mesresult.getStatus().equals("1")) {
                        //upload(new File(attestation.getCard1()), false, s1);
                        //upload(new File(attestation.getCard1()), false, s1);
                        //upload(new File(attestation.getCard3()), true, s3);
                        UpdateLoadFile(attestation.getCard1(), false, s1);
                        UpdateLoadFile(attestation.getCard2(), true, s2);

                    } else {
                        Toashow.show(mesresult.getMsg());
                        dialogLoadings();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialogLoadings();
                }

            }

            @Override
            public void fall(int code) {
                dialog_loading.dismiss();
                Toashow.show(getString(R.string.eorrfali3));
            }
        });
    }

    /**
     * 提交数据到服务端
     */
    private void Postattestationadd() {
        if (!Config.isNetworkAvailable()) {
            dialogLoadings();
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("username", userInfo.getUsername())
                .add("tencent", "1")
                .add("token", userInfo.getToken())
                .add("card1", TextUtils.isEmpty(attestation.getCard1()) ? "" : attestation.getCard1())
                .add("card2", TextUtils.isEmpty(attestation.getCard2()) ? "" : attestation.getCard2())
                .add("card3", TextUtils.isEmpty(attestation.getCard3()) ? "" : attestation.getCard3())
                .build();
        PostModule.postModule(Webrowse.attestationadd, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = gson.fromJson(date, Mesresult.class);
                    dissmshow(mesresult.getMsg());
                } catch (Exception e) {
                    e.printStackTrace();
                    dialogLoadings();
                }

            }

            @Override
            public void fall(int code) {
                dialog_loading.dismiss();
            }
        });
    }

    /**
     * 关闭弹窗
     *
     * @param msg
     */
    private void dissmshow(String msg) {
        itembackTopbr.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toashow.show(msg);
                dialogLoadings();
                //上传成功重新刷---->新数据
                datamodule.getusername(paymnets);
            }
        }, DRTIME);
    }


    /**
     * 设置更新UI
     *
     * @param renzheng
     */
    private void settextview(Renzheng renzheng) {
        userInfo.setState(renzheng.getMember());
        if (!TextUtils.isEmpty(renzheng.getMsg())) {
            ename.setText(renzheng.getMsg());
        }
        if (!TextUtils.isEmpty(renzheng.getOk())) {
            eidnumber.setText(renzheng.getOk());
        }
        if (renzheng.getAttestation() != null) {
            attestation = renzheng.getAttestation();

        }
        if (!TextUtils.isEmpty(attestation.getCard1())) {
            Glideload.loadImage(iv_img1, attestation.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(attestation.getCard1()) : attestation.getCard1(), 4);
            delete1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(attestation.getCard2())) {
            Glideload.loadImage(iv_img2, attestation.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(attestation.getCard2()) : attestation.getCard2(), 4);
            delete2.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(attestation.getCard3())) {
            Glideload.loadImage(iv_img3, attestation.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(attestation.getCard3()) : attestation.getCard3(), 4);
        }

        //禁止控件可用
        //senbnt.setBackground(getResources().getDrawable(R.drawable.bg_jianbian_yuanjiao4));
        //senbnt.setTextColor(getResources().getColor(R.color.home3));
        //senbnt.setBackground(getResources().getDrawable(R.drawable.acitvity08));

        switch (userInfo.getState()) {
            case s0:
                senbnt.setText(R.string.tv_msg7);
                break;
            case s1:
                senbnt.setText(R.string.tv_msg8);
                mysetEnabled();
                break;
            case s2:
                senbnt.setText(R.string.tv_msg9);
                mysetEnabled();
                break;
            case s3:
                senbnt.setText(R.string.tv_msg10);
                mysetEnabled();
                break;
            default:
                senbnt.setText(getString(R.string.tv_msg14));
                mysetEnabled();
                break;
        }

    }

    /**
     * 申请读写权限
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resultCodeImages(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS);
            }
        }
    }

    /**
     * 返回图片处理
     *
     * @param resultCode
     * @param data
     */
    private void resultCodeImages(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String path = FilePathHelper.getAbsPathFromUri(DemoApplication.instance(), data.getData());
            if (TextUtils.isEmpty(path)) {
                return;
            }

            //直接用选择的文件URI去展示图片 解析图片
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                switch (requestCode) {
                    case OPEN_VIDEO_CODE1:
                        Glideload.loadImage(iv_img1, bitmap, 4);
                        break;
                    case OPEN_VIDEO_CODE2:
                        Glideload.loadImage(iv_img2, bitmap, 4);
                        break;
                    case OPEN_VIDEO_CODE3:
                        Glideload.loadImage(iv_img3, bitmap, 4);
                        break;
                }

            }

            //保存需要上传的本地文件地址
            switch (requestCode) {
                case OPEN_VIDEO_CODE1:
                    attestation.setCard1(path);
                    delete1.setVisibility(View.VISIBLE);
                    break;
                case OPEN_VIDEO_CODE2:
                    attestation.setCard2(path);
                    delete2.setVisibility(View.VISIBLE);
                    break;
                case OPEN_VIDEO_CODE3:
                    attestation.setCard3(path);
                    break;
            }


        }
    }

    /**
     * 执行上传文件
     */
    public void upload(File file, boolean IsUpFile, int TYPE) {
        Buckets sbuckets = Buckets.getSbuckets();
        cosXmlService = DemoApplication.cosXmlService;
        //初始化数据
        if (cosXmlService == null) {
            cosXmlService = CosServiceFactory.getCosXmlService(this, sbuckets.getLocation(), com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_ID, com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_KEY, true);
        }
        //腾讯云存储位置标识符
        String keypath = String.format("attestation/%s/%s", Config.DateTime(false), file.getName());
        transferManager = new TransferManager(cosXmlService, new TransferConfig.Builder().build());
        //开始上传文件
        cosxmlTask = transferManager.upload(sbuckets.getName(), keypath, file.getPath(), null);
        //设置上传进度回调刷新上载状态
        cosxmlTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(final TransferState state) {
                Log.d(TAG, "onStateChanged: " + state.toString());
            }
        });
        //设置返回结果回调 刷新上传进度
        cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(final long complete, final long total) {
                Log.d(TAG, "onProgress: " + complete + " " + total);
                long progress = 100 * complete / total;
                Log.d(TAG, "onProgress: " + Utils.readableStorageSize(progress) + "/" + Utils.readableStorageSize(total));
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
                switch (TYPE) {
                    case s1:
                        attestation.setCard1(accessUrl);
                        break;
                    case s2:
                        attestation.setCard2(accessUrl);
                        break;
                    case s3:
                        attestation.setCard3(accessUrl);
                        break;
                }
                if (IsUpFile) {
                    iv_img1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Postattestationadd();
                        }
                    }, 100);
                }
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                    cosxmlTask = null;
                }
                exception.printStackTrace();
                serviceException.printStackTrace();
                iv_img1.post(new Runnable() {
                    @Override
                    public void run() {
                        dissmshow("上传失败");
                    }
                });
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

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {


        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object object) {
            renzheng = (Renzheng) object;
            userInfo.settRole(renzheng.gettRole());
            userInfo.setVip(renzheng.getVip());
            userInfo.setLevel(renzheng.getLevel());
            userInfo.setSex(String.valueOf(renzheng.getSex()));
            userInfo.setName(renzheng.getAlias());
            userInfo.setAvatar(renzheng.getPicture());
            settextview(renzheng);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

    };


    public void mysetEnabled() {
        ename.setEnabled(false);
        eidnumber.setEnabled(false);
        senbnt.setEnabled(false);
        iv_img1.setEnabled(false);
        iv_img2.setEnabled(false);
    }

    /**
     * 图片压缩后再上传
     *
     * @param path
     * @param isupfile
     * @param type
     */
    public void UpdateLoadFile(String path, boolean isupfile, int type) {
        Luban.with(context)
                .load(path)
                .ignoreBy(100)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        upload(file, isupfile, type); //file, isupfile, type //file文件 isupfile=true 最后一张  类型type
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }
}