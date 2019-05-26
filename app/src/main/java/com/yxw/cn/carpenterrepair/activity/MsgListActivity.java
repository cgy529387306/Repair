package com.yxw.cn.carpenterrepair.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.HomeMsgAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.NoticeListData;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 消息
 */
public class MsgListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private HomeMsgAdapter mAdapter;
    private int mPage = 2;
    private boolean isNext = false;
    public static final int loadCount = 10;
    @Override
    protected int getLayoutResId() {
        return R.layout.act_transaction_details;
    }


    private void getOrderData(int p) {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", p);
        map.put("pageSize", loadCount);
        OkGo.<ResponseData<NoticeListData>>post(UrlConstant.GET_NOTICE)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<NoticeListData>>() {
                    @Override
                    public void onSuccess(ResponseData<NoticeListData> response) {
                        if (response!=null){
                            if (response.isSuccess() && response.getData()!=null) {
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
                    public void onError(Response<ResponseData<NoticeListData>> response) {
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
    public void initView() {
        titleBar.setTitle("消息中心");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.gray_divider)));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mAdapter = new HomeMsgAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.getItem(position)!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("data",mAdapter.getData().get(position));
            startActivity(MsgDetailActivity.class, bundle);
        }
    }
}
