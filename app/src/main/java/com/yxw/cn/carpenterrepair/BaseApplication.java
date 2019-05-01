package com.yxw.cn.carpenterrepair;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yxw.cn.carpenterrepair.service.InitService;


public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        InitService.startActionInit(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

