package com.yxw.cn.carpenterrepair.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.order.MyOrderActivity;
import com.yxw.cn.carpenterrepair.adapter.HomeMsgAdapter;
import com.yxw.cn.carpenterrepair.adapter.OrderTypeAdapter;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.util.ImageUtils;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 首页
 * Created by cgy on 2018/11/25
 */
public class HomeFragment extends BaseRefreshFragment {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private GridView mGridCate;
    private Banner mBanner;

    private HomeMsgAdapter mAdapter;



    @Override
    protected int getLayout() {
        return R.layout.frg_home;
    }

    @Override
    protected void initView() {
        titlebar.setTitle("工作台");
        titlebar.setLeftVisible(false);
        titlebar.addAction(new TitleBar.ImageAction(R.drawable.icon_remind) {
            @Override
            public void performAction(View view) {

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.gray_divider)));
        mAdapter = new HomeMsgAdapter(getImageList());
        mRecyclerView.setAdapter(mAdapter);

        //添加Header
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_header, mRecyclerView, false);
        mGridCate = header.findViewById(R.id.gridCate);
        mBanner = header.findViewById(R.id.bannerView);
        mAdapter.addHeaderView(header);

        mGridCate.setAdapter(new OrderTypeAdapter(getActivity(),getOrderTypeList()));
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setImages(getImageList());
        mBanner.start();

        mGridCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("type",getOrderTypeList().get(i));
                startActivity(MyOrderActivity.class,bundle);
            }
        });
    }

    private List<OrderType> getOrderTypeList(){
        List<OrderType> orderTypeList = new ArrayList<>();
        orderTypeList.add(new OrderType(0,R.drawable.icon_orders,"订单池"));
        orderTypeList.add(new OrderType(1,R.drawable.icon_order_waiting,"待预约"));
        orderTypeList.add(new OrderType(2,R.drawable.icon_order_finishing,"待完成"));
        orderTypeList.add(new OrderType(3,R.drawable.icon_order_coming,"带上门"));
        orderTypeList.add(new OrderType(4,R.drawable.icon_order_finished,"已完成"));
        return orderTypeList;
    }

    private List<String> getImageList(){
        List<String> dataLsit = new ArrayList<>();
        dataLsit.add("http://pic.58pic.com/58pic/15/68/59/71X58PICNjx_1024.jpg");
        dataLsit.add("http://pic1.win4000.com/wallpaper/9/5450ae2fdef8a.jpg");
        dataLsit.add("http://pic15.nipic.com/20110628/1369025_192645024000_2.jpg");
        return dataLsit;
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageUtils.loadImageUrl(imageView,((String)path));
        }
    }

}