package com.yxw.cn.carpenterrepair.entity;

import java.util.List;

public class LoginInfo {

    private String token;
    private String lastLoginTime;
    private String mobile;
    private String avatar;
    private String username;
    private String userId;
    private String nickname;
    private List<BeGoodAtCategory> tags;
    private long expire;
    private int role;  //角色 0用户 1兼职工程师 2专职工程师
    private String identityCard;
    private String identityCardFront;
    private String identityCardBack;
    private String residentName;
    private String resident;
    private String serviceDate;
    private String serviceTime;
    private int idCardStatus;//身份证状态 0未上传 1已上传 2审核未通过 3审核通过
    private String aliplayAccount;

    public String getAliplayAccount() {
        return aliplayAccount;
    }

    public void setAliplayAccount(String aliplayAccount) {
        this.aliplayAccount = aliplayAccount;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getIdCardStatus() {
        return idCardStatus;
    }

    public void setIdCardStatus(int idCardStatus) {
        this.idCardStatus = idCardStatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<BeGoodAtCategory> getTags() {
        return tags;
    }

    public void setTags(List<BeGoodAtCategory> tags) {
        this.tags = tags;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getIdentityCardFront() {
        return identityCardFront;
    }

    public void setIdentityCardFront(String identityCardFront) {
        this.identityCardFront = identityCardFront;
    }

    public String getIdentityCardBack() {
        return identityCardBack;
    }

    public void setIdentityCardBack(String identityCardBack) {
        this.identityCardBack = identityCardBack;
    }
}
