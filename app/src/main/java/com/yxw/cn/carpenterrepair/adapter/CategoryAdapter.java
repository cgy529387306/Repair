package com.yxw.cn.carpenterrepair.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.BeGoodAtCategory;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.CategorySub;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    private List<BeGoodAtCategory> selectedList;

    public CategoryAdapter(@Nullable List<Category> data) {
        super(R.layout.item_category, data);
    }

    public CategoryAdapter(@Nullable List<Category> data, List<BeGoodAtCategory> selectedList) {
        super(R.layout.item_category, data);
        this.selectedList = selectedList;
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.tv_name, item.getName());
        RecyclerView rv = helper.getView(R.id.rv_sub);
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        List<CategorySub> data = new ArrayList<>();
        if (item.getSub() != null) {
            data.addAll(item.getSub());
        }
        if (this.selectedList != null) {
            for (BeGoodAtCategory category :
                    selectedList) {
                for (CategorySub categorySub :
                        data) {
                    if (category.getCategoryId().equals(categorySub.getId() + "")) {
                        categorySub.setSelected(true);
                        break;
                    }
                }

            }
        }

        CategorySubAdapter adapter = new CategorySubAdapter(data, selectedList);
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                item.getSub().get(position).setSelected(!item.getSub().get(position).isSelected());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
