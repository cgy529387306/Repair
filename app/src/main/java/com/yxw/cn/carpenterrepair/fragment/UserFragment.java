package com.yxw.cn.carpenterrepair.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.yxw.cn.carpenterrepair.BaseApplication;
import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.WebActivity;
import com.yxw.cn.carpenterrepair.activity.user.PersonInfoActivity;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Asset;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.entity.ResponseData3;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import com.yxw.cn.carpenterrepair.activity.setting.SettingActivity;
import com.yxw.cn.carpenterrepair.activity.user.WalletActivity;

import com.yxw.cn.carpenterrepair.activity.setting.UserFeedBackActivity;

/**
 * 用户中心
 * Created by cgy on 2018/11/25
 */
public class UserFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_carry_amount)
    TextView mTvCarryAmount;
    @BindView(R.id.tv_deposit)
    TextView mTvDeposit;
    @BindView(R.id.tv_settlement_amount)
    TextView mTvSettlementAmount;

    @Override
    protected int getLayout() {
        return R.layout.frg_user;
    }

    @Override
    protected void initView() {
        notifyInfo();
    }

    public void notifyInfo() {
        if (CurrentUser.getInstance().isLogin()){
            LoginInfo loginInfo = CurrentUser.getInstance();
            mTvName.setText(loginInfo.getNickname());
            mTvPhone.setText(AppUtil.getStarPhone(loginInfo.getMobile()));
            AppUtil.showPic(mContext, mIvAvatar, loginInfo.getAvatar());
        }
    }

    @Override
    public void getData() {
        OkGo.<ResponseData<LoginInfo>>post(UrlConstant.GET_WORKER_INFO)
                .execute(new JsonCallback<ResponseData<LoginInfo>>() {
                             @Override
                             public void onSuccess(ResponseData<LoginInfo> response) {
                                 if (response.getCode() == 0) {
                                     CurrentUser.getInstance().login(response.getData());
                                     notifyInfo();
                                 }
                             }
                         }
                );

    }

    @Override
    public void onResume() {
        super.onResume();
        getAssets();
    }

    private void getAssets() {
        OkGo.<ResponseData<Asset>>post(UrlConstant.GET_ASSETS)
                .execute(new JsonCallback<ResponseData<Asset>>() {
                             @Override
                             public void onSuccess(ResponseData<Asset> response) {
                                 mTvCarryAmount.setText(response.getData().getCarryAmount() + "");
                                 mTvDeposit.setText(response.getData().getDeposit() + "");
                                 mTvSettlementAmount.setText(response.getData().getSettlementAmount() + "");
                                 Map<String, String> map = new HashMap<>();
                                 map.put("userId", response.getData().getUserId() + "");
                                 map.put("userName", response.getData().getUsername());
                                 map.put("portrait", response.getData().getAvatar());
//                                 getRongCloudToken(map);
                             }
                         }
                );
    }

    private void getRongCloudToken(Map<String, String> map) {
        Logger.d("getRongCloudToken");
        OkGo.<ResponseData3>post(UrlConstant.RONG_CLOUD_TOKEN)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData3>() {
                             @Override
                             public void onSuccess(ResponseData3 response) {
                                 connect(response.getToken());
                             }
                         }
                );
    }


    @OnClick({R.id.ll_info, R.id.ll_withdrawal, R.id.tv_contact, R.id.tv_help, R.id.tv_feedback,R.id.tv_join,R.id.img_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.ll_info:
                startActivity(PersonInfoActivity.class);
                break;
            case R.id.ll_withdrawal:
                Bundle walletBundle = new Bundle();
                walletBundle.putString("title", "钱包");
                walletBundle.putString("carryAmount",mTvCarryAmount.getText().toString());
                walletBundle.putString("deposit",mTvDeposit.getText().toString());
                walletBundle.putString("settlementAmount",mTvSettlementAmount.getText().toString());
                startActivity(WalletActivity.class, walletBundle);
                break;
            case R.id.tv_contact:
//                startActivity(ConversationActivity.class);
                break;
            case R.id.tv_help:
                Bundle webBundle = new Bundle();
                webBundle.putString("url", "http://jx.bdelay.com/worker/system/help.html");
                webBundle.putString("title", "帮助中心");
                startActivity(WebActivity.class, webBundle);
                break;
            case R.id.tv_feedback:
                startActivity(UserFeedBackActivity.class);
                break;
            case R.id.tv_join:
//                startActivity(UserFeedBackActivity.class);
                break;
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_INFO:
                notifyInfo();
                break;
            case MessageConstant.NOTIFY_CARRY_AMONUT:
                OkGo.<ResponseData<Asset>>post(UrlConstant.GET_ASSETS)
                        .execute(new JsonCallback<ResponseData<Asset>>() {
                                     @Override
                                     public void onSuccess(ResponseData<Asset> response) {
                                         mTvCarryAmount.setText(response.getData().getCarryAmount() + "");
                                         mTvDeposit.setText(response.getData().getDeposit() + "");
                                         mTvSettlementAmount.setText(response.getData().getSettlementAmount() + "");
                                     }
                                 }
                        );
                break;
        }
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {
        Logger.d("connect");
        if (getContext().getApplicationInfo().packageName.equals(BaseApplication.getCurProcessName(getContext().getApplicationContext()))) {
            Logger.d("RongIM connect");

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    Logger.d("onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    Logger.d("onSuccess: " + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.d("onError: " + errorCode.getValue() + " " + errorCode.getMessage());
                }
            });
        }
    }
}
