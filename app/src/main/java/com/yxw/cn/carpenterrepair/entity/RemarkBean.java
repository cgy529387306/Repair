package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class RemarkBean implements Serializable {


    /**
     * creationTime : 2019-08-01 18:35:43
     * operationUserId : 1
     * orderContent : 1123331
     * orderId : 7845215877433720832
     * orderStatus : 27
     * realName : 总部后台超级管理员
     */

    private String creationTime;
    private String operationUserId;
    private String orderContent;
    private String orderId;
    private int orderStatus;
    private String realName;

    public String getCreationTime() {
        return creationTime == null ? "" : creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getOperationUserId() {
        return operationUserId == null ? "" : operationUserId;
    }

    public void setOperationUserId(String operationUserId) {
        this.operationUserId = operationUserId;
    }

    public String getOrderContent() {
        return orderContent == null ? "" : orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public String getOrderId() {
        return orderId == null ? "" : orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getRealName() {
        return realName == null ? "" : realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
