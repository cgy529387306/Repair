package com.yxw.cn.carpenterrepair.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    private List<Category> mSelectedList = new ArrayList<>();
    private OnCategorySelectListener mSelectListener;

    public void setSelectListener(OnCategorySelectListener selectListener) {
        this.mSelectListener = selectListener;
    }

    public List<Category> getSelectedList() {
        if (mSelectedList == null) {
            return new ArrayList<>();
        }
        return mSelectedList;
    }

    public void setSelectedList(List<Category> selectedList) {
        this.mSelectedList = selectedList;
    }

    public interface OnCategorySelectListener{
        void onSelect(List<Category> categoryList);
    }

    public CategoryAdapter(@Nullable List<Category> data) {
        super(R.layout.item_category, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.tv_name, item.getName());
        RecyclerView rv = helper.getView(R.id.rv_sub);
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        CategorySubAdapter adapter = new CategorySubAdapter(item.getChildList());
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Category category = item.getChildList().get(position);
                category.setSelected(!category.isSelected());
                if (category.isSelected()){
                    mSelectedList.add(category);
                }else{
                    mSelectedList.remove(category);
                }
                adapter.notifyDataSetChanged();
                if (mSelectListener!=null){
                    mSelectListener.onSelect(mSelectedList);
                }
            }
        });
    }



}
