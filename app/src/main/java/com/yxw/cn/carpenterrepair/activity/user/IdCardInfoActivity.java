package com.yxw.cn.carpenterrepair.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;
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
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.util.ImageUtils;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
    private static final int REQUEST_CODE_CAMERA = 102;
    @Override
    protected int getLayoutResId() {
        return R.layout.act_id_card_info;
    }

    @Override
    public void initView() {
        titlebar.setTitle("身份证信息");
        titlebar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initOcrCamera();
        if (CurrentUser.getInstance().isLogin()){
            LoginInfo loginInfo = CurrentUser.getInstance();
            if(loginInfo.getIdCardStatus()==2){
                tv_status.setVisibility(View.VISIBLE);
            }else{
                tv_status.setVisibility(View.GONE);
            }
        }
    }

    private void initOcrCamera(){
        CameraNativeHelper.init(this, OCR.getInstance(this).getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        Log.e("CameraNativeHelper:","本地质量控制初始化错误，错误原因： " + msg);
                    }
                });
    }

    @OnClick({R.id.iv_idCard_front, R.id.iv_idCard_back,R.id.iv_idCard_both, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_idCard_front:
                Intent intent = new Intent(IdCardInfoActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        ImageUtils.getSaveFrontFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                        true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                        true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
                break;
            case R.id.iv_idCard_back:
                Intent intent1 = new Intent(IdCardInfoActivity.this, CameraActivity.class);
                intent1.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        ImageUtils.getSaveBackFile(getApplication()).getAbsolutePath());
                intent1.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                        true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent1.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                        true);
                intent1.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent1, REQUEST_CODE_CAMERA);
                break;
            case R.id.iv_idCard_both:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(true)// 是否裁剪 true or false
                        .withAspectRatio(23,15)
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(33);
                break;
            case R.id.confirm:
                AppUtil.disableViewDoubleClick(view);
                if (idCardBack == null || idCardFront == null || icCardBoth == null) {
                    toast("请上传齐全证件图片！");
                }else {
                    showLoading();
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("idCardFront", Base64Util.getBase64ImageStr(idCardFront));
                    map.put("idCardBack", Base64Util.getBase64ImageStr(idCardBack));
                    map.put("idCardHand", Base64Util.getBase64ImageStr(icCardBoth));
                    OkGo.<ResponseData<Object>>post(UrlConstant.UPLOAD_IDCARD)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<Object>>() {
                                @Override
                                public void onSuccess(ResponseData<Object> response) {
                                    dismissLoading();
                                    if (response!=null){
                                        if (response.isSuccess()){
                                            try {
                                                toast("提交成功");
                                                if(CurrentUser.getInstance().isLogin() && TextUtils.isEmpty(CurrentUser.getInstance().getCategory())){
                                                    Intent intent = new Intent(IdCardInfoActivity.this, ChooseCategoryActivity.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("cateList", (Serializable) new ArrayList<>());
                                                    bundle.putBoolean("canBack",false);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }else{
                                                    startActivityFinish(WaitCheckActivity.class);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            toast(response.getMsg());
                                        }
                                    }
                                }

                                @Override
                                public void onError(Response<ResponseData<Object>> response) {
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
                case REQUEST_CODE_CAMERA:
                    if (data != null) {
                        String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                        if (!TextUtils.isEmpty(contentType)) {
                            if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                                idCardFront = ImageUtils.getSaveFrontFile(getApplicationContext()).getAbsolutePath();
                                Glide.with(this).load(idCardFront).into(iv_idCard_front);
//                                recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                            } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                                idCardBack = ImageUtils.getSaveBackFile(getApplicationContext()).getAbsolutePath();
                                Glide.with(this).load(idCardBack).into(iv_idCard_back);
//                                recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                            }
                        }
                    }
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

    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);
        OCR.getInstance(this).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    new MaterialDialog.Builder(IdCardInfoActivity.this).title("提示").content(result.toString())
                            .positiveText("知道了").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    }).show();
                }
            }

            @Override
            public void onError(OCRError error) {
                new MaterialDialog.Builder(IdCardInfoActivity.this).title("提示").content(error.getMessage())
                        .positiveText("知道了").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).show();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
