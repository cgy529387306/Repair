package com.yxw.cn.carpenterrepair.activity.user;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.QrBean;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.ScreenShotUtils;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 推广
 */
public class PromoteActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.iv_repair_engineer)
    ImageView mIvEngineer;
    @BindView(R.id.iv_repair_service)
    ImageView mIvService;
    @BindView(R.id.line_android)
    View mLineAndroid;
    @BindView(R.id.line_ios)
    View mLineIOS;
    @BindView(R.id.tv_invite_code)
    TextView mTvInviteCode;
    private int mType = 0;//0:android 1:ios
    private List<QrBean> mDataList = new ArrayList<>();
    @Override
    protected int getLayoutResId() {
        return R.layout.act_promote;
    }

    @Override
    public void initView() {
        titleBar.setTitle("推广码");
        mTvInviteCode.setText("您的邀请码是"+CurrentUser.getInstance().getInvitationCode());
        toggleButton();
        getPromote();
    }

    @OnClick({R.id.ll_android, R.id.ll_ios,R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_android:
                mType = 0;
                toggleButton();
                break;
            case R.id.ll_ios:
                mType = 1;
                toggleButton();
                break;
            case R.id.btn_save:
                boolean isSuccess = ScreenShotUtils.shotScreen(PromoteActivity.this);
                toast(isSuccess?"保存成功":"保存失败");
                break;
        }
    }

    private void toggleButton(){
        mLineAndroid.setVisibility(mType==0?View.VISIBLE:View.INVISIBLE);
        mLineIOS.setVisibility(mType==1?View.VISIBLE:View.INVISIBLE);
        initQRData();
    }

    private void getPromote(){
        showLoading();
        Map<String, String> map = new HashMap<>();
        OkGo.<ResponseData<List<QrBean>>>post(UrlConstant.GET_QR)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<List<QrBean>>>() {
                             @Override
                             public void onSuccess(ResponseData<List<QrBean>> response) {
                                 dismissLoading();
                                 if (response!=null){
                                     if (response.isSuccess()){
                                         mDataList = response.getData();
                                         initQRData();
                                     }else{
                                         toast(response.getMsg());
                                     }
                                 }
                             }

                             @Override
                             public void onError(Response<ResponseData<List<QrBean>>> response) {
                                 super.onError(response);
                                 dismissLoading();
                             }
                         }
                );
    }

    public void initQRData(){
        if (Helper.isEmpty(mDataList)){
            return;
        }
        for (QrBean qrBean:mDataList){
            if (mType==0 && qrBean.getAppType()==1){
                //android
                AppUtil.showPic(PromoteActivity.this, qrBean.getAppSite()==1?mIvEngineer:mIvService, qrBean.getUrl());
            }else if (mType==1 && qrBean.getAppType()==2){
                //ios
                AppUtil.showPic(PromoteActivity.this, qrBean.getAppSite()==1?mIvEngineer:mIvService, qrBean.getUrl());
            }
        }
    }


}
