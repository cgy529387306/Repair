package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.CategoryAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.MyTaskUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择分类
 */
public class ChooseCategoryActivity1 extends BaseActivity {

    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;

    @BindView(R.id.confirm)
    Button mBtnConfirm;

    private CategoryAdapter mCategoryAdapter;
    private List<Category> mCategoryList = new ArrayList<>();
    private List<String> mSelectCateList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.act_choose_category;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(false).init();
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mSelectCateList = (List<String>) bundle.getSerializable("cateList");
        mRvCategory.setLayoutManager(new LinearLayoutManager(this));
        mCategoryAdapter = new CategoryAdapter(mCategoryList);
        mRvCategory.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setSelectListener(new CategoryAdapter.OnCategorySelectListener() {
            @Override
            public void onSelect(List<Category> categoryList) {
                mBtnConfirm.setEnabled(Helper.isNotEmpty(categoryList));
                mBtnConfirm.setText(Helper.isEmpty(categoryList)?"请至少选择一个项目哦~":"确定");
            }
        });
    }

    @Override
    public void getData() {
        if (AppUtil.categoryItemList != null && AppUtil.categoryItemList.size() > 0) {
            initCateData();
        } else {
            showLoading();
            OkGo.<ResponseData<List<Category>>>post(UrlConstant.GET_ALL_CATEGORY)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<Category>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<Category>> response) {
                            dismissLoading();
                            if (response!=null){
                                if (response.isSuccess()){
                                    AppUtil.categoryItemList = response.getData();
                                    initCateData();
                                }else{
                                    toast(response.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<List<Category>>> response) {
                            super.onError(response);
                            dismissLoading();
                        }
                    });
        }
    }

    private void initCateData(){
        List<Category> categoryList = new ArrayList<>();
        if (Helper.isNotEmpty(mSelectCateList) && Helper.isNotEmpty(AppUtil.categoryItemList)) {
            for (Category category : AppUtil.categoryItemList) {
               if (Helper.isNotEmpty(category.getChildList())){
                   for (Category childCate:category.getChildList()){
                       for (String name : mSelectCateList) {
                           if (name.equals(childCate.getName())) {
                               childCate.setSelected(true);
                               categoryList.add(childCate);
                           }
                       }
                   }
               }
            }
        }
        mCategoryList.clear();
        mCategoryList.addAll(AppUtil.categoryItemList);
        mCategoryAdapter.setSelectedList(categoryList);
        mCategoryAdapter.notifyDataSetChanged();
        mBtnConfirm.setEnabled(Helper.isNotEmpty(categoryList));
        mBtnConfirm.setText(Helper.isEmpty(categoryList)?"请至少选择一个项目哦~":"确定");
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                saveProject(mCategoryAdapter.getSelectedList());
                break;
        }
    }

    private void saveProject(List<Category> dataList){
        Intent intent = new Intent();
        intent.putExtra("cateList", (Serializable) dataList);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
