package com.yxw.cn.carpenterrepair.activity;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看图片
 */
public class LookImageActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView iv;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_look_image;
    }

    @Override
    public void initView() {
        super.initView();
        Glide.with(this).load(getIntent().getStringExtra("path")).into(iv);
    }

    @OnClick({R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv:
                this.finish();
                break;

        }
    }

}