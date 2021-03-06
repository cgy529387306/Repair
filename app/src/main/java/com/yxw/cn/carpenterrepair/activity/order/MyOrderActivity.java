package com.yxw.cn.carpenterrepair.activity.order;


import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OrderCount;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.fragment.OrderFragment;
import com.yxw.cn.carpenterrepair.util.LocationUtils;
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
        LocationUtils.instance().startLocation();
        OrderType orderType = (OrderType) getIntent().getSerializableExtra("type");
        titlebar.setTitle(orderType.getName());

        mFragmentList = new ArrayList<>();
        mFragmentList.add(OrderFragment.getInstance(orderType.getStatus(),0));
        mFragmentList.add(OrderFragment.getInstance(orderType.getStatus(),1));
        mFragmentList.add(OrderFragment.getInstance(orderType.getStatus(),2));
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

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.UPDATE_ORDER_COUNT:
                refreshCount(event);
                break;
        }
    }

    private void refreshCount(MessageEvent event){
        try {
            OrderCount orderCount = (OrderCount) event.getData();
            if (orderCount.getType() == 0){
                TabLayout.Tab tab = mTabLayout.getTabAt(orderCount.getType());
                tab.setText(orderCount.getCount()>0?"今天("+orderCount.getCount()+")":"今天");
            }else if (orderCount.getType() == 1){
                TabLayout.Tab tab = mTabLayout.getTabAt(orderCount.getType());
                tab.setText(orderCount.getCount()>0?"明天("+orderCount.getCount()+")":"明天");
            }else if (orderCount.getType() == 2){
                TabLayout.Tab tab = mTabLayout.getTabAt(orderCount.getType());
                tab.setText(orderCount.getCount()>0?"全部("+orderCount.getCount()+")":"全部");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
