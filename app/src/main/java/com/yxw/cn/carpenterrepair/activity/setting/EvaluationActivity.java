package com.yxw.cn.carpenterrepair.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yxw.cn.carpenterrepair.entity.Evaluation;
import com.yxw.cn.carpenterrepair.entity.OrderUpload;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评价
 */
public class EvaluationActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener{

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_star1)
    ImageView mIvStar1;
    @BindView(R.id.iv_star2)
    ImageView mIvStar2;
    @BindView(R.id.iv_star3)
    ImageView mIvStar3;
    @BindView(R.id.iv_star4)
    ImageView mIvStar4;
    @BindView(R.id.iv_star5)
    ImageView mIvStar5;
    @BindView(R.id.rv_upload)
    RecyclerView mRv;
    private List<OrderUpload> mList;
    private OrderUploadAdapter mAdapter;
    private int orderId;
    private int star;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_evaluation;
    }

    @Override
    public void initView() {
        titlebar.setTitle("评价");
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

    @OnClick({R.id.iv_star1,R.id.iv_star2,R.id.iv_star3,R.id.iv_star4,R.id.iv_star5,R.id.confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.confirm:
                showLoading();
                Gson gson = new Gson();
                List<Evaluation.Picture> pictures=new ArrayList<>();
                for (OrderUpload orderUpload:mList){
                    pictures.add(new Evaluation.Picture(orderUpload.getPath()));
                }
                Evaluation evaluation=new Evaluation(orderId,star,etRemark.getText().toString().trim(),pictures);
                OkGo.<ResponseData<Object>>post(UrlConstant.USER_EVALUATE)
                        .upJson(gson.toJson(evaluation))
                        .execute(new JsonCallback<ResponseData<Object>>() {
                            @Override
                            public void onSuccess(ResponseData<Object> response) {
                                toast(response.getMsg());
                                dismissLoading();
                            }

                            @Override
                            public void onError(Response<ResponseData<Object>> response) {
                                super.onError(response);
                                dismissLoading();
                            }
                        });
                break;
            case R.id.iv_star1:
                star=1;
                mIvStar1.setImageResource(R.drawable.icon_star_red);
                mIvStar2.setImageResource(R.drawable.icon_star);
                mIvStar3.setImageResource(R.drawable.icon_star);
                mIvStar4.setImageResource(R.drawable.icon_star);
                mIvStar5.setImageResource(R.drawable.icon_star);
                break;
            case R.id.iv_star2:
                star=2;
                mIvStar1.setImageResource(R.drawable.icon_star_red);
                mIvStar2.setImageResource(R.drawable.icon_star_red);
                mIvStar3.setImageResource(R.drawable.icon_star);
                mIvStar4.setImageResource(R.drawable.icon_star);
                mIvStar5.setImageResource(R.drawable.icon_star);
                break;
            case R.id.iv_star3:
                star=3;
                mIvStar1.setImageResource(R.drawable.icon_star_red);
                mIvStar2.setImageResource(R.drawable.icon_star_red);
                mIvStar3.setImageResource(R.drawable.icon_star_red);
                mIvStar4.setImageResource(R.drawable.icon_star);
                mIvStar5.setImageResource(R.drawable.icon_star);
                break;
            case R.id.iv_star4:
                star=4;
                mIvStar1.setImageResource(R.drawable.icon_star_red);
                mIvStar2.setImageResource(R.drawable.icon_star_red);
                mIvStar3.setImageResource(R.drawable.icon_star_red);
                mIvStar4.setImageResource(R.drawable.icon_star_red);
                mIvStar5.setImageResource(R.drawable.icon_star);
                break;
            case R.id.iv_star5:
                star=5;
                mIvStar1.setImageResource(R.drawable.icon_star_red);
                mIvStar2.setImageResource(R.drawable.icon_star_red);
                mIvStar3.setImageResource(R.drawable.icon_star_red);
                mIvStar4.setImageResource(R.drawable.icon_star_red);
                mIvStar5.setImageResource(R.drawable.icon_star_red);
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
