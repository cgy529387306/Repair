package com.yxw.cn.carpenterrepair.entity;

public class OperateResult {

    /**
     * acceptId : 7818417497285132288
     * orderStatus : 40
     */

    private String acceptId;
    private int orderStatus;

    public String getAcceptId() {
        return acceptId == null ? "" : acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
