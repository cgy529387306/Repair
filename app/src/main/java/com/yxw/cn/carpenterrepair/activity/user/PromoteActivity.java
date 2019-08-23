package com.yxw.cn.carpenterrepair.activity.user;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 推广
 */
public class PromoteActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_promote;
    }

    @Override
    public void initView() {
        titleBar.setTitle("推广码");
        getPromote();
    }


    private void getPromote(){
        showLoading();
        Map<String, String> map = new HashMap<>();
        OkGo.<ResponseData<Object>>post(UrlConstant.GET_QR)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<Object>>() {
                             @Override
                             public void onSuccess(ResponseData<Object> response) {
                                 dismissLoading();
                                 if (response!=null){

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
