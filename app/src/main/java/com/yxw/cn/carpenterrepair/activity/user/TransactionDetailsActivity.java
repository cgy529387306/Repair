package com.yxw.cn.carpenterrepair.activity.user;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.TransactionDetailsAdapter;
import com.yxw.cn.carpenterrepair.view.RecycleViewDivider;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 交易明细
 */
public class TransactionDetailsActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    TransactionDetailsAdapter mTransactionDetailsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_transaction_details;
    }

    @Override
    public void initView() {
        titleBar.setTitle("交易明细");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecycleViewDivider(LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.gray_divider)));
        mTransactionDetailsAdapter = new TransactionDetailsAdapter(getList());
        mRecyclerView.setAdapter(mTransactionDetailsAdapter);

    }
    private List<String> getList(){
        List<String> dataLsit = new ArrayList<>();
        dataLsit.add("支付宝提现");
        dataLsit.add("微信提现");
        return dataLsit;
    }

}
