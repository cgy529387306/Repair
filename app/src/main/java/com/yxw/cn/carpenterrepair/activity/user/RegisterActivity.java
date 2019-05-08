package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.CountDownTextView;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_show)
    ImageView mIvShow;
    @BindView(R.id.bt_code)
    CountDownTextView mCountDownTextView;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_register;
    }

    @Override
    public void initView() {
        titlebar.setTitle("注册");
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AppUtil.isPhone(s.toString())) {
                    mCountDownTextView.setBackgroundResource(R.drawable.corner_red);
                } else {
                    mCountDownTextView.setBackgroundResource(R.drawable.corner_gray);
                }
            }
        });
        mCountDownTextView.setNormalText("获取验证码")
                .setCountDownText("重新获取", "")
                .setCloseKeepCountDown(false)//关闭页面保持倒计时开关
                .setCountDownClickable(false)//倒计时期间点击事件是否生效开关
                .setShowFormatTime(true)//是否格式化时间
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
                        Toast.makeText(RegisterActivity.this, "倒计时完毕", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppUtil.isPhone(mEtPhone.getText().toString())) {
                            showLoading();
                            Map<String, String> map = new HashMap<>();
                            map.put("mobile", mEtPhone.getText().toString());
                            OkGo.<ResponseData<String>>post(UrlConstant.GET_CODE)
                                    .upJson(gson.toJson(map))
                                    .execute(new JsonCallback<ResponseData<String>>() {
                                                 @Override
                                                 public void onSuccess(ResponseData<String> response) {
                                                     dismissLoading();
                                                     if (response!=null){
                                                         if (response.isSuccess()){
                                                             toast("短信发送成功");
                                                             mCountDownTextView.startCountDown(59);
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
                        } else {
                            toast("请输入正确的手机号！");
                        }
                    }
                });
    }

    @OnClick({R.id.iv_show, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.tv_register:
                if (TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                    toast("手机号不能为空！");
                } else if (TextUtils.isEmpty(mEtCode.getText().toString().trim())) {
                    toast("验证码不能为空！");
                } else if (TextUtils.isEmpty(mEtPassword.getText().toString().trim())) {
                    toast("密码不能为空！");
                } else if (mEtPassword.getText().toString().trim().length() < 6 || mEtPassword.getText().toString().trim().length() > 16) {
                    toast("新密码为6到16个字符或数字！");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userName", mEtPhone.getText().toString().trim());
                    map.put("password", mEtPassword.getText().toString().trim());
                    map.put("smsCode", mEtCode.getText().toString().trim());
                    map.put("appSign", UrlConstant.mRoleSign);
                    showLoading();
                    OkGo.<ResponseData<String>>post(UrlConstant.REGISTER)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                         @Override
                                         public void onSuccess(ResponseData<String> response) {
                                             dismissLoading();
                                             if (response!=null){
                                                 if (response.isSuccess()) {
                                                     toast("注册成功");
                                                     SpUtil.putStr(SpConstant.LOGIN_MOBILE, mEtPhone.getText().toString().trim());
                                                     EventBusUtil.post(MessageConstant.REGISTER);
                                                     finish();
//                                                     Intent intent = new Intent(RegisterActivity.this, ChooseCategoryActivity.class);
//                                                     Bundle bundle = new Bundle();
//                                                     bundle.putSerializable("cateList", (Serializable) new ArrayList<String>());
//                                                     bundle.putBoolean("canBack",false);
//                                                     intent.putExtras(bundle);
//                                                     startActivity(intent);
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
                }
                break;
        }
    }


    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.LOGIN:
        }
    }
}
