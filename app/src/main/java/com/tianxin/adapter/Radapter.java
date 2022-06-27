/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/26 0026
 */


package com.tianxin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.github.chrisbanes.photoview.PhotoView;
import com.tianxin.Module.api.message;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.serviceaccount;
import com.tianxin.Module.api.servicetitle;
import com.tianxin.adapter.Holder.itemvideoshow1;
import com.tianxin.adapter.Holder.itemvideoshow2;
import com.tianxin.adapter.Holder.itemvideoshow3;
import com.tianxin.adapter.Holder.itemvideoshow4;
import com.tianxin.adapter.Holder.listimageshow;
import com.tianxin.adapter.itemholder.ViewHolder01;
import com.tianxin.adapter.itemholder.ViewHolder02;
import com.tianxin.adapter.itemholder.ViewHolder03;
import com.tianxin.adapter.itemholder.ViewHolder04;
import com.tianxin.adapter.itemholder.ViewHolfaxing;
import com.tianxin.adapter.itemholder.bgviewpager_item;
import com.tianxin.adapter.itemholder.chat_itemholde;
import com.tianxin.adapter.itemholder.fviewholder;
import com.tianxin.adapter.itemholder.item_caht;
import com.tianxin.adapter.itemholder.item_caht_04;
import com.tianxin.adapter.itemholder.item_caht_05;
import com.tianxin.adapter.itemholder.item_caht_06;
import com.tianxin.adapter.itemholder.item_caht_07;
import com.tianxin.adapter.itemholder.item_cityshow;
import com.tianxin.adapter.itemholder.item_flowlayout;
import com.tianxin.adapter.itemholder.item_money_show;
import com.tianxin.adapter.itemholder.item_pay_list;
import com.tianxin.adapter.itemholder.item_select;
import com.tianxin.adapter.itemholder.item_text_title;
import com.tianxin.adapter.itemholder.item_title_game;
import com.tianxin.adapter.itemholder.item_trend_show;
import com.tianxin.adapter.itemholder.item_video_room;
import com.tianxin.adapter.itemholder.itemholderoNew;
import com.tianxin.adapter.itemholder.mvidewloder15;
import com.tianxin.adapter.itemholder.mvidewloder16;
import com.tianxin.adapter.itemholder.mvidewloder20;
import com.tianxin.adapter.itemholder.mvidewloder8;
import com.tianxin.adapter.itemholder.myIMGRES;
import com.tianxin.adapter.itemholder.myViewHolder;
import com.tianxin.adapter.itemholder.myViewHolder1;
import com.tianxin.adapter.itemholder.mydialoglisttrend;
import com.tianxin.adapter.itemholder.partyholder;
import com.tianxin.adapter.itemholder.sLiveTuivoiceRoom;
import com.tianxin.adapter.itemholder.itemholderoNe;
import com.tianxin.listener.Callback;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.Socket.data;
import com.tencent.opensource.model.tupianzj;
import com.tencent.opensource.model.videolist;
import com.tianxin.Module.api.videotitle;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.Util.ColorBg;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Config;
import com.tianxin.listener.OnItemChildClickListener;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.utils.HtmlTagHandler;
import com.tianxin.widget.FlowLayout;
import com.tencent.opensource.model.curriculum;
import com.tencent.opensource.model.getevaluate;
import com.tencent.opensource.model.getservice;
import com.tencent.opensource.model.imglist;
import com.tencent.opensource.model.member;
import com.tianxin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Radapter extends RecyclerView.Adapter {
    private static final String TAG = "Radapter";
    private final Context content;
    private final List<Object> list;
    private int TYPE;
    public static final int Randomgreet = 1000;
    public static final int mylikeyou = 1001;
    public static final int references = 1002;
    public static final int dialoglistmsg = 1003;
    public static final int activity_homepage = 1004;
    public static final int dialog_item_recy_view = 1005;
    public static final int activity_album = 1006;
    public static final int activity_servicetitle = 1007;
    public static final int fragment_presentation = 1008;
    public static final int Banner = 1009;
    public static final int Banner2 = 10091;
    public static final int Fragment_videotitle = 1010;
    public static final int activity_home = 1011;
    public static final int activity_sevaluate = 1012;
    public static final int activity_list_column = 1013;
    public static final int activity_list_column1 = 1014;
    public static final int activity_list_column2 = 1015;
    public static final int dialog_item_sicon = 1016;
    public static final int activity_mycurriculum = 1017;
    public static final int dialog_follow_view = 1018;
    public static final int activity_servicepj = 1019;
    public static final int activity_servicepj2 = 1020;
    public static final int dialog_item_show_home = 1021;
    public static final int per3 = 1022;
    public static final int per4 = 10222;

    public static final int Detailedlist = 1023;
    public static final int activity_select = 1024;
    public static final int One = 1025;
    public static final int fmessage = 1026; //朋友圈动态
    public static final int fmessage_recyview = 1027;  //读取模板样式
    public static final int fmessage_recyview2 = 1030; //模板样式
    public static final int search1 = 1028;
    public static final int search2 = 1029;
    public static final int Video_page = 1031;
    public static final int activity_img_cover = 1032;
    public static final int activity_img_page = 1033;
    public static final int fragmentfaxing = 1034;
    public static final int item_video_show1 = 100001;
    public static final int item_video_show2 = 100002;
    public static final int item_video_show3 = 100003;
    public static final int item_video_show4 = 100004;
    public static final int item_moneymsg = 100005;
    public static final int item_trend = 100006;
    public static final int followlist_item = 100008;
    public static final int item_moneymsg1 = 100009;
    public static final int LiveTuivoiceRoom = 100010;
    public static final int itemvideoroom = 100011;
    public static final int itemtexttitle = 100012;
    public static final int itemcityshow = 100013;
    public static final int item_vihome = 100014;
    public static final int gamelist = 100015;
    public static final int Activity_title = 100016;
    public static final int Activity_list_game = 100017;
    public static final int page3_4 = 100018;
    public static final int getOne1 = 100019;
    public static final int getOne2 = 100020;
    public static final int IMGRES = 100021;
    public static final int IMG22 = 100022;
    public static final int chatitemholde = 100023;
    public static final int one6 = 100024;
    public static final int party = 100025;
    public static final int party_item = 100026;
    public static final int party_item01 = 100027;
    public static final int party_item02 = 100028;
    public static final int party_item03 = 100029;
    public static final int activity_t = 100030;

    public static final int chat_item01 = 1;
    public static final int chat_item02 = 2;
    public static final int chat_item03 = 3;
    public static final int chat_item04 = 4;
    public static final int chat_item05 = 5;
    public static final int chat_item06 = 6;
    public static final int chat_item07 = 7;
    public static final int chat_item11 = 11;
    public static final int chat_item12 = 12;
    public static final int chat_item13 = 13;

    public static final int bgviewPager = 101;

    private int Chat_Socket;
    private Paymnets paymnets;
    private boolean delshow = false;
    private int style;
    private Callback callback;
    public AMapLocation aMapLocation; //用于判断定位服务
    private OnItemChildClickListener onItemChildClickListener;

    public boolean isDelshow() {
        return delshow;
    }

    public void setDelshow(boolean delshow) {
        this.delshow = delshow;
        notifyDataSetChanged();
    }

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
        notifyDataSetChanged();
    }

    public Radapter(Context content, List<Object> list, int TYPE) {
        this.content = content;
        this.list = list;
        this.TYPE = TYPE;
    }

    public Radapter(Context content, List<Object> list, int TYPE, Paymnets paymnets) {
        this.content = content;
        this.list = list;
        this.TYPE = TYPE;
        this.paymnets = paymnets;
    }

    public Radapter(Context content, List<Object> list, int TYPE, Callback callback) {
        this.content = content;
        this.list = list;
        this.TYPE = TYPE;
        this.callback = callback;
    }

    public Radapter(Context content, List<Object> list, int TYPE, int style, Paymnets paymnets) {
        this.content = content;
        this.list = list;
        this.TYPE = TYPE;
        this.style = style;
        this.paymnets = paymnets;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case bgviewPager:
                return new bgviewpager_item(content, parent);
            case activity_t:
                return new item_select(content, parent);
            case chat_item01:
                return new item_caht(content, parent);
            case chat_item02:
                return new item_caht(content, parent, 02);
            case chat_item03:
                return new item_caht(content, parent, 03, false);
            case chat_item04:
                return new item_caht_04(content, parent);
            case chat_item05:
                return new item_caht_05(content, parent);
            case chat_item06:
                return new item_caht_06(content, parent);
            case chat_item07:
                return new item_caht_07(content, parent);
            case party:
                //聚会列表
                return new item_flowlayout(content, parent);
            case one6:
                //聚会列表
                return new partyholder(content, parent);
            case chatitemholde:
                //等级设置
                return new chat_itemholde(content, parent);
            case IMG22:
                return new mydialoglisttrend(content, parent, false);
            case IMGRES:
                return new myIMGRES(content, parent, false);
            case Randomgreet:
                //一键打招呼
                return new myViewHolder(content, parent);
            case references:
                //邀请
                return new myViewHolder1(content);
            case dialoglistmsg:
                return new mvidewloder(LayoutInflater.from(content).inflate(R.layout.dialog_list_msg, parent, false));
            case activity_homepage:
                return new mvidewloder3(LayoutInflater.from(content).inflate(R.layout.dialog_list_pic, parent, false));
            case dialog_item_recy_view:
                return new mvidewloder4(LayoutInflater.from(content).inflate(R.layout.item_recy_item_con, parent, false));
            case activity_album:
                return new mvidewloder5(LayoutInflater.from(content).inflate(R.layout.item_list_imgs, parent, false));
            case activity_servicetitle:
                return new mvidewloder6(LayoutInflater.from(content).inflate(R.layout.item_serviectitle, parent, false));
            case fragment_presentation:
                return new mvidewloder7(LayoutInflater.from(content).inflate(R.layout.item_it_list, parent, false));
            case Banner:
            case activity_home:
            case dialog_item_sicon:
                //导航图标
                return new mvidewloder8(LayoutInflater.from(content).inflate(R.layout.indexlay_item, parent, false));
            case Banner2:
                //导航图标
                return new mvidewloder8(LayoutInflater.from(content).inflate(R.layout.banner_navg, parent, false));
            case Activity_title:
                //导航图标
                return new mvidewloder8(LayoutInflater.from(content).inflate(R.layout.banner_navg, null));
            case Fragment_videotitle:
            case dialog_follow_view:
                return new mvidewloder9(LayoutInflater.from(content).inflate(R.layout.item_conver, null));
            case activity_sevaluate:
                return new mvidewloder10(LayoutInflater.from(content).inflate(R.layout.item_conver_evaluate, parent, false));
            case activity_list_column:
            case activity_mycurriculum:
                return new mvidewloder11(LayoutInflater.from(content).inflate(R.layout.item_list_view1, parent, false));
            case activity_list_column1:
                return new mvidewloder11(LayoutInflater.from(content).inflate(R.layout.item_list_view2, parent, false));
            case activity_list_column2:
                return new mvidewloder12(LayoutInflater.from(content).inflate(R.layout.item_list_view3, parent, false));
            case activity_servicepj:
                return new mvidewloder13(LayoutInflater.from(content).inflate(R.layout.cool_item_search_grid_one, parent, false));
            case activity_servicepj2:
                return new mvidewloder14(LayoutInflater.from(content).inflate(R.layout.cool_item_search_grid_two, parent, false));
            case dialog_item_show_home:
                return new mvidewloder15(LayoutInflater.from(content).inflate(R.layout.dialog_item_show_home, parent, false));
            case per3:
            case activity_img_cover:
                //视频
                return new mvidewloder16(content, parent, viewType);
            case per4:
                //相册
                return new listimageshow(LayoutInflater.from(content).inflate(R.layout.list_image_show, null));
            case One:
                //首页列表
                return new mvidewloder20(LayoutInflater.from(content).inflate(R.layout.item_dy, parent, false));
            case Detailedlist:
                return new mvidewloder17(LayoutInflater.from(content).inflate(R.layout.moneys, null));
            case activity_select:
                return new mvidewloder18(LayoutInflater.from(content).inflate(R.layout.item_greet, parent, false));
            case fmessage:
                //最新动态列表
                return new ViewHolder01(ViewHolder01.view(content, parent));
            case fmessage_recyview:
            case fmessage_recyview2:
                return new ViewHolder02(ViewHolder02.view(content, parent));
            case search1:
                return new ViewHolder03(content, parent);
            case search2:
                return new ViewHolder04(content, parent);
            case Video_page:
                return new ViewHolder05(LayoutInflater.from(content).inflate(R.layout.item_tiktok_layout, parent, false));
            case activity_img_page:
                return new ViewHolder06(LayoutInflater.from(content).inflate(R.layout.item_view_img_page, parent, false));
            case fragmentfaxing:
                return new ViewHolfaxing(content);
            case item_video_show1:
                //首页
                return new itemvideoshow1(content);
            case item_video_show2:
                //同城
                return new itemvideoshow2(content);
            case item_video_show3:
                //附近
                return new itemvideoshow3(content);
            case item_video_show4:
                //首页视频通话列表
                return new itemvideoshow4(content);
            case item_moneymsg:
            case item_moneymsg1:
                //收益动态
                return new item_money_show(LayoutInflater.from(content).inflate(R.layout.item_money_msg, null));
            case item_trend:
                //评论列表
                return new item_trend_show(LayoutInflater.from(content).inflate(R.layout.item_trend, null));
            case followlist_item:
            case party_item01:
                //谁看过我 我看过谁
                return new fviewholder(content);
            case LiveTuivoiceRoom:
                return new sLiveTuivoiceRoom(LayoutInflater.from(content).inflate(R.layout.item_sex, parent, false));
            case itemvideoroom:
                return new item_video_room(LayoutInflater.from(content).inflate(R.layout.live_video_room, null));
            case itemtexttitle:
                return new item_text_title(LayoutInflater.from(content).inflate(R.layout.item_text_title, null));
            case itemcityshow:
                return new item_cityshow(LayoutInflater.from(content).inflate(R.layout.item_text_title_img, null));
            case gamelist:
            case Activity_list_game:
            case page3_4:
            case party_item:
            case party_item02:
                return new item_title_game(content, parent);
            case getOne1:
                //首页列表
                return new itemholderoNe(itemholderoNe.viewholder(content));
            case getOne2:
                //首页列表
                return new itemholderoNew(itemholderoNew.viewholder(content));
            case chat_item11:
                return new item_pay_list(content, parent);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Object itemobject = list.get(position);
        switch (getItemViewType(position)) {
            case bgviewPager:
                ((bgviewpager_item) holder).bind(itemobject, position, callback);
                break;
            case activity_t:
                ((item_select) holder).bind(itemobject, position, callback);
                break;
            case chat_item01:
            case chat_item02:
            case chat_item03:
                ((item_caht) holder).bind(itemobject, position, callback);
                break;
            case chat_item04:
                ((item_caht_04) holder).bind(itemobject, position, callback);
                break;
            case chat_item05:
                ((item_caht_05) holder).bind(itemobject, position, callback);
                break;
            case chat_item06:
                ((item_caht_06) holder).bind(itemobject, position, callback);
                break;
            case chat_item07:
                ((item_caht_07) holder).bind(itemobject, position, callback);
                break;
            case party:
                ((item_flowlayout) holder).bind(itemobject, position, callback);
                break;
            case one6:
                ((partyholder) holder).bind(content, itemobject, position, callback);
                break;
            case chatitemholde:
                ((chat_itemholde) holder).bind(itemobject, position, callback);
                break;
            case IMG22:
                ((mydialoglisttrend) holder).bind(itemobject, position, callback);
                break;
            case Randomgreet:
                ((myViewHolder) holder).bind(itemobject, position, paymnets);
                break;
            case references:
                ((myViewHolder1) holder).bind(itemobject, position, paymnets);
                break;
            case dialoglistmsg:
                message message = (com.tianxin.Module.api.message) itemobject;
                mvidewloder mvidewloder = (mvidewloder) holder;
                mvidewloder.title.setText(message.getMsg());
                if (paymnets != null) {
                    mvidewloder.title.setOnClickListener(new myOnClickListener2(message.getMsg()));
                }
                break;

            case activity_homepage:
                imglist perimg = (imglist) itemobject;
                mvidewloder3 mvidewloder3 = (mvidewloder3) holder;
                if (!perimg.getPic().isEmpty()) {
                    String presignedURL = perimg.getPic();
                    try {
                        presignedURL = perimg.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(perimg.getPic()) : perimg.getPic();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glideload.loadImage(mvidewloder3.icon, presignedURL, 4);
                    if (paymnets != null) {
                        mvidewloder3.itemView.setOnClickListener(new myOnClickListener(position));
                    }
                }
                mvidewloder3.icon.setVisibility(!perimg.getPic().isEmpty() ? VISIBLE : GONE);
                break;
            case dialog_item_recy_view:
                mvidewloder4 m4 = (mvidewloder4) holder;
                getservice getservice = (getservice) itemobject;
                String text = "<b><font color='red'>￥</font><myfont color='red' size='60px'>" + getservice.getMoney() + "</myfont></b>/" + getservice.getDuration() + "分钟  好评率<font color='000000'>100%</font>";
                Spanned spanned = Html.fromHtml(text, null, new HtmlTagHandler("myfont"));
                m4.textmsg.setText(spanned);
                m4.title.setText(getservice.getTitle());
                m4.count.setText("销量" + getservice.getVolume());
                if (paymnets != null) {
                    m4.itemView.setOnClickListener(new myOnClickListener(position));
                }
                break;
            case activity_album:
                mvidewloder5 m5 = (mvidewloder5) holder;
                m5.bind(itemobject, position);
                break;
            case activity_servicetitle:
                servicetitle as = (com.tianxin.Module.api.servicetitle) itemobject;
                mvidewloder6 m6 = (mvidewloder6) holder;
                m6.title.setText(as.getTitle());
                if (paymnets != null) {
                    m6.itemView.setOnClickListener(new myOnClickListener(position));

                }
                break;
            case fragment_presentation:
                mvidewloder7 m7 = (mvidewloder7) holder;
                serviceaccount se = (serviceaccount) itemobject;
                m7.title.setText(se.getTitle());
                m7.content.setText(se.getContent());
                if (!TextUtils.isEmpty(se.getIcon())) {
                    Glideload.loadImage(m7.icon, se.getIcon());

                }
                break;


            case Banner:
            case Banner2:
            case activity_home:
            case dialog_item_sicon:
                ((mvidewloder8) holder).bind(itemobject, position, paymnets);
                break;
            case Activity_title:
                ((mvidewloder8) holder).bind(itemobject, position, callback);
                break;
            case Fragment_videotitle:
            case dialog_follow_view:
                mvidewloder9 m9 = (mvidewloder9) holder;
                videotitle videotitle = (videotitle) itemobject;
                m9.title.setText(videotitle.getTitle());
                m9.tag.setText(videotitle.getTag());
                m9.icon.setVisibility(GONE);
                m9.tag.setVisibility(GONE);
                m9.tag.setVisibility(TYPE == dialog_follow_view ? GONE : VISIBLE);
                m9.layout.setBackground(content.getResources().getDrawable(videotitle.getSelect() == 1 ? R.drawable.diis_bg7 : R.drawable.dis_bg));
                if (paymnets != null) {
                    m9.layout.setOnClickListener(v -> paymnets.status(position));
                }
                break;
            case activity_sevaluate:
                mvidewloder10 m10 = (mvidewloder10) holder;
                getevaluate getevaluate = (getevaluate) itemobject;
                m10.bind(getevaluate, position);
                break;
            case activity_list_column:
            case activity_list_column1:
            case activity_mycurriculum:
                curriculum curriculum = (curriculum) itemobject;
                String ts1 = "<b><font color='red'>￥</font><myfont color='red' size='40'>" + curriculum.getPrice() + "</myfont>元</b>";
                Spanned spanneds1 = Html.fromHtml(ts1, null, new HtmlTagHandler("myfont"));
                mvidewloder11 m11 = (mvidewloder11) holder;
                m11.name.setText(curriculum.getTitle());
                m11.msg.setText(curriculum.getTag());
                m11.myoney.setText(spanneds1);
                m11.count.setText(String.format(content.getString(R.string.tv_msg6), "" + curriculum.getSellout()));
                m11.img_del.setVisibility(delshow ? VISIBLE : GONE);
                String path = curriculum.getPic();
                try {
                    path = curriculum.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(curriculum.getPic()) : curriculum.getPic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glideload.loadImage(m11.icon, path, 4);
                if (paymnets != null) {
                    m11.itemView.setOnClickListener(new myOnClickListener3(position));
                    m11.itemView.setOnLongClickListener(new myOnLongClickListener(position));
                }
                switch (curriculum.getStatus()) {
                    case 1:
                        m11.tv_switch.setVisibility(VISIBLE);
                        m11.tv_switch.setText(R.string.tv_msg66);
                        break;
                    case 2:
                        m11.tv_switch.setVisibility(VISIBLE);
                        m11.tv_switch.setText(R.string.tv_msg65);
                        break;
                    case 3:
                        m11.tv_switch.setVisibility(VISIBLE);
                        m11.tv_switch.setText(R.string.tv_msg81);
                        break;
                    default:
                        m11.tv_switch.setVisibility(GONE);
                        break;
                }

                break;
            case activity_list_column2:
                curriculum curriculum2 = (curriculum) itemobject;
                String ts = "<b><font color='red'>￥</font><myfont color='red' size='40'>" + curriculum2.getPrice() + "</myfont>元</b>";
                Spanned spanneds = Html.fromHtml(ts, null, new HtmlTagHandler("myfont"));
                mvidewloder12 m12 = (mvidewloder12) holder;
                m12.name.setText(curriculum2.getTitle());
                m12.myoney.setText(curriculum2.getMsg());
                m12.count.setText(spanneds);
                String path22 = curriculum2.getPic();
                try {
                    path22 = curriculum2.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(curriculum2.getPic()) : curriculum2.getPic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glideload.loadImage(m12.icon, path22, 6);
                Glideload.loadImage(m12.circleimageview, curriculum2.getPic());
                if (paymnets != null) {
                    m12.itemView.setOnClickListener(new myOnClickListener3(position));
                }
                break;
            case activity_servicepj:
                mvidewloder13 m13 = (mvidewloder13) holder;
                m13.item_search_one_con.setBackgroundColor(Color.parseColor(ColorBg.sColor()));
                break;
            case activity_servicepj2:
                mvidewloder14 m14 = (mvidewloder14) holder;
                m14.item_search_two_con.setBackgroundColor(Color.parseColor(ColorBg.sColor()));
                break;
            case dialog_item_show_home:
                mvidewloder15 m15 = (mvidewloder15) holder;
                m15.bind(content, list, itemobject, position, callback);
                break;
            case per3:
                videolist svideo = (videolist) itemobject;
                ((mvidewloder16) holder).bind(svideo, svideo.getMember(), position, callback);
                break;
            case per4:
                imglist imglist = (imglist) itemobject;
                listimageshow listimageshow = (listimageshow) holder;
                listimageshow.bind(imglist, position, callback);
                break;
            case activity_img_cover:
                ((mvidewloder16) holder).bind(itemobject, position, callback);
                break;
            case One:
                ((mvidewloder20) holder).bind(itemobject, position, callback);
                break;
            case Detailedlist:
                ((mvidewloder17) holder).bind(itemobject, position, paymnets);
                break;
            case activity_select:
                ((mvidewloder18) holder).bind(itemobject, position);
                break;
            case fmessage:
                //动态朋友圈
                ((ViewHolder01) holder).bind(content, itemobject, position, callback, fmessage, onItemChildClickListener);
                break;
            case fmessage_recyview:
                //绑定显示动态数据1
                ((ViewHolder02) holder).bind(content, list, itemobject.toString(), position, fmessage_recyview, onItemChildClickListener);
                break;
            case fmessage_recyview2:
                //绑定显示动态数据2
                ((ViewHolder02) holder).bind(content, list, itemobject.toString(), position, fmessage_recyview2, onItemChildClickListener);
                break;
            case search1:
                ((ViewHolder03) holder).bind(itemobject, position, callback);
                break;
            case search2:
                ((ViewHolder04) holder).bind(itemobject, position);
                break;
            case Video_page:
                ((ViewHolder05) holder).bind(itemobject, position);
                break;
            case activity_img_page:
                ((ViewHolder06) holder).bind(itemobject, position);
                break;
            case fragmentfaxing:
                ((ViewHolfaxing) holder).bind(itemobject, position);
                break;
            case item_video_show1:
                ((itemvideoshow1) holder).bind(itemobject, position, callback, aMapLocation);
                break;
            case item_video_show2:
                ((itemvideoshow2) holder).bind(itemobject, position, callback, aMapLocation);
                break;
            case item_video_show3:
                ((itemvideoshow3) holder).bind(itemobject, position, callback, aMapLocation);
                break;
            case item_video_show4:
                ((itemvideoshow4) holder).bind(itemobject, position, callback, aMapLocation);
                break;
            case item_moneymsg:
                ((item_money_show) holder).bind(itemobject, position, null);
                break;
            case item_moneymsg1:
                ((item_money_show) holder).bind(content, itemobject, position, null);
                break;
            case item_trend:
                ((item_trend_show) holder).bind(itemobject, position, callback);
                break;
            case followlist_item:
                ((fviewholder) holder).bind(itemobject, position, callback, delshow);
                break;
            case LiveTuivoiceRoom:
                ((sLiveTuivoiceRoom) holder).bind(itemobject, position, callback);
                break;
            case itemvideoroom:
                ((item_video_room) holder).bind(itemobject, position, callback);
                break;
            case itemtexttitle:
                ((item_text_title) holder).bind(itemobject, position, callback);
                break;
            case itemcityshow:
                ((item_cityshow) holder).bind(itemobject, position, callback);
                break;
            case gamelist:
                ((item_title_game) holder).bind(itemobject, position, callback);
                break;
            case Activity_list_game:
                ((item_title_game) holder).bind(content, itemobject, position, delshow, callback);
                break;
            case page3_4:
                ((item_title_game) holder).bind(content, itemobject, position, callback);
                break;
            case party_item:
                ((item_title_game) holder).bind(itemobject, position, callback, 0);
                break;
            case getOne1:
                ((itemholderoNe) holder).bind(content, itemobject, position, style, paymnets, aMapLocation);
                break;
            case getOne2:
                ((itemholderoNew) holder).bind(content, itemobject, position, style, paymnets, aMapLocation);
                break;
            case Radapter.IMGRES:
                ((myIMGRES) holder).bind(itemobject, position, null);
                break;
            case party_item01:
                ((fviewholder) holder).bind(itemobject, position, callback);
                break;
            case party_item02:
                ((item_title_game) holder).bind(itemobject, position, callback, true);
                break;
            case chat_item11:
                ((item_pay_list) holder).bind(itemobject, position, callback);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void notifyData(int TYPE) {
        this.TYPE = TYPE;
        notifyDataSetChanged();
    }

    public void removenotifyDate(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        this.onItemChildClickListener = onItemChildClickListener;
    }

    public class myOnClickListener implements View.OnClickListener {
        int position;

        public myOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            paymnets.status(position);
        }
    }

    class myOnClickListener3 implements View.OnClickListener {
        int position;

        public myOnClickListener3(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            paymnets.onItemClick(v, position);

        }
    }

    class myOnClickListener2 implements View.OnClickListener {
        String msg;

        public myOnClickListener2(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View v) {
            paymnets.success(msg);

        }
    }

    class myOnLongClickListener implements View.OnLongClickListener {
        int position;

        public myOnLongClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View v) {
            paymnets.status(position);
            return true;
        }


    }

    class mvidewloder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        public mvidewloder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class mvidewloder3 extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;

        public mvidewloder3(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class mvidewloder4 extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.textmsg)
        TextView textmsg;
        @BindView(R.id.sned)
        TextView sned;
        @BindView(R.id.count)
        TextView count;

        public mvidewloder4(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder5 extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView iv_image;

        public mvidewloder5(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        public void bind(Object itemobject, int position) {
            imglist peimg = (imglist) itemobject;
            String path = peimg.getPic();
            try {
                path = peimg.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(peimg.getPic()) : peimg.getPic();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Glideload.loadImage(iv_image, path, 8);
            if (paymnets != null) {
                itemView.setOnClickListener(new myOnClickListener(position));
            }
        }
    }

    class mvidewloder6 extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.msg)
        TextView msg;

        public mvidewloder6(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder7 extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.icon)
        ImageView icon;

        public mvidewloder7(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder9 extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.tag)
        TextView tag;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.layout)
        LinearLayout layout;

        public mvidewloder9(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder10 extends RecyclerView.ViewHolder {
        @BindView(R.id.circleimageview)
        ImageView circleimageview;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.conver)
        TextView conver;
        @BindView(R.id.layout)
        LinearLayout layout;
        @BindView(R.id.flowLayout)
        FlowLayout flowLayout;

        public mvidewloder10(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        public void bind(getevaluate getevaluate, int position) {
            member member1 = getevaluate.getMember();
            String typetitle = getevaluate.getEvaltitle();
            flowLayout.removeAllViews();
            if (!TextUtils.isEmpty(typetitle)) {
                String[] split = typetitle.split(",");
                for (String s : split) {
                    if (TextUtils.isEmpty(s)) {
                        continue;
                    }
                    TextView tv = (TextView) LayoutInflater.from(content).inflate(R.layout.item_flowlayout, flowLayout, false);
                    tv.setText(s);
                    tv.setTextColor(content.getResources().getColor(R.color.colorAccent));
                    tv.setBackground(content.getResources().getDrawable(R.drawable.diis_bg7));
                    tv.setTextSize(12);
                    flowLayout.addView(tv);
                }
            }
            if (!TextUtils.isEmpty(getevaluate.getDatetime())) {
                String s = Config.stampToDate(getevaluate.getDatetime(), 0);
                time.setText(String.format("%s 购买了服务", s));
            }
            layout.setVisibility(TextUtils.isEmpty(getevaluate.getContent()) ? VISIBLE : GONE);
            conver.setText(TextUtils.isEmpty(getevaluate.getContent()) ? "" : getevaluate.getContent());
            conver.setVisibility(TextUtils.isEmpty(getevaluate.getContent()) ? GONE : VISIBLE);
            if (member1 != null) {
                name.setText(!TextUtils.isEmpty(member1.getTvname()) ? member1.getTvname() : member1.getTruename());
                String p1 = member1.getPicture();
                try {
                    p1 = member1.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(member1.getPicture()) : member1.getPicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glideload.loadImage(circleimageview, p1);
            }
            if (paymnets != null) {
                itemView.setOnClickListener(new myOnClickListener(position));
            }
        }
    }

    class mvidewloder11 extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.msg)
        TextView msg;
        @BindView(R.id.myoney)
        TextView myoney;
        @BindView(R.id.count)
        TextView count;
        @BindView(R.id.img_del)
        ImageView img_del;
        @BindView(R.id.tv_switch)
        TextView tv_switch;

        public mvidewloder11(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder12 extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.circleimageview)
        ImageView circleimageview;
        @BindView(R.id.myoney)
        TextView myoney;
        @BindView(R.id.count)
        TextView count;


        public mvidewloder12(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder13 extends RecyclerView.ViewHolder {

        @BindView(R.id.item_search_one_nums)
        TextView item_search_one_nums;
        @BindView(R.id.item_search_one_con)
        TextView item_search_one_con;

        public mvidewloder13(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder14 extends RecyclerView.ViewHolder {

        @BindView(R.id.item_search_two_con)
        TextView item_search_two_con;

        public mvidewloder14(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }
    }

    class mvidewloder17 extends RecyclerView.ViewHolder {
        @BindView(R.id.bgmplay)
        LinearLayout bgmplay;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;

        public mvidewloder17(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        public void bind(Object itemobject, int position, Paymnets paymnets) {
            moneylist moneylist = (com.tianxin.Module.api.moneylist) itemobject;
            tv1.setText(String.valueOf(moneylist.getGive()));
            tv3.setText(moneylist.getMoney() + "元");
            if (delshow && style == position) {
                //选中
                bgmplay.setBackgroundResource(R.drawable.acitvity015);
                tv1.setTextColor(content.getResources().getColor(R.color.colorAccent));
                tv2.setTextColor(content.getResources().getColor(R.color.colorAccent));
                tv3.setTextColor(content.getResources().getColor(R.color.colorAccent));
            } else {
                //未先中
                bgmplay.setBackgroundResource(R.drawable.acitvity016);
                tv1.setTextColor(content.getResources().getColor(R.color.homeback));
                tv2.setTextColor(content.getResources().getColor(R.color.c_font_3));
                tv3.setTextColor(content.getResources().getColor(R.color.c_font_3));
            }
            itemView.setOnClickListener(v -> {
                style = position;
                delshow = true;
                notifyDataSetChanged();
                if (paymnets != null) {
                    paymnets.onClick(moneylist);
                }
            });

        }
    }

    class mvidewloder18 extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address)
        TextView address;

        public mvidewloder18(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        public void bind(Object item, int position) {
            member member = (com.tencent.opensource.model.member) item;
            if (member == null) {
                return;
            }
            if (!TextUtils.isEmpty(member.getPicture())) {
                String spath = member.getPicture();
                try {
                    spath = member.getTencent() == 1 ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glideload.loadImage(icon, spath);
            } else {
                Glideload.loadImage(icon, member.getSex() == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
            }
            name.setText(TextUtils.isEmpty(member.getTvname()) ? member.getTruename() : member.getTvname());
            address.setVisibility(GONE);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(content, activity_picenter.class);
                    intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
                    content.startActivity(intent);
                }
            });
        }
    }

    class mvidewloder19 extends RecyclerView.ViewHolder {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.stitle)
        TextView stitle;

        public mvidewloder19(View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);
        }

        public void bind() {
            Glideload.loadImage(icon, "GameActivityfinish");
            stitle.setText("1111");
        }
    }

    /**
     * 视频播放列表
     */
    class ViewHolder05 extends RecyclerView.ViewHolder {
        private final ImageView mThumb;
        private final ImageView mticon;
        private final ImageView iv_msg;
        private final ImageView mPlay;
        private final LinearLayout relayout1;
        private final LinearLayout relayout2;
        private final LinearLayout relayout3;
        private final TextView tv1;
        private final TextView tv2;
        private final TextView tv3;
        private final TextView mTitle;
        private final TextView mMarquee;
        private final PLVideoView mVideoView;

        public ViewHolder05(View itemview) {
            super(itemview);
            mVideoView = itemview.findViewById(R.id.video_view);
            mThumb = itemview.findViewById(R.id.mThumb);
            mticon = itemview.findViewById(R.id.mticon);
            relayout1 = itemview.findViewById(R.id.relayout1);
            relayout2 = itemview.findViewById(R.id.relayout2);
            relayout3 = itemview.findViewById(R.id.relayout3);
            tv1 = itemview.findViewById(R.id.tv1);
            tv2 = itemview.findViewById(R.id.tv2);
            tv3 = itemview.findViewById(R.id.tv3);
            mTitle = itemview.findViewById(R.id.mTitle);
            mMarquee = itemview.findViewById(R.id.mMarquee);
            iv_msg = itemview.findViewById(R.id.iv_msg);
            mPlay = itemview.findViewById(R.id.mPlay);
        }

        public void bind(Object itemobject, int position) {
            videolist video = (videolist) itemobject;
            Glideload.loadImage(mThumb, video.getBigpicurl());
            Glideload.loadImage(mticon, video.getPicurl());
            tv1.setText("11");
            tv2.setText("22");
            tv3.setText("33");
            mTitle.setText(video.getTitle());

        }
    }

    class ViewHolder06 extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_view)
        PhotoView photoView;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.title)
        TextView title;

        public ViewHolder06(View itemview) {
            super(itemview);
            ButterKnife.bind(this, itemview);
        }

        public void bind(Object object, int position) {
            tupianzj tupianzj = (tupianzj) object;
            Glideload.loadImage(photoView, tupianzj.getPic());
            title.setText(tupianzj.getTitle());
            progressBar.setVisibility(GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (TYPE) {
            case party_item03:
                return ((data) list.get(position)).getType();
        }
        return TYPE;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        //是否可见
        int adapterPosition = holder.getAdapterPosition();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //可见可用
        int adapterPosition = holder.getAdapterPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
