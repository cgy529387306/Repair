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
import com.yxw.cn.carpenterrepair.activity.order.OrderDetailActivity;
import com.yxw.cn.carpenterrepair.adapter.OrderAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OrderFilterRequest;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.JsonHelper;
import com.yxw.cn.carpenterrepair.util.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 订单列表
 */
public class OrderFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final String KEY_STATE = "key_state";
    private static final String KEY_TYPE = "key_type";
    private OrderAdapter mAdapter;
    private int page = 2;
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

        mAdapter = new OrderAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mRecyclerView.setAdapter(mAdapter);
        getOrderData(1);
    }

    @Override
    public void getData() {
        List<UserOrder.ListBean> dataList = new ArrayList<>();
        for (int i=0;i<10;i++){
            UserOrder.ListBean userOrder = new UserOrder.ListBean();
            dataList.add(userOrder);
        }
        mAdapter.setNewData(dataList);
//        getOrderData(1);
    }

    private void getOrderData(int p) {
        OrderFilterRequest filterRequest = new OrderFilterRequest(mBookingTime,mOrderStatus+"");
        Map<String, Object> map = new HashMap<>();
        map.put("filter", JsonHelper.toJson(filterRequest));
        map.put("pageIndex", p);
        map.put("pageSize", loadCount);
        OkGo.<ResponseData<UserOrder>>post(UrlConstant.ORDER_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<UserOrder>>() {
                    @Override
                    public void onSuccess(ResponseData<UserOrder> response) {
                        if (response.isSuccess()) {
                            if (p == 1) {
                                page = 2;
                                mAdapter.setNewData(response.getData().getList());
                                mRefreshLayout.finishRefresh();
                            } else {
                                mAdapter.addData(response.getData().getList());
                                isNext = response.getData().isIsNext();
                                if (isNext) {
                                    page++;
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

                    @Override
                    public void onError(Response<ResponseData<UserOrder>> response) {
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
        getOrderData(page);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.getItem(position)!=null){
            startActivity(OrderDetailActivity.class, mAdapter.getItem(position).getId());
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_ORDER:
                getOrderData(1);
                break;
        }
    }
}