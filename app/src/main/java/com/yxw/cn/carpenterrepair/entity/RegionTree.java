package com.yxw.cn.carpenterrepair.entity;

import java.util.ArrayList;
import java.util.List;

public class RegionTree {

    private int parentId;
    private String regionName;
    private long agencyId;
    private List<RegionTree> children;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getRegionName() {
        return regionName == null ? "" : regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public long getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(long agencyId) {
        this.agencyId = agencyId;
    }

    public List<RegionTree> getChildren() {
        if (children == null) {
            return new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<RegionTree> children) {
        this.children = children;
    }
}
