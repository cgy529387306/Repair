package com.yxw.cn.carpenterrepair.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.RemarkBean;

import java.util.List;

public class RemarkAdapter extends BaseQuickAdapter<RemarkBean, BaseViewHolder> {

    public RemarkAdapter(@Nullable List<RemarkBean> data) {
        super(R.layout.item_remark, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemarkBean item) {
        helper.setText(R.id.tv_remark,(helper.getAdapterPosition()+1)+"."+item.getOrderContent());
    }

}
