package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

/**
 * Created by cgy on 19/4/14.
 */

public class OrderType implements Serializable{

    /**
     * 0:待接单  1：待预约  2：待完成 3：待上门  4：已完成
     *
     * 订单整体状态 0待派单1待预约2待上门3待完成4已完成5待完单审核6取消单7异常单8未完成
     */
    private int status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderType(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public OrderType(int status, int drawableId, String name) {
        this.status = status;
        this.drawableId = drawableId;
        this.name = name;
    }
}
