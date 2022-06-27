/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/20 0020
 */


package com.tencent.opensource.model;

import java.util.List;

public class City {
    private String name;
    private List<String> area;

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", area=" + area +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}
