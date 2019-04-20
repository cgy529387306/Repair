package com.yxw.cn.carpenterrepair.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.CategoryTitleAdapter;
import com.yxw.cn.carpenterrepair.adapter.DetailCategoryAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.CategorySub;
import com.yxw.cn.carpenterrepair.entity.CategorySubItem;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.yxw.cn.carpenterrepair.activity.order.OrderActivity;
import com.yxw.cn.carpenterrepair.util.ToastUtil;

/**
 * 找服务
 */
public class FindServiceFragment extends BaseFragment {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;
    @BindView(R.id.rv_sub)
    RecyclerView mRvSub;

    private CategoryTitleAdapter titleAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<CategorySub> categorySubList = new ArrayList<>();
    private DetailCategoryAdapter detailCategoryAdapter;
    private String selectedId = "";

    @Override
    protected int getLayout() {
        return R.layout.frg_find_service;
    }

    @Override
    protected void initView() {
        titlebar.setTitle("找服务");
        titlebar.setLeftVisible(false);
        mRvCategory.setLayoutManager(new LinearLayoutManager(mContext));
        titleAdapter = new CategoryTitleAdapter(categoryList);
        mRvCategory.setAdapter(titleAdapter);
        titleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                titleAdapter.setSelectIndex(position);

                notifyDetailCategory(position);
            }
        });

        mRvSub.setLayoutManager(new LinearLayoutManager(mContext));
        detailCategoryAdapter = new DetailCategoryAdapter(selectedId, categorySubList);
        mRvSub.setAdapter(detailCategoryAdapter);
    }

    public void notifyDetailCategory(int index) {
        categorySubList.clear();
        if (categoryList.get(index).getSub() != null && categoryList.get(index).getSub().size() > 0) {
            categorySubList.addAll(categoryList.get(index).getSub());
        }
        detailCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void getData() {
        if (AppUtil.categoryItemList != null && AppUtil.categoryItemList.size() > 0) {
            categoryList.clear();
            categoryList.addAll(AppUtil.categoryItemList);
            titleAdapter.setSelectIndex(0);
            titleAdapter.notifyDataSetChanged();

            notifyDetailCategory(0);
        } else {
            OkGo.<ResponseData<List<Category>>>get(UrlConstant.GET_ALL_CATEGORY)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<Category>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<Category>> response) {
                            AppUtil.categoryItemList.clear();
                            AppUtil.categoryItemList.addAll(response.getData());
                            categoryList.clear();
                            categoryList.addAll(response.getData());
                            titleAdapter.setSelectIndex(0);

                            notifyDetailCategory(0);
                        }

                        @Override
                        public void onError(Response<ResponseData<List<Category>>> response) {
                            super.onError(response);
                            ToastUtil.show("网络异常");
                        }
                    });
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.CHOOSE_CATEGORY_SERVICE:
                CategorySubItem categorySubItem = (CategorySubItem) event.getData();
                selectedId =categorySubItem.getId()+"";
                detailCategoryAdapter.setSelectId(selectedId);
                startActivity(OrderActivity.class,categorySubItem);
                break;
        }
    }
}
