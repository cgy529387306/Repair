package com.yxw.cn.carpenterrepair.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseApplication;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.CityBean;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.entity.ReasonBean;
import com.yxw.cn.carpenterrepair.entity.RegionTree;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.TradeItem;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {

    public static List<RegionTree> regionTreeList = new ArrayList<>();
    public static List<Category> categoryItemList = new ArrayList<>();
    public static List<CityBean> cityItemList = new ArrayList<>();
    public static List<ReasonBean> signReasonList = new ArrayList<>();
    public static List<ReasonBean> reservationReasonList = new ArrayList<>();
    private static Gson gson = new Gson();

    /**
     * 防止控件被连续点击
     * @param view
     */
    public static void disableViewDoubleClick(final View view) {
        if(view == null) {
            return;
        }
        view.setEnabled(false);
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);
    }

    public static String getVerName() {
        String verName = "";
        try {
            verName = BaseApplication.getInstance().getPackageManager().
                    getPackageInfo(BaseApplication.getInstance().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    public static int getVersionCode() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = BaseApplication.getInstance().getPackageManager().
                    getPackageInfo(BaseApplication.getInstance().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getOrderDetailAddress(UserOrder.ListBean order){
        return order.getProvince()+order.getCity()+order.getDistrict()+order.getAddress();
    }

    public static String getTradeName(TradeItem item) {
        String tradeName = "";
        if (item==null){
            return tradeName;
        }
        if (item.getTradeType()==0){
            if (item.getTradeWay()==0){
                tradeName = "支付宝提现";
            }else if (item.getTradeWay()==1){
                tradeName = "微信提现";
            }else if (item.getTradeWay()==2){
                tradeName = "银联提现";
            }
        }else if (item.getTradeType()==1){
            tradeName = "交保证金";
        }else if (item.getTradeType()==3){
            tradeName = "订单结算";
        }
        return tradeName;
    }

    public static String getOrderStatus(int orderStatus) {
        //待接单 待预约 待上门 待完成 已完成
        if (orderStatus<=20){
            return "待接单";
        }else if (orderStatus<=40){
            return "待预约";
        }else if (orderStatus<=55){
            return "待上门";
        }else if (orderStatus<90){
            return "待完成";
        }else{
            return "已完成";
        }
    }

    public static String getOrderFinishedStatus(int orderStatus) {
        //待接单 待预约 待上门 待完成 已完成
        if (orderStatus==100){
            return "已审核";
        }else if (orderStatus==110){
            return "已结算";
        }else {
            return "待审核";
        }
    }

    public static void showPic(Context context, ImageView mIv, String url) {
        RequestOptions options = new RequestOptions()
//                .placeholder(R.mipmap.launcher)
                .error(R.mipmap.launcher);
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.mipmap.launcher).into(mIv);
        } else {
            Glide.with(context).load(url).apply(options).into(mIv);
        }
    }

    public static String getStarPhone(String phone) {
        try {
            phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return phone;
        }
    }

    public static String getIdCardStatus(int status) {
        //身份证状态 0未上传 1已上传 2审核未通过 3审核通过
        if (status==1){
            return "待审核";
        }else if (status==2){
            return "未通过";
        }else if (status==3){
            return "通过";
        }else{
            return "未提交";
        }
    }

    public static boolean isPhone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isEmail(String str) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void initRegionTreeData() {
        OkGo.<ResponseData<List<RegionTree>>>post(UrlConstant.GET_ALL_REGION)
                .execute(new JsonCallback<ResponseData<List<RegionTree>>>() {

                    @Override
                    public void onSuccess(ResponseData<List<RegionTree>> response) {
                        if (response!=null){
                            if (response.isSuccess() && response.getData()!=null){
                                AppUtil.regionTreeList = response.getData();
                                if (Helper.isNotEmpty(AppUtil.regionTreeList) && CurrentUser.getInstance().isLogin() && Helper.isEmpty(CurrentUser.getInstance().getResidentArea())){
                                    EventBusUtil.post(MessageConstant.SELECT_AREA);
                                }
                            }
                        }
                    }
                });
    }

    public static void initCategoryData() {
        OkGo.<ResponseData<List<Category>>>post(UrlConstant.GET_ALL_CATEGORY)
                .execute(new JsonCallback<ResponseData<List<Category>>>() {

                    @Override
                    public void onSuccess(ResponseData<List<Category>> response) {
                        if (response!=null){
                            if (response.isSuccess()){
                                AppUtil.categoryItemList = response.getData();
                            }
                        }
                    }
                });
    }

    public static void initSignReasonData() {
        HashMap<String,String> map = new HashMap<>();
        map.put("dictKey","SIGN_IN_EXCEPTION");
        OkGo.<ResponseData<List<ReasonBean>>>post(UrlConstant.GET_EXCEPTION_REASON)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<List<ReasonBean>>>() {

                    @Override
                    public void onSuccess(ResponseData<List<ReasonBean>> response) {
                        if (response!=null){
                            if (response.isSuccess() && response.getData()!=null){
                                AppUtil.signReasonList = response.getData();
                            }
                        }
                    }
                });
    }

    public static void initReservationReasonData() {
        HashMap<String,String> map = new HashMap<>();
        map.put("dictKey","TURN_RESERVATION_REASON");
        OkGo.<ResponseData<List<ReasonBean>>>post(UrlConstant.GET_EXCEPTION_REASON)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<List<ReasonBean>>>() {

                    @Override
                    public void onSuccess(ResponseData<List<ReasonBean>> response) {
                        if (response!=null){
                            if (response.isSuccess() && response.getData()!=null){
                                AppUtil.reservationReasonList = response.getData();
                            }
                        }
                    }
                });
    }

    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    public static int getNavigationBarHeight(Activity context) {
        int result = 0;
        if (isNavigationBarShow(context)&&hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    public static boolean isNavigationBarShow(Activity context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean  result  = realSize.y!=size.y;
            return realSize.y!=size.y;
        }else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 获取id
     * @param orderItem
     * @return
     */
    public static String getDetailId(OrderItem orderItem){
        int orderStatus = orderItem.getOrderStatus();
        if (orderStatus<=25){
            return orderItem.getOrderId();
        }else if (orderStatus<=30){
            return orderItem.getServiceId();
        }else{
            return orderItem.getAcceptId();
        }
    }

    public static String getDetailUrl(OrderItem orderItem){
        int orderStatus = orderItem.getOrderStatus();
        if (orderStatus<=25){
            return UrlConstant.ORDER_DETAIL_DJD;
        }else if (orderStatus<=30){
            return UrlConstant.ORDER_DETAIL_DFP;
        }else{
            return UrlConstant.ORDER_DETAIL_YFP;
        }
    }

    public static void vibrate(final Activity activity, long milliseconds) {
        try {
            Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(milliseconds);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
