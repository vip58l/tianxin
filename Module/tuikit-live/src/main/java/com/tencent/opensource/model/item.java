package com.tencent.opensource.model;

import java.io.Serializable;

public class item implements Serializable {
    public int type;
    public Object object;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
