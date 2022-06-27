/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/20 0020
 */


package com.tencent.opensource.model;

import java.util.List;

public class address {
    private String name;
    private List<City> city;

    @Override
    public String toString() {
        return "address{" +
                "name='" + name + '\'' +
                ", city=" + city +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}
