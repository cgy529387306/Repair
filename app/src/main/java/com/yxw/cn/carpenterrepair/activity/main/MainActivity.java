package com.yxw.cn.carpenterrepair.activity.main;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.fragment.HomeFragment;
import com.yxw.cn.carpenterrepair.fragment.UserFragment;

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
    private FragmentManager fragmentManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_main;
    }

    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        showFragment(0);
    }

    private void showFragment(int page) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // 想要显示一个fragment,先隐藏所有fragment，防止重叠
        hideFragments(ft);
        switch (page) {
            case 0:
                ImmersionBar.with(MainActivity.this).fitsSystemWindows(true).statusBarColor(R.color.white).statusBarDarkFont(true).init();
                if (homeFragment != null)
                    ft.show(homeFragment);
                else {
                    homeFragment = new HomeFragment();
                    ft.add(R.id.main_container_content, homeFragment);
                }
                break;
            case 1:
                ImmersionBar.with(MainActivity.this).fitsSystemWindows(true).statusBarColor(R.color.bg_personal).statusBarDarkFont(false).init();
                if (userFragment != null) {
                    ft.show(userFragment);
                } else {
                    userFragment = new UserFragment();
                    ft.add(R.id.main_container_content, userFragment);
                }
                break;
        }
        ft.commit();
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


    // 当fragment已被实例化，相当于发生过切换，就隐藏起来
    public void hideFragments(FragmentTransaction ft) {
        if (homeFragment != null)
            ft.hide(homeFragment);
        if (userFragment != null)
            ft.hide(userFragment);
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


}
