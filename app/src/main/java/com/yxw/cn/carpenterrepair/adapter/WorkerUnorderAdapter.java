package com.yxw.cn.carpenterrepair.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.util.AppUtil;

import java.util.List;

import util.DoubleUtil;

/**
 * Created by CY on 2018/11/25
 */
public class WorkerUnorderAdapter extends BaseQuickAdapter<UserOrder.ListBean, BaseViewHolder> {

    public WorkerUnorderAdapter(List<UserOrder.ListBean> data) {
        super(R.layout.item_worker_unorder, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrder.ListBean item) {
        helper.setText(R.id.name, item.getCategoryName())
                .setText(R.id.price, "¥" + DoubleUtil.getTwoDecimal(item.getTotalPrice()))
                .setText(R.id.time, item.getBookingDate() + " " + item.getBookingTime())
                .setText(R.id.addr, AppUtil.getOrderDetailAddress(item))
                .setText(R.id.remark, TextUtils.isEmpty(item.getRemark()) ? "无备注" : item.getRemark())
//                .setText(R.id.recive_order, AppUtil.getWorkerOrderStatus(item))
                .setText(R.id.recive_order, item.getReceiveStatus()==0?"我要接单":"已接单")
                .addOnClickListener(R.id.recive_order);
    }
}
