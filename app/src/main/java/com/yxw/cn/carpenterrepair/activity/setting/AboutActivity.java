package com.yxw.cn.carpenterrepair.activity.setting;

import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;

/**
 * 关于
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_about;
    }

    @Override
    public void initView() {
        titleBar.setTitle("关于达奇");
        tvVersion.setText(String.format("达奇 V%s", AppUtil.getVerName()));
    }


}
