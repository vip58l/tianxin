package com.tencent.opensource.model;

import java.util.Date;

public class Live_RoomSelect {
    private int pos;
    private long roomid;
    private long useridx;
    private int gender;
    private int serverid;
    private int lrcurrent;
    private int allnum;
    private String userid;
    private String myname;
    private String smallpic;
    private String bigpic;
    private String gps;
    private String flv;
    private String rtmp;
    private int starlevel;
    private String familyname;
    private String stardate;
    private int issign;
    private String nation;
    private String nationflag;
    private int gameid;
    private int anchorlevel;
    private int ntype;
    private int totalcount;

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

    public int getLrcurrent() {
        return lrcurrent;
    }

    public void setLrcurrent(int lrcurrent) {
        this.lrcurrent = lrcurrent;
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

    public String getFamilyname() {
        return familyname;
    }

    public void setFamilyname(String familyname) {
        this.familyname = familyname;
    }

    public String getStardate() {
        return stardate;
    }

    public void setStardate(String stardate) {
        this.stardate = stardate;
    }

    public int getIssign() {
        return issign;
    }

    public void setIssign(int issign) {
        this.issign = issign;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNationflag() {
        return nationflag;
    }

    public void setNationflag(String nationflag) {
        this.nationflag = nationflag;
    }

    public int getGameid() {
        return gameid;
    }

    public void setGameid(int gameid) {
        this.gameid = gameid;
    }

    public int getAnchorlevel() {
        return anchorlevel;
    }

    public void setAnchorlevel(int anchorlevel) {
        this.anchorlevel = anchorlevel;
    }

    public int getNtype() {
        return ntype;
    }

    public void setNtype(int ntype) {
        this.ntype = ntype;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    @Override
    public String toString() {
        return "Live_RoomSelect{" +
                "pos=" + pos +
                ", roomid=" + roomid +
                ", useridx=" + useridx +
                ", gender=" + gender +
                ", serverid=" + serverid +
                ", lrcurrent=" + lrcurrent +
                ", allnum=" + allnum +
                ", userid='" + userid + '\'' +
                ", myname='" + myname + '\'' +
                ", smallpic='" + smallpic + '\'' +
                ", bigpic='" + bigpic + '\'' +
                ", gps='" + gps + '\'' +
                ", flv='" + flv + '\'' +
                ", rtmp='" + rtmp + '\'' +
                ", starlevel=" + starlevel +
                ", familyname='" + familyname + '\'' +
                ", stardate=" + stardate +
                ", issign=" + issign +
                ", nation='" + nation + '\'' +
                ", nationflag='" + nationflag + '\'' +
                ", gameid=" + gameid +
                ", anchorlevel=" + anchorlevel +
                ", ntype=" + ntype +
                ", totalcount=" + totalcount +
                '}';
    }
}
