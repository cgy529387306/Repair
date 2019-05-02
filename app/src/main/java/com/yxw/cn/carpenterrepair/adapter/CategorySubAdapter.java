package com.yxw.cn.carpenterrepair.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.Category;

import java.util.List;

/**
 * Created by CY on 2018/11/25
 */
public class CategorySubAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public CategorySubAdapter(List<Category> data) {
        super(R.layout.item_category_sub, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.name, item.getName())
                .setTextColor(R.id.name, item.isSelected()? Color.parseColor("#FFFFFF")
                        : Color.parseColor("#666666"))
                .setBackgroundRes(R.id.name, item.isSelected()? R.drawable.corner_repair_sel
                        : R.drawable.corner_repair_unsel);
    }
}