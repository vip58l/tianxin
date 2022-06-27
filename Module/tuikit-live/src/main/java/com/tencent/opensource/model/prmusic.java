/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/19 0019
 */


package com.tencent.opensource.model;

import java.util.List;

public class prmusic {
    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private List<Music> data;
    @Override
    public String toString() {
        return "prmusic{" +
                "total=" + total +
                ", per_page=" + per_page +
                ", current_page=" + current_page +
                ", last_page=" + last_page +
                ", music=" + data +
                '}';
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public List<Music> getMusic() {
        return data;
    }

    public void setMusic(List<Music> music) {
        this.data = music;
    }
}
