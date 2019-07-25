package com.yxw.cn.carpenterrepair.activity.user;


import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.TransactionDetailsAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.TradeListData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 提现明细
 */
public class TransactionDetailsActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    TransactionDetailsAdapter mAdapter;
    private int mPage = 2;
    private boolean isNext = false;
    public static final int loadCount = 10;
    @Override
    protected int getLayoutResId() {
        return R.layout.act_transaction_details;
    }


    private void getOrderData(int p) {
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("userId", CurrentUser.getInstance().getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", p);
        map.put("pageSize",loadCount);
        map.put("filter",requestMap);
        OkGo.<ResponseData<TradeListData>>post(UrlConstant.APPLY_WITHDRAWAL_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<TradeListData>>() {
                             @Override
                             public void onSuccess(ResponseData<TradeListData> response) {
                                 if (response != null && response.getData() != null) {
                                     if (response.isSuccess()) {
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
                             public void onError(Response<ResponseData<TradeListData>> response) {
                                 super.onError(response);
                                 if (p == 1) {
                                     mRefreshLayout.finishRefresh(false);
                                 } else {
                                     mRefreshLayout.finishLoadMore(false);
                                 }
                             }
                         }
                );

    }

    @Override
    public void initView() {
        titleBar.setTitle("提现明细");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.gray_divider)));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mAdapter = new TransactionDetailsAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        getOrderData(1);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getOrderData(mPage);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getOrderData(1);
    }
}
