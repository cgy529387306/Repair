package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {

    private int dictId;
    private int parentId;
    private String name;
    private List<Category> childList;
    private boolean selected;

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getChildList() {
        if (childList == null) {
            return new ArrayList<>();
        }
        return childList;
    }

    public void setChildList(List<Category> childList) {
        this.childList = childList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
