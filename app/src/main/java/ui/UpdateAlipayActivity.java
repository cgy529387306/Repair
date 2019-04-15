package ui;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.MessageConstant;
import com.yxw.cn.carpenterrepair.contast.SpConstant;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.CurrentUser;
import com.yxw.cn.carpenterrepair.entity.LoginInfo;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.SpUtil;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import util.EventBusUtil;

/**
 * Created by CY on 2018/11/24
 */
public class UpdateAlipayActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.et_name)
    EditText mEtName;

    private Gson gson = new Gson();
    private LoginInfo loginInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_update_name;
    }

    @Override
    public void initView() {
        titleBar.setTitle("修改支付宝");
        if (CurrentUser.getInstance().isLogin()) {
            loginInfo = CurrentUser.getInstance();
            if (!TextUtils.isEmpty(loginInfo.getAliplayAccount())) {
                mEtName.setText(loginInfo.getAliplayAccount());
                mEtName.setSelection(loginInfo.getAliplayAccount().length());
            }
        } else {
            loginInfo = new LoginInfo();
        }
    }

    @OnClick({R.id.del, R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.del:
                mEtName.setText("");
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(mEtName.getText().toString().trim())) {
                    toast("您还未输入支付宝账号！");
                    return;
                }
                showLoading();
                Map<String, String> map = new HashMap<>();
                map.put("aliplayAccount", mEtName.getText().toString().trim());
                OkGo.<ResponseData<String>>post(UrlConstant.UPDATE_ALIPAY_ACCOUNT)
                        .upJson(gson.toJson(map))
                        .execute(new JsonCallback<ResponseData<String>>() {
                                     @Override
                                     public void onSuccess(ResponseData<String> response) {
                                         dismissLoading();
                                         toast(response.getMsg());
                                         if (response.getCode() == 0) {
                                             loginInfo.setAliplayAccount(mEtName.getText().toString().trim());
                                             CurrentUser.getInstance().login(loginInfo);
                                             EventBusUtil.post(MessageConstant.NOTIFY_INFO);
                                             finish();
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
                break;
        }

    }
}
