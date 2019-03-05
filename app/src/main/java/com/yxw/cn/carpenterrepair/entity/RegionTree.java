package com.yxw.cn.carpenterrepair.entity;

import java.util.List;

public class RegionTree {

    private int id;
    private int parent_id;
    private String name;
    private int type;
    private long agency_id;
    private List<RegionTreeSub> sub;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getAgency_id() {
        return agency_id;
    }

    public void setAgency_id(long agency_id) {
        this.agency_id = agency_id;
    }

    public List<RegionTreeSub> getSub() {
        return sub;
    }

    public void setSub(List<RegionTreeSub> sub) {
        this.sub = sub;
    }
}
