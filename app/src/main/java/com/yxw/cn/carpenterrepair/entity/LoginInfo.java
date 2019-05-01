package com.yxw.cn.carpenterrepair.entity;

import com.yxw.cn.carpenterrepair.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class LoginInfo {
    private String aliplayAccount;
    private String avatar;
    private String lastLoginTime;
    private String mobile;
    private String nickname;
    private String token;
    private String userName;
    private String realName;
    private String userId;
    private String parentId;
    private String refreshToken;
    private String registerTime;
    private List<BeGoodAtCategory> tags;
    private long expire;
    private int role;  //角色 0用户 1兼职工程师 2专职工程师
    private String idCardBack;
    private String idCardFront;
    private String idCardHand;
    private String residentName;
    private String resident;
    private String serviceDate;
    private String serviceTime;
    private int idCardStatus;//身份证状态 0未上传 1已上传 2审核未通过 3审核通过


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

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName == null ? "" : realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return Helper.isEmpty(nickname)?userName:nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getParentId() {
        return parentId == null ? "" : parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getIdCardBack() {
        return idCardBack == null ? "" : idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardFront() {
        return idCardFront == null ? "" : idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getIdCardHand() {
        return idCardHand == null ? "" : idCardHand;
    }

    public void setIdCardHand(String idCardHand) {
        this.idCardHand = idCardHand;
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

    public String getRefreshToken() {
        return refreshToken == null ? "" : refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRegisterTime() {
        return registerTime == null ? "" : registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
}
