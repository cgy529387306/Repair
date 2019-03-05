package com.yxw.cn.carpenterrepair.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.CategorySubItem;

import java.util.List;

public class DetailCategorySubAdapter extends BaseQuickAdapter<CategorySubItem, BaseViewHolder> {

    private String selectId;

    public DetailCategorySubAdapter(String selectId, @Nullable List<CategorySubItem> data) {
        super(R.layout.item_detail_category_sub, data);
        this.selectId = selectId;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategorySubItem item) {
        helper.setText(R.id.tv_name, item.getName())
                .setTextColor(R.id.tv_name, selectId.equals(item.getId() + "") ? mContext.getResources().getColor(R.color.text_red) : mContext.getResources().getColor(R.color.text_gray))
                .setBackgroundRes(R.id.ll_item, selectId.equals(item.getId() + "") ? R.drawable.border_red : 0)
                .setGone(R.id.iv_selected, selectId.equals(item.getId() + ""));
    }
}
