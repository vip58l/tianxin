package com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model;

public class TRTCLiveRoomDef {
    // 房间的状态
    public static final int ROOM_STATUS_NONE = 0;      //人数房间人数
    public static final int ROOM_STATUS_SINGLE = 1;    //单人房间
    public static final int ROOM_STATUS_LINK_MIC = 2;  //连麦
    public static final int ROOM_STATUS_PK = 3;        //进入PK

    //【字段含义】是否依赖于 TUIkit 使用本 Model
    public static final class TRTCLiveRoomConfig {
        // 【字段含义】是否依赖于 TUIkit 使用本 Model
        // 【特殊说明】true: 依赖于 TUIkit，将会使用广播的形式接收IM信息 false: 不依赖于 TUIkit 使用，独立监听IM消息
        public boolean isAttachTuikit = false;
    }

    /**
     * 主播相关信息
     */
    public static final class LiveAnchorInfo {
        // 【字段含义】主播唯一标识
        public String userId;
        // 【字段含义】主播昵称
        public String userName;
        // 【字段含义】主播头像
        public String avatarUrl;
        // 【字段含义】主播经验值
        public int empiricalValue;
        // 【字段含义】主播经验值对应的计量单位名/标签，如：星光/经验
        public int empiricalUnitName;

        public String province;     //省份
        public String city;         //城市
        public String areacounty;   //区县

        @Override
        public String toString() {
            return "AnchorInfo{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", empiricalValue='" + empiricalValue + '\'' +
                    ", empiricalUnitName='" + empiricalUnitName + '\'' +
                    '}';
        }
    }

    /**
     * 用户唯一标识1
     */
    public static final class LiveAudienceInfo {
        // 【字段含义】用户唯一标识
        public String userId;
        // 【字段含义】用户昵称
        public String userName;
        // 【字段含义】用户头像
        public String avatarUrl;
        // 【字段含义】当前观众相关的权重数值，可以是排名，金币数等
        public int weightValue;

        public String province;     //省份
        public String city;         //城市
        public String areacounty;   //区县
        @Override
        public String toString() {
            return "AnchorInfo{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", weightValue='" + weightValue + '\'' +
                    '}';
        }
    }

    /**
     * 观众用户唯一标识2
     */
    public static final class TRTCLiveUserInfo {
        // 【字段含义】用户唯一标识
        public String userId;
        // 【字段含义】用户昵称
        public String userName;
        //【字段含义】用户头像
        public String avatarUrl;
        // 【字段含义】用户经验值
        public String audienceWeight;
        //自定义余额
        public int balance;

        public String province;     //省份
        public String city;         //城市
        public String areacounty;   //区县

        @Override
        public String toString() {
            return "TRTCLiveUserInfo{" +
                    "userId='" + userId + '\'' +
                    ", userName='" + userName + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", audienceWeight='" + audienceWeight + '\'' +
                    ", balance=" + balance +
                    '}';
        }
    }

    /**
     * 房间名称1
     */
    public static final class TRTCCreateRoomParam {
        // 【字段含义】房间名称
        public String roomName;
        // 【字段含义】房间封面图
        public String coverUrl;

        public String province;     //省份
        public String city;         //城市
        public String areacounty;   //区县

        @Override
        public String toString() {
            return "TRTCCreateRoomParam{" +
                    "roomName='" + roomName + '\'' +
                    ", coverUrl='" + coverUrl + '\'' +
                    '}';
        }
    }

    /**
     * 房间唯一标识2
     */
    public static final class TRTCLiveRoomInfo {
        // 【字段含义】房间唯一标识
        public int roomId;
        // 【字段含义】房间名称
        public String roomName;
        // 【字段含义】房间封面图
        public String coverUrl;
        // 【字段含义】房主id
        public String ownerId;
        // 【字段含义】房主昵称
        public String ownerName;
        // 【字段含义】cdn模式下的播放流地址
        public String streamUrl;
        // 【字段含义】房间的状态: 单人/连麦/PK
        public int roomStatus;
        // 【字段含义】房间人数
        public int memberCount;

        public String province;     //省份
        public String city;         //城市
        public String areacounty;   //区县

        @Override
        public String toString() {
            return "TRTCLiveRoomInfo{" +
                    "roomId=" + roomId +
                    ", roomName='" + roomName + '\'' +
                    ", coverUrl='" + coverUrl + '\'' +
                    ", ownerId='" + ownerId + '\'' +
                    ", ownerName='" + ownerName + '\'' +
                    ", streamUrl='" + streamUrl + '\'' +
                    ", roomStatus=" + roomStatus +
                    ", memberCount=" + memberCount +
                    '}';
        }
    }
}