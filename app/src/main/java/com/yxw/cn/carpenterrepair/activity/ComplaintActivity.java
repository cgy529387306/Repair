package com.yxw.cn.carpenterrepair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.adapter.OrderUploadAdapter;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.Complain;
import com.yxw.cn.carpenterrepair.entity.OrderUpload;
import com.yxw.cn.carpenterrepair.entity.ResponseData2;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 投诉
 */
public class ComplaintActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_upload)
    RecyclerView mRv;

    private List<OrderUpload> mList;
    private OrderUploadAdapter mAdapter;
    private int orderId;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_complaint;
    }

    @Override
    public void initView() {
        titlebar.setTitle("投诉");
        Bundle bundle =getIntent().getExtras();
        orderId= bundle.getInt("orderId");
        mList = new ArrayList<>();
        mList.add(new OrderUpload(2));
        mAdapter = new OrderUploadAdapter(mList);
        mAdapter.setOnItemChildClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRv.setLayoutManager(gridLayoutManager);
        mRv.setAdapter(mAdapter);
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCount.setText(editable.toString().length() + "/50");
            }
        });
    }

    @OnClick({R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (etTel.getText().toString().isEmpty()){
                    toast("请输入您的手机号码！");
                }else {
                    showLoading();
                    Gson gson = new Gson();
                    List<Complain.Picture> pictures=new ArrayList<>();
                    for (OrderUpload orderUpload:mList){
                        pictures.add(new Complain.Picture(orderUpload.getPath()));
                    }
                    Complain complain=new Complain(orderId,etTel.getText().toString(),etRemark.getText().toString().trim(),pictures);
                    OkGo.<ResponseData2>post(UrlConstant.USER_COMPLAIN)
                            .upJson(gson.toJson(complain))
                            .execute(new JsonCallback<ResponseData2>() {
                                @Override
                                public void onSuccess(ResponseData2 response) {
                                    toast(response.getMsg());
                                    dismissLoading();
                                }

                                @Override
                                public void onError(Response<ResponseData2> response) {
                                    super.onError(response);
                                    dismissLoading();
                                }
                            });
                }

                break;
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_del:
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.rl_card_1:
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
                        for (LocalMedia localMedia : selectList) {
                            mList.add(mList.size() - 1, new OrderUpload(localMedia.getCompressPath()));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
