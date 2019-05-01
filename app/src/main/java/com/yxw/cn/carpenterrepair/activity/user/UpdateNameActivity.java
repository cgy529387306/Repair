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
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改姓名
 */
public class UpdateNameActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.et_name)
    EditText mEtName;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_update_name;
    }

    @Override
    public void initView() {
        titleBar.setTitle("修改姓名");
        if (CurrentUser.getInstance().isLogin()) {
            CurrentUser currentUser = CurrentUser.getInstance();
            if (!TextUtils.isEmpty(currentUser.getRealName())) {
                mEtName.setText(currentUser.getRealName());
                mEtName.setSelection(currentUser.getRealName().length());
            }
        }
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (TextUtils.isEmpty(mEtName.getText().toString().trim())) {
                    toast("您还未输入姓名！");
                    return;
                }
                showLoading();
                Map<String, String> map = new HashMap<>();
                map.put("realName", mEtName.getText().toString().trim());
                OkGo.<ResponseData<String>>post(UrlConstant.CHANGE_USERINFO)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<String>>() {
                                     @Override
                                     public void onSuccess(ResponseData<String> response) {
                                         dismissLoading();
                                         if (response != null){
                                             if (response.isSuccess()) {
                                                 EventBusUtil.post(MessageConstant.NOTIFY_GET_INFO);
                                                 finish();
                                             } else {
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
}
