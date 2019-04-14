package com.yxw.cn.carpenterrepair.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.CancelOrderActivity;
import com.yxw.cn.carpenterrepair.activity.CustomerServiceActivity;
import com.yxw.cn.carpenterrepair.activity.WebActivity;
import com.yxw.cn.carpenterrepair.adapter.OrderAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import util.SpaceItemDecoration;

public class UserOrderListFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv)
    RecyclerView mRv;

    private String type;
    private int status;
    private HashMap<String, Object> map;
    private List<UserOrder.ListBean> mList;
    private OrderAdapter mAdapter;
    private int page = 2;
    private boolean isNext = false;

    public static UserOrderListFragment newInstance(String type, int status) {
        UserOrderListFragment f = new UserOrderListFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putInt("status", status);
        f.setArguments(args);
        return f;
    }

    @Override
    protected int getLayout() {
        return R.layout.common_recycleview;
    }

    @Override
    protected void initView() {
        type = getArguments().getString("type");
        status = getArguments().getInt("status");
        getMap();
        mList = new ArrayList<>();
        mAdapter = new OrderAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new SpaceItemDecoration(20));
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void getData() {
        OkGo.<ResponseData<UserOrder>>post(UrlConstant.USER_ORDER_LIST)
                .upJson(type)
                .execute(new JsonCallback<ResponseData<UserOrder>>() {
                    @Override
                    public void onSuccess(ResponseData<UserOrder> response) {
                        mRefreshLayout.finishRefresh(true);
                        if (response.getCode() == 0) {
                            page = 2;
                            isNext = response.getData().isIsNext();
                            mList.clear();
                            if (response.getData() != null) {
                                mList.addAll(response.getData().getList());
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            toast(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<UserOrder>> response) {
                        super.onError(response);
                        mRefreshLayout.finishRefresh(false);
                    }
                });
    }

    private void getMap() {
        map = new HashMap<>();
        if (status == 1) {
            map.put("page", 1);
            map.put("limit", loadCount);
        } else if (status == 2) {
            map.put("receiveStatus", "0");
            map.put("page", 1);
            map.put("limit", loadCount);
        } else if (status == 3) {
            map.put("receiveStatus", "1");
            map.put("serviceStatus", "0");
            map.put("page", 1);
            map.put("limit", loadCount);
        } else if (status == 4) {
            map.put("receiveStatus", "1");
            map.put("serviceStatus", "1");
            map.put("page", 1);
            map.put("limit", loadCount);
        } else if (status == 5) {
            map.put("receiveStatus", "1");
            map.put("serviceStatus", "3");
            map.put("page", 1);
            map.put("limit", loadCount);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        map.put("page", page);
        OkGo.<ResponseData<UserOrder>>post(UrlConstant.USER_ORDER_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<UserOrder>>() {
                    @Override
                    public void onSuccess(ResponseData<UserOrder> response) {
                        if (response.getCode() == 0) {
                            isNext = response.getData().isIsNext();
                            if (isNext) {
                                page++;
                                mRefreshLayout.finishLoadMore(true);
                            } else {
                                mRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                            if (response.getData() != null) {
                                mList.addAll(response.getData().getList());
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRefreshLayout.finishLoadMore(false);
                            toast(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<UserOrder>> response) {
                        super.onError(response);
                        mRefreshLayout.finishLoadMore(false);
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle webBundle = new Bundle();
        webBundle.putString("url", "http://jx.bdelay.com/worker/order/detail/id/" + mList.get(position).getId() + ".html");
        webBundle.putString("title", "订单详情");
        webBundle.putBoolean("header", true);
        startActivity(WebActivity.class, webBundle);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_contact:
/*                Intent intent = new Intent(getContext(), ComplaintActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", mList.get(position).getId());
                intent.putExtras(bundle);
                startActivity(intent);*/
                startActivity(CustomerServiceActivity.class);
                break;
            case R.id.tv_cancel:
                Intent intent = new Intent(mContext, CancelOrderActivity.class);
                intent.putExtra("data", mList.get(position));
                if (mList.get(position).getOrderStatus() < 40) {
                    startActivity(intent);
                } else {
                    DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                            .setContentHolder(new ViewHolder(R.layout.dlg_cancel_order))
                            .setGravity(Gravity.CENTER)
                            .setCancelable(true)
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {
                                    switch (view.getId()) {
                                        case R.id.dialog_confirm:
                                            dialog.dismiss();
                                            startActivity(intent);
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
                break;
        }
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
