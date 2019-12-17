package com.yxw.cn.carpenterrepair;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tencent.bugly.crashreport.CrashReport;


/**
 * 简单测试crash
 * 注：如想查看crash日志，需先到http://bugly.qq.com/注册app，并配置appID，之后就可以在bugly查看到日志啦
 * @author wenjiewu
 * @date 2016/5/23
 */
public class BuglyTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTestJavaCrash;
    private Button btnTestANRCrash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bugly_test);

        btnTestJavaCrash = (Button) findViewById(R.id.btnTestJavaCrash);
        btnTestANRCrash = (Button) findViewById(R.id.btnTestANRCrash);
        btnTestJavaCrash.setOnClickListener(this);
        btnTestANRCrash.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTestJavaCrash: // 点击测试Java Crash
                CrashReport.testJavaCrash();
                break;
            case R.id.btnTestANRCrash: // 点击测试ANR Crash
                CrashReport.testANRCrash();
                break;
        }
    }
}
