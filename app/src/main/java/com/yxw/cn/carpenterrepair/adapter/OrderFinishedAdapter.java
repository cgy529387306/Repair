package com.yxw.cn.carpenterrepair.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.OrderItem;
import com.yxw.cn.carpenterrepair.util.AppUtil;

import java.util.List;

/**
 * Created by CY on 2018/11/25
 */
public class OrderFinishedAdapter extends BaseQuickAdapter<OrderItem, BaseViewHolder> {


    public OrderFinishedAdapter(List<OrderItem> data) {
        super(R.layout.item_worker_ordered, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderItem item) {
        TextView tvOperate0 = helper.getView(R.id.tv_operate0);
        TextView tvOperate1 = helper.getView(R.id.tv_operate1);
        TextView tvOperate2 = helper.getView(R.id.tv_operate2);
        tvOperate0.setVisibility(View.GONE);
        tvOperate1.setVisibility(View.GONE);
        tvOperate2.setVisibility(View.VISIBLE);
        tvOperate2.setText("查看");
        int orderStatus = item.getOrderStatus();
        double price = orderStatus <= 30 ? item.getTotalPrice() : item.getFee();
        helper.setText(R.id.tv_ordre_name, item.getCategoryPName() + "/" + item.getCategoryCName())
                .setText(R.id.tv_order_no, item.getOrderSn())
                .setText(R.id.tv_order_time, item.getCustomerBookingTime())
                .setText(R.id.tv_order_address, item.getAddress())
                .setText(R.id.tv_order_content, item.getFaultDesc())
                .setText(R.id.tv_order_state, AppUtil.getOrderFinishedStatus(item.getOrderStatus()))
                .setText(R.id.tv_price, String.valueOf(price));
    }
}
