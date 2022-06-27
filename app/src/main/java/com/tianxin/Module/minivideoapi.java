/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/13 0013
 */


package com.tianxin.Module;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tianxin.Fragment.fragment.item;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.getlist.minivideo;
import com.pili.pldroid.player.widget.PLVideoView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class minivideoapi {

    private static final String TAG = "minivideoapi";
    public viewitem viewitem;
    private View view;
    private ImageView playbg;
    private ProgressBar loading;
    private final Context context;
    public PLVideoView mIjkVideoVie;
    private final List<View> viewlist;

    public ProgressBar getLoading() {
        return loading;
    }

    public void setLoading(ProgressBar loading) {
        this.loading = loading;
    }

    public ImageView getPlaybg() {
        return playbg;
    }

    public void setPlaybg(ImageView playbg) {
        this.playbg = playbg;
    }

    public minivideoapi(Context context, List<View> viewlist) {
        this.context = context;
        this.viewlist = viewlist;
    }

    /**
     * 初始化播放页布局
     *
     * @param items
     * @return
     */
    public List<View> setaddview(List<item> items) {
        for (int i = 0; i < items.size(); i++) {
            minivideo minivideo = (minivideo) items.get(i).object;
            viewitem = new viewitem();
            Glide.with(DemoApplication.instance()).load(minivideo.picurl).override(320, 640)
                    .skipMemoryCache(true)                      //禁止Glide内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE)  //不缓存资源
                    .into(viewitem.playbg);
            //Glide.with(DemoApplication.instance()).load(minivideo.picurl).apply(bitmapTransform(new BlurTransformation(25, 1))).override( 320, 640 ).into(viewitem.playbg);
            Glide.with(DemoApplication.instance()).asBitmap().load(minivideo.picurl).override(50, 50).into(viewitem.icon);
            Glide.with(DemoApplication.instance()).load(minivideo.picurl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                    if (drawable != null) {
                        viewitem.playbg.setImageDrawable(drawable);
                    }
                }
            });


            viewitem.title.setText(minivideo.alias);
            viewitem.mtitle.setText("短视频");
            viewitem.tv1.setText(String.valueOf(minivideo.znum));
            viewitem.tv2.setText(String.valueOf(minivideo.znum));
            viewitem.tv3.setText(String.valueOf(minivideo.sec));
            viewitem.viptype1title.setText("关注作者及时了解更多优质作品");
            viewitem.viptype2title.setText("关注");

            //点击事件处理
            viewitem.sRlayout.setOnClickListener(new ssetOnClickListener(viewitem.play));
            viewitem.refresh_view.setOnClickListener(new ssetOnClickListener2(minivideo));
            viewitem.mhde_img_back.setOnClickListener(new ssetOnClickListener2(minivideo));
            viewitem.icon.setOnClickListener(new ssetOnClickListener2(minivideo));
            viewitem.relayout1.setOnClickListener(new ssetOnClickListener2(minivideo));
            viewitem.relayout2.setOnClickListener(new ssetOnClickListener2(minivideo));
            viewitem.relayout3.setOnClickListener(new ssetOnClickListener2(minivideo));
            viewlist.add(view);
        }
        return viewlist;
    }

    public void SetPLVideoView(PLVideoView mIjkVideoVie) {
        this.mIjkVideoVie = mIjkVideoVie;
    }

    /**
     * 播放暂停监听
     */
    class ssetOnClickListener implements View.OnClickListener {
        ImageView play;

        public ssetOnClickListener(ImageView play) {
            this.play = play;
        }

        @Override
        public void onClick(View v) {
            int visibility = play.getVisibility();
            if (visibility == View.GONE) {
                play.setVisibility(View.VISIBLE);
                mIjkVideoVie.pause();
            } else {
                play.setVisibility(View.GONE);
                mIjkVideoVie.start();
            }

        }
    }

    /**
     * 点击事件监听
     */
    class ssetOnClickListener2 implements View.OnClickListener {

        private final minivideo minivideo;

        public ssetOnClickListener2(minivideo minivideo) {
            this.minivideo = minivideo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mhde_img_back:
                    ((Activity) context).finish();
                    break;
                case R.id.refresh_view:
                    dialog_Config.dialog_jibao(context);
                    break;
                case R.id.icon:
                    Toashow.show( "她设置了隐私权限 会员免费浏览");
                    break;
                case R.id.relayout1:
                    Toashow.show( "感谢支持");
                    break;
                case R.id.relayout2:
                    dialog_Config.Pinglun(context, minivideo.uid);
                    break;
                case R.id.relayout3:
                    dialog_Config.fenxing(context);
                    break;
            }
        }
    }


    public static String formatNumber(long val) {
        if (val < 10000) {
            return val + "";
        }
        DecimalFormat df = new DecimalFormat("######0.0");
        double d = val / 10000.0;
        return df.format(d) + "万";
    }

    /**
     * 布局组件
     */
    public class viewitem {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.mtitle)
        TextView mtitle;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;
        @BindView(R.id.tv3)
        TextView tv3;
        @BindView(R.id.viptype1title)
        TextView viptype1title;
        @BindView(R.id.viptype2title)
        TextView viptype2title;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.bgmplay)
        ImageView playbg;
        @BindView(R.id.play)
        ImageView play;
        @BindView(R.id.sRlayout)
        RelativeLayout sRlayout;
        @BindView(R.id.progressbar)
        ProgressBar progressbar;
        @BindView(R.id.refresh_view)
        ImageView refresh_view;
        @BindView(R.id.mhde_img_back)
        ImageView mhde_img_back;
        @BindView(R.id.relayout1)
        RelativeLayout relayout1;
        @BindView(R.id.relayout2)
        RelativeLayout relayout2;
        @BindView(R.id.relayout3)
        RelativeLayout relayout3;
        @BindView(R.id.loading)
        ProgressBar loading;

        public viewitem() {
            view = LayoutInflater.from(DemoApplication.instance()).inflate(R.layout.item_chid_play, null);
            ButterKnife.bind(this,view);
        }

    }

}
