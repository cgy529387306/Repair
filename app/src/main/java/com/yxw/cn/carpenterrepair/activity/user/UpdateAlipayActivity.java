package com.yxw.cn.carpenterrepair.activity.user;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更改阿里账号
 */
public class UpdateAlipayActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.et_name)
    EditText mEtName;

    private LoginInfo loginInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_update_aliaccount;
    }

    @Override
    public void initView() {
        titleBar.setTitle("绑定支付宝账号");
        if (CurrentUser.getInstance().isLogin()) {
            loginInfo = CurrentUser.getInstance();
            if (!TextUtils.isEmpty(loginInfo.getAliplayAccount())) {
                titleBar.setTitle("修改支付宝账号");
                mEtName.setText(loginInfo.getAliplayAccount());
                mEtName.setSelection(loginInfo.getAliplayAccount().length());
            }else{
                titleBar.setTitle("绑定支付宝账号");
            }
        } else {
            loginInfo = new LoginInfo();
        }
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                String aliAccount = mEtName.getText().toString().trim();
                if (TextUtils.isEmpty(aliAccount)) {
                    toast("请输入支付宝账号！");
                    return;
                }
                showLoading();
                OkGo.<ResponseData<String>>post(UrlConstant.UPDATE_ALIPAY_ACCOUNT+aliAccount)
                        .execute(new JsonCallback<ResponseData<String>>() {
                                     @Override
                                     public void onSuccess(ResponseData<String> response) {
                                         dismissLoading();
                                         if (response!=null){
                                             if (response.isSuccess()){
                                                 toast("保存成功");
                                                 loginInfo.setAliplayAccount(aliAccount);
                                                 CurrentUser.getInstance().login(loginInfo);
                                                 EventBusUtil.post(MessageConstant.NOTIFY_GET_INFO);
                                                 finish();
                                             }else{
                                                 toast(response.getMsg());
                                             }
                                         }
                                     }

                                     @Override
                                     public void onError(Response<ResponseData<String>> response) {
                                         super.onError(response);
                                         dismissLoading();
                                     }
                                 }
                        );
                break;
        }
    }
    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_INFO:
                initData();
                break;
        }
    }

}
