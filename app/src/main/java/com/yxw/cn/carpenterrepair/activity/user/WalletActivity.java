package com.yxw.cn.carpenterrepair.activity.user;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
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

    private LoginInfo loginInfo;
    @Override
    protected int getLayoutResId() {
        return R.layout.act_wallet;
    }

    @Override
    public void initView() {
        titleBar.setTitle(getIntent().getStringExtra("title"));
    }

    @Override
    public void initData() {
        if (CurrentUser.getInstance().isLogin()) {
            try {
                loginInfo = CurrentUser.getInstance();
                mTvAlipay.setText(Helper.isEmpty(loginInfo.getAliplayAccount())?"未绑定":loginInfo.getAliplayAccount());
                mTvCarryAmount.setText(loginInfo.getCarryAmount());
                mTvDeposit.setText(loginInfo.getDeposit());
                mTvSettlementAmount.setText(loginInfo.getSettlementAmount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginInfo = new LoginInfo();
        }
    }

    @OnClick({R.id.ll_alipay,R.id.ll_withdrawal_cash,R.id.ll_transaction_details})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_alipay:
                startActivity(UpdateAlipayActivity.class);
                break;
            case R.id.ll_withdrawal_cash:
                startActivity(WithdrawalCashActivity.class);
                break;
            case R.id.ll_transaction_details:
                startActivity(TransactionDetailsActivity.class);
                break;
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_INFO:
                initData();
                break;
        }
    }

}
