package com.yxw.cn.carpenterrepair.activity.setting;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;

/**
 * 客服
 */
public class CustomerServiceActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_customer_service;
    }

    @Override
    public void initView() {
        titlebar.setTitle("达奇客服");
        Glide.with(this).load(R.mipmap.launcher).apply(RequestOptions.circleCropTransform()).into(mIvLeft);
        Glide.with(this).load(R.mipmap.launcher).apply(RequestOptions.circleCropTransform()).into(mIvRight);
    }
}
