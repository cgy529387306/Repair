package com.yxw.cn.carpenterrepair.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.WorkerOrderedAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ui.WorkerOrderDetailActivity;
import util.EventBusUtil;
import util.SpaceItemDecoration;

public class WorkerOrderedFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv)
    RecyclerView mRv;

    private List<UserOrder.ListBean> mList;
    private WorkerOrderedAdapter mAdapter;
    private int page = 2;
    private boolean isNext = false;

    @Override
    protected int getLayout() {
        return R.layout.common_recycleview;
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        mAdapter = new WorkerOrderedAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new SpaceItemDecoration(20));
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void getData() {
        getOrderData(1);
    }

    private void getOrderData(int p) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", null);
        map.put("agencyId", null);
        map.put("createTime", null);
        map.put("page", p);
        map.put("limit", loadCount);
        OkGo.<ResponseData<UserOrder>>post(UrlConstant.WORKER_ORDER_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<UserOrder>>() {
                    @Override
                    public void onSuccess(ResponseData<UserOrder> response) {
                        if (response.getCode() == 0) {
                            if (p == 1) {
                                page=2;
                                mList.clear();
                                mRefreshLayout.finishRefresh(true);
                            } else {
                                isNext = response.getData().isIsNext();
                                if (isNext) {
                                    page++;
                                    mRefreshLayout.finishLoadMore(true);
                                } else {
                                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                            mList.addAll(response.getData().getList());
                            mAdapter.notifyDataSetChanged();
                            mRefreshLayout.finishRefresh();
                            EventBusUtil.post(MessageConstant.WORKER_ORDERED_COUNT, mList.size());
                        } else {
                            toast(response.getMsg());
                            if (p == 1) {
                                mRefreshLayout.finishRefresh(true);
                            } else {
                                mRefreshLayout.finishRefresh(false);
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
        startActivity(WorkerOrderDetailActivity.class, mList.get(position).getId());
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_ORDER:
                getData();
                break;
        }
    }
}
