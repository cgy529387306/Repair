package com.yxw.cn.carpenterrepair.activity.user;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.MainActivity;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.PreferencesHelper;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.CountDownTextView;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 验证码登录
 */
public class QuickLoginActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_tel)
    EditText mEtTel;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.tv_get_code)
    CountDownTextView mCountDownTextView;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_quick_login;
    }

    @Override
    public void initView() {
        titlebar.setTitle("验证码登录");
        mEtTel.setText(SpUtil.getStr(SpConstant.LOGIN_MOBILE));
        mEtTel.setSelection(mEtTel.getText().toString().length());
        validPhone();
        mEtTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                validPhone();
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
                        Toast.makeText(QuickLoginActivity.this, "倒计时完毕", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppUtil.isPhone(mEtTel.getText().toString())) {
                            showLoading();
                            Map<String, String> map = new HashMap<>();
                            map.put("mobile", mEtTel.getText().toString());
                            OkGo.<ResponseData<Object>>post(UrlConstant.GET_CODE)
                                    .upJson(gson.toJson(map))
                                    .execute(new JsonCallback<ResponseData<Object>>() {
                                                 @Override
                                                 public void onSuccess(ResponseData<Object> response) {
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
                                                 public void onError(Response<ResponseData<Object>> response) {
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

    private void validPhone(){
        if (AppUtil.isPhone(mEtTel.getText().toString())) {
            mCountDownTextView.setEnabled(true);
            mCountDownTextView.setBackgroundResource(R.drawable.corner_red);
        } else {
            mCountDownTextView.setEnabled(false);
            mCountDownTextView.setBackgroundResource(R.drawable.corner_gray);
        }
    }

    @OnClick({R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (TextUtils.isEmpty(mEtTel.getText().toString().trim())) {
                    toast("手机号不能为空！");
                } else if (TextUtils.isEmpty(mEtPassword.getText().toString().trim())) {
                    toast("验证码不能为空！");
                } else {
                    showLoading();
                    Map<String, Object> map = new HashMap<>();
                    map.put("userName", mEtTel.getText().toString());
                    map.put("smsCode", mEtPassword.getText().toString().trim());
                    map.put("appSign", UrlConstant.mRoleSign);
                    map.put("lastLoginSystem", "Android");
                    String rid = PreferencesHelper.getInstance().getString(SpConstant.REGISTER_ID);
                    if (Helper.isEmpty(rid)){
                        rid = JPushInterface.getRegistrationID(getApplicationContext());
                    }
                    map.put("regId", rid);
                    OkGo.<ResponseData<LoginInfo>>post(UrlConstant.QUICK_LOGIN)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<LoginInfo>>() {
                                         @Override
                                         public void onSuccess(ResponseData<LoginInfo> response) {
                                             dismissLoading();
                                             if (response!=null){
                                                 if (response.isSuccess()) {
                                                     toast("登录成功");
                                                     SpUtil.putStr(SpConstant.LOGIN_MOBILE, mEtTel.getText().toString().trim());
                                                     CurrentUser.getInstance().login(response.getData());
                                                     HttpHeaders headers = new HttpHeaders();
                                                     headers.put("Authorization", "Bearer "+response.getData().getToken());
                                                     OkGo.getInstance().addCommonHeaders(headers);
                                                     startActivityFinish(MainActivity.class);
                                                 }else {
                                                     toast(response.getMsg());
                                                 }
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
        }
    }

}
