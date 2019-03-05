package ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.CategoryAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.BeGoodAtCategory;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.CategorySub;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import util.EventBusUtil;

/**
 * Created by CY on 2018/11/25
 */
public class MyCategoryActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;

    private CategoryAdapter adapter;
    private List<Category> categoryItemList = new ArrayList<>();
    private List<BeGoodAtCategory> selectedList;
    private Gson gson = new Gson();

    @Override
    protected int getLayoutResId() {
        return R.layout.act_my_category;
    }

    @Override
    public void initView() {
        selectedList = (List<BeGoodAtCategory>) getIntent().getSerializableExtra("data");
        titleBar.setTitle("我擅长的项目");
        if (getIntent().getBooleanExtra("force", false)) {
            titleBar.setLeftVisible(false);
        } else {
            titleBar.setLeftVisible(true);
        }
        mRvCategory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategoryAdapter(categoryItemList, selectedList);
        mRvCategory.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!getIntent().getBooleanExtra("force", false)) {
            super.onBackPressed();
        }
    }

    @Override
    public void getData() {
        if (AppUtil.categoryItemList != null && AppUtil.categoryItemList.size() > 0) {
            notifyCategory();
        } else {
            OkGo.<ResponseData<List<Category>>>get(UrlConstant.GET_ALL_CATEGORY)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<Category>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<Category>> response) {
                            AppUtil.categoryItemList.clear();
                            AppUtil.categoryItemList.addAll(response.getData());
                            notifyCategory();
                        }

                        @Override
                        public void onError(Response<ResponseData<List<Category>>> response) {
                            super.onError(response);
                            toast("网络异常");
                        }
                    });
        }
    }

    public void notifyCategory() {
        categoryItemList.clear();
        categoryItemList.addAll(AppUtil.categoryItemList);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                List<Integer> ids = new ArrayList<>();
                List<BeGoodAtCategory> beGoodAtCategories = new ArrayList<>();
                for (Category category : categoryItemList) {
                    if (category.getSub() != null && category.getSub().size() > 0) {
                        for (CategorySub categorySub : category.getSub()) {
                            if (categorySub.isSelected()) {
                                ids.add(categorySub.getId());
                                beGoodAtCategories.add(new BeGoodAtCategory(categorySub.getId() + "", categorySub.getName()));
                            }
                        }
                    }
                }
                if (ids.size() == 0) {
                    toast("至少选择一个项目!");
                } else {
                    showLoading();
                    Map<String, Object> map = new HashMap<>();
                    map.put("categoryIds", ids);
                    OkGo.<ResponseData<String>>post(UrlConstant.EDIT_GOOD_CATEGORY)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                @Override
                                public void onSuccess(ResponseData<String> response) {
                                    dismissLoading();
                                    toast(response.getMsg());
                                    if (response.getCode() == 0) {
                                        finish();
                                        EventBusUtil.post(MessageConstant.MY_CATEGORY, beGoodAtCategories);
                                    }
                                }

                                @Override
                                public void onError(Response<ResponseData<String>> response) {
                                    super.onError(response);
                                    dismissLoading();
                                    toastNetError();
                                }
                            });
                }
                break;
        }
    }
}
