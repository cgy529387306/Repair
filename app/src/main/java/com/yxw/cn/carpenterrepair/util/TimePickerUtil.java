package com.yxw.cn.carpenterrepair.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.listerner.OnChooseDateListener;
import com.yxw.cn.carpenterrepair.listerner.OnChooseTimeListener;
import com.yxw.cn.carpenterrepair.timepicker.CustomTimePickerView;
import com.yxw.cn.carpenterrepair.timepicker.CustomTimePikerBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by CY on 2018/11/29
 */
public class TimePickerUtil {

    private static OptionsPickerView pvCustomOptions;
    private static List<String> weekList = new ArrayList<>();
    private static List<String> timeList = new ArrayList<>();

    public static void showPicker(Context context, TextView chooseTime, OnChooseTimeListener listener) {
        getPickerData();
        pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                chooseTime.setText(weekList.get(options1) + " " + timeList.get(options2));
                listener.getDateTime(weekList.get(options1), timeList.get(options2));
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("类型选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(20)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_date, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight((Activity) context);
                        bottomView.setLayoutParams(params);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();
        pvCustomOptions.setNPicker(weekList, timeList, null);//添加数据源
        pvCustomOptions.show();
    }

    public static void showTimePicker(Context context, TextView chooseTime, OnChooseTimeListener listener) {
        getPickerData();
        pvCustomOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                chooseTime.setText(timeList.get(options1));
                listener.getDateTime(timeList.get(options1), timeList.get(options1));
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("类型选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(20)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_date, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        final TextView tvCancel = v.findViewById(R.id.tv_cancel);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight((Activity) context);
                        bottomView.setLayoutParams(params);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();
        pvCustomOptions.setNPicker(timeList, null, null);//添加数据源
        pvCustomOptions.show();
    }

    private static void getPickerData() {
        weekList.clear();
        timeList.clear();
        weekList.addAll(DateUtil.getdateAndweeks(2));
        timeList.add("00:00");
        timeList.add("01:00");
        timeList.add("02:00");
        timeList.add("03:00");
        timeList.add("04:00");
        timeList.add("05:00");
        timeList.add("06:00");
        timeList.add("07:00");
        timeList.add("08:00");
        timeList.add("09:00");
        timeList.add("10:00");
        timeList.add("11:00");
        timeList.add("12:00");
        timeList.add("13:00");
        timeList.add("14:00");
        timeList.add("15:00");
        timeList.add("16:00");
        timeList.add("17:00");
        timeList.add("18:00");
        timeList.add("19:00");
        timeList.add("20:00");
        timeList.add("21:00");
        timeList.add("22:00");
        timeList.add("23:00");
    }

    public static void showYearPicker(Context context, OnChooseDateListener listener) {
        //时间选择器
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,1);
        calendar.set(Calendar.MINUTE,0);
        CustomTimePickerView pvTime = new CustomTimePikerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (date.getTime()<new Date().getTime()){
                    ToastUtil.show("上门时间不能小于当前时间");
                    return;
                }
                if (listener != null) listener.getDate(date);
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setRangDate(calendar, null)
                .setContentTextSize(20)
                .setTitleText("预约时间")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .build();
        pvTime.show();
    }

}
