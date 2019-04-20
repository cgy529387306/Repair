package com.yxw.cn.carpenterrepair.activity.user;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.WithdrawalCash;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;

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

    private DialogPlus dialog;
    private String carryAmount;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_withdrawal_cash;
    }

    @Override
    public void initView() {
        titleBar.setTitle("提现");
        carryAmount = getIntent().getStringExtra("data");
        cash1.setText(carryAmount);
        cash2.setText(carryAmount);
    }

    @OnClick({R.id.rl_type, R.id.btn_withdrawal_now, R.id.all})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_type:
                if (dialog == null) {
                    dialog = DialogPlus.newDialog(this)
                            .setContentHolder(new ViewHolder(R.layout.dlg_withdrawal))
                            .setGravity(Gravity.BOTTOM)
                            .setCancelable(true)
                            .create();
                    View dialogView = dialog.getHolderView();
                    ImageView ivSelZhifubao = dialogView.findViewById(R.id.iv_selected_zhifubao);
                    ImageView ivSelWechat = dialogView.findViewById(R.id.iv_selected_wechat);
                    ImageView ivSelBank = dialogView.findViewById(R.id.iv_selected_bank);
                    dialogView.findViewById(R.id.ll_zhifubao).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivSelZhifubao.setVisibility(View.VISIBLE);
                            ivSelWechat.setVisibility(View.GONE);
                            ivSelBank.setVisibility(View.GONE);
                            tvType.setText("支付宝");
                            dialog.dismiss();
                        }
                    });
                    dialogView.findViewById(R.id.ll_wechat).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivSelZhifubao.setVisibility(View.GONE);
                            ivSelWechat.setVisibility(View.VISIBLE);
                            ivSelBank.setVisibility(View.GONE);
                            tvType.setText("微信");
                            dialog.dismiss();
                        }
                    });
                    dialogView.findViewById(R.id.ll_bank).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ivSelZhifubao.setVisibility(View.GONE);
                            ivSelWechat.setVisibility(View.GONE);
                            ivSelBank.setVisibility(View.VISIBLE);
                            tvType.setText("储蓄卡");
                            dialog.dismiss();
                        }
                    });
                }
                dialog.show();
                break;
            case R.id.btn_withdrawal_now:
                if (Double.parseDouble(cash1.getText().toString().trim()) == 0.0) {
                    toast("提现金额不能为零！");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("deposit", Double.parseDouble(cash1.getText().toString()));
                    map.put("payWay", "aliplay");
                    OkGo.<ResponseData<WithdrawalCash>>post(UrlConstant.WORKER_DEPOSIT)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<WithdrawalCash>>() {
                                         @Override
                                         public void onSuccess(ResponseData<WithdrawalCash> response) {
                                             if (response.getCode() == 0) {
                                                 if(response.getData().isIsExist()){
                                                     EventBusUtil.post(MessageConstant.NOTIFY_CARRY_AMONUT);
                                                     WithdrawalCashActivity.this.finish();
                                                 }else {
                                                     startActivity(UpdateAlipayActivity.class);
                                                 }
                                             }
                                             toast(response.getMsg());
                                         }
                                     }
                            );
                }
                break;
            case R.id.all:
                cash1.setText("￥" + cash2.getText().toString());
                break;
        }
    }

}
