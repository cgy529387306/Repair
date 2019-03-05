package com.yxw.cn.carpenterrepair.activity.login;

import android.os.Bundle;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.UserMainActivity;
import com.yxw.cn.carpenterrepair.activity.main.WorkerMainActivity;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.util.SpUtil;

import butterknife.OnClick;

public class RegisterSuccessActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register_success;
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
        switch (SpUtil.getInt(SpConstant.ROLE)) {
            case 0:
                startActivityFinish(UserMainActivity.class,bundle);
                break;
            case 1:
                startActivityFinish(WorkerMainActivity.class,bundle);
                break;
        }
    }
}
