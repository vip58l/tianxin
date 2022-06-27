package com.tencent.qcloud.tim.tuikit.live.component.topbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.adapter.SpacesDecoration;
import com.tencent.qcloud.tim.tuikit.live.component.topbar.adapter.TopAudienceListAdapter;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;

import java.util.ArrayList;
import java.util.List;


/**
 * //主播头像和个人信息填充
 */
public class TopToolBarLayout extends LinearLayout implements View.OnClickListener, TopAudienceListAdapter.OnItemClickListener {
    private final String TAG = "TopAnchorInfoLayout";
    private Context mContext;
    private LinearLayout mLayoutRoot;
    private CircleImageView mImageAnchorIcon;
    private ImageView closexit;
    private TextView mTextAnchorName;
    private TextView mButtonAnchorFollow;
    private TextView mAudienceNumber;
    private RecyclerView recyclerview;
    private TopAudienceListAdapter mTopAudienceListAdapter;
    private TopToolBarDelegate mTopToolBarDelegate;
    private TRTCLiveRoomDef.LiveAnchorInfo mLiveAnchorInfo;

    public TopToolBarLayout(Context context) {
        super(context);
        init(context);
    }

    public TopToolBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TopToolBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mLayoutRoot = (LinearLayout) inflate(context, R.layout.live_layout_top_tool_bar, this);
        initAnchorInfoView();
        initAudienceRecyclerView();
        initAudienceNumberView();
        updateAudienceNumber();
    }

    private void initAnchorInfoView() {
        mImageAnchorIcon = mLayoutRoot.findViewById(R.id.iv_anchor_head);
        mTextAnchorName = mLayoutRoot.findViewById(R.id.tv_anchor_name);
        mButtonAnchorFollow = mLayoutRoot.findViewById(R.id.btn_anchor_follow);
        closexit = mLayoutRoot.findViewById(R.id.topback);
        mImageAnchorIcon.setOnClickListener(this);           //处理主播头像点击事件
        mButtonAnchorFollow.setOnClickListener(this);        //处理主播关注按钮点击事件
        closexit.setOnClickListener(this);                   //处理点击退出直播

    }

    /**
     * 设置recyclerview相关数据
     */
    private void initAudienceRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);  //管理器
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);           //排列方式
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();    //默认项动画制作程序
        defaultItemAnimator.setAddDuration(500);                                //设置添加持续时间
        defaultItemAnimator.setRemoveDuration(500);                             //设置移除

        recyclerview = mLayoutRoot.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addItemDecoration(new SpacesDecoration(mContext, 3, SpacesDecoration.HORIZONTAL));
        recyclerview.setItemAnimator(defaultItemAnimator);                      //设置条目动画
        mTopAudienceListAdapter = new TopAudienceListAdapter(new ArrayList<TRTCLiveRoomDef.TRTCLiveUserInfo>(), this);
        recyclerview.setAdapter(mTopAudienceListAdapter);
    }

    /**
     * 在线访问人数
     */
    private void initAudienceNumberView() {
        mAudienceNumber = mLayoutRoot.findViewById(R.id.tv_audience_number);
        mAudienceNumber.setOnClickListener(this);
    }

    /**
     * 设置关注后隐藏或显示关注图标
     *
     * @param followed
     */
    public void setHasFollowed(boolean followed) {
        mButtonAnchorFollow.setVisibility(followed ? GONE : VISIBLE);
    }

    /**
     * 设置主播头像相关内容
     *
     * @param anchorInfo
     */
    public void setAnchorInfo(TRTCLiveRoomDef.LiveAnchorInfo anchorInfo) {
        mLiveAnchorInfo = anchorInfo;
        mTextAnchorName.setText(!TextUtils.isEmpty(anchorInfo.userName) ? anchorInfo.userName : anchorInfo.userId);
        if (!TextUtils.isEmpty(anchorInfo.avatarUrl)) {
            //主播网络头像
            Glide.with(TUIKitLive.getAppContext()).load(anchorInfo.avatarUrl).into(mImageAnchorIcon);
        } else {
            //主播放默认头像
            Glide.with(TUIKitLive.getAppContext()).load(R.drawable.live_default_head_img).into(mImageAnchorIcon);
        }
    }

    /**
     * 添加访问群体列表用户
     *
     * @param userInfoList
     */
    public void addAudienceListUser(List<TRTCLiveRoomDef.TRTCLiveUserInfo> userInfoList) {
        mTopAudienceListAdapter.addAudienceUser(userInfoList); //头像适配器
        updateAudienceNumber();                                 //数据在线人数
    }

    /**
     * 添加访问群体列表用户
     *
     * @param userInfo
     */
    public void addAudienceListUser(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mTopAudienceListAdapter.addAudienceUser(userInfo);
        updateAudienceNumber();
    }

    /**
     * 删除访问群体用户
     *
     * @param userInfo
     */
    public void removeAudienceUser(TRTCLiveRoomDef.TRTCLiveUserInfo userInfo) {
        mTopAudienceListAdapter.removeAudienceUser(userInfo);
        updateAudienceNumber();
    }

    /**
     * 更新在线人数
     */
    private void updateAudienceNumber() {
        int size = mTopAudienceListAdapter.getAudienceListSize();
        String audienceNum = mContext.getString(R.string.live_on_line_number, size < 0 ? 0 : size);
        mAudienceNumber.setText(audienceNum);
    }

    /**
     * 设置接口传入回调对像
     *
     * @param delegate
     */
    public void setTopToolBarDelegate(TopToolBarDelegate delegate) {
        mTopToolBarDelegate = delegate;
    }

    @Override
    public void onClick(View v) {
        if (mTopToolBarDelegate == null) {
            return;
        }
        int id = v.getId();
        if (id == R.id.iv_anchor_head) {
            mTopToolBarDelegate.onClickAnchorAvatar();
        }
        if (id == R.id.btn_anchor_follow) {
            mTopToolBarDelegate.onClickFollow(mLiveAnchorInfo);
        }
        if (id == R.id.topback) {
            mTopToolBarDelegate.onCloseexit();
        }
        if (id == R.id.tv_audience_number) {
            mTopToolBarDelegate.onClickOnlineNum();
        }
    }

    @Override
    public void onItemClick(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo) {
        /**
         * 点击条目回调
         */
        if (mTopToolBarDelegate != null) {
            mTopToolBarDelegate.onClickAudience(audienceInfo);
        }
    }

    /**
     * 自定义接口回调方法
     */
    public interface TopToolBarDelegate {

        /**
         * 点击主播头像
         */
        void onClickAnchorAvatar();

        /**
         * 点击关注
         *
         * @param liveAnchorInfo
         */
        void onClickFollow(TRTCLiveRoomDef.LiveAnchorInfo liveAnchorInfo);

        /**
         * 回调item数据
         *
         * @param audienceInfo
         */
        void onClickAudience(TRTCLiveRoomDef.TRTCLiveUserInfo audienceInfo);

        /**
         * 在线访问人数点击
         */
        void onClickOnlineNum();

        /**
         * 退出直播
         */
        void onCloseexit();


    }

}
