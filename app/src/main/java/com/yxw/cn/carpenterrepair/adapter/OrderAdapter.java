package com.yxw.cn.carpenterrepair.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
public class OrderAdapter extends BaseQuickAdapter<UserOrder.ListBean, BaseViewHolder> {

    public OrderAdapter(List data) {
        super(R.layout.item_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrder.ListBean item) {
        int receiveStatus = item.getReceiveStatus();
        String status=AppUtil.getUserOrderStatus(item);
        helper.setText(R.id.name, item.getCategoryName())
                .setText(R.id.price, "￥"+DoubleUtil.getTwoDecimal(item.getTotalPrice()))
                //0未上门 1已上门 2已评估 3已完成
                .setText(R.id.serviceStatus,status)
                .setText(R.id.tv_addr, item.getProvince() + item.getCity() + item.getDistrict() + item.getAddress())
                .setText(R.id.time, item.getBookingDate() + " " + item.getBookingTime())
                .setText(R.id.remark, TextUtils.isEmpty(item.getRemark()) ?"无备注":item.getRemark())
                .setGone(R.id.tv_cancel,receiveStatus == 0)
                .setGone(R.id.tv_contact,receiveStatus == 0)
                .addOnClickListener(R.id.tv_contact)
                .addOnClickListener(R.id.tv_cancel);
        TextView contact=helper.getView(R.id.tv_contact);
        TextView cancel=helper.getView(R.id.tv_cancel);
        if(status.equals("未接单")||status.equals("待服务")){
            contact.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            contact.setText("联系客服");
            cancel.setText("取消订单");
        }else if(status.equals("进行中")){
            contact.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            contact.setText("联系客服");
            cancel.setText("投诉");
        }else if(status.equals("待评价")){
            contact.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
            contact.setText("评价晒单");
        }else if(status.equals("已完成")){
            contact.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
    }
}
