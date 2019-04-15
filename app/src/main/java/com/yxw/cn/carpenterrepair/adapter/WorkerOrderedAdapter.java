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
public class WorkerOrderedAdapter extends BaseQuickAdapter<UserOrder.ListBean, BaseViewHolder> {

    public WorkerOrderedAdapter(List<UserOrder.ListBean> data) {
        super(R.layout.item_worker_ordered, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrder.ListBean item) {
        helper.setText(R.id.tv_ordre_name, item.getCategoryName())
                .setText(R.id.tv_order_time, item.getBookingDate() + " " + item.getBookingTime())
                .setText(R.id.tv_order_address, AppUtil.getOrderDetailAddress(item))
                .setText(R.id.tv_order_content, TextUtils.isEmpty(item.getRemark()) ? "无备注" : item.getRemark())
                .setText(R.id.tv_order_state, item.getOrderStatusName())
                .addOnClickListener(R.id.tv_order_state);
    }
}
