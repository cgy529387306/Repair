package com.yxw.cn.carpenterrepair.entity;

import java.util.List;

/**
 * Created by CY on 2018/12/8
 */
public class InsertCategory {

    private List<String> ids;

    public InsertCategory(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
