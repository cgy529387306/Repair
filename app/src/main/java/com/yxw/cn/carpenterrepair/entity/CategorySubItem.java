package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class CategorySubItem implements Serializable {

    private int id;
    private String name;
    private String keywords;
    private String description;
    private int parentId;
    private int showIndex;
    private int isShow;
    private String bannerUrl;
    private String iconUrl;
    private String imgUrl;
    private int type;
    private float price;
    private float specialPrice;
    private String shareType;
    private int shareValue;
    private int isLeaf;
    private int show;
    private String parentName;
    private String sub;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getShowIndex() {
        return showIndex;
    }

    public void setShowIndex(int showIndex) {
        this.showIndex = showIndex;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(int specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public int getShareValue() {
        return shareValue;
    }

    public void setShareValue(int shareValue) {
        this.shareValue = shareValue;
    }

    public int getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }
}
