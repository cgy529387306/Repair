package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.CityAdapter;
import com.yxw.cn.carpenterrepair.adapter.HotCityAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CityBean;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.QGridView;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * Created by Administrator on 2017/11/25.
 */

public class SelectCityActivity extends BaseActivity {
    private CityAdapter mCityAdapter;
    private BannerHeaderAdapter mBannerHeaderAdapter;
    private String[] mHotCity = {"福州","厦门","北京","上海","广州","深圳","杭州","苏州","武汉","长沙","重庆","南京"};
    private IndexableLayout mIndexableLayout;
    private HotCityAdapter mHotCityAdapter;
    private TextView mTvCurrentCity;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_select_city;
    }

    @Override
    public void initView() {
        TitleBar titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("常驻");
        mIndexableLayout =  findViewById(R.id.indexableLayout);
        mIndexableLayout.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        setListener();
    }

    @Override
    public void getData() {
        if (AppUtil.cityItemList != null && AppUtil.cityItemList.size() > 0) {
            initCityData();
        } else{
            showLoading();
            OkGo.<ResponseData<List<CityBean>>>post(UrlConstant.GET_ALL_REGION)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<CityBean>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<CityBean>> response) {
                            dismissLoading();
                            if (response!=null){
                                if (response.isSuccess() && response.getData()!=null){
                                    AppUtil.cityItemList = response.getData();
                                    initCityData();
                                }else{
                                    toast(response.getMsg());
                                }
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<List<CityBean>>> response) {
                            super.onError(response);
                            dismissLoading();
                        }
                    });
        }
    }

    private void initCityData(){
        mCityAdapter.setDatas(AppUtil.cityItemList);
    }

    public void initAdapter(){
        mCityAdapter = new CityAdapter(this);
        mIndexableLayout.setAdapter(mCityAdapter);
        mIndexableLayout.setOverlayStyle_Center();
        mIndexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("↑", null, bannerList);
        mIndexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    public void setListener(){
        mCityAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityBean>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityBean entity) {
                if (originalPosition >= 0) {
                    Intent intent = new Intent();
                    intent.putExtra("city", entity.getRegionName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter {
        private static final int TYPE = 1;

        public BannerHeaderAdapter(String index, String indexTitle, List datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(SelectCityActivity.this).inflate(R.layout.item_hot_city_header, parent, false);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            // 数据源为null时, 该方法不用实现
            final VH vh = (VH) holder;
            List dataList = Arrays.asList(mHotCity);
            mHotCityAdapter = new HotCityAdapter(SelectCityActivity.this, dataList);
            vh.hotCityGrid.setAdapter(mHotCityAdapter);
            vh.hotCityGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putExtra("city", (String) dataList.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            vh.tvCurrentCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("city", vh.tvCurrentCity.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

        }

        private class VH extends RecyclerView.ViewHolder {
            GridView hotCityGrid;
            TextView tvCurrentCity;
            public VH(View itemView) {
                super(itemView);
                hotCityGrid = (QGridView)itemView.findViewById(R.id.item_hot_city);
                mTvCurrentCity = tvCurrentCity = itemView.findViewById(R.id.tv_current_city);
            }
        }
    }
}
