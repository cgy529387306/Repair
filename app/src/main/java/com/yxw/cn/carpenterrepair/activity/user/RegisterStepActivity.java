package com.yxw.cn.carpenterrepair.activity.user;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;

/**
 * 注册
 */
public class RegisterStepActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_register_step;
    }

    @Override
    public void initView() {
        titlebar.setTitle("注册");
    }


}
