package com.tianxin.getlist;

import org.json.JSONArray;

import java.io.Serializable;

public class getonemh  implements Serializable {
    public String chapnum;
    public JSONArray chapinfo;
    public String imghead;
    public JSONArray chapimgs;
    @Override
    public String toString() {
        return "getonemh{" +
                "chapnum='" + chapnum + '\'' +
                ", chapinfo=" + chapinfo +
                ", imghead='" + imghead + '\'' +
                ", chapimgs=" + chapimgs +
                '}';
    }
}
