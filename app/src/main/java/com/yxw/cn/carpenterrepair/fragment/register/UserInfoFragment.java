package com.yxw.cn.carpenterrepair.fragment.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yxw.cn.carpenterrepair.BaseFragment;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.activity.main.MainActivity;
import com.yxw.cn.carpenterrepair.activity.user.ChooseCategoryActivity;
import com.yxw.cn.carpenterrepair.activity.user.JoinServiceProviderActivity;
import com.yxw.cn.carpenterrepair.activity.user.ServiceProviderEmptyActivity;
import com.yxw.cn.carpenterrepair.adapter.MyCategoryAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.RegionPickerUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 录入用户信息
 */
public class UserInfoFragment extends BaseFragment {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_idCardNo)
    TextView mTvIdCardNo;
    @BindView(R.id.tv_resident)
    TextView mTvResident;
    @BindView(R.id.tv_service_provider)
    TextView mTvServiceProvider;
    @BindView(R.id.rv_category)
    RecyclerView mRvCate;
    @BindView(R.id.ll_good)
    LinearLayout mLlGood;

    private List<String> mCateList = new ArrayList<String>();
    private MyCategoryAdapter mCateAdapter;
    private LoginInfo loginInfo;


    @Override
    protected int getLayout() {
        return R.layout.frg_user_info;
    }

    @Override
    public void initView() {
        mCateAdapter = new MyCategoryAdapter(mCateList);
        mRvCate.setNestedScrollingEnabled(false);
        mRvCate.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        mRvCate.setAdapter(mCateAdapter);
        notifyInfo();
    }

    public void notifyInfo() {
        if (CurrentUser.getInstance().isLogin()) {
            try {
                loginInfo = CurrentUser.getInstance();
                mTvName.setText(loginInfo.getRealName());
                mTvPhone.setText(loginInfo.getMobile());
                mTvServiceProvider.setText(TextUtils.isEmpty(loginInfo.getpName())?"":"服务商"+loginInfo.getpName());
                mTvIdCardNo.setText(loginInfo.getIdCardNo());
                mTvResident.setText(loginInfo.getResidentAreaName());
                if (Helper.isNotEmpty(loginInfo.getCategory())){
                    String[] dataArray = loginInfo.getCategory().split(",");
                    if (Helper.isNotEmpty(dataArray)){
                        mCateList = Arrays.asList(dataArray);
                        mCateAdapter.setNewData(mCateList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginInfo = new LoginInfo();
        }
    }

    @OnClick({ R.id.ll_good, R.id.ll_resident, R.id.ll_service_provider,R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_good:
                Intent intent = new Intent(getActivity(), ChooseCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cateList", (Serializable) mCateList);
                bundle.putBoolean("canBack",true);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_resident:
                RegionPickerUtil.showPicker(getActivity(), mTvResident, true);
                break;
            case R.id.ll_service_provider:
                if (loginInfo!=null && !TextUtils.isEmpty(loginInfo.getParentId())){
                    startActivity(JoinServiceProviderActivity.class);
                }else{
                    startActivity(ServiceProviderEmptyActivity.class);
                }
                break;
            case R.id.btn_confirm:
                AppUtil.disableViewDoubleClick(view);
                if (Helper.isEmpty(CurrentUser.getInstance().getResidentAreaName())){
                    ToastUtil.show("请选择常驻地址");
                    return;
                }
                if (Helper.isEmpty(CurrentUser.getInstance().getCategory())){
                    ToastUtil.show("请选择擅长项目");
                    return;
                }
                EventBusUtil.post(MessageConstant.REGISTER);
                SpUtil.putStr(SpConstant.LOGIN_MOBILE, CurrentUser.getInstance().getMobile());
                startActivity(MainActivity.class);
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_INFO:
                notifyInfo();
                break;
        }
    }

}