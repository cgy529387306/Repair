package com.yxw.cn.carpenterrepair;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.crash.CrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import io.rong.imkit.RongIM;
import okhttp3.OkHttpClient;
import ui.LocationService;


public class BaseApplication extends Application {

    private static BaseApplication mInstance;
    public LocationService locationService;
    private List<Activity> mAllActivities;

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        locationService = new LocationService(getApplicationContext());
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        mInstance = this;
        init();
    }

    public Activity getLastActivity() {
        if (mAllActivities != null && mAllActivities.size() > 0) {
            return mAllActivities.get(mAllActivities.size() - 1);
        } else {
            return null;
        }
    }

    public void addActivity(Activity act) {
        if (mAllActivities == null) {
            mAllActivities = new ArrayList<>();
        }
        mAllActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (mAllActivities != null) {
            mAllActivities.remove(act);
        }
    }

    public void exitApp() {
        if (mAllActivities != null) {
            synchronized (mAllActivities) {
                for (Activity act : mAllActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 初始化第三方控件
     */
    private void init() {
        OkGo.getInstance().init(this);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        OkGo.getInstance().setOkHttpClient(builder.build());
        Logger.addLogAdapter(new AndroidLogAdapter());
        CrashHandler.getInstance().init(this);
        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
//            RongIM.init(this);//初始化,然后记得在清单文件配置此类。
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

