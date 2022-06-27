package com.tianxin.getHandler;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//公共调用数据API接口类
public class Webrowse {

    //请求API地址
    public static final String HTTPAPI = BuildConfig.HTTP_API;
    //主网址
    public static final String HttpWEB = BuildConfig.HTTP_WEB;
    //用户登录请求
    public static final String memberlogin = HTTPAPI + "/memberlogin";
    /**
     * 用户登录请求2
     */
    public static final String memberlogin2 = HTTPAPI + "/memberlogin2";
    //购买VIP会员
    public static final String buypaymoney = HTTPAPI + "/buypaymoney";
    //获取用户列表
    public static final String memberlist = HTTPAPI + "/memberlist";
    //消费榜
    public static final String goldcoinlist = HTTPAPI + "/goldcoinlist";
    //获取个人资料
    public static final String personal = HTTPAPI + "/personal";
    //获取动态
    public static final String trend = HTTPAPI + "/trend";
    //关注动态
    public static final String trends = HTTPAPI + "/trends";
    //获取个人资料
    public static final String member = HTTPAPI + "/member";
    //获取动态发布
    public static final String addtrend = HTTPAPI + "/addtrend";
    //获取直值消息提示
    public static final String configmsg = HTTPAPI + "/configmsg";
    //获取视频内容
    public static final String videolist = HTTPAPI + "/videolist";

    public static final String myvideolist = HTTPAPI + "/myvideolist";
    //获取发送验证码
    public static final String code = HTTPAPI + "/appsms";
    //获取验证码
    public static final String postcode = HTTPAPI + "/postcode";
    //添加视频
    public static final String addvideolist = HTTPAPI + "/addvideolist";
    //获取删除视频
    public static final String deletevideo = HTTPAPI + "/deletevideo";
    //获取图片列表
    public static final String mypicturelist = HTTPAPI + "/mypicturelist";
    //提交修改视频资料
    public static final String videolistedit = HTTPAPI + "/videolistedit";
    //评论发表
    public static final String addtrendcomment = HTTPAPI + "/addtrendcomment";
    //动态评论内容
    public static final String quertrendcomment = HTTPAPI + "/quertrendcomment";
    //删除自己的评论
    public static final String deletecomment = HTTPAPI + "/deletecomment";
    //金币查询
    public static final String goldcoin = HTTPAPI + "/goldcoin";
    //扣金币提交
    public static final String jbdsgoldcoin = HTTPAPI + "/jbdsgoldcoin";
    //开播记录
    public static final String livecreate = HTTPAPI + "/livecreate";
    //下播关闭记录
    public static final String livenextplay = HTTPAPI + "/livenextplay";
    //删除动态
    public static final String deletetrend = HTTPAPI + "/deletetrend";
    //获取金额列表
    public static final String moneylist = HTTPAPI + "/moneylist";
    //获取广告展示
    public static final String dsbanner = HTTPAPI + "/dsbanner";
    //礼物特效
    public static final String giftinfo = HTTPAPI + "/giftinfo";
    //支付请求API
    public static final String playmnet = HTTPAPI + "/playmnet";
    //获取好友
    public static final String invitefriends = HTTPAPI + "/invitefriends";
    /**
     * 获取支付配置
     */
    public static final String alipay = HTTPAPI + "/alipay";
    //写入系统订单记录
    public static final String addorderid = HTTPAPI + "/addorderid";
    //获取会员资料
    public static final String getmember = HTTPAPI + "/getmember";
    //获取广告内容 导航图标参数固定type=0
    public static final String navigation = HTTPAPI + "/navigation?type=0";
    //获取用户资料
    public static final String member2 = HTTPAPI + "/member2";
    //获取订单明细
    public static final String order = HTTPAPI + "/order";
    //获取扣除最底消费
    public static final String allcharge = HTTPAPI + "/allcharge";
    //跟据关建字搜索视频内容
    public static final String selectvideolist = HTTPAPI + "/selectvideolist";
    //获取会员资料
    public static final String listmember = HTTPAPI + "/listmember";
    //更新支付订单
    public static final String updateorderid = HTTPAPI + "/updateorderid";
    //获取我的消费记录
    public static final String querdetailedlist = HTTPAPI + "/querdetailedlist";
    //获取充值记录接口
    public static final String getorderidlist = HTTPAPI + "/getorderidlist";
    //获取模板随机消息
    public static final String message = HTTPAPI + "/message";
    //服务后台运行更新在线时间
    public static final String recordmember = HTTPAPI + "/recordmember";
    //获取VIP状态
    public static final String getvip = HTTPAPI + "/getvip";
    //提现申请
    public static final String addreward = HTTPAPI + "/addreward";
    //金额列表
    public static final String wmoney = HTTPAPI + "/wmoney";
    //判断帮定银行卡
    public static final String qrcode = HTTPAPI + "/qrcode";
    //保存二维码数据
    public static final String uploads3 = HTTPAPI + "/uploads3";
    //提现说明文本
    public static final String webview = HTTPAPI + "/webview";
    //获取版本号更新
    public static final String version = HTTPAPI + "/version";
    //提提余额
    public static final String reward = HTTPAPI + "/reward";
    //任务赚钱
    public static final String tasklist = HTTPAPI + "/tasklist";
    //每日签到
    public static final String qiandaoadd = HTTPAPI + "/qiandaoadd";
    //查看邀请好友
    public static final String memberparent = HTTPAPI + "/memberparent";
    //获取好友绑定
    public static final String binding_recommendation = HTTPAPI + "/binding_recommendation";
    //查是否绑定成功
    public static final String selecttasklist = HTTPAPI + "/selecttasklist";
    //随机匹配会员
    public static final String getrmbers = HTTPAPI + "/getrmbers";
    //获取认证状态
    public static final String getusername = HTTPAPI + "/getusername";
    //获取公会状态
    public static final String getrmard = HTTPAPI + "/getrmard";
    //提交服务实名认证
    public static final String attestationadd = HTTPAPI + "/attestationadd";
    //我的关注
    public static final String listowlist = HTTPAPI + "/listowlist";
    //我的粉丝
    public static final String myfollowlist = HTTPAPI + "/myfollowlist";
    //邀请注册
    public static final String register = HTTPAPI + "/register";
    //删除平台数据
    public static final String delcurriculum = HTTPAPI + "/delcurriculum";
    //查询金币
    public static final String gofollowlist = HTTPAPI + "/gofollowlist";
    //关注对方
    public static final String getfollowok = HTTPAPI + "/getfollowok";
    //搜索好会员
    public static final String activitselect = HTTPAPI + "/activitselect";
    //系统消息
    public static final String sysmessage = HTTPAPI + "/sysmessage";
    //音乐列表
    public static final String music = HTTPAPI + "/music";
    //新间展示
    public static final String showsysmessage = HTTPAPI + "/showsysmessage";
    //上传文件
    public static final String uploads2 = HTTPAPI + "/uploads2";

    //删除数居
    public static final String deleteimg = HTTPAPI + "/deleteimg";
    //编辑QQ微信
    public static final String editmember = HTTPAPI + "/editmember";
    //获取相册
    public static final String imglist = HTTPAPI + "/imglist";
    //注册会员
    public static final String regmember = HTTPAPI + "/regmember";
    /**
     * 注册会员
     */
    public static final String regmember2 = HTTPAPI + "/regmember2";
    //获取图片封面1
    public static final String perimgpic = HTTPAPI + "/perimgpic";
    //获取图片封面2
    public static final String perimgpic2 = HTTPAPI + "/perimgpic2";
    //加入随机匹配
    public static final String addrmbers = HTTPAPI + "/addrmbers";
    //退出匹配数据
    public static final String rmbersdelete = HTTPAPI + "/rmbersdelete";
    //上传头像
    public static final String uploads = HTTPAPI + "/uploads";
    //上传图片
    public static final String uploadimg = HTTPAPI + "/uploadimg";
    //擅长领域
    public static final String videotypeadd = HTTPAPI + "/videotypeadd";
    //上传身份证
    public static final String editusername = HTTPAPI + "/editusername";
    //意见反馈
    public static final String report = HTTPAPI + "/report";
    //红娘申请
    public static final String matchmaker = HTTPAPI + "/matchmaker";
    //找回密码
    public static final String Retrievepassword = HTTPAPI + "/Retrievepassword";
    //修改密码
    public static final String editpwd = HTTPAPI + "/editpwd";
    //打招听
    public static final String randomgreet = HTTPAPI + "/randomgreet";
    //支付金币
    public static final String jinbi = HTTPAPI + "/jinbi";
    //聊天窗口限制数
    public static final String dschat = HTTPAPI + "/dschat";
    //申请提现订单记录
    public static final String ordermoeny = HTTPAPI + "/ordermoeny";
    //余额动态
    public static final String getmoneylog = HTTPAPI + "/getmoneylog";
    //金币转换
    public static final String goldcoinconversion = HTTPAPI + "/goldcoinconversion";

    //分类内容
    public static final String videotitle = BuildConfig.HTTP_API + "/videotitle?type=" + (BuildConfig.TYPE - 1);
    //我的收藏
    public static final String collection = HTTPAPI + "/collection";
    //#查看我的/我查看的
    public static final String view_user = HTTPAPI + "/view_user";
    //#插入或更新查看记录
    public static final String addlistviewuser = HTTPAPI + "/addlistviewuser";
    //显示评论
    public static final String comment = HTTPAPI + "/comment";
    //删除评论内容
    public static final String delcomment = HTTPAPI + "/delcomment";
    //添加评论
    public static final String addcomment = HTTPAPI + "/addcomment";
    //查询会员信息回调
    public static String AddMore = HTTPAPI + "/AddMore";
    //获取聊天语音
    public static String liveRoom = HTTPAPI + "/LiveRoom";
    //直播源获取
    public static String Livevideo = HTTPAPI + "/Livevideo";
    //电影列表
    public static String dyvideolist = HTTPAPI + "/dyvideolist";
    //课程列表
    public static String curriculum = HTTPAPI + "/curriculum";
    //虚拟直播封面
    public static String Livevimage = HTTPAPI + "/Livevimage";
    //城市列表
    public static String citydate = HTTPAPI + "/citydate";
    //城市详情
    public static String citychat = HTTPAPI + "/citychat";
    //#获取配置
    public static String basicconfig = HTTPAPI + "/basicconfig";
    //注销帐号申请
    public static String logout = HTTPAPI + "/logout";
    //游戏列表
    public static String gamelist = HTTPAPI + "/gamelist";
    //我的游戏列表
    public static String gamelistAPI = HTTPAPI + "/mygamelist";
    //游戏写入
    public static String gameupdate = HTTPAPI + "/gameupdate";
    //游戏删除
    public static String gamedelete = HTTPAPI + "/gamedelete";
    //游戏分类
    public static String gametitle = HTTPAPI + "/gametitle";
    //检查用户权限
    public static String member_user = HTTPAPI + "/member_user";
    //游戏下单
    public static String gameorder = HTTPAPI + "/gameorder";
    //获取订单详情
    public static String gameActivityfinish = HTTPAPI + "/gameActivityfinish";
    //确认订单
    public static String gamebunttin = HTTPAPI + "/gamebunttin";
    //#接下/下单查看
    public static String GameActivitylist = HTTPAPI + "/GameActivitylist";
    //修改签名
    public static String toeditmember = HTTPAPI + "/toeditmember";
    //获取签名
    public static String getpesigntext = HTTPAPI + "/getpesigntext";
    public static String getpesigntext2 = HTTPAPI + "/getpesigntext2";
    //每天允许聊天N条
    public static String chatlimit = HTTPAPI + "/chatlimit";
    //获取在线男性用户
    public static String getlistmember = HTTPAPI + "/getlistmember";
    //开放平台授权登录
    public static String openwexin = HTTPAPI + "/openwexin";
    public static String openwxinlogin = HTTPAPI + "/openwxinlogin";
    //微信分享
    public final static String Sharewchat = HttpWEB + "/index.php/callback.index/Sharewchat";
    //微信支付请求
    public final static String wxapppay = HttpWEB + "/index.php/callback.WechatPay/wxapppay";
    //随机获取1条异常
    public final static String Randmember_User = HTTPAPI + "/randmemberuser";
    //背景音乐
    public final static String playmusic = HTTPAPI + "/playmusic";
    //自动登录IM
    public final static String WelcomeLogin = HTTPAPI + "/WelcomeLogin";
    //请求随机消息发送给
    public final static String activemessage = HTTPAPI + "/activemessage";
    //请求给自己发送
    public final static String getNotmessage = HTTPAPI + "/getNotmessage";
    //扣除金币
    public final static String chatjinbi = HTTPAPI + "/chatjinbi";
    //合作方式
    public final static String cooperationmode = HTTPAPI + "/cooperationmode";
    //获取IMsdkappid
    public final static String APISDKAPPID = HTTPAPI + "/sdkappid";
    //主播申请公会
    public final static String addrmard = HTTPAPI + "/addrmard";
    /**
     * 动态列表
     */
    public final static String playvideotrend = HTTPAPI + "/playvideotrend";
    /**
     * 视频电话
     */
    public final static String tovideocall = HTTPAPI + "/tovideocall";
    /**
     * 点赞
     */
    public final static String givelist = HTTPAPI + "/givelist";
    /**
     * 获取开放平台帐号
     */
    public final static String openweixi = HTTPAPI + "/openweixi";
    /**
     * 上用户定位消息
     */
    public final static String toAMapLocation = HTTPAPI + "/toAMapLocation";

    /**
     * 收费设置
     */
    public final static String chatmoney = HTTPAPI + "/chatmoney";
    /**
     * 收费设置
     */
    public final static String chatsetpost = HTTPAPI + "/chatsetpost";
    /**
     * 请求免费短视频资源
     */
    public final static String freevideo = HTTPAPI + "/freevideo";

    /**
     * 播放完成记录
     */
    public final static String senvideo = HTTPAPI + "/senvideo";

    /**
     * 微信开放平台
     */
    public final static String wxapi = "https://api.weixin.qq.com";

    /**
     * 隐私设置
     */
    public final static String videopush = HTTPAPI + "/videopush";

    /**
     * 隐私设置
     */
    public final static String videopushedit = HTTPAPI + "/videopushedit";

    /**
     * 视频置顶
     */
    public static String senvideotop = HTTPAPI + "/senvideotop";
    /**
     * 聚会活动
     */
    public static String partylist = HTTPAPI + "/partylist";
    /**
     * 聚会插入
     */
    public static String addpartyl = HTTPAPI + "/addpartyl";
    /**
     * 聚会消息
     */
    public static String partyfind = HTTPAPI + "/partyfind";

    /**
     * 报名聚会
     */
    public static String partyname = HTTPAPI + "/partyname";
    /**
     * 关闭活动
     */
    public static String patrfinish = HTTPAPI + "/patrfinish";

    /**
     * 获取报名列表
     */
    public static String partynameUser = HTTPAPI + "/partynameUser";
    /**
     * 删除聚会消息
     */
    public static String delpartyl = HTTPAPI + "/delpartyl";
    /**
     * 我参与的聚会
     */
    public static String partynamelist = HTTPAPI + "/partynamelist";

    /**
     * 聚会说明
     */
    public static String tips = HTTPAPI + "/tips.html";

    /**
     * 搜索用户
     */
    public static String userselect = HTTPAPI + "/userselect";

    /**
     * 分享海报
     */
    public static String share = HTTPAPI + "/share";

    /**
     * 获取比例调整
     */
    public static String getamountconfig = HTTPAPI + "/getamountconfig";

    /**
     * 设置比例调整
     */
    public static String editamountconfig = HTTPAPI + "/editamountconfig";

    /**
     * 获取用户
     */
    public static String posmember = HTTPAPI + "/posmember";

    /**
     * 绑定手机号
     */
    public static String bindphone = HTTPAPI + "/bindphone";


    /**
     * 支付请求列表
     */
    public static String playmoneylist = HttpWEB + "/index.php/callback.WechatPay/playmoneylist";

    /**
     * 发起支付操作
     */
    public static String playlist = HttpWEB + "/index.php/callback.WechatPay/playlist";
    /**
     * 查询支付成功
     */
    public static String querselect = HttpWEB + "/index.php/callback.WechatPay/querselect";


    /**
     * 字符串拼接
     *
     * @param map
     * @param TYPE
     * @return
     */
    public static String getMap(Map<String, String> map, int TYPE) {
        StringBuffer sb = new StringBuffer();
        int is = 0;
        switch (TYPE) {
            case 0:
            case 1:
                //第一种：
                for (String key : map.keySet()) {
                    if (is > 0) {
                        sb.append("&" + key + "=" + map.get(key));
                    } else {
                        sb.append(key + "=" + map.get(key));
                    }
                    is++;
                }
                break;
            case 2:
                //第二种
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (is > 0) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        sb.append(entry.getKey() + "=" + entry.getValue());
                    }
                    is++;
                }
                break;
            case 3:
                //第三种：
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (is > 0) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        sb.append(entry.getKey() + "=" + entry.getValue());
                    }
                    is++;
                }
                break;
        }

        return sb.toString();
    }

    public static String param(String url, Map<String, String> map) {
        return url + "?" + getMap(map, 3);
    }

    /**
     * 返回HashMap<String, String>
     *
     * @return
     */
    public static HashMap<String, String> getparams() {
        HashMap<String, String> params = new HashMap();
        params.put("userid", "2201");
        params.put("code", "123456");
        return params;
    }

    /**
     * 返回Map<String, String>
     *
     * @return
     */
    public static Map<String, String> getparams2() {
        Map<String, String> params = new HashMap();
        params.put("userid", "2201");
        params.put("code", "123456");
        return params;
    }

    private static String resultcode() {
        return "{\"resultcode\":\"101\",\"reason\":\"错误的请求KEY!\",\"result\":123,\"error_code\":10001}";
    }

    public void myTdate() {
        Tdate tdate = new Gson().fromJson(resultcode(), Tdate.class);
    }

    public class Tdate {
        String resultcode;
        String reason;
        String result;
        String error_code;

        public String getResultcode() {
            return resultcode;
        }

        public void setResultcode(String resultcode) {
            this.resultcode = resultcode;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getError_code() {
            return error_code;
        }

        public void setError_code(String error_code) {
            this.error_code = error_code;
        }
    }


    public static String getUrlMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();

        return s;
    }


}
