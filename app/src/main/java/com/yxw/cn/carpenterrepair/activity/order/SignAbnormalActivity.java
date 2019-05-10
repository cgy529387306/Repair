package com.yxw.cn.carpenterrepair.activity.order;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.OrderAbnormalAdapter;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ReasonBean;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.util.Base64Util;
import com.yxw.cn.carpenterrepair.util.EventBusUtil;
import com.yxw.cn.carpenterrepair.util.Helper;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 签到异常
 */
public class SignAbnormalActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.rv_reason)
    RecyclerView mRvReason;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    private OrderAbnormalAdapter mAdapter;
    private String orderId;
    private String exceptionIds;
    private String path;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_sign_abnormal;
    }

    @Override
    public void initView() {
        titleBar.setTitle("签到异常反馈");
        orderId = getIntent().getStringExtra("orderId");
        mAdapter = new OrderAbnormalAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(this);
        mRvReason.setLayoutManager(new GridLayoutManager(this, 2));
        mRvReason.setAdapter(mAdapter);
    }

    @Override
    public void getData() {
        getSignReasonData();
    }

    private void getSignReasonData(){
        if (AppUtil.signReasonList != null && AppUtil.signReasonList.size() > 0) {
            exceptionIds = AppUtil.signReasonList.get(0).getDictId();
            mAdapter.setNewData(AppUtil.signReasonList);
        } else{
            showLoading();
            HashMap<String,String> map = new HashMap<>();
            map.put("dictKey","SIGN_IN_EXCEPTION");
            OkGo.<ResponseData<List<ReasonBean>>>post(UrlConstant.GET_EXCEPTION_REASON)
                    .upJson(gson.toJson(map))
                    .execute(new JsonCallback<ResponseData<List<ReasonBean>>>() {

                        @Override
                        public void onSuccess(ResponseData<List<ReasonBean>> response) {
                            dismissLoading();
                            if (response!=null){
                                if (response.isSuccess() && response.getData()!=null){
                                    AppUtil.signReasonList = response.getData();
                                    if (AppUtil.signReasonList != null && AppUtil.signReasonList.size() > 0) {
                                        exceptionIds = AppUtil.signReasonList.get(0).getDictId();
                                        mAdapter.setNewData(AppUtil.signReasonList);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<List<ReasonBean>>> response) {
                            super.onError(response);
                            dismissLoading();
                        }
                    });
        }
    }


    @OnClick({R.id.iv_picture,R.id.cancel, R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_picture:
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        .enableCrop(false)// 是否裁剪 true or false
                        .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .forResult(11);
                break;
            case R.id.confirm:
                String desc = etRemark.getText().toString().trim();
                if (Helper.isEmpty(exceptionIds)) {
                    toast("请先选择异常原因");
                } else {
                    showLoading();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("orderId", orderId);
                    map.put("ids", exceptionIds);
                    map.put("shot", path);
                    if (Helper.isNotEmpty(desc)){
                        map.put("fixDesc", desc);
                    }
                    OkGo.<ResponseData<String>>post(UrlConstant.ORDER_EXEPTION_SIGN)
                            .upJson(gson.toJson(map))
                            .execute(new JsonCallback<ResponseData<String>>() {
                                @Override
                                public void onSuccess(ResponseData<String> response) {
                                    dismissLoading();
                                    if (response!=null){
                                        if (response.isSuccess()) {
                                            toast("异常反馈成功");
                                            SignAbnormalActivity.this.finish();
                                            EventBusUtil.post(MessageConstant.NOTIFY_UPDATE_ORDER);
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
            case R.id.cancel:
                this.finish();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        mAdapter.setSelect(position);
        mAdapter.notifyDataSetChanged();
        if (Helper.isNotEmpty(mAdapter.getData()) && mAdapter.getData().size()>position){
            exceptionIds = mAdapter.getData().get(position).getDictId();
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
                        for (LocalMedia localMedia : selectList) {
                            path = Base64Util.getBase64ImageStr(localMedia.getCompressPath());
                            Glide.with(this).load(localMedia.getCompressPath()).into(ivPicture);
                        }
                    }
                    break;
            }
        }
    }
}
