package com.tianxin.IMtencent.scenes.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.opensource.svgaplayer.SVGAImageView;
import com.tianxin.R;
import com.tianxin.utils.ClickUtils;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import java.util.List;

/**
 * 用于展示房间直播列表的item适配器
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ScenesRoomInfo> mList;
    private final OnItemClickListener onItemClickListener;

    public RoomListAdapter(Context context, List<ScenesRoomInfo> list, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mList = list;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.live_room_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mContext, mList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImagePic;
        private SVGAImageView svgaImageView;
        private TextView mTextRoomName;
        private TextView mTextAnchorName;
        private TextView mTextMemberCount;

        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            mTextRoomName = itemView.findViewById(R.id.live_tv_live_room_name);
            svgaImageView = itemView.findViewById(R.id.live_iv_live_room_image);
            mTextAnchorName = itemView.findViewById(R.id.live_tv_live_anchor_name);
            mTextMemberCount = itemView.findViewById(R.id.live_tv_live_member_count);
            mImagePic = itemView.findViewById(R.id.live_iv_live_room_pic);
        }

        public void bind(Context context, final ScenesRoomInfo roomInfo, final OnItemClickListener listener) {
            if (TextUtils.isEmpty(roomInfo.roomName)) {
                mTextRoomName.setVisibility(View.GONE);
            } else {
                mTextRoomName.setVisibility(View.VISIBLE);
                mTextRoomName.setText(roomInfo.roomName);
            }
            if (TextUtils.isEmpty(roomInfo.anchorName)) {
                mTextAnchorName.setVisibility(View.GONE);
            } else {
                mTextAnchorName.setVisibility(View.VISIBLE);
                mTextAnchorName.setText(roomInfo.anchorName);
            }
            mTextMemberCount.setText(String.valueOf(roomInfo.memberCount));
            if (!TextUtils.isEmpty(roomInfo.coverUrl)) {
                GlideEngine.loadImage(mImagePic, roomInfo.coverUrl, 8, 0, 0);
            } else {
                GlideEngine.loadImage(mImagePic, R.drawable.live_room_default_cover, 6);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ClickUtils.isFastClick(v.getId())) {
                        listener.onItemClick(getLayoutPosition(), roomInfo);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ScenesRoomInfo roomInfo);
    }

    public static class ScenesRoomInfo {
        public String roomName;     //房间名称
        public String roomId;       //房间唯一标识
        public String anchorName;   //房主昵称
        public String coverUrl;     //房间封面图
        public String anchorId;     //房主id
        public String streamUrl;    //【字段含义】cdn模式下的播放流地址
        public int roomStatus;      //【字段含义】房间的状态: 单人/连麦/PK
        public int memberCount;     //【字段含义】房间人数
        public String province;     //省份
        public String city;         //城市
        public String areacounty;   //区县

        @Override
        public String toString() {
            return "ScenesRoomInfo{" +
                    "roomName='" + roomName + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", anchorName='" + anchorName + '\'' +
                    ", coverUrl='" + coverUrl + '\'' +
                    ", anchorId='" + anchorId + '\'' +
                    ", streamUrl='" + streamUrl + '\'' +
                    ", roomStatus=" + roomStatus +
                    ", memberCount=" + memberCount +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", areacounty='" + areacounty + '\'' +
                    '}';
        }
    }
}
