package com.yxw.cn.carpenterrepair.activity.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.IdCardInfoActivity;
import com.yxw.cn.carpenterrepair.activity.WorkerPersonInfoActivity;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.fragment.WorkerPersonalCenterFragment;
import com.yxw.cn.carpenterrepair.fragment.WebFragment;
import com.yxw.cn.carpenterrepair.fragment.WorkAreaFragment;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CY on 2018/11/25
 */
public class WorkerMainActivity extends BaseActivity {

    @BindView(R.id.tv_work)
    TextView tv_work;
    @BindView(R.id.tv_mall)
    TextView tv_mall;
    @BindView(R.id.tv_personal)
    TextView tv_personal;
    private WorkAreaFragment workAreaFragment;
    private WebFragment shoppingMallFragment;
    private WorkerPersonalCenterFragment personalCenterFragment;
    private FragmentManager fragmentManager;

    private int index = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_worker_main;
    }

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        showFragment(1);
        if (getIntent().getBooleanExtra("perfect", false)) {
            startActivity(WorkerPersonInfoActivity.class);
        }
        tv_mall.setVisibility(View.GONE);
    }

    @Override
    public void getData() {
        OkGo.<ResponseData<LoginInfo>>post(UrlConstant.GET_WORKER_INFO)
                .execute(new JsonCallback<ResponseData<LoginInfo>>() {
                             @Override
                             public void onSuccess(ResponseData<LoginInfo> response) {
                                 if (response.getCode() == 0) {
                                     SpUtil.putStr(SpConstant.LOGIN_INFO, gson.toJson(response.getData()));
                                     AppUtil.checkStatus(WorkerMainActivity.this);
                                 }
                             }
                         }
                );
    }

    private void showFragment(int page) {
        index = page;
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (page) {
            case 1:
                // 如果fragment1已经存在则将其显示出来
                if (workAreaFragment != null)
                    ft.show(workAreaFragment);
                    // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    workAreaFragment = new WorkAreaFragment();
                    ft.add(R.id.main_container_content, workAreaFragment);
                }
                break;
            case 2:
                if (shoppingMallFragment != null)
                    ft.show(shoppingMallFragment);
                else {
                    shoppingMallFragment = WebFragment.newInstance(UrlConstant.URL_SHOPPING);
                    ft.add(R.id.main_container_content, shoppingMallFragment);
                }
                break;
            case 3:
                if (personalCenterFragment != null) {
                    ft.show(personalCenterFragment);
                } else {
                    personalCenterFragment = new WorkerPersonalCenterFragment();
                    ft.add(R.id.main_container_content, personalCenterFragment);
                }
                break;
        }
        ft.commit();
    }

    private static final long DOUBLE_CLICK_INTERVAL = 2000;
    private long mLastClickTimeMills = 0;

//    @Override
//    public void onBackPressed() {
//        if (System.currentTimeMillis() - mLastClickTimeMills > DOUBLE_CLICK_INTERVAL) {
//            toast("再按一次返回退出");
//            mLastClickTimeMills = System.currentTimeMillis();
//            return;
//        }
//        finish();
//    }

    @Override
    public void onBackPressed() {
        if (index == 2) {
            if (shoppingMallFragment.canGoBack()) {
                shoppingMallFragment.goBack();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (workAreaFragment != null)
            ft.hide(workAreaFragment);
        if (shoppingMallFragment != null)
            ft.hide(shoppingMallFragment);
        if (personalCenterFragment != null)
            ft.hide(personalCenterFragment);
    }


    @OnClick({R.id.tv_work, R.id.tv_mall, R.id.tv_personal})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_work:
                showFragment(1);
                tv_work.setTextColor(Color.parseColor("#FF5E5E"));
                tv_mall.setTextColor(Color.parseColor("#666666"));
                tv_personal.setTextColor(Color.parseColor("#666666"));

                Drawable drawable = getResources().getDrawable(R.drawable.work_on);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                tv_work.setCompoundDrawables(null, drawable, null, null);
                Drawable drawable2 = getResources().getDrawable(R.drawable.mall_off);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());

                tv_mall.setCompoundDrawables(null, drawable2, null, null);
                Drawable drawable3 = getResources().getDrawable(R.drawable.personal_off);
                drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());

                tv_personal.setCompoundDrawables(null, drawable3, null, null);

                break;
            case R.id.tv_mall:
                showFragment(2);
                tv_mall.setTextColor(Color.parseColor("#FF5E5E"));
                tv_work.setTextColor(Color.parseColor("#666666"));
                tv_personal.setTextColor(Color.parseColor("#666666"));
                Drawable drawable4 = getResources().getDrawable(R.drawable.work_off);
                drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
                tv_work.setCompoundDrawables(null, drawable4, null, null);
                Drawable drawable5 = getResources().getDrawable(R.drawable.mall_on);
                drawable5.setBounds(0, 0, drawable5.getMinimumWidth(), drawable5.getMinimumHeight());
                tv_mall.setCompoundDrawables(null, drawable5, null, null);
                Drawable drawable6 = getResources().getDrawable(R.drawable.personal_off);
                drawable6.setBounds(0, 0, drawable6.getMinimumWidth(), drawable6.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable6, null, null);
                break;
            case R.id.tv_personal:
                showFragment(3);
                tv_personal.setTextColor(Color.parseColor("#FF5E5E"));
                tv_mall.setTextColor(Color.parseColor("#666666"));
                tv_work.setTextColor(Color.parseColor("#666666"));
                Drawable drawable7 = getResources().getDrawable(R.drawable.work_off);
                drawable7.setBounds(0, 0, drawable7.getMinimumWidth(), drawable7.getMinimumHeight());

                tv_work.setCompoundDrawables(null, drawable7, null, null);
                Drawable drawable8 = getResources().getDrawable(R.drawable.mall_off);
                drawable8.setBounds(0, 0, drawable8.getMinimumWidth(), drawable8.getMinimumHeight());

                tv_mall.setCompoundDrawables(null, drawable8, null, null);
                Drawable drawable9 = getResources().getDrawable(R.drawable.personal_on);
                drawable9.setBounds(0, 0, drawable9.getMinimumWidth(), drawable9.getMinimumHeight());

                tv_personal.setCompoundDrawables(null, drawable9, null, null);
                break;
        }
    }


}
