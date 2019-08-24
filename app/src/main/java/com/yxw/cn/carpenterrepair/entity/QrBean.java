package com.yxw.cn.carpenterrepair.entity;

import java.io.Serializable;

public class QrBean implements Serializable {
    /**
     * appSite : 1
     * appType : 1
     * codeId : 1
     * url : https://jxdj2.oss-cn-shenzhen.aliyuncs.com/code/Android/MtLS.png
     */

    private int appSite;//1：工程师端 2：服务商端
    private int appType;//app客户端（1:Android 2:IOS)
    private int codeId;
    private String url;

    public int getAppSite() {
        return appSite;
    }

    public void setAppSite(int appSite) {
        this.appSite = appSite;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
