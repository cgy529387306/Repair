package com.yxw.cn.carpenterrepair.activity.user;


import android.view.View;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 加入服务商
 */
public class JoinServiceProviderActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_join_service_provider;
    }

    @Override
    public void initView() {
        titleBar.setTitle("加入服务商");

    }

    @OnClick({R.id.btn_confirm})
    public void click(View view) {
        if (view.getId() == R.id.btn_confirm){
            startActivity(ApplyServiceProviderActivity.class);
            finish();
        }
    }
}
