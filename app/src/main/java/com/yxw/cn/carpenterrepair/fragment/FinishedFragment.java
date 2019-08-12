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
import com.yxw.cn.carpenterrepair.adapter.OrderFinishedAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.OrderCount;
import com.yxw.cn.carpenterrepair.entity.OrderListData;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 订单列表
 */
public class FinishedFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private static final String KEY_STATE = "key_state";
    private OrderFinishedAdapter mAdapter;
    private int mPage = 2;
    private boolean isNext = false;
    private int mState;
    /**
     * @param state 0:全部 1:已审核 2:已结算
     * @return
     */
    public static Fragment getInstance(int state) {
        Fragment fragment = new FinishedFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_STATE, state);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.common_recycleview;
    }

    @Override
    protected void initView() {
        mState = (int) getArguments().get(KEY_STATE);
        mAdapter = new OrderFinishedAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mRecyclerView.setAdapter(mAdapter);
        getOrderData(1);
    }


    private void getOrderData(int p) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("status", 4);
        if (mState==1){
            requestMap.put("orderStatus",100);
        }else if (mState==2){
            requestMap.put("orderStatus",110);
        }
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
                                OrderCount orderCount = new OrderCount(response.getData().getTotalCount(),mState);
                                EventBusUtil.post(MessageConstant.UPDATE_ORDER_COUNT,orderCount);
                                isNext = response.getData().isHasNext();
                                if (p == 1) {
                                    mAdapter.setNewData(response.getData().getItems());
                                    mAdapter.setEmptyView(R.layout.empty_order, (ViewGroup) mRecyclerView.getParent());
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
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_ORDER:
                getOrderData(1);
                break;
        }
    }
}