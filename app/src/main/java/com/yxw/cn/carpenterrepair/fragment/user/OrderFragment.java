package com.yxw.cn.carpenterrepair.fragment.user;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.MyFragmentPagerAdapter;
import com.yxw.cn.carpenterrepair.fragment.UserOrderListFragment;
import com.yxw.cn.carpenterrepair.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CY on 2018/11/25
 */
public class OrderFragment extends BaseFragment {

    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_wjz)
    TextView tv_wjz;
    @BindView(R.id.tv_dfw)
    TextView tv_dfw;
    @BindView(R.id.tv_jxz)
    TextView tv_jxz;
    @BindView(R.id.tv_ywc)
    TextView tv_ywc;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.v2)
    View v2;
    @BindView(R.id.v3)
    View v3;
    @BindView(R.id.v4)
    View v4;
    @BindView(R.id.v5)
    View v5;
    @BindView(R.id.view_pager)
    NoScrollViewPager mVp;

    private MyFragmentPagerAdapter myAdapter;
    private List<Fragment> mFragments;

    @Override
    protected int getLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", BaseRefreshFragment.loadCount);
        mFragments.add(UserOrderListFragment.newInstance(gson.toJson(map),1));
        map.clear();
        map.put("receiveStatus", "0");
        map.put("page", 1);
        map.put("limit", BaseRefreshFragment.loadCount);
        mFragments.add(UserOrderListFragment.newInstance(gson.toJson(map),2));
        map.clear();
        map.put("receiveStatus", "1");
        map.put("serviceStatus", "0");
        map.put("page", 1);
        map.put("limit", BaseRefreshFragment.loadCount);
        mFragments.add(UserOrderListFragment.newInstance(gson.toJson(map),3));
        map.clear();
        map.put("receiveStatus", "1");
        map.put("serviceStatus", "1");
        map.put("page", 1);
        map.put("limit", BaseRefreshFragment.loadCount);
        mFragments.add(UserOrderListFragment.newInstance(gson.toJson(map),4));
        map.clear();
        map.put("receiveStatus", "1");
        map.put("serviceStatus", "3");
        map.put("page", 1);
        map.put("limit", BaseRefreshFragment.loadCount);
        mFragments.add(UserOrderListFragment.newInstance(gson.toJson(map),5));
        myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments);
        mVp.setAdapter(myAdapter);
        mVp.setOffscreenPageLimit(mFragments.size());
    }

    @OnClick({R.id.tv_all, R.id.tv_wjz, R.id.tv_dfw, R.id.tv_jxz, R.id.tv_ywc})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                onChangeTab(0);
                break;
            case R.id.tv_wjz:
                onChangeTab(1);
                break;
            case R.id.tv_dfw:
                onChangeTab(2);
                break;
            case R.id.tv_jxz:
                onChangeTab(3);
                break;
            case R.id.tv_ywc:
                onChangeTab(4);
                break;
        }
    }

    public void onChangeTab(int index) {
        switch (index) {
            case 0:
                change(tv_all, tv_wjz, tv_dfw, tv_jxz, tv_ywc, v1, v2, v3, v4, v5);
                break;
            case 1:
                change(tv_wjz, tv_all, tv_dfw, tv_jxz, tv_ywc, v2, v1, v3, v4, v5);
                break;
            case 2:
                change(tv_dfw, tv_all, tv_wjz, tv_jxz, tv_ywc, v3, v2, v1, v4, v5);
                break;
            case 3:
                change(tv_jxz, tv_all, tv_wjz, tv_dfw, tv_ywc, v4, v2, v3, v1, v5);
                break;
            case 4:
                change(tv_ywc, tv_all, tv_wjz, tv_dfw, tv_jxz, v5, v2, v3, v4, v1);
                break;
        }
        mVp.setCurrentItem(index);
    }

    private void change(TextView t1, TextView t2, TextView t3, TextView t4, TextView t5, View view1
            , View view2, View view3, View view4, View view5) {
        t1.setTextColor(getResources().getColor(R.color.text_red));
        t2.setTextColor(getResources().getColor(R.color.text_black));
        t3.setTextColor(getResources().getColor(R.color.text_black));
        t4.setTextColor(getResources().getColor(R.color.text_black));
        t5.setTextColor(getResources().getColor(R.color.text_black));
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.INVISIBLE);
        view4.setVisibility(View.INVISIBLE);
        view5.setVisibility(View.INVISIBLE);
    }
}
