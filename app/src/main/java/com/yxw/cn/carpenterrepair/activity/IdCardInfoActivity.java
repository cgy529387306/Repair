package com.yxw.cn.carpenterrepair.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.IdCardInfo;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import util.Base64Util;

public class IdCardInfoActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.iv_card1)
    ImageView mIvCard1;
    @BindView(R.id.iv_card2)
    ImageView mIvCard2;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_id)
    EditText mEtId;

    private String idCardFront = null;
    private String idCardBack = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_id_card_info;
    }

    @Override
    public void initView() {
        titlebar.setTitle("身份证信息");
        titlebar.setLeftVisible(false);
        LoginInfo loginInfo = gson.fromJson(SpUtil.getStr(SpConstant.LOGIN_INFO), LoginInfo.class);
        if(loginInfo.getIdCardStatus()==0){
            tv_status.setVisibility(View.GONE);
        }else if(loginInfo.getIdCardStatus()==2){
            tv_status.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.view_add_card_1, R.id.view_add_card_2, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_add_card_1:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(false)// 是否裁剪 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(11);
                break;
            case R.id.view_add_card_2:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(false)// 是否裁剪 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(22);
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(mEtName.getText().toString())) {
                    toast("请输入真实姓名！");
                } else if (TextUtils.isEmpty(mEtId.getText().toString())) {
                    toast("请输入身份证号码！");
                } else if(!isIDCard(mEtId.getText().toString())){
                    toast("身份证格式不合法，请重新输入！");
                } else if (idCardBack == null || idCardFront == null) {
                    toast("请上传齐全证件图片！");
                }else {
                    showLoading();
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("username", mEtName.getText().toString());
                    map.put("idCardNo", mEtId.getText().toString());
                    map.put("idCardFront", Base64Util.getBase64ImageStr(idCardFront));
                    map.put("idCardBack", Base64Util.getBase64ImageStr(idCardBack));
                    OkGo.<ResponseData<IdCardInfo>>post(UrlConstant.UPLOAD_IDCARD)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<IdCardInfo>>() {
                                @Override
                                public void onSuccess(ResponseData<IdCardInfo> response) {
                                    toast(response.getMsg());
                                    dismissLoading();
                                    if (response.getCode() == 0) {
                                        IdCardInfoActivity.this.finish();
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("idCardFront", response.getData().getIdCardFrontPath());
                                        map.put("idCardBack", response.getData().getIdCardBackPath());
                                        try {
                                            LoginInfo loginInfo = gson.fromJson(SpUtil.getStr(SpConstant.LOGIN_INFO), LoginInfo.class);
                                            loginInfo.setIdentityCard(response.getData().getIdCardNo());
                                            loginInfo.setIdentityCardFront(response.getData().getIdCardFrontPath());
                                            loginInfo.setIdentityCardBack(response.getData().getIdCardBackPath());
                                            loginInfo.setIdCardStatus(1);
                                            SpUtil.putStr(SpConstant.LOGIN_INFO, gson.toJson(loginInfo));
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        AppUtil.checkStatus(IdCardInfoActivity.this);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(Response<ResponseData<IdCardInfo>> response) {
                                    super.onError(response);
                                    dismissLoading();
                                }
                            });
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
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.size() > 0) {
                        Glide.with(this).load(selectList.get(0).getPath()).into(mIvCard1);
                        idCardFront = selectList.get(0).getPath();
                        System.out.println("11111111111"+idCardFront);
                    }
                    break;
                case 22:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList2 = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList2.size() > 0) {
                        Glide.with(this).load(selectList2.get(0).getPath()).into(mIvCard2);
                        idCardBack = selectList2.get(0).getPath();
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }
}
