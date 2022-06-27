package com.tencent.opensource.model;

/**
 * 第三方支付返回结果
 */
public class byprice {
    private int id;
    private String name;
    private int opentype;
    private String channelid;
    private int status;
    private int sort;
    private String note;
    private String tag;
    private String iconurl;
    private int isrecommand;
    private String opentime;
    private boolean checkbox;

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpentype() {
        return opentype;
    }

    public void setOpentype(int opentype) {
        this.opentype = opentype;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public int getIsrecommand() {
        return isrecommand;
    }

    public void setIsrecommand(int isrecommand) {
        this.isrecommand = isrecommand;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    @Override
    public String toString() {
        return "playeeee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", opentype=" + opentype +
                ", channelid='" + channelid + '\'' +
                ", status=" + status +
                ", sort=" + sort +
                ", note='" + note + '\'' +
                ", tag='" + tag + '\'' +
                ", iconurl='" + iconurl + '\'' +
                ", isrecommand=" + isrecommand +
                ", opentime='" + opentime + '\'' +
                '}';
    }
}
