package com.yxw.cn.carpenterrepair.activity.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.MainActivity;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.et_tel)
    EditText mEtTel;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_login;
    }

    @OnClick({R.id.tv_login, R.id.tv_register,R.id.tv_forget_password,R.id.tv_quick_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                doLogin();
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_password:
                startActivity(ForgetPasswordActivity.class);
                break;
            case R.id.tv_quick_login:
                startActivity(QuickLoginActivity.class);
                break;
        }
    }

    private void doLogin(){
        if (TextUtils.isEmpty(mEtTel.getText().toString().trim())) {
            toast("手机号不能为空！");
        } else if (mEtPassword.getText().toString().trim().isEmpty()) {
            toast("密码不能为空！");
        } else {
            showLoading();
            Map<String, Object> map = new HashMap<>();
            map.put("userName", mEtTel.getText().toString().trim());
            map.put("password", mEtPassword.getText().toString().trim());
            map.put("roleSign", UrlConstant.mRoleSign);
            OkGo.<ResponseData<LoginInfo>>post(UrlConstant.LOGIN)
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<LoginInfo>>() {
                                 @Override
                                 public void onSuccess(ResponseData<LoginInfo> response) {
                                     dismissLoading();
                                     if (response.isSuccess()) {
                                         SpUtil.putStr(SpConstant.LOGIN_MOBILE, mEtTel.getText().toString().trim());
                                         CurrentUser.getInstance().login(response.getData());
                                         startActivityFinish(MainActivity.class);
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
    }

    @Override
    public void initView() {
    }



    public void getData() {

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

    private static final long DOUBLE_CLICK_INTERVAL = 2000;
    private long mLastClickTimeMills = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastClickTimeMills > DOUBLE_CLICK_INTERVAL) {
            toast("再按一次返回退出");
            mLastClickTimeMills = System.currentTimeMillis();
            return;
        }
        finish();
    }
}
