package com.yxw.cn.carpenterrepair.entity;

import java.util.ArrayList;
import java.util.List;

public class TradeListData extends BaseListResp{

    private List<TradeItem> items;

    public List<TradeItem> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<TradeItem> items) {
        this.items = items;
    }
}
