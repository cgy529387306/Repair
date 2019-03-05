package ui.user;

import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.UserOrderDetailAdapter;
import com.yxw.cn.carpenterrepair.adapter.UserOrderPicAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.UserOrder;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ui.LocationActivity;
import util.DateUtil;
import util.DoubleUtil;

/**
 * Created by CY on 2018/11/25
 */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_rule2)
    TextView tvRule2;
    @BindView(R.id.tv_rule3)
    TextView tvRule3;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_wait_pay)
    TextView tvWaitPay;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.tv_addr)
    TextView tvAddr;
    @BindView(R.id.chooseTime)
    TextView chooseTime;
    @BindView(R.id.rv_order_detail)
    RecyclerView orderRv;
    @BindView(R.id.rv_pic)
    RecyclerView picRv;

    private List<UserOrder.ListBean.OrderItemsBean> orderList;
    private UserOrderDetailAdapter orderAdapter;
    private List<UserOrder.ListBean.PicListBean> picList;
    private UserOrderPicAdapter picAdapter;

    private OptionsPickerView pvCustomOptions;
    private List<String> weekList = new ArrayList<>();
    private List<String> timeList = new ArrayList<>();

    private int orderId;
    private int payStatus;//付款状态 0未付款 1已付款 2未全部付款

    @Override
    protected int getLayoutResId() {
        return R.layout.act_service_detail;
    }

    @Override
    public void initView() {
        titleBar.setTitle("订单详情");
        String str2 = "<font color='#000000'>2.</font> <font color='#FF3431'>请仔细对核对您填写的手机号，</font><font color='#000000'>并保持电话通畅，工程师会在服务开始前与此号码沟通服务具体事宜</font>";
        tvRule2.setText(Html.fromHtml(str2));
        String str3 = "<font color='#000000'>3.您的退款、赔偿处理均以线上交易的订单金额作为唯一有效凭证</font><font color='#FF3431'>《匠修平台争议处理规则》</font>";
        tvRule3.setText(Html.fromHtml(str3));
        orderId = getIntent().getIntExtra("orderId", 0);
        orderList = new ArrayList<>();
        orderAdapter = new UserOrderDetailAdapter(orderList);
        orderRv.setLayoutManager(new LinearLayoutManager(this));
        orderRv.setAdapter(orderAdapter);
        picList = new ArrayList<>();
        picAdapter = new UserOrderPicAdapter(picList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        picRv.setLayoutManager(gridLayoutManager);
        picRv.setAdapter(picAdapter);
    }

    @Override
    public void getData() {
        Gson gson = new Gson();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("orderId", orderId);
        OkGo.<ResponseData<UserOrder.ListBean>>post(UrlConstant.ORDER_VIEW)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<UserOrder.ListBean>>() {
                    @Override
                    public void onSuccess(ResponseData<UserOrder.ListBean> response) {
                        orderList.clear();
                        orderList.addAll(response.getData().getOrderItems());
                        orderAdapter.notifyDataSetChanged();
                        picList.clear();
                        picList.addAll(response.getData().getPicList());
                        picAdapter.notifyDataSetChanged();
                        tvTotal.setText("￥"+DoubleUtil.getTwoDecimal(response.getData().getTotalPrice()));
                        tvWaitPay.setText(tvTotal.getText().toString());
                        etRemark.setText(response.getData().getRemark());
                        etName.setText(response.getData().getName());
                        etTel.setText(response.getData().getMobile());
                        tvAddr.setText(response.getData().getAddress());
                        chooseTime.setText(response.getData().getBookingDate() + " " + response.getData().getBookingTime());
                        payStatus = response.getData().getPayStatus();
                    }
                });
    }

    @OnClick({R.id.ll_address, R.id.ll_choose_time})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                startActivity(LocationActivity.class);
                break;
            case R.id.ll_choose_time:
                getPickerData();
                pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        chooseTime.setText(weekList.get(options1) + " " + timeList.get(options2));
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
                        .setLayoutRes(R.layout.view_picker_options_date, new CustomListener() {
                            @Override
                            public void customLayout(View v) {
                                final TextView tvSubmit = v.findViewById(R.id.tv_confirm);
                                final TextView tvCancel = v.findViewById(R.id.tv_cancel);
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
                                tvCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        pvCustomOptions.dismiss();
                                    }
                                });
                            }
                        }).build();
                pvCustomOptions.setNPicker(weekList, timeList, null);//添加数据源
                pvCustomOptions.show();
                break;
        }
    }

    private void getPickerData() {
        weekList.clear();
        timeList.clear();
        weekList.addAll(DateUtil.getdateAndweeks(2));
        timeList.add("00:00");
        timeList.add("01:00");
        timeList.add("02:00");
        timeList.add("03:00");
        timeList.add("04:00");
        timeList.add("05:00");
        timeList.add("06:00");
        timeList.add("07:00");
        timeList.add("08:00");
        timeList.add("09:00");
        timeList.add("10:00");
        timeList.add("11:00");
        timeList.add("12:00");
        timeList.add("13:00");
        timeList.add("14:00");
        timeList.add("15:00");
        timeList.add("16:00");
        timeList.add("17:00");
        timeList.add("18:00");
        timeList.add("19:00");
        timeList.add("20:00");
        timeList.add("21:00");
        timeList.add("22:00");
        timeList.add("23:00");
    }
}
