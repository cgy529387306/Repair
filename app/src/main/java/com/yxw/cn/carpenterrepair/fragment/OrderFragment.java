package com.yxw.cn.carpenterrepair.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.order.AppointAbnormalActivity;
import com.yxw.cn.carpenterrepair.activity.order.MyOrder1Activity;
import com.yxw.cn.carpenterrepair.activity.order.MyOrderActivity;
import com.yxw.cn.carpenterrepair.activity.order.OrderDetailActivity;
import com.yxw.cn.carpenterrepair.activity.order.OrderSignInActivity;
import com.yxw.cn.carpenterrepair.activity.order.SignAbnormalActivity;
import com.yxw.cn.carpenterrepair.adapter.OrderAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OperateResult;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.entity.OrderListData;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.listerner.OnChooseDateListener;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.pop.ConfirmOrderPop;
import com.yxw.cn.carpenterrepair.pop.ContactPop;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.PreferencesHelper;
import com.yxw.cn.carpenterrepair.util.SpaceItemDecoration;
import com.yxw.cn.carpenterrepair.util.TimePickerUtil;
import com.yxw.cn.carpenterrepair.util.TimeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 订单列表
 */
public class OrderFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener , OrderAdapter.OnOrderOperateListener,ContactPop.SelectListener,ConfirmOrderPop.SelectListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final String KEY_STATE = "key_state";
    private static final String KEY_TYPE = "key_type";
    private OrderAdapter mAdapter;
    private int mPage = 2;
    private boolean isNext = false;
    private int mOrderStatus;
    private int mOrderType;
    private String mBookingTime;
    private ContactPop mContactPop;
    private ConfirmOrderPop mConfirmOrderPop;
    /**
     * @param type 0:今天 1:明天 2:全部
     * @return
     */
    public static Fragment getInstance(int state,int type) {
        Fragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_STATE, state);
        bundle.putInt(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.common_recycleview;
    }

    @Override
    protected void initView() {
        mOrderStatus = (int) getArguments().get(KEY_STATE);
        mOrderType = (int) getArguments().get(KEY_TYPE);
        if (mOrderType==0){
            mBookingTime = Helper.date2String(new Date(),"yyyy-MM-dd");
        }else if (mOrderType==1){
            Calendar calendar = Calendar.getInstance();
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            mBookingTime = Helper.date2String(calendar.getTime(),"yyyy-MM-dd");
        }else{
            mBookingTime = "";
        }

        mAdapter = new OrderAdapter(new ArrayList<>(),this);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mRecyclerView.setAdapter(mAdapter);
        getOrderData(1);
    }


    private void getOrderData(int p) {
        Map<String, Object> requestMap = new HashMap<>();
        if (mOrderType!=2){
            if (mOrderStatus == 2 || mOrderStatus == 3){
                requestMap.put("bookingStartTime",mBookingTime);
            }else{
                requestMap.put("customerBookingTime",mBookingTime);
            }
        }
        if (mOrderStatus==0){
            String locationLat = PreferencesHelper.getInstance().getString("latitude","26.088114");
            String locationLng = PreferencesHelper.getInstance().getString("longitude","119.310492");
            requestMap.put("locationLat",locationLat);
            requestMap.put("locationLng",locationLng);
        }
        requestMap.put("status",mOrderStatus);
        Map<String, Object> map = new HashMap<>();
        map.put("filter", requestMap);
        map.put("pageIndex", p);
        map.put("pageSize", loadCount);
        OkGo.<ResponseData<OrderListData>>post(UrlConstant.ORDER_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<OrderListData>>() {
                    @Override
                    public void onSuccess(ResponseData<OrderListData> response) {
                        if (response!=null){
                            if (response.isSuccess() && response.getData()!=null) {
                                isNext = response.getData().isHasNext();
                                if (p == 1) {
                                    mAdapter.setNewData(response.getData().getItems());
                                    mAdapter.setEmptyView(R.layout.empty_data, (ViewGroup) mRecyclerView.getParent());
                                    mRefreshLayout.finishRefresh();
                                    if (isNext){
                                        mPage = 2;
                                    }else{
                                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                } else {
                                    mAdapter.addData(response.getData().getItems());
                                    if (isNext) {
                                        mPage++;
                                        mRefreshLayout.finishLoadMore();
                                    } else {
                                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                toast(response.getMsg());
                                if (p == 1) {
                                    mRefreshLayout.finishRefresh(false);
                                } else {
                                    mRefreshLayout.finishLoadMore(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<OrderListData>> response) {
                        super.onError(response);
                        if (p == 1) {
                            mRefreshLayout.finishRefresh(false);
                        } else {
                            mRefreshLayout.finishLoadMore(false);
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getOrderData(1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getOrderData(mPage);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.getItem(position)!=null){
            startActivity(OrderDetailActivity.class, mAdapter.getData().get(position));
        }
    }

    @Override
    public void onOrderTaking(OrderItem orderItem) {
        if (mConfirmOrderPop==null){
            mConfirmOrderPop = new ConfirmOrderPop(getActivity(),orderItem,this);
        }
        mConfirmOrderPop.showPopupWindow(mRecyclerView);
    }

    @Override
    public void onAbnormal(OrderItem orderItem,int type) {
        if (type==0){
            Bundle bundle = new Bundle();
            bundle.putString("acceptId",orderItem.getAcceptId());
            startActivity(AppointAbnormalActivity.class,bundle);
        }else{
            Bundle bundle = new Bundle();
            bundle.putString("acceptId",orderItem.getAcceptId());
            startActivity(SignAbnormalActivity.class,bundle);
        }
    }

    @Override
    public void onContact(OrderItem orderItem) {
        if (mContactPop==null){
            mContactPop = new ContactPop(getActivity(),this,orderItem);
        }
        mContactPop.showPopupWindow(mRecyclerView);
    }

    @Override
    public void onTurnReservation(OrderItem orderItem) {
        TimePickerUtil.showYearPicker(getActivity(), new OnChooseDateListener() {
            @Override
            public void getDate(Date date) {
                String startTime = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
                String endTime = TimeUtil.getAfterHourTime(date);
                showLoading();
                HashMap<String, Object> map = new HashMap<>();
                map.put("acceptId", orderItem.getAcceptId());
                map.put("bookingStartTime", startTime);
                map.put("bookingEndTime", endTime);
                OkGo.<ResponseData<Object>>post(UrlConstant.ORDER_TURN_RESERVATION)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<Object>>() {
                            @Override
                            public void onSuccess(ResponseData<Object> response) {
                                dismissLoading();
                                if (response!=null){
                                    if (response.isSuccess()) {
                                        toast("改约成功");
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
    public void onSign(OrderItem orderItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",orderItem);
        bundle.putInt("type",0);
        startActivity(OrderSignInActivity.class,bundle);
    }

    @Override
    public void onFinish(OrderItem orderItem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order",orderItem);
        bundle.putInt("type",1);
        startActivity(OrderSignInActivity.class,bundle);
    }

    @Override
    public void onView(OrderItem orderItem) {
        startActivity(OrderDetailActivity.class, orderItem);
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_ORDER:
                getOrderData(1);
                break;
            case MessageConstant.MY_LOCATION:
                break;
        }
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
        TimePickerUtil.showYearPicker(getActivity(), new OnChooseDateListener() {
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
                                        refreshActivity(new OrderType(2,"待上门"));
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
        TimePickerUtil.showYearPicker(getActivity(), new OnChooseDateListener() {
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
                                        refreshActivity(new OrderType(2,"待上门"));
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
    public void onOrderComfirm(OrderItem orderItem) {
        showLoading();
        OkGo.<ResponseData<OperateResult>>post(UrlConstant.ORDER_RECEIVE+orderItem.getOrderId())
                .execute(new JsonCallback<ResponseData<OperateResult>>() {
                             @Override
                             public void onSuccess(ResponseData<OperateResult> response) {
                                 dismissLoading();
                                 if (response!=null){
                                     if (response.isSuccess()) {
                                         refreshActivity(new OrderType(1,"待预约"));
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

    private void refreshActivity(OrderType orderType){
        if (getActivity()!=null && !getActivity().isFinishing()){
            toast(orderType.getStatus()==1?"抢单成功":"预约成功");
            Bundle bundle = new Bundle();
            bundle.putSerializable("type",orderType);
            startActivity(getActivity() instanceof MyOrderActivity?MyOrder1Activity.class:MyOrderActivity.class,bundle);
            getActivity().finish();
        }
    }

}