package com.yxw.cn.carpenterrepair.entity;

public class ResponseData<T> {

    private String msg;
    private int status;
    private T data;
    private boolean success;
    private int dicts;

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getDicts() {
        return dicts;
    }

    public void setDicts(int dicts) {
        this.dicts = dicts;
    }
}
