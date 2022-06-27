package com.tencent.qcloud.tim.uikit.modules.group.member;

import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;

public interface IGroupMemberRouter {

    /**
     * 获取成员列表
     * @param info
     */
    void forwardListMember(GroupInfo info);

    /**
     *  用于添加成员
     * @param info
     */
    void forwardAddMember(GroupInfo info);

    /**
     * 用于删除成员
     * @param info
     */
    void forwardDeleteMember(GroupInfo info);

    /**
     * 设置禁言时间
     * @param info
     */
   default void TimeMember(GroupInfo info,String userid){}
}
