package com.yxw.cn.carpenterrepair.okgo;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;
import com.yxw.cn.carpenterrepair.BaseApplication;
import com.yxw.cn.carpenterrepair.activity.user.LoginActivity;
import com.yxw.cn.carpenterrepair.entity.ResponseData;
import com.yxw.cn.carpenterrepair.util.ActivityManager;
import com.yxw.cn.carpenterrepair.util.ToastUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback
        /*
         * 一般直接 new JsonCallback 会直接用无参构造器，但是无参构造器不能带有Bean类类型，
         * 无参的Bean类类型在泛型T中已传入，所以在这里先判断一下，如果为空，就获取一下。
         */
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            }
        }

        ResponseBody body = response.body();
        if (body == null) return null;
        T data = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (type != null) data = gson.fromJson(jsonReader, type);
        if (clazz != null) data = gson.fromJson(jsonReader, clazz);
        return data;
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        ToastUtil.show("网络异常");
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {
        if (response.body() instanceof ResponseData) {
            ResponseData responseData = (ResponseData) response.body();
            //token 问题
            if (responseData.getStatus()==401) {
                ToastUtil.show("Token 失效，请重新登录");
                Intent intent = new Intent(BaseApplication.getInstance(), LoginActivity.class);
                // Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getInstance().startActivity(intent);
                ActivityManager.getInstance().closeAllActivityExceptOne(LoginActivity.class.getName());
            } else {
                onSuccess(response.body());
            }
        } else {
            onSuccess(response.body());
        }
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    public abstract void onSuccess(T response);
}
