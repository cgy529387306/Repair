package com.yxw.cn.carpenterrepair.entity;

public class PicUrl {

    private String url;
    private String id;
    private int orderId;
    private String desc;
    private String sortNo;

    public PicUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSortNo() {
        return sortNo;
    }

    public void setSortNo(String sortNo) {
        this.sortNo = sortNo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
