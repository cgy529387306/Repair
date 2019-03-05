package ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import util.EventBusUtil;
import util.RegionPickerUtil;
import util.ToastUtil;

/**
 * Created by CY on 2018/11/25
 */
public class LocationActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.areaTv)
    TextView areaTv;
    @BindView(R.id.et_detail)
    EditText et_detail;
    @BindView(R.id.mapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private GeoCoder mSearch;
    private boolean isFinish;
    private OnGetGeoCoderResultListener geoCoderListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                isFinish = false;
                ToastUtil.show("没有检索到结果");
                //没有检索到结果
            } else {
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
//                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(result.getLocation().latitude)
                        .longitude(result.getLocation().longitude).build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);
                LatLng ll = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                if (isFinish) {
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
                } else {
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            }

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                isFinish = false;
            } else {
                if (isFinish) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("addr1", areaTv.getText().toString().replace("-", ""));
                    map.put("addr2", et_detail.getText().toString());
                    map.put("result", result);
                    EventBusUtil.post(MessageConstant.LOCATION, map);
                    finish();
                }
            }
            //获取反向地理编码结果
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.act_location;
    }

    @Override
    public void initView() {
        titleBar.setTitle("选择服务地址");
        titleBar.addAction(new TitleBar.TextAction("确定") {
            @Override
            public void performAction(View view) {
                if (!TextUtils.isEmpty(areaTv.getText().toString())) {
                    isFinish = true;
                    confirmLocation();
                }
            }
        });
        initLocation();
    }

    @OnClick({R.id.rl_location, R.id.areaTv, R.id.complete})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_location:
                mLocationClient.start();
                break;
            case R.id.areaTv:
                RegionPickerUtil.showPicker(this, areaTv, true);
                break;
            case R.id.complete:
                confirmLocation();
                break;
        }
    }

    private void confirmLocation() {
        mSearch.geocode(new GeoCodeOption()
                .city(areaTv.getText().toString().split("-")[1])
                .address(areaTv.getText().toString().split("-")[2] + et_detail.getText().toString()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    private void initLocation() {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(geoCoderListener);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
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
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置自定义图标
//            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
//                    .fromResource(R.drawable.icon_add2);
//            MyLocationConfiguration config = new MyLocationConfiguration(
//                    MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
//            mBaiduMap.setMyLocationConfigeration(config);
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
                areaTv.setTag(location.getAdCode());
                areaTv.setText(location.getProvince() + "-" + location.getCity() + "-" + location.getDistrict());
                et_detail.setText(location.getStreet());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                areaTv.setTag(location.getAdCode());
                areaTv.setText(location.getProvince() + "-" + location.getCity() + "-" + location.getDistrict());
                et_detail.setText(location.getStreet());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                areaTv.setText(location.getProvince() + "-" + location.getCity() + "-" + location.getDistrict());
                areaTv.setTag(location.getAdCode());
                et_detail.setText(location.getStreet());
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(LocationActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(LocationActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(LocationActivity.this, "请确认手机是否开启gps", Toast.LENGTH_SHORT).show();
            }
            mLocationClient.stop();
        }
    }

}
