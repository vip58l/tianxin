/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/28 0028
 */


package com.tianxin.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;

import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.utils.DemoLog;
import com.tianxin.IMtencent.utils.MessageNotification;
import com.tianxin.IMtencent.utils.PrivateConstants;
import com.tianxin.Module.Datamodule;
import com.tianxin.Module.api.Buckets;
import com.tianxin.Receiver.videoService;
import com.tianxin.activity.WelcomeActivity;

import com.tianxin.wxapi.WXpayObject;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.service.GetServiceRequest;
import com.tencent.cos.xml.model.service.GetServiceResult;
import com.tencent.cos.xml.model.tag.ListAllMyBuckets;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;

import com.tencent.qcloud.costransferpractice.BuildConfig;
import com.tencent.qcloud.costransferpractice.CosServiceFactory;
import com.tencent.qcloud.costransferpractice.transfer.Api;
import com.tencent.qcloud.tim.uikit.TUIKit;

import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.rtmp.TXLiveBase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.tencent.liteav.debug.GenerateTestUserSig.SDKAPPID;


public class DemoApplication extends Application {
    private static String TAG = DemoApplication.class.getName();
    private static DemoApplication demoApplication;

    /**
     * 监听是否在线 和接收消息内容
     */
    private IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onNewMessage(V2TIMMessage msg) {
            MessageNotification notification = MessageNotification.getInstance();
            notification.notify(msg);
        }

        @Override
        public void onForceOffline() {
            super.onForceOffline();
            Log.d(TAG, "您的帐号已在其它终端登录");
            BaseActivity.logout(DemoApplication.instance());
        }

        @Override
        public void onUserSigExpired() {
            super.onUserSigExpired();
            Log.d(TAG, "帐号已过期，请重新登录: ");
            BaseActivity.logout(DemoApplication.instance());
        }


    };

    public static DemoApplication instance() {
        if (demoApplication == null) {
            demoApplication = new DemoApplication();
        }
        return demoApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        demoApplication = this;
        cacheDir();                 //缓存目录直播礼物缓存
        sUserStrategybugly();       //bugly上报收集
        TUIKitConfigs();            //腾讯IM即时通信
        getBuckets1();              //腾讯云
        developerumeng();           //友盟统计

        //监听前后台切换界面状态或退出不在当前应用
        registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());

        //解决android7.0拍照问题（亲测有效）
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        //初始化微信分享登录
        WXpayObject.getsWXpayObject();
    }

    /**
     * 腾讯IM通讯聊天初始化
     */
    private void TUIKitConfigs() {
        TUIKitConfigs configs = TUIKit.getConfigs();
        configs.setSdkConfig(new V2TIMSDKConfig());
        configs.setCustomFaceConfig(new CustomFaceConfig());
        configs.setGeneralConfig(new GeneralConfig());

        if (true) {
            TUIKit.init(this, configs);
            Datamodule.getInstance(this).SDKAPPID(this, configs);
        } else {
            TUIKit.init(this, SDKAPPID, configs);
        }
    }

    /**************初始存储桶 ********************/
    public static ListAllMyBuckets.Bucket bucket;
    public static CosXmlService cosXmlService;

    /**
     * 初始化腾讯云
     */
    private void getBuckets1() {
        CosXmlService cosXmlService1 = CosServiceFactory.getCosXmlService(DemoApplication.instance(), BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, false);
        cosXmlService1.getServiceAsync(new GetServiceRequest(), new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, final CosXmlResult result) {
                List<ListAllMyBuckets.Bucket> buckets = ((GetServiceResult) result).listAllMyBuckets.buckets;
                //拿到存储桶列表最新的一个存储桶
                bucket = buckets.get(buckets.size() - 1);
                Buckets sbuckets = Buckets.getSbuckets();
                sbuckets.setName(bucket.name);
                sbuckets.setLocation(bucket.location);
                sbuckets.setCreateDate(bucket.createDate);
                sbuckets.setType(bucket.type);
                cosXmlService = getBuckets2(bucket.location);
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                exception.printStackTrace();
                serviceException.printStackTrace();
            }
        });
    }

    private static CosXmlService getBuckets2(String bucketRegion) {
        CosXmlService cosXmlService2 = CosServiceFactory.getCosXmlService(DemoApplication.instance(), bucketRegion, BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, true);
        return cosXmlService2;
    }

    /**
     * 解析网址正常显示
     *
     * @param path
     * @return
     */
    public static String presignedURL(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        Buckets sbuckets = Buckets.getSbuckets();
        if (cosXmlService != null) {
            return Api.getPresignedURL(cosXmlService, bucket == null ? sbuckets.getName() : bucket.name, path);
        } else {
            cosXmlService = getBuckets2(sbuckets.getLocation());

            return Api.getPresignedURL(cosXmlService, bucket == null ? sbuckets.getName() : bucket.name, path);
        }

    }

    /**************初始存储桶 ********************/
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory();
        }
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //内存低的时候，清理Glide的缓存
        Glide.get(this).clearMemory();
    }

    /**
     * bugly上报异常收集
     */
    private void sUserStrategybugly() {

        //Android视频直播腾讯云推流
        String licenceURL = ""; // 获取到的 licence url
        String licenceKey = ""; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
        try {
            // bugly上报
            String processName = getProcessName(android.os.Process.myPid());                               //获取当前进程名
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(DemoApplication.instance());  //设置是否为上报进程
            strategy.setAppVersion(V2TIMManager.getInstance().getVersion());
            strategy.setUploadProcess(processName == null || processName.equals(DemoApplication.instance().getPackageName()));
            //获取APP ID并将以下代码复制到项目Application类onCreate()中，Bugly会为自动检测环境并完成配置：初始化Bugly
            CrashReport.initCrashReport(getApplicationContext(), PrivateConstants.BUGLY_APPID, false, strategy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调
     * 用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
     * UMConfigure.init调用中appkey和channel参数请置为null）。
     */
    private void developerumeng() {
        // 初始化SDK
        UMConfigure.init(this, com.tencent.qcloud.tim.tuikit.live.BuildConfig.DEVICE_TYPE_KEY, "channel_paixide", UMConfigure.DEVICE_TYPE_PHONE, null);
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        //UMConfigure.setLogEnabled(false);
    }

    /**
     * 获取进程号对应的进程名
     * 其中获取进程名的方法“getProcessName”有多种实现方法
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 监听前后台切换界面状态或退出不在当前应用
     * 活动生命周期回调
     */
    private class StatisticActivityLifecycleCallback implements ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;

        /**
         * 华为离线推送角标
         */
        private ConversationManagerKit.MessageUnreadWatcher mUnreadWatcher = new ConversationManagerKit.MessageUnreadWatcher() {
            @Override
            public void updateUnread(int count) {

            }
        };

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.d(TAG, "onActivityCreated: 创建的活动");
            if (bundle != null) {
                // 重启整个程序
                Intent intent = new Intent(activity, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "App切到前台");

            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台 APP打开显示状态
                V2TIMManager.getOfflinePushManager().doForeground(new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        DemoLog.e(TAG, "doForeground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        DemoLog.i(TAG, "doForeground success");
                    }
                });

                //应用切到前台 移除监听
                TUIKit.removeIMEventListener(mIMEventListener);
                ConversationManagerKit.getInstance().removeUnreadWatcher(mUnreadWatcher);
                MessageNotification.getInstance().cancelTimeout();
            }
            isChangingConfiguration = false;

            //判断服务是否停止了
            videoService.isServiceRunning(instance(), videoService.class.getSimpleName());

        }

        @Override
        public void onActivityResumed(Activity activity) {
//            Log.d(TAG, "onActivityResumed: 活动恢复");
        }

        @Override
        public void onActivityPaused(Activity activity) {
//            Log.d(TAG, "onActivityPaused: 活动暂停" + activity.getLocalClassName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "App切到后台");
            foregroundActivities--;
            if (foregroundActivities == 0) {
                //应用切到后台
                int unReadCount = ConversationManagerKit.getInstance().getUnreadTotal();
                V2TIMManager.getOfflinePushManager().doBackground(unReadCount, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        DemoLog.e(TAG, "doBackground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        DemoLog.i(TAG, "doBackground success");
                    }
                });

                //应用退到后台，消息转化为系统通知 添加监听消息
                TUIKit.addIMEventListener(mIMEventListener);

                //添加未读计数监听器
                ConversationManagerKit.getInstance().addUnreadWatcher(mUnreadWatcher);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//            Log.d(TAG, "onActivitySaveInstanceState:保存恢复状态值 ");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
//            Log.d(TAG, "onActivityDestroyed: 关于活动");

        }
    }

//*********************** HttpProxyCacheServer (音视频缓存框架) *********************************//

    private static HttpProxyCacheServer proxy;

    /**
     * 默认代硬请求2
     *
     * @return
     */
    public static HttpProxyCacheServer getProxy() {
        DemoApplication app = DemoApplication.instance();
        return app.proxy == null ? (app.proxy = app.newProxy2()) : app.proxy;
    }

    /**
     * 默认缓存配置
     *
     * @return
     */
    private HttpProxyCacheServer newProxy1() {
        return new HttpProxyCacheServer(this);
    }

    /**
     * 缓存的文件最大大小
     *
     * @return
     */
    private HttpProxyCacheServer newProxy2() {
        return new HttpProxyCacheServer.Builder(this).maxCacheSize(1024 * 1024 * 1024).build();
    }

    /**
     * 缓存文件个数
     *
     * @return
     */
    private HttpProxyCacheServer newProxy3() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(20)
                .build();
    }

//***************************** HttpProxyCacheServer (音视频缓存框架) ***************************//

    /**
     * 直播礼物缓存 http://svga.io/svga-preview.html
     */
    private void cacheDir() {
        try {
            File cacheDir = new File(getApplicationContext().getCacheDir(), "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

