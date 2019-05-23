package com.yxw.cn.carpenterrepair.entity;

import java.util.ArrayList;
import java.util.List;

public class BannerListData extends BaseListResp{
    private List<BannerBean> items;

    public List<BannerBean> getItems() {
        if (items == null) {
            return new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<BannerBean> items) {
        this.items = items;
    }
}
