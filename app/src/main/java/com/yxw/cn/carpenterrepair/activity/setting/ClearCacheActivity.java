package com.yxw.cn.carpenterrepair.activity.setting;

import android.view.View;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.util.CacheHelper;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.ToastUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 清理缓存
 */
public class ClearCacheActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv3)
    TextView used;
    @BindView(R.id.tv4)
    TextView afterClear;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_clear_cache;
    }

    @Override
    public void initView() {
        titleBar.setTitle("清理缓存");
        used.setText(CacheHelper.getCacheSize(this));
    }

    @OnClick({ R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                CacheHelper.cleanCache(ClearCacheActivity.this);
                ToastUtil.show("清理缓存成功！");
                used.setText(CacheHelper.getCacheSize(this));
                EventBusUtil.post(MessageConstant.CLEAR_CACHE);
                break;
        }

    }

}
