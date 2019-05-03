package com.yxw.cn.carpenterrepair.entity;

/**
 * Created by cgy on 19/5/4.
 */

public class OrderFilterRequest {
    private String customerBookingTime;
    private String orderStatus;

    public OrderFilterRequest(String customerBookingTime, String orderStatus) {
        this.customerBookingTime = customerBookingTime;
        this.orderStatus = orderStatus;
    }

    public String getCustomerBookingTime() {
        return customerBookingTime == null ? "" : customerBookingTime;
    }

    public void setCustomerBookingTime(String customerBookingTime) {
        this.customerBookingTime = customerBookingTime;
    }

    public String getOrderStatus() {
        return orderStatus == null ? "" : orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
