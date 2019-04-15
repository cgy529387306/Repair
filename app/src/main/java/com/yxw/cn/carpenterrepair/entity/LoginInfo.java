package com.yxw.cn.carpenterrepair.entity;

import java.util.ArrayList;
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

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastLoginTime() {
        return lastLoginTime == null ? "" : lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar == null ? "" : avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname == null ? "" : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<BeGoodAtCategory> getTags() {
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<BeGoodAtCategory> tags) {
        this.tags = tags;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getIdentityCard() {
        return identityCard == null ? "" : identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getIdentityCardFront() {
        return identityCardFront == null ? "" : identityCardFront;
    }

    public void setIdentityCardFront(String identityCardFront) {
        this.identityCardFront = identityCardFront;
    }

    public String getIdentityCardBack() {
        return identityCardBack == null ? "" : identityCardBack;
    }

    public void setIdentityCardBack(String identityCardBack) {
        this.identityCardBack = identityCardBack;
    }

    public String getResidentName() {
        return residentName == null ? "" : residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getResident() {
        return resident == null ? "" : resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getServiceDate() {
        return serviceDate == null ? "" : serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceTime() {
        return serviceTime == null ? "" : serviceTime;
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

    public String getAliplayAccount() {
        return aliplayAccount == null ? "" : aliplayAccount;
    }

    public void setAliplayAccount(String aliplayAccount) {
        this.aliplayAccount = aliplayAccount;
    }
}
