# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.pili.pldroid.player.** { *; }
-keep class com.qiniu.qplayer.mediaEngine.MediaPlayer{*;}

-keep class com.tencent.** { *; }
-keep class com.tencent.imsdk.** { *; }

#腾讯请避免混淆Bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#处理混淆后gson无法解析问题
-dontwarn com.google.**
-keep class com.google.gson.** { *; }
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.google.firebase.**{*;}
-dontwarn com.google.firebase.**
-keep class sun.misc.Unsafe.**{ *; }
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** {*; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class com.tianxin.Module.api.**{*;}
-keep class com.tianxin.getlist.**{*;}
-keep class com.tianxin.IMtencent.**{*;}
-keep class com.tencent.opensource.**{*;}
-keep class com.tencent.liteav.login.**{*;}
-keep class com.tianxin.Module.api.**{*;}
-keep class com.tianxin.dialog.adapter.**{*;}

#礼物动画 SVGAPlayer
-keep class com.squareup.wire.** { *; }
-keep class com.opensource.svgaplayer.proto.** { *; }


 #2D地图:
 -keep class com.amap.api.maps2d.**{*;}
 -keep class com.amap.api.mapcore2d.**{*;}

 #3D地图 V5.0.0之前：
 -keep class com.amap.api.maps.**{*;}
 -keep class com.autonavi.amap.mapcore.*{*;}
 -keep class com.amap.api.trace.**{*;}

 #3D地图 V5.0.0之后：
 -keep class com.amap.api.maps.**{*;}
 -keep class com.autonavi.**{*;}
 -keep class com.amap.api.trace.**{*;}

 #定位：
 -keep class com.amap.api.location.**{*;}
 -keep class com.amap.api.fence.**{*;}
 -keep class com.autonavi.aps.amapapi.model.**{*;}

 #搜索：
 -keep class com.amap.api.services.**{*;}

 #导航 V7.3.0以前：
 -keep class com.amap.api.navi.**{*;}
 -keep class com.alibaba.idst.nls.** {*;}
 -keep class com.nlspeech.nlscodec.** {*;}
 -keep class com.google.**{*;}

 #导航 V7.3.0及以后：
 -keep class com.amap.api.navi.**{*;}
 -keep class com.alibaba.mit.alitts.*{*;}
 -keep class com.google.**{*;}


#如果您的应用使用了代码混淆，请添加如下配置，以避免【友盟+】SDK被错误混淆导致SDK不可用。
    -keep class com.umeng.** {*;}
    -keepclassmembers class * {
       public <init> (org.json.JSONObject);
    }
    -keepclassmembers enum * {
        public static **[] values();
        public static ** valueOf(java.lang.String);
    }
    # SDK 9.2.4及以上版本自带oaid采集模块，不再需要开发者再手动引用oaid库，所以可以不添加这些混淆
    -keep class com.zui.**{*;}
    -keep class com.miui.**{*;}
    -keep class com.heytap.**{*;}
    -keep class a.**{*;}
    -keep class com.vivo.**{*;}

#https://docs.agora.io/cn/Video/start_call_android?platform=Android#防止代码混淆
-keep class io.agora.**{*;}
-keep class io.agora**{*;}

-keep class com.videofilm.model.** { *; }

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.tencent.liteav.trtcvoiceroom.**{ public *;}
-keep class com.tencent.opensource.model.**{*;}
-keep class com.tianxin.wxapi.**{*;}

#Android 图片裁剪库 uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#微信分享
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** { *;}

-keep class tv.danmaku.ijk.media.** { *;}
-keep class tv.danmaku.ijk.media.player.** { *;}