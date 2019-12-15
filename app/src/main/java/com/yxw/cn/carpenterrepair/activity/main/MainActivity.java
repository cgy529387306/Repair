package com.yxw.cn.carpenterrepair.activity.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.fragment.HomeFragment;
import com.yxw.cn.carpenterrepair.fragment.UserFragment;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.ActivityManager;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.MyTaskUtil;
import com.yxw.cn.carpenterrepair.util.RegionPickerUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 * Created by cgy on 2018/11/25
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_work)
    TextView tv_work;
    @BindView(R.id.tv_personal)
    TextView tv_personal;

    private HomeFragment homeFragment;
    private UserFragment userFragment;
    private Fragment currentFragment;
    @Override
    protected int getLayoutResId() {
        return R.layout.act_main;
    }

    @Override
    public void initView() {
        homeFragment = new HomeFragment();
        userFragment = new UserFragment();
        showFragment(0);
        PgyUpdateManager.setIsForced(false); //设置是否强制更新。true为强制更新；false为不强制更新（默认值）。
        PgyUpdateManager.register(this);
        PgyCrashManager.register(this);
        getUserInfo();
        AppUtil.initCategoryData();
        AppUtil.initRegionTreeData();
        AppUtil.initSignReasonData();
        AppUtil.initReservationReasonData();
        AppUtil.initReturnData();
        MyTaskUtil.setVersion();
        MyTaskUtil.doTimeTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
        PgyCrashManager.unregister();
    }

    private void showFragment(int page) {
        switch (page) {
            case 0:
                ImmersionBar.with(MainActivity.this).fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true).init();
                switchFragment(homeFragment).commit();
                break;
            case 1:
                ImmersionBar.with(MainActivity.this).fitsSystemWindows(true).statusBarColor(R.color.bg_personal).statusBarDarkFont(false).init();
                switchFragment(userFragment).commit();
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
        ActivityManager.getInstance().closeAllActivity();
        finish();
    }

    @OnClick({R.id.tv_work, R.id.tv_personal})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_work:
                showFragment(0);
                tv_work.setTextColor(Color.parseColor("#FF5E5E"));
                tv_personal.setTextColor(Color.parseColor("#666666"));

                Drawable drawable = getResources().getDrawable(R.drawable.work_on);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_work.setCompoundDrawables(null, drawable, null, null);

                Drawable drawable3 = getResources().getDrawable(R.drawable.personal_off);
                drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable3, null, null);

                break;
            case R.id.tv_personal:
                getUserInfo();
                showFragment(1);
                tv_personal.setTextColor(Color.parseColor("#FF5E5E"));
                tv_work.setTextColor(Color.parseColor("#666666"));

                Drawable drawable7 = getResources().getDrawable(R.drawable.work_off);
                drawable7.setBounds(0, 0, drawable7.getMinimumWidth(), drawable7.getMinimumHeight());
                tv_work.setCompoundDrawables(null, drawable7, null, null);

                Drawable drawable9 = getResources().getDrawable(R.drawable.personal_on);
                drawable9.setBounds(0, 0, drawable9.getMinimumWidth(), drawable9.getMinimumHeight());
                tv_personal.setCompoundDrawables(null, drawable9, null, null);
                break;
        }
    }

    private void getUserInfo(){
        if (CurrentUser.getInstance().isLogin()){
            OkGo.<ResponseData<LoginInfo>>get(UrlConstant.GET_WORKER_INFO)
                    .execute(new JsonCallback<ResponseData<LoginInfo>>() {
                                 @Override
                                 public void onSuccess(ResponseData<LoginInfo> response) {
                                     if (response!=null){
                                         if (response.isSuccess()) {
                                             CurrentUser.getInstance().login(response.getData());
                                             EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_INFO);
                                         }
                                     }
                                 }
                             }
                    );
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_GET_INFO:
                getUserInfo();
                break;
            case MessageConstant.SELECT_AREA:
                RegionPickerUtil.showPicker(this, findViewById(R.id.tv_tag), true);
                break;
        }
    }

    private  FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.main_container_content, targetFragment,targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }
}
