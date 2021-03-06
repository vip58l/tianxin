package com.tianxin.activity.ZYservices;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tianxin.activity.activity_music_play;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Buckets;
import com.tianxin.Module.api.misc;
import com.tianxin.Module.api.videotitle;
import com.tianxin.R;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_item_view_edit;
import com.tianxin.dialog.dialog_load;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.getHandler.PostModule;
import com.tianxin.widget.itembackTopbr;
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
import com.tencent.opensource.model.HttpUtils;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.curriculum;
import com.tencent.qcloud.costransferpractice.CosServiceFactory;
import com.tencent.qcloud.costransferpractice.common.FilePathHelper;
import com.tencent.qcloud.costransferpractice.common.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.tianxin.Util.Imagecompressiontool.dataDir;
import static com.tianxin.Util.Config.getFileName;

/**
 * ????????????
 */
public class activity_course extends BasActivity2 {
    private static final String TAG = "activity_course";
    @BindView(R.id.itemback)
    itembackTopbr itembackTopbr;
    @BindView(R.id.iv_img1)
    ImageView iv_img1;
    @BindView(R.id.iv_img2)
    ImageView iv_img2;
    @BindView(R.id.iv_img3)
    ImageView iv_img3;
    @BindView(R.id.img_del)
    ImageView img_del;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.listener)
    TextView listener;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.tv2)
    EditText tv2;
    @BindView(R.id.howtime)
    TextView howtime;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.layout11)
    RelativeLayout layout11;
    @BindView(R.id.layout12)
    RelativeLayout layout12;
    @BindView(R.id.layout13)
    RelativeLayout layout13;
    @BindView(R.id.video_img)
    ImageView video_img;
    @BindView(R.id.audio_img)
    ImageView audio_img;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.tv_play)
    TextView tv_play;
    dialog_load dialog_load;

    private final int OPEN_IMG_CODE = 10001;
    private final int OPEN_VIDEO_CODE = 10002;
    private final int OPEN_Audio_CODE = 10003;
    public final int PERMISSIONS = 10001;
    private final int s0 = 0;
    private final int s1 = 1;
    private final int s2 = 2;
    private final int s3 = 3;
    private final int s4 = 4;
    private curriculum curriculum;
    private UserInfo userInfo;
    private int TYPE;
    private final int upTtype = 0;
    private final int textcount = 120;
    private final String Log_path = "";
    private File file, newfile;

    private CosXmlService cosXmlService;
    private COSXMLUploadTask cosxmlTask;
    private TransferManager transferManager;
    private String currentUploadPath;
    private String dataaccessUrl;
    private String FileName;
    List<String> list;
    private List<String> hlist;
    private List<Boolean> booleanList;
    private final int videozies = 100 * 1024 * 1024; //????????????????????????100M
    private final int audiozies = 20 * 1024 * 1024; //????????????????????????100M
    private activity_course activityCourse;

    @Override
    protected int getview() {
        return R.layout.activity_course;
    }

    @Override
    public void iniview() {
        activityCourse = this;
        requestPermissions();
        itembackTopbr.settitle(getString(R.string.tv_msg4));
        itembackTopbr.sendright.setVisibility(View.VISIBLE);
        relativeLayout.setVisibility(View.GONE);
        video_img.setVisibility(View.GONE);
        audio_img.setVisibility(View.GONE);
        img_del.setVisibility(View.GONE);
        content.setText(getShareText());
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > textcount) {
                    content.setText(s.toString().substring(0, textcount)); //??????EditText???????????????6?????????
                    content.setSelection(content.length());          //?????????????????????
                    listener.setText(String.format("%s/", s.length() - 1, textcount));
                    Toashow.show(getString(R.string.Toast_msg4));
                    return;
                }
                Log.d(TAG, "onTextChanged start:== " + start + "before= " + before + " count=" + count);
                listener.setText(String.format("%s/%s", start, textcount));

            }

            @Override
            public void afterTextChanged(Editable s) {
                curriculum.setTitle(s.toString());
                curriculum.setMsg(s.toString());
            }
        });
        TYPE = getIntent().getIntExtra(Constants.TYPE, -1);
        userInfo = UserInfo.getInstance();
        curriculum = new curriculum();
        curriculum.setUserid(userInfo.getUserId());
        curriculum.setTencent(1);
        curriculum.setPay(1);
        curriculum.setType(TYPE);
        switch (TYPE) {
            case s0: //??????
                layout12.setVisibility(View.VISIBLE);
                layout13.setVisibility(View.GONE);
                break;
            case s1: //??????
                layout12.setVisibility(View.GONE);
                layout13.setVisibility(View.VISIBLE);
                break;
        }
        Log.d(TAG, "TYPE: " + TYPE);
    }

    @Override
    public void initData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
    PostModule.getModule(HttpUtils.videotitle, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<videotitle> videotypetitle = JsonUitl.stringToList(date, videotitle.class);
                    hlist = new ArrayList<>();
                    booleanList = new ArrayList<>();
                    for (videotitle videotitle : videotypetitle) {
                        hlist.add(videotitle.getTitle());
                        booleanList.add(false);
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

    @OnClick({R.id.sendright, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.iv_img1, R.id.iv_img2, R.id.video_img, R.id.iv_img3, R.id.audio_img, R.id.tv_play, R.id.tv_delete, R.id.img_del})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.sendright:
                postdata();
                break;
            case R.id.layout1:
                showSingleAlertDialog();
                break;
            case R.id.layout2:
                //sProgressDialog();
                break;
            case R.id.layout3:
                showMutilAlertDialog();
                break;
            case R.id.iv_img1:
                OpenImages();
                break;
            case R.id.iv_img2:
            case R.id.video_img:
                if (file != null && TYPE == s0) {
                    activityvideoijkplayer();
                } else {
                    OpenVideo();
                }
                break;
            case R.id.iv_img3:
            case R.id.audio_img:
                if (file != null && TYPE == s1) {
                    gotartActivity();
                } else {
                    OpenAudio();
                }
                break;
            case R.id.tv_play:
                file = null;
                Glide.with(this).load(R.mipmap.rc_image_error3).into(iv_img3);
                video_img.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                audio_img.setVisibility(View.GONE);
                break;
            case R.id.tv_delete:
            case R.id.img_del:
                file = null;
                Glide.with(this).load(R.mipmap.rc_image_error2).into(iv_img2);
                video_img.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
                img_del.setVisibility(View.GONE);
                break;
            case R.id.layout4:
                dialog_item_view_edit.myedit(this, currentUploadPath, new Paymnets() {
                    @Override
                    public void activity(String str) {
                        if (!TextUtils.isEmpty(str)) {
                            tv4.setText(str);
                            currentUploadPath = str;
                            curriculum.setDownload(currentUploadPath);
                        }

                    }
                });
                break;
        }
    }

    /**
     * ?????????????????????
     */
    private void postdata() {
        String sontent = content.getText().toString().trim();
        if (TextUtils.isEmpty(sontent)) {
            Toashow.show(getString(R.string.tv_msg41));
            content.setEnabled(true);
            content.setFocusable(true);
            content.setFocusableInTouchMode(true);
            content.requestFocus();
            content.setSelection(content.length());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return;
        }
        curriculum.setTitle(sontent);
        curriculum.setMsg(sontent);
        curriculum.setMoney(tv2.getText().toString().trim());
        curriculum.setPrice(tv2.getText().toString().trim());

        if (sontent.length() < 10) {
            Toashow.show(getString(R.string.Toast_msg12));
            return;
        }
        double price = 0;
        try {
            price = Double.valueOf(curriculum.getPrice());
        } catch (Exception e) {
        }
        if (price <= 0) {
            Toashow.show(getString(R.string.Toast_msg6));
            tv2.setEnabled(true);
            tv2.setFocusable(true);
            tv2.setFocusableInTouchMode(true);
            tv2.requestFocus();
            tv2.setSelection(tv2.length());
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return;
        }
        if (TextUtils.isEmpty(curriculum.getTag())) {
            Toashow.show(getString(R.string.tv_msg38));
            return;
        }
        if (TextUtils.isEmpty(curriculum.getPic())) {
            Toashow.show(getString(R.string.Toast_msg7));
            return;
        }
        if (TextUtils.isEmpty(curriculum.getVideo())) {
            Toashow.show(getString(R.string.Toast_msg10));
            return;
        }

        dialog_load = new dialog_load(this);
        dialog_load.show();
        if (newfile == null) {
            mydismiss();
            Toashow.show(getString(R.string.Toast_msg7));
            return;
        }
        if (file == null) {
            mydismiss();
            Toashow.show(getString(R.string.Toast_msg10));
            return;
        }
        if (hlist == null || hlist.size() == 0) {
            Toashow.show(getString(R.string.eorrfali3));
            dialog_load.dismiss();
            return;
        }
        switch (TYPE) {
            case s0: //????????????
                if (file.length() > videozies) {
                    Log.d(TAG, String.format("????????????:%sMB", file.length() / 1024 / 1024));
                    Toashow.show(getString(R.string.Toast_msg13));
                    mydismiss();
                    return;
                }
                upload(newfile, false);  //?????????
                upload(file, true);      //????????????
                break;
            case s1: //????????????
                if (file.length() > audiozies) {
                    Toashow.show(getString(R.string.Toast_msg14));
                    mydismiss();
                    return;
                }
                upload(newfile, false);//?????????
                upload(file, true);    //????????????
                break;
        }
    }


    /**
     * ??????????????????
     */
    private void resupdate() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add("token", userInfo.getToken())
                .add("userid", TextUtils.isEmpty(curriculum.getUserid()) ? "" : curriculum.getUserid())
                .add("title", curriculum.getTitle())
                .add("msg", curriculum.getMsg())
                .add("tag", curriculum.getTag())
                .add("pic", TextUtils.isEmpty(curriculum.getPic()) ? "" : curriculum.getPic())
                .add("video", curriculum.getVideo())
                .add("money", curriculum.getMoney())
                .add("price", curriculum.getPrice())
                .add("duration", TextUtils.isEmpty(curriculum.getDuration()) ? "" : curriculum.getDuration())
                .add("type", String.valueOf(curriculum.getType()))
                .add("pay", String.valueOf(curriculum.getPay()))
                .add("tencent", String.valueOf(curriculum.getTencent()))
                .add("download", TextUtils.isEmpty(curriculum.getDownload()) ? "" : curriculum.getDownload())
                .build();
        PostModule.postModule(BuildConfig.HTTP_API + "/curriculumadd", requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getStatus().equals("1") ? getString(R.string.Toast_msg9) : mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        gostartActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toashow.show(getString(R.string.eorrfali3));
                }
                if (dialog_load != null) {
                    dialog_load.dismiss();
                }
            }

            @Override
            public void fall(int code) {
                Toashow.show(getString(R.string.eorrfali3));
                if (dialog_load != null) {
                    dialog_load.dismiss();
                }
            }
        });

    }

    /**
     * ????????????????????????
     */
    private void gostartActivity() {
        startActivity(new Intent(activityCourse, activity_mycurriculum.class));
        finish();
    }

    @Override
    public void OnEorr() {

    }

    /**
     * ??????????????????
     */
    public void OpenImages() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*"); //???????????? ????????????
        startActivityForResult(intent, OPEN_IMG_CODE);
    }

    /**
     * ??????????????????
     */
    public void OpenVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*"); //???????????? ????????????
        startActivityForResult(intent, OPEN_VIDEO_CODE);
    }

    /**
     * ??????????????????
     */
    public void OpenAudio() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*"); //???????????? ????????????
        startActivityForResult(intent, OPEN_Audio_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPEN_IMG_CODE:
                resultCodeImages(resultCode, data);
                break;
            case OPEN_VIDEO_CODE:
                resultCodevideo(resultCode, data);
                break;
            case OPEN_Audio_CODE:
                resultCodeAudio(resultCode, data);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS);
            }
        }
    }


    /**
     * ??????????????????
     *
     * @param resultCode
     * @param data
     */
    private void resultCodeImages(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String path = FilePathHelper.getAbsPathFromUri(DemoApplication.instance(), data.getData());
            if (TextUtils.isEmpty(path)) {
                return;
            }
            File file = new File(path);

            //????????????????????????URI??????????????? ????????????
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                iv_img1.setImageBitmap(bitmap);
            }
            //??????????????????????????????
            newfile = dataDir();
            //??????????????????
            displayToGallery(this, newfile);

            //??????????????????????????????????????????????????????????????????????????? ????????????500KB
            if (file.length() > 512000) {
                Imagecompressiontool.sizeCompress(bitmap, newfile, 2);
                while (true) {
                    if (newfile.length() <= 512000) {
                        Log.d(TAG, "???????????? ???" + newfile.length());
                        break;
                    }
                    Bitmap bitmap2 = BitmapFactory.decodeFile(newfile.getPath());
                    Imagecompressiontool.sizeCompress(bitmap2, newfile, 2);
                    Log.d(TAG, "???????????????" + newfile.length());
                }

            } else {
                /**
                 * ???????????????????????????
                 * @param bmp
                 * @param file
                 * @return
                 */
                Imagecompressiontool.qualityCompress(bitmap, newfile);
                Log.d(TAG, "myresultCode: ?????????");
            }

            //????????????
            Log.d(TAG, "??????????????????" + file.getPath());
            Log.d(TAG, "??????????????????" + file.length());
            Log.d(TAG, "???????????????" + getFileName(file.getPath()));
            Log.d(TAG, "*********************?????????************************");
            Log.d(TAG, "??????????????? : " + newfile.getPath());
            Log.d(TAG, "??????????????? : " + newfile.length());
            Log.d(TAG, "???????????? ???" + getFileName(newfile.getPath()));

            //????????????????????????
            curriculum.setPic(newfile.getPath());

        }

    }

    /**
     * ??????????????????
     *
     * @param resultCode
     * @param data
     */
    private void resultCodevideo(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String path = FilePathHelper.getAbsPathFromUri(DemoApplication.instance(), data.getData());
            file = new File(path);
            if (!file.exists()) {
                return;
            }

            //????????????????????????URI??????????????? ????????????
            //Bitmap bitmap = BitmapFactory.decodeFile(path);
            //???????????????????????????
            Bitmap bitmap = getVedioThumbnail(file);
            //???????????????????????????
            Long totalTime = getVedioTotalTime(file);
            if (bitmap != null) {
                Glide.with(this).load(bitmap).into(iv_img2);
                Glide.with(this).load(bitmap).into(iv_img1);
                howtime.setText(Config.generateTime(totalTime));
                curriculum.setDuration(Config.generateTime(totalTime));
                video_img.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);
                img_del.setVisibility(View.VISIBLE);
                tv_play.setVisibility(View.GONE);
            }
            Log.d(TAG, String.format("????????????:%sMB", file.length() / 1024 / 1024));
            if (file.length() > videozies) {
                Toashow.show(getString(R.string.Toast_msg13));
            }

            //??????????????????
            newfile = dataDir();
            curriculum.setPic(newfile.getPath());
            curriculum.setVideo(file.getPath());
            //??????????????????
            displayToGallery(this, newfile);
            Imagecompressiontool.sizeCompress(bitmap, newfile, 2);
            while (true) {
                if (newfile.length() <= 512000) {
                    Log.d(TAG, "???????????? ???" + newfile.length());
                    break;
                }
                Imagecompressiontool.sizeCompress(BitmapFactory.decodeFile(newfile.getPath()), newfile, 2);
                Log.d(TAG, "???????????????" + newfile.length());
            }


            //??????????????????
            StringBuilder sb = new StringBuilder();
            sb.append("???????????????" + file.getPath() + "\n");
            sb.append("???????????????" + file.length() + "\n");
            sb.append("???????????????" + file.getName() + "\n");
            sb.append("???????????????" + totalTime + "\n");
            sb.append("???????????????" + totalTime / 1000 + "\n");
            sb.append("???????????????" + Config.generateTime(totalTime) + "\n");

            sb.append("*********************?????????************************\n");
            sb.append("????????????: " + newfile.getPath() + "\n");
            sb.append("????????????: " + newfile.length() + "\n");
            sb.append("???????????????" + newfile.getName() + "\n");
            Log.d(TAG, "??????????????????: \n" + sb.toString());
        }

    }


    /**
     * ??????????????????
     *
     * @param resultCode
     * @param data
     */
    private void resultCodeAudio(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String path = FilePathHelper.getAbsPathFromUri(DemoApplication.instance(), data.getData());
            file = new File(path);
            if (!file.exists()) {
                return;
            }
            Long totalTime = getVedioTotalTime(file);
            iv_img3.setImageResource(R.mipmap.ms_play_icon_gif3);
            howtime.setText(Config.generateTime(totalTime) + " " + file.getName());
            curriculum.setDuration(Config.generateTime(totalTime));
            curriculum.setVideo(file.getPath());

            relativeLayout.setVisibility(View.VISIBLE);
            audio_img.setVisibility(View.VISIBLE);
            tv_play.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.GONE);
            Log.d(TAG, String.format("????????????:%sMB", file.length() / 1024 / 1024));
            if (file.length() > audiozies) {
                Toashow.show(getString(R.string.Toast_msg14));
            }

            //??????????????????
            StringBuilder sb = new StringBuilder();
            sb.append("???????????????" + file.getPath() + "\n");
            sb.append("???????????????" + file.length() + "\n");
            sb.append("???????????????" + file.getName() + "\n");
            sb.append("???????????????" + totalTime + "\n");
            sb.append("???????????????" + totalTime / 1000 + "\n");
            sb.append("???????????????" + Config.generateTime(totalTime) + "\n");
            Log.d(TAG, "??????????????????: \n" + sb.toString());
        }

    }


    /**
     * ????????????????????? ???Android????????????????????????????????????MediaMetadataRetriever??????
     *
     * @param file
     * @return
     */
    public static Bitmap getVedioThumbnail(File file) {
        if (!file.exists()) {
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getAbsolutePath());
        Bitmap bitmap = retriever.getFrameAtTime();
        //Bitmap bitmap = retriever.getFrameAtTime(1000);//???????????????,???????????????????????????????????????
        return bitmap;
    }

    /**
     * ???????????????????????????
     *
     * @param file
     * @return
     */
    public static Long getVedioTotalTime(File file) {
        if (!file.exists()) {
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getAbsolutePath());
        String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        Long time = Long.valueOf(timeString);
        return time;

    }


    /**
     * ?????????????????????????????????
     *
     * @param file
     */
    private static void FileVideo(File file) {
        //??????????????????
        Bitmap bitmap = getVedioThumbnail(file);
        //???????????????????????????
        Long totalTime = getVedioTotalTime(file);

        //??????????????????
        String time = Config.generateTime(totalTime);
    }

    /**
     * Glide???????????????????????????
     */
    public void SaveBitmap(String path) throws ExecutionException, InterruptedException {
        //??????Glide????????????
        File file = Glide.with(this).asFile().load(path).submit().get();
        //???????????????
        displayToGallery(this, file);
    }

    /**
     * ??????????????????????????????
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
            //??????????????????????????????
            //ContentResolver contentResolver = context.getContentResolver();
            //MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);

            //??????????????????
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * ??????????????????
     */
    public void upload(File file, boolean IsUpFile) {

        Buckets sbuckets = Buckets.getSbuckets();
        cosXmlService = DemoApplication.cosXmlService;
        //???????????????
        if (cosXmlService == null) {
            cosXmlService = CosServiceFactory.getCosXmlService(this, sbuckets.getLocation(), com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_ID, com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_KEY, true);
        }
        transferManager = new TransferManager(this.cosXmlService, new TransferConfig.Builder().build());
        //??????????????????
        cosxmlTask = transferManager.upload(sbuckets.getName(), file.getName(), file.getPath(), null);
        //??????????????????????????????????????????
        cosxmlTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(final TransferState state) {
                Log.d(TAG, "onStateChanged: " + state.toString());
            }
        });
        //???????????????????????? ??????????????????
        cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(final long complete, final long total) {
                Log.d(TAG, "onProgress: " + complete + " " + total);
                long progress = 100 * complete / total;
                Log.d(TAG, "onProgress: " + Utils.readableStorageSize(progress) + "/" + Utils.readableStorageSize(total));
            }
        });
        //????????????????????????, ????????????????????????
        cosxmlTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                cosxmlTask = null;
                //???????????????????????????
                final String accessUrl = cOSXMLUploadTaskResult.accessUrl;
                Log.d(TAG, "accessUrl: " + accessUrl);
                if (accessUrl.toLowerCase().endsWith(".png") || accessUrl.toLowerCase().endsWith(".jpg") || accessUrl.toLowerCase().endsWith(".gif")) {
                    //????????????
                    curriculum.setPic(accessUrl);
                } else {
                    //?????????????????????
                    curriculum.setVideo(accessUrl);

                }

                if (IsUpFile) {
                    iv_img1.post(new Runnable() {
                        @Override
                        public void run() {
                            resupdate();
                        }
                    });
                }

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
     * ??????????????????
     */
    public void staralertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alertDialog_title);
        alertDialog.setMessage(R.string.dialog_setMessage);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setNegativeButton(R.string.AlertDialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //????????????
        AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e(TAG, "??????????????????");
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG, "??????????????????");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });
        dialog.show();


    }


    /**
     * ???????????????
     */
    public void showSingleAlertlist() {
        final String[] items = {"??????1", "??????2", "??????3", "??????4"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("???????????????");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertBuilder.show();
    }

    /**
     * ????????????????????????
     */
    public void showSingleAlertDialog() {
        final String[] items = {"??????", "??????"};
        String[] textconter = {""};
        int[] is = new int[1];
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("??????????????????");
        alertBuilder.setCancelable(false);
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                textconter[0] = items[i];
                is[0] = i;
            }
        });
        alertBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                curriculum.setPay(is[0] == 0 ? 0 : 1);
                tv1.setText(textconter[0]);
            }
        });
        alertBuilder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Log.d(TAG, "onClick: ??????" + i);

            }
        });
        alertBuilder.show();
    }

    /**
     * ???????????????
     */
    public void showMutilAlertDialog() {

        if (hlist == null || hlist.size() == 0) {
            initData();//????????????
            Toashow.show(getString(R.string.eorrfali3));
            return;
        }
        String[] items = hlist.toArray(new String[hlist.size()]);
        boolean[] checkedItems = new boolean[booleanList.size()];
        for (int i = 0; i < booleanList.size(); i++) {
            checkedItems[i] = booleanList.get(i);
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(R.string.tv_msg39);
        /**
         *???????????????:???????????????????????????????????????????????????
         * ??????????????????????????????????????????????????????
         * ????????????????????????????????????
         */
        alertBuilder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                checkedItems[i] = isChecked;
            }
        });
        alertBuilder.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < checkedItems.length; i++) {
                    //??????????????????
                    if (checkedItems[i]) {
                        sb.append(items[i] + ",");
                    }
                    if (!TextUtils.isEmpty(sb)) {
                        tv3.setText(sb.toString());
                        curriculum.setTag(sb.toString());

                    } else {
                        tv3.setText("????????????");
                        curriculum.setTag("");
                    }
                    //2.????????????????????????
                    dialogInterface.dismiss();
                }
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertBuilder.show();

    }

    /**
     * ???????????????
     */
    public void sProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("?????????????????????...");
        //??????????????????????????????
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        //?????????????????????
        new Thread() {
            @Override
            public void run() {
                //???????????????????????????
                dialog.setMax(100);
                //??????????????????
                for (int i = 0; i <= 100; i++) {
                    dialog.setProgress(i);
                    //???????????????
                    SystemClock.sleep(50);
                }
                //???????????????
                dialog.dismiss();
            }
        }.start();
        dialog.show();
    }

    /**
     * ??????????????????
     */
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS);
        }
    }


    /**
     * ?????????????????????
     */
    private void activityvideoijkplayer() {
        Intent intent = new Intent(this, videoijkplayer0.class);
        intent.putExtra(Constants.TITLE, content.getText().toString().trim());
        intent.putExtra(Constants.PATHVIDEO, file.getPath());
        intent.putExtra(Constants.PATHIMG, "");
        intent.putExtra(Constants.POSITION, 0);
        startActivity(intent);
    }

    private void mydismiss() {
        if (dialog_load != null) {
            dialog_load.dismiss();
        }
    }


    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    public String getShareText() {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            String shareText = getShareText();
            if (TextUtils.isEmpty(content.getText())) {
                content.setText(shareText);
            }
        }
    }

    public void gotartActivity() {
        String tag = content.getText().toString().trim();
        misc misc = new misc();
        misc.setId("1");
        misc.setUrl(file.getPath());
        misc.setTitle(file.getName());
        if (!TextUtils.isEmpty(tag)) {
            misc.setTag(tag);
        } else {
            misc.setTag(file.getName());
        }
        if (newfile != null) {
            misc.setPicture(newfile.getPath());
        }
        Intent intent = new Intent(this, activity_music_play.class);
        intent.putExtra(Constants.PATHVIDEO, misc);
        startActivity(intent);
    }
}