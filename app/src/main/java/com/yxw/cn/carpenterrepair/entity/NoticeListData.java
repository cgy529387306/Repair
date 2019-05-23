package com.yxw.cn.carpenterrepair.entity;

import java.util.ArrayList;
import java.util.List;

public class NoticeListData extends BaseListResp{
    private List<NoticeBean> items;

    public List<NoticeBean> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<NoticeBean> items) {
        this.items = items;
    }
}
