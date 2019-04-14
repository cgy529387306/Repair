package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.OrderType;

import java.util.List;


/**
 * Created by necer on 2017/6/7.
 */
public class HomeMsgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HomeMsgAdapter(List data) {
        super(R.layout.item_home_msg, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}




