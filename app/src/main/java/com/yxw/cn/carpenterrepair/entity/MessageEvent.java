package com.yxw.cn.carpenterrepair.entity;

public class MessageEvent {

    private String id;
    private boolean success;
    private Object data;

    public MessageEvent(String messageId) {
        this.id = messageId;
        this.success = true;
    }

    public MessageEvent(String messageId, boolean success ) {
        this.id = messageId;
        this.success = success;
    }

    public MessageEvent(String messageId, boolean success, Object data ) {
        this.id = messageId;
        this.success = success;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
