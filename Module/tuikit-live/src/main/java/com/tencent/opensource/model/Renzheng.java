/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/26 0026
 */


package com.tencent.opensource.model;

public class Renzheng {
    private int status;
    private int member;
    private String msg;
    private String ok;
    private int video;
    private int cover;
    private int photo;
    private int sex;
    private int tRole;
    private int vip;
    private int level;
    private int trend;
    private int follow;
    private String alias;
    private String picture;
    private attestation attestation;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int gettRole() {
        return tRole;
    }

    public void settRole(int tRole) {
        this.tRole = tRole;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public com.tencent.opensource.model.attestation getAttestation() {
        return attestation;
    }

    public void setAttestation(com.tencent.opensource.model.attestation attestation) {
        this.attestation = attestation;
    }

    @Override
    public String toString() {
        return "Renzheng{" +
                "status=" + status +
                ", member=" + member +
                ", msg='" + msg + '\'' +
                ", ok='" + ok + '\'' +
                ", video=" + video +
                ", cover=" + cover +
                ", photo=" + photo +
                ", sex=" + sex +
                ", tRole=" + tRole +
                ", vip=" + vip +
                ", level=" + level +
                ", trend=" + trend +
                ", follow=" + follow +
                ", alias='" + alias + '\'' +
                ", picture='" + picture + '\'' +
                ", attestation=" + attestation +
                '}';
    }
}
