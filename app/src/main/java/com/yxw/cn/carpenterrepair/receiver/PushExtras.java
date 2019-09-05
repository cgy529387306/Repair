package com.yxw.cn.carpenterrepair.receiver;

/**
 * Created by cgy on 18/10/20.
 */

public class PushExtras {

    private String id;//orderId

    private String messageType;

    private String orderStatus;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageType() {
        return messageType == null ? "" : messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getOrderStatus() {
        return orderStatus==null?"0":orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
