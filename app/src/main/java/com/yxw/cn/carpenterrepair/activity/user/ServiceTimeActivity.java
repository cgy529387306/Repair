package com.yxw.cn.carpenterrepair.activity.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import com.yxw.cn.carpenterrepair.listerner.OnChooseAddrListener;
import com.yxw.cn.carpenterrepair.listerner.OnChooseTimeListener;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.RegionPickerUtil;
import com.yxw.cn.carpenterrepair.util.TimePickerUtil;

/**
 * 服务时间
 */
public class ServiceTimeActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.iv_check1)
    ImageView mIvCheck1;
    @BindView(R.id.iv_check2)
    ImageView mIvCheck2;
    @BindView(R.id.iv_check3)
    ImageView mIvCheck3;
    @BindView(R.id.iv_check4)
    ImageView mIvCheck4;
    @BindView(R.id.iv_check5)
    ImageView mIvCheck5;
    @BindView(R.id.iv_check6)
    ImageView mIvCheck6;
    @BindView(R.id.iv_check7)
    ImageView mIvCheck7;
    @BindView(R.id.tv_start)
    TextView mTvStart;
    @BindView(R.id.tv_end)
    TextView mTvEnd;
    @BindView(R.id.tv_place)
    TextView mTvPlace;
    @BindView(R.id.rl_place)
    RelativeLayout mRlPlace;
    private LoginInfo loginInfo;
    private boolean b1,b2,b3,b4,b5,b6,b7;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_service_time;
    }

    @Override
    public void initView() {
        titleBar.setTitle("服务时间");
        if (getIntent().getBooleanExtra("place", false)) {
            mRlPlace.setVisibility(View.VISIBLE);
            titleBar.setLeftVisible(false);
        } else {
            mRlPlace.setVisibility(View.GONE);
            titleBar.setLeftVisible(true);
        }
        if (CurrentUser.getInstance().isLogin()){
            loginInfo = CurrentUser.getInstance();
            notifyData();
        }else{
            loginInfo = new LoginInfo();
        }

    }

    public void notifyData() {
        try {
            if (!TextUtils.isEmpty(loginInfo.getServiceTime())) {
                mTvStart.setText(loginInfo.getServiceTime().split("-")[0]);
                mTvEnd.setText(loginInfo.getServiceTime().split("-")[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIvCheck1.setImageResource(R.drawable.icon_unselect);
        mIvCheck2.setImageResource(R.drawable.icon_unselect);
        mIvCheck3.setImageResource(R.drawable.icon_unselect);
        mIvCheck4.setImageResource(R.drawable.icon_unselect);
        mIvCheck5.setImageResource(R.drawable.icon_unselect);
        mIvCheck6.setImageResource(R.drawable.icon_unselect);
        mIvCheck7.setImageResource(R.drawable.icon_unselect);
        try {
            if (!TextUtils.isEmpty(loginInfo.getServiceDate())) {
                for (String date :
                        loginInfo.getServiceDate().split(",")) {
                    switch (date) {
                        case "1":
                            b1=true;
                            mIvCheck1.setImageResource(R.drawable.icon_selected);
                            break;
                        case "2":
                            b2=true;
                            mIvCheck2.setImageResource(R.drawable.icon_selected);
                            break;
                        case "3":
                            b3=true;
                            mIvCheck3.setImageResource(R.drawable.icon_selected);
                            break;
                        case "4":
                            b4=true;
                            mIvCheck4.setImageResource(R.drawable.icon_selected);
                            break;
                        case "5":
                            b5=true;
                            mIvCheck5.setImageResource(R.drawable.icon_selected);
                            break;
                        case "6":
                            b6=true;
                            mIvCheck6.setImageResource(R.drawable.icon_selected);
                            break;
                        case "7":
                            b7=true;
                            mIvCheck7.setImageResource(R.drawable.icon_selected);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_place, R.id.rl_start, R.id.rl_end, R.id.rl1, R.id.rl2, R.id.rl3, R.id.rl4, R.id.rl5, R.id.rl6, R.id.rl7, R.id.tv_sure})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_place:
                RegionPickerUtil.showCityPicker(this, new OnChooseAddrListener() {
                    @Override
                    public void getAddr(int options1, int options2, int options3) {
                        mTvPlace.setText(RegionPickerUtil.getCity(options1,options2));
                        mTvPlace.setTag(AppUtil.regionTreeList.get(options1).getSub().get(options2).getAgency_id()+"");
                    }
                });
                break;
            case R.id.rl_start:
                TimePickerUtil.showTimePicker(this, mTvStart, new OnChooseTimeListener() {
                    @Override
                    public void getDateTime(String date, String time) {
                        mTvStart.setText(time);
                    }
                });
                break;
            case R.id.rl_end:
                TimePickerUtil.showTimePicker(this, mTvEnd, new OnChooseTimeListener() {
                    @Override
                    public void getDateTime(String date, String time) {
                        mTvEnd.setText(time);
                    }
                });
                break;
            case R.id.tv_sure:
                if (getIntent().getBooleanExtra("place", false)) {
                    if (TextUtils.isEmpty(mTvPlace.getText().toString())) {
                        toast("您还未选择驻点");
                        return;
                    }
                }
                if (TextUtils.isEmpty(mTvStart.getText().toString())) {
                    toast("您还未选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(mTvEnd.getText().toString())) {
                    toast("您还未选择结束时间");
                    return;
                }
                String date = "";
                if (b1) {
                    date += 1 + ",";
                }
                if (b2) {
                    date += 2 + ",";
                }
                if (b3) {
                    date += 3 + ",";
                }
                if (b4) {
                    date += 4 + ",";
                }
                if (b5) {
                    date += 5 + ",";
                }
                if (b6) {
                    date += 6 + ",";
                }
                if (b7) {
                    date += 7 + ",";
                }


                Logger.d("date: " + date);
                if (TextUtils.isEmpty(date)) {
                    toast("您还未选择服务日期");
                    return;
                } else {
                    date = date.substring(0, date.length() - 1);
                }
                editTrait(mTvStart.getText().toString() + "-" + mTvEnd.getText().toString(), date);
                break;
            case R.id.rl1:
                b1=!b1;
                if (b1) {
                    mIvCheck1.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck1.setImageResource(R.drawable.icon_unselect);
                }
                break;
            case R.id.rl2:
                b2=!b2;
                if (b2) {
                    mIvCheck2.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck2.setImageResource(R.drawable.icon_unselect);
                }
                break;
            case R.id.rl3:
                b3=!b3;
                if (b3) {
                    mIvCheck3.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck3.setImageResource(R.drawable.icon_unselect);
                }
                break;
            case R.id.rl4:
                b4=!b4;
                if (b4) {
                    mIvCheck4.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck4.setImageResource(R.drawable.icon_unselect);
                }
                break;
            case R.id.rl5:
                b5=!b5;
                if (b5) {
                    mIvCheck5.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck5.setImageResource(R.drawable.icon_unselect);
                }
                break;
            case R.id.rl6:
                b6=!b6;
                if (b6) {
                    mIvCheck6.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck6.setImageResource(R.drawable.icon_unselect);
                }
                break;
            case R.id.rl7:
                b7=!b7;
                if (b7) {
                    mIvCheck7.setImageResource(R.drawable.icon_selected);
                } else {
                    mIvCheck7.setImageResource(R.drawable.icon_unselect);
                }
                break;
        }
    }

    private void editTrait(String serviceTime, String serviceDate) {
        Map<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(mTvPlace.getText().toString())) {
            map.put("resident", "");
        } else {
            map.put("resident", mTvPlace.getTag().toString());
        }
        map.put("serviceTime", serviceTime);
        map.put("serviceDate", serviceDate);
        OkGo.<ResponseData>post(UrlConstant.EDIT_TRAIT)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData>() {
                    @Override
                    public void onSuccess(ResponseData response) {
                        toast(response.getMsg());
                        if (response.isSuccess()) {
                            loginInfo.setServiceTime(serviceTime);
                            loginInfo.setServiceDate(serviceDate);
                            CurrentUser.getInstance().login(loginInfo);
                            EventBusUtil.post(MessageConstant.NOTIFY_INFO);
                            AppUtil.checkStatus(ServiceTimeActivity.this);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (!getIntent().getBooleanExtra("place", false)) {
            super.onBackPressed();
        }
    }
}
