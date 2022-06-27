package com.tianxin.activity.LatestNews.upfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tianxin.Module.api.Buckets;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.Util.Config;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Listeningstate;
import com.steven.selectimage.model.Image;
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
import com.tencent.qcloud.costransferpractice.CosServiceFactory;
import com.tencent.qcloud.costransferpractice.common.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 公共上传类可以在其他任意类用引用
 **/
public class Fileupdate {
    private static String TAG = Fileupdate.class.getSimpleName();
    public List<String> mlist = new ArrayList<>();
    private CosXmlService cosXmlService;
    private COSXMLUploadTask cosxmlTask;
    private TransferManager transferManager;
    private Context mcontext;
    private Listeningstate listeningstate;
    private final static int IMG = 1;
    private final static int VIDEO = 2;
    private static long zise = 1024 * 800;
    private static Fileupdate fileupdate;

    public static Fileupdate getFileupdate(Context context) {
        if (fileupdate == null) {
            fileupdate = new Fileupdate(context);
        }
        return fileupdate;
    }

    public Fileupdate(Context context) {
        mcontext = context;
    }

    /**
     * 图片处理压缩
     **/
    public void processingfile2(String path, String catalogue, Listeningstate listeningstate) {
        this.listeningstate = listeningstate;
        File file = new File(path);
        File newfile = null;
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // 转成图片Bitmap
        if (file.length() > zise) {
            newfile = Imagecompressiontool.dataDir();
            bitmap = Imagecompressiontool.sizeCompress(bitmap, newfile, 5); //尺寸压缩 大于500KB
            while (newfile.length() > zise) {
                bitmap = Imagecompressiontool.sizeCompress(bitmap, newfile, 2);
            }

        }

        File updatefile = file.length() > zise ? newfile : file;
        //执行上传生成.jpg文件操作
        upload(updatefile, catalogue, ".jpg");
    }

    /**
     * 图片处理压缩
     **/
    public void processingfile(String path, String catalogue, Listeningstate listeningstate) {
        this.listeningstate = listeningstate;
        Luban.with(mcontext)
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
                        upload(file, catalogue, ".jpg");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                    }
                }).launch();
    }

    /**
     * 视频封面
     **/
    public void videocoverfile(Bitmap bitmap, String catalogue, Listeningstate listeningstate) {
        this.listeningstate = listeningstate;
        File file = Imagecompressiontool.dataDir();
        Imagecompressiontool.oNqualityCompress(bitmap, file);
        //执行上传生成.jpg文件操作
        upload(file, catalogue, IMG, ".jpg");
    }

    /**
     * 视频文件
     **/
    public void videofile(File file, String catalogue, Listeningstate listeningstate) {
        this.listeningstate = listeningstate;
        upload(file, catalogue, VIDEO, ".mp4");
    }

    /**
     * 执行上传文件到腾讯云端云
     */
    private void upload(File file, String catalogue, String ext) {
        try {
            String cosPath = String.format("%s/%s/%s", catalogue, Config.DateTime(false), Config.getRandomFileName() + ext);   //对象在存储桶中的位置标识符，即称对象键
            Buckets sbuckets = Buckets.getSbuckets();
            cosXmlService = DemoApplication.cosXmlService;
            //初始化数据
            if (cosXmlService == null) {
                cosXmlService = CosServiceFactory.getCosXmlService(mcontext, sbuckets.getLocation(), com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_ID, com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_KEY, true);
            }
            transferManager = new TransferManager(cosXmlService, new TransferConfig.Builder().build());
            //开始上传文件
            cosxmlTask = transferManager.upload(sbuckets.getName(), cosPath, file.getPath(), null);
            //设置上传进度回调刷新上载状态
            cosxmlTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(TransferState state) {
                    listeningstate.onStateChanged(state);
                }
            });
            //设置返回结果回调 刷新上传进度
            cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(final long complete, final long total) {
                    long progress = 100 * complete / total;
                    String size = Utils.readableStorageSize(progress) + "/" + Utils.readableStorageSize(total);
                    listeningstate.onProgress(complete, total, progress, size);
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
                    mlist.add(accessUrl);
                    listeningstate.onSuccess(mlist, accessUrl);
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                    listeningstate.onFail();
                }
            });

        } catch (Exception e) {
            listeningstate.oncomplete();
            e.printStackTrace();
        }
    }

    /**
     * 执行上传文件到腾讯云端云
     */
    private void upload(File file, String catalogue, int type, String ext) {
        try {
            String cosPath = String.format("%s/%s/%s", catalogue, Config.DateTime(false), Config.getRandomFileName() + ext);   //对象在存储桶中的位置标识符，即称对象键
            Buckets sbuckets = Buckets.getSbuckets();
            cosXmlService = DemoApplication.cosXmlService;
            //初始化数据
            if (cosXmlService == null) {
                cosXmlService = CosServiceFactory.getCosXmlService(mcontext, sbuckets.getLocation(), com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_ID, com.tencent.qcloud.costransferpractice.BuildConfig.COS_SECRET_KEY, true);
            }
            transferManager = new TransferManager(cosXmlService, new TransferConfig.Builder().build());
            //开始上传文件
            cosxmlTask = transferManager.upload(sbuckets.getName(), cosPath, file.getPath(), null);
            //设置上传进度回调刷新上载状态
            cosxmlTask.setTransferStateListener(new TransferStateListener() {
                @Override
                public void onStateChanged(TransferState state) {
                    listeningstate.onStateChanged(state);
                }
            });
            //设置返回结果回调 刷新上传进度
            cosxmlTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                @Override
                public void onProgress(final long complete, final long total) {
                    long progress = 100 * complete / total;
                    String size = Utils.readableStorageSize(progress) + "/" + Utils.readableStorageSize(total);
                    listeningstate.onProgress(complete, total, progress, size);
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
                    mlist.add(accessUrl);
                    switch (type) {
                        case IMG:
                            break;
                        case VIDEO:
                            listeningstate.onSuccess(mlist, accessUrl);
                            break;
                    }
                }

                @Override
                public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                    if (cosxmlTask.getTaskState() != TransferState.PAUSED) {
                        cosxmlTask = null;
                    }
                    exception.printStackTrace();
                    serviceException.printStackTrace();
                    listeningstate.onFail();
                }
            });

        } catch (Exception e) {
            listeningstate.oncomplete();
            e.printStackTrace();
        }
    }

    /**
     * 拼接图片内容
     *
     * @param mlist
     * @return
     */
    public String getStringBuffer(List<String> mlist) {
        if (mlist.size() == 0 || mlist == null) {
            return "";
        }
        StringBuffer sverurl = new StringBuffer();
        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.size() == 1) {
                sverurl.append(mlist.get(i));
                break;
            }
            sverurl.append(i < (mlist.size() - 1) ? mlist.get(i) + "," : mlist.get(i));
        }
        return sverurl.toString();
    }

    /**
     * 判断是否已加载添加图片
     *
     * @return
     */
    public boolean isgetboolean(List<Image> mSelectImages) {
        for (Image mSelectImage : mSelectImages) {
            String path = mSelectImage.getPath();
            if (TextUtils.isEmpty(path)) {
                return true;
            }
        }
        return false;
    }


}
