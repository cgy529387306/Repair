package com.yxw.cn.carpenterrepair.entity;

import java.util.List;

/**
 * Created by CY on 2018/12/8
 */
public class Complain {

    private int orderId;
    private String mobile;
    private String content;
    private List<Picture> pictures;

    public Complain(int orderId, String mobile, String content, List<Picture> pictures) {
        this.orderId = orderId;
        this.mobile = mobile;
        this.content = content;
        this.pictures = pictures;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public static class Picture {

        private String url;
        private String name;
        private String ext;
        private String desc;
        private int size;
        private int sortNo;

        public Picture(String url) {
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSortNo() {
            return sortNo;
        }

        public void setSortNo(int sortNo) {
            this.sortNo = sortNo;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
