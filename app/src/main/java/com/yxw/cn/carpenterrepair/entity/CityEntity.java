package com.yxw.cn.carpenterrepair.entity;


import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by YoKey on 16/10/8.
 */
public class CityEntity implements IndexableEntity {
    private String name;
    private String id;

    public CityEntity(String name) {
        this.name = name;
    }


    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        // 需要用到拼音时(比如:搜索), 可增添pinyin字段 this.pinyin  = pinyin
        // 见 CityEntity
    }
}
