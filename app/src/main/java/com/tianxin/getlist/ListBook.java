package com.tianxin.getlist;

import java.io.Serializable;

public class ListBook implements Serializable {
    public String author;
    public String bookid;
    public String booksize;
    public String booktype;
    public String cover;
    public String descc;
    public String id;
    public String name;
    public String tag;
    public String txturl;
    public String type;
    public String type_id;

    @Override
    public String toString() {
        return "ListBook{" +
                "author='" + author + '\'' +
                ", bookid='" + bookid + '\'' +
                ", booksize='" + booksize + '\'' +
                ", booktype='" + booktype + '\'' +
                ", cover='" + cover + '\'' +
                ", descc='" + descc + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", txturl='" + txturl + '\'' +
                ", type='" + type + '\'' +
                ", type_id='" + type_id + '\'' +
                '}';
    }
}
