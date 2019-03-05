package com.yxw.cn.carpenterrepair.activity;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import io.rong.imkit.RongIM;

public class ConversationActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_customer_service1;
    }

    @Override
    public void initView() {
        titlebar.setTitle("匠修客服");
//        if (RongIM.getInstance() != null) {
//            RongIM.getInstance().startPrivateChat(this, "KEFU154510079986682", "标题");
//        }
    }
}
