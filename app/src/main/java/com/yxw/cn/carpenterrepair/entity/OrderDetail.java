package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderDetail extends OrderItem implements Serializable{
    private String parentCategoryName;
//    private List<?> fixOrderPicViewRespIOList;
    private List<OrderStatusLineBean> fixOrderTimelineViewRespIOList;

    public List<OrderStatusLineBean> getFixOrderTimelineViewRespIOList() {
        if (fixOrderTimelineViewRespIOList == null) {
            return new ArrayList<>();
        }
        return fixOrderTimelineViewRespIOList;
    }


    public void setFixOrderTimelineViewRespIOList(List<OrderStatusLineBean> fixOrderTimelineViewRespIOList) {
        this.fixOrderTimelineViewRespIOList = fixOrderTimelineViewRespIOList;
    }

    public String getParentCategoryName() {
        return parentCategoryName == null ? "" : parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }
}
