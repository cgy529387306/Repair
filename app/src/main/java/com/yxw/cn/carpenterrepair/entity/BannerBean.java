package com.yxw.cn.carpenterrepair.entity;

public class BannerBean {


    /**
     * createTime : 2019-05-23 14:36:25
     * createUser : 1
     * isShow : 1
     * path : 1
     * pictureId : 1
     * pictureName : 1
     * place : 1
     * remark : 1
     * site : 1
     * url : 1
     */
    private String createTime;
    private String createUser;
    private int isShow;//状态 0：隐藏 1：显示
    private String path;
    private String pictureId;
    private String pictureName;
    private int place;//图片所属位置0：用户端 1：工程师端 2：服务商端，例:0、1、2
    private String remark;
    private int site;//图片位置1：启动页 2：工作台轮播
    private String url;//图片链接

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser == null ? "" : createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getPath() {
        return path == null ? "" : path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPictureId() {
        return pictureId == null ? "" : pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getPictureName() {
        return pictureName == null ? "" : pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSite() {
        return site;
    }

    public void setSite(int site) {
        this.site = site;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
