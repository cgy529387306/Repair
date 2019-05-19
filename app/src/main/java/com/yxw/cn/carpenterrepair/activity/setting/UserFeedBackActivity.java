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
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.okgo.JsonCallback;
import com.yxw.cn.carpenterrepair.util.AppUtil;
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
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_tel)
    EditText etTel;

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
                tvCount.setText(etRemark.toString().length()+"/3000");
            }
        });
    }

    @OnClick({R.id.confirm})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                String remark = etRemark.getText().toString();
                String tel = etTel.getText().toString();
                String email = etEmail.getText().toString();
                if (remark.isEmpty()) {
                    toast("请输入反馈内容");
                }else if (tel.isEmpty()){
                    toast("请输入手机号码");
                }else if (email.isEmpty()){
                    toast("请输入邮箱");
                }else if (!AppUtil.isPhone(tel)) {
                    toast("请输入正确的手机号");
                } else if (!AppUtil.isEmail(email)) {
                    toast("请输入正确的邮箱地址");
                }else{
                        Gson gson = new Gson();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("content", remark);
                        map.put("email", email);
                        map.put("mobile", tel);
                        map.put("feedType", 0);
                        OkGo.<ResponseData<Object>>post(UrlConstant.USER_FEEDBACK)
                                .upJson(gson.toJson(map))
                                .execute(new JsonCallback<ResponseData<Object>>() {
                                    @Override
                                    public void onSuccess(ResponseData<Object> response) {
                                        if (response!=null){
                                            if (response.isSuccess()){
                                                toast("反馈成功");
                                                finish();
                                            }else{
                                                toast(response.getMsg());
                                            }
                                        }
                                    }
                                });
                }
                break;
        }
    }

}
