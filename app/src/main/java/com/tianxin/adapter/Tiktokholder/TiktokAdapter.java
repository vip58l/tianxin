/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/1 0001
 */


package com.tianxin.adapter.Tiktokholder;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.adapter.itemholder.tiokeholder1;
import com.tianxin.adapter.itemholder.tiokeholder2;
import com.tianxin.adapter.itemholder.tiokeholder3;
import com.tianxin.adapter.itemholder.tiokeholder4;
import com.tianxin.adapter.itemholder.tiokeholder5;
import com.tianxin.adapter.itemholder.tiokeholder6;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.videolist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 播放器适配器item
 */
public class TiktokAdapter extends RecyclerView.Adapter {
    private static final String TAG = TiktokAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<Object> videolist;
    public int TYPE;
    private Callback callback;
    public final static int TYPE1 = 1;
    public final static int TYPE2 = 2;
    public final static int TYPE3 = 3;
    public final static int TYPE4 = 4;
    public final static int TYPE5 = 5;
    public final static int TYPE6 = 6;
    public final static int TYPE7 = 7;
    public final static int TYPE8 = 8;


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public TiktokAdapter(Context context, List<Object> list, int TYPE) {
        this.mContext = context;
        this.videolist = list;
        this.TYPE = TYPE;
    }

    @Override
    public int getItemCount() {
        return videolist.size();
    }

    @NonNull
    @Override//关于创建视图持有者
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        int itemViewtype = getItemViewType(position);
        switch (itemViewtype) {
            case TYPE1://player
                return new tiokeholder1(mContext, parent);
            case TYPE2://player
            case TYPE7://player
            case TYPE8://player
                return new tiokeholder2(mContext, parent);
            case TYPE3://PLVideoView
                return new tiokeholder3(mContext, parent);
            case TYPE4://player
                return new tiokeholder4(mContext, parent);
            case TYPE5://VideoView
                return new tiokeholder5(mContext, parent);
            case TYPE6://PLVideoView
                return new tiokeholder6(mContext, parent);
            default:
                return null;
        }
    }

    @Override//关于绑定视图持有者
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        videolist video = (videolist) videolist.get(position);
        int type = getItemViewType(position);
        switch (type) {
            case TYPE1://player
                ((tiokeholder1) holder).bind(mContext, video, callback);
                break;
            case TYPE2: //player
                ((tiokeholder2) holder).bind(mContext, video, position);
                break;
            case TYPE3://PLVideoView
                ((tiokeholder3) holder).bind(mContext, video, position);
                break;
            case TYPE4: //player
                ((tiokeholder4) holder).bind(video);
                break;
            case TYPE5://VideoView
                ((tiokeholder5) holder).bind(video);
                break;
            case TYPE6://PLVideoView
                ((tiokeholder6) holder).bind(video,position,null);
                break;
            case TYPE7: //player
                ((tiokeholder2) holder).bind(mContext, video, position, null);
                break;
            case TYPE8: //player
                ((tiokeholder2) holder).bind8(mContext, video);
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView: 关于附加到RecyclerView");
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
        Log.d(TAG, "onFailedToRecycleView: 关于回收失败");
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d(TAG, "onDetachedFromRecyclerView:从回收器视图中分离 ");
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d(TAG, "onViewAttachedToWindow: 打开视图连接到窗口");
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Log.d(TAG, "onViewDetachedFromWindow: 从窗口分离的视图");
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        Log.d(TAG, "onViewRecycled: 在视图中循环使用");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        Log.d(TAG, "onBindViewHolder:关于绑定视图持有者 ");
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE;
    }


    private static List<String> data() {
        List<String> list = new ArrayList<>();
        list.add("http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319104618910544.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/17/mp4/190317150237409904.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/12/mp4/190312083533415853.mp4");
        list.add("http://vfx.mtime.cn/Video/2019/03/09/mp4/190309153658147087.mp4");
        list.add("http://vod.51dengta.net/24c975fbvodcq1302331714/3a4bfa623701925921002797139/15PNADylOl8A.mp4");


        //打乱集合中的元素
        Collections.shuffle(list);

        //将集合按照默认的规则排序,按照数字从小到大的顺序排序
        //Collections.sort(list);

        //将集合中的元素反转
        //Collections.reverse(list);

        //按照字符串首字符的升序排列
        //Collections.sort(list);

        return list;
    }

    public static List<videolist> date() {
        List<videolist> list = new ArrayList<>();
        for (String s : data()) {
            videolist video = new videolist();
            video.setTitle("我是小美女");
            video.setAlias("我是小美女123");
            video.setAvatar("https://img1.baidu.com/it/u=1180948136,2356188357&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889");
            video.setBigpicurl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fnimg.ws.126.net%2F%3Furl%3Dhttp%253A%252F%252Fdingyue.ws.126.net%252F2021%252F0625%252F747301e2j00qv9j2c003bc000hs012jc.jpg%26thumbnail%3D650x2147483647%26quality%3D80%26type%3Djpg&refer=http%3A%2F%2Fnimg.ws.126.net&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1646479906&t=9760a929c6fb484771a47eb3c2e3a24d");
            video.setPicurl("https://img1.baidu.com/it/u=1180948136,2356188357&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889");
            video.setPicuser("https://img1.baidu.com/it/u=1180948136,2356188357&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889");
            video.setPlayurl(s);
            video.setPlaytest("");
            video.setTencent(0);
            video.setAnum("10");
            video.setPnum("100");
            video.setFnum("1000");
            list.add(video);
        }
        return list;
    }

}