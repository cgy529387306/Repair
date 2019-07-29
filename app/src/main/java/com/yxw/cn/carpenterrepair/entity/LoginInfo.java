package com.yxw.cn.carpenterrepair.entity;

import com.yxw.cn.carpenterrepair.util.Helper;

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
    private String category;
    private long expire;
    private int role;  //角色 0用户 1兼职工程师 2专职工程师
    private int serviceStatus;
    private String idCardBack;
    private String idCardFront;
    private String idCardHand;
    private String residentArea;
    private String residentAreaName;
    private String serviceDate;
    private String serviceTime;
    private int idCardStatus;//身份证状态 0未上传 1已上传 2审核未通过 3审核通过
    private String carryAmount;//可提现金额
    private String deposit;//押金
    private long refreshTime;
    private String sex;
    private String pName;
    private String jobNumber;


    public long getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(long refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getIdCardNo() {
        return idCardNo == null ? "" : idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    private String settlementAmount;//待结算金额
    private String bindingCode;
    private int isRest;//是否休息(0工作中1休息中）
    private String idCardNo;


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

    public String getCategory() {
        return category == null ? "" : category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
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

    public String getResidentArea() {
        return residentArea == null ? "" : residentArea;
    }

    public void setResidentArea(String residentArea) {
        this.residentArea = residentArea;
    }

    public String getResidentAreaName() {
        return residentAreaName == null ? "" : residentAreaName;
    }

    public void setResidentAreaName(String residentAreaName) {
        this.residentAreaName = residentAreaName;
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

    public String getCarryAmount() {
        return carryAmount == null ? "" : carryAmount;
    }

    public void setCarryAmount(String carryAmount) {
        this.carryAmount = carryAmount;
    }

    public String getDeposit() {
        return deposit == null ? "" : deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getSettlementAmount() {
        return settlementAmount == null ? "" : settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public String getBindingCode() {
        return bindingCode == null ? "" : bindingCode;
    }

    public void setBindingCode(String bindingCode) {
        this.bindingCode = bindingCode;
    }

    public int getIsRest() {
        return isRest;
    }

    public void setIsRest(int isRest) {
        this.isRest = isRest;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getpName() {
        return pName == null ? "" : pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getJobNumber() {
        return jobNumber == null ? "" : jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }
}
