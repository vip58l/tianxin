package com.tianxin.getlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

//https://wap.yequ.live/api/mainpage/mainPage/hotRooms
public class HotRooms implements Parcelable, Serializable {
    public String sign;
    public String headerImageOriginal;
    public String chargeCoinBalance;
    public String id;
    public String liveStatus;
    public String hlsUrl;
    public String nickName;
    public String rtmpUrl;
    public String position;
    public String hdlUrl;
    public String bigImageOriginal;
    public String name;
    public String gps;
    public String familyName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public HotRooms() {
    }

    protected HotRooms(Parcel in) {
        sign = in.readString();
        headerImageOriginal = in.readString();
        chargeCoinBalance = in.readString();
        id = in.readString();
        liveStatus = in.readString();
        hlsUrl = in.readString();
        nickName = in.readString();
        rtmpUrl = in.readString();
        position = in.readString();
        hdlUrl = in.readString();
        bigImageOriginal = in.readString();
    }

    public static final Creator<HotRooms> CREATOR = new Creator<HotRooms>() {
        @Override
        public HotRooms createFromParcel(Parcel in) {
            return new HotRooms(in);
        }

        @Override
        public HotRooms[] newArray(int size) {
            return new HotRooms[size];
        }
    };

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getHeaderImageOriginal() {
        return headerImageOriginal;
    }

    public void setHeaderImageOriginal(String headerImageOriginal) {
        this.headerImageOriginal = headerImageOriginal;
    }

    public String getChargeCoinBalance() {
        return chargeCoinBalance;
    }

    public void setChargeCoinBalance(String chargeCoinBalance) {
        this.chargeCoinBalance = chargeCoinBalance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(String liveStatus) {
        this.liveStatus = liveStatus;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getHdlUrl() {
        return hdlUrl;
    }

    public void setHdlUrl(String hdlUrl) {
        this.hdlUrl = hdlUrl;
    }

    public String getBigImageOriginal() {
        return bigImageOriginal;
    }

    public void setBigImageOriginal(String bigImageOriginal) {
        this.bigImageOriginal = bigImageOriginal;
    }

    @Override
    public String toString() {
        return "HotRooms{" +
                "sign='" + sign + '\'' +
                ", headerImageOriginal='" + headerImageOriginal + '\'' +
                ", chargeCoinBalance='" + chargeCoinBalance + '\'' +
                ", id='" + id + '\'' +
                ", liveStatus='" + liveStatus + '\'' +
                ", hlsUrl='" + hlsUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", rtmpUrl='" + rtmpUrl + '\'' +
                ", position='" + position + '\'' +
                ", hdlUrl='" + hdlUrl + '\'' +
                ", bigImageOriginal='" + bigImageOriginal + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sign);
        dest.writeString(headerImageOriginal);
        dest.writeString(chargeCoinBalance);
        dest.writeString(id);
        dest.writeString(liveStatus);
        dest.writeString(hlsUrl);
        dest.writeString(nickName);
        dest.writeString(rtmpUrl);
        dest.writeString(position);
        dest.writeString(hdlUrl);
        dest.writeString(bigImageOriginal);
    }



}
