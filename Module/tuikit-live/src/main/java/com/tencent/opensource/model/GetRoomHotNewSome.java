package com.tencent.opensource.model;

import java.util.Date;

public class GetRoomHotNewSome {
    private int pos;
    private long roomid;
    private long useridx;
    private int gender;
    private int serverid;
    private int lrCurrent;
    private int allnum;
    private String userid;
    private String myname;
    private String smallpic;
    private String bigpic;
    private String gps;
    private String flv;
    private String rtmp;
    private int starlevel;
    private String familyName;
    private String stardate;
    private int isSign;
    private String nation;
    private String nationFlag;
    private int gameid;
    private int anchorLevel;
    private int nType;
    private int totalCount;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public long getRoomid() {
        return roomid;
    }

    public void setRoomid(long roomid) {
        this.roomid = roomid;
    }

    public long getUseridx() {
        return useridx;
    }

    public void setUseridx(long useridx) {
        this.useridx = useridx;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getLrCurrent() {
        return lrCurrent;
    }

    public void setLrCurrent(int lrCurrent) {
        this.lrCurrent = lrCurrent;
    }

    public int getAllnum() {
        return allnum;
    }

    public void setAllnum(int allnum) {
        this.allnum = allnum;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getSmallpic() {
        return smallpic;
    }

    public void setSmallpic(String smallpic) {
        this.smallpic = smallpic;
    }

    public String getBigpic() {
        return bigpic;
    }

    public void setBigpic(String bigpic) {
        this.bigpic = bigpic;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getFlv() {
        return flv;
    }

    public void setFlv(String flv) {
        this.flv = flv;
    }

    public String getRtmp() {
        return rtmp;
    }

    public void setRtmp(String rtmp) {
        this.rtmp = rtmp;
    }

    public int getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(int starlevel) {
        this.starlevel = starlevel;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getStardate() {
        return stardate;
    }

    public void setStardate(String stardate) {
        this.stardate = stardate;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNationFlag() {
        return nationFlag;
    }

    public void setNationFlag(String nationFlag) {
        this.nationFlag = nationFlag;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getAnchorLevel() {
        return anchorLevel;
    }

    public void setAnchorLevel(int anchorLevel) {
        this.anchorLevel = anchorLevel;
    }

    public int getnType() {
        return nType;
    }

    public void setnType(int nType) {
        this.nType = nType;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "GetRoomHotNewSome{" +
                "pos=" + pos +
                ", roomid=" + roomid +
                ", useridx=" + useridx +
                ", gender=" + gender +
                ", serverid=" + serverid +
                ", lrCurrent=" + lrCurrent +
                ", allnum=" + allnum +
                ", userid='" + userid + '\'' +
                ", myname='" + myname + '\'' +
                ", smallpic='" + smallpic + '\'' +
                ", bigpic='" + bigpic + '\'' +
                ", gps='" + gps + '\'' +
                ", flv='" + flv + '\'' +
                ", rtmp='" + rtmp + '\'' +
                ", starlevel=" + starlevel +
                ", familyName='" + familyName + '\'' +
                ", stardate=" + stardate +
                ", isSign=" + isSign +
                ", nation='" + nation + '\'' +
                ", nationFlag='" + nationFlag + '\'' +
                ", gameid=" + gameid +
                ", anchorLevel=" + anchorLevel +
                ", nType=" + nType +
                ", totalCount=" + totalCount +
                '}';
    }
}
