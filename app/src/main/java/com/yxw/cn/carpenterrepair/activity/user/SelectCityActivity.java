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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.LocationService;
import com.yxw.cn.carpenterrepair.adapter.CityAdapter;
import com.yxw.cn.carpenterrepair.adapter.HotCityAdapter;
import com.yxw.cn.carpenterrepair.entity.CityEntity;
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

    private LocationService mLocationService;


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
        mLocationService = new LocationService(this);
        mLocationService.registerListener(mLocationListener);
        mLocationService.start();
    }

    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (mTvCurrentCity!=null && bdLocation!=null && bdLocation.getCity()!=null){
                mTvCurrentCity.setText(bdLocation.getCity());
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        mLocationService.stop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationService.unregisterListener(mLocationListener);
    }

    public void initAdapter(){
        mCityAdapter = new CityAdapter(this);
        mIndexableLayout.setAdapter(mCityAdapter);
        mIndexableLayout.setOverlayStyle_Center();
        mCityAdapter.setDatas(initDatas());
//        indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);
        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        mIndexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
//        indexableLayout.addHeaderAdapter(new SimpleHeaderAdapter<>(mAdapter, "☆",null, null));
//         构造函数里3个参数,分别对应 (IndexBar的字母索引, IndexTitle, 数据源), 不想显示哪个就传null, 数据源传null时,代表add一个普通的View
//        mMenuHeaderAdapter = new MenuHeaderAdapter("↑", null, initMenuDatas());
//        indexableLayout.addHeaderAdapter(mMenuHeaderAdapter);
        // 这里BannerView只有一个Item, 添加一个长度为1的任意List作为第三个参数
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("↑", null, bannerList);
        mIndexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    public void setListener(){
        mCityAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityEntity entity) {
                if (originalPosition >= 0) {
                    Intent intent = new Intent();
                    intent.putExtra("city", entity.getName());
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

    private List<CityEntity> initDatas() {
        List<CityEntity> list = new ArrayList<>();
        // 初始化数据
        List<String> cityList = Arrays.asList(getResources().getStringArray(R.array.provinces));
        for (int i = 0; i < cityList.size(); i++) {
            CityEntity contactEntity = new CityEntity(cityList.get(i));
            list.add(contactEntity);
        }
        return list;
    }
}
