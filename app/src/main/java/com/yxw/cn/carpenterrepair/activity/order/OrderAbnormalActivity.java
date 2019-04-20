package com.yxw.cn.carpenterrepair.activity.order;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.OrderAbnormalAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Abnormal;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
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
    @BindView(R.id.rv)
    RecyclerView mRv;

    private List<Abnormal> mList;
    private OrderAbnormalAdapter mAdapter;
    private int orderId;
    private String exceptionIds;


    @Override
    protected int getLayoutResId() {
        return R.layout.act_oder_abnormal;
    }

    @Override
    public void initView() {
        titleBar.setTitle("异常反馈");
        orderId = getIntent().getIntExtra("orderId", 0);
        mList = new ArrayList<>();
        mAdapter = new OrderAbnormalAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mRv.setLayoutManager(new GridLayoutManager(this, 2));
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void getData() {
        super.getData();
        HashMap<String, Object> map = new HashMap<>();
        map.put("mark", "TURN_RESERVATION_REASON");
        OkGo.<ResponseData<List<Abnormal>>>post(UrlConstant.ABNORMAL_TYPE)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<List<Abnormal>>>() {
                    @Override
                    public void onSuccess(ResponseData<List<Abnormal>> response) {
                        if (response.getCode() == 0) {
                            if (response.getData().size() > 0) {
                                mList.addAll(response.getData());
                                mAdapter.notifyDataSetChanged();
                                exceptionIds = response.getData().get(0).getValue();
                            }
                        } else {
                            ToastUtil.show(response.getMsg());
                        }
                    }
                });
    }

    @OnClick({R.id.rl_time, R.id.no, R.id.ok})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_time:
                TimePickerUtil.showYearPicker(this, new OnChooseDateListener() {
                    @Override
                    public void getDate(Date date) {
                        String ss = TimeUtil.dateToString(date, "yyyy-MM-dd HH:mm:00");
                        tv_time.setText(ss);
                    }
                });
                break;
            case R.id.ok:
                if (tv_time.getText().toString().isEmpty()) {
                    toast("请先选择再次预约时间！");
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderId", orderId);
                    map.put("bookingDate", tv_time.getText().toString().split(" ")[0]);
                    map.put("bookingTime", tv_time.getText().toString().split(" ")[1]);
                    map.put("exceptionIds", exceptionIds);
                    OkGo.<ResponseData<String>>post(UrlConstant.ABNORMAL_COMMIT)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                @Override
                                public void onSuccess(ResponseData<String> response) {
                                    if (response.getCode() == 0) {
                                        OrderAbnormalActivity.this.finish();
                                        EventBusUtil.post(MessageConstant.NOTIFY_ORDER_DETAIL);
                                    }
                                    ToastUtil.show(response.getMsg());
                                }
                            });
                }
                break;
            case R.id.no:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setSelect(position);
        exceptionIds = mList.get(position).getValue();
        mAdapter.notifyDataSetChanged();
    }
}
