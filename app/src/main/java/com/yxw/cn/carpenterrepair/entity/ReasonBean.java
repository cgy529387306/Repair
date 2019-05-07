package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class ReasonBean implements Serializable{


    /**
     * dictId : 10
     * mark : TURN_RESERVATION_REASON
     * markName : 预约异常
     * name : 联系不上客户（电话是空号、电话无人接听、手机关机等）
     * open : 1
     * parentId : 0
     * remark :
     * sort : 0
     * type : 2
     * updateId : 1
     * updateTime : 1547049600000
     * value : 0
     */
    private String dictId;
    private String mark;
    private String markName;
    private String name;
    private String open;
    private String parentId;
    private String remark;
    private int sort;
    private String type;
    private String updateId;
    private long updateTime;
    private String value;

    public String getDictId() {
        return dictId == null ? "" : dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getMark() {
        return mark == null ? "" : mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getMarkName() {
        return markName == null ? "" : markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen() {
        return open == null ? "" : open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getParentId() {
        return parentId == null ? "" : parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateId() {
        return updateId == null ? "" : updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getValue() {
        return value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
