package com.yxw.cn.carpenterrepair.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseRefreshFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.WorkerUnorderAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Abnormal;
import com.yxw.cn.carpenterrepair.entity.Category;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.QueryListByMark;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import listerner.OnChooseAddrListener;
import ui.WorkerOrderDetailActivity;
import util.EventBusUtil;
import util.RegionPickerUtil;
import util.SpaceItemDecoration;
import util.ToastUtil;

public class WorkerUnOrderFragment extends BaseRefreshFragment implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.typeTv)
    TextView typeTv;
    @BindView(R.id.areaTv)
    TextView areaTv;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.rl_area)
    RelativeLayout rlArea;

    //  类型
    ArrayList<String> typeList = new ArrayList<>();
    ArrayList<String> timeTypeList = new ArrayList<>();
    private List<UserOrder.ListBean> mList;
    private WorkerUnorderAdapter mAdapter;
    private OptionsPickerView pvCustomOptions;
    private int page = 2;
    private boolean isNext = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_worker_unorder;
    }

    @Override
    protected void initView() {
        typeTv.setTag("");
        mTvTime.setTag("");
        areaTv.setTag("");
        rlArea.setVisibility(View.GONE);
        mList = new ArrayList<>();
        mAdapter = new WorkerUnorderAdapter(mList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new SpaceItemDecoration(20));
        mRv.setAdapter(mAdapter);

    }

    @Override
    public void getData() {
        getOrderData(1);
    }

    @OnClick({R.id.rl_type, R.id.rl_area, R.id.rl_time})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_type:
                showPicker0();
                break;
            case R.id.rl_area:
                RegionPickerUtil.showAllPicker(getContext(), new OnChooseAddrListener() {
                    @Override
                    public void getAddr(int options1, int options2, int options3) {
                        if (options1 == 0) {
                            areaTv.setText("全部");
                            areaTv.setTag("");
                        } else {
                            areaTv.setText(RegionPickerUtil.getCity(options1 - 1, options2) + "-" + RegionPickerUtil.getDistrict(options1 - 1, options2, options3));
                            areaTv.setTag(AppUtil.regionTreeList.get(options1 - 1).getSub().get(options2).getSub().get(options3).getAgency_id());
                        }
                        getOrderData(1);
                    }
                });
                break;
            case R.id.rl_time:
                getTime();
                break;
        }
    }

    public void getTime() {
        if (AppUtil.timeList != null && AppUtil.timeList.size() > 0) {
            showTimePicker();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("mark", "ORDER_SEARCH_FRAME");
            OkGo.<ResponseData<List<QueryListByMark>>>post(UrlConstant.QUERT_LIST_BY_MARK)
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<List<QueryListByMark>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<QueryListByMark>> response) {
                            if (response.getData() != null && response.getData().size() > 0) {
                                AppUtil.timeList.clear();
                                AppUtil.timeList.addAll(response.getData());
                                showTimePicker();
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<List<QueryListByMark>>> response) {
                            super.onError(response);
                        }
                    });
        }
    }

    private void showPicker0() {
        if (AppUtil.categoryItemList0 != null && AppUtil.categoryItemList0.size() > 0) {
            showTypePicker0();
        } else {
            OkGo.<ResponseData<List<Abnormal>>>get(UrlConstant.SERVICE_TYPE)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<Abnormal>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<Abnormal>> response) {
                            if (response.getCode() == 0) {
                                AppUtil.categoryItemList0.clear();
                                AppUtil.categoryItemList0.addAll(response.getData());
                                showTypePicker0();
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<List<Abnormal>>> response) {
                            super.onError(response);
                            ToastUtil.show("网络异常");
                        }
                    });
        }
    }

    public void showTypePicker0() {
        typeList.clear();
        for (Abnormal category :
                AppUtil.categoryItemList0) {
            typeList.add(category.getName());
        }
        pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1 == 0) {
                    typeTv.setTag("");
                } else {
                    typeTv.setTag(AppUtil.categoryItemList0.get(options1).getValue());
                }
                typeTv.setText(typeList.get(options1));
                getOrderData(1);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("类型选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight(v.getContext());
                        bottomView.setLayoutParams(params);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();

        pvCustomOptions.setPicker(typeList);//添加数据源
        pvCustomOptions.show();
    }


    private void showPicker() {
        if (AppUtil.categoryItemList != null && AppUtil.categoryItemList.size() > 0) {
            showTypePicker();
        } else {
            OkGo.<ResponseData<List<Category>>>get(UrlConstant.GET_ALL_CATEGORY)
                    .tag(this)
                    .execute(new JsonCallback<ResponseData<List<Category>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<Category>> response) {
                            AppUtil.categoryItemList.clear();
                            AppUtil.categoryItemList.addAll(response.getData());
                            showTypePicker();
                        }

                        @Override
                        public void onError(Response<ResponseData<List<Category>>> response) {
                            super.onError(response);
                            ToastUtil.show("网络异常");
                        }
                    });
        }
    }

    public void showTypePicker() {
        typeList.clear();
        for (Category category :
                AppUtil.categoryItemList) {
            typeList.add(category.getName());
        }
        pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if (options1 == 0) {
                    typeTv.setTag("");
                } else {
                    typeTv.setTag(AppUtil.categoryItemList.get(options1).getId());
                }
                typeTv.setText(typeList.get(options1));
                getOrderData(1);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("类型选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight(v.getContext());
                        bottomView.setLayoutParams(params);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();

        pvCustomOptions.setPicker(typeList);//添加数据源
        pvCustomOptions.show();
    }

    public void showTimePicker() {
        timeTypeList.clear();
        for (QueryListByMark queryListByMark :
                AppUtil.timeList) {
            timeTypeList.add(queryListByMark.getName());
        }
        pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvTime.setText(timeTypeList.get(options1));
                mTvTime.setTag(AppUtil.timeList.get(options1).getValue());
                getOrderData(1);
            }
        }).setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择时间")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#FF3431"))//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setLayoutRes(R.layout.view_picker_options_type, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                        View bottomView = v.findViewById(R.id.view_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bottomView.getLayoutParams();
                        params.height = AppUtil.getNavigationBarHeight(v.getContext());
                        bottomView.setLayoutParams(params);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });
                    }
                }).build();

        pvCustomOptions.setPicker(timeTypeList);//添加数据源
        pvCustomOptions.show();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.recive_order:
                startActivity(WorkerOrderDetailActivity.class, mList.get(position).getId());
//                DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
//                        .setContentHolder(new ViewHolder(R.layout.dlg_confirm_order))
//                        .setGravity(Gravity.CENTER)
//                        .setCancelable(true)
//                        .setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(DialogPlus dialog, View view) {
//                                switch (view.getId()) {
//                                    case R.id.dialog_confirm:
//                                        dialog.dismiss();
//                                        if (mList.get(position).getReceiveStatus() == 0) {
//                                            Gson gson = new Gson();
//                                            HashMap<String, Integer> map = new HashMap<>();
//                                            map.put("orderId", mList.get(position).getId());
//                                            OkGo.<ResponseData<String>>post(UrlConstant.RECEIVE_ORDER)
//                                                    .upJson(gson.toJson(map))
//                                                    .execute(new JsonCallback<ResponseData<String>>() {
//                                                        @Override
//                                                        public void onSuccess(ResponseData<String> response) {
//                                                            ToastUtil.show(response.getMsg());
//                                                            if (response.getCode() == 0) {
//                                                                EventBusUtil.post(MessageConstant.NOTIFY_ORDER);
//                                                            }
//                                                        }
//                                                    });
//                                        }
//                                        break;
//                                    case R.id.dialog_cancel:
//                                        dialog.dismiss();
//                                        break;
//                                }
//                            }
//                        })
//                        .create();
//                dialogPlus.show();
                break;
        }
    }

    private void getOrderData(int p) {
        Map<String, Object> map = new HashMap<>();
        map.put("categoryId", typeTv.getTag().toString().equals("") ? null : typeTv.getTag().toString());
        map.put("agencyId", areaTv.getTag().toString().equals("") ? null : areaTv.getTag().toString());
        map.put("createTime", mTvTime.getTag().toString().equals("") ? null : mTvTime.getTag().toString());
        map.put("page", p);
        map.put("limit", loadCount);
        OkGo.<ResponseData<UserOrder>>post(UrlConstant.WORKER_WAIT_ORDER_LIST)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<UserOrder>>() {
                    @Override
                    public void onSuccess(ResponseData<UserOrder> response) {
                        if (response.getCode() == 0) {
                            if (p == 1) {
                                page = 2;
                                mList.clear();
                                mRefreshLayout.refreshComplete();
                            } else {
                                isNext = response.getData().isIsNext();
                                if (isNext) {
                                    page++;
                                    mRefreshLayout.loadMoreComplete();
                                } else {
                                    mRefreshLayout.loadNothing();
                                }
                            }
                            mList.addAll(response.getData().getList());
                            mAdapter.notifyDataSetChanged();
                            EventBusUtil.post(MessageConstant.WORKER_UNORDER_COUNT, mList.size());
                        } else {
                            if (p == 1) {
                                mRefreshLayout.refreshComplete();
                            } else {
                                mRefreshLayout.loadMoreFail();
                            }
                            toast(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<UserOrder>> response) {
                        super.onError(response);
                        if (p == 1) {
                            mRefreshLayout.refreshComplete();
                        } else {
                            mRefreshLayout.loadMoreFail();
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getOrderData(1);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getOrderData(page);

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(WorkerOrderDetailActivity.class, mList.get(position).getId());
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_ORDER:
                getData();
                break;
        }
    }
}
