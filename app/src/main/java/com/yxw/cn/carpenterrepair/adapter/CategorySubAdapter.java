package com.yxw.cn.carpenterrepair.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.BeGoodAtCategory;
import com.yxw.cn.carpenterrepair.entity.CategorySub;

import java.util.List;

/**
 * Created by CY on 2018/11/25
 */
public class CategorySubAdapter extends BaseQuickAdapter<CategorySub, BaseViewHolder> {

    private int select = -1;

    public CategorySubAdapter(List data, List<BeGoodAtCategory> selectedList) {
        super(R.layout.item_category_sub, data);
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategorySub item) {

        helper.setText(R.id.name, item.getName())
                .setTextColor(R.id.name, item.isSelected()? Color.parseColor("#FFFFFF")
                        : Color.parseColor("#7B7B7B"))
                .setBackgroundRes(R.id.name, item.isSelected()? R.drawable.corner_repair_sel
                        : R.drawable.corner_repair_unsel);
    }
}