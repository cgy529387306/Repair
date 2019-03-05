package com.yxw.cn.carpenterrepair.entity;

public class Asset {

    private float carryAmount;
    private float deposit;
    private float settlementAmount;
    private int userId;
    private String username;
    private String avatar;

    public float getCarryAmount() {
        return carryAmount;
    }

    public void setCarryAmount(float carryAmount) {
        this.carryAmount = carryAmount;
    }

    public float getDeposit() {
        return deposit;
    }

    public void setDeposit(float deposit) {
        this.deposit = deposit;
    }

    public float getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(float settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
