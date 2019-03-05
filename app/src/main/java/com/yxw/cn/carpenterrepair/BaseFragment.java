package com.yxw.cn.carpenterrepair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yxw.cn.carpenterrepair.entity.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import util.EventBusUtil;

public abstract class BaseFragment extends Fragment {

    public Context mContext;
    public Gson gson = new Gson();
    private Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(getLayout(), container, false);
        mUnBinder = ButterKnife.bind(this, view);
        EventBusUtil.register(this);
        initData();
        initView();
        getData();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle object) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(object);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Object object) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra("data", (Serializable) object);
        startActivity(intent);
    }

    protected abstract int getLayout();

    protected abstract void initView();

    public void initData() {
    }

    public void getData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUnBinder.unbind();
        EventBusUtil.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        onEvent(event);
    }

    public void onEvent(MessageEvent event) {
    }

    public void toast(int msgId) {
        toast(getResources().getString(msgId));
    }

    public void toast(final String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void toastNetError() {
        Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
    }

}
