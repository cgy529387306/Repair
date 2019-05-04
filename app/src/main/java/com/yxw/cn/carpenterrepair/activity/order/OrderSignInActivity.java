package com.yxw.cn.carpenterrepair.activity.order;

import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.CountDownTextView;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.ToastUtil;

/**
 * 签到
 */
public class OrderSignInActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.mapView)
    MapView mMapView;

    private OrderItem mOrderItem;
    private boolean flag;
    private String path;
    private BDLocation mLocation;
    private int page;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private GeoCoder mSearch;
    private OnGetGeoCoderResultListener geoCoderListener = new OnGetGeoCoderResultListener() {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtil.show("没有检索到订单具体位置！");
                //没有检索到结果
            } else {
                LatLng point = new LatLng(result.getLocation().latitude, result.getLocation().longitude);//坐标参数（纬度，经度）
                BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                        .fromView(View.inflate(OrderSignInActivity.this, R.layout.view_location_icon, null));
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(mCurrentMarker);
                mBaiduMap.addOverlay(option);
            }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("result", result);
                finish();
            }
            //获取反向地理编码结果
        }
    };


    @Override
    protected int getLayoutResId() {
        return R.layout.act_order_sign_in;
    }

    @Override
    public void initView() {
        super.initView();
        mOrderItem = (OrderItem) getIntent().getSerializableExtra("order");
        page = getIntent().getIntExtra("flag", 0);
        if (page == 0) {
            titleBar.setTitle("上门签到");
            ok.setText("立即签到");
        } else {
            titleBar.setTitle("确认服务完成");
            ok.setText("完成服务");
        }
        initLocation();
    }

    @OnClick({R.id.rl_iv, R.id.ok})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ok:
                confirmArrival();
//                if (page == 0) {
//                    if (flag) {
//                        confirmArrival();
//                    } else {
//                        toast("在签到范围外，不能签到!");
//                    }
//                } else {
//                    confirmArrival();
//
//                }
                break;
            case R.id.rl_iv:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(false)// 是否裁剪 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(11);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.size() > 0) {
                        for (LocalMedia localMedia : selectList) {
                            path = Base64Util.getBase64ImageStr(localMedia.getCompressPath());
                            Glide.with(this).load(localMedia.getCompressPath()).into(iv);
                        }
                    }
                    break;
            }
        }
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

    private void confirmLocation() {
        mSearch.geocode(new GeoCodeOption()
                .city(mOrderItem.getCity())
                .address(mOrderItem.getAddress()));
    }

    public void getDistance(double lat1, double lon1,
                            double lat2, double lon2) {
        float[] results = new float[1];
        try {
            Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (results[0] > 2000) {
            distance.setText("(在签到范围外，不能签到)");
            flag = false;
        } else {
            flag = true;
            distance.setText("(在签到范围内)");
        }
    }

    private void confirmArrival() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", mOrderItem.getOrderId());
        map.put("locationLat", mLocation.getLatitude());
        map.put("locationLng", mLocation.getLongitude());
        map.put("shot", path);
        map.put("remark", etRemark.getText().toString());
        showLoading();
        OkGo.<ResponseData<String>>post(UrlConstant.ORDER_ARRIVAL)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(ResponseData<String> response) {
                        dismissLoading();
                        if (response!=null){
                            if (response.isSuccess()){
                                finish();
                                EventBusUtil.post(MessageConstant.NOTIFY_ORDER_DETAIL);
                            }else{
                                toast(response.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<String>> response) {
                        super.onError(response);
                        dismissLoading();
                    }
                });
    }

    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocation = location;
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置自定义图标
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromView(View.inflate(OrderSignInActivity.this, R.layout.view_location_icon_my, null));
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);

            /**
             * 绘制圆形
             */
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());//坐标参数（纬度，经度）
            OverlayOptions oCircle = new CircleOptions().fillColor(0x90F8EFDE)
                    .center(point).stroke(new Stroke(5, 0xAAF0D2C2))
                    .radius(2000);
            mBaiduMap.addOverlay(oCircle);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(12.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
                tv_location.setText(location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet() + location.getLocationDescribe());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
                tv_location.setText(location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet() + location.getLocationDescribe());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
                tv_location.setText(location.getProvince() + location.getCity() + location.getDistrict() + location.getStreet() + location.getLocationDescribe());
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(OrderSignInActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(OrderSignInActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(OrderSignInActivity.this, "请确认手机是否开启GPS", Toast.LENGTH_SHORT).show();
            }
            getDistance(mLocation.getLatitude(), mLocation.getLongitude(), mOrderItem.getLocationLat(), mOrderItem.getLocationLng());
            confirmLocation();
            mLocationClient.stop();
        }
    }
}
