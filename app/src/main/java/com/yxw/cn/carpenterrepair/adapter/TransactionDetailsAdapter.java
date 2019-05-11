package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.TradeItem;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.util.AppUtil;

import java.util.List;


/**
 * Created by necer on 2017/6/7.
 */
public class TransactionDetailsAdapter extends BaseQuickAdapter<TradeItem, BaseViewHolder> {

    public TransactionDetailsAdapter(List<TradeItem> data) {
        super(R.layout.item_transaction_detail, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, TradeItem item) {
        String tradeType =  item.getTradeType()==3?"+":"-";
        helper.setText(R.id.tv_name, AppUtil.getTradeName(item));
        helper.setText(R.id.tv_trade_amount,tradeType+item.getAmount());
        helper.setText(R.id.tv_time,item.getStartTime());
        helper.setText(R.id.tv_rest_money, CurrentUser.getInstance().getCarryAmount());
    }
}




