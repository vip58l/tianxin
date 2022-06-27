package com.tianxin.getlist;

import java.io.Serializable;

public class recommend implements Serializable {
    public String id;
    public String title;
    public  String grade;
    public  String clarity;
    public String description;
    public String index;
    public String verticalPic;
    public String horizontalPic;

    @Override
    public String toString() {
        return "recommend{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", grade='" + grade + '\'' +
                ", clarity='" + clarity + '\'' +
                ", description='" + description + '\'' +
                ", index='" + index + '\'' +
                ", verticalPic='" + verticalPic + '\'' +
                ", horizontalPic='" + horizontalPic + '\'' +
                '}';
    }
}
