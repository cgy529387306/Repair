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

import com.lzy.okgo.OkGo;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.order.MyOrderActivity;
import com.yxw.cn.carpenterrepair.adapter.HomeMsgAdapter;
import com.yxw.cn.carpenterrepair.adapter.OrderTypeAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.OrderDetail;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.ImageUtils;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                if(CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getIdCardStatus()==3){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("type",getOrderTypeList().get(i));
                    startActivity(MyOrderActivity.class,bundle);
                }else{
                    toast("工程师身份审核未通过!");
                }
            }
        });
    }

    @Override
    public void getData() {
        super.getData();
        getLunboData();
    }

    private List<OrderType> getOrderTypeList(){
        List<OrderType> orderTypeList = new ArrayList<>();
        orderTypeList.add(new OrderType(0,R.drawable.icon_orders,"订单池"));
        orderTypeList.add(new OrderType(1,R.drawable.icon_order_waiting,"待预约"));
        orderTypeList.add(new OrderType(2,R.drawable.icon_order_coming,"待上门"));
        orderTypeList.add(new OrderType(3,R.drawable.icon_order_finishing,"待完成"));
        orderTypeList.add(new OrderType(4,R.drawable.icon_order_finished,"已完成"));
        return orderTypeList;
    }

    private List<String> getImageList(){
        List<String> dataLsit = new ArrayList<>();
        dataLsit.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1558272884&di=da63d7e99458ed81153d0181b4e9a163&src=http://pic40.photophoto.cn/20160731/1155116428420006_b.jpg");
        dataLsit.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558282603949&di=b7d884ed76be39e27be78642397757e2&imgtype=0&src=http%3A%2F%2Fwww.wkgogo.com%2Fuserfiles%2Fzzgg%2Fwk%2Ftask%2Fc%2F1%2F125%2F1%2F1.jpg");
        dataLsit.add("https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1558272590&di=d83d8fca1a0ff5cd246ff3a23e7b1170&src=http://imgmall.tg.com.cn/group2/M00/70/42/CgooeFn0g_j3ubxeAAj7CpF4_w8737.jpg");
        return dataLsit;
    }

    private void getLunboData(){
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", 1);
        map.put("pageSize", 10);
        OkGo.<ResponseData<Object>>post(UrlConstant.GET_LUNBO)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<Object>>() {
                    @Override
                    public void onSuccess(ResponseData<Object> response) {
                        if (response!=null){
                            if (response.isSuccess()){
                            }else{
                                toast(response.getMsg());
                            }
                        }
                    }
                });
    }


    @Override
    public void onRefresh() {
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onLoad() {
        mRefreshLayout.finishLoadMore(true);
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageUtils.loadImageUrl(imageView,((String)path));
        }
    }

}