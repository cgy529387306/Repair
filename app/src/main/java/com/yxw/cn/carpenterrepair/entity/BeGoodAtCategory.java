package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

/**
 * Created by CY on 2018/12/11
 */
public class BeGoodAtCategory implements Serializable {

    /**
     * id : 63
     * name : 洗衣机
     */

    public BeGoodAtCategory(String id, String name) {
        this.categoryId = id;
        this.categoryName = name;
    }

    private String categoryId;
    private String categoryName;
    private String tagId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
