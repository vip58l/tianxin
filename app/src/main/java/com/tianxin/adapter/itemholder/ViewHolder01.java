package com.tianxin.adapter.itemholder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.activity.LatestNews.activity_show_trend;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.OnItemChildClickListener;
import com.tianxin.widget.MTextView;
import com.tianxin.widget.item_dianzhang;
import com.tencent.opensource.model.personal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 动态列表
 */
public class ViewHolder01 extends BaseHolder {
    private static final String TAG = ViewHolder01.class.getSimpleName();
    @BindView(R.id.icon)
    public ImageView icon;
    @BindView(R.id.show_img)
    public ImageView show_img;
    @BindView(R.id.play_mp)
    public ImageView play_mp;
    @BindView(R.id.title)
    public TextView title;
    @BindView(R.id.tv_mtextiew)
    public MTextView mTextView;
    @BindView(R.id.datetime)
    public TextView datetime;
    @BindView(R.id.relayout)
    public LinearLayout relayout;
    @BindView(R.id.recyclerview)
    public RecyclerView recyclerview;
    @BindView(R.id.play)
    public RelativeLayout play;
    @BindView(R.id.chat)
    item_dianzhang chat;
    @BindView(R.id.chatcaw)
    item_dianzhang chatcaw;
    @BindView(R.id.imagesvip)
    TextView imagesvip;
    @BindView(R.id.age)
    TextView age;
    public com.tencent.opensource.model.member member;
    public com.tencent.opensource.model.trend trend;

    public static View view(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_view_trend, parent, false);
    }

    public ViewHolder01(@NonNull View itemView) {
        super(itemView);
    }

    @OnClick({R.id.title, R.id.icon, R.id.showdelete, R.id.show_img})
    public void OnClick(View v) {
        if (onItemChildClickListener != null)
            onItemChildClickListener.onItemChildClick(trend, v, position);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback, int TYPE, OnItemChildClickListener onItemChildClickListener) {
        this.context = context;
        this.callback = callback;
        this.position = position;
        this.object = object;
        this.TYPE = TYPE;
        this.onItemChildClickListener = onItemChildClickListener;
        this.trend = (com.tencent.opensource.model.trend) object;
        this.member = trend.getMember();
        if (trend == null || member == null) {
            return;
        }
        //显示文本
        showtext();

        //显示视频
        if (!TextUtils.isEmpty(trend.getVideo())) {
            playVideo();
        }

        //显示图片
        if (TextUtils.isEmpty(trend.getVideo()) && !TextUtils.isEmpty(trend.getImage())) {
            setGridview();
        }
    }

    /**
     * 处理多格图片
     */
    public void setGridview() {
        relayout.setVisibility(VISIBLE);
        int index = trend.getImage().indexOf(",");
        if (index > 0) {
            play.setVisibility(GONE);
            recyclerview.setVisibility(VISIBLE);
            List<Object> list = new ArrayList<>();
            String[] split = trend.getImage().split(",");
            list.addAll(Arrays.asList(split));
            int managerlist = 3;
            int TYPEs = Radapter.fmessage_recyview; //读取模板样式
            switch (list.size()) {
                case 2:
                case 4:
                    managerlist = 2;
                    TYPEs = Radapter.fmessage_recyview2; //模板样式
                    break;
            }

            //2列显示或3列显示模横向排列
            GridLayoutManager manager = new GridLayoutManager(context, managerlist);
            Radapter radapter = new Radapter(context, list, TYPEs);
            radapter.setOnItemChildClickListener(onItemChildClickListener);
            recyclerview.setLayoutManager(manager);
            recyclerview.setAdapter(radapter);

        } else {
            //显示单张图片样式
            play.setVisibility(VISIBLE);
            show_img.setVisibility(VISIBLE);
            play_mp.setVisibility(GONE);
            recyclerview.setVisibility(GONE);
            ImageLoadHelper.glideShowCornerImageWithUrl(context, trend.getImage(), show_img);
        }


    }

    /**
     * 视频播器显示
     */
    public void playVideo() {
        relayout.setVisibility(VISIBLE);
        play_mp.setVisibility(VISIBLE);
        show_img.setVisibility(VISIBLE);
        play.setVisibility(VISIBLE);
        recyclerview.setVisibility(GONE);
        String path = !TextUtils.isEmpty(trend.getImage()) ? trend.getImage() : trend.getVideo();
        path = tiokeholder2.getvideo(path, trend.getTencent());
        ImageLoadHelper.glideShowCornerImageWithUrl(context, path, show_img);
    }

    /**
     * 显示文本
     */
    @Override
    public void showtext() {
        //头像显示
        if (TextUtils.isEmpty(member.getPicture())) {
            //设置默认图片
            int icons = member.getSex() == Constants.TENCENT2 ? R.mipmap.girl_on : R.mipmap.boy_on;

            ImageLoadHelper.glideShowImageWithUrl(context, icons, icon);
        } else {
            ImageLoadHelper.glideShowCornerImageWithUrl(context, member.getPicture(), icon);
        }
        title.setText(member.getTruename());

        //内容字体设置
        mTextView.setText(trend.getTitle());
        mTextView.setTextSize(15);                                                          //字体大小14
        mTextView.setMaxLine(2);                                                            //超出最大2行隐藏文本
        mTextView.setButtontextColor(context.getResources().getColor(R.color.textCol2));    //全文/收起 字体色
        mTextView.settextColor(context.getResources().getColor(R.color.teal006));           //展示字体色

        //昨天今天发布
        datetime.setText(!TextUtils.isEmpty(trend.getDatetime()) ? Config.timestamp(trend.getDatetime()) : "");
        relayout.setVisibility(GONE);
        String strend = new Gson().toJson(trend);

        //打开评论详情
        chat.setOnClickListener(v -> activity_show_trend.starsetAction(context, strend, String.valueOf(member.getId())));
        chat.seticons(R.mipmap.c64);
        chat.setuntion(trend.getCount());
        chat.setAlphas(0.3f);

        //给TA点击赞
        chatcaw.setOnClickListener(v -> onItemChildClickListener.onItemdefaultListener(trend, v, position));
        chatcaw.seticons(R.mipmap.dynamics_list_unpraise);
        chatcaw.setuntion(trend.getLove());
        chatcaw.setAlphas(0.3f);

        imagesvip.setVisibility(member.getVip() == 1 ? VISIBLE : GONE);
        if (member.getVip() == 1) {
            title.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        //个人详情资料
        personal personal = trend.getPersonal();
        if (personal != null) {
            age.setText(personal.getAge() + " 岁");
        }

    }

    /**
     * 不能和自己聊天哦
     */
    private void tostartChatActivity() {
        if (!userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            startChatActivity(String.valueOf(member.getId()), member.getTruename());
        } else {
            Toashow.show(context.getString(R.string.tv_msg_messeg));
        }
    }


}
