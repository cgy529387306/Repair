package com.yxw.cn.carpenterrepair.activity.order;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.RemarkAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderDetailAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderPicAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderStatusAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OperateResult;
import com.yxw.cn.carpenterrepair.entity.OrderDetail;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.listerner.OnChooseDateListener;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.pop.ConfirmOrderPop;
import com.yxw.cn.carpenterrepair.pop.ContactPop;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.MapUtil;
import com.yxw.cn.carpenterrepair.util.TimePickerUtil;
import com.yxw.cn.carpenterrepair.util.TimeUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements ContactPop.SelectListener ,ConfirmOrderPop.SelectListener{

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_rest_time)
    TextView tvRestTime;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_booking_time)
    TextView tvBookingTime;
    @BindView(R.id.tv_time_type)
    TextView tvTimeType;

    @BindView(R.id.tv_fixDesc)
    TextView tvFixDesc;
    @BindView(R.id.tv_faultDesc)
    TextView tvFaultDesc;
    @BindView(R.id.tv_solution)
    TextView tvSolution;
    @BindView(R.id.tv_remark)
    TextView tvRemark;

    @BindView(R.id.tv_operate0)
    TextView tvOperate0;
    @BindView(R.id.tv_operate1)
    TextView tvOperate1;
    @BindView(R.id.tv_operate2)
    TextView tvOperate2;
    @BindView(R.id.rv_order_detail)
    RecyclerView orderRv;
    @BindView(R.id.rv_order_status)
    RecyclerView statusRv;
    @BindView(R.id.rv_remark)
    RecyclerView remarkRv;
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    private OrderDetail orderDetail;
    private OrderItem orderItem;
    private String orderId;
    private String orderStatus;
    private String orderAddress;
    private String city;
    private UserOrderDetailAdapter orderAdapter;
    private UserOrderStatusAdapter statusAdapter;
    private RemarkAdapter remarkAdapter;
    private boolean mStop;
    private int connectFlag = 0;
    private TitleBar.TextAction textAction;

    private BaiduMap mBaiDuMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private ContactPop mContactPop;
    private ConfirmOrderPop mConfirmOrderPop;
    private Disposable mDisposable;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_order_detail;
    }

    @Override
    public void initView() {
        titleBar.setTitle("订单详情");
        orderItem = (OrderItem) getIntent().getSerializableExtra("data");
        if (orderItem!=null){
            orderId = orderItem.getOrderId();
        }else{
            orderId = getIntent().getStringExtra("orderId");
        }
        if (Helper.isEmpty(orderId)){
            toast("订单不存在");
            finish();
        }

        orderRv.setLayoutManager(new LinearLayoutManager(this));
        orderRv.setNestedScrollingEnabled(false);
        orderAdapter = new UserOrderDetailAdapter(new ArrayList<>());
        orderRv.setAdapter(orderAdapter);

        remarkRv.setLayoutManager(new LinearLayoutManager(this));
        remarkRv.setNestedScrollingEnabled(false);
        remarkAdapter = new RemarkAdapter(new ArrayList<>());
        remarkRv.setAdapter(remarkAdapter);

        statusRv.setLayoutManager(new LinearLayoutManager(this));
        statusRv.setNestedScrollingEnabled(false);
        statusAdapter = new UserOrderStatusAdapter(new ArrayList<>());
        statusRv.setAdapter(statusAdapter);

        initMyLocation();
        initOrderData();
    }

    private void initOrderData(){
        if (orderItem!=null){
            int orderStatus = orderItem.getOrderStatus();
            double price = orderStatus<=30?orderItem.getTotalPrice():orderItem.getFee();
            tvName.setText(orderItem.getName());
            tvTel.setText(orderItem.getMobile());
            tvAddress.setText(orderItem.getAddress());
            tvTotalPrice.setText(String.valueOf(price));
            tvOrderNo.setText(String.format("订单编号：%s", orderItem.getOrderSn()));
            tvCreateTime.setText(String.format("下单时间：%s", orderItem.getCreateTime()));
            tvTitle.setText(orderItem.getCategoryPName());
            tvTitle2.setText(orderItem.getCategoryCName());
            tvBookingTime.setText(orderItem.getBookingStartTime());
            tvFaultDesc.setText(orderItem.getFaultDesc());
            tvFixDesc.setText(orderItem.getFixDesc());
            tvSolution.setText(orderItem.getSolution());
            tvRemark.setText(orderItem.getRemark());
            tvTimeType.setText(orderStatus<=40?"服务时间":"上门时间");
            initOrderStatus();
            initOrderLocation();
        }
    }

    private void initDetail(){
        if (orderDetail!=null){
            tvTitle.setText(orderDetail.getCategoryName());
            tvTitle2.setText(orderDetail.getCategoryNameJoint());
            statusAdapter.setNewData(orderDetail.getFixOrderTimelineViewRespIOList());
            remarkAdapter.setNewData(orderDetail.getRemarkList());
        }
    }

    private void initOrderLocation(){
        LatLng point = new LatLng(orderItem.getLocationLat(), orderItem.getLocationLng());//坐标参数（纬度，经度）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromView(View.inflate(OrderDetailActivity.this, R.layout.view_location_icon, null));
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(mCurrentMarker);
        mBaiDuMap.addOverlay(option);
    }

    @Override
    public void getData() {
        String url = AppUtil.getDetailUrl(orderItem)+ AppUtil.getDetailId(orderItem);
        OkGo.<ResponseData<OrderDetail>>post(url)
                .execute(new JsonCallback<ResponseData<OrderDetail>>() {
                    @Override
                    public void onSuccess(ResponseData<OrderDetail> response) {
                        if (response!=null){
                            if (response.isSuccess()){
                                orderItem = response.getData();
                                orderDetail = response.getData();
                                initOrderData();
                                initDetail();
                            }else{
                                toast(response.getMsg());
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.bt_copy,R.id.tv_nav, R.id.tv_call})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_call:
                if (orderItem!=null){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + orderItem.getMobile());
                    intent.setData(data);
                    startActivity(intent);
                }
                break;
            case R.id.tv_nav:
                if (orderItem !=null ){
                    MapUtil.navWithMap(OrderDetailActivity.this,orderItem.getLocationLat(),orderItem.getLocationLng(),orderItem.getAddress());
                }
                break;
            case R.id.bt_copy:
                ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("copy from demo", tvOrderNo.getText().toString());
                mClipboardManager.setPrimaryClip(clipData);
                toast("复制成功！");
                break;
        }
    }


    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_ORDER:
                if (mDisposable!=null){
                    mDisposable.dispose();
                }
                getData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(mLocationListener);
        mLocationClient.stop();
        mBaiDuMap.setMyLocationEnabled(false);
        mBaiDuMap.clear();
        mMapView.onDestroy();
        mMapView = null;
        if (mDisposable!=null){
            mDisposable.dispose();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 停止定位
        if (mBaiDuMap!=null && mLocationClient!=null){
            mStop = true;
            mBaiDuMap.setMyLocationEnabled(false);
            mLocationClient.stop();
        }
    }

    private void initMyLocation() {
        mBaiDuMap = mMapView.getMap();
        mBaiDuMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
        // 开启定位
        mBaiDuMap.setMyLocationEnabled(true);
        mLocationClient.start();
    }

    @Override
    public void onCall(OrderItem orderItem) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + orderItem.getMobile());
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    public void onTime(OrderItem orderItem) {
        TimePickerUtil.showYearPicker(OrderDetailActivity.this, new OnChooseDateListener() {
            @Override
            public void getDate(Date date) {
                String startTime = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
                String endTime = TimeUtil.getAfterHourTime(date);
                showLoading();
                HashMap<String, Object> map = new HashMap<>();
                map.put("acceptId", orderItem.getAcceptId());
                map.put("bookingStartTime", startTime);
                map.put("bookingEndTime", endTime);
                OkGo.<ResponseData<Object>>post(UrlConstant.ORDER_RESERVATION)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<Object>>() {
                            @Override
                            public void onSuccess(ResponseData<Object> response) {
                                dismissLoading();
                                if (response!=null){
                                    if (response.isSuccess()) {
                                        toast("预约成功");
                                        EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
                                    }else{
                                        toast(response.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onError(Response<ResponseData<Object>> response) {
                                super.onError(response);
                                dismissLoading();
                            }
                        });

            }
        });
    }

    @Override
    public void onConfirm(OrderItem orderItem) {
        TimePickerUtil.showYearPicker(OrderDetailActivity.this, new OnChooseDateListener() {
            @Override
            public void getDate(Date date) {
                String startTime = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
                String endTime = TimeUtil.getAfterHourTime(date);
                showLoading();
                HashMap<String, Object> map = new HashMap<>();
                map.put("acceptId", orderItem.getAcceptId());
                map.put("bookingStartTime", startTime);
                map.put("bookingEndTime", endTime);
                OkGo.<ResponseData<Object>>post(UrlConstant.ORDER_RESERVATION)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<Object>>() {
                            @Override
                            public void onSuccess(ResponseData<Object> response) {
                                dismissLoading();
                                if (response!=null){
                                    if (response.isSuccess()) {
                                        toast("预约成功");
                                        EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
                                    }else{
                                        toast(response.getMsg());
                                    }
                                }
                            }

                            @Override
                            public void onError(Response<ResponseData<Object>> response) {
                                super.onError(response);
                                dismissLoading();
                            }
                        });

            }
        });
    }


    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location!=null && location.getLatitude() != 4.9E-324 && location.getLongitude() != 4.9E-324){
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(0).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                // 设置定位数据
                mBaiDuMap.setMyLocationData(locData);

                BitmapDescriptor currentMarker = BitmapDescriptorFactory
                        .fromView(View.inflate(OrderDetailActivity.this, R.layout.view_location_icon_my, null));
                MyLocationConfiguration config = new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, true, currentMarker);
                mBaiDuMap.setMyLocationConfiguration(config);

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(14.0f);
                mBaiDuMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(OrderDetailActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(OrderDetailActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(OrderDetailActivity.this, "请确认手机是否开启GPS", Toast.LENGTH_SHORT).show();
                }
                mLocationClient.stop();
            }
        }
    }

    private void initOrderStatus(){
        int orderStatus = orderItem.getOrderStatus();
        if (orderStatus==20 || orderStatus==25 || orderStatus==27){
            //待接单
            tvRestTime.setVisibility(View.GONE);
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.GONE);
            tvOperate2.setVisibility(View.VISIBLE);
            if (orderStatus == 27){
                tvOperate2.setText("确认接单");
                tvOperate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(OrderDetailActivity.this).title("确认接单").content("是否接取该订单?")
                                .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                confirmReceiveOrder(orderItem,1);
                            }
                        }).negativeText("否").onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                confirmReceiveOrder(orderItem,0);
                            }
                        }).show();
                    }
                });
            }else{
                tvOperate2.setText("我要接单");
                tvOperate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOrderTakingDialog(orderItem);
                    }
                });
            }
        }else if (orderStatus<=40){
            //待预约
            tvRestTime.setVisibility(View.VISIBLE);
            if (Helper.isEmpty(orderItem.getReceiveTime())){
                tvRestTime.setText("预约时间异常");
            }else{
                if (TimeUtil.compareTime2(orderItem.getReceiveTime())){
                    mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                            .takeWhile(new Predicate<Long>() {
                                @Override
                                public boolean test(Long aLong) throws Exception {
                                    return !mStop;
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    if (TimeUtil.reFreshTime2(orderItem.getReceiveTime()) == null) {
                                        tvRestTime.setText("预约倒计时已结束");
                                    } else {
                                        tvRestTime.setText(String.format("预约倒计时：%s", TimeUtil.reFreshTime2(orderItem.getReceiveTime())));
                                    }
                                }
                            });
                }else{
                    tvRestTime.setText("预约时间已过期");
                }
            }
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.VISIBLE);
            tvOperate1.setText("取消订单");
            tvOperate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("acceptId",orderItem.getAcceptId());
                    startActivity(CancelOrderActivity.class,bundle);
                    finish();
                }
            });

            tvOperate2.setVisibility(View.VISIBLE);
            tvOperate2.setText("联系用户");
            tvOperate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContactPop==null){
                        mContactPop = new ContactPop(OrderDetailActivity.this,OrderDetailActivity.this,orderItem);
                    }
                    mContactPop.showPopupWindow(mMapView);
                }
            });
        }else if (orderStatus<=55){
            //待上门
            tvRestTime.setVisibility(View.VISIBLE);
            if (Helper.isEmpty(orderItem.getBookingStartTime())){
                tvRestTime.setText("上门时间异常");
            }else{
                if (TimeUtil.compareTime(orderItem.getBookingStartTime())){
                    mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                            .takeWhile(new Predicate<Long>() {
                                @Override
                                public boolean test(Long aLong) throws Exception {
                                    return !mStop;
                                }
                            })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    if (TimeUtil.reFreshTime(orderItem.getBookingStartTime()) == null) {
                                        tvRestTime.setText("上门倒计时已结束");
                                    } else {
                                        tvRestTime.setText(String.format("上门倒计时：%s", TimeUtil.reFreshTime(orderItem.getBookingStartTime())));
                                    }
                                }
                            });
                }else{
                    tvRestTime.setText("服务时间已过期");
                }
            }
            tvOperate0.setVisibility(View.VISIBLE);
            tvOperate0.setText("改约");
            tvOperate0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",0);
                    bundle.putString("acceptId",orderItem.getAcceptId());
                    startActivity(AppointAbnormalActivity.class,bundle);
                }
            });
            tvOperate1.setVisibility(View.VISIBLE);
            tvOperate1.setText("异常反馈");
            tvOperate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",1);
                    bundle.putString("acceptId",orderItem.getAcceptId());
                    startActivity(SignAbnormalActivity.class,bundle);
                }
            });

            tvOperate2.setVisibility(View.VISIBLE);
            tvOperate2.setText("签到");
            tvOperate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order",orderItem);
                    bundle.putInt("type",0);
                    startActivity(OrderSignInActivity.class,bundle);
                }
            });
        }else if (orderStatus<90){
            //待完成
            tvRestTime.setVisibility(View.GONE);
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.GONE);
            tvOperate2.setVisibility(View.VISIBLE);
            tvOperate2.setText("服务完成");
            tvOperate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order",orderItem);
                    bundle.putInt("type",1);
                    startActivity(OrderSignInActivity.class,bundle);
                }
            });
        }else{
            //已完成
            tvRestTime.setVisibility(View.GONE);
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.GONE);
            tvOperate2.setVisibility(View.GONE);
        }
    }


    public void showOrderTakingDialog(OrderItem orderItem) {
        if (mConfirmOrderPop==null){
            mConfirmOrderPop = new ConfirmOrderPop(OrderDetailActivity.this,orderItem,this);
        }
        mConfirmOrderPop.showPopupWindow(mMapView);
    }

    @Override
    public void onOrderComfirm(OrderItem orderItem) {
        showLoading();
        OkGo.<ResponseData<OperateResult>>post(UrlConstant.ORDER_RECEIVE+orderItem.getOrderId())
                .execute(new JsonCallback<ResponseData<OperateResult>>() {
                             @Override
                             public void onSuccess(ResponseData<OperateResult> response) {
                                 dismissLoading();
                                 if (response!=null){
                                     if (response.isSuccess() && response.getData()!=null) {
                                         toast("抢单成功");
                                         orderItem.setOrderStatus(response.getData().getOrderStatus());
                                         orderItem.setAcceptId(response.getData().getAcceptId());
                                         EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
                                     }else{
                                         toast(response.getMsg());
                                     }
                                 }
                             }

                             @Override
                             public void onError(Response<ResponseData<OperateResult>> response) {
                                 super.onError(response);
                                 dismissLoading();
                             }
                         }
                );
    }

    private void confirmReceiveOrder(OrderItem orderItem,int type){
        showLoading();
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderItem.getOrderId());
        map.put("type", type);
        OkGo.<ResponseData<Object>>post(UrlConstant.ORDER_CONFIRM_RECEIVE)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<Object>>() {
                    @Override
                    public void onSuccess(ResponseData<Object> response) {
                        dismissLoading();
                        if (response!=null){
                            if (response.isSuccess()) {
                                toast("操作成功");
                                EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
                            }else{
                                toast(response.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<Object>> response) {
                        super.onError(response);
                        dismissLoading();
                    }
                });
    }
}
