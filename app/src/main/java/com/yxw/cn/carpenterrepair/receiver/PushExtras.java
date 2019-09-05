package com.yxw.cn.carpenterrepair.receiver;

/**
 * Created by cgy on 18/10/20.
 */

public class PushExtras {

    private String id;//orderId

    private String messageType;


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
}
