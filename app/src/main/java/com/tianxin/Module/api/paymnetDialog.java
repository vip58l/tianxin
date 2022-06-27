package com.tianxin.Module.api;

import android.app.Dialog;
import android.content.Context;

public abstract class paymnetDialog extends Dialog {
    private String name;
    private String typename;
    private double money;
    private boolean Selected;
    private int type;
    public paymnetDialog(Context context, int fei) {
        super(context, fei);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
