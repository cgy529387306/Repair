package com.yxw.cn.carpenterrepair.activity.setting;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.yxw.cn.carpenterrepair.BaseActivity;
import com.yxw.cn.carpenterrepair.R;
import com.yxw.cn.carpenterrepair.contast.UrlConstant;
import com.yxw.cn.carpenterrepair.entity.ResponseData2;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.view.TitleBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户反馈
 */
public class UserFeedBackActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titleBar;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_count)
    TextView tvCount;
//    @BindView(R.id.et_email)
//    EditText etEmail;
//    @BindView(R.id.et_tel)
//    EditText etTel;

    @Override
    protected int getLayoutResId() {
        return R.layout.act_user_feedback;
    }

    @Override
    public void initView() {
        titleBar.setTitle("用户反馈");
        etRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCount.setText(editable.toString().length() + "/3000");
            }
        });
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                if (etRemark.getText().toString().isEmpty()) {
                    toast("填写内容不能为空!");
                } else {
                        Gson gson = new Gson();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("content", etRemark.getText().toString().trim());
                        OkGo.<ResponseData2>post(UrlConstant.USER_FEEDBACK)
                                .upJson(gson.toJson(map))
                                .execute(new JsonCallback<ResponseData2>() {
                                    @Override
                                    public void onSuccess(ResponseData2 response) {
                                        toast(response.getMsg());
                                        UserFeedBackActivity.this.finish();
                                    }
                                });
                }
                break;
        }
    }

}
