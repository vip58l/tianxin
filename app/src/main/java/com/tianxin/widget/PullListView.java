package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tianxin.R;

/**
 * @author 自定义listview实现上拉刷新和下拉加载
 * 2018-08-25 刘明昆 q571039838
 */
public class PullListView extends ListView implements AbsListView.OnScrollListener {
    private View Head; //下拉头布局
    private View Bottom; //上拉布局
    private int totaItemCounts;//记录是下拉还是上拉
    private int lassVisible; //上拉
    private int firstVisible; //下拉
    private int bottomHeight;//尾文件高度
    private int headHeight; //头文件高度
    private int Yload;//位置
    boolean isLoading;//加载状态
    private TextView headtxt;//头文件textview显示加载文字
    private ImageView head_xw_ptr_arrow_img;//箭头提示
    private TextView headtime;//头文件textview显示加载时间
    private ProgressBar progressBar;//加载进度
    private LoadListener loadListener; //接口回调

    public PullListView(Context context) {
        super(context);
        Init(context);
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        //拿到头布局文件xml
        Head = LinearLayout.inflate(context, R.layout.layout_xw_ptr_refresh_head, null);
        headtxt = Head.findViewById(R.id.head_xw_ptr_hint_text);
        head_xw_ptr_arrow_img = Head.findViewById(R.id.head_xw_ptr_arrow_img);
        progressBar = Head.findViewById(R.id.head_xw_ptr_progress_bar);
        //headtime.setText("上次更新时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

        //拿到尾布局文件
        Bottom = LinearLayout.inflate(context, R.layout.layout_xw_ptr_load_foot, null);
        //测量尾文件高度
        Bottom.measure(0, 0);
        //拿到高度
        bottomHeight = Bottom.getMeasuredHeight();
        //隐藏view
        Bottom.setPadding(0, -bottomHeight, 0, 0);

        Head.measure(0, 0);
        headHeight = Head.getMeasuredHeight();
        Head.setPadding(0, -headHeight, 0, 0);

        addHeaderView(Head);
        addFooterView(Bottom);
        setOnScrollListener(this); //设置滚动监听
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisible == 0) {
                    Yload = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();
                int paddingY = headHeight + (moveY - Yload) / 2; //下拉距离
                if (paddingY < headHeight) {
                    headtxt.setText("下拉刷新");
                    progressBar.setVisibility(View.GONE);
                }
                if (paddingY > headHeight) {
                    headtxt.setText("松开立即刷新");
                    progressBar.setVisibility(View.GONE);
                    //head_xw_ptr_arrow_img.setAnimation(animationboot());
                    head_xw_ptr_arrow_img.setAnimation(animation());
                }
                Head.setPadding(0, paddingY - headHeight, 0, 0);//设置移动距离
                break;
            case MotionEvent.ACTION_UP:
                head_xw_ptr_arrow_img.setVisibility(GONE);
                head_xw_ptr_arrow_img.clearAnimation();//清除动画
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totaItemCounts == lassVisible && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                isLoading = true;
                head_xw_ptr_arrow_img.setVisibility(GONE);
                Bottom.setPadding(0, 0, 0, 0);
                //加载数据
                loadListener.onLoad();
            }
        }

        if (firstVisible == 0 && scrollState == SCROLL_STATE_IDLE) {
            Head.setPadding(0, 0, 0, 0);
            headtxt.setText("正在刷新...");
            progressBar.setVisibility(View.VISIBLE);
            head_xw_ptr_arrow_img.setVisibility(GONE);
            loadListener.Refresh();
        }
    }


    //接口回调
    public interface LoadListener {
        void onLoad();

        void Refresh();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisible = firstVisibleItem;
        this.lassVisible = firstVisibleItem + visibleItemCount;
        this.totaItemCounts = totalItemCount;
    }

    //加载完成
    public void loadComplete() {
        isLoading = false;
        Head.setPadding(0, -headHeight, 0, 0);//头部隐藏
        Bottom.setPadding(0, -bottomHeight, 0, 0);//底部隐藏
    }

    //接口设置
    public void setInteface(LoadListener loadListener) {
        this.loadListener = loadListener;
    }


    private RotateAnimation animationboot() {

        //箭头由朝上转为朝下
        RotateAnimation animation = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        return animation;

        //head_xw_ptr_arrow_img.clearAnimation();    //清除箭头动画效果
    }

    private RotateAnimation animation() {
        //即箭头由朝下转为朝上
        RotateAnimation animation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        return animation;
    }
}
