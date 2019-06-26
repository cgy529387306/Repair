package com.yxw.cn.carpenterrepair.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.LocationUtils;
import com.yxw.cn.carpenterrepair.util.PreferencesHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

/**
 * 同步位置服务
 */
public class SyncLocationService extends Service {

    public static long REFRESH_TIME = 60*1000;

    private MyTimeTask mTimeTask;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getRefreshTime()!=0){
            REFRESH_TIME = CurrentUser.getInstance().getRefreshTime()*1000;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        setTimerTask();
        return super.onStartCommand(intent, flags, startId);
    }

    private void setTimerTask(){
        Log.d("REFRESH_TIME",REFRESH_TIME+"");
        mTimeTask = new MyTimeTask(REFRESH_TIME, new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
            }
        });
        mTimeTask.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    //在此执行定时操作
                    LocationUtils.instance().requestLocation(new BDAbstractLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            if (bdLocation!=null && bdLocation.getLatitude() != 4.9E-324 && bdLocation.getLongitude() != 4.9E-324){
                                PreferencesHelper.getInstance().putString("latitude",bdLocation.getLatitude()+"");
                                PreferencesHelper.getInstance().putString("longitude",bdLocation.getLongitude()+"");
                                EventBusUtil.post(MessageConstant.MY_LOCATION,bdLocation);
                                refreshLocation(bdLocation);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    public void refreshLocation(BDLocation bdLocation){
        if (CurrentUser.getInstance().isLogin()){
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("userId", CurrentUser.getInstance().getUserId());
            requestMap.put("locationLat", bdLocation.getLatitude());
            requestMap.put("locationLng", bdLocation.getLongitude());
            OkGo.<ResponseData<Object>>post(UrlConstant.UPDATE_LOCATION)
                    .upJson(new Gson().toJson(requestMap))
                    .execute(new JsonCallback<ResponseData<Object>>() {
                                 @Override
                                 public void onSuccess(ResponseData<Object> response) {
                                     if (response!=null && response.isSuccess()){
                                         Log.d("refreshLocation","locationLat:"+bdLocation.getLatitude()+",locationLng:"+bdLocation.getLongitude());
                                     }
                                 }
                             }
                    );
        }

    }


}
