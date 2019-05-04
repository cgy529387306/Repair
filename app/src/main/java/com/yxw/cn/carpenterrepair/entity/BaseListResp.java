package com.yxw.cn.carpenterrepair.entity;

public class BaseListResp {
    private int currPage;
    private int pageSize;
    private int totalCount;
    private int totalPage;
    private boolean hasNext;

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isHasNext() {
        return currPage<totalPage;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
