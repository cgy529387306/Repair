package com.yxw.cn.carpenterrepair.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.BeGoodAtCategory;

import java.util.List;

/**
 * Created by CY on 2018/12/11
 */
public class MyCategoryAdapter extends BaseQuickAdapter<BeGoodAtCategory, BaseViewHolder> {

    public MyCategoryAdapter(List<BeGoodAtCategory> data) {
        super(R.layout.item_my_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BeGoodAtCategory item) {
        helper.setText(R.id.name, item.getCategoryName());
    }
}
