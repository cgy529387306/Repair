package com.yxw.cn.carpenterrepair.activity.login;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.UserMainActivity;
import com.yxw.cn.carpenterrepair.activity.main.WorkerMainActivity;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import util.EventBusUtil;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_tel)
    EditText mEtTel;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_show)
    ImageView mIvShow;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        titlebar.setTitle("登录");
        titlebar.setLeftVisible(getIntent().getBooleanExtra("back", true));
        mEtTel.setText(SpUtil.getStr(SpConstant.LOGIN_MOBILE));
    }

    @OnClick({R.id.tv_quick_login, R.id.iv_show, R.id.tv_login, R.id.tv_register, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_quick_login:
                startActivity(QuickLoginActivity.class);
                break;
            case R.id.iv_show:
                if (mEtPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mIvShow.setImageResource(R.drawable.eyes_off);
                } else {
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mIvShow.setImageResource(R.drawable.eyes_on);
                }
                mEtPassword.setSelection(mEtPassword.getText().toString().length());
                break;
            case R.id.tv_login:
                if (TextUtils.isEmpty(mEtTel.getText().toString().trim())) {
                    toast("手机号不能为空！");
                } else if (mEtPassword.getText().toString().trim().isEmpty()) {
                    toast("密码不能为空！");
           /*     } else if (!AppUtil.isphone(mEtTel.getText().toString().trim())) {
                    toast("请输入正确的手机号！");*/
                } else {
                    showLoading();
                    Map<String, String> map = new HashMap<>();
                    map.put("mobile", mEtTel.getText().toString().trim());
                    map.put("password", mEtPassword.getText().toString().trim());
                    OkGo.<ResponseData<LoginInfo>>post(UrlConstant.LOGIN)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<LoginInfo>>() {
                                         @Override
                                         public void onSuccess(ResponseData<LoginInfo> response) {
                                             dismissLoading();
                                             if (response.getCode() == 0) {
                                                 SpUtil.putStr(SpConstant.LOGIN_MOBILE, mEtTel.getText().toString().trim());
                                                 SpUtil.putStr(SpConstant.TOKEN, response.getData().getToken());
                                                 SpUtil.putStr(SpConstant.USERID, response.getData().getUserId());
                                                 SpUtil.putInt(SpConstant.ROLE, response.getData().getRole());
                                                 SpUtil.putStr(SpConstant.LOGIN_INFO, gson.toJson(response.getData()));
                                                 switch (response.getData().getRole()) {
                                                     case 0:
                                                         startActivityFinish(UserMainActivity.class);
                                                         break;
                                                     default:
                                                         startActivityFinish(WorkerMainActivity.class);
                                                         break;
                                                 }
                                                 EventBusUtil.post(MessageConstant.LOGIN);
                                             }else {
                                                 toast(response.getMsg());
                                             }
                                         }

                                         @Override
                                         public void onError(Response<ResponseData<LoginInfo>> response) {
                                             super.onError(response);
                                             dismissLoading();
                                         }
                                     }
                            );
                }
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_password:
                startActivity(ForgetPasswordActivity.class);
                break;
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.LOGIN:
            case MessageConstant.REGISTER:
                finish();
                break;
        }
    }
}
