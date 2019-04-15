package com.yxw.cn.carpenterrepair.fragment.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.CustomerServiceActivity;
import com.yxw.cn.carpenterrepair.activity.UserPersonInfoActivity;
import com.yxw.cn.carpenterrepair.activity.WebActivity;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ui.SettingActivity;
import ui.user.UserFeedBackActivity;

/**
 * Created by CY on 2018/11/25
 */
public class MyFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView() {
        notifyInfo();
    }

    public void notifyInfo(){
        if (CurrentUser.getInstance().isLogin()){
            LoginInfo loginInfo = CurrentUser.getInstance();
            mTvName.setText(loginInfo.getNickname());
            mTvPhone.setText(AppUtil.getStarPhone(loginInfo.getMobile()));
            AppUtil.showPic(mContext, mIvAvatar, loginInfo.getAvatar());
        }
    }

    @Override
    public void getData() {
        OkGo.<ResponseData<LoginInfo>>get(UrlConstant.GET_USER_INFO)
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

    @OnClick({R.id.iv_set, R.id.rl_info,R.id.rl_feedback, R.id.rl_contact,R.id.rl_help})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_set:
                startActivity(SettingActivity.class);
                break;
            case R.id.rl_info:
                startActivity(UserPersonInfoActivity.class);
                break;
            case R.id.rl_feedback:
                startActivity(UserFeedBackActivity.class);
                break;
            case R.id.rl_contact:
                startActivity(CustomerServiceActivity.class);
                break;
            case R.id.rl_help:
                Bundle webBundle = new Bundle();
                webBundle.putString("url","http://jx.bdelay.com/worker/system/help.html");
                webBundle.putString("title","帮助中心");
                startActivity(WebActivity.class,webBundle);
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
        }
    }
}
