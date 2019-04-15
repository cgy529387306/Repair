package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

/**
 * Created by cgy on 19/4/14.
 */

public class OrderType implements Serializable{

    private int type;

    private int drawableId;

    private String name;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderType(int type,int drawableId, String name) {
        this.type = type;
        this.drawableId = drawableId;
        this.name = name;
    }
}
