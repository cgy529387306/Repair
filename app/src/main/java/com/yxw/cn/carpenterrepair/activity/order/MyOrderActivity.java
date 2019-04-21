package com.yxw.cn.carpenterrepair.activity.order;


import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.fragment.OrderFragment;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 订单列表
 * @author @author chenqm on 2018/1/15.
 */

public class MyOrderActivity extends BaseActivity{

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private String[] mTitles = {"今天","明天","全部"};

    @Override
    protected int getLayoutResId() {
        return R.layout.act_my_order;
    }

    @Override
    public void initView() {
        OrderType orderType = (OrderType) getIntent().getSerializableExtra("type");
        titlebar.setTitle(orderType.getName());

        mFragmentList = new ArrayList<>();
        mFragmentList.add(OrderFragment.getInstance(0,orderType.getType()));
        mFragmentList.add(OrderFragment.getInstance(1,orderType.getType()));
        mFragmentList.add(OrderFragment.getInstance(2,orderType.getType()));
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
        mViewPager.setOffscreenPageLimit(mFragmentList.size());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
    }


}
