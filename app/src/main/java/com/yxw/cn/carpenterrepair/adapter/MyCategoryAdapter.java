package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;

import java.util.List;

/**
 * Created by CY on 2018/12/11
 */
public class MyCategoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MyCategoryAdapter(List<String> data) {
        super(R.layout.item_my_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.name, item);
    }
}
