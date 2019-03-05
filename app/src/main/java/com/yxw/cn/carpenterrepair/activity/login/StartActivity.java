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
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.UserMainActivity;
import com.yxw.cn.carpenterrepair.activity.main.WorkerMainActivity;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.util.SpUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class StartActivity extends BaseActivity {

    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    private Handler handler = new Handler();
    private SweetAlertDialog dialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_start;
    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                startActivity(LoginActivity.class);
//                startActivity(UserMainActivity.class);
                break;
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
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
        if (TextUtils.isEmpty(SpUtil.getStr(SpConstant.LOGIN_MOBILE))
//                || TextUtils.isEmpty(SpUtil.getStr(SpConstant.TOKEN))
                || -1 == SpUtil.getInt(SpConstant.ROLE, -1)) {
            mLlBottom.setVisibility(View.VISIBLE);
        } else {
            mLlBottom.setVisibility(View.GONE);
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
                            mLlBottom.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }, 500);
        }
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
     /*   AppUtil.initRegionTreeData();
        AppUtil.initCategoryData();
        AppUtil.initTimeData();*/
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
