package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class NoticeBean implements Serializable {

    /**
     * content : Rookie测试数据
     * createTime : 2019-05-23 10:54:16
     * createUser : 1
     * isShow : 1
     * noticeId : 1
     * remark : 1
     * site : 0,1,2
     * title : 测试数据
     * url : 111
     * userId : 7812835463187701760
     */
    private String content;
    private String createTime;
    private String createUser;
    private int isShow;//状态 0：隐藏 1：显示
    private String noticeId;
    private String remark;
    private String title;
    private String url;//图片链接
    private String userId;

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public String getNoticeId() {
        return noticeId == null ? "" : noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
