package com.yxw.cn.carpenterrepair.activity.user;


import android.view.View;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.MainActivity;
import com.yxw.cn.carpenterrepair.util.ActivityManager;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 申请加入服务商
 */
public class ApplyServiceProviderActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_apply_service_provider;
    }

    @Override
    public void initView() {
        titleBar.setTitle("加入服务商");

    }

    @OnClick({R.id.btn_ok})
    public void click(View view) {
        if (view.getId() == R.id.btn_ok){
            finish();
        }
    }
}
