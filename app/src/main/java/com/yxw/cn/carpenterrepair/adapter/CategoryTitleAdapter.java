package com.yxw.cn.carpenterrepair.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.Category;

import java.util.List;

public class CategoryTitleAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    private int selectIndex = 0;

    public CategoryTitleAdapter(@Nullable List<Category> data) {
        super(R.layout.item_category_title, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.tv_title, item.getName())
                .setTextColor(R.id.tv_title, selectIndex == helper.getAdapterPosition() ? mContext.getResources().getColor(R.color.text_red) : mContext.getResources().getColor(R.color.text_gray))
                .setVisible(R.id.view_label, selectIndex == helper.getAdapterPosition())
                .setBackgroundColor(R.id.ll_title, selectIndex == helper.getAdapterPosition() ? mContext.getResources().getColor(R.color.bg_gray2) : mContext.getResources().getColor(R.color.white));
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }
}
