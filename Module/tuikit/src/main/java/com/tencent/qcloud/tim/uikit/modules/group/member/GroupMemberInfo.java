package com.tencent.qcloud.tim.uikit.modules.group.member;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;

import java.io.Serializable;

public class GroupMemberInfo implements Serializable {

    private static final String TAG = GroupMemberInfo.class.getSimpleName();
    private String iconUrl;  //头像
    private String account;  //昵称
    private String signature;//签名
    private String location; //位置
    private String birthday; //生日
    private String nameCard; //名片
    private String userid;   //会员Id
    private String friendRemark; //会员Id
    private boolean isTopChat;
    private boolean isFriend;
    private long joinTime;
    private long tinyId;
    private int memberType;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSignature() {
        return signature;
    }

    public String getFriendRemark() {
        return friendRemark;
    }

    public void setFriendRemark(String friendRemark) {
        this.friendRemark = friendRemark;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    public String getNameCard() {
        return nameCard;
    }

    public boolean isTopChat() {
        return isTopChat;
    }

    public void setTopChat(boolean topChat) {
        isTopChat = topChat;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public long getTinyId() {
        return tinyId;
    }

    public void setTinyId(long tinyId) {
        this.tinyId = tinyId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    /**
     * 设置头像名称
     *
     * @param info
     * @return
     */
    public GroupMemberInfo covertTIMGroupMemberInfo(final V2TIMGroupMemberInfo info) {
        if (info instanceof V2TIMGroupMemberFullInfo) {
            V2TIMGroupMemberFullInfo v2TIMGroupMemberFullInfo = (V2TIMGroupMemberFullInfo) info;
            setJoinTime(v2TIMGroupMemberFullInfo.getJoinTime());
            setMemberType(v2TIMGroupMemberFullInfo.getRole());
        }
        setAccount(info.getNickName());
        setUserid(info.getUserID());
        setNameCard(info.getNameCard());
        setIconUrl(info.getFaceUrl());
        setSignature(info.getFriendRemark());
        setFriendRemark(info.getFriendRemark());
        return this;
    }

    @Override
    public String toString() {
        return "GroupMemberInfo{" +
                "iconUrl='" + iconUrl + '\'' +
                ", account='" + account + '\'' +
                ", signature='" + signature + '\'' +
                ", location='" + location + '\'' +
                ", birthday='" + birthday + '\'' +
                ", nameCard='" + nameCard + '\'' +
                ", userid='" + userid + '\'' +
                ", isTopChat=" + isTopChat +
                ", isFriend=" + isFriend +
                ", joinTime=" + joinTime +
                ", tinyId=" + tinyId +
                ", memberType=" + memberType +
                '}';
    }
}
