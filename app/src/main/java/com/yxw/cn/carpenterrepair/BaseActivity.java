package com.yxw.cn.carpenterrepair;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.yxw.cn.carpenterrepair.activity.user.LoginActivity;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private LoadingDailog loadDialog;
    public Gson gson = new Gson();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        BaseApplication.getInstance().addActivity(this);
        EventBusUtil.register(this);
        ButterKnife.bind(this);
        setStatusBar();
        initData();
        initView();
        getData();
    }

    protected abstract int getLayoutResId();

    public void setStatusBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true).init();
    }

    public void initData() {
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void initView() {
    }

    public void getData() {
    }

    public void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    public void startActivity(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startActivityFinish(Class clazz) {
        startActivity(new Intent(this, clazz));
        finish();
    }

    public void startActivityFinish(Class clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void toast(int msgId) {
        toast(getResources().getString(msgId));
    }

    public void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toastNetError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoading();
        EventBusUtil.unregister(this);
        BaseApplication.getInstance().removeActivity(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        onEvent(event);
    }

    public void onEvent(MessageEvent event) {
        switch (event.getId()) {
            case MessageConstant.LOGOUT:
                if (!(this instanceof LoginActivity)) {
                    finish();
                }
                break;
        }
    }

    public void toastError(Object object) {
        if (object == null) {
            toast("网络异常");
        } else {
            toast(object.toString());
        }
    }


    public void showLoading() {
        if (loadDialog == null) {
            LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                    .setMessage("请稍后...")
                    .setCancelable(true)
                    .setCancelOutside(true);
            loadDialog = loadBuilder.create();
        }
        loadDialog.show();
    }

    public void dismissLoading() {
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        onPermGranted(requestCode, perms);
    }

    public void onPermGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        onPermDenied(requestCode, perms);
    }

    public void onPermDenied(int requestCode, @NonNull List<String> perms) {
    }
}
