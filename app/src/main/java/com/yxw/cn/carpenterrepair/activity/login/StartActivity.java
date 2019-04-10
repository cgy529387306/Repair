package com.yxw.cn.carpenterrepair.activity.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
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
import com.yxw.cn.carpenterrepair.util.SpUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import util.EventBusUtil;

public class StartActivity extends BaseActivity {


    @BindView(R.id.et_tel)
    EditText mEtTel;
    @BindView(R.id.et_password)
    EditText mEtPassword;

    private Handler handler = new Handler();
    private SweetAlertDialog dialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_start;
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
    }

    @Override
    public void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public void init() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (SpUtil.getInt(SpConstant.ROLE)) {
                    case 0:
                        startActivityFinish(UserMainActivity.class);
                        break;
                    case 1:
                        startActivityFinish(WorkerMainActivity.class);
                        break;
                    case 2:
                        startActivityFinish(WorkerMainActivity.class);
                        break;
                    default:
                        break;
                }
            }
        }, 500);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Logger.d("check");
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 22);

        } else {
            init();
        }
    }

    public void showPermissionDialog() {
        if (dialog == null) {
            dialog = new SweetAlertDialog(this)
                    .setTitleText("请开启权限")
                    .setCancelText("取消")
                    .setConfirmText("确定")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent mIntent = new Intent();
                            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            mIntent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivity(mIntent);
                            sDialog.cancel();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            finish();
                            sDialog.cancel();
                        }
                    });
            dialog.setCancelable(false);
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Logger.d("onRequestPermissionsResult");
        switch (requestCode) {
            case 22: {
                if (grantResults.length == 0) {
                    showPermissionDialog();
                    return;
                }
                // 用户拒绝了某些权限
                for (int x : grantResults) {
                    if (x == PackageManager.PERMISSION_DENIED) {
                        showPermissionDialog();
                        return;
                    }
                }
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    init();
                }
            }
        }
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
