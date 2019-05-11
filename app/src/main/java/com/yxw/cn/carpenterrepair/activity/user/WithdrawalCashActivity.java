package com.yxw.cn.carpenterrepair.activity.user;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.pop.TradeSuccessPop;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现
 */
public class WithdrawalCashActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.cash1)
    EditText cash1;
    @BindView(R.id.cash2)
    TextView cash2;

    private TradeSuccessPop mTradeSuccessPop;
    private String carryAmount;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_withdrawal_cash;
    }

    @Override
    public void initView() {
        titleBar.setTitle("提现");
        carryAmount = CurrentUser.getInstance().isLogin()?CurrentUser.getInstance().getCarryAmount():"0.0";
        cash1.setText(carryAmount);
        cash1.setSelection(cash1.getText().toString().length());
        cash2.setText(carryAmount);
        mTradeSuccessPop = new TradeSuccessPop(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTradeSuccessPop.dismiss();
                finish();
            }
        });
    }

    @OnClick({R.id.rl_type, R.id.btn_withdrawal_now, R.id.all})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_type:
//                if (dialog == null) {
//                    dialog = DialogPlus.newDialog(this)
//                            .setContentHolder(new ViewHolder(R.layout.dlg_withdrawal))
//                            .setGravity(Gravity.BOTTOM)
//                            .setCancelable(true)
//                            .create();
//                    View dialogView = dialog.getHolderView();
//                    ImageView ivSelZhifubao = dialogView.findViewById(R.id.iv_selected_zhifubao);
//                    ImageView ivSelWechat = dialogView.findViewById(R.id.iv_selected_wechat);
//                    ImageView ivSelBank = dialogView.findViewById(R.id.iv_selected_bank);
//                    dialogView.findViewById(R.id.ll_zhifubao).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ivSelZhifubao.setVisibility(View.VISIBLE);
//                            ivSelWechat.setVisibility(View.GONE);
//                            ivSelBank.setVisibility(View.GONE);
//                            tvType.setText("支付宝");
//                            dialog.dismiss();
//                        }
//                    });
//                    dialogView.findViewById(R.id.ll_wechat).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ivSelZhifubao.setVisibility(View.GONE);
//                            ivSelWechat.setVisibility(View.VISIBLE);
//                            ivSelBank.setVisibility(View.GONE);
//                            tvType.setText("微信");
//                            dialog.dismiss();
//                        }
//                    });
//                    dialogView.findViewById(R.id.ll_bank).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            ivSelZhifubao.setVisibility(View.GONE);
//                            ivSelWechat.setVisibility(View.GONE);
//                            ivSelBank.setVisibility(View.VISIBLE);
//                            tvType.setText("储蓄卡");
//                            dialog.dismiss();
//                        }
//                    });
//                }
//                dialog.show();
                break;
            case R.id.btn_withdrawal_now:
                if (Double.parseDouble(cash1.getText().toString().trim()) == 0.0) {
                    toast("提现金额不能为零！");
                } else {
                    showLoading();
                    Map<String, Object> map = new HashMap<>();
                    map.put("amount", Double.parseDouble(cash1.getText().toString()));
                    map.put("tradeWay", 0);//交易方式 0支付宝 1微信 2银行卡
                    OkGo.<ResponseData<String>>post(UrlConstant.APPLY_WITHDRAWAL)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                         @Override
                                         public void onSuccess(ResponseData<String> response) {
                                             dismissLoading();
                                             if (response!=null){
                                                 if (response.isSuccess()) {
                                                     EventBusUtil.post(MessageConstant.NOTIFY_GET_INFO);
                                                     mTradeSuccessPop.showPopupWindow(cash1);
                                                 }else{
                                                     toast(response.getMsg());
                                                 }
                                             }
                                         }

                                         @Override
                                         public void onError(Response<ResponseData<String>> response) {
                                             super.onError(response);
                                             dismissLoading();
                                         }
                                     }
                            );
                }
                break;
            case R.id.all:
                cash1.setText(carryAmount);
                cash1.setSelection(cash1.getText().toString().length());
                break;
        }
    }

}
