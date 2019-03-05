package com.yxw.cn.carpenterrepair.activity.login;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by CY on 2018/11/25
 */
public class ChooseCategoryActivity extends BaseActivity {

    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;

    private CategoryAdapter adapter;
    private List<Category> categoryItemList = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.act_choose_category;
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
    }

    @Override
    public void initView() {
        mRvCategory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(categoryItemList);
        mRvCategory.setAdapter(adapter);
    }

    @Override
    public void getData() {
        if (AppUtil.categoryItemList != null&& AppUtil.categoryItemList.size() > 0) {
            categoryItemList.clear();
            categoryItemList.addAll(AppUtil.categoryItemList);
            adapter.notifyDataSetChanged();
        } else {
            OkGo.<ResponseData<List<Category>>>get(UrlConstant.GET_ALL_CATEGORY)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<Category>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<Category>> response) {
                            AppUtil.categoryItemList.clear();
                            AppUtil.categoryItemList.addAll(response.getData());
                            categoryItemList.clear();
                            categoryItemList.addAll(response.getData());
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Response<ResponseData<List<Category>>> response) {
                            super.onError(response);
                            toast("网络异常");
                        }
                    });
        }
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
             /*   if (homeAdapter.getSelect() == -1 && eleAdapter.getSelect() == -1 && computerAdapter.getSelect() == -1) {
                    ToastUtil.show("请至少选择一个项目哦~");
                } else {
                    startActivityFinish(RegisterSuccessActivity.class);
                }*/
                    startActivityFinish(RegisterSuccessActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
