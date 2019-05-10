package com.yxw.cn.carpenterrepair.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.ll_withdrawal_cash)
    LinearLayout mLlWithdrawalCash;
    @BindView(R.id.tv_carry_amount)
    TextView mTvCarryAmount;
    @BindView(R.id.tv_deposit)
    TextView mTvDeposit;
    @BindView(R.id.tv_settlement_amount)
    TextView mTvSettlementAmount;
    @BindView(R.id.ll_alipay)
    LinearLayout mLlAlipay;
    @BindView(R.id.tv_alipay)
    TextView mTvAlipay;
    @BindView(R.id.ll_weixin)
    LinearLayout mLlWeiXin;
    @BindView(R.id.tv_weixin)
    TextView mTvWeiXin;
    @BindView(R.id.ll_card)
    LinearLayout mLlCard;
    @BindView(R.id.tv_card)
    TextView mTvCard;
    @BindView(R.id.ll_transaction_details)
    LinearLayout mLlTransactionDetails;

    private DialogPlus dialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_wallet;
    }

    @Override
    public void initView() {
        titleBar.setTitle(getIntent().getStringExtra("title"));
        mTvCarryAmount.setText(getIntent().getStringExtra("carryAmount"));
        mTvDeposit.setText(getIntent().getStringExtra("deposit"));
        mTvSettlementAmount.setText(getIntent().getStringExtra("settlementAmount"));
    }

    @Override
    public void initData() {
        super.initData();
        mTvAlipay.setText(Helper.isEmpty(CurrentUser.getInstance().getAliplayAccount())?"未绑定":CurrentUser.getInstance().getAliplayAccount());
    }

    @OnClick({R.id.ll_alipay,R.id.ll_withdrawal_cash,R.id.ll_transaction_details})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_alipay:
                startActivity(UpdateAlipayActivity.class);
                break;
            case R.id.ll_withdrawal_cash:
                Bundle bundle = new Bundle();
                bundle.putString("data",mTvCarryAmount.getText().toString());
                startActivity(WithdrawalCashActivity.class,bundle);
                break;
            case R.id.ll_transaction_details:
                startActivity(TransactionDetailsActivity.class);
                break;
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
//                break;
//            case R.id.btn_withdrawal_now:
//                if (Double.parseDouble(cash1.getText().toString().trim()) == 0.0) {
//                    toast("提现金额不能为零！");
//                } else {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("deposit", Double.parseDouble(cash1.getText().toString()));
//                    map.put("payWay", "aliplay");
//                    OkGo.<ResponseData<WithdrawalCash>>post(UrlConstant.WORKER_DEPOSIT)
//                            .upJson(gson.toJson(map))
//                            .execute(new JsonCallback<ResponseData<WithdrawalCash>>() {
//                                         @Override
//                                         public void onSuccess(ResponseData<WithdrawalCash> response) {
//                                             if (response.getCode() == 0) {
//                                                 if(response.getData().isIsExist()){
//                                                     EventBusUtil.post(MessageConstant.NOTIFY_CARRY_AMONUT);
//                                                     WalletActivity.this.finish();
//                                                 }else {
//                                                     startActivity(UpdateAlipayActivity.class);
//                                                 }
//                                             }
//                                             toast(response.getMsg());
//                                         }
//                                     }
//                            );
//                }
//                break;
//            case R.id.all:
//                cash1.setText(cash2.getText().toString());
//                break;
        }
    }

}
