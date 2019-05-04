package com.yxw.cn.carpenterrepair.entity;

import java.util.ArrayList;
import java.util.List;

public class OrderListData extends BaseListResp{

    private List<OrderItem> items;

    public List<OrderItem> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
