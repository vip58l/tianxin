package com.tianxin.Test;
//IntelliJ IDEA常用快捷键总结
//https://www.cnblogs.com/ka-bu-qi-nuo/p/9182655.html
//for / fori  psvm sout
//https://www.jianshu.com/p/d6c8b3f8f48c
//https://www.jb51.net/article/136761.htm
//Ctrl+Shift+Space  Ctrl+Space 自动提示
//Alt+Enter快速修复 Ctrl+Shift+Enter

/*
       格式化代码Ctrl+Alt+L
        Ø  Top #10切来切去：Ctrl+Tab
        Ø  Top #9选你所想：Ctrl+W
        Ø  Top #8代码生成：Template/Postfix +Tab
        Ø  Top #7发号施令：Ctrl+Shift+A
        Ø  Top #6无处藏身：Shift+Shift
        Ø  Top #5自动完成：Ctrl+Shift+Enter
        Ø  Top #4创造万物：Alt+Insert
        Ø  Top #1智能补全：Ctrl+Shift+Space
        Ø  Top #1自我修复：Alt+Enter
        Ø  Top #1重构一切：Ctrl+Shift+Alt+T
*/


import com.google.gson.Gson;
import com.tianxin.Module.api.Config_Uration;
import com.tianxin.Util.log;
import com.tianxin.getlist.Advert;
import com.tianxin.getlist.playvideo;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IntelliJIDEA {

    public static List<playvideo> list = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println();

    }


    /**
     * 拼接字符串
     */
    public static void logs() {
        StringBuilder sb = new StringBuilder();
        sb.append("11");
        sb.append("22");
        String s = sb.toString();
        log.d(s);



    }


    public static List<playvideo> vod_51dengta_net() {
        try {
            String text1 = "{\"result\":true,\"code\":0,\"run_time\":0,\"info\":{\"total\":1480,\"list\":[{\"id\":2335,\"title\":\"\uD83D\uDE0D\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/964e741a5285890807198634981/5285890807198634982.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/964e741a5285890807198634981/Zk6XTgNvISwA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":4,\"label\":\"日常\",\"latitude\":\"0.00000000\",\"ctime\":1599189151646,\"width\":720,\"height\":1264,\"duration\":\"00:00:07\",\"comment_count\":5,\"share_count\":0,\"from\":{\"avatar\":\"http://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/c6a2bf071d39aef08870bcd7d1f7e06d1597390348818.jpeg\",\"height\":167,\"name\":\"冉魅儿\",\"sex\":2,\"unique_id\":39,\"age\":25,\"place\":{\"province\":\"安徽\",\"city\":\"蚌埠\",\"code\":340300},\"user_id\":\"1324252970\",\"link_level\":3,\"is_online\":0},\"duration_time\":7,\"is_follow\":0,\"like_count\":19,\"is_like\":0},{\"id\":2213,\"title\":\"我相信相聚或者分离，命中早以注定，我坦然接受分离，也接受与你不期而遇\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/8519d4795285890807133369977/5285890807133369978.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/8519d4795285890807133369977/M3mak7DmtWkA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":1600860122244,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599125379773,\"width\":720,\"height\":1280,\"duration\":\"00:00:10\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/8ac9ae6c9388261838eadb294881c13a1598874713603.jpeg\",\"height\":161,\"name\":\"啊婧\",\"sex\":2,\"unique_id\":811,\"age\":31,\"place\":{\"province\":\"福建\",\"city\":\"厦门\",\"code\":350200},\"user_id\":\"8527606463\",\"link_level\":0,\"is_online\":0},\"duration_time\":10,\"is_follow\":0,\"like_count\":22,\"is_like\":0},{\"id\":2263,\"title\":\"没有胭脂的年代，女儿的脸只为情郎红，可后来有了胭脂，便分不清是真情还是假意。\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/c30cc6495285890807185626112/5285890807185626113.png\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/c30cc6495285890807185626112/uBqMuprftO4A.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599139041036,\"width\":720,\"height\":1280,\"duration\":\"00:00:07\",\"comment_count\":3,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/27F41A60AD61E4E52FB424A892774C17.png\",\"height\":162,\"name\":\"阿奈\",\"sex\":2,\"unique_id\":660,\"age\":28,\"place\":{\"province\":\"广东\",\"city\":\"揭阳\",\"code\":445200},\"user_id\":\"7131004661\",\"link_level\":0,\"is_online\":0},\"duration_time\":7,\"is_follow\":0,\"like_count\":34,\"is_like\":0},{\"id\":2854,\"title\":\"没心没肺才活着不累，傻人有傻福。\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/a234ff435285890807344104046/5285890807344104047.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/a234ff435285890807344104046/STAhiyQ8sC8A.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599556181296,\"width\":720,\"height\":960,\"duration\":\"00:00:16\",\"comment_count\":0,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/9fee5dc666eb5d6e5560c76f52b97c1e1598873627244.jpeg\",\"height\":162,\"name\":\"蓝色妖姬\",\"sex\":2,\"unique_id\":870,\"age\":33,\"place\":{\"province\":\"广东\",\"city\":\"深圳\",\"code\":440300},\"user_id\":\"9109959103\",\"link_level\":0,\"is_online\":0},\"duration_time\":16,\"is_follow\":0,\"like_count\":16,\"is_like\":0},{\"id\":2347,\"title\":\"顺其自然的意思大概就是  我也很无奈但是随他妈便吧\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/603d89235285890807160213076/5285890807160213077.png\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/603d89235285890807160213076/lcrXRjoVfIAA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599192490819,\"width\":592,\"height\":1280,\"duration\":\"00:00:10\",\"comment_count\":2,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/48DAB2C102204F0F8BC5DAE98716F165.png\",\"height\":160,\"name\":\"哇哦\",\"sex\":2,\"unique_id\":447,\"age\":25,\"place\":{\"province\":\"贵州\",\"city\":\"贵阳\",\"code\":520100},\"user_id\":\"5111116097\",\"link_level\":0,\"is_online\":0},\"duration_time\":10,\"is_follow\":0,\"like_count\":17,\"is_like\":0},{\"id\":2505,\"title\":\"为什么这张照片做出来的视频是半张脸\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/e7a5d4c75285890807219025092/5285890807219025094.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/e7a5d4c75285890807219025092/Ja9d2bY25NoA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599226431187,\"width\":1920,\"height\":1072,\"duration\":\"00:00:08\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"http://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/4ba66d48f033bc6dbed6dede8c5a50e51597327615909.jpeg\",\"height\":160,\"name\":\"柠萌\",\"sex\":2,\"unique_id\":636,\"age\":31,\"place\":{\"province\":\"河南\",\"city\":\"驻马店\",\"code\":411700},\"user_id\":\"6938313124\",\"link_level\":0,\"is_online\":0},\"duration_time\":8,\"is_follow\":0,\"like_count\":10,\"is_like\":0},{\"id\":2646,\"title\":\"新人出道，多多指教\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/513e59f45285890807305965366/5285890807305965367.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/513e59f45285890807305965366/dckCboJzHSMA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599451903477,\"width\":720,\"height\":960,\"duration\":\"00:00:13\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/a6f1c113771145c0d3a9b95de85f82101598610861300.jpeg\",\"height\":168,\"name\":\"青柠\",\"sex\":2,\"unique_id\":618,\"age\":27,\"place\":{\"province\":\"吉林\",\"city\":\"长春\",\"code\":220100},\"user_id\":\"6739480538\",\"link_level\":0,\"is_online\":0},\"duration_time\":13,\"is_follow\":0,\"like_count\":13,\"is_like\":0},{\"id\":2727,\"title\":\"小女子不才\\n未得公子青睐\\n扰公子良久\\n公子勿怪\\n。\\n。\\n。\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/369013885285890807315670441/5285890807315670442.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/369013885285890807315670441/hHwNOgkfUVoA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":1600861438886,\"label_id\":3,\"label\":\"二次元\",\"latitude\":\"0.00000000\",\"ctime\":1599472413231,\"width\":544,\"height\":960,\"duration\":\"00:00:11\",\"comment_count\":2,\"share_count\":1,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/ed41aae3bb1394e85a530111d3720d0a1599326773175.jpeg\",\"height\":155,\"name\":\"胭脂泪\",\"sex\":2,\"unique_id\":902,\"age\":31,\"place\":{\"province\":\"上海\",\"city\":\"上海\",\"code\":310100},\"user_id\":\"9434355795\",\"link_level\":0,\"is_online\":0},\"duration_time\":11,\"is_follow\":0,\"like_count\":35,\"is_like\":0},{\"id\":2792,\"title\":\"在对的时间遇见对的人\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/730a2cae5285890807340317487/5285890807340317488.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/730a2cae5285890807340317487/gMPD6uT5I8wA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":1600939180573,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599536310209,\"width\":720,\"height\":1280,\"duration\":\"00:00:09\",\"comment_count\":2,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/1bf1d32c9556a4168ede52f8cd97e9091599535654739.jpeg\",\"height\":163,\"name\":\"小小甜\",\"sex\":2,\"unique_id\":651,\"age\":25,\"place\":{\"province\":\"浙江\",\"city\":\"宁波\",\"code\":330200},\"user_id\":\"7086557890\",\"link_level\":0,\"is_online\":1},\"duration_time\":9,\"is_follow\":0,\"like_count\":13,\"is_like\":0},{\"id\":2837,\"title\":\"祝你快乐，不止今天\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/6b18dfc85285890807350835313/5285890807350835315.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/6b18dfc85285890807350835313/r1MWcG3AFJwA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599552048074,\"width\":720,\"height\":1280,\"duration\":\"00:00:06\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/7c6a0481337c974791f1aeeae1c36f821598870391107.jpeg\",\"height\":170,\"name\":\"阿慧儿超甜\",\"sex\":2,\"unique_id\":436,\"age\":22,\"place\":{\"province\":\"辽宁\",\"city\":\"大连\",\"code\":210200},\"user_id\":\"5027252800\",\"link_level\":0,\"is_online\":0},\"duration_time\":6,\"is_follow\":0,\"like_count\":16,\"is_like\":0},{\"id\":2593,\"title\":\"早！\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/5a9086f75285890807265454749/5285890807265454750.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/5a9086f75285890807265454749/LjC5RyfuqM4A.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1599348820112,\"width\":720,\"height\":1264,\"duration\":\"00:00:08\",\"comment_count\":2,\"share_count\":0,\"from\":{\"avatar\":\"http://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/c6a2bf071d39aef08870bcd7d1f7e06d1597390348818.jpeg\",\"height\":167,\"name\":\"冉魅儿\",\"sex\":2,\"unique_id\":39,\"age\":25,\"place\":{\"province\":\"安徽\",\"city\":\"蚌埠\",\"code\":340300},\"user_id\":\"1324252970\",\"link_level\":3,\"is_online\":0},\"duration_time\":8,\"is_follow\":0,\"like_count\":16,\"is_like\":0},{\"id\":1824,\"title\":\"惊艳&惊吓\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/6a6cf4fb5285890807099479381/5285890807099479382.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/6a6cf4fb5285890807099479381/XFJ5xf0g1ZwA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1598941630206,\"width\":576,\"height\":1024,\"duration\":\"00:00:10\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/9d418386b2769f0096d95c3759902f781600331012825.jpeg\",\"height\":160,\"name\":\"意宝\",\"sex\":2,\"unique_id\":468,\"age\":31,\"place\":{\"province\":\"黑龙江\",\"city\":\"绥化\",\"code\":231200},\"user_id\":\"5343610915\",\"link_level\":0,\"is_online\":0},\"duration_time\":10,\"is_follow\":0,\"like_count\":11,\"is_like\":0},{\"id\":2900,\"title\":\"再来一波\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/c8e4e1f15285890807334944409/5285890807334944410.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/c8e4e1f15285890807334944409/fe6jtfAfPFMA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":4,\"label\":\"日常\",\"latitude\":\"0.00000000\",\"ctime\":1599623893467,\"width\":720,\"height\":960,\"duration\":\"00:00:19\",\"comment_count\":2,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/30773ea4a74e1e6104c3baca0f3daf8b1598956684317.png\",\"height\":0,\"name\":\"彼岸花\",\"sex\":2,\"unique_id\":310,\"age\":30,\"place\":{\"province\":\"陕西\",\"city\":\"延安\",\"code\":610600},\"user_id\":\"3983267253\",\"link_level\":0,\"is_online\":0},\"duration_time\":19,\"is_follow\":0,\"like_count\":20,\"is_like\":0},{\"id\":3026,\"title\":\"叮咚，你有一条爱意的信息请注意查收\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/495da5735285890807655822356/5285890807655822357.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/495da5735285890807655822356/KJUFabOG1RwA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1600165268468,\"width\":672,\"height\":528,\"duration\":\"00:00:12\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/9d79a9c1ef9c8350aa79252305e16f631598672627107.jpeg\",\"height\":165,\"name\":\"蝴蝶结\",\"sex\":2,\"unique_id\":380,\"age\":31,\"place\":{\"province\":\"陕西\",\"city\":\"西安\",\"code\":610100},\"user_id\":\"4481996468\",\"link_level\":0,\"is_online\":0},\"duration_time\":12,\"is_follow\":0,\"like_count\":12,\"is_like\":0},{\"id\":3018,\"title\":\"当有人惹你生气时，深吸一口气，从10开始倒数，数到7的时候开始打他，他肯定不会想到的。\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/9f3a6ab85285890807653144789/5285890807653144790.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/9f3a6ab85285890807653144789/dA5QdPASTlkA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":1600936285799,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1600159998641,\"width\":720,\"height\":960,\"duration\":\"00:00:15\",\"comment_count\":4,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/9fee5dc666eb5d6e5560c76f52b97c1e1598873627244.jpeg\",\"height\":162,\"name\":\"蓝色妖姬\",\"sex\":2,\"unique_id\":870,\"age\":33,\"place\":{\"province\":\"广东\",\"city\":\"深圳\",\"code\":440300},\"user_id\":\"9109959103\",\"link_level\":0,\"is_online\":0},\"duration_time\":15,\"is_follow\":0,\"like_count\":17,\"is_like\":0},{\"id\":3237,\"title\":\"抠脚小公主\uD83D\uDC78\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/1fa4d14c5285890807974019014/5285890807974019015.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/1fa4d14c5285890807974019014/TiLCm1a156MA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":3,\"label\":\"二次元\",\"latitude\":\"0.00000000\",\"ctime\":1600938857692,\"width\":576,\"height\":1024,\"duration\":\"00:00:07\",\"comment_count\":0,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/23c9e96fff522110a53410d3103578fe1607083542494.jpeg\",\"height\":162,\"name\":\"梦淇\",\"sex\":2,\"unique_id\":765,\"age\":31,\"place\":{\"province\":\"吉林\",\"city\":\"吉林\",\"code\":220200},\"user_id\":\"8086952764\",\"link_level\":0,\"is_online\":0},\"duration_time\":7,\"is_follow\":0,\"like_count\":12,\"is_like\":0},{\"id\":3248,\"title\":\"撩你是我的错，动心就是你不对了\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/9d29d2685285890807914326420/5285890807914326421.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/9d29d2685285890807914326420/9K8rwW20Mz4A.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":1600958114383,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1600958097333,\"width\":720,\"height\":1280,\"duration\":\"00:00:08\",\"comment_count\":2,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/1ec4037e0a3771b1720abb17ab17ff0f1598878216633.jpeg\",\"height\":0,\"name\":\"柔儿\",\"sex\":2,\"unique_id\":916,\"age\":23,\"place\":{\"province\":\"吉林\",\"city\":\"吉林\",\"code\":220200},\"user_id\":\"9549900480\",\"link_level\":0,\"is_online\":0},\"duration_time\":8,\"is_follow\":0,\"like_count\":21,\"is_like\":0},{\"id\":4397,\"title\":\"这么热的天，让你出来喝喝酒，降降温，是害你嘛？是爱你啊 笨蛋\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/ca4a6b1e5285890810377998961/5285890810377998963.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/ca4a6b1e5285890810377998961/kNhwgjzBq0oA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":3,\"label\":\"二次元\",\"latitude\":\"0.00000000\",\"ctime\":1606188682694,\"width\":720,\"height\":1280,\"duration\":\"00:00:08\",\"comment_count\":3,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/F2ABFECEB9995C6074E370F22CE280ED.png\",\"height\":162,\"name\":\"小酥\",\"sex\":2,\"unique_id\":1716139828,\"age\":21,\"place\":{\"province\":\"广东\",\"city\":\"汕头\",\"code\":440500},\"user_id\":\"9117735128\",\"link_level\":1,\"is_online\":0},\"duration_time\":8,\"is_follow\":0,\"like_count\":21,\"is_like\":0},{\"id\":4445,\"title\":\"初次见面，想让你把我放在眼里\uD83D\uDE1B\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/18f6d18d5285890810337951617/5285890810337951618.png\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/18f6d18d5285890810337951617/5e3sg9cxOlEA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":1606360813096,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1606279136566,\"width\":720,\"height\":1280,\"duration\":\"00:00:05\",\"comment_count\":2,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/CACAA38CBF43D21236B8A4C77514D704.png\",\"height\":165,\"name\":\"Eliauk\",\"sex\":2,\"unique_id\":1123446966,\"age\":22,\"place\":{\"province\":\"贵州\",\"city\":\"贵阳\",\"code\":520100},\"user_id\":\"5050185387\",\"link_level\":0,\"is_online\":0},\"duration_time\":5,\"is_follow\":0,\"like_count\":15,\"is_like\":0},{\"id\":4480,\"title\":\"你确定这就是爱吗？\",\"cover\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/92c6f2505285890810444952755/5285890810444952756.jpg\",\"url\":\"http://vod.51dengta.net/24c975fbvodcq1302331714/92c6f2505285890810444952755/EYKELGuLLYUA.mp4\",\"address\":\"\",\"longitude\":\"0.00000000\",\"allow_visit\":1,\"sticky_time\":0,\"label_id\":1,\"label\":\"颜值\",\"latitude\":\"0.00000000\",\"ctime\":1606308012300,\"width\":544,\"height\":960,\"duration\":\"00:00:12\",\"comment_count\":1,\"share_count\":0,\"from\":{\"avatar\":\"https://51dengta.oss-cn-shenzhen.aliyuncs.com/upload/images/geren_touxiang/593dfbdf9073a3a6d7200a9250c88e121606306186396.jpeg\",\"height\":158,\"name\":\"兔牙妹儿\",\"sex\":2,\"unique_id\":4214846138,\"age\":25,\"place\":{\"province\":\"上海\",\"city\":\"上海\",\"code\":310100},\"user_id\":\"7833030998\",\"link_level\":0,\"is_online\":0},\"duration_time\":12,\"is_follow\":0,\"like_count\":14,\"is_like\":0}]},\"msg\":\"\",\"request_id\":\"ebrkvsj9teebrktuc0tv\"}";
            IntelliJIDEA.list.clear();
            addplayvideo(text1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

    public static void addplayvideo(String str) throws JSONException {
        JSONObject jsonObject = new JSONObject(str);
        Gson gson = new Gson();
        JSONArray list = jsonObject.getJSONObject("info").getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject json = (JSONObject) list.get(i);
            playvideo playvideo = gson.fromJson(json.toString(), playvideo.class);
            IntelliJIDEA.list.add(playvideo);
        }

    }

    //转换实例Gson
    public void setGson() {

        Gson gson = new Gson();

        //1对像转JSON字符串
        Advert advert = new Advert();
        String json = gson.toJson(advert);

        //2 JSON字符串转对像
        Advert advert1 = gson.fromJson(json, Advert.class);

    }

    /**
     * 基本配置信息
     */
    public static void addConfiguration() {
        Config_Uration instance = Config_Uration.getInstance();
        instance.setId("123");
        instance.setUserid("2201");
        instance.setAutoLogin(true);
        instance.setAvatar("http://www.baidu.com");
        instance.setName("小清纯");
        instance.setPhone("18372630538");
        instance.setUsername("m18372630538");
        instance.setPasswoird("123456");
        instance.setToken("dhkflkhfeihldkhfl@386ksljf");
        instance.setSex(1);
    }


    /**
     * 读取配置信息
     */
    public static void getconfiguration() {
        Config_Uration instance = Config_Uration.getInstance();
        String userid = instance.getUserid();
        log.d(userid);

    }

    public void sss() {
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();
        v2TIMUserFullInfo.setNickname("哈哈");
    }
}
