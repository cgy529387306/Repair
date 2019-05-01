package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
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
import com.yxw.cn.carpenterrepair.entity.BeGoodAtCategory;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.listerner.OnChooseAddrListener;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.RegionPickerUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.tv_identificate)
    TextView mTvIdentificate;
    @BindView(R.id.tv_resident)
    TextView mTvResident;
    @BindView(R.id.tv_service_provider)
    TextView mTvServiceProvider;
    @BindView(R.id.rv_category)
    RecyclerView mRv;
    @BindView(R.id.ll_good)
    LinearLayout mLlGood;
    @BindView(R.id.img_back)
    ImageView mImgBack;

    private List<BeGoodAtCategory> mList;
    private MyCategoryAdapter mAdapter;
    private LoginInfo loginInfo;

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarColor(R.color.bg_personal).statusBarDarkFont(false).init();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.act_user_person_info;
    }

    @Override
    public void initView() {
        mList = new ArrayList<>();
        mAdapter = new MyCategoryAdapter(mList);
        mRv.setNestedScrollingEnabled(false);
        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.setAdapter(mAdapter);
        notifyInfo();
    }

    public void notifyInfo() {
        String avatarUrl = null;
        if (CurrentUser.getInstance().isLogin()) {
            try {
                loginInfo = CurrentUser.getInstance();
                mTvName.setText(loginInfo.getUsername());
                mTvPhone.setText(loginInfo.getMobile());
                mTvIdCardNo.setText(loginInfo.getIdentityCard());
                mTvResident.setText(loginInfo.getResidentName());
                if (!TextUtils.isEmpty(loginInfo.getServiceDate()) && !TextUtils.isEmpty(loginInfo.getServiceTime())) {
                    StringBuilder date = new StringBuilder();
                    for (String dateInd :
                            loginInfo.getServiceDate().split(",")) {
                        switch (dateInd) {
                            case "1":
                                date.append("周一、");
                                break;
                            case "2":
                                date.append("周二、");
                                break;
                            case "3":
                                date.append("周三、");
                                break;
                            case "4":
                                date.append("周四、");
                                break;
                            case "5":
                                date.append("周五、");
                                break;
                            case "6":
                                date.append("周六、");
                                break;
                            case "7":
                                date.append("周日、");
                                break;
                        }
                    }
                }
                avatarUrl = loginInfo.getAvatar();
                List<BeGoodAtCategory> beGoodAtCategories = loginInfo.getTags();
                if (beGoodAtCategories != null && beGoodAtCategories.size() > 0) {
                    mList.clear();
                    mList.addAll(beGoodAtCategories);
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            loginInfo = new LoginInfo();
        }
        AppUtil.showPic(this, mIvAvatar, avatarUrl);
    }

    @OnTouch(R.id.rv_category)
    public boolean onRvTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mLlGood.performClick();  //模拟父控件的点击
        }
        return false;
    }

    @OnClick({R.id.iv_avatar, R.id.ll_name, R.id.ll_mobile, R.id.ll_good,
            R.id.ll_resident, R.id.ll_identificate, R.id.ll_idCardNo,R.id.img_back,R.id.ll_service_provider})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_idCardNo:// 身份证号码
                break;
            case R.id.ll_identificate: //身份证认证
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
                startActivity(UpdateMobileActivity.class);
                break;
            case R.id.ll_name:
                startActivity(UpdateNameActivity.class);
                break;
            case R.id.ll_good:
                Intent intent = new Intent(this, ChooseCategoryActivity.class);
                intent.putExtra("data", (Serializable) mList);
                startActivity(intent);
                break;
            case R.id.ll_resident:
                RegionPickerUtil.showCityPicker(this, new OnChooseAddrListener() {
                    @Override
                    public void getAddr(int options1, int options2, int options3) {
                        System.out.println(AppUtil.regionTreeList.get(options1).getSub().get(options2).getName());
                        System.out.println(AppUtil.regionTreeList.get(options1).getSub().get(options2).getAgency_id());
                        editTrait(AppUtil.regionTreeList.get(options1).getSub().get(options2).getAgency_id() + "", RegionPickerUtil.getCity(options1, options2));
                    }
                });
                break;
            case R.id.ll_service_provider:
                startActivity(ServiceProviderEmptyActivity.class);
                break;
        }
    }

    private void editTrait(String resident, String city) {
        Map<String, String> map = new HashMap<>();
        map.put("resident", resident);
        map.put("serviceTime", "");
        map.put("serviceDate", "");
        OkGo.<ResponseData>post(UrlConstant.EDIT_TRAIT)
                .upJson(gson.toJson(map))
                .execute(new JsonCallback<ResponseData>() {
                    @Override
                    public void onSuccess(ResponseData response) {
                        toast(response.getMsg());
                        if (response.isSuccess()) {
                            loginInfo.setResident(resident);
                            loginInfo.setResidentName(city);
                            CurrentUser.getInstance().login(loginInfo);
                            notifyInfo();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        showLoading();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("avatar", Base64Util.getBase64ImageStr(selectList.get(0).getCompressPath()));
                        showLoading();
                        OkGo.<ResponseData<String>>post(UrlConstant.CHANGE_AVATAR)
                                .upJson(gson.toJson(map))
                                .execute(new JsonCallback<ResponseData<String>>() {
                                             @Override
                                             public void onSuccess(ResponseData<String> response) {
                                                 dismissLoading();
                                                 toast(response.getMsg());
                                                 if (response.isSuccess()) {
                                                     loginInfo.setAvatar(response.getData());
                                                     AppUtil.showPic(PersonInfoActivity.this, mIvAvatar, selectList.get(0).getCompressPath());
                                                     CurrentUser.getInstance().login(loginInfo);
                                                     EventBusUtil.post(MessageConstant.NOTIFY_INFO);
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
            }
        }
    }

    @Override
    public void onEvent(MessageEvent event) {
        super.onEvent(event);
        switch (event.getId()) {
            case MessageConstant.NOTIFY_INFO:
                notifyInfo();
                break;
            case MessageConstant.MY_CATEGORY:
                List<BeGoodAtCategory> names = (List<BeGoodAtCategory>) event.getData();
                mList.clear();
                mList.addAll(names);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }


}
