package com.tencent.qcloud.tim.uikit.modules.group.info;

public class MemberList {
    private String Member_Account;
    private int Result;

    public String getMember_Account() {
        return Member_Account;
    }

    public void setMember_Account(String member_Account) {
        Member_Account = member_Account;
    }

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    @Override
    public String toString() {
        return "MemberList{" +
                "Member_Account='" + Member_Account + '\'' +
                ", Result=" + Result +
                '}';
    }
}
