package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class OrderItem implements Serializable {

    /**
     * address : 福建省福州市鼓楼区软件园40号2楼
     * agencyId : 350102
     * bookingEndTime : null
     * bookingStartTime : null
     * categoryId : 43
     * city : 福州市
     * createTime : 2019-05-03 21:56:44
     * district : 鼓楼区
     * endTime : null
     * fixDesc : null
     * handleStatus : 1
     * locationLat : 26.088114
     * locationLng : 119.310492
     * mobile : 13800000000
     * name : 张三
     * orderId : 7812652845701304320
     * orderSn : JDA12019050300004
     * orderSnOriginal : null
     * payStatus : 1
     * province : 福建省
     * receiveStatus : 0
     * remark :
     * source : 4
     * totalPrice : 150.0
     * userId : null
     */

    private String address;
    private int agencyId;
    private String bookingEndTime;
    private String bookingStartTime;
    private int categoryId;
    private String city;
    private String createTime;
    private String district;
    private String endTime;
    private String fixDesc;
    private int handleStatus;
    private double locationLat;
    private double locationLng;
    private String mobile;
    private String name;
    private String orderId;
    private String orderSn;
    private String orderSnOriginal;
    private int payStatus;
    private String province;
    private int receiveStatus;
    private String remark;
    private int source;
    private double totalPrice;
    private String userId;
    private int orderStatus;
    private String orderStatusName;
    private String categoryCName;//维修小类
    private String categoryPName;//维修大类

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public String getBookingEndTime() {
        return bookingEndTime == null ? "" : bookingEndTime;
    }

    public void setBookingEndTime(String bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public String getBookingStartTime() {
        return bookingStartTime == null ? "" : bookingStartTime;
    }

    public void setBookingStartTime(String bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCity() {
        return city == null ? "" : city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDistrict() {
        return district == null ? "" : district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEndTime() {
        return endTime == null ? "" : endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getFixDesc() {
        return fixDesc == null ? "" : fixDesc;
    }

    public void setFixDesc(String fixDesc) {
        this.fixDesc = fixDesc;
    }

    public int getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(int handleStatus) {
        this.handleStatus = handleStatus;
    }

    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLng() {
        return locationLng;
    }

    public void setLocationLng(double locationLng) {
        this.locationLng = locationLng;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId == null ? "" : orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSn() {
        return orderSn == null ? "" : orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderSnOriginal() {
        return orderSnOriginal == null ? "" : orderSnOriginal;
    }

    public void setOrderSnOriginal(String orderSnOriginal) {
        this.orderSnOriginal = orderSnOriginal;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getProvince() {
        return province == null ? "" : province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(int receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderStatus(){
        return orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName == null ? "" : orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getCategoryCName() {
        return categoryCName == null ? "" : categoryCName;
    }

    public void setCategoryCName(String categoryCName) {
        this.categoryCName = categoryCName;
    }

    public String getCategoryPName() {
        return categoryPName == null ? "" : categoryPName;
    }

    public void setCategoryPName(String categoryPName) {
        this.categoryPName = categoryPName;
    }



}
