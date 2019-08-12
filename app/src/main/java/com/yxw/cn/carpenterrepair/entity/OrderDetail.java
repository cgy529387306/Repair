package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail extends OrderItem implements Serializable{
    private String categoryName;//维修小类
    private String parentCategoryName;//维修大类
    private String categoryNameJoint;
//    private List<?> fixOrderPicViewRespIOList;
    private List<OrderStatusLineBean> fixOrderTimelineViewRespIOList;
    private List<RemarkBean> remarkList;


    public String getCategoryName() {
        return categoryName == null ? "" : categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentCategoryName() {
        return parentCategoryName == null ? "" : parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public String getCategoryNameJoint() {
        return categoryNameJoint == null ? "" : categoryNameJoint;
    }

    public void setCategoryNameJoint(String categoryNameJoint) {
        this.categoryNameJoint = categoryNameJoint;
    }

    public List<OrderStatusLineBean> getFixOrderTimelineViewRespIOList() {
        if (fixOrderTimelineViewRespIOList == null) {
            return new ArrayList<>();
        }
        return fixOrderTimelineViewRespIOList;
    }

    public void setFixOrderTimelineViewRespIOList(List<OrderStatusLineBean> fixOrderTimelineViewRespIOList) {
        this.fixOrderTimelineViewRespIOList = fixOrderTimelineViewRespIOList;
    }

    public List<RemarkBean> getRemarkList() {
        if (remarkList == null) {
            return new ArrayList<>();
        }
        return remarkList;
    }

    public void setRemarkList(List<RemarkBean> remarkList) {
        this.remarkList = remarkList;
    }
}
