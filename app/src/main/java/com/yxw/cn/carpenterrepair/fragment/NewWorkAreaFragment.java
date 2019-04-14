package com.yxw.cn.carpenterrepair.fragment;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.MyFragmentPagerAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CY on 2018/11/25
 */
public class NewWorkAreaFragment extends BaseFragment {

    @BindView(R.id.tv_current_city)
    TextView tvCurrentCity;
    @BindView(R.id.view_pager)
    NoScrollViewPager mVp;
    @BindView(R.id.tv_unorder)
    TextView mTvUnorder;
    @BindView(R.id.tv_ordered)
    TextView mTvOrdered;

    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;

    private MyFragmentPagerAdapter myAdapter;
    private List<Fragment> mFragments;
    private boolean toastLoactionErr = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_work_area;
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(new WorkerUnOrderFragment());
        mFragments.add(new WorkerOrderedFragment());
        myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragments);
        mVp.setAdapter(myAdapter);
        mVp.setOffscreenPageLimit(mFragments.size());
        initLocation();
        onChangeTab(0);
    }

    @OnClick({R.id.iv_message, R.id.tv_unorder, R.id.tv_ordered})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_message:
//                startActivity(CustomerServiceActivity.class);
                break;
            case R.id.tv_unorder:
                onChangeTab(0);
                break;
            case R.id.tv_ordered:
                onChangeTab(1);
                break;
        }
    }

    public void onChangeTab(int index) {
        switch (index) {
            case 0:
                mTvUnorder.setBackgroundColor(Color.parseColor("#FF5E5E"));
                mTvOrdered.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
            case 1:
                mTvOrdered.setBackgroundColor(Color.parseColor("#FF5E5E"));
                mTvUnorder.setBackgroundColor(getResources().getColor(R.color.transparent));
                break;
        }
        mVp.setCurrentItem(index);
    }

    private void initLocation() {
        mLocationClient = new LocationClient(getContext());
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 2000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    public void onEvent(MessageEvent event) {
        switch (event.getId()) {
            case MessageConstant.WORKER_UNORDER_COUNT:
                mTvUnorder.setText("未确认订单(" + event.getData() + ")");
                break;
            case MessageConstant.WORKER_ORDERED_COUNT:
                mTvOrdered.setText("已接订单(" + event.getData() + ")");
                break;
        }
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Logger.d("location: " + location);
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
                tvCurrentCity.setTag(location.getAdCode());
                tvCurrentCity.setText(location.getCity());
                mLocationClient.stop();
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                tvCurrentCity.setTag(location.getAdCode());
                tvCurrentCity.setText(location.getCity());
                mLocationClient.stop();
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                tvCurrentCity.setTag(location.getAdCode());
                tvCurrentCity.setText(location.getCity());
                mLocationClient.stop();
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                if (!toastLoactionErr) {
                    toastLoactionErr = true;
                    Toast.makeText(getContext(), "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                }
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                if (!toastLoactionErr) {
                    toastLoactionErr = true;
                    Toast.makeText(getContext(), "网络错误，请检查", Toast.LENGTH_SHORT).show();
                }
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                if (!toastLoactionErr) {
                    toastLoactionErr = true;
                    Toast.makeText(getContext(), "请确认手机是否开启gps", Toast.LENGTH_SHORT).show();
                }
            }
//            mLocationClient.stop();
        }
    }
}