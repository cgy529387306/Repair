package com.yxw.cn.carpenterrepair.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ui.UpdateNameActivity;
import ui.user.UpdateMobileActivity;
import util.Base64Util;
import util.EventBusUtil;

public class UserPersonInfoActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;

    private Gson gson = new Gson();
    private LoginInfo loginInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_user_person_info;
    }


    @Override
    public void initView() {
        titlebar.setTitle("个人资料");
        notifyInfo();
    }

    public void notifyInfo() {
        if (CurrentUser.getInstance().isLogin()){
            loginInfo = CurrentUser.getInstance();
            mTvName.setText(loginInfo.getNickname());
            mTvPhone.setText(AppUtil.getStarPhone(loginInfo.getMobile()));
            AppUtil.showPic(this, mIvAvatar, loginInfo.getAvatar());
        }
    }

    @OnClick({R.id.ll_avatar, R.id.ll_name, R.id.ll_mobile})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_avatar:
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
            case R.id.ll_name:
                startActivity(UpdateNameActivity.class);
                break;
            case R.id.ll_mobile:
                startActivity(UpdateMobileActivity.class);
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
                                                     AppUtil.showPic(UserPersonInfoActivity.this, mIvAvatar, selectList.get(0).getCompressPath());
                                                     CurrentUser.getInstance().login(loginInfo);
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
        }
    }
}
