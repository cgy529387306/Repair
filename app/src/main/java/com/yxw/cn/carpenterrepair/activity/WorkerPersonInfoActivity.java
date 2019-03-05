package com.yxw.cn.carpenterrepair.activity;

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

import com.bumptech.glide.Glide;
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
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.BeGoodAtCategory;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import de.hdodenhof.circleimageview.CircleImageView;
import listerner.OnChooseAddrListener;
import ui.LookImageActivity;
import ui.MyCategoryActivity;
import ui.ServiceTimeActivity;
import ui.UpdateAlipayActivity;
import ui.UpdateNameActivity;
import ui.user.UpdateMobileActivity;
import util.Base64Util;
import util.EventBusUtil;
import util.RegionPickerUtil;

public class WorkerPersonInfoActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_alipay)
    TextView mTvAlipay;
    @BindView(R.id.rv_category)
    RecyclerView mRv;
    @BindView(R.id.ll_good)
    LinearLayout mLlGood;
    @BindView(R.id.tv_idCardNo)
    TextView mTvIdCardNo;
    @BindView(R.id.iv_idcard1)
    ImageView ivIdcard1;
    @BindView(R.id.iv_idcard2)
    ImageView ivIdcard2;
    @BindView(R.id.tv_resident)
    TextView mTvResident;
    @BindView(R.id.tv_time)
    TextView mTvTime;

    private List<BeGoodAtCategory> mList;
    private MyCategoryAdapter mAdapter;
    private LoginInfo loginInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_worker_person_info;
    }

    @Override
    public void initView() {
        titlebar.setTitle("个人资料");
        mList = new ArrayList<>();
        mAdapter = new MyCategoryAdapter(mList);
        mRv.setNestedScrollingEnabled(false);
        mRv.setLayoutManager(new GridLayoutManager(this, 4));
        mRv.setAdapter(mAdapter);
        notifyInfo();
    }

    public void notifyInfo() {
        String avatarUrl = null;
        if (!TextUtils.isEmpty(SpUtil.getStr(SpConstant.LOGIN_INFO))) {
            try {
                loginInfo = gson.fromJson(SpUtil.getStr(SpConstant.LOGIN_INFO), LoginInfo.class);
                mTvName.setText(loginInfo.getUserName());
                mTvPhone.setText(loginInfo.getMobile());
                mTvAlipay.setText(loginInfo.getAliplayAccount());
                mTvIdCardNo.setText(loginInfo.getIdentityCard());
                mTvResident.setText(loginInfo.getResidentName());
                if (!TextUtils.isEmpty(loginInfo.getServiceDate()) && !TextUtils.isEmpty(loginInfo.getServiceTime())) {
                    String date = "";
                    for (String dateInd :
                            loginInfo.getServiceDate().split(",")) {
                        switch (dateInd) {
                            case "1":
                                date += "周一、";
                                break;
                            case "2":
                                date += "周二、";
                                break;
                            case "3":
                                date += "周三、";
                                break;
                            case "4":
                                date += "周四、";
                                break;
                            case "5":
                                date += "周五、";
                                break;
                            case "6":
                                date += "周六、";
                                break;
                            case "7":
                                date += "周日、";
                                break;
                        }
                    }
                    if (!TextUtils.isEmpty(date)) {
                        date = date.substring(0, date.length() - 1);
                    }
                    mTvTime.setText(date + " " + loginInfo.getServiceTime());
                }
                if (ivIdcard1 != null && ivIdcard2 != null) {
                    Glide.with(this).load(loginInfo.getIdentityCardFront()).into(ivIdcard1);
                    Glide.with(this).load(loginInfo.getIdentityCardBack()).into(ivIdcard2);
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

    @OnClick({R.id.rl_photo, R.id.ll_name, R.id.ll_mobile, R.id.ll_time, R.id.ll_good,
            R.id.ll_resident, R.id.ll_alipay, R.id.iv_idcard1, R.id.iv_idcard2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_idcard1:
                Bundle webBundle = new Bundle();
                webBundle.putString("path",loginInfo.getIdentityCardFront());
                startActivity(LookImageActivity.class, webBundle);
                break;
            case R.id.iv_idcard2:
                Bundle bundle = new Bundle();
                bundle.putString("path",loginInfo.getIdentityCardBack());
                startActivity(LookImageActivity.class, bundle);
                break;
            case R.id.rl_photo:
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
//                startActivity(IdCardInfoActivity.class);
                break;
            case R.id.ll_name:
//                startActivity(UpdateNameActivity.class);
                break;
            case R.id.ll_alipay:
                startActivity(UpdateAlipayActivity.class);
                break;
            case R.id.ll_time:
                startActivity(ServiceTimeActivity.class);
                break;
            case R.id.ll_good:
                Intent intent = new Intent(this, MyCategoryActivity.class);
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
                        if (response.getCode() == 0) {
                            loginInfo.setResident(resident);
                            loginInfo.setResidentName(city);
                            SpUtil.putStr(SpConstant.LOGIN_INFO, gson.toJson(loginInfo));
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
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
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
                                                 if (response.getCode() == 0) {
                                                     loginInfo.setAvatar(response.getData());
                                                     AppUtil.showPic(WorkerPersonInfoActivity.this, mIvAvatar, selectList.get(0).getCompressPath());
                                                     SpUtil.putStr(SpConstant.LOGIN_INFO, gson.toJson(loginInfo));
                                                     EventBusUtil.post(MessageConstant.NOTIFY_INFO);
                                                 }
                                             }

                                             @Override
                                             public void onError(Response<ResponseData<String>> response) {
                                                 super.onError(response);
                                                 toastNetError();
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
