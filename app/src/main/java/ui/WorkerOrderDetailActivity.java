package ui;

import android.annotation.SuppressLint;
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
import android.widget.Button;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.UserOrderDetailAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderPicAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderStatusAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.CountDownTextView;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import listerner.OnChooseDateListener;
import util.DoubleUtil;
import util.EventBusUtil;
import util.TimePickerUtil;
import util.TimeUtil;
import util.ToastUtil;

/**
 * Created by CY on 2019/1/3
 */
public class WorkerOrderDetailActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_time2)
    TextView tv_time2;
    @BindView(R.id.tv_time3)
    TextView tv_time3;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_addr)
    TextView tvAddr;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_booking_time)
    TextView tvBookingTime;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.confirm)
    Button btConfirm;
    @BindView(R.id.cancel)
    Button btCancel;
    @BindView(R.id.rv_order_detail)
    RecyclerView orderRv;
    @BindView(R.id.rv_order_status)
    RecyclerView statusRv;
    @BindView(R.id.rv_pic)
    RecyclerView picRv;
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.bt_code)
    CountDownTextView mCountDownTextView;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t2)
    TextView t2;
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
                        .fromView(View.inflate(WorkerOrderDetailActivity.this, R.layout.view_location_icon, null));
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
//                    map.put("addr1", areaTv.getText().toString().replace("-", ""));
//                    map.put("addr2", et_detail.getText().toString());
                map.put("result", result);
                finish();
            }
            //获取反向地理编码结果
        }
    };
    private int orderId;
    private String orderStatus;
    private String orderAddr;
    private String city;
    private List<UserOrder.ListBean.OrderItemsBean> orderList;
    private UserOrderDetailAdapter orderAdapter;
    private List<UserOrder.ListBean.PicListBean> picList;
    private UserOrderPicAdapter picAdapter;
    private List<UserOrder.ListBean.TimelineListBean> statusList;
    private UserOrderStatusAdapter statusAdapter;
    private UserOrder.ListBean listBean;
    private boolean mStop;
    private int connectFlag = 0;
    private TitleBar.TextAction textAction;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_worker_order_detail;
    }

    @Override
    public void initView() {
        titleBar.setTitle("订单详情");
        orderId = getIntent().getIntExtra("data", 0);
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


    }

    @Override
    public void getData() {
        Gson gson = new Gson();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("orderId", orderId);
        OkGo.<ResponseData<UserOrder.ListBean>>post(UrlConstant.ORDER_VIEW)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<UserOrder.ListBean>>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onSuccess(ResponseData<UserOrder.ListBean> response) {
                        listBean = response.getData();
                        connectFlag = listBean.getContactClient();
                        orderStatus = AppUtil.getUserOrderStatus(response.getData());
                        if(response.getData().getCity()!=null){
                            city = response.getData().getCity();
                        }
                        if(response.getData().getAddress()!=null){
                            orderAddr = response.getData().getAddress();
                        }
                        tvName.setText(response.getData().getName()==null?"":response.getData().getName());
                        tvTel.setText(response.getData().getMobile()==null?"":response.getData().getMobile());
                        tvAddr.setText(response.getData().getProvince()==null?"":response.getData().getProvince()
                                + response.getData().getCity()==null?"":response.getData().getCity()
                                + response.getData().getDistrict()==null?"":response.getData().getDistrict()
                                + response.getData().getAddress()==null?"":response.getData().getAddress());
                        tvTitle.setText(response.getData().getCategoryName()==null?"":response.getData().getCategoryName());
                        tvTitle2.setText(response.getData().getCategoryName()==null?"":response.getData().getCategoryName());
                        tvTotal.setText("￥" + DoubleUtil.getTwoDecimal(response.getData().getTotalPrice()));
                        tvOrderNo.setText(response.getData().getOrderSn()==null?"":response.getData().getOrderSn());
                        tvCreateTime.setText(response.getData().getCreateTime()==null?"":response.getData().getCreateTime());
                        tvBookingTime.setText(response.getData().getBookingDate() + " " + response.getData().getBookingTime());
                        tvRemark.setText(response.getData().getRemark()==null?"":response.getData().getRemark());

                        orderList.clear();
                        orderList.addAll(response.getData().getOrderItems());
                        orderAdapter.notifyDataSetChanged();
                        picList.clear();
                        picList.addAll(response.getData().getPicList());
                        picAdapter.notifyDataSetChanged();

                        statusList = new ArrayList<>();
                        statusList.addAll(response.getData().getTimelineList());
                        statusAdapter = new UserOrderStatusAdapter(statusList);
                        LinearLayoutManager layoutManager1 = new LinearLayoutManager(WorkerOrderDetailActivity.this) {
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        };
                        statusRv.setLayoutManager(layoutManager1);
                        statusRv.setAdapter(statusAdapter);
                        if (orderStatus.equals("未接单")) {
                            dissmissCancel();
                            connectFlag = -1;
                            tv_time.setVisibility(View.VISIBLE);
                            tv_time2.setVisibility(View.GONE);
                            tv_time3.setVisibility(View.GONE);
                            if (TimeUtil.compareTime(response.getData().getBookingDate() + " " + response.getData().getBookingTime())) {
                                Observable.interval(0, 1, TimeUnit.SECONDS)
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
                                                tv_time.setText("接单倒计时：" + TimeUtil.reFreshTime(response.getData().getBookingDate() + " " + response.getData().getBookingTime()));
                                            }
                                        });
                                btConfirm.setText("我要接单");
                                btCancel.setVisibility(View.GONE);
                            } else {
                                btConfirm.setVisibility(View.GONE);
                                btCancel.setVisibility(View.GONE);
                                tv_time.setText("订单时间已过期");
                            }
                        } else if (orderStatus.equals("待预约")) {
                            cancelOrder();
                            tv_time2.setVisibility(View.VISIBLE);
                            tv_time.setVisibility(View.GONE);
                            tv_time3.setVisibility(View.GONE);
                            if(response.getData().getReceiveTime()==null){
                                btConfirm.setVisibility(View.GONE);
                                btCancel.setVisibility(View.VISIBLE);
                                tv_time2.setText("预约时间异常");
                            }else {
                                if (TimeUtil.compareTime2(response.getData().getReceiveTime())) {
                                    Observable.interval(0, 1, TimeUnit.SECONDS)
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
                                                    tv_time2.setText("预约倒计时：" + TimeUtil.reFreshTime2(response.getData().getReceiveTime()));
                                                }
                                            });
                                    btConfirm.setText("预约时间");
                                    btConfirm.setVisibility(View.VISIBLE);
                                    btCancel.setVisibility(View.VISIBLE);
                                } else {
                                    btConfirm.setVisibility(View.GONE);
                                    btCancel.setVisibility(View.VISIBLE);
                                    tv_time2.setText("预约时间已过期");
                                }
                            }
                        } else if (orderStatus.equals("待服务")) {
                            cancelOrder();
                            tv_time3.setVisibility(View.VISIBLE);
                            tv_time.setVisibility(View.GONE);
                            tv_time2.setVisibility(View.GONE);
                            if (TimeUtil.compareTime(response.getData().getBookingDate() + " " + response.getData().getBookingTime())) {
                                Observable.interval(0, 1, TimeUnit.SECONDS)
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
                                                tv_time3.setText("上门倒计时：" + TimeUtil.reFreshTime(response.getData().getBookingDate() + " " + response.getData().getBookingTime()));
                                            }
                                        });
                                btConfirm.setText("上门服务");
                                btConfirm.setVisibility(View.VISIBLE);
                                btCancel.setVisibility(View.VISIBLE);
                            } else {
                                btConfirm.setVisibility(View.GONE);
                                btCancel.setVisibility(View.VISIBLE);
                                tv_time3.setText("服务时间已过期");
                            }
                        } else if (orderStatus.equals("进行中")) {
                            dissmissCancel();
                            tv_time3.setVisibility(View.GONE);
                            tv_time.setVisibility(View.VISIBLE);
                            tv_time.setText("服务中");
                            tv_time2.setVisibility(View.GONE);
                            btConfirm.setText("完成服务");
                            btCancel.setVisibility(View.VISIBLE);
                        } else {
                            dissmissCancel();
                            tv_time3.setVisibility(View.GONE);
                            tv_time.setVisibility(View.VISIBLE);
                            tv_time.setText("已完成");
                            tv_time2.setVisibility(View.GONE);
                            btConfirm.setVisibility(View.GONE);
                            btCancel.setVisibility(View.GONE);
                        }
                        showTel();
                        initLocation();
//                        else if (orderStatus.equals("待服务")) {
//                            if (payStatus == 2) {
////                                btConfirm.setText("开始服务");
////                                btCancel.setVisibility(View.GONE);
//                            } else {
////                                btConfirm.setText("维修人员到场验证");
////                                btCancel.setVisibility(View.VISIBLE);
//                            }
//                        } else if (orderStatus.getText().toString().equals("进行中")) {
////                            btConfirm.setText("确认完成订单");
////                            btCancel.setVisibility(View.GONE);
//                        } else {
////                            btConfirm.setVisibility(View.GONE);
////                            btCancel.setVisibility(View.GONE);
//                        }
                    }
                });
    }

    @OnClick({R.id.confirm, R.id.cancel, R.id.bt_copy, R.id.rl_call})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_call:
                connectFlag = -1;
                HashMap<String, Object> map = new HashMap<>();
                map.put("orderId", orderId);
                OkGo.<ResponseData<String>>post(UrlConstant.CONTACT_CLIENT)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<String>>() {

                            @Override
                            public void onSuccess(ResponseData<String> response) {
                                if (response.getCode() == 0) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + tvTel.getText().toString());
                                    intent.setData(data);
                                    startActivity(intent);
                                }
                            }
                        });
                break;
            case R.id.bt_copy:
                ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("copy from demo", tvOrderNo.getText().toString());
                mClipboardManager.setPrimaryClip(clipData);
                toast("复制成功！");
                break;
            case R.id.cancel:
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", orderId);
                startActivity(WorkerAbnormalActivity.class, bundle);
                /*DialogPlus dialogPlus = DialogPlus.newDialog(this)
                        .setContentHolder(new ViewHolder(R.layout.dlg_worker_cancel_order))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(true)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.dialog_confirm:
                                        dialog.dismiss();
                                        HashMap<String, Integer> map = new HashMap<>();
                                        map.put("orderId", orderId);
                                        OkGo.<ResponseData<String>>post(UrlConstant.WORKER_CANCEL_ORDER)
                                                .upJson(gson.toJson(map))
                                                .execute(new JsonCallback<ResponseData<String>>() {
                                                    @Override
                                                    public void onSuccess(ResponseData<String> response) {
                                                        ToastUtil.show(response.getMsg());
                                                        if (response.getCode() == 0) {
                                                            EventBusUtil.post(MessageConstant.NOTIFY_ORDER);
                                                            WorkerOrderDetailActivity.this.finish();
                                                        }
                                                    }
                                                });
                                        break;
                                    case R.id.dialog_cancel:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        })
                        .create();
                dialogPlus.show();*/
                break;
            case R.id.confirm:
                if (orderStatus.equals("未接单")) {
                    DialogPlus dialogPlus1 = DialogPlus.newDialog(this)
                            .setContentHolder(new ViewHolder(R.layout.dlg_confirm_order))
                            .setGravity(Gravity.CENTER)
                            .setCancelable(true)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    switch (view.getId()) {
                                        case R.id.dialog_confirm:
                                            dialog.dismiss();
                                            HashMap<String, Integer> map1 = new HashMap<>();
                                            map1.put("orderId", orderId);
                                            OkGo.<ResponseData<String>>post(UrlConstant.RECEIVE_ORDER)
                                                    .upJson(gson.toJson(map1))
                                                    .execute(new JsonCallback<ResponseData<String>>() {
                                                        @Override
                                                        public void onSuccess(ResponseData<String> response) {
                                                            ToastUtil.show(response.getMsg());
                                                            if (response.getCode() == 0) {
                                                                getData();
                                                            }
                                                        }
                                                    });
                                            break;
                                        case R.id.dialog_cancel:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .create();
                    dialogPlus1.show();
                } else if (orderStatus.equals("待预约")) {
                    TimePickerUtil.showYearPicker(this, new OnChooseDateListener() {
                        @Override
                        public void getDate(Date date) {
                            String ss = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:ss");
                            reservationTime(ss.split(" ")[0], ss.split(" ")[1]);
                        }
                    });
                } else if (orderStatus.equals("待服务")) {
                    Intent intent2 = new Intent();
                    intent2.setClass(this, WorkerSignInActivity.class);
                    intent2.putExtra("data", listBean);
                    intent2.putExtra("flag", 0);
                    startActivity(intent2);
                } else if (orderStatus.equals("进行中")) {
                    Intent intent1 = new Intent();
                    intent1.setClass(this, WorkerSignInActivity.class);
                    intent1.putExtra("data", listBean);
                    intent1.putExtra("flag", 1);
                    startActivity(intent1);
                }

                /*if (tvStatus.getText().toString().equals("未接单")) {

                } else if (tvStatus.getText().toString().equals("待服务")) {
                    if (payStatus == 2) {
                       *//* DialogPlus dialogPlus = DialogPlus.newDialog(this)
                                .setContentHolder(new ViewHolder(R.layout.dlg_evalute_fee))
                                .setGravity(Gravity.CENTER)
                                .setContentBackgroundResource(R.drawable.corner_write)
                                .setCancelable(true)
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        switch (view.getId()) {
                                            case R.id.dialog_confirm:
                                                dialog.dismiss();
                                                EditText et = (EditText) dialog.findViewById(R.id.et_cash);
                                                Gson gson = new Gson();
                                                HashMap<String, Object> map = new HashMap<>();
                                                map.put("orderId", orderId);
                                                map.put("evaluationFees", Double.parseDouble(et.getText().toString()));
                                                OkGo.<ResponseData<String>>post(UrlConstant.CONFIRM_FEE)
                                                        .upJson(gson.toJson(map))
                                                        .execute(new JsonCallback<ResponseData<String>>() {
                                                            @Override
                                                            public void onSuccess(ResponseData<String> response) {
                                                                ToastUtil.show(response.getMsg());
                                                                if (response.getCode() == 0) {
                                                                    EventBusUtil.post(MessageConstant.NOTIFY_ORDER_DETAIL);
//                                                                sendCode();
                                                                }
                                                            }
                                                        });
                                                break;
                                        }
                                    }
                                })
                                .create();
                        dialogPlus.show();*//*
                    } else {
                        sendCode();
                    }
                } else if (tvStatus.getText().toString().equals("进行中")) {
                    sendEndCode();
                }*/
                break;
        }
    }

    private void sendCode() {
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        OkGo.<ResponseData<String>>post(UrlConstant.CONFIRM_ARRIVAL_SMS)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(ResponseData<String> response) {
                        ToastUtil.show(response.getMsg());
                        if (response.getCode() == 0) {
                            DialogPlus dialogPlus = DialogPlus.newDialog(WorkerOrderDetailActivity.this)
                                    .setContentHolder(new ViewHolder(R.layout.dlg_confirm_arrival_code))
                                    .setGravity(Gravity.CENTER)
                                    .setContentBackgroundResource(R.drawable.corner_write)
                                    .setCancelable(true)
                                    .setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(DialogPlus dialog, View view) {
                                            switch (view.getId()) {
                                                case R.id.dialog_confirm:
                                                    EditText et = (EditText) dialog.findViewById(R.id.et_cash);
                                                    Gson gson = new Gson();
                                                    HashMap<String, Object> map = new HashMap<>();
                                                    map.put("orderId", orderId);
                                                    map.put("smsCode", et.getText().toString());
                                                    OkGo.<ResponseData<String>>post(UrlConstant.START_SERVICE_SMS)
                                                            .upJson(gson.toJson(map))
                                                            .execute(new JsonCallback<ResponseData<String>>() {
                                                                @Override
                                                                public void onSuccess(ResponseData<String> response) {
                                                                    ToastUtil.show(response.getMsg());
                                                                    if (response.getCode() == 0) {
                                                                        dialog.dismiss();
                                                                        EventBusUtil.post(MessageConstant.NOTIFY_ORDER_DETAIL);
                                                                    }
                                                                }
                                                            });
                                                    break;
                                            }
                                        }
                                    })
                                    .create();
                            dialogPlus.show();
                        }
                    }
                });
    }

    private void sendEndCode() {
        Gson gson = new Gson();
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        OkGo.<ResponseData<String>>post(UrlConstant.END_SERVICE_CODE)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(ResponseData<String> response) {
                        ToastUtil.show(response.getMsg());
                        if (response.getCode() == 0) {
                            DialogPlus dialogPlus = DialogPlus.newDialog(WorkerOrderDetailActivity.this)
                                    .setContentHolder(new ViewHolder(R.layout.dlg_confirm_arrival_code))
                                    .setGravity(Gravity.CENTER)
                                    .setContentBackgroundResource(R.drawable.corner_write)
                                    .setCancelable(true)
                                    .setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(DialogPlus dialog, View view) {
                                            switch (view.getId()) {
                                                case R.id.dialog_confirm:
                                                    EditText et = (EditText) dialog.findViewById(R.id.et_cash);
                                                    Gson gson = new Gson();
                                                    HashMap<String, Object> map = new HashMap<>();
                                                    map.put("orderId", orderId);
                                                    map.put("smsCode", et.getText().toString());
                                                    OkGo.<ResponseData<String>>post(UrlConstant.END_SERVICE_BY_CODE)
                                                            .upJson(gson.toJson(map))
                                                            .execute(new JsonCallback<ResponseData<String>>() {
                                                                @Override
                                                                public void onSuccess(ResponseData<String> response) {
                                                                    ToastUtil.show(response.getMsg());
                                                                    if (response.getCode() == 0) {
                                                                        dialog.dismiss();
                                                                        EventBusUtil.post(MessageConstant.NOTIFY_ORDER_DETAIL);
                                                                    }
                                                                }
                                                            });
                                                    break;
                                            }
                                        }
                                    })
                                    .create();
                            dialogPlus.show();
                        }
                    }
                });
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_ORDER_DETAIL:
                getData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.post(MessageConstant.NOTIFY_ORDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStop = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStop = true;
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
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    private void confirmLocation() {
        mSearch.geocode(new GeoCodeOption()
                .city(city)
                .address(orderAddr));
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
                        if (response.getCode() == 0) {
                            getData();
                        }
                    }
                });
    }

    private void cancelOrder() {
        if(textAction==null){
            textAction=new TitleBar.TextAction("取消订单") {
                @Override
                public void performAction(View view) {
                    DialogPlus dialogPlus = DialogPlus.newDialog(WorkerOrderDetailActivity.this)
                            .setContentHolder(new ViewHolder(R.layout.dlg_worker_cancel_order))
                            .setGravity(Gravity.CENTER)
                            .setCancelable(true)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    switch (view.getId()) {
                                        case R.id.dialog_confirm:
                                            dialog.dismiss();
                                            HashMap<String, Integer> map = new HashMap<>();
                                            map.put("orderId", orderId);
                                            OkGo.<ResponseData<String>>post(UrlConstant.WORKER_CANCEL_ORDER)
                                                    .upJson(gson.toJson(map))
                                                    .execute(new JsonCallback<ResponseData<String>>() {
                                                        @Override
                                                        public void onSuccess(ResponseData<String> response) {
                                                            ToastUtil.show(response.getMsg());
                                                            if (response.getCode() == 0) {
                                                                EventBusUtil.post(MessageConstant.NOTIFY_ORDER);
                                                                WorkerOrderDetailActivity.this.finish();
                                                            }
                                                        }
                                                    });
                                            break;
                                        case R.id.dialog_cancel:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            })
                            .create();
                    dialogPlus.show();
                }
            };
            titleBar.addAction(textAction);
        }
    }

    private void dissmissCancel(){
        if(textAction!=null){
            titleBar.getViewByAction(textAction).setVisibility(View.GONE);
        }
    }

    private void showTel() {
        if (connectFlag == 0) {
            mCountDownTextView.setVisibility(View.VISIBLE);
            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            mCountDownTextView.setNormalText("建议在5分钟内联系客户")
                    .setCountDownText("", "")
                    .setCloseKeepCountDown(false)//关闭页面保持倒计时开关
                    .setCountDownClickable(false)//倒计时期间点击事件是否生效开关
                    .setShowFormatTime(true)//是否格式化时间
                    .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                        @Override
                        public void onFinish() {
                            mCountDownTextView.setVisibility(View.GONE);
                            t1.setVisibility(View.GONE);
                            t2.setVisibility(View.GONE);
//                        Toast.makeText(WorkerOrderDetailActivity.this, "倒计时完毕", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        mCountDownTextView.setClickable(false);


//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("orderId", listBean.getId());
//                        OkGo.<ResponseData<String>>post(UrlConstant.END_SERVICE_CODE)
//                                .upJson(gson.toJson(map))
//                                .execute(new JsonCallback<ResponseData<String>>() {
//                                    @Override
//                                    public void onSuccess(ResponseData<String> response) {
//                                        ToastUtil.show(response.getMsg());
//                                    }
//                                });
                        }
                    });
            mCountDownTextView.startCountDown(60 * 5);
        } else {
            mCountDownTextView.setVisibility(View.GONE);
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
        }
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
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                    .fromView(View.inflate(WorkerOrderDetailActivity.this, R.layout.view_location_icon_my, null));
            MyLocationConfiguration config = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);

            /**
             * 绘制圆形
             */
//            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());//坐标参数（纬度，经度）
//            OverlayOptions oCircle = new CircleOptions().fillColor(0x90F8EFDE)
//                    .center(point).stroke(new Stroke(5, 0xAAF0D2C2))
//                    .radius(1500);
//            mBaiduMap.addOverlay(oCircle);

//            /**
//             * 绘制文本文字
//             */
//            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());//坐标参数（纬度，经度）
//            OverlayOptions ooText = new TextOptions().bgColor(0xAAFFFF00)
//                    .fontSize(24).fontColor(0xFFFF00FF).text("我的位置").rotate(0)
//                    .position(point);
//            mBaiduMap.addOverlay(ooText);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(12.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                // GPS定位结果
//                tv_location.setText(location.getProvince() + location.getCity() +  location.getDistrict()+location.getStreet());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                // 网络定位结果
//                tv_location.setText(location.getProvince() + location.getCity() +  location.getDistrict()+location.getStreet());
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                // 离线定位结果
//                tv_location.setText(location.getProvince() + location.getCity() +  location.getDistrict()+location.getStreet());
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                Toast.makeText(WorkerOrderDetailActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                Toast.makeText(WorkerOrderDetailActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                Toast.makeText(WorkerOrderDetailActivity.this, "请确认手机是否开启GPS", Toast.LENGTH_SHORT).show();
            }
            confirmLocation();
            mLocationClient.stop();
        }
    }


}
