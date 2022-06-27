/**
 * Description :
 * 开发者 小清新 QQ804031885
 *自定义数据转换类
 * @author WSoban
 * @date 2020/12/28 0028
 */

package com.tianxin.getlist;
import org.json.JSONObject;

public class pagevideo {
    private String id;
    private String title;
    private String longMs;
    private String content;
    private String mvUrl;
    private String vediopath;
    private String videopic;
    private String videobg;
    private String imgUrl;
    private JSONObject object;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLongMs() {
        return longMs;
    }

    public void setLongMs(String longMs) {
        this.longMs = longMs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMvUrl() {
        return mvUrl;
    }

    public void setMvUrl(String mvUrl) {
        this.mvUrl = mvUrl;
    }

    public String getVediopath() {
        return vediopath;
    }

    public void setVediopath(String vediopath) {
        this.vediopath = vediopath;
    }

    public String getVideopic() {
        return videopic;
    }

    public void setVideopic(String videopic) {
        this.videopic = videopic;
    }

    public String getVideobg() {
        return videobg;
    }

    public void setVideobg(String videobg) {
        this.videobg = videobg;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "pagevideo{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", longMs='" + longMs + '\'' +
                ", content='" + content + '\'' +
                ", mvUrl='" + mvUrl + '\'' +
                ", vediopath='" + vediopath + '\'' +
                ", videopic='" + videopic + '\'' +
                ", videobg='" + videobg + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", object=" + object +
                '}';
    }
}
