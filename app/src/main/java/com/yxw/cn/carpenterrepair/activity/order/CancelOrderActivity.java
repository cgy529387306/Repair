package com.yxw.cn.carpenterrepair.activity.order;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.OrderAbnormalAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ReasonBean;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 取消订单
 */
public class CancelOrderActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.rv_reason)
    RecyclerView mRvReason;
    @BindView(R.id.et_remark)
    EditText etRemark;

    private OrderAbnormalAdapter mAdapter;
    private String acceptId;
    private String exceptionIds;


    @Override
    protected int getLayoutResId() {
        return R.layout.act_cancel_order;
    }

    @Override
    public void initView() {
        titleBar.setTitle("取消订单");
        acceptId = getIntent().getStringExtra("acceptId");
        mAdapter = new OrderAbnormalAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRvReason.setLayoutManager(new GridLayoutManager(this, 2));
        mRvReason.setAdapter(mAdapter);
    }

    @Override
    public void getData() {
        getReturnData();
    }

    private void getReturnData(){
        if (AppUtil.returnReasonList != null && AppUtil.returnReasonList.size() > 0) {
            exceptionIds = AppUtil.returnReasonList.get(0).getDictId();
            mAdapter.setNewData(AppUtil.returnReasonList);
        } else{
            showLoading();
            HashMap<String,String> map = new HashMap<>();
            map.put("dictKey","TURN_CANCELORDR_REASON");
            OkGo.<ResponseData<List<ReasonBean>>>post(UrlConstant.GET_EXCEPTION_REASON)
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<List<ReasonBean>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<ReasonBean>> response) {
                            dismissLoading();
                            if (response!=null){
                                if (response.isSuccess() && response.getData()!=null){
                                    if (AppUtil.returnReasonList != null && AppUtil.returnReasonList.size() > 0) {
                                        exceptionIds = AppUtil.returnReasonList.get(0).getDictId();
                                        mAdapter.setNewData(AppUtil.returnReasonList);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<List<ReasonBean>>> response) {
                            super.onError(response);
                            dismissLoading();
                        }
                    });
        }
    }


    @OnClick({R.id.cancel, R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                String desc = etRemark.getText().toString().trim();
                if (Helper.isEmpty(exceptionIds)) {
                    toast("请先选择取消订单原因");
                } else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("acceptId", acceptId);
                    map.put("ids", exceptionIds);
                    if (Helper.isNotEmpty(desc)){
                        map.put("fixDesc", desc);
                    }
                    OkGo.<ResponseData<Object>>post(UrlConstant.ORDER_RETURN)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<Object>>() {
                                @Override
                                public void onSuccess(ResponseData<Object> response) {
                                    dismissLoading();
                                    if (response!=null){
                                        if (response.isSuccess()) {
                                            toast("取消成功");
                                            CancelOrderActivity.this.finish();
                                            EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
                                        }else{
                                            toast(response.getMsg());
                                        }
                                    }
                                }

                                @Override
                                public void onError(Response<ResponseData<Object>> response) {
                                    super.onError(response);
                                    dismissLoading();
                                }
                            });
                }
                break;
            case R.id.cancel:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setSelect(position);
        mAdapter.notifyDataSetChanged();
        if (Helper.isNotEmpty(mAdapter.getData()) && mAdapter.getData().size()>position){
            exceptionIds = mAdapter.getData().get(position).getDictId();
        }
    }
}
