package com.yxw.cn.carpenterrepair.activity.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.UserPersonInfoActivity;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.fragment.WebFragment;
import com.yxw.cn.carpenterrepair.fragment.user.FindServiceFragment;
import com.yxw.cn.carpenterrepair.fragment.user.MallFragment;
import com.yxw.cn.carpenterrepair.fragment.user.MyFragment;
import com.yxw.cn.carpenterrepair.fragment.user.OrderFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CY on 2018/11/25
 */
public class UserMainActivity extends BaseActivity {

    @BindView(R.id.tv_order)
    TextView tv_order;
    @BindView(R.id.tv_find_service)
    TextView tv_find_service;
    @BindView(R.id.tv_mall)
    TextView tv_mall;
    @BindView(R.id.tv_personal)
    TextView tv_personal;
    private OrderFragment orderFragment;
    private FindServiceFragment findServiceFragment;
    private WebFragment mallFragment;
    private MyFragment myFragment;
    private FragmentManager fragmentManager;
    private int index = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_user_main;
    }

    @Override
    public void initView() {
        super.initView();
        fragmentManager = getSupportFragmentManager();
        showFragment(1);
        if(getIntent().getBooleanExtra("perfect",false)){
            startActivity(UserPersonInfoActivity.class);
        }
    }

    private void showFragment(int page) {
        index = page;

        FragmentTransaction ft = fragmentManager.beginTransaction();

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (page) {
            case 1:
                // 如果fragment1已经存在则将其显示出来
                if (orderFragment != null)
                    ft.show(orderFragment);
                    // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    orderFragment = new OrderFragment();
                    ft.add(R.id.main_container_content, orderFragment);
                }
                break;
            case 2:
                if (findServiceFragment != null)
                    ft.show(findServiceFragment);
                else {
                    findServiceFragment = new FindServiceFragment();
                    ft.add(R.id.main_container_content, findServiceFragment);
                }
                break;
            case 3:
                if (mallFragment != null) {
                    ft.show(mallFragment);
                } else {
                    mallFragment = WebFragment.newInstance(UrlConstant.URL_SHOPPING);
                    ft.add(R.id.main_container_content, mallFragment);
                }
                break;
            case 4:
                if (myFragment != null) {
                    ft.show(myFragment);
                } else {
                    myFragment = new MyFragment();
                    ft.add(R.id.main_container_content, myFragment);
                }
                break;
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (index == 3) {
            if (mallFragment.canGoBack()) {
                mallFragment.goBack();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (orderFragment != null)
            ft.hide(orderFragment);
        if (findServiceFragment != null)
            ft.hide(findServiceFragment);
        if (mallFragment != null)
            ft.hide(mallFragment);
        if (myFragment != null)
            ft.hide(myFragment);
    }


    @OnClick({R.id.tv_order, R.id.tv_find_service, R.id.tv_mall, R.id.tv_personal})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_order:
                showFragment(1);
                tv_order.setTextColor(Color.parseColor("#FF5E5E"));
                tv_find_service.setTextColor(Color.parseColor("#666666"));
                tv_mall.setTextColor(Color.parseColor("#666666"));
                tv_personal.setTextColor(Color.parseColor("#666666"));

                Drawable drawable = getResources().getDrawable(R.drawable.order_on);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_order.setCompoundDrawables(null, drawable, null, null);

                Drawable drawable2 = getResources().getDrawable(R.drawable.find_service_off);
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                tv_find_service.setCompoundDrawables(null, drawable2, null, null);

                Drawable drawable3 = getResources().getDrawable(R.drawable.mall_off);
                drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                tv_mall.setCompoundDrawables(null, drawable3, null, null);

                Drawable drawable4 = getResources().getDrawable(R.drawable.personal_off);
                drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable4, null, null);

                break;
            case R.id.tv_find_service:
                showFragment(2);
                tv_find_service.setTextColor(Color.parseColor("#FF5E5E"));
                tv_order.setTextColor(Color.parseColor("#666666"));
                tv_mall.setTextColor(Color.parseColor("#666666"));
                tv_personal.setTextColor(Color.parseColor("#666666"));

                Drawable drawable5 = getResources().getDrawable(R.drawable.order_off);
                drawable5.setBounds(0, 0, drawable5.getMinimumWidth(), drawable5.getMinimumHeight());
                tv_order.setCompoundDrawables(null, drawable5, null, null);

                Drawable drawable6 = getResources().getDrawable(R.drawable.find_service_on);
                drawable6.setBounds(0, 0, drawable6.getMinimumWidth(), drawable6.getMinimumHeight());
                tv_find_service.setCompoundDrawables(null, drawable6, null, null);

                Drawable drawable7 = getResources().getDrawable(R.drawable.mall_off);
                drawable7.setBounds(0, 0, drawable7.getMinimumWidth(), drawable7.getMinimumHeight());
                tv_mall.setCompoundDrawables(null, drawable7, null, null);

                Drawable drawable8 = getResources().getDrawable(R.drawable.personal_off);
                drawable8.setBounds(0, 0, drawable8.getMinimumWidth(), drawable8.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable8, null, null);
                break;
            case R.id.tv_mall:
                showFragment(3);
                tv_mall.setTextColor(Color.parseColor("#FF5E5E"));
                tv_find_service.setTextColor(Color.parseColor("#666666"));
                tv_order.setTextColor(Color.parseColor("#666666"));
                tv_personal.setTextColor(Color.parseColor("#666666"));

                Drawable drawable9 = getResources().getDrawable(R.drawable.order_off);
                drawable9.setBounds(0, 0, drawable9.getMinimumWidth(), drawable9.getMinimumHeight());
                tv_order.setCompoundDrawables(null, drawable9, null, null);

                Drawable drawable10 = getResources().getDrawable(R.drawable.find_service_off);
                drawable10.setBounds(0, 0, drawable10.getMinimumWidth(), drawable10.getMinimumHeight());
                tv_find_service.setCompoundDrawables(null, drawable10, null, null);

                Drawable drawable11 = getResources().getDrawable(R.drawable.mall_on);
                drawable11.setBounds(0, 0, drawable11.getMinimumWidth(), drawable11.getMinimumHeight());
                tv_mall.setCompoundDrawables(null, drawable11, null, null);

                Drawable drawable12 = getResources().getDrawable(R.drawable.personal_off);
                drawable12.setBounds(0, 0, drawable12.getMinimumWidth(), drawable12.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable12, null, null);
                break;
            case R.id.tv_personal:
                showFragment(4);
                tv_personal.setTextColor(Color.parseColor("#FF5E5E"));
                tv_find_service.setTextColor(Color.parseColor("#666666"));
                tv_mall.setTextColor(Color.parseColor("#666666"));
                tv_order.setTextColor(Color.parseColor("#666666"));

                Drawable drawable13 = getResources().getDrawable(R.drawable.order_off);
                drawable13.setBounds(0, 0, drawable13.getMinimumWidth(), drawable13.getMinimumHeight());
                tv_order.setCompoundDrawables(null, drawable13, null, null);

                Drawable drawable14 = getResources().getDrawable(R.drawable.find_service_off);
                drawable14.setBounds(0, 0, drawable14.getMinimumWidth(), drawable14.getMinimumHeight());
                tv_find_service.setCompoundDrawables(null, drawable14, null, null);

                Drawable drawable15 = getResources().getDrawable(R.drawable.mall_off);
                drawable15.setBounds(0, 0, drawable15.getMinimumWidth(), drawable15.getMinimumHeight());
                tv_mall.setCompoundDrawables(null, drawable15, null, null);

                Drawable drawable16 = getResources().getDrawable(R.drawable.personal_on);
                drawable16.setBounds(0, 0, drawable16.getMinimumWidth(), drawable16.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable16, null, null);
                break;
        }
    }
}
