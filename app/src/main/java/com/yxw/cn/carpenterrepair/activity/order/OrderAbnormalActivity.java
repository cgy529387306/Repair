package com.yxw.cn.carpenterrepair.activity.order;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.OrderAbnormalAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Abnormal;
import com.yxw.cn.carpenterrepair.entity.CityBean;
import com.yxw.cn.carpenterrepair.entity.ReasonBean;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.yxw.cn.carpenterrepair.listerner.OnChooseDateListener;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.TimePickerUtil;
import com.yxw.cn.carpenterrepair.util.TimeUtil;
import com.yxw.cn.carpenterrepair.util.ToastUtil;

/**
 * 异常反馈
 */
public class OrderAbnormalActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.rv_reason)
    RecyclerView mRvReason;

    private OrderAbnormalAdapter mAdapter;
    private int orderId;
    private String exceptionIds;

    private int type;

    private String startTime;
    private String endTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_oder_abnormal;
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra("type",0);
        titleBar.setTitle(type==0?"预约异常反馈":"签到异常反馈");
        orderId = getIntent().getIntExtra("orderId", 0);
        mAdapter = new OrderAbnormalAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRvReason.setLayoutManager(new GridLayoutManager(this, 2));
        mRvReason.setAdapter(mAdapter);
    }

    @Override
    public void getData() {
        if (type == 0){
            getReservationReasonData();
        }else{
            getSignReasonData();
        }
    }

    private void getSignReasonData(){
        if (AppUtil.signReasonList != null && AppUtil.signReasonList.size() > 0) {
            mAdapter.setNewData(AppUtil.signReasonList);
        } else{
            showLoading();
            HashMap<String,String> map = new HashMap<>();
            map.put("dictKey","SIGN_IN_EXCEPTION");
            OkGo.<ResponseData<List<ReasonBean>>>post(UrlConstant.GET_EXCEPTION_REASON)
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<List<ReasonBean>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<ReasonBean>> response) {
                            dismissLoading();
                            if (response!=null){
                                if (response.isSuccess() && response.getData()!=null){
                                    AppUtil.signReasonList = response.getData();
                                    mAdapter.setNewData(AppUtil.signReasonList);
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

    private void getReservationReasonData(){
        if (AppUtil.reservationReasonList != null && AppUtil.reservationReasonList.size() > 0) {
            mAdapter.setNewData(AppUtil.reservationReasonList);
        } else{
            showLoading();
            HashMap<String,String> map = new HashMap<>();
            map.put("dictKey","TURN_RESERVATION_REASON");
            OkGo.<ResponseData<List<ReasonBean>>>post(UrlConstant.GET_EXCEPTION_REASON)
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<List<ReasonBean>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<ReasonBean>> response) {
                            dismissLoading();
                            if (response!=null){
                                if (response.isSuccess() && response.getData()!=null){
                                    AppUtil.reservationReasonList = response.getData();
                                    mAdapter.setNewData(AppUtil.reservationReasonList);
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


    @OnClick({R.id.rl_time, R.id.cancel, R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_time:
                TimePickerUtil.showYearPicker(this, new OnChooseDateListener() {
                    @Override
                    public void getDate(Date date) {
                        startTime = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
                        endTime = TimeUtil.getAfterHourTime(date);
                        tv_time.setText(startTime);
                    }
                });
                break;
            case R.id.confirm:
                if (Helper.isEmpty(startTime)) {
                    toast("请先选择再次预约时间！");
                } else if (Helper.isEmpty(exceptionIds)) {
                    toast("请先选择异常原因");
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderId", orderId);
                    map.put("bookingStartTime", startTime);
                    map.put("bookingEndTime", endTime);
                    map.put("exceptionIds", exceptionIds);
                    String requestUrl = type==0?UrlConstant.ORDER_EXEPTION_APPOINT:UrlConstant.ORDER_EXEPTION_SIGN;
                    OkGo.<ResponseData<String>>post(requestUrl)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                @Override
                                public void onSuccess(ResponseData<String> response) {
                                    if (response.isSuccess()) {
                                        OrderAbnormalActivity.this.finish();
                                        EventBusUtil.post(MessageConstant.NOTIFY_ORDER_DETAIL);
                                    }
                                    ToastUtil.show(response.getMsg());
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
