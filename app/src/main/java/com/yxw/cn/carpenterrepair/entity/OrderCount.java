package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class OrderCount implements Serializable {

    private int count;
    private int type;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public OrderCount(int count, int type) {
        this.count = count;
        this.type = type;
    }
}
