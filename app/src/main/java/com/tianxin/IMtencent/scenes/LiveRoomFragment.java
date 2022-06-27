package com.tianxin.IMtencent.scenes;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianxin.IMtencent.scenes.adapter.RoomListAdapter;
import com.tianxin.IMtencent.scenes.net.RoomManager;
import com.tianxin.R;
import com.tianxin.utils.ClickUtils;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播聊天列表
 */
public class LiveRoomFragment extends BaseScenesFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = LiveRoomFragment.class.getSimpleName();
    private View mRootView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public RoomListAdapter mRoomListAdapter;
    private LinearLayout eorr;
    private final List<RoomListAdapter.ScenesRoomInfo> mRoomInfoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.live_fragment_live_room, container, false);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        getScenesRoomInfos();  //获取加载直播房间信息


    }

    @Override
    public void onRefresh() {
        getScenesRoomInfos();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.live_scenes_live_create_room:
                if (ClickUtils.isFastClick(v.getId())) {
                    return;
                }
                createRoom(null);
                break;
            case R.id.eorr:
                eorr.setVisibility(View.GONE);
                getScenesRoomInfos();
                break;
        }
    }

    private void initView() {

        //未什么加这些，主是要腾讯直播需要先创建会话列表才能去开直播，不然返回会闪退
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ConversationFragment()).commitAllowingStateLoss();

        //从布局文件中获取会话列表面板-->主是要腾讯直播需要先创建会话列表才能去开直播，不然返回会闪退
        ConversationLayout mConversationLayout = mRootView.findViewById(R.id.conversation_layout);
        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault();
        //隐藏会话列表
        mConversationLayout.setVisibility(View.GONE);
        //下拉刷新
        mSwipeRefreshLayout = mRootView.findViewById(R.id.live_scenes_live_room_swipe_refresh_layout_list);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        eorr = mRootView.findViewById(R.id.eorr);
        eorr.setOnClickListener(this);
        mRootView.findViewById(R.id.live_scenes_live_create_room).setOnClickListener(this);
        mRootView.findViewById(R.id.live_scenes_live_create_room).setVisibility(View.GONE);
        mRecyclerView = mRootView.findViewById(R.id.live_scenes_live_room_rv_room_list);

        //列表适配器判断是否为主主播
        mRoomListAdapter = new RoomListAdapter(getContext(), mRoomInfoList, new RoomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RoomListAdapter.ScenesRoomInfo roomInfo) {
                String selfUserId = ProfileManager.getInstance().getUserModel().userId;
                boolean equals = roomInfo.anchorId.equals(selfUserId);
                if (equals) {
                    //创建房间或进入直播间
                    createRoom(roomInfo);
                } else {
                    //进入房间
                    enterRoom(roomInfo);
                }
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mRoomListAdapter);

        //处理边框宽度
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int space = getResources().getDimensionPixelOffset(R.dimen.page_margin2);
                outRect.top = space;
                int childLayoutPosition = parent.getChildLayoutPosition(view);
                if (childLayoutPosition % 2 == 0) {
                    outRect.left = space;
                    outRect.right = space / 2;
                } else {
                    outRect.left = space / 2;
                    outRect.right = space;
                }
            }
        });
    }

    public RoomListAdapter getmRoomListAdapter() {
        return mRoomListAdapter;
    }


    /**
     * 获取会员直播房间列表信息
     */
    private void getScenesRoomInfos() {
        RoomManager.getInstance().getScenesRoomInfos(getContext(), RoomManager.TYPE_LIVE_ROOM, new RoomManager.GetScenesRoomInfosCallback() {
            @Override
            public void onSuccess(List<RoomListAdapter.ScenesRoomInfo> scenesRoomInfos) {
                mRoomInfoList.clear();
                mRoomInfoList.addAll(scenesRoomInfos);    //载入数据
                mSwipeRefreshLayout.setRefreshing(false); //关闭滚动刷新
                mRoomListAdapter.notifyDataSetChanged();  //刷新通知
                eorr.setVisibility(scenesRoomInfos.size() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onFailed(int code, String msg) {
                try {
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                    Log.d(TAG, "onFailed: "+msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 创建直播房间号
     */
    private void createRoom(RoomListAdapter.ScenesRoomInfo info) {
        LiveRoomAnchorActivity.start(getContext(), info != null ? info.roomId : "");
    }

    /**
     * 主播资料不是空的进入房间
     *
     * @param info
     */
    private void enterRoom(RoomListAdapter.ScenesRoomInfo info) {
        Intent intent = new Intent(getActivity(), LiveRoomAudienceActivity.class);
        intent.putExtra(RoomManager.ROOM_TITLE, info.roomName);
        intent.putExtra(RoomManager.GROUP_ID, Integer.valueOf(info.roomId));//房间号
        intent.putExtra(RoomManager.USE_CDN_PLAY, false);
        intent.putExtra(RoomManager.ANCHOR_ID, info.anchorId);
        intent.putExtra(RoomManager.PUSHER_NAME, info.anchorName);
        intent.putExtra(RoomManager.COVER_PIC, info.coverUrl);
        intent.putExtra(RoomManager.PUSHER_AVATAR, info.coverUrl);
        startActivity(intent);
    }
}
