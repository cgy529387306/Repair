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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.MsgDetailActivity;
import com.yxw.cn.carpenterrepair.activity.MsgListActivity;
import com.yxw.cn.carpenterrepair.activity.order.MyOrderActivity;
import com.yxw.cn.carpenterrepair.activity.order.MyOrderFinishActivity;
import com.yxw.cn.carpenterrepair.activity.user.IdCardInfoActivity;
import com.yxw.cn.carpenterrepair.adapter.HomeMsgAdapter;
import com.yxw.cn.carpenterrepair.adapter.OrderTypeAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.BannerBean;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.NoticeListData;
import com.yxw.cn.carpenterrepair.entity.OrderType;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.ImageUtils;
import com.yxw.cn.carpenterrepair.util.LocationUtils;
import com.yxw.cn.carpenterrepair.util.PreferencesHelper;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 首页
 * Created by cgy on 2018/11/25
 */
public class HomeFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_msg)
    ImageView mIvMsg;

    private GridView mGridCate;
    private Banner mBanner;
    private Badge mMsgBadge;

    private HomeMsgAdapter mAdapter;
    private int mPage = 2;
    private boolean isNext = false;


    @Override
    protected int getLayout() {
        return R.layout.frg_home;
    }

    @Override
    protected void initView() {
        mMsgBadge = new QBadgeView(mContext).bindTarget(mIvMsg);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.gray_divider)));
        mAdapter = new HomeMsgAdapter(new ArrayList());
        mRecyclerView.setAdapter(mAdapter);

        //添加Header
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_header, mRecyclerView, false);
        mGridCate = header.findViewById(R.id.gridCate);
        mBanner = header.findViewById(R.id.bannerView);
        mAdapter.addHeaderView(header);

        mGridCate.setAdapter(new OrderTypeAdapter(getActivity(),getOrderTypeList()));
        mAdapter.setOnItemClickListener(this);
        mGridCate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getIdCardStatus()==3){
                    if (i == 4) {
                        startActivity(MyOrderFinishActivity.class);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("type",getOrderTypeList().get(i));
                        startActivity(MyOrderActivity.class,bundle);
                    }
                }else if(CurrentUser.getInstance().isLogin() && CurrentUser.getInstance().getIdCardStatus()==0){
                    toast("工程师身份信息未提交!");
                    startActivity(IdCardInfoActivity.class);
                }else{
                    toast("工程师身份审核未通过!");
                }
            }
        });
    }

    @OnClick({R.id.iv_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_msg:
                startActivity(MsgListActivity.class);
                break;
            default:
                break;
        }
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

    @Override
    public void getData() {
        super.getData();
        getLunboData();
        getNoticeData(1);
        LocationUtils.instance().startLocation();
        showLoading();
    }

    private void getLunboData(){
        Map<String, Object> map = new HashMap<>();
        map.put("place", 1);//图片所属位置0：用户端 1：工程师端 2：服务商端，例:0、1、2
        map.put("site", 2);//图片位置1：启动页 2：工作台轮播
        OkGo.<ResponseData<List<BannerBean>>>post(UrlConstant.GET_LUNBO)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<List<BannerBean>>>() {
                    @Override
                    public void onSuccess(ResponseData<List<BannerBean>> response) {
                        dismissLoading();
                        if (response!=null){
                            if (response.isSuccess() && mBanner!=null && response.getData()!=null){
                                mBanner.setImageLoader(new GlideImageLoader());
                                mBanner.setImages(response.getData());
                                mBanner.start();
                            }else{
                                toast(response.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<List<BannerBean>>> response) {
                        super.onError(response);
                        dismissLoading();
                    }
                });
    }

    private void getNoticeData(int p){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("site",1);
        Map<String, Object> map = new HashMap<>();
        map.put("filter", requestMap);
        map.put("pageIndex", p);
        map.put("pageSize", loadCount);
        OkGo.<ResponseData<NoticeListData>>post(UrlConstant.GET_NOTICE)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<NoticeListData>>() {
                    @Override
                    public void onSuccess(ResponseData<NoticeListData> response) {
                        dismissLoading();
                        if (response!=null){
                            initMsgBadge(response);
                            if (response.isSuccess() && response.getData()!=null) {
                                isNext = response.getData().isHasNext();
                                if (p == 1) {
                                    mAdapter.setNewData(response.getData().getItems());
                                    mRefreshLayout.finishRefresh();
                                    if (isNext){
                                        mPage = 2;
                                    }else{
                                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                } else {
                                    mAdapter.addData(response.getData().getItems());
                                    if (isNext) {
                                        mPage++;
                                        mRefreshLayout.finishLoadMore();
                                    } else {
                                        mRefreshLayout.finishLoadMoreWithNoMoreData();
                                    }
                                }
                            } else {
                                toast(response.getMsg());
                                if (p == 1) {
                                    mRefreshLayout.finishRefresh(false);
                                } else {
                                    mRefreshLayout.finishLoadMore(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<NoticeListData>> response) {
                        super.onError(response);
                        dismissLoading();
                        if (p == 1) {
                            mRefreshLayout.finishRefresh(false);
                        } else {
                            mRefreshLayout.finishLoadMore(false);
                        }
                    }
                });
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        getNoticeData(1);
        LocationUtils.instance().startLocation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getNoticeData(mPage);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (mAdapter.getItem(position)!=null){
            startActivity(MsgDetailActivity.class, mAdapter.getData().get(position));
        }
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageUtils.loadImageUrl(imageView,((BannerBean)path).getPath());
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.MY_LOCATION:
                String city = PreferencesHelper.getInstance().getString("city","");
                if (Helper.isEmpty(city)||city.contains("null")){
                    mTvLocation.setVisibility(View.GONE);
                }else{
                    mTvLocation.setVisibility(View.VISIBLE);
                    mTvLocation.setText(city);
                }
                break;
            case MessageConstant.GET_MSG_COUNT:
                onRefresh();
                break;
        }
    }

    private void initMsgBadge(ResponseData responseData){
        int msgCount = responseData.getDicts();
        if (msgCount==0){
            mMsgBadge.hide(false);
        }else{
            mMsgBadge.setBadgeNumber(msgCount);
        }
    }

}