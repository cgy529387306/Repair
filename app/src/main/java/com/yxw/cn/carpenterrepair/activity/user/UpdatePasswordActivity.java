package com.yxw.cn.carpenterrepair.activity.user;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class UpdatePasswordActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_phone)
    EditText mTvPhone;
    @BindView(R.id.et_password_old)
    EditText oldPassword;
    @BindView(R.id.et_password_new)
    EditText newPassword;
    @BindView(R.id.et_password_confirm)
    EditText newPassword2;
    @BindView(R.id.iv_show_old)
    ImageView eyes1;
    @BindView(R.id.iv_show_new)
    ImageView eyes2;
    @BindView(R.id.iv_show_confirm)
    ImageView eyes3;
    int flag1 = 0;
    int flag2 = 0;
    int flag3 = 0;
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.act_update_password;
    }

    @Override
    public void initView() {
        titlebar.setTitle("修改密码");
        mTvPhone.setText(SpUtil.getStr(SpConstant.LOGIN_MOBILE));
        oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    @OnClick({R.id.iv_show_old, R.id.iv_show_new, R.id.iv_show_confirm, R.id.btn_confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(oldPassword.getText().toString().trim())) {
                    toast("您还未输入旧密码！");
                } else if (TextUtils.isEmpty(newPassword.getText().toString().trim())) {
                    toast("您还未输入新密码！");
                } else if (TextUtils.isEmpty(newPassword2.getText().toString().trim())) {
                    toast("您还未输入确认密码！");
                } else if (newPassword.getText().toString().trim().length() < 6 || newPassword.getText().toString().trim().length() > 16) {
                    toast("新密码为6到16个字符或数字！");
                } else if (!newPassword.getText().toString().trim().equals(newPassword2.getText().toString().trim())) {
                    toast("新密码两次输入不相同！");
                } else {
                    showLoading();
                    Map<String, String> map = new HashMap<>();
                    map.put("password", oldPassword.getText().toString().trim());
                    map.put("newPassword", newPassword.getText().toString().trim());
                    OkGo.<ResponseData<String>>post(UrlConstant.MODIFY_PASSWORD)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                         @Override
                                         public void onSuccess(ResponseData<String> response) {
                                             dismissLoading();
                                             toast(response.getMsg());
                                             if (response.isSuccess()) {
                                                 finish();
                                             }
                                         }

                                         @Override
                                         public void onError(Response<ResponseData<String>> response) {
                                             super.onError(response);
                                             dismissLoading();
                                             toast("网络异常");
                                         }
                                     }
                            );
                }
                break;
            case R.id.iv_show_old:
                if (flag1 == 0) {
                    flag1 = 1;
                    oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyes1.setImageResource(R.drawable.eyes_on);
                } else {
                    flag1 = 0;
                    oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyes1.setImageResource(R.drawable.eyes_off);
                }
                break;
            case R.id.iv_show_new:
                if (flag2 == 0) {
                    flag2 = 1;
                    newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyes2.setImageResource(R.drawable.eyes_on);
                } else {
                    flag2 = 0;
                    newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyes2.setImageResource(R.drawable.eyes_off);
                }
                break;
            case R.id.iv_show_confirm:
                if (flag3 == 0) {
                    flag3 = 1;
                    newPassword2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eyes3.setImageResource(R.drawable.eyes_on);
                } else {
                    flag3 = 0;
                    newPassword2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eyes3.setImageResource(R.drawable.eyes_off);
                }
                break;
        }
    }

}
