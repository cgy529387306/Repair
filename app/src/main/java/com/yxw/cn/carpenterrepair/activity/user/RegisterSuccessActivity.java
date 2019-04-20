package com.yxw.cn.carpenterrepair.activity.user;

import android.os.Bundle;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.MainActivity;

import butterknife.OnClick;

/**
 * 注册成功
 */
public class RegisterSuccessActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.act_register_success;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).fitsSystemWindows(true).init();
    }

    @OnClick({R.id.tv_pass, R.id.btn_perfect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pass:
                gotoMainActivity(false);
                break;
            case R.id.btn_perfect:
                gotoMainActivity(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        gotoMainActivity(false);
    }

    public void gotoMainActivity(boolean perfect) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("perfect", perfect);
        startActivityFinish(MainActivity.class,bundle);
    }
}
