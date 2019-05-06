package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

import me.yokeyword.indexablerv.IndexableEntity;

public class CityBean implements IndexableEntity,Serializable{
    private int agencyId;
    private int enable;
    private int level;
    private String parentId;
    private String regionId;
    private String regionName;
    private double serviceFee;

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParentId() {
        return parentId == null ? "" : parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRegionId() {
        return regionId == null ? "" : regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName == null ? "" : regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    @Override
    public String getFieldIndexBy() {
        return regionName;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.regionName = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}
