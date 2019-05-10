package com.yxw.cn.carpenterrepair.activity.order;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
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
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.UserOrderDetailAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderPicAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderStatusAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OrderDetail;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.listerner.OnChooseDateListener;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.pop.ContactPop;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.TimePickerUtil;
import com.yxw.cn.carpenterrepair.util.TimeUtil;
import com.yxw.cn.carpenterrepair.util.ToastUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements ContactPop.SelectListener {

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
    @BindView(R.id.tv_desc)
    TextView tvDesc;
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
    @BindView(R.id.rv_pic)
    RecyclerView picRv;
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
    private List<UserOrder.ListBean.OrderItemsBean> orderList;
    private UserOrderDetailAdapter orderAdapter;
    private List<UserOrder.ListBean.PicListBean> picList;
    private UserOrderPicAdapter picAdapter;
    private UserOrderStatusAdapter statusAdapter;
    private boolean mStop;
    private int connectFlag = 0;
    private TitleBar.TextAction textAction;

    private BaiduMap mBaiDuMap;
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private ContactPop mContactPop;
    private DialogPlus mTakingDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_order_detail;
    }

    @Override
    public void initView() {
        titleBar.setTitle("订单详情");
        orderItem = (OrderItem) getIntent().getSerializableExtra("data");
        orderId = orderItem.getOrderId();
        orderList = new ArrayList<>();
        orderAdapter = new UserOrderDetailAdapter(orderList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        orderRv.setLayoutManager(layoutManager);
        orderRv.setAdapter(orderAdapter);

        picList = new ArrayList<>();
        picAdapter = new UserOrderPicAdapter(picList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        picRv.setLayoutManager(gridLayoutManager);
        picRv.setAdapter(picAdapter);

        statusRv.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });
        statusRv.setNestedScrollingEnabled(false);
        statusAdapter = new UserOrderStatusAdapter(new ArrayList<>());
        statusRv.setAdapter(statusAdapter);

        initMyLocation();
        initOrderData();
    }

    private void initOrderData(){
        if (orderItem!=null){
            tvName.setText(orderItem.getName());
            tvTel.setText(orderItem.getMobile());
            tvAddress.setText(orderItem.getAddress());
            tvTotalPrice.setText(String.valueOf(orderItem.getTotalPrice()));
            tvOrderNo.setText(orderItem.getOrderSn());
            tvTitle.setText(orderItem.getCategoryPName());
            tvTitle2.setText(orderItem.getCategoryCName());
            tvBookingTime.setText(orderItem.getBookingStartTime());
            tvDesc.setText(orderItem.getFixDesc());
            initOrderStatus();
            initOrderLocation();
        }
    }

    private void initDetail(){
        if (orderDetail!=null){
            tvTitle.setText(orderDetail.getCategoryName());
            tvTitle2.setText(orderDetail.getCategoryNameJoint());
            statusAdapter.setNewData(orderDetail.getFixOrderTimelineViewRespIOList());
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
        OkGo.<ResponseData<OrderDetail>>post(UrlConstant.ORDER_DETAIL+orderId)
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

    @OnClick({R.id.bt_copy, R.id.tv_call})
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


    private void reservationTime(String bookingDate, String bookingTime) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("bookingDate", bookingDate);
        map.put("bookingTime", bookingTime);
        OkGo.<ResponseData<String>>post(UrlConstant.RESERVATION_TIME)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(ResponseData<String> response) {
                        ToastUtil.show(response.getMsg());
                        if (response.isSuccess()) {
                            getData();
                        }
                    }
                });
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
                map.put("orderId", orderItem.getOrderId());
                map.put("bookingStartTime", startTime);
                map.put("bookingEndTime", endTime);
                OkGo.<ResponseData<String>>post(UrlConstant.ORDER_RESERVATION)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<String>>() {
                            @Override
                            public void onSuccess(ResponseData<String> response) {
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
                            public void onError(Response<ResponseData<String>> response) {
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
                map.put("orderId", orderItem.getOrderId());
                map.put("bookingStartTime", startTime);
                map.put("bookingEndTime", endTime);
                OkGo.<ResponseData<String>>post(UrlConstant.ORDER_RESERVATION)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<String>>() {
                            @Override
                            public void onSuccess(ResponseData<String> response) {
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
                            public void onError(Response<ResponseData<String>> response) {
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

    private void initOrderStatus(){
        int orderStatus = orderItem.getOrderStatus();
        if (orderStatus<=20){
            //待接单
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.GONE);
            tvOperate2.setVisibility(View.VISIBLE);
            tvOperate2.setText("我要接单");
            tvOperate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrderTakingDialog(orderItem);
                }
            });
        }else if (orderStatus<=40){
            //待预约
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.VISIBLE);
            tvOperate1.setText("异常反馈");
            tvOperate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",0);
                    bundle.putString("orderId",orderItem.getOrderId());
                    startActivity(AppointAbnormalActivity.class,bundle);
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
            tvOperate0.setVisibility(View.VISIBLE);
            tvOperate0.setText("改约");
            tvOperate0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerUtil.showYearPicker(OrderDetailActivity.this, new OnChooseDateListener() {
                        @Override
                        public void getDate(Date date) {
                            String startTime = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
                            String endTime = TimeUtil.getAfterHourTime(date);
                            showLoading();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("orderId", orderItem.getOrderId());
                            map.put("bookingStartTime", startTime);
                            map.put("bookingEndTime", endTime);
                            OkGo.<ResponseData<String>>post(UrlConstant.ORDER_TURN_RESERVATION)
                                    .upJson(gson.toJson(map))
                                    .execute(new JsonCallback<ResponseData<String>>() {
                                        @Override
                                        public void onSuccess(ResponseData<String> response) {
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
                                        public void onError(Response<ResponseData<String>> response) {
                                            super.onError(response);
                                            dismissLoading();
                                        }
                                    });

                        }
                    });
                }
            });

            tvOperate1.setVisibility(View.VISIBLE);
            tvOperate1.setText("异常反馈");
            tvOperate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",1);
                    bundle.putString("orderId",orderItem.getOrderId());
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
            tvOperate0.setVisibility(View.GONE);
            tvOperate1.setVisibility(View.GONE);
            tvOperate2.setVisibility(View.GONE);
        }
    }

    public void showOrderTakingDialog(OrderItem orderItem) {
        if (mTakingDialog == null) {
            mTakingDialog = DialogPlus.newDialog(this)
                    .setContentHolder(new ViewHolder(R.layout.dlg_confirm_order))
                    .setGravity(Gravity.CENTER)
                    .setCancelable(true)
                    .create();
            View dialogView = mTakingDialog.getHolderView();
            dialogView.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTakingDialog.dismiss();
                }
            });
            dialogView.findViewById(R.id.dialog_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTakingDialog.dismiss();
                    showLoading();
                    OkGo.<ResponseData<String>>post(UrlConstant.ORDER_RECEIVE+orderItem.getOrderId())
                            .execute(new JsonCallback<ResponseData<String>>() {
                                         @Override
                                         public void onSuccess(ResponseData<String> response) {
                                             dismissLoading();
                                             if (response!=null){
                                                 if (response.isSuccess()) {
                                                     toast("抢单成功");
                                                     EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
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
                                     }
                            );
                }
            });
        }
        mTakingDialog.show();
    }


}
