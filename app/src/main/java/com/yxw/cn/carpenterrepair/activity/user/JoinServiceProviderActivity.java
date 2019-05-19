package com.yxw.cn.carpenterrepair.activity.user;


import android.text.TextUtils;
import android.view.View;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.ClearEditText;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 加入服务商
 */
public class JoinServiceProviderActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;

    @BindView(R.id.et_parent_id)
    ClearEditText mEtParentId;
    @Override
    protected int getLayoutResId() {
        return R.layout.act_join_service_provider;
    }

    @Override
    public void initView() {
        titleBar.setTitle("加入服务商");
        if (CurrentUser.getInstance().isLogin()){
            mEtParentId.setText(CurrentUser.getInstance().getParentId());
            mEtParentId.setSelection(mEtParentId.getText().toString().length());
        }
    }

    @OnClick({R.id.btn_confirm})
    public void click(View view) {
        if (view.getId() == R.id.btn_confirm){
            doConfirm();
        }
    }

    private void doConfirm(){
        String parentId = mEtParentId.getText().toString().trim();
        if (TextUtils.isEmpty(parentId)){
            toast("请输入要加入的服务商编号");
            return;
        }
        OkGo.<ResponseData<Object>>post(UrlConstant.JOIN_SERVICE+parentId)
                .execute(new JsonCallback<ResponseData<Object>>() {
                             @Override
                             public void onSuccess(ResponseData<Object> response) {
                                 dismissLoading();
                                 if (response!=null){
                                     if (response.isSuccess()) {
                                         toast("申请成功");
                                         startActivityFinish(ApplyServiceProviderActivity.class);
                                     }else{
                                         toast(response.getMsg());
                                     }
                                 }
                             }

                             @Override
                             public void onError(Response<ResponseData<Object>> response) {
                                 super.onError(response);
                                 dismissLoading();
                             }
                         }
                );
    }
}
