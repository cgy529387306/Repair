package com.yxw.cn.carpenterrepair.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.login.StartActivity;


/**
 * 起始页
 *
 * @author @author chenqm on 2018/1/15.
 */

public class SplashActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this,StartActivity.class));
            finish();
        }, 1500);
    }


}
