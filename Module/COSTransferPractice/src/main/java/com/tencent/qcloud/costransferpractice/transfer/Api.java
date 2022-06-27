/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/15 0015
 */


package com.tencent.qcloud.costransferpractice.transfer;

import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.PresignedUrlRequest;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.ListAllMyBuckets;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.http.RequestBodySerializer;
import com.tencent.qcloud.costransferpractice.BuildConfig;
import com.tencent.qcloud.costransferpractice.CosServiceFactory;
import com.tencent.qcloud.costransferpractice.common.base.BaseActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_EXTRA_BUCKET_NAME;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_EXTRA_FOLDER_NAME;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_EXTRA_REGION;

/**
 * 生成预签名链接
 * https://cloud.tencent.com/document/product/436/46421
 */
public class Api extends AppCompatActivity {

    private static final String TAG = "Api";

    /**
     * {@link CosXmlService} 是您访问 COS 服务的核心类，它封装了所有 COS 服务的基础 API 方法。
     * <p>
     * 每一个{@link CosXmlService} 对象只能对应一个 region，如果您需要同时操作多个 region 的
     * Bucket，请初始化多个 {@link CosXmlService} 对象。
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetlOGIN();
    }

    /**
     * 获取访问地址
     */
    public void GetlOGIN() {
        String bucketName = getIntent().getStringExtra(ACTIVITY_EXTRA_BUCKET_NAME);
        String bucketRegion = getIntent().getStringExtra(ACTIVITY_EXTRA_REGION);
        String filename = "2201_mmexport1610593622153.mp4";
        CosXmlService cosXmlService = CosServiceFactory.getCosXmlService(this, bucketRegion, BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, true);
        String presignDownloadUrl = Api.getPresignedURL(cosXmlService, bucketName, filename);
        Log.d(TAG, "GetlOGIN: " + presignDownloadUrl);

    }

    /**
     * 生成对象预签名链接 获取预签名下载链接
     */
    public static String getPresignedURL(CosXmlService cosXmlService, String bucketName, String filename) {
        if (cosXmlService == null || TextUtils.isEmpty(bucketName)) {
            return "";
        }
        try {
            String bucket = bucketName; //存储桶名称
            String cosPath = getFileName(filename); //即对象在存储桶中的位置标识符。即文件名称
            String method = "GET"; //请求 HTTP 方法.
            PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(bucket, cosPath);
            presignedUrlRequest.setRequestMethod(method);
            //拿到文件临时访问链接
            return cosXmlService.getPresignedURL(presignedUrlRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 示例代码一：生成预签名上传链接
     *
     * @param cosXmlService
     * @param bucketName
     * @param filename
     * @return
     */
    public static String getPresignedURL2(CosXmlService cosXmlService, String bucketName, String filename) {
        try {
            String bucket = bucketName; //存储桶名称
            String cosPath = getFileName(filename); //即对象在存储桶中的位置标识符。
            String method = "PUT"; //请求 HTTP 方法
            PresignedUrlRequest presignedUrlRequest = new PresignedUrlRequest(bucket, cosPath) {
                @Override
                public RequestBodySerializer getRequestBody() throws CosXmlClientException {
                    //用于计算 put 等需要带上 body 的请求的签名 URL
                    return RequestBodySerializer.string("text/plain", "this is test");
                }
            };
            presignedUrlRequest.setRequestMethod(method);
            return cosXmlService.getPresignedURL(presignedUrlRequest);

        } catch (CosXmlClientException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取文件名称
     * 获取URL文件名称
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        String[] result = path.split(".com/");
        return result[result.length - 1];
    }

    /**
     * 获取文件名称
     * 获取URL文件名称
     *
     * @param path
     * @return
     */
    public static String getFileName(String path, String key) {
        String[] result = path.split(key);
        return result[0];
    }

    /**
     * 获取文件名称
     *
     * @param path
     * @return
     */
    public static String getFileName(String path, String key, boolean bool) {
        String[] result = path.split(key);
        return result[bool ? result.length - 1 : 0];
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //内存低的时候，清理Glide的缓存
        //Glide.get(this).clearMemory();

        //Glide.get(this).trimMemory(level);


        //延时操作
        new Handler().postDelayed(new Runnable() {
            public void run() {

            }
        }, 10000);

        //定时器
        TimerTask task = new TimerTask() {
            public void run() {
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000);
    }


}
