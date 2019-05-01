package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;

import java.util.List;


/**
 * Created by necer on 2017/6/7.
 */
public class TransactionDetailsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TransactionDetailsAdapter(List data) {
        super(R.layout.item_transaction_detail, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name,item);
    }
}




