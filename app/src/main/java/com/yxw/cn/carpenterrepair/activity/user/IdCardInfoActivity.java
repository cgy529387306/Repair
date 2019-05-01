package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
import android.view.View;
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
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 身份证信息
 */
public class IdCardInfoActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.iv_idCard_front)
    ImageView iv_idCard_front;
    @BindView(R.id.iv_idCard_back)
    ImageView iv_idCard_back;
    @BindView(R.id.iv_idCard_both)
    ImageView iv_idCard_both;

    private String idCardFront = null;
    private String idCardBack = null;
    private String icCardBoth = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_id_card_info;
    }

    @Override
    public void initView() {
        titlebar.setTitle("身份证信息");
        if (CurrentUser.getInstance().isLogin()){
            LoginInfo loginInfo = CurrentUser.getInstance();
            if(loginInfo.getIdCardStatus()==2){
                tv_status.setVisibility(View.VISIBLE);
            }else{
                tv_status.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.iv_idCard_front, R.id.iv_idCard_back,R.id.iv_idCard_both, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_idCard_front:
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
            case R.id.iv_idCard_back:
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
            case R.id.iv_idCard_both:
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
                        .forResult(33);
                break;
            case R.id.confirm:
                if (idCardBack == null || idCardFront == null || icCardBoth == null) {
                    toast("请上传齐全证件图片！");
                }else {
                    showLoading();
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("idCardFront", Base64Util.getBase64ImageStr(idCardFront));
                    map.put("idCardBack", Base64Util.getBase64ImageStr(idCardBack));
                    map.put("idCardHand", Base64Util.getBase64ImageStr(icCardBoth));
                    OkGo.<ResponseData<String>>post(UrlConstant.UPLOAD_IDCARD)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                @Override
                                public void onSuccess(ResponseData<String> response) {
                                    dismissLoading();
                                    if (response!=null){
                                        if (response.isSuccess()){
                                            try {
                                                toast("提交成功");
                                                startActivity(WaitCheckActivity.class);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            toast(response.getMsg());
                                        }
                                    }
                                }

                                @Override
                                public void onError(Response<ResponseData<String>> response) {
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
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    if (selectList.size() > 0) {
                        idCardFront = selectList.get(0).getPath();
                        Glide.with(this).load(idCardFront).into(iv_idCard_front);
                    }
                    break;
                case 22:
                    List<LocalMedia> selectList2 = PictureSelector.obtainMultipleResult(data);
                    if (selectList2.size() > 0) {
                        idCardBack = selectList2.get(0).getPath();
                        Glide.with(this).load(idCardBack).into(iv_idCard_back);
                    }
                    break;
                case 33:
                    List<LocalMedia> selectList3 = PictureSelector.obtainMultipleResult(data);
                    if (selectList3.size() > 0) {
                        icCardBoth = selectList3.get(0).getPath();
                        Glide.with(this).load(icCardBoth).into(iv_idCard_both);
                    }
                    break;
            }
        }
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
