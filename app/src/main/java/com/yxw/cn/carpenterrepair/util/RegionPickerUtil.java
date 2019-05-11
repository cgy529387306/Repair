package com.yxw.cn.carpenterrepair.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.RegionTree;
import com.yxw.cn.carpenterrepair.entity.RegionTreeList;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.listerner.OnChooseAddrListener;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CY on 2018/11/25
 */
public class RegionPickerUtil {

    //  省份
    public static ArrayList<String> provinceBeanList = new ArrayList<>();
    public static ArrayList<String> allProvinceBeanList = new ArrayList<>();
    //  城市
    public static ArrayList<String> cities;
    public static ArrayList<List<String>> cityList = new ArrayList<>();
    public static ArrayList<List<String>> allCityList = new ArrayList<>();
    //  区/县
    public static ArrayList<String> district;
    public static ArrayList<List<String>> districts;
    public static ArrayList<List<List<String>>> districtList = new ArrayList<>();
    public static ArrayList<List<List<String>>> allDistrictList = new ArrayList<>();
    public static OptionsPickerView pvCustomOptions;

    public static void showPicker(Context context, TextView textView, boolean showDistrict) {
        if (AppUtil.regionTreeList != null && AppUtil.regionTreeList.size() > 0) {
            handlerData();
            show(context, textView, showDistrict);
        } else {
            OkGo.<ResponseData<List<RegionTree>>>post(UrlConstant.GET_ALL_REGION)
                    .execute(new JsonCallback<ResponseData<List<RegionTree>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<RegionTree>> response) {
                            if (response!=null){
                                if (response.isSuccess() && response.getData()!=null){
                                    AppUtil.regionTreeList.clear();
                                    AppUtil.regionTreeList.addAll(response.getData());
                                    handlerData();
                                    show(context, textView, showDistrict);
                                }
                            }
                        }
                    });
        }
    }

    public static String getCity(int provinceIndex, int cityIndex){
        return cityList.get(provinceIndex).get(cityIndex);
    }

    public static String getDistrict(int provinceIndex, int cityIndex, int districtIndex){
        return districtList.get(provinceIndex).get(cityIndex).get(districtIndex);
    }

    public static void show(Context context, TextView textView, boolean showDistrict) {
        pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                long agencyId = 0;
                if (AppUtil.regionTreeList != null && AppUtil.regionTreeList.size() > 0) {
                    agencyId = AppUtil.regionTreeList.get(options1).getChildren().get(options2).getChildren().get(options3).getAgencyId();
                }
                String address; //  如果是直辖市或者特别行政区只设置市和区/县
                if (showDistrict) {
                    address = provinceBeanList.get(options1) + ">" + cityList.get(options1).get(options2) + ">" + districtList.get(options1).get(options2).get(options3);
                } else {
                    address = provinceBeanList.get(options1) + ">" + cityList.get(options1).get(options2);
                }
                textView.setText(address);
                textView.setTag(agencyId + "");
                doSaveCity(agencyId + "");
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        final TextView tType = v.findViewById(R.id.tv_type);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight((Activity) context);
                        bottomView.setLayoutParams(params);
                        tType.setText("城市选择");
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();

        if (showDistrict) {
            pvCustomOptions.setPicker(provinceBeanList, cityList, districtList);//添加数据源
        } else {
            pvCustomOptions.setPicker(provinceBeanList, cityList);//添加数据源
        }
        String area = textView.getText().toString();
        try {
            if (!TextUtils.isEmpty(area)) {
                String[] areaArr = area.split("-");
                int proIndex, cityIndex, disIndex;
                if (areaArr.length == 2) {
                    proIndex = provinceBeanList.indexOf(areaArr[0]);
                    cityIndex = cityList.get(proIndex).indexOf(areaArr[1]);
                    pvCustomOptions.setSelectOptions(proIndex, cityIndex);
                } else if (areaArr.length == 3) {
                    proIndex = provinceBeanList.indexOf(areaArr[0]);
                    cityIndex = cityList.get(proIndex).indexOf(areaArr[1]);
                    disIndex = districtList.get(proIndex).get(cityIndex).indexOf(areaArr[2]);
                    pvCustomOptions.setSelectOptions(proIndex, cityIndex, disIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pvCustomOptions.show();
    }


    private static void doSaveCity(String cityId){
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();
        map.put("residentArea", cityId);
        OkGo.<ResponseData<String>>post(UrlConstant.CHANGE_USERINFO)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<String>>() {
                             @Override
                             public void onSuccess(ResponseData<String> response) {
                                 if (response != null){
                                     if (response.isSuccess()) {
                                         EventBusUtil.post(MessageConstant.NOTIFY_GET_INFO);
                                     } else {
                                         ToastUtil.show(response.getMsg());
                                     }
                                 }
                             }
                         }
                );
    }

    public static void showAllPicker(Context context, OnChooseAddrListener listener) {
        if (provinceBeanList != null && provinceBeanList.size() > 0) {
            showAll(context, listener);
        } else {
            OkGo.<ResponseData<RegionTreeList>>get(UrlConstant.GET_REGION_TREE)
                    .execute(new JsonCallback<ResponseData<RegionTreeList>>() {

                        @Override
                        public void onSuccess(ResponseData<RegionTreeList> response) {
                            if (response.getData().getList() != null && response.getData().getList().size() > 0) {
                                AppUtil.regionTreeList.clear();
                                AppUtil.regionTreeList.addAll(response.getData().getList());
                                handlerData();
                                showAll(context, listener);
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<RegionTreeList>> response) {
                            super.onError(response);
                        }
                    });
        }
    }

    public static void showPicker(Context context, OnChooseAddrListener listener) {
        if (provinceBeanList != null && provinceBeanList.size() > 0) {
            show(context, listener);
        } else {
            OkGo.<ResponseData<RegionTreeList>>get(UrlConstant.GET_REGION_TREE)
                    .execute(new JsonCallback<ResponseData<RegionTreeList>>() {

                        @Override
                        public void onSuccess(ResponseData<RegionTreeList> response) {
                            if (response.getData().getList() != null && response.getData().getList().size() > 0) {
                                AppUtil.regionTreeList.clear();
                                AppUtil.regionTreeList.addAll(response.getData().getList());
                                handlerData();
                                show(context, listener);
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<RegionTreeList>> response) {
                            super.onError(response);
                        }
                    });
        }
    }

    public static void showCityPicker(Context context, OnChooseAddrListener listener) {
        if (provinceBeanList != null && provinceBeanList.size() > 0) {
            showCity(context, listener);
        } else {
            OkGo.<ResponseData<RegionTreeList>>get(UrlConstant.GET_REGION_TREE)
                    .execute(new JsonCallback<ResponseData<RegionTreeList>>() {

                        @Override
                        public void onSuccess(ResponseData<RegionTreeList> response) {
                            if (response.getData().getList() != null && response.getData().getList().size() > 0) {
                                AppUtil.regionTreeList.clear();
                                AppUtil.regionTreeList.addAll(response.getData().getList());
                                handlerData();
                                showCity(context, listener);
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<RegionTreeList>> response) {
                            super.onError(response);
                        }
                    });
        }
    }

    public static void showAll(Context context, OnChooseAddrListener listener) {
        pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.getAddr(options1,options2,options3);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        final TextView tType = v.findViewById(R.id.tv_type);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight((Activity) context);
                        bottomView.setLayoutParams(params);
                        tType.setText("城市选择");
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();
        pvCustomOptions.setPicker(allProvinceBeanList, allCityList, allDistrictList);//添加数据源
        pvCustomOptions.show();
    }


    public static void show(Context context, OnChooseAddrListener listener) {
        pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.getAddr(options1,options2,options3);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        final TextView tType = v.findViewById(R.id.tv_type);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight((Activity) context);
                        bottomView.setLayoutParams(params);
                        tType.setText("城市选择");
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();

        pvCustomOptions.setPicker(provinceBeanList, cityList, districtList);//添加数据源
        pvCustomOptions.show();
    }

    public static void showCity(Context context, OnChooseAddrListener listener) {
        pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.getAddr(options1,options2,options3);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        final TextView tType = v.findViewById(R.id.tv_type);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight((Activity) context);
                        bottomView.setLayoutParams(params);
                        tType.setText("城市选择");
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();

        pvCustomOptions.setPicker(provinceBeanList, cityList, districtList);//添加数据源
        pvCustomOptions.show();
    }

    public static void init() {
        handlerData();
    }

    private static void handlerData() {
        provinceBeanList.clear();
        allProvinceBeanList.clear();
        cityList.clear();
        allCityList.clear();
        districtList.clear();
        allDistrictList.clear();
        if (AppUtil.regionTreeList != null && AppUtil.regionTreeList.size() > 0) {
            for (RegionTree regionTree :
                    AppUtil.regionTreeList) {
                if (Helper.isNotEmpty(regionTree.getChildren())){
                    provinceBeanList.add(regionTree.getRegionName());
                    cities = new ArrayList<>();
                    districts = new ArrayList<>();
                    for (RegionTree regionTreeSub :
                            regionTree.getChildren()) {
                        if (Helper.isNotEmpty(regionTreeSub.getChildren())){
                            cities.add(regionTreeSub.getRegionName());
                            district = new ArrayList<>();
                            for (RegionTree regionTreeSubItem :
                                    regionTreeSub.getChildren()) {
                                district.add(regionTreeSubItem.getRegionName());
                            }
                            districts.add(district);
                        }
                    }
                    districtList.add(districts);
                    cityList.add(cities);
                }
            }
            allProvinceBeanList.add("全部");
            allProvinceBeanList.addAll(provinceBeanList);
            List<String> city = new ArrayList<>();
            city.add("全部");
            allCityList.add(city);
            allCityList.addAll(cityList);
            List<String> district = new ArrayList<>();
            district.add("全部");
            List<List<String>> districts = new ArrayList<>();
            districts.add(district);
            allDistrictList.add(districts);
            allDistrictList.addAll(districtList);
        }
    }
}