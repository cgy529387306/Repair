package com.yxw.cn.carpenterrepair.activity.order;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.OrderUploadAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.AliPayBiz;
import com.yxw.cn.carpenterrepair.entity.AliPayCheckInfo;
import com.yxw.cn.carpenterrepair.entity.CategorySubItem;
import com.yxw.cn.carpenterrepair.entity.Fee;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.Order;
import com.yxw.cn.carpenterrepair.entity.OrderUpload;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.WeChatPay;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import com.yxw.cn.carpenterrepair.listerner.OnChooseTimeListener;
import com.yxw.cn.carpenterrepair.activity.LocationActivity;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.util.DateUtil;
import com.yxw.cn.carpenterrepair.util.DoubleUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.TimePickerUtil;

/**
 * Created by CY on 2018/11/25
 */
public class OrderActivity extends BaseActivity implements OnChooseTimeListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_rule2)
    TextView tvRule2;
    @BindView(R.id.tv_rule3)
    TextView tvRule3;
    @BindView(R.id.tv_ps)
    TextView tvPs;
    @BindView(R.id.tv_repair)
    TextView tvRepair;
    @BindView(R.id.chooseTime)
    TextView chooseTime;
    @BindView(R.id.tv_door_fee)
    TextView tvDoorFee;
    @BindView(R.id.tv_repair_fee)
    TextView tvRepairFee;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_wait_pay)
    TextView tvWaitPay;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.cb_argee)
    CheckBox cbArgee;
    @BindView(R.id.rv_upload)
    RecyclerView mRv;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv1)
    TextView tv1;
    private List<OrderUpload> mList;
    private OrderUploadAdapter mAdapter;
    private ReverseGeoCodeResult result = null;
    private String addr1;
    private String addr2;
    private String bookingDate = null;
    private String bookingTime = null;
    private int uploadCout = 0;
    private ArrayList<String> urlList = new ArrayList<>();
    private CategorySubItem categorySubItem;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_order;
    }

    @Override
    public void initView() {
        titleBar.setTitle("服务详情");
        String str2 = "<font color='#000000'>2.</font> <font color='#FF3431'>请仔细对核对您填写的手机号，</font><font color='#000000'>并保持电话通畅，工程师会在服务开始前与此号码沟通服务具体事宜</font>";
        tvRule2.setText(Html.fromHtml(str2));
        String str3 = "<font color='#000000'>3.您的退款、赔偿处理均以线上交易的订单金额作为唯一有效凭证</font><font color='#FF3431'>《匠修平台争议处理规则》</font>";
        tvRule3.setText(Html.fromHtml(str3));
        categorySubItem = (CategorySubItem) getIntent().getSerializableExtra("data");
        tv1.setText(categorySubItem.getName());
        //0固定，1浮动
        if (categorySubItem.getType() == 0) {
            tvPs.setVisibility(View.GONE);
            tvRepairFee.setText("￥" + DoubleUtil.getTwoDecimal(categorySubItem.getPrice()));
        } else {
            tvRepair.setVisibility(View.GONE);
            tvRepairFee.setVisibility(View.GONE);
        }
        mList = new ArrayList<>();
        mList.add(new OrderUpload(2));
        mAdapter = new OrderUploadAdapter(mList);
        mAdapter.setOnItemChildClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRv.setLayoutManager(gridLayoutManager);
        mRv.setAdapter(mAdapter);
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCount.setText(editable.toString().length() + "/50");
            }
        });
    }

    @Override
    public void getData() {
        getFee(110101 + "");
    }

    public void getFee(String agencyId) {
        OkGo.<ResponseData<Fee>>post(UrlConstant.GET_SERVICE_FEE)
                .params("agencyId", agencyId)
                .execute(new JsonCallback<ResponseData<Fee>>() {
                    @Override
                    public void onSuccess(ResponseData<Fee> response) {
                        tvDoorFee.setText("￥" + DoubleUtil.getTwoDecimal(response.getData().getFee()));
                        if (categorySubItem.getType() == 0) {
                            tvTotal.setText("￥" + DoubleUtil.getTwoDecimal(response.getData().getFee() + categorySubItem.getPrice()));
                        } else {
                            tvTotal.setText("￥" + DoubleUtil.getTwoDecimal(response.getData().getFee()));
                        }
                        tvWaitPay.setText(tvTotal.getText().toString());
                    }
                });
    }

    @OnClick({R.id.ll_address, R.id.ll_choose_time, R.id.bt_order})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_address:
                startActivity(LocationActivity.class);
                break;
            case R.id.ll_choose_time:
                TimePickerUtil.showPicker(this, chooseTime, this);
                break;
            case R.id.bt_order:
                if (cbArgee.isChecked()) {
                    if (etName.getText().toString().trim().isEmpty() || etTel.getText().toString().trim().isEmpty()
                            || result == null || bookingDate == null) {
                        toast("填写内容不能为空！");
                    } else {
                        showLoading();
                        startOrder();
                    }
                } else {
                    toast("请先勾选协议！");
                }
                break;
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.LOCATION:
                Map<String, Object> map = (Map<String, Object>) event.getData();
                addr1 = (String) map.get("addr1");
                addr2 = (String) map.get("addr2");
                result = (ReverseGeoCodeResult) map.get("result");
                mTvAddress.setText(addr1 + addr2);
                getFee(result.getAdcode() + "");
                break;
        }
    }

    @Override
    public void getDateTime(String date, String time) {
        bookingDate = DateUtil.getChooseDate(date);
        bookingTime = time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.size() > 0) {
                        for (LocalMedia localMedia : selectList) {
                            mList.add(mList.size() - 1, new OrderUpload(localMedia.getCompressPath()));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_del:
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.rl_card_1:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(false)// 是否裁剪 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(11);
                break;
        }
    }

    /**
     * 上传图片完成，开始下单
     */
    private void startOrder() {
        uploadCout = 0;
        urlList.clear();
        if (mList.size() > 1) {
            for (int i = 0; i < mList.size() - 1; i++) {
                urlList.add(Base64Util.getBase64ImageStr(mList.get(i).getPath()));
            }
        }
        Order order = new Order();
        order.setName(etName.getText().toString().trim());
        order.setMobile(etTel.getText().toString().trim());
        order.setProvince(result.getAddressDetail().province);
        order.setCity(result.getAddressDetail().city);
        order.setDistrict(result.getAddressDetail().district);
        order.setAddress(addr2);
        order.setAgencyId(result.getAdcode() + "");
        order.setLocationLat(result.getLocation().latitude);
        order.setLocationLng(result.getLocation().longitude);
        order.setTotalPrice(tvTotal.getText().toString().substring(1, tvTotal.getText().length()));
        order.setBookingDate(bookingDate);
        order.setBookingTime(bookingTime);
        order.setCategoryId(categorySubItem.getId());
        order.setRemark(etRemark.getText().toString());
        order.setOrderPictures(urlList);
        Gson gson = new Gson();
        OkGo.<ResponseData<String>>post(UrlConstant.ORDER)
                .upJson(gson.toJson(order))
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(ResponseData<String> response) {
                        dismissLoading();
                        if (response.getCode() == 0) {
                            aliPay(response.getData());
                        } else {
                            toast(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<String>> response) {
                        dismissLoading();
                        toastNetError();
                    }
                });
    }

    public void aliPay(String info) {
        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    PayTask alipay = new PayTask(OrderActivity.this);
                    Map<String, String> result = alipay.payV2(info, true);
                    for (String infoStr :
                            info.split("&")) {
                        if (infoStr.startsWith("biz_content")) {
                            AliPayBiz aliPayBiz = gson.fromJson(URLDecoder.decode(infoStr, "UTF-8").split("=")[1], AliPayBiz.class);
                            checkAliPay(aliPayBiz.getOut_trade_no());
                        }
                    }
                } catch (Exception e) {
                    toast("订单支付解析失败");
                    e.printStackTrace();
                }
           /*     if ("9000".equals(result.get("resultStatus"))) {
                    toast("下单成功");
                    EventBusUtil.post(MessageConstant.NOTIFY_ORDER);
                    finish();
                } else {
                    toast(result.get("memo"));
                }*/
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void checkAliPay(String outTradeNo) {
        Map<String, String> map = new HashMap<>();
        map.put("outTradeNo", outTradeNo);
        OkGo.<ResponseData<AliPayCheckInfo>>post(UrlConstant.CHECK_ALI_PAY)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<AliPayCheckInfo>>() {
                    @Override
                    public void onSuccess(ResponseData<AliPayCheckInfo> response) {
                        if (response.getCode() == 0) {
                            if ("9000".equals(response.getData().getAlipay_trade_query_response().getCode())) {
                                toast("下单成功");
                                EventBusUtil.post(MessageConstant.NOTIFY_ORDER);
                                finish();
                            } else {
                                toast(response.getData().getAlipay_trade_query_response().getSub_msg());
                            }
                        } else {
                            toast(response.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<AliPayCheckInfo>> response) {
                        super.onError(response);
                        toastNetError();
                    }
                });
    }

    private void wechatPay(String orderId) {
        OkGo.<ResponseData<WeChatPay>>post(UrlConstant.WECHAT_PAY_ORDER)
                .params("orderId", orderId)
                .params("from", "APP")
                .execute(new JsonCallback<ResponseData<WeChatPay>>() {
                    @Override
                    public void onSuccess(ResponseData<WeChatPay> response) {
                        toast(response.getMsg());
                        dismissLoading();
                    }

                    @Override
                    public void onError(Response<ResponseData<WeChatPay>> response) {
                        super.onError(response);
                        dismissLoading();
                        toastNetError();
                    }
                });
    }
}
