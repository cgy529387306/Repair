package com.yxw.cn.carpenterrepair.fragment;

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
import com.yxw.cn.carpenterrepair.adapter.OrderAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.entity.OrderListData;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.listerner.OnChooseDateListener;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
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
public class OrderFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener , OrderAdapter.OnOrderOperateListener {

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

    /**
     * @param state 0:今天 1:明天 2:全部
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
        requestMap.put("customerBookingTime",mBookingTime);
        requestMap.put("orderStatus",mOrderStatus);
        Map<String, Object> map = new HashMap<>();
        map.put("filter", requestMap);
        map.put("pageIndex", p);
        map.put("pageSize", loadCount);
        map.put("sorter", "");
        OkGo.<ResponseData<OrderListData>>post(UrlConstant.ORDER_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<OrderListData>>() {
                    @Override
                    public void onSuccess(ResponseData<OrderListData> response) {
                        if (response!=null && response.getData()!=null){
                            if (response.isSuccess()) {
                                if (p == 1) {
                                    mPage = 2;
                                    mAdapter.setNewData(response.getData().getItems());
                                    mRefreshLayout.finishRefresh();
                                } else {
                                    mAdapter.addData(response.getData().getItems());
                                    isNext = response.getData().isHasNext();
                                    if (isNext) {
                                        mPage++;
                                        mRefreshLayout.finishLoadMore();
                                    } else {
                                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                                mAdapter.setEmptyView(R.layout.empty_data, (ViewGroup) mRecyclerView.getParent());
                                EventBusUtil.post(MessageConstant.WORKER_ORDERED_COUNT, mAdapter.getData().size());
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
//            startActivity(OrderDetailActivity.class, mAdapter.getItem(position).get());
        }
    }

    @Override
    public void onOrderTaking(OrderItem orderItem) {
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

    @Override
    public void onAbnormal(OrderItem orderItem) {

    }

    @Override
    public void onContact(OrderItem orderItem) {
        TimePickerUtil.showYearPicker(getActivity(), new OnChooseDateListener() {
            @Override
            public void getDate(Date date) {
                String ss = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
            }
        });
    }

    @Override
    public void onTurnReservation(OrderItem orderItem) {

    }

    @Override
    public void onFinish(OrderItem orderItem) {

    }

    @Override
    public void onView(OrderItem orderItem) {

    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_ORDER:
                getOrderData(1);
                break;
        }
    }
}