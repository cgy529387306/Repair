package com.yxw.cn.carpenterrepair.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by CY on 2018/11/30
 */
public class OrderUpload implements MultiItemEntity {

    public static final int UPLOAD = 1;
    public static final int ADD = 2;
    private int itemType;
    private String path;

    public OrderUpload(String path) {
        this.itemType = UPLOAD;
        this.path=path;
    }

    public OrderUpload(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
