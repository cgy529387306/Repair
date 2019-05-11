package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class TradeItem implements Serializable {

    /**
     * amount : 1.0
     * auditStatus : 1
     * endTime : null
     * operatorId : 1
     * outTradeNo : null
     * recordId : 7815480998414319616
     * remark : [18059131279]申请提现
     * startTime : 2019-05-11 17:14:48
     * tradeStatus : 1
     * tradeType : 0
     * tradeWay : 0
     * userId : 7813688003872198656
     */

    private double amount;
    private int auditStatus;
    private String operatorId;
    private String recordId;
    private String remark;
    private String startTime;
    private int tradeStatus;
    private int tradeType;
    private int tradeWay;
    private String userId;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getOperatorId() {
        return operatorId == null ? "" : operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getRecordId() {
        return recordId == null ? "" : recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime == null ? "" : startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public int getTradeType() {
        return tradeType;
    }

    public void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    public int getTradeWay() {
        return tradeWay;
    }

    public void setTradeWay(int tradeWay) {
        this.tradeWay = tradeWay;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
