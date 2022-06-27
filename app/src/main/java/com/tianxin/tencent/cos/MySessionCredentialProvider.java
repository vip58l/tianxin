/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/21 0021
 */


package com.tianxin.tencent.cos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.DeleteMultiObjectRequest;
import com.tencent.cos.xml.model.object.DeleteMultiObjectResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.DeleteObjectResult;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.tag.pic.PicOperationRule;
import com.tencent.cos.xml.model.tag.pic.PicOperations;
import com.tencent.cos.xml.transfer.COSXMLDownloadTask;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.core.auth.BasicLifecycleCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.QCloudLifecycleCredentials;
import com.tencent.qcloud.core.auth.SessionQCloudCredentials;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.qcloud.core.common.QCloudClientException;
import com.tencent.qcloud.costransferpractice.BuildConfig;
import com.tencent.qcloud.costransferpractice.CosServiceFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 实现一个 BasicLifecycleCredentialProvider的子类，实现请求临时密钥并返回结果的过程。
 */
public class MySessionCredentialProvider extends BasicLifecycleCredentialProvider {
    private static String TAG = MySessionCredentialProvider.class.getSimpleName();
    /**
     * 实现获取临时密钥
     */
    private static QCloudCredentialProvider myCredentialProvider;
    private static Paymnets paymnets;

    public static void setPaymnets(Paymnets paymnets) {
        MySessionCredentialProvider.paymnets = paymnets;
    }

    @Override
    protected QCloudLifecycleCredentials fetchNewCredentials() throws QCloudClientException {
        // 首先从您的临时密钥服务器获取包含了密钥信息的响应
        // 然后解析响应，获取临时密钥信息
        String tmpSecretId = BuildConfig.COS_SECRET_ID;      // 临时密钥 SecretId
        String tmpSecretKey = BuildConfig.COS_SECRET_KEY;    // 临时密钥 SecretKey
        String sessionToken = "COS_SESSIONTOKEN";            // 临时密钥 Token
        long expiredTime = 1556189496L;//临时密钥有效截止时间戳，单位是秒
        //建议返回服务器时间作为签名的开始时间，避免由于用户手机本地时间偏差过大导致请求过期
        // 返回服务器时间作为签名的起始时间
        long startTime = 1556182000L; //临时密钥有效起始时间，单位是秒
        // 最后返回临时密钥信息对象
        return new SessionQCloudCredentials(tmpSecretId, tmpSecretKey, sessionToken, startTime, expiredTime);
    }


    /**
     * //这里假设类名为 MySessionCredentialProvider。初始化一个实例，来给 SDK 提供密钥。
     */
    private static QCloudCredentialProvider getQCloudCredentialProvider1() {
        //这里假设类名为 MySessionCredentialProvider。初始化一个实例，来给 SDK 提供密钥。
        if (myCredentialProvider == null) {
            myCredentialProvider = new MySessionCredentialProvider();
        }
        return myCredentialProvider;

    }


    /**
     * 使用永久密钥进行本地调试
     * 您可以使用腾讯云的永久密钥来进行开发阶段的本地调试。由于该方式存在泄漏密钥的风险，请务必在上线前替换为临时密钥的方式。
     */
    private static QCloudCredentialProvider getQCloudCredentialProvider2() {
        String secretId = BuildConfig.COS_SECRET_ID;   //永久密钥COS_SECRETID secretId
        String secretKey = BuildConfig.COS_SECRET_KEY; //永久密钥 COS_SECRETKEY secretKey
        // keyDuration 为请求中的密钥有效期，单位为秒
        if (myCredentialProvider == null) {
            myCredentialProvider = new ShortTimeCredentialProvider(secretId, secretKey, 5000);
        }
        return myCredentialProvider;
    }


    /**
     * 2. 初始化 COS Service
     * 使用您提供密钥的实例 myCredentialProvider，初始化一个 CosXmlService 的实例。
     * CosXmlService 提供了访问 COS 的所有接口，建议作为 程序单例 使用。
     */
    private static CosXmlService getCosXmlService(Context context) {
        //实现获取临时密钥1
        QCloudCredentialProvider myCredentialProvider = getQCloudCredentialProvider1();

        //使用永久密钥进行本地调试2
        //QCloudCredentialProvider myCredentialProvider = getQCloudCredentialProvider2();


        // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
        String region = "COS_REGION";
        // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlService cosXmlService1 = CosServiceFactory.getCosXmlService(DemoApplication.instance(), BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, false);
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder();
// 初始化 COS Service，获取实例
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, myCredentialProvider);


        return cosXmlService;
    }


    /**
     * SDK 支持上传本地文件、二进制数据、Uri 以及输入流。下面以上传本地文件为例。
     */
    private static void Uploadobject(Context context) {

        //获取初始化对像
        CosXmlService cosXmlService = getCosXmlService(context);

        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        // 初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

        String bucket = "examplebucket-1250000000"; //存储桶，格式：BucketName-APPID
        String cosPath = "exampleobject"; //对象在存储桶中的位置标识符，即称对象键
        String srcPath = new File(context.getCacheDir(), "exampleobject").toString(); //本地文件的绝对路径

        //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
        String uploadId = null;
        // 上传文件
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
        //设置上传进度回调
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

            }
        });
        //设置返回结果回调
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                String accessUrl = cOSXMLUploadTaskResult.accessUrl;
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

        //设置任务状态回调, 可以查看任务过程
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {

            }
        });
    }

    /**
     * 下载对象
     */
    private static void Downloadobject(Context context) {
        CosXmlService cosXmlService = getCosXmlService(context);
        // 高级下载接口支持断点续传，所以会在下载前先发起 HEAD 请求获取文件信息。
        // 如果您使用的是临时密钥或者使用子账号访问，请确保权限列表中包含 HeadObject 的权限。
        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        //初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);
        String bucket = "examplebucket-1250000000"; //存储桶，格式：BucketName-APPID
        String cosPath = "exampleobject"; //对象在存储桶中的位置标识符，即称对象名称
        //本地目录路径
        String savePathDir = context.getExternalCacheDir().toString();
        //本地保存的文件名，若不填（null），则与 COS 上的文件名一样
        String savedFileName = "exampleobject";
        Context applicationContext = context.getApplicationContext();

        COSXMLDownloadTask cosxmlDownloadTask = transferManager.download(applicationContext, bucket, cosPath, savePathDir, savedFileName);
        //设置下载进度回调
        cosxmlDownloadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {

            }
        });
        //设置返回结果回调
        cosxmlDownloadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLDownloadTask.COSXMLDownloadTaskResult cOSXMLDownloadTaskResult = (COSXMLDownloadTask.COSXMLDownloadTaskResult) result;
                String accessUrl = cOSXMLDownloadTaskResult.accessUrl;
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
        //设置任务状态回调，可以查看任务过程
        cosxmlDownloadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(TransferState state) {

            }
        });

    }


    /**
     * 删除单个对象	在存储桶中删除指定对象
     */
    public static void DELETEObject(String exampleobject) {
        String bucket = DemoApplication.bucket.name; //存储桶名称，格式：BucketName-APPID
        String cosPath = exampleobject; //对象在存储桶中的位置标识符，即对象名称 (exampleobject)文件名称 name_001.jpg
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, cosPath);
        DemoApplication.cosXmlService.deleteObjectAsync(deleteObjectRequest, new CosXmlResultListener() {

            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult result) {
                DeleteObjectResult deleteObjectResult = (DeleteObjectResult) result;
                Log.d(TAG, "删除成功: " + deleteObjectResult.printResult());
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if (clientException != null) {
                    clientException.printStackTrace();
                } else {
                    serviceException.printStackTrace();
                }
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }
        });
    }

    /**
     * 删除多个对像
     *
     * @SuppressLint("LongLogTag")
     */
    public static void DELETEObject(List<String> objectList) {
        String bucket = DemoApplication.bucket.name; //存储桶名称，格式：BucketName-APPID
        //List<String> objectList = new ArrayList();
        //objectList.add("exampleobject1"); //对象在存储桶中的位置标识符，即对象键
        //objectList.add("exampleobject2"); //对象在存储桶中的位置标识符，即对象键

        DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(bucket, objectList);
        // Quiet 模式只返回报错的 Object 信息。否则返回每个 Object 的删除结果。
        deleteMultiObjectRequest.setQuiet(true);
        DemoApplication.cosXmlService.deleteMultiObjectAsync(deleteMultiObjectRequest, new CosXmlResultListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult result) {
                DeleteMultiObjectResult deleteMultiObjectResult = (DeleteMultiObjectResult) result;
                Log.d(TAG, "删除成功: " + deleteMultiObjectResult.printResult());
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if (clientException != null) {
                    clientException.printStackTrace();
                } else {
                    serviceException.printStackTrace();
                }
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }
        });
    }


    /**
     * 删除多个对像
     *
     * @SuppressLint("LongLogTag")
     */
    public static void DELETEMultipleObject(List<String> objectList) {
        String bucket = DemoApplication.bucket.name; //存储桶名称，格式：BucketName-APPID
        //List<String> objectList = new ArrayList();
        //objectList.add("exampleobject1"); //对象在存储桶中的位置标识符，即对象键
        //objectList.add("exampleobject2"); //对象在存储桶中的位置标识符，即对象键

        DeleteMultiObjectRequest deleteMultiObjectRequest = new DeleteMultiObjectRequest(bucket, objectList);
        // Quiet 模式只返回报错的 Object 信息。否则返回每个 Object 的删除结果。
        deleteMultiObjectRequest.setQuiet(true);
        DemoApplication.cosXmlService.deleteMultiObjectAsync(deleteMultiObjectRequest, new CosXmlResultListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult result) {
                DeleteMultiObjectResult deleteMultiObjectResult = (DeleteMultiObjectResult) result;
                Log.d(TAG, "删除成功: " + deleteMultiObjectResult.printResult());
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
                if (clientException != null) {
                    clientException.printStackTrace();
                } else {
                    serviceException.printStackTrace();
                }
                if (paymnets != null) {
                    paymnets.onFail();
                }
            }
        });
    }


    /**
     * 上传时使用图片处理
     * 下面示例展示了如何在上传图片时自动实现图片处理。
     */
    private void getpicOperations() {

        String bucket = "";
        String cosPath = "";
        String srcPath = "";
        String uploadId = "";

        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        // 初始化 TransferManager
        TransferManager transferManager = new TransferManager(DemoApplication.cosXmlService, transferConfig);

        List<PicOperationRule> rules = new LinkedList<>();
        // 添加一条将图片转化为 png 格式的 rule，处理后的图片在存储桶中的位置标识符为
        rules.add(new PicOperationRule("examplepngobject", "imageView2/format/png"));
        PicOperations picOperations = new PicOperations(true, rules);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, cosPath, srcPath);
        putObjectRequest.setPicOperations(picOperations);

        // 上传成功后，您将会得到 2 张图片，分别是原始图片和处理后图片
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath, srcPath, uploadId);
    }
}
