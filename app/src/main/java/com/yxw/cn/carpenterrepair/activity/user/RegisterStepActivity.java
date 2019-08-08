package com.yxw.cn.carpenterrepair.activity.user;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.fragment.register.IdCardFragment;
import com.yxw.cn.carpenterrepair.fragment.register.RegisterFragment;
import com.yxw.cn.carpenterrepair.fragment.register.UserInfoFragment;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;

/**
 * 注册
 */
public class RegisterStepActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.lb_step1)
    TextView mLbStep1;
    @BindView(R.id.tv_step1)
    TextView mTvStep1;
    @BindView(R.id.lb_step2)
    TextView mLbStep2;
    @BindView(R.id.tv_step2)
    TextView mTvStep2;
    @BindView(R.id.lb_step3)
    TextView mLbStep3;
    @BindView(R.id.tv_step3)
    TextView mTvStep3;
    @BindView(R.id.line1)
    View mLine1;
    @BindView(R.id.line2)
    View mLine2;

    private RegisterFragment mRegisterFragment;
    private IdCardFragment mIdCardFragment;
    private UserInfoFragment mUserInfoFragment;
    private Fragment currentFragment;
    private int mCurrentStep;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_register_step;
    }

    @Override
    public void initView() {
        titlebar.setTitle("注册");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        int step = getIntent().getIntExtra("step",2);
        mRegisterFragment = new RegisterFragment();
        mIdCardFragment = new IdCardFragment();
        mUserInfoFragment = new UserInfoFragment();
        showFragment(step);
        AppUtil.initCategoryData();
        AppUtil.initRegionTreeData();
    }


    private void showFragment(int page) {
        mCurrentStep = page;
        initStep(mCurrentStep);
        switch (page) {
            case 0:
                switchFragment(mRegisterFragment).commit();
                break;
            case 1:
                switchFragment(mIdCardFragment).commit();
                break;
            case 2:
                switchFragment(mUserInfoFragment).commit();
                break;
            default:
                switchFragment(mRegisterFragment).commit();
                break;
        }
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.content_container, targetFragment,targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    private void initStep(int step){
        if (step == 0){
            mLbStep1.setBackgroundResource(R.drawable.circle_red_step);
            mTvStep1.setTextColor(Color.parseColor("#333333"));
            mLbStep2.setBackgroundResource(R.drawable.circle_grey_step);
            mTvStep2.setTextColor(Color.parseColor("#BBBBBB"));
            mLbStep3.setBackgroundResource(R.drawable.circle_grey_step);
            mTvStep3.setTextColor(Color.parseColor("#BBBBBB"));
            mLine1.setBackgroundColor(Color.parseColor("#E4E4E4"));
            mLine2.setBackgroundColor(Color.parseColor("#E4E4E4"));
        }else if (step == 1){
            mLbStep1.setBackgroundResource(R.drawable.circle_red_step);
            mTvStep1.setTextColor(Color.parseColor("#333333"));
            mLbStep2.setBackgroundResource(R.drawable.circle_red_step);
            mTvStep2.setTextColor(Color.parseColor("#333333"));
            mLbStep3.setBackgroundResource(R.drawable.circle_grey_step);
            mTvStep3.setTextColor(Color.parseColor("#BBBBBB"));
            mLine1.setBackgroundColor(Color.parseColor("#FF5E5E"));
            mLine2.setBackgroundColor(Color.parseColor("#E4E4E4"));
        }else if (step == 2){
            mLbStep1.setBackgroundResource(R.drawable.circle_red_step);
            mTvStep1.setTextColor(Color.parseColor("#333333"));
            mLbStep2.setBackgroundResource(R.drawable.circle_red_step);
            mTvStep2.setTextColor(Color.parseColor("#333333"));
            mLbStep3.setBackgroundResource(R.drawable.circle_red_step);
            mTvStep3.setTextColor(Color.parseColor("#333333"));
            mLine1.setBackgroundColor(Color.parseColor("#FF5E5E"));
            mLine2.setBackgroundColor(Color.parseColor("#FF5E5E"));
        }
    }

    public void goToNext(){
        if (mCurrentStep<2){
            mCurrentStep++;
            showFragment(mCurrentStep);
        }
    }

    @Override
    public void onBackPressed() {
        EventBusUtil.post(MessageConstant.REGISTER_OUT);
        super.onBackPressed();
    }
}
