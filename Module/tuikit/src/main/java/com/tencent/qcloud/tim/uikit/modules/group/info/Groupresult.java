package com.tencent.qcloud.tim.uikit.modules.group.info;

import java.util.List;

public class Groupresult {

    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private List<MemberList> MemberList;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String actionStatus) {
        ActionStatus = actionStatus;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        ErrorInfo = errorInfo;
    }

    public List<com.tencent.qcloud.tim.uikit.modules.group.info.MemberList> getMemberList() {
        return MemberList;
    }

    public void setMemberList(List<com.tencent.qcloud.tim.uikit.modules.group.info.MemberList> memberList) {
        MemberList = memberList;
    }

    @Override
    public String toString() {
        return "Groupresult{" +
                "ActionStatus='" + ActionStatus + '\'' +
                ", ErrorCode=" + ErrorCode +
                ", ErrorInfo='" + ErrorInfo + '\'' +
                ", MemberList=" + MemberList +
                '}';
    }
}
