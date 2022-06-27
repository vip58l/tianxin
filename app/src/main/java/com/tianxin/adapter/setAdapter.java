package com.tianxin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.adapter.itemholder.bookitemshujia;
import com.tianxin.adapter.itemholder.dyv6;
import com.tianxin.adapter.itemholder.geton2;
import com.tianxin.adapter.itemholder.ysbaseadapter;
import com.tianxin.adapter.setAdapterholder.item_view_one;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.item;
import com.tianxin.Module.api.goldcoin;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.order;
import com.tianxin.Module.api.order_moeny;
import com.tianxin.Module.api.wmoney;
import com.tianxin.Module.api.shouchangmember;
import com.tianxin.R;
import com.tianxin.activity.activity_svip;
import com.tianxin.Util.ColorBg;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Config;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.getlist.HotRooms;
import com.tianxin.utils.HtmlTagHandler;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.gethelp;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.orderdetailed;
import com.tencent.opensource.model.videotype;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.utils.GildeRoundTransform;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * listview 适配器
 */
public class setAdapter extends BaseAdapter {
    private final String[] arraylist;
    private final Context context;
    private final List<item> list;
    public static final int index = 1000;
    public static final int one = 1001;
    public static final int two = 1002;
    public static final int video = 1003;
    public static final int img = 1004;
    public static final int dy = 1008;
    public static final int sbaseadapter = 1005; //街拍视频列表
    public static final int getOne2 = 1006; //首页交友item
    public static final int Recharge = 1007;
    public static final int Search1 = 1009;
    public static final int Search2 = 1010;
    public static final int booklist = 1011;
    public static final int shouchang = 1012;
    public static final int svip = 1013;
    public static final int svip2 = 1014;
    public static final int Recharge2 = 1015;
    public static final int Recharge3 = 10021;
    public static final int followlist = 1016;
    public static final int wealth = 1017;
    public static final int Withdrawals = 1018;
    public static final int ordermoeny = 1019;
    public static final int getOne3 = 1020;
    public AMapLocation samapLocation; //用于判断定位服务
    public int ii;
    public static String TAG = "setAdapter";
    public Paymnets paymnet;
    private MyOpenhelper myOpenhelper;
    private UserInfo userInfo;

    public setAdapter(Context context, List<item> list) {
        this.context = context;
        this.list = list;
        this.userInfo = UserInfo.getInstance();
        this.myOpenhelper = MyOpenhelper.getOpenhelper();
        this.arraylist = context.getResources().getStringArray(R.array.title);
    }

    public setAdapter(Context context, List<item> list, AMapLocation samapLocation) {
        this.context = context;
        this.list = list;
        this.samapLocation = samapLocation;
        this.userInfo = UserInfo.getInstance();
        this.myOpenhelper = MyOpenhelper.getOpenhelper();
        this.arraylist = context.getResources().getStringArray(R.array.title);
    }

    public setAdapter(Context context, List<item> list, int type) {
        this.context = context;
        this.list = list;
        this.ii = type;
        this.userInfo = UserInfo.getInstance();
        this.myOpenhelper = MyOpenhelper.getOpenhelper();
        this.arraylist = context.getResources().getStringArray(R.array.title);
    }

    public setAdapter(Context context, List<item> list, int type, Paymnets paymnets) {
        this.context = context;
        this.list = list;
        this.ii = type;
        this.paymnet = paymnets;
        this.userInfo = UserInfo.getInstance();
        this.myOpenhelper = MyOpenhelper.getOpenhelper();
        this.arraylist = context.getResources().getStringArray(R.array.title);
    }

    public void setSamapLocation(AMapLocation samapLocation) {
        this.samapLocation = samapLocation;
        notifyDataSetChanged();
    }

    public void setPaymnet(Paymnets paymnet) {
        this.paymnet = paymnet;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        item item = list.get(position);
        int type = getItemViewType(position);
        switch (type) {
            case index:
                if (view == null) {
                    view = View.inflate(context, R.layout.item_vihome_list, null);
                    view.setTag(new listviewite(view));
                }
                listviewite hoder = (listviewite) view.getTag();
                showview(hoder, position);
                break;
            case dy:
                //小视频
                if (view == null) {
                    view = dyv6.view(context);
                    view.setTag(new dyv6(view));
                }
                dyv6 dyv6 = (dyv6) view.getTag();
                dyv6.bind(item.object);
                break;
            case one:
                if (view == null) {
                    view = View.inflate(context, R.layout.item_fl, null);
                    view.setTag(new one(view));
                }
                one one = (setAdapter.one) view.getTag();
                one.bind(item.object);
                break;
            case two:
             /*   if (view == null) {
                    view = View.inflate(context, R.layout.item_fl, null);
                    view.setTag(new two(view));
                }*/
                if (view == null) {
                    view = item_view_one.view(context);
                    item_view_one item_view_one = new item_view_one(view);
                    view.setTag(item_view_one);
                }
                item_view_one itemViewOne = (item_view_one) view.getTag();
                itemViewOne.bind(item.object);
                break;
            case video:
                if (view == null) {
                    view = ysbaseadapter.view(context);
                    view.setTag(new ysbaseadapter(view));
                }
                ((ysbaseadapter) view.getTag()).bind(item.object);
                break;
            case img:
                if (view == null) {
                    view = ysbaseadapter.view(context);
                    view.setTag(new ysbaseadapter(view));
                }
                ((ysbaseadapter) view.getTag()).bind2(item.object);
                break;
            case sbaseadapter:
                if (view == null) {
                    view = ysbaseadapter.view(context);
                    view.setTag(new ysbaseadapter(view));
                }
                ((ysbaseadapter) view.getTag()).bind3(item.object);
                break;
            case getOne2:
                //交友
                if (view == null) {
                    view = geton2.inflater(context);
                    geton2 geton2 = new geton2(view);
                    view.setTag(geton2);
                }
                ((geton2) view.getTag()).bind(context, item.object, position, ii, paymnet, samapLocation);
                break;
            case Recharge:
                if (view == null) {
                    view = View.inflate(context, R.layout.item_detailswater, null);
                    view.setTag(new rechagre(view));
                }
                rechagre r1 = (rechagre) view.getTag();
                r1.bind(item.object, 1);
                break;
            case Recharge2:
                if (view == null) {
                    view = View.inflate(context, R.layout.item_detailswater, null);
                    view.setTag(new rechagre(view));
                }
                rechagre r2 = (rechagre) view.getTag();
                r2.bind(item.object, 2);
                break;
            case Recharge3:
                if (view == null) {
                    view = View.inflate(context, R.layout.item_detailswater, null);
                    view.setTag(new rechagre(view));
                }
                rechagre r3 = (rechagre) view.getTag();
                r3.bind(item.object);
                break;

            case Search1:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.cool_item_search_grid_one, null);
                    view.setTag(new SearchActivity1(view));
                }
                SearchActivity1 ty1 = (SearchActivity1) view.getTag();
                ty1.bind(item.object, position);
                break;
            case Search2:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.cool_item_search_grid_two, null);
                    view.setTag(new SearchActivity2(view));
                }
                SearchActivity2 ty2 = (SearchActivity2) view.getTag();
                ty2.bind(item.object);
                break;
            case booklist:
                if (view == null) {
                    view = bookitemshujia.view(context);
                    view.setTag(new bookitemshujia(view));
                }
                bookitemshujia bookitemshujia = (bookitemshujia) view.getTag();
                bookitemshujia.bind(item.object);
                break;
            case shouchang:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_mxl, null);
                    view.setTag(new myshouchang(view));
                }
                myshouchang myshouchang = (myshouchang) view.getTag();
                myshouchang.bind(item.object);
                break;
            case svip:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_barbott, null);
                    view.setTag(new svips(view));
                }
                svips svips = (svips) view.getTag();
                svips.bind(item.object);

                break;
            case svip2:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_dialog_svip, null);
                    view.setTag(new svip2(view));
                }
                svip2 svip2 = (svip2) view.getTag();
                svip2.bind(item.object, position);
                break;
            case setAdapter.followlist:
                //我的关注列表
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.followlist_item, null);
                    view.setTag(new gzlist(view));
                }
                gzlist foll = (gzlist) view.getTag();
                foll.bind(item.object, position);
                break;
            case wealth:
                //财富列表
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.followlist_item, null);
                    view.setTag(new gzlist(view));
                }
                gzlist a1 = (gzlist) view.getTag();
                a1.bind2(item.object, position);
                break;
            case Withdrawals:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_money, null);
                    view.setTag(new item_money(view));
                }
                item_money itemMoney = (item_money) view.getTag();
                itemMoney.bind(item.object, position);
                break;
            case ordermoeny:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_money_msg, null);
                    view.setTag(new money_msg(view));
                }
                money_msg moneyMsg = (money_msg) view.getTag();
                moneyMsg.bind(item.object);
                break;
            case getOne3:
                if (view == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.item_view_oner, null);
                    view.setTag(new item_view_one3(view));
                }
                item_view_one3 viewOne3 = (item_view_one3) view.getTag();
                viewOne3.bind(item.object);
                break;

        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    /**
     * 给上面变量定义一个有参构造
     */
    public void setClickPosition(int itemview) {
        this.ii = itemview;
    }

    private void showview(listviewite hoder, int position) {
        hoder.tag_name.setText(arraylist[position]);
        hoder.tag_ms.setText("更多" + arraylist[position]);
    }


    /********************** 模板组件处理 ***********************************/

    class listviewite {
        @BindView(R.id.tag_name)
        TextView tag_name;
        @BindView(R.id.tag_ms)
        TextView tag_ms;
        @BindView(R.id.item_home_top)
        LinearLayout item_home_top;
        @BindView(R.id.item_home_bottom)
        LinearLayout item_home_bottom;

        public listviewite(View view) {
            ButterKnife.bind(this, view);
        }
    }


    class one {
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.zbmsg)
        TextView zbmsg;

        public one(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(Object object) {
            HotRooms hotRooms = (HotRooms) object;
            Glide.with(context).load(hotRooms.getBigImageOriginal()).apply(GildeRoundTransform.getoptions()).dontAnimate().into(image);
            tv_name.setText(hotRooms.getNickName());
            tv_type.setText(hotRooms.getSign());
        }
    }

    class two {
        @BindView(R.id.imgbg)
        ImageView imgbg;
        @BindView(R.id.bg_shade_bom)
        ImageView bg_shade_bom;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_type)
        TextView tv_type;
        @BindView(R.id.zbmsg)
        TextView zbmsg;

        public two(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class rechagre {
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;

        public rechagre(View itemview) {
            ButterKnife.bind(this, itemview);
        }

        public void bind(Object object) {
            order order = (order) object;
            tv1.setText(String.format("%s %s 元", order.getMsg(), order.getMoney(), context.getString(R.string.tv_msg182)));
            if (!TextUtils.isEmpty(order.getDatetime())) {
                tv2.setText(Config.stampToDate(order.getDatetime()));
            }
            if (order.getOk() == 1) {
                String namess = order.getGive();
                if (TextUtils.isEmpty(namess)) {
                    namess = String.format("+%s%s", order.getChangecurrency(), context.getString(R.string.tv_msg181));
                } else {
                    namess = String.format("+%s%s", namess, context.getString(R.string.tv_msg181));
                }
                String format = String.format("%s", order.getVip() == 1 ? context.getString(R.string.tv_msg233) : namess);
                tv3.setText(format);
                tv3.setTextColor(context.getResources().getColor(R.color.c_fu));
                tv3.setPaintFlags(tv3.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            } else {
                tv3.setText(R.string.tv_msg183);
                tv3.setPaintFlags(tv3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tv3.setTextColor(context.getResources().getColor(R.color.home));
            }
        }

        public void bind(Object object, int TYPE) {
            orderdetailed orderdetailed = (orderdetailed) object;
            tv1.setText(orderdetailed.getMsg());
            if (!TextUtils.isEmpty(orderdetailed.getDatetime())) {
                tv2.setText(Config.stampToDate(orderdetailed.getDatetime()));
            }
            if (TYPE == Constants.TENCENT) {
                tv3.setText(String.format("+%s币", orderdetailed.getMoney()));
                tv3.setTextColor(context.getResources().getColor(R.color.c_fu));

            } else {
                tv3.setText(String.format("-%s币", orderdetailed.getPaymoney()));
                tv3.setTextColor(context.getResources().getColor(R.color.half_transparent));
            }
        }


    }

    class SearchActivity1 {
        @BindView(R.id.item_search_one_nums)
        TextView item_search_one_nums;
        @BindView(R.id.item_search_one_con)
        TextView item_search_one_con;

        public SearchActivity1(View itemview) {
            ButterKnife.bind(this, itemview);
        }


        public void bind(Object object, int position) {
            String name = (String) object;
            int itempage = position++;
            item_search_one_con.setText(name);
            item_search_one_nums.setText(String.valueOf(itempage));
            item_search_one_nums.setBackgroundColor(Color.parseColor(ColorBg.sColor(position)));
        }
    }

    class SearchActivity2 {
        @BindView(R.id.item_search_two_con)
        TextView item_search_two_con;

        public SearchActivity2(View itemview) {
            ButterKnife.bind(this, itemview);

        }


        public void bind(Object object) {
            String name1 = (String) object;
            item_search_two_con.setText(name1);
            item_search_two_con.setBackgroundColor(Color.parseColor(ColorBg.sColor()));
        }
    }

    class myshouchang {
        ImageView icon;
        TextView title;
        TextView title1;
        TextView title2;

        public myshouchang(View itemview) {
            icon = itemview.findViewById(R.id.icon);
            title = itemview.findViewById(R.id.title);
            title1 = itemview.findViewById(R.id.title1);
            title2 = itemview.findViewById(R.id.title2);
        }

        public void bind(Object object) {
            shouchangmember shou = (shouchangmember) object;
            ImageLoadHelper.glideShowCornerImageWithUrl(context, shou.getPic(), icon);
            title.setText(shou.getUsername());
            title1.setText(shou.getTruename());
            title2.setText(shou.getTitle());
        }
    }

    class svips {

        ImageView icon;
        TextView tv_name;

        public svips(View itemview) {
            this.icon = itemview.findViewById(R.id.icon);
            this.tv_name = itemview.findViewById(R.id.tv_name);
        }

        public void bind(Object object) {
            activity_svip.bgs bgs = (activity_svip.bgs) object;
            Glide.with(context).load(bgs.img).dontAnimate().into(icon);
            tv_name.setText(bgs.title);
        }
    }

    class svip2 {
        TextView tv1, tv2, tv3;
        View itemview;

        public svip2(View itemview) {
            tv1 = itemview.findViewById(R.id.tv1);
            tv2 = itemview.findViewById(R.id.tv2);
            tv3 = itemview.findViewById(R.id.tv3);
            this.itemview = itemview;
        }

        public void bind(Object object, int position) {
            moneylist moneylist = (moneylist) object;
            tv1.setText(String.valueOf(moneylist.getGive()));
            tv3.setText(moneylist.getMoney() + "元");
            itemview.setBackgroundResource(ii == position ? R.drawable.money_bgs : R.drawable.momey_bg);
        }
    }

    class gzlist {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_map)
        TextView tv_map;
        @BindView(R.id.tv_gx)
        TextView tv_gx;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.tv_vip)
        TextView tv_vip;
        @BindView(R.id.lay_money)
        LinearLayout lay_money;
        @BindView(R.id.tv_money)
        TextView tv_money;

        public gzlist(View itemview) {
            ButterKnife.bind(this, itemview);

        }

        public void bind(Object object, int position) {
            followlist follw = (followlist) object;
            member member3 = follw.getMember();
            List<videotype> videotype1 = member3.getVideotype();
            StringBuffer sbb = new StringBuffer();

            for (int i = 0; i < videotype1.size(); i++) {
                if (i > 5) {
                    break;
                }
                videotype videotype = videotype1.get(i);
                sbb.append(videotype.getTitle() + " ");
            }

            if (member3 != null) {
                tv_title.setText(member3.getTruename());

                tv_map.setText(!TextUtils.isEmpty(member3.getProvince()) ? member3.getProvince().substring(0, 2) + "." + member3.getCity().substring(0, 2) : "");

                if (BuildConfig.TYPE == 1) {
                    tv_gx.setText(follw.getMsg());
                } else {
                    tv_gx.setText(!TextUtils.isEmpty(sbb.toString()) ? sbb.toString() : follw.getMsg());
                }
                tv_gx.setVisibility(View.VISIBLE);
                lay_money.setVisibility(View.GONE);
                tv_vip.setVisibility(member3.getVip() == 1 ? View.VISIBLE : View.GONE);
                if (TextUtils.isEmpty(member3.getPicture())) {
                    GlideEngine.loadImage(icon, member3.getSex() == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
                } else {
                    String url1 = member3.getPicture();
                    try {
                        url1 = member3.getTencent() == 1 ? DemoApplication.presignedURL(member3.getPicture()) : member3.getPicture();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    GlideEngine.loadImage(icon, url1);
                }
            }
        }

        public void bind2(Object object, int position) {
            goldcoin gol = (goldcoin) object;
            member ber1 = gol.getMember();
            if (ber1 != null) {
                tv_title.setText(ber1.getTruename());
                tv_map.setText(!TextUtils.isEmpty(ber1.getProvince()) ? ber1.getProvince().replace("省", "") : "");
                tv_gx.setText(!TextUtils.isEmpty(gol.getMsg()) ? gol.getMsg() : "");
                tv_gx.setVisibility(View.GONE);
                lay_money.setVisibility(View.VISIBLE);
                int getmoney = ii > 1 ? gol.getBalance() : gol.getMoney();
                tv_money.setText(String.valueOf(getmoney));
                if (TextUtils.isEmpty(ber1.getPicture())) {
                    Glide.with(context).load(ber1.getSex() == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose).into(icon);
                } else {
                    String p1 = ber1.getPicture();
                    try {
                        p1 = ber1.getTencent() == 1 ? DemoApplication.presignedURL(ber1.getPicture()) : ber1.getPicture();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Glide.with(context).load(p1).into(icon);
                }


            }
        }
    }

    class item_money {
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.msg)
        TextView msg;
        View itemview;

        public item_money(View itemview) {
            ButterKnife.bind(this, itemview);
            this.itemview = itemview;
        }

        public void bind(Object object, int position) {
            wmoney wmoney = (wmoney) object;
            itemview.setBackgroundResource(ii == position ? R.drawable.money_bgs : R.drawable.momey_bg);
            money.setText(String.format("%s元", wmoney.getMoney()));
            if (!TextUtils.isEmpty(wmoney.getMsg())) {
                msg.setText(wmoney.getMsg());
            } else {
                msg.setVisibility(View.GONE);
            }
        }
    }

    class money_msg {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.datetime)
        TextView datetime;
        @BindView(R.id.state)
        TextView state;

        public money_msg(View itemview) {
            ButterKnife.bind(this, itemview);
        }

        public void bind(Object object) {
            order_moeny t = (order_moeny) object;
            if (!TextUtils.isEmpty(t.getSexplain())) {
                Spanned spanned = Html.fromHtml(String.format("%s <font color='red'>(%s)</font>", t.getMsg(), t.getSexplain()));
                tv_title.setText(spanned);
            } else {
                tv_title.setText(t.getMsg());
            }
            datetime.setText(Config.stampToDate(t.getDatetime()));
            money.setText("-" + t.getMoney());
            switch (t.getStatus()) {
                case Constants.TENCENT0:
                    state.setText(R.string.tv_msg56);
                    break;
                case Constants.TENCENT:
                    state.setText(R.string.tv_msg57);
                    state.setTextColor(context.getResources().getColor(R.color.rtc_green_bg));
                    break;
                case Constants.TENCENT2:
                    state.setText(R.string.tv_msg58);
                    money.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    state.setTextColor(context.getResources().getColor(R.color.colorAccent));
                    break;
            }
        }
    }


    class item_view_one3 {
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.pinglun)
        TextView pinglun;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.tv4)
        TextView tv4;
        @BindView(R.id.money)
        TextView money;
        @BindView(R.id.msg1)
        TextView msg1;
        @BindView(R.id.msg2)
        TextView msg2;
        @BindView(R.id.mapgps)
        TextView mapgps;
        @BindView(R.id.action)
        TextView action;
        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;

        public item_view_one3(View itemview) {
            ButterKnife.bind(this, itemview);

        }

        public void bind(Object object) {
            member member = (member) object;
            name.setText(!TextUtils.isEmpty(member.getTvname()) ? member.getTvname() : member.getTruename());
            gethelp help = member.getHelp();
            List<videotype> videotype = member.getVideotype();
            int people = help != null ? help.getPeople() : 0;
            int duration = help != null ? help.getDuration() : 0;
            String price = TextUtils.isEmpty(help.getPrice()) ? "0" : help.getPrice();
            int evaluate = member.getEvaluate();
            String content = "<font color=\"#333\"> " + evaluate + " </font> 条评论";
            String content1 = "帮助<font color=\"#333\"> " + people + "</font>人";
            String content2 = "服务<font color=\"#333\"> " + duration + "</font> 小时";
            String text = "<b><font color='red'>￥<myfont color='red' size='50px'>" + price + "</myfont>起</font></b>";
            Spanned content3 = Html.fromHtml(text, null, new HtmlTagHandler("myfont"));
            if (!TextUtils.isEmpty(help.getNametitle())) {
                tv1.setText(help.getNametitle());
            }
            linearLayout.removeAllViews();
            for (videotype types : videotype) {
                if (TextUtils.isEmpty(types.getTitle())) {
                    continue;
                }
                if (linearLayout.getChildCount() > 2) {
                    break;
                }
                TextView tv = new TextView(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv.setLayoutParams(params);
                tv.setText(types.getTitle());
                tv.setBackground(context.getResources().getDrawable(R.drawable.acitvity011));
                tv.setTextSize(10);
                tv.setPadding(10, 0, 5, 0);
                linearLayout.addView(tv);
            }
            if (!TextUtils.isEmpty(member.getJwd())) {
                String jwd = lbsamap.scalculateLineDistance(samapLocation, member.getJwd());
                msg1.setText(jwd);
            }
            String content4 = !TextUtils.isEmpty(member.getProvince()) ? member.getProvince().substring(0, 2) + "." + member.getCity().substring(0, 2) : "";
            pinglun.setText(Html.fromHtml(content));
            tv3.setText(Html.fromHtml(content1));
            tv4.setText(Html.fromHtml(content2));
            money.setText(content3);
            mapgps.setText(Html.fromHtml(content4));
            if (TextUtils.isEmpty(member.getPicture())) {
                Glideload.loadImage(icon, member.getSex() == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose, 4);
            } else {
                Glideload.loadImage(icon, member.getPicture(), 4);
            }
            action.setOnClickListener(v -> {
                if (paymnet != null) {
                    paymnet.onClick(member);
                }
            });
        }
    }
}

