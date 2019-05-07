package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class OrderStatusLineBean implements Serializable{
    /**
     * acceptId : null
     * description : 管理员[admin]导入订单[JDA12019050300004]导入成功
     * orderId : 7812652845701304320
     * orderStatus : 20
     * relevanceId : null
     * showUser : 0
     * time : 2019-05-03 21:56:44
     * timelineId : null
     */

    private String acceptId;
    private String description;
    private String orderId;
    private int orderStatus;
    private String relevanceId;
    private int showUser;
    private String time;
    private String timelineId;

    public String getAcceptId() {
        return acceptId == null ? "" : acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getRelevanceId() {
        return relevanceId == null ? "" : relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }

    public int getShowUser() {
        return showUser;
    }

    public void setShowUser(int showUser) {
        this.showUser = showUser;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimelineId() {
        return timelineId == null ? "" : timelineId;
    }

    public void setTimelineId(String timelineId) {
        this.timelineId = timelineId;
    }
}
