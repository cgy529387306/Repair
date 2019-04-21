package com.yxw.cn.carpenterrepair.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseApplication;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.user.ChooseCategoryActivity;
import com.yxw.cn.carpenterrepair.activity.user.IdCardInfoActivity;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Abnormal;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.QueryListByMark;
import com.yxw.cn.carpenterrepair.entity.RegionTree;
import com.yxw.cn.carpenterrepair.entity.RegionTreeList;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yxw.cn.carpenterrepair.activity.user.ServiceTimeActivity;

public class AppUtil {

    public static List<QueryListByMark> timeList = new ArrayList<>();
    public static List<RegionTree> regionTreeList = new ArrayList<>();
    public static List<Category> categoryItemList = new ArrayList<>();
    public static List<Abnormal> categoryItemList0 = new ArrayList<>();
    private static Gson gson = new Gson();

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

    public static void checkStatus(Context context){
        LoginInfo loginInfo = CurrentUser.getInstance();
        Intent intent;
        if(loginInfo.getIdCardStatus() == 0 || loginInfo.getIdCardStatus() == 2){
            intent = new Intent(context,IdCardInfoActivity.class);
            context.startActivity(intent);
        }else if(TextUtils.isEmpty(loginInfo.getServiceTime())||TextUtils.isEmpty(loginInfo.getServiceDate())){
            intent = new Intent(context, ServiceTimeActivity.class);
            intent.putExtra("place",true);
            context.startActivity(intent);
        }else if(loginInfo.getTags() == null || loginInfo.getTags().size()==0){
            intent = new Intent(context, ChooseCategoryActivity.class);
            intent.putExtra("force",true);
            context.startActivity(intent);
        }
    }

    public static String getOrderDetailAddress(UserOrder.ListBean order){
        return order.getProvince()+order.getCity()+order.getDistrict()+order.getAddress();
    }

    public static String getUserOrderStatus(UserOrder.ListBean item) {
        if (item.getOrderStatus()< 25) {
            return "未接单";
        }else if (item.getOrderStatus() >= 25 && item.getOrderStatus() < 35) {
            return "待预约";
        }
        else if (item.getOrderStatus() >= 35 && item.getOrderStatus() < 55) {
            return "待服务";
        } else if (item.getOrderStatus() >= 55 && item.getOrderStatus() < 60) {
            return "进行中";
        } else if (item.getOrderStatus() >= 60) {
            return "已完成";
        } else {
            return "";
        }
    }

    public static String getWorkerOrderStatus(UserOrder.ListBean item) {
        if (item.getOrderStatus() < 35) {
            return "我要接单";
        } else if (item.getOrderStatus() >= 35 && item.getOrderStatus() < 40) {
            return "等待上门";
        } else if (item.getOrderStatus() >= 40 && item.getOrderStatus() < 60) {
            return "进行中";
        } else if (item.getOrderStatus() >= 60) {
            return "已完成";
        } else {
            return "";
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

    public static boolean isphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isEmail(String str) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static void initTimeData() {
        Map<String, String> map = new HashMap<>();
        map.put("mark", "ORDER_SEARCH_FRAME");
        OkGo.<ResponseData<List<QueryListByMark>>>post(UrlConstant.QUERT_LIST_BY_MARK)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<List<QueryListByMark>>>() {

                    @Override
                    public void onSuccess(ResponseData<List<QueryListByMark>> response) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            timeList.clear();
                            timeList.addAll(response.getData());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<List<QueryListByMark>>> response) {
                        super.onError(response);
                    }
                });
    }

    public static void initRegionTreeData() {
        OkGo.<ResponseData<RegionTreeList>>get(UrlConstant.GET_REGION_TREE)
                .execute(new JsonCallback<ResponseData<RegionTreeList>>() {

                    @Override
                    public void onSuccess(ResponseData<RegionTreeList> response) {
                        if (response.getData().getList() != null && response.getData().getList().size() > 0) {
                            regionTreeList.clear();
                            regionTreeList.addAll(response.getData().getList());
                            RegionPickerUtil.init();
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<RegionTreeList>> response) {
                        super.onError(response);
                    }
                });
    }

    public static void initCategoryData() {
        OkGo.<ResponseData<List<Category>>>get(UrlConstant.GET_ALL_CATEGORY)
                .execute(new JsonCallback<ResponseData<List<Category>>>() {

                    @Override
                    public void onSuccess(ResponseData<List<Category>> response) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            categoryItemList.clear();
                            categoryItemList.addAll(response.getData());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<List<Category>>> response) {
                        super.onError(response);
                    }
                });
    }

    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    public static int getNavigationBarHeight(Context context) {
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

    public static boolean isNavigationBarShow(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = BaseApplication.getInstance().getLastActivity().getWindowManager().getDefaultDisplay();
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

}
