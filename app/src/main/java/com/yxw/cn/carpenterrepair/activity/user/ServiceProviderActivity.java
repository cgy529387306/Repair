package com.yxw.cn.carpenterrepair.activity.user;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.ServiceProdiverAdapter;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 隶属服务商
 */
public class ServiceProviderActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    CircleImageView  mIvAvatar;
    TextView mTvServiceId;
    ServiceProdiverAdapter mServiceProdiverAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_service_provider;
    }

    @Override
    public void initView() {
        titleBar.setTitle("隶属服务商");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.gray_divider)));
        mServiceProdiverAdapter = new ServiceProdiverAdapter(getList());
        mRecyclerView.setAdapter(mServiceProdiverAdapter);
        //添加Header
        View header = LayoutInflater.from(this).inflate(R.layout.item_service_provider_head, mRecyclerView, false);
        mIvAvatar = header.findViewById(R.id.iv_avatar);
        mTvServiceId = header.findViewById(R.id.tv_service_id);
        mServiceProdiverAdapter.addHeaderView(header);

    }
    private List<String> getList(){
        List<String> dataLsit = new ArrayList<>();
        dataLsit.add("陈秋梅");
        dataLsit.add("蔡桂有");
        return dataLsit;
    }

    @OnClick({})
    public void click(View view) {

    }
}
