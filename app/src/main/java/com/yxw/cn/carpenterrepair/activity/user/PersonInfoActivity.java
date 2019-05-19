package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.MyCategoryAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.util.RegionPickerUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人资料
 */
public class PersonInfoActivity extends BaseActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_idCardNo)
    TextView mTvIdCardNo;
    @BindView(R.id.tv_idCardStatus)
    TextView mTvIdCardStatus;
    @BindView(R.id.tv_resident)
    TextView mTvResident;
    @BindView(R.id.tv_service_provider)
    TextView mTvServiceProvider;
    @BindView(R.id.rv_category)
    RecyclerView mRvCate;
    @BindView(R.id.ll_good)
    LinearLayout mLlGood;
    @BindView(R.id.img_back)
    ImageView mImgBack;

    private List<String> mCateList = new ArrayList<String>();
    private MyCategoryAdapter mCateAdapter;
    private LoginInfo loginInfo;

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarDarkFont(false).init();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.act_user_person_info;
    }

    @Override
    public void initView() {
        mCateAdapter = new MyCategoryAdapter(mCateList);
        mRvCate.setNestedScrollingEnabled(false);
        mRvCate.setLayoutManager(new GridLayoutManager(this, 4));
        mRvCate.setAdapter(mCateAdapter);
        notifyInfo();
    }

    public void notifyInfo() {
        if (CurrentUser.getInstance().isLogin()) {
            try {
                loginInfo = CurrentUser.getInstance();
                mTvName.setText(loginInfo.getRealName());
                mTvPhone.setText(loginInfo.getMobile());
                mTvIdCardStatus.setText(AppUtil.getIdCardStatus(loginInfo.getIdCardStatus()));
                mTvServiceProvider.setText(TextUtils.isEmpty(loginInfo.getParentId())?"":"服务商ID"+loginInfo.getParentId());
//                mTvIdCardNo.setText(loginInfo.getIdentityCard());
                mTvResident.setText(loginInfo.getResidentAreaName());
                if (Helper.isNotEmpty(loginInfo.getCategory())){
                    String[] dataArray = loginInfo.getCategory().split(",");
                    if (Helper.isNotEmpty(dataArray)){
                        mCateList = Arrays.asList(dataArray);
                        mCateAdapter.setNewData(mCateList);
                    }
                }
                AppUtil.showPic(this, mIvAvatar, loginInfo.getAvatar());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginInfo = new LoginInfo();
        }
    }

    @OnTouch(R.id.rv_category)
    public boolean onRvTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mLlGood.performClick();  //模拟父控件的点击
        }
        return false;
    }

    @OnClick({R.id.iv_avatar, R.id.ll_name, R.id.ll_mobile, R.id.ll_good,
            R.id.ll_resident, R.id.ll_idCardStatus, R.id.ll_idCardNo,R.id.img_back,R.id.ll_service_provider})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_idCardNo:// 身份证号码

                break;
            case R.id.ll_idCardStatus: //身份证认证
                if (loginInfo!=null && loginInfo.getIdCardStatus()!=3){
                    startActivity(IdCardInfoActivity.class);
                }
                break;
            case R.id.iv_avatar:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(true)// 是否裁剪 true or false
                        .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(11);
                break;
            case R.id.ll_mobile:
//                startActivity(UpdateMobileActivity.class);
                break;
            case R.id.ll_name:
//                startActivity(UpdateNameActivity.class);
                break;
            case R.id.ll_good:
                Intent intent = new Intent(this, ChooseCategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cateList", (Serializable) mCateList);
                bundle.putBoolean("canBack",true);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.ll_resident:
                RegionPickerUtil.showPicker(this, mTvResident, true);
                break;
            case R.id.ll_service_provider:
                if (loginInfo!=null && !TextUtils.isEmpty(loginInfo.getParentId())){
                    startActivity(JoinServiceProviderActivity.class);
                }else{
                    startActivity(ServiceProviderEmptyActivity.class);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    //上传头像
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                       LocalMedia localMedia = selectList.get(0);
                       doUploadAvatar(localMedia);
                    }
                    break;
            }
        }
    }

    private void doUploadAvatar(LocalMedia localMedia){
        if (localMedia==null || localMedia.getCompressPath()==null){
            return;
        }
        showLoading();
        HashMap<String, String> map = new HashMap<>();
        map.put("avatar", Base64Util.getBase64ImageStr(localMedia.getCompressPath()));
        AppUtil.showPic(PersonInfoActivity.this, mIvAvatar, localMedia.getCompressPath());
        OkGo.<ResponseData<Object>>post(UrlConstant.CHANGE_AVATAR)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData<Object>>() {
                             @Override
                             public void onSuccess(ResponseData<Object> response) {
                                 dismissLoading();
                                 toast(response.getMsg());
                                 if (response.isSuccess()) {
                                     EventBusUtil.post(MessageConstant.NOTIFY_GET_INFO);
                                 }
                             }

                             @Override
                             public void onError(Response<ResponseData<Object>> response) {
                                 super.onError(response);
                                 dismissLoading();
                             }
                         }
                );
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_UPDATE_INFO:
                notifyInfo();
                break;
            case MessageConstant.MY_CATEGORY:
                mCateAdapter.notifyDataSetChanged();
                break;
        }
    }


}
